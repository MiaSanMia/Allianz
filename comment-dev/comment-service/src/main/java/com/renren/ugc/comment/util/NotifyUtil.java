package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.share.ShareFeedUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.album.PhotoWith;
import com.xiaonei.antispam.AntiSpamUtils;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;
import com.xiaonei.xce.buddyadapter.buddyrelationcache.BuddyRelationCacheAdapter;
import com.xiaonei.xce.domain.buddy.BuddyRelation;
import com.xiaonei.xce.notify.NotifyAdapter;
import com.xiaonei.xce.notify.NotifyBody;

/**
 * @author wangxx 评论发送通知工具类
 */
public class NotifyUtil {

    private Logger logger = Logger.getLogger(this.getClass());

    private static NotifyUtil instance = new NotifyUtil();

    public static final int SHARE_SCHEMA_PAGE_COMMENT = 30;

    public static final int SHARE_SCHEMA_BLOG_COMMENT = 95;

    private static final int SHARE_BLOG_REALTIME_SCHEMA_ID = 430;

    private static final int SHARE_BLOG_REALTIME_TYPE = 95;

    public static final int SHARE_SCHEMA_TERMINAL_TYPE = 58;

    public static final int SHARE_SCHEMA_TERMINAL_COMMENT = 58;

    // 分享相册终端页
    public static final int SCHEMA_SHARE_ALBUM_PHOTO_COMMENT = 124;

    public static final int SHARE_SCHEMA_AUDIO_COMMENT = 59;

    public static final int PHOTO_ZUJI_COMMENT = 445;

    private Map<CommentType, NotifyInfo> noticeInfoMap =
            new HashMap<CommentType, NotifyInfo>();

    private NotifyUtil(){
        noticeInfoMap.put(CommentType.Blog, new NotifyInfo(17, 425));
        noticeInfoMap.put(CommentType.Status, new NotifyInfo(16, 77));
        noticeInfoMap.put(CommentType.Photo, new NotifyInfo(18, 128));
        noticeInfoMap.put(CommentType.Album, new NotifyInfo(19, 19));
        noticeInfoMap.put(CommentType.Share, new NotifyInfo(0, 0)); // share在构造NotifyBody时会替换这两个值
        noticeInfoMap.put(CommentType.ShareAlbumPhoto, new NotifyInfo(0, 0));// ShareAlbumPhoto在构造NotifyBody时会替换这两个值
        noticeInfoMap.put(CommentType.ShortVideo, new NotifyInfo(1052, 1052));
        noticeInfoMap.put(CommentType.CampusPost, new NotifyInfo(NotifyConstants.CAMPUS_REPLY, NotifyConstants.CAMPUS_REPLY));
        noticeInfoMap.put(CommentType.CampusAlbumPost, new NotifyInfo(NotifyConstants.CAMPUS_ALBUM_REPLY, NotifyConstants.CAMPUS_ALBUM_REPLY));
        noticeInfoMap.put(CommentType.CampusTop, new NotifyInfo(NotifyConstants.CAMPUS_TOP_POST_REPLY, NotifyConstants.CAMPUS_TOP_POST_REPLY));
        noticeInfoMap.put(CommentType.CampusExcellent, new NotifyInfo(NotifyConstants.CAMPUS_EXCELLENT_POST_REPLY, NotifyConstants.CAMPUS_EXCELLENT_POST_REPLY));
    }

    public static NotifyUtil getInstance() {
        return instance;
    }

    public boolean sendNotice(CommentType type, int actorId, int entryOwnerId,
        long entryId, Comment comment, CommentStrategy strategy) {

        if (comment == null) {
            return false;
        }

        // 1.get type and schemaId, type = schemId,so get only one just ok
        NotifyInfo notifyInfo = noticeInfoMap.get(type);
        if (notifyInfo == null) {
            logger.warn("sendNotice gettype from noticeInfoMap is null | type:"
                        + type);
            return false;
        }

        // 2.buildNotifyBody
        NotifyBody notifyBody =
                this.buildNotifyBody(type, actorId, entryOwnerId, entryId,
                    comment, notifyInfo, strategy);

        // 3.send
        try {
            if (notifyBody != null) {
                NotifyAdapter.getInstance().dispatch(notifyBody);
            }
        } catch (Exception e) {
            logger.error("NotifyUtil sendNotice error:", e);
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the comment created notification is sent");
        }
        return true;
    }

