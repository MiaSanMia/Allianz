/**
 * 
 */
package com.renren.ugc.comment.task;

import com.renren.ugc.framework.localqueue.executor.model.TaskNecessity;
import com.renren.ugc.framework.localqueue.executor.model.TaskPriority;


/**
 * 评论异步任务定义 
 * 
 * @author zhang.liang
 * @createTime 2014-4-29 下午4:43:06
 *
 */
public enum CommentTaskType {
   
    
    /** 更新新鲜事评论 */
    UPDATE_FEED("comment.updateFeed", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 删除新鲜事评论 */
    REMOVE_FEED("comment.removeFeed", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 发送评论通知 */
    SEND_NOTICE("comment.sendNotice", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 发送校园主页被评论通知，和正常的评论通知文案不同 */
    SEND_NOTIFIED("comment.sendNotified", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 创建评论后做的事情 */
    CREATE_COMMENT("comment.createComment", 1000, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 删除评论后做的事情 */
    REMOVE_COMMENT("comment.removeComment", 1000, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 更新评论数量相关数字 */
    UPDATE_COMMENT_COUNT("comment.updateCommentCount", 1000, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    
    
    /** 创建全站评论后做的事情 */
    CREATE_GLOBAL_COMMENT("comment.createGlobalComment", 1000, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 删除全站评论后做的事情 */
    REMOVE_GLOBAL_COMMENT("comment.removeGlobalComment", 1000, TaskNecessity.NORMAL, TaskPriority.NORMAL),
     
    /** 发送分享通知 */
    SHARE_SEND_FEED_NOTICE("comment.shareSendFeedNotice", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 发送MQ消息 */
    SEND_MQ_MESSAGE("comment.sendMqMessage", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 异步送审评论*/
    DO_AUDIT("comment.doAudit", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL), 
    
    /** 发送与我相关数据 */
    CALL_MATTER("comment.callMatter", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 把状态评论推送到MQ系统上 */
    SEND_STATUSCOMMENT_TO_MQ("comment.sendStatuscommentToMq", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 语音评论送审 */
    DO_VOICE_AUDIT("comment.doVoiceAudit", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 分享评论后 给好友发也评论的通知 */
    SHARE_FRIEND_NOTICE("comment.shareFriendNotice", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 状态也评论通知  给好友发也评论的通知 */
    SEND_STATUS_FRIEND_NOTICE("comment.sendStatusFriendNotice", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 相册发送with通知 */
    PHOTO_WITH_NOTICE("comment.photoWithNotice", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 发送到评论中心原来的kafka MQ集群上的消息 */
    SEND_COMMENT_OWN_MQ_MESSAGE("comment.sendCommentOwnMqMessage", 500, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    /** 好友评论需要 */
    WRITE_AUTHORS_TO_CACHE("comment.writeAuthorsToCache", 2000, TaskNecessity.NORMAL, TaskPriority.NORMAL),
    
    

    /** 调用搜索接口推送数据 */
    CALL_SEARCH("comment.callSearch", 200, TaskNecessity.NEGLIGIBLE, TaskPriority.LOW),
    
    /**分享评论创建后的处理，包括和其他视频网站的评论互通*/
    SHARE_PUBLIC_FEED_COMMENT("comment.sharePublicFeedComment", 1000, TaskNecessity.NEGLIGIBLE, TaskPriority.LOW),
    
    
    
    /** 给用户加积分 */
    ADD_SCORE("comment.addScore", 500, TaskNecessity.NEGLIGIBLE, TaskPriority.LOW),
    
    /** 更新redis新鲜事评论 */
    UPDATE_REDIS_FEED("comment.updateRedisFeed", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT),
    
    /** 删除redis新鲜事评论 */
    REMOVE_REDIS_FEED("comment.removeRedisFeed", 500, TaskNecessity.NECESSARY, TaskPriority.HIGHT)
     
    ;
    
    
    private String name;
    
    private int timeoutMS;
    
    private TaskNecessity necessity;
    
    private TaskPriority priority;
    
    private CommentTaskType(final String name, final int timeoutMS, TaskNecessity necessity, TaskPriority priority) {
        this.name = name;
        this.timeoutMS = timeoutMS;
        this.necessity = necessity;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public int getTimeoutMS() {
        return timeoutMS;
    }

    public TaskNecessity getNecessity() {
        return this.necessity;
    }

    public TaskPriority getPriority() {
        return priority;
    }
    
    @Override
    public String toString() {
        return String.format("name=%s, timeoutMS=%s, necessity=%s, priority=%s", name, timeoutMS, necessity, priority);
    }

}
