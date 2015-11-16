package com.renren.ugc.comment.storm.alarm.gauss;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.ugc.comment.storm.alarm.AlarmService;


/**	每天算定时任务
 * @author meng.liu
 *
 */
public class GaussDaily {
	private static final Log logger = LogFactory.getLog(GaussDaily.class);
	@Autowired
	private AlarmService alarmService;
	
	
	protected void work() {
		try{
			alarmService.dailyCalculation();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
}
