/**
 * 
 */
package com.renren.pillar.common.service;

import com.renren.pillar.common.model.AccessLog;
import com.renren.pillar.common.model.ErrorLog;
import com.renren.pillar.common.model.LogType;


/**
 * @author zhang.liang
 * @createTime 2014-5-21 下午6:14:08
 *
 */
public class BaseServiceFactory {
        
    private static BaseService<ErrorLog> errorLogService = new BaseService<ErrorLog>();
     
    private static BaseService<AccessLog> accessLogService = new BaseService<AccessLog>();
    
    @SuppressWarnings("rawtypes")
    public static BaseService getBaseService(LogType logType){

        if(LogType.ERROR_LOG == logType){
            return errorLogService;
        }else if(LogType.ACCESS_LOG == logType){
            return accessLogService;
        }else{
            return null;
        }
    }
    
}
