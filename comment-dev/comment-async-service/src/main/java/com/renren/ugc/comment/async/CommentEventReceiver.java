package com.renren.ugc.comment.async;


import com.renren.ugc.comment.kafka.consumer.InfraKafkaHighConsumer;
import com.renren.ugc.comment.kafka.exception.InfraKafkaException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jiankuan
 * Date: 18/12/13
 * Time: 13:39
 * receive the events from message queue and send the event to the dispatch
 */
public class CommentEventReceiver {

    private Logger logger = Logger.getLogger(this.getClass());

    // In kafka we setup 5 partitions per broker * 2 brokers
    private static final int HANDLER_THREAD_COUNT = 5;

    private static final int MAX_WAIT_TO_QUIT_MS = 30000; // 30s

    private CommentEventHandlerThread[] threads = new CommentEventHandlerThread[HANDLER_THREAD_COUNT];

    public CommentEventReceiver() {

        for (int i = 0; i < HANDLER_THREAD_COUNT; i++) {
            CommentEventHandlerThread t = new CommentEventHandlerThread(i + 1);
            threads[i] = t;
        }
    }

    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("Comment event receiver starts to fetching messages");
        }
        for (CommentEventHandlerThread t: threads) {
            t.start();
        }
    }

    public void quit() {
        for(CommentEventHandlerThread t: threads) {
            t.setQuit(true);
        }

        for (CommentEventHandlerThread t: threads) {
            try {
                t.join(MAX_WAIT_TO_QUIT_MS);
            } catch (InterruptedException e) {
                // do nothing, simply join next thread
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("All handler threads have quit");
        }
    }

    static class CommentEventHandlerThread extends Thread {

        private Logger logger = Logger.getLogger(CommentEventHandlerThread.class);

        private volatile boolean quit = false;

        private InfraKafkaHighConsumer consumer;

        private static final String COMMENT_TOPIC = "universe_comment";

        public CommentEventHandlerThread(int threadNum) {
            this.setName("Comment Event Handler Thread " + threadNum);
            try {
                consumer = new InfraKafkaHighConsumer(COMMENT_TOPIC, "1");
            } catch (InfraKafkaException e) {
                throw new RuntimeException("Can't create kafka consumer", e);
            }
        }

        public void run() {
            if (logger.isInfoEnabled()) {
                logger.info("Start to receive event from kafka");
            }
            while(!quit) {
                // the consumer should always
                String eventStr = consumer.getMessage();
                if (logger.isDebugEnabled()) {
                    logger.debug(this.getName() + " get message: " + eventStr);
                }
                // TODO convert to CommentEvent object & dispatch
            }

            if (logger.isInfoEnabled()) {
                logger.info(this.getName() + " has quit");
            }
            consumer.close();
        }


        public void setQuit(boolean quit) {
            this.quit = quit;
        }
    }
}
