/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.tair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.app.like.data.client.model.LikeInfoResult;
import com.renren.tair.ext.TairByteManagerImpl;
import com.renren.tair.util.TairManagerFactory;
import com.renren.ugc.comment.cache.CacheManager;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.DefaultTairManager;

/**
 * Descriptions of the class TairCacheManagerImpl.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-9-4 下午4:30:48
 */
public class TairCacheManagerImpl implements CacheManager ,InitializingBean{
    private static final Logger logger = Logger.getLogger(TairCacheManagerImpl.class);
    
    private static TairCacheManagerImpl instance ;
    
    public static TairCacheManagerImpl getInstance(){
    	return instance;
    }
    
    @Autowired
    private DefaultCacheConfigration defaultCacheConfigration;
    
    // biz names
    public static final String SHARE_COMMENT_FRIEND_LIST = "comment_friend_list";

    public static final String COMMENT_USER_LIST = "comment_user_list";
    
    //"也评论"的评论数量
    public static final String COMMENT_ALSO_COUNT  = "comment_also_count";
    
    /**
     * 开关缓存
     */
    public static final String SWITCH_ON_OFF = "comment_kafka_onoff";
    
    /**
     * 一个entry的最大楼数
     */
    public static final String ENTRY_MAX_FLOOR = "entry_max_floor";
    
    /**
     * 缓存entry 作者的好友列表
     */
    public static final String COMMENT_FRIEND_LIST_BY_AUTHOR = "comment_friend_list_by_author";
    
    /**
     * 开关缓存
     */
    public static final String UGCMQ_ON_OFF_SWITCH = "ugcMQ_kafka_onoff";
    
    //当KV返回的是整数时的默认错误返回值
    public static final int ERR_INT_CODE = -1;

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CacheManager#put(java.lang.String, java.io.Serializable, java.io.Serializable)
     */
    @Override
    public boolean put(String bizName, Serializable key, Serializable value) {
     // 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);

        DefaultTairManager tairManager = TairManagerFactory.getDefaultTairManager(cacheConfig.getGroup());

        ResultCode resultCode = tairManager.put(cacheConfig.getNamespace(),
                                                cacheConfig.getKeyPrefix() + key.toString(), value,
                                                cacheConfig.getExpireTime());
        if (resultCode.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CacheManager#get(java.lang.String, java.io.Serializable)
     */
    @Override
    public Object get(String bizName, Serializable key) {
     // 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);

        DefaultTairManager tairManager = TairManagerFactory.getDefaultTairManager(cacheConfig.getGroup());

        Result<DataEntry> result = tairManager.get(cacheConfig.getNamespace(),
                                                   cacheConfig.getKeyPrefix() + key.toString());
        if (result.isSuccess()) {// 能够确保result不会为null
            // todo成功打印日志
            DataEntry dataEntry = result.getValue();
            if (dataEntry != null) {
                return dataEntry.getValue();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CacheManager#getIntList(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<Integer> getIntList(String bizName, String key,
        Integer pos, Integer limit) {
        
        long start = System.nanoTime();
        boolean miss = false;
        boolean success = true;
        
        // 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);
        List<Integer> intList = null;
        
        TairByteManagerImpl manager = TairManagerFactory.getByteTairManager(cacheConfig.getGroup());
        Result<List<DataEntry>> result = manager.getDataFromSmallerList(cacheConfig.getNamespace(), cacheConfig.getKeyPrefix() + key, pos, limit);
        if (result.isSuccess()) {
            List<DataEntry> dataEntrys = result.getValue();
            if (dataEntrys != null && dataEntrys.size() != 0) { 
                intList = new ArrayList<Integer>(dataEntrys.size());
                for (DataEntry dataEntry : dataEntrys) {
                    intList.add((Integer) dataEntry.getValue());
                }
            } else {
                miss = true;
                if (result.getRc() != null) {
                    //logger.error("tair result return true, but data is empty. code: " + result.getRc().getCode() + ", message: " + result.getRc().getMessage());
                }
            }
        } else {
            success = false;
            if (result.getRc() != null) {
                logger.error("tair result return false, but data is empty. code: " + result.getRc().getCode() + ", message: " + result.getRc().getMessage());
            }
        }
        
        long end = System.nanoTime();
        if (bizName.equals(SHARE_COMMENT_FRIEND_LIST)) {
            StatisticsHelper.invokeGetCommentedFriendListFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss, success);
        } else if (bizName.equals(COMMENT_USER_LIST)) {
            StatisticsHelper.invokeGetCommentedUserListFromCache((end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);
        }
        return intList;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.cache.CacheManager#putToIntList(java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public boolean putToIntList(String bizName, String key, Integer value) {
     // 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);
        
        TairByteManagerImpl tairManager = TairManagerFactory.getByteTairManager(cacheConfig.getGroup());
        
        ResultCode resultCode = tairManager.putDataToNoRepeatDataOfSmallerList(cacheConfig.getNamespace(), cacheConfig.getKeyPrefix() + key, value, cacheConfig.getExpireTime());
        if (resultCode == null) {
            return false;
        }
        
        if (resultCode.isSuccess()) {
            return true;
        } else {
            logger.error("tair put data return false, but data is empty. code: " + resultCode.getCode() + ", message: " + resultCode.getMessage());
        }
        return false;
    }

    @Override
    public Result<List<DataEntry>> mget(String bizName, List<String> ugcCommentIds) {
        if(CollectionUtils.isEmpty(ugcCommentIds)){
            return null;
        }
        // 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);
        DefaultTairManager tairManager = TairManagerFactory.getDefaultTairManager(cacheConfig.getGroup());
        Result<List<DataEntry>> result = tairManager.mget(cacheConfig.getNamespace(), ugcCommentIds);
        ResultCode resultCode = result.getRc();
        if(result.isSuccess() || resultCode.equals(ResultCode.PARTSUCC)){
            return result;
        }else{
            logger.error("tair get data return false, but data is empty. code: " + resultCode.getCode() + ", message: " + resultCode.getMessage());
            return null;
        }
    }

    public  static void main(String[] args) {
        DefaultTairManager manager = TairManagerFactory.getDefaultTairManager("group_1");
        List<String> keys = new ArrayList<String>();
        keys.add("blog_key1");
        keys.add("blog_key2");

        
        LikeInfoResult like1 = new LikeInfoResult();
        like1.setFriendCount(12);
        like1.setSourceKey("test12");
        
        LikeInfoResult like2 = new LikeInfoResult();
        like2.setFriendCount(11);
        like2.setSourceKey("test11");
        manager.put(0, "blog_key1", like1);
        manager.put(0, "blog_key2", like2);
        
        System.out.println(manager.mget(0, keys));

    }

	@Override
	public int incr(String bizName, String key, int value, int defaultValue,int expireTime) {
		// 获取该业务对应的配置信息
        CacheConfig cacheConfig = defaultCacheConfigration.getCacheConfig(bizName);

        DefaultTairManager tairManager = TairManagerFactory.getDefaultTairManager(cacheConfig.getGroup());
        String  cacheKey = cacheConfig.getKeyPrefix() + key;
        
        Result<Integer> result = tairManager.incr(cacheConfig.getNamespace(), cacheKey, value, defaultValue, expireTime);
        if (result.isSuccess()) {
            return result.getValue();
        } else {
        	logger.error("tair incr error value:" + result.getValue() + ",code:"+result.getRc() != null ? result.getRc().getCode() : 0 + ",msg:"+result.getRc() != null ? result.getRc().getMessage() : "");
            return ERR_INT_CODE;
        }
        
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		TairCacheManagerImpl.instance = this;
	}
}