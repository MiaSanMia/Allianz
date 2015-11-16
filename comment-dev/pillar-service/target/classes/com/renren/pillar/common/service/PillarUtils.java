/**
 * 
 */
package com.renren.pillar.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.renren.pillar.common.model.BaseModel;


/**
 * @author zhang.liang
 * @createTime 2014-5-14 上午11:20:18
 *
 */
public class PillarUtils {

    public static final String SEPERATOR = "###";
    
    public static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
     
    /**
     * 第一个时间是否晚于第二个时间                        
     * 
     * @param timeCurrent
     * @param timeOld
     * @return
     */
    public static boolean isLater(String timeCurrent, String timeOld){
        return timeCurrent.compareTo(timeOld) > 0;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> change(List<BaseModel> list) {
        List<T> r = new ArrayList<T>();
        for (BaseModel t : list) {
            r.add((T) t);
        }
        return r;
    }
    
    
}