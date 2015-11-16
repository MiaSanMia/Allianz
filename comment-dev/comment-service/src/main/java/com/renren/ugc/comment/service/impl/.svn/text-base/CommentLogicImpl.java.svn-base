package com.renren.ugc.comment.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.renren.app.share.IShareConstants;
import com.renren.doingubb.tool.DoingUbbReplace;
import com.renren.ugc.comment.cache.CacheManager;
import com.renren.ugc.comment.cache.CommentCacheService;
import com.renren.ugc.comment.cache.CommentFirstPageCacheService;
import com.renren.ugc.comment.cache.FeedRedisCacheService;
import com.renren.ugc.comment.cache.FeedRedisCacheService.RedisKeyGen;
import com.renren.ugc.comment.event.CommentEventManager;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.feed.FeedCommentUtil;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.model.FeedCommentInfo;
import com.renren.ugc.comment.model.FeedCommentResult;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.service.CommentUrlStorageService;
import com.renren.ugc.comment.service.DatabaseAdapter;
import com.renren.ugc.comment.service.FriendRelationThreadPool;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.renren.ugc.comment.util.AsyncCommentOpUtil;
import com.renren.ugc.comment.util.AtUtil;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.CommentPropertyPlaceholderConfigurer;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.IDGenerator;
import com.renren.ugc.comment.util.LinkedInfoUtil;
import com.renren.ugc.comment.util.MetadataResolveUtil;
import com.renren.ugc.comment.util.NotifyAlsoUtil;
import com.renren.ugc.comment.util.NotifyUtil;
import com.renren.ugc.comment.util.ShortUrlUtil;
import com.renren.ugc.comment.util.TimeUtil;
import com.renren.ugc.comment.util.VipUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.antispam.filter.Replace;
import com.xiaonei.platform.core.head.HeadUrlUtil;
import com.xiaonei.platform.core.model.WUserCache;
import com.xiaonei.platform.core.opt.ice.WUserCacheAdapter;
import com.xiaonei.xce.buddyadapter.buddyrelationcache.BuddyRelationCacheAdapter;
import com.xiaonei.xce.domain.buddy.BuddyRelation;

/**
 * The main comment logic implementation that's responsible of
 * creating/removing/getting list/updating/recovering comments.
 * 
 * @author jiankuan.xing
 * 
 */
@Service
public class CommentLogicImpl implements CommentLogic, InitializingBean {

    private Logger logger = Logger.getLogger(this.getClass());

    private static final Integer MAX_USER_LIST_NUM = 150000;
    
    private static final Integer MAX_THREAD_NUM = 50;
    
    private static String kafka_switch = "off";

    @Autowired
    @Qualifier("comment.service.mysql.url")
    private CommentUrlStorageService cuss;

    @Autowired
    private DatabaseAdapter dataAdapter;

    @Autowired
    private CacheManager tairCacheManager;

    /**
     * if the content's length is larger than this threshold, it must be
     * added to the comment_ext table
     */
    private static final int EXT_CONTENT_LEN_THRESHOLD = 4090;

    private static CommentLogicImpl instance;

    public static CommentLogic getInstance() {
        return instance;
    }

    @Override
    public Comment create(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {

        //1. create data
        com.renren.ugc.comment.model.Comment c = doCreate(commentType, actorId, entryId,
                entryOwnerId, comment, strategy);

        if (strategy.isCacheEnabled()) {
            updateCacheForCommentCreation(commentType, actorId, entryId, entryOwnerId, strategy);
            updateCommentUserForShare(commentType, actorId, entryId, strategy);
            //updateCommentUserForShareAsyn(commentType, actorId, entryId, strategy);
        }

        //2. 如果是分享相片的类型，要替换share实体的数据
        if (commentType == CommentType.ShareAlbumPhoto) {
            replaceShareEntryInfo(strategy);
        }

        //3. get some other comments information
        this.getExtraOneCommentInfo(commentType, c, strategy);

        //4. set entry to CommentStrategy
        if (strategy.getEntry() == null && c.getEntry() != null) {
            strategy.setEntry(c.getEntry());
        }

        sendEvent(strategy, c);

        return c;
    }

	private void sendEvent(CommentStrategy strategy,
			com.renren.ugc.comment.model.Comment c) {
//		String switchOnOff = (String) tairCacheManager.get(TairCacheManagerImpl.SWITCH_ON_OFF, "kafka");
//        if (StringUtils.isNotBlank(switchOnOff)) {
//        	kafka_switch = switchOnOff;
//        }
//        if ("on".equals(kafka_switch)) {
        	CommentEventManager.getInstance().sendEvent(CommentEventManager.CommentEvent.CREATE, c, strategy);
//        }
        
        Map<String, String> params = strategy.getParams();
        if(params == null){
        	params = new HashMap<String,String>();
        	strategy.setParams(params);
        }
        params.put("kafka_switch", kafka_switch);
	}

    /**
     * 在kv数据库中存储评论该实体的user列表
     * 
     * @param commentType
     * @param actorId
     * @param entryId
     * @param strategy
     *
     * 状态的key比较特殊,生成规则参见
     * @see #getCommentedUserListForStatus() 方法
     */

    private void updateCommentUserForShare(CommentType commentType, int actorId, long entryId, CommentStrategy strategy) {
        String commentUserListKey = "";
        if(NotifyAlsoUtil.isSupportAlsoNotice(commentType)){
            commentUserListKey = entryId + "-" + commentType;
        } else {
            commentUserListKey = strategy.getUrlMd5();
        }

        List<Integer> authors = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.COMMENT_USER_LIST,
            commentUserListKey, 0, MAX_USER_LIST_NUM);
        if (authors != null) {
            tairCacheManager.putToIntList(TairCacheManagerImpl.COMMENT_USER_LIST, commentUserListKey, actorId);
        }
    }

    private void updateCacheForCommentCreation(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) {

        CommentCacheService ccs = strategy.getCommentCacheService();

        if (strategy.needCount()) {
            // increase 1 to the count of entry comment list cache
            ccs.incCount(entryId, entryOwnerId, commentType.getValue(), 1);
            ccs.incGlobalCount(strategy.getUrlMd5(), 1);
        }
        ccs.removeCacheByEntry(entryId, entryOwnerId, commentType.getValue());
        ccs.removeGlobalListCacheByEntry(strategy.getUrlMd5());
    }

    private Comment doCreate(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {

        comment.setType(commentType.getValue());
        Comment c = comment.clone();

        //1.插入db之前的一些准备工作
        //1.1 生成评论id
        initializeCommentId(commentType, c, strategy);

        c.setAuthorId(actorId);

        fillEntryToComment(commentType, entryId, entryOwnerId, c);

        c.setCreatedTime(System.currentTimeMillis());

        //1.2 生成flag字段
        this.doSetCommentFlag(c,strategy);

//        //1.3  判断是否被linked，是的话需要预先生成linked评论Id列表
//        List<Comment> linkedComments = new ArrayList<Comment>();
//        if (c.isLinked()) {
//            linkedComments = LinkedInfoUtil.getCommentFromLinkedInfos(commentType, strategy, c);
//        }

        //1.3 生成metadata字段
        Metadata metadata = new Metadata();
        //1.4 针对关联评论做特殊处理
        List<Comment> linkedComments = null;
        if (c.isLinked()) {
        	//1.4.1 CommentLinkedInfo--->Comment
        	linkedComments = LinkedInfoUtil.getCommentFromLinkedInfos(commentType, strategy.getCommentLinkedInfos(), c);
        	//1.4.2 Comment--->Map
            Map<String, String> linkedMaps = LinkedInfoUtil.getLinkedMaps(c, linkedComments);
            if (linkedMaps != null) {
                metadata.putAll(linkedMaps);
            }
        }
        if(commentType == CommentType.Album && c.getToCommentId() > 0){
            //当回复的评论也是关联评论的时候,要变成双写,metadata需要重新生成,shi一样的逻辑>_<
            //1.4.3 CommentLinkedInfo--->Comment
            linkedComments = doReplyWithLinked(c, entryId, entryOwnerId, commentType, strategy);
            //1.4.4 Comment--->Map
            Map<String, String> linkedMaps = LinkedInfoUtil.getLinkedMaps(c, linkedComments);
            if (linkedMaps != null) {
                metadata.putAll(linkedMaps);
            }
        }
        metadata.putAll(strategy.getParams());
        c.setMetadata(metadata);

        //2. 插入db
        try {
            //css.create(commentType, c, metadata.encode());
            dataAdapter.create(commentType, c, metadata, strategy);

            if (logger.isDebugEnabled()) {
                logger.debug("comment with id " + c.getId() + " is saved");
            }

        } catch (Exception e) {
            logger.error("an error occurs during put the new comment into storage", e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        //3. 异步写入LinkedCommentList
        strategy.setMetaData(metadata);
        if (CollectionUtils.isNotEmpty(linkedComments)) {
            AsyncCommentOpUtil.asyncCreateComments(commentType, linkedComments, strategy, actorId);
        }

        return c;
    }

    private void fillEntryToComment(CommentType commentType, long entryId, int entryOwnerId,
            com.renren.ugc.comment.model.Comment comment) {
        com.renren.ugc.comment.model.Entry entry = comment.getEntry();
        if (entry == null) {
            entry = new com.renren.ugc.comment.model.Entry();
        }
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);
        comment.setEntry(entry);
    }

    private void initializeCommentId(CommentType commentType,
            com.renren.ugc.comment.model.Comment comment, CommentStrategy strategy) {
        long commentId = comment.getId();
        if (commentId <= 0) {
            try {
                long externalId = strategy.getExternalId();
                if (externalId > 0) {
                    commentId = IDGenerator.nextCommentId(commentType, externalId);
                } else {
                    commentId = IDGenerator.nextCommentId(commentType);
                }
            } catch (Exception e) {
                logger.error("an error occurs during generating comment id", e);
                throw new UGCCommentException(CommentError.STORAGE_ERROR,
                        CommentError.STORAGE_ERROR_MSG, e);
            }

            comment.setId(commentId);
        }
    }

    @Override
    public Comment getLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy,int actorId) throws UGCCommentException {

        com.renren.ugc.comment.model.Comment c = null;
        // disable cache
        //        if (strategy.isCacheEnabled()) {
        //            c = strategy.getCommentCacheService().getLatestEntryCommentCache(entryId, entryOwnerId,
        //                    type.getValue());
        //            if (c != null) {
        //                if (com.renren.ugc.comment.model.Comment.isEmptyComment(c)) {
        //                    return null;
        //                } else {
        //                    return c.toInterfaceComment();
        //                }
        //            }
        //        }

        c = doGetLatestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);

        // disable cache
        //        if (strategy.isCacheEnabled()) {
        //            if (c == null) {
        //                c = com.renren.ugc.comment.model.Comment.EMPTY_COMMENT;
        //            }
        //            strategy.getCommentCacheService().setLatestEntryCommentCache(entryId, entryOwnerId,
        //                    type.getValue(), c);
        //        }

        //filter whisper
        if (c == null || c.getWhipserToId() > 0) {
            return null;
        }

        // get some other comments information
        this.getExtraOneCommentInfo(type, c, strategy);

        return c;
    }

