package com.renren.ugc.comment.model;

import java.util.Date;

/**
 * 插入Mysql数据库，安全同学用这些数据做离线广告的拦截 User: jinshunlong Date: 12-12-19 Time:
 * 下午1:35
 */
public class AuditComment {

    private long id;//commentId

    private int type;//评论类型

    private int authorId;//评论的作者

    private int entryOwnerId;//评论的实体的作者

    private long entryId;//评论的实体ID

    private String content;//评论内容

    private String extContent;//评论的额外内容

    private Date createdTime;//评论创建的时间

    private int flag; //删除标记

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtContent() {
        return extContent;
    }

    public void setExtContent(String extContent) {
        this.extContent = extContent;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
