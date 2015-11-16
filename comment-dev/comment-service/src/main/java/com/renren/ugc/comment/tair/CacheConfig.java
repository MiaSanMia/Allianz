/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.tair;

/**
 * Descriptions of the class CacheConfig.java's implementation：TODO described the implementation of class
 * @author xiaoqiang 2013-9-4 下午4:35:09
 */
public class CacheConfig {
    /**
     * 该业务对应的namespace空间
     */
    private int    namespace;

    /**
     * key字段的前缀
     */
    private String keyPrefix;

    /**
     * 该业务对应的过期时间
     */
    private int    expireTime;

    /**
     * 该业务所在的组
     */
    private String group;

    public int getNamespace() {
        return namespace;
    }

    public void setNamespace(int namespace) {
        this.namespace = namespace;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "CacheConfig [namespace=" + namespace + ", keyPrefix=" + keyPrefix + ", expireTime=" + expireTime
               + ", group=" + group + "]";
    }


}
