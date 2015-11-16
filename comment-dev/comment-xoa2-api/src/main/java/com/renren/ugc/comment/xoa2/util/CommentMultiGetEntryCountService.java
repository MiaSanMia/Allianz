package com.renren.ugc.comment.xoa2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.Entry;
import com.renren.ugc.comment.xoa2.IXCommentCenter;


public class CommentMultiGetEntryCountService {
	
	 private static Logger logger = Logger.getLogger(CommentMultiGetEntryCountService.class);
	 
	 private static ExecutorService executor = Executors.newFixedThreadPool(20);
	 
	 private static final int DEFAULT_TIME_OUT = 20 * 1000;
	 
	 //总的超时时间(ms)
	 private int totalTimeout;
	 
	 private CommentMultiGetEntryCountService(int totalTimeout){
		 this. totalTimeout= totalTimeout;
	 }
	 
	 public static CommentMultiGetEntryCountService newService(){
		 return new CommentMultiGetEntryCountService(DEFAULT_TIME_OUT);
	 }
	 
	 public static CommentMultiGetEntryCountService newService(int timeout){
		 return new CommentMultiGetEntryCountService(timeout);
	 }
	
	public  Map<Long, Integer> multiGetEntryCount(IXCommentCenter xcc,
			List<Entry> entryList, CommentType commentType, int actorId){
		
		if(CollectionUtils.isEmpty(entryList)){
			return Collections.EMPTY_MAP;
		}
		
		//1.根据ownerId进行切分
		Map<Integer,List<Entry>> entryMaps = splitByOwnerId(entryList);
		
		//2.线程池调用
		CountDownLatch countLatch = new CountDownLatch(entryMaps.size());
		ConcurrentHashMap<Long,Integer> result = new ConcurrentHashMap<Long,Integer>(entryList.size());
		
		Iterator iter = entryMaps.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry oneEntry = (Map.Entry) iter.next();
			List<Entry> list = (List<Entry>) oneEntry.getValue();
			executor.submit(new GetEntryCountRPCTask(list,commentType,actorId,xcc,countLatch,result));
		}
		
		//3.等待结果
		//这里如果客户端调用超时时间设的过大，可能导致线程池的线程rpc调用迟迟不能完成，因此我们在这里设置一个超时时间
		try {
			countLatch.await(totalTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("multiGetEntryCount interrupt error");
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	/**
	 * @param entryList
	 * @return
	 * 
	 *  根据ownerId切分list
	 *  返回的Map结构，key:ownerId, value:对应的ownerId list
	 */
	private Map<Integer,List<Entry>> splitByOwnerId(List<Entry> entryList){
		
		Map<Integer,List<Entry>> entryMaps = new HashMap<Integer,List<Entry>>();
		
		for(Entry entry:entryList){
			List<Entry> lists = entryMaps.get(entry.getOwnerId());
			if(lists == null){
				lists = new ArrayList<Entry>();
				entryMaps.put(entry.getOwnerId(), lists);
			}
			lists.add(entry);
		}
		
		return entryMaps;
	}

}
