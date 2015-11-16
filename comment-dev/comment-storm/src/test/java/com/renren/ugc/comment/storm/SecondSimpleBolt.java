package com.renren.ugc.comment.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/** 
 * @author meng.liu 
 * @date 2014-5-22 下午5:42:32 
 */
@SuppressWarnings("serial")
public class SecondSimpleBolt extends BaseBasicBolt {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
            String msg = input.getString(0);
            if (msg != null){
//                System.out.println("msg="+msg);
                collector.emit(new Values(msg + "second second second!"));
            }
                
        } catch (Exception e) {
            e.printStackTrace(); 
        }


	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("SimpleBolt"));

	}

}
