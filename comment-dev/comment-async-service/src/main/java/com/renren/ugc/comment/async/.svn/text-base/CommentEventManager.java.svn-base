package com.renren.ugc.comment.async;


import com.renren.ugc.comment.event.receiver.AbstractCommentEventReceiver;
import com.renren.ugc.comment.event.receiver.CreateCommentEventReceiver;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jiankuan
 * Date: 18/12/13
 * Time: 13:39
 * receive the events from message queue and send the event to the dispatch
 */
public class CommentEventManager {

    private Logger logger = Logger.getLogger(this.getClass());

    // In kafka we setup 5 partitions per broker * 2 brokers
    private static final int HANDLER_THREAD_COUNT = 5;

    private static final int MAX_WAIT_TO_QUIT_MS = 30000; // 30s

    private List<AbstractCommentEventReceiver> threads = new LinkedList<AbstractCommentEventReceiver>();

    public CommentEventManager() {

        for (int i = 0; i < HANDLER_THREAD_COUNT; i++) {
            CreateCommentEventReceiver t = new CreateCommentEventReceiver(i + 1);
            threads.add(t);
        }

//        for (int i = 0; i < HANDLER_THREAD_COUNT; i++) {
//            RemoveCommentEventReceiver t = new RemoveCommentEventReceiver(i + 1);
//            threads.add(t);;
//        }
    }

    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("Comment event receiver starts to fetching messages");
        }
        for (AbstractCommentEventReceiver t: threads) {
            t.start();
        }
    }

    public void quit() {
        for(AbstractCommentEventReceiver t: threads) {
            t.setQuit(true);
        }

        for (AbstractCommentEventReceiver t: threads) {
            try {
                t.join(MAX_WAIT_TO_QUIT_MS);
            } catch (InterruptedException e) {
                // do nothing, simply join next thread
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("All receiver threads have quit");
        }
    }

}
