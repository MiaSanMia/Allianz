package com.renren.ugc.comment.event.handler;

import com.renren.ugc.comment.kafka.model.CreateCommentEvent;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import org.apache.log4j.Logger;

/**
 * Created by xiaoqiang on 14-1-13.
 *
 * 处理请求的类的抽象类
 */
public abstract class CreateCommentEventHandler {
    private Logger logger = Logger.getLogger(CreateCommentEventHandler.class);
    // 下一个Handler对象
    private CreateCommentEventHandler nextSuccessor;

    public CreateCommentEventHandler(CreateCommentEventHandler nextSuccessor) {
        this.nextSuccessor = nextSuccessor;
    }
    /*
     * 处理创建评论的事件
     */
    public void handlerEvent(CreateCommentEvent event) {
        try {
            if (event != null) {
                doHandler(event);
            }
        } catch (Throwable e) {
            logger.error("handler comment error. e=[" + e + "]");
        }

        nextHandler(event);

    }

    /*
     * 处理评论
     */
    protected abstract void doHandler(CreateCommentEvent event);

    private void nextHandler(CreateCommentEvent event) {
        if (this.nextSuccessor != null) {
            nextSuccessor.handlerEvent(event);
        }
    }

}
