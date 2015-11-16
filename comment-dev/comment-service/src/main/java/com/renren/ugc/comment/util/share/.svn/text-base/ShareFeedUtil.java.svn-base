/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xce.feed.FeedReply;

import com.renren.app.at.local.service.impl.AtInfoFormatLocalServiceImpl;
import com.renren.app.at.model.AtFormatType;
import com.renren.app.share.IShareConstants;
import com.renren.app.shorturl.xoa.api.ShortUrlService;
import com.renren.doingubb.tool.DoingUbbReplace;
import com.renren.shorturl.local.config.ShortUrlConfigUtil;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.ShareFeedConfig;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.task.BaseCommentTask;
import com.renren.ugc.comment.task.CommentTaskDispatcherClient;
import com.renren.ugc.comment.task.CommentTaskType;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.common.util.JSONUtil;
import com.renren.ugc.model.shorturl.FORMAT_TYPE;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.ServiceFutureHelper;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.xiaonei.antispam.filter.Replace;
import com.xiaonei.commons.gid.util.G;
import com.xiaonei.commons.gid.util.Type;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;
import com.xiaonei.platform.component.xfeed.reply.FeedReplyContentBuilder;
import com.xiaonei.platform.component.xfeed.reply.XFeedReplyer;
import com.xiaonei.platform.core.head.HeadUrlUtil;
import com.xiaonei.xce.feed.reply.FeedReplyDispatcher2;
import com.xiaonei.xce.feed.reply.FeedReplyObj;
/**
 * Descriptions of the class ShareFeedUtil.java's
 * implementation：产生一次对share的评论，向feed发送消息
 * 
 * @author xiaoqiang 2013-9-13 上午11:36:13
 */
public class ShareFeedUtil {

    private static Logger logger = Logger.getLogger(ShareFeedUtil.class);

    /** 小站 blog */
    public static final int FEED_TYPE_SITE_BLOG = 3801;

    /** 小站 video */
    public static final int FEED_TYPE_SITE_VIDEO = 3802;

    /** 小站 photo */
    public static final int FEED_TYPE_SITE_PHOTO = 3803;

    /** 小站 link */
    public static final int FEED_TYPE_SITE_LINK = 3804;

    /** 小站 audio */
    public static final int FEED_TYPE_SITE_AUDIO = 3805;
    
    /** 分享群的新鲜事类型 */
    public static final int FEED_TYPE_LBSGROUP = 1217;
    

    /** 短链接用于统计的from字段 */
    private static final String SHORTURL_FROM = "from";
    
    /** 分享发给短链接的from字段 */
    private static final String SHORTURL_SHARE_FROM_PARAM = "01030000002";

    private static final String SHARE_PAGE_CONFIG_FILE = "share-page-config.xml";
    
    private static final long TIMEOUT_MILLIS = 500;
    
    /**
     * 短链接服务
     */
    private static ShortUrlService service = ServiceFactories.getFactory().getService(
        ShortUrlService.class);

    private static Map<Integer, ShareFeedConfig> shareFeedConfigMap;

    private static Map<Integer, ShareFeedConfig> outterShareFeedConfigMap = new HashMap<Integer, ShareFeedConfig>();

    public static Map<Integer, List<Integer>> sharePageReplyConfigMap;

    static {
        loadShareFeedConfig();
    }

    /**
     * 分享类型和对应的新鲜事类型的map
     */
    private static Map<Integer, Integer> feedTypeMap = new HashMap<Integer, Integer>();

