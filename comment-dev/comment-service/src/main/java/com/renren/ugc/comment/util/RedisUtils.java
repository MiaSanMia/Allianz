package com.renren.ugc.comment.util;

import org.apache.log4j.Logger;

import com.renren.cluster.ClusterException.ClusterConnException;
import com.renren.cluster.client.redis.RedisClusterPoolClient;


/**
 * Redis Cluster client
 * 
 * @author jiankuan.xing
 * 
 */
public class RedisUtils {

    private static Logger logger = Logger.getLogger(RedisUtils.class);

    private static RedisClusterPoolClient client = null;

    // Redis for test (10.3.20.161:23893)
    private static final String testBizname = "comment.dev";

    private static final String testRedisClusterZookeeperAddr = "10.22.206.142:2181";


    // Redis for production
    private static final String bizname = "comment";

    private static final String redisClusterZookeeperAddr = "webzk1.d.xiaonei.com:2181,webzk2.d.xiaonei.com:2181,webzk3.d.xiaonei.com:2181,webzk4.d.xiaonei.com:2181,webzk5.d.xiaonei.com:2181";

    static {
        try {

            String currentBizName = null;
            String currentRedisClusterZookeeperAddr = null;

            boolean useOnlineRedis = Boolean.valueOf(System.getProperty("comment.redis.online", "true"));
            if (useOnlineRedis) {
                currentBizName = bizname;
                currentRedisClusterZookeeperAddr = redisClusterZookeeperAddr;
                logger.info("Using online Redis Cluster");
            } else {
                currentBizName = testBizname;
                currentRedisClusterZookeeperAddr = testRedisClusterZookeeperAddr;
                logger.info("Using test Redis Cluster");              
            }

            if (logger.isInfoEnabled()) {
                logger.info("connect to redis cluster: biz=" + currentBizName
                        + ", zk=" + currentRedisClusterZookeeperAddr);
            }
            client = new RedisClusterPoolClient(currentBizName,
                    currentRedisClusterZookeeperAddr);
            client.init();
        } catch (ClusterConnException e) {
            logger.fatal("Cannot connect to redis cluster", e);
            System.exit(1);
        }
    }

    public static RedisClusterPoolClient getClient() {
        return client;
    }
}
