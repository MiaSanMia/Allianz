package com.renren.ugc.comment.storm.utils;

import org.apache.log4j.Logger;

import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;
import com.renren.ugc.comment.storm.model.Config;


public class RedisUtils {

    private static RedisClusterPoolClient client;

    private static Logger LOG = Logger.getLogger(RedisUtils.class);

    static {
        try {
            client =
                    new RedisClusterPoolClient(Config.REDIS_BIZNAME,
                        Config.REDIS_ZK_ADDR);
            client.init();
        } catch (ClusterConnException e) {
            LOG.error(e);
        }
    }

    public static RedisClusterPoolClient getClient() {
        return client;
    }
}
