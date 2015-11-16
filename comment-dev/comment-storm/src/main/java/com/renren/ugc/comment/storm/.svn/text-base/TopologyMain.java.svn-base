package com.renren.ugc.comment.storm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.storm.bolts.ApiExecStatisticsBolt;
import com.renren.ugc.comment.storm.spouts.KafkaConsumerSpout;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * @author wangxx
 * 
 *  comment topology
 *
 */
public class TopologyMain {
	private static final Log logger =
            LogFactory.getLog(TopologyMain.class);

    public static void main(String[] args) throws InterruptedException {

        // Topology definition
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-consumer", new KafkaConsumerSpout(),1);
        builder.setBolt("comment-api-counter", new ApiExecStatisticsBolt(), 4).fieldsGrouping(
                "kafka-consumer", new Fields("method"));
        System.out.println("start");

        // Configuration
        Config conf = new Config();
        conf.setDebug(false);
        // Topology run,进程数
        conf.setNumWorkers(1);
        try {
            StormSubmitter.submitTopology("comment-monitor-topology", conf,
                builder.createTopology());
        } catch (Exception e) {
            logger.error("StormSubmitter.submitTopology error. e=" + e);
        }
    }
}