    private com.renren.ugc.comment.model.Comment doGetLatestCommentOfEntry(CommentType type,
            long entryId, int entryOwnerId, CommentStrategy strategy,int actorId) throws UGCCommentException {
        com.renren.ugc.comment.model.Comment c = null;
        try {
            //c = css.getLatestCommentOfEntry(type, entryId, entryOwnerId);
            c = dataAdapter.getLatestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);

            if (c == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has no comment");
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has the latest comment "
                            + c.getId());
                    fillAuthorInfoToComment(c, strategy);
                }
            }
        } catch (Exception e) {
            String msg = "an error occurs during looking up the latest comment of entry " + entryId;
            logger.error(msg, e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        return c;
    }

    @Override
    public Comment getOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy,int actorId) throws UGCCommentException {
        com.renren.ugc.comment.model.Comment c = null;
        // disable cache
        //        if (strategy.isCacheEnabled()) {
        //            c = strategy.getCommentCacheService().getOldestEntryCommentCache(entryId, entryOwnerId,
        //                    type.getValue());
        //            if (c != null) {
        //                if (com.renren.ugc.comment.model.Comment.isEmptyComment(c)) {
        //                    return null;
        //                } else {
        //                    return c.toInterfaceComment();
        //                }
        //            }
        //        }

        c = doGetOldestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);

        // disable cache
        //        if (strategy.isCacheEnabled()) {
        //            if (c == null) {
        //                c = com.renren.ugc.comment.model.Comment.EMPTY_COMMENT;
        //            }
        //            strategy.getCommentCacheService().setOldestEntryCommentCache(entryId, entryOwnerId,
        //                    type.getValue(), c);
        //        }

        //filter whisper
        if (c.getWhipserToId() > 0) {
            return null;
        }

        // get some other comments information
        this.getExtraOneCommentInfo(type, c, strategy);

        return c;
    }

    private com.renren.ugc.comment.model.Comment doGetOldestCommentOfEntry(CommentType type,
            long entryId, int entryOwnerId, CommentStrategy strategy,int actorId) throws UGCCommentException {

        com.renren.ugc.comment.model.Comment c = null;

        try {
            //c = css.getOldestCommentOfEntry(type, entryId, entryOwnerId);
            c = dataAdapter.getOldestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);

            if (c == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has no comment");
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has the oldest comment "
                            + c.getId());
                    fillAuthorInfoToComment(c, strategy);
                }
            }

        } catch (Exception e) {
            String msg = "an error occurs during looking up the latest comment of entry " + entryId;
            logger.error(msg, e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        return c;
    }

    @Override
    public CommentListResult getList(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
    	
        long borderId = strategy.getQueryBorderID();
        int limit = strategy.getQueryLimit();
        QueryOrder order = strategy.getQueryOrder();
        int offset = strategy.getQueryOffset();

        List<com.renren.ugc.comment.model.Comment> cList = null;
        if (borderId == 0 && offset > 0) {
            // for compatibility reason, support offset + limit query.
            cList = doGetListWithOffset(commentType, actorId, entryId, entryOwnerId, strategy);
        } else {

            boolean isFirstPage = (borderId == 0);
            int tempLimit = CommentFirstPageCacheService.PAGE_COUNT;

            //1.get comments from db or cache
            if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page
               
                // check cache
                cList = strategy.getCommentCacheService().getCommentListByEntryCache(entryId,
                        entryOwnerId, commentType.getValue(), getQueryOrderString(order),
                        tempLimit, borderId);

            }
            
            if (cList != null) {
                cList = getSubCommentList(cList, limit);
            } else {
                // cache not found, set temp limit for DB query
                strategy.setQueryLimit(tempLimit);
            }
               

            //2.there is no data in cache, so get comments from database , and set comments into cache
            if (cList == null) {
                cList = doGetList(commentType, actorId, entryId, entryOwnerId, strategy);

                if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page
                    strategy.getCommentCacheService().setCommentListByEntryCache(entryId,
                            entryOwnerId, commentType.getValue(), getQueryOrderString(order),
                            tempLimit, borderId, cList);
                }
                // only return the "limit" size of comments
                cList = getSubCommentList(cList, limit);
            }
            
            //3.if limit > cachecount,need to get other datas from db
            if(limit > CommentFirstPageCacheService.PAGE_COUNT){
                //get count
                long count  = getCount(commentType, actorId, entryId, entryOwnerId, strategy);
                if(count > CommentFirstPageCacheService.PAGE_COUNT)
                    getOtherCommentsFromDBAndFillList(cList, limit - CommentFirstPageCacheService.PAGE_COUNT, commentType, actorId, entryId, entryOwnerId, strategy);
            }
        }

        //3.judge hasMore ,hasPre and trim list
        boolean hasMore = cList.size() > strategy.getOriginalQueryLimit() ? true : false;
        if(hasMore){
        	//or sublist?
        	for(int index = strategy.getOriginalQueryLimit() ; index < cList.size(); index++){
        		cList.remove(index);
        	}
        }
        boolean hasPre = false;
       if(strategy.isNeedHasPre()){
    	   hasPre = this.judgeHasPre(order, commentType, entryId, entryOwnerId, strategy, borderId,actorId);
       }
        
        //4. ok, comments are ready,filter whisper comment
        cList = filterWhisperComment(cList, actorId, strategy);

        //5. get some other comments information
        getExtraCommentInfos(commentType, cList, strategy);
        
        //6. get simpelcommentinfo,
        //getRelatedCommentInfo(commentType, actorId, entryId, entryOwnerId, cList, strategy);

        //7 return commentlistresult
        CommentListResult commentListResult = new CommentListResult(cList,hasMore,hasPre);
        
        return commentListResult;
    }

    public CommentListResult getGlobalCommentList(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, String urlmd5, CommentStrategy strategy) throws UGCCommentException {
    	
        long borderId = strategy.getQueryBorderID();
        int offset = strategy.getQueryOffset();
        int limit = strategy.getQueryLimit();
        QueryOrder order = strategy.getQueryOrder();

        // in this method, urlmd5 is must. entryId and entryOwnerId should be converted to urlmd5 in the
        // interceptors
        if (StringUtils.isEmpty(urlmd5)) {
            urlmd5 = strategy.getUrlMd5();
        }
        if (StringUtils.isEmpty(urlmd5)) {
            throw new UGCCommentException(CommentError.COMMENT_PARAMS_INVALID,
                    "urlmd5 is must to get global comment list");
        }

        List<com.renren.ugc.comment.model.Comment> cList = null;
        if (borderId == 0 && offset > 0) {
            cList = doGetGlobalListWithOffset(commentType, actorId, urlmd5, strategy);
        } else {
            
            boolean isFirstPage = (borderId == 0);
            int tempLimit = CommentFirstPageCacheService.PAGE_COUNT;

            //1.get comments from db or cache
            if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page

                // check cache
                cList = strategy.getCommentCacheService().getGlobalCommentListByEntryCache(urlmd5, getQueryOrderString(order),
                        tempLimit, borderId);
             
                if (cList != null) {
                    cList = getSubCommentList(cList, limit);
                } else {
                    // cache not found, set temp limit for DB query
                    strategy.setQueryLimit(tempLimit);
                }   
            }
            
            //2.there is no data in cache, so get comments from database , and set comments into cache
            if (cList == null) {
                cList = doGetGlobalListWithBorderID(commentType, actorId, urlmd5, strategy);

                if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page
                    strategy.getCommentCacheService().setGlobalListByEntryCache(urlmd5, getQueryOrderString(order), tempLimit, cList);
                    cList = getSubCommentList(cList, limit);
                }
            }

        }
        
      //3.judge hasMore and trim list
        boolean hasMore = cList.size() > strategy.getOriginalQueryLimit() ? true : false;
        if(hasMore){
        	for(int index = strategy.getOriginalQueryLimit() ; index < cList.size(); index++){
        		cList.remove(index);
        	}
        }

        //4. ok, comments are ready,filter whisper comment
        cList = filterWhisperComment(cList, actorId, strategy);

        //5. get some other comments information
        getExtraCommentInfos(commentType, cList, strategy);

        //6 return commentlist
        CommentListResult commentListResult = new CommentListResult(cList,hasMore);
        
        return commentListResult;
    }

    /**
     * 根据urlmd5和borderId，从全局分享评论中获取评论列表
     * 
     * @param commentType 评论类型
     * @param actorId 查询者ID
     * @param urlmd5 share全局唯一键
     * @param strategy
     * @return
     */
    private List<com.renren.ugc.comment.model.Comment> doGetGlobalListWithBorderID(
            CommentType commentType, int actorId, String urlmd5, CommentStrategy strategy) {
        long borderId = strategy.getQueryBorderID();
        int limit = strategy.getQueryLimit();
        QueryOrder order = strategy.getQueryOrder();

        List<com.renren.ugc.comment.model.Comment> list = null;

        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(
                        "get comment list, type=%s, actorId=%d, urlmd5=%s, limit=%d, order=%s",
                        commentType.toString(), actorId, urlmd5, limit, order.toString()));
            }
            list = cuss.getListByEntry(commentType, urlmd5, borderId, limit, order);

            if (CollectionUtils.isEmpty(list)) {
                logger.debug("nothing is found");
            }
        } catch (Exception e) {
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        return list;
    }

    /**
     * 根据urlmd5和offset，从全局分享评论中获取评论列表
     * 
     * @param commentType 评论类型
     * @param actorId 查询者ID
     * @param urlmd5 share全局唯一键
     * @param strategy
     * @return
     */
    private List<com.renren.ugc.comment.model.Comment> doGetGlobalListWithOffset(
            CommentType commentType, int actorId, String urlmd5, CommentStrategy strategy) {

        int limit = strategy.getQueryLimit();
        int offset = strategy.getQueryOffset();
        QueryOrder order = strategy.getQueryOrder();

        List<com.renren.ugc.comment.model.Comment> list = null;
        try {

            if (logger.isDebugEnabled()) {
                logger.debug(String
                        .format("get global comment list by offset, type=%s, urlmd5=%s, offset=%d, limit=%d, order=%s",
                                commentType.toString(), urlmd5, offset, limit, order.toString()));
            }
            list = cuss.getListByEntry(commentType, urlmd5, offset, limit, order);

            if (CollectionUtils.isEmpty(list)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("nothing is found");
                }
            }

        } catch (Exception e) {
            logger.error("an error occurs during looking up global comment list with offset");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }
        return list;
    }

    private List<com.renren.ugc.comment.model.Comment> doGetListWithOffset(CommentType commentType,
            int actorId, long entryId, int entryOwnerId, CommentStrategy strategy) {
        int limit = strategy.getQueryLimit();
        int offset = strategy.getQueryOffset();
        QueryOrder order = strategy.getQueryOrder();

        List<com.renren.ugc.comment.model.Comment> list = null;
       // try {

            if (logger.isDebugEnabled()) {
                logger.debug(String
                        .format("get comment list by offset, type=%s, entryId=%d, entryOwnerId=%d, offset=%d, limit=%d, order=%s",
                                commentType.toString(), entryId, entryOwnerId, offset, limit,
                                order.toString()));
            }

            try {

                //根据不同的类型去不同的表里取数据
                list = dataAdapter.getListByEntryWithOffsetDispatch(commentType, entryId,
                        entryOwnerId, offset, limit, order, strategy,actorId);

            } catch (Exception e) {
                logger.error("an error occurs during looking up comment list with offset");
                throw new UGCCommentException(CommentError.STORAGE_ERROR, CommentError.SUCCESS_MSG,
                        e);
            }

            if (list.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("nothing is found");
                }
            }
