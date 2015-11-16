package com.renren.ugc.comment.storm.bolts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.storm.alarm.BaseAlarm;
import com.renren.ugc.comment.storm.dao.CommentApiDBWriter;
import com.renren.ugc.comment.storm.model.AlarmStatus;
import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;
import com.renren.ugc.comment.storm.model.Config;
import com.renren.ugc.comment.storm.model.CommentApiObj;
import com.renren.ugc.comment.storm.model.Record;
import com.renren.ugc.comment.storm.model.StatType;
import com.renren.ugc.comment.storm.model.RotateRecordList;
import com.renren.ugc.comment.storm.redis.CommentApiRedisWriter;
import com.renren.ugc.comment.storm.utils.AlarmUtils;
import com.renren.ugc.comment.storm.utils.AsyncExecutor;
import com.renren.ugc.comment.storm.utils.TimeUtils;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ApiExecStatisticsBolt  extends BaseRichBolt {
	
	private static final long serialVersionUID = -1213190970218006188L;

	private static Logger LOG = Logger.getLogger(ApiExecStatisticsBolt.class);

	    public final SimpleDateFormat DB_FORMATTER = new SimpleDateFormat(
	        "yyyy-MM-dd HH:mm");

	    private OutputCollector _collector;

	    /**
	     * api 调用最大时间
	     */
	    //private Record maxTimeRecord = new Record();

	    /**
	     * api 最小调用时间
	     */
	    //private Record minTimeRecord = new Record();

	    /**
	     * api 调用的总时间
	     */
	    //private Record avgTimeRecord = new AvgRecord();

	    /** 记录每个接口在当前小时内当前时间之前被调用的次数 **/
	    //private Record callCountRecord = new Record();

	    private AlarmStatus alarmStatus = new AlarmStatus();

	    /**
	     * The current time. The log entries within this period will be recorded
	     * internally and updated to Redis. If the time has changed, the data will
	     * be flushed into MySQL
	     */
	    private long currentTime;
	    
	    @Override
	    public void prepare(Map stormConf, TopologyContext context,
	        OutputCollector collector) {
	    	currentTime = new Date().getTime();
	        _collector = collector;
	    }

	    @Override
	    public void execute(Tuple input) {
	    	
	    	String type = input.getStringByField("type");
	    	
	    	 String method = input.getStringByField("method");
	    	 int maxTime = input.getIntegerByField("maxTime");
	    	 int avgTime = input.getIntegerByField("avgTime");
	    	 int count = input.getIntegerByField("count");
	    	 int missCount = input.getIntegerByField("missCount");
	    	 int exceptionCount = input.getIntegerByField("exceptionCount");
	    	 int timeoutCount = input.getIntegerByField("timeoutCount");
	    	 String ip = input.getStringByField("ipAddress");
	    	 //format date
	    	 Date formateDate = TimeUtils.formatToMin(new Date(input.getLongByField("date")));
	    	 long date = formateDate.getTime();
	    	 
	    	 CommentAccessLogEntry entry = new CommentAccessLogEntry(type,method,maxTime,avgTime,count,missCount,exceptionCount,timeoutCount,ip,date);
    	 
	    	//System.out.println("method="+method+"|maxTime="+maxTime+"|type:"+type);
	    	
	    	 //1.根据type看看有没有配置
	    	 BaseAlarm alarm = BaseAlarm.getAlarm(type);
	    	 if(alarm != null){
	    		 //根据不同的类型去分析报警
	    		 alarm.sendAlarm(entry);
	    	 }
	    	  
	    	 //报警
	    	 //System.out.println(list.getAvg(method));
	    	 
	    	 //2.异步存db
	    	 writeApiExecStatusToDb(entry);
	    	 
	        // emit something to make it observable in storm UI
	        _collector.emit(new Values('0'));
	    }

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dummy"));
	}
	
	 /**
     * try to send alarm
     */
    private void tryAlarm(String method, long timestamp, long btick,
        long count, long avgTime) {
        // 调用次数报警
        if (count >= StatType.CALL_COUNT.getThreshold()
            && !alarmStatus.hasAlarmed(method, AlarmStatus.API_COUNT)) {
            AlarmUtils.sendAlarm(method, timestamp,
                StatType.CALL_COUNT.getAlarmMsg());
            alarmStatus.markAlarmed(method, AlarmStatus.API_COUNT);
        }

        // 最大响应时间超过临界值就报警
        if (btick >= StatType.MAX_EXEC_TIME.getThreshold()
            && !alarmStatus.hasAlarmed(method, AlarmStatus.API_MAX_EXEC_TIME)) {
            AlarmUtils.sendAlarm(method, timestamp,
                StatType.MAX_EXEC_TIME.getAlarmMsg());
            alarmStatus.markAlarmed(method, AlarmStatus.API_MAX_EXEC_TIME);
        }

        // 平均响应时间超过临界值就报警
        if (avgTime >= StatType.AVG_EXEC_TIME.getThreshold()
            && !alarmStatus.hasAlarmed(method, AlarmStatus.API_AVG_EXEC_TIME)) {
            AlarmUtils.sendAlarm(method, timestamp,
                StatType.AVG_EXEC_TIME.getAlarmMsg());
        }
    }
    
    /**
     * asynchronize write data into Redis
     */
    private void writeApiExecStatusToRedis(String method, int count,
        int maxTime, int minTime, int avgTime, Date date) {
        CommentApiObj obj = new CommentApiObj();
        obj.setApi(method);
        obj.setMaxTime(maxTime);
        obj.setAvgTime(avgTime);
        obj.setMinTime(minTime);
        obj.setCount(count);
        obj.setTimePeriod(date);
        CommentApiRedisWriter writer = new CommentApiRedisWriter(obj);
        AsyncExecutor.getInstance().execute(writer);
    }
    
    /**
     * Asynchronized write api exec status to db
     */
    private void writeApiExecStatusToDb(CommentAccessLogEntry obj) {
        CommentApiDBWriter writer = new CommentApiDBWriter(obj);
        AsyncExecutor.getInstance().execute(writer);
    }
    
    /**
     * reset all the records
     * 
     * @param method
     */
//    private void resetRecords(String method) {
//        maxTimeRecord.remove(method);
//        minTimeRecord.remove(method);
//        avgTimeRecord.remove(method);
//        callCountRecord.remove(method);
//    }

    private void resetAlarm(String method) {
        alarmStatus.resetAlarm(method, AlarmStatus.API_COUNT);
        alarmStatus.resetAlarm(method, AlarmStatus.API_MAX_EXEC_TIME);
        alarmStatus.resetAlarm(method, AlarmStatus.API_AVG_EXEC_TIME);
    }

}