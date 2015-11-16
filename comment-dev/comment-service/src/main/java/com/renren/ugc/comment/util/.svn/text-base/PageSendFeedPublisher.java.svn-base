package com.renren.ugc.comment.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.xiaonei.page.xoa.api.PageFeedReplyService;
import com.xiaonei.page.xoa.model.feed.PageFeedReply;
import com.xiaonei.page.xoa.model.feed.ReplyContainer;
import com.xiaonei.platform.component.tools.JSONUtil;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;
import com.xiaonei.platform.core.model.User;


/**
 * @author wangxx
 * 
 *         page发送新鲜事,现在只有相册有
 */
public class PageSendFeedPublisher {

    private static Logger logger = Logger.getLogger(PageSendFeedPublisher.class);

    public static void sendPageReplyFeed(User host, long entryId, int entryOwnerId,
            int commentCount, Comment oldestComment, Comment latestComment, int replyTo,
            boolean isDelete, long replyCommentId, CommentType type) {
        PageFeedReplyService pageFeedReplyservice = ServiceFactories.getFactory().getService(
                PageFeedReplyService.class);
        ReplyContainer replyContainer = new ReplyContainer();
        replyContainer.setCount(commentCount);

        if (oldestComment != null) {
            replyContainer.setOldReply(buildPageFeedReply(oldestComment,type));
        }
        if (latestComment != null) {
            replyContainer.setNewReply(buildPageFeedReply(latestComment,type));
        }
        ServiceFuture<Object> sf = null;

        switch (type) {
            case Blog:
                if (isDelete) {
                    sf = pageFeedReplyservice.delBlogReply(host.getRealUserId(), host.getId(),
                            replyCommentId, entryId, entryOwnerId, replyContainer);
                } else {
                    sf = pageFeedReplyservice.addBlogReply(host.getRealUserId(), host.getId(),
                            entryId, entryOwnerId, replyTo, replyContainer);
                }
                break;
            case Album:
                if (isDelete) {
                    sf = pageFeedReplyservice.delAlbumReply(host.getRealUserId(), host.getId(),
                            replyCommentId, entryId, entryOwnerId, replyContainer);
                } else {
                    sf = pageFeedReplyservice.addAlbumReply(host.getRealUserId(), host.getId(), entryId,
                            entryOwnerId, replyTo, replyContainer);
                }
                break;
            case Photo:
                if (isDelete) {
                    sf = pageFeedReplyservice.delPhotoReply(host.getRealUserId(), host.getId(),
                            replyCommentId, entryId, entryOwnerId, replyContainer);
                } else {
                    sf = pageFeedReplyservice.addPhotoReply(host.getRealUserId(), host.getId(), entryId,
                            entryOwnerId, replyTo, replyContainer);
                }
                break;
            default:
                // do nothing
                logger.debug("sendPageReplyFeed no match commenttype|type:" + type);
        }
        
        if(sf == null){
            return;
        }

        try {
            XoaClientAdapter.doSubmit2(sf);
        } catch (Exception e) {
            logger.error("sendPageReplyFeed error", e);
        }
    }

    private static PageFeedReply buildPageFeedReply(Comment comment,CommentType type) {

        if (comment == null) {
            return null;
        }

        PageFeedReply reply = new PageFeedReply();

        String content = comment.getContent();
        reply.setContent(JSONUtil.quote(content));
        if(type == CommentType.Photo || type == CommentType.Album){
            reply.setImContent(JSONUtil.quote(content));
        } else {
            reply.setImContent(content);
        }
        reply.setFromId(comment.getAuthorId());
        reply.setFromName(comment.getAuthorName());
        reply.setReplyId(comment.getId());
        reply.setTime(new Date(comment.getCreatedTime()));
        reply.setTinyImg(comment.getAuthorHead());
        
        switch(type){
            case Album:
            case Photo:
                reply.setReplyType(FeedDefinition.PHOTO_REPLY);
                break;
            case Blog:
                //reply.setReplyType(FeedDefinition.BLOG_PUBLISH);
            default:
                        
        }
        
        return reply;
    }
}
