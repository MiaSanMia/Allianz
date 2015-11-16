/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.json.JSONObject;

import com.renren.api.realtime.xoa.client.RealTimeObjectFactory;
import com.renren.app.share.IShareConstants;
import com.renren.app.share.model.Share;
import com.renren.app.share.model.ShareContent;
import com.renren.app.share.model.Url;
import com.renren.app.share.model.WarpMapResult;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.commons.tools.at.UGCCommonUtils;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.platform.business.client.service.SyncPromptService;
import com.xiaonei.platform.component.application.sync.model.SyncResponseBean;
import com.xiaonei.platform.component.application.sync.service.SyncServiceForShare;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.adminPermission.AdminManager;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

/**
 * Descriptions of the class ShareUtil.java's implementation：TODO described
 * the implementation of class
 * 
 * @author xiaoqiang 2013-9-13 上午10:43:02
 */
public class ShareUtil {

    private static final int MILLIS_TO_SEC = 1000;

    private final static String REGEX = "http://([^/]*)/(.*?)$";

    private final static Pattern PATTERN = Pattern.compile(REGEX);

    private static HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();

    private static HttpClient httpClient = new HttpClient(httpConnectionManager);

    /*
     * 根据2012/06/12的数据，总分享数7819302, 56视频分享数有783851, 每秒峰值约为
     * 783851/10/3600*5/12=9.07(除以10大约是小时峰值，乘以2.5是秒峰值,12是6台web(4台backup),6台xoa)
     * 既然比默认值小，那就把tocal和perhost都设成默认值(20)好了。
     */
    private static final int MAX_CONNECTION_NUMBER = 20;

