package com.renren.ugc.comment.cache;

/**
 * keep the generic cache key generation code
 * 
 * @author jiankuan.xing
 * 
 */
class BaseCacheKeyGenerator {

    protected static final String SEPARATOR = "_";

    protected static final String ENTRY_QUERY_CACHE_KEY_PREFIX = "cq";

    protected static final String GLOBAL_ENTRY_QUERY_CACHE_KEY_PREFIX = "gcq";
    
    protected static final String FRIEND_ENTRY_QUERY_CACHE_KEY_PREFIX = "fcq";
    
    protected static final String REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX = "rcq";

    protected static final String OLDEST_COMMENT_SUFFIX = "oc";

    protected static final String LATEST_COMMENT_SUFFIX = "lc";
    
    protected static final String OLDEST_COMMENTS_SUFFIX = "ocs";

    protected static final String LATEST_COMMENTS_SUFFIX = "lcs";

    protected static final String COUNT_COMMENT = "ctc";
    
    protected static final String OLDEST_COMMENTS_WITHORIGINAL_SUFFIX = "ocso";

    protected static final String LATEST_COMMENTS_WITHORIGINAL_SUFFIX = "lcso";

    /**
     * 产生缓存“entry评论总数”的key. 格式:
     * [prefix]_[entryId]_[entryOwnerId]_[type]_ctc
     */
    public static String genCountEntryCacheKey(long entryId, long entryOwnerId, int type) {
        StringBuilder sb = new StringBuilder();
        sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
        sb.append(SEPARATOR);
        sb.append(entryId);
        sb.append(SEPARATOR);
        sb.append(entryOwnerId);
        sb.append(SEPARATOR);
        sb.append(type);
        sb.append(SEPARATOR);
        sb.append(COUNT_COMMENT);
        return sb.toString();
    }

    /**
     * 产生缓存“entry评论总数”的key. 格式: [prefix]_[urlMd5]_ctc
     */
    public static String genCountEntryCacheKey(String urlMd5) {
        StringBuilder sb = new StringBuilder();
        sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
        sb.append(SEPARATOR);
        sb.append(urlMd5);
        sb.append(SEPARATOR);
        sb.append(COUNT_COMMENT);
        return sb.toString();
    }

}
