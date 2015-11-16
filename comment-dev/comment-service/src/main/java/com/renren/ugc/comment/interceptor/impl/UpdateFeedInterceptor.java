package com.renren.ugc.comment.interceptor.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.EntryConfigUtil;
import com.renren.ugc.comment.util.FeedPublisher;
import com.renren.ugc.comment.util.FeedUtil;
import com.renren.ugc.comment.util.PageSendFeedPublisher;
import com.renren.ugc.comment.util.Photo708FeedPublisher;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;
import com.xiaonei.platform.component.xfeed.remove.XFeedRemover;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

/**
 * send comment created notification to feed
 * 
 * @author jiankuan.xing
 * 
 */
public class UpdateFeedInterceptor extends CommentLogicAdapter {

    private static final Log logger = LogFactory.getLog(UpdateFeedInterceptor.class);

    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {
        updateFeedAsyn(type, actorId, entryId, entryOwnerId, comment, strategy);
        return comment;
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	//选取最后一个非悄悄话的评论加入feed
    	List<CommentPackage> packageList = forInvokeStrategy.getPackageList(); 

    	if(null != packageList){
    		for(int i=packageList.size()-1;i>=0;--i){
    			if(packageList.get(i).getComment().getWhipserToId()<=0){
    				updateFeedAsyn(type, packageList.get(i).getActorId(), entryId, 
    						entryOwnerId, packageList.get(i).getComment(), packageList.get(i).getForCommentStrategy());
    				break;
    			}
    		}
    	}
    	return null;
	}

