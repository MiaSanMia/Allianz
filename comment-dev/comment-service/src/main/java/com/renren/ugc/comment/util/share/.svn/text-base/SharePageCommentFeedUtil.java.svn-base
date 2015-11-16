/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.xiaonei.commons.gid.util.G;
import com.xiaonei.commons.gid.util.Type;
import com.xiaonei.page.xoa.api.PageFeedReplyService;
import com.xiaonei.page.xoa.model.feed.PageFeedReply;
import com.xiaonei.page.xoa.model.feed.ReplyContainer;
import com.xiaonei.platform.component.xfeed.reply.FeedReplyContentBuilder;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;
import com.xiaonei.xce.feed.reply.FeedReplyObj;

/**
 * Descriptions of the class SharePageCommentFeedUtil.java's
 * implementation：公共主页的 评论回复新鲜事
 * 
 * @author xiaoqiang 2013-9-13 下午3:14:18
 */
public class SharePageCommentFeedUtil {

    private static Logger logger = Logger.getLogger(SharePageCommentFeedUtil.class);

    public static void sendFeedNotice(Entry entry, FeedReplyContentBuilder firstReplyBuilder,
            FeedReplyContentBuilder lastReplyBuilder, int count, boolean isDelete, int userId,
            boolean isNew, FeedReplyObj firstReplyObj, FeedReplyObj lastReplyObj,CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug("发公共主页评论消息了：share.getId:" + entry.getId() + " serId: "
                    + entry.getOwnerId());
        }

        User user = WUserAdapter.getInstance().get(userId);

