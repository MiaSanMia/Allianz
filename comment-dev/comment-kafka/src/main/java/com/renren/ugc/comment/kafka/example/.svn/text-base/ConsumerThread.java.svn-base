/**
 * <p> @(#)ConsumerThread.java, 2013-9-29. </p>
 * 
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.kafka.example;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;

/**
 * @author wmc
 */
public class ConsumerThread extends Thread {
    private final Logger logger = Logger.getLogger(ConsumerThread.class);
    private InfraKafkaHighConsumer consumer = null;
    private int threadNum;

    public ConsumerThread(int number, String topic, String group) throws InfraKafkaException {
        consumer = new InfraKafkaHighConsumer(topic, group);
        threadNum = number;
    }

    public void run() {

        logger.info("threadNum: " + threadNum + " begin to start!");

        while (true) {
            String result = consumer.getMessage();
            logger.info("get message:" + result);
            if (result.equals("exit")) {
                break;
            }
        }
    }

}
