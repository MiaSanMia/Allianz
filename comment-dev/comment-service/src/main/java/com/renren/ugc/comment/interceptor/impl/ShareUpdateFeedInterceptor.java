/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.share.ShareFeedUtil;
import com.renren.ugc.comment.util.share.ShareUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Descriptions of the class ShareUpdateFeedInterceptor.java's
 * implementation：TODO described the implementation of class
 * 
 * @author xiaoqiang 2013-9-13 上午10:38:28
 */
public class ShareUpdateFeedInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId,
        int entryOwnerId, final Comment comment, final CommentStrategy strategy)
        throws UGCCommentException {
        Comment realComment = (Comment) strategy.getReturnedValue();

        doPublicFeedComment(type, actorId, entryId, entryOwnerId,
            realComment.getId(), strategy, false);

        return comment;
    }

    /**
     * @param type
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param comment
     * @param strategy
     */
    private void doPublicFeedComment(final CommentType type, final int actorId,
        final long entryId, final int entryOwnerId, final long commentId,
        final CommentStrategy strategy, final boolean isDeleteReply) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SHARE_PUBLIC_FEED_COMMENT){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {

                if (strategy.isFeedDispatch()) {
                    try {
                        ShareFeedUtil.replaceFeedComment(type, actorId,
                            entryOwnerId, entryId, commentId, strategy,
                            isDeleteReply);
                    } catch (Exception e) {
                        logger.warn("发新鲜事错误", e);
                    }
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("不发新鲜事！");
                    }
                }
                com.renren.ugc.comment.model.Entry entry = strategy.getEntry();
                Comment comment = null;

                int shareType =
                        Integer.valueOf(entry.getEntryProps().get(
                            EntryConfig.ENTRY_SHARE_TYPE));
                String shareUrl =
                        entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
                if (isDeleteReply) {
                    comment = (Comment) strategy.getComment();

                    // 通过开放平台走评论互通功能
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.agreeCommentTreaty(actorId, shareUrl)) {
                        ShareUtil.removeShareComment(entry.getId(),
                            entry.getOwnerId(), commentId, actorId, entry,
                            comment);
                    }

                    // 56视频因为账号和人人打通了，不属于connect接入，所以无法走平台，单独走分享
                    if (comment != null && ShareUtil.isWoleVideo(shareUrl)
                        && (comment.getToUserId() == 0)) {
                        ShareUtil.removeShareComment(entry, comment);
                    }

                } else {
                    comment = (Comment) strategy.getReturnedValue();

                    // 如果是视频，并且用户同意了互通协议
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.agreeCommentTreaty(entry.getOwnerId(),
                            shareUrl)) {
                        ShareUtil.addShareComment(entry.getId(),
                            entry.getOwnerId(), commentId, actorId, entry,
                            comment);
                    }
                    // 如果是视频，并且仍旧提示用户则发送通知 ，这个通知目前是告诉开放平台这个用户已经评论过了
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.showInterflowCommentPrompt(actorId, entry)) {
                        ShareUtil.notifyPlatform(actorId, shareUrl);
                    }

                    /*
                     * 56视频因为账号和人人打通了，不属于connect接入，所以无法走平台，单独走分享
                     * 有开关控制，并且分享的必须是56视频，而且不是回复他人而是直接回复分享的，发到56
                     */
                    if (comment != null && ShareUtil.isWoleVideo(shareUrl)
                        && (comment.getToUserId() == 0)) {
                        ShareUtil.addShareComment(entry, comment);
                    }
                }

            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                if (strategy.isFeedDispatch()) {
                    try {
                        ShareFeedUtil.replaceFeedComment(type, actorId,
                            entryOwnerId, entryId, commentId, strategy,
                            isDeleteReply);
                    } catch (Exception e) {
                        logger.warn("发新鲜事错误", e);
                    }
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("不发新鲜事！");
                    }
                }
                com.renren.ugc.comment.model.Entry entry = strategy.getEntry();
                Comment comment = null;

                int shareType =
                        Integer.valueOf(entry.getEntryProps().get(
                            EntryConfig.ENTRY_SHARE_TYPE));
                String shareUrl =
                        entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
                if (isDeleteReply) {
                    comment = (Comment) strategy.getComment();

                    // 通过开放平台走评论互通功能
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.agreeCommentTreaty(actorId, shareUrl)) {
                        ShareUtil.removeShareComment(entry.getId(),
                            entry.getOwnerId(), commentId, actorId, entry,
                            comment);
                    }

                    // 56视频因为账号和人人打通了，不属于connect接入，所以无法走平台，单独走分享
                    if (comment != null && ShareUtil.isWoleVideo(shareUrl)
                        && (comment.getToUserId() == 0)) {
                        ShareUtil.removeShareComment(entry, comment);
                    }

                } else {
                    comment = (Comment) strategy.getReturnedValue();

                    // 如果是视频，并且用户同意了互通协议
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.agreeCommentTreaty(entry.getOwnerId(),
                            shareUrl)) {
                        ShareUtil.addShareComment(entry.getId(),
                            entry.getOwnerId(), commentId, actorId, entry,
                            comment);
                    }
                    // 如果是视频，并且仍旧提示用户则发送通知 ，这个通知目前是告诉开放平台这个用户已经评论过了
                    if (IShareConstants.URL_TYPE_VIDEO == shareType
                        && ShareUtil.showInterflowCommentPrompt(actorId, entry)) {
                        ShareUtil.notifyPlatform(actorId, shareUrl);
                    }

                    
                     * 56视频因为账号和人人打通了，不属于connect接入，所以无法走平台，单独走分享
                     * 有开关控制，并且分享的必须是56视频，而且不是回复他人而是直接回复分享的，发到56
                     
                    if (comment != null && ShareUtil.isWoleVideo(shareUrl)
                        && (comment.getToUserId() == 0)) {
                        ShareUtil.addShareComment(entry, comment);
                    }
                }

            }

        });*/

    }

    @Override
    public boolean remove(CommentType type, int actorId, long entryId,
        int entryOwnerId, long commentId, CommentStrategy strategy) {
        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result) {
            doPublicFeedComment(type, actorId, entryId, entryOwnerId,
                commentId, strategy, true);
        }
        return true;
    }

    @Override
    public boolean removeGlobalComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId, CommentStrategy strategy) {
        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result) {
            doPublicFeedComment(type, actorId, entryId, entryOwnerId,
                commentId, strategy, true);
        }
        return true;
    }

}
