package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.service.impl.PhotoCommentMySQLService;
import com.renren.ugc.comment.service.impl.ShareCommentMySQLService;
import com.renren.ugc.comment.service.impl.StatusCommentSpecialNeedsMySQLService;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * 更新状态的评论计数,这个在评论中心中回调状态接口来更新状态相关表的评论数 以后要切换为调评论中心接口来获取评论数
 * 
 * @author lei.xu1
 * @since 2013-09-12
 * 
 */
public class UpdateCountInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {

        // get the just created comment
        Comment latest = (Comment) strategy.getReturnedValue();

        doUpdateCommentCount(type, entryOwnerId, entryId, actorId, 1, latest, strategy);
        return comment;
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    		 // get the just created comment
            List<CommentPackage> latest = (List<CommentPackage>) forInvokeStrategy.getReturnedValue();
            
            //actorId和comment在update中没有用到，所以传定制null和0。批量评论对应一个实体，用forInvokeStrategy
            doUpdateCommentCount(type, entryOwnerId, entryId, 0
            			, forInvokeStrategy.getPackageList().size(), null, forInvokeStrategy);
    	return forInvokeStrategy.getPackageList();
	}

    @Override
    public boolean remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        if (entryId == 0) {
            entryId = strategy.getEntry().getId();
        }
        if ((Boolean)strategy.getReturnedValue()) {
            doUpdateCommentCount(type, entryOwnerId, entryId, actorId, -1, null, strategy);
        }
        return true;
    }
    
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        if (entryId == 0) {
            entryId = strategy.getEntry().getId();
        }
        if ((Boolean)strategy.getReturnedValue()) {
            doUpdateCommentCount(type, entryOwnerId, entryId, actorId, -1, null, strategy);
        }
        return true;
    }

    //更新状态db、好友的状态DB中状态的评论数，还有某人的状态被评论次数
    private void doUpdateCommentCount(final CommentType type, final int entryOwnerId,
            final long entryId, final int actorId, final int incCount, final Comment comment,
            final CommentStrategy strategy) {

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.UPDATE_COMMENT_COUNT){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {

                try {
                    //关联评论
                	List<CommentLinkedInfo> infos = strategy.getCommentLinkedInfos();
                    if (!CollectionUtils.isEmpty(infos)) {
                    	for(CommentLinkedInfo info:infos){
                    		updateLinkedCommentCount(CommentType.findByValue(info.getCommentType()), info.getEntryOwnerId(), info.getEntryId(),incCount);    
                    	}
                    } 
                    
                    //正常评论
                    updateCommentCount(type, entryOwnerId, entryId, incCount, strategy);
                } catch (Exception e) {
                    logger.error("doUpdateCommentCount error", e);
                }
            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                try {
                    //关联评论
                    long tmpEntryId = entryId;
                    if ((comment != null && comment.isLinked()) || strategy.isReplyLinked()) {
                        if (strategy.getAsynSourceId() > 0) {
                            tmpEntryId = strategy.getAsynSourceId();
                        }
                        updateLinkedCommentCount(type,entryOwnerId,tmpEntryId,incCount);    
                    } 
                    
                    //非关联评论
                    updateCommentCount(type, entryOwnerId, entryId, incCount);
                } catch (Exception e) {
                    logger.error("doUpdateCommentCount error", e);
                }
            }
        });*/
    }


    private void updateCommentCount(final CommentType type, final int entryOwnerId,
            final long entryId, final int incCount,final CommentStrategy strategy) {
        switch (type) {
            case Status:
                StatusCommentSpecialNeedsMySQLService.getInstance().updateCommentCount(
                        entryOwnerId, entryId, incCount);
                break;
            case Photo:
                PhotoCommentMySQLService.getInstance().updatePhotoCommentCount(entryOwnerId,
                        entryId, incCount);
                break;
            case Album:
            	if(strategy.getParentEntryId() > 0){
            		//虚拟相册
            		PhotoCommentMySQLService.getInstance().updateChildAlbumCommentCount(entryOwnerId, entryId, incCount);
            	} else {
            		//正常相册
	                PhotoCommentMySQLService.getInstance().updateAlbumCommentCount(entryOwnerId, entryId, incCount);
            	}
                break;
            case Share:
            case ShareAlbumPhoto:
                ShareCommentMySQLService.getInstance().updateShareCommentCount(entryOwnerId, entryId, incCount);
                break;
            default:
        }
    }
    
    private void updateLinkedCommentCount(final CommentType type, final int entryOwnerId,
            final long entryId, final int incCount) {
        switch (type) {
            case Album:
            	//照片评论同步到虚拟相册
            	PhotoCommentMySQLService.getInstance().updateChildAlbumCommentCount(entryOwnerId, entryId, incCount);
                break;
            case Photo:
            	//虚拟相册同步到照片
            	PhotoCommentMySQLService.getInstance().updatePhotoCommentCount(entryOwnerId,
                        entryId, incCount);
            	break;
            default:
        }
    }
    
    
        


}
