package com.renren.ugc.comment.storm.utils;

public class Constants {
	
	 /**
     * The sms notification api url
     */
    public static final String XIAONEI_SMS_URL = "http://sms.notify.d.xiaonei.com:2000/receiver";
    
    // Mail Alarm
    public static final String ALARM_SMTP_SERVER = "smtp.renren.com";

    public static final String ALARM_SMTP_USERNAME = "chao.wang9@renren-inc.com";

    public static final String ALARM_SMTP_PASSWORD = "Yueguang1030";

    // 平均访问时间阀值
    public static final int ALARM_API_AVG_THRESHOLD = 200;

    // 最大访问时间阀值
    public static final int ALARM_API_MAX_THRESHOLD = 500;

    //异常率阀值,%%
    public static final int ALARM_API_EXCEPTION_RATE_THRESHOLD = 200;
    
    //超时率阀值,%%
    public static final int ALARM_API_TIMEOUT_RATE_THRESHOLD = 300;
    
    //不要报警
    public static final int ALARM_IGNORE_THRESHOLD = Integer.MAX_VALUE;

    // Alarm
    public static final String CK_MAILLIST = "chao.wang9@renren-inc.com";
    public static final String CK_PHONELIST = "13661226467";
    public static final String CK_ALARMTITLE = "commentStorm alarm";
    
   

}
