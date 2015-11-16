package com.renren.ugc.comment.service;

import java.util.concurrent.ThreadFactory;

/**
 * @author wangxx
 * 
 * 默认的异步打印任务
 *
 */
public class AsynPrintJobService{
	
	private static BaseAsyncJobService core = new BaseAsyncJobService(5,5,new AsyncPrintThreadFactory());

	public static int getQueueSize(){
    	return core.getQueueSize();
    }
	
	public static void asynRun(Runnable runnable){
		core.asynRun(runnable);
	}
		
}
class AsyncPrintThreadFactory implements ThreadFactory {
	
	/**
	 * counter 
	 */
	private int counter;
	
	public AsyncPrintThreadFactory(){
		counter = 1;
	}
	
	@Override  
	public Thread newThread(Runnable r) {  
		return new Thread(r,"asynPrintThread-"+counter++);  
	}  
}