    static {
        feedTypeMap.put(IShareConstants.URL_TYPE_BLOG, FeedDefinition.SHARE_BLOG);
        feedTypeMap.put(IShareConstants.URL_TYPE_PHOTO, FeedDefinition.SHARE_PHOTO);
        feedTypeMap.put(IShareConstants.URL_GROUP_THREAD, FeedDefinition.SHARE_THREAD);
        feedTypeMap.put(IShareConstants.URL_TYPE_FRIEND, FeedDefinition.SHARE_FRIEND);
        feedTypeMap.put(IShareConstants.URL_TYPE_ALBUM, FeedDefinition.SHARE_ALBUM);
        feedTypeMap.put(IShareConstants.URL_TYPE_BBS, FeedDefinition.SHARE_THEME);
        feedTypeMap.put(IShareConstants.URL_TYPE_LINK, FeedDefinition.SHARE_LINK);
        feedTypeMap.put(IShareConstants.URL_TYPE_VIDEO, FeedDefinition.SHARE_VIDEO);
        feedTypeMap.put(IShareConstants.URL_TYPE_AUDIO, FeedDefinition.SHARE_MUSIC);
        //状态是140 TODO 
        feedTypeMap.put(IShareConstants.URL_TYPE_STATUS, 140);

        // feedTypeMap.put(IShareConstants.URL_TYPE_MARKET,
        // FeedDefinition.SHARE_MARKET);
        feedTypeMap.put(IShareConstants.URL_MOVIE_COMMENT, FeedDefinition.SHARE_MOVIECOMMENT);
        // feedTypeMap.put(IShareConstants.URL_TYPE_EVENT,
        // FeedDefinition.SHARE_ACTIVITY);
        feedTypeMap.put(IShareConstants.URL_TYPE_APP, FeedDefinition.SHARE_APP);

        feedTypeMap.put(17, FeedDefinition.PAGE_SHARE_FRIEND);

        feedTypeMap.put(18, FeedDefinition.XIAONEI_CONNECT_VIDEO);
        feedTypeMap.put(24, FeedDefinition.CONNECT_SHARE_PIC);

        // page
        feedTypeMap.put(19, FeedDefinition.PAGE_SHARE_MUSIC);
        feedTypeMap.put(20, FeedDefinition.PAGE_SHARE_BLOG);
        feedTypeMap.put(21, FeedDefinition.PAGE_SHARE_LINK);
        feedTypeMap.put(22, FeedDefinition.PAGE_SHARE_PHOTO);
        feedTypeMap.put(23, FeedDefinition.PAGE_SHARE_VIDEO);
        feedTypeMap.put(IShareConstants.URL_TYPE_PAGE_ALBUM, 2009);

        feedTypeMap.put(IShareConstants.URL_TYPE_CONNECT_NEW_LINK, FeedDefinition.SHARE_LINK);
        feedTypeMap.put(IShareConstants.URL_TYPE_CONNECT_NEW_VIDEO,
                FeedDefinition.XIAONEI_CONNECT_VIDEO);

        feedTypeMap.put(IShareConstants.URL_TYPE_COMMODITY_TAOBAO, 126);

        feedTypeMap.put(IShareConstants.URL_TYPE_ACTIVITY_LINK, FeedDefinition.SHARE_LINK);

        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_FORUM, 130);

        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_THREAD, 131);

        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_PAGE_THREAD, 131);

        // 新分享音乐
        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_AUDIO, 8602);
        // 新分享音乐专辑
        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_ALBUMAUDIO, 134);
        // 新分享用户自定义音乐专辑
        feedTypeMap.put(IShareConstants.URL_TYPE_NEW_USER_DEFINED_ALBUM_AUDIO, 135);

        feedTypeMap.put(IShareConstants.URL_TYPE_EDM_PIC, FeedDefinition.SHARE_EDM_PIC);

        feedTypeMap.put(IShareConstants.URL_TYPE_EDM_TEXT, FeedDefinition.SHARE_EDM_TEXT);

        feedTypeMap.put(IShareConstants.URL_TYPE_EDM_VIDEO, FeedDefinition.SHARE_EDM_VIEDO);

        feedTypeMap.put(IShareConstants.URL_TYPE_ZHAN_AUDIO, FEED_TYPE_SITE_AUDIO);

        feedTypeMap.put(IShareConstants.URL_TYPE_ZHAN_BLOG, FEED_TYPE_SITE_BLOG);

        feedTypeMap.put(IShareConstants.URL_TYPE_ZHAN_LINK, FEED_TYPE_SITE_LINK);

        feedTypeMap.put(IShareConstants.URL_TYPE_ZHAN_VIDEO, FEED_TYPE_SITE_VIDEO);

        feedTypeMap.put(IShareConstants.URL_TYPE_ZHAN_PHOTO, FEED_TYPE_SITE_PHOTO);

        //豆丁文档
        feedTypeMap.put(IShareConstants.URL_TYPE_COOPERATIVE_DOUDING, 136);
        
        //分享群
        //修复bug: http://qa.d.xiaonei.com/jira/browse/IOSCONCEPT-5568
        feedTypeMap.put(IShareConstants.URL_TYPE_GROUP, FEED_TYPE_LBSGROUP);

    }

    /** Page分享Page时需要调用XOA的分享类型 */
    public static Set<Integer> pageSharePageType = new HashSet<Integer>();
    static {
        pageSharePageType.add(IShareConstants.URL_TYPE_PAGE_VIDEO);
        pageSharePageType.add(IShareConstants.URL_TYPE_PAGE_PHOTO);
        pageSharePageType.add(IShareConstants.URL_TYPE_PAGE_BLOG);
        pageSharePageType.add(IShareConstants.URL_TYPE_PAGE_LINK);
        pageSharePageType.add(IShareConstants.URL_TYPE_PAGE_ALBUM);
    }

    /** Page用户分享Page新鲜事时ShareType所对应的FeedType */
    public static Map<Integer, Integer> pageSharePageFeedMap = new HashMap<Integer, Integer>();
    static {
        pageSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_BLOG, 2032);
        pageSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_ALBUM, 2035);
        pageSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_PHOTO, 2036);
        pageSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_VIDEO,
                FeedDefinition.PAGE_SHARE_VIDEO);
        pageSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_LINK, FeedDefinition.PAGE_SHARE_LINK);
    }

    /** page用户分享普通用户的新鲜事中，ShareType所对应的新鲜事类型 */
    public static Map<Integer, Integer> pageShareNormalFeedMap = new HashMap<Integer, Integer>();
    static {
        pageShareNormalFeedMap.put(IShareConstants.URL_TYPE_BLOG, 2032);
        pageShareNormalFeedMap.put(IShareConstants.URL_TYPE_ALBUM, 2035);
        pageShareNormalFeedMap.put(IShareConstants.URL_TYPE_PHOTO, 2036);
        pageShareNormalFeedMap.put(IShareConstants.URL_TYPE_VIDEO, FeedDefinition.PAGE_SHARE_VIDEO);
        pageShareNormalFeedMap.put(IShareConstants.URL_TYPE_LINK, FeedDefinition.PAGE_SHARE_LINK);
    }

    /** 普通用户分享page的新鲜事中，ShareType所对应的新鲜事类型 */
    public static Map<Integer, Integer> normalSharePageFeedMap = new HashMap<Integer, Integer>();
    static {
        normalSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_BLOG,
                FeedDefinition.PAGE_SHARE_BLOG);
        normalSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_ALBUM,
                FeedDefinition.PAGE_SHARE_ALBUM);
        normalSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_PHOTO,
                FeedDefinition.PAGE_SHARE_PHOTO);
        normalSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_VIDEO, 2041);
        normalSharePageFeedMap.put(IShareConstants.URL_TYPE_PAGE_LINK, 2042);
    }
    
    public static Set<String> set = new HashSet<String>();
    private static int feedCutWords = 140;// orgin 45
    static {
        set.add(DigestUtils.md5Hex("meituan.com"));
        set.add(DigestUtils.md5Hex("lashou.com"));
        set.add(DigestUtils.md5Hex("groupon.cn"));
        set.add(DigestUtils.md5Hex("manzuo.com"));
        set.add(DigestUtils.md5Hex("aibang.com"));
        set.add(DigestUtils.md5Hex("fantong.com"));
        set.add(DigestUtils.md5Hex("ftuan.com"));
    }

    /**
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     * @param isDeleteReply
     */
    public static void replaceFeedComment(CommentType type, int actorId, int entryOwnerId,
            long entryId, long commentId, CommentStrategy strategy, boolean isDeleteReply) {
        Entry entry = strategy.getEntry();

        //分享类型
        int shareType = Integer.valueOf(entry.getEntryProps().get("share_type"));

        CommentLogic commentLogic = strategy.getCommentLogic();

        // get the total number
        long count = commentLogic.getCount(type, actorId, entryId, entryOwnerId, strategy);

        // 获取最新的一条评论
        Comment latest = commentLogic.getLatestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);

        // get the oldest comment
        Comment oldest = null;
        if (count > 1) {
            oldest = commentLogic.getOldestCommentOfEntry(type, entryId, entryOwnerId, strategy,actorId);
        }

        //下面几种 分享类型使用新的新鲜事模板 
        if (ShareUtil.useNewFeed(shareType)) {
            replaceFeedCommentNew(entry, commentId, oldest, latest, isDeleteReply, actorId, count, strategy);
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("替换新鲜事里的评论: share=%d, %d", entry.getId(), entry.getOwnerId()));
        }

        Integer feedType = getFeedType(shareType,strategy, entryOwnerId);
        //如果分享者是page或者被分享源是page，则使用page的新鲜事定义
        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE) || isSharePageXOAType(shareType)) {
            feedType = getPageFeedType(entry.getOwnerId(), shareType,strategy);
        }

        //temp: FIXME page上传单张新鲜事的临时方案，需要删除这一段 
        if (shareType == 2
                && G.isTypeOf(Integer.valueOf(entry.getEntryProps().get("resource_user_id")),
                        Type.PAGE) && !G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            feedType = 2004;
        }
        // end temp

        if (feedType == null) {
            logger.warn("分享类型到新鲜事映射类型不存在，不替换新鲜事评论: " + shareType);
            return;
        }

        // 现在的分享评论散100份，id是每个表自增的，需要加个后缀
        FeedReplyContentBuilder firstBuilder = new FeedReplyContentBuilder();
        FeedReplyContentBuilder lastBuilder = new FeedReplyContentBuilder();
        int mod = entry.getOwnerId() % 100;
        long commentID = commentId * 100 + mod;// 为了产生一个全局唯一的回复ID

        String firstReply = buildReplyContent(firstBuilder, oldest, strategy);
        String lastReply = buildReplyContent(lastBuilder, latest, strategy);

