package com.renren.ugc.comment.storm.spouts;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
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

/** 
 * @author meng.liu 
 * @date 2014-5-26 下午4:15:27 
 */
public class AlarmTestSpout extends BaseRichSpout {
	
	private static final long serialVersionUID = -1L;
	
	private static final Log logger =
            LogFactory.getLog(AlarmTestSpout.class);
	
	/**
     * spout发送数据
     */
    private SpoutOutputCollector collector;
    
    private LineNumberReader lnr = null;

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		
		System.out.println("==============");
		
		try {
	   		lnr = new LineNumberReader(new FileReader(new File("/data/web/storm/test/alarmtest")));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			System.exit(0);
		}
		
		this.collector = collector;

	}

	@Override
	public void nextTuple() {
		
		if(lnr == null) {
			logger.error("open test file failed");
			return;
		}
		
		try {
			String line = lnr.readLine();
			
			if(StringUtils.isNotBlank(line)) {
				logger.info("read new line:" + line);
			}
			
			if(StringUtils.isNotBlank(line)) {
				String[] parts = line.split(",");
				
				if(parts.length >= 7) {
					String message = parts[0];
					String location = parts[1];
					String business = parts[2];
					String stack = parts[3];
					String threadName = parts[4];
					String ip = parts[5];
					int count = Integer.parseInt(parts[6]);
					
					collector.emit(new Values(message,location,business,stack,threadName,ip,count));
					
				} else {
					logger.error("data format error/////////////////////");
					return;
				}
				
			} 
		} catch (Exception e) {
			logger.error("Error reading tuple \n" + e.getMessage(), e);
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message", "location", "business", "stack","threadName","ip", "count"));
	}

}
