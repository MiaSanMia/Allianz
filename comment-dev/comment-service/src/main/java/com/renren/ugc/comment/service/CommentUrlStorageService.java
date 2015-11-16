package com.renren.ugc.comment.service;

import java.util.List;

import com.renren.app.share.model.ShareDel;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx 针对entryId是String类型的接口
 */
public interface CommentUrlStorageService {

    /**
     * create a comment
     */
    public void create(CommentType type, Comment comment, String metadata,
        String url);

    /**
     * remove a comment
     * 
     * @return the removed Comment
     */
    public Comment remove(int actorId, String entryUrl, long commentId,
        CommentType commentType);

    /**
     * Get a comment with specified Id. Note all the deleted comment won't be
     * returned.<br>
     * This API would be put to public due to the MySQL table is sherded by
     * entryId
     * 
     * @return the comment with <code>commentId</code>
     */
    public Comment get(String entryUrl, long commentId);

    /**
     * get the comments list belonging to an entry.
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner's id
     * @param borderCommentId the comment where the query starts. Comment with
     *        this id will be returned in the result list.
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @return a list of comments that matches the query conditions, or an empty
     *         list if nothing matched are found
     */
    public List<Comment> getListByEntry(CommentType type, String entryUrl,
        long borderCommentId, int limit, QueryOrder order);

    /**
     * get the comments list belonging to an entry (with non-trivial offset)
     * warning!! this method is only used to support old pageNum based comment
     * list query. Any new biz who's gonna use comment center should not use
     * this kind of query
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner's id
     * @param offset the start index in the query result
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @return a list of comments that matches the query conditions, or an empty
     *         list if nothing matched are found
     */
    public List<Comment> getListByEntry(CommentType type, String entryUrl,
        int offset, int limit, QueryOrder order);

    /**
     * get the oldest comment of an entry
     * 
     * @param type comment type
     * @param entryUrl entry url
     * @return the oldest comment of the entry or null if not found
     */
    public Comment getOldestCommentOfEntry(String entryUrl);

    /**
     * get the oldest comment of an entry
     * 
     * @param type comment type
     * @param entryUrl entry url
     * @return the latest comment of the entry or null if not found
     */
    public Comment getLatestCommentOfEntry(String entryUrl);

    /**
     * get the author list of the entry's comment
     * 
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @return
     */
    public List<Integer> getAuthorListByEntry(String entryId, int entryOwnerId);

    /**
     * get the comments list belonging to an entry.
     * 
     * @param type comment type
     * @param urlmd5
     * @param entryOwnerId entry owner's id
     * @param offset the start index in the query result
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @param userIds actor's friends list
     * @return a list of comments that matches the query conditions, or an empty
     *         list if nothing matched are found
     */
    public List<Comment> getFriendsCommentListByEntry(CommentType type,
        String urlmd5, int entryOwnerId, int offset, int limit,
        QueryOrder order, List<Integer> userIds);

    /**
     * 根据urlmd5获取全局评论数量
     * 
     * @param type
     * @param urlmd5
     * @return
     */
    public long getCommentCountByEntry(CommentType type, String urlmd5);

    /**
     * 根据urlmd5和userIds获取好友评论的数量
     * 
     * @param type
     * @param urlmd5
     * @param userIds
     * @return
     */
    public long getFriendCommentCountByEntry(CommentType type, String urlmd5,
        List<Integer> userIds);

    /**
     * 获取好友评论的最新的N条评论
     * 
     * @param type
     * @param entryId 实体ID
     * @param entryOwnerId 实体拥有者ID
     * @param urlmd5
     * @param userIds
     * @param limit 取多少条
     * @return
     */
    public List<Comment> getNLatestFriendCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, String urlmd5, List<Integer> userIds,
        int limit);

    /**
     * 获取好友评论的最老的N条评论
     * 
     * @param type
     * @param entryId TODO
     * @param entryOwnerId TODO
     * @param urlmd5
     * @param userIds
     * @param limit 取多少条
     * @return
     */
    public List<Comment> getNOldestFriendCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, String urlmd5, List<Integer> userIds,
        int limit);

    /**
     * 根据分享ID获取删除的评论
     * 
     * @param entryId
     * @param entryOwnerId
     * @return
     */
    public ShareDel getDeletedShareById(int entryOwnerId, long entryId);

}
