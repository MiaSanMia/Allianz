package com.campus.mbean;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.campus.cache.CacheClient;
import com.campus.cache.CacheClientFactory;
import com.campus.cache.CachePoolType;
import com.campus.cache.util.Model2StringUtil;

/** 
 * @author meng.liu 
 * @date 2014-4-5 下午2:53:09 
 */
public class CampusMonitorClient implements CampusMonitorClientMBean {
	
	private static Log logger = LogFactory.getLog(CampusMonitorClient.class);
	
	private static String valueInKV = "come on, give me value from kv~~~";
	private static String valueInDB;
	
	private String tairArea;
	
	private String tairClientInfo;
	
	private static CacheClient tairClient;
	private static CachePoolType cachePoolType;
	
	@Override
	public String getTairArea() {
		return tairArea;
	}


	@Override
	public void setTairArea(String tairArea) {
		this.tairArea = tairArea;
		cachePoolType = CachePoolType.valueOf(tairArea);
		tairClient = CacheClientFactory.getClient(cachePoolType);
		
		tairClientInfo = tairClient.getTailClientInfo();
		
		logger.info("get tairClient successfuly by cachePool:" + cachePoolType);
	}

	


	@Override
	public void clearKVBySingleKey(String key) {
		
		logger.info("clearKVBySingleKey, key=" + key);
		try {
			tairClient.remove(key);
			logger.info(String.format("remove key [%s] from kv successfully", key));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}


	@Override
	public void clearKV(String prefix, int beginIndex, int endIndex) {
		
		logger.info(String.format("begin to clear data from kv: prefix[%s],beginIndex[%d],endIndex[%d] ",
					prefix, beginIndex, endIndex));
		
		for(int i = beginIndex;i<=endIndex; i++) {
			String key = prefix + String.valueOf(i);
			try {
				tairClient.remove(key);
				logger.info(String.format("remove key [%s] from kv successfully", key));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
	}


	@Override
	public void getValueFromKV(String key) {
		logger.info(String.format("query the value from kv, key [%s]", key));
		
		Object object = null;
		try {
			object = (Object)tairClient.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if(object != null) {
			if(object instanceof String) {
				valueInKV = (String)object;
			} else if(object instanceof Integer) {
				valueInKV = String.valueOf((Integer)object);
			} else if(object instanceof Date) {
				valueInKV = ((Date)object).toString();
			} else if(object instanceof List) {
				int index = 0;
				StringBuilder strBuilder = new StringBuilder();
				
				List list = (List)object;
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					String value = null;
					  Object entry = iter.next();
					  if(entry instanceof String) {
						  value = entry.toString();
					  } else if(entry instanceof Integer) {
						  value = String.valueOf((Integer)entry);
					  } else {
						  value = Model2StringUtil.transModel2String(entry);
					  }
					  strBuilder.append(index ++ +"--[" + value + "]");
					  
				}
				valueInKV = strBuilder.toString();
			} else if(object instanceof Long) {
				valueInKV = String.valueOf((Long)object);
			}
			else {
				valueInKV = Model2StringUtil.transModel2String(object);
			} 
			
			logger.info("get value from kv:" + valueInKV);
			
			logger.info("get value from kv:" + object);
		} else {
			valueInKV = "null";
			logger.info("didn't get value from kv:" + null);
		}
		
	}


	@Override
	public String getValueInKV() {
		return valueInKV;
	}

	@Override
	public String getTairClientInfo() {
		return tairClientInfo;
	}


	@Override
	public String getValueInDB() {
		return valueInDB;
	}

}
