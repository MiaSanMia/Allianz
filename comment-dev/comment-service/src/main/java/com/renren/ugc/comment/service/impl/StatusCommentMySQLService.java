package com.renren.ugc.comment.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.IdSequenceDAO;
import com.renren.ugc.comment.dao.StatusCommentDAO;
import com.renren.ugc.comment.dao.VoiceCommentDAO;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.service.CommentStorageService;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.util.AdminManagerUtil;
import com.renren.ugc.comment.util.CommentBusIDentifier;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.util.UrlUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

/**
 * @author wangxx
 * 
 * 2014-10-21，完成所有数据迁移，这个类的使命也完成了
 */
@Deprecated
@Service("comment.service.mysql.status")
public class StatusCommentMySQLService implements CommentStorageService, InitializingBean {

    @Autowired
    private VoiceCommentDAO voiceCommentDAO;

    @Autowired
    private StatusCommentDAO statusCommentDAO;

    @Autowired
    private IdSequenceDAO idSequenceDAO;

    private static StatusCommentMySQLService instance;

    private Logger logger = Logger.getLogger(this.getClass());
    
    /**	从DB中一次获取数据的数量	**/
    private static final int ONCE_GET_LIMIT = 100;

    public static StatusCommentMySQLService getInstance() {
        return instance;
    }

    @Override
    public void create(CommentType type, Comment comment, String metadata) {

        if (logger.isDebugEnabled()) {
            logger.debug("insert a new comment " + comment.getId() + " to comment table");
        }

        if (comment.getEntry() == null) {
            logger.warn("the comment's corresponding entry is not specified, ignored comment creation");
            throw new UGCCommentException("the comment's entry is not specified");
        }

        long entryId = comment.getEntry().getId();

        if (entryId <= 0) {
            logger.warn("the entryId " + entryId + " is invalid");
        }

        int entryOwnerId = comment.getEntry().getOwnerId();
        if (entryId <= 0) {
            logger.warn("the entryOnwerId " + entryOwnerId + " is invalid");
        }
        String content = comment.getContent();
        Flag flag = comment.getFlag();

        //        commentDAO.insert(comment.getId(), type.getValue(), comment.getAuthorId(), entryId,
        //                entryOwnerId, content, new Date(comment.getCreatedTime()), comment.getToUserId(),
        //                comment.getToCommentId(), comment.getWhipserToId(), flag.getValue(), metadata);

        //这里需要对id和state进行转换
        int state = 0;
        comment.setId(idSequenceDAO.nextId(IdSequenceDAO.STATUS_COMMENT_SEQ));
        if (flag != null && flag.isUseVoice()) {
            state = CommentOldUgcStateUtil.setStatusVoice(state);
        }
        //设置头像和作者名字,好dt >_<
        final User ownerUser = WUserAdapter.getInstance().get(comment.getAuthorId());

        statusCommentDAO.insert(comment.getId(), comment.getAuthorId(), entryId, content,
                entryOwnerId, new Date(comment.getCreatedTime()), state, ownerUser.getHeadUrl(),
                ownerUser.getName());
        //要存进语音评论DB中的id，用状态评论的id做个映射
        long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
        // insert voiceCommentDAO
        if (flag != null && flag.isUseVoice()) {
            CommentVoiceInfo voiceInfo = comment.getVoiceInfo();
            voiceCommentDAO.insert(id, comment.getType(), comment.getId(), entryId, entryOwnerId,
                    voiceInfo.getVoiceUrl(), voiceInfo.getVoiceLength(), voiceInfo.getVoiceRate(),
                    voiceInfo.getVoiceSize(), new Date(comment.getCreatedTime()));
        }
    }

    @Override
    public List<Comment> getListByEntry(CommentType type, long entryId, int entryOwnerId,
            long borderCommentId, int limit, QueryOrder order) {

        if (logger.isDebugEnabled()) {
            logger.debug("get comment list by entry " + entryId);
        }

        List<Comment> comments = null;

        if (borderCommentId == 0) {
            comments = statusCommentDAO.getCommentListByEntry(entryId, entryOwnerId,
                    getQueryOrderString(order), limit);
        } else {
            if (order == QueryOrder.ASC) {
                comments = statusCommentDAO.getCommentListByEntryASC(entryId, entryOwnerId,
                        borderCommentId, limit);
            } else {
                comments = statusCommentDAO.getCommentListByEntryDESC(entryId, entryOwnerId,
                        borderCommentId, limit);
            }
        }

        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }

