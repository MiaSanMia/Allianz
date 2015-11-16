package com.renren.ugc.comment.model;


/**
 * @author wangxx
 *  
 *   “新鲜事"评论请求信息
 */
public class FeedCommentInfo {
    
    /**
     * 新鲜事id
     */
    private long feedId;
    
    /**
     * 评论业务类型（值）
     */
    private int type;
    
    /**
     * 被评论的实体id
     */
    private long entryId;
    
    /**
     * 被评论的实体ownerId
     */
    private int entryOwnerId;
    
    /**
     * 返回头部结果的最大个数
     */
    private int headLimit;
    
    /**
     * 返回尾部结果的最大个数
     */
    private int tailLimit;
    
    /**
     * paixu
     */
    //private boolean desc;

    
    public long getFeedId() {
        return feedId;
    }

    
    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    
    public int getType() {
        return type;
    }

    
    public void setType(int type) {
        this.type = type;
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


    public int getHeadLimit() {
        return headLimit;
    }

    
    public void setHeadLimit(int headLimit) {
        this.headLimit = headLimit;
    }

    
    public int getTailLimit() {
        return tailLimit;
    }

    
    public void setTailLimit(int tailLimit) {
        this.tailLimit = tailLimit;
    }

    
//    public boolean isDesc() {
//        return desc;
//    }
//
//    
//    public void setDesc(boolean desc) {
//        this.desc = desc;
//    }
    
    public FeedCommentInfo(com.renren.ugc.comment.xoa2.FeedCommentInfo feedCommentInfo) {
        toInternalFeedComment(feedCommentInfo);
    }
    
    private void toInternalFeedComment(com.renren.ugc.comment.xoa2.FeedCommentInfo feedCommentInfo) {
       //this.setDesc(feedCommentInfo.isDesc());
       this.setEntryId(feedCommentInfo.getEntryId());
       this.setEntryOwnerId(feedCommentInfo.getEntryOwnerId());
       this.setFeedId(feedCommentInfo.getFeedid());
       this.setHeadLimit(feedCommentInfo.getHeadLimit());
       this.setTailLimit(feedCommentInfo.getTailLimit());
       this.setType(feedCommentInfo.getType().getValue());
    }
    
    

}
