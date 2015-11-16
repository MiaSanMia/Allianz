package com.renren.ugc.comment.service;

import java.util.concurrent.ThreadFactory;

/**
 * 统一的异步执行的服务
 * User: jinshunlong
 * Date: 12-12-19
 * Time: 下午3:29
 * @deprecated 替换为CommentTaskDispatcherClient来做异步任务 
 */
public class AsynJobService {
	
	private static BaseAsyncJobService core = new BaseAsyncJobService(50,50,new AsyncCommentThreadFactory());

	public static int getQueueSize(){
    	return core.getQueueSize();
    }
	
	public static void asynRun(Runnable runnable){
		core.asynRun(runnable);
	}
}
class AsyncCommentThreadFactory implements ThreadFactory {
	
	/**
	 * counter 
	 */
	private int counter;
	
	public AsyncCommentThreadFactory(){
		counter = 1;
	}
	
	@Override  
	public Thread newThread(Runnable r) {  
		return new Thread(r,"asynCommentThread-"+counter++);  
	}  
}