        setVoiceToComments(comments,entryId,entryOwnerId);

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size() + " comment(s) from DB");
        }

        return comments;
    }

    private void fillVoiceComments(List<Long> voiceCommentIds, long entryId, int entryOwnerId,
            List<Comment> comments) {
        // add voice comment
        if (voiceCommentIds != null && !voiceCommentIds.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("get voice comments: " + voiceCommentIds);
            }
            Map<Long, CommentVoiceInfo> voiceInfos = voiceCommentDAO.getVoiceInfos(voiceCommentIds,
                    entryId, entryOwnerId);

            if (voiceInfos == null || voiceInfos.isEmpty()) {
                logger.warn("can't read voice comment from DB");
                return;
            }

            if (voiceInfos.size() != voiceInfos.size()) {
                logger.warn(String.format(
                        "not all the voice comments are read from DB, expected: %d, actual: %d",
                        voiceCommentIds.size(), voiceInfos.size()));
            }

            CommentVoiceInfo voiceInfo = null;
            for (Comment comment : comments) {
                Flag flag = comment.getFlag();
                //要存进语音评论DB中的id，用状态评论的id做个映射
                long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
                if (flag.isUseVoice() && (voiceInfo = voiceInfos.get(id)) != null) {

                    //check url is full url
                    if (!voiceInfo.getVoiceUrl().startsWith("http://")) {
                        voiceInfo.setVoiceUrl(UrlUtil.getFullUrl(voiceInfo.getVoiceUrl()));
                    }
                    comment.setVoiceInfo(voiceInfo);
                }
            }

        }
    }

    @Override
    public List<Comment> getListByEntry(CommentType type, long entryId, int entryOwnerId,
            int offset, int limit, QueryOrder order) {
        if (logger.isDebugEnabled()) {
            logger.debug("get comment list by entry " + entryId + " with offset");
        }

        List<Comment> comments = null;

        //        comments = commentDAO.getCommentListByEntryWithOffset(type.getValue(), entryId,
        //                entryOwnerId, offset, getQueryOrderString(order), limit);
        comments = statusCommentDAO.getCommentListByEntryWithOffset(entryId, entryOwnerId, offset,
                getQueryOrderString(order), limit);

        if (comments == null) {
            comments = Collections.emptyList();
        }
        
        setVoiceToComments(comments,entryId,entryOwnerId);

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size() + " comment(s) from DB");
        }

        return comments;
    }

    @Override
    public Comment getOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the oldest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        Comment comment = statusCommentDAO.getOldestCommentOfEntry(entryId, entryOwnerId);
        if (comment == null) {
            return comment;
        }
        
        Flag flag = comment.getFlag();
        List<Long> voiceCommentIds = new ArrayList<Long>();
        
        if (flag.isUseVoice()) {
            //要存进语音评论DB中的id，用状态评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
            voiceCommentIds.add(id);
        }

        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);

        return comment;
    }

    @Override
    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the latest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getLatestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        Comment comment = statusCommentDAO.getLatestCommentOfEntry(entryId, entryOwnerId);

        if (comment == null) {
            return comment;
        }
        
        Flag flag = comment.getFlag();
        List<Long> voiceCommentIds = new ArrayList<Long>();
        
        if (flag.isUseVoice()) {
            //要存进语音评论DB中的id，用状态评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
            voiceCommentIds.add(id);
        }

        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);

        return comment;
    }

    @Override
    public long getCommentCountByEntry(CommentType type, long entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entry " + entryId);
        }

        //return commentDAO.countByEntry(type.getValue(), entryId, entryOwnerId);
        return statusCommentDAO.countByEntry(entryId, entryOwnerId);
    }

    @Override
    public boolean updateContent(CommentType type, long entryId, int entryOwnerId, long commentId,
            String newContent) {
        return false;
    }

    @Override
    public Comment remove(int actorId, long entryId, int entryOwnerId, long commentId,
            CommentType commentType, CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId + " from comment table to comment_del table");
        }

        // Comment comment = commentDAO.get(commentId, entryId, entryOwnerId);
        Comment comment = statusCommentDAO.get(commentId, entryOwnerId);
        if (comment == null) {
            logger.warn("can't remove the comment by id " + commentId
                    + " because it doesn't exists");

            return null;
        }

        //check rights
        boolean canDeleteComment = (actorId == comment.getAuthorId()) || (actorId == entryOwnerId)
                || (AdminManagerUtil.getInstance().hasRight(commentType, actorId, strategy,0,0));
        if (!canDeleteComment) {
            throw new UGCCommentException(CommentError.PERMISSION_DENY_MSG);
        }

        //        int count = commentDAO.insertToDeletedCommentTable(commentId, comment.getType(), comment
        //                .getAuthorId(), entryId, entryOwnerId, comment.getContent(),
        //                new Date(comment.getCreatedTime()), comment.getToUserId(),
        //                comment.getToCommentId(), comment.getWhipserToId(), comment.getFlag().getValue(),
        //                comment.getMetadata().encode(), actorId, new Date(System.currentTimeMillis()));
        //设置头像和作者名字,好dt >_<
        final User ownerUser = WUserAdapter.getInstance().get(comment.getAuthorId());

        int count = statusCommentDAO.insertToDeletedCommentTable(commentId, comment.getAuthorId(),
                entryId, comment.getContent(), entryOwnerId, new Date(comment.getCreatedTime()),
                comment.getFlag().getValue(), actorId, new Date(System.currentTimeMillis()),
                ownerUser.getHeadUrl(), ownerUser.getName());
        if (count == 0) {
            logger.error("can't move the comment with id " + commentId
                    + " to the comment_del table, remove failed");
            return null;
        }

        //count = commentDAO.purgeFromCommentTable(commentId, entryId, entryOwnerId);
        count = statusCommentDAO.purgeFromCommentTable(commentId, entryOwnerId);
        if (count == 0) {
            logger.error("can't purge the comment with id " + commentId + " from DB, remove failed");
            return null;
        }

        return comment;
    }

    @Override
    public boolean recover(long entryId, int entryOwnerId, long commentId) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId + " from comment_del table to comment table");
        }

        // Comment comment = commentDAO.getFromDeletedCommentTable(commentId, entryId, entryOwnerId);
        Comment comment = statusCommentDAO.getFromDeletedCommentTable(commentId, entryId,
                entryOwnerId);
        if (comment == null) {
            logger.warn("can't recover the comment by id " + commentId
                    + " because it doesn't exists in the delete table");

            return false;
        }

        //        int count = commentDAO.insert(commentId, comment.getType(), comment.getAuthorId(), entryId,
        //                entryOwnerId, comment.getContent(), new Date(comment.getCreatedTime()), comment
        //                        .getToUserId(), comment.getToCommentId(), comment.getWhipserToId(), comment
        //                        .getFlag().getValue(), comment.getMetadata().toString());
        //设置头像和作者名字,好dt >_<
        final User ownerUser = WUserAdapter.getInstance().get(comment.getAuthorId());

        int count = statusCommentDAO.insert(commentId, comment.getAuthorId(), entryId,
                comment.getContent(), entryOwnerId, new Date(comment.getCreatedTime()),
                comment.getFlag().getValue(), ownerUser.getHeadUrl(), ownerUser.getName());
        if (count == 0) {
            logger.error("can't insert the comment with id " + commentId
                    + " to the comment table, recover failed");
            return false;
        }

        //count = commentDAO.purgeFromDeletedCommentTable(commentId, entryId, entryOwnerId);
        count = statusCommentDAO.purgeFromDeletedCommentTable(commentId, entryId, entryOwnerId);
        if (count == 0) {
            logger.warn("can't purge the comment with id " + commentId + " from DB");
            return false;
        }
        return true;
    }

    @Override
    public Comment get(long entryId, int entryOwnerId, long commentId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get comment " + commentId);
        }

        Comment comment = null;
        comment = statusCommentDAO.get(commentId, entryOwnerId);

        if (comment == null) {
            return null;
        }

        List<Long> extCommentIds = new ArrayList<Long>();
        List<Long> voiceCommentIds = new ArrayList<Long>();

        Flag flag = comment.getFlag();
        if (flag.isUseExtent()) {
            extCommentIds.add(comment.getId());
        }

        if (flag.isUseVoice()) {
        	//要存进语音评论DB中的id，用状态评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
            voiceCommentIds.add(id);
        }

        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);

        return comment;
    }

    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment) {
        return 0;
    }

    /**
     * convert query order to a query string "ASC" or "DESC". default is
     * "ASC"
     */
    private static final String getQueryOrderString(QueryOrder order) {
        if (order == null || !order.isDesc()) {
            return "ASC";
        }

        return "DESC";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StatusCommentMySQLService.instance = this;
    }

    @Override
    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type, List<Long> entryIds,
            int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entryOwnerId "
                    + entryOwnerId);
        }

        return statusCommentDAO.getCountBatch(entryIds, entryOwnerId);

    }

    @Override
    public boolean removeAll(int actorId, long entryId, int entryOwnerId, CommentType commentType, CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment  from comment table to comment_del table");
        }

        //check rights
        boolean canDeleteComment = (actorId == entryOwnerId)
                || (AdminManagerUtil.getInstance().hasRight(commentType, actorId, strategy,0,0));
        if (!canDeleteComment) {
            throw new UGCCommentException(CommentError.PERMISSION_DENY_MSG);
        }

        //1.get all comments
        long count = this.getCommentCountByEntry(commentType, entryId, entryOwnerId);

        List<Comment> comments = new ArrayList<Comment>();
        //get 50 once
        int limit = 50;
        int begin = 0;
        while (begin < count) {
            List<Comment> tempComments = this.getListByEntry(commentType, entryId, entryOwnerId,
                    begin, limit, QueryOrder.ASC);

            if (tempComments == null || tempComments.size() == 0) {
                break;
            }
            comments.addAll(tempComments);
            begin += limit;
        }

        //2.insert into table del
        statusCommentDAO.insertEntryCommentDelList(entryOwnerId, entryId, actorId);

        statusCommentDAO.purgeFromCommentTable(entryId, entryOwnerId);

        return true;
    }

    @Override
    public List<Comment> getFriendsCommentListByEntry(CommentType type, String entryId,
            int entryOwnerId, boolean includeGlobalComments, int offset, int limit,
            QueryOrder order, List<Integer> userIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId,
            boolean includeGlobalComments, CommentType type) {
    	long commentId = 0L;
    	long statusId = entryId;
    	List<Comment> commentList = new ArrayList<Comment>();
    	List<Comment> onceGetList = statusCommentDAO.getAuthorListByEntry(entryOwnerId, statusId, commentId, ONCE_GET_LIMIT);
    	if(CollectionUtils.isEmpty(onceGetList)) {
    		return Collections.emptyList();
    	}
    	commentList.addAll(onceGetList);
    	int size = onceGetList.size();
    	commentId = onceGetList.get(size - 1).getId();
    	while(onceGetList != null && onceGetList.size() >= ONCE_GET_LIMIT) {
    		onceGetList = statusCommentDAO.getAuthorListByEntry(entryOwnerId, statusId, commentId, ONCE_GET_LIMIT);
    		if(CollectionUtils.isNotEmpty(onceGetList)) {
    			commentList.addAll(onceGetList);
    			size = onceGetList.size();
    			commentId = onceGetList.get(size - 1).getId();
        	}
    	}
    	
    	List<Integer> result = new ArrayList<Integer>();
    	for(Comment c : commentList) {
    		result.add(c.getAuthorId());
    	}
        return result;
    }

    @Override
    public Map<Long, Comment> getMulti(long entryId, int entryOwnerId, List<Long> commentIds) {
        // This feature is only necessary by share
        return null;
    }
    
    /**
     * @param comments
     * @param entryId
     * @param entryOwnerId
     * 
     *  把voice信息set到comment里去
     */
    private void setVoiceToComments(List<Comment> comments,long entryId,int entryOwnerId){
        
        if(comments == null || comments.size() == 0){
            return;
        }
        
        List<Long> voiceCommentIds = new ArrayList<Long>();

        // extract special comments
        for (Comment comment : comments) {
            Flag flag = comment.getFlag();

            if (flag.isUseVoice()) {
                //要存进语音评论DB中的id，用状态评论的id做个映射
                long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, comment.getId());
                voiceCommentIds.add(id);
            }
        }

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);
    }

    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n oldest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
       List<Comment> comments = statusCommentDAO.getNOldestCommentOfEntry(entryId, entryOwnerId,limit);
       
       if (comments == null || comments.isEmpty()) {
           if (logger.isDebugEnabled()) {
               logger.debug("nothing is found from DB, return empty list");
           }
           return Collections.emptyList();
       }

        return comments;
    }

    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n lateest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
       List<Comment> comments = statusCommentDAO.getNLatestCommentOfEntry(entryId, entryOwnerId,limit);
       
       if (comments == null || comments.isEmpty()) {
           if (logger.isDebugEnabled()) {
               logger.debug("nothing is found from DB, return empty list");
           }
           return Collections.emptyList();
       }

        return comments;
    }
    
    /**
     * @param comments
     * @param entryId
     * @param entryOwnerId
     * 
     *  把comment的voice信息set到comment里去
     */
    private void setExtAndVoiceToComments(List<Comment> comments,long entryId,int entryOwnerId){
        
        if(comments == null || comments.size() == 0){
            return;
        }

        List<Long> voiceCommentIds = new ArrayList<Long>();

        // extract special comments
        for (Comment comment : comments) {
            Flag flag = comment.getFlag();

            if (flag.isUseVoice()) {
                voiceCommentIds.add(comment.getId());
            }
        }

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);
    }

	@Override
	public long getCountByAuthorId(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, long borderCommentId, int limit,
			QueryOrder order, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, int offset, int limit,
			QueryOrder order, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getOldestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getLatestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCommentCountByEntry(CommentType type, long entryId,
			int entryOwnerId, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdWithOffset(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			int offset, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdWithBorderId(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			long borderId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getEntryId(CommentType type, int entryOwnerId, long commentId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
