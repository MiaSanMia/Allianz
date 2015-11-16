/**
 * 
 */
package com.renren.pillar.common.model;



/**
 * @author zhang.liang
 * @createTime 2014-5-15 上午11:11:12
 *
 */
public class ErrorLog extends BaseModel{
   
    private static final long serialVersionUID = 4133715112736697358L;

    private String message;
    
    /** classname + method + codeline */
    private String  location;
    
    private String  threadName;
    
    /** exception stack info */
    private String  stack;
     
    private int count;
     
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }

    
    public String getLocation() {
        return location;
    }

    
    public void setLocation(String location) {
        this.location = location;
    }

    
    public String getThreadName() {
        return threadName;
    }

    
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    
    public String getStack() {
        return stack;
    }

    
    public void setStack(String stack) {
        this.stack = stack;
    }
 
    public int getCount() {
        return count;
    }
 
    public void setCount(int count) {
        this.count = count;
    }

    public LogType getLogType() {
        return LogType.ERROR_LOG; 
    } 

	
    
    
    
    
}
