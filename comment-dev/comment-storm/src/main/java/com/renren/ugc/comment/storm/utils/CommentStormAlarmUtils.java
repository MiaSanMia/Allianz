/**
 * <p> @(#)KafkaAlarm.java, 2013-9-18. </p>
 * 
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.storm.utils;

import java.net.URLEncoder;
import org.apache.log4j.Logger;


public class CommentStormAlarmUtils {

	private static final Logger logger = Logger
          			.getLogger(CommentStormAlarmUtils.class);
	private static CommentStormAlarmUtils uniqueInstance = null;

    private static final String reqUrl = Constants.XIAONEI_SMS_URL;
	private static String contentString = null;
	private static final String[] phoneList = Constants.CK_PHONELIST.split(",");
	private static final String title = Constants.CK_ALARMTITLE;
    private static final String[] emailList = Constants.CK_MAILLIST.split(",");
	private CommentStormAlarmUtils() {
	}
	
	private static synchronized void initialize(){
		if (uniqueInstance == null)
			uniqueInstance = new CommentStormAlarmUtils();
	}

	public static CommentStormAlarmUtils getInstance() {
		if (uniqueInstance == null)
			initialize();
		return uniqueInstance;
	}

	public CommentStormAlarmUtils setContent(String content) {
		contentString = content;
		return this;
	}
	public CommentStormAlarmUtils sendEmailAlarm(String content) {
		for (String item:emailList) {
			EmailUtils.getInstance().sendEmail(title, content, item,
		            null);
		}
		return this;
	}

	public CommentStormAlarmUtils sendSMSAlarm() {
        for (String item: phoneList) {
        	HttpUtils.sendGet(reqUrl, splice(item));
            if (logger.isDebugEnabled()) {
        	    logger.debug("send message to " + item + " successfully!");
            }
		}
    	return this;
	}
	
	/**
	 * Description:
	 * 	splice the send message content 
	 * @param number
	 * @return
	 */
	private String splice(String number)
	{
		return "number="+number+"&message="+URLEncoder.encode(contentString);
	}
}
