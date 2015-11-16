package com.renren.ugc.comment.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.CommentDAO;
import com.renren.ugc.comment.dao.ExtCommentDAO;
import com.renren.ugc.comment.dao.VoiceCommentDAO;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.service.CommentStorageService;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.util.AdminManagerUtil;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.CommentPropertyPlaceholderConfigurer;
import com.renren.ugc.comment.util.UrlUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;

/**
 * The MySQL comment storage service implementation
 * 
 * @author jiankuan.xing
 * 
 */
@Service("comment.service.mysql")
public class CommentMySQLService implements CommentStorageService, InitializingBean {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private ExtCommentDAO extCommentDAO;

    @Autowired
    private VoiceCommentDAO voiceCommentDAO;

    private static CommentMySQLService instance;

    /**
     * if the content's length is larger than this threshold, it must be
     * added to the comment_ext table
     */
    private static final int EXT_CONTENT_LEN_THRESHOLD = 4090;

    private Logger logger = Logger.getLogger(this.getClass());

    public static CommentMySQLService getInstance() {
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

        //把这段逻辑放在了CommentLogicImpl
        //        Flag flag = new Flag();
        //        String content = comment.getContent();
        //
        //        // check it should be added to ext comment table?
        //        if (content.length() > EXT_CONTENT_LEN_THRESHOLD) {
        //            flag.setUseExtent();
        //            content = "";
        //            if (logger.isDebugEnabled()) {
        //                logger.debug("the content is too long, insert it to comment_ext table");
        //            }
        //        }
        //
        //        // check it should be added to voice comment table?
        //        if (comment.getVoiceInfo() != null) {
        //            flag.setUseVoice();
        //            if (logger.isDebugEnabled()) {
        //                logger.debug("the comment has voice info");
        //            }
        //        }
        //        
        //     // check it has linkedInfo
        //        if(comment.isLinked()){
        //            flag.setUseLinked();
        //            if (logger.isDebugEnabled()) {
        //                logger.debug("the comment has linked info");
        //            }
        //        }
        //        comment.setFlag(flag);
        String content = comment.getContent();
        Flag flag = comment.getFlag();

        //to_comment_id在db中应该为long字段，现在为int字段
        //因为没有使用到，暂且把它设为0
        commentDAO.insert(comment.getId(), type.getValue(), comment.getAuthorId(), entryId,
                entryOwnerId, content, new Date(comment.getCreatedTime()), comment.getToUserId(),
                comment.getToCommentId(), comment.getWhipserToId(), flag.getValue(), metadata);

        // insert extCommentDAO
        if (flag.isUseExtent()) {
            String extContent = comment.getContent();
            extCommentDAO.insert(comment.getId(), entryId, entryOwnerId, extContent);
        }

        // insert voiceCommentDAO
        if (flag.isUseVoice()) {
            CommentVoiceInfo voiceInfo = comment.getVoiceInfo();
            voiceCommentDAO.insert(comment.getId(), comment.getType(), comment.getId(), entryId, entryOwnerId,
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
            comments = commentDAO.getCommentListByEntry(type.getValue(), entryId, entryOwnerId,
                    getQueryOrderString(order), limit);
        } else {
            if (order == QueryOrder.ASC) {
                comments = commentDAO.getCommentListByEntryASC(type.getValue(), entryId,
                        entryOwnerId, borderCommentId, limit);
            } else {
                comments = commentDAO.getCommentListByEntryDESC(type.getValue(), entryId,
                        entryOwnerId, borderCommentId, limit);
            }
        }

        if (comments == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get comments is null|type:"+type.getValue()+"|entryId:"+entryId+"|entryOwnerId:"+entryOwnerId);
            }
            return Collections.emptyList();
        }
        
        if(comments.isEmpty()){
            if (logger.isDebugEnabled()) {
                logger.debug("get comments size is 0 |type:"+type.getValue()+"|entryId:"+entryId+"|entryOwnerId:"+entryOwnerId);
            }
            return Collections.emptyList();
        }

        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size() + " comment(s) from DB");
        }

        return comments;
    }

    private void fillExtComments(List<Long> extCommentIds, long entryId, int entryOwnerId,
            Collection<Comment> comments) {
        // add extended comment contents
        if (extCommentIds != null && !extCommentIds.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("get ext comment contents for comments: " + extCommentIds);
            }
            Map<Long, String> extContents = extCommentDAO.getExtContents(extCommentIds, entryId,
                    entryOwnerId);

            if (extContents == null || extContents.isEmpty()) {
                logger.warn("can't read extended content from DB");
                return;
            }

            if (extContents.size() != extCommentIds.size()) {
                logger.warn(String.format(
                        "not all the ext comments are read from DB, expected: %d, actual: %d",
                        extCommentIds.size(), extContents.size()));
            }

            for (Comment comment : comments) {
                Flag flag = comment.getFlag();
                if (flag.isUseExtent()) {
                    comment.setContent(extContents.get(comment.getId()));
                }
            }
        }
    }

    private void fillVoiceComments(List<Long> voiceCommentIds, long entryId, int entryOwnerId,
            Collection<Comment> comments) {
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
                if (flag.isUseVoice() && (voiceInfo = voiceInfos.get(comment.getId())) != null) {

                    //check url is full url
                    if (voiceInfo.getVoiceUrl() != null && !voiceInfo.getVoiceUrl().startsWith("http://")) {
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

        comments = commentDAO.getCommentListByEntryWithOffset(type.getValue(), entryId,
                entryOwnerId, offset, getQueryOrderString(order), limit);

        if (comments == null) {
            comments = Collections.emptyList();
        }
        
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

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

        Comment comment = commentDAO.getOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        if (comment == null) {
            return comment;
        }

        List<Comment> comments = new ArrayList<Comment>(1);
        comments.add(comment);
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comment;
    }

    @Override
    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the latest comment of entry " + entryId);
        }

        Comment comment = commentDAO.getLatestCommentOfEntry(type.getValue(), entryId, entryOwnerId);
        if (comment == null) {
            return comment;
        }
        
        List<Comment> comments = new ArrayList<Comment>(1);
        comments.add(comment);
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comment;
    }

    @Override
    public long getCommentCountByEntry(CommentType type, long entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entry " + entryId);
        }

        return commentDAO.countByEntry(type.getValue(), entryId, entryOwnerId);
    }

    @Override
    public boolean updateContent(CommentType type, long entryId, int entryOwnerId, long commentId,
            String newContent) {
        if (logger.isDebugEnabled()) {
            logger.debug("update content for comment " + commentId);
        }

        Flag flag = new Flag();
        String content = newContent;
        if (newContent.length() > EXT_CONTENT_LEN_THRESHOLD) {
            if (logger.isDebugEnabled()) {
                logger.debug("the new content is too long, insert it to comment_ext table");
            }
            flag.setUseExtent();
            content = "";
        }
        int count = commentDAO.updateCommentContent(commentId, entryId, entryOwnerId, content,
                flag.getValue());

        if (count == 0) {
            return false;
        }

        if (flag.isUseExtent()) {
            count = extCommentDAO.updateCommentContent(commentId, entryId, entryOwnerId, newContent);
            if (count == 0) {
                logger.warn("the new content isn't inserted to the comment_ext table");
            }
        }
        return true;
    }

    @Override
    public Comment remove(int actorId, long entryId, int entryOwnerId, long commentId,
            CommentType commentType,CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId + " from comment table to comment_del table");
        }

        Comment comment = commentDAO.get(commentId, entryId, entryOwnerId);
        if (comment == null) {
            logger.warn("can't remove the comment by id " + commentId
                    + " because it doesn't exists");

            return null;
        }

        //check rights
        boolean canDeleteComment = (actorId == comment.getAuthorId()) || (actorId == entryOwnerId)
                || (AdminManagerUtil.getInstance().hasRight(commentType, actorId, strategy,entryOwnerId,comment.getAuthorId()));
        if (!canDeleteComment) {
            throw new UGCCommentException(CommentError.PERMISSION_DENY_MSG);
        }

        int count = commentDAO.insertToDeletedCommentTable(commentId, comment.getType(),
                comment.getAuthorId(), entryId, entryOwnerId, comment.getContent(), new Date(
                        comment.getCreatedTime()), comment.getToUserId(), comment.getToCommentId(),
                comment.getWhipserToId(), comment.getFlag().getValue(),
                comment.getMetadata().encode(), actorId, new Date(System.currentTimeMillis()));
        if (count == 0) {
            logger.error("can't move the comment with id " + commentId
                    + " to the comment_del table, remove failed");
            return null;
        }

        count = commentDAO.purgeFromCommentTable(commentId, entryId, entryOwnerId);
        if (count == 0) {
            logger.error("can't purge the comment with id " + commentId + " from DB, remove failed");
            return null;
        }

