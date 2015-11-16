package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.NotifyedUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *  发送"被"评论的信息
 */
public class SendNotifyedInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {

        //1.get return value
        Comment result = (Comment) strategy.getReturnedValue();
        
        if(result == null || result.getToUserId() <= 0 || result.getToUserId() == entryOwnerId){
        	//以下情况，不发"被"评论信息
        	//1.结果为空 2.不是回复评论 3.回复的是ownerId，此时发送逻辑在{@link:SendNoticeInterceptor()}里
        	return null;
        }

        this.doSendNotice(type, result, entryOwnerId, entryId, actorId, strategy);
        
        return null;
    }
    
    private void doSendNotice(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final CommentStrategy strategy) {

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_NOTIFIED){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {
                boolean result = NotifyedUtil.getInstance().sendNotice(type, actorId, entryOwnerId,
                        entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendNotified comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendNotified return false");
                }
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = NotifyedUtil.getInstance().sendNotice(type, actorId, entryOwnerId,
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