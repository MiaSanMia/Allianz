package com.renren.ugc.comment.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.renren.ugc.comment.interceptor.impl.AntispamInterceptor;
import com.renren.ugc.comment.interceptor.impl.AtFilterInterceptor;
import com.renren.ugc.comment.interceptor.impl.AuditInterceptor;
import com.renren.ugc.comment.interceptor.impl.CampusIsGagInterceptor;
import com.renren.ugc.comment.interceptor.impl.CheckFriendsBlockInterceptor;
import com.renren.ugc.comment.interceptor.impl.CheckLengthInterceptor;
import com.renren.ugc.comment.interceptor.impl.CommentScoreInterceptor;
import com.renren.ugc.comment.interceptor.impl.GetEntryInterceptor;
import com.renren.ugc.comment.interceptor.impl.GetLikeInfoInterceptor;
import com.renren.ugc.comment.interceptor.impl.GetRelatedCommentInterceptor;
import com.renren.ugc.comment.interceptor.impl.GlobalCommentProcessInterceptor;
import com.renren.ugc.comment.interceptor.impl.MatterToMeInterceptor;
import com.renren.ugc.comment.interceptor.impl.PhotoSendWithNoticeInterceptor;
import com.renren.ugc.comment.interceptor.impl.SendNoticeInterceptor;
import com.renren.ugc.comment.interceptor.impl.SendNotifyedInterceptor;
import com.renren.ugc.comment.interceptor.impl.SetAuthorInfoInterceptor;
import com.renren.ugc.comment.interceptor.impl.SetFloorInterceptor;
import com.renren.ugc.comment.interceptor.impl.ShareAntispamInterceptor;
import com.renren.ugc.comment.interceptor.impl.ShareUpdateFeedInterceptor;
import com.renren.ugc.comment.interceptor.impl.ShortUrlEncryptInterceptor;
import com.renren.ugc.comment.interceptor.impl.StatusCommentMqInterceptor;
import com.renren.ugc.comment.interceptor.impl.StatusCommentNoticeInterceptor;
import com.renren.ugc.comment.interceptor.impl.SyncPhotoCommentToAlbumInterceptor;
import com.renren.ugc.comment.interceptor.impl.UgcCommentProcessInterceptor;
import com.renren.ugc.comment.interceptor.impl.UgcMqInterceptor;
import com.renren.ugc.comment.interceptor.impl.UpdateCountInterceptor;
import com.renren.ugc.comment.interceptor.impl.UpdateFeedInterceptor;
import com.renren.ugc.comment.interceptor.impl.UpdateFeedRedisInterceptor;
import com.renren.ugc.comment.interceptor.impl.WriteOldDatabaseInterceptor;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.antispam.model.CheckType;

/**
 * The comment center configuration for each business.
 * 
 * @author jiankuan xing
 */
public class CommentConfig {

    public static final int DEFAULT_MAX_CONTENT_LENGTH = 4096;