//        } catch (Exception e) {
//            logger.error("an error occurs during looking up comment list with offset");
//            throw new UGCCommentException(CommentError.STORAGE_ERROR,
//                    CommentError.STORAGE_ERROR_MSG, e);
//        }
        return list;
    }

    private List<com.renren.ugc.comment.model.Comment> doGetList(CommentType commentType,
            int actorId, long entryId, int entryOwnerId, CommentStrategy strategy)
            throws UGCCommentException {

        long borderId = strategy.getQueryBorderID();
        int limit = strategy.getQueryLimit();
        QueryOrder order = strategy.getQueryOrder();

        List<com.renren.ugc.comment.model.Comment> list = null;
        //List<com.renren.ugc.comment.model.Comment> commentList = new ArrayList<com.renren.ugc.comment.model.Comment>();
        //try {
        if (logger.isDebugEnabled()) {
            logger.debug(String
                    .format("get comment list, type=%s, entryId=%d, entryOwnerId=%d, borderCommentId=%d, limit=%d, order=%s",
                            commentType.toString(), entryId, entryOwnerId, borderId, limit,
                            order.toString()));
        }

        try {
            //list = css.getListByEntry(commentType, entryId, entryOwnerId, borderId, limit, order);
            list = dataAdapter.getListByEntry(commentType, entryId, entryOwnerId, borderId, limit,
                    order, strategy,actorId);
        } catch (Exception e) {
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (list.isEmpty() && logger.isDebugEnabled()) {
            logger.debug("nothing is found");
        }

        return list;
    }

    /**
     * fill the author's information to comment objects in the list
     * 
     */
    private void fillAuthorInfoToComments(List<com.renren.ugc.comment.model.Comment> commentList,
            CommentStrategy strategy) {
        if (CollectionUtils.isEmpty(commentList)) {
            return;
        }

        Set<Integer> authorIds = new HashSet<Integer>(commentList.size());
        for (com.renren.ugc.comment.model.Comment comment : commentList) {
            authorIds.add(comment.getAuthorId());
        }

        // get authors information
        Map<Integer, WUserCache> authorId2UserMap = WUserCacheAdapter.getInstance()
                .getUserCacheMap(new ArrayList<Integer>(authorIds));

        // update user information
        boolean returnFullHeadUrl = strategy.isReturnFullHeadUrl();
        for (com.renren.ugc.comment.model.Comment comment : commentList) {
            WUserCache author = authorId2UserMap.get(comment.getAuthorId());
            if (author != null) {
                // set name
                comment.setAuthorName(author.getName());

                // set keep use
                comment.setAuthorKeepUse(author.isKeepUse());

                // set head
                String headUrl = author.getTinyUrl();

                // for some biz, they need full head url
                if (returnFullHeadUrl) {
                    headUrl = HeadUrlUtil.getHeadFullUrl(headUrl);
                }
                comment.setAuthorHead(headUrl);
            }
        }
    }

    /**
     * retrieve the first max <code>limit</code> comment from the original
     * <code>commentList</code>. If the size of commentList is smaller than
     * limit, return the input <code>commentList</code>.
     */
    // disable cache
    private List<com.renren.ugc.comment.model.Comment> getSubCommentList(
            List<com.renren.ugc.comment.model.Comment> commentList, int limit) {
        if (commentList.size() <= limit) {
            return commentList;
        }

        return commentList.subList(0, limit);
    }

    private void fillAuthorInfoToComment(com.renren.ugc.comment.model.Comment comment,
            CommentStrategy strategy) {
        if (comment == null) {
            return;
        }

        List<com.renren.ugc.comment.model.Comment> commentList = new ArrayList<com.renren.ugc.comment.model.Comment>();
        commentList.add(comment);

        fillAuthorInfoToComments(commentList, strategy);
    }

    @Override
    public long getCount(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        long count = 0;
        if (strategy.isCacheEnabled()) {

            count = strategy.getCommentCacheService().getCount(entryId, entryOwnerId,
                    commentType.getValue());

            if (count != CommentCacheService.COUNT_CACHE_NOT_EXIST) {
                return count;
            }
        }

        try {
            //count = css.getCommentCountByEntry(commentType, entryId, entryOwnerId);
            count = dataAdapter
                    .getCommentCountByEntry(commentType, entryId, entryOwnerId, strategy,actorId);
        } catch (Exception e) {
            logger.error("an error occurs during get comment total count");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (strategy.isCacheEnabled()) {
            strategy.getCommentCacheService().setCount(entryId, entryOwnerId,
                    commentType.getValue(), count);
        }

        return count;
    }

    @Override
    public boolean remove(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        if (entryId == 0 && strategy != null && strategy.getEntry() != null) {
            entryId = strategy.getEntry().getId();
        }
        
        //判断数量,2014-03-11,采用安全中心的频率检测
//        String key = "cq_rmlimit_"+actorId+"_"+commentType.getValue();
//        long count = FeedRedisCacheService.getInstance().incLimitCount(key, 1);
//        if(count <= 1){
//        	//set
//        	FeedRedisCacheService.getInstance().expire1Sec(key);
//        } else if (count >= 5){
//        	logger.error("you remove too often |actorId:"+actorId+"|entryId:"+entryId+"|type:"+commentType.getValue());
//        	throw new UGCCommentException(CommentError.COMMENT_TOO_FAST,
//                    CommentError.COMMENT_TOO_FAST_MSG);
//        }

        boolean removed = doRemove(actorId, entryId, entryOwnerId, commentId, commentType, strategy);
        if (strategy.isCacheEnabled() && removed) {
            updateCacheForCommentRemoval(commentType, entryId, entryOwnerId, strategy,false);
        }

        return removed;
    }

    private boolean doRemove(int actorId, long entryId, int entryOwnerId, long commentId,
            CommentType commentType, CommentStrategy strategy) {

        com.renren.ugc.comment.model.Comment comment = null;

        //1.db删除
        try {
            //comment = css.remove(actorId, entryId, entryOwnerId, commentId, commentType);
            comment = dataAdapter.remove(commentType, actorId, entryId, entryOwnerId, commentId,
                    strategy);
        } catch (UGCCommentException e) {
        	logger.error("remove a comment error|actorId:"+actorId+"|entryId:"+entryId+"|entryOwnerId:"+entryOwnerId+"|type:"+commentType.getValue()+"|cid:"+commentId);
            throw new UGCCommentException(e.getMessage());
        } catch (Exception e) {
            logger.error("an internal error occurs during removing a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (comment == null) {
            return false;
        }

        //2. 如果是linkedComment，异步删除评论
        if (comment.getFlag().isUseLinked()) {
            this.doLinkedCommentRemoval(comment, commentType, actorId, strategy);
        }

        //3.删除评论的时候可能没有传入entryId，但是entryId在删除新鲜事的时候用到
        if (entryId <= 0) {
            strategy.setEntry(comment.getEntry());
        }
        
        //4.删除评论的时候，需要把删除的comment赋值给strategy
        strategy.setComment(comment);
        return true;
    }

    private void updateCacheForCommentRemoval(CommentType commentType, long entryId,
            int entryOwnerId, CommentStrategy strategy,boolean removeAll) {
         //删除评论的时候可能没有传入entryId
        if(entryId == 0 && strategy.getEntry() != null){
            entryId = strategy.getEntry().getId();
        }
        if (strategy.needCount()) {
        	if(removeAll){
        		strategy.getCommentCacheService().setCount(entryId, entryOwnerId, commentType.getValue(), 0);
        		strategy.getCommentCacheService().setGlobalCount(strategy.getUrlMd5(), 0);
        	} else {
	            strategy.getCommentCacheService().incCount(entryId, entryOwnerId,
	                    commentType.getValue(), -1);
	            strategy.getCommentCacheService().incGlobalCount(strategy.getUrlMd5(), -1);
        	}
        }
	   	 strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId,
	             commentType.getValue());
	     strategy.getCommentCacheService().removeGlobalListCacheByEntry(strategy.getUrlMd5());
	     strategy.getCommentCacheService().removeFriendListCacheByEntry(strategy.getUrlMd5(), entryOwnerId);
    }

    @Override
    public boolean removeAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {

        boolean removed = doRemoveAll(actorId, entryId, entryOwnerId, commentType, strategy);
        if (strategy.isCacheEnabled() && removed) {
        	this.updateCacheForCommentRemoval(commentType, entryId, entryOwnerId, strategy, true);
        }

        return removed;
    }

    @Override
    public boolean recover(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        boolean recovered = doRecover(commentType, entryId, entryOwnerId, commentId);

        if (recovered && strategy.isCacheEnabled()) {
            updateCacheForCommentCreation(commentType, actorId, entryId, entryOwnerId, strategy);
        }

        return recovered;
    }

    private boolean doRecover(CommentType commentType, long entryId, int entryOwnerId,
            long commentId) {
        try {
            // return css.recover(entryId, entryOwnerId, commentId);
            return dataAdapter.recover(entryId, entryOwnerId, commentId, commentType);
        } catch (Exception e) {
            logger.error("an internal error occurs during recovering a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }
    }

    @Override
    public boolean recoverAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        throw new NotImplementedException();
    }

    @Override
    public Comment get(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        try {
            //com.renren.ugc.comment.model.Comment c = css.get(entryId, entryOwnerId, commentId);
            com.renren.ugc.comment.model.Comment c = dataAdapter.get(entryId, entryOwnerId,
                    commentId, commentType);
            if (c == null) {
                return null;
            } else {
                c.setOriginalContent(c.getContent()); // BIZFRAMEWORK-148 ensure original content is not null
                fillAuthorInfoToComment(c, strategy);
               //get metadata Info
                if(strategy.isNeedMetadataResolve()){
                	List<Comment> list = new ArrayList<Comment>();
                	list.add(c);
                	MetadataResolveUtil.resolveCommentFields(list);
                }
                
                if(entryId <= 0){
                    strategy.setEntry(c.getEntry());
                }
                return c;
            }
        } catch (Exception e) {
            logger.error("an internal error occurs during getting a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }
    }

    @Override
    public boolean update(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy)
            throws UGCCommentException {
        boolean updated = doUpdate(commentType, actorId, entryId, entryOwnerId, commentId,
                newComment, strategy);

        if (strategy.isCacheEnabled() && updated) {
            strategy.getCommentCacheService().removeCacheByEntry(entryId, entryOwnerId,
                    commentType.getValue());
        }
        return updated;
    }

    private boolean doUpdate(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy) {

        boolean updated = false;
        try {
            //            updated = css.updateContent(commentType, entryId, entryOwnerId, commentId,
            //                    newComment.getContent());
            updated = dataAdapter.updateContent(commentType, entryId, entryOwnerId, commentId,
                    newComment.getContent());
        } catch (Exception e) {
            logger.error("an internal error occurs during updating a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (updated) {

            Comment comment = get(commentType, actorId, entryId, entryOwnerId, commentId, strategy);

            // make sure "newComment" contains the comment data
            newComment.setId(comment.getId());
            newComment.setAuthorId(comment.getAuthorId());
            newComment.setCreatedTime(comment.getCreatedTime());
            newComment.setEntry(comment.getEntry());
            newComment.setToUserId(comment.getToUserId());
            newComment.setWhipserToId(comment.getWhipserToId());
        }

        return updated;
    }

    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment, CommentStrategy strategy) {
        //        int playCount = css.increaseVoiceCommentPlayCount(type, entryId, entryOwnerId, commentId,
        //                increment);
        int playCount = dataAdapter.increaseVoiceCommentPlayCount(type, entryId, entryOwnerId,
                commentId, increment);

        return playCount;
    }

    /**
     * judge whether should filter out the current comment by the whisper
     * policy
     * 
     * @param whisperToId the whipster target user id
     * @param actorId the comment retriever's id
     * @param authorId the comment's author user id
     * @param entryOwnerId the comment's entry owner id
     */
    private boolean shouldFilterOutDueToWhisper(int whisperToId, int actorId, int authorId,
            int entryOwnerId) {
        if (whisperToId > 0) {
            if (actorId == 0) {
                // when the retriever is not set (0), filter out all whispers
                return true;
            }
            if (whisperToId != actorId && authorId != actorId && entryOwnerId != actorId) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format(
                            "whisper filted out: actor=%d, whisperId=%d, authorId=%d ", actorId,
                            whisperToId, authorId));
                }
                return true;
            } else {
                return false;
            }
        } else {
            // not whisper
            return false;
        }
    }

    /**
     * convert query order to a query string "ASC" or "DESC". default is
     * "ASC"
     */
    private String getQueryOrderString(QueryOrder order) {
        if (order == null) {
            return QueryOrder.ASC.toString();

        }
        return order.toString();
    }

    private List<com.renren.ugc.comment.model.Comment> filterWhisperComment(
            List<com.renren.ugc.comment.model.Comment> list, int actorId, CommentStrategy strategy) {

        if (list == null || list.size() == 0) {
            return new ArrayList<com.renren.ugc.comment.model.Comment>();
        }

        List<com.renren.ugc.comment.model.Comment> results = new ArrayList<com.renren.ugc.comment.model.Comment>(
                list.size());

        for (com.renren.ugc.comment.model.Comment c : list) {
            // whisper filter out
            if (shouldFilterOutDueToWhisper(c.getWhipserToId(), actorId, c.getAuthorId(), c
                    .getEntry().getOwnerId())) {
                continue;
            }

            results.add(c);
        }

        return results;

    }

    /**
     * 
     * @param type comment type
     * @param strategy commentStrategy
     * 
     *        getExtraCommentInfo,eg: "@" info,author head url...
     */
    private void getExtraCommentInfos(CommentType type,
            List<com.renren.ugc.comment.model.Comment> list, CommentStrategy strategy) {

        if (list == null || list.isEmpty()) {
            return;
        }

        fillAuthorInfoToComments(list, strategy);

        for (com.renren.ugc.comment.model.Comment c : list) {

            c.setOriginalContent(c.getContent());

            // ubb replacement
            if (strategy.isReplaceUbb() || strategy.isReplaceUbbLarge()) {
                long start = System.nanoTime();
                String replacedContent = DoingUbbReplace.getInstance().replaceUBB(
                        c.getContent(),
                        strategy.getReplaceUbbLarge() != null ? strategy.getReplaceUbbLarge()
                                : false);
                long end = System.nanoTime();
                StatisticsHelper.invokeReplaceUbb((end - start) / StatisticsHelper.NANO_TO_MILLIS,
                        true);
                c.setContent(replacedContent);
            }

            // link replacement
            c.setContent(Replace.replaceLink(c.getContent()));

            //@ replacement
            if (strategy.isReplaceAt()) {
                c.setContent(AtUtil.getInstance().getWithHrefAt(c.getContent()));
            }
        }

        //short url replacement
        if (strategy.isReplaceShortUrl()) {
            ShortUrlUtil.getInstance().getBatchOriginalContent(list);
        }

        //get vip user's icon url
        VipUtil.getInstance().setVipIconUrlToComment(list);
        
        //get metadata Info
        if(strategy.isNeedMetadataResolve()){
        	MetadataResolveUtil.resolveCommentFields(list);
        }

    }

    /**
     * 替换strategy中share实体的信息，用于之后发送消息
     * 
     * @param strategy
     */
    private void replaceShareEntryInfo(CommentStrategy strategy) {
        com.renren.ugc.comment.model.Entry entry = strategy.getEntry();
        entry.getEntryProps().put(EntryConfig.ENTRY_SHARE_TYPE,
                Integer.toString(IShareConstants.URL_TYPE_PHOTO));
        entry.getEntryProps().put(EntryConfig.ENTRY_SHARE_VIRTUAL_ID, Long.toString(entry.getId()));
        entry.setId(Long.valueOf(strategy.getShareRealId()));
    }

    private void getExtraOneCommentInfo(CommentType type, com.renren.ugc.comment.model.Comment c,
            CommentStrategy strategy) {

        List<com.renren.ugc.comment.model.Comment> list = new ArrayList<com.renren.ugc.comment.model.Comment>();
        list.add(c);
        this.getExtraCommentInfos(type, list, strategy);
    }

    /**
     * pass the singleton bean initialized by spring to instance so
     * non-spring code can access it
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CommentLogicImpl.instance = this;
    }

    @Override
    public Map<Long, Integer> getCountBatch(CommentType commentType, int actorId,
            List<Long> entryIds, int entryOwnerId, CommentStrategy strategy)
            throws UGCCommentException {

        Map<Long, Integer> results = new HashMap<Long, Integer>();

        if (entryIds == null || entryIds.size() == 0) {
            return results;
        }

        if (strategy.isCacheEnabled()) {
            
            results = strategy.getCommentCacheService().getCountBatch(entryIds, entryOwnerId,
                    commentType.getValue());

            if (results != null && results.size() == entryIds.size()) {   
                return results;
            }
        }

        //1. find entryIds which are not in cache
        List<Long> notInCacheIds = new ArrayList<Long>();
        for (Long entryId : entryIds) {
            if (results == null || results.get(entryId) == null) {
                notInCacheIds.add(entryId);
            }
        }

        // 2.according to notInCacheIds,found data in database
        Map<Long, Integer> notInCacheResults = null;
        try {
            //            notInCacheResults = css.getCommentCountByEntryBatch(commentType, notInCacheIds,
            //                    entryOwnerId);
            notInCacheResults = dataAdapter.getCommentCountByEntryBatch(commentType, notInCacheIds,
                    entryOwnerId);
        } catch (Exception e) {
            logger.error("an error occurs during get comment total count batch");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        //3.可能从db中还是取不到,那么就设为0
        if (notInCacheResults == null) {
            notInCacheResults = new HashMap<Long, Integer>();
        }
        for (Long notInCache : notInCacheIds) {
            if (notInCacheResults.get(notInCache) == null) {
                notInCacheResults.put(notInCache, 0);
            }
        }

        //4. set into cache
        if (strategy.isCacheEnabled()) {
            for (java.util.Map.Entry<Long, Integer> entry : notInCacheResults.entrySet()) {
                strategy.getCommentCacheService().setCount(entry.getKey(), entryOwnerId,
                        commentType.getValue(), entry.getValue());
            }
        }

        //add all
        if (results == null) {
            results = new HashMap<Long, Integer>();
        }
        results.putAll(notInCacheResults);
        return results;
    }

    private boolean doRemoveAll(int actorId, long entryId, int entryOwnerId, CommentType commentType, CommentStrategy strategy) {
        boolean removed = false;
        try {
            //removed = css.removeAll(actorId, entryId, entryOwnerId, commentType);
            removed = dataAdapter.removeAll(actorId, entryId, entryOwnerId, commentType, strategy);

        } catch (UGCCommentException e) {
            throw new UGCCommentException(e.getMessage());
        } catch (Exception e) {
            logger.error("an internal error occurs during removing a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }
        return removed;
    }

    /**
     * @param comment
     * 
     *        设置comment Flag位
     */
    private void doSetCommentFlag(com.renren.ugc.comment.model.Comment comment,CommentStrategy strategy) {
        Flag flag = new Flag();
        String content = comment.getContent();

        // check it should be added to ext comment table?
        if (content.length() > EXT_CONTENT_LEN_THRESHOLD) {
            flag.setUseExtent();
            content = "";
            if (logger.isDebugEnabled()) {
                logger.debug("the content is too long, insert it to comment_ext table");
            }
        }

        // check it should be added to voice comment table?
        if (comment.getVoiceInfo() != null) {
            flag.setUseVoice();
            if (logger.isDebugEnabled()) {
                logger.debug("the comment has voice info");
            }
        }

        // check it has linkedInfo
        if (comment.isLinked()) {
            flag.setUseLinked();
            if (logger.isDebugEnabled()) {
                logger.debug("the comment has linked info");
            }
        }

        //check photo tag info
        if(strategy.isPhotoTagComment()) {
            flag.setUsePhotoTag(true);
            if (logger.isDebugEnabled()) {
                logger.debug("the comment has photo tag info");
            }
        }

        comment.setFlag(flag);
    }

    /**
     * 获取对实体评论的好友列表
     * 
     * @param actorId
     * @param entryId 如果includeGlobalComment为true，entryId中存储的是url_md5值,
     *        否则是universe_comment的entry_id值
     * @param entryOwnerId
     * @param type
     * @return
     */
    @Override
    public List<Integer> getFriendsList(int actorId, long entryId, int entryOwnerId,
            String urlmd5, CommentType type) {
        
        String commentFriendListKey = null;
        if (NotifyAlsoUtil.isSupportAlsoNotice(type)) {
            commentFriendListKey = entryId + "-" + type + "-" + actorId;
        } else {
            commentFriendListKey = urlmd5 + "-" + actorId;
        }
        
        //1.从KV中获取好友
        List<Integer> friendIds = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.SHARE_COMMENT_FRIEND_LIST,
                commentFriendListKey, 0, MAX_USER_LIST_NUM);;
       
        if (CollectionUtils.isEmpty(friendIds)) {
        	//2.KV中没有的话，需要分2步
        	//2.1 获取authors
            List<Integer> authors = null;
            if (NotifyAlsoUtil.isSupportAlsoNotice(type)){
            	authors = getAuthorListFromCommentTable(entryId, entryOwnerId, type, actorId);
            }else {
                authors = getAuthorListFromUrlCommentTable(entryId, entryOwnerId, urlmd5, type, actorId);
            }
            if (CollectionUtils.isEmpty(authors)) {
                return Collections.emptyList();
            }
        	//2.2 计算好友关系，然后异步存到KV中
            friendIds = getUserFriendIdList(actorId, authors);
            AsyncCommentOpUtil.asyncWriteAuthorsToTair(friendIds, tairCacheManager, TairCacheManagerImpl.SHARE_COMMENT_FRIEND_LIST, commentFriendListKey);
        }
       
        return friendIds;
        
    }

    /**
     * 获取对该实体评论的实体拥有者好友列表
     * 
     * @param entryId
     * @param entryOwnerId
     * @param authors
     * @param type
     * @return
     */
    /*private List<Integer> getCommentedFriendListByEntryOwnerId(long entryId,
        int entryOwnerId, List<Integer> authors, CommentType type) {
        String commentFriendListKey = entryId + "-" + type + "-" + entryOwnerId;
        //获取评论用户列表
        List<Integer> friendIds = null;
        
        friendIds = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.COMMENT_FRIEND_LIST_BY_AUTHOR,
                commentFriendListKey, 0, MAX_USER_LIST_NUM);
       
        if (CollectionUtils.isEmpty(friendIds)) {
            friendIds = getUserFriendIdList(entryOwnerId, authors);

            for (Integer friendId : friendIds) {

                tairCacheManager.putToIntList(TairCacheManagerImpl.COMMENT_FRIEND_LIST_BY_AUTHOR, commentFriendListKey,
                        friendId);
            }
        }
       

        return friendIds;
    }*/

    /**
     * 获取authors中user的好友ID列表
     * 
     * @param userId
     * @param authors
     * @return
     */
    private List<Integer> getUserFriendIdList(int userId,
        List<Integer> authors) {
        List<Integer> friendIds = new CopyOnWriteArrayList<Integer>();
        //获取好友列表
        int authorsSize = authors.size();
        
        if (authorsSize > 100) {
            CountDownLatch totalThreadLaunch = null;
            if (authorsSize > MAX_THREAD_NUM * 100) {
                // 控制线程数
                totalThreadLaunch = new CountDownLatch(MAX_THREAD_NUM);
                // 启动固定长度的线程池
                //ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_NUM);
                
                for (int i = 0; i < MAX_THREAD_NUM; i++) {
                    FriendRelationThreadPool.asynRun(new FriendRelationThread(totalThreadLaunch, authors, friendIds, i, authorsSize, MAX_THREAD_NUM, userId));
                }
                try {
                    logger.info("Waiting All Thread Finish!");
                    // 等待其他线程执行完
                    //totalThreadLaunch.await();
                    //TODO看这个效率而定
                    totalThreadLaunch.await(1000, TimeUnit.MILLISECONDS);
                    logger.info("All Thread Finish!");
                } catch (Exception e) {
                    logger.error("CountDownLatch thread is interrupted.", e);
                }
                /* finally {
                    executorService.shutdown();
                }*/
            } else {
                int num = authorsSize % 100 == 0 ? (authorsSize / 100) : (authorsSize / 100 + 1);
                // 控制线程数
                totalThreadLaunch = new CountDownLatch(num);
                // 启动固定长度的线程池
                //ExecutorService executorService = Executors.newFixedThreadPool(num);
                
                for (int i = 0; i < num; i++) {
                    FriendRelationThreadPool.asynRun(new FriendRelationThread(totalThreadLaunch, authors, friendIds, i, authorsSize, num, userId));
                }
                try {
                    logger.info("Waiting All Thread Finish!");
                    // 等待其他线程执行完
                    //totalThreadLaunch.await();
                    //TODO看这个效率而定
                    totalThreadLaunch.await(1000, TimeUnit.MILLISECONDS);
                    logger.info("All Thread Finish!");
                } catch (Exception e) {
                    logger.error("CountDownLatch thread is interrupted.", e);
                }
                /* finally {
                    executorService.shutdown();
                }*/
            }
            
        } else {
            Map<Integer, BuddyRelation> relationMap = BuddyRelationCacheAdapter.getInstance().getMultiRelation(userId, authors);
            Set<Integer> keys = relationMap.keySet();
            for (Integer key : keys) {
                if (relationMap.get(key).isFriend()) {
                    friendIds.add(key);
                }
            }
        }

        
        //添加自己,并进行缓存， 证明自己5分钟内已经调用过好友接口
        friendIds.add(userId);
        return friendIds;
    }

    /**
     * 获取对该实体评论的浏览者好友列表
     * 
     * @param entryId
     * @param authors
     * @param authors
     * @param type
     * @param urlmd5 
     * @return
     */
    /*private List<Integer> getCommentedFriendListByActor(long entryId, int actorId, List<Integer> authors,
            CommentType type, String urlmd5) {

        String commentFriendListKey = null;
        if (type == CommentType.Status) {
            commentFriendListKey = entryId + "-" + type + "-" + actorId;
        } else {
            commentFriendListKey = urlmd5 + "-" + actorId;
        }
        //获取评论用户列表
        List<Integer> friendIds = null;
        
        friendIds = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.SHARE_COMMENT_FRIEND_LIST,
                commentFriendListKey, 0, MAX_USER_LIST_NUM);
       
        if (CollectionUtils.isEmpty(friendIds)) {
            friendIds = getUserFriendIdList(actorId, authors);
            //异步写缓存
            AsyncCommentOpUtil.asyncWriteAuthorsToTair(friendIds, tairCacheManager, TairCacheManagerImpl.SHARE_COMMENT_FRIEND_LIST, commentFriendListKey);
        }
       
        return friendIds;
    }*/

    /**
     * 从全局评论表获取对该实体评论的用户列表
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param actorId 
     * @return
     */

    private List<Integer> getAuthorListFromUrlCommentTable(long entryId, int entryOwnerId, String urlmd5,
            CommentType type, int actorId) {

        String commentUserListKey = urlmd5;
        //获取评论用户列表
        // try get from cache
        List<Integer> authors = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.COMMENT_USER_LIST,
                commentUserListKey, 0, MAX_USER_LIST_NUM);
        if (authors == null) {
            //从DB获取分享有全局评论
            authors = cuss.getAuthorListByEntry(urlmd5, entryOwnerId);
            logger.error("getCommentedUserList entryId=" + entryId + ", entryOwnerId=" + entryOwnerId + ", type=" + type.name() + 
                ", actorId=" + actorId + ", number=" + authors.size());
            AsyncCommentOpUtil.asyncWriteAuthorsToTair(authors, tairCacheManager, TairCacheManagerImpl.COMMENT_USER_LIST, commentUserListKey);
        }
       
        return authors;
    }
    
