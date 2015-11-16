/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.kafka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.renren.ugc.comment.kafka.util.KafkaConstants;
import com.renren.ugc.comment.kafka.util.Validators;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.kafka.exception.InfraKafkaExceptionMonitor;
import com.renren.ugc.comment.kafka.log.ClientLogger;
import com.renren.ugc.comment.kafka.util.KafkaConfigLoader;

import kafka.javaapi.producer.ProducerData;
import kafka.message.Message;
import kafka.producer.ProducerConfig;

/**
 * @author yueqiang.zheng<tjuxiaoqiang@163.com>
 * @since  2014-2-7
 */
public class InfraKafkaProducer {

	private static final Logger logger = ClientLogger.getLog();

	private static InfraKafkaExceptionMonitor infraKafkaExceptionMonitor = null;
	
	/**
	 * 默认重试次数
	 */
	private static final int DEFAULT_RETRY_TIMES = 1;
	
	/**
	 * 默认超时时间
	 */
	private static final int DEFAULT_TIMEOUT = 3000;


	private kafka.javaapi.producer.Producer<String, Message> kproducer;

    private volatile boolean suspend;

	public InfraKafkaProducer() throws InfraKafkaException {

        logger.info("setup exception monitor");
        infraKafkaExceptionMonitor = new InfraKafkaExceptionMonitor();
		infraKafkaExceptionMonitor.start(120, 60, 100,
				"Comment kafka producer alarm");

		try {
			kproducer = new kafka.javaapi.producer.Producer<String, Message>(
					createProducerConfig());
		} catch (kafka.common.InvalidConfigException e) {
			monitorException(
                    new InfraKafkaException(
                            "KafkaException:The given config parameter has invalid values!",
                            e), "");
		} catch (kafka.common.UnavailableProducerException e) {
			monitorException(
                    new InfraKafkaException(
                            "KafkaException:The producer pool cannot be initialized !",
                            e), "");
		}
	}

	private ProducerConfig createProducerConfig() throws InfraKafkaException {
		Properties props = KafkaConfigLoader.loadPropertyFile(
                this.getClass().getClassLoader().getResourceAsStream(KafkaConstants.KAFKA_CONFIG_FILE));
		ProducerConfig resultConfig = null;
		try {
			resultConfig = new ProducerConfig(props);
		} catch (kafka.common.InvalidConfigException e) {
			monitorException(
                    new InfraKafkaException(
                            "KafkaException:The given config parameter has invalid values!",
                            e), "");
		} catch (kafka.common.UnavailableProducerException e) {
			monitorException(
                    new InfraKafkaException(
                            "KafkaException:The producer pool cannot be initialized !",
                            e), "");
		} catch (Exception e) {
			monitorException(new InfraKafkaException(
                    "KafkaException:Other exception!", e), "");
		}
		return resultConfig;
	}

	private void monitorException(InfraKafkaException e, String alarmString) {
		infraKafkaExceptionMonitor.recordException(e, alarmString);

	}

	private List<Message> constructMessageList(List<String> data) {
		List<Message> result = new ArrayList<Message>();
		for (String msgStr: data) {
			result.add(new Message(msgStr.getBytes()));
		}
		return result;
	}

    public boolean sendMessage(String topic, String key, String msgData) throws InfraKafkaException {
    	Validators.checkMessage(topic, msgData);
        List<String> msgDataList = new ArrayList<String>(1);
        msgDataList.add(msgData);
        return sendMessage(topic, key, msgDataList);
    }

	public boolean sendMessage(String topic, String key, List<String> msgDataList) {
		List<Message> msgs = constructMessageList(msgDataList);
		final long beginTimestamp = System.currentTimeMillis();
		long endTimestamp = beginTimestamp;
		
		for (int times = 0; times <= DEFAULT_RETRY_TIMES && (endTimestamp - beginTimestamp) < DEFAULT_TIMEOUT; times++) {
			try {
				if (suspend) {
					return false;
				}

				ProducerData<String, Message> data = new ProducerData<String, Message>(
						topic, key, msgs);
				kproducer.send(data);
				endTimestamp = System.currentTimeMillis();
				return true;

			} catch (kafka.common.NoBrokersForPartitionException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:No brokers exists, please restart brokers!";
				monitorException(new InfraKafkaException(errorMsg, e), "");
				logger.error(errorMsg, e);
				continue;

			} catch (kafka.producer.async.QueueFullException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:Async queue full,please reset parameters!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;

			} catch (kafka.producer.async.QueueClosedException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:The broker is close!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;

			} catch (kafka.producer.async.IllegalQueueStateException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:The given config parameter has invalid value!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;
			} catch (kafka.producer.async.AsyncProducerInterruptedException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:The producer is been interrupted!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;

			} catch (kafka.producer.async.MissingConfigException e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:Missing config parameters!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;
			} catch (Exception e) {
				endTimestamp = System.currentTimeMillis();
				String errorMsg = "KafkaException:Send Message exception!";
				logger.error(errorMsg, e);
				monitorException(new InfraKafkaException(errorMsg, e), "");
				continue;
			}
		}
		
		return false;
	}

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }
}