//        if (!isDeleteReply) {
//            count = count + 1;
//        } else {
//            count = count - 1;
//        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "评论数:%s,feedType:%s,commentID:%s, shareid:%s, shareuserid:s, firstReply::%s,lastReply:%s",
                    count, feedType, commentID, entry.getId(), entry.getOwnerId(), firstReply,
                    lastReply));
        }
        FeedReply reply = new FeedReply((null == oldest) ? 0 : (oldest.getId()), firstReply,
                (null == latest) ? 0 : (latest.getId()), lastReply, (int) count);

        XFeedReplyer xFeedReplyer = XFeedReplyer.getInstance();
        List<Integer> pageFeedReplyTypes = getPageFeedReplyType(entry);
        if (isDeleteReply) {
            if (null == pageFeedReplyTypes) {
                xFeedReplyer.removeFeedReply(entry.getId(), feedType, entry.getOwnerId(),
                        commentID, reply);
            } else {
                for (Integer pageFeedType : pageFeedReplyTypes) {
                    xFeedReplyer.removeFeedReply(entry.getId(), pageFeedType, entry.getOwnerId(),
                            commentID, reply);
                }
            }
        } else {
            if (null == pageFeedReplyTypes) {
                xFeedReplyer.addFeedReply(entry.getId(), feedType, entry.getOwnerId(), reply);
            } else {
                for (Integer pageFeedType : pageFeedReplyTypes) {
                    xFeedReplyer.addFeedReply(entry.getId(), pageFeedType, entry.getOwnerId(),
                            reply);
                }
            }
        }

        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            if (logger.isDebugEnabled()) {
                logger.debug("公共主页新鲜事啦：share.getId:" + entry.getId() + " share.getUserId: "
                        + entry.getOwnerId());
            }

            asynSendFeedNotice(entry, firstBuilder, lastBuilder, (int) count, isDeleteReply,
                    actorId, false, null, null,strategy);
        }

    }

    /**
     * @param entry
     * @param firstBuilder
     * @param lastBuilder
     * @param commentCount
     * @param isDeleteReply
     * @param authorId
     * @param isNew
     * @param object
     * @param object2
     */
    private static void asynSendFeedNotice(final Entry entry,
            final FeedReplyContentBuilder firstBuilder, final FeedReplyContentBuilder lastBuilder,
            final int commentCount, final boolean isDeleteReply, final int authorId,
            final boolean isNew, final FeedReplyObj firstReplyObj, final FeedReplyObj lastReplyObj,final CommentStrategy strategy) {
        
        CommentTaskDispatcherClient.putTask(new BaseCommentTask<Void>(CommentTaskType.SHARE_SEND_FEED_NOTICE){
            private static final long serialVersionUID = 1L;
            @Override
            protected Void doCall() throws Exception {
                SharePageCommentFeedUtil.sendFeedNotice(entry, firstBuilder, lastBuilder,
                        commentCount, isDeleteReply, authorId, false, null, null,strategy);
                return null;
            }
        });
        
        /*AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
                SharePageCommentFeedUtil.sendFeedNotice(entry, firstBuilder, lastBuilder,
                        commentCount, isDeleteReply, authorId, false, null, null,strategy);
            }

        });*/

    }

    /**
     * @param entry
     * @param commentID
     * @param oldest
     * @param latest
     * @param isDeleteReply
     * @param authorId
     */
    private static void replaceFeedCommentNew(Entry entry, long commentID, Comment firstComment,
            Comment lastComment, boolean isDeleteReply, int authorId, long commentCount, CommentStrategy strategy) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("替换新鲜事里的评论: share=%d, %d, %s", entry.getId(),
                    entry.getOwnerId(), isDeleteReply));
        }

        int shareType = Integer.valueOf(entry.getEntryProps().get("share_type"));
        Integer feedType = getFeedType(shareType,strategy,entry.getOwnerId());

        //如果分享者是page或者被分享源是page，则使用page的新鲜事定义
        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE) || isSharePageXOAType(shareType)) {
            feedType = getPageFeedType(entry.getOwnerId(), shareType,strategy);
        }

        //temp: FIXME page上传单张新鲜事的临时方案，需要删除这一段 
        if (shareType == 2
                && G.isTypeOf(Integer.valueOf(entry.getEntryProps().get("resource_user_id")),
                        Type.PAGE) && !G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            feedType = 2004;
        }
        // end temp

        if (feedType == null) {
            logger.warn("分享类型到新鲜事映射类型不存在，不替换新鲜事评论: " + shareType);
            return;
        }

        // 现在的分享评论散100份，id是每个表自增的，需要加个后缀 
        int mod = entry.getOwnerId() % 100;
        commentID = commentID * 100 + mod;// 为了产生一个全局唯一的回复ID

        //cache已经同步了comment数量，不用进行增减
