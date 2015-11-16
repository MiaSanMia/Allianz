package com.renren.ugc.comment.service;

import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * The comment storage engine interface. This interface is designed to
 * migrate Mongo DB to MySQL.
 * 
 * @author jiankuan.xing
 * 
 */
public interface CommentStorageService {

    /**
     * create a comment
     */
    public void create(CommentType type, Comment comment, String metadata);

    /**
     * remove a comment
     * 
     * @return the removed Comment
     */
    public Comment remove(int actorId, long entryId, int entryOwnerId, long commentId,
            CommentType commentType,CommentStrategy strategy);

    /**
     * recover a comment
     * 
     * @return whether recovered succeeds
     */
    public boolean recover(long entryId, int entryOwnerId, long commentId);

    /**
     * Get a comment with specified Id. Note all the deleted comment won't
     * be returned.<br>
     * 
     * This API would be put to public due to the MySQL table is sherded by
     * entryId
     * 
     * @return the comment with <code>commentId</code>
     */
    public Comment get(long entryId, int entryOwnerId, long commentId);

    /**
     * Get multiple comments by comment id list
     */
    public Map<Long, Comment> getMulti(long entryId, int entryOwnerId, List<Long> commentIds);

    /**
     * get the comments list belonging to an entry.
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner's id
     * @param borderCommentId the comment where the query starts. Comment
     *        with this id will be returned in the result list.
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @return a list of comments that matches the query conditions, or an
     *         empty list if nothing matched are found
     */
    public List<Comment> getListByEntry(CommentType type, long entryId, int entryOwnerId,
            long borderCommentId, int limit, QueryOrder order);

    /**
     * get the comments list belonging to an entry (with non-trivial
     * offset) warning!! this method is only used to support old pageNum
     * based comment list query. Any new biz who's gonna use comment center
     * should not use this kind of query
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner's id
     * @param offset the start index in the query result
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @return a list of comments that matches the query conditions, or an
     *         empty list if nothing matched are found
     */
    public List<Comment> getListByEntry(CommentType type, long entryId, int entryOwnerId,
            int offset, int limit, QueryOrder order);

    /**
     * get the oldest comment of an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @return the oldest comment of the entry or null if not found
     */
    public Comment getOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId);

    /**
     * get the oldest comment of an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @return the latest comment of the entry or null if not found
     */
    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId);

    /**
     * get the total number of comments that belongs to an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @return the total number of comments that belongs to an entry
     */
    public long getCommentCountByEntry(CommentType type, long entryId, int entryOwnerId);

    /**
     * update the comment's content
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @param commentId the comment id which to be updated
     * @param newContent new content
     */
    public boolean updateContent(CommentType type, long entryId, int entryOwnerId, long commentId,
            String newContent);

    /**
     * increase a voice comment's play count
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @param commentId the comment id which to be updated
     * @param increment the increment count to be added
     * @return the comment play count after add
     */
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment);

    /**
     * batch get the total number of comments that belongs to an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @return the total number of comments that belongs to an entry
     */
    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type, List<Long> entryId,
            int entryOwnerId);

    /**
     * remove all comment
     * 
     * @return whether delete succeeds
     */
    public boolean removeAll(int actorId, long entryId, int entryOwnerId, CommentType commentType, CommentStrategy strategy);

    /**
     * get the author list of the entry's comment
     * 
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @param includeGlobalComments
     * @param commentType
     * @return
     */
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId,
            boolean includeGlobalComments, CommentType commentType);

    /**
     * get the comments list belonging to an entry.
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner's id
     * @param includeGlobalComments
     * @param offset the start index in the query result
     * @param limit the max number of returned
     * @param order the result list order, ASC (oldest to latest) or DESC
     *        (latest to oldest)
     * @param userIds actor's friends list
     * @return a list of comments that matches the query conditions, or an
     *         empty list if nothing matched are found
     */
    public List<Comment> getFriendsCommentListByEntry(CommentType type, String entryId,
            int entryOwnerId, boolean includeGlobalComments, int offset, int limit,
            QueryOrder order, List<Integer> userIds);
    
    /**
     * get the N oldest comment of an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @param limit N
     * @return the n oldest comments of the entry or null if not found
     */
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit);

    /**
     * get the N newest comment of an entry
     * 
     * @param type comment type
     * @param entryId entry id
     * @param entryOwnerId entry owner id
     * @param limit N
     * @return the n latest comments of the entry or null if not found
     */
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit);
   
    /**
     * 根据评论作者过滤要显示的评论
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @param order
     * @param offset
     * @param limit
     * @return
     */
	public List<Comment> getCommentListByAuthorIdWithOffset(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			int offset, int limit);
    /**
     * 返回一个author的评论总数
     * 
     * @param type
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @return
     */
	public long getCountByAuthorId(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId);
	
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type, long entryId, int entryOwnerId,
            long borderCommentId, int limit, QueryOrder order,List<Integer> authorIds,List<Integer> toUserIds);

    public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type, long entryId, int entryOwnerId,
            int offset, int limit, QueryOrder order,List<Integer> authorIds,List<Integer> toUserIds);
    
    public Comment getOldestCommentByAuthorIdAndToUserId(CommentType type, long entryId, int entryOwnerId,List<Integer> authorIds,List<Integer> toUserIds);

    public Comment getLatestCommentByAuthorIdAndToUserId(CommentType type, long entryId, int entryOwnerId,List<Integer> authorIds,List<Integer> toUserIds);
    
    public long getCommentCountByEntry(CommentType type, long entryId, int entryOwnerId,List<Integer> authorIds,
			List<Integer> toUserIds);
    
    public List<Comment> getCommentListByAuthorIdWithBorderId(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			long borderId, int limit);
    
    public long getEntryId(CommentType type, int entryOwnerId, long commentId);

}
