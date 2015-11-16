package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.AtUtil;
import com.renren.ugc.comment.util.ShortUrlUtil;
import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         把@信息转化为"带链接"的前端可以识别的形式
 * 
 */
public class GetAtInfoInterceptor extends CommentLogicAdapter {

//    private Logger logger = Logger.getLogger(this.getClass());
//
//    @Override
//    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
//            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {
//
//        if (!strategy.isReplaceAt()) {
//            if (logger.isDebugEnabled()) {
//                logger.info("ignore sending comment created at info");
//            }
//            return null;
//        }
//
//        //1.get return value
//        Comment result = (Comment) strategy.getReturnedValue();
//
//        //2.replace @
//        result.setOriginalContent(result.getContent());
//        result.setContent(AtUtil.getInstance().getWithHrefAt(result.getContent()));
//
//        return null;
//    }
//
//    @Override
//    public List<Comment> getList(CommentType commentType, int actorId, long entryId,
//            int entryOwner, CommentStrategy strategy) throws UGCCommentException {
//
//        if (!strategy.isReplaceAt()) {
//            if (logger.isDebugEnabled()) {
//                logger.info("ignore sending comment created at info");
//            }
//            return null;
//        }
//
//        //1.get return value
//        List<Comment> results = (List<Comment>) strategy.getReturnedValue();
//
//        //2.replace @
//        for (Comment c : results) {
//            c.setOriginalContent(c.getContent());
//            c.setContent(AtUtil.getInstance().getWithHrefAt(c.getContent()));
//        }
//
//        return null;
//
//    }
//
//    @Override
//    public Comment getOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
//            CommentStrategy strategy) {
//
//        //1.get return value
//        Comment result = (Comment) strategy.getReturnedValue();
//
//        //2.replace @
//        result.setOriginalContent(result.getContent());
//        result.setContent(AtUtil.getInstance().getWithHrefAt(result.getContent()));
//
//        return null;
//
//    }
//
//    @Override
//    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
//            CommentStrategy strategy) {
//
//        //1.get return value
//        Comment result = (Comment) strategy.getReturnedValue();
//
//        //2.replace @
//        result.setOriginalContent(result.getContent());
//        result.setContent(AtUtil.getInstance().getWithHrefAt(result.getContent()));
//        return null;
//    }

}
