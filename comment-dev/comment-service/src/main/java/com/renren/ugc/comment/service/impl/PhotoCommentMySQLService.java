package com.renren.ugc.comment.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.IdSequenceDAO;
import com.renren.ugc.comment.dao.PhotoCommentDAO;
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
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.util.UrlUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

@Service("comment.service.mysql.photo")
public class PhotoCommentMySQLService implements CommentStorageService, InitializingBean {

    @Autowired
    private VoiceCommentDAO voiceCommentDAO;

    @Autowired
    private PhotoCommentDAO photoCommentDAO;

    @Autowired
    private IdSequenceDAO idSequenceDAO;

    private static PhotoCommentMySQLService instance;

    private Logger logger = Logger.getLogger(this.getClass());

    public static PhotoCommentMySQLService getInstance() {
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
        comment.setId(idSequenceDAO.nextId(IdSequenceDAO.PHOTO_COMMENT_SEQ));
        if (flag != null && flag.isUseVoice()) {
            state = CommentOldUgcStateUtil.setPhotoVoice(state);
        }
        if (flag != null && flag.isUsePhotoTag()) {
            state = CommentOldUgcStateUtil.setPhotoTag(state);
        }
        //设置头像和作者名字,好dt >_<
        final User ownerUser = WUserAdapter.getInstance().get(comment.getAuthorId());
        //悄悄话设置 ,好dt
        if(comment.getWhipserToId() > 0){
            content = content + CommentCenterConsts.WHISPER_MARK;
            if(comment.getWhipserToId() != entryOwnerId){
                content = content + CommentCenterConsts.WHISPER_TOID_MARK + comment.getWhipserToId();
            }
        }

        photoCommentDAO.insert(comment.getId(), comment.getAuthorId(), entryId, content,
                entryOwnerId, new Date(comment.getCreatedTime()), state, ownerUser.getHeadUrl(),
                ownerUser.getName());
        //要存进语音评论DB中的id，用photo评论的id做个映射
        long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
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
            comments = photoCommentDAO.getCommentListByEntry(entryId, entryOwnerId,
                    getQueryOrderString(order), limit);
        } else {
            if (order == QueryOrder.ASC) {
                comments = photoCommentDAO.getCommentListByEntryASC(entryId, entryOwnerId,
                        borderCommentId, limit);
            } else {
                comments = photoCommentDAO.getCommentListByEntryDESC(entryId, entryOwnerId,
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
                //要存进语音评论DB中的id，用photo评论的id做个映射
                long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
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
        comments = photoCommentDAO.getCommentListByEntryWithOffset(entryId, entryOwnerId, offset,
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
        Comment comment = photoCommentDAO.getOldestCommentOfEntry(entryId, entryOwnerId);
        if (comment == null) {
            return comment;
        }
        
        Flag flag = comment.getFlag();
        List<Long> voiceCommentIds = new ArrayList<Long>();
        
        if (flag.isUseVoice()) {
            //要存进语音评论DB中的id，用photo评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
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
        Comment comment = photoCommentDAO.getLatestCommentOfEntry(entryId, entryOwnerId);

        if (comment == null) {
            return comment;
        }
        
        Flag flag = comment.getFlag();
        List<Long> voiceCommentIds = new ArrayList<Long>();
        
        if (flag.isUseVoice()) {
            //要存进语音评论DB中的id，用photo评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
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
        return photoCommentDAO.countByEntry(entryId, entryOwnerId);
    }

    @Override
    public boolean updateContent(CommentType type, long entryId, int entryOwnerId, long commentId,
            String newContent) {
        return false;
    }

    @Override
    public Comment remove(int actorId, long entryId, int entryOwnerId, long commentId,
            CommentType commentType,CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId + " from comment table to comment_del table");
        }

        // Comment comment = commentDAO.get(commentId, entryId, entryOwnerId);
        Comment comment = photoCommentDAO.get(commentId, entryId, entryOwnerId);
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

        int count = photoCommentDAO.insertToDeletedCommentTable(commentId, comment.getAuthorId(),
                entryId, comment.getContent(), entryOwnerId, new Date(comment.getCreatedTime()),
                comment.getFlag().getValue(), actorId, new Date(System.currentTimeMillis()),
                ownerUser.getHeadUrl(), ownerUser.getName());
        if (count == 0) {
            logger.error("can't move the comment with id " + commentId
                    + " to the comment_del table, remove failed");
            return null;
        }

        //count = commentDAO.purgeFromCommentTable(commentId, entryId, entryOwnerId);
        count = photoCommentDAO.purgeFromCommentTable(commentId, entryId, entryOwnerId);
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
        Comment comment = photoCommentDAO.getFromDeletedCommentTable(commentId, entryId,
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

        int count = photoCommentDAO.insert(commentId, comment.getAuthorId(), entryId,
                comment.getContent(), entryOwnerId, new Date(comment.getCreatedTime()),
                comment.getFlag().getValue(), ownerUser.getHeadUrl(), ownerUser.getName());
        if (count == 0) {
            logger.error("can't insert the comment with id " + commentId
                    + " to the comment table, recover failed");
            return false;
        }

        //count = commentDAO.purgeFromDeletedCommentTable(commentId, entryId, entryOwnerId);
        count = photoCommentDAO.purgeFromDeletedCommentTable(commentId, entryId, entryOwnerId);
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
        if (entryOwnerId == 0) {
            logger.warn("get comment only by commentId, ignore sharding column");
        } else {
            comment = photoCommentDAO.get(commentId, entryId, entryOwnerId);
        }

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
        	//要存进语音评论DB中的id，用photo评论的id做个映射
            long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
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
        PhotoCommentMySQLService.instance = this;
    }

    @Override
    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type, List<Long> entryIds,
            int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entryOwnerId "
                    + entryOwnerId);
        }

        return photoCommentDAO.getCountBatch(entryIds, entryOwnerId);

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
        photoCommentDAO.insertEntryCommentDelList(entryOwnerId, entryId, actorId);

        //3.delete from comment
        photoCommentDAO.purgeFromCommentTable(entryId, entryOwnerId);

        return true;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getFriendsCommentListByEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, boolean, int, int, com.renren.ugc.comment.strategy.QueryOrder, java.util.List)
     */
    @Override
    public List<Comment> getFriendsCommentListByEntry(CommentType type, String entryId,
            int entryOwnerId, boolean includeGlobalComments, int offset, int limit,
            QueryOrder order, List<Integer> userIds) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getAuthorListByEntry(java.lang.String, int, boolean)
     */
    @Override
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId,
            boolean includeGlobalComments, CommentType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Long, Comment> getMulti(long entryId, int entryOwnerId, List<Long> commentIds) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entryOwnerId "
                    + entryOwnerId);
        }

        return photoCommentDAO.getMultipleCommentsByEntry(entryId, entryOwnerId, commentIds);
    }
    
    /**
     * @param entryOwnerId
     * @param entryId
     * @param incCount
     * 
     *  修改照片评论数量
     */
    public void updatePhotoCommentCount(int entryOwnerId, 
            long entryId, int incCount) {
        photoCommentDAO.incPhotoCommentCount(entryId, entryOwnerId,incCount);
    }
    
    /**
     * @param entryOwnerId
     * @param entryId
     * @param incCount
     * 
     *  修改照片评论数量
     */
    public void updateAlbumCommentCount(int entryOwnerId, 
            long entryId, int incCount) {
        photoCommentDAO.incAlbumCommentCount(entryId, entryOwnerId,incCount);
    }
    
    /**
     * @param entryOwnerId
     * @param entryId
     * @param incCount
     * 
     *  修改虚拟相册评论数量
     */
    public void updateChildAlbumCommentCount(int entryOwnerId, 
            long entryId, int incCount) {
        photoCommentDAO.incChildAlbumCommentCount(entryId, entryOwnerId,incCount);
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
                long id = CommentBusIDentifier.getInstance().genCommentId(CommentType.Photo, comment.getId());
                voiceCommentIds.add(id);
            }
        }

        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);
    }

    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n oldest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        List<Comment> comments = photoCommentDAO.getNOldestCommentOfEntry(entryId, entryOwnerId,limit);
        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }
        
        setVoiceToComments(comments,entryId,entryOwnerId);

        return comments;
    }

    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n latest comment of entry " + entryId);
        }

        //        Comment comment = commentDAO
        //                .getLatestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        List<Comment> comments = photoCommentDAO.getNLatestCommentOfEntry(entryId, entryOwnerId,limit);

        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }
        
        setVoiceToComments(comments,entryId,entryOwnerId);

        return comments;
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
