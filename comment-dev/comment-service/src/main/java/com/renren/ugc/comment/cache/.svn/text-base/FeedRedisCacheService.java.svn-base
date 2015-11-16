package com.renren.ugc.comment.cache;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.renren.cluster.ClusterException.ClusterOpException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.renren.ugc.comment.feed.FeedCommentPropertyFilter;
import com.renren.ugc.comment.feed.FeedOriginalCommentPropertyFilter;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.MultiFeedComment;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.RedisUtils;



public class FeedRedisCacheService{
    
    private final RedisClusterPoolClient client = RedisUtils.getClient();

    private static Log logger = LogFactory.getLog(FeedRedisCacheService.class);
    
    private static FeedRedisCacheService instance = new FeedRedisCacheService();

    public static FeedRedisCacheService getInstance() {
        return instance;
    }

    public long getCount(long entryId, int entryOwnerId, int type) {
        
        String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);

        long longValue = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        String strValue = null;
        
        try {
            strValue = client.get(key);
        } catch (ClusterOpException e) {
            logger.error("can't get from map (key = " + key + ")", e);
        }
        
        if(strValue == null){
            return longValue;
        }
        
        try{
            longValue = Long.parseLong(strValue);
        } catch(Exception e){
            logger.error("get count cache error |key:" + key + "|value:"+strValue);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("get count cache, key=%s, value=%d", key, longValue));
        }
        return longValue;
    }

    public long incCount(long entryId, int entryOwnerId, int type,int step) {
        
        String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.incrBy(key, step);
        } catch (ClusterOpException e) {
            logger.error("can't incCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, value=%d", key,value));
        }
        return value;
    }
    
    public long incFriendCommentCount(String urlmd5, int entryOwnerId, int type, int step) {
        String key = RedisKeyGen.genFriendCountEntryCacheKey(urlmd5, entryOwnerId);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.incrBy(key, step);
        } catch (ClusterOpException e) {
            logger.error("can't incCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, value=%d", key,value));
        }
        return value;
    }
    
