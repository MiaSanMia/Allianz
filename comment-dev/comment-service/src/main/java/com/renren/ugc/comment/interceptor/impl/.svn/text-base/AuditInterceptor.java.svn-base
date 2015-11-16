package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.AsyncCommentOpUtil;
import com.renren.ugc.comment.util.AuditCommentUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * The post-interceptor that sends content to security center for auditing.
 * Audit contains two steps: 1. on-line audit done by audit-api; 2.
 * off-line audit done by scanning the comment table, which would be done
 * by "security center".
 * 
 * @author jiankuan.xing
 * 
 */
public final class AuditInterceptor extends CommentLogicAdapter {

    private static final Log logger = LogFactory.getLog(AuditInterceptor.class); 

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {
        Comment createdComment = (Comment) strategy.getReturnedValue();
        doAudit(type, createdComment, false, strategy);

        return null; // as an interceptor, returned value is of no use
    }

    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	doAuditForMuti(type,false,forInvokeStrategy);
    	return null;
	}
    
    @Override
    public boolean update(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy) {
        Boolean updated = (Boolean) strategy.getReturnedValue();
        if (updated) { // only do audit when the update succeeds
            doAudit(type, newComment, true, strategy);
        }
        return false;
    }

    private void doAudit(final CommentType type, final Comment comment, final boolean isUpdate,
            final CommentStrategy strategy) {

        if (strategy.shouldAudit()) {
            
            CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.DO_AUDIT){
                private static final long serialVersionUID = 1L;
                @Override
                protected Void doCall() throws Exception {
                    boolean result = AuditCommentUtil.getInstance().doAudit(type, comment, strategy);
                    if (result && logger.isDebugEnabled()) {
                        logger.debug("send comment " + comment.getId() + " to on-line audit api");
                    } else {
                        logger.warn("on-line audit return false");
                    }
                    return null;
                }
            });
            
            /*AsynJobService.asynRun(new Runnable() {

                @Override
                public void run() {
                    boolean result = AuditCommentUtil.getInstance().doAudit(type, comment, strategy);
                    if (result && logger.isDebugEnabled()) {
                        logger.debug("send comment " + comment.getId() + " to on-line audit api");
                    } else {
                        logger.warn("on-line audit return false");
                    }
                }
            });*/
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("no need to do online audit");
            }
        }
    }
    
    private void doAuditForMuti(final CommentType type, final boolean isUpdate,
            final ForInvokeStrategy forInvokeStrategy) {

        if (forInvokeStrategy.shouldAudit()) {
            
            CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.DO_AUDIT){
                private static final long serialVersionUID = 1L;
                @Override
                protected Void doCall() throws Exception {
                	for(CommentPackage commnetPackage : forInvokeStrategy.getPackageList()){
	                    boolean result = AuditCommentUtil.getInstance().doAudit(type, commnetPackage.getComment(),
	                    		commnetPackage.getForCommentStrategy());
	                    if (result && logger.isDebugEnabled()) {
	                        logger.debug("send comment " + commnetPackage.getComment().getId() + " to on-line audit api");
	                    } else {
	                        logger.warn("on-line audit return false");
	                    }
                	}
                    return null;
                }
            });
            
            
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("no need to do online audit");
            }
        }
    }

    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment, CommentStrategy strategy) {

        Integer playCount = (Integer) strategy.getReturnedValue();

        //judge if need audit
        int oldPlayCount = playCount - increment;

        if (oldPlayCount > 0 && oldPlayCount < AuditCommentUtil.VOICE_COMMENT_AUDIT_LIMIT
                && playCount > AuditCommentUtil.VOICE_COMMENT_AUDIT_LIMIT) {
            CommentLogic commentLogic = strategy.getCommentLogic();
            //1. get comment
            Comment comment = commentLogic.get(type, entryOwnerId, entryId, entryOwnerId,
                    commentId, strategy);

            //2.audit
            doVoiceAudit(type, comment, strategy);
        }

        return playCount;
    }

    private void doVoiceAudit(final CommentType type, final Comment comment,
            final CommentStrategy strategy) {

        if (strategy.shouldAudit()) {
            
            CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.DO_VOICE_AUDIT){
                private static final long serialVersionUID = 1L;
                @Override
                protected Void doCall() throws Exception {
                    boolean result = AuditCommentUtil.getInstance().doVoiceAudit(type, comment,
                            strategy);
                    if (result && logger.isDebugEnabled()) {
                        logger.debug("do voice audit " + comment.getId() + " to on-line audit api");
                    } else {
                        logger.warn("do voice audit return false");
                    }
                    return null;
                }
            });

            /*AsynJobService.asynRun(new Runnable() {

                @Override
                public void run() {
                    boolean result = AuditCommentUtil.getInstance().doVoiceAudit(type, comment,
                            strategy);
                    if (result && logger.isDebugEnabled()) {
                        logger.debug("send comment " + comment.getId() + " to on-line audit api");
                    } else {
                        logger.warn("on-line audit return false");
                    }
                }
            });*/
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("no need to do online audit");
            }
        }
    }
}
