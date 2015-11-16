package com.renren.ugc.comment.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import xce.tripod.TripodException;
import xce.tripod.client.Entry;
import xce.tripod.client.EntryType;
import xce.tripod.client.TripodCacheClient;
import xce.tripod.client.TripodCacheClientFactory;

/**
 * 标注:{@link TripodCacheMgr} 评论中心缓存底层操作类. 目前采用通用Cache.
 * 
 * @author xinyan.yang@renren-inc.com
 * @date 2013-1-29 21:48:08
 */
public class TripodCacheMgr {

    private static Logger logger = Logger.getLogger(TripodCacheMgr.class);

    /**
     * the universe comment cache label
     */
    private static final Entry BIZ_UNIVERSE_COMMENT =
            EntryType.BIZ_UNIVERSE_COMMENT;

    /**
     * seconds of one day
     */
    private static final int EXPIRE_HOUR = 3600;

    /**
     * seconds of one day
     */
    private static final int EXPIRE_DAY = EXPIRE_HOUR * 24;

    /**
     * seconds of cache expire time - 7 days
     */
    private static final int EXPIRE_TIME = EXPIRE_DAY * 7;

    /**
     * comment count, seconds of cache expire time - 1 hour
     */
    private static final int COUNT_EXPIRE_TIME = EXPIRE_HOUR;

    private static TripodCacheMgr instance = new TripodCacheMgr();

    /**
     * the tripod cache client
     */
    private TripodCacheClient tcc;

    /**
     * indicate the counter cache's value is not available
     */
    public static long ERROR_COUNT = TripodCacheClient.ERROR_COUNT;

    /**
     * indicate whether to use the internal hash map as the cache
     */
    private boolean useTestCache;

    /**
     * the internal cache for test use
     */
    private Map<String, Object> testCache;

    private TripodCacheMgr(){
        useTestCache =
                Boolean.valueOf(System.getProperty("comment.cache.test",
                    "false"));
        if (useTestCache) {
            logger.info("use hash map as the test cache");
            testCache = new HashMap<String, Object>();
        } else {
            tcc = TripodCacheClientFactory.getClient(BIZ_UNIVERSE_COMMENT);
        }
    }

    public static TripodCacheMgr getInstance() {
        return instance;
    }

    /**
     * retrieve the cached list which is implemented by tripod's cache's
     * 
     * @return the cached list or null if the cache is not found
     */
    public List<String> getCachedList(String key) {
        try {
            return (List<String>) tcc.zSetGet(key, 0, -1);
        } catch (TripodException e) {
            logger.warn("an error occured when fetching cache by key " + key, e);
            return null;
        }
    }

    /**
     * append a value to a cached list
     */
    public void addToCachedList(String key, String value) {
        if (value == null) {
            return;
        }
        try {
            String score = String.valueOf(System.currentTimeMillis());
            Map<String, String> scoredValue = new HashMap<String, String>();
            scoredValue.put(score, value);
            boolean result = tcc.zSetAdd(key, scoredValue);
            if (result == false) {
                result = tcc.zSetWrite(key, scoredValue, EXPIRE_TIME);
            }

            if (result == false) {
                logger.warn("an error occured when fetching cache by key "
                            + key);
            }

        } catch (TripodException e) {
            logger.warn("an error occured when fetching cache by key " + key, e);
        }
    }

    public void removeCache(String key) {
        try {
            if (useTestCache) {
                testCache.remove(key);
            } else {
                tcc.remove(key);
            }
        } catch (TripodException e) {
            logger.warn("an error occured when removing cache by key " + key, e);
        }
    }

    /**
     * 创建一个cache
     */
    public void setCache(String key, Object value) {
        try {
            if (useTestCache) {
                testCache.put(key, value);
            } else {
                tcc.set(key, value, EXPIRE_TIME);
            }
        } catch (TripodException e) {
            String message = "error set cache key:" + key;
            logger.warn(message, e);
        }
    }

    /**
     * 创建一个指定过期时间的cache
     * 
     * @param key
     * @param value
     * @param expireTime 单位是秒
     */
    public void setCache(String key, Object value, int expireTime) {
        try {
            if (useTestCache) {
                testCache.put(key, value);
            } else {
                tcc.set(key, value, expireTime);
            }
        } catch (TripodException e) {
            String message = "error set cache key:" + key;
            logger.warn(message, e);
        }
    }

    /**
     * 获取一个Cache的value
     */
    public Object getCache(String key) {
        try {
            if (useTestCache) {
                return testCache.get(key);
            } else {
                return tcc.get(key);
            }
        } catch (TripodException e) {
            String message = "error get cache key:" + key;
            logger.warn(message, e);
            return null;
        }
    }

    /**
     * 计数器自增step. 如果Cache出错，返回<code>ERROR_COUNT</code>
     */
    public long incCounter(String key, long step) {

        long result = ERROR_COUNT;
        try {
            if (useTestCache) {
                Long value = (Long) testCache.get(key);
                if (value == null) {
                    return result;
                }
                value += step;
                testCache.put(key, value);
                return value;

            } else {
                result = tcc.incLong(key, step);
            }

            return result;
        } catch (TripodException e) {
            String message =
                    "an error occured when increasing the counter by key:"
                            + key;
            logger.warn(message, e);
            return result;
        }
    }

    /**
     * 读取计数器. 如果Cache出错，返回<code>ERROR_COUNT</code>
     */
    public long getCounter(String key) {
        try {
            if (useTestCache) {
                Long value = (Long) testCache.get(key);
                if (value == null) return ERROR_COUNT;
                return value;
            } else {
                return tcc.getLong(key);
            }
        } catch (TripodException e) {
            String message =
                    "an error occured when get the counter by key: " + key;
            logger.warn(message, e);
            return ERROR_COUNT;
        }
    }

    /**
     * 初始化计数器
     */
    public void setCounter(String key, long value) {

        try {
            if (useTestCache) {
                testCache.put(key, value);
            } else {
                tcc.setLong(key, value, COUNT_EXPIRE_TIME);
            }
        } catch (TripodException e) {
            String message =
                    "an error occured when set the counter by key:" + key;
            logger.warn(message, e);
        }
    }

    /**
     * 批量读取计数器. 如果Cache出错，返回<code>null</code>
     */
    public Map<String, Long> getCounterBatch(List<String> key) {
        try {
            if (useTestCache) {
                Map<String, Long> maps = new HashMap<String, Long>();
                for (String s : key) {
                    Long value = (Long) testCache.get(s);
                    if (value != null) {
                        maps.put(s, value);
                    }
                }
                return maps;
            } else {
                return tcc.multiGetLong(key);
            }
        } catch (TripodException e) {
            String message =
                    "an error occured when get the counter by key: " + key;
            logger.warn(message, e);
            return null;
        }
    }
}
