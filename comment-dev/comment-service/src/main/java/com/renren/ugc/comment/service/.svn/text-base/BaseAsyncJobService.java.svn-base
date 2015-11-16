package com.renren.ugc.comment.service;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxx
 * 封装线程池
 */
public  class BaseAsyncJobService {
	
	/*private  int corePoolSize;
	
	private  int maximumPoolSize;*/
	
	private  LinkedBlockingQueue<Runnable> threadQueue = new LinkedBlockingQueue<Runnable>();
	
	private  Executor executor = null;
	
	BaseAsyncJobService(int corePoolSize,int maximumPoolSize,ThreadFactory threadFactory){
		/*this.corePoolSize = corePoolSize;
		this.maximumPoolSize = maximumPoolSize;*/
		this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
	            0L, TimeUnit.MILLISECONDS,
	            threadQueue,threadFactory);
	}
	
	public  int getQueueSize(){
    	return threadQueue != null ? threadQueue.size() : 0;
    }
	
	public  void asynRun(Runnable runnable){
		if(executor != null){
			executor.execute(runnable);
		}
	}
	

}
