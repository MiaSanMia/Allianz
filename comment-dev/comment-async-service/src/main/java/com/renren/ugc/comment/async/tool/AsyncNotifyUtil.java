package com.renren.ugc.comment.async.tool;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.kafka.model.CreateCommentEvent;
import com.renren.ugc.comment.service.AsynJobService;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * 异步发送消息类
 * 
 * @author xiaoqiang
 *
 */
public class AsyncNotifyUtil {
	
	private static Logger logger = Logger.getLogger(AsyncNotifyUtil.class);
	
	private static CommentCenter commentCenter = CommentCenter.getInstance();
	
	/**
	 * 发送也评论消息
	 * 
	 * @param event 创建评论消息
	 */
	public static void asynSendFriendNotify(final CreateCommentEvent event) {
		if (event == null) {
			return;
		}
		
		AsynJobService.asynRun(new Runnable() {
			@Override
            public void run() {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				FutureTask<Boolean> future =
				       new FutureTask<Boolean>(new Callable<Boolean>() {//使用Callable接口作为构造参数
				         public Boolean call() {
				        	 try {
						            if (event != null) {
						                Map<String, String> param = event.getParam();
						                if (param != null && param.get("needSendFriendNotify") !=null && Boolean.valueOf(param.get("needSendFriendNotify"))) {
						                    commentCenter.sendFriendNotify(CommentType.findByValue(event.getCommentType()), event.getActorId(), event.getEntryId(),
						                            event.getEntryOwnerId(), event.getCommentId(), new CommentStrategy());
						                }
						            }
						            return true;
						        } catch (Throwable e) {
						            logger.error("FriendNotifyHandler doHandler error. message=[" + event + "], e=[" + e + "]");
						            return false;
						        }
				       }});
				executor.execute(future);
				Boolean result = Boolean.TRUE;
				try {
					result = future.get(3000, TimeUnit.MILLISECONDS); //取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
					if (!result) {
						logger.error("FriendNotifyHandler doHandler error. message=[" + event + "]");
					}
				} catch (InterruptedException e) {
					future.cancel(true);
				} catch (ExecutionException e) {
					future.cancel(true);
				} catch (TimeoutException e) {
					future.cancel(true);
					logger.error("FriendNotifyHandler doHandler timeout. message=[" + event + "], e=[" + e + "]");
				} finally {
					executor.shutdown();
				}
				
			}
		});
	}

}
