package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.renren.app.at.local.service.AtParseHelper;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.AtUtil;
import com.renren.ugc.comment.util.NotifyUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * This interceptor will send the comment created notification. Some
 * comment process (e.g. antispam) may force the notice is not sent
 * 
 * @author jiankuan.xing
 * 
 */
public class SendNoticeInterceptor extends CommentLogicAdapter {

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

        //1.get return value
        Comment result = (Comment) strategy.getReturnedValue();

        this.doSendNotice(type, result, entryOwnerId, entryId, actorId, strategy);
        if (AtParseHelper.hasAtInfo(result.getOriginalContent())) {
            if (!strategy.getNeedSendAtInfo()) {
                return null;
            }
            //发送@通知,这里没有做成异步的，因为atIdlist在{@link:com.renren.ugc.comment.interceptor.impl.MatterToMeInterceptor}中有用，如果换陈异步的，无法保证顺序性
            List<Integer> atIdLists = AtUtil.getInstance().sendNotice(type, actorId, entryOwnerId,
                    entryId, result, strategy);
            if (atIdLists != null) {
                strategy.setAtIdLists(atIdLists);
            }
        }

        return null;
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	if (!forInvokeStrategy.isSendNotice()) {
            if (logger.isDebugEnabled()) {
                logger.info("ignore sending comment created notification");
            }
            return null;
        }
    	
    	List<CommentPackage> result = (List<CommentPackage>) forInvokeStrategy.getReturnedValue();
    	for(CommentPackage commentpackage : result){
    		this.doSendNotice(type, commentpackage.getComment(), entryOwnerId, entryId, commentpackage.getActorId(),
    				commentpackage.getForCommentStrategy());
    		if (AtParseHelper.hasAtInfo(commentpackage.getComment().getOriginalContent())) {
                if (!commentpackage.getForCommentStrategy().getNeedSendAtInfo()) {
                    return null;
                }
                //发送@通知,这里没有做成异步的，因为atIdlist在{@link:com.renren.ugc.comment.interceptor.impl.MatterToMeInterceptor}中有用，如果换陈异步的，无法保证顺序性
                List<Integer> atIdLists = AtUtil.getInstance().sendNotice(type, commentpackage.getActorId(), entryOwnerId,
                        entryId, commentpackage.getComment(), commentpackage.getForCommentStrategy());
                if (atIdLists != null) {
                	commentpackage.getForCommentStrategy().setAtIdLists(atIdLists);
                }
            }
    	}
    	return null;
	}

    private void doSendNotice(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final CommentStrategy strategy) {

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {
                boolean result = NotifyUtil.getInstance().sendNotice(type, actorId, entryOwnerId,
                        entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendNotice return false");
                }
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = NotifyUtil.getInstance().sendNotice(type, actorId, entryOwnerId,
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
