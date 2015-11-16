/**
 * 
 */
package com.renren.ugc.comment.task;

import com.renren.ugc.framework.localqueue.monitor.alarm.SmsQueueAlarm;


/**
 * 短信报警设置 
 * 
 * @author zhang.liang
 * @createTime 2014-4-29 下午3:44:53
 *
 */
public class CommentSmsQueueAlarm extends SmsQueueAlarm{

    /**
     * 当队列长度超过这个值的时候发短信报警
     */
    @Override
    protected int getAlarmQueueLimitSize() {
        return 1000;
    }

}
