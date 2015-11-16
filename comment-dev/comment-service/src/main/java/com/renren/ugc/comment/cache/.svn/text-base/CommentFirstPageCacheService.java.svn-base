package com.renren.ugc.comment.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.QueryOrder;

/**
 * The cache service will cache the first page of the comment data to
 * tripod cache.<br/>
 * 
 * For caching the query result with the paging parameters, you have to
 * append the paging parameters part of the cache key. However, when remove
 * the cache, you have no idea how many paging caches have been created
 * because you can't construct the key since you have no paging parameters
 * now.<br/>
 * 
 * This cache service will only cache the first page comment list, that is
 * borderCommentId always is 0 while limit is page number. The default
 * comment number for a page is 61. You can change it by specifying an
 * integer to the property "comment.cache.count".
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentFirstPageCacheService implements CommentCacheService {

    /**
     * the page count that this cache service support
     */
    public static final int PAGE_COUNT = Integer.valueOf(System.getProperty("comment.cache.count",
            "31"));
    
    private static final int FRIEND_COMMENT_EXPIRE_TIME = 300;

    private static CommentFirstPageCacheService instance = new CommentFirstPageCacheService();

    public static CommentCacheService getInstance() {
        return instance;
    }

    private static Logger logger = Logger.getLogger(CommentFirstPageCacheService.class);

    private TripodCacheMgr cacheMgr = TripodCacheMgr.getInstance();

    @Override
    public long getCount(long entryId, int entryOwnerId, int type) {
        long start = System.nanoTime();
        boolean miss = false;
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long value = cacheMgr.getCounter(key);
        if (value == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("get count cache fails, key = " + key);
            }
            miss = true;
            value = CommentCacheService.COUNT_CACHE_NOT_EXIST;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("get count cache, key=%s, value=%d", key, value));
        }
        
        long end = System.nanoTime();
        StatisticsHelper.invokeGetCountFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);

        return value;
    }

    @Override
    public long incCount(long entryId, int entryOwnerId, int type, int step) {
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);

        long value = cacheMgr.incCounter(key, step);
        if (value == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("increase count cache fails, key = " + key);
            }
            return CommentCacheService.COUNT_CACHE_NOT_EXIST;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, step=%d, value=%d", key,
                    step, value));
        }
        return value;
    }

    @Override
    public void setCount(long entryId, int entryOwnerId, int type, long value) {
        String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("set count cache for key=%s, value=%d", key, value));
        }
        cacheMgr.setCounter(key, value);
    }

    @Override
    public void setOldestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment) {
        String key = KeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the oldest comment for key:" + key + ", comment id:"
                    + comment.getId());
        }
        cacheMgr.setCache(key, comment);
    }

    @Override
    public Comment getOldestEntryCommentCache(long entryId, int entryOwnerId, int type) {
        String key = KeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
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

    @Override
    public void setLatestEntryCommentCache(long entryId, int entryOwnerId, int type, Comment comment) {
        String key = KeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the latest comment for key:" + key + ", comment id:"
                    + comment.getId());
        }
        cacheMgr.setCache(key, comment);
    }

    @Override
    public Comment getLatestEntryCommentCache(long entryId, int entryOwnerId, int type) {
        String key = KeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit) {
        long start = System.nanoTime();
        boolean miss = false;
        
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't get comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return null;
        }
        String key = KeyGen.getEntryListCacheKey(entryId, entryOwnerId, type, order, limit);
        List<Comment> commentList = (List<Comment>) cacheMgr.getCache(key);
        if (logger.isDebugEnabled()) {
            if (commentList != null) {
                logger.debug("the comment list cache is found by key:" + key
                        + ", count of comments:" + commentList.size());
            } else {
                logger.debug("the comment list cache cannot be found by key:" + key);
            }

        }
        
        if (commentList == null) {
            miss = true;
        }
        long end = System.nanoTime();
        //for debug
       long cost = (end - start) / StatisticsHelper.NANO_TO_MILLIS;
        if(cost > 2000){
        	logger.error("first page cache too long,cost = " + cost + "key = " + key);
        }
        StatisticsHelper.invokeGetFirstPageFromCache(cost, miss);
        return commentList;
    }

    @Override
    public List<Comment> getCommentListByEntryCache(long entryId, int entryOwnerId, int type,
            String order, int limit, long borderCommentId) {
        if (borderCommentId != 0) {
            logger.warn(String.format(
                    "Can't get comment list cache because the borderId doesn't equals to 0, borderId=%d",
                    borderCommentId));
            return null;
        }
        return getCommentListByEntryCache(entryId, entryOwnerId, type, order, limit);
    }

    @Override
    public void setCommentListByEntryCache(long entryId, int entryOwnerId, int type, String order,
            int limit, List<Comment> commentList) {
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't create comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return;
        }
        String key = KeyGen.getEntryListCacheKey(entryId, entryOwnerId, type, order, limit);
        if (logger.isDebugEnabled()) {
            logger.debug("create comment list cache, key:" + key + ", count of comments:"
                    + commentList.size());
        }
        cacheMgr.setCache(key, commentList);
    }

    @Override
    public void setCommentListByEntryCache(final long entryId, final int entryOwnerId,
            final int type, final String order, final int limit, final long borderCommentId,
            final List<Comment> commentList) {
        if (borderCommentId != 0) {
            logger.warn(String.format(
                    "Can't create comment list cache because the borderId doesn't equals to 0, borderId=%d",
                    borderCommentId));
            return;
        }
        setCommentListByEntryCache(entryId, entryOwnerId, type, order, limit, commentList);
        //modified by wangxx,不能变成异步的，变成异步的后果可能是cache中的存的数据为"替换表情"后的内容
//        AsynJobService.asynRun(new Runnable() {
//
//            @Override
//            public void run() {
//                setCommentListByEntryCache(entryId, entryOwnerId, type, order, limit, commentList);
//            }
//        });
    }

    @Override
    public void removeCacheByEntry(long entryId, int entryOwnerId, int type) {
        String[] keys = KeyGen.getAllKeysByEntry(entryId, entryOwnerId, type);
        if (logger.isDebugEnabled()) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(
                        "remove all the caches for entry, entryId=%d, entryOwnerId=%d, type=%d",
                        entryId, entryOwnerId, type));
            }
        }
        for (String key : keys) {
            cacheMgr.removeCache(key);
        }
    }

    /**
     * The key generator for this cache service
     * 
     * @author jiankuan.xing
     * 
     */
    private static class KeyGen extends BaseCacheKeyGenerator {

        /**
         * 产生缓存“entry评论列表”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_[order]_[limit]
         * 
         * @param borderCommentId 若为0,表示从第一条评论开始查询的列表
         */
        public static String getEntryListCacheKey(long entryId, long entryOwnerId, int type,
                String order, int limit) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(order);
            sb.append(SEPARATOR);
            sb.append(limit);

            return sb.toString();
        }

        /**
         * 产生缓存“entry全局评论列表”的key. 格式: [prefix]_[urmMd5]_[order]_[limit]
         * 
         */
        public static String getGlobalEntryListCacheKey(String urlMd5, String order, int limit) {
            StringBuilder sb = new StringBuilder();
            sb.append(GLOBAL_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(urlMd5);
            sb.append(SEPARATOR);
            sb.append(order);
            sb.append(SEPARATOR);
            sb.append(limit);

            return sb.toString();
        }
        
        /**
         * 产生缓存“entry好友评论列表”的key. 格式: [prefix]_[urmMd5]_[actorId]_[order]_[limit]
         * 
         */
        public static String getFriendEntryListCacheKey(String urlMd5, int actorId, String order, int limit) {
            StringBuilder sb = new StringBuilder();
            sb.append(FRIEND_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(urlMd5);
            sb.append(SEPARATOR);
            sb.append(actorId);
            sb.append(SEPARATOR);
            sb.append(order);
            sb.append(SEPARATOR);
            sb.append(limit);

            return sb.toString();
        }

        /**
         * 产生缓存“最旧一条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_oc
         */
        public static String getOldestCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(OLDEST_COMMENT_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生缓存“最新一条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_lc
         */
        public static String getLatestCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(LATEST_COMMENT_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生评论列表缓存的所有key
         */
        public static String[] getAllKeysByEntry(long entryId, int entryOwnerId, int type) {
            String[] keys = new String[4];
            keys[0] = getEntryListCacheKey(entryId, entryOwnerId, type, QueryOrder.ASC.toString(),
                    PAGE_COUNT);
            keys[1] = getEntryListCacheKey(entryId, entryOwnerId, type, QueryOrder.DESC.toString(),
                    PAGE_COUNT);
            keys[2] = getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
            keys[3] = getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type);

            return keys;
        }

        /**
         * 产生好友评论列表缓存的所有key
         */
        public static String[] getAllKeysByFriendEntry(String urlmd5, int actorId) {
            String[] keys = new String[2];
            keys[0] = getFriendEntryListCacheKey(urlmd5, actorId, QueryOrder.ASC.toString(), PAGE_COUNT);
            keys[1] = getFriendEntryListCacheKey(urlmd5, actorId, QueryOrder.DESC.toString(), PAGE_COUNT);

            return keys;
        }
        
        /**
         * 产生全局评论列表缓存的所有key
         */
        public static String[] getAllKeysByGlobalEntry(String urlmd5) {
            String[] keys = new String[2];
            keys[0] = getGlobalEntryListCacheKey(urlmd5, QueryOrder.ASC.toString(), PAGE_COUNT);
            keys[1] = getGlobalEntryListCacheKey(urlmd5, QueryOrder.DESC.toString(), PAGE_COUNT);

            return keys;
        }
    }

    @Override
    public Map<Long, Integer> getCountBatch(List<Long> entryIds, int entryOwnerId, int type) {
        long start = System.nanoTime();
        boolean miss = false;
        List<String> keys = new ArrayList<String>(entryIds.size());
        for (Long entryId : entryIds) {
            keys.add(KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type));
        }
        Map<String, Long> results = cacheMgr.getCounterBatch(keys);
        Map<Long, Integer> countResults = null;
        if (results == null) {
            miss = true;
            if (logger.isDebugEnabled()) {
                logger.debug("getCountBatch fails, entryOwnerId = " + entryOwnerId);
            }
        } else {

            countResults = new HashMap<Long, Integer>();
    
            for (Long entryId : entryIds) {
                String key = KeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
                if (results.get(key) != null) {
                    try {
                        Integer count = results.get(key).intValue();
                        countResults.put(entryId, count);
                    } catch (Exception e) {
                        logger.error("getCountBatch error|key:" + key + "|value:" + results.get(key));
                    }
                }
            }
        }
        
        long end = System.nanoTime();
        StatisticsHelper.invokeGetCountBatchFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);

        return countResults;
    }

    @Override
    public void setGlobalListByEntryCache(String urlMd5, String order, int limit,
            List<Comment> commentList) {
        if (StringUtils.isEmpty(urlMd5)) {
            return;
        }
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't create comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return;
        }
        String key = KeyGen.getGlobalEntryListCacheKey(urlMd5, order, limit);
        if (logger.isDebugEnabled()) {
            logger.debug("create comment list cache, key:" + key + ", count of comments:"
                    + commentList.size());
        }
        cacheMgr.setCache(key, commentList);
    }

    @Override
    public void removeGlobalListCacheByEntry(String urlmd5) {
        if (StringUtils.isEmpty(urlmd5)) {
            return;
        }
        String[] keys = KeyGen.getAllKeysByGlobalEntry(urlmd5);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("remove all the caches for entry, urlmd5=%s", urlmd5));
        }

        for (String key : keys) {
            cacheMgr.removeCache(key);
        }
    }

    @Override
    public long incGlobalCount(String urlmd5, int step) {
        String key = KeyGen.genCountEntryCacheKey(urlmd5);
        if (StringUtils.isEmpty(urlmd5)) {
            return TripodCacheMgr.ERROR_COUNT;
        }
        long value = cacheMgr.incCounter(key, step);
        if (value == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("increase count cache fails, key = " + key);
            }
            return CommentCacheService.COUNT_CACHE_NOT_EXIST;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, step=%d, value=%d", key,
                    step, value));
        }
        return value;
    }

    @Override
    public long getGlobalCount(String urlmd5) {
        if (StringUtils.isEmpty(urlmd5)) {
            return TripodCacheMgr.ERROR_COUNT;
        }
        long start = System.nanoTime();
        boolean miss = false;
        
        String key = KeyGen.genCountEntryCacheKey(urlmd5);
        long value = cacheMgr.getCounter(key);
        if (value == TripodCacheMgr.ERROR_COUNT) {
            if (logger.isDebugEnabled()) {
                logger.debug("get global count cache fails, key = " + key);
            }
            miss = true;
            value = CommentCacheService.COUNT_CACHE_NOT_EXIST;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("get global count cache, key=%s, value=%d", key, value));
        }

        long end = System.nanoTime();
        StatisticsHelper.invokeGetGlobalCountFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);

        return value;
    }

    @Override
    public void setGlobalCount(String urlmd5, long value) {
        String key = KeyGen.genCountEntryCacheKey(urlmd5);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("set count cache for key=%s, value=%d", key, value));
        }
        cacheMgr.setCounter(key, value);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5,
            String order, int limit) {

        long start = System.nanoTime();
        boolean miss = false;
        
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't get comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return null;
        }
        String key = KeyGen.getGlobalEntryListCacheKey(urlMd5, order, limit);
        List<Comment> commentList = (List<Comment>) cacheMgr.getCache(key);
        if (logger.isDebugEnabled()) {
            if (commentList != null) {
                logger.debug("the comment list cache is found by key:" + key
                        + ", count of comments:" + commentList.size());
            } else {
                logger.debug("the comment list cache cannot be found by key:" + key);
            }

        }
        
        if (commentList == null) {
            miss = true;
        }
        
        long end = System.nanoTime();
        StatisticsHelper.invokeGetGlobalFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);
        
        return commentList;
    }
    
    @Override
    public List<Comment> getGlobalCommentListByEntryCache(String urlMd5,
            String order, int limit, long borderCommentId) {
        if (borderCommentId != 0) {
            logger.warn(String.format(
                    "Can't get comment list cache because the borderId doesn't equals to 0, borderId=%d",
                    borderCommentId));
            return null;
        }
        return getGlobalCommentListByEntryCache(urlMd5,order, limit);
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#getFriendCommentListByEntryCache(java.lang.String, int, java.lang.String, int, long)
     */
    @Override
    public List<Comment> getFriendCommentListByEntryCache(String urlMd5,
        int actorId, String order, int limit, long borderId) {
        if (borderId != 0) {
            logger.warn(String.format(
                    "Can't get comment list cache because the borderId doesn't equals to 0, borderId=%d",
                    borderId));
            return null;
        }
        
        long start = System.nanoTime();
        boolean miss = false;
        
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't get comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return null;
        }
        String key = KeyGen.getFriendEntryListCacheKey(urlMd5, actorId, order, limit);
        List<Comment> commentList = (List<Comment>) cacheMgr.getCache(key);
        if (logger.isDebugEnabled()) {
            if (commentList != null) {
                logger.debug("the comment list cache is found by key:" + key
                        + ", count of comments:" + commentList.size());
            } else {
                logger.debug("the comment list cache cannot be found by key:" + key);
            }

        }
        
        if (commentList == null) {
            miss = true;
        }
        
        long end = System.nanoTime();
        StatisticsHelper.invokeGetGlobalFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);
        
        return commentList;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#setFriendCommentListByEntryCache(java.lang.String, int, java.lang.String, int, java.util.List)
     */
    @Override
    public void setFriendCommentListByEntryCache(String urlMd5, int actorId,
        String order, int limit, List<Comment> commentList) {
        if (StringUtils.isEmpty(urlMd5) || 0 == actorId) {
            return;
        }
        if (limit != PAGE_COUNT) {
            logger.warn(String.format(
                    "Can't create comment list cache because the limit doesn't equals to PAGE_COUNT, limit=%d, page_count=%d",
                    limit, PAGE_COUNT));
            return;
        }
        String key = KeyGen.getFriendEntryListCacheKey(urlMd5, actorId, order, limit);
        if (logger.isDebugEnabled()) {
            logger.debug("create comment list cache, key:" + key + ", count of comments:"
                    + commentList.size());
        }
        cacheMgr.setCache(key, commentList, FRIEND_COMMENT_EXPIRE_TIME);
        
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CommentCacheService#removeFriendListCacheByEntry(java.lang.String, int)
     */
    @Override
    public void removeFriendListCacheByEntry(String urlmd5, int actorId) {
        if (StringUtils.isEmpty(urlmd5) || 0 == actorId) {
            return;
        }
        String[] keys = KeyGen.getAllKeysByFriendEntry(urlmd5, actorId);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("remove all the caches for entry, urlmd5=%s", urlmd5));
        }

        for (String key : keys) {
            cacheMgr.removeCache(key);
        }
        
    }

}
