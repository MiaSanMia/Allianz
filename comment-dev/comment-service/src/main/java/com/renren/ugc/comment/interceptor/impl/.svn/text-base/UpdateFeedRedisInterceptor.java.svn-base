package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.FeedRedisCacheService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.feed.FeedCommentUtil;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.xoa2.CommentType;


/**
 * @author wangxx
 *
 * 写到新鲜事的redis中去
 * 
 */
public class UpdateFeedRedisInterceptor extends CommentLogicAdapter{
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {
        
        //悄悄话不进入redis
        if(comment.getWhipserToId() > 0){
            // is a whipser, return
            if (logger.isDebugEnabled()) {
                logger.debug("UpdateFeedRedisInterceptor create getLatest is whiper|entryId:" + entryId
                        + "|entryOwnerId:" + entryOwnerId);
            }
            return comment;
        }
        
        updateFeedAsyn(type, entryId, entryOwnerId, strategy, comment);
        
        return comment;
        
    }
    @Override
    public boolean remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {

        Boolean result = (Boolean) strategy.getReturnedValue();
        if (!result) {
            return false;
        }
        
        removeFeedAsyn(type, entryId, entryOwnerId, strategy);
        
        return true;
    }
    
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {

        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result) {
            removeFeedAsyn(type, entryId, entryOwnerId, strategy);
        }
        
        return true;
    }
    
    private static long incOrPutIfAbsentCount(CommentType type,long entryId,int entryOwnerId,CommentStrategy strategy,int step){
        
        long returnValue = 0l;
        long cacheCount = FeedRedisCacheService.getInstance().getCount(entryId, entryOwnerId, type.getValue());
        
        if(cacheCount == CommentCenterConsts.CACHE_DEFUALT_RETURN_LONG_VALUE){
            //expire or doesn't exist ,so set to cache
            long newCount = FeedCommentUtil.putCountToRedisFromCommentLogic(type, entryId, entryOwnerId, strategy);
            
            return newCount;
        } else {
            //has data in cache,so just inc or dec:)
            if(step > 0){
                returnValue = FeedRedisCacheService.getInstance().incCount(entryId, entryOwnerId, type.getValue(),step);
            } else if(cacheCount > 0){
                returnValue = FeedRedisCacheService.getInstance().decCount(entryId, entryOwnerId, type.getValue(),-step);
            }
        }
        
        return returnValue;
    }
    
    /**
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     * @param comment
     */
    
    private void updateFeedAsyn(final CommentType type,final long entryId,final int entryOwnerId,final CommentStrategy strategy, final Comment comment){
    	
    	CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.UPDATE_REDIS_FEED){
    	     
            private static final long serialVersionUID = 1L;

            @Override
            public String getSerializedMsg() {
                return String.format("%s %s %s %s %s ", type, entryId, entryOwnerId, comment, strategy);
            }

            @Override
            protected Void doCall() throws Exception {
            	updateFeedCommentAsyn(type, entryId, entryOwnerId, strategy);
                return null;
            }
        });
    	
//        AsynJobService.asynRun(new Runnable() {
//
//            @Override
//            public void run() {
//                updateFeedCommentAsyn(type, entryId, entryOwnerId, strategy);
//            }
//
//            
//        });
    }

    /**
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     */
    private static void updateFeedCommentAsyn(final CommentType type,
        long entryId, final int entryOwnerId,
        final CommentStrategy strategy) {
        
         //关联评论的话,添加的新鲜事的sourceId应该是关联评论的主entryId,这个是在{@link:CommentLogicImpl #doReplyWithLinked()}方法里设置的
//        if (strategy.getAsynSourceId() > 0) {
//            entryId = strategy.getAsynSourceId();
//        }
        
        //1.update count
        long count = incOrPutIfAbsentCount(type, entryId, entryOwnerId, strategy, 1);
        
        //我们优先填充"最新的"cache
        //2.update latest cache
        FeedCommentUtil.putLatestCommentToRedisFromCommentLogic(type, entryId, entryOwnerId, strategy);
        
        //3.update oldest cache
        if(count > FeedCommentUtil.FEED_REDIS_NUM){
            FeedCommentUtil.putOldestCommentToRedisFromCommentLogic(type, entryId, entryOwnerId, strategy,count - FeedCommentUtil.FEED_REDIS_NUM);
        }
    }
    
    /**
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     */
    private static void removeFeedCommentAsyn(final CommentType type,
        long entryId, final int entryOwnerId,
        final CommentStrategy strategy) {
        
        //关联评论的话,添加的新鲜事的sourceId应该是关联评论的主entryId,这个是在{@link:CommentLogicImpl #doReplyWithLinked()}方法里设置的
//        if (strategy.getAsynSourceId() > 0) {
//            entryId = strategy.getAsynSourceId();
//        }
        
        //1.update count
        Comment removedComment = strategy.getComment();
        int step = -1;
        if(removedComment != null && removedComment.getWhipserToId() > 0){
            //如果该评论是悄悄话，那么我们就不应该修改新鲜事评论的数量了
            step = 0;
        }
        long count = incOrPutIfAbsentCount(type, entryId, entryOwnerId, strategy, step);
        
      //我们优先填充"最新的"cache
        if(count >= 0){
            //2.update latest cache
            FeedCommentUtil.putLatestCommentToRedisFromCommentLogic(type, entryId, entryOwnerId, strategy);
        }
        
        //3.update oldest cache
        if(count > FeedCommentUtil.FEED_REDIS_NUM){
            FeedCommentUtil.putOldestCommentToRedisFromCommentLogic(type, entryId, entryOwnerId, strategy,count-FeedCommentUtil.FEED_REDIS_NUM);
        }
    }
    
    /**
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     */
    
    private void removeFeedAsyn(final CommentType type,final long entryId,final int entryOwnerId,final CommentStrategy strategy){
    	
    	CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.REMOVE_REDIS_FEED){
   	     
            private static final long serialVersionUID = 1L;

            @Override
            public String getSerializedMsg() {
            	 return String.format("%s %s %s %s", type, entryId, entryOwnerId, strategy);
            }

            @Override
            protected Void doCall() throws Exception {
            	removeFeedCommentAsyn(type, entryId, entryOwnerId, strategy);
                return null;
            }
        });
    	
//        AsynJobService.asynRun(new Runnable() {
//
//            @Override
//            public void run() {
//                removeFeedCommentAsyn(type, entryId, entryOwnerId, strategy);
//            }
//
//        });
    }

}
