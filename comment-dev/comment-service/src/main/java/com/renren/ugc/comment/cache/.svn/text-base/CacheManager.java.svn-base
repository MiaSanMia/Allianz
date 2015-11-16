/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.cache;

import java.io.Serializable;
import java.util.List;

import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;

/**
 * Descriptions of the class CacheManager.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-9-4 下午4:31:25
 */
public interface CacheManager {
    /**
     * 在缓存中存储数据
     * 
     * @param bizName
     * @param key
     * @param value
     * @return
     */
    public boolean put(String bizName, Serializable key, Serializable value);

    /**
     * 从缓存中读取对应key值的value
     * 
     * @param bizName
     * @param key
     * @return
     */
    public Object get(String bizName, Serializable key);
    
    
    /**
     * 从kv数据库中读取key值对应的Integer列表
     * 
     * @param bizName
     * @param key
     * @param pos    读取开始位置
     * @param limit  读取个数
     * @return
     */
    public List<Integer> getIntList(String bizName, String key, Integer pos, Integer limit);
    
    
    /**
     * 向kv数据库中插入key值对应的Integer列表
     * 
     * @param bizName
     * @param key
     * @param value
     * @return
     */
    public boolean putToIntList(String bizName, String key, Integer value);
    
    public Result<List<DataEntry>> mget(String bizName, List<String> ugcCommentIds);
    
    public int incr(String bizName,String key,int value,int defauleValue,int expireTime);

}