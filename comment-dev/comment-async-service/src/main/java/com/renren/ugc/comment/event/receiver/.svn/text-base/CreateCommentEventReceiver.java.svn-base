package com.renren.ugc.comment.event.receiver;

import com.alibaba.fastjson.JSON;
import com.renren.ugc.comment.event.handler.FriendNotifyHandler;
import com.renren.ugc.comment.event.handler.MatterToMeHandler;
import com.renren.ugc.comment.interceptor.impl.MatterToMeInterceptor;
import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;
import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.kafka.model.CreateCommentEvent;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import org.apache.log4j.Logger;

/**
 * Created by xiaoqiang on 14-1-13.
 */
public class CreateCommentEventReceiver extends AbstractCommentEventReceiver {
    private Logger logger = Logger.getLogger(CreateCommentEventReceiver.class);

    private static final String COMMENT_TOPIC = "create_comment";
    
    private static final String COMMENT_GROUP = "comment_center";

    public CreateCommentEventReceiver(int threadNum) {
        this.setName("Create Comment Event Receiver Thread " + threadNum);
        try {
            consumer = new InfraKafkaHighConsumer(COMMENT_TOPIC, COMMENT_GROUP);
        } catch (InfraKafkaException e) {
            throw new RuntimeException("CreateCommentEventReceiver: Can't create kafka consumer", e);
        }
    }

    @Override
    public void dispatch(String eventStr) {
        CreateCommentEvent event = null;
        try {
            event = JSON.parseObject(eventStr, CreateCommentEvent.class);
        } catch (Exception e) {
            logger.error("parse Object error. e=[" + e + "]");
        }

        if (event == null) {
            logger.error("createCommentEvent is null.");
            return;
        }

        MatterToMeHandler matterToMeHandler = new MatterToMeHandler(null);
        FriendNotifyHandler friendNotifyHandler = new FriendNotifyHandler(matterToMeHandler);
        friendNotifyHandler.doHandler(event);

    }
}
