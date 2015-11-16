package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import mop.hi.oce.adapter.AdapterFactory;
import mop.hi.oce.domain.buddy.BuddyRelation;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;

/**
 * @author wangxx
 * 
 *         当发评论的actorId被entryOwnerId或者replyTo的id给block，就不能发评论
 * 
 */
public class CheckFriendsBlockInterceptor extends CommentLogicAdapter {

    private static Logger logger = Logger.getLogger(CheckFriendsBlockInterceptor.class);

    @Override
    public Comment create(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {

        int replyId = comment.getToUserId();

        if (isBlock(actorId, entryOwnerId)) {
            throw new UGCCommentException(CommentError.PERMISSON_DENY,
                    CommentError.PERMISSION_DENY_MSG);
        } else if (replyId > 0) {
            if (isBlock(actorId, replyId)) {
                throw new UGCCommentException(CommentError.PERMISSON_DENY,
                        CommentError.PERMISSION_DENY_MSG);
            }
        }

        return null;
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {

        for(CommentPackage onepackage : forInvokeStrategy.getPackageList()) {
        	int replay2Id = onepackage.getComment().getToUserId();
        	
        	if(isBlock(onepackage.getActorId(), entryOwnerId)){
        		throw new UGCCommentException(CommentError.PERMISSON_DENY,
                        CommentError.PERMISSION_DENY_MSG);
        	}
        	if(replay2Id > 0 ){
        		if (isBlock(onepackage.getActorId(), replay2Id)) {
                    throw new UGCCommentException(CommentError.PERMISSON_DENY,
                            CommentError.PERMISSION_DENY_MSG);
                }
        	}
        }
		return null;
	}

    private boolean isBlock(int userId1, int userId2) {
        if (userId1 == 0 || userId2 == 0 || userId1 == userId2) {
            return false;
        }
        try {
            BuddyRelation relation = AdapterFactory.getBuddyCoreAdapter().getRelation(userId1,
                    userId2);
            return relation.isBlocked();
        } catch (Exception e) {
            logger.warn("判断黑名单出错", e);
        }
        return false;
    }
}
