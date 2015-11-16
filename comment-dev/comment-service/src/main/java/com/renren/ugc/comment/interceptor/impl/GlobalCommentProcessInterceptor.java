package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.util.AsyncCommentOpUtil;
import com.renren.ugc.comment.xoa2.CommentType;

public class GlobalCommentProcessInterceptor extends CommentLogicAdapter {

    @Override
    public Comment create(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {
        if (!strategy.isNeedGlobal()) {
            return null;
        }

        Comment result = (Comment) strategy.getReturnedValue();
        if (result == null) {
            return null;
        }
        AsyncCommentOpUtil.asyncCreateGlobalComment(commentType, result, strategy);

        return null;
    }

    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    		if (!forInvokeStrategy.isNeedGlobal()) {
    			return null;
            }
    		
    		//在post第一个拦截器添加resultvalue
    		List<CommentPackage> packageList = forInvokeStrategy.getPackageList(); 
        	List<CommentPackage> resultList = (List<CommentPackage>) forInvokeStrategy.getReturnedValue();
        	if(null!=packageList&&resultList!=null&&
        			packageList.size() == resultList.size()){
        		for(int i=0;i<packageList.size();++i){
        			packageList.get(i).getForCommentStrategy().setReturnedValue(resultList.get(i).getComment());
        		}
        	}

    		
            if (resultList == null) {
            	return null;
            }
            AsyncCommentOpUtil.asyncCreateGlobalCommentList(type, resultList);
    	
    	return null;
	}
    
    @Override
    public boolean remove(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        if (!strategy.isNeedGlobal()) {
            return false;
        }

        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result == false) {
            return false;
        }
        AsyncCommentOpUtil.asyncRemoveGlobalComment(commentType, actorId, strategy.getUrlMd5(),
                commentId, strategy);

        return false;
    }
}
