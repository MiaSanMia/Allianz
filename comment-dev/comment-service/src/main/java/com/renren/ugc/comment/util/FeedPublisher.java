package com.renren.ugc.comment.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.renren.doingubb.tool.DoingUbbReplace;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.component.tools.JSONUtil;
import com.xiaonei.platform.component.xfeed.reply.FeedReplyContentBuilder;
import com.xiaonei.xce.feed.reply.FeedReplyDispatcher2;
import com.xiaonei.xce.feed.reply.FeedReplyObj;

public class FeedPublisher {

    private static final Logger logger = Logger.getLogger(FeedPublisher.class);

    /**
     * publish the created/removed feed comment. When getting this
     * notification, the feed server will upgrade the feed UI.<br>
     * 
     * If the <code>feedType</code> or <code>ubbType</code> is invalid, the
     * notification is ignored.
     * 
     * @param entryOwnerId entry owner id
     * @param oldestComment the first comment added to the entry
     * @param latestComment the last comment added to the entry
     * @param entryId the entry id
     * @param feedType the feed's type
     * @param ubbType the ubb type
     * @param count the total number of the comments of the current entry
     * @param commentId only set when to publish comment creation; for
     *        removal, set this to 0
     */
    public static void publishFeedComment(int entryOwnerId, Comment oldestComment,
            Comment latestComment, long entryId, int feedType, boolean replaceUbb, long sourceId,
            long count, long commentId,int feedOwnerId,CommentStrategy strategy) {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "publish notification to feed, entryId:%d, oldest:%d, latest:%d, count:%d, replaceUbb:%b",
                    entryId, (oldestComment == null) ? 0 : oldestComment.getId(),
                    (latestComment == null) ? 0 : latestComment.getId(), count, replaceUbb));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("feed type is " + feedType);
        }

        if (feedType == 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("feed type is 0, ignore sending notification to feed");
            }
            return;
        }
        
        if(feedOwnerId == 0){
            feedOwnerId = entryOwnerId;
        }

        // avoid the case that oldestComment == latestComment
        if (oldestComment != null && latestComment != null
                && (oldestComment.getId() == latestComment.getId())) {
            if (logger.isDebugEnabled()) {
                logger.debug("oldest comment id = latest comment id = " + latestComment.getId());
            }

            oldestComment = null;
        }
        //        String oldReply = buildCommentXML(oldestComment, replaceUbb);
        //        String newReply = buildCommentXML(latestComment, replaceUbb);
        //replace by wangxx
        FeedReplyObj oldReply = buildFeedReplyObj(oldestComment,strategy);
        FeedReplyObj newReply = buildFeedReplyObj(latestComment,strategy);
        if (logger.isDebugEnabled()) {
            if (oldReply != null) {
                logger.debug("buildCommentXML for feed, oldest comment:\n" + oldReply);
            }
            if (newReply != null) {
                logger.debug("buildCommentXML for feed, latest comment:\n" + newReply);
            }
        }
        //        FeedReply reply = new FeedReply(oldestComment == null ? 0 : oldestComment.getId(),
        //                oldReply, latestComment == null ? 0 : latestComment.getId(), newReply, (int) count);
        if (commentId == 0) {
            // 增加评论
            // XFeedReplyer.getInstance().addFeedReply(sourceId, feedType, entryOwnerId, reply);
            FeedReplyDispatcher2.getInstance().addFeedReply(newReply, oldReply, (int) count,
                    sourceId, (int) feedType, feedOwnerId);
            if (logger.isDebugEnabled()) {
                logger.debug("comment created notice is updated to feed");
                logger.debug(String.format("entryId=%d, feedType=%d, entryOwnerId=%d, sourceId=%d",
                        entryId, feedType, entryOwnerId, sourceId));
                logger.debug(String.format("oldest comment id=%d, latest comment id=%d, count=%d",
                        oldestComment == null ? 0 : oldestComment.getId(),
                        latestComment == null ? 0 : latestComment.getId(), count));

            }
        } else {
            // 删除评论
            //            XFeedReplyer.getInstance().removeFeedReply(sourceId, feedType, entryOwnerId, commentId,
            //                    reply);
            FeedReplyDispatcher2.getInstance().removeFeedReply(newReply, oldReply, (int) count,
                    commentId, sourceId, (int) feedType, feedOwnerId);
            if (logger.isDebugEnabled()) {
                logger.debug("comment removed notice is updated to feed");
                logger.debug(String.format("entryId=%d, feedType=%d, entryOwnerId=%d, sourceId=%d",
                        entryId, feedType, entryOwnerId, sourceId));
                logger.debug(String.format("oldest comment id=%d, latest comment id=%d, count=%d",
                        oldestComment == null ? 0 : oldestComment.getId(),
                        latestComment == null ? 0 : latestComment.getId(), count));
            }
        }
    }

    //旧的新鲜事接口，已不用
    private static String buildCommentXML(Comment comment, boolean replaceUbb) {
        if (comment == null) {
            return "";
        }
        if (comment.isWhisper()) {
            logger.debug("whisper filted  for feed :" + comment.getId());
            return "";
        }

        String content = comment.getContent();
        //comment.setOriginalContent(content);
        //        if (replaceUbb) {
        //            content = DoingUbbReplace.getInstance().replaceUBB(comment.getContent());
        //            comment.setContent(content); // replace the content after ubb replacement
        //        }

        FeedReplyContentBuilder builder = new FeedReplyContentBuilder();
        builder.tinyImg(comment.getAuthorHead());
        builder.body(JSONUtil.quote(content));
        builder.imBody(JSONUtil.quote(content));
        builder.replyId(comment.getId());
        builder.time(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date(
                comment.getCreatedTime())));
        builder.actorName(comment.getAuthorName());
        builder.actor(comment.getAuthorId());

        String result = builder.getXml();

        // too long xml notification
        if (result.length() > 5120) {
            String msg = "你的评论内容过长(" + result.length() + ")";
            logger.warn(msg);
            throw new UGCCommentException(msg);
        }

        return result;
    }

    private static FeedReplyObj buildFeedReplyObj(Comment comment,CommentStrategy strategy) {

        if (comment == null) return null;
        FeedReplyObj reply = new FeedReplyObj();
        reply.setFromId(comment.getAuthorId());
        reply.setFromName(comment.getAuthorName());
        reply.setReplyId(comment.getId());
        reply.setTime(new Date(comment.getCreatedTime()));
        reply.setTinyImg(comment.getAuthorHead());
        
        String content = comment.getContent();
        //发新鲜事的时候统一替换为ubb
        if(!strategy.isReplaceUbb()){
            content = DoingUbbReplace.getInstance().replaceUBB(content,false);
        }
        //发新鲜事的时候统一替换@
        if(!strategy.isReplaceAt()){
            content = AtUtil.getInstance().getWithHrefAt(content);
        }
        //截取字符
        content = LengthUtil.cutString(content, CommentCenterConsts.FEED_COMMENT_LIMIT).replaceAll("\n", " ");

        if(strategy.isNeedFeedQuote()){
            reply.setContent(JSONUtil.quote(content));
        } else {
            reply.setContent(content);
        }
        if(strategy.isNeedIMFeedQuote()){
            reply.setImContent(JSONUtil.quote(content));
        } else {
            reply.setImContent(content);
        }
        
        //语音评论
        if(comment.getFlag() != null && comment.getFlag().isUseVoice()){
            CommentVoiceInfo voiceInfo = comment.getVoiceInfo();
            if(voiceInfo != null){
                reply.setVoiceCount(voiceInfo.getVoicePlayCount());
                reply.setVoiceLength(voiceInfo.getVoiceLength());
                reply.setVoiceRate(voiceInfo.getVoiceRate());
                reply.setVoiceSize(voiceInfo.getVoiceSize());
                reply.setVoiceUrl(voiceInfo.getVoiceUrl());
                //语音评论写死
                reply.setReplyType(200);
            }
        }
        return reply;
    }
}
