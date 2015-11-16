/**
 * <p> @(#)Producer.java, 2013-9-25. </p>
 * 
 * Copyright 2013 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.kafka.example;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.kafka.producer.InfraKafkaProducer;

/**
 * 
 * @author wmc
 * The producer demo
 */


public class Producer {
	//example
	private static Logger logger = Logger.getLogger(Producer.class);
	
	public static void main(String[] args) {
		String topic = "create_comment";
		InfraKafkaProducer producerHandler = null;
		
		try {
			producerHandler = new InfraKafkaProducer();
		} catch (InfraKafkaException e2) {
			e2.printStackTrace();
		}
		
		try {
			int messageNo = 1;
			while (true) {
			    String messageString = "test-message"
						+ Integer.toString(messageNo);

				if (producerHandler.sendMessage(topic, String.valueOf(String.valueOf(messageNo)), messageString)) {
					messageNo++;


                    logger.info("Successfully send messages "+ messageString);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
				}
			}
		} catch (InfraKafkaException e) {
			e.printStackTrace();
		}
	}
}