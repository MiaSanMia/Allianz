/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.interceptor.impl;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.AsyncCommentOpUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Descriptions of the class UgcCommentProcessInterceptor.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-11-8 上午10:03:31
 */
public class UgcCommentProcessInterceptor  extends CommentLogicAdapter {
    
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {
        
        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result == false) {
            return false;
        }
        
        //由于在{@link:CommentLogicImpl}的removeGlobalComment方法里数量已经减1，因此这里不再更新数量
        Boolean oldValue = strategy.isCacheDisabled();
        strategy.setCacheDisabled(true);
        AsyncCommentOpUtil.syncRemoveUgcComment(type, actorId, entryId, entryOwnerId,
            commentId, strategy);
        strategy.setCacheDisabled(oldValue);
        
        return true;
    }

}