//    /**
//     * 获取已经评论过的用户Id，这个接口目前只有状态在用
//     * @param entryId
//     * @param entryOwnerId
//     * @param type
//     * @param actorId 
//     * @return
//     */
//    private List<Integer> getCommentedUserListForStatus(long entryId, int entryOwnerId,
//            CommentType type, int actorId) {
//
//        String commentUserListKey = entryId + "-" + type;
//        //获取评论用户列表
//        
//        // try get from cache
//        List<Integer> authors = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.COMMENT_USER_LIST,
//                commentUserListKey, 0, MAX_USER_LIST_NUM);
//        
//        if (authors == null) {
//            //从DB获取分享有全局评论
//            authors = statusCss.getAuthorListByEntry(String.valueOf(entryId), entryOwnerId, true, type);
//            AsyncCommentOpUtil.asyncWriteAuthorsToTair(authors, tairCacheManager, TairCacheManagerImpl.COMMENT_USER_LIST, commentUserListKey);
//        }
//        return authors;
//    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendsCommentList(com.renren.ugc.comment.xoa2.CommentType, int, long, int, boolean, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public CommentListResult getFriendsCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {

        int limit = strategy.getQueryLimit();
        int offset = strategy.getQueryOffset();
        QueryOrder order = strategy.getQueryOrder();
        if (strategy == null || strategy.getEntry() == null) {
            return CommentListResult.EMPTY_LIST_RESULT;
        }

        //获取真正的实体ID，是全局评论实体ID是md5值
        String urlmd5 = strategy.getUrlMd5();
        if (StringUtils.isEmpty(urlmd5)) {
            logger.error("getFriendsCommentList error. urlmd5 is null. entryId: " + entryId + " entryOwnerId: " + entryOwnerId + " commentType: " + type);
            return CommentListResult.EMPTY_LIST_RESULT;
        }

        //1. get friends list
        List<Integer> friendIds = getFriendsList(actorId, entryId, entryOwnerId, urlmd5, type);
        if (CollectionUtils.isEmpty(friendIds)) {
            return CommentListResult.EMPTY_LIST_RESULT;
        }

        //2. get friends' comment list
        List<com.renren.ugc.comment.model.Comment> cList = getFriendCommends(type, urlmd5,
                entryOwnerId, offset, limit, order, friendIds, strategy);

        //3.judge "hasMore" and trim list
        boolean hasMore = cList.size() > strategy.getOriginalQueryLimit() ? true : false;
        if(hasMore){
        	for(int index = strategy.getOriginalQueryLimit() ; index < cList.size(); index++){
        		cList.remove(index);
        	}
        }
        
        //4. ok, comments are ready,filter whisper comment
        cList = filterWhisperComment(cList, actorId, strategy);

        //5. get some other comments information
        getExtraCommentInfos(type, cList, strategy);

        //6 return commentlist
        CommentListResult commentListResult = new CommentListResult(cList,hasMore);
        
        return commentListResult;
    }

    /**
     * 根据是否是全局评论，从不同的数据表获取好友评论，includeGlobalComment为true在url_comment中获取，
     * 否则在universe_comment中获取
     * 
     * @param type
     * @param entryOwnerId
     * @param offset
     * @param limit
     * @param order
     * @param friendIds
     * @param strategy 
     * @return
     */
    private List<com.renren.ugc.comment.model.Comment> getFriendCommends(CommentType type,
            String urlmd5, int entryOwnerId, int offset, int limit, QueryOrder order,
            List<Integer> friendIds, CommentStrategy strategy) {
        
        List<com.renren.ugc.comment.model.Comment> cList = null;
        
        if ("Y".equals((String) CommentPropertyPlaceholderConfigurer.getContextProperty(CommentCenterConsts.FRIEND_LIST_FROM_OWNER))) {
            long borderId = strategy.getQueryBorderID();
            boolean isFirstPage = (borderId == 0);
            int tempLimit = CommentFirstPageCacheService.PAGE_COUNT;

            //1.get comments from db or cache
            if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page
               
                // check cache
                cList = strategy.getCommentCacheService().getFriendCommentListByEntryCache(urlmd5, entryOwnerId, getQueryOrderString(order), tempLimit, borderId);

            }
            
            if (cList != null) {
                cList = getSubCommentList(cList, limit);
            } else {
                // cache not found, set temp limit for DB query
                strategy.setQueryLimit(tempLimit);
            }
               

            //2.there is no data in cache, so get comments from database , and set comments into cache
            if (cList == null) {
                cList = cuss.getFriendsCommentListByEntry(type, urlmd5, entryOwnerId, offset, limit, order, friendIds);

                if (strategy.isCacheEnabled() && isFirstPage) { // only cache the first page
                    strategy.getCommentCacheService().setFriendCommentListByEntryCache(urlmd5, entryOwnerId, getQueryOrderString(order), tempLimit, cList);
                }
                // only return the "limit" size of comments
                cList = getSubCommentList(cList, limit);
            }
        } else {
            cList = cuss.getFriendsCommentListByEntry(type, urlmd5, entryOwnerId, offset, limit, order, friendIds);
        }
        
        return cList;
    }
    
    /**
     * @param comment
     * @param commentType
     * @param actorId
     * @param strategy
     * 
     *   关联评论的删除
     */
    private void doLinkedCommentRemoval(com.renren.ugc.comment.model.Comment comment,CommentType commentType,int actorId,CommentStrategy strategy){
//        List<CommentLinkedInfo> linkedCommentInfos = LinkedInfoUtil.getLinkedCommentIdList(comment);
//        CommentLinkedInfo mainLinkedInfo = LinkedInfoUtil.getLinkedCommentIdList(comment, linkedCommentInfos);
//        //1.主线程同步删除mainLinkedInfo,不放在异步线程里了，因为新鲜事的Interceptor里需要它"及时"的删除
//        try {
//            if(mainLinkedInfo != null){
//                comment = dataAdapter.remove(commentType, actorId, mainLinkedInfo.getEntryId(), mainLinkedInfo.getEntryOwnerId(), mainLinkedInfo.getLinkedCommentId(),
//                        strategy);
//                //减小数量
//                if (strategy.isCacheEnabled() && comment != null) {
//                    updateCacheForCommentRemoval(commentType, mainLinkedInfo.getEntryId(), mainLinkedInfo.getEntryOwnerId(), strategy,false);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("an internal error occurs during removing a comment");
//        }
    	//1.取得关联评论
    	List<CommentLinkedInfo> linkedCommentInfos = LinkedInfoUtil.getLinkedCommentIdList(comment,strategy);
        //2. 关联评论，异步删除
        AsyncCommentOpUtil.asyncRemoveComments(commentType, actorId, linkedCommentInfos,
                strategy);
    }

    @Override
    public Map<Long, Comment> getMulti(CommentType type, int actorId, long entryId,
            int entryOwnerId, List<Long> commentIds, CommentStrategy strategy)
            throws UGCCommentException {

        Map<Long, com.renren.ugc.comment.model.Comment> commentMap;
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "get multiple, type=%s, entryId=%d, entryOwnerId=%d, commentIds=%s",
                    type.toString(), entryId, entryOwnerId, commentIds.toString()));
        }

        try {
            //commentMap = css.getMulti(entryId, entryOwnerId, commentIds);
            commentMap = dataAdapter.getMulti(type, entryId, entryOwnerId, commentIds);

        } catch (Exception e) {
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (commentMap.isEmpty()) {
            if(logger.isDebugEnabled()){
                logger.debug("nothing is found");
            }
            return commentMap;
        }
        
        //turn to  list
        List<com.renren.ugc.comment.model.Comment> cList = new ArrayList<com.renren.ugc.comment.model.Comment>(commentMap.size());
        for(Map.Entry<Long, com.renren.ugc.comment.model.Comment> entry:commentMap.entrySet()){
            cList.add(entry.getValue());
        }
        
        // ok, comments are ready,filter whisper comment
        cList = filterWhisperComment(cList, actorId, strategy);

        // get some other comments information
        getExtraCommentInfos(type, cList, strategy);
        
        //turn to map
        commentMap = new HashMap<Long,com.renren.ugc.comment.model.Comment>(cList.size());
        for(com.renren.ugc.comment.model.Comment comment:cList){
            commentMap.put(comment.getId(), comment);
        }

        return commentMap;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getCount(com.renren.ugc.comment.xoa2.CommentType, int, java.lang.String, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public long getGlobalCount(CommentType commentType, int actorId, String urlmd5,
            CommentStrategy strategy) throws UGCCommentException {
        long count = 0;

        if (strategy.isCacheEnabled()) {
           
            count = strategy.getCommentCacheService().getGlobalCount(urlmd5);
            
            if (count != CommentCacheService.COUNT_CACHE_NOT_EXIST) {
                return count;
            }
        }

        try {
            count = cuss.getCommentCountByEntry(commentType, urlmd5);
        } catch (Exception e) {
            logger.error("an error occurs during get comment total count");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        if (strategy.isCacheEnabled()) {
            strategy.getCommentCacheService().setGlobalCount(urlmd5, count);
        }

        return count;
    }
    
    private List<Comment> doReplyWithLinked(Comment c,long entryId,int entryOwnerId,CommentType commentType,CommentStrategy strategy){
        //当回复的评论也是关联评论的时候,要变成双写,metadata需要重新生成
        Comment replyComment = dataAdapter.get(entryId, entryOwnerId,  c.getToCommentId(), commentType);
        if(replyComment != null && replyComment.getFlag().isUseLinked()){
        	//1.找到对应的CommentLinkedInfos
        	List<CommentLinkedInfo> infos = LinkedInfoUtil.getLinkedCommentIdList(replyComment,strategy);
        	//2.CommentLinkedInfo--->Comment
        	List<Comment> linkedComments = LinkedInfoUtil.getCommentFromLinkedInfos(commentType,infos , c);
        	//3.取得这一条回复评论中的一些其他信息放在额外字段中
        	LinkedInfoUtil.buildExtraInfoFromReplyComment(replyComment, strategy);
        	//4.标记这个评论为关联
        	c.getFlag().setUseLinked();
        	return linkedComments;
//        	linkedComments =  LinkedInfoUtil.getCommentFromReplyComment(commentType, replyComment, c,linkedMaps);
//            if(CollectionUtils.isNotEmpty(linkedComments)){
//	            //生成flag字段
//	            c.getFlag().setUseLinked();
//	           //strategy.setIsReplyLinked(true);
//            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy) {
        List<com.renren.ugc.comment.model.Comment> cList = null;

        cList = doGetNOldestCommentOfEntry(type, entryId, entryOwnerId, strategy);

        //filter whisper
        cList = filterAllWhisperComment(cList, strategy);
        
        // get some other comments information
        getExtraCommentInfosWithNoReplace(type, cList, strategy);

        return cList;
    }

    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy) {
        List<com.renren.ugc.comment.model.Comment> cList = null;

        cList = doGetNLatestCommentOfEntry(type, entryId, entryOwnerId, strategy);

        //filter all whisper
        cList = filterAllWhisperComment(cList, strategy);
        
        // get some other comments information
        getExtraCommentInfosWithNoReplace(type, cList, strategy);

        return cList;
    }
    
    private List<com.renren.ugc.comment.model.Comment> filterAllWhisperComment(
            List<com.renren.ugc.comment.model.Comment> list, CommentStrategy strategy) {

        if (list == null || list.size() == 0) {
            return new ArrayList<com.renren.ugc.comment.model.Comment>();
        }

        List<com.renren.ugc.comment.model.Comment> results = new ArrayList<com.renren.ugc.comment.model.Comment>(
                list.size());

        for (com.renren.ugc.comment.model.Comment c : list) {
            // whisper filter out
            if (c.getWhipserToId() > 0) {
                continue;
            }

            results.add(c);
        }

        return results;

    }
    
    private List<com.renren.ugc.comment.model.Comment> doGetNLatestCommentOfEntry(CommentType type,
            long entryId, int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        List<com.renren.ugc.comment.model.Comment> cList = null;
        try {
            //c = css.getLatestCommentOfEntry(type, entryId, entryOwnerId);
            cList = dataAdapter.getNLatestCommentOfEntry(type, entryId, entryOwnerId, strategy.getLatestCount());

            if (cList == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has no comment");
                }
            }
//            } else {
//                fillAuthorInfoToComments(cList, strategy);
//            }
        } catch (Exception e) {
            String msg = "an error occurs during looking up the latest comment of entry " + entryId;
            logger.error(msg, e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        return cList;
    }
    
    private List<com.renren.ugc.comment.model.Comment> doGetNOldestCommentOfEntry(CommentType type,
            long entryId, int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        List<com.renren.ugc.comment.model.Comment> cList = null;
        try {
            //c = css.getLatestCommentOfEntry(type, entryId, entryOwnerId);
            cList = dataAdapter.getNOldestCommentOfEntry(type, entryId, entryOwnerId, strategy.getOldestCount());

            if (cList == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("entry with id " + entryId + " has no comment");
                }
            }
//            } else {
//                fillAuthorInfoToComments(cList, strategy);
//            }
        } catch (Exception e) {
            String msg = "an error occurs during looking up the oldest comment of entry " + entryId;
            logger.error(msg, e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        return cList;
    }
    
    /**
     * The param <code>commentType</code> is of no use at all.
     */
    @Override
    public Map<Long,FeedCommentResult> getCommentsForFeed(CommentType commentType, List<FeedCommentInfo> infos, CommentStrategy strategy){
        //1.build keys
        List<String> keys = new ArrayList<String>(infos.size() * 3);
        for(FeedCommentInfo info:infos){
            int entryOwnerId = info.getEntryOwnerId();
            long entryId = info.getEntryId();
            int type = info.getType();
            //1.1 count key
            keys.add(RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type));
            
            if(strategy.isOriginalContentQuery()){
            	//查询请求为originalContent
            	//1.1.1 latest key
	            keys.add(RedisKeyGen.getLatestOriginalCommentOfEntryCacheKey(entryId, entryOwnerId, type));
	            //1.1.2 oldest key
	            keys.add(RedisKeyGen.getOldestOriginalCommentOfEntryCacheKey(entryId, entryOwnerId, type));
            } else {
	            //1.2.1 latest key
	            keys.add(RedisKeyGen.getLatestCommentOfEntryCacheKey(entryId, entryOwnerId, type));
	            //1.2.2 oldest key
	            keys.add(RedisKeyGen.getOldestCommentOfEntryCacheKey(entryId, entryOwnerId, type));
            }
        }
        
        //2.get data from redis
        List<String> values = FeedRedisCacheService.getInstance().getMulti(keys);
//        if(values == null){
//            return Collections.emptyMap();
//        }
        
        //3.parse data from cache
        Map<Long, FeedCommentResult> results = new HashMap<Long, FeedCommentResult>();
        Iterator<String> valueIter = values.iterator();
        for (FeedCommentInfo info : infos) {
            int entryOwnerId = info.getEntryOwnerId();
            long entryId = info.getEntryId();
            int type = info.getType();

            //3.1 get count
            long count = FeedCommentUtil.parseCountFromValue(valueIter.next());
            if (count == -1) {
                //null,so set into redis
                count = FeedCommentUtil.putCountToRedisFromCommentLogic(
                        CommentType.findByValue(type), entryId, entryOwnerId, strategy);
            }

            //3.2 get latest
            List<Comment> tailComments = FeedCommentUtil.parseCommentFromValue(valueIter.next());
            if (CollectionUtils.isEmpty(tailComments) && count > 0) {
                //null,so set into redis
                tailComments = FeedCommentUtil.putLatestCommentToRedisFromCommentLogic(
                        CommentType.findByValue(type), entryId, entryOwnerId, strategy);
            }

            //3.3 get oldest
            List<Comment> headComments = FeedCommentUtil.parseCommentFromValue(valueIter.next());
            if (CollectionUtils.isEmpty(headComments) && count > FeedCommentUtil.FEED_REDIS_NUM) {
                //null,so set into redis
                headComments = FeedCommentUtil.putOldestCommentToRedisFromCommentLogic(
                        CommentType.findByValue(type), entryId, entryOwnerId, strategy, count
                                - FeedCommentUtil.FEED_REDIS_NUM);
            }
            //反转tail
            //Collections.reverse(tailComments);

            //优先从tail中取，这个是由{@link :UpdateFeedRedisInterceptor}中create方法保证的
            //int tailBegin = tailComments.size() - info.getTailLimit() > 0 ? tailComments.size()
            //        - info.getTailLimit() : 0;
            FeedCommentResult oneResult =
                    generateFeedCommentResult(info, count, tailComments,
                        headComments);

            results.put(info.getFeedId(), oneResult);
        }
        
        
        return results;
    }
    
    /**
     * 根据从redis中获取的最新5条和最老5条评论，构建新鲜事需要的评论
     * 
     * @param info
     * @param count
     * @param tailComments
     * @param headComments
     * @return
     */
    private FeedCommentResult generateFeedCommentResult(FeedCommentInfo info,
        long count, List<Comment> tailComments, List<Comment> headComments) {
        int tailEnd = tailComments.size() < info.getTailLimit() ? tailComments.size() : info.getTailLimit();
                
        //4.build data
         
        List<Comment> resultTailComments = new LinkedList<Comment>();
        List<Comment> resultHeadComments = new LinkedList<Comment>();
        //get tail
        for(int i = tailEnd -1 ; i >= 0; --i){
            resultTailComments.add(tailComments.get(i));
        }
        
        //get head
        if(count >= FeedCommentUtil.FEED_REDIS_NUM * 2){
            int headSize = info.getHeadLimit() > headComments.size() ? headComments.size(): info.getHeadLimit();
            for(int i = 0 ; i < headSize; ++i){
                resultHeadComments.add(headComments.get(i));
            }
        } else if(count > FeedCommentUtil.FEED_REDIS_NUM && count < FeedCommentUtil.FEED_REDIS_NUM * 2){
          //先从headComments取出所有的
          int headSize = info.getHeadLimit() > headComments.size() ? headComments.size()
                  : info.getHeadLimit();
          for(int i = 0 ; i < headSize; ++i){
              resultHeadComments.add(headComments.get(i));
          }
          //还差 ? 个,只能从tailComments中取了, 取tail的size不能超过tailBegin
          int needCount = info.getHeadLimit() - resultHeadComments.size();
          int tailBegin = tailEnd <  tailComments.size() - needCount ?  tailComments.size() - needCount : tailEnd;
          
          for(int i = tailBegin; i < tailComments.size(); ++i){
              resultHeadComments.add(tailComments.get(i));
          }
        } else if(count<= FeedCommentUtil.FEED_REDIS_NUM){
            //取tail时，末尾不能不能超过tailEnd
            for(int i = tailComments.size() -1 ; i >= tailEnd; --i){
                resultHeadComments.add(tailComments.get(i));
            }
        }

        //get tail
//            List<Comment> resultTailComments = tailComments.subList(tailBegin, tailComments.size());
//            List<Comment> resultHeadComments = new ArrayList<Comment>();
//
//            //get head
//            if (count >= FeedCommentUtil.FEED_REDIS_NUM * 2) {
//                int headSize = info.getHeadLimit() > headComments.size() ? headComments.size()
//                        : info.getHeadLimit();
//                resultHeadComments = headComments.subList(0, headSize);
//            } else if (count > FeedCommentUtil.FEED_REDIS_NUM
//                    && count < FeedCommentUtil.FEED_REDIS_NUM * 2) {
//                //优先取headComments
//                int headSize = info.getHeadLimit() > headComments.size() ? headComments.size()
//                        : info.getHeadLimit();
//                resultHeadComments = headComments.subList(0, headSize);
//                //还差 ? 个,只能从tailComments中取了, 取tail的size不能超过tailBegin
//                int tailEnd = info.getHeadLimit() - resultHeadComments.size() > tailBegin ? tailBegin
//                        : info.getHeadLimit() - resultHeadComments.size();
//                resultHeadComments.addAll(tailComments.subList(0, tailEnd));
//            } else if (count <= FeedCommentUtil.FEED_REDIS_NUM) {
//                //取tail的size不能超过tailBegin
//                int tailEnd = info.getHeadLimit() > tailBegin ? tailBegin : info.getHeadLimit();
//                resultHeadComments = tailComments.subList(0, tailEnd);
//            }

        //5.ok
        FeedCommentResult oneResult = new FeedCommentResult();
        oneResult.setHeadCommentList(resultHeadComments);
        oneResult.setTailCommentList(resultTailComments);
        oneResult.setTotalCount(count);
        oneResult.setMore(count - resultHeadComments.size() - resultTailComments.size() > 0);
        return oneResult;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCommentCount(com.renren.ugc.comment.xoa2.CommentType, int, java.lang.String, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public long getFriendCommentCount(CommentType commentType, int entryOwnerId,
        String urlmd5, CommentStrategy strategy) throws UGCCommentException {
        
        if (StringUtils.isEmpty(urlmd5) || 0 == entryOwnerId) {
            return 0;
        }
        //1. 获取该urlmd5下的所有评论的作者
        //从DB获取分享有全局评论
        List<Integer> authors = cuss.getAuthorListByEntry(urlmd5, entryOwnerId);
        
        //2. 获取所有评论的作者中是entryOwner好友的用户列表
        List<Integer> friendsId = getUserFriendIdList(entryOwnerId, authors);
        
        //3. 获取entryOwner好友的评论列表（包含entryOwner自己的评论）
        long result = cuss.getFriendCommentCountByEntry(commentType, urlmd5, friendsId);
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getNLatestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public List<Comment> getNLatestFriendCommentOfEntry(CommentType type,
        String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy) {
        if (StringUtils.isEmpty(urlmd5) || 0 == entryOwnerId) {
            return Collections.emptyList();
        }
        
        List<com.renren.ugc.comment.model.Comment> cList = null;
        
        //1. 获取该urlmd5下的所有评论的作者
        //从DB获取分享有全局评论
        List<Integer> authors = cuss.getAuthorListByEntry(urlmd5, entryOwnerId);
        
        //2. 获取所有评论的作者中是entryOwner好友的用户列表
        List<Integer> friendsId = getUserFriendIdList(entryOwnerId, authors);

        //3. 获取最新的N条好友评论
        cList = cuss.getNLatestFriendCommentOfEntry(type, entryId, entryOwnerId, urlmd5, friendsId, strategy.getLatestCount());

        //filter all whisper
        cList = filterAllWhisperComment(cList, strategy);
        
        // get some other comments information
        getExtraCommentInfos(type, cList, strategy);

        return cList;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getNOldestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public List<Comment> getNOldestFriendCommentOfEntry(CommentType type,
        String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy) {
        if (StringUtils.isEmpty(urlmd5) || 0 == entryOwnerId) {
            return Collections.emptyList();
        }
        
        List<com.renren.ugc.comment.model.Comment> cList = null;
        
        //1. 获取该urlmd5下的所有评论的作者
        //从DB获取分享有全局评论
        List<Integer> authors = cuss.getAuthorListByEntry(urlmd5, entryOwnerId);
        
        //2. 获取所有评论的作者中是entryOwner好友的用户列表
        List<Integer> friendsId = getUserFriendIdList(entryOwnerId, authors);

        //3. 获取最老的N条好友评论
        cList = cuss.getNOldestFriendCommentOfEntry(type, entryId, entryOwnerId, urlmd5, friendsId, strategy.getOldestCount());

        //filter all whisper
        cList = filterAllWhisperComment(cList, strategy);
        
        // get some other comments information
        getExtraCommentInfos(type, cList, strategy);

        return cList;
    }

    @Override
    public Map<Long, Integer> getCommentsForFeedCount(CommentType commentType,
            List<FeedCommentInfo> infos, CommentStrategy strategy) {
        
        //1.build keys
        List<String> keys = new ArrayList<String>(infos.size());
        for(FeedCommentInfo info:infos){
            int entryOwnerId = info.getEntryOwnerId();
            long entryId = info.getEntryId();
            int type = info.getType();
            //1.1 count key
            keys.add(RedisKeyGen.genCountEntryCacheKey(entryId, entryOwnerId, type));
        }
        
        //2.get data from redis
        List<String> values = FeedRedisCacheService.getInstance().getMulti(keys);
        if(values == null){
            return Collections.emptyMap();
        }
        
        //3.parse data from cache
        Map<Long, Integer> results = new HashMap<Long, Integer>();
        Iterator<String> valueIter = values.iterator();
        for (FeedCommentInfo info : infos) {
            int entryOwnerId = info.getEntryOwnerId();
            long entryId = info.getEntryId();
            int type = info.getType();

            //3.1 get count
            long count = FeedCommentUtil.parseCountFromValue(valueIter.next());
            if (count == -1) {
                //null,so set into redis
                count = FeedCommentUtil.putCountToRedisFromCommentLogic(
                        CommentType.findByValue(type), entryId, entryOwnerId, strategy);
            }
            
            //4.ok
            results.put(info.getFeedId(), (int)count);
        }
        
        return results;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCommentsForFeedCount(com.renren.ugc.comment.xoa2.CommentType, java.util.List, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public Map<Long, Integer> getFriendCommentsForFeedCount(CommentType commentType,
        List<FeedCommentInfo> infos, CommentStrategy strategy) {
        //1.build keys
        List<String> keys = new ArrayList<String>(infos.size());
        for(FeedCommentInfo info:infos){
            int entryOwnerId = info.getEntryOwnerId();
            String urlMd5 = strategy.getUrlMd5();
            //1.1 count key
            keys.add(RedisKeyGen.genFriendCountEntryCacheKey(urlMd5, entryOwnerId));
        }
        
        //2.get data from redis
        List<String> values = FeedRedisCacheService.getInstance().getMulti(keys);
        if(values == null){
            return Collections.emptyMap();
        }
        
        //3.parse data from cache
        Map<Long, Integer> results = new HashMap<Long, Integer>();
        Iterator<String> valueIter = values.iterator();
        for (FeedCommentInfo info : infos) {
            int entryOwnerId = info.getEntryOwnerId();
            int type = info.getType();
            String urlMd5 = strategy.getUrlMd5();

            //3.1 get count
            long count = FeedCommentUtil.parseCountFromValue(valueIter.next());
            if (count == -1) {
                //null,so set into redis
                count = FeedCommentUtil.putFriendCommentCountToRedisFromCommentLogic(CommentType.findByValue(type), urlMd5, entryOwnerId, strategy);
            }
            
            //4.ok
            results.put(info.getFeedId(), (int)count);
        }
        
        return results;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCommentsForFeed(com.renren.ugc.comment.xoa2.CommentType, java.util.List, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public Map<Long, FeedCommentResult> getFriendCommentsForFeed(
        CommentType commentType, List<FeedCommentInfo> infos, CommentStrategy strategy) {
        //1.build keys
        List<String> keys = new ArrayList<String>(infos.size() * 3);
        for(FeedCommentInfo info:infos){
            int entryOwnerId = info.getEntryOwnerId();
            String urlMd5 = strategy.getUrlMd5();
            //1.1 count key
            keys.add(RedisKeyGen.genFriendCountEntryCacheKey(urlMd5, entryOwnerId));
            //1.2 latest key
            keys.add(RedisKeyGen.getLatestFriendCommentOfEntryCacheKey(urlMd5, entryOwnerId));
            //1.3 oldest key
            keys.add(RedisKeyGen.getOldestFriendCommentOfEntryCacheKey(urlMd5, entryOwnerId));
        }
        
        //2.get data from redis
        List<String> values = FeedRedisCacheService.getInstance().getMulti(keys);
        if(values == null){
            return Collections.emptyMap();
        }
        
        //3.parse data from cache
        Map<Long, FeedCommentResult> results = new HashMap<Long, FeedCommentResult>();
        Iterator<String> valueIter = values.iterator();
        for (FeedCommentInfo info : infos) {
            int entryOwnerId = info.getEntryOwnerId();
            long entryId = info.getEntryId();
            int type = info.getType();
            String urlMd5 = strategy.getUrlMd5();

            //3.1 get count
            long count = FeedCommentUtil.parseCountFromValue(valueIter.next());
            if (count == -1) {
                //null,so set into redis
                count = FeedCommentUtil.putFriendCommentCountToRedisFromCommentLogic(CommentType.findByValue(type), urlMd5, entryOwnerId, strategy);
            }

            //3.2 get latest
            List<Comment> tailComments = FeedCommentUtil.parseCommentFromValue(valueIter.next());
            if (CollectionUtils.isEmpty(tailComments) && count > 0) {
                //null,so set into redis
                tailComments = FeedCommentUtil.putFriendLatestCommentToRedisFromCommentLogic(CommentType.findByValue(type), urlMd5, entryId, entryOwnerId, strategy);
            }

            //3.3 get oldest
            List<Comment> headComments = FeedCommentUtil.parseCommentFromValue(valueIter.next());
            if (CollectionUtils.isEmpty(headComments) && count > FeedCommentUtil.FEED_REDIS_NUM) {
                //null,so set into redis
                headComments = FeedCommentUtil.putFriendOldestCommentToRedisFromCommentLogic(
                        CommentType.findByValue(type), urlMd5, entryId, entryOwnerId, strategy, count
                                - FeedCommentUtil.FEED_REDIS_NUM);
            }

            FeedCommentResult oneResult =
                    generateFeedCommentResult(info, count, tailComments,
                        headComments);

            results.put(info.getFeedId(), oneResult);
        }
        
        
        return results;
    }
    
    /**
     * @param type
     * @param list
     * @param strategy
     * 
     *  不替换@和短链接
     */
    private void getExtraCommentInfosWithNoReplace(CommentType type,
            List<com.renren.ugc.comment.model.Comment> list, CommentStrategy strategy) {

        if (list == null || list.isEmpty()) {
            return;
        }

        fillAuthorInfoToCommentsWithNoReplace(list, strategy);

        for (com.renren.ugc.comment.model.Comment c : list) {

            c.setOriginalContent(c.getContent());

            // ubb replacement
            long start = System.nanoTime();
            String replacedContent = DoingUbbReplace.getInstance().replaceUBB(
                    c.getContent(),
                    strategy.getReplaceUbbLarge() != null ? strategy.getReplaceUbbLarge()
                            : false);
            long end = System.nanoTime();
            StatisticsHelper.invokeReplaceUbb((end - start) / StatisticsHelper.NANO_TO_MILLIS,
                    true);
            c.setContent(replacedContent);

            // link replacement
            c.setContent(Replace.replaceLink(c.getContent()));

            //@ replacement
            c.setContent(AtUtil.getInstance().getWithHrefAt(c.getContent()));
        }

        //short url replacement
        ShortUrlUtil.getInstance().getBatchOriginalContent(list);

        //get vip user's icon url
        VipUtil.getInstance().setVipIconUrlToComment(list);

    }
    
    private void fillAuthorInfoToCommentsWithNoReplace(List<com.renren.ugc.comment.model.Comment> commentList,
            CommentStrategy strategy) {
        if (CollectionUtils.isEmpty(commentList)) {
            return;
        }

        Set<Integer> authorIds = new HashSet<Integer>(commentList.size());
        for (com.renren.ugc.comment.model.Comment comment : commentList) {
            authorIds.add(comment.getAuthorId());
        }

        // get authors information
        Map<Integer, WUserCache> authorId2UserMap = WUserCacheAdapter.getInstance()
                .getUserCacheMap(new ArrayList<Integer>(authorIds));

        // update user information
        for (com.renren.ugc.comment.model.Comment comment : commentList) {
            WUserCache author = authorId2UserMap.get(comment.getAuthorId());
            if (author != null) {
                // set name
                comment.setAuthorName(author.getName());

                // set keep use
                comment.setAuthorKeepUse(author.isKeepUse());

                // set head
                String headUrl = author.getTinyUrl();

                // for some biz, they need full head url
                //if (returnFullHeadUrl) {
                    headUrl = HeadUrlUtil.getHeadFullUrl(headUrl);
                //}
                comment.setAuthorHead(headUrl);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#removeGlobalComment(com.renren.ugc.comment.xoa2.CommentType, int, long, int, long, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {
        if (entryId == 0 && strategy != null && strategy.getEntry() != null) {
            entryId = strategy.getEntry().getId();
        }

        String urlmd5 = strategy.getUrlMd5();
        Comment comment = null;
        try {
            comment = cuss.remove(actorId, urlmd5, commentId, type);
        } catch (UGCCommentException e) {
            throw new UGCCommentException(e.getMessage());
        } catch (Exception e) {
            logger.error("an internal error occurs during removing a comment");
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }
        if (comment == null) {
            return false;
        }
        
        if (strategy.isCacheEnabled() && comment != null) {
        	updateCacheForCommentRemoval(type, entryId, entryOwnerId,
                strategy,false);
        }

        return true;
    }
    
    /**
     * @param commentType
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     * @return
     * 
     *  如果limit > cache数量的话，从db中获得剩余的数据
     */
    private void getOtherCommentsFromDBAndFillList(List<Comment> cList,int dbLimit,CommentType commentType,int actorId,long entryId,int entryOwnerId,CommentStrategy strategy){

        if(cList == null || dbLimit <= 0){
            cList = new ArrayList();
        }
        //set borderId
        //borderId加一是因为在{@link:CommentDAO#getCommentListByEntryASC} 方法中，是大于等于borderId的
        long dbBorderId = 0;
        if(cList.size() > 0){
            dbBorderId = (strategy.getQueryOrder() == QueryOrder.ASC) ? cList.get(cList.size() - 1).getId() + 1 :  cList.get(cList.size() - 1).getId() - 1;
        }
        strategy.setQueryBorderID(dbBorderId);
        //set limit 
        strategy.setQueryLimit(dbLimit);
        //get data
        List<Comment >dbList = doGetList(commentType, actorId, entryId, entryOwnerId, strategy);
        
        cList.addAll(dbList);
    }

	/*
	 *    发通知的作者要限制每天发送的最大次数
     *    jira见:http://jira.d.xiaonei.com/browse/COMMENT-1
	 */
	@Override
	public List<Integer> filterAuthorByFrequency(CommentType type,int actorId, long entryId,
			int entryOwnerId,int toId, List<Integer> authorIds,CommentStrategy strategy) {
		
		List<Integer> results = new ArrayList<Integer>();
		
		for(Integer authorId:authorIds){
			
			if(authorId == actorId || authorId == entryOwnerId || authorId == toId || isInAtUserList(authorId,strategy)){
				//以下几种情况就不用发"也通知"了
				//1.是发送者本人; 2.是ownerId; 3.是回复人的id;4.在@列表中
				//http://qa.d.xiaonei.com/jira/browse/CONCEPT-4526
				continue;
			}
			String key = entryId + "-" + type + "-" + authorId;
			
			int defauleValue = 0;
			int addValue = 1;
			
			int count  = tairCacheManager.incr(TairCacheManagerImpl.COMMENT_ALSO_COUNT, key, addValue, defauleValue, TimeUtil.getTomorrowTime());
			if(count > 0 && count <= CommentCenterConsts.MAX_ALSO_COMMENT_COUNT){
				//oh yeah,符合条件
				results.add(authorId);
			}
		}
		
		return results;
	}
	
	/**
     * "异步"在kv数据库中存储评论该实体的user列表
     * 
     * @param commentType
     * @param actorId
     * @param entryId
     * @param strategy 
     *
     * 状态的key比较特殊,生成规则参见
     * @see
     */

    /*private void updateCommentUserForShareAsyn(final CommentType commentType, final int actorId, final long entryId, final CommentStrategy strategy) {
    	 AsynJobService.asynRun(new Runnable() {

             @Override
             public void run() {
            	 updateCommentUserForShare(commentType,actorId,entryId,strategy);
             }
         });
    }*/

    @Override
    public boolean sendFriendNotify(CommentType type, int actorId, long entryId, int entryOwnerId,
                                    long commentId, CommentStrategy strategy) throws UGCCommentException {
        boolean result = false;

        try {
            Comment comment = this.get(type, actorId, entryId, entryOwnerId, commentId, strategy);

            if (comment == null) {
                logger.error("sendFriendNotify error: comment is not exist. commentId = " + commentId + ", commentType = " +
                    type + ", entryId = " + entryId + ", entryOwnerId = " + entryOwnerId);
                return false;
            }
            result = NotifyUtil.getInstance().sendFriendNotice(type, actorId, entryOwnerId,
                    entryId, comment, strategy);

            if (result) {
                if (logger.isDebugEnabled()) {
                    logger.debug("sendNotice comment " + comment.getId() + " success");
                }
            } else {
                logger.warn("sendNotice return false");
            }
        } catch (Exception e) {
            logger.error("sendFriendNotify error. e=[" + e + "], commentId=[" + commentId + "], entryId=[" + entryId + "], entryOwnerId=[" +
            		entryOwnerId + "], actorId=[" + actorId + "], commentType=[" + type + "]");
        }

        return result;
    }
    
  //判断userId是否在at的列表中
    private boolean isInAtUserList(int userId,CommentStrategy strategy){
    	if(strategy.getAtIdLists() != null){
    		return strategy.getAtIdLists().contains(userId);
    	}
    	return false;
    }

	@Override
	public CommentListResult getCommentListWithFilter(CommentType type,
			int actorId, long entryId, int entryOwnerId, int authorId,
			CommentStrategy strategy) {
		int limit = strategy.getQueryLimit();
		int offset = strategy.getQueryOffset();
		QueryOrder order = strategy.getQueryOrder();
		long borderId = strategy.getQueryBorderID();
		List<com.renren.ugc.comment.model.Comment> cList = null;
		// TODO ... 增加缓存
		if(borderId > 0){
			cList = dataAdapter.getCommentListWithFilterByBorderId(type, entryId, entryOwnerId, authorId, order, borderId, limit);
		} else {
			cList = dataAdapter.getCommentListWithFilterByOffset(type, entryId,
					entryOwnerId, authorId, order, offset, limit);
		}

		boolean hasMore = cList.size() > strategy.getOriginalQueryLimit() ? true
				: false;
		if (hasMore) {
			// or sublist?
			for (int index = strategy.getOriginalQueryLimit(); index < cList
					.size(); index++) {
				cList.remove(index);
			}
		}
		cList = filterWhisperComment(cList, actorId, strategy);
		// get some other comments information
		getExtraCommentInfos(type, cList, strategy);
		//getRelatedCommentInfo(type, actorId, entryId, entryOwnerId, cList,strategy);
		CommentListResult commentListResult = new CommentListResult(cList,
				hasMore);
		return commentListResult;

	}

	@Override
	public long getCountWithFilter(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId, CommentStrategy strategy) {
		long count = 0 ;
		try{
			count = dataAdapter.getCountWithFilter(type, actorId, entryId, entryOwnerId, authorId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return count;
	}
	
	private boolean judgeHasPre(QueryOrder order,CommentType commentType,long entryId,int entryOwnerId,CommentStrategy strategy,long borderId,int actorId){
		Comment comment = null;
		//judge by order
        if(order == QueryOrder.ASC){
        	comment = doGetOldestCommentOfEntry(commentType, entryId, entryOwnerId, strategy,actorId);
        } else {
        	comment = doGetLatestCommentOfEntry(commentType, entryId, entryOwnerId, strategy,actorId);
        }
        
       return comment != null && comment.getId() != borderId;
	}
	
	 /**
     * 从评论中心表中获取已经评论过的用户Id,不是从全局评论表中获取
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param actorId 
     * @return
     */
    private List<Integer> getAuthorListFromCommentTable(long entryId, int entryOwnerId,
            CommentType type, int actorId) {

        String commentUserListKey = entryId + "-" + type;
        //1.先从KV中获取
        List<Integer> authors = (List<Integer>) tairCacheManager.getIntList(TairCacheManagerImpl.COMMENT_USER_LIST,
                commentUserListKey, 0, MAX_USER_LIST_NUM);
        
        if (authors == null) {
            //2.从数据库中获取作者列表
        	authors = dataAdapter.getAuthorListByEntry(entryId, entryOwnerId, type);
            AsyncCommentOpUtil.asyncWriteAuthorsToTair(authors, tairCacheManager, TairCacheManagerImpl.COMMENT_USER_LIST, commentUserListKey);
        }
        return authors;
    }

	@Override
	public List<CommentPackage> createByList(CommentType type, long entryId,
			int entryOwnerId, ForInvokeStrategy forInvokeStrategy)
			throws UGCCommentException {
		List<CommentPackage> result = new ArrayList<CommentPackage>();
		
		for(CommentPackage commentPer:forInvokeStrategy.getPackageList()){
			Comment comment = create(type,commentPer.getActorId() , entryId, entryOwnerId,
					commentPer.getComment(), commentPer.getForCommentStrategy());
			CommentPackage onepackage = new CommentPackage();
			onepackage.setComment(comment);
			onepackage.setForCommentStrategy(commentPer.getForCommentStrategy());
			onepackage.setActorId(comment.getAuthorId());
			result.add(onepackage);
		}
		return result;
	}
    
}