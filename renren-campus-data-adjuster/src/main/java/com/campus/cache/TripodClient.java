package com.campus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import xce.tripod.client.EntryType;
import xce.tripod.client.TripodCacheClient;
import xce.tripod.client.TripodCacheClientFactory;

/** 
 * 通用cache的client，之所以没有用“TripodCacheClient”这个名字，是因为与
 * xce.tripod.client.TripodCacheClient类名重复
 * @author meng.liu 
 * @date 2014-7-22 下午1:35:51 
 */
public class TripodClient {
	
	private static Log logger = LogFactory.getLog(TripodClient.class);
	private TripodCacheClient cacheClient = TripodCacheClientFactory.getClient(EntryType.BIZ_CAMPUS_PLAZA);
	private static TripodClient instance = new TripodClient();
	
	private TripodClient() {
		
	}
	
	public static TripodClient getInstance() {
		return instance;
	}
	
    /**
     * 向zSet中追加数据
     * 注：通用cache文档里面写的“第一次使用之前必须使用zSet初始化写接口zSetWrite来初始化，否则该接口调用会失败“
     * 这个感觉挺弱的，因为你不知到接口调用失败 是因为没有初始化过，还是其他什么原因，也没有相应错误码来指定这种错误，
     * 如果失败就调zSetWrite，万一不是因为没初始化过，就会把原先的值给覆盖掉导致数据不正确。
     * 考虑到我们业务中的使用场景：看广场新鲜事的时候，一定会先读一次，调用端根据读出的数据是否为空来决定写的时候用
     * zSetAdd还是zSetWrite就可以了。此处如果调用失败，方法内部不再调用zSetWrite
     * @param key
     * @param valueWithScore key对应score，value对应要添加的值
     * @param sec 过期时间
     */
	public boolean zSetAdd(String key, Map<String, String> valueWithScore, int sec) {
		
		boolean success = false;
		try {
			success = cacheClient.zSetAdd(key, valueWithScore);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return success;
	}
	
	/**
	 * 向zSet中新建数据
	 * @param key
	 * @param valueWithScore
	 * @param sec
	 */
	public void zSetWrite(String key, Map<String, String> valueWithScore, int sec) {
		
		try {
			cacheClient.zSetWrite(key, valueWithScore, sec);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	/**
	 * 获取zSet中的元素（此需求要求全部返回）
	 * @param key
	 * @return
	 */
	public List<String> zSetGet(String key) {
		List<String> result = null;
		try {
			result = cacheClient.zSetGet(key, 0, -1);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 删除zSet中指定score范围内的数据
	 * @param key
	 * @param scoreMin
	 * @param scoreMax
	 */
	public void removeDataFromZSet(String key,String scoreMin, String scoreMax) {
		try {
			cacheClient.zSetRemoveByScore(key, scoreMin, scoreMax);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void remove(String key) {
		
		try {
			cacheClient.remove(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param sec
	 */
	public void setList(String key, List<String> value, int sec) {
		
		try {
			cacheClient.setList(key, value, sec);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param begin
	 * @param limit
	 * @return
	 */
	public List<String> getList(String key,int begin, int limit) {
		try {
			return cacheClient.getList(key, begin, limit);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 往list头部插入一条数据
	 * 不知道为什么通用cache之前制定了 禁止持久化的规定，进而因为这个规定，又对所有诸如List,zSet类接口制定了插入操作前必先
	 * 初始化否则插入不成功的规则。一个本应该是很轻量级的redis lpush操作，非得弄成下面这个样，使用的时候实在很无语，太难用了。
	 * 有时间的话考虑改用人人网另一个redis工具！
	 * @param key
	 * @param item
	 */
	public void addItem2ListHead(String key, String item) {
		
		boolean success = false;
		List<String> value = new ArrayList<String>();
		value.add(item);
		try {
			success = cacheClient.lPushList(key, value);
			if(!success) {
				List<String> list = getList(key, 0, -1);
				if(list == null || list.size() == 0) {
					cacheClient.setList(key, value, Integer.MAX_VALUE);
				} else {
					logger.error(String.format("lPushList failed, key=[%s],item=[%s]", key,item));
				}
			}
		} catch (Exception e) {
			logger.error(String.format("lPushList failed, key=[%s],item=[%s]", key,item),e);
		}

	}
	
	/**
	 * 基本的key-value  set接口
	 * @param key
	 * @param value
	 * @param expireTime
	 * @throws CacheException
	 */
	public void basicSet(String key, Object value, int expireTime) {
		
		try {
			cacheClient.set(key, value, expireTime);
		} catch (Exception e) {
			logger.error(String.format("basicSet failed, key=[%s],value=[%s]", key,value.toString()),e);
		}
		
	}
	
	/**
	 * 基本的get接口
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public Object basicGet(String key) {
		
		Object value = null;
		try {
			value = cacheClient.get(key);
		} catch (Exception e) {
			logger.error(String.format("basicGet failed, key=[%s]", key),e);
		}
		return value;
	}

}
