package com.renren.ugc.comment.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.renren.ugc.comment.model.MonitorMessage;
import com.renren.ugc.comment.service.AsynPrintJobService;
import com.renren.ugc.comment.service.FriendRelationThreadPool;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.framework.mqclient.MqProducer;
import com.renren.ugc.framework.mqclient.model.MqTopics;

/**
 * portal to record the statistics
 * 
 * @author jiankuan.xing
 * 
 */
public class StatisticsHelper {

    private static final Log LOG = LogFactory.getLog("comment-statistics");
    
//    private static InfraKafkaProducer producerHandler = null;
//    static {
//        try {
//            producerHandler = new InfraKafkaProducer();
//        } catch (InfraKafkaException e2) {
//        	LOG.error("Start producerHandler error. exception=[" + e2 + "]");
//        }
//    }

    public static final int NANO_TO_MILLIS = 1000000;
    
    private static final Map<CommentType, StatisticsOp.Type> entryCacheMap = new HashMap<CommentType, StatisticsOp.Type>(6);
    static {
        entryCacheMap.put(CommentType.Blog, StatisticsOp.Type.GetBlogFromCache);
        entryCacheMap.put(CommentType.Status, StatisticsOp.Type.GetStatusFromCache);
        entryCacheMap.put(CommentType.Share, StatisticsOp.Type.GetShareFromCache);
        entryCacheMap.put(CommentType.Album, StatisticsOp.Type.GetAlbumFromCache);
        entryCacheMap.put(CommentType.Photo, StatisticsOp.Type.GetPhotoFromCache);
        entryCacheMap.put(CommentType.ShareAlbumPhoto, StatisticsOp.Type.GetShareAlbumPhotoFromCache);
    }

    private static final Map<CommentType, StatisticsOp.Type> entryMap = new HashMap<CommentType, StatisticsOp.Type>(6);
    static {
        entryMap.put(CommentType.Blog, StatisticsOp.Type.GetBlog);
        entryMap.put(CommentType.Status, StatisticsOp.Type.GetStatus);
        entryMap.put(CommentType.Share, StatisticsOp.Type.GetShare);
        entryMap.put(CommentType.Album, StatisticsOp.Type.GetAlbum);
        entryMap.put(CommentType.Photo, StatisticsOp.Type.GetPhoto);
        entryMap.put(CommentType.ShareAlbumPhoto, StatisticsOp.Type.GetShareAlbumPhoto);
    }

    private static final BlockingQueue<StatisticsOp> queue = new LinkedBlockingQueue<StatisticsOp>();

    private static class StatisticsThread extends Thread {

        private long prevOutputTime = System.currentTimeMillis();

        private static final long OUTPUT_INTERVAL = 60 * 1000; // 1 minute  

        private Map<StatisticsOp.Type, Record> recordMap = new HashMap<StatisticsOp.Type, Record>();

