package com.renren.ugc.comment.storm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author wangxx
 * 
 * 线程安全的定长的Record list
 * 
 * 这里每一个小格子存储一分钟的数据，如果是5个小格子的话，相当于存储了最近5分钟的数据
 * 
 *  这个结构没有保证key的严格有序，只是保证了在替代的时候，如果key比要替代的小的话，那么忽略这次key
 * 
 */
public class RotateRecordList implements Cloneable{
	
	private static Logger LOG = Logger.getLogger(RotateRecordList.class);
	
	private static final int DEFAULT_LENGTH = 5;
	
	//值
	private final Record[] datas;
	
	//长度
	private final int length;
	
	//当前要写入的位置
	private int index;
	
	private  final Set<Long> keySets = new HashSet<Long>();
	
	public RotateRecordList(int length){
		this.length = length;
		this.datas = new Record[length];
		this.index = 0;
	}
	
	public RotateRecordList(){
		this(DEFAULT_LENGTH);
	}
	
	//如果有这个key，则返回对应的Record，没有的话则新建一个,这个函数类似于ConcurrentHashMap.putIfAbsent()
	private  Record putIfAbsent(long key){
		
		if(keySets.contains(key)){
			return getRecord(key);
		}
		
		synchronized(RotateRecordList.class){
		
			if(keySets.contains(key)){
				return getRecord(key);
			}
			
			Record oldRecord = datas[index];
			long oldKey = 0;
			
			if(oldRecord != null){
				//比要替代的还要小
				if(key < oldRecord.getKey()){
					LOG.error("error record is tooSmall,key="+key+",trimlistkey is ="+this.toKeyString());
					return null;
				}
				oldKey = oldRecord.getKey();
			}
					
			datas[index] = new Record(key);
			index = (index + 1)%length;
			
			//保持keySet的一致性
			if(oldKey != 0){
				keySets.remove(oldKey);
			}
			keySets.add(key);
		}
		
		return getRecord(key);
	}
	
	public boolean incr(String method,int value,long time){
		
		//1.loop，首先找到要存储的格子
		Record record = getRecord(time);
		if(record == null){
			record  = this.putIfAbsent(time);
			//如果未空，则可能是record已经被丢弃
			if(record == null){
				LOG.error("error record is null,key="+time+",trimlistkey is ="+this.toKeyString());
				return false;
			}
		}
		
		//2.incr
		record.incr(method, value);
		
		return true;
	}
	
	public double getAvg(String method){
		double value = 0;
		int count = 0;
		for(int i = 0;i<length;i++){
			if(datas[i] != null && datas[i].contains(method)){
				value += datas[i].get(method);
				count += datas[i].getCount(method);
			}
		}
		return count != 0 ? (double)value / count : 0;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<length;i++){
			if(datas[i]!= null && datas[i].getDataMap() != null){
				sb.append(datas[i].getKey()+"|");
				Iterator iter = datas[i].getDataMap().entrySet().iterator();
				while(iter.hasNext()){
					Entry entry = (Entry) iter.next(); 
					RecordValue value = (RecordValue)entry.getValue();
					sb.append(entry.getKey()+"|"+ value.getValue()+"|"+value.getCount());
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}
	
	public String toKeyString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<length;i++){
			if(datas[i]!= null && datas[i].getDataMap() != null){
				sb.append(datas[i].getKey()+"|");
			}
		}
		return sb.toString();
	}
	
	private Record getRecord(long time){
		Record record = null;
		
		for(int i=0;i<length;++i){
			if(datas[i] != null && datas[i].getKey() == time){
				record = datas[i];
				break;
			}
		}
		return record;
	}
	
	
	
}
