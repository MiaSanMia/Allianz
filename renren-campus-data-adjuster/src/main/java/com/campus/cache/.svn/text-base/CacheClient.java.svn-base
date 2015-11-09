package com.campus.cache;

import java.util.List;
import java.util.Map;

import com.campus.exception.CacheException;

/**
 * 操作cache的包装对象，将来底层的cache类型换了之后只要实现新的子类就可以了。
 * 异常类型一般为网络异常，server异常，或者其他异常，由具体实现类自己封装。
 *
 * @author Wang Shufeng [shufeng.wang@renren-inc.com]
 * @since Mar 12, 2014
 */
public interface CacheClient {

    /**
     * 封装get操作
     * 
     * @param key
     * @return
     * @throws CacheException
     */
    public Object get(String key) throws CacheException;
    
    /**
     * 封装set操作
     * 
     * @param key
     * @return 操作成功还是失败
     * @throws CacheException
     */
    public boolean set(String key, Object value, int expiredTime) throws CacheException;
    
    /**
     * 封装remove操作
     * 
     * @param key
     * @return 操作成功还是失败
     * @throws CacheException
     */
    public boolean remove(String key) throws CacheException;
    
    /**
     * 封装批量获取数据操作
     * 
     * @param key
     * @return
     * @throws CacheException
     */
    public Map<String, Object> multiGet(List<String> keys) throws CacheException;
    
    /**
     * 封装计数器操作
     * 注：如果kv中不存在这个key，则用0作为默认值，然后进行+step操作
     * @param key
     * @param step 
     * @return add后的结果
     * @throws CacheException
     */
    public int addAndGet(String key, int step,  int expiredTime) throws CacheException;
    
   /**
    * 封装计数器操作
    * 注：如果kv中不存在这个key，则用defaultValue作为默认值，然后进行+step操作
    * @param key
    * @param step
    * @param defaultValue 
    * @param expiredTime
    * @return
    * @throws CacheException
    */
    public int addAndGet(String key, int step,  int defaultValue, int expiredTime) throws CacheException;
    
    /**
     * 封装setCount操作.
     * 
     * @param key
     * @param count
     * @param expiredTime
     * @return
     * @throws CacheException
     */
    public boolean setCount(String key, int count, int expiredTime) throws CacheException;
    
    public String getTailClientInfo();
}
