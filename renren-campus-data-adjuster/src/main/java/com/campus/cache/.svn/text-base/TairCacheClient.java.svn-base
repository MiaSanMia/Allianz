package com.campus.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.campus.exception.CacheException;
import com.campus.exception.CampusErrorCode;
import com.renren.tair.util.TairManagerFactory;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.DefaultTairManager;

/**
 * 以kv系統做底层提供的缓存客户端.
 * 
 * @author Wang Shufeng [shufeng.wang@renren-inc.com]
 * @author lei.xu1
 * @since Mar 12, 2014
 */
public class TairCacheClient implements CacheClient {

    private Log logger = LogFactory.getLog(getClass());

    private DefaultTairManager tairManager;

    private String group;
    private int namespace;

    public TairCacheClient(String group, int namespace) {
        if (StringUtils.isBlank(group)) {
            throw new IllegalArgumentException(String.format("invalid group [%s]", group));
        }
        this.tairManager = TairManagerFactory.getDefaultTairManager(group);
        this.namespace = namespace;
        this.group = group;
    }

    @Override
    public Object get(String key) throws CacheException {
        Result<DataEntry> result = null;
        try {
            result = tairManager.get(namespace, key);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call get error", e);
        }

        if (result == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call get error, null result");
        }

        if (result.isSuccess() && result.getValue() != null) {
            return result.getValue().getValue();
        } else if (ObjectUtils.equals(result.getRc(), ResultCode.DATANOTEXSITS)
                || ObjectUtils.equals(result.getRc(), ResultCode.DATAEXPIRED)) {

            logger.warn("get tair cache, result:" + result.getRc().toString());

            return null;
        } else {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "get tair cache error, result:"
                    + result.getRc().toString());
        }
    }

    @Override
    public boolean set(String key, Object value, int expiredTime) throws CacheException {
        ResultCode resultCode = null;
        try {
            resultCode = tairManager.put(namespace, (Serializable) key, (Serializable) value,
                    expiredTime);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call set error", e);
        }

        if (resultCode == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call set error, null result");
        }

        boolean result = resultCode.isSuccess();

        if (!result) {
            logger.warn("set tair cache, result:" + resultCode.toString());
        }
        return result;
    }

    @Override
    public boolean remove(String key) throws CacheException {
        ResultCode resultCode = null;
        try {
            resultCode = tairManager.delete(namespace, (Serializable) key);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call remove error", e);
        }

        if (resultCode == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call set error, null result");
        }

        boolean result = resultCode.isSuccess();

        if (!result) {
            logger.warn("remove tair cache, result:" + resultCode.toString());
        }
        return result;
    }

    @Override
    public Map<String, Object> multiGet(List<String> keys) throws CacheException {
        Map<String, Object> map = new HashMap<String, Object>();
        Result<List<DataEntry>> result = null;

        try {
            result = tairManager.mget(namespace, keys);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call multiGet error", e);
        }

        if (result == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER,
                    "call multiGet error, null result");
        }

        //kv的multiGet有一种返回码叫做“PARTSUCC”,意思是有部分key是miss的，部分key获取成功 meng.liu
        if ((result.isSuccess() || result.getRc().equals(ResultCode.PARTSUCC)) && result.getValue() != null) {
            List<DataEntry> list = result.getValue();
            for (DataEntry entry : list) {
                map.put(entry.getKey().toString(), entry.getValue());
            }
        } else {
            logger.warn("remove tair cache, result:" + result.getRc().toString());
        }

        return map;
    }

    @Override
    public int addAndGet(String key, int step, int expiredTime) throws CacheException {
        Result<Integer> result = null;
        try {
            int defaultValue = 0;
            result = tairManager.incr(namespace, key, step, defaultValue, expiredTime);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call addAndGet error", e);
        }

        if (result == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER,
                    "call addAddGet error, null result");
        }

        if (!result.getRc().isSuccess()) {
            logger.warn("incr tair cache, result:" + result.getRc().toString());
        }

        return result.getValue();
    }

    public boolean setCount(String key, int count, int expiredTime) throws CacheException {
        ResultCode resultCode = null;
        try {
            resultCode = tairManager.setCount(namespace, key, count, 0, expiredTime);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call setCount error", e);
        }

        if (resultCode == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER,
                    "call setCount error, null result");
        }

        boolean result = resultCode.isSuccess();
        
        if (result) {
            logger.warn("incr tair cache, result:" + resultCode.toString());
            return false;
        }

        return result;
    }

	@Override
	public int addAndGet(String key, int step, int defaultValue, int expiredTime)
			throws CacheException {
        Result<Integer> result = null;
        try {
            result = tairManager.incr(namespace, key, step, defaultValue, expiredTime);
        } catch (Exception e) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER, "call addAndGet error", e);
        }

        if (result == null) {
            throw new CacheException(CampusErrorCode.CACHE_OTHER,
                    "call addAddGet error, null result");
        }

        if (!result.getRc().isSuccess()) {
            logger.warn("incr tair cache, result:" + result.getRc().toString());
        }

        return result.getValue();
    }

	@Override
	public String getTailClientInfo() {
		return String.format("group:[%s],area:[%d]",this.group,this.namespace );
	}
	
    
}
