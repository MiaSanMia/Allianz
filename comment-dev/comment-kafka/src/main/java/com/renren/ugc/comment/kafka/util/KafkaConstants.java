package com.renren.ugc.comment.kafka.util;

/**
 * Created with IntelliJ IDEA.
 * User: jiankuan
 * Date: 16/12/13
 * Time: 16:22
 * Some constant definition for kafka
 *  */
public class KafkaConstants {

    public static final String KAFKA_CONFIG_FILE = "kafka-config.properties";

    public static final long CONSUMER_RECONNECT_INTERVAL_MS = 10000; // 10sec

    /**
     * The sms notification api url
     */
    public static final String XIAONEI_SMS_URL = "http://sms.notify.d.xiaonei.com:2000/receiver";
}
