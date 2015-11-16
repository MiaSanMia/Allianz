package com.renren.ugc.comment.async;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: jiankuan
 * Date: 18/12/13
 * Time: 13:36
 * The main portal of comment asynchronous service
 */
public class CommentAsyncService {
    private static final Object monitor = new Object();

    private static Logger logger = Logger.getLogger(CommentAsyncService.class);

    private static ApplicationContext currentContext = new ClassPathXmlApplicationContext(
            "classpath:applicationContext-service.xml");

    public static void main(String[] args) {
        CommentEventManager commentEventManager = (CommentEventManager) currentContext.getBean("commentEventManager");
        if (commentEventManager == null) {
            logger.error("commentEventManager is null.");
        }
        commentEventManager.start();
    }
}