public long decCount(long entryId, int entryOwnerId, int type,int step) {
        
        String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.decrBy(key, step);
        } catch (ClusterOpException e) {
            logger.error("can't incCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, value=%d", key, value));
        }
        return value;
    }

    public long decFriendCommentCount(String urlmd5, int entryOwnerId, int type, int step) {
        String key = RedisKeyGen.genFriendCountEntryCacheKey(urlmd5, entryOwnerId);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.decrBy(key, step);
        } catch (ClusterOpException e) {
            logger.error("can't incCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, value=%d", key, value));
        }
        return value;
    }

    /**
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param value
     * 
     *  expire day = EXPIRE_TIME_30_DAY
     */
    public void setCount(long entryId, int entryOwnerId, int type, long value) {
        String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        
        String reply = "";
        try {
            reply = client.setex(key, (int)CommentCenterConsts.EXPIRE_TIME_30_DAYS, String.valueOf(value));
        } catch (ClusterOpException e) {
            logger.error("can't setCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        if (!reply.equals("OK")){
            logger.error(" setCount error(key = " + key + ", value = " + value
                    + ")");
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("set count cache for key=%s, value=%d", key, value));
        }
        
    }
    
    /**
     * set Friend comment count
     * @param urlmd5
     * @param entryOwnerId
     * @param type
     * @param value
     * 
     * expire day = EXPIRE_TIME_15_DAY
     */
    public void setFriendCommentCount(String urlmd5, int entryOwnerId, int type, long value) {
        String key = RedisKeyGen.genFriendCountEntryCacheKey(urlmd5, entryOwnerId);
        
        String reply = "";
        try {
            reply = client.setex(key, (int)CommentCenterConsts.EXPIRE_TIME_15_DAYS, String.valueOf(value));
        } catch (ClusterOpException e) {
            logger.error("can't setCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        if (!reply.equals("OK")){
            logger.error(" setCount error(key = " + key + ", value = " + value
                    + ")");
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("set count cache for key=%s, value=%d", key, value));
        }
        
    }
    
    public long getFriendCommentCount(String urlmd5, int entryOwnerId, int type) {
        String key = RedisKeyGen.genFriendCountEntryCacheKey(urlmd5, entryOwnerId);

        long longValue = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        String strValue = null;
        
        try {
            strValue = client.get(key);
        } catch (ClusterOpException e) {
            logger.error("can't get from map (key = " + key + ")", e);
        }
        
        if(strValue == null){
            return longValue;
        }
        
        try{
            longValue = Long.parseLong(strValue);
        } catch(Exception e){
            logger.error("get count cache error |key:" + key + "|value:"+strValue);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("get count cache, key=%s, value=%d", key, longValue));
        }
        return longValue;
    }
    
    public boolean set(String key, String value, int expTime) {
        String reply = "";
        try {
            reply = client.setex(key, expTime, value);
        } catch (ClusterOpException e) {
            logger.error("can't set into map (key = " + key + ", value = " + value
                    + ")", e);
        }
        return (reply.equals("OK"));
    }
    
    public String get(String key) {
        String value = null;
        try {
            value = client.get(key);
        } catch (ClusterOpException e) {
            logger.error("can't get from map (key = " + key + ")", e);
        }

        return value;
    }

    public void setOldestEntryCommentsCache(long entryId, int entryOwnerId, int type, List<Comment> comments,boolean withOriginalContent) {
        
        if(CollectionUtils.isEmpty(comments)){
            return;
        }
        
        String key = null;
        String jsonValue = null;
        
        MultiFeedComment multiComment = new MultiFeedComment();
        multiComment.setComments(comments);
        
        if(withOriginalContent){
        	//需要存入redis的为OriginalContent
        	//1.1.1 generate key
        	key = RedisKeyGen.getOldestOriginalCommentOfEntryCacheKey(entryId, entryOwnerId, type);
            //1.1.2 to json
            jsonValue =  JSON.toJSONString(multiComment,FeedOriginalCommentPropertyFilter.getInstance());
        } else {
        	//1.2.1 generate key
        	key = RedisKeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
            //1.2.2 to json  
            jsonValue =  JSON.toJSONString(multiComment,FeedCommentPropertyFilter.getInstance());
        }
        
        //2.set cache,expire time = 30 days
        set(key, jsonValue, CommentCenterConsts.EXPIRE_TIME_30_DAYS);
        
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the oldest comment for key:" + key + ", comment size:"
                    + comments.size());
        }

    }
    
    public void setFriendOldestEntryCommentsCache(String urlmd5, int entryOwnerId, int type, List<Comment> comments) {
        if(CollectionUtils.isEmpty(comments)){
            return;
        }
        
        String key = RedisKeyGen.getOldestFriendCommentOfEntryCacheKey(urlmd5, entryOwnerId);
        
        MultiFeedComment multiComment = new MultiFeedComment();
        multiComment.setComments(comments);
        
        String jsonValue =  JSON.toJSONString(multiComment,FeedCommentPropertyFilter.getInstance());
        
        //2.set cache,expire time = 15 days
        set(key, jsonValue, CommentCenterConsts.EXPIRE_TIME_15_DAYS);
        
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the oldest comment for key:" + key + ", comment size:"
                    + comments.size());
        }
    }

    public List<Comment> getOldestEntryCommentCache(long entryId, int entryOwnerId, int type) {
        
        String key = RedisKeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
        
        //1.get value from cache
        String jsonValue = get(key);
        
        //2.parse from json
        MultiFeedComment decodeMultiCommet = null;
        try{
            decodeMultiCommet =  JSON.parseObject(jsonValue,MultiFeedComment.class);
        } catch (Exception e){
            logger.error("getOldestEntryCommentCache decode error|key:"+key);
        }
        
        if(decodeMultiCommet == null){
            if (logger.isDebugEnabled()) {
                logger.debug("the oldest comment cache for key " + key + " doesn't exists");
            }
            return Collections.emptyList();
        }         
        
        return decodeMultiCommet.getComments();
    }
    

    public void setLatestEntryCommentCache(long entryId, int entryOwnerId, int type, List<Comment> comments,boolean withOriginalContent) {
        
        if(CollectionUtils.isEmpty(comments)){
            return;
        }
        
        String key = null;
        String jsonValue = null;
        
        MultiFeedComment multiComment = new MultiFeedComment();
        multiComment.setComments(comments);
        
        if(withOriginalContent){
        	//需要存入redis的为OriginalContent
        	//1.1.1 generate key
        	key = RedisKeyGen.getLatestOriginalCommentOfEntryCacheKey(entryId, entryOwnerId, type);
            //1.1.2 to json
            jsonValue =  JSON.toJSONString(multiComment,FeedOriginalCommentPropertyFilter.getInstance());
        } else {
        	//1.2.1 generate key
        	key = RedisKeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
            //1.2.2 to json  
            jsonValue =  JSON.toJSONString(multiComment,FeedCommentPropertyFilter.getInstance());
        }
        
        //2.set cache,expire time = 30 days
        set(key,jsonValue,CommentCenterConsts.EXPIRE_TIME_30_DAYS);
        
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the latest comment for key:" + key + ", comment size:"
                    + comments.size());
        }
    }
    
    public void setFriendLatestEntryCommentCache(String urlmd5, int entryOwnerId, int type, List<Comment> comments) {
        if(CollectionUtils.isEmpty(comments)){
            return;
        }
        
        String key = RedisKeyGen.getLatestFriendCommentOfEntryCacheKey(urlmd5, entryOwnerId);
        
        //1.to json
        MultiFeedComment multiComment = new MultiFeedComment();
        multiComment.setComments(comments);
        
        String jsonValue =  JSON.toJSONString(multiComment,FeedCommentPropertyFilter.getInstance());
        
        //2.set cache,expire time = 15 days
        set(key,jsonValue,CommentCenterConsts.EXPIRE_TIME_15_DAYS);
        
        if (logger.isDebugEnabled()) {
            logger.debug("create cache for the latest comment for key:" + key + ", comment size:"
                    + comments.size());
        }
    }

    public List<Comment> getLatestEntryCommentCache(long entryId, int entryOwnerId, int type) {
  String key = RedisKeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type);
        
        //1.get value from cache
        String jsonValue = get(key);
        
        //2.parse from json
        MultiFeedComment decodeMultiCommet = null;
        try{
            decodeMultiCommet =  JSON.parseObject(jsonValue,MultiFeedComment.class);
        } catch (Exception e){
            logger.error("getLatestEntryCommentCache decode error|key:"+key);
        }
        
        if(decodeMultiCommet == null){
            if (logger.isDebugEnabled()) {
                logger.debug("the latest comment cache for key " + key + " doesn't exists");
            }
            return Collections.emptyList();
        }         
        
        return decodeMultiCommet.getComments();
    }
    
    public List<String> getMulti(List<String> keys) {
        List<String> values = null;
        try {
            values = client.mget(keys);
        } catch (ClusterOpException e) {
            logger.error("can't get from map (keys size = " + keys.size() + ")", e);
        }

        return values;
    }
    
    public long getLimitCount(String key) {
        
        //String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);

        long longValue = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        String strValue = null;
        
        try {
            strValue = client.get(key);
        } catch (ClusterOpException e) {
            logger.error("can't get from map (key = " + key + ")", e);
        }
        
        if(strValue == null){
            return longValue;
        }
        
        try{
            longValue = Long.parseLong(strValue);
        } catch(Exception e){
            logger.error("get count cache error |key:" + key + "|value:"+strValue);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("get count cache, key=%s, value=%d", key, longValue));
        }
        return longValue;
    }
    
    public void setLimitCount(String key,long value) {
        //String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        
        String reply = "";
        try {
            reply = client.setex(key, (int)CommentCenterConsts.EXPIRE_MINS, String.valueOf(value));
        } catch (ClusterOpException e) {
            logger.error("can't setCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        if (!reply.equals("OK")){
            logger.error(" setCount error(key = " + key + ", value = " + value
                    + ")");
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("set count cache for key=%s, value=%d", key, value));
        }
        
    }
    
    public long incLimitCount(String key,int step) {
        
        //String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.incrBy(key, step);
        } catch (ClusterOpException e) {
            logger.error("can't incCount (key = " + key + ", value = " + value
                    + ")", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("increase count cache, key=%s, value=%d", key,value));
        }
        return value;
    }
    
    public long expire1Sec(String key) {
        
        //String key = RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type);
        long value = CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE;
        try {
            value = client.expire(key, 1);
        } catch (ClusterOpException e) {
            logger.error("expire error(key = " + key + ", value = " + value
                    + ")", e);
        }
        
        return value;
    }
    
    
    /**
     * The key generator for this cache service
     * 
     * @author jiankuan.xing
     * 
     */
    public static class RedisKeyGen extends BaseCacheKeyGenerator {

        /**
         * 产生缓存“最旧n条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_ocs
         */
        public static String getOldestCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(OLDEST_COMMENTS_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生缓存“最新n条评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_lcs
         */
        public static String getLatestCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(LATEST_COMMENTS_SUFFIX);
            return sb.toString();
        }
        
        /**
         * 产生缓存“最旧n条好友评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_ocs
         */
        public static String getOldestFriendCommentOfEntryCacheKey(String urlMd5, int entryOwnerId) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(urlMd5);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(OLDEST_COMMENTS_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生缓存“最新n条好友评论”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_lcs
         */
        public static String getLatestFriendCommentOfEntryCacheKey(String urlMd5, int entryOwnerId) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(urlMd5);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(LATEST_COMMENTS_SUFFIX);
            return sb.toString();
        }
        
        /**
         * 产生缓存“entry评论总数”的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_ctc
         */
        public static String genFriendCountEntryCacheKey(String urlMd5, long entryOwnerId) {
            StringBuilder sb = new StringBuilder();
            sb.append(ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(urlMd5);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(COUNT_COMMENT);
            return sb.toString();
        }
        
        /**
         * 产生缓存“最旧n条评论”的而且内容是orignalContent的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_ocs
         */
        public static String getOldestOriginalCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(OLDEST_COMMENTS_WITHORIGINAL_SUFFIX);
            return sb.toString();
        }

        /**
         * 产生缓存“最新n条评论”的而且内容是orignalContent的key. 格式:
         * [prefix]_[entryId]_[entryOwnerId]_[type]_lcs
         */
        public static String getLatestOriginalCommentOfEntryCacheKey(long entryId, int entryOwnerId,
                int type) {
            StringBuilder sb = new StringBuilder();
            sb.append(REDIS_ENTRY_QUERY_CACHE_KEY_PREFIX);
            sb.append(SEPARATOR);
            sb.append(entryId);
            sb.append(SEPARATOR);
            sb.append(entryOwnerId);
            sb.append(SEPARATOR);
            sb.append(type);
            sb.append(SEPARATOR);
            sb.append(LATEST_COMMENTS_WITHORIGINAL_SUFFIX);
            return sb.toString();
        }

    }

}
