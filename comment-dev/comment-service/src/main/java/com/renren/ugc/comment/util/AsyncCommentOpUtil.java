package com.renren.ugc.comment.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.renren.ugc.comment.cache.CacheManager;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.service.DatabaseAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentTypeUtils;

public class AsyncCommentOpUtil {

    private static final Log logger = LogFactory.getLog(AsyncCommentOpUtil.class);

    public static void asyncCreateComments(final CommentType type, final List<Comment> comments,
            final CommentStrategy strategy,final int actorId) {
        if (comments == null) {
            return;
        }

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CREATE_COMMENT) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {

            	//1.写db
                Multiset<Entry> bag = HashMultiset.create();
                for (Comment comment : comments) {
                    try {
                        DatabaseAdapter.getInstance().create(CommentType.findByValue(comment.getType()), comment, strategy.getMetaData(),
                                strategy);
                        if (logger.isDebugEnabled()) {
                            logger.debug("asyncCreateComment comment " + comment.getId()
                                    + " success");
                        }

                        bag.add(comment.getEntry());

                    } catch (Exception e) {
                        logger.error("asyncCreateComment error|commentId:" + comment.getId()
                                + "|entryId:" + comment.getEntry().getId() + "|ownerId:"
                                + comment.getEntry().getOwnerId() + "|type:" + type.getValue(),e);
                    }
                }
                //int typeNum = type.getValue();
                //2.增加评论数目
                if (strategy.isCacheEnabled()) {
                    for (Multiset.Entry<Entry> countEntry : bag.entrySet()) {
                        long entryId = countEntry.getElement().getId();
                        int entryOwnerId = countEntry.getElement().getOwnerId();
                        try {
                        	CommentType entryType = CommentTypeUtils.valueOf(countEntry.getElement().getType());
	                        int typeNum = entryType.getValue();
	                        String urlmd5 =  UrlMd5Util.getUrlMd5(entryType, entryOwnerId, entryId);
                            if (strategy.needCount()) {
                                // increase 1 to the count of entry comment list cache
                                strategy.getCommentCacheService().incCount(entryId, entryOwnerId,
                                        typeNum, countEntry.getCount());
                                strategy.getCommentCacheService().incGlobalCount(urlmd5, countEntry.getCount());
                            }
                            strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId, typeNum);
                            strategy.getCommentCacheService().removeGlobalListCacheByEntry(urlmd5);
                            
                        } catch (Exception e) {
                            logger.error("asyncCreateComment updatecount error| entryId:" + entryId
                                    + "|ownerId:" + entryOwnerId + "|type:" + countEntry.getElement().getType());
                            continue;
                        }
                    }
                }
                