    static {
        HttpConnectionManagerParams params = httpConnectionManager.getParams();
        params.setMaxTotalConnections(MAX_CONNECTION_NUMBER);
        params.setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION,
                MAX_CONNECTION_NUMBER);
        params.setConnectionTimeout(2000);
        params.setSoTimeout(2000);
    }

    //分享开发者
    public static Integer[] SHARE_DEVELOP_ID = { 136536176, 276578400 };

    /**
     * 发送评论到56的url，新发评论和删除评论都是这一个
     */
    private static final String POST_COMMENT_TO_WOLE_URL = "http://comment.56.com/new/api/renren/track_back.php";

    /**
     * 原始56视频的url
     */
    private static final String PARAM_KEY_ORIGIN_URL = "origin";

    /**
     * 56的url返回的json格式的结果中，是否有error的key
     */
    private static final String RESULT_KEY_ERROR = "error";

    /**
     * 评论id(在分享这边的数据库中的id)
     */
    private static final String PARAM_KEY_COMMENT_ID = "cmt_id";

    /**
     * 评论者的用户id
     */
    private static final String PARAM_KEY_COMMENT_UID = "cmt_uid";

    /**
     * 评论时间
     */
    private static final String PARAM_KEY_COMMENT_TIME = "cmt_time";

    /**
     * 评论者的用户名
     */
    private static final String PARAM_KEY_USER_NAME = "user_name";

    /**
     * 用于校验的签名
     */
    private static final String PARAM_KEY_SIGNATURE = "signature";

    /**
     * 评论内容
     */
    private static final String PARAM_KEY_COMMENT_CONTENT = "cmt_content";

    /**
     * 与56约定的私钥，以后可能是需要定期更新的。TODO 最好改到配置中
     */
    private static final String PRIVATE_KEY = "rr974561JHM125";
    
    /**
     * 是否是删除的标记的key
     */
    private static final String PARAM_KEY_DELETE_FLAG = "a";

    /**
     * 是否是删除的标记的value
     */
    private static final String PARAM_VALUE_DELETE = "del";

    /**
     * 发送评论到56时用于签名的值的key
     */
    private static String[] SIGNATURE_KEYS_ADD_COMMENT = new String[] { PARAM_KEY_ORIGIN_URL,
            PARAM_KEY_COMMENT_UID, PARAM_KEY_COMMENT_TIME, PARAM_KEY_COMMENT_ID };

    /**
     * 发送删除评论到56时用于签名的值的key
     */
    private static String[] SIGNATURE_KEYS_DEL_COMMENT = new String[] { PARAM_KEY_ORIGIN_URL,
            PARAM_KEY_COMMENT_ID };

    private static SyncPromptService syncPromptService = ServiceFactories.getFactory().getService(
            SyncPromptService.class);

    protected ShareUtil() {
    }

    private static Log logger = LogFactory.getLog(ShareUtil.class);

    static List<Integer> list = new ArrayList<Integer>();

    /**
     * 拷贝Url的属性到Share
     * 
     * @param url
     * @return
     */
    public static Share makeShareFromUrl(Url url) {
        Share share = new Share();
        share.setUrl(url.getUrl());
        share.setUrlMd5(url.getMd5());
        share.setResourceId(url.getResourceId());
        share.setResourceUserId(url.getResourceUserId());
        share.setSummary(url.getSummary());
        share.setThumbUrl(url.getThumbUrl());
        share.setTitle(url.getTitle());
        share.setType(url.getType());
        share.setMeta(url.getMeta());
        if (url.getShareContent() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("含有东西:" + url.getShareContent().getContent());
            }
            share.setStatus(IShareConstants.SHARE_STATUS_HAS_CONTENT | share.getStatus());
            share.setShareContent(url.getShareContent());
        }

        // 如果是从Url构建的Shar一定是Orgin
        share.setOrgin(true);

        return share;
    }

    /**
     * 复制分享的主要数据，所有Share->Share的创建都要走这个入口，这里会处理源的关系
     * 
     * @param share
     * @return
     */
    public static Share makeShareFromShare(Share share) {
        Share copy = new Share();
        if (share.getSourceShareId() > 0) {
            // share不是源，直接继承
            copy.setSourceShareId(share.getSourceShareId());
            copy.setSourceUserId(share.getSourceUserId());
        } else {
            // share是源，保存关系
            copy.setSourceShareId(share.getId());
            copy.setSourceUserId(share.getUserId());
        }
        copy.setFromShareId(share.getId());
        copy.setFromUserId(share.getUserId());
        copy.setMeta(filterBrower(share.getMeta()));
        copy.setResourceId(share.getResourceId());
        copy.setResourceUserId(share.getResourceUserId());
        copy.setUrl(share.getUrl());
        copy.setSummary(share.getSummary());
        copy.setThumbUrl(share.getThumbUrl());
        copy.setTitle(share.getTitle());
        copy.setType(share.getType());
        if (share.getShareContent() != null) {
            copy.setStatus(IShareConstants.SHARE_STATUS_HAS_CONTENT);
            copy.setShareContent(share.getShareContent());
        }

        copy.setOrgin(false);

        return copy;
    }

    public static String filterBrower(String meta) {
        if ((meta != null) && meta.contains(IShareConstants.JSON_BROWSER)) {
            try {
                JSONObject json = new JSONObject(meta);
                json.remove(IShareConstants.JSON_BROWSER);
                return json.toString();
            } catch (ParseException e) {}
        }
        return meta;
    }

    /**
     * 是否内部url
     * 
     * @param url
     * @return
     */
    public static boolean isInternalUrl(String url) {
        return url.matches("http://[^\\.]*?\\.?(renren|kaixin)\\.com.*");
    }

    private static class WhRate {

        private int wigth;

        private double rate;

        public int getWigth() {
            return wigth;
        }

        public void setWigth(int wigth) {
            this.wigth = wigth;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

    }

    public static String FILTER_WRONG_HTML = "<.*\\.\\.\\.";

    /**
     * 把视频搞大
     * 
     * @param share
     */
    public static void warpShareWidth(Share share) {

        try {
            if ((share != null) && share.isVideo()) {
                int width = 610;
                double rate = 1.2d;
                if (share.getMetaMap() != null) {
                    if (share.getUrl() != null) {

                        WhRate wR1 = new WhRate();
                        wR1.setWigth(610);
                        wR1.setRate(1.224899598);

                        width = wR1.getWigth();
                        rate = wR1.getRate();
                    }
                }
                if (width > 610) {
                    width = 610;
                    rate = 1.2;
                }
                share.setWidth(width);
                share.setWidthHightRate(rate);
            } else if ((share != null)
                    && (share.getType() == IShareConstants.URL_TYPE_COOPERATIVE_DOUDING)) {
                share.setWidth(700);
                share.setWidthHightRate(1.2);
            }
        } catch (NumberFormatException e) {} catch (MalformedPerl5PatternException e) {}

    }

    /**
     * 截断过长描述，评论，标题
     * 
     * @param share
     * @return
     */
    public static Share getAbbreviateShare(Share share) {
        if (logger.isDebugEnabled()) {
            logger.debug("获取分享id：" + share.getId());
        }
        if (StringUtils.length(share.getSummary()) > 200) {
            if (logger.isInfoEnabled()) {
                logger.info("截短过长的描述：" + share.getSummary());
            }
            share.setSummary(StringUtils.abbreviate(share.getSummary(), 200));
        }
        if (StringUtils.length(share.getComment()) > 200) {
            if (logger.isInfoEnabled()) {
                logger.info("截短过长的评论：" + share.getComment());
            }
            share.setComment(StringUtils.abbreviate(share.getComment(), 200));
        }

        if (StringUtils.length(share.getTitle()) > 100) {
            if (logger.isDebugEnabled()) {
                logger.info("截短过长的标题：" + share.getTitle());
            }
            share.setTitle(StringUtils.abbreviate(share.getTitle(), 100));
        }
        return share;
    }

    /**
     * 处理分享附加内容
     * 
     * @param share
     * @return
     */
    public static ShareContent dealWithShareContent(Share share) {
        ShareContent shareContent = share.getShareContent();
        if (shareContent != null) {
            shareContent.setId(share.getId());
            shareContent.setUserId(share.getUserId());
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("分享有附加内容，保存：id=%d, user=%d, content=%s",
                        shareContent.getId(), shareContent.getUserId(), shareContent.getContent()));
            }
        }
        return shareContent;
    }

    /**
     * 生成分享之前的分享
     * 
     * @param share
     * @return
     */
    public static Share getPreShare(Share share) {
        if (share != null) {
            long formerShareId = share.getFromShareId();
            int formerShareUserId = share.getFromUserId();
            if ((formerShareId > 0) && (formerShareUserId > 0)) {
                Share preShare = ShareUtil.makeShareFromShare(share);
                preShare.setId(formerShareId);
                preShare.setUserId(formerShareUserId);
                return preShare;
            }
        }
        return null;
    }

    /**
     * 获取用户信息
     * 
     * @param userId
     * @return
     */
    public static User getUser(int userId) {
        User user = null;
        try {
            user = WUserAdapter.getInstance().get(userId);
        } catch (Exception e) {
            logger.warn("获取用户信息失败:" + e.getMessage());
        }

        if (user == null) {
            try {
                user = WUserAdapter.getInstance().get(userId);
            } catch (Exception e) {
                logger.warn("获取用户信息失败:" + e.getMessage());
            }
        }
        if (user == null) {
            logger.warn("未取到用户信息 userId:" + userId);
        }
        return user;

    }

    public static String getDomainURI(String url) {
        try {
            Matcher matcher = PATTERN.matcher(url);
            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                return "";
            }
        } catch (Exception e) {
            logger.warn("正则匹配异常:" + e.getMessage());
            return "";
        }
    }

    /**
     * 判断是否是管理员
     * 
     * @param host
     * @return
     */
    public static boolean isAdmin(int host) {
        try {
            return AdminManager.getInstance().hasRight(host, 202, 4);
        } catch (Exception e) {
            logger.warn("判断管理员失败: " + host + "  errorMsg:" + e.getMessage());
            return false;
        }
    }

    /**
     * 序列化对象<br/>
     * 
     * @see 用于log的输出，注意此类对象一定是可序列化，<br/>
     *      最好是纯javaBean，否则返回为空串
     * 
     * @param o
     * @return
     */
    public static String writeObjectAsJson(Object o) {
        return UGCCommonUtils.writeObjectAsJson(o);
    }

    /**
     * 把warpResultMap转换成ShareComment的 Map<String,Object><br/>
     * 为了兼容老接口
     * 
     * @param warpMapResult
     * @return
     */
    public static Map<String, Object> covertWarpResultMap2ShareCommentMap(
            WarpMapResult warpMapResult) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", warpMapResult.getShareComments());
        map.put("hasMore", warpMapResult.isHasMore());
        map.put("listShareComment", warpMapResult.getShareComments());
        map.put("commentMore", warpMapResult.isHasMore());
        return map;
    }

    /**
     * 把warpResultMap转换成UrlComment的Map<String,Object><br/>
     * 为了兼容老接口
     * 
     * @param warpMapResult
     * @return
     */
    public static Map<String, Object> covertWarpResultMap2UrlCommentMap(WarpMapResult warpMapResult) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (warpMapResult.getUrlComments() != null) {
            map.put("list", warpMapResult.getUrlComments());
            map.put("hasMore", warpMapResult.isHasMore());
            map.put("listShareComment", warpMapResult.getUrlComments());
            map.put("commentMore", warpMapResult.isHasMore());
        }
        return map;
    }

    /**
     * 处理分享的评论，去掉换行等字符
     * 
     * @param share
     */
    public static void dealShareComment(Share share) {

        if ((share != null) && (share.getComment() != null)) {
            share.setComment(share.getComment().replaceAll("\n", "").replaceAll("\r", ""));
        }

    }

    /**
     * 分享截字
     * 
     * @param str
     * @param length
     * @return
     */
    public static String abbreviate(String str, int length) {
        if (StringUtils.isNotBlank(str)) {
            return StringUtils.abbreviate(str, length + 3);
        } else {
            return str;
        }
    }

    //TODO 
    public static boolean hasShareForward(Share share) {

        if ((share != null)
                && ((IShareConstants.URL_TYPE_BLOG == share.getType())
                        || (IShareConstants.URL_TYPE_PHOTO == share.getType())
                        || (IShareConstants.URL_TYPE_LINK == share.getType())
                        || (IShareConstants.URL_TYPE_ALBUM == share.getType())
                        || (IShareConstants.URL_TYPE_VIDEO == share.getType()) || (IShareConstants.URL_TYPE_STATUS == share.getType()
                //|| IShareConstants.URL_TYPE_COMMODITY_TAOBAO == share.getType()
                ))) {
            return true;
        }
        return false;
    }

    private static final HashSet<Integer> NEW_FEED_TEMPLATE_TYPES = new HashSet<Integer>();
    static {
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_BLOG);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_PHOTO);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_LINK);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ALBUM);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_VIDEO);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_STATUS);
        //NEW_FEED_TEMPLATE_TYPE.add(IShareConstants.URL_TYPE_COMMODITY_TAOBAO);

        // new added for xiaozhan's share
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZHAN_BLOG);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZHAN_PHOTO);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZHAN_LINK);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZHAN_VIDEO);
        //NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZHAN_AUDIO);//TODO 小站暂时不打算更新音乐

        // added for 小组
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_NEW_FORUM);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_NEW_THREAD);

        // added for 音乐
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_NEW_AUDIO);

        // added for 足迹
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZUJI);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_ZUJI_PHOTO);

        // added for wiki电影简评
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_MOVIE_SUMMARY);

        // added for 校招
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_XIAOZHAO);

        // added for招聘日程 & 提问
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_XIAOZHAO_SCHEDULE);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_XIAOZHAO_INTERVIEW);
        
        //校园论坛
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_CAMPUS_POST);
        NEW_FEED_TEMPLATE_TYPES.add(IShareConstants.URL_TYPE_CAMPUS_IMGPOST);
        
    }

    private static Set<Integer> outterNewFeedTemplateTypes = new HashSet<Integer>();

    // 外部配置文件，share type和分享type的对应关系
    private static final String OUTTER_NEW_FEED_TEMPLATE_TYPES_FILE_NAME = "/new_feed_template_types.properties";

    /**
     * 是否使用新的新鲜事系统
     * 
     * @param shareType 分享类型, see {@link IShareConstants}
     * @return
     */
    public static boolean useNewFeed(int shareType) {
        return NEW_FEED_TEMPLATE_TYPES.contains(shareType)
                || outterNewFeedTemplateTypes.contains(shareType);
    }

    private static final Set<Integer> NEW_EDM_SHARE_TYPES = new HashSet<Integer>(3);
    static {
        NEW_EDM_SHARE_TYPES.add(IShareConstants.URL_TYPE_NEW_EDM_PIC);
        NEW_EDM_SHARE_TYPES.add(IShareConstants.URL_TYPE_NEW_EDM_TEXT);
        NEW_EDM_SHARE_TYPES.add(IShareConstants.URL_TYPE_NEW_EDM_VIDEO);
    }

    /**
     * 是不是新的广告类型。<br>
     * 新的广告类型(53,54,55)不再由广告自己往分享的url表里面插数据，而是要每次都插入或更新。
     * 
     * @param type
     * @return
     */
    public static boolean isNewEdmShareType(final int type) {
        return NEW_EDM_SHARE_TYPES.contains(type);
    }

    /**
     * 安全中心删除日志，照片等源时，想把相应的分享也给删掉。这里定义了安全指定的类型。
     */
    private static final Set<Integer> INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE = new HashSet<Integer>(3);
    static {
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_BLOG);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_PHOTO);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_ALBUM);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_PAGE_BLOG);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_PAGE_PHOTO);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_PAGE_ALBUM);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_NEW_THREAD);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_ZHAN_BLOG);
        INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.add(IShareConstants.URL_TYPE_ZHAN_PHOTO);
    }

    /**
     * 是不是安全指定的，删除源时也要删除相应分享的分享类型。<br>
     * 
     * @param type 分享类型, refer: {@link IShareConstants}中以URL_TYPE_开头的常量。
     * @return true: 是指定类型；false：不是置顶类型。
     */
    public static boolean isInternalShareTypesForSafeDelete(final int type) {
        return INTERNAL_SHARE_TYPES_FOR_SAFE_DELETE.contains(type);
    }

    /**
     * 获取url的cache key，仅仅为安全中心删除搜索中的所有分享。
     * 
     * @param type 分享类型
     * @param resourceUserId 源创建者id
     * @param resourceId 源id
     * 
     * @return url的cache key
     */
    public static final String getUrlKeyForSafeDelete(final int type, final int resourceUserId,
            final long resourceId) {
        return new StringBuilder("internal-").append(type).append("-").append(resourceUserId).append(
                "-").append(resourceId).toString();
    }

    /**
     * 用户是否同意了通论互通
     * 
     * @param userId
     * @return
     */
    public static boolean agreeCommentTreaty(int userId, String url) {
        try {
            if (userId == 0) {
                return false;
            }
            SyncResponseBean syncResponseBean = SyncServiceForShare.hasPermissonForCommentSync(
                    userId, url);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("参数结果：userId:%s, url:%s, hasPermission:%s ", userId,
                        url, syncResponseBean.hasPermission()));
            }
            return syncResponseBean == null ? false : syncResponseBean.hasPermission();
        } catch (Exception e) {
            logger.error(
                    String.format("获取是否同意评论互通异常：userId:%s, url:%s  ", userId, url) + e.getMessage(),
                    e);
            return false;
        }

    }

    /**
     * 增加评论 目前是直接评论 评论视频
     * 
     * @param shareId
     * @param shareUserId
     * @param commentId
     * @param userId
     * @param share
     * @param comment
     */
    public static void addShareComment(long shareId, int shareUserId, long commentId, int userId,
            Entry share, Comment comment) {

        int shareType = Integer.valueOf(share.getEntryProps().get(EntryConfig.ENTRY_SHARE_TYPE));
        if (logger.isInfoEnabled()) {
            logger.info("shareId" + shareId + "|| share.getType:" + shareType);
        }

        String shareUrl = share.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
        // 如果没同意互通评论或者不是视频类型的则直接返回
        if (!agreeCommentTreaty(userId, share.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL))
                || IShareConstants.URL_TYPE_VIDEO != shareType) {
            logger.warn(String.format("参数：userId:%s, url:%s ", userId,
                    share.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL)));
            return;
        }
        // 判断是直接评论而不是回复，在sharecomment里面加字段
        if (comment.getToUserId() > 0) {
            return;
        }
        
        //原有的ShareComment中externalParamMap会发给其他业务，目前无法获得其中数据，所以new一个空map comment.getExternalParamMap());
        long begin = System.currentTimeMillis();
                RealTimeObjectFactory.getShareService().increaseComment(shareUrl,
                        shareId, commentId, shareUserId, userId, new HashMap<String, Object>());
                        
        long end = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info(String.format(
                    "shareId: %s || shareUserId: %s || commentId: %s || userId: %s || addShareComment cost:%d",
                    shareId, shareUserId, commentId, userId, (end - begin)));
        }

    }
    
    /**
     * 删除分享评论时 目前是直接评论 评论视频
     * 
     * @param share
     * @param userId
     * @param ownerId
     * @param ugcId
     */
    public static void removeShareComment(long shareId, int shareUserId,
            long commentId, int userId, Entry share, Comment comment) {
        
        int shareType = Integer.valueOf(share.getEntryProps().get(EntryConfig.ENTRY_SHARE_TYPE));
        String shareUrl = share.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);

        if (logger.isInfoEnabled()) {
            logger.info("shareId" + shareId + "|| shareType:"
                    + shareType);
        }
        // 56视频回复的评论不调用，目前修改这部分逻辑为调用
        if (comment !=null && comment.getContent().startsWith("回复:")) {
            return;
        }
        long begin = System.currentTimeMillis();
        //原有的ShareComment中externalParamMap会发给其他业务，目前无法获得其中数据，所以new一个空map comment.getExternalParamMap());
        RealTimeObjectFactory.getShareService().decreaseComment(shareUrl,
                shareId, commentId, shareUserId, userId,
                new HashMap<String, Object>());
        long end = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger
                    .info(String
                            .format(
                                    "shareId: %s || shareUserId: %s || commentId: %s || userId: %s || removeShareComment cost:%d",
                                    shareId, shareUserId, commentId, userId,
                                    (end - begin)));
        }
    }

    /**
     * 是否在终端页显示提示用户的信息，提示的信息是告诉用户他们的评论会被同步到优酷等网站
     * 
     * @param userId
     * @param share
     * @return
     */
    public static boolean showInterflowCommentPrompt(int userId, Entry share) {
        int shareType = Integer.valueOf(share.getEntryProps().get(EntryConfig.ENTRY_SHARE_TYPE));
        String shareUrl = share.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);

        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format(
                        "参数结果： userId:%s, url:%s, showInterflowCommentPrompt:%s ", userId,
                        shareUrl, SyncServiceForShare.showPrompt(userId, shareUrl)));
            }
            if ((userId != 0) && (share != null) && (IShareConstants.URL_TYPE_VIDEO == shareType)
                    && SyncServiceForShare.showPrompt(userId, shareUrl)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(String.format("获取是否可以评论互通异常：url:%s   ", shareUrl) + e.getMessage(), e);
            return false;
        }

    }

    /**
     * 通知开发平台这个用户已经回复过这个url了
     * 
     * @param userId
     * @param url
     */
    public static void notifyPlatform(int userId, String url) {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("userId=%s,url=%s", userId, url));
        }
        try {
            syncPromptService.notifyForComment(userId, url).submit();
        } catch (Exception e) {
            logger.error("开放平台评论通知notifyPlatform异常：" + e.getMessage(), e);
        }
    }

    /**
     * 56视频的url正则，当前适用。(2012/04/09)
     * 
     */
    private static final Pattern WOLE_VIDEO_URL_PATTERN = Pattern.compile("^http://www.56.com/[u|p|w]\\d+/.*?\\.html");

    /**
     * 判断某url是否是56视频。
     * 
     * @param url 链接地址
     * @return true表示是56视频地址，false表示不是。
     */
    public static boolean isWoleVideo(final String url) {
        Matcher matcher = WOLE_VIDEO_URL_PATTERN.matcher(url);
        return matcher.lookingAt();
    }

    /**
     * 发送视频分享的评论到56.
     * 
     * @param entry
     * @param comment
     */
    public static void addShareComment(final Entry entry, final Comment comment) {
        String shareUrl = entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
        int shareType = Integer.valueOf(entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_TYPE));
        if (logger.isInfoEnabled()) {
            logger.info(String.format("send share comment to 56, share.id:%d, share.userId:%d,"
                    + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                    entry.getOwnerId(), shareUrl, comment.getId(), comment.getAuthorId()));
        }

        if ((IShareConstants.URL_TYPE_VIDEO == shareType && !isWoleVideo(shareUrl))
                || (comment.getToUserId() != 0)) {
            logger.warn(String.format("wrong call,share.id:%d, share.userId:%d,"
                    + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                    entry.getOwnerId(), shareUrl, comment.getId(), comment.getAuthorId()));
            return;
        }

        PostMethod postMethod = new PostMethod(POST_COMMENT_TO_WOLE_URL);
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.addParameter(PARAM_KEY_ORIGIN_URL, shareUrl);
        postMethod.addParameter(PARAM_KEY_COMMENT_UID, String.valueOf(comment.getAuthorId()));
        postMethod.addParameter(PARAM_KEY_USER_NAME, comment.getAuthorName());
        postMethod.addParameter(PARAM_KEY_COMMENT_TIME,
                String.valueOf(comment.getCreatedTime() / MILLIS_TO_SEC));//换成以秒为单位的时间戳，56要求
        postMethod.addParameter(PARAM_KEY_COMMENT_CONTENT, comment.getContent());
        postMethod.addParameter(PARAM_KEY_COMMENT_ID, String.valueOf(comment.getId()));

        parseAndSetSignature(postMethod, SIGNATURE_KEYS_ADD_COMMENT);

        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_BAD_GATEWAY) {
                statusCode = httpClient.executeMethod(postMethod);
            }

            String result = postMethod.getResponseBodyAsString();
            if (logger.isInfoEnabled()) {
                logger.info(String.format(
                        "send share comment to 56 with return, statusCode:%d, share.id:%d, share.userId:%d,"
                                + " url:%s, comment.id:%d, comment.userId:%d, result:%s",
                        statusCode, entry.getId(), entry.getOwnerId(), shareUrl, comment.getId(),
                        comment.getAuthorId(), result));
            }

            if (statusCode == HttpStatus.SC_OK) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (!json.has(RESULT_KEY_ERROR) || json.getInt(RESULT_KEY_ERROR) != 0) {
                        logger.error(String.format(
                                "send share comment to 56 failed, share.id:%d, share.userId:%d,"
                                        + " url:%s, comment.id:%d, comment.userId:%d, result:%s",
                                entry.getId(), entry.getOwnerId(), shareUrl, comment.getId(),
                                comment.getAuthorId(), result));
                    }
                } catch (ParseException e) {
                    logger.error(String.format(
                            "return code 200 but result is not json, result is:%s", result), e);
                }
            }
        } catch (Exception e) {
            logger.error(
                    String.format("send share comment to 56 failed, share.id:%d, share.userId:%d,"
                            + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                            entry.getOwnerId(), shareUrl, comment.getId(), comment.getAuthorId()),
                    e);
        } finally {
            postMethod.releaseConnection();
        }
    }

    /**
     * 按照顺序从<code>postMethod</code>中获取<code>keys</code>中每个key所对应的值，全部连接在一起,<br>
     * 最后加上约定好的私钥，进行md5计算的值就是签名，并放置到<code>postMethod</code>中。
     * 
     * @param postMethod 已经设定好了其他参数的PostMethod对象
     * @param keys key的数组
     */
    private static final void parseAndSetSignature(final PostMethod postMethod, final String[] keys) {
        StringBuffer strBuffer = new StringBuffer();
        for (String key : keys) {
            strBuffer.append(postMethod.getParameter(key).getValue());
        }
        strBuffer.append(PRIVATE_KEY);
        postMethod.addParameter(PARAM_KEY_SIGNATURE, DigestUtils.md5Hex(strBuffer.toString()));
    }

    /**
     * 发送删除视频分享评论的消息到56.
     * 
     * @param entry 分享的实体
     * @param comment 删除的评论
     */
    public static void removeShareComment(Entry entry, Comment comment) {
        String shareUrl = entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_URL);
        int shareType = Integer.valueOf(entry.getEntryProps().get(EntryConfig.ENTRY_SHARE_TYPE));
        if (logger.isInfoEnabled()) {
            logger.info(String.format(
                    "send delete share comment to 56, share.id:%d, share.userId:%d,"
                            + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                    entry.getOwnerId(), shareUrl, comment.getId(),
                    comment.getAuthorId()));
        }

        if ((IShareConstants.URL_TYPE_VIDEO == shareType && !isWoleVideo(shareUrl))
                || (comment.getToUserId() != 0)) {
            logger.warn(String.format("wrong call,share.id:%d, share.userId:%d,"
                    + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                    entry.getOwnerId(), shareUrl, comment.getId(), comment.getAuthorId()));
            return;
        }
        
        PostMethod postMethod = new PostMethod(POST_COMMENT_TO_WOLE_URL);
        postMethod.getParams().setContentCharset("UTF-8");

        postMethod.addParameter(PARAM_KEY_DELETE_FLAG, PARAM_VALUE_DELETE);
        postMethod.addParameter(PARAM_KEY_ORIGIN_URL, shareUrl);
        postMethod.addParameter(PARAM_KEY_COMMENT_ID, String.valueOf(comment.getId()));

        parseAndSetSignature(postMethod, SIGNATURE_KEYS_DEL_COMMENT);

        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_BAD_GATEWAY) {
                statusCode = httpClient.executeMethod(postMethod);
            }
            
            String result = postMethod.getResponseBodyAsString();
            if (logger.isInfoEnabled()) {
                logger.info(String.format(
                        "send share comment delete to 56, statusCode:%d, share.id:%d, share.userId:%d,"
                                + " url:%s, comment.id:%d, comment.userId:%d, result:%s",
                        statusCode, entry.getId(), entry.getOwnerId(), shareUrl,
                        comment.getId(), comment.getAuthorId(), result));
            }

            if (statusCode == HttpStatus.SC_OK) {
                JSONObject json = new JSONObject(result);
                if (!json.has(RESULT_KEY_ERROR) || json.getInt(RESULT_KEY_ERROR) != 0) {
                    logger.error(String.format(
                            "send delete share comment to 56 failed, share.id:%d, share.userId:%d,"
                                    + " url:%s, comment.id:%d, comment.userId:%d, result:%s",
                            entry.getId(), entry.getOwnerId(), shareUrl, comment.getId(),
                            comment.getAuthorId(), result));
                }
            }

        } catch (Exception e) {
            logger.error(String.format(
                    "send delete share comment to 56 failed, share.id:%d, share.userId:%d,"
                            + " url:%s, comment.id:%d, comment.userId:%d", entry.getId(),
                    entry.getOwnerId(), shareUrl, comment.getId(),
                    comment.getAuthorId()), e);
        } finally {
            postMethod.releaseConnection();
        }
        
    }

}
