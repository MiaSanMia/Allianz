package com.renren.ugc.comment.storm.model;




/**
 * @author wangxx
 *
 */
public class CommentAccessLogEntry {
    
    /**
     * @param line
     * 
     * format as :
     * EXTERNAL_API|replace-ubb|{maxTime}|{avgTime}|{count}|{missCount}|{exceptionCount}|{timeoutCount}|{localhost}|{date}
     */
    public CommentAccessLogEntry(String line){
        String[] sections = line.split("\\|");
        if (sections.length != 10) {
            throw new IllegalArgumentException(
                "Can't convert input line into log entry: " + line);
        }
       
        type = sections[0];
        method = sections[1];
        maxTime = Integer.parseInt(sections[2]);
        avgTime = Integer.parseInt(sections[3]);
        count = Integer.parseInt(sections[4]);
        missCount = Integer.parseInt(sections[5]);
        exceptionCount = Integer.parseInt(sections[6]);
        timeoutCount = Integer.parseInt(sections[7]);
        ipAddress = sections[8];
        date = Long.parseLong(sections[9]);

    }
    
    public CommentAccessLogEntry(String type,String method,int maxTime,int avgTime,int count,int missCount,int exceptionCount,int timeoutCount,String ipAddress,long date){
    	this.type = type;
    	this.method = method;
    	this.maxTime = maxTime;
    	this.avgTime = avgTime;
    	this.count = count;
    	this.missCount = missCount;
    	this.exceptionCount = exceptionCount;
    	this.timeoutCount = timeoutCount;
    	this.ipAddress = ipAddress;
    	this.date = date;
    }

    private final String type;
    private final String method;
    private final int maxTime ;
    private final int avgTime;
    private final int count;
    private final int missCount;
    private final int exceptionCount;
    private final int timeoutCount;
    private final String ipAddress;
    private final long date;

	public String getType() {
		return type;
	}
	public String getMethod() {
		return method;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public int getAvgTime() {
		return avgTime;
	}
	public int getCount() {
		return count;
	}
	public int getMissCount() {
		return missCount;
	}
	public int getExceptionCount() {
		return exceptionCount;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public long getDate() {
		return date;
	}
	public int getTimeoutCount() {
		return timeoutCount;
	}

}