//        if (!isDeleteReply) {
//            commentCount = commentCount + 1;
//        } else {
//            commentCount = commentCount - 1;
//        }
        if (logger.isDebugEnabled()) {
            logger.debug("评论数：" + commentCount);
        }
        FeedReplyObj firstReplyObj = buildFeedReply(firstComment, strategy);
        FeedReplyObj lastReplyObj = buildFeedReply(lastComment, strategy);

        if (logger.isDebugEnabled()) {
            if (firstReplyObj != null) {
                logger.debug(String.format("第一条回复: %d, %s, %s, %d, %s", firstReplyObj.getReplyId(),
                        firstReplyObj.getContent(), firstReplyObj.getImContent(),
                        firstReplyObj.getFromId(), firstReplyObj.getFromName()));
            }
            if (lastReplyObj != null) {
                logger.debug(String.format("最后一条回复: %d, %s, %s, %d, %s", lastReplyObj.getReplyId(),
                        lastReplyObj.getContent(), lastReplyObj.getImContent(),
                        lastReplyObj.getFromId(), lastReplyObj.getFromName()));

            }
            logger.debug("feedType:" + feedType + "share.getId()" + entry.getId()
                    + "share.getUserId()" + entry.getOwnerId());
        }
        List<Integer> pageFeedReplyTypes = getPageFeedReplyType(entry);
        if (isDeleteReply) {
            try {
                if (null == pageFeedReplyTypes) {
                    FeedReplyDispatcher2.getInstance().removeFeedReply(lastReplyObj, firstReplyObj,
                            (int) commentCount, commentID, entry.getId(), feedType,
                            entry.getOwnerId());
                } else {
                    for (Integer pageFeedType : pageFeedReplyTypes) {
                        FeedReplyDispatcher2.getInstance().removeFeedReply(lastReplyObj,
                                firstReplyObj, (int) commentCount, commentID, entry.getId(),
                                pageFeedType, entry.getOwnerId());
                    }
                }
            } catch (Exception e) {
                logger.warn(
                        String.format(
                                "call FeedReplyDispatcher2#removeFeedReply error, shareId:%d,userId:%d,feedType:%d",
                                entry.getId(), entry.getOwnerId(), feedType), e);
            }
        } else {
            try {
                if (null == pageFeedReplyTypes) {
                    FeedReplyDispatcher2.getInstance().addFeedReply(lastReplyObj, firstReplyObj,
                            (int) commentCount, entry.getId(), feedType, entry.getOwnerId());
                } else {
                    for (Integer pageFeedType : pageFeedReplyTypes) {
                        FeedReplyDispatcher2.getInstance().addFeedReply(lastReplyObj,
                                firstReplyObj, (int) commentCount, entry.getId(), pageFeedType,
                                entry.getOwnerId());
                    }
                }
            } catch (Exception e) {
                logger.warn(
                        String.format(
                                "call FeedReplyDispatcher2#addFeedReply error, shareId:%d,userId:%d,feedType:%d",
                                entry.getId(), entry.getOwnerId(), feedType), e);
            }
        }
        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            if (logger.isDebugEnabled()) {
                logger.debug("公共主页新鲜事啦：share.getId:" + entry.getId() + " share.getUserId: "
                        + entry.getOwnerId());
            }

            asynSendFeedNotice(entry, null, null, (int) commentCount, isDeleteReply, authorId,
                    true, firstReplyObj, lastReplyObj,strategy);
        }

    }

    /**
     * @param lastComment
     * @return
     */
    private static FeedReplyObj buildFeedReply(Comment shareComment, CommentStrategy strategy) {
        if (shareComment == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("没有评论，跳过");
            }
            return null;
        }
        FeedReplyObj replyObj = new FeedReplyObj();
        replyObj.setReplyId((long) shareComment.getId());
        replyObj.setTime(new Date(shareComment.getCreatedTime()));

        //setVip(shareComment);

        String content = cutBodyContentIcon(shareComment.getOriginalContent(), feedCutWords);
        if (shareComment.getOriginalContent().length() > content.length()) {
            content = content + "...";
        } 
        
     // wenchen.di modify
        content = getCommentWithHerfShortUrl(content, Boolean.TRUE);

        // wang shufeng modify for calling method
        content =  DoingUbbReplace.getInstance().replaceUBB(content, false);
        
        content = Replace.replaceLinkwithNoHref(content);

        try {
            String result = new AtInfoFormatLocalServiceImpl().format(content, AtFormatType.HREF_NAME);
            if (logger.isDebugEnabled()) {
                logger.info(String.format("content:%s, \n result:%s", content, result));
            }
            if (result != null && !"".equals(result)) {
                replyObj.setContent(JSONUtil.quote(result));   
            } else { 
                replyObj.setContent(JSONUtil.quote(content));
            }
        } catch (Exception e) {
            replyObj.setContent(JSONUtil.quote(content));
        }

        replyObj.setImContent(content);

        replyObj.setFromId(shareComment.getAuthorId());
        replyObj.setFromName(shareComment.getAuthorName());
        replyObj.setTinyImg(getHeadFullUrl(shareComment.getAuthorHead())); 

        // 设置语音评论信息
        if (shareComment.getVoiceInfo() != null) {
            CommentVoiceInfo voice = shareComment.getVoiceInfo();
            replyObj.setVoiceCount(0);//页面不展示数字，所以默认为0
            replyObj.setVoiceLength(voice.getVoiceLength());
            replyObj.setVoiceRate(voice.getVoiceRate());
            replyObj.setVoiceSize(voice.getVoiceSize());
            replyObj.setVoiceUrl(voice.getVoiceUrl());
            replyObj.setReplyType(200);//指语音评论
        }

        return replyObj;
    }

    /**
     * 返回分享类型对应的新鲜事的类型。<br>
     * 如果map没有，会抛NullPointerException的。
     * 
     * @param shareType 分享类型
     * @return 对应的新鲜事类型
     */
    public static int getFeedType(int shareType,CommentStrategy strategy,int entryOwnerId) {
        Integer feedType = feedTypeMap.get(shareType);
        if (feedType == null) {
            if (getFeedConfig(shareType) != null) {
                feedType = getFeedConfig(shareType).getFeedType();
            }
        }
        //继续为空的话，那么调用传入的参数
        if(feedType == null){
        	feedType = strategy.getFeedType();
        }
        //做一些特殊处理
        int specialType = doSpecialNeed(shareType,entryOwnerId);
        if(specialType != 0){
        	feedType = specialType;
        }
        return feedType;
    }

    /**
     * 获取feed config信息。
     * 
     * @param shareType 分享类型
     * @return
     */
    public static ShareFeedConfig getFeedConfig(int shareType) {
        ShareFeedConfig feedConfig = null;

        if (shareFeedConfigMap != null) {
            feedConfig = shareFeedConfigMap.get(shareType);
        }
        if (feedConfig == null && outterShareFeedConfigMap != null) {
            feedConfig = outterShareFeedConfigMap.get(shareType);
        }
        return feedConfig;
    }

    public static int getPageFeedType(int userId, int shareType,CommentStrategy strategy) {
        if (G.isTypeOf(userId, Type.PAGE) && pageSharePageType.contains(shareType)) {
            //如果是Page用户分享Page的新鲜事
            return pageSharePageFeedMap.get(shareType);
        } else if (G.isTypeOf(userId, Type.PAGE)) {
            //如果是Page分享普通用户的新鲜事
            return pageShareNormalFeedMap.get(shareType);
        } else if (normalSharePageFeedMap.containsKey(shareType)) {
            //如果是普通用户分享Page的新鲜事
            return normalSharePageFeedMap.get(shareType);
        }
        //都找不到就代表逻辑有问题，先返回普通的feedType
        return getFeedType(shareType,strategy,0);
    }

    /**
     * 构建新鲜事回复
     * 
     * @param builder
     * @param sc
     * @return
     */
    public static String buildReplyContent(FeedReplyContentBuilder builder, Comment comment, CommentStrategy strategy) {
        if (comment == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("没有评论，跳过");
            }
            return null;
        }
        //setVip(comment);
        
        builder.tinyImg(getHeadFullUrl(comment.getAuthorHead()));

        String content = cutBodyContentIcon(comment.getOriginalContent());
                if (logger.isDebugEnabled()) {
                    logger.debug("cutBodyContentIcon:" + comment.getOriginalContent());
                }
        // wenchen.di modify
        content = getCommentWithHerfShortUrl(content, Boolean.TRUE);

        // wang shufeng modify for calling method
        content =  DoingUbbReplace.getInstance().replaceUBB(content, false);

        if (logger.isDebugEnabled()) {
            logger.debug("after cutBodyContentIcon:" + content);
        }
        content = Replace.replaceLinkwithNoHref(content);

        try {
            String result = new AtInfoFormatLocalServiceImpl().format(content, AtFormatType.HREF_NAME);
            if (logger.isDebugEnabled()) {
                logger.info(String.format("content:%s, \n result:%s", content,
                        result));
            }
            if (result != null && !"".equals(result)) {
                builder.body(JSONUtil.quote(result));
            } else {
                builder.body(JSONUtil.quote(content));
            }
        } catch (Exception e) {
            builder.body(JSONUtil.quote(content));
        }


        builder.imBody(content);

        builder.replyId((long) comment.getId());
        try {
            builder.time(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(comment.getCreatedTime()));
        } catch (Exception e) {}
        builder.actorName(comment.getAuthorName());
        builder.actor(comment.getAuthorId());

         //设置语音评论信息
        if (comment.getVoiceInfo() != null) {
            CommentVoiceInfo voice = comment.getVoiceInfo();
            builder.setVoiceCount(0);//页面不展示数字，所以默认为0
            builder.setVoiceLength(voice.getVoiceLength());
            builder.setVoiceRate(voice.getVoiceRate());
            builder.setVoiceSize(voice.getVoiceSize());
            builder.setVoiceUrl(voice.getVoiceUrl());
            builder.type(200);//表示是语音评论
        }

        return builder.getXml();
    }

    /**
     * 获取需要评论的分享page新鲜事类型
     * 
     * @param share
     * @return
     */
    public static List<Integer> getPageFeedReplyType(Entry entry) {
        List<Integer> typeList = null;
        if (entry != null && !G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
            int type = Integer.valueOf(entry.getEntryProps().get("share_type"));
            //源是video/link 所有者是page，需要将type变为23/21
            // 个人分享page资源，才会走到这里
            if (type == IShareConstants.URL_TYPE_PHOTO
                    && G.isTypeOf(Integer.valueOf(entry.getEntryProps().get("resource_user_id")),
                            Type.PAGE)) {
                // 当type=IShareConstants.URL_TYPE_PHOTO时，如果源是page的照片，同步评论到ss新鲜事
                type = IShareConstants.URL_TYPE_PAGE_PHOTO;
            }

            if (sharePageReplyConfigMap.containsKey(type)) {
                typeList = sharePageReplyConfigMap.get(type);
            }
            //判断上一级分享者是page，ss将来会推
        }
        return typeList;
    }

    /**
     * load inner share page config
     */
    private static void loadShareFeedConfig() {
        try {
            ApplicationContext appContext = new ClassPathXmlApplicationContext(
                    SHARE_PAGE_CONFIG_FILE);

            @SuppressWarnings("unchecked")
            Map<Integer, List<Integer>> tempSharePageReplyConfigMap = (Map<Integer, List<Integer>>) appContext.getBean(
                    "pageFeedReplyMap", Map.class);
            sharePageReplyConfigMap = tempSharePageReplyConfigMap;
        } catch (Exception e) {
            logger.error(
                    String.format("read sharePageConfig fail, file:[%s]", SHARE_PAGE_CONFIG_FILE),
                    e);
        }

    }

    /**
     * 是否是分享源为page的日志、照片、相册、视频、链接
     * 
     * @param type 分享类型
     * @return 如果是分享源为page的日志、照片、相册、视频、链接，则返回true；否则返回false
     */
    public static boolean isSharePageXOAType(int type) {
        return (IShareConstants.URL_TYPE_PAGE_BLOG == type
                || IShareConstants.URL_TYPE_PAGE_PHOTO == type
                || IShareConstants.URL_TYPE_PAGE_ALBUM == type
                || IShareConstants.URL_TYPE_PAGE_LINK == type || IShareConstants.URL_TYPE_PAGE_VIDEO == type);
    }
    
    public static String getHeadFullUrl(String headUrl) {
        if (StringUtils.isEmpty(headUrl) || headUrl.startsWith("http:")) {
            return headUrl;
        }
        return HeadUrlUtil.getHeadFullUrl(headUrl);
    }
    
    public static String cutBodyContentIcon(String comment) {
        if (comment.length() > feedCutWords) {
            String cutBody = cutBodyContentIcon(comment,
                    feedCutWords);
            return new StringBuffer().append(cutBody).append("...").toString();
        } else {
            return comment;
        }
    }
    
    public static String cutBodyContentIcon(String body,int size){
        if(body == null)
            return null;
        int length = body.length();
        if(length <= size || size <= 0)
            return body;
        
        char beginIcon = '(';
        char endIcon = ')';
        int iconLength = 0;
        
        char[] bodyArray = body.toCharArray();
        
        int currentPos = 0;
        int tmpCurrentPos = 0;
        while(currentPos < length){
            if(bodyArray[currentPos] != beginIcon){
                currentPos++;
                if(currentPos == size)
                    break;
            }
            else{               
                iconLength = 0;
                tmpCurrentPos = currentPos;
                while(bodyArray[tmpCurrentPos++] != endIcon){
                    if(bodyArray[tmpCurrentPos] == beginIcon){
                        iconLength = 0;
                        currentPos = tmpCurrentPos;
                        if(tmpCurrentPos == size){
                            break;
                        }
                    }
                    else{
                        iconLength++;
                    }
                }
                currentPos = currentPos + iconLength + 1;
                if(currentPos >= size){
                    break;
                }
            }
        }
        
        return StringUtils.substring(body, 0, currentPos);
    }
    
    /**
     * 
     * @description 调用短链接的xoa接口，返回一条带\<a\>的短链接，中间调用失败则不会进行转换
     * 
     * @author wenchen.di [wenchen.di(at)renren-in.com]
     * @since 2012-10-8 下午20:56:57
     * 
     * @param content
     * @param toShortUrl
     * @return
     */
    public static String getCommentWithHerfShortUrl(String content, boolean toShortUrl) {

        if (toShortUrl) {
            if (content == null || StringUtils.isBlank(content)) {
                if (logger.isInfoEnabled()) {
                    logger.info("分享评论短链接转换工具，字符串为空");
                }
                return content;
            }

            if (!ShortUrlConfigUtil.containsShortUrl(content)) {
                return content;
            }

            String convertedContent = "";
            XoaBizErrorListener bizErrorListener = new XoaBizErrorListener();
            ServiceFuture<String> sf = service.getOriginalContent(
                    FORMAT_TYPE.ENCRYPTED_HREF_HTML.getValue(), content);
            sf.setParam(SHORTURL_FROM, SHORTURL_SHARE_FROM_PARAM);
            sf.addListener(bizErrorListener);

            try {
                sf.submit();
                if (sf.await(TIMEOUT_MILLIS)) {
                    if (sf.isSuccess()) {
                        convertedContent = sf.getContent();
                    } else {
                        XoaBizErrorBean errorBean = bizErrorListener.getReturn();
                        if (errorBean != null) {
                            logger.error(String.format("分享评论转换短链接失败 errorCode:%d, errorMessage:%s",
                                    errorBean.getErrorCode(), errorBean.getMessage()));
                        }
                        logger.error("分享评论调用短链接XOA失败 ServiceMessage:"
                                + ServiceFutureHelper.getErrorMessage(sf));
                        return content;
                    }
                } else { // timeout
                    logger.error("分享评论调用短链接XOA超时 InvocationInfo: "
                            + ServiceFutureHelper.getInvocationInfo(sf));
                    return content;
                }
            } catch (Throwable e) {
                logger.error("分享评论调用短链接服务失败 message: " + e.getMessage(), e);
                return content;
            }
            return convertedContent;
        }
        // 不转shortUrl，直接返回
        return content;
    }
    
    public static int getShareFeedType(int shareType,Entry entry,CommentStrategy strategy){
    	
    	if(entry == null){
    		return 0;
    	}
    	
    	try {
	    	Integer feedType = getFeedType(shareType,strategy,entry.getOwnerId());
	        //如果分享者是page或者被分享源是page，则使用page的新鲜事定义
	        if (G.isTypeOf(entry.getOwnerId(), Type.PAGE) || isSharePageXOAType(shareType)) {
	            feedType = getPageFeedType(entry.getOwnerId(), shareType,strategy);
	        }
	
	        //temp: FIXME page上传单张新鲜事的临时方案，需要删除这一段 
	        if (shareType == 2
	                && G.isTypeOf(Integer.valueOf(entry.getEntryProps().get("resource_user_id")),
	                        Type.PAGE) && !G.isTypeOf(entry.getOwnerId(), Type.PAGE)) {
	            feedType = 2004;
	        }
	        // end temp
	        return feedType != null ? feedType : 0;
    	} catch (Exception e){
    		logger.error("getShareFeedType error", e);
    	}
    	return 0;
    }
    
    /**
     * @param entryOwnerId
     * @return
     * 
     * shit
     */
    private static int doSpecialNeed(int shareType,int entryOwnerId){
    	int feedType = 0;
    	if(shareType == IShareConstants.URL_TYPE_LINK && entryOwnerId == 820873483){
    		// for bibi
    		feedType = 126;
    	}
    	return feedType;
    }

}
