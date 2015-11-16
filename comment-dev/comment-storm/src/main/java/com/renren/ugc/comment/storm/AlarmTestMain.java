package com.renren.ugc.comment.storm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.renren.ugc.comment.storm.alarm.AlarmService;
import com.renren.ugc.comment.storm.bolts.AlarmBolt;
import com.renren.ugc.comment.storm.spouts.AlarmTestSpout;

/** 
 * @author meng.liu 
 * @date 2014-5-26 下午4:47:46 
 */
public class AlarmTestMain {
	
	private static final Log logger =
            LogFactory.getLog(TopologyMain.class);

    public static void main(String[] args) throws InterruptedException {

        // Topology definition
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("alarm-file-lineReader", new AlarmTestSpout(),1);
        builder.setBolt("comment-alarm-tester", new AlarmBolt(), 4).fieldsGrouping(
                "alarm-file-lineReader", new Fields("message"));
        System.out.println("-----------------start-------------------");

        // Configuration
        Config conf = new Config();
        conf.setDebug(true);
        // Topology run,进程数
        conf.setNumWorkers(1);
        try {
            StormSubmitter.submitTopology("comment-alarm-topology", conf,
                builder.createTopology());
        } catch (Exception e) {
            logger.error("StormSubmitter.submitTopology error. e=" + e);
        }
    /*    
        try {
        	
			new Thread(new Runnable() {
				public void run() {
					AlarmService.start();
				}
			}).start();
			
			System.out.println("gauss info calculation has been startedddddddddddd");
			
		} catch (Exception e) {
			logger.error("start calculate gaussVO failedddddddddddddd");
		}*/
        
        
    }

}