        public StatisticsThread() {
            // main interfaces
            recordMap.put(StatisticsOp.Type.CreateComment, new Record(RecordType.COMMENT_API,
                    "create-comment"));
            recordMap.put(StatisticsOp.Type.RemoveComment, new Record(RecordType.COMMENT_API,
                    "remove-comment"));
            recordMap.put(StatisticsOp.Type.RecoverComment, new Record(RecordType.COMMENT_API,
                    "recover-comment"));
            recordMap.put(StatisticsOp.Type.GetCommentList, new Record(RecordType.COMMENT_API,
                    "get-comment-list"));
            recordMap.put(StatisticsOp.Type.GetComment, new Record(RecordType.COMMENT_API,
                    "get-comment"));
            recordMap.put(StatisticsOp.Type.GetMultiComments, new Record(RecordType.COMMENT_API,
                    "get-multi-comments"));
            recordMap.put(StatisticsOp.Type.GetGlobalCommentList, new Record(
                    RecordType.COMMENT_API,
                    "get-global-comment-list"));
            recordMap.put(StatisticsOp.Type.GetCommentCount, new Record(RecordType.COMMENT_API,
                    "get-count"));
            recordMap.put(StatisticsOp.Type.GetCommentCountBatch, new Record(
                    RecordType.COMMENT_API,
                    "get-count-batch"));
            recordMap.put(StatisticsOp.Type.GetHeadAndTailCommentList, new Record(
                    RecordType.COMMENT_API,
                    "get-head-and-tail-comment-list"));
            recordMap.put(StatisticsOp.Type.CreateLinkedComment, new Record(RecordType.COMMENT_API,
                    "create-linked-comment"));
            recordMap.put(StatisticsOp.Type.CreateVoiceComment, new Record(RecordType.COMMENT_API,
                    "create-voice-comment"));
            recordMap.put(StatisticsOp.Type.IncreaseVoicePlayCount, new Record(
                    RecordType.COMMENT_API,
                    "increase-voice-play-count"));
            recordMap.put(StatisticsOp.Type.GetFriendsCommentList, new Record(
                    RecordType.COMMENT_API,
                    "get-friends-comment-list"));
            recordMap.put(StatisticsOp.Type.GetCommentsForFeed, new Record(
                    RecordType.COMMENT_API,
                    "get-comments-forfeed"));
            recordMap.put(StatisticsOp.Type.GetCommentsForFeedCount, new Record(
                    RecordType.COMMENT_API,
                    "get-comments-forfeed-count"));
            recordMap.put(StatisticsOp.Type.CreateCommentByList, new Record(
                    RecordType.COMMENT_API,
                    "create-comment-by-list"));

            // external api
            recordMap.put(StatisticsOp.Type.GetEncrpytedShortUrl, new Record(
                    RecordType.EXTERNAL_API,
                    "get-encrypted-short-url"));
            recordMap.put(StatisticsOp.Type.GetShortUrlOriginalContent, new Record(
                    RecordType.EXTERNAL_API,
                    "get-short-url-content-batch"));
            recordMap.put(StatisticsOp.Type.GetShortUrlOriginalContentBatch, new Record(
                    RecordType.EXTERNAL_API,
                    "get-short-url-original-content-batch"));
            recordMap.put(StatisticsOp.Type.ReplaceUbb, new Record(RecordType.EXTERNAL_API,
                    "replace-ubb"));
            recordMap.put(StatisticsOp.Type.FormatAt, new Record(RecordType.EXTERNAL_API,
                    "at-format"));
            recordMap.put(StatisticsOp.Type.FormatAtHerf, new Record(RecordType.EXTERNAL_API,
                    "at-format-herf"));
            recordMap.put(StatisticsOp.Type.AtFilter, new Record(RecordType.EXTERNAL_API,
                    "at-filter"));
            recordMap.put(StatisticsOp.Type.GetLikeDetailBatch, new Record(
                    RecordType.EXTERNAL_API, "get-like-detail-batch"));
            recordMap.put(StatisticsOp.Type.GetBlogFromCache, new Record(
                    RecordType.EXTERNAL_API, "get-entry-from-cache"));
            recordMap.put(StatisticsOp.Type.ReplaceLink, new Record(RecordType.EXTERNAL_API,
                    "replace-link"));
            recordMap.put(StatisticsOp.Type.SendToFeed, new Record(RecordType.EXTERNAL_API,
                    "send-to-feed"));
            recordMap.put(StatisticsOp.Type.PostIsGagged, new Record(RecordType.EXTERNAL_API,
                    "post-is-gagged"));

            // get entry
            recordMap.put(StatisticsOp.Type.GetShare, new Record(RecordType.GET_ENTRY,
                    "get-share"));
            recordMap.put(StatisticsOp.Type.GetBlog, new Record(RecordType.GET_ENTRY, "get-blog"));
            recordMap.put(StatisticsOp.Type.GetStatus, new Record(RecordType.GET_ENTRY,
                    "get-status"));
            recordMap.put(StatisticsOp.Type.GetAlbum, new Record(RecordType.GET_ENTRY,
                    "get-album"));
            recordMap.put(StatisticsOp.Type.GetPhoto, new Record(RecordType.GET_ENTRY,
                    "get-photo"));
            recordMap.put(StatisticsOp.Type.GetShareAlbumPhoto, new Record(RecordType.GET_ENTRY,
                    "get-share-album-photo"));
            recordMap.put(StatisticsOp.Type.GetVideo, new Record(RecordType.GET_ENTRY,
                    "get-video"));
            recordMap.put(StatisticsOp.Type.GetPosts, new Record(RecordType.GET_ENTRY,
                    "get-posts"));
            recordMap.put(StatisticsOp.Type.GetAlbumPosts, new Record(RecordType.GET_ENTRY,
                    "get-albumposts"));

            // get entry from cache (This cache should have been deprecated)
            recordMap.put(StatisticsOp.Type.GetShareFromCache, new Record(
                    RecordType.GET_ENTRY_CACHE, "get-share-From-cache"));
            recordMap.put(StatisticsOp.Type.GetStatusFromCache, new Record(
                    RecordType.GET_ENTRY_CACHE, "get-status-From-cache"));
            recordMap.put(StatisticsOp.Type.GetAlbumFromCache, new Record(
                    RecordType.GET_ENTRY_CACHE, "get-album-From-cache"));
            recordMap.put(StatisticsOp.Type.GetPhotoFromCache, new Record(
                    RecordType.GET_ENTRY_CACHE, "get-photo-From-cache"));
            recordMap.put(StatisticsOp.Type.GetShareAlbumPhotoFromCache, new Record(
                    RecordType.GET_ENTRY_CACHE, "get-share-album-photo-From-cache"));

            //internal cache
            recordMap.put(StatisticsOp.Type.GetFirstPageFromCache, new Record(
                    RecordType.COMMENT_CACHE,
                    "get-firstPage-from-cache"));
            recordMap.put(StatisticsOp.Type.GetGlobalFromCache, new Record(
                    RecordType.COMMENT_CACHE,
                    "get-global-comment-from-cache"));
            recordMap.put(StatisticsOp.Type.GetCountBatchFromCache, new Record(
                    RecordType.COMMENT_CACHE, "get-count-batch-from-cache"));
            recordMap.put(StatisticsOp.Type.GetGlobalCountFromCache, new Record(
                    RecordType.COMMENT_CACHE, "get-global-count-batch-from-cache"));
            recordMap.put(StatisticsOp.Type.GetCountFromCache, new Record(RecordType.COMMENT_CACHE,
                    "get-count-from-cache"));
            recordMap.put(StatisticsOp.Type.GetCommentedFriendListFromCache, new Record(
                    RecordType.COMMENT_CACHE, "get-friendList-From-cache"));
            recordMap.put(StatisticsOp.Type.GetCommentedUserListFromCache, new Record(
                    RecordType.COMMENT_CACHE, "get-commentedUserList-from-cache"));
            recordMap.put(StatisticsOp.Type.GetLikeDetailBatchFromCache, new Record(
                    RecordType.COMMENT_CACHE, "get-like-detail-batch-From-cache"));

            // internal db access
            recordMap.put(StatisticsOp.Type.GetFirstPageFromDB, new Record(RecordType.COMMENT_DB,
                    "get-firstPage-from-DB"));
            recordMap.put(StatisticsOp.Type.CreateCommentDB, new Record(RecordType.COMMENT_DB,
                    "create-comment-DB"));
            recordMap.put(StatisticsOp.Type.WriteOldDB, new Record(RecordType.COMMENT_DB,
                    "write-old-DB"));
            
            recordMap.put(StatisticsOp.Type.GetCommentsForFeed, new Record(RecordType.COMMENT_REDIS_CACHE,
                    "get-comments-for-feed"));
            
            recordMap.put(StatisticsOp.Type.GetFriendCommentsForFeed, new Record(RecordType.COMMENT_REDIS_CACHE,
                    "get-friend-comments-for-feed"));
            
            recordMap.put(StatisticsOp.Type.GetFriendCommentsForFeedCount, new Record(RecordType.COMMENT_REDIS_CACHE,
                    "get-friend-comments-for-feed-count"));
            
            recordMap.put(StatisticsOp.Type.GetCommentsForFeedCount, new Record(RecordType.COMMENT_REDIS_CACHE,
                    "get-comments-for-feed-count"));
            
            recordMap.put(StatisticsOp.Type.GetRelatedComments, new Record(RecordType.INTERNAL_API,
                    "get-related-comment"));
            
            recordMap.put(StatisticsOp.Type.GetCommentListWithFilter, new Record(RecordType.COMMENT_API,
                    "get-commentList-with-filter"));

        }

