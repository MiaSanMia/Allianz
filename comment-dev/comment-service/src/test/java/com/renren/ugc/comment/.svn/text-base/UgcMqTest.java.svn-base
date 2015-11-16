package com.renren.ugc.comment;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.renren.ugc.comment.interceptor.impl.UgcMqInterceptor;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.framework.mqclient.MqConsumer;
import com.renren.ugc.framework.mqclient.MqConsumerIterator;
import com.renren.ugc.framework.mqclient.MqProducer;
import com.renren.ugc.framework.mqclient.model.MqConsumerGroups;
import com.renren.ugc.framework.mqclient.model.MqTopics;
import com.renren.ugc.framework.mqclient.util.MqUtils;

public class UgcMqTest {
	
	private AtomicLong mockCommentId = new AtomicLong(0);
	private AtomicInteger mockUserId = new AtomicInteger(0);
	
	private int[] types = new int[]{1,2,1,1,2,1};
	
	private Random randomSeed = new Random(System.currentTimeMillis());
	
	private UgcMqInterceptor tester = new UgcMqInterceptor();
	
	@Test
	@Ignore
	public void testSendToMQ(){
		for(int i = 0; i < 100 ; ++ i){
			MqProducer.send(MqTopics.UGC_COMMENTCENTER, null, "test" + Calendar.getInstance().getTime().toLocaleString());
		}
	}

	@Test
	public void testConsumeFromMq() {
		MqConsumer consumer = new MqConsumer(MqConsumerGroups.TEST,
				MqTopics.UGC_COMMENTCENTER);
		MqConsumerIterator it = consumer.createMqConsumerIterator();

		while (it.hasNext()) {
			try {
				System.out.println(MqUtils.getMessage(it.next().message()));
			} catch (Exception e) {
				// logger.error(e.getMessage(), e);
			}
		}
	}
	@Test
	@Before
	public void testProduceCommentInfo(){
		try{
			int loopCount = 100;
			for(int i = 0; i < loopCount; ++i){
				//CommentEventManager.getInstance().sendEvent(CommentEvent.CREATE, mockComment(), mockCommentStrategy());
				tester.create(CommentType.CampusPost, 0, 0, 0, mockComment(), mockCommentStrategy());
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private Comment mockComment(){
		Comment comment = new Comment();
		comment.setId(mockCommentId.getAndIncrement());
		comment.setAuthorId(mockUserId.getAndIncrement());
		comment.setEntry(mockEntry());
		int type_it = Math.abs(randomSeed.nextInt()) % (types.length -1);
		comment.setType(types[type_it]);
		comment.setCreatedTime(System.currentTimeMillis());
		return comment;
	}
	
	private Entry mockEntry(){
		Entry entry = new Entry();
		entry.setId(mockCommentId.getAndIncrement());
		entry.setOwnerId(mockUserId.getAndIncrement());
		return entry;
	}
	
	private CommentStrategy mockCommentStrategy(){
		CommentStrategy strategy = new CommentStrategy();
		return strategy;
	}

}
