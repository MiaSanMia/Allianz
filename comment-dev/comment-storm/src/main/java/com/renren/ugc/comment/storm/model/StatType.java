package com.renren.ugc.comment.storm.model;




/**
 * @author xulei
 * @date 2013-8-12
 * @email lei.xu1@renren-inc.com
 * @tags 统计分析的类型，比如调用量、最大响应时间...
 */
public enum StatType {

    /** 调用量 **/
    CALL_COUNT(Config.ALARM_API_COUNT_THRESHOLD, "call count too big", "api_ch"),
    /** 最大响应时间 **/
    MAX_EXEC_TIME(Config.ALARM_API_MAX_TIME_THRESHOLD, "max time too big",
                  "api_maxt"),
    /** 平均响应时间 **/
    AVG_EXEC_TIME(Config.ALARM_API_AVG_TIME_THRESHOLD, "avg time too big",
                  "api_avgt"),
    /** 最小响应时间 **/
    MIN_EXEC_TIME(0, "", "api_mint"),
    /** 每个userid的实时请求数量 **/
    USER_ACCESS_COUNT(0, "", "cnt_uid"),
    /** 每个ip的实时请求数量 **/
    IP_ACCESS_COUNT(0, "", "cnt_ip");

    /** 报警阀值 **/
    private int threshold;
    /** 报警信息 **/
    private String alarmMsg;
    /** redis和内存中存储的key的前缀 **/
    private String keyPrefix;

    private StatType(int threshold, String alarmMsg, String keyPrefix){
        this.threshold = threshold;
        this.alarmMsg = alarmMsg;
        this.keyPrefix = keyPrefix;
    }

    public String getAlarmMsg() {
        return alarmMsg;
    }

    public int getThreshold() {
        return threshold;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

}
