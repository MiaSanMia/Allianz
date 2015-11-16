package com.renren.ugc.comment.interceptor.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.MatterToMeUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         保存"与我相关"的数据 由于"与我相关"中设计到@的列表，因此需要放在{@link
 *         :SendNoticeInterceptor} 拦截器之后
 * 
 */
public class MatterToMeInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {

        //1.get comment
        Comment createdComment = (Comment) strategy.getReturnedValue();

        //2.get "at" id list
        List<Integer> atIdLists = strategy.getAtIdLists();

        atIdLists = uniqueListElements(atIdLists);

        this.doSaveMatterToMe(type, createdComment, entryOwnerId, entryId, actorId, atIdLists,
                strategy);

        return null; // as an interceptor, returned value is of no use
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {

    		//1.get comment
            List<CommentPackage> createdComment = (List<CommentPackage>) forInvokeStrategy.getReturnedValue(); 
    	
    	return null;
	}
    

    private void doSaveMatterToMe(final CommentType type, final Comment comment,
            final int entryOwnerId, final long entryId, final int actorId,
            final List<Integer> userIdList, final CommentStrategy strategy) {

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CALL_MATTER){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {
                boolean result = MatterToMeUtil.getInstance().saveMatterTomeData(type, comment,
                        entryOwnerId, entryId, actorId, userIdList, strategy);
                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("saveMatterTomeData comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("saveMatterTomeData return false");
                }
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                boolean result = MatterToMeUtil.getInstance().saveMatterTomeData(type, comment,
                        entryOwnerId, entryId, actorId, userIdList, strategy);
                if (result) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("saveMatterTomeData comment " + comment.getId() + " success");
                    }
                } else {
                    logger.warn("saveMatterTomeData return false");
                }
            }
        });*/
    }
    
    private void doSaveMatterToMeList(final CommentType type, 
            final int entryOwnerId, final long entryId, 
            final List<Integer> userIdList,final List<CommentPackage> commentList ) {

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CALL_MATTER){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {
            	for(CommentPackage onepackage : commentList){
	                boolean result = MatterToMeUtil.getInstance().saveMatterTomeData(type, onepackage.getComment(),
	                        entryOwnerId, entryId, onepackage.getActorId(), userIdList, onepackage.getForCommentStrategy());
	                if (result) {
	                    if (logger.isDebugEnabled()) {
	                        logger.debug("saveMatterTomeDataList comment " + onepackage.getComment().getId() + " success");
	                    }
	                } else {
	                    logger.warn("saveMatterTomeData return false");
	                }
            	}
                return null;
            }
        });
        
    }

    /**
     * remove duplicated data in a list.
     * 
     * @param list
     * @return
     */
    private <T> List<T> uniqueListElements(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("用户输入的at的userid:" + Arrays.toString(list.toArray()));
        }

        if (list.size() >= 2) {
            Set<T> userIdSet = new HashSet<T>(list);
            list.clear();
            list.addAll(userIdSet);
        }

        return list;
    }

}
