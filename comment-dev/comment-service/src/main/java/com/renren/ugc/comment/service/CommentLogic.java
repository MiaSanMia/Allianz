package com.renren.ugc.comment.service;

import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.FeedCommentInfo;
import com.renren.ugc.comment.model.FeedCommentResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * The interface for a comment logic which defines the basic operations
 * 
 * @author lvenle, jiankuan.xing
 */
public interface CommentLogic {

    /**
     * create a comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who creates this comment. This user will
     *        be considered as the author of this comment.
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param comment the comment to be created
     * @param strategy the operation context object
     * @return the created comment
     * @throws UGCCommentException
     */
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException;

    /**
     * get the comment list of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to get the comment list
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object, which contains the
     *        query border, limit and order
     * 
     * @return the comment list or an emply list if nothing is found
     * @throws UGCCommentException
     */
    public CommentListResult getList(CommentType type, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException;

    /**
     * get the comment list of an entry from global comments in the
     * url_comment table of share
     * 
     * @param commentType
     * @param actorId
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy
     * @return
     * @throws UGCCommentException
     */
    public CommentListResult getGlobalCommentList(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, String urlmd5, CommentStrategy strategy) throws UGCCommentException;

    /**
     * get the total number of comments belongs to an entry
     * 
     * @param commentType the biz type the comment belongs to
     * @param actorId the user id who wants to get the count
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the comments' count
     * @throws UGCCommentException
     */
    public long getCount(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException;
    
    /**
     * @param commentType commentType the biz type the comment belongs to
     * @param entryOwnerId the entry's owner id
     * @param urlmd5
     * @param strategy strategy the operation context object
     * @return
     * @throws UGCCommentException
     */
    public long getFriendCommentCount(CommentType commentType, int entryOwnerId, String urlmd5,
        CommentStrategy strategy) throws UGCCommentException;

    /**
     * get the total number of comments belongs to an entry in the
     * url_comment table of share
     * 
     * @param commentType the biz type the comment belongs to
     * @param urlmd5
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the comments' count
     * @throws UGCCommentException
     */
    public long getGlobalCount(CommentType commentType, int actorId, String urlmd5,
            CommentStrategy strategy) throws UGCCommentException;

    /**
     * remove a comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to remove the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be removed
     * @param strategy the operation context object
     * 
     * @return whether successfully removed
     * @throws UGCCommentException
     */
    public boolean remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException;
    
    /**
     * remove a global comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to remove the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be removed
     * @param strategy the operation context object
     * 
     * @return whether successfully removed
     * @throws UGCCommentException
     */
    public boolean removeGlobalComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException;

    /**
     * send friend notify
     *
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to remove the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be removed
     * @param strategy the operation context object
     *
     * @return whether successfully removed
     * @throws UGCCommentException
     */
    public boolean sendFriendNotify(CommentType type, int actorId, long entryId, int entryOwnerId,
                                       long commentId, CommentStrategy strategy) throws UGCCommentException;

    /**
     * 删除某个源的所有评论
     * 
     * @param commentType 业务类型 enum
     * @param keySpace 存储类型 enum，1，按源存储，2，按用户存储
     * @param host 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwner 源拥有者，比如日志entryOwner，相册entryOwner，分享entryOwner
     * @return
     * 
     */
    public boolean removeAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException;

    /**
     * recover a comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to recover the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be recovered
     * @param strategy the operation context object
     * 
     * @return whether successfully recovered
     * @throws UGCCommentException
     */
    public boolean recover(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException;

    /**
     * 恢复一个源的所有评论 undo delete all
     * 
     * @param type 业务类型 enum
     * @param keySpace 存储类型 enum，1，按源存储，2，按用户存储
     * @param host 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwner 源拥有者，比如日志entryOwner，相册entryOwner，分享entryOwner
     * @return
     * @throws UGCCommentException
     * 
     * @deprecated
     */
    public boolean recoverAll(CommentType type, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException;

    /**
     * get a comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to get the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be got
     * @param strategy the operation context object
     * 
     * @return the retrieved comment or null if not found
     * @throws UGCCommentException
     */
    public Comment get(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException;

    /**
     * get multiple comments by a collection of comment ids
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to get the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentIds the comment id list which to be got
     * @param strategy the operation context object
     * @return the map that key is the comment id while value is the
     *         comment object; if not found, return an empty map
     * @throws UGCCommentException
     */
    public Map<Long, Comment> getMulti(CommentType type, int actorId, long entryId,
            int entryOwnerId, List<Long> commentIds, CommentStrategy strategy)
            throws UGCCommentException;

    /**
     * update a comment
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to update the comment
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param commentId the comment id which to be updated
     * @param strategy the operation context object
     * 
     * @return whether successfully updated
     * @throws UGCCommentException
     */
    @Deprecated
    public boolean update(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy)
            throws UGCCommentException;

    /**
     * get the oldest comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the oldest comment of the entry or null if nothing is found
     * @throws UGCCommentException
     */
    public Comment getOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy,int actorId);

    /**
     * get the latest comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the latest comment of the entry or null if nothing is found
     * @throws UGCCommentException
     */
    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy,int actorId);

    /**
     * increase one voice comment's play count
     * 
     * 
     * @param type the biz type the comment belongs to
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param increment the play count number to be increased
     * @param strategy the operation context object
     * @return the total play count after add
     * @throws UGCCommentException
     */
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment, CommentStrategy strategy);

    /**
     * get the total numbers of comments belongs to an entry
     * 
     * @param commentType the biz type the comment belongs to
     * @param actorId the user id who wants to get the count
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the comments' count
     * @throws UGCCommentException
     */
    public Map<Long, Integer> getCountBatch(CommentType commentType, int actorId,
            List<Long> entryIds, int entryOwnerId, CommentStrategy strategy)
            throws UGCCommentException;

    /**
     * get the comment list of the actor's friends to an entry
     * 
     * @param type the biz type the comment belongs to
     * @param actorId the user id who wants to get the count
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return
     * @throws UGCCommentException
     */
    public CommentListResult getFriendsCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException;
    
    /**
     * get the n oldest comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the n oldest comment of the entry or null if nothing is found
     * @throws UGCCommentException
     */
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy);

    /**
     * get the latest comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param entryId the entry id
     * @param entryOwnerId the entry's owner id
     * @param strategy the operation context object
     * 
     * @return the n latest comment of the entry or null if nothing is found
     * @throws UGCCommentException
     */
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy);
    
