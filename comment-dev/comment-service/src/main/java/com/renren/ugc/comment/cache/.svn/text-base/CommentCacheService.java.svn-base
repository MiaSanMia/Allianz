package com.renren.ugc.comment.cache;

import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.model.Comment;

/**
 * 标注:{@link CommentCacheService} 评论中心cache服务接口
 * 
 * @author xinyan.yang@renren-inc.com
 * 
 * @date 2013-1-30 11:31:59
 * 
 */
public interface CommentCacheService {

    /**
     * means the count cache doesn't exist
     */
    public static final long COUNT_CACHE_NOT_EXIST = -1;

    /**
     * get the total number of the comments belonging to an entry
     */
    public long getCount(long entryId, int entryOwnerId, int type);

    /**
     * increase total number of the comments belonging to an entry by
     * <code>step</code>
     * 
     * @return the increased count
     */
    public long incCount(long entryId, int entryOwnerId, int type, int step);

    /**
     * initialize the total number of the comments belonging to an entry to
     * <code>value</code>
     */
    public void setCount(long entryId, int entryOwnerId, int type, long value);

    /**
     * set the oldest comment of an entry to cache
     */
    public void setOldestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment);

    /**
     * get the oldest comment of an entry to cache
     */
    public Comment getOldestEntryCommentCache(long entryId, int entryOwnerId, int type);

    /**
     * set the latest comment of an entry to cache
     */
    public void setLatestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment);

    /**
     * get the latest comment of an entry to cache
     */
    public Comment getLatestEntryCommentCache(long entryId, int entryOwnerId, int type);

    /**
     * get the comment list cache belonging to an entry
     */
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit);

    /**
     * get the comment list cache belonging to an entry (with border
     * comment id)
     */
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit, long borderCommentId);

    /**
     * set the comment list cache belonging to an entry
     */
    public void setCommentListByEntryCache(long entryId, int entryOwnerId, int type, String order,
            int limit, List<Comment> commentList);

    /**
     * set the comment list cache belonging to an entry (with border
     * comment id)
     */
    public void setCommentListByEntryCache(long entryId, int entryOwnerId, int type, String order,
            int limit, long borderCommentId, List<Comment> commentList);

    /**
     * remove all the cache belonging to an entry (except the count cache)
     */
    public void removeCacheByEntry(long entryId, int entryOwnerId, int type);
    
    /**
     * get the total number of the comments belonging to an entry
     */
    public Map<Long,Integer> getCountBatch(List<Long> entryIds, int entryOwnerId, int type);
    
    /**
     * set the comment list cache belonging to an urlmd5
     */
    public void setGlobalListByEntryCache(String urlMd5, String order,
            int limit, List<Comment> commentList);
    
    /**
     * remove all the cache belonging to an urlmd5 (except the count cache)
     */
    public void removeGlobalListCacheByEntry(String urlmd5);
    
    /**
     * increase total number of the comments belonging to an urlmd5 by
     * <code>step</code>
     * 
     * @return the increased count
     */
    public long incGlobalCount(String urlmd5, int step);
    
    /**
     * get the total number of the comments belonging to an urlmd5
     */
    public long getGlobalCount(String urlmd5);
    
    /**
     * initialize the total number of the comments belonging to an urlmd5 to
     * <code>value</code>
     */
    public void setGlobalCount(String urlmd5, long value);
    
    /**
     * get the comment list cache belonging to an entry
     */
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5,
            String order, int limit);
    
    /**
     * get the comment list cache belonging to an entry
     */
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5,
            String order, int limit,long borderId);
    
    /**
     * get the comment list of the actor's friends cache belonging to an entry
     * @param urlMd5
     * @param actorId
     * @param order
     * @param limit
     * @param borderId
     * @return
     */
    public List<Comment> getFriendCommentListByEntryCache(String urlMd5, int actorId, String order, int limit, long borderId);
    
    /**
     * set the comment list of the actor's friends cache belonging to an entry
     * 
     * @param urlMd5
     * @param actorId
     * @param order
     * @param limit
     * @param commentList
     */
    public void setFriendCommentListByEntryCache(String urlMd5, int actorId, String order,
            int limit, List<Comment> commentList);
    
    /**
     * remove the comment list of the actor's friends cache belonging to an entry
     * 
     * @param urlmd5
     * @param actorId
     */
    public void removeFriendListCacheByEntry(String urlmd5, int actorId);
    
}
