package com.renren.ugc.comment.storm;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/** 
 * @author meng.liu 
 * @date 2014-5-21 下午2:47:52 
 */
@SuppressWarnings("serial")
public class SimpleSpout extends BaseRichSpout {
	
	//用来发射数据的工具类
    private SpoutOutputCollector collector;
    
    private static String[] info = new String[]{
        "comaple\t,12424,44w46,654,12424,44w46,654,",
        "lisi\t,435435,6537,12424,44w46,654,",
        "lipeng\t,45735,6757,12424,44w46,654,",
        "hujintao\t,45735,6757,12424,44w46,654,",
        "jiangmin\t,23545,6457,2455,7576,qr44453",
        "beijing\t,435435,6537,12424,44w46,654,",
        "xiaoming\t,46654,8579,w3675,85877,077998,",
        "xiaozhang\t,9789,788,97978,656,345235,09889,",
        "ceo\t,46654,8579,w3675,85877,077998,",
        "cto\t,46654,8579,w3675,85877,077998,",
        "zhansan\t,46654,8579,w3675,85877,077998,"};
    
    Random random=new Random();

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void nextTuple() {
		try {
            String msg = info[random.nextInt(11)];
            // 调用发射方法
            collector.emit(new Values(msg));
            // 模拟等待100ms
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("source")); //collector.emit(new Values(msg));参数要对应
	}

}
