package com.renren.ugc.comment.storm.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * @author wangxx
 * 
 *  需要做成线程安全的
 */
public class Record implements Cloneable, Serializable {

	private static final long serialVersionUID = 8560697234630011259L;
	
	private static Logger LOG = Logger.getLogger(Record.class);
   
    private final ConcurrentHashMap<String, RecordValue> map;

    /**
     * 标记本次record的key
     */
    private final long key;
    
    public Record(long key){
    	this.key = key;
    	this.map =  new ConcurrentHashMap<String, RecordValue>();
    }
    
    public long getKey(){
    	return key;
    }

    public Object clone() {
    	try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			LOG.error("clone record error!", e);
		}
    	return null;
    }
    
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    /**
     * 把记录增加<code>increment</code>
     * 
     * @return the value after increasing
     */
    public void incr(String key, int increment) {
    	RecordValue curr = map.get(key);
        if (curr == null) {
        	curr = map.putIfAbsent(key, new RecordValue());
        	if(curr == null){
        		curr = map.get(key);
        	}
        }
        curr.incr(increment);
        return;
    }

    public int get(String key) {
    	RecordValue curr = map.get(key);
        if (curr != null) {
            return curr.getValue();
        }
        return 0;
    }
    
    public double getCount(String key) {
    	RecordValue curr = map.get(key);
        if (curr != null) {
            return curr.getCount();
        }
        return 0;
    }

    public void remove(String key) {
        map.remove(key);
    }
    
    public Map<String, RecordValue> getDataMap() {
    	return map;
    }
  
}
class RecordValue{
	private final AtomicInteger value;
	private final AtomicInteger count;
	
	public RecordValue(){
		this.value = new AtomicInteger(0);
		this.count = new AtomicInteger(0);
	}
	
	public void incr(int incr){
		this.value.addAndGet(incr);
		this.count.incrementAndGet();
	}
	
	public int getCount(){
		return count.get();
	}
	
	public int getValue(){
		return value.get();
	}
	
}
