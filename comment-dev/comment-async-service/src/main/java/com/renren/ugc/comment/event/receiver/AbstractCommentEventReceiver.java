package com.renren.ugc.comment.event.receiver;

import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;
import org.apache.log4j.Logger;

/**
 * Created by xiaoqiang on 14-1-13.
 */
public abstract class AbstractCommentEventReceiver extends Thread {
    private Logger logger = Logger.getLogger(AbstractCommentEventReceiver.class);

    protected volatile boolean quit = false;

    protected InfraKafkaHighConsumer consumer;

    public void run() {
        if (logger.isInfoEnabled()) {
            logger.info("Start to receive event from kafka");
        }
        while(!quit) {
            // the consumer should always
            String eventStr = consumer.getMessage();
            //logger.error(this.getName() + " get message: " + eventStr);
            // TODO convert to CommentEvent object & dispatch
            dispatch(eventStr);
        }

        if (logger.isInfoEnabled()) {
            logger.info(this.getName() + " has quit");
        }
        consumer.close();
    }


    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    protected abstract void dispatch(String eventStr);
}
