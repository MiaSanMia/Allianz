package com.renren.ugc.comment.cache;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;

/**
 * The cache service will cache all the comment data to tripod cache.<br/>
 * 
 * For caching the query result with the paging parameters, you have to
 * append the paging parameters part of the cache key. However, when remove
 * the cache, you have no idea how many paging caches have been created
 * because you can't construct the key since you have no paging parameters
 * now.<br/>
 * 
 * This cache service resolves this problem by setting up 2-level cache.
 * The master cache stores an increasing value (time stamp in
 * implementation) for the comment list of an entry. The format of the
 * master cache key is "prefix_type_entryId_entryOwnerId".<br/>
 * 
 * The sub cache stores the actual query result. Its key format is
 * "prefix_type_entryId_entryOwnerId_seq_order_limit_borderId". The "ts" in
 * the key is just the value of the master cache<br/>
 * 
 * When creating the cache, first get the value of master cache (create a
 * new one if it doesn't exist), then use the retrieved seq to construct
 * the sub cache key. When fetching the sub cache, first get the value of
 * master cache, if succeed, use the seq to construct the sub cache key
 * then get it; otherwise, consider the cache doesn't exist. When removing
 * the cache, simply put a new time stamp to the master key.<br/>
 * 
 * With this way, all the query result can be cached. However, the cost is
 * that each cache fetching has to invoke tripod cache twice.
 * 
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentAllCacheService implements CommentCacheService {

    private static CommentCacheService instance = new CommentAllCacheService();

    //TODO........异步缓存,待压测后看性能瓶颈
    //private AsyncTaskDispatcher<CommentTaskModel> dispatcher;

    private static Logger logger = Logger.getLogger(CommentAllCacheService.class);

    private TripodCacheMgr cacheMgr = TripodCacheMgr.getInstance();

    private CommentAllCacheService() {
        //TODO will add async cache support
        //dispatcher = new AsyncTaskDispatcher<CommentTaskModel>(new PersistTaskHandler(), 10000,
        //        true, 100, 8);
    }

    public static CommentCacheService getInstance() {
        return instance;
    }

    /**
     * 创建entry的主Cache
     */
    private void setCommentEntryMainCache(long entryId, int entryOwnerId, int type, long ts) {
        String key = KeyGen.getMasterEntryCacheKey(type, entryId, entryOwnerId);
        cacheMgr.setCache(key, new Long(ts));
    }

    /**
     * 删除entry的主Cache
     */
    @Override
    public void removeCacheByEntry(long entryId, int entryOwnerId, int type) {
        String key = KeyGen.getMasterEntryCacheKey(type, entryId, entryOwnerId);
        cacheMgr.setCache(key, new Long(System.currentTimeMillis()));
    }

    /**
     * 获取entry的主Cache
     */
    private Long getCommentEntryMainCache(long entryId, int entryOwnerId, int type) {
        String key = KeyGen.getMasterEntryCacheKey(type, entryId, entryOwnerId);
        return (Long) cacheMgr.getCache(key);
    }

    /**
     * 创建entry的主Cache
     */
    @Override
    public void setCommentListByEntryCache(long entryId, int entryOwnerId, int type, String order,
            int limit, List<Comment> commentList) {
        setCommentListByEntryCache(entryId, entryOwnerId, type, order, limit, 0, commentList);

    }

    /**
     * 保存一个entry的页评论列表的缓存
     * 
     */
    @Override
    public void setCommentListByEntryCache(long entryId, int entryOwnerId, int type, String order,
            int limit, long borderCommentId, List<Comment> commentList) {
        if (commentList == null) {
            return;
        }

        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        long tsValue;
        if (ts == null) {
            tsValue = System.currentTimeMillis();
        } else {
            tsValue = ts.longValue();
        }

        String dataKey = KeyGen.getEntryListCacheKey(entryId, entryOwnerId, type, tsValue, order,
                limit, borderCommentId);
        cacheMgr.setCache(dataKey, commentList);
        if (ts == null) {
            setCommentEntryMainCache(entryId, entryOwnerId, type, tsValue);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("create comment list cache, key:" + dataKey + ", count of comments:"
                    + commentList.size());
        }
    }

    /**
     * 得到一个entry的评论列表. 如果cache不存在，返回null
     * 
     */
    @Override
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit) {
        return getCommentListByEntryCache(entryId, entryOwnerId, type, order, limit, 0);
    }

    /**
     * 得到一个entry的分页评论列表. 如果cache不存在，返回null
     * 
     */
    @Override
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit, long borderCommentId) {

        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        if (ts == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("the main entry cache doesn't exist, ignore to fetch query cache");
            }
            return null;
        }

        String dataKey = KeyGen.getEntryListCacheKey(entryId, entryOwnerId, type, ts.longValue(),
                order, limit, borderCommentId);
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) cacheMgr.getCache(dataKey);

        if (logger.isDebugEnabled()) {
            if (commentList != null) {
                logger.debug("the comment list cache is found by key:" + dataKey
                        + ", count of comments:" + commentList.size());
            } else {
                logger.debug("the comment list cache cannot be found by key:" + dataKey);
            }

        }
        return commentList;
    }

    /**
     * 保存最新的一条评论cache
     * 
     */
    @Override
    public void setLatestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment) {
        if (comment == null) {
            return;
        }

        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        long tsValue;
        if (ts == null) {
            tsValue = System.currentTimeMillis();
        } else {
            tsValue = ts.longValue();
        }

        String latestKey = KeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type,
                tsValue);
        cacheMgr.setCache(latestKey, comment);
        if (ts == null) {
            setCommentEntryMainCache(entryId, entryOwnerId, type, tsValue);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the latest comment for key:" + latestKey
                    + ", comment id:" + comment.getId());
        }
    }

    /**
     * 从Cache中获取最新一条评论的缓存，如果Cache不存在则返回null
     * 
     */
    @Override
    public Comment getLatestEntryCommentCache(long entryId, int entryOwnerId, int type) {
        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        if (ts == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("the main entry cache doesn't exist, ignore to fetch query cache");
            }
            return null;
        }

        String key = KeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type,
                ts.longValue());
        Comment comment = (Comment) cacheMgr.getCache(key);
        if (logger.isDebugEnabled()) {
            if (comment == null) {
                logger.debug("the latest comment cache for key " + key + " doesn't exists");
            } else {
                logger.debug("the latest comment cache for key " + key + " is fetched, comment id:"
                        + comment.getId());
            }
        }
        return comment;
    }

    /**
     * 保存最老的一条评论cache
     * 
     */
    @Override
    public void setOldestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment) {
        if (comment == null) {
            return;
        }

        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        long tsValue;
        if (ts == null) {
            tsValue = System.currentTimeMillis();
        } else {
            tsValue = ts.longValue();
        }

        String latestKey = KeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type,
                tsValue);
        cacheMgr.setCache(latestKey, comment);

        if (ts == null) {
            setCommentEntryMainCache(entryId, entryOwnerId, type, tsValue);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the oldest comment for key:" + latestKey
                    + ", comment id:" + comment.getId());
        }
    }

    /**
     * 从Cache中获取最新一条评论的缓存，如果Cache不存在则返回null，如果Cache不存在则返回null
     * 
     */
    @Override
    public Comment getOldestEntryCommentCache(long entryId, int entryOwnerId, int type) {
        Long ts = getCommentEntryMainCache(entryId, entryOwnerId, type);
        if (ts == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("the main entry cache doesn't exist, ignore to fetch query cache");
            }
            return null;
        }

        String key = KeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type,
                ts.longValue());
        Comment comment = (Comment) cacheMgr.getCache(key);
        if (logger.isDebugEnabled()) {
            if (comment == null) {
                logger.debug("the oldest comment cache for key " + key + " doesn't exists");
            } else {
                logger.debug("the oldest comment cache for key " + key + " is fetched, comment id:"
                        + comment.getId());
            }
        }
        return comment;
    }

    /**
     * 读取cache计数器
     * 
     */
    @Override
    public long getCount(long entryId, int entryOwnerId, int type) {
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long count = cacheMgr.getCounter(key);
        if (count == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't fetch cached count for key " + key);
            }
            return CommentCacheService.COUNT_CACHE_NOT_EXIST;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("cached count for key " + key + " is fetched, " + count);
            }
            return count;
        }
    }

    /**
     * 增加cache计数器值
     * 
     */
    @Override
    public long incCount(long entryId, int entryOwnerId, int type, int step) {
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long count = cacheMgr.incCounter(key, step);
        if (count == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("can't inc cached count for key " + key);
            }
            return CommentCacheService.COUNT_CACHE_NOT_EXIST;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("cached count for key " + key + " is increased, now is " + count);
            }
            return count;
        }
    }

    /**
     * 初始化cache计数器值
     */
    @Override
    public void setCount(long entryId, int entryOwnerId, int type, long value) {
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        if (logger.isDebugEnabled()) {
            logger.debug("set count " + key + " to count " + value);
        }
        cacheMgr.setCounter(key, value);
    }

    //TODO........异步缓存,待压测后看性能瓶颈
    private class PersistTaskHandler implements ITaskHandler<CommentTaskModel> {

        @Override
        public void handle(List<CommentTaskModel> tasks) {
            for (CommentTaskModel taskModel : tasks) {
                cacheMgr.setCache(taskModel.getKey(), taskModel.getValue());
            }
        }
    }

    /**
     * Cache key generator for this cache service
     * 
     * @author jiankuan.xing
     * 
     */
    private static final class KeyGen extends BaseCacheKeyGenerator {

        private static final String MAIN_ENTRY_CACHE_KEY_PREFIX = "cs";

        /**
         * 产生一个Entry的主key. 格式: [prefix]_[entryId]_[entryOwnerId]
         * 
         */
        public static String getMasterEntryCacheKey(long entryId, long entryOwnerId, int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(MAIN_ENTRY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            return sb.toString();
        }

        /**
         * 产生缓存“entry评论列表”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_[ts
         * ]_[order]_[limit]_[borderId]
         * 
         * @param borderCommentId 若为0,表示从第一条评论开始查询的列表
         */
        public static String getEntryListCacheKey(long entryId, long entryOwnerId, int type,
                long ts, String order, int limit, long borderCommentId) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(ts);
            sb.append(SEPARATOR);
            sb.append(order);
            sb.append(SEPARATOR);
            sb.append(limit);
            sb.append(SEPARATOR);
            sb.append(borderCommentId);
            return sb.toString();
        }

        /**
         * 产生缓存“最旧一条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_[ts]_oc
         */
        public static String getOldestCommentOfEntryCacheKey(long entryId, long entryOwnerId,
                int type, long ts) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(ts);
            sb.append(SEPARATOR);
            sb.append(OLDEST_COMMENT_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生缓存“最新一条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_[ts]_lc
         */
        public static String getLatestCommentOfEntryCacheKey(long entryId, long entryOwnerId,
                int type, long ts) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(ts);
            sb.append(SEPARATOR);
            sb.append(LATEST_COMMENT_SUFFIX);

            return sb.toString();
        }
    }

    @Override
    public Map<Long, Integer> getCountBatch(List<Long> entryIds, int entryOwnerId, int type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGlobalListByEntryCache(String urlMd5, String order, int limit,
            List<Comment> commentList) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeGlobalListCacheByEntry(String urlmd5) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long incGlobalCount(String urlmd5, int step) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getGlobalCount(String urlmd5) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setGlobalCount(String urlmd5, long value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5, String order, int limit) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5, String order, int limit,
            long borderId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#getFriendCommentListByEntryCache(java.lang.String, int, java.lang.String, int, long)
     */
    @Override
    public List<Comment> getFriendCommentListByEntryCache(String urlMd5,
        int actorId, String order, int limit, long borderId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#setFriendCommentListByEntryCache(java.lang.String, int, java.lang.String, int, java.util.List)
     */
    @Override
    public void setFriendCommentListByEntryCache(String urlMd5, int actorId,
        String order, int limit, List<Comment> commentList) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#removeFriendListCacheByEntry(java.lang.String, int)
     */
    @Override
    public void removeFriendListCacheByEntry(String urlmd5, int actorId) {
        // TODO Auto-generated method stub
        
    }
}