    private static final Map<CommentType, CommentConfig> config_map =
            new ConcurrentHashMap<CommentType, CommentConfig>();

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(500).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config_map.put(CommentType.ForumPhoto, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setAntispamType(CheckType.PHOTO_REPLY_TYPE).setNeedEntry(
                    false);
        config_map.put(CommentType.MusicComment, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setAntispamType(CheckType.MUSIC_REPLY).setMaxContentLength(
                    140);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.SongBookComment, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setReplaceUbb(true).setNeedCount(
                    true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.Gift, config);
    }
    static {
        CommentConfig config = new CommentConfig().setNeedEntry(false);
        config_map.put(CommentType.DiyGift, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setUseExtContent(true).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.Thread, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(500).setReplaceUbb(true).setUseFakeEntryOwnerId(
                    true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.Qun, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.OpenCommentBoxS, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.OpenCommentBoxF, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setAntispamType(CheckType.VIDEO_REPLY).setMaxContentLength(
                    500).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.Video, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setUseExtContent(true).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config_map.put(CommentType.Dummy, config);
    }

    static {
        CommentConfig config =new CommentConfig();
        config_map.put(CommentType.Feed, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setAntispamType(CheckType.FEED_COMMENT).setMaxContentLength(
                    140).setSendNotice(true).setReplaceUbb(true).setShouldAudit(
                    true).setReturnFullHeadUrl(true).setNeedCount(true);

        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config_map.put(CommentType.Generic, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setAntispamType(
                    CheckType.COMMENT_OF_USER_PLATFORM).setReplaceUbb(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config_map.put(CommentType.CustomerExperiencePlatform, config);
    }

    // 2013-07-22,新增blog类型
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setAntispamType(
                    CheckType.BLOG_REPLY_TYPE).setReplaceUbb(true).setNeedLikeInfo(
                    true).setNeedVoiceInfo(true).setReturnFullHeadUrl(true).setNeedCount(
                    true).setSendNotice(true).setReplaceAt(true).setReplaceShortUrl(
                    true).setNeedGlobal(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AtFilterInterceptor.class);
        config.addPreInterceptor(ShortUrlEncryptInterceptor.class);
        //config.addPreInterceptor(WriteOldDatabasePreInterceptor.class);

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(GetLikeInfoInterceptor.class);
        config.addPostInterceptor(SendNoticeInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);
        config.addPostInterceptor(MatterToMeInterceptor.class);
        //config.addPostInterceptor(WriteOldDatabaseInterceptor.class);
        //config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
        config.addPostInterceptor(CommentScoreInterceptor.class);

        config_map.put(CommentType.Blog, config);
    }

    // 2013-09-10,新增status类型
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(240).setTrimWhenContentTooLong(
                    true).setAntispamType(CheckType.STATE_REPLY).setReplaceUbb(
                    true).setNeedLikeInfo(true).setNeedVoiceInfo(true).setReturnFullHeadUrl(
                    true).setNeedCount(true).setSendNotice(true).setReplaceAt(
                    true).setReplaceShortUrl(true).setFeedQuote(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AtFilterInterceptor.class);
        config.addPreInterceptor(ShortUrlEncryptInterceptor.class);
        // config.addPreInterceptor(WriteOldDatabasePreInterceptor.class);

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(GetLikeInfoInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);
        config.addPostInterceptor(SendNoticeInterceptor.class);
        config.addPostInterceptor(StatusCommentNoticeInterceptor.class);
        config.addPostInterceptor(MatterToMeInterceptor.class);
        config.addPostInterceptor(StatusCommentMqInterceptor.class);
        config.addPostInterceptor(UpdateCountInterceptor.class);
        // config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
        config.addPostInterceptor(WriteOldDatabaseInterceptor.class);
        config.addPostInterceptor(CommentScoreInterceptor.class);

        config_map.put(CommentType.Status, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(500).setTrimWhenContentTooLong(
                    true).setReplaceAt(true).setNeedCount(true).setReplaceUbb(
                    true).setReplaceShortUrl(true).setNeedLikeInfo(true).setAntispamType(
                    CheckType.SHARE_REPLY).setReturnFullHeadUrl(true).setSendNotice(
                    true).setNeedGlobal(true).setNeedVoiceInfo(true).setFeedQuote(
                    true);
        config.addPreInterceptor(CheckLengthInterceptor.class); // ok
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class); // ok
        config.addPreInterceptor(ShareAntispamInterceptor.class); // ok
        config.addPreInterceptor(GetEntryInterceptor.class); // ok
        config.addPreInterceptor(AtFilterInterceptor.class); // ok
        config.addPreInterceptor(ShortUrlEncryptInterceptor.class); // ok

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class); // 删除全局评论时，异步删除原ugc评论
        config.addPostInterceptor(GetLikeInfoInterceptor.class); // ok
        config.addPostInterceptor(AuditInterceptor.class); // ok 处理审核
        config.addPostInterceptor(ShareUpdateFeedInterceptor.class); // ok
        config.addPostInterceptor(SendNoticeInterceptor.class); // (处理@ 和
                                                                // 向page发送notify)
                                                                // @处理OK
        //config.addPostInterceptor(FriendNotifyInterceptor.class);
        config.addPostInterceptor(MatterToMeInterceptor.class); // ok 处理‘与我相关’通知
        //config.addPostInterceptor(WriteOldDatabaseInterceptor.class);
        config.addPostInterceptor(UpdateCountInterceptor.class);
        //config.addPostInterceptor(UpdateFeedRedisInterceptor.class);

        config_map.put(CommentType.Share, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(500).setTrimWhenContentTooLong(
                    true).setReplaceAt(true).setReplaceUbb(true).setReplaceShortUrl(
                    true).setNeedLikeInfo(true).setNeedCount(true).setNeedVoiceInfo(
                    true).setAntispamType(CheckType.SHARE_REPLY).setReturnFullHeadUrl(
                    true).setSendNotice(true).setNeedCache(false).setFeedQuote(
                    true);
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class); // ok
        config.addPreInterceptor(CheckLengthInterceptor.class); // ok
        config.addPreInterceptor(ShareAntispamInterceptor.class); // ok
        config.addPreInterceptor(GetEntryInterceptor.class); // ok
        config.addPreInterceptor(AtFilterInterceptor.class); // ok
        config.addPreInterceptor(ShortUrlEncryptInterceptor.class); // ok

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class);
        config.addPostInterceptor(GetLikeInfoInterceptor.class); // ok
        config.addPostInterceptor(AuditInterceptor.class); // ok 处理审核
        // config.addPostInterceptor(ShareUpdateFeedInterceptor.class); //ok
        config.addPostInterceptor(SendNoticeInterceptor.class); // (处理@ 和
                                                                // 向page发送notify)
                                                                // @处理OK
        config.addPostInterceptor(MatterToMeInterceptor.class); // ok 处理‘与我相关’通知
        //config.addPostInterceptor(WriteOldDatabaseInterceptor.class);
        config.addPostInterceptor(UpdateCountInterceptor.class);
        //config.addPostInterceptor(UpdateFeedRedisInterceptor.class);

        config_map.put(CommentType.ShareAlbumPhoto, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(500).setAntispamType(
                    CheckType.PHOTO_REPLY_TYPE).setReplaceUbb(true).setNeedLikeInfo(
                    true).setNeedVoiceInfo(true).setReturnFullHeadUrl(true).setNeedCount(
                    true).setSendNotice(true).setReplaceAt(true).setNeedGlobal(
                    true).setFeedQuote(true).setIMFeedQuote(true).setNeedMetadataResolve(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AtFilterInterceptor.class);
        // config.addPreInterceptor(WriteOldDatabasePreInterceptor.class);

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(GetLikeInfoInterceptor.class);
        config.addPostInterceptor(SendNoticeInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);
        config.addPostInterceptor(MatterToMeInterceptor.class);
        //config.addPostInterceptor(WriteOldDatabaseInterceptor.class);
        config.addPostInterceptor(UpdateCountInterceptor.class);
        //config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
        config.addPostInterceptor(CommentScoreInterceptor.class);

        config_map.put(CommentType.Album, config);
    }

    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(600).setAntispamType(
                    CheckType.PHOTO_REPLY_TYPE).setReplaceUbb(true).setNeedLikeInfo(
                    true).setNeedVoiceInfo(true).setReturnFullHeadUrl(true).setNeedCount(
                    true).setSendNotice(true).setReplaceAt(true).setNeedGlobal(
                    true).setFeedQuote(true).setIMFeedQuote(true);
        ;
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AtFilterInterceptor.class);
        config.addPreInterceptor(SyncPhotoCommentToAlbumInterceptor.class);

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(GetLikeInfoInterceptor.class);
        config.addPostInterceptor(SendNoticeInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);
        config.addPostInterceptor(MatterToMeInterceptor.class);
        config.addPostInterceptor(PhotoSendWithNoticeInterceptor.class);
        config.addPostInterceptor(UpdateCountInterceptor.class);
        //config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
        config.addPostInterceptor(CommentScoreInterceptor.class);
//        config.addPostInterceptor(WriteOldDatabaseInterceptor.class);

        config_map.put(CommentType.Photo, config);
    }

    // lbs群状态
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(240).setAntispamType(
                    CheckType.LBS_GROUP_COMMENT).setReturnFullHeadUrl(true).setNeedCount(
                    true).setFeedQuote(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);

        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);

