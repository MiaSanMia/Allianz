package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.WriteOldDatabaseUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         写到各个老ugc业务的db中
 */
public class WriteOldDatabaseInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {

        Comment createdComment = (Comment) strategy.getReturnedValue();

        WriteOldDatabaseUtil.getInstance().writeOldDatabase(type, createdComment, strategy);

        return null;
    }

}
