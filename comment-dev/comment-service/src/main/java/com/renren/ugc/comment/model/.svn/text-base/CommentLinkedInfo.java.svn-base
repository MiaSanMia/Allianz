package com.renren.ugc.comment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxx
 * 
 *         评论的linked info，适用于评论的多写
 */
public class CommentLinkedInfo {

    /**
     * 实体拥有者id
     */
    private int entryOwnerId;

    /**
     * 实体id
     */
    private long entryId;

    /**
     * 评论类型
     */
    private int commentType;

    /**
     * 评论Id
     */
    private long linkedCommentId;

    public int getEntryOwnerId() {
        return entryOwnerId;
    }

    public void setEntryOwnerId(int entryOwnerId) {
        this.entryOwnerId = entryOwnerId;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public long getLinkedCommentId() {
        return linkedCommentId;
    }

    public void setLinkedCommentId(long linkedCommentId) {
        this.linkedCommentId = linkedCommentId;
    }

    private static CommentLinkedInfo toInternalCommentLinkInfo(
            com.renren.ugc.comment.xoa2.CommentLinkedInfo commentLinkedInfo) {

        CommentLinkedInfo internalCommentLinkedInfo = new CommentLinkedInfo();

        internalCommentLinkedInfo.setEntryId(commentLinkedInfo.getEntryId());
        internalCommentLinkedInfo.setEntryOwnerId(commentLinkedInfo.getEntryOwnerId());
        internalCommentLinkedInfo.setCommentType(commentLinkedInfo.getType().getValue());

        return internalCommentLinkedInfo;

    }

    public static List<CommentLinkedInfo> toInternalCommentLinkInfos(
            List<com.renren.ugc.comment.xoa2.CommentLinkedInfo> commentLinkedInfos) {

        if (commentLinkedInfos == null) {
            return null;
        }

        List<CommentLinkedInfo> internalCommentLinkedInfos = new ArrayList<CommentLinkedInfo>(
                commentLinkedInfos.size());

        for (com.renren.ugc.comment.xoa2.CommentLinkedInfo commentLinkedInfo : commentLinkedInfos) {
            internalCommentLinkedInfos.add(toInternalCommentLinkInfo(commentLinkedInfo));
        }

        return internalCommentLinkedInfos;
    }

    public CommentLinkedInfo() {

    }

    public CommentLinkedInfo(long linkedCommentId, int entryOwnerId, long entryId,int commentType) {
        this.entryOwnerId = entryOwnerId;
        this.linkedCommentId = linkedCommentId;
        this.entryId = entryId;
        this.commentType = commentType;
    }

}
