/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Descriptions of the class ShareBusinessNotifyBody.java's implementation：分享业务notifyBody封装
 * @author xiaoqiang 2013-9-12 下午2:46:54
 */
public class ShareBusinessNotifyBody {
    private Map<String, String> valueMap;

    private int schemaId;

    /**
     * at通知类型
     */
    private int type;

    public ShareBusinessNotifyBody() {
        valueMap = new HashMap<String, String>();
    }

    public final Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setValue(String key, String value) {
        valueMap.put(key, value);
    }

}