        try {
            user = WUserAdapter.getInstance().get(userId);
        } catch (Exception e1) {
            try {
                user = WUserAdapter.getInstance().get(userId);
            } catch (Exception e2) {
                logger.error(
                        "发公共主页评论发消息失败了：share.getId:" + entry.getId() + " serId: "
                                + entry.getOwnerId() + "|| exception:" + e2.getMessage(), e2);
            }
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //添加回复：
        ReplyContainer replys = new ReplyContainer();
        long replyId = 0;
        PageFeedReply oldReply = null;
        if (isNew) {
            if (firstReplyObj != null) {
                oldReply = new PageFeedReply();
                oldReply.setReplyId(firstReplyObj.getReplyId());
                oldReply.setReplyType(firstReplyObj.getReplyType());
                oldReply.setTime(firstReplyObj.getTime());
                oldReply.setContent(firstReplyObj.getContent());
                oldReply.setImContent(firstReplyObj.getImContent());
                oldReply.setFromId((int) firstReplyObj.getFromId());
                oldReply.setFromName(firstReplyObj.getFromName());
                oldReply.setTinyImg(firstReplyObj.getTinyImg());
            }
        } else {
            if (firstReplyBuilder != null && firstReplyBuilder.getReplyId() > 0) {
                oldReply = new PageFeedReply();
                oldReply.setReplyId(firstReplyBuilder.getReplyId());
                oldReply.setReplyType(firstReplyBuilder.getType());
                //2011-10-12 19:06 
                try {
                    oldReply.setTime(sf.parse(firstReplyBuilder.getTime()));
                } catch (Exception e) {
                    oldReply.setTime(new Date());
                }
                oldReply.setContent(firstReplyBuilder.getBody());
                oldReply.setImContent(firstReplyBuilder.getImBody());
                oldReply.setFromId(firstReplyBuilder.getActor());
                oldReply.setFromName(firstReplyBuilder.getActorName());
                oldReply.setTinyImg(firstReplyBuilder.getTinyImg());
            }
        }

        PageFeedReply newReply = null;
        if (isNew) {
            if (lastReplyObj != null) {
                newReply = new PageFeedReply();
                newReply.setReplyId(lastReplyObj.getReplyId());
                newReply.setReplyType(lastReplyObj.getReplyType());
                newReply.setTime(lastReplyObj.getTime());
                newReply.setContent(lastReplyObj.getContent());
                newReply.setImContent(lastReplyObj.getImContent());
                newReply.setFromId((int) lastReplyObj.getFromId());
                newReply.setFromName(lastReplyObj.getFromName());
                newReply.setTinyImg(lastReplyObj.getTinyImg());
                replyId = lastReplyObj.getReplyId();
            }
        } else {
            if (lastReplyBuilder != null && lastReplyBuilder.getReplyId() > 0) {
                newReply = new PageFeedReply();
                newReply.setReplyId(lastReplyBuilder.getReplyId());
                newReply.setReplyType(lastReplyBuilder.getType());
                try {
                    newReply.setTime(sf.parse(lastReplyBuilder.getTime()));
                } catch (ParseException e) {
                    newReply.setTime(new Date());
                }
                newReply.setContent(lastReplyBuilder.getBody());
                newReply.setImContent(lastReplyBuilder.getImBody());
                newReply.setFromId(lastReplyBuilder.getActor());
                newReply.setFromName(lastReplyBuilder.getActorName());
                newReply.setTinyImg(lastReplyBuilder.getTinyImg());
                replyId = lastReplyBuilder.getReplyId();
            }
        }

        replys.setOldReply(oldReply);
        replys.setNewReply(newReply);
        replys.setCount(count);
        int shareType = Integer.valueOf(entry.getEntryProps().get("share_type"));

        try {
            PageFeedReplyService service = ServiceFactories.getFactory().getService(
                    PageFeedReplyService.class);
            ServiceFuture<Object> future = null;
            Integer feedType = ShareFeedUtil.getFeedType(Integer.valueOf(entry.getEntryProps().get(
                    "share_type")),strategy,entry.getOwnerId());

            //如果分享者是page或者被分享源是page，则使用page的新鲜事定义
            if (G.isTypeOf(entry.getOwnerId(), Type.PAGE) || isSharePageXOAType(shareType)) {
                feedType = ShareFeedUtil.getPageFeedType(entry.getOwnerId(), shareType,strategy);
            }

            if (isDelete) {
                future = service.delShareReply(user.getRealUserId(), user.getId(), replyId,
                        feedType, entry.getId(), entry.getOwnerId(), replys);
            } else {
                future = service.addShareReply(user.getRealUserId(), user.getId(), entry.getId(),
                        entry.getOwnerId(), feedType, replyId, replys);
            }
            future.submit();

            XoaBizErrorListener bizErrorListener = new XoaBizErrorListener();
            future.addListener(bizErrorListener);
            long timeoutMillis = 500;// ms 
            if (future.await(timeoutMillis)) {
                if (future.isSuccess()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("成功发送：share.getId:" + entry.getId() + " serId: "
                                + entry.getOwnerId());
                    }
                } else {
                    logger.error("发公共主页评论发消息没成功：share.getId:" + entry.getId() + " serId: "
                            + entry.getOwnerId());
                    XoaBizErrorBean errorBean = bizErrorListener.getReturn();
                    if (errorBean != null) {
                        logger.error(String.format("errorMessage:%s,errorCode:%d",
                                errorBean.getMessage(), errorBean.getErrorCode()));
                    }
                }
            }
        } catch (InterruptedException e) {
            logger.error("发公共主页评论发消息失败了：share.getId:" + entry.getId() + " serId: "
                    + entry.getOwnerId() + "|| exception:" + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("发公共主页评论发消息失败了：share.getId:" + entry.getId() + " serId: "
                    + entry.getOwnerId() + "|| exception:" + e.getMessage());
        }

    }

    /**
     * 是否是分享源为page的日志、照片、相册、视频、链接
     * 
     * @param type 分享类型
     * @return 如果是分享源为page的日志、照片、相册、视频、链接，则返回true；否则返回false
     */
    public static boolean isSharePageXOAType(int type) {
        return (IShareConstants.URL_TYPE_PAGE_BLOG == type
                || IShareConstants.URL_TYPE_PAGE_PHOTO == type
                || IShareConstants.URL_TYPE_PAGE_ALBUM == type
                || IShareConstants.URL_TYPE_PAGE_LINK == type || IShareConstants.URL_TYPE_PAGE_VIDEO == type);
    }

}
