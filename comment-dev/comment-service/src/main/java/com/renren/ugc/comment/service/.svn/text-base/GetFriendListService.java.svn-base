package com.renren.ugc.comment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.xiaonei.xce.buddyadapter.buddyrelationcache.BuddyRelationCacheAdapter;
import com.xiaonei.xce.domain.buddy.BuddyRelation;

/**
 * @author wangxx
 *
 *  获取好友服务
 */
public class GetFriendListService {
	
//	private static Logger logger = Logger.getLogger(GetFriendListService.class);
//	
//	private static ExecutorService executor = Executors.newFixedThreadPool(30,new GetFriendListThreadFactory());//默认30个线程
//	
//	//authors超过这个数目则进行并发的获取
//	private static final int MAX_FRIEND_LIMIT = 100;
//	
//	//每次并发的获取authors的数目
//	private static final int MAX_ONCE_GET_FRIEND = 100;
//	
//	//调用"好友服务"的超时时间
//	private static final int TIMEOUT = 2000;
//	
//	/**
//	 * @param userId
//	 * @param authors
//	 * @return
//	 * 
//	 *  获取author列表中userId的好友列表
//	 */
//	public static List<Integer> getAllFriends(int userId,List<Integer> authors){
//		
//		if(userId == 0 || CollectionUtils.isEmpty(authors)){
//			return Collections.EMPTY_LIST;
//		}
//		
//		int authorSize = authors.size();
//		List<Integer> friendIds = new LinkedList<Integer>();
//		
//		//1.count太小的话，直接调用接口返回
//		if(authorSize < MAX_FRIEND_LIMIT){
//			 Map<Integer, BuddyRelation> relationMap = BuddyRelationCacheAdapter.getInstance().getMultiRelation(userId, authors);
//	            Set<Integer> keys = relationMap.keySet();
//	            for (Integer key : keys) {
//	                if (relationMap.get(key).isFriend()) {
//	                    friendIds.add(key);
//	                }
//	            }
//	            return friendIds;
//		}
//		
//		//2.并发的去获取，每次
//		int concurrentSize = authorSize / MAX_ONCE_GET_FRIEND + (authorSize % MAX_ONCE_GET_FRIEND > 0 ? 1 : 0) ;
//		List<Future> futures = new ArrayList<Future>(concurrentSize);
//		
//		for(int i = 0;i < concurrentSize;i++){
//			int begin = i * MAX_ONCE_GET_FRIEND;
//			int end = (i + 1 ) * MAX_ONCE_GET_FRIEND > authorSize ? authorSize :  (i + 1 ) * MAX_ONCE_GET_FRIEND;
//			
//			List<Integer> subAuthors = authors.subList(begin, end);
//			
//			Future future = executor.submit(new GetFriendListThread(userId,subAuthors));
//			futures.add(future);
//		}
//		
//		//2.1 等待
//		long sleepTime = TIMEOUT;
//		
//		for(int i = 0; i< concurrentSize;i++){
//			
//			 long begin = System.currentTimeMillis();
//			 
//			 if(sleepTime < 0){
//				 logger.error("getAllFriends sleepTime error|userId:"+userId);
//				 continue;
//			 }
//			try{
//				List<Integer> subFriends = (List<Integer>)futures.get(i).get(sleepTime, TimeUnit.MILLISECONDS);
//				
//				 friendIds.addAll(subFriends);
//			} catch(Exception e){
//				logger.error("getAllFriends error",e);
//			} finally {
//				//更新sleepTime
//				 long cost = System.currentTimeMillis() - begin;
//				 sleepTime = (sleepTime - cost) < 0 ? 0 : sleepTime - cost ;
//			}
//		}
//		
//		return friendIds;
//	}
//	
//	public static void main(String[] args){
//		
//		int userId = 30400315;
//		
//		List<Integer> authors = new ArrayList<Integer>();
//		
//		for(int i = 454926876;i<454927076;i++){
//			authors.add(i);
//		}
//		authors.add(454265592);
//		authors.add(423603526);
//		
//		List<Integer> result = getAllFriends(userId,authors);
//		
//		System.out.println(result);
//	}
//
//}
//class GetFriendListThreadFactory implements ThreadFactory {
//	
//	/**
//	 * counter 
//	 */
//	private int counter;
//	
//	public GetFriendListThreadFactory(){
//		counter = 1;
//	}
//	
//	@Override  
//	public Thread newThread(Runnable r) {  
//		return new Thread(r,"getFriendListThread-"+counter++);  
//	}  
}