//        //硬编码，以后一定要记得即使删除
//        if (commentType == CommentType.Blog && !comment.isOldUgc()) {
//            this.delOldBlogComment(actorId, entryOwnerId, entryId, comment);
//        }
        return comment;
    }

    @Override
    public boolean recover(long entryId, int entryOwnerId, long commentId) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId + " from comment_del table to comment table");
        }

        Comment comment = commentDAO.getFromDeletedCommentTable(commentId, entryId, entryOwnerId);
        if (comment == null) {
            logger.warn("can't recover the comment by id " + commentId
                    + " because it doesn't exists in the delete table");

            return false;
        }

        int count = commentDAO.insert(commentId, comment.getType(), comment.getAuthorId(), entryId,
                entryOwnerId, comment.getContent(), new Date(comment.getCreatedTime()),
                comment.getToUserId(), comment.getToCommentId(), comment.getWhipserToId(),
                comment.getFlag().getValue(), comment.getMetadata().toString());
        if (count == 0) {
            logger.error("can't insert the comment with id " + commentId
                    + " to the comment table, recover failed");
            return false;
        }

        count = commentDAO.purgeFromDeletedCommentTable(commentId, entryId, entryOwnerId);
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

        if (entryId > 0) {
            comment = commentDAO.get(commentId, entryId, entryOwnerId);
        } else if (commentId > 0 && entryId == 0) {
            comment = commentDAO.getByCommentId(commentId, entryOwnerId);
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
            voiceCommentIds.add(comment.getId());
        }

        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);

        fillExtComments(extCommentIds, entryId, entryOwnerId, comments);
        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);

        return comment;
    }

    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment) {
        if (logger.isDebugEnabled()) {
            logger.debug("increase play count for comment  " + commentId);
        }

        //1. get comment
        Comment comment = commentDAO.get(commentId, entryId, entryOwnerId);
        if (comment == null) {
            logger.warn("can't increase the commentplaycount by id  " + commentId
                    + " because it doesn't exists in comment");
            throw new UGCCommentException(CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG);
        }

        //2.get voice
        if (!comment.isVoiceComment()) {
            logger.warn("try to increase play count to a non-voice comment");
            throw new UGCCommentException(CommentError.COMMENT_IS_NOT_VOICE,
                    CommentError.COMMENT_IS_NOT_VOICE_MSG);
        }

        CommentVoiceInfo commentVoiceInfo = voiceCommentDAO.getVoiceInfo(commentId, entryId,
                entryOwnerId);

        if (commentVoiceInfo == null) {
            logger.warn("can't increase the commentplaycount by id  " + commentId
                    + " because it doesn't exists in voicecomment");
            throw new UGCCommentException(CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG);
        }

        int currPlayCount = commentVoiceInfo.getVoicePlayCount();

        int increased = voiceCommentDAO.increasePlayCount(commentId, entryId, entryOwnerId,
                increment);
        if (increased == 0) {
            logger.error("can't increase play count for comment " + commentId);

            return 0;
        } else {
            return currPlayCount + increment;
        }
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
        CommentMySQLService.instance = this;
    }

    @Override
    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type, List<Long> entryIds,
            int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entryOwnerId "
                    + entryOwnerId);
        }

        return commentDAO.getCountBatch(type.getValue(), entryIds, entryOwnerId);

    }

    @Override
    public boolean removeAll(int actorId, long entryId, int entryOwnerId, CommentType commentType, CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("move comment  from comment table to comment_del table");
        }

        //check rights
        boolean canDeleteComment = (actorId == entryOwnerId)
                || (AdminManagerUtil.getInstance().hasRight(commentType, actorId, strategy,entryOwnerId,0));
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
        //so hard 555
        //----------------------------------------------commentId现在在db里是int,因此这里先设为0-------------------
        //----------------------------------------------db改完之后记得要改回来---------------------------------------
        for (Comment comment : comments) {
        	comment.setToCommendId(0);
            int res = commentDAO.insertToDeletedCommentTable(comment.getId(), comment.getType(),
                    comment.getAuthorId(), entryId, entryOwnerId, comment.getContent(), new Date(
                            comment.getCreatedTime()), comment.getToUserId(),
                    comment.getToCommentId(), comment.getWhipserToId(),
                    comment.getFlag().getValue(), comment.getMetadata().encode(), actorId,
                    new Date(System.currentTimeMillis()));
            if (res == 0) {
                logger.error("can't move the comment with id " + comment.getId()
                        + " to the comment_del table, remove failed");
                return false;
            }

        }

        count = commentDAO.purgeFromCommentTable(entryId, entryOwnerId,commentType.getValue());

        return true;
    }

