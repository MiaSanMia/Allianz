package com.renren.ugc.comment.interceptor.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CreateCommentEvent;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.framework.mqclient.MqConsumer;
import com.renren.ugc.framework.mqclient.MqConsumerIterator;
import com.renren.ugc.framework.mqclient.model.MqConsumerGroups;
import com.renren.ugc.framework.mqclient.model.MqTopics;
import com.renren.ugc.framework.mqclient.util.MqUtils;
/**
 * 初始化cache的值
 * 
 */
public class UgcMqInterceptorTest {
	
	private UgcMqInterceptor tester = new UgcMqInterceptor();
	
	/** 校园主页 需要的参数  学校id */
	private static final String KEY_CAMPUS_POST_SCHOOL_ID = "campus_post_school_id";
	/** 校园主页 需要的参数  帖子的创建时间 */
	private static final String KEY_CAMPUS_POST_CREATETIME = "campus_post_createtime";
	
	@Test
	public void testConsumeFromUgcMQ(){
		MqConsumer consumer = new MqConsumer(MqConsumerGroups.CAMPUS_RP_SYNC,
				MqTopics.UGC_COMMENTCENTER);
		MqConsumerIterator it = consumer.createMqConsumerIterator();
		int count = 0;
		while(it.hasNext()){
			System.out.println(MqUtils.getMessage(it.next().message()));
			count += 1;
			System.out.println("in" + count);
		}
		System.out.println(count);
	}
	
	@Test
	public void testFastJson(){
		Comment comment = new Comment();
		Entry entry = new Entry();
		entry.setEntryProps(new HashMap<String, String>(1));
		comment.setEntry(entry);
		System.out.println(JSON.toJSONString(generateCreateCommentEvent(comment)));
	}
	
	public CreateCommentEvent generateCreateCommentEvent(Comment comment) {
		CreateCommentEvent commentEvent = new CreateCommentEvent();
		commentEvent.setActorId(comment.getAuthorId());
		commentEvent.setCommentId(comment.getId());
		commentEvent.setCommentType(comment.getType());
		commentEvent.setEntryId(comment.getEntry().getId());
		commentEvent.setEntryOwnerId(comment.getEntry().getOwnerId());
		commentEvent.setCreateTime(comment.getCreatedTime());
		Map<String, String> params = new HashMap<String, String>(2);
		Map<String, String> entryProps = comment.getEntry() == null ? null
				: comment.getEntry().getEntryProps();
		if (null != entryProps) {
			params.put(KEY_CAMPUS_POST_SCHOOL_ID, 1112222 + "");
			params.put(KEY_CAMPUS_POST_CREATETIME, System.currentTimeMillis()
					+ "");
		}
		commentEvent.setParam(params);
		return commentEvent;
	}
	
}
