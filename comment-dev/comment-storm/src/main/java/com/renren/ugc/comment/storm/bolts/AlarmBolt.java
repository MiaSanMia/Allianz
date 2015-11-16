package com.renren.ugc.comment.storm.bolts;

import java.util.Map;


import org.apache.log4j.Logger;

import com.renren.ugc.comment.storm.alarm.AlarmSender;
import com.renren.ugc.comment.storm.alarm.AlarmService;
import com.renren.ugc.comment.storm.alarm.strategy.AlarmConfig;
import com.renren.ugc.comment.storm.alarm.strategy.BusinessType;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/** 
 * @author meng.liu 
 * @date 2014-5-23 下午4:35:36 
 */
public class AlarmBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = -1213190970218006187L;
	private static Logger logger = Logger.getLogger(AlarmBolt.class);
	private OutputCollector _collector;
	
//	private AlarmService alarmService;
	
	@Override
	public void execute(Tuple input) {
		
		String message = input.getStringByField("message");
		String location = input.getStringByField("location");
		String threadName = input.getStringByField("threadName");
		String stack = input.getStringByField("stack");
		
		String business = input.getStringByField("business");
		String ip = input.getStringByField("ip");
		
		int count = input.getIntegerByField("count");
		
		AlarmConfig config = getAlarmConfig(business);
		
		
		logger.info(String.format("alarm bolt,input msg: message[%s],location[%s],threadName[%s],stack[%s]," +
				"business[%s],ip[%s],count[%d]", message,location,threadName,stack,business,ip,count));
		
		
		if(AlarmService.isNeedAlarm(message, location, business, config, count)) {
			String message2Send = buildSmsMessage(message, location, business, stack,count);
			AlarmSender.sendSmsAlarm(config, message2Send);
		}
		
		_collector.emit(new Values(message,business,location,stack,ip,count));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message","business","location","stack","ip","count"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
//		RoseAppContext rose = new RoseAppContext();
//		alarmService = rose.getBean(AlarmServiceImpl.class);
		
	}
	
	private AlarmConfig getAlarmConfig(String business) {
		return AlarmConfig.getAlarmConfig(BusinessType.getBusinessType(business)); 
	}
	
	private String buildSmsMessage(String message, String location, String business,String stack, int count) {
		return business + "|" + location + "|" + stack + "|" + analyzeProblem(message);
	}
	
	/**
	 * 根据错误MESSAGE分析出问题的业务，如xce,db等
	 * @param message
	 * @return
	 */
	private String analyzeProblem(String message) {
		
		return message;
		
	}
	

}