        config_map.put(CommentType.GroupStatus, config);
    }

    // lbs群单张照片
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(240).setAntispamType(
                    CheckType.LBS_GROUP_COMMENT).setReturnFullHeadUrl(true).setNeedCount(
                    true).setFeedQuote(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);

        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);

        config_map.put(CommentType.GroupSinglePhoto, config);
    }

    // lbs群多张照片
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(240).setAntispamType(
                    CheckType.LBS_GROUP_COMMENT).setReturnFullHeadUrl(true).setNeedCount(
                    true).setFeedQuote(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);

        config.addPostInterceptor(AuditInterceptor.class);
        config.addPostInterceptor(UpdateFeedInterceptor.class);

        config_map.put(CommentType.GroupMultiPhoto, config);
    }
    
    //短视频
    static {
        CommentConfig config =
                new CommentConfig().setMaxContentLength(140).setAntispamType(
                    CheckType.VIDEO_COMMENT).setReplaceUbb(true).setNeedLikeInfo(
                    true).setReturnFullHeadUrl(true).setNeedCount(
                    true).setSendNotice(true).setReplaceAt(true).setFeedQuote(true).setNeedGlobal(true);
        config.addPreInterceptor(CheckLengthInterceptor.class);
        config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
        config.addPreInterceptor(AntispamInterceptor.class);
        config.addPreInterceptor(GetEntryInterceptor.class);
        config.addPreInterceptor(AtFilterInterceptor.class);

        config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
        config.addPostInterceptor(UgcCommentProcessInterceptor.class);
        config.addPostInterceptor(AuditInterceptor.class);
    //  config.addPostInterceptor(GetLikeInfoInterceptor.class);
        config.addPostInterceptor(SendNoticeInterceptor.class);
       config.addPostInterceptor(UpdateFeedInterceptor.class);
    //    config.addPostInterceptor(MatterToMeInterceptor.class);
        config.addPostInterceptor(CommentScoreInterceptor.class);

        config_map.put(CommentType.ShortVideo, config);
    }
    
 // 校园主页
 	static {
 		CommentConfig config = new CommentConfig().setMaxContentLength(1024)
 				.setAntispamType(CheckType.SCOOL_PAGE_COMMENT).setReplaceUbb(true)
 				.setNeedLikeInfo(true).setReturnFullHeadUrl(true)
 				.setNeedCount(true).setSendNotice(true).setReplaceAt(true)
 				.setFeedQuote(true).setNeedMetadataResolve(true).setNeedSendAtInfo(true);
 		config.addPreInterceptor(CheckLengthInterceptor.class);
 		config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
 		config.addPreInterceptor(AntispamInterceptor.class);
 		config.addPreInterceptor(GetEntryInterceptor.class);
 		config.addPreInterceptor(CampusIsGagInterceptor.class);
 		config.addPreInterceptor(AtFilterInterceptor.class);
 		config.addPreInterceptor(SetFloorInterceptor.class);
 		config.addPreInterceptor(SetAuthorInfoInterceptor.class);

 		// config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
 		config.addPostInterceptor(AuditInterceptor.class);
 		config.addPostInterceptor(SendNoticeInterceptor.class);
 		config.addPostInterceptor(SendNotifyedInterceptor.class);
 		config.addPostInterceptor(StatusCommentNoticeInterceptor.class);
 		config.addPostInterceptor(GetRelatedCommentInterceptor.class);
 		config.addPostInterceptor(UgcMqInterceptor.class);
 		config.addPostInterceptor(UpdateFeedRedisInterceptor.class);

 		config_map.put(CommentType.CampusPost, config);
 	}
 	
 	// 校园主页图贴
 		static {
 			CommentConfig config = new CommentConfig().setMaxContentLength(1024)
 					.setAntispamType(CheckType.SCOOL_PAGE_COMMENT).setReplaceUbb(true)
 					.setNeedLikeInfo(true).setReturnFullHeadUrl(true)
 					.setNeedCount(true).setSendNotice(true).setReplaceAt(false)
 					.setFeedQuote(true).setNeedMetadataResolve(true).setNeedSendAtInfo(false);
 			config.addPreInterceptor(CheckLengthInterceptor.class);
 			config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
 			config.addPreInterceptor(AntispamInterceptor.class);
 			config.addPreInterceptor(GetEntryInterceptor.class);
 			config.addPreInterceptor(CampusIsGagInterceptor.class);
 			//config.addPreInterceptor(AtFilterInterceptor.class);
 			config.addPreInterceptor(SetFloorInterceptor.class);

 			// config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
 			config.addPostInterceptor(AuditInterceptor.class);
 			config.addPostInterceptor(SendNoticeInterceptor.class);
 			config.addPostInterceptor(SendNotifyedInterceptor.class);
 			config.addPostInterceptor(StatusCommentNoticeInterceptor.class);
 			config.addPostInterceptor(GetRelatedCommentInterceptor.class);
 			config.addPostInterceptor(UgcMqInterceptor.class);
 			config.addPostInterceptor(UpdateFeedRedisInterceptor.class);

 			config_map.put(CommentType.CampusAlbumPost, config);
 		}
 		
 		//2014-07-16,增加app"向右"类型
 		static {
 	        CommentConfig config =
 	                new CommentConfig().setAntispamType(CheckType.GOD_AT_RIGHT_COMMENT).setMaxContentLength(
 	                    140).setSendNotice(true).setReplaceUbb(true).setShouldAudit(
 	                    true).setReturnFullHeadUrl(true).setNeedCount(true);

 	        config.addPreInterceptor(CheckLengthInterceptor.class);
 	        config.addPreInterceptor(AntispamInterceptor.class);
 	        config.addPreInterceptor(GetEntryInterceptor.class);
 	        config.addPostInterceptor(UpdateFeedInterceptor.class);
 	        config.addPostInterceptor(AuditInterceptor.class);
 	        config_map.put(CommentType.Meet, config);
 	    }
 		
 	// 校园主页人人精选
	static {
		CommentConfig config = new CommentConfig().setMaxContentLength(1024)
				.setAntispamType(CheckType.SCOOL_PAGE_COMMENT).setReplaceUbb(true)
				.setNeedLikeInfo(false).setReturnFullHeadUrl(true).setNeedMetadataResolve(true)
				.setNeedCount(true).setSendNotice(true).setReplaceAt(true).setNeedSendAtInfo(true);
		config.addPreInterceptor(CheckLengthInterceptor.class);
		config.addPreInterceptor(CheckFriendsBlockInterceptor.class);
		config.addPreInterceptor(AntispamInterceptor.class);
		config.addPreInterceptor(SetFloorInterceptor.class);
		config.addPreInterceptor(GetEntryInterceptor.class);
		config.addPreInterceptor(CampusIsGagInterceptor.class);
		//config.addPreInterceptor(AtFilterInterceptor.class);
		//config.addPreInterceptor(SetFloorInterceptor.class);

		// config.addPostInterceptor(GlobalCommentProcessInterceptor.class);
		config.addPostInterceptor(AuditInterceptor.class);
		config.addPostInterceptor(GetRelatedCommentInterceptor.class);
		config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
		config.addPostInterceptor(SendNoticeInterceptor.class);
		//config.addPostInterceptor(SendNotifyedInterceptor.class);
		//config.addPostInterceptor(GetRelatedCommentInterceptor.class);
		//config.addPostInterceptor(UgcMqInterceptor.class);

		config_map.put(CommentType.CampusExcellent, config);
	}
	
	//2014-08-12,增加"校园主页置顶帖子"类型
		static {
	        CommentConfig config =
	                new CommentConfig().setAntispamType(CheckType.SCOOL_PAGE_COMMENT).setMaxContentLength(
	                    1024).setSendNotice(true).setReplaceUbb(true).setShouldAudit(
	                    true).setReturnFullHeadUrl(true).setNeedCount(true).setNeedMetadataResolve(true);

	        config.addPreInterceptor(CheckLengthInterceptor.class);
	        config.addPreInterceptor(AntispamInterceptor.class);
	        config.addPreInterceptor(SetFloorInterceptor.class);
	        config.addPreInterceptor(GetEntryInterceptor.class);
	        config.addPreInterceptor(CampusIsGagInterceptor.class);
	        
	        config.addPostInterceptor(AuditInterceptor.class);
	        config.addPostInterceptor(GetRelatedCommentInterceptor.class);
	        config.addPostInterceptor(SendNoticeInterceptor.class);
	        config.addPostInterceptor(UpdateFeedRedisInterceptor.class);
	        config_map.put(CommentType.CampusTop, config);
	    }
		
		//2014-09-23,增加app"向右"心情模块
 		static {
 	        CommentConfig config =
 	                new CommentConfig().setAntispamType(CheckType.GOD_AT_RIGHT_COMMENT).setMaxContentLength(
 	                    140).setSendNotice(false).setReplaceUbb(true).setShouldAudit(
 	                    true).setReturnFullHeadUrl(true).setNeedCount(true);

 	        config.addPreInterceptor(CheckLengthInterceptor.class);
 	        config.addPreInterceptor(AntispamInterceptor.class);
 	        config.addPostInterceptor(AuditInterceptor.class);
 	        config_map.put(CommentType.MeetMood, config);
 	    }
    
    public static CommentConfig getCommentConfig(CommentType type) {
        return config_map.get(type);
    }

    private int antispamType = 0;

    private int feedType = 0;

    /**
     * the max length of content (character number), 0 means not check length
     */
    private int maxContentLength = 4096;

    /**
     * Whether to trim the content when it's longer than
     * <code>maxContentLength</code>
     */
    private boolean trimWhenContentTooLong = false;

    /**
     * Whether should do online audit
     */
    private boolean shouldAudit;

    /**
     * Whether should replace the ubb symbol in the content
     */
    private boolean replaceUbb;

    /**
     * Whether to send notice (提醒)
     */
    private boolean sendNotice;

    /**
     * whether need entry logic
     */
    private boolean isNeedEntry;

    /**
     * whether to enable extended content support which can avoid max 4096 limit
     */
    private boolean useExtContent = false;

    /**
     * Some old business pass some id to "entryOwnerId", which actually is not a
     * valid user id. In such case, "entryOwnerId" can't be used to fetch a user
     * object, but can only be used to do the "get comment list by entry" query.
     */
    private boolean useFakeEntryOwnerId = false;

    /**
     * Whether to return full head url
     */
    private boolean returnFullHeadUrl = false;

    /**
     * Whether the invoker need comments' count when create/remove/recover a
     * comment
     */
    private boolean needCount;

    /**
     * Whether the biz need like information
     */
    private boolean needLikeInfo;

    /**
     * Whether the biz need voice information
     */
    private boolean needVoiceInfo;

    /**
     * Whether should replace the at in the content
     */
    private boolean replaceAt;

    /**
     * Whether should replace the shorturl in the content
     */
    private boolean replaceShortUrl;

    /**
     * Whether to double-write to url_comment table for "Global Comment"
     */
    private boolean needGlobal;

    /**
     * Whether to call send friend notify function
     */
    private boolean needSendFriendNotify = true;

    /**
     * Whether to call send at notify function
     */
    private boolean needSendAtInfo = true;

    /**
     * Whether to cache query result
     */
    private boolean needCache = true;
    
    /**
	 * whether to resove metadata
	 */
	private boolean needMetadataResolve = false;

    public boolean isUseExtContent() {
        return useExtContent;
    }

    public CommentConfig setUseExtContent(boolean useExtContent) {
        this.useExtContent = useExtContent;
        this.setMaxContentLength(16777215); // 2^24 - 1, the max length of MySQL
                                            // MEDIUMTEXT col type
        return this;
    }

    /**
     * the pre-interceptors of comment logic
     */
    List<Class<? extends CommentLogic>> preInterceptors =
            new ArrayList<Class<? extends CommentLogic>>();

    /**
     * the post-interceptors of comment logic
     */
    List<Class<? extends CommentLogic>> postInterceptors =
            new ArrayList<Class<? extends CommentLogic>>();

    public List<Class<? extends CommentLogic>> getPreInterceptors() {
        return preInterceptors;
    }

    public void addPreInterceptor(Class<? extends CommentLogic> interceptor) {
        preInterceptors.add(interceptor);
    }

    public List<Class<? extends CommentLogic>> getPostInterceptors() {
        return postInterceptors;
    }

    public void addPostInterceptor(Class<? extends CommentLogic> interceptor) {
        postInterceptors.add(interceptor);
    }

    public boolean isNeedEntry() {
        return isNeedEntry;
    }

    public CommentConfig setNeedEntry(boolean isNeedEntry) {
        this.isNeedEntry = isNeedEntry;
        return this;
    }

    public boolean isSendNotice() {
        return sendNotice;
    }

    public CommentConfig setSendNotice(boolean sendNotice) {
        this.sendNotice = sendNotice;
        return this;
    }

    public boolean shouldNeedSendAtInfo() {
        return needSendAtInfo;
    }

    public CommentConfig setNeedSendAtInfo(boolean needSendAtInfo) {
        this.needSendAtInfo = needSendAtInfo;
        return this;
    }

    public CommentConfig setMaxContentLength(int maxContentLength) {
        if (maxContentLength > DEFAULT_MAX_CONTENT_LENGTH && !useExtContent) {
            throw new RuntimeException("illegal max content length");
        }
        this.maxContentLength = maxContentLength;
        return this;
    }

    public CommentConfig setTrimWhenContentTooLong(
        boolean trimWhenContentTooLong) {
        this.trimWhenContentTooLong = trimWhenContentTooLong;
        return this;
    }

    public CommentConfig setAntispamType(int antispamType) {
        this.antispamType = antispamType;
        return this;
    }

    public CommentConfig setFeedType(int feedType) {
        this.feedType = feedType;
        return this;
    }

    public CommentConfig setShouldAudit(boolean shouldAudit) {
        this.shouldAudit = shouldAudit;
        return this;
    }

    public boolean shouldCheckSpamContent() {
        return (antispamType > 0);
    }

    public int getAntispamType() {
        return antispamType;
    }

    public int getFeedType() {
        return feedType;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public boolean isTrimWhenContentTooLong() {
        return trimWhenContentTooLong;
    }

    public boolean isReplaceUbb() {
        return replaceUbb;
    }

    public CommentConfig setReplaceUbb(boolean replaceUbb) {
        this.replaceUbb = replaceUbb;
        return this;
    }

    public boolean isPublishFeed() {
        return feedType > 0;
    }

    public boolean shouldAudit() {
        return shouldAudit;
    }

    @Override
    public String toString() {
        StringBuilder desc = new StringBuilder();
        desc.append("antispamType:\t");
        desc.append(antispamType);
        desc.append("\n");

        desc.append("feedType:\t");
        desc.append(feedType);
        desc.append("\n");

        desc.append("maxContentLength:\t");
        desc.append(maxContentLength);
        desc.append("\n");

        return desc.toString();
    }

    public boolean isUseFakeEntryOwnerId() {
        return useFakeEntryOwnerId;
    }

    public CommentConfig setUseFakeEntryOwnerId(boolean useFakeEntryOwnerId) {
        this.useFakeEntryOwnerId = useFakeEntryOwnerId;
        return this;
    }

    public boolean isReturnFullHeadUrl() {
        return returnFullHeadUrl;
    }

    public CommentConfig setReturnFullHeadUrl(boolean returnFullHeadUrl) {
        this.returnFullHeadUrl = returnFullHeadUrl;
        return this;
    }

    public boolean needCount() {
        return needCount;
    }

    public CommentConfig setNeedCount(boolean needCount) {
        this.needCount = needCount;
        return this;
    }

    public boolean isNeedLikeInfo() {
        return needLikeInfo;
    }

    public CommentConfig setNeedLikeInfo(boolean needLikeInfo) {
        this.needLikeInfo = needLikeInfo;
        return this;
    }

    public boolean isNeedVoiceInfo() {
        return needVoiceInfo;
    }

    public CommentConfig setNeedVoiceInfo(boolean needVoiceInfo) {
        this.needVoiceInfo = needVoiceInfo;
        return this;
    }

    public boolean isReplaceAt() {
        return replaceAt;
    }

    public CommentConfig setReplaceAt(boolean replaceAt) {
        this.replaceAt = replaceAt;
        return this;
    }

    public boolean isReplaceShortUrl() {
        return replaceShortUrl;
    }

    public CommentConfig setReplaceShortUrl(boolean replaceShortUrl) {
        this.replaceShortUrl = replaceShortUrl;
        return this;
    }

    public boolean isNeedGlobal() {
        return needGlobal;
    }

    public CommentConfig setNeedGlobal(boolean needGlobal) {
        this.needGlobal = needGlobal;
        return this;
    }

    public boolean isNeedCache() {
        return needCache;
    }

    public CommentConfig setNeedCache(boolean needCache) {
        this.needCache = needCache;
        return this;
    }

    /**
     * 是否在发送新鲜事的时候加上quote,参见{@link:FeedPublisher#buildFeedReplyObj()}方法
     */
    private boolean needFeedQuote;

    public boolean isNeedFeedQuote() {
        return needFeedQuote;
    }

    public CommentConfig setFeedQuote(boolean needFeedQuote) {
        this.needFeedQuote = needFeedQuote;
        return this;
    }

    public boolean isNeedSendFriendNotify() {
        return needSendFriendNotify;
    }

    public CommentConfig setNeedSendFriendNotify(boolean needSendFriendNotify) {
        this.needSendFriendNotify = needSendFriendNotify;
        return this;
    }

    /**
     * 是否在发送IM新鲜事的时候加上quote,参见{@link:FeedPublisher#buildFeedReplyObj()}方法
     */
    private boolean needIMFeedQuote;

    public boolean isNeedIMFeedQuote() {
        return needIMFeedQuote;
    }

    public CommentConfig setIMFeedQuote(boolean needIMFeedQuote) {
        this.needIMFeedQuote = needIMFeedQuote;
        return this;
    }
    
    public boolean isNeedMetadataResolve() {
		return needMetadataResolve;
	}

	public CommentConfig setNeedMetadataResolve(boolean needMetadataResolve) {
		this.needMetadataResolve = needMetadataResolve;
		return this;
	}

}
