package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.NotifyAlsoUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.model.User;

/**
 * 状态评论提醒实现(之后评论的人向之前评论过该条ugc的好友发送提醒)
 * 
 * @author lei.xu1
 * @since 2013-09-11
 * 
 */
public class StatusCommentNoticeInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {

        if (!strategy.isSendNotice()) {
            if (logger.isDebugEnabled()) {
                logger.info("ignore sending comment created notification");
            }
            return null;
        }
        
        if (User.isPageId(entryOwnerId)) {
            return null;
        }

        //1.get return value
        Comment result = (Comment) strategy.getReturnedValue();
        if(result.getToUserId() > 0){
        	//modified by wangxx,2013-12-27,当评论是回复某人的话，那么也不会增加"也评论"
        	return null;
        }
        
        this.doSendNotice(type, result, entryOwnerId, entryId, actorId, strategy);
        return null;
    }

    private void doSendNotice(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final CommentStrategy strategy) {

        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_STATUS_FRIEND_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception { 

                boolean result = NotifyAlsoUtil.getInstance().sendAlsoNotice(type,
                        actorId, entryOwnerId, entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendStatusFriendNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendStatusFriendNotice return false");
                }
            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = NotifyUtil.getInstance().sendNoticeForCommentedFriends(type,
                        actorId, entryOwnerId, entryId, comment, strategy);

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
