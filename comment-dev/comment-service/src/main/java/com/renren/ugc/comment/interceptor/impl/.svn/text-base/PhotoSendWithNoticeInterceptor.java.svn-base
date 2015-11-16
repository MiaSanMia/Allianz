package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.EntryConfigUtil;
import com.renren.ugc.comment.util.NotifyUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.model.User;

/**
 * @author wangxx
 * 
 *         photo with比较特殊，需要发送多个通知
 */
public class PhotoSendWithNoticeInterceptor extends CommentLogicAdapter {

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

        //photo 有with信息的话，才会发with信息
        boolean withFeed = EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_WITH);
        if (withFeed && !User.isPageId(entryOwnerId) && EntryConfigUtil.getIsPublic(type, strategy)) {
            this.doSendNotice(type, result, entryOwnerId, entryId, actorId, strategy);
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

            //1.get return value
            List<CommentPackage> result = (List<CommentPackage>) forInvokeStrategy.getReturnedValue();

            //with是实体的属性。存入forInvokeStrategy中 。暂时用同一个actorId
            boolean withFeed = EntryConfigUtil.getBoolean(forInvokeStrategy, EntryConfig.ENTRY_IS_WITH);
            if (withFeed && !User.isPageId(entryOwnerId) && EntryConfigUtil.getIsPublic(type, forInvokeStrategy)
            		&& null!=result && result.size()>0) {
                this.doSendNoticeByList(type, result, entryOwnerId, entryId, forInvokeStrategy);
            }

    	return null;
	}

    private void doSendNotice(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final CommentStrategy strategy) {

        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.PHOTO_WITH_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {


                boolean result = NotifyUtil.getInstance().sendPhotoWithNotice(type, actorId,
                        entryOwnerId, entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendPhotoWithNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendPhotoWithNotice return false");
                }
            
                return null;
            }
        });
        
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = NotifyUtil.getInstance().sendPhotoWithNotice(type, actorId,
                        entryOwnerId, entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendPhotoWithNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendNotice return false");
                }
            }
        });*/
    }
    
    
    private void doSendNoticeByList(final CommentType type, final List<CommentPackage> comments,
            final int entryOwnerId, final long entryId,final ForInvokeStrategy forInvokeStrategy) {

        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.PHOTO_WITH_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {

            	for(CommentPackage onePackage : comments) {
	                boolean result = NotifyUtil.getInstance().sendPhotoWithNotice(type, onePackage.getActorId(),
	                        entryOwnerId, entryId, onePackage.getComment(), onePackage.getForCommentStrategy());
	
	                if (result) {
	                    if (logger.isDebugEnabled()) {
	                        logger.debug("sendPhotoWithNotice comment " + onePackage.getComment().getId() + " success");
	                    }
	                } else {
	                    logger.warn("sendPhotoWithNotice return false");
	                }
            	}
            
                return null;
            }
        });
        
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = NotifyUtil.getInstance().sendPhotoWithNotice(type, actorId,
                        entryOwnerId, entryId, comment, strategy);

                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("sendPhotoWithNotice comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("sendNotice return false");
                }
            }
        });*/
    }

}
