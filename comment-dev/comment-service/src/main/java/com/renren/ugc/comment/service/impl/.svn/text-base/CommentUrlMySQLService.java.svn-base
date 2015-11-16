package com.renren.ugc.comment.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.app.share.model.ShareDel;
import com.renren.ugc.comment.cache.impl.CommentEntryCacheServiceImpl;
import com.renren.ugc.comment.dao.ExtCommentDAO;
import com.renren.ugc.comment.dao.IdSequenceDAO;
import com.renren.ugc.comment.dao.ShareDeleteLogDAO;
import com.renren.ugc.comment.dao.UrlCommentDAO;
import com.renren.ugc.comment.dao.VoiceCommentDAO;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.service.CommentUrlStorageService;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.util.UrlUtil;
import com.renren.ugc.comment.xoa2.CommentType;

@Service("comment.service.mysql.url")
public class CommentUrlMySQLService implements CommentUrlStorageService, InitializingBean {

    @Autowired
    private UrlCommentDAO urlCommentDAO;

    @Autowired
    private ExtCommentDAO extCommentDAO;

    @Autowired
    private VoiceCommentDAO voiceCommentDAO;

    @Autowired
    private IdSequenceDAO idSequenceDAO;

    @Autowired
    private ShareDeleteLogDAO shareDeleteLogDAO;

    private Logger logger = Logger.getLogger(this.getClass());
    
    private static CommentUrlMySQLService instance;

    public static CommentUrlMySQLService getInstance() {
        return instance;
    }

    @Override
    public void create(CommentType type, Comment comment, String metadata,
        String url) {

        if (logger.isDebugEnabled()) {
            logger.debug("insert a new comment " + comment.getId()
                         + " to comment table");
        }

        if (comment.getEntry() == null) {
            logger.warn("the comment's corresponding entry is not specified, ignored comment creation");
            throw new UGCCommentException(
                "the comment's entry is not specified");
        }

        if (StringUtils.isEmpty(url)) {
            logger.warn("CommentUrlMySQLService urlmdu is null|entryId:"
                        + comment.getEntry().getId() + "|ownerId:"
                        + comment.getEntry().getOwnerId() + "|type:"
                        + type.getValue());
            return;
        }

        long entryId = comment.getEntry().getId();
        int entryOwnerId = comment.getEntry().getOwnerId();
        String content = comment.getOriginalContent();
        Flag flag = comment.getFlag();

        // 重新设置mark_bit
        int state = 0;
        // 全局评论的Id和评论中心产生的id一样，这里就不设置了
        if (flag != null && flag.isUseVoice()) {
            state = CommentOldUgcStateUtil.setUrlVoice(state);
        }

        urlCommentDAO.insert(comment.getId(), url, comment.getAuthorId(),
            entryId, entryOwnerId, content, comment.getToUserId(), state,
            metadata, type.ordinal());

    }

    @Override
    public Comment remove(int actorId, String entryUrl, long commentId,
        CommentType commentType) {
        if (logger.isDebugEnabled()) {
            logger.debug("move comment " + commentId
                         + " from comment table to comment_del table");
        }

        if (StringUtils.isBlank(entryUrl)) {
            return null;
        }

        Comment comment = urlCommentDAO.getUrlCommentByUrl(entryUrl, commentId);
        if (comment == null) {
            logger.warn("can't remove the comment by id " + commentId
                        + " because it doesn't exists");

            return null;
        }

        int count = urlCommentDAO.deleteUrlComment(commentId, entryUrl);
        if (count == 0) {
            logger.error("can't purge the comment with id " + commentId
                         + " from DB, remove failed");
            return null;
        }

        return comment;
    }

    @Override
    public Comment get(String entryUrl, long commentId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get comment " + commentId);
        }

        Comment comment = urlCommentDAO.getUrlCommentByUrl(entryUrl, commentId);

        if (comment == null) {
            return null;
        }

