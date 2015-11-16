/**
 * 
 */
package com.renren.pillar.common.model;


/**
 * @author zhang.liang
 * @createTime 2014-5-15 下午3:00:14
 *
 */
public enum LogType {

    ERROR_LOG("ErrorLog"),
    
    ACCESS_LOG("AccessLog");
    
    private LogType(String name){
        this.name = name;
    }
    
    public String name;
    
    public static LogType getLogTypeByName(String name){
    	if(ERROR_LOG.name.equals(name)){
    		return ERROR_LOG;
    	}if(ACCESS_LOG.name.equals(name)){
    		return ACCESS_LOG;
    	}else{
    		return ERROR_LOG;
    	}
    }
}
