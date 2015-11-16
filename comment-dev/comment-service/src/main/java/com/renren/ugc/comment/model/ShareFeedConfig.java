/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.model;

/**
 * Descriptions of the class ShareFeedConfig.java's implementation：发现新鲜事的配置信息。
 * @author xiaoqiang 2013-9-13 下午2:15:02
 */
public class ShareFeedConfig {
    
    /**
     * 分享类型
     */
    private int type;
    
    /**
     * 新鲜事类型
     */
    private int feedType;
    
    /**
     * max length of title
     */
    private int titleLength;
    
    /**
     * max length of summary
     */
    private int summaryLength;
    
    /**
     * 分享类型
     * @return
     */
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
    public int getFeedType() {
        return feedType;
    }
    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }
    public int getTitleLength() {
        return titleLength;
    }
    public void setTitleLength(int titleLength) {
        this.titleLength = titleLength;
    }
    public int getSummaryLength() {
        return summaryLength;
    }
    public void setSummaryLength(int summaryLength) {
        this.summaryLength = summaryLength;
    }
    @Override
    public String toString() {
        return "ShareFeedConfig [type=" + type + ", feedType=" + feedType
               + ", titleLength=" + titleLength + ", summaryLength="
               + summaryLength + "]";
    }

}