    /**
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @return 由于每个业务的通知走的模板都不一样，而且key值也不一样，因此这里做个统一
     */
    private NotifyBody buildNotifyBody(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment, NotifyInfo notifyInfo,
        CommentStrategy strategy) {

        if (comment == null || notifyInfo == null) {
            return null;
        }

        NotifyBody notifyBody = new NotifyBody();

        // 1. 去各个业务取entry
        // EntryAdapter.getInstance().getEntryInfo(type, actorId, entryOwnerId,
        // entryId, comment);

        // 2.notify的共用部分
        notifyBody.setType(notifyInfo.getType());
        notifyBody.setSchemaId(notifyInfo.getSchemaId());
        notifyBody.setFromId(actorId);
        notifyBody.setTime(comment.getCreatedTime());
        notifyBody.setOwner(entryOwnerId);
        notifyBody.setSource(entryId);

        boolean ret = false;
        // 3. build notifyBody
        switch (type) {
            case Blog:
                ret =
                        buildBlogNotifyBody(actorId, entryOwnerId, entryId,
                            comment, notifyBody, strategy);
                break;
            case Status:
                ret =
                        buildStatusNotifyBody(actorId, entryOwnerId, entryId,
                            comment, notifyBody, strategy);
                break;
            case Photo:
                if (User.isPageId(entryOwnerId)) {
                    // orz ,photo对于page用户发送的通知不一样,copy from photo
                    notifyBody.setType(28);
                }
                if (strategy.isZujiComment()) {
                    // 足迹评论构造特有的zuji信息
                    notifyBody.setSchemaId(PHOTO_ZUJI_COMMENT);
                    notifyBody.setType(PHOTO_ZUJI_COMMENT);
                    ret =
                            this.buildPhotoZujiNotifyBody(actorId,
                                entryOwnerId, entryId, comment, notifyBody,
                                strategy);
                } else {
                    // 普通照片评论
                    ret =
                            buildPhotoNotifyBody(actorId, entryOwnerId,
                                entryId, comment, notifyBody, strategy);
                }
                break;

            case Share:
            case ShareAlbumPhoto:
                ret =
                        buildShareNotifyBody(actorId, entryOwnerId, entryId,
                            comment, notifyBody, strategy);
                break;

            case Album:
                ret =
                        buildAlbumNotifyBody(actorId, entryOwnerId, entryId,
                            comment, notifyBody, strategy);
                break;
                
            case ShortVideo:
            	ret = buildVideoNotifyBody(actorId, entryOwnerId, entryId,
                        comment, notifyBody, strategy);
            	break;
            	
            case CampusPost:
            case CampusAlbumPost:
            	ret = buildCampusNotifyBody(actorId, entryOwnerId, entryId, comment, notifyBody, strategy);
            	break;
            case CampusTop:
            	ret = buildCampusTopNotifyBody(actorId, entryOwnerId, entryId, comment, notifyBody, strategy);
            	break;
            case CampusExcellent:
            	ret = buildCampusExcellentNotifyBody(actorId, entryOwnerId, entryId, comment, notifyBody, strategy);
            	break;
            default:
                logger.warn("no valid type in getBusiParams | type:" + type);
        }
        
        return ret ? notifyBody : null;

    }

    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     */
    private boolean buildShareNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {
        Entry share = strategy.getEntry();
        String title = getTitle(share);
        int shareType =
                Integer.valueOf(share.getEntryProps().get("share_type"));
        notifyBody.setValue("share_id", String.valueOf(share.getId()));
        notifyBody.setValue("comment_id", String.valueOf(comment.getId()));
        // notifyBody.setValue("share_name", !StringUtils.isEmpty(title) ?
        // title:"分享");
        // modified by wangxx 2013-11-18,如果为空的话，默认不加"分享"两个字了
        notifyBody.setValue("share_name",
            !StringUtils.isEmpty(title) ? title : "");
        notifyBody.setValue("share_type",
            share.getEntryProps().get("share_type"));
        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue("reply_content", comment.getContent());// 概念版消息中心改动加评论内容
        notifyBody.setValue("imgUrl", share.getEntryProps().get("thumb_url"));// 概念版消息中心改动加缩略图
        
        //用户头像的路径
        User host = WUserAdapter.getInstance().get(actorId);
        notifyBody.setValue("from_pic",host.getHeadFullUrl());
        
       // 新鲜事相关
        notifyBody.setValue("feed_stype",ShareFeedUtil.getShareFeedType(shareType, share,strategy)+"");
        notifyBody.setValue("feed_source", entryId + "");
        notifyBody.setValue("feed_actor", entryOwnerId + "");
        
        long virtualId =
                share.getEntryProps().get("virtual_id") == null ? 0 : Long.valueOf(share.getEntryProps().get(
                    "virtual_id"));

        Set<Integer> toIdSet = new HashSet<Integer>();

        if (comment.getToUserId() != 0) {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }

        Map<String, String> map = null;
        int type = 0;
        int schemaId = 0;

        if (strategy.isAllowRealTimeNotify()) {
            if ((share.getOwnerId() >= 600000000)
                && (share.getOwnerId() < 700000000)) {
                type = SHARE_SCHEMA_PAGE_COMMENT;
                schemaId = SHARE_SCHEMA_PAGE_COMMENT;
            } else {
                if (shareType == IShareConstants.URL_TYPE_VIDEO
                    || shareType == IShareConstants.URL_TYPE_CONNECT_VIDEO
                    || shareType == IShareConstants.URL_TYPE_PAGE_VIDEO
                    || shareType == IShareConstants.URL_TYPE_CLUB_VIDEO) {
                    type = 58;
                    schemaId = 218;
                    map = new HashMap<String, String>();
                    map.put("feed_stype",
                        String.valueOf(FeedDefinition.SHARE_VIDEO));
                    map.put("feed_actor", String.valueOf(share.getOwnerId()));
                    map.put("feed_source", String.valueOf(share.getId()));
                    map.put("from_pic", comment.getAuthorHead());
                    map.put("reply_content", comment.getContent());
                } else if (shareType == IShareConstants.URL_TYPE_AUDIO
                           || shareType == IShareConstants.URL_TYPE_NEW_AUDIO
                           || shareType == IShareConstants.URL_TYPE_NEW_ALBUMAUDIO
                           || shareType == IShareConstants.URL_TYPE_COOPERATIVE_DOUDING) {
                    type = 0;
                    schemaId = 0;
                } else if (shareType == IShareConstants.URL_TYPE_BLOG) {
                    type = SHARE_BLOG_REALTIME_TYPE;
                    schemaId = SHARE_BLOG_REALTIME_SCHEMA_ID;
                    map = new HashMap<String, String>();
                    map.put("feed_actor", String.valueOf(share.getOwnerId()));
                    map.put("feed_source", String.valueOf(share.getId()));
                    map.put("feed_stype",
                        String.valueOf(FeedDefinition.SHARE_BLOG));
                    map.put("from_pic", comment.getAuthorHead());
                    map.put("reply_content", comment.getContent());
                    map.put(
                        "blog_id",
                        String.valueOf(share.getEntryProps().get("resource_id")));
                } else if (shareType == IShareConstants.URL_TYPE_PHOTO
                           && virtualId > 0) {
                    type = 0;
                    schemaId = 0;
                } else if (shareType == IShareConstants.URL_TYPE_PHOTO
                           && !(virtualId > 0)) {
                    type = 58; // IShareServiceConstants.IWebPaperNotifyConst.PHOTO_REALTIME_TYPE;
                    schemaId = 233; // IShareServiceConstants.IWebPaperNotifyConst.PHOTO_REALTIME_SCHEMA_ID;

                    map = new HashMap<String, String>();
                    if (share.getEntryProps().get("fromConnect") != null
                        && share.getEntryProps().get("fromConnect").equals("1")) {
                        map.put(
                            "feed_stype",
                            String.valueOf(FeedDefinition.XIAONEI_CONNECT_PHOTO));
                    } else if (shareType == 1) {// (G.isTypeOf((getShare().getResourceUserId()),
                                                // Type.PAGE)) {
                        // 这里使用resourceUser来判断，是因为type不足以区分是分享page的照片还是用户的照片
                        // 有一种情况就是，在page域下和photo域下分享，走的不同的type
                        map.put("feed_stype",
                            String.valueOf(FeedDefinition.PAGE_SHARE_PHOTO));
                    } else {
                        map.put("feed_stype",
                            String.valueOf(FeedDefinition.SHARE_PHOTO));
                    }

                    map.put("feed_actor", String.valueOf(share.getOwnerId()));
                    map.put("feed_source", String.valueOf(share.getId()));
                    map.put("from_pic", comment.getAuthorHead());
                    map.put("reply_content", comment.getContent());
                } else {
                    type = 0;
                    schemaId = 0;
                }
            }

            // 这里是实时化特殊，把owner发的也实时化提醒
            if (actorId == share.getOwnerId()) {
                notifyBody.addToid(actorId);
            }

        } else {
            if ((share.getOwnerId() >= 600000000)
                && (share.getOwnerId() < 700000000)) {
                type = SHARE_SCHEMA_PAGE_COMMENT;
                schemaId = SHARE_SCHEMA_PAGE_COMMENT;
                map = new HashMap<String, String>();
                map.put("audio_id", String.valueOf(comment.getId()));
                map.put("audio_name", getTitle(share));
            } else {
                if (shareType == IShareConstants.URL_TYPE_VIDEO
                    || shareType == IShareConstants.URL_TYPE_CONNECT_VIDEO
                    || shareType == IShareConstants.URL_TYPE_PAGE_VIDEO
                    || shareType == IShareConstants.URL_TYPE_CLUB_VIDEO) {
                    type = 58;
                    schemaId = 58;
                } else if (shareType == IShareConstants.URL_TYPE_AUDIO
                           || shareType == IShareConstants.URL_TYPE_NEW_AUDIO
                           || shareType == IShareConstants.URL_TYPE_NEW_ALBUMAUDIO
                           || shareType == IShareConstants.URL_TYPE_COOPERATIVE_DOUDING
                           || shareType == IShareConstants.URL_TYPE_ALBUM) {
                    type = SHARE_SCHEMA_TERMINAL_TYPE;
                    schemaId = SHARE_SCHEMA_TERMINAL_COMMENT;
                } else if (shareType == IShareConstants.URL_TYPE_BLOG) {
                    type = SHARE_SCHEMA_BLOG_COMMENT;
                    schemaId = SHARE_SCHEMA_BLOG_COMMENT;
                } else if (shareType == IShareConstants.URL_TYPE_PHOTO
                           && virtualId > 0) {
                    type = SCHEMA_SHARE_ALBUM_PHOTO_COMMENT;
                    schemaId = SCHEMA_SHARE_ALBUM_PHOTO_COMMENT;

                    map = new HashMap<String, String>();
                    map.put("album_name", getTitle(share));
                    // 下面主要针对分享相册中的相片
                    map.put("virtual_id",
                        share.getEntryProps().get("virtual_id"));
                    map.put("shareOwner", String.valueOf(share.getOwnerId()));
                } else if (shareType == IShareConstants.URL_TYPE_PHOTO
                           && !(virtualId > 0)) {
                    type = SHARE_SCHEMA_TERMINAL_TYPE; // IShareServiceConstants.IWebPaperNotifyConst.PHOTO_TYPE;
                    schemaId = SHARE_SCHEMA_TERMINAL_TYPE;// IShareServiceConstants.IWebPaperNotifyConst.PHOTO_SCHEMA_ID;
                } else {
                    type = SHARE_SCHEMA_AUDIO_COMMENT;
                    schemaId = SHARE_SCHEMA_AUDIO_COMMENT;
                }
            }

        }

        if (map != null) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                notifyBody.setValue(key, map.get(key));
            }
        }

        notifyBody.setType(type);
        notifyBody.setSchemaId(schemaId);
        return true;
    }

    /**
     * 获取分享标题
     * 
     * @return
     */
    private String getTitle(Entry entry) {
        String title = entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_TITLE);
        return title;
    }

    private boolean buildStatusNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {
        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue("doing_id", String.valueOf(entryId));
        notifyBody.setValue(
            "doing_content",
            AntiSpamUtils.getInstance().htmlSecurityEscape(
                strategy.getEntry().getEntryProps().get(
                    EntryConfig.ENTRY_CONTENT)));
        notifyBody.setValue("replied_id", String.valueOf(comment.getId()));
        notifyBody.setValue("feed_stype",
            strategy.getEntry().getEntryProps().get(EntryConfig.ENTRY_STYPE));
        notifyBody.setValue("feed_source", String.valueOf(entryId));
        notifyBody.setValue("feed_actor", String.valueOf(entryOwnerId));
        notifyBody.setValue("from_pic", comment.getAuthorHead());
        notifyBody.setValue("reply_content", comment.getContent());

        Set<Integer> toIdSet = new HashSet<Integer>();
        if (comment.getWhipserToId() > 0) {
            toIdSet.add(comment.getWhipserToId());// 被回复的人
        } else {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }
        return true;
    }

    // copy from blog...
    private boolean buildBlogNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {

        if (comment == null) {
            return false;
        }
        
        // 根据来源发不同的通知
        switch(strategy.getIsFrom()){
 
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	LookingWorldUrlUtil.buildLookingWorldNotifyBodyAll(CommentType.Blog, entryOwnerId, entryId, comment, notifyBody, strategy);
		    	break;
	        default: {
	        	//默认
	        	 notifyBody.setValue("from_name", comment.getAuthorName());
	             notifyBody.setValue("blog_id", entryId + "");
	             notifyBody.setValue("anchor", comment.getId() + "");
	             notifyBody.setValue("blog_title",
	                 EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
	             notifyBody.setValue("is_whisper",
	                 comment.getWhipserToId() > 0 ? "1" : "0");
	             notifyBody.setValue("feed_stype", "601");// 发表日志新鲜事类型
	             notifyBody.setValue("feed_source", entryId + "");
	             notifyBody.setValue("feed_actor", entryOwnerId + "");
	             // String content =
	             // DoingUbbReplace.getInstance().replaceUBB(comment.getContent(), true);
	             notifyBody.setValue("reply_content", comment.getContent());
	             notifyBody.setValue("from_pic", comment.getAuthorHead());// 回复者的头像
	             // 为人人桌面离线回复优化，加属性feed_creator_id
	             notifyBody.setValue("feed_creator_id", entryOwnerId + "");
	        }
        }
        
        Set<Integer> toIdSet = new HashSet<Integer>();
        if (comment.getWhipserToId() > 0) {
            toIdSet.add(comment.getWhipserToId());// 被回复的人
        } else {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }
        return true;
    }

    private boolean buildPhotoNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {
    	
    	  // 根据来源发不同的通知
        switch(strategy.getIsFrom()){
 
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	LookingWorldUrlUtil.buildLookingWorldNotifyBodyAll(CommentType.Photo, entryOwnerId, entryId, comment, notifyBody, strategy);
		    	break;
		    default:
		    {
		        notifyBody.setValue("from_name", comment.getAuthorName());
		        notifyBody.setValue("album_name",
		            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_PARENT_NAME));
		
		        notifyBody.setValue("photo_id", String.valueOf(entryId));
		        notifyBody.setValue("is_whisper",
		            comment.getWhipserToId() > 0 ? "1" : "0");
		        // 3G win8需要照片描述信息
		        notifyBody.setValue("photo_desc",
		            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
		
		        // 新鲜事相关,copy from photo
		        final int stype =
		                (EntryConfigUtil.getBoolean(strategy,
		                    EntryConfig.ENTRY_IS_MULTI) ? FeedDefinition.PHOTO_REPLY : FeedDefinition.PHOTO_PUBLISH_ONE);
		
		        notifyBody.setValue("feed_stype", String.valueOf(stype));
		        notifyBody.setValue("feed_source", String.valueOf(entryId));
		        notifyBody.setValue("feed_actor", String.valueOf(entryOwnerId));
		        notifyBody.setValue("from_pic", comment.getAuthorHead());
		        notifyBody.setValue("reply_content", comment.getContent());
		        notifyBody.setValue("imgUrl",
		            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
		        notifyBody.setValue("big_image",
		            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_LARGEURL));
		    }
        }

        Set<Integer> toIdSet = new HashSet<Integer>();
        if (comment.getWhipserToId() > 0) {
            toIdSet.add(comment.getWhipserToId());// 被回复的人
        } else {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }
        return true;
    }

    private boolean buildAlbumNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {
    	
    	  // 根据来源发不同的通知
        switch(strategy.getIsFrom()){
 
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	LookingWorldUrlUtil.buildLookingWorldNotifyBodyAll(CommentType.Album, entryOwnerId, entryId, comment, notifyBody, strategy);
		    	break;
		    	
		    default: 
		    	{
			        notifyBody.setValue("from_name", comment.getAuthorName());
			        notifyBody.setValue("album_id", entryId + "");
			        notifyBody.setValue(
			            "album_name",
			            AntiSpamUtils.getInstance().htmlSecurityEscape(
			                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE)));
			        notifyBody.setValue("is_whisper",
			            comment.getWhipserToId() > 0 ? "1" : "0");
			
			        // 新鲜事相关
			        notifyBody.setValue("feed_stype",
			            String.valueOf(FeedDefinition.PHOTO_PUBLISH_MORE));
			        notifyBody.setValue("feed_source", entryId + "");
			        notifyBody.setValue("feed_actor", entryOwnerId + "");
			
			        notifyBody.setValue("reply_content", comment.getContent());
			        notifyBody.setValue("imgUrl",
			            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
			        
			        //用户头像的路径
			        User host = WUserAdapter.getInstance().get(actorId);
			        notifyBody.setValue("from_pic",host.getHeadFullUrl());
		    	}
        }

        Set<Integer> toIdSet = new HashSet<Integer>();
        if (comment.getWhipserToId() > 0) {
            toIdSet.add(comment.getWhipserToId());// 被回复的人
        } else if (comment.getToUserId() > 0) {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }

        return true;
    }

    public boolean sendPhotoWithNotice(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment,
        CommentStrategy strategy) {

        if (comment == null) {
            return false;
        }

        // 1.photo with type = 641,copy from photo
        NotifyInfo notifyInfo = new NotifyInfo(641, 641);

        // 2.buildNotifyBody
        NotifyBody notifyBody =
                this.buildPhotoWithNotifyBody(type, actorId, entryOwnerId,
                    entryId, comment, notifyInfo, strategy);

        // 3.send
        try {
            if (notifyBody != null) {
                NotifyAdapter.getInstance().dispatch(notifyBody);
            }
        } catch (Exception e) {
            logger.error("NotifyUtil sendNotice error:", e);
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the comment created notification is sent");
        }
        return true;
    }

    private NotifyBody buildPhotoWithNotifyBody(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment, NotifyInfo notifyInfo,
        CommentStrategy strategy) {

        if (comment == null || notifyInfo == null) {
            return null;
        }

        NotifyBody notifyBody = new NotifyBody();

        // 1. 去各个业务取entry
        // EntryAdapter.getInstance().getEntryInfo(type, actorId, entryOwnerId,
        // entryId, comment);

        // 2.notify的共用部分
        notifyBody.setType(notifyInfo.getType());
        notifyBody.setSchemaId(notifyInfo.getSchemaId());
        notifyBody.setFromId(actorId);
        notifyBody.setTime(comment.getCreatedTime());
        notifyBody.setOwner(entryOwnerId);
        notifyBody.setSource(entryId);

        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue(
            "album_name",
            AntiSpamUtils.getInstance().htmlSecurityEscape(
                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE)));
        notifyBody.setValue("album_url", String.format(
            "http://photo.renren.com/photo/%d/photo-%d", entryOwnerId, entryId));
        notifyBody.setValue("icon",
            "http://xnimg.cn/imgpro/icons/wall_post.gif");

        // 从strategy取得photoWith信息
        List<PhotoWith> photoWiths = strategy.getPhotoWiths();
        if (photoWiths == null) {
            return null;
        }
        List<Integer> photoWithIds = new ArrayList<Integer>();
        for (PhotoWith photoWith : photoWiths) {
            photoWithIds.add(photoWith.getUserId());
        }
        for (final Integer targertId : photoWithIds) {
            notifyBody.addToid(targertId);
        }

        return notifyBody;

    }

    private boolean buildPhotoZujiNotifyBody(int actorId, int entryOwnerId,
        long entryId, Comment comment, NotifyBody notifyBody,
        CommentStrategy strategy) {
        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue(
            "address",
            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJISITE_NAME));
        notifyBody.setValue("ownerId", entryOwnerId + "");
        notifyBody.setValue("siteId",
            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJISITE_ID));
        notifyBody.setValue("zujiId",
            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJI_ID));
        notifyBody.setValue("itemId",
            EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJIITEM_ID));

        Set<Integer> toIdSet = new HashSet<Integer>();
        if (comment.getWhipserToId() > 0) {
            toIdSet.add(comment.getWhipserToId());// 被回复的人
        } else {
            toIdSet.add(comment.getToUserId());
        }
        toIdSet.add(entryOwnerId);

        for (Integer toId : toIdSet) {
            notifyBody.addToid(toId);
        }
        return true;
    }

    /**
     * 向好友发送消息, 目前之针对相册、日志、照片分享发送通知
     * 
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     * @return
     */
    public boolean sendFriendNotice(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment,
        CommentStrategy strategy) {
        Entry entry = strategy.getEntry();
        int replyToUser = comment.getToUserId();
        int resourceUserId = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID);
        String shareUrl =
                entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
        String shareTitle = getShareTitle(entry);
        
      //匿名帖子做特殊处理
        int shareType = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SHARE_TYPE);
        if(shareType == IShareConstants.URL_TYPE_CAMPUS_POST){
        	resourceUserId = AlbumPostUtil.getCampusRealUserId(resourceUserId);
        }

        // 判断该条评论是否向原ugc作者发送好友消息
        if (!isSendFriendNotify(actorId, entry, comment, replyToUser, type,
            resourceUserId, entryOwnerId)) {
            return true;
        }

        // 过隐私，判断原ugc当前状态时候是公开的

        // 好友评论的typeId 和 schemaId
        NotifyInfo notifyInfo = new NotifyInfo(1040, 1040);

        NotifyBody notifyBody =
                this.buildFriendNotifyBody(type, actorId, entryOwnerId,
                    entryId, comment, notifyInfo, strategy, resourceUserId,
                    shareUrl, shareTitle);

        // 3.send
        try {
            if (notifyBody != null) {
                NotifyAdapter.getInstance().dispatch(notifyBody);
            }
        } catch (Exception e) {
            logger.error("NotifyUtil sendNotice error:", e);
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("the comment created notification is sent");
        }
        return true;
    }

    /**
     * @param entry
     * @return
     */
    private String getShareTitle(Entry entry) {
        String shareTitle =
                entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_TITLE);
        if (StringUtils.isNotBlank(shareTitle)) {
            return shareTitle;
        }
        int shareType =
                Integer.valueOf(entry.getEntryProps().get(
                    EntryConfig.ENTRY_SHARE_TYPE));
        if (IShareConstants.URL_TYPE_BLOG == shareType) {
            return "日志";
        } else if (IShareConstants.URL_TYPE_PHOTO == shareType) {
            return "照片";
        } else if (IShareConstants.URL_TYPE_ALBUM == shareType) {
            return "相册";
        } else {
            return "分享";
        }
    }

    /**
     * 判断该条评论是否向原ugc作者发送好友消息
     * 
     * @param actorId
     * @param entry
     * @param comment
     * @param type
     * @param replyToUser
     * @param resourceUserId
     * @return
     */
    private boolean isSendFriendNotify(int actorId, Entry entry,
        Comment comment, int replyToUser, CommentType type, int resourceUserId,int entryOwnerId) {
        int shareType =
                Integer.valueOf(entry.getEntryProps().get(
                    EntryConfig.ENTRY_SHARE_TYPE));
        // 当分享的不是以下类型则不发送通知
        if (shareType != IShareConstants.URL_TYPE_BLOG && shareType != IShareConstants.URL_TYPE_LINK
            && shareType != IShareConstants.URL_TYPE_PHOTO && shareType != IShareConstants.URL_TYPE_VIDEO
            && shareType != IShareConstants.URL_TYPE_ALBUM && shareType != IShareConstants.URL_TYPE_CAMPUS_IMGPOST
            && shareType != IShareConstants.URL_TYPE_CAMPUS_POST) {
            return false;
        }

        // 在下述情况不发送消息 如果actorId和ugc原创者不是好友关系，
        if (CommentType.Share != type
            || !isFriendComment(actorId, resourceUserId)
            || actorId == resourceUserId
            || resourceUserId == entryOwnerId
            || replyToUser > 0
            || actorId == entryOwnerId) {
            return false;
        }

        return true;
    }

    /**
     * 判断actor是否是entryOwner的好友
     * 
     * @param actorId
     * @param entryOwnerId
     * @return
     */
    private boolean isFriendComment(int actorId, int entryOwnerId) {
        if (actorId == 0 || entryOwnerId == 0) {
            return false;
        }
        List<Integer> actorList = new ArrayList<Integer>();
        boolean isFriend = false;
        actorList.add(actorId);
        Map<Integer, BuddyRelation> relationMap =
                BuddyRelationCacheAdapter.getInstance().getMultiRelation(
                    entryOwnerId, actorList);
        Set<Integer> keys = relationMap.keySet();
        for (Integer key : keys) {
            if (relationMap.get(key).isFriend()) {
                isFriend = true;
                break;
            }
        }
        return isFriend;
    }

    /**
     * 构建好友评论消息体
     * 
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyInfo
     * @param strategy
     * @param resourceUserId
     * @param shareUrl
     * @param shareTitle
     * @return
     */
    private NotifyBody buildFriendNotifyBody(CommentType type, int actorId,
        int entryOwnerId, long entryId, Comment comment, NotifyInfo notifyInfo,
        CommentStrategy strategy, int resourceUserId, String shareUrl,
        String shareTitle) {
        NotifyBody notifyBody = new NotifyBody();
        notifyBody.setType(notifyInfo.getType());
        notifyBody.setSchemaId(notifyInfo.getSchemaId());
        notifyBody.setFromId(actorId);
        notifyBody.setTime(comment.getCreatedTime());
        notifyBody.setOwner(entryOwnerId);
        notifyBody.setSource(entryId);
        notifyBody.addToid(resourceUserId);

        notifyBody.setValue("from", Integer.toString(actorId));
        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue("shared_link", "http://share.renren.com/share/"
                                           + entryOwnerId + "/" + entryId
                                           + "#friendComment" + comment.getId());
        notifyBody.setValue("shared_content", shareTitle);
        //2014-07-29,add by wangxx
        notifyBody.setValue("share_type", Integer.toString(EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SHARE_TYPE)));
        notifyBody.setValue("share_id", Long.toString(entryId));
        notifyBody.setValue("share_name", shareTitle);
        notifyBody.setValue("imgUrl", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_SHARE_THUMB_URL));
        notifyBody.setValue("reply_content",comment.getContent());

        return notifyBody;
    }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建短视频的notifyBody
     */
    private boolean buildVideoNotifyBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {

            notifyBody.setValue("from_name", comment.getAuthorName());
            notifyBody.setValue("title",
                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
            notifyBody.setValue("title_url",
                    EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL));
            notifyBody.setValue("reply_content",comment.getContent());
            notifyBody.setValue("tiny_photo",
                    EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));

            Set<Integer> toIdSet = new HashSet<Integer>();
            if (comment.getToUserId() > 0) {
                toIdSet.add(comment.getToUserId());
            }
            toIdSet.add(entryOwnerId);

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建校园论坛的notifyBody
     */
    private boolean buildCampusNotifyBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {

            notifyBody.setValue("from_name", comment.getAuthorName());
//            notifyBody.setValue("title",
//                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
            //加上评论id
            notifyBody.setValue("title_url",
                    EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
            
            //因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
            //2014-04-09,传给消息中心的imgUrl字段表示帖子的第一张图片
            String text = ImgJsonUtil.getCampusText(comment.getContent());
            //String imgUrl = ImgJsonUtil.getCampusPic(comment.getContent());
            String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
            
            notifyBody.setValue("reply_content",text);
            notifyBody.setValue("imgUrl",imgUrl);
            notifyBody.setValue("floor", comment.getId()+"");
            
            //2014-04-22当标题为空时,截取帖子内容的最前的一段字符,填充到话题的标题
            String title =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE);
            if(StringUtils.isEmpty(title)){
            	String content = EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CONTENT);
            	int trimLength = content.length() > CommentCenterConsts.MAX_CAMPUS_TRIM_LENGTH ?  CommentCenterConsts.MAX_CAMPUS_TRIM_LENGTH : content.length();
            	title = content.substring(0, trimLength);
            }
            notifyBody.setValue("title",title);

            Set<Integer> toIdSet = new HashSet<Integer>();
