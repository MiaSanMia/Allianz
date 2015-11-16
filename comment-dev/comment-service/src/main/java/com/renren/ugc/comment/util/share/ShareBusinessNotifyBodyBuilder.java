/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import com.renren.ugc.comment.model.ShareBusinessNotifyBody;

/**
 * Descriptions of the class ShareBusinessNotifyBodyBuilder.java's implementation：ShareBusinessNotifyBodyBuilder
 * @author xiaoqiang 2013-9-12 下午3:14:34
 */
public class ShareBusinessNotifyBodyBuilder {
    private ShareBusinessNotifyBody notifyBody = new ShareBusinessNotifyBody();

    /**
     * 设置notifyBody的schemaId
     * 
     * @param schemaId
     * @return
     */
    public ShareBusinessNotifyBodyBuilder setSchemaId(int schemaId) {
        notifyBody.setSchemaId(schemaId);
        return this;
    }

    /**
     * 设置notifyBody的type
     * 
     * @param type
     * @return
     */
    public ShareBusinessNotifyBodyBuilder setType(int type) {
        notifyBody.setType(type);
        return this;
    }

    /**
     * 设置notifyBody里的key和value值
     * 
     * @param key
     * @param value
     * @return
     */
    public ShareBusinessNotifyBodyBuilder setValue(String key, String value) {
        notifyBody.getValueMap().put(key, value);
        return this;
    }

    /**
     * 构建
     * @return
     */
    public ShareBusinessNotifyBody build() {
        return notifyBody;
    }

}
