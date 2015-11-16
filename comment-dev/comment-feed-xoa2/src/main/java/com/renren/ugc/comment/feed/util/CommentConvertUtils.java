package com.renren.ugc.comment.feed.util;

import com.renren.ugc.comment.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO now there are two CommentConvertUtils in comment-xoa2 and comment-xoa2-feed, we need to create a package
// named "comment-commons" to hold these redundancy code
public class CommentConvertUtils {

    /**
     * convert the internal comment to the comment defined in xoa2 interface
     */
    public static com.renren.ugc.comment.xoa2.Comment toXoa2Comment(Comment c) {
        com.renren.ugc.comment.xoa2.Comment comment =
                new com.renren.ugc.comment.xoa2.Comment();
        comment.setId(c.getId());
        comment.setAuthorId(c.getAuthorId());
        comment.setAuthorName(c.getAuthorName());
        comment.setAuthorHead(c.getAuthorHead());
        comment.setAuthorKeepUse(c.isAuthorKeepUse());
        // modified by
        // wangxx,entry统一放在了{@link:com.renren.ugc.comment.strategy.CommentStrategy}
        // 而不统一放在Comment里返回,这里仅仅返回id和ownerId
        comment.setEntry(CommentConvertUtils.toXoa2EntryWithOnlyIdAndOwner(c.getEntry()));

        comment.setContent(c.getContent());
        comment.setOriginalContent(c.getOriginalContent());
        comment.setCreateTimeMillis(c.getCreatedTime());
        com.renren.ugc.comment.xoa2.RepliedUser repliedUser =
                new com.renren.ugc.comment.xoa2.RepliedUser();
        repliedUser.setId(c.getToUserId());
        comment.setReplyToUser(repliedUser);
        comment.setToCommentId(c.getToCommentId());
        comment.setWhipserId(c.getWhipserToId());
        if (c.getLikeInfo() != null) {
            comment.setLikeInfo(CommentConvertUtils.toXoa2CommentLikeInfo(c.getLikeInfo()));
        }
        if (c.isVoiceComment() && c.getVoiceInfo() != null) {
            comment.setVoiceInfo(CommentConvertUtils.toXoa2CommentVoiceInfo(c.getVoiceInfo()));
            comment.setVoiceComment(true);
        }
        comment.setAuthorVipIcon(c.getAuthorVipIcon());
        comment.setOriginalCommentId(c.getIdIfHasOldUgc());
        comment.setLinkedComment(c.isLinked());

        comment.setType(com.renren.ugc.comment.xoa2.CommentType.findByValue(c.getType()));

        return comment;
    }

    /**
     * convert the internal comment to the comment defined in xoa2 interface,if
     * needMetadata is true,set metadata to the comment params
     */
    public static com.renren.ugc.comment.xoa2.Comment toXoa2Comment(Comment c,
        boolean needMetadata) {
        com.renren.ugc.comment.xoa2.Comment comment = toXoa2Comment(c);
        if (needMetadata) {
            comment.setParams(c.getMetadata().toInterfaceMetadata());
        }

        return comment;
    }

    /**
     * convert a list of internal comment object to a list of interface comment.
     * This method assumes that the input list is not null
     */
    public static List<com.renren.ugc.comment.xoa2.Comment> toXoa2CommentList(
        List<Comment> cList) {
        List<com.renren.ugc.comment.xoa2.Comment> commentList =
                new ArrayList<com.renren.ugc.comment.xoa2.Comment>(cList.size());
        for (Comment c : cList) {
            commentList.add(CommentConvertUtils.toXoa2Comment(c));
        }

        return commentList;
    }

    public static com.renren.ugc.comment.xoa2.Entry toXoa2EntryWithOnlyIdAndOwner(
        Entry e) {
        com.renren.ugc.comment.xoa2.Entry entry =
                new com.renren.ugc.comment.xoa2.Entry();
        if (e != null) {
            entry.setId(e.getId());
            entry.setName(e.getName());
            entry.setOwnerId(e.getOwnerId());
            entry.setOwnerName(e.getOwnerName());
        }
        return entry;
    }

    public static com.renren.ugc.comment.xoa2.CommentLikeInfo toXoa2CommentLikeInfo(
        CommentLikeInfo lf) {
        com.renren.ugc.comment.xoa2.CommentLikeInfo likeInfo =
                new com.renren.ugc.comment.xoa2.CommentLikeInfo();
        likeInfo.setLikeCount(lf.getLikeCount());
        likeInfo.setLiked(lf.isLiked());

        return likeInfo;
    }

    public static com.renren.ugc.comment.xoa2.CommentVoiceInfo toXoa2CommentVoiceInfo(
        CommentVoiceInfo vi) {
        com.renren.ugc.comment.xoa2.CommentVoiceInfo voiceInfo =
                new com.renren.ugc.comment.xoa2.CommentVoiceInfo();
        voiceInfo.setVoiceLength(vi.getVoiceLength());
        voiceInfo.setVoiceRate(vi.getVoiceRate());
        voiceInfo.setVoiceSize(vi.getVoiceSize());
        voiceInfo.setVoiceUrl(vi.getVoiceUrl());

        return voiceInfo;
    }

    public static Map<Long, com.renren.ugc.comment.xoa2.FeedCommentResult> toXoa2FeedCommentMap(
        Map<Long, FeedCommentResult> cMap) {
        Map<Long, com.renren.ugc.comment.xoa2.FeedCommentResult> commentMap =
                new HashMap<Long, com.renren.ugc.comment.xoa2.FeedCommentResult>(
                    cMap.size());
        for (long id : cMap.keySet()) {
            FeedCommentResult result = cMap.get(id);
            commentMap.put(id, toXoa2FeedComment(result));
        }

        return commentMap;
    }

    public static com.renren.ugc.comment.xoa2.FeedCommentResult toXoa2FeedComment(
        FeedCommentResult feedComment) {
        com.renren.ugc.comment.xoa2.FeedCommentResult commentResult =
                new com.renren.ugc.comment.xoa2.FeedCommentResult();

        commentResult.setMore(feedComment.isMore());
        commentResult.setTotalCount(feedComment.getTotalCount());
        commentResult.setHeadCommentList(toXoa2CommentList(feedComment.getHeadCommentList()));
        commentResult.setTailCommentList(toXoa2CommentList(feedComment.getTailCommentList()));
        return commentResult;
    }
}
