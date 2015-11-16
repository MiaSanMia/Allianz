package com.renren.ugc.comment.model;

import java.io.Serializable;

public class CommentVoiceInfo implements Serializable {

    private static final long serialVersionUID = -5033357072863794097L;

    /**
     * 语音评论的URL
     */
    public String voiceUrl;

    /**
     * 语音评论的时间长度 (in sec)
     */
    public int voiceLength;

    /**
     * 语音评论的文件大小 (in byte)
     */
    public int voiceSize;

    /**
     * 语音评论的频率
     */
    public int voiceRate;

    /**
     * 语音评论的播放次数
     */
    public int voicePlayCount;

    public int getVoicePlayCount() {
        return voicePlayCount;
    }

    public void setVoicePlayCount(int voicePlayCount) {
        this.voicePlayCount = voicePlayCount;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public int getVoiceSize() {
        return voiceSize;
    }

    public void setVoiceSize(int voiceSize) {
        this.voiceSize = voiceSize;
    }

    public int getVoiceRate() {
        return voiceRate;
    }

    public void setVoiceRate(int voiceRate) {
        this.voiceRate = voiceRate;
    }

    public CommentVoiceInfo() {

    }

    public CommentVoiceInfo(com.renren.ugc.comment.xoa2.CommentVoiceInfo commentVoiceInfo) {
        toInternalCommentVoiceInfo(commentVoiceInfo);
    }

    /**
     * convert the commentVoiceInfo defined in the xoa2 interface to
     * internal commentVoiceInfo
     */
    private void toInternalCommentVoiceInfo(
            com.renren.ugc.comment.xoa2.CommentVoiceInfo commentVoiceInfo) {
        this.setVoiceLength(commentVoiceInfo.getVoiceLength());
        this.setVoiceRate(commentVoiceInfo.getVoiceRate());
        this.setVoiceSize(commentVoiceInfo.getVoiceSize());
        this.setVoiceUrl(commentVoiceInfo.getVoiceUrl());
    }

}
