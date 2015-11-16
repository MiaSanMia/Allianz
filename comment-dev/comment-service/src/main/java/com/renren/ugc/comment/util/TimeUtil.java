package com.renren.ugc.comment.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author wangxx
 *
 * 时间处理类
 */
public class TimeUtil {
	
	/**
	 * 一天的时间(ms)
	 */
	private static long oneDayTimeMS = 24 * 60 * 60 * 1000;
	
	private static long eightHourTimeMS = 8 * 60 * 60 * 1000;
	
	/**
	 * @return 返回第2天00:00:00的时间戳，单位是秒
	 */
	public static int getTomorrowTime(){
		
		//1.get current time
		long currentTime = new Date().getTime();
		
		//2.得到天数，为什么加eightHourTimeMS?因为时间戳是相对于1970-01-01 08:00:00东八区的时间
		long day = (currentTime+ eightHourTimeMS) / oneDayTimeMS;
		
		int result = (int)(((day + 1) * oneDayTimeMS - eightHourTimeMS ) / 1000);
		
		return result;
	}

}
