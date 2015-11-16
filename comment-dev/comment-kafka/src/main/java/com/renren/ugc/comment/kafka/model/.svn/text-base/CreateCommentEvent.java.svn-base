package com.renren.ugc.comment.kafka.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jiankuan
 * Date: 26/12/13
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */

public class CreateCommentEvent implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 获取评论的用户id
     */
    private int actorId; // required
    /**
     * 被评论实体Id
     */
    private long entryId; // required
    /**
     * 被评论实体所有者Id
     */
    private int entryOwnerId; // required
    /**
     * 评论Id
     */
    private long commentId; // required
    /**
     * 评论类型
     */
    private int commentType; //required
    
    /**  创建评论的时间 */
    private long createTime;

    /**
     *
     */
    private Map<String, String> param;

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public int getEntryOwnerId() {
        return entryOwnerId;
    }

    public void setEntryOwnerId(int entryOwnerId) {
        this.entryOwnerId = entryOwnerId;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
    
    public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

    @Override
    public String toString() {
        return "CreateCommentEvent{" +
                "actorId=" + actorId +
                ", entryId=" + entryId +
                ", entryOwnerId=" + entryOwnerId +
                ", commentId=" + commentId +
                ", commentType=" + commentType +
                ", param=" + param +
                '}';
    }
}
