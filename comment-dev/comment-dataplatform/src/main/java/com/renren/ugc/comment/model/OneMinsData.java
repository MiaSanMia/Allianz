package com.renren.ugc.comment.model;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wangxx
 * 
 *  每分钟打印的数据
 */
public class OneMinsData {
    
    private long nowTime;
    
    private String nowDate;
    
    private Map<String,OneLineData> dataMaps;
    
    public OneMinsData(long nowTime,String nowDate){
        this.nowTime = nowTime;
        this.nowDate = nowDate;
        dataMaps = new HashMap<String,OneLineData>();
    }

    
    public long getNowTime() {
        return nowTime;
    }

    
    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    
    public String getNowDate() {
        return nowDate;
    }

    
    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    
    public Map<String, OneLineData> getDataMaps() {
        return dataMaps;
    }

    
    public void setDataMaps(Map<String, OneLineData> dataMaps) {
        this.dataMaps = dataMaps;
    }
    
    

}