    @Override
    public boolean remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) {
        
        Boolean result = (Boolean) strategy.getReturnedValue();
        if (!result) {
            return false;
        }
        
        removeFeedAsyn(type, actorId, entryId, entryOwnerId, commentId, strategy);
        return true;
    }
    
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) {
        Boolean result = (Boolean) strategy.getReturnedValue();
        if (result) {
            removeFeedAsyn(type, actorId, entryId, entryOwnerId, commentId, strategy);
        }
        return true;
    }

    private void doPageSendFeedPublisher(final CommentType type, final Comment oldest,
            final Comment latest, final int entryOwnerId, final long entryId, final int actorId,
            final boolean isDelete, final int count) {

        try {
            // publisher page reply
            User host = WUserAdapter.getInstance().get(actorId);
            PageSendFeedPublisher.sendPageReplyFeed(host, entryId, entryOwnerId, count, oldest,
                    latest, latest != null ? latest.getToUserId() : 0, isDelete, latest != null ? latest.getToCommentId() : 0, type);
        } catch (Exception e) {
            logger.error("doPageSendFeedPublisher error", e);
        }
    }

    /**
     * @param commentType
     * @return
     * 
     *         判断各个业务是否要发送page新鲜事
     */
    private boolean needSendPageFeed(CommentType commentType, int entryOwnerId,
            CommentStrategy strategy) {

        if (!User.isPageId(entryOwnerId)) {
            return false;
        }

//        boolean ret = false;
//
//        switch (commentType) {
//            case Album:
//            case Photo:
//                ret = EntryConfigUtil.getIsPublic(commentType, strategy);
//                break;
//            case Blog:
//                //blog 没有判断public
//                ret = true;
//                break;
//            default:
//
//        }
//
//        return ret;
        return true;
    }

    private int getPhotoStype(int actorId, CommentStrategy strategy, int entryOwnerId,
            long entryId, Comment latest, Comment oldest) {
        int stype = FeedDefinition.PHOTO_PUBLISH_ONE;
        boolean withHeadFeed = EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_HEAD_FEED);

        if (withHeadFeed) {
            //头像新鲜事
            return FeedDefinition.HEADPHOTO_UPDATE;
        } else {
            //708新鲜事
            boolean send708Feed = !User.isPageId(entryOwnerId)
                    && EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_MULTI)
                    && dateInMonth(EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_CREATE_TIME));
            if (send708Feed) {
                if (latest != null && oldest == null) {
                    //调用照片接口，发送708新鲜事
                    Photo708FeedPublisher.send708Feed(actorId, entryOwnerId, entryId, strategy,
                            latest, oldest);
                }
                return strategy.getFeedType() > 0 ? strategy.getFeedType() : FeedDefinition.PHOTO_REPLY;
            }
        }
        
        return strategy.getFeedType() > 0 ? strategy.getFeedType() : stype;
    }

    private boolean dateInMonth(final long dateTime) {
        boolean dateInMonth = false;
        final int day = (int) ((new Date().getTime() - dateTime) / (24 * 60 * 60 * 1000));
        if (day <= 15) {
            dateInMonth = true;
        }
        return dateInMonth;
    }

    private void updateFeedAsyn(final CommentType type, final int actorId, final long entryId,
            final int entryOwnerId, final Comment comment, final CommentStrategy strategy) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.UPDATE_FEED){
     
            private static final long serialVersionUID = 1L;

            @Override
            public String getSerializedMsg() {
                return String.format("%s %s %s %s %s %s ", type, actorId, entryId, entryOwnerId, comment, strategy);
            }

            @Override
            protected Void doCall() throws Exception {
                updateFeed(type, actorId, entryId, entryOwnerId, comment, strategy);
                return null;
            }
        });
        
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                updateFeed(type, actorId, entryId, entryOwnerId, comment, strategy);
            }
        });*/
    }

    private void updateFeed(CommentType type, int actorId, long entryId, int entryOwnerId,
            final Comment comment, final CommentStrategy strategy) {
        CommentLogic commentLogic = strategy.getCommentLogic();

        //1.get sourceId
        if (strategy.getSourceId() == 0) {
            //如果是ugc的，那么则用entryId
            switch (type) {
                case Blog:
                case Album:
                case Photo:
                case Status:
                case ShortVideo:
                    strategy.setSourceId(entryId);
                    break;
                default:
            }
        }
//        //关联评论的话,添加的新鲜事的sourceId应该是关联评论的主entryId,这个是在{@link:CommentLogicImpl #doReplyWithLinked()}方法里设置的
//        if (strategy.getAsynSourceId() > 0) {
//            strategy.setSourceId(strategy.getAsynSourceId());
//            entryId = strategy.getAsynSourceId();
//        }
        
        // 2.get the total number
        long count = commentLogic.getCount(type, actorId, entryId, entryOwnerId, strategy);

        // 3.get the just created comment
        Comment latest = (Comment) strategy.getReturnedValue();

        if (latest.getWhipserToId() > 0) {
            // is a whipser, return
            if (logger.isDebugEnabled()) {
                logger.debug("UpdateFeedInterceptor create getLatest is whiper|entryId:" + entryId
                        + "|entryOwnerId:" + entryOwnerId);
            }
            return;
        }

        //4. get the oldest comment
        Comment oldest = null;
        if (count > 1) {
            oldest = commentLogic.getOldestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);
        }

        
        // 5. get feedtype
        int feedtype = strategy.getFeedType();
        if (type == CommentType.Photo) {
            //photo新鲜事类型比较多，这里对photo业务做了特殊的stype处理
            feedtype = this.getPhotoStype(actorId, strategy, entryOwnerId, entryId, latest, oldest);
        }
        if(feedtype == 0){
            feedtype = getDefaultFeedStype(type);
        }
        
        //6.get feedownerId
        int feedOwnerId = strategy.getFeedOwnerId();
        
        /*
         * 7.真正的发送新鲜事
         */
        try {
            // update feed
            FeedPublisher.publishFeedComment(entryOwnerId, oldest, latest, entryId, feedtype,
                    strategy.isReplaceUbb(), strategy.getSourceId(), count, 0,feedOwnerId,strategy);

        } catch (UGCCommentException e) {
            // something is wrong, remove the created comment
            commentLogic.remove(type, actorId, entryId, entryOwnerId, latest.getId(), strategy);
            logger.warn("Due to an exception during updating to feed, remove the just created comment");
            throw e;
        }

        //8.给page发送新鲜事
        if (this.needSendPageFeed(type, entryOwnerId, strategy)) {
            this.doPageSendFeedPublisher(type, oldest, latest, entryOwnerId, entryId, actorId,
                    false, (int) count);
        }

        //9.一些特殊处理
        //这段代码逻辑非常丑，因为blog增加评论的时候会发两条不同的stype新鲜事，暂且没有想到更好的方案
        if (type == CommentType.Blog) {
            try {
                // update feed
                FeedPublisher.publishFeedComment(entryOwnerId, oldest, latest, entryId, 602,
                        strategy.isReplaceUbb(), strategy.getSourceId(), count, 0,feedOwnerId,strategy);

            } catch (UGCCommentException e) {
                // something is wrong, remove the created comment
                commentLogic.remove(type, actorId, entryId, entryOwnerId, latest.getId(), strategy);
                logger.warn("Due to an exception during updating to feed, remove the just created comment");
                throw e;
            }
        }
    }

    @SuppressWarnings("serial")
    private void removeFeedAsyn(final CommentType type, final int actorId, final long entryId,
            final int entryOwnerId, final long commentId, final CommentStrategy strategy) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.REMOVE_FEED){ 

            @Override
            public String getSerializedMsg() {
                return String.format("%s %s %s %s %s %s ", type, actorId, entryId, entryOwnerId, commentId, strategy);
            }

            @Override
            protected Void doCall() throws Exception {
                removeFeed(type, actorId, entryId, entryOwnerId, commentId, strategy);
                return null;
            }
        });
        
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                removeFeed(type, actorId, entryId, entryOwnerId, commentId, strategy);
            }
        });*/
    }

    private void removeFeed(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) {

        Comment oldest = null;
        Comment latest = null;
        long count = 0;
        //1.如果调用方没传entryId的话，就从strategy中取一下
        if (entryId == 0) {
            entryId = strategy.getEntry().getId();
        }

        //2.get sourceId
        if (strategy.getSourceId() == 0) {
            //如果是ugc的，那么则用entryId
            switch (type) {
                case Blog:
                case Album:
                case Photo:
                case Status:
                case ShortVideo:
                    strategy.setSourceId(entryId);
                    break;
                default:
            }
        }
//        //关联评论的话,删除的新鲜事的sourceId应该是关联评论的主entryId,这个是在{@link:CommentLogicImpl #doRemove()}方法里设置的
//        if (strategy.getAsynSourceId() > 0) {
//            strategy.setSourceId(strategy.getAsynSourceId());
//            entryId = strategy.getAsynSourceId();
//        }

        // 3. get feedtype
        int feedtype = strategy.getFeedType();
        if (type == CommentType.Photo) {
            //photo feedtype比较多，这里对photo业务做了特殊的stype处理
            feedtype = getPhotoStype(actorId, strategy, entryOwnerId, entryId, latest, oldest);
        }
        if(feedtype == 0){
            feedtype = getDefaultFeedStype(type);
        }

        try {
            CommentLogic commentLogic = strategy.getCommentLogic();

            // 4.get total number
            count = commentLogic.getCount(type, actorId, entryId, entryOwnerId, strategy);

            if (count > 0) {
                // 5.get the oldest and latest comment
                latest = commentLogic.getLatestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);
                if (count > 1) {
                    oldest = commentLogic.getOldestCommentOfEntry(type, entryId, entryOwnerId,
                            strategy,actorId);
                }
            }

        } catch (UGCCommentException e) {
            e.printStackTrace();
        }
        
        //6.feed ownerId
        int feedOwnerId = strategy.getFeedOwnerId();

        /*
         * 7.真正的发送新鲜事
         */
        FeedPublisher.publishFeedComment(entryOwnerId, oldest, latest, entryId, feedtype,
                strategy.isReplaceUbb(), strategy.getSourceId(), count, commentId,feedOwnerId,strategy);

        //8.给page发送新鲜事
        if (this.needSendPageFeed(type, entryOwnerId, strategy)) {
            this.doPageSendFeedPublisher(type, oldest, latest, entryOwnerId, entryId, actorId,
                    true, (int) count);
        }

        //9.这段代码逻辑非常丑，因为blog增加评论的时候会发两条不同的stype新鲜事601,602，暂且没有想到更好的方案
        if (type == CommentType.Blog) {
            try {
                // update feed
                FeedPublisher.publishFeedComment(entryOwnerId, oldest, latest, entryId, 602,
                        strategy.isReplaceUbb(), strategy.getSourceId(), count, commentId,feedOwnerId,strategy);

            } catch (UGCCommentException e) {
                // something is wrong, remove the created comment
                logger.warn("Due to an exception during updating to feed, remove the just created comment");
                throw e;
            }
        } else if(type == CommentType.Photo){
            //del 708 feed
            if(count == 0 &&  EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_708Feed)){
                try{
                    XFeedRemover.getInstance().removeFeedBySource(entryId,708);
                } catch (Exception e){
                    logger.error("XFeedRemover removeFeedBySource708 error");
                }
              }
        }

    }
    
    private int getDefaultFeedStype(CommentType type) {
        int feedType = 0;
        switch(type){
            case Album:
                feedType = FeedUtil.FEED_TYPE_ALBUM;
                break;
            case Blog:
                feedType = FeedUtil.FEED_TYPE_BLOG;
                break;
            case ShortVideo:
            	feedType = FeedUtil.FEED_TYPE_SHORT_VIDEO;
            	break;
                default:
        }
        return feedType;
    }

}
