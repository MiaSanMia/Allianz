package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.util.AtUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         这个拦截器主要用来过滤@信息，比如actorId和他@的人不是好友的话，那么经过这个函数后，@信息会被破坏掉，从@wangxx(
 *         id) 变成@wangxx(id ) ,然后我们会把这个破坏掉的信息放在数据库中
 * 
 */
public class AtFilterInterceptor extends CommentLogicAdapter {

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {

      //对于所有业务存储的时候都存成@合法的形式，展示的时候由业务方发送的参数isReplaceAt来决定是否转化为@带有链接的形式
//        if (!strategy.isReplaceAt()) {
//            if (logger.isDebugEnabled()) {
//                logger.info("ignore sending comment created atinfo");
//            }
//            return null;
//        }

        //1. check @ is valid
        comment.setContent(this.filterAtContent(comment, actorId, type, entryOwnerId, entryId,
                strategy));

        return comment;
    }
    
    @Override
    public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
    		ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	for(CommentPackage onepackage : forInvokeStrategy.getPackageList()){
    		onepackage.getComment().setContent(this.filterAtContent(onepackage.getComment(), 
    				onepackage.getActorId(), type, entryOwnerId, entryId,onepackage.getForCommentStrategy()));
    	}
    	
    	return forInvokeStrategy.getPackageList();
	}

    /**
     * @param comment
     * @param actorId
     * @return
     * 
     */
    private String filterAtContent(Comment comment, int actorId, final CommentType type,
            final int entryOwnerId, final long entryId, final CommentStrategy strategy) {
        long start = System.nanoTime();
        if (comment == null) {
            return null;
        }

        String filterString = comment.getContent();

        filterString = AtUtil.getInstance().filterAtInfo(actorId, filterString, type, entryOwnerId,
                entryId, comment, strategy);
        StatisticsHelper.invokeAtFilter((System.nanoTime() - start)/StatisticsHelper.NANO_TO_MILLIS, true);
        return filterString;
    }

}
