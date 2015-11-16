//package com.renren.ugc.comment.storm.redis;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.log4j.Logger;
//
//import com.renren.cluster.client.redis.RedisClusterPoolClient;
//import com.renren.ugc.comment.storm.model.Config;
//import com.renren.ugc.comment.storm.model.Record;
//import com.renren.ugc.comment.storm.model.StatType;
//import com.renren.ugc.comment.storm.utils.RedisUtils;
//
//public class McsUserAccessCountRedisWriter implements Runnable {
//
//    private static Logger LOG = Logger.getLogger(McsUserAccessCountRedisWriter.class);
//
//    private Record record;
//    
//    private Date date;
//
//    public McsUserAccessCountRedisWriter(Record record,Date date){
//        this.record = record;
//        this.date = date;
//    }
//
//    public void insert() {
//    	Map<String, String> map = new HashMap<String, String>();
//    	Map<String, Integer> countMap = record.getDataMap();
//    	Set<String> redisKeySet = new HashSet<String>();
//    	for (Entry<String, Integer> entry : countMap.entrySet()) {
//    	    String key = entry.getKey();
//    	    int value = entry.getValue();
//    	    
//    	    //record中没存储date，key也没包含date信息
//    	    String redisKey = KeyUtils.getKey(StatType.USER_ACCESS_COUNT, key, date);
//    	    map.put(redisKey, String.valueOf(value));
//    	    redisKeySet.add(redisKey);
//    	}
//    	try {
//    		RedisClusterPoolClient client = RedisUtils.getClient();
//    		//batch set expire time
//    		client.expire(redisKeySet, Config.REDIS_API_STATISTICS_EXPIRE);
//    	} catch (Exception e) {
//    		LOG.error("batch set UserAccessCount into redis error !", e);
//    	}
//    }
//
//    @Override
//    public void run() {
//        insert();
//    }
//}
