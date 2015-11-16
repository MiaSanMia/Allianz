/**
 * 
 */
package com.renren.ugc.comment.service;

import java.util.concurrent.ThreadFactory;

/**
 * @author zhang.liang
 * @createTime 2014-5-5 下午5:33:39
 * 
 */
public class FriendRelationThreadPool {

    private static BaseAsyncJobService core = new BaseAsyncJobService(50, 50, new FriendRelationThreadFactory());

    public static int getQueueSize() {
        return core.getQueueSize();
    }

    public static void asynRun(Runnable runnable) {
        core.asynRun(runnable);
    }

}

class FriendRelationThreadFactory implements ThreadFactory {

    /**
     * counter
     */
    private int counter;

    public FriendRelationThreadFactory() {
        counter = 1;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "friendRelationThread-" + counter++);
    }
}
