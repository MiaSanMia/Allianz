/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.tair;

import java.util.HashMap;
import java.util.Map;


/**
 * Descriptions of the class DefaultCacheConfigration.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-9-4 下午4:34:44
 */
public class DefaultCacheConfigration {
    
    /**
     * 保存个个业务的tair配置信息
     */
    private Map<String, CacheConfig> cacheConfigs = new HashMap<String, CacheConfig>();

    /**
     * 根据业务字段获取该业务的tair配置信息
     * 
     * @param bizName
     * @return
     */
    public CacheConfig getCacheConfig(String bizName) {
        return cacheConfigs.get(bizName);
    }

    public void setCacheConfigs(Map<String, CacheConfig> cacheConfigs) {
        this.cacheConfigs = cacheConfigs;
    }

}
