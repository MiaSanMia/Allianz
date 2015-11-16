/**
 * 
 */
package com.renren.ugc.comment.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.renren.ugc.framework.localqueue.executor.CommonThreadFactory;
import com.renren.ugc.framework.localqueue.executor.LogTaskPersister;
import com.renren.ugc.framework.localqueue.executor.TaskDispatcher;
import com.renren.ugc.framework.localqueue.executor.model.ITask;
import com.renren.ugc.framework.localqueue.executor.model.TaskProcessParams;
import com.renren.ugc.framework.localqueue.executor.model.ThreadPoolExecutorParam;
import com.renren.ugc.framework.localqueue.executor.model.ThreadPoolExecutorParams;
import com.renren.ugc.framework.localqueue.monitor.alarm.QueueAlarm;
import com.renren.ugc.framework.localqueue.monitor.alarm.SmsQueueAlarm;


/**
 * 内存队列的客户端，主要是配置一下参数，提供一个入口 
 * 
 * @author zhang.liang
 * @createTime 2014-4-29 下午3:32:06
 *
 */
public class CommentTaskDispatcherClient {

    private static TaskDispatcher taskDispatcher = null;
    
    private static final String BUSINESS_NAME = "comment-center";
    private static final int NECESSARY_THREAD_POOL_QUEUE_SIZE = 100000;
    private static final String NECESSARY_THREAD_POOL_PREFIX = BUSINESS_NAME + "-ugc-framework-necessary-thread-";
    private static final int NORMAL_THREAD_POOL_QUEUE_SIZE = 100000;
    private static final String NORMAL_THREAD_POOL_PREFIX = BUSINESS_NAME + "-ugc-framework-normal-thread-";
    private static final int NEGLIGIBLE_THREAD_POOL_QUEUE_SIZE = 100000;
    private static final String NEGLIGIBLE_THREAD_POOL_PREFIX = BUSINESS_NAME + "-ugc-framework-negligible-thread-";

    public static void init() throws Exception {
        ArrayList<String> alarmPhoneNumbers = new ArrayList<String>();
        alarmPhoneNumbers.add("13661226467");// chao.wang9
        alarmPhoneNumbers.add("15210833640");// meng.liu
        alarmPhoneNumbers.add("15801462977");// zhang.liang
        
        TaskProcessParams taskProcessParams = new TaskProcessParams();
        taskProcessParams.setTaskPersister(new LogTaskPersister());
        SmsQueueAlarm smsQueueAlarm = new CommentSmsQueueAlarm();
        smsQueueAlarm.setPhoneNumbers(alarmPhoneNumbers);
        HashSet<QueueAlarm> queueAlarms = new HashSet<QueueAlarm>();
        queueAlarms.add(new SmsQueueAlarm());
        taskProcessParams.setQueueAlarms(queueAlarms);
        taskProcessParams.setBusinessName(BUSINESS_NAME);
        
        int processorCount = Runtime.getRuntime().availableProcessors();
        
        ThreadPoolExecutorParams executorsParams = new ThreadPoolExecutorParams();
        executorsParams.setNecessaryExecutorParam(new ThreadPoolExecutorParam(processorCount,
                (int) (processorCount * 2), 5, TimeUnit.MINUTES,
                NECESSARY_THREAD_POOL_QUEUE_SIZE, new CommonThreadFactory(
                        NECESSARY_THREAD_POOL_PREFIX)));
        executorsParams.setNormalExecutorParam(new ThreadPoolExecutorParam(processorCount,
                processorCount * 2, 5, TimeUnit.MINUTES, NORMAL_THREAD_POOL_QUEUE_SIZE,
                new CommonThreadFactory(NORMAL_THREAD_POOL_PREFIX)));
        executorsParams.setNegligibleExecutorParam(new ThreadPoolExecutorParam(processorCount / 2,
                processorCount, 5, TimeUnit.MINUTES, NEGLIGIBLE_THREAD_POOL_QUEUE_SIZE,
                new CommonThreadFactory(NEGLIGIBLE_THREAD_POOL_PREFIX)));
        
        taskDispatcher = new TaskDispatcher(executorsParams, taskProcessParams);
        taskDispatcher.init();
        taskDispatcher.initStartThreads();
    }
    
    public static void putTask(ITask<?> task) {
        taskDispatcher.putTask(task);
    }

}
