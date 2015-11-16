package com.renren.ugc.comment.event.handler;

import com.renren.ugc.comment.async.tool.AsyncNotifyUtil;
import com.renren.ugc.comment.kafka.model.CreateCommentEvent;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.NotifyUtil;
import com.renren.ugc.comment.xoa2.CommentType;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by xiaoqiang on 14-1-13.
 *
 *
 */
public class FriendNotifyHandler extends CreateCommentEventHandler {
    private Logger logger = Logger.getLogger(FriendNotifyHandler.class);

    public FriendNotifyHandler(CreateCommentEventHandler nextSuccessor) {
        super(nextSuccessor);
    }

    @Override
    public void doHandler(CreateCommentEvent event) {
    	try {
    		if (CommentType.Share.getValue() == event.getCommentType()) {
    			AsyncNotifyUtil.asynSendFriendNotify(event);
    		}
    	} catch (Throwable t) {
    		logger.error("AsyncNotifyUtil.asynSendFriendNotify(event) error. event=[" + event + "], throwable=[" + t + "]");
    	}
    }
}
