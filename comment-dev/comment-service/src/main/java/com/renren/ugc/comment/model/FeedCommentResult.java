package com.renren.ugc.comment.model;

import java.util.List;


/**
 * @author wangxx
 * 
 *  为新鲜事评论返回的结构体
 */
public class FeedCommentResult {
    
    /**
     * entry的评论总数
     */
    private long totalCount;
    
    /**
     * entry是否有更多评论
     */
    private boolean more;
    
    /**
     * entry的头部评论
     */
    private List<Comment> headCommentList;
    
    /**
     * entry的尾部评论
     */
    private List<Comment> tailCommentList;

    
    public long getTotalCount() {
        return totalCount;
    }

    
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    
    public boolean isMore() {
        return more;
    }

    
    public void setMore(boolean more) {
        this.more = more;
    }

    
    public List<Comment> getHeadCommentList() {
        return headCommentList;
    }

    
    public void setHeadCommentList(List<Comment> headCommentList) {
        this.headCommentList = headCommentList;
    }

    
    public List<Comment> getTailCommentList() {
        return tailCommentList;
    }

    
    public void setTailCommentList(List<Comment> tailCommentList) {
        this.tailCommentList = tailCommentList;
    }
    
    

}
