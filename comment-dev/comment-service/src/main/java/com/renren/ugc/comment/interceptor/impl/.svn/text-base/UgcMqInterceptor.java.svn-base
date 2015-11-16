package com.renren.ugc.comment.interceptor.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CreateCommentEvent;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.EntryConfigUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.framework.mqclient.MqProducer;
import com.renren.ugc.framework.mqclient.model.MqTopics;

/**
 * 这个拦截器用来把一些消息发送到ugc的kafka集群中
 * 
 * @author tuanwang.liu@ 2014--3-12
 * 
 */
public class UgcMqInterceptor extends CommentLogicAdapter {
	/** ugc mq 开关 存入kv */
	private String ugc_mq_switch = "off";

	private Log logger = LogFactory.getLog(UgcMqInterceptor.class);
	
	/** 校园主页 需要的参数  学校id */
	private static final String KEY_CAMPUS_POST_SCHOOL_ID = "campus_post_school_id";
	/** 校园主页 需要的参数  帖子的创建时间 */
	private static final String KEY_CAMPUS_POST_CREATETIME = "campus_post_createtime";

	@Override
	public Comment create(CommentType type, int actorId, long entryId,
			int entryOwnerId, Comment comment, CommentStrategy strategy) {
        //1.get return value
        Comment result = (Comment) strategy.getReturnedValue();
        
        if(result == null){
        	return null;
        }
		asynSendMessage(result, strategy);
		return null;
	}

	private void asynSendMessage(final Comment comment, final CommentStrategy strategy) {
	    
	    CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_MQ_MESSAGE){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {
                sendMessage(comment, strategy);
                return null;
            }
        });
	    
	    
		/*AsynJobService.asynRun(new Runnable() {

			@Override
			public void run() {
				sendMessage(comment, strategy);
			}
		});*/
	}

	private void sendMessage(Comment comment, CommentStrategy strategy) {
		String cacheSwitch = (String) TairCacheManagerImpl.getInstance().get(
				TairCacheManagerImpl.UGCMQ_ON_OFF_SWITCH, "ugcMQ");
		if (StringUtils.isNotBlank(cacheSwitch)) {
			ugc_mq_switch = cacheSwitch;
		}
		if ("on".equals(ugc_mq_switch)) {
			String message = buildMessage(comment, strategy);
			try {
				// 同时发往ugc的 broker集群, 给ugc业务需要订阅
				MqProducer.send(MqTopics.UGC_COMMENTCENTER, comment.getId(),
						message);
			} catch (Exception e) {
				logger.error("send to mq error, the message is " + message, e);
			}
		}
	}

	private String buildMessage(Comment comment, CommentStrategy strategy) {
		CreateCommentEvent createEvent = generateCreateCommentEvent(comment, strategy);
		return JSON.toJSONString(createEvent);
	}

	/**
	 * 生成CreateCommentEvent对象
	 * 
	 * @param comment
	 * @param strategy
	 * @return
	 */
	private CreateCommentEvent generateCreateCommentEvent(Comment comment,
			CommentStrategy strategy) {
		CreateCommentEvent commentEvent = new CreateCommentEvent();
		commentEvent.setActorId(comment.getAuthorId());
		commentEvent.setCommentId(comment.getId());
		commentEvent.setCommentType(comment.getType());
		Entry entry = strategy.getEntry();
		commentEvent.setEntryId(entry.getId());
		commentEvent.setEntryOwnerId(entry.getOwnerId());
		commentEvent.setCreateTime(comment.getCreatedTime());
		Map<String, String> params = new HashMap<String, String>(2);
		params.put(KEY_CAMPUS_POST_SCHOOL_ID,
				EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SCHOOL_ID)
						+ "");
		params.put(
				KEY_CAMPUS_POST_CREATETIME,
				EntryConfigUtil.getLong(strategy,
						EntryConfig.ENTRY_CAMPUS_POST_CREATETIME) + "");
		commentEvent.setParam(params);
		return commentEvent;
	}
}
