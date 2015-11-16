package com.renren.ugc.comment.interceptor.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.framework.mqclient.MqProducer;
import com.renren.ugc.framework.mqclient.model.MqTopics;

/**
 * 向ugc的MQ上发送评论数据
 * 
 * @author lei.xu1
 * @since 2013-09-11
 */
public class StatusCommentMqInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {
        doSendStatusCommentToMq(type, actorId, entryId, entryOwnerId, comment, strategy);
        return comment;
    }

    // 更新状态db、好友的状态DB中状态的评论数，还有某人的状态被评论次数
    private void doSendStatusCommentToMq(CommentType type, int actorId, final long entryId,
            final int entryOwnerId, final Comment comment, CommentStrategy strategy) {

        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SEND_STATUSCOMMENT_TO_MQ){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {

                if (comment == null) {
                    logger.error(" comment is null ");
                    return null;
                }
                FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", comment.getId());
                jsonObject.put("doing_id", entryId);
                jsonObject.put("doing_owner_id", entryOwnerId);

                jsonObject.put("user_id", comment.getAuthorId());
                jsonObject.put("content", comment.getContent());
                jsonObject.put("create_time", dateFormat.format(comment.getCreatedTime()));

                jsonObject.put("reply_to_user_id", comment.getToCommentId());
                jsonObject.put("is_voice_comment", comment.isVoiceComment());

                long time = System.currentTimeMillis();
                // mq 开头的是 系统使用字段
                jsonObject.put("mq_action", "create");
                jsonObject.put("mq_gid", "statusreply_" + comment.getId());
                jsonObject.put("mq_sendtime", time);
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("%s|%s|%s|cc", comment.getId(), time,
                            comment.getAuthorId()));
                }
                MqProducer.send(MqTopics.UGC_STATUS_REPLY, null, jsonObject.toString());
            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                if (comment == null) {
                    logger.error(" comment is null ");
                    return;
                }
                FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", comment.getId());
                jsonObject.put("doing_id", entryId);
                jsonObject.put("doing_owner_id", entryOwnerId);

                jsonObject.put("user_id", comment.getAuthorId());
                jsonObject.put("content", comment.getContent());
                jsonObject.put("create_time", dateFormat.format(comment.getCreatedTime()));

                jsonObject.put("reply_to_user_id", comment.getToCommentId());
                jsonObject.put("is_voice_comment", comment.isVoiceComment());

                long time = System.currentTimeMillis();
                // mq 开头的是 系统使用字段
                jsonObject.put("mq_action", "create");
                jsonObject.put("mq_gid", "statusreply_" + comment.getId());
                jsonObject.put("mq_sendtime", time);
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("%s|%s|%s|cc", comment.getId(), time,
                            comment.getAuthorId()));
                }
                MqProducer.send(MqTopics.UGC_STATUS_REPLY, null, jsonObject.toString());
            }
        });*/
    }

}