    /**
     * get the latest friend comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param urlmd5
     * @param entryId the entry id
     * @param entryOwnerId  the entry's owner id
     * @param strategy the operation context object
     * @return the n latest comment of the entry or null if nothing is found
     */
    public List<Comment> getNLatestFriendCommentOfEntry(CommentType type, String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy);
    
    /**
     * get the oldest friend comment of an entry
     * 
     * @param type the biz type the comment belongs to
     * @param urlmd5
     * @param entryId TODO
     * @param entryOwnerId  the entry's owner id
     * @param strategy the operation context object
     * @return the n latest comment of the entry or null if nothing is found
     */
    public List<Comment> getNOldestFriendCommentOfEntry(CommentType type, String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy);
    
    
    public Map<Long,FeedCommentResult> getCommentsForFeed(CommentType type,List<FeedCommentInfo> infos, CommentStrategy strategy);
    
    public Map<Long,Integer> getCommentsForFeedCount(CommentType type,List<FeedCommentInfo> infos, CommentStrategy strategy);

    /**
     * get the count of friends' comments for feed
     * 
     * @param type
     * @param infos
     * @param strategy
     * @return
     */
    public Map<Long, Integer> getFriendCommentsForFeedCount(CommentType type, List<FeedCommentInfo> infos, CommentStrategy strategy);

    /**
     * get the comments of friends for feed
     * 
     * @param type
     * @param infos
     * @param strategy
     * @return
     */
    public Map<Long, FeedCommentResult> getFriendCommentsForFeed(CommentType type, List<FeedCommentInfo> infos, CommentStrategy strategy);
    
    
    /**
     * 获取已经评论过的好友id
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param urlmd5
     * @param type
     * @return
     */
    public List<Integer> getFriendsList(int actorId, long entryId, int entryOwnerId,
            String urlmd5, CommentType type);
    
    public List<Integer> filterAuthorByFrequency(CommentType type,int actorId, long entryId, int entryOwnerId,int toId,List<Integer> authors,CommentStrategy strategy);
    
    /**
     * 根据规则来取对应的评论
     * 
     * @param type
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     * @return
     */
    public CommentListResult getCommentListWithFilter(CommentType type, int actorId, long entryId, int entryOwnerId, int authorId, CommentStrategy strategy);
    
    /**
     * 根据规则返回一个总数
     * 
     * @param type
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @param strategy
     * @return
     */
    public long getCountWithFilter(CommentType type, int actorId, long entryId, int entryOwnerId, int authorId, CommentStrategy strategy);
    
    /**
     * 批量创建评论
     * 现阶段只有photo圈图评论会调用接口
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param commentList
     * @param strategy
     * @return
     * @throws UGCCommentException
     */
    public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
    		ForInvokeStrategy forInvokeStrategy) throws UGCCommentException;

}