/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.AntispamUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.antispam.AntiSpamAdapter;
import com.xiaonei.antispam.AntiSpamUtils;
import com.xiaonei.antispam.model.CheckResult;

/**
 * Descriptions of the class ShareAntispamInterceptor.java's
 * implementation：TODO described the implementation of class
 * 
 * @author xiaoqiang 2013-9-11 下午7:37:43
 */
public class ShareAntispamInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(ShareAntispamInterceptor.class);

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {

        String safeContent = doAntispam(type, actorId, entryOwnerId, comment.getContent(), strategy);

        comment.setContent(safeContent);

        return null; // as an interceptor, returned value is of no use
    }

    /**
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param content
     * @param strategy
     * @return
     */
    private String doAntispam(CommentType type, int actorId, int entryOwnerId, String content,
            CommentStrategy strategy) throws UGCCommentException {
        CommentConfig config = strategy.getConfig();
        if (config != null && !strategy.isNeedAntispam()) {
            if (logger.isDebugEnabled()) {
                logger.debug("antispam is ignored for comment type " + type.name());
            }
            return content;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "do antispam with check type:%d, authorId:%d, toId:%d, userIp:%s",
                    config.getAntispamType(), actorId, entryOwnerId, strategy.getUserIp()));
        }

        AntispamUtil.checkActivity(actorId, entryOwnerId, config.getAntispamType(), content);

        CheckResult result = null;
        try {
            result = AntiSpamAdapter.getInstance().checkAndFilter(actorId, entryOwnerId,
                    config.getAntispamType(), content, strategy.getUserIp());
        } catch (Exception e) {
            logger.warn("call AntiSpamAdapter#checkAndFilter failed", e);
        }

        //默认不送审
        strategy.setShouldAudit(false);
        if (result == null) {
            return AntiSpamUtils.getInstance().getSafePureContent(content);
        } else if (result.getFlag() == CheckResult.SAFE) {
            return result.getSafePureContent();
        } else if (result.getFlag() == CheckResult.PROHIBITED) {
            // 违禁词
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("违禁coment.body:%s", content));
            }
            logger.warn(String.format("内容违禁:%s [shareOwner: %d, hostId: %d, code: %d]", content,
                    entryOwnerId, actorId, CheckResult.PROHIBITED));
            throw new UGCCommentException(CommentError.PROHIBITED_BY_ANTISPAM,
                    result.getShowMessage());

        } else if (result.getFlag() == CheckResult.CONTENT_NOT_NOTIFY) {
            strategy.setShouldAudit(true);
            strategy.setFeedDispatch(false); //不发送新鲜事
        } else if (result.getFlag() == CheckResult.CONTENT_NEED_AUDIT) {
            strategy.setShouldAudit(true);

        } else if (result.getFlag() == CheckResult.AUDIT_AND_NOT_NOTIFY) {
            strategy.setShouldAudit(true);
            strategy.setSendNotice(false);
            strategy.setFeedDispatch(false); //不发送新鲜事
        }

        return result.getSafePureContent();
    }

}