        @Override
        public void run() {
            long loopCount = 0;

            StatisticsOp op = null;
            while (true) {
                try {
                    op = queue.poll(1, TimeUnit.SECONDS);
                    if (op == null) {
                        continue;
                    }
                    loopCount++;
                    StatisticsOp.Type type = op.getType();
                    long time = op.getTime();
                    boolean success = op.isSuccess();
                    boolean miss = op.isMiss();

                    Record r = recordMap.get(type);
                    if (r == null) {
                        LOG.error("Unknown statistics type: " + type);
                        continue;
                    }
                    
                    if (r.getType().isForCache()) {
                        if (miss) { // cache always success
                            if (r.getType() == RecordType.COMMENT_REDIS_CACHE) {
                                r.invokeMissForCache(time);
                            } else {
                                r.invokeMiss(time);
                            }
                        } else {
                            if (r.getType() == RecordType.COMMENT_REDIS_CACHE) {
                                r.invokeDoneForCache(time);
                            } else {
                                r.invokeDone(time);
                            }
                        }
                    } else {
                        if (success) {
                            r.invokeDone(time);
                        } else {
                            r.invokeException(time);
                        }
                    }
                    if (loopCount % 100 == 0) {
                        // try once per 100 times
                        tryOutputStatisticsData();
                    }

                } catch (InterruptedException e) {
                    LOG.error("Statistics is interrupted", e);
                }

            }
        }