//    private void delOldBlogComment(int hostId, int entryOwnerId, long entryId, Comment c) {
//
//        Metadata meta = c.getMetadata();
//
//        if (meta != null && meta.get(CommentCenterConsts.ORIGIN_COMMENT_ID_FIELD) != null) {
//
//            try {
//                long oldId = Long.parseLong(meta.get(CommentCenterConsts.ORIGIN_COMMENT_ID_FIELD));
//
//                //1.放在del表中
//                blogCommentDAO.insertEntryCommentDel(entryOwnerId, oldId, hostId);
//
//                //2.删blog 表
//                blogCommentDAO.remove(entryOwnerId, oldId);
//            } catch (Exception e) {
//                logger.error("delOldBlogComment error|ownerId:" + entryOwnerId + "|entryId:"
//                        + entryId);
//            }
//        }
//    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getFriendsCommentListByEntry(com.renren.ugc.comment.xoa2.CommentType, long, int, boolean, int, int, com.renren.ugc.comment.strategy.QueryOrder, java.util.List)
     */
    @Override
    public List<Comment> getFriendsCommentListByEntry(CommentType type, String entryId,
            int entryOwnerId, boolean fromGlobalComment, int offset, int limit,
            QueryOrder order, List<Integer> userIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("get friends comment list by entry " + entryId + " with offset");
        }

        List<Integer> replayToUsers = getReplayToUsers(userIds);

        List<Comment> comments = null;

        if (type == CommentType.Album) {
            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_ALBUM_COMMENT))) {

            }
        } else if (type == CommentType.Photo) {
            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_PHOTO_COMMENT))) {
//                comments = photoCommentDAO.getFriendsCommentListByEntryWithOffset(
//                        Long.valueOf(entryId), entryOwnerId, offset, getQueryOrderString(order),
//                        limit, userIds);
            }
        } else if (type == CommentType.Status) {
            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_STATUS_COMMENT))) {
//                comments = statusCommentDAO.getFriendsCommentListByEntryWithOffset(
//                        Long.valueOf(entryId), entryOwnerId, offset, getQueryOrderString(order),
//                        limit, userIds);
            }
        } else if (type == CommentType.Share) {
            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_SHARE_COMMENT))) {
                //如果迁移不完，这个地方要访问老的分享评论
            }
        } else {
            comments = commentDAO.getFriendsCommentListByEntryWithOffset(type.getValue(),
                    Long.valueOf(entryId), entryOwnerId, offset, getQueryOrderString(order), limit,
                    userIds, replayToUsers);
        }

        if (comments == null) {
            comments = Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size() + " comment(s) from DB");
        }

        return comments;
    }

    /**
     * 获取回复者
     * 
     * @param userIds
     * @return
     */
    private List<Integer> getReplayToUsers(List<Integer> userIds) {
        List<Integer> replayToUsers = new ArrayList<Integer>(userIds.size() + 1);
        replayToUsers.addAll(userIds);
        replayToUsers.add(0);
        return replayToUsers;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getAuthorListByEntry(java.lang.String, int, boolean)
     */
    @Override
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId,
            boolean includeGlobalComments, CommentType commentType) {
        if (logger.isDebugEnabled()) {
            logger.debug("get author list by entry " + entryId + " with offset");
        }

        List<Integer> authors = null;
//        if (commentType == CommentType.Album) {
//            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_ALBUM_COMMENT))) {
//                //如果迁移不完，这个地方这个地方要访问老的album评论
//            }
//        } else if (commentType == CommentType.Photo) {
//            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_PHOTO_COMMENT))) {
//                authors = photoCommentDAO.getAuthorListByEntry(Long.valueOf(entryId), entryOwnerId);
//            }
//        } else if (commentType == CommentType.Status) {
//            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_STATUS_COMMENT))) {
//                authors = statusCommentDAO.getAuthorListByEntry(Long.valueOf(entryId), entryOwnerId);
//            }
//        } else if (commentType == CommentType.Share) {
//            if ("N".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.CENTER_INCLUDE_SHARE_COMMENT))) {
//                //如果迁移不完，这个地方要访问老的分享评论
//            }
//        } else {
            authors = commentDAO.getAuthorListByEntry(entryId, entryOwnerId,
                    commentType.getValue());
//        }

        if (logger.isDebugEnabled()) {
            logger.debug("totally authors return " + authors.size() + " author(s) from DB");
        }

        return authors;
    }

    @Override
    public Map<Long, Comment> getMulti(long entryId, int entryOwnerId, List<Long> commentIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("get comment " + commentIds);
        }

        Map<Long, Comment> commentMap = commentDAO.getMultipleCommentsByEntry(entryId,
                entryOwnerId, commentIds);

        if (commentMap == null) {
            return null;
        }

        List<Long> extCommentIds = new ArrayList<Long>();
        List<Long> voiceCommentIds = new ArrayList<Long>();

        Collection<Comment> comments = commentMap.values();

        for (Comment comment : comments) {
            Flag flag = comment.getFlag();
            if (flag.isUseExtent()) {
                extCommentIds.add(comment.getId());
            }

            if (flag.isUseVoice()) {
                voiceCommentIds.add(comment.getId());
            }
        }

        fillExtComments(extCommentIds, entryId, entryOwnerId, comments);
        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);

        return commentMap;
    }
    
    /**
     * @param comments
     * @param entryId
     * @param entryOwnerId
     * 
     *  把comment的ext和voice信息set到comment里去
     */
    private void setExtAndVoiceToComments(List<Comment> comments,long entryId,int entryOwnerId){
        
        if(comments == null || comments.size() == 0){
            return;
        }

        List<Long> extCommentIds = new ArrayList<Long>();
        List<Long> voiceCommentIds = new ArrayList<Long>();

        // extract special comments
        for (Comment comment : comments) {
            Flag flag = comment.getFlag();
            if (flag.isUseExtent()) {
                extCommentIds.add(comment.getId());
            }

            if (flag.isUseVoice()) {
                voiceCommentIds.add(comment.getId());
            }
        }

        fillExtComments(extCommentIds, entryId, entryOwnerId, comments);
        fillVoiceComments(voiceCommentIds, entryId, entryOwnerId, comments);
    }

    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n oldest comment of entry " + entryId);
        }

        List<Comment> comments = commentDAO.getNOldestCommentOfEntry(type.getValue(), entryId, entryOwnerId,limit);
        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }

        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comments;
    }

    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n latest comment of entry " + entryId);
        }

        List<Comment> comments = commentDAO.getNLatestCommentOfEntry(type.getValue(), entryId, entryOwnerId,limit);
        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }
        
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comments;
    }

	@Override
	public List<Comment> getCommentListByAuthorIdWithOffset(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			int offset, int limit) {
		// TODO Auto-generated method stub
		List<Comment> comments = commentDAO.getCommentListByAuthorIdWithOffset(
				type.getValue(), entryId, entryOwnerId, authorId, offset,
				getQueryOrderString(order), limit);
		if (CollectionUtils.isEmpty(comments)) {
			comments = Collections.emptyList();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("getCommentListByAuthorIdWithOffset entryId is:  " + entryId
					+ " authorId:" + authorId + "want:" + limit
					+ " and return " + comments.size());
		}
		return comments;
	}

	@Override
	public long getCountByAuthorId(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId) {
		return commentDAO.countByEntryAndAuthorId(type.getValue(), entryId, entryOwnerId, authorId);
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, long borderCommentId, int limit,
			QueryOrder order,List<Integer> authorIds,List<Integer> toUserIds) {
		if (logger.isDebugEnabled()) {
            logger.debug("getCommentListByAuthorIdAndToUserId list by entry " + entryId);
        }

        List<Comment> comments = null;

        if (borderCommentId == 0) {
            comments = commentDAO.getCommentListByAuthorIdAndToUserId(type.getValue(), entryId, entryOwnerId, getQueryOrderString(order), limit, authorIds, toUserIds);
        } else {
            if (order == QueryOrder.ASC) {
                comments = commentDAO.getCommentListByAuthorIdAndToUserIdWithIdAsc(type.getValue(), entryId, entryOwnerId, borderCommentId, limit, authorIds, toUserIds);
            } else {
            	comments = commentDAO.getCommentListByAuthorIdAndToUserIdWithIdDesc(type.getValue(), entryId, entryOwnerId, borderCommentId, limit, authorIds, toUserIds);
            }
        }

        if (comments == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get comments is null|type:"+type.getValue()+"|entryId:"+entryId+"|entryOwnerId:"+entryOwnerId);
            }
            return Collections.emptyList();
        }
        
        if(comments.isEmpty()){
            if (logger.isDebugEnabled()) {
                logger.debug("get comments size is 0 |type:"+type.getValue()+"|entryId:"+entryId+"|entryOwnerId:"+entryOwnerId);
            }
            return Collections.emptyList();
        }

        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size() + " comment(s) from DB");
        }

        return comments;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, int offset, int limit,
			QueryOrder order,List<Integer> authorIds,List<Integer> toUserIds) {
		 if (logger.isDebugEnabled()) {
	            logger.debug("get comment list by entry " + entryId + " with offset");
	        }

	        List<Comment> comments = null;

	        comments = commentDAO.getCommentListByAuthorIdAndToUserIdWithOffset(type.getValue(), entryId, entryOwnerId, offset, getQueryOrderString(order), limit, authorIds, toUserIds);

	        if (comments == null) {
	            comments = Collections.emptyList();
	        }
	        
	        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

	        if (logger.isDebugEnabled()) {
	            logger.debug("totally return " + comments.size() + " comment(s) from DB");
	        }

	        return comments;
	}

	@Override
	public Comment getOldestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		if (logger.isDebugEnabled()) {
            logger.debug("get the oldest comment of entry " + entryId);
        }

        Comment comment = commentDAO.getOldestCommentByAuthorIdAndToUserId(type.getValue(), entryId, entryOwnerId, authorIds, toUserIds);
        if (comment == null) {
            return comment;
        }

        List<Comment> comments = new ArrayList<Comment>(1);
        comments.add(comment);
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comment;
	}

	@Override
	public Comment getLatestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		if (logger.isDebugEnabled()) {
            logger.debug("get the latest comment of entry " + entryId);
        }

        Comment comment = commentDAO.getLatestCommentByAuthorIdAndToUserId(type.getValue(), entryId, entryOwnerId, authorIds, toUserIds);
        if (comment == null) {
            return comment;
        }

        List<Comment> comments = new ArrayList<Comment>(1);
        comments.add(comment);
        setExtAndVoiceToComments(comments,entryId,entryOwnerId);

        return comment;
	}

	@Override
	public long getCommentCountByEntry(CommentType type, long entryId,
			int entryOwnerId, List<Integer> authorIds, List<Integer> toUserIds) {
		 if (logger.isDebugEnabled()) {
	            logger.debug("get the total number of comments belonging to entry " + entryId);
	        }

	        return commentDAO.getCommentCountByAuthorIdAndToUserId(type.getValue(), entryId, entryOwnerId, authorIds, toUserIds);
	}
	
	@Override
	public List<Comment> getCommentListByAuthorIdWithBorderId(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			long borderId, int limit) {
		
		 List<Comment> comments = null;
		
        if (order == QueryOrder.ASC) {
            comments = commentDAO.getCommentListByAuthorIdWithBorderIdASC(type.getValue(), entryId, 
            		entryOwnerId, authorId, borderId, limit);
        } else {
            comments = commentDAO.getCommentListByAuthorIdWithBorderIdDESC(type.getValue(), entryId, 
            		entryOwnerId, authorId, borderId, limit);
        }
		 
		if (CollectionUtils.isEmpty(comments)) {
			comments = Collections.emptyList();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("getCommentListByAuthorIdWithBorderId entryId is:  " + entryId
					+ " authorId:" + authorId + "want:" + limit
					+ " and return " + comments.size());
		}
		return comments;
	}

	@Override
	public long getEntryId(CommentType type, int entryOwnerId, long commentId) {

		if(commentId == 0 || entryOwnerId == 0) {
    		return 0;
    	}
    	Long result = commentDAO.getEntryId(type.getValue(), entryOwnerId, commentId);
    	if(result != null) {
    		return result.longValue();
    	}
    	return 0;
	}
	
	
}