        return comment;
    }

    @Override
    public List<Comment> getListByEntry(CommentType type, String entryUrl,
        long borderCommentId, int limit, QueryOrder order) {

        if (logger.isDebugEnabled()) {
            logger.debug("get comment list by entryurl " + entryUrl);
        }

        if (StringUtils.isEmpty(entryUrl)) {
            logger.warn("getListByEntry entryUrl is null");
            return null;
        }

        List<Comment> comments = null;

        if (borderCommentId == 0) {
            comments =
                    urlCommentDAO.getUrlCommentListByUrl(entryUrl,
                        getQueryOrderString(order), limit);
        } else {
            comments =
                    urlCommentDAO.getUrlCommentListByUrlWithId(entryUrl,
                        borderCommentId, getQueryOrderString(order), limit);
        }

        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size()
                         + " comment(s) from DB");
        }

        return comments;
    }

    @Override
    public List<Comment> getListByEntry(CommentType type, String entryUrl,
        int offset, int limit, QueryOrder order) {
        if (logger.isDebugEnabled()) {
            logger.debug("get comment list by entryUrl " + entryUrl
                         + " with offset");
        }

        if (StringUtils.isEmpty(entryUrl)) {
            logger.warn("getListByEntry entryUrl is null");
            return null;
        }

        List<Comment> comments = null;

        comments =
                urlCommentDAO.getUrlCommentListByUrlWithOffset(entryUrl,
                    getQueryOrderString(order), limit, offset);

        if (comments == null) {
            comments = Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size()
                         + " comment(s) from DB");
        }

        return comments;
    }

    @Override
    public Comment getOldestCommentOfEntry(String entryUrl) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the oldest comment of entryUrl " + entryUrl);
        }

        if (StringUtils.isEmpty(entryUrl)) {
            logger.warn("getOldestCommentOfEntry entryUrl is null");
            return null;
        }

        Comment comment = urlCommentDAO.getOldestCommentOfEntry(entryUrl);
        if (comment == null) {
            return comment;
        }

        return comment;
    }

    @Override
    public Comment getLatestCommentOfEntry(String entryUrl) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the latest comment of entryUrl " + entryUrl);
        }

        if (StringUtils.isEmpty(entryUrl)) {
            logger.warn("getLatestCommentOfEntry entryUrl is null");
            return null;
        }

        Comment comment = urlCommentDAO.getLatestCommentOfEntry(entryUrl);
        if (comment == null) {
            return comment;
        }

        return comment;
    }

    /**
     * convert query order to a query string "ASC" or "DESC". default is "ASC"
     */
    private static final String getQueryOrderString(QueryOrder order) {
        if (order == null || !order.isDesc()) {
            return "ASC";
        }

        return "DESC";
    }

    /*
     * (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#
     * getFriendsCommentListByEntry(com.renren.ugc.comment.xoa2.CommentType,
     * long, int, int, int, com.renren.ugc.comment.strategy.QueryOrder,
     * java.util.List)
     */
    @Override
    public List<Comment> getFriendsCommentListByEntry(CommentType type,
        String urlmd5, int entryOwnerId, int offset, int limit,
        QueryOrder order, List<Integer> userIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("get friends comment list by urlmd5 " + urlmd5
                         + " with offset");
        }

        List<Integer> replayToUsers = getReplayToUsers(userIds);

        List<Comment> comments =
                urlCommentDAO.getFriendsCommentListByEntryWithOffset(urlmd5,
                    entryOwnerId, offset, getQueryOrderString(order), limit,
                    userIds, replayToUsers);

        if (comments == null) {
            comments = Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + comments.size()
                         + " comment(s) from DB");
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
        List<Integer> replayToUsers =
                new ArrayList<Integer>(userIds.size() + 1);
        replayToUsers.addAll(userIds);
        replayToUsers.add(0);
        return replayToUsers;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.service.CommentStorageService#getAuthorListByEntry
     * (java.lang.String, int)
     */
    @Override
    public List<Integer> getAuthorListByEntry(String entryId, int entryOwnerId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get author list by entry " + entryId + " with offset");
        }

        List<Integer> authors =
                urlCommentDAO.getAuthorListByEntry(entryId, entryOwnerId);

        if (logger.isDebugEnabled()) {
            logger.debug("totally authors return " + authors.size()
                         + " author(s) from DB");
        }

        return authors;
    }

    @Override
    public long getCommentCountByEntry(CommentType type, String urlmd5) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the total number of comments belonging to entry "
                         + urlmd5);
        }

        return urlCommentDAO.countByEntry(urlmd5);
    }

    /*
     * (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentUrlStorageService#
     * getFriendCommentCountByEntry(com.renren.ugc.comment.xoa2.CommentType,
     * java.lang.String, java.util.List)
     */
    @Override
    public long getFriendCommentCountByEntry(CommentType type, String urlmd5,
        List<Integer> userIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("get friends comment list by urlmd5 " + urlmd5
                         + " with userIds " + userIds);
        }

        List<Integer> replyToUsers = getReplayToUsers(userIds);

        long result =
                urlCommentDAO.getFriendCommentCountByEntry(urlmd5, userIds,
                    replyToUsers);

        if (logger.isDebugEnabled()) {
            logger.debug("totally return " + result + " from DB");
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentUrlStorageService#
     * getNLatestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType,
     * java.lang.String, java.util.List)
     */
    @Override
    public List<Comment> getNLatestFriendCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, String urlmd5, List<Integer> userIds,
        int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n latest comment of entry " + urlmd5
                         + " userIds " + userIds);
        }

        List<Integer> replyToUsers = getReplayToUsers(userIds);

        List<Comment> comments =
                urlCommentDAO.getNLatestFriendCommentOfEntry(urlmd5, userIds,
                    replyToUsers, limit);
        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }

        setExtAndVoiceToComments(comments, entryId, entryOwnerId);

        return comments;
    }

    /*
     * (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentUrlStorageService#
     * getNOldestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType,
     * java.lang.String, java.util.List)
     */
    @Override
    public List<Comment> getNOldestFriendCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, String urlmd5, List<Integer> userIds,
        int limit) {
        if (logger.isDebugEnabled()) {
            logger.debug("get the n latest comment of entry " + urlmd5
                         + " userIds " + userIds);
        }

        List<Integer> replyToUsers = getReplayToUsers(userIds);

        List<Comment> comments =
                urlCommentDAO.getNOldestFriendCommentOfEntry(urlmd5, userIds,
                    replyToUsers, limit);
        if (comments == null || comments.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("nothing is found from DB, return empty list");
            }
            return Collections.emptyList();
        }

        setExtAndVoiceToComments(comments, entryId, entryOwnerId);

        return comments;
    }

    /**
     * @param comments
     * @param entryId
     * @param entryOwnerId 把comment的ext和voice信息set到comment里去
     */
    private void setExtAndVoiceToComments(List<Comment> comments, long entryId,
        int entryOwnerId) {

        if (comments == null || comments.size() == 0) {
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

    private void fillExtComments(List<Long> extCommentIds, long entryId,
        int entryOwnerId, Collection<Comment> comments) {
        // add extended comment contents
        if (extCommentIds != null && !extCommentIds.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("get ext comment contents for comments: "
                             + extCommentIds);
            }
            Map<Long, String> extContents =
                    extCommentDAO.getExtContents(extCommentIds, entryId,
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

    private void fillVoiceComments(List<Long> voiceCommentIds, long entryId,
        int entryOwnerId, Collection<Comment> comments) {
        // add voice comment
        if (voiceCommentIds != null && !voiceCommentIds.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("get voice comments: " + voiceCommentIds);
            }
            Map<Long, CommentVoiceInfo> voiceInfos =
                    voiceCommentDAO.getVoiceInfos(voiceCommentIds, entryId,
                        entryOwnerId);

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
                if (flag.isUseVoice()
                    && (voiceInfo = voiceInfos.get(comment.getId())) != null) {

                    // check url is full url
                    if (voiceInfo.getVoiceUrl() != null
                        && !voiceInfo.getVoiceUrl().startsWith("http://")) {
                        voiceInfo.setVoiceUrl(UrlUtil.getFullUrl(voiceInfo.getVoiceUrl()));
                    }
                    comment.setVoiceInfo(voiceInfo);
                }
            }

        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.service.CommentUrlStorageService#getDeletedShareById
     * (int, long)
     */
    @Override
    public ShareDel getDeletedShareById(int entryOwnerId, long entryId) {
        if (logger.isDebugEnabled()) {
            logger.debug("get deleted share. entryOwnerId:" + entryOwnerId
                         + " entryId:" + entryId);
        }

        return shareDeleteLogDAO.getById(entryOwnerId, entryId);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
