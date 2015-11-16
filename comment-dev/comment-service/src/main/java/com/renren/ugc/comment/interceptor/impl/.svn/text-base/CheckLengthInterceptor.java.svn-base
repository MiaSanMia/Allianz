package com.renren.ugc.comment.interceptor.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.CommentFirstPageCacheService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;

/**
 * The pre-interceptor that checks comment's length is. The length of
 * comment's content must not greater than the max length limit, and can't
 * be 0. To apply this check you need to first specify max content length
 * in the comment config and enable this interceptor.
 * 
 * @author jiankuan xing
 * 
 */
public final class CheckLengthInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {

        doCheck(comment, strategy);
        return null; // as an interceptor, return value is of no use
    }

    @Override
    public boolean update(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy) {
        doCheck(newComment, strategy);
        return false; // as an interceptor, return value is of no use
    }
    
    /**
     * 这个方法中设置每个commentstrategy，设置commentstrategy通用的配置
     * 必须放在第一interceptor之中修改interceptor顺序时要注意
     */
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
    		ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	//将invoke中设置的strategy设置到commentstratey中
		for(CommentPackage onepackage : forInvokeStrategy.getPackageList()){
			onepackage.getForCommentStrategy().setCommentLogic(forInvokeStrategy.getCommentLogic());
			onepackage.getForCommentStrategy().setCommentCacheService(
					CommentFirstPageCacheService.getInstance());
			onepackage.getForCommentStrategy().setConfig(forInvokeStrategy.getConfig());
			doCheck(onepackage.getComment(), onepackage.getForCommentStrategy());
		}
		return null;
	}

    private void doCheck(Comment comment, CommentStrategy strategy) {
        String content = comment.getContent();
        boolean isEmpty = checkEmpty(content);
        if (isEmpty) {
            logger.warn("评论内容不能为空");
            throw new UGCCommentException(CommentError.EMPTY_CONTENT,
                    CommentError.EMPTY_CONTENT_MSG);
        }

        boolean isTooLong = checkLength(content, strategy);
        if (isTooLong) {
            if (strategy.isTrimWhenContentTooLong()) {
                // do trim
                logger.info("Trim the too long content");
                String contentAfterTrim = content.substring(0, strategy.getMaxContentLength());
                comment.setContent(contentAfterTrim);
            } else {
                throw new UGCCommentException(CommentError.CONTENT_TOO_LONG,
                        CommentError.CONTENT_TOO_LONG_MSG);
            }
        }
    }

    private boolean checkEmpty(String content) {
        return StringUtils.isBlank(content);
    }

    private boolean checkLength(String content, CommentStrategy strategy) {
        CommentConfig config = strategy.getConfig();
        int maxlen = config.getMaxContentLength();
        return !validate(content, maxlen, null);
    }

    /**
     * validate the length is great or equal than max length
     * 
     * @param encoding the encoding method by which the content will be
     *        encoded before comparison. If this is set to null, directly
     *        compare the number of characters
     */
    private boolean validate(String content, int maxLength, String encoding) {
        if (maxLength == 0) {
            return true;
        }

        try {
            int len = 0;
            if (encoding == null) {
                len = content.length();
                if (logger.isDebugEnabled()) {
                    logger.debug("the length of content is " + len);
                }
            } else {
                len = content.getBytes(encoding).length;
                if (logger.isDebugEnabled()) {
                    logger.debug("after encoding, the length of content bytes is " + len);
                }
            }

            return (len <= maxLength);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }
}
