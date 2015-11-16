package com.renren.ugc.comment.model;

public class CommentLikeInfo {

    /**
     * 是否被喜欢
     */
    public boolean liked;

    /**
     * 被喜欢的数量
     */
    public int likeCount;

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public com.renren.ugc.comment.xoa2.CommentLikeInfo toInterfaceCommentLikeInfo() {
        com.renren.ugc.comment.xoa2.CommentLikeInfo likeInfo = new com.renren.ugc.comment.xoa2.CommentLikeInfo();
        likeInfo.setLikeCount(this.getLikeCount());
        likeInfo.setLiked(this.isLiked());

        return likeInfo;
    }

}
