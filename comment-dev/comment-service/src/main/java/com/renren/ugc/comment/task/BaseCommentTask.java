/**
 * 
 */
package com.renren.ugc.comment.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.framework.localqueue.executor.model.AbstractBaseTask;
import com.renren.ugc.framework.localqueue.executor.model.TaskNecessity;


/**
 * 简化一下构造task的成本，打一下log等 
 * 
 * @author zhang.liang
 * @param <V>
 * @createTime 2014-4-29 下午6:13:22
 *
 */
public abstract class BaseCommentTask<V> extends AbstractBaseTask<V>{
 
    private static final long serialVersionUID = 1L;
    
    private static final Log logger = LogFactory.getLog(BaseCommentTask.class);
    
    private CommentTaskType commentTaskType;
    
    public BaseCommentTask(CommentTaskType type){
        commentTaskType = type;
    }
    @Override
    public String getType() {
        return commentTaskType.getName();
    }

    @Override
    public TaskNecessity getNecessity() {
        return commentTaskType.getNecessity();
    }
    
    @Override
    public String getSerializedMsg() {
        return "";
    }
    
    
    @Override
    public V call() throws Exception {
        if(logger.isInfoEnabled()){
            logger.info(commentTaskType);
        }
        return doCall();
    }
    
    @Override
    public int getTimeoutMS() {
        return commentTaskType.getTimeoutMS();
    }
 

}
