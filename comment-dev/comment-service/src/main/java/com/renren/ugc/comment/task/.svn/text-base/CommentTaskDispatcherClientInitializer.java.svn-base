package com.renren.ugc.comment.task;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;


/**
 * 初始化内存队列 
 * 
 * @author zhang.liang
 * @createTime 2014-4-29 下午4:00:22
 *
 */
@Service
public class CommentTaskDispatcherClientInitializer {

    private static final Log logger = LogFactory.getLog(CommentTaskDispatcherClientInitializer.class);
    
    @PostConstruct
    public void initTaskDispatcherClient() throws Exception {
        CommentTaskDispatcherClient.init();
        if(logger.isInfoEnabled()){
            logger.info("CommentTaskDispatcherClientInitializer has bean initialized");
        }
        
    }
}
