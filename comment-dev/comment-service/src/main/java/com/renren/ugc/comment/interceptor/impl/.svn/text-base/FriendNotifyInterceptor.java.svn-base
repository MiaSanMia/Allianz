/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.interceptor.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.ugc.comment.cache.CacheManager;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.NotifyUtil;
import com.renren.ugc.comment.xoa2.CommentType;


/**
 * Descriptions of the class FriendNotifyInterceptor.java's implementation：如果原ugc通知源ugc
 * @author xiaoqiang 2013-10-17 下午5:30:37
 * 
 * 这个服务全部迁移到了kafka中，见comment-async-service工程
 * modified by wangxx
 * 
 */
@Deprecated
public class FriendNotifyInterceptor extends CommentLogicAdapter {
    private Logger logger = Logger.getLogger(FriendNotifyInterceptor.class);
    
    //private static String kafka_switch = "off";
    
    @Autowired
    private CacheManager tairCacheManager;
    
    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {
    	
    	Map<String, String> params = strategy.getParams();
    	String kafkaSwitch = null;
    	if (params != null) {
    		kafkaSwitch = params.get("kafka_switch");
    	}
        if (!"on".equals(kafkaSwitch)) {
        	if (!strategy.getNeedSendFriendNotify()) {
                if (logger.isDebugEnabled()) {
                    logger.info("ignore sending comment created friend notification");
                }
                return null;
            }

            //1.get return value
            Comment result = (Comment) strategy.getReturnedValue();

            this.doSendNotice(type, result, entryOwnerId, entryId, actorId, strategy);
        }

        return null;
        
    }
    
    private void doSendNotice(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final CommentStrategy strategy) {
    
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SHARE_FRIEND_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {

                boolean result = NotifyUtil.getInstance().sendFriendNotice(type, actorId, entryOwnerId,
                        entryId, comment, strategy);
    
                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendShareFriendNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendShareFriendNotice return false");
                }
            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {
    
            @Override
            public void run() {
                boolean result = NotifyUtil.getInstance().sendFriendNotice(type, actorId, entryOwnerId,
                        entryId, comment, strategy);
    
                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendNotice return false");
                }
            }
        });*/
    }

}