package com.renren.ugc.comment.storm.spouts;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;
import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;

public class KafkaConsumerSpout extends BaseRichSpout {

    private static final Log logger =
            LogFactory.getLog(KafkaConsumerSpout.class);

    /**
     * 
     */
    private static final long serialVersionUID = 2758185210740560930L;
    /**
     * spout发送数据
     */
    private SpoutOutputCollector collector;
    /**
     * kafka消息客户端
     */
   // private static InfraKafkaConsumerApi consumerHandler = null;
    private static InfraKafkaHighConsumer consumer = null;

    /**
     * comment kafka消息队列的topic名称
     */
    private static final String TOPIC = "comment-test-monitor";
    
    private static final String COMMENT_GROUP = "comment_monitor";
    
    private static long count = 0;

    /**
     * The only thing that the methods will do It is emit each file line
     */
    @Override
    public void nextTuple() {
        /**
         * The nextuple it is called forever
         */
    	if(consumer == null){
    		logger.error("kafka consumer is null");
    		return;
    	}
    	try{
	    		String str = consumer.getMessage();
	    		if(++count % 50 == 0){
	    			System.out.println("kafka receive|"+str);
	    		}
	    		
	    		CommentAccessLogEntry entry = new CommentAccessLogEntry(str);
	    		collector.emit(new Values(entry.getType(),entry.getMethod(),entry.getMaxTime(),entry.getAvgTime(),entry.getCount(),entry.getMissCount(),entry.getExceptionCount(),
	    				entry.getTimeoutCount(),entry.getIpAddress(),entry.getDate()));
    	} catch (Exception e){
    		logger.error("Error reading tuple", e);
    	}
    }

    /**
     * We will create the file and get the collector object
     */
    @Override
    public void open(Map conf, TopologyContext context,
        SpoutOutputCollector collector) {

    	try{
			consumer = new InfraKafkaHighConsumer(TOPIC, COMMENT_GROUP);
    	} catch(InfraKafkaException e){
    		 logger.error("kafka start fail!!" + e);
    		 throw new RuntimeException("kafka start fail!!" + e);
    	}
        this.collector = collector;
    }

    /**
     * Declare the output field "word"
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declare(new Fields("type", "method", "maxTime", "avgTime", "count", "missCount", "exceptionCount","timeoutCount",
				"ipAddress", "date"));
    }

    public static void main(String[] args) {

    }
}
