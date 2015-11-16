package com.renren.ugc.comment.interceptor.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.wservice.userscore.client.StrategyType;
import com.xiaonei.wservice.userscore.client.UserScoreAdapter;

/**
 * @author wangxx
 * 
 *         增加评论时，增加用户积分. jira地址: http://jira.d.xiaonei.com/browse/COMMENT-4
 */
public class CommentScoreInterceptor extends CommentLogicAdapter {

	private static final Log logger = LogFactory.getLog(CommentScoreInterceptor.class);

	@Override
	public Comment create(CommentType commentType, int actorId, long entryId,
			int entryOwnerId, Comment comment, CommentStrategy strategy)
					throws UGCCommentException {

		int replyId = comment.getToUserId();

		if (actorId != entryOwnerId && actorId != replyId) {
			//异步调用
			addScoreAsyn(actorId,replyId,entryOwnerId);
		}

		return null;
	}

	private void addScoreAsyn(final int actorId,final int replyId,final int entryOwnerId){

	    
	    CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.ADD_SCORE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {
                addScore(actorId,StrategyType.UGC_REPLY, 1);// 主动回复
                if (replyId != actorId && replyId != 0) {
                    addScore(replyId,StrategyType.CONTRIBUTION_UGC_REPLY, 1);// 被动回复
                }
                if (replyId != entryOwnerId) {
                    addScore(entryOwnerId,StrategyType.CONTRIBUTION_UGC_REPLY, 1);// 被动回复
                }
                return null;
            }
        });
	    
		/*AsynJobService.asynRun(new Runnable() {

			@Override
			public void run() {
				addScore(actorId,StrategyType.UGC_REPLY, 1);// 主动回复
				if (replyId != actorId && replyId != 0) {
					addScore(replyId,StrategyType.CONTRIBUTION_UGC_REPLY, 1);// 被动回复
				}
				if (replyId != entryOwnerId) {
					addScore(entryOwnerId,StrategyType.CONTRIBUTION_UGC_REPLY, 1);// 被动回复
				}
			} 
		});*/

	}

	private void addScore(int userId,int type, int count){

		try{
			UserScoreAdapter.getInstance().addScore(userId,type,count);
		} catch (Exception e){
			logger.error("addScoreWrapper error userId:"+userId + "|type:"+type+"|count:"+count,e);
		}

	}

}