                //3.更新新鲜事，因为新鲜事依赖于2更新count后
                for (Comment comment : comments) {
                	AsyncCommentOpUtil.updateLinkedFeed(CommentType.findByValue(comment.getType()), actorId,comment.getEntry().getId() , 
                			comment.getEntry().getOwnerId(), 0, strategy);
                }
                
            
                return null;
            }
            
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                Multiset<Entry> bag = HashMultiset.create();
                for (Comment comment : comments) {
                    try {
                        DatabaseAdapter.getInstance().create(type, comment, strategy.getMetaData(),
                                strategy);
                        if (logger.isDebugEnabled()) {
                            logger.debug("asyncCreateComment comment " + comment.getId()
                                    + " success");
                        }

                        bag.add(comment.getEntry());

                    } catch (Exception e) {
                        logger.error("asyncCreateComment error|commentId:" + comment.getId()
                                + "|entryId:" + comment.getEntry().getId() + "|ownerId:"
                                + comment.getEntry().getOwnerId() + "|type:" + type.getValue());
                    }
                }
                //增加评论数目
                int typeNum = type.getValue();
                if (strategy.isCacheEnabled()) {
                    for (Multiset.Entry<Entry> countEntry : bag.entrySet()) {
                        long entryId = countEntry.getElement().getId();
                        int entryOwnerId = countEntry.getElement().getOwnerId();
                        String urlmd5 =  UrlMd5Util.getUrlMd5(type, entryOwnerId, entryId);
                        try {
                            if (strategy.needCount()) {
                                // increase 1 to the count of entry comment list cache
                                strategy.getCommentCacheService().incCount(entryId, entryOwnerId,
                                        typeNum, countEntry.getCount());
                                strategy.getCommentCacheService().incGlobalCount(urlmd5, countEntry.getCount());
                            }
                            strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId, type.getValue());
                            strategy.getCommentCacheService().removeGlobalListCacheByEntry(urlmd5);
                        } catch (Exception e) {
                            logger.error("asyncCreateComment updatecount error| entryId:" + entryId
                                    + "|ownerId:" + entryOwnerId + "|type:" + typeNum);
                            continue;
                        }
                    }
                }
            }
        });*/
    }

    public static void asyncCreateGlobalComment(final CommentType type, final Comment comment,
            final CommentStrategy strategy) {
        if (comment == null) {
            return;
        }

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CREATE_GLOBAL_COMMENT){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {

                try {

                    DatabaseAdapter.getInstance().createGlobal(type, comment, strategy.getMetaData().encode(),
                            strategy);
                    if (logger.isDebugEnabled()) {
                        logger.debug("asyncCreateGlobalComment comment " + comment.getId()
                                + " success");
                    }

                } catch (Exception e) {
                    logger.error("asyncCreateGlobalComment error|commentId:" + comment.getId()
                            + "|entryId:" + comment.getEntry().getId() + "|ownerId:"
                            + comment.getEntry().getOwnerId() + "|type:" + type.getValue(),e);
                }
                //TODO增加评论数目
                
                return null;
            }
        });
        
       /* AsynGlobalCommentJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                try {

                    DatabaseAdapter.getInstance().createGlobal(type, comment, strategy.getMetaData().encode(),
                            strategy);
                    if (logger.isDebugEnabled()) {
                        logger.debug("asyncCreateGlobalComment comment " + comment.getId()
                                + " success");
                    }

                } catch (Exception e) {
                    logger.error("asyncCreateGlobalComment error|commentId:" + comment.getId()
                            + "|entryId:" + comment.getEntry().getId() + "|ownerId:"
                            + comment.getEntry().getOwnerId() + "|type:" + type.getValue(),e);
                }
            }
            //TODO增加评论数目
            

        });*/
    }
    
    public static void asyncCreateGlobalCommentList(final CommentType type, final List<CommentPackage> packageList) {
        if (packageList == null) {
            return;
        }

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.CREATE_GLOBAL_COMMENT){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {
            	for(CommentPackage commentpackage : packageList ){
	                try {
	
	                    DatabaseAdapter.getInstance().createGlobal(type, commentpackage.getComment()
	                    		, commentpackage.getForCommentStrategy().getMetaData().encode(),
	                    		commentpackage.getForCommentStrategy());
	                    if (logger.isDebugEnabled()) {
	                        logger.debug("asyncCreateGlobalComment comment " + commentpackage.getComment().getId()
	                                + " success");
	                    }
	
	                } catch (Exception e) {
	                    logger.error("asyncCreateGlobalComment error|commentId:" + commentpackage.getComment().getId()
	                            + "|entryId:" + commentpackage.getComment().getEntry().getId() + "|ownerId:"
	                            + commentpackage.getComment().getEntry().getOwnerId() + "|type:" + type.getValue(),e);
	                }
	                //TODO增加评论数目
            	}
                
                return null;
            }
        });
        
      
    }

    public static void asyncRemoveComments(final CommentType type, final int actorId,
            final List<CommentLinkedInfo> infos, final CommentStrategy strategy) {
        if (infos == null) {
            return;
        }

        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.REMOVE_COMMENT){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {

                Multiset<Entry> bag = HashMultiset.create();
                Entry entry = strategy.getEntry();
                for (CommentLinkedInfo info : infos) {
                    try {
                        DatabaseAdapter.getInstance().remove(CommentType.findByValue(info.getCommentType()), actorId, info.getEntryId(),
                                info.getEntryOwnerId(), info.getLinkedCommentId(), strategy);
                        if (logger.isDebugEnabled()) {
                            logger.debug("asyncRemoveComment comment " + info.getLinkedCommentId()
                                    + " success");
                        }

                        bag.add(new Entry(info.getEntryId(),info.getEntryOwnerId(),CommentType.findByValue(info.getCommentType()).toString()));

                    } catch (Exception e) {
                        logger.error("asyncRemoveComment error|commentId:"
                                + info.getLinkedCommentId() + "|entryId:" + entry.getId()
                                + "|ownerId:" + entry.getOwnerId() + "|type:" + type);
                    }
                }
                //增加评论数目
                //int typeNum = type.getValue();
                if (strategy.isCacheEnabled()) {
                    for (Multiset.Entry<Entry> countEntry : bag.entrySet()) {
                        long entryId = countEntry.getElement().getId();
                        int entryOwnerId = countEntry.getElement().getOwnerId();
                        try {
                        	CommentType entryType = CommentTypeUtils.valueOf(countEntry.getElement().getType());
                        	int typeNum = entryType.getValue();
                            String urlmd5 =  UrlMd5Util.getUrlMd5(entryType, entryOwnerId, entryId);
                            if (strategy.needCount()) {
                                // increase 1 to the count of entry comment list cache
                                strategy.getCommentCacheService().incCount(entryId, entryOwnerId,
                                        typeNum, -countEntry.getCount());
                                strategy.getCommentCacheService().incGlobalCount(urlmd5, -countEntry.getCount());
                            }
                            strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId, typeNum);
                            strategy.getCommentCacheService().removeGlobalListCacheByEntry(urlmd5);
                        } catch (Exception e) {
                            logger.error("asyncRemoveComment updatecount error| entryId:" + entryId
                                    + "|ownerId:" + entryOwnerId + "|type:" + countEntry.getElement().getType());
                            continue;
                        }
                    }
                }

              //3.更新新鲜事，因为新鲜事依赖于2更新count后
                for (CommentLinkedInfo info : infos) {
                	AsyncCommentOpUtil.updateLinkedFeed(CommentType.findByValue(info.getCommentType()), actorId,info.getEntryId() , 
                			info.getEntryOwnerId(), info.getLinkedCommentId(), strategy);
                }
            
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                Multiset<Entry> bag = HashMultiset.create();
                Entry entry = strategy.getEntry();
                for (CommentLinkedInfo info : infos) {
                    try {
                        DatabaseAdapter.getInstance().remove(type, actorId, info.getEntryId(),
                                info.getEntryOwnerId(), info.getLinkedCommentId(), strategy);
                        if (logger.isDebugEnabled()) {
                            logger.debug("asyncRemoveComment comment " + info.getLinkedCommentId()
                                    + " success");
                        }

                        bag.add(new Entry(info.getEntryId(),info.getEntryOwnerId(),type.toString()));

                    } catch (Exception e) {
                        logger.error("asyncRemoveComment error|commentId:"
                                + info.getLinkedCommentId() + "|entryId:" + entry.getId()
                                + "|ownerId:" + entry.getOwnerId() + "|type:" + type);
                    }
                }
                //增加评论数目
                int typeNum = type.getValue();
                if (strategy.isCacheEnabled()) {
                    for (Multiset.Entry<Entry> countEntry : bag.entrySet()) {
                        long entryId = countEntry.getElement().getId();
                        int entryOwnerId = countEntry.getElement().getOwnerId();
                        String urlmd5 =  UrlMd5Util.getUrlMd5(type, entryOwnerId, entryId);
                        try {

                            if (strategy.needCount()) {
                                // increase 1 to the count of entry comment list cache
                                strategy.getCommentCacheService().incCount(entryId, entryOwnerId,
                                        typeNum, -countEntry.getCount());
                                strategy.getCommentCacheService().incGlobalCount(urlmd5, -countEntry.getCount());
                            }
                            strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId, type.getValue());
                            strategy.getCommentCacheService().removeGlobalListCacheByEntry(urlmd5);
                        } catch (Exception e) {
                            logger.error("asyncRemoveComment updatecount error| entryId:" + entryId
                                    + "|ownerId:" + entryOwnerId + "|type:" + typeNum);
                            continue;
                        }
                    }
                }
            }
        });*/
    }

    public static void asyncRemoveGlobalComment(final CommentType type, final int actorId,
            final String urlmd5, final long commentId, final CommentStrategy strategy) {

        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.REMOVE_GLOBAL_COMMENT){
            private static final long serialVersionUID = 1L;
            @Override 
            protected Void doCall() throws Exception {

                try {

                    DatabaseAdapter.getInstance().removeGlobal(type, actorId, urlmd5, commentId,
                            strategy);

                    if (logger.isDebugEnabled()) {
                        logger.debug("asyncRemoveGlobalComment comment " + commentId + " success");
                    }

                } catch (Exception e) {
                    logger.error("asyncRemoveGlobalComment error|commentId:" + commentId
                            + "|urlmd5:" + urlmd5 + "|type:" + type);
                }
                //TODO增加评论数目
                return null;
            }
        });
        
        
    	/*AsynGlobalCommentJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                try {

                    DatabaseAdapter.getInstance().removeGlobal(type, actorId, urlmd5, commentId,
                            strategy);

                    if (logger.isDebugEnabled()) {
                        logger.debug("asyncRemoveGlobalComment comment " + commentId + " success");
                    }

                } catch (Exception e) {
                    logger.error("asyncRemoveGlobalComment error|commentId:" + commentId
                            + "|urlmd5:" + urlmd5 + "|type:" + type);
                }
              //TODO增加评论数目
            }
        });*/
    }
    
    public static boolean syncRemoveUgcComment(final CommentType type, final int actorId,
        final long entryId, final int entryOwnerId, final long commentId, final CommentStrategy strategy) {

            try {
                CommentLogic commentlogic = strategy.getCommentLogic();
                commentlogic.remove(type, actorId, entryId, entryOwnerId, commentId, strategy);

                if (logger.isDebugEnabled()) {
                    logger.debug("asyncRemoveUgcComment comment " + commentId + " success");
                }

            } catch (Exception e) {
                logger.error("asyncRemoveUgcComment error|commentId:" + commentId
                        + "|entryId:" + entryId + "|entryOwnerId:" + entryOwnerId + "|type:" + type);
                return false;
            }
            
            return true;
    }

    /**
     * @param authors
     * @param tairCacheManager 
     * @param commentUserListKey 
     * @param commentUserList 
     */
    public static void asyncWriteAuthorsToTair(final List<Integer> authors, final CacheManager tairCacheManager, final String commentUserList, final String commentUserListKey) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.WRITE_AUTHORS_TO_CACHE) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {
                //TODO 这块要有批量插入接口 
                for (Integer author : authors) {
                    tairCacheManager.putToIntList(commentUserList, commentUserListKey, author);
                }
                return null;
            }
            
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                for (Integer author : authors) {
                    tairCacheManager.putToIntList(commentUserList, commentUserListKey, author);
                }
            }
        });*/
    }
    
    private static void updateLinkedFeed(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) {

        Comment oldest = null;
        Comment latest = null;
        long count = 0;
        
        //1.get sourceId
        long sourceId = entryId;

        // 3. get feedtype
        int feedType = getDefaultLinkedType(type);
        if(feedType == 0){
        	if (logger.isDebugEnabled()) {
                logger.debug("removeFeed feedType is 0|entryId:" + entryId
                        + "|entryOwnerId:" + entryOwnerId);
            }
        	return;
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
            logger.error("updateLinkedFeed error",e);
        }
        
        //6.feed ownerId
        int feedOwnerId = strategy.getFeedOwnerId();

        /*
         * 7.真正的发送新鲜事
         */
        FeedPublisher.publishFeedComment(entryOwnerId, oldest, latest, entryId, feedType,
                strategy.isReplaceUbb(), sourceId, count, commentId, feedOwnerId,strategy);
    }
    
    private static int getDefaultLinkedType(CommentType type) {
        int feedType = 0;
        switch(type){
            case Album:
                feedType = FeedUtil.FEED_TYPE_ALBUM;
                break;
            case Photo:
                feedType = FeedUtil.FEED_TYPE_PHOTO_REPLY;
                break;
                default:
        }
        return feedType;
    }
    
}