        private void tryOutputStatisticsData() {
            long now = System.currentTimeMillis();

            if (now - prevOutputTime < OUTPUT_INTERVAL) {
                return;
            }
            prevOutputTime = now;

            final DeltaRecord[] drs = new DeltaRecord[recordMap.size()];

            int i = 0;
            for (Record r : recordMap.values()) {
                if (r.hasNewData()) { // just output the new data
                    continue;
                }
                drs[i] = r.getDelta();
                i++;
            }

            AsynPrintJobService.asynRun(new Runnable() {

                @Override
                public void run() {
                    Multimap<RecordType, DeltaRecord> rMap = HashMultimap.create();

                    try {
                        for (DeltaRecord dr : drs) {

                            if (null == dr || dr.getCount() <= 0 || dr.getMaxTime() <= 0) {
                                continue;
                            }
                            rMap.put(dr.getType(), dr);
                        }

                        StringBuilder sb = new StringBuilder();
                        for (RecordType type : rMap.keySet()) {
                            sb.append(type.toString());
                            sb.append("\n");
                            for (DeltaRecord dr : rMap.get(type)) {
                                sb.append("\t");
                                sb.append(dr.toString());
                                sb.append("\n");
                            }
                            sb.append("\n"); // output a new line for each record type
                        }

                        LOG.info("\n" + sb.toString());
                        sendKafka(rMap);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }

				private void sendKafka(Multimap<RecordType, DeltaRecord> rMap) {
					if (isSwitchOn()) {
						//1.send map info
	                	for (RecordType type : rMap.keySet()) { //loop1
	                        for (DeltaRecord dr : rMap.get(type)) {   //loop2                 
	                            //send 
	                           sendToKafka(new MonitorMessage(dr).toString());
	                        } //end loop2
	                	} // end loop1
					}
					//2.send thread info
					//sendToKafka(new MonitorMessage(MonitorMessage.THREAD_QUEUE_HEAD,"asynJob",AsynJobService.getQueueSize()).toString());
					//sendToKafka(new MonitorMessage(MonitorMessage.THREAD_QUEUE_HEAD,"asynGlobalJob",AsynGlobalCommentJobService.getQueueSize()).toString());
					
					
					sendToKafka(new MonitorMessage(MonitorMessage.THREAD_QUEUE_HEAD,"friendRelationThreadPool",FriendRelationThreadPool.getQueueSize()).toString());
					sendToKafka(new MonitorMessage(MonitorMessage.THREAD_QUEUE_HEAD,"asynPrintJob",AsynPrintJobService.getQueueSize()).toString());
				}
            });

        }
    }

