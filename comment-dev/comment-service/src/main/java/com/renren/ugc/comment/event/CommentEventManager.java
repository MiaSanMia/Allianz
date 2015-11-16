package com.renren.ugc.comment.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CreateCommentEvent;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.framework.mqclient.MqProducer;
import com.renren.ugc.framework.mqclient.model.MqTopics;

/**
 * Created by xiaoqiang on 14-1-17.
 *
 * send kafka event message
 */
public class CommentEventManager {
    private static Logger logger = Logger.getLogger(CommentEventManager.class);

    private static CommentEventManager instance = new CommentEventManager();
    
    //private static Executor executor = Executors.newFixedThreadPool(10);

    public static enum  CommentEvent {
        CREATE, REMOVE
    };

//    private static InfraKafkaProducer producerHandler = null;
//    static {
//        try {
//            producerHandler = new InfraKafkaProducer();
//        } catch (InfraKafkaException e2) {
//            logger.error("Start producerHandler error. exception=[" + e2 + "]");
//        }
//    }

//    private static final String CREATE_COMMENT_EVENT_TOPIC = "create_comment";

    //private static final String REMOVE_COMMENT_EVENT_TOPIC = "remove_comment";

    public static CommentEventManager getInstance() { return instance; }


    /**
     * 异步发送评论事件
     *
     * @param event
     * @param comment
     * @param strategy
     */
    public void sendEvent(final CommentEvent event, final Comment comment, final CommentStrategy strategy) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_COMMENT_OWN_MQ_MESSAGE){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {

                if (CommentEvent.CREATE.equals(event)) {
                    CreateCommentEvent commentEvent = generateCreateCommentEvent(comment, strategy);
                    if (commentEvent != null) {
                        String str = JSON.toJSONString(commentEvent);
                        try {
//                            if (!producerHandler.sendMessage(CREATE_COMMENT_EVENT_TOPIC, String.valueOf(String.valueOf(comment.getId())), str)) {
//                                logger.error("send create comment event error. commentEvent=[" + str + "]");
//                            }
                        	//替换为新版客户端
                        	MqProducer.send(MqTopics.CREATE_COMMENT, comment.getId(), str);
                        } catch (Throwable e) {
                            logger.error("send create comment event Exception. commentEvent=[" + str + "], exception=[" + e + "]");
                        }
                    }
                }
            
                return null;
            }
        });
        
    	/*Runnable task = new Runnable() {
    		@Override
    		public void run() {
    			if (CommentEvent.CREATE.equals(event)) {
    	            CreateCommentEvent commentEvent = generateCreateCommentEvent(comment, strategy);
    	            if (commentEvent != null) {
    	                String str = JSON.toJSONString(commentEvent);
    	                try {
    	                    if (!producerHandler.sendMessage(CREATE_COMMENT_EVENT_TOPIC, String.valueOf(String.valueOf(comment.getId())), str)) {
    	                        logger.error("send create comment event error. commentEvent=[" + str + "]");
    	                    }
    	                } catch (Throwable e) {
    	                    logger.error("send create comment event Exception. commentEvent=[" + str + "], exception=[" + e + "]");
    	                }
    	            }
    	        }
    		}
    	};
    	
    	//使用线程池来执行发送事件
    	executor.execute(task);  */
        
    }

    /**
     * 生成CreateCommentEvent对象
     *
     * @param comment
     * @param strategy
     * @return
     */
    private CreateCommentEvent generateCreateCommentEvent(Comment comment, CommentStrategy strategy) {
        CreateCommentEvent commentEvent = new CreateCommentEvent();
        commentEvent.setActorId(comment.getAuthorId());
        commentEvent.setCommentId(comment.getId());
        commentEvent.setCommentType(comment.getType());
        commentEvent.setEntryId(comment.getEntry().getId());
        commentEvent.setEntryOwnerId(comment.getEntry().getOwnerId());
        commentEvent.setCreateTime(comment.getCreatedTime());
        Map<String, String> map = new HashMap<String, String>();
        map.put("needSendFriendNotify", strategy.getNeedSendFriendNotify().toString());
        commentEvent.setParam(map);
        return commentEvent;
    }
}
