/**
 * <p> @(#)Consumer.java, 2013-9-25. </p>
 *
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.kafka.example;

import com.renren.ugc.comment.kafka.exception.InfraKafkaException;


/**
 * @author wmc
 */
public class Consumer {


    //example
    public static void main(String[] args) throws InterruptedException {
        String topic = "testComment";
        for (int i = 0; i != 5; ++i) {
            ConsumerThread consumerThread = null;
            try {
                consumerThread = new ConsumerThread(i, topic, "test-group");
            } catch (InfraKafkaException e) {
                e.printStackTrace();
            }
            consumerThread.start();
        }
    }
}
