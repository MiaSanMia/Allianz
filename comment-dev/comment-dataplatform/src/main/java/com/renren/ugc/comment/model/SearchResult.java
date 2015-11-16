package com.renren.ugc.comment.model;

import org.json.JSONArray;


/**
 * @author wangxx
 * 
 *  封装一个返回结果类
 *
 */
public class SearchResult { 
    
    /**
     * 返回结果的array
     */
    private JSONArray array;
    
    /**
     * 返回结果的max
     */
    private int max;

    
    public JSONArray getArray() {
        return array;
    }

    
    public void setArray(JSONArray array) {
        this.array = array;
    }

    
    public int getMax() {
        return max;
    }

    
    public void setMax(int max) {
        this.max = max;
    }
    
    
    
}
