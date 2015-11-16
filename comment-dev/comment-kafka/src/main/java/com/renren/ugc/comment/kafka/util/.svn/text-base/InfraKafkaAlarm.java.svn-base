/**
 * <p> @(#)KafkaAlarm.java, 2013-9-18. </p>
 * 
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.kafka.util;

import java.net.URLEncoder;
import org.apache.log4j.Logger;


/**
 * 
 * @author wmc
 * 
 */
public class InfraKafkaAlarm {

	private static final Logger logger = Logger
          			.getLogger(InfraKafkaAlarm.class);
	private static InfraKafkaAlarm uniqueInstance = null;

    private static final String reqUrl = KafkaConstants.XIAONEI_SMS_URL;
	private static String contentString = null;
	private static final String[] phoneList = InfraKafkaUtil.CK_PHONELIST.split(",");
	private static final String title = InfraKafkaUtil.CK_ALARMTITLE;
    private static final String[] emailList = InfraKafkaUtil.CK_MAILLIST.split(",");
	private InfraKafkaAlarm() {
	}
	
	private static synchronized void initialize(){
		if (uniqueInstance == null)
			uniqueInstance = new InfraKafkaAlarm();
	}

	public static InfraKafkaAlarm getInstance() {
		if (uniqueInstance == null)
			initialize();
		return uniqueInstance;
	}

	public InfraKafkaAlarm setContent(String content) {
		contentString = content;
		return this;
	}
	public InfraKafkaAlarm sendEmailAlarm() {
		for (String item:emailList) {
			EmailUtils.getInstance().sendEmail(title, contentString, item,
		            null);
		}
		return this;
	}

	public InfraKafkaAlarm sendSMSAlarm() {
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
