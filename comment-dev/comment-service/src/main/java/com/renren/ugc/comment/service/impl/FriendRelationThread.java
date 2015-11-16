/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.xiaonei.xce.buddyadapter.buddyrelationcache.BuddyRelationCacheAdapter;
import com.xiaonei.xce.domain.buddy.BuddyRelation;

/**
 * Descriptions of the class FriendRelationThread.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-11-14 下午2:56:41
 */
public class FriendRelationThread implements Runnable {
    
    private CountDownLatch totalThreadLaunch;
    
    /**
     * 评论作者列表
     */
    private List<Integer> authors;
    
    /**
     * 分析结果列表
     */
    private List<Integer> friendIds;
    
    /**
     * 第几个执行线程
     */
    private int pageIndex;
    
    /**
     * 作者数量
     */
    private int authorsSize;
    
    /**
     * 线程数量
     */
    private int num;
    
    /**
     * 用户ID
     */
    private int userId;
    
    /**
     * 每页线程数
     */
    private int pageCount;

    /**
     * @param totalThreadLaunch
     * @param authors
     * @param friendIds
     * @param i
     * @param authorsSize 
     * @param num 
     * @param userId 
     */
    public FriendRelationThread(CountDownLatch totalThreadLaunch,
                                List<Integer> authors, List<Integer> friendIds,
                                int pageIndex, int authorsSize, int MAX_THREAD_NUM, int userId){
        this.totalThreadLaunch = totalThreadLaunch;
        this.authors = authors;
        this.friendIds = friendIds;
        this.pageIndex = pageIndex;
        this.authorsSize = authorsSize;
        this.num = MAX_THREAD_NUM;
        this.userId = userId;
        this.pageCount = authorsSize % MAX_THREAD_NUM == 0 ? (authorsSize / MAX_THREAD_NUM) : (authorsSize / MAX_THREAD_NUM + 1);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            List<Integer> subAuthors = null;
            if (pageIndex == num - 1) {
                subAuthors = authors.subList(pageIndex * pageCount, authorsSize);
            } else {
                subAuthors = authors.subList(pageIndex * pageCount, (pageIndex + 1) * pageCount);
            }
            
            Map<Integer, BuddyRelation> relationMap = BuddyRelationCacheAdapter.getInstance()
                    .getMultiRelation(userId, subAuthors);
            Set<Integer> keys = relationMap.keySet();
            for (Integer key : keys) {
                if (relationMap.get(key).isFriend()) {
                    friendIds.add(key);
                }
            }
        } finally {
            totalThreadLaunch.countDown();
        }
        

    }

}
