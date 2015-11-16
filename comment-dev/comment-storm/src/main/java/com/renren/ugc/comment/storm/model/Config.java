package com.renren.ugc.comment.storm.model;

/**
 * @author wangxx
 *
 */
public class Config {

    // Redis
    public static final String REDIS_ZK_ADDR = "webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181";

    public static final String REDIS_BIZNAME = "comment";

    /**
     * redis超时时间，单位为s
     * 现在统一的设为1小时
     */
    public static final int REDIS_API_STATISTICS_EXPIRE = 60 * 60;

    // Kafka

    // DB
    public static final String DB_BIZNAME = "dp_storm_analysis";

    public static final String DB_CONNECTION_URL =
            "jdbc:mysql://10.3.20.169:3306/comment_access_analysis";

    public static final String DB_USERNAME = "root";

    public static final String DB_PASSWORD = "root";

    // Mail Alarm
    public static final String ALARM_SMTP_SERVER = "smtp.renren.com";

    public static final String ALARM_SMTP_USERNAME = "platform@renren-inc.com";

    public static final String ALARM_SMTP_PASSWORD = "CY9kYNZA5W";

    // Alarm Threshold
    public static final int ALARM_API_COUNT_THRESHOLD = 100;

    public static final int ALARM_API_MAX_TIME_THRESHOLD = 200;

    public static final int ALARM_API_AVG_TIME_THRESHOLD = 150;

    // Bolt
    /**
     * after how many times the records are updated should flush to redis
     */
    public static final int MCS_API_FLUSH_REDIS_UPDATE_COUNT = 1;
    
    /**
     * after how many times the records are updated should flush to redis
     */
    public static final int MCS_USER_FLUSH_REDIS_UPDATE_COUNT = 5000;
    
    /**
     * after how many times the records are updated should flush to redis
     */
    public static final int MCS_IP_FLUSH_REDIS_UPDATE_COUNT = 5000;

}
