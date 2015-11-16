package com.renren.ugc.comment.feed;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.renren.ugc.comment.cache.FeedRedisCacheService;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.MultiFeedComment;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;


public class FeedCommentUtil {
    
    /**
     * 每条feed放在redis中的评论数量
     */
    public static final int FEED_REDIS_NUM = 5;
    
    private static Logger logger = Logger.getLogger(FeedRedisCacheService.class);
    
    public static List<Comment> parseCommentFromValue(String value) {

        if(StringUtils.isEmpty(value)){
            return Collections.emptyList();
        }
        
        //1.get value 
        String jsonValue = value;
        
        //2.parse from json
        MultiFeedComment decodeMultiComment = null;
        try{
            decodeMultiComment =  JSON.parseObject(jsonValue,MultiFeedComment.class);
        } catch (Exception e){
            logger.error("getOldestEntryCommentCache decode error|value:"+jsonValue);
        }
        
        if(decodeMultiComment == null){
            if (logger.isDebugEnabled()) {
                logger.debug("the oldest comment cache for value error|value:" + value);
            }
            return Collections.emptyList();
        }         
        
        return decodeMultiComment.getComments();
    }
    
    public static long parseCountFromValue(String value){
        long result = -1;
        if(StringUtils.isEmpty(value)){
            return result;
        }
        try{
            result = Long.parseLong(value);
        } catch(NumberFormatException ne){
            logger.error("getLongFromCache error|value:"+value);
        }
        return result;
}
    
    public static long putCountToRedisFromCommentLogic(CommentType type,long entryId,int entryOwnerId,CommentStrategy strategy){
        //1.get count
        long newCount = strategy.getCommentLogic().getCount(type, entryOwnerId, entryId, entryOwnerId, strategy);
        //2.set to cache
        FeedRedisCacheService.getInstance().setCount(entryId, entryOwnerId, type.getValue(), newCount);
        
        return newCount;
    }
    
    public static long putFriendCommentCountToRedisFromCommentLogic(CommentType type, String urlmd5, int entryOwnerId, CommentStrategy strategy) {
        //1.get count
        long newCount = strategy.getCommentLogic().getFriendCommentCount(type, entryOwnerId, urlmd5, strategy);
        //2.set to cache
        FeedRedisCacheService.getInstance().setFriendCommentCount(urlmd5, entryOwnerId, type.getValue(), newCount);
        
        return newCount;
    }
    
 public static  List<Comment> putOldestCommentToRedisFromCommentLogic(CommentType type,long entryId,int entryOwnerId,CommentStrategy strategy,long oldestCount){
        
        CommentLogic commentLogic = strategy.getCommentLogic();
        
         //3.1get comments
        strategy.setOldestCount(oldestCount > FEED_REDIS_NUM ? (int)(FEED_REDIS_NUM) : (int)oldestCount);
        List<Comment> oldstComments = commentLogic.getNOldestCommentOfEntry(type, entryId, entryOwnerId, strategy);
        //3.2set into cache
        FeedRedisCacheService.getInstance().setOldestEntryCommentsCache(entryId, entryOwnerId, type.getValue(), oldstComments,false);
        //3.3 set orignalContent into cache
        FeedRedisCacheService.getInstance().setOldestEntryCommentsCache(entryId, entryOwnerId, type.getValue(), oldstComments,true);
        
        return oldstComments;
    }

    public static  List<Comment> putLatestCommentToRedisFromCommentLogic(CommentType type,long entryId,int entryOwnerId,CommentStrategy strategy){
        
        CommentLogic commentLogic = strategy.getCommentLogic();
        
       //2.1get comments
        strategy.setLatestCount(FEED_REDIS_NUM);
        List<Comment> latestComments = commentLogic.getNLatestCommentOfEntry(type, entryId, entryOwnerId, strategy);
        //2.2 set into cache
        FeedRedisCacheService.getInstance().setLatestEntryCommentCache(entryId, entryOwnerId, type.getValue(), latestComments,false);
        //2.3 set orignalContent into cache
        FeedRedisCacheService.getInstance().setLatestEntryCommentCache(entryId, entryOwnerId, type.getValue(), latestComments,true);
        
        return latestComments;
    }
    
    public static List<Comment> putFriendLatestCommentToRedisFromCommentLogic(CommentType type, String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy) {
        CommentLogic commentLogic = strategy.getCommentLogic();
        
        strategy.setLatestCount(FEED_REDIS_NUM);
        
        List<Comment> latestComments = commentLogic.getNLatestFriendCommentOfEntry(type, urlmd5, entryId, entryOwnerId, strategy);
        
        FeedRedisCacheService.getInstance().setFriendLatestEntryCommentCache(urlmd5, entryOwnerId, type.getValue(), latestComments);
        
        return latestComments;
    }
    
    public static List<Comment> putFriendOldestCommentToRedisFromCommentLogic(CommentType type, String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy, long oldestCount) {
        CommentLogic commentLogic = strategy.getCommentLogic();
        
        strategy.setOldestCount(oldestCount > FEED_REDIS_NUM ? (int)(FEED_REDIS_NUM) : (int)oldestCount);
        
        List<Comment> oldstComments = commentLogic.getNOldestFriendCommentOfEntry(type, urlmd5, entryId, entryOwnerId, strategy);
        
        FeedRedisCacheService.getInstance().setFriendLatestEntryCommentCache(urlmd5, entryOwnerId, type.getValue(), oldstComments);
        
        return oldstComments;
    }
   

}
