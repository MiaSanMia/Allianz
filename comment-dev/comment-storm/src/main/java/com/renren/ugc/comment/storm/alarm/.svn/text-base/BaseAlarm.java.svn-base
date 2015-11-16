package com.renren.ugc.comment.storm.alarm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;
import com.renren.ugc.comment.storm.model.RotateRecordList;
import com.renren.ugc.comment.storm.utils.CommentStormAlarmUtils;

/**
 * @author wangxx
 * 
 * 报警抽象基类
 * 对于不同的报警，可能关注的点不一样，比如cache报警，不会关注count值(访问次数)，但是对于线程报警，我们会关注count值
 * 因此抽象出了avg,max,exception,count等值交给各个子类自己去实现
 *
 */
public abstract class BaseAlarm {
	
	private static final int PRINT_DELAY = 5 * 60 * 1000;
	
	private static final int ALARM_DELAY = 5 * 60 * 1000;
	
	//平均时间list
	public static final RotateRecordList avgList = new RotateRecordList();

	//最大时间list
	public static final RotateRecordList maxList = new RotateRecordList();

	//异常率list
	public static final RotateRecordList exceptionRateList = new RotateRecordList();
	
	//超时率list
	public static final RotateRecordList timeoutRateList = new RotateRecordList();

	//数量list
	public static final RotateRecordList countList = new RotateRecordList();

	//alarm 配置config
	private static final Map<String, BaseAlarm> maps = new HashMap<String, BaseAlarm>();
	
	//报警缓存，保证每隔5min才会触发一个method的报警
	private static final ConcurrentHashMap<String /*method*/,Boolean> alarmTimeMaps = new ConcurrentHashMap<String,Boolean>();
	
	//每隔5min打印出这些list
	private static final ScheduledExecutorService scheduledExecutorService = Executors
	        .newSingleThreadScheduledExecutor(new ThreadFactory() {
	            @Override
	            public Thread newThread(Runnable r) {
	                return new Thread(r, "BaseAlarmPrint");
	            }
	        });

	static {
		maps.put("COMMENT_CACHE", new CommentCacheAlarm());
		maps.put("COMMENT_API", new CommentAPIAlarm());
		maps.put("EXTERNAL_API", new ExternalAPIAlarm());
		maps.put("THREAD_QUEUE", new ThreadQueueAlarm());
		maps.put("GET_ENTRY", new GetEntryAlarm());
		
		//定时打印
		scheduledExecutorService.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				
				System.out.println("avgList="+avgList.toString());
				System.out.println("maxList="+maxList.toString());
				System.out.println("exceptionRateList="+exceptionRateList.toString());
				System.out.println("countList="+countList.toString());
				System.out.println("timeoutRateList="+timeoutRateList.toString());
				
			}
			
		}, PRINT_DELAY,PRINT_DELAY, TimeUnit.MILLISECONDS);
		
		//定时清报警标志
		scheduledExecutorService.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				alarmTimeMaps.clear();
			}
			
		}, ALARM_DELAY,ALARM_DELAY, TimeUnit.MILLISECONDS);
		
	}

	public static BaseAlarm getAlarm(String key) {
		return maps.get(key);
	}

	//public abstract String getServiceName();

	public abstract void shouldSendAlarm();
	
	public abstract int getAvgThreshold();
	
	public abstract int getMaxThreshold();
	
	public abstract int getExceptionThreshold();
	
	public abstract int getTimoutThreshold();
	
	public abstract int getCountThreshold();

	/*----------------------------------------------------------------------------*/
	/*---------------------------------主方法---------------------------------*/
	/*----------------------------------------------------------------------------*/
	public boolean sendAlarm(CommentAccessLogEntry entry) {

		// 1.change time
		long date = entry.getDate();
		String method = entry.getMethod();
		
		//2.add to list and get count
		double avgCount = 0 ,maxCount = 0,exceptionCount = 0,countCount = 0,timeoutCount = 0;
		boolean avgAlarm = false ,maxAlarm = false , exceptionAlarm = false,countAlarm = false,timeoutAlarm = false;
		
		//avg
		if(getAvgThreshold() != 0){
			avgList.incr(method, entry.getAvgTime(), date);
			avgCount = avgList.getAvg(method);
			avgAlarm = avgCount > getAvgThreshold();
		}
		//max
		if(getMaxThreshold() != 0){
			maxList.incr(method, entry.getMaxTime(), date);
			maxCount = maxList.getAvg(method);
			maxAlarm = maxCount > getMaxThreshold();
		}
		//exceptionRate
		if(getExceptionThreshold() > 0){
			if(entry.getCount() > 0){
				exceptionRateList.incr(method, entry.getExceptionCount() * 10000 / entry.getCount(), date);
			}
			exceptionCount = exceptionRateList.getAvg(method);
			exceptionAlarm = exceptionCount > getExceptionThreshold();
		}
		//timeoutRate
		if(getTimoutThreshold() > 0){
			if(entry.getCount() > 0){
				timeoutRateList.incr(method, entry.getTimeoutCount() * 10000 / entry.getCount(), date);
			}
			timeoutCount = timeoutRateList.getAvg(method);
			 timeoutAlarm = timeoutCount > getTimoutThreshold();
		}
		//count
		if(getCountThreshold() > 0){
			countList.incr(method, entry.getCount(), date);
			countCount = countList.getAvg(method);
			countAlarm = countCount > getCountThreshold();
		}

		// 3.build alarm body,以方法为单位报警
		StringBuilder alarmSb = new StringBuilder();
		
		//add method name
		if(avgAlarm | maxAlarm | exceptionAlarm | countAlarm | timeoutAlarm){
			 alarmSb.append(method);
		}
		
		//add avg
		alarmSb.append(avgAlarm ? ",avg is " + avgCount : "");
		//add max
		alarmSb.append(maxAlarm ? ",max is " + maxCount : "");
		//add count
		alarmSb.append(countAlarm ? ",count is "+ countCount : "");
		//add timeoutRate
		alarmSb.append(timeoutAlarm ? ",timeoutRate is " + timeoutCount : "");
		// add exceptionRate
		alarmSb.append(exceptionAlarm ? ",exceptionRate is " + exceptionCount : "");

		if (alarmSb.length() > 0 && needSendAlarm(method)) {
			System.out.println("alarm = "+alarmSb.toString());
			//CommentStormAlarmUtils.getInstance().sendEmailAlarm(alarmSb.toString());
		}
		return false;
	}
	
	//只发一次报警
	private boolean needSendAlarm(String method){
		
		if(alarmTimeMaps.containsKey(method)){
			return false;
		} else {
			Boolean ret = alarmTimeMaps.putIfAbsent(method, Boolean.TRUE);
			if(ret == null){
				return true;
			}
		}
		return false;
	}

}
