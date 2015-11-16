package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.ShortUrlUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         短链接加密，把一个url变成一个合法的"人人短链接"
 * 
 */
public class ShortUrlEncryptInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {

        //对于所有业务存储的时候都存成短链接的形式，展示的时候由业务方发送的参数isReplaceShortUrl来决定是否转化为带有链接的形式
//        if (!strategy.isReplaceShortUrl()) {
//            if (logger.isDebugEnabled()) {
//                logger.info("ignore sending comment created shorturl info");
//            }
//            return null;
//        }

        comment.setContent(ShortUrlUtil.getInstance().getEncryptedContent(comment.getContent()));

        return comment;
    }

}
