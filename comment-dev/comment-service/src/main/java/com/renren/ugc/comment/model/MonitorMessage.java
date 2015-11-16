package com.renren.ugc.comment.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.renren.ugc.comment.statistics.DeltaRecord;

public class MonitorMessage {
	
	public static final String THREAD_QUEUE_HEAD = "THREAD_QUEUE";

	private static String SEPARATOR = "|";
	
	private static String localhost;
	
	static{
		try {
			localhost = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			localhost = "";
		}
	}

	private String head;

	private String apiName;

	private long maxTime;

	private long avgTime;

	private long count;

	private long missedCount;
	
	private long exceptionCount;
	
	private long timeoutCount;

	public MonitorMessage(String head, String apiName, long maxTime,
			long avgTime, long count, long missedCount,long exceptionCount,long timeoutCount) {
		this.setApiName(apiName);
		this.setAvgTime(avgTime);
		this.setHead(head);
		this.setMaxTime(maxTime);
		this.setCount(count);
		this.setMissedCount(missedCount);
		this.setExceptionCount(exceptionCount);
		this.setTimeoutCount(timeoutCount);
	}

	public MonitorMessage(DeltaRecord deltaRecord) {
		this(deltaRecord.getType().toString(), deltaRecord.getName(),
				deltaRecord.getMaxTime(), deltaRecord.getAvgTime(), deltaRecord
						.getCount(), deltaRecord.getMissCount(),deltaRecord.getExceptionCount(),deltaRecord.getTimeoutCount());
	}
	
	public MonitorMessage(String head,String apiName,int count){
		this.head = head;
		this.apiName = apiName;
		this.count = count;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(head);
		sb.append(SEPARATOR);
		sb.append(apiName);
		sb.append(SEPARATOR);
		sb.append(maxTime);
		sb.append(SEPARATOR);
		sb.append(avgTime);
		sb.append(SEPARATOR);
		sb.append(count);
		sb.append(SEPARATOR);
		sb.append(missedCount);
	    sb.append(SEPARATOR);
	    sb.append(exceptionCount);
	    sb.append(SEPARATOR);
	    sb.append(timeoutCount);
	    sb.append(SEPARATOR);
	    sb.append(localhost);
	    sb.append(SEPARATOR);
	    sb.append(new Date().getTime());
		return sb.toString();
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(long avgTime) {
		this.avgTime = avgTime;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getMissedCount() {
		return missedCount;
	}

	public void setMissedCount(long missedCount) {
		this.missedCount = missedCount;
	}

	public long getExceptionCount() {
		return exceptionCount;
	}

	public void setExceptionCount(long exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

	public long getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(long timeoutCount) {
		this.timeoutCount = timeoutCount;
	}

	public static void main(String[] args){
		System.out.println("in");
		MonitorMessage m = new MonitorMessage("fff", "createComment", 1, 311, 111, 123,123,123);
		System.out.println(m.toString());
		System.out.println("end");
	}
}