//            if (comment.getToUserId() > 0) {
//                toIdSet.add(comment.getToUserId());
//            }
            toIdSet.add(entryOwnerId);

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建校园论坛top的notifyBody
     */
    private boolean buildCampusTopNotifyBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {
    	
    	if(comment.getToUserId() <= 0){
    		//不是回复的话，那么不发通知
    		return false;
    	}

            notifyBody.setValue("from_name", comment.getAuthorName());
            //加上评论id
            notifyBody.setValue("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"&comment="+comment.getId());
            
            String text = ImgJsonUtil.getCampusText(comment.getContent());
            String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
            
            notifyBody.setValue("reply_content",text);
            notifyBody.setValue("imgUrl",imgUrl);
            notifyBody.setValue("floor", comment.getId()+"");
            
            //2014-04-22当标题为空时,截取帖子内容的最前的一段字符,填充到话题的标题
            String title =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE);
            if(StringUtils.isEmpty(title)){
            	String content = EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CONTENT);
            	title = StringUtils.left(content, CommentCenterConsts.MAX_CAMPUS_TRIM_LENGTH);
            }
            notifyBody.setValue("title",title);
            
            String schoolId = EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_SCHOOL_ID);
            notifyBody.setValue("school_id",schoolId);

            Set<Integer> toIdSet = new HashSet<Integer>();
            toIdSet.add(comment.getToUserId());

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param notifyBody
     * @param strategy
     * @return
     * 
     *  构建人人精品的notifyBody
     */
    private boolean buildCampusExcellentNotifyBody(int actorId, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {
    	
    	if(comment.getToUserId() <= 0){
    		//不是回复的话，那么不发通知
    		return false;
    	}

            notifyBody.setValue("from_name", comment.getAuthorName());
            //加上评论id
            notifyBody.setValue("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
            
            String text = ImgJsonUtil.getCampusText(comment.getContent());
            String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
            String title =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE);
            long sourceId = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_SHARE_RESOURCE_ID);
            int sourceOwnerId = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID);
            int feedType =  EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_TYPE);
            String shareName =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_NAME);
            String shareTime =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_TIME);
            String shareImg =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_IMG);
            
            notifyBody.setValue("reply_content",text);
            notifyBody.setValue("imgUrl",imgUrl);
            notifyBody.setValue("floor", String.valueOf(comment.getId()));
            notifyBody.setValue("title",title);
            notifyBody.setValue("share_type",String.valueOf(feedType));
            notifyBody.setValue("source_id",String.valueOf(sourceId));
            notifyBody.setValue("source_owner_id",String.valueOf(sourceOwnerId));
            notifyBody.setValue("share_name", shareName);
            notifyBody.setValue("share_time", shareTime);
            notifyBody.setValue("share_img", shareImg);

            Set<Integer> toIdSet = new HashSet<Integer>();
            toIdSet.add(comment.getToUserId());

            for (Integer toId : toIdSet) {
                notifyBody.addToid(toId);
            }

            return true;
        }

}

class NotifyInfo {

    // 评论的type
    private int type;

    // 评论的schemaId
    private int schemaId;

    public NotifyInfo(int type, int schemaId){
        this.type = type;
        this.schemaId = schemaId;
    }

    public int getType() {
        return type;
    }

    public int getSchemaId() {
        return schemaId;
    }

}