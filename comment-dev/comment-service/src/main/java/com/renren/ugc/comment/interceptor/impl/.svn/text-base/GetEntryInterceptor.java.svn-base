package com.renren.ugc.comment.interceptor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.renren.app.share.model.ShareDel;
import com.renren.privacy.permission.define.PrivacySourceControlType;
import com.renren.ugc.comment.cache.CommentEntryCacheService;
import com.renren.ugc.comment.cache.impl.CommentEntryCacheServiceImpl;
import com.renren.ugc.comment.entry.EntryGetService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.service.CommentUrlStorageService;
import com.renren.ugc.comment.service.impl.CommentMySQLService;
import com.renren.ugc.comment.service.impl.CommentUrlMySQLService;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.UrlMd5Util;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;

@Service
public class GetEntryInterceptor extends CommentLogicAdapter {

    private CommentEntryCacheService commentEntryCacheService =
            CommentEntryCacheServiceImpl.getInstance();
    
    private CommentUrlStorageService cuss = CommentUrlMySQLService.getInstance();

    @Override
    public Comment create(CommentType commentType, int actorId, long entryId,
        int entryOwnerId, Comment comment, CommentStrategy strategy)
        throws UGCCommentException {

        // 去各个业务取entry
        // EntryAdapter.getInstance().getEntryInfo(commentType, actorId,
        // entryOwnerId, entryId, comment);
        boolean needGetEntry = isNeedGetEntryWhenWhite(commentType, entryId);

        fillEntryToStrategyByType(commentType, actorId, entryOwnerId, entryId,
            comment, strategy, needGetEntry);

        fillStypeToStrategy(commentType, strategy);

        return null;
    }
    
    @Override
	public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
			ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	//为了同步单条创建评论的逻辑，循环单条的逻辑，其实一批评论对应的是唯一的entry，所以理论上是只取一次即可
    	for(CommentPackage onepackage : forInvokeStrategy.getPackageList()){
    		  boolean needGetEntry = isNeedGetEntryWhenWhite(type, entryId);

    	      fillEntryToStrategyByType(type, onepackage.getActorId(), entryOwnerId, entryId,
    	    		  onepackage.getComment(), onepackage.getForCommentStrategy(), needGetEntry);
    	      
    	      fillStypeToStrategy(type, onepackage.getForCommentStrategy());
    	}
		return null;
	}

    @Override
    public CommentListResult getList(CommentType commentType, int actorId, long entryId,
            int entryOwner, CommentStrategy strategy) throws UGCCommentException {
        // 这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否

        boolean needGetEntry = isNeedGetEntryWhenRead(commentType, strategy);

        fillEntryToStrategyByType(commentType, actorId, entryOwner, entryId,
            null, strategy, needGetEntry);

        return null;
    }

    @Override
    public boolean remove(CommentType commentType, int actorId, long entryId,
        int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {

        // try {
        Comment comment = new Comment();
        comment.setId(commentId);

        boolean needGetEntry = isNeedGetEntryWhenWhite(commentType, entryId);

        // 这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否
        fillEntryToStrategyByType(commentType, actorId, entryOwnerId, entryId,
            comment, strategy, needGetEntry);

        fillStypeToStrategy(commentType, strategy);
        // } catch (Exception e) {
        // logger.error("GetEntryInterceptor error|type:" + commentType + "msg:"
        // + e.getMessage());
        // }

        return true;
    }

    @Override
    public boolean removeGlobalComment(CommentType commentType, int actorId,
        long entryId, int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {

        // try {
        Comment comment = new Comment();
        comment.setId(commentId);

        boolean needGetEntry = isNeedGetEntryWhenWhite(commentType, entryId);

        // 这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否
        try {
            //COMMENT-6 当被评论的Entry被删除后，对应的全站评论无法删除。
            fillEntryToStrategyByTypeWithException(commentType, actorId,
                entryOwnerId, entryId, comment, strategy, needGetEntry);
        } catch (UGCCommentException e) {
            if (e.getErrorCode() == CommentErrorCode.ENTRY_NOT_EXIST) {
                generateDeletedEntry(commentType, entryId, entryOwnerId,
                    strategy);
            } else {
                // 统一包装一下返回给客户端
                int code = getReturnErrorCode(e);
                String msg = getReturnErrorMsg(e);

                throw new UGCCommentException(code, msg);
            }
        }

        fillStypeToStrategy(commentType, strategy);

        return true;
    }

    /**
     * @param commentType
     * @param entryId
     * @param entryOwnerId
     * @param strategy
     */
    private void generateDeletedEntry(CommentType commentType, long entryId,
        int entryOwnerId, CommentStrategy strategy) {
        ShareDel shareDel = null;
        Entry entry = new Entry(entryId, entryOwnerId, commentType.toString());
        Map<String, String> pros = new HashMap<String, String>();
        if (commentType == CommentType.Share) {
            shareDel = cuss.getDeletedShareById(entryOwnerId, entryId);
            if (shareDel != null) {
                pros.put(EntryConfig.ENTRY_TITLE, shareDel.getTitle());
                //pros.put(EntryConfig.ENTRY_IS_PUBLIC,"true");
                pros.put(EntryConfig.ENTRY_CONTROL, PrivacySourceControlType.Open.getType() + "");
                pros.put(EntryConfig.ENTRY_CREATE_TIME, shareDel.getCreationDate().getTime() + "");
                pros.put(EntryConfig.ENTRY_URL_MD5, shareDel.getUrlMd5() + "");
                pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID,
                        Integer.toString(shareDel.getResourceUserId()));
                pros.put(EntryConfig.ENTRY_SHARE_RESOURCE_ID, Long.toString(shareDel.getResourceId()));
                pros.put(EntryConfig.ENTRY_SHARE_THUMB_URL, shareDel.getThumbUrl() + "");
                pros.put(EntryConfig.ENTRY_SHARE_TITLE, shareDel.getTitle() + "");
                pros.put(EntryConfig.ENTRY_SHARE_TYPE, Integer.toString(shareDel.getType()));
                pros.put(EntryConfig.ENTRY_SHARE_URL, shareDel.getUrl() + "");
                pros.put(EntryConfig.ENTRY_SHARE_VIRTUAL_ID, Long.toString(shareDel.getVirtualId()));
            }
        }
        //还未空的话，自己生成一个urlMd5
        if(StringUtils.isEmpty(pros.get(EntryConfig.ENTRY_URL_MD5))){
            String urlmd5 = UrlMd5Util.getUrlMd5(commentType, entryOwnerId, entryId);
            pros.put(EntryConfig.ENTRY_URL_MD5, urlmd5 + "");
        }
        
        entry.setEntryProps(pros);
        strategy.setEntry(entry);
    }

    @Override
    public Comment get(CommentType commentType, int actorId, long entryId,
        int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {
        // 这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否
        Comment comment = new Comment();
        comment.setId(commentId);

        boolean needGetEntry = isNeedGetEntryWhenRead(commentType, strategy);

        fillEntryToStrategyByType(commentType, actorId, entryOwnerId, entryId,
            comment, strategy, needGetEntry);

        return null;
    }

    @Override
    public CommentListResult getFriendsCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        //这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否
        boolean needGetEntry = isNeedGetEntryWhenRead(type,strategy);
        
        fillEntryToStrategyByType(type, actorId, entryOwnerId, entryId, null, strategy,needGetEntry);

        fillEntryToStrategyByType(type, actorId, entryOwnerId, entryId, null,
            strategy, needGetEntry);

        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.service.CommentLogic#getGlobalCommentList(com.
     * renren.ugc.comment.xoa2.CommentType, int, java.lang.String,
     * com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public CommentListResult getGlobalCommentList(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, String urlmd5, CommentStrategy strategy) throws UGCCommentException {
        //这里的作用就是去调用各个业务的xoa服务，查看这个entryId存在否
        boolean needGetEntry = isNeedGetEntryWhenRead(commentType,strategy);
        
        fillEntryToStrategyByType(commentType, actorId, entryOwnerId, entryId, null, strategy,needGetEntry);

        return null;
    }

    @Override
    public boolean sendFriendNotify(CommentType commentType, int actorId, long entryId, int entryOwnerId,
                                    long commentId, CommentStrategy strategy) throws UGCCommentException {
        Comment comment = new Comment();
        comment.setId(commentId);

        boolean needGetEntry = isNeedGetEntryWhenWhite(commentType, entryId);

        fillEntryToStrategyByType(commentType, actorId, entryOwnerId, entryId,
                comment, strategy, needGetEntry);

        fillStypeToStrategy(commentType, strategy);

        return true;
    }

    /**
     * 因为调状态接口时没传stype，只能在这里设置一下
     * 
     * @param type
     * @param strategy
     */
    private void fillStypeToStrategy(CommentType type, CommentStrategy strategy) {
        // 因为调状态接口时没传stype，只能在这里设置一下
        if (type == CommentType.Status) {
            Entry entry = strategy.getEntry();
            if (entry == null) {
                return;
            }
            strategy.setFeedType(Integer.parseInt(entry.getEntryProps().get(
                EntryConfig.ENTRY_STYPE)));
        }
    }

    /**
     * 向strategy中填充实体对象
     * 
     * @param commentType
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     */
    private void fillEntryToStrategyByType(CommentType commentType,
        int actorId, int entryOwnerId, long entryId, Comment comment,
        CommentStrategy strategy, boolean needGetEntry) {
        try {
            fillEntryToStrategyByTypeWithException(commentType, actorId,
                entryOwnerId, entryId, comment, strategy, needGetEntry);
        } catch (UGCCommentException ue) {
            // 统一包装一下返回给客户端
            int code = getReturnErrorCode(ue);
            String msg = getReturnErrorMsg(ue);

            throw new UGCCommentException(code, msg);
        }

    }

    /**
     * @param commentType
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     * @param needGetEntry
     */
    private void fillEntryToStrategyByTypeWithException(
        CommentType commentType, int actorId, int entryOwnerId, long entryId,
        Comment comment, CommentStrategy strategy, boolean needGetEntry) {
        // 状态的评论取不到entryId，只能在这回调一下来取entryId，主要用于删除评论和取单条评论时
        if (commentType == CommentType.Status && entryId == 0
            && comment != null) {
            entryId =
            		CommentMySQLService.getInstance().getEntryId(commentType, entryOwnerId, comment.getId());
        } else if ((commentType == CommentType.Share || commentType.ShareAlbumPhoto == commentType)
                   && entryId == 0) {
            // 对于share,photo,album的评论取不到entryId，不用取entry直接获取comment，主要用于删除评论和取单条评论时
            return;
        }

        long start = System.nanoTime();
        // Entry entry = getEntryFromCache(commentType, actorId,
        // entryOwnerId, entryId);
        // 2013-09-27 去掉entry cache
        Entry entry = null;

        if (needGetEntry) {
            // 需要取entry
            EntryGetService entryService =
                    (EntryGetService) EntryConfig.getEntryService(commentType);
            if (entryService == null) {
                // default entry object, only hold entry id and entry owner
                // id
                entry =
                        new Entry(entryId, entryOwnerId, commentType.toString());
            } else {
                entry =
                        entryService.getEntryInfo(actorId, entryOwnerId,
                            entryId, comment, strategy);
            }
        } else {
            // 不需要取entry
            entry = new Entry(entryId, entryOwnerId, commentType.toString());
        }

        boolean success = false;
        if (null != entry) {
            success = true;
            // setEntryToCache(commentType, actorId, entryOwnerId, entryId,
            // entry);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetEntry(commentType,
            (end - start) / StatisticsHelper.NANO_TO_MILLIS, success);

        strategy.setEntry(entry);
        fillUrlMd5ToEntry(commentType, entry,strategy);

    }

    private void fillUrlMd5ToEntry(CommentType type, Entry entry,CommentStrategy strategy) {
        int entryOwnerId = 0;
        long entryId = 0l;
        if (entry == null || CommentType.Share == type) {
            return;
        }

        entryOwnerId = entry.getOwnerId();
        entryId = entry.getId();

        //long entryUrlMd5Id = this.getUrlMd5EntryId(type,entryId,strategy);
        String urlMd5 = UrlMd5Util.getUrlMd5(type, entryOwnerId, entryId);
        if (urlMd5 == null) {
            return;
        }
        entry.getEntryProps().put(EntryConfig.ENTRY_URL_MD5, urlMd5);
    }

    /**
     * 从缓存里面取entry对象
     * 
     * @param commentType
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @return
     */
    private Entry getEntryFromCache(CommentType commentType, int actorId,
        int entryOwnerId, long entryId) {
        long start = System.nanoTime();
        boolean miss = false;
        Entry entry = null;
        entry =
                (Entry) commentEntryCacheService.getEntryCache(entryId,
                    entryOwnerId, commentType);
        if (null == entry) {
            miss = true;
        }

        long end = System.nanoTime();
        StatisticsHelper.invokeGetEntryFromCache(commentType,
            (end - start) / StatisticsHelper.NANO_TO_MILLIS, miss);
        return entry;

    }

    private void setEntryToCache(CommentType commentType, int actorId,
        int entryOwnerId, long entryId, Entry commentEntry) {
        commentEntryCacheService.setEntryCache(entryId, entryOwnerId,
            commentType, commentEntry);
    }

    private boolean isNeedGetEntryWhenRead(CommentType commentType,
        CommentStrategy strategy) {
        // 2013-09-27 定的策略
        // 1.只对status,blog,photo,album业务，会取entry
        // 2.对status,blog,photo,album业务.如果没有设置参数getEntryWhenRead的不再取entry，见{@link:
        // http://wiki.d.xiaonei.com/pages/viewpage.action?pageId=24782079}
        if (commentType == CommentType.ShareAlbumPhoto
            || commentType == CommentType.Share) {
            return true;
        }
        if (commentType != CommentType.Status
            && commentType != CommentType.Blog
            && commentType != CommentType.Album
            && commentType != CommentType.Photo) {
            return false;
        }
        return strategy.getGetEntryWhenRead();

    }

    private boolean isNeedGetEntryWhenWhite(CommentType commentType,
        long entryId) {

        if ((commentType == CommentType.Share
             || commentType == CommentType.ShareAlbumPhoto
             || commentType == CommentType.Photo || commentType == CommentType.Album)
            && entryId == 0) {
            // 对于share,photo,album的评论取不到entryId，不用取entry直接获取comment，主要用于删除评论和取单条评论时
            return false;
        }
        return true;
    }

    private int getReturnErrorCode(UGCCommentException ue) {
        // 区别对待"entry隐私"和"entry获取失败"
        if (ue.getErrorCode() == CommentErrorCode.PROTECTED_SOURCE) {
            return CommentError.PERMISSON_DENY;
        }
        return CommentError.COMMENT_GETENTRY_ERROR;
    }

    private String getReturnErrorMsg(UGCCommentException ue) {
        // 区别对待"entry隐私"和"entry获取失败"
        // TODO:错误code和msg可以做成一个Map
        if (ue.getErrorCode() == CommentErrorCode.PROTECTED_SOURCE) {
            return CommentError.PERMISSION_DENY_MSG;
        }
        return CommentError.COMMENT_GETENTRY_ERROR_MSG;
    }
    
//    private long getUrlMd5EntryId(CommentType type,long entryId,CommentStrategy strategy){
//    	//这里针对"相册"特殊处理了
//    	return type == CommentType.Album && strategy.getParentEntryId() != 0 ? strategy.getParentEntryId() : entryId;
//    }
    
    @Override
    public boolean removeAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
    	
    	Entry entry = new Entry(entryId,entryOwnerId,commentType.toString());
    	fillUrlMd5ToEntry(commentType, entry,strategy);
    	
        return false;
    }
}
