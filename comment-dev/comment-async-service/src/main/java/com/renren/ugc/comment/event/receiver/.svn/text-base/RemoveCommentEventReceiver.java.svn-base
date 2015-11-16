package com.renren.ugc.comment.event.receiver;

import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;
import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import com.renren.ugc.comment.service.CommentCenter;
import org.apache.log4j.Logger;

/**
 * Created by xiaoqiang on 14-1-13.
 */
public class RemoveCommentEventReceiver extends AbstractCommentEventReceiver {
    private Logger logger = Logger.getLogger(CreateCommentEventReceiver.class);

    private static final String COMMENT_TOPIC = "create_comment";

    public RemoveCommentEventReceiver(int threadNum) {
        this.setName("Remove Comment Event Receiver Thread " + threadNum);
        try {
            consumer = new InfraKafkaHighConsumer(COMMENT_TOPIC, "1");
        } catch (InfraKafkaException e) {
            throw new RuntimeException("RemoveCommentEventReceiver: Can't create kafka consumer", e);
        }
    }

    @Override
    public void dispatch(String eventStr) {

    }
}
