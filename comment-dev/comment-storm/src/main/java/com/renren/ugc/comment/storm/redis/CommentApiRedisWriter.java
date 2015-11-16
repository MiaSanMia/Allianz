package com.renren.ugc.comment.storm.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.renren.ugc.comment.storm.model.Config;
import com.renren.ugc.comment.storm.model.CommentApiObj;
import com.renren.ugc.comment.storm.model.StatType;
import com.renren.ugc.comment.storm.utils.RedisUtils;


public class CommentApiRedisWriter implements Runnable {

    private static Logger LOG = Logger.getLogger(CommentApiRedisWriter.class);

    private CommentApiObj obj;

    public CommentApiRedisWriter(CommentApiObj obj){
        this.obj = obj;
    }

    public void insert() {
        String method = obj.getApi();
        Date date = obj.getTimePeriod();
        String keyCount = KeyUtils.getKey(StatType.CALL_COUNT, method, date);
        String keyMax = KeyUtils.getKey(StatType.MAX_EXEC_TIME, method, date);
        String keyMin = KeyUtils.getKey(StatType.MIN_EXEC_TIME, method, date);
        String keyAvg = KeyUtils.getKey(StatType.AVG_EXEC_TIME, method, date);

        Map<String, String> map = new HashMap<String, String>();
        map.put(keyCount, String.valueOf(obj.getCount()));
        map.put(keyMax, String.valueOf(obj.getMaxTime()));
        map.put(keyMin, String.valueOf(obj.getMinTime()));
        map.put(keyAvg, String.valueOf(obj.getAvgTime()));

        RedisClusterPoolClient client = RedisUtils.getClient();
        client.mset(map);
        
        System.out.println("cache settttttttttttttttttttttttttttttttttttttttt|key:"+keyCount+",value:"+obj.getCount());
        System.out.println(map);

        client.expire(Config.REDIS_API_STATISTICS_EXPIRE, keyCount, keyMax,
            keyMin, keyAvg);
    }

    @Override
    public void run() {
        insert();
    }
}
