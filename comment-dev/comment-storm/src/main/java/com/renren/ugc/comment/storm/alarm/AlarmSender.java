package com.renren.ugc.comment.storm.alarm;

import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.storm.alarm.strategy.AlarmConfig;
import com.renren.ugc.comment.storm.bolts.AlarmBolt;
import com.renren.ugc.comment.storm.utils.HttpUtils;

/** 
 * @author meng.liu 
 * @date 2014-5-23 下午6:09:50 
 */
public class AlarmSender {
	
	private static final String URL = "http://sms.notify.d.xiaonei.com:2000/receiver";
	
	private static Logger logger = Logger.getLogger(AlarmSender.class);
	
	public static void sendSmsAlarm(AlarmConfig config,String message) {
		
		logger.info(String.format("send sms alarm, message[%s],phone[%s]",message,
				config.getPhoneList().get(0)));
		
		
		List<String> receiverList = config.getPhoneList();
		for(String num : receiverList) {
			String param = "number=" + num + "&message=" + URLEncoder.encode(message);
			HttpUtils.sendGet(URL, param);
		}
		
	}
	
	public static void sendEmailAlarm(AlarmConfig config,String message) {
		
	}

}