    // get entry from cache
    public static void invokeGetEntryFromCache(CommentType commentType, long time, boolean miss) {
        StatisticsOp.Type sType = entryCacheMap.get(commentType);
        if (sType == null) {
            return;
        }
        queue.offer(new StatisticsOp(sType, time, miss, true));
    }
    
    // we assume all the cache always success
    public static void invokeGetShareFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShareFromCache, time, miss, true));
    }
    
    public static void invokeGetBlogFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetBlogFromCache, time, miss, true));
    }
    
    public static void invokeGetAlbumFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetAlbumFromCache, time, miss, true));
    }
    
    public static void invokeGetPhotoFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetPhotoFromCache, time, miss, true));
    }
    
    public static void invokeGetStatusFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetStatusFromCache, time, miss, true));
    }
    
    public static void invokeGetShareAlbumPhotoFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShareAlbumPhotoFromCache, time, miss, true));
    }
    
    // get entry
    public static void invokeGetEntry(CommentType commentType, long time, boolean success) {
        StatisticsOp.Type sType = entryMap.get(commentType);
        if (sType == null) {
            return;
        }
        queue.offer(new StatisticsOp(sType, time,  success));
    }
    
    public static void invokeGetShare(long time, boolean miss, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShare, time, miss, success));
    }
    
    public static void invokeGetBlog(long time, boolean miss, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetBlog, time, miss, success));
    }
    
    public static void invokeGetAlbum(long time,boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetAlbum, time, success));
    }
    
    public static void invokeGetPhoto(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetPhoto, time, success));
    }
    
    public static void invokeGetStatus(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetStatus, time, success));
    }
    
    public static void invokeGetShareAlbumPhoto(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShareAlbumPhoto, time, success));
    }
    
    public static void invokeCreateComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.CreateComment, time, success));
    }
    
    public static void invokeCreateCommentByList(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.CreateCommentByList, time, success));
    }

    public static void invokeGetCommentList(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentList, time, success));
    }

    public static void invokeGetComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetComment, time, success));
    }

    public static void invokeGetMultiComments(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetMultiComments, time, success));
    }

    public static void invokeGetCommentCount(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentCount, time, success));
    }

    public static void invokeRemoveComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.RemoveComment, time, success));
    }

    public static void invokeRecoverComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.RecoverComment, time, success));
    }

    public static void invokeGetCommentCountBatch(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentCountBatch, time, success));
    }

    public static void invokeCreateLinkedComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.CreateLinkedComment, time, success));
    }

    public static void invokeGetHeadAndTailCommentList(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetHeadAndTailCommentList, time, success));
    }

    public static void invokeCreateVoiceComment(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.CreateVoiceComment, time, success));
    }

    public static void invokeIncreaseVoicePlayCount(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.IncreaseVoicePlayCount, time, success));
    }

    public static void invokeGetEncrpyptedShortUrl(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetEncrpytedShortUrl, time, success));
    }

    public static void invokeGetGlobalCommentList(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetGlobalCommentList, time, success));
    }

    public static void invokeGetShortUrlOriginalContent(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShortUrlOriginalContent, time, success));
    }

    public static void invokeGetShortUrlOriginalContentBatch(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShortUrlOriginalContentBatch, time,
                success));
    }

    public static void invokeReplaceUbb(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.ReplaceUbb, time, success));
    }

    public static void invokeFormatAt(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.FormatAt, time, success));
    }

    public static void invokeFormatAtWithHerf(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.FormatAtHerf, time, success));
    }

    public static void invokeGetBlog(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetBlog, time, success));
    }

    public static void invokeGetShare(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetShare, time, success));
    }

    public static void invokeGetLikeDetailBatch(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetLikeDetailBatch, time, success));
    }

    public static void invokeGetFriendsCommentList(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetFriendsCommentList, time, success));
    }

    public static void invokeGetFirstPageFromDB(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetFirstPageFromDB, time, success));
    }

    public static void invokeReplaceLink(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.ReplaceLink, time, success));
    }

    public static void invokeCreateCommentDB(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.CreateCommentDB, time, success));
    }

    public static void invokeWriteOldDB(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.WriteOldDB, time, success));
    }

    public static void invokeSendToFeed(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.SendToFeed, time, success));
    }
    
    // internal cache
    public static void invokeGetLikeDetailBatchFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetLikeDetailBatchFromCache, time, miss,
                true));
    }

    public static void invokeGetEntryFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetBlogFromCache, time, miss, true));
    }

    public static void invokeGetFirstPageFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetFirstPageFromCache, time, miss, true));
    }

    public static void invokeGetGlobalFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetGlobalFromCache, time, miss, true));
    }
    
    public static void invokeGetCountBatchFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCountBatchFromCache, time, miss, true));
    }

    public static void invokeGetGlobalCountFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetGlobalCountFromCache, time, miss, true));
    }

    public static void invokeGetCountFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCountFromCache, time, miss, true));
    }
    
    public static void invokeGetCommentedFriendListFromCache(long time, boolean miss, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentedFriendListFromCache, time, miss,
                success));
    }
    
    public static void invokeGetCommentedUserListFromCache(long time, boolean miss) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentedUserListFromCache, time, miss,
                true));
    }
    
    public static void invokeGetCommentsForFeed(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentsForFeed, time, success));
    }
    
    public static void invokeGetCommentsForFeedCount(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentsForFeedCount, time, success));
    }
    
    public static void invokeGetFriendCommentsForFeed(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetFriendCommentsForFeed, time, success));
    }
    
    public static void invokeGetFriendCommentsForFeedCount(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetFriendCommentsForFeedCount, time, success));
    }
    
    public static void invokeAtFilter(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.AtFilter, time, success));
    }
    
    public static void invokeGetVideo(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetVideo, time, success));
    }
    
    public static void invokeGetRelatedComments(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetRelatedComments, time, success));
    }
    
    public static void invokeGetCommentListWithFilter(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetCommentListWithFilter, time, success));
    }
    
    public static void invokeGetPosts(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetPosts, time, success));
    }
    
    public static void invokeIsGagged(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.PostIsGagged, time, success));
    }
    
    public static void invokeGetAlbumPosts(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetAlbumPosts, time, success));
    }
    
    public static void invokePostIsAdmin(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.PostIsAdmin, time, success));
    }
    
    public static void invokeGetTopPosts(long time, boolean success) {
        queue.offer(new StatisticsOp(StatisticsOp.Type.GetTopPosts, time, success));
    }


    static {
        // start statistics thread
        Thread t = new StatisticsThread();
        t.setName("comment-statistics");
        t.setDaemon(true);
        t.start();
    }
    
    private static void sendToKafka(String message){
    	try{
//            producerHandler.sendMessage("comment-test-monitor",
//					null, message);
    		MqProducer.send(MqTopics.COMMENT_TEST_MONITOR, null, message);
            } catch (Exception e) {
            	LOG.error(e.getMessage(), e);
            }
    }
    
    /**
     * 看推送到评论中心kafka开关是否打开
     * 
     * 
     * @return  默认返回true
     */
	public static boolean isSwitchOn() {
		try {
			String switchOnOff = (String) TairCacheManagerImpl
					.getInstance().get(
							TairCacheManagerImpl.SWITCH_ON_OFF,
							"kafka");
			return "on".equals(switchOnOff);
		} catch (Exception e) {
			return true;
		}
	}
    
}
