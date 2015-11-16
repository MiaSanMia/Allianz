package com.renren.ugc.comment.service;

import java.util.concurrent.ThreadFactory;

/**
 * @author wangxx
 * 
 *  全局评论的异步任务，因为全局评论表有时比较慢，所以这里我们把{#link:AsynJobService}区分开来
 * @deprecated 替换为CommentTaskDispatcherClient来做异步任务
 */
public class AsynGlobalCommentJobService {

	//private static BaseAsyncJobService core = new BaseAsyncJobService(30,30,new GlobalCommentThreadFactory());

	/*public static int getQueueSize(){
    	return core.getQueueSize();
    }*/
	
	/*public static void asynRun(Runnable runnable){
		core.asynRun(runnable);
	}*/

}
class GlobalCommentThreadFactory implements ThreadFactory {
	
	/**
	 * counter 
	 */
	private int counter;
	
	public GlobalCommentThreadFactory(){
		counter = 1;
	}
	
	@Override  
	public Thread newThread(Runnable r) {  
		return new Thread(r,"asynGlobalCommentThread-"+counter++);  
	}  
}
