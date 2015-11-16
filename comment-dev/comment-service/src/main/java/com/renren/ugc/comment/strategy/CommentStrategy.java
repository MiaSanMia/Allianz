package com.renren.ugc.comment.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.CommentCacheService;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.service.CommentLogic;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.model.album.PhotoWith;

/**
 * The context object which is used by Comment Logic code and interceptors.
 * Strategy can record the runtime configuration, biz related parameters and
 * dynamically bound comment logics or entry logics. With strategy, one
 * interceptor has a chance to "tell" another interceptor some information (e.g.
 * <code>AntispamInterceptor</code> can tell <code>AuditInterceptor</code>
 * whether or not to do audit.
 * 
 * @author jiankuan.xing
 */
/**
 * @author wangxx
 *
 */
public class CommentStrategy {

    /**
     * whether to publish comment creation notification (提醒)
     */
    private Boolean sendNotice = null;

    private String emailTitle;

    private Map<String, String> params;

    private List<Integer> additionalReplyTo;

    private boolean filterWhisper = true;

    private Integer feedType = null;

    private Boolean replaceUbb = null;

    /**
     * ubb大表情的替换
     */
    private Boolean replaceUbbLarge = null;

    private Boolean replaceAt = null;

    private Boolean replaceShortUrl = null;

    private int queryCount;

    private int queryOffset;

    private long queryBorderID;

    private QueryOrder queryOrder = QueryOrder.DESC;

    private CommentLogic commentLogic;

    private CommentConfig config;

    private Integer maxContentLength;

    private Boolean trimWhenContentTooLong;

    /**
     * Whether to do on-line audit (used in AuditIntercetpor)
     */
    private Boolean shouldAudit;

    /**
     * Whether this is an internal invocation to Comment Logic so that all the
     * interceptors are disabled
     */
    private boolean internal;

    /**
     * client's ip address
     */
    private String userIp;

    /**
     * the sourceId used by "新鲜事"
     */
    private long sourceId;

    /**
     * the external id of comment passed by invoker
     */
    private long externalId;

    /**
     * whether to return full head url
     */
    private Boolean returnFullHeadUrl;

    /**
     * the value returned by comment logic implementation
     */
    private Object returnedValue;

    /**
     * the cache service
     */
    private CommentCacheService commentCacheService;

    /**
     * Whether to disable all the cache when invoke comment center api
     */
    private Boolean cacheDisabled;

    /**
     * Whether the invoker need comments' count when create/remove/recover a
     * comment
     */
    private Boolean needCount;

    /**
     * Whether the invoker need comments' likeInfo when create/remove/recover a
     * comment
     */
    private Boolean needLikeInfo;

    /**
     * Whether the invoker need comments' voice when create/remove/recover a
     * comment
     */
    private Boolean needVoiceInfo;

    /**
     * 由于调用第三方接口,加上pageNum
     */
    private int pageNum;

    /**
     * @相关的人，然后由@拦截器传给“与我相关”拦截器
     */
    private List<Integer> atIdLists;

    /**
     * 如果评论有关联，这里存储一些关联信息
     */
    private List<CommentLinkedInfo> commentLinkedInfos;

    /**
     * 被评论的实体
     */
    private Entry entry;

    /**
     * 向与我相关发送notify,是否实时发送
     */
    private boolean isAllowRealTimeNotify;

    /**
     * 是否需要向feed发送notify, 默认为true
     */
    private boolean isFeedDispatch = Boolean.TRUE;;

    /**
     * 异步评论插入的metaData，因为需要在{@link:AsynCreateCommentInterceptor} 中用到，但是这个属性没有在
     * {@link : Comment#toInterfaceComment()}方法中传出去
     */
    private Metadata metadata;

    /**
     * photo with信息，因为很难放在entry的map<String,String>中，只能通过在CommentStrategy中传递
     */
    private List<PhotoWith> photoWiths;

    /**
     * 是否需要metadata字段
     */
    private boolean needMetadata;

    /**
     * 是否是照片的zuji评论
     */
    private boolean isZujiComment;

    /**
     * 是否是照片的圈图评论
     */
    private boolean isPhotoTagComment;

    /**
     * Whether double-write to url_comment table for "Glboal Comment"
     */
    private Boolean needGlobal;

    /**
     * 是否是回复产生的关联评论
     */
//    private boolean isReplyLinked;

    /**
     * 当读取评论(比如获取评论列表时),是否由评论中心调用getEntry接口以强制检查Entry的隐私
     */
    private boolean getEntryWhenRead;

    /**
     * 该条评论是否需要进行安全检查
     */
    private Boolean needAntispam = Boolean.TRUE;

    /**
     * 该条评论需要发送@信息
     */
    private Boolean needSendAtInfo = Boolean.TRUE;

    /**
     * 该条评论需要发送好友评论提醒
     */
    private Boolean needSendFriendNotify = Boolean.TRUE;

    /**
     * 查询的"最老评论"的数量
     */
    private int oldestCount;

    /**
     * 查询的"最新评论"的数量
     */
    private int latestCount;

    /**
     * feed ownerid
     */
    private int feedOwnerId;

    /**
     * entry的密码，现在是明文传输
     */
    private String entryPassword;

    /**
     * 删除的comment, 当是删除操作时，该值是删除的comment, 其他操作该值为null
     */
    private Comment comment;
    
    /**
     * 原始的查询limit
     */
    private int originalQueryLimit;
    
    /**
     * 是否要求评论的内容为originalContent
     */
    private boolean isOriginalContentQuery;
    
    /**
     * 评论来源
     */
    private int isFrom;
    
    /**
     * 父entryId,比如对于虚拟子相册来说，就是父相册的Id
     */
    private long parentEntryId;
    
    /**
     * 是否需要取简单的信息
     */
    private boolean needSimpleInfo;
    
    /**
     * 是否需要返回"这条评论之前有多少信息"
     */
    private boolean needHasPre;
    
    /**
     * 是否需要解析metadata字段
     */
    private Boolean needMetadataResolve;
    
    /**
     * 是否需要替换作者信息，true的话，评论中心会保存每次"创建评论的当时的作者"的名字和头像，当取评论的时候，就为"创建评论的当时的作者"的名字和头像
     */
    private boolean saveAuthorInfo;
    
    /**
     * 校园主页的"学校id"
     */
    private int schoolId = 0;

    public CommentStrategy(){

    }

    public CommentStrategy setSourceId(long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public CommentLogic getCommentLogic() {
        return commentLogic;
    }

    public CommentStrategy setCommentLogic(CommentLogic commentLogic) {
        this.commentLogic = commentLogic;
        return this;
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getUserIp() {
        return userIp;
    }

    public CommentStrategy setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public boolean isFilterWhisper() {
        return filterWhisper;
    }

    public CommentStrategy setFilterWhisper(boolean filterWhisper) {
        this.filterWhisper = filterWhisper;
        return this;
    }

    public int getFeedType() {
        if (feedType == null) {
            if (config == null) {
                return 0;
            } else {
                return config.getFeedType();
            }
        } else {
            return feedType;
        }
    }

    public CommentStrategy setFeedType(int feedType) {
        this.feedType = feedType;
        return this;
    }

    public boolean isReplaceUbb() {
        if (replaceUbb == null) {
            if (config == null) {
                return false;
            } else {
                return config.isReplaceUbb();
            }
        }
        return replaceUbb;
    }

    public CommentStrategy setReplaceUbb(boolean replaceUbb) {
        this.replaceUbb = replaceUbb;
        return this;
    }

    public int getQueryLimit() {
        return queryCount;
    }

    public CommentStrategy setQueryLimit(int queryCount) {
        this.queryCount = queryCount;
        return this;
    }

    public int getQueryOffset() {
        return queryOffset;
    }

    public CommentStrategy setQueryOffset(int queryOffset) {
        this.queryOffset = queryOffset;
        return this;
    }

    public long getQueryBorderID() {
        return queryBorderID;
    }

    public CommentStrategy setQueryBorderID(long queryBorderID) {
        this.queryBorderID = queryBorderID;
        return this;
    }

    public QueryOrder getQueryOrder() {
        return queryOrder;
    }

    public CommentStrategy setQueryOrder(QueryOrder queryOrder) {
        this.queryOrder = queryOrder;
        return this;
    }

    public boolean isSendNotice() {
        if (sendNotice == null) {
            if (this.getConfig() != null) {
                return this.getConfig().isSendNotice();
            } else {
                return false;
            }
        }
        return sendNotice;
    }

    public CommentStrategy setSendNotice(boolean sendNotice) {
        this.sendNotice = sendNotice;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public CommentStrategy setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public CommentStrategy addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put(key, value);
        return this;
    }

    public List<Integer> getAdditionalReplyTo() {
        return additionalReplyTo;
    }

    public CommentStrategy setAdditionalReplyTo(List<Integer> additionalReplyTo) {
        this.additionalReplyTo = additionalReplyTo;
        return this;
    }

    public void additionalReplyTo(int id) {
        if (additionalReplyTo == null) {
            additionalReplyTo = new ArrayList<Integer>();
        }
        additionalReplyTo.add(id);
    }

    public CommentConfig getConfig() {
        return config;
    }

    public void setConfig(CommentConfig config) {
        this.config = config;
    }

    public boolean isUseFakeEntryOwnerId() {
        if (this.config == null) {
            return false;
        }

        return config.isUseFakeEntryOwnerId();
    }

    public int getFeedOwnerId() {
        return feedOwnerId;
    }

    public void setFeedOwnerId(int feedOwnerId) {
        this.feedOwnerId = feedOwnerId;
    }

    public String getEntryPassword() {
        return entryPassword;
    }

    public void setEntryPassword(String entryPassword) {
        this.entryPassword = entryPassword;
    }

    @Override
    public String toString() {
        StringBuilder desc = new StringBuilder();

        desc.append("isSendFeed:\t");
        desc.append(sendNotice);
        desc.append("\n");

        desc.append("emailTitle:\t");
        desc.append(emailTitle == null ? "NULL" : emailTitle);
        desc.append("\n");

        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                desc.append("properties");
                desc.append(key);
                desc.append("\t==>\t");
                desc.append(params.get(key));
            }
            desc.append("\n");
        } else {
            desc.append("properties is null.\n");
        }

        if (additionalReplyTo != null && additionalReplyTo.size() > 0) {
            desc.append("additionalReplyTo: \t");
            for (Integer to : additionalReplyTo) {
                desc.append(to);
                desc.append(",");
            }
        } else {
            desc.append("additionalReplyTo is null");
        }

        return desc.toString();
    }

    public boolean isInternal() {
        return internal;
    }

    public CommentStrategy setInternal(boolean internal) {
        this.internal = internal;
        return this;
    }

    public CommentStrategy setShouldAudit(boolean shouldAudit) {
        this.shouldAudit = shouldAudit;
        return this;
    }

    public boolean shouldAudit() {
        if (shouldAudit == null) {
            if (this.getConfig() != null) {
                return this.getConfig().shouldAudit();
            } else {
                return false;
            }
        }

        return shouldAudit;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public boolean isReturnFullHeadUrl() {
        if (returnFullHeadUrl == null) {
            if (this.getConfig() != null) {
                return this.getConfig().isReturnFullHeadUrl();
            } else {
                return false;
            }
        }
        return returnFullHeadUrl;
    }

    public boolean needCount() {
        if (needCount == null) {
            if (this.getConfig() != null) {
                return this.getConfig().needCount();
            } else {
                return false;
            }
        }
        return needCount;
    }

    public CommentStrategy setNeedCount(boolean needCount) {
        this.needCount = needCount;
        return this;
    }

    public CommentStrategy setReturnFullHeadUrl(boolean returnFullHeadUrl) {
        this.returnFullHeadUrl = returnFullHeadUrl;
        return this;
    }

    public Object getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(Object returnedValue) {
        this.returnedValue = returnedValue;
    }

    public CommentCacheService getCommentCacheService() {
        return commentCacheService;
    }

    public void setCommentCacheService(CommentCacheService commentCacheService) {
        this.commentCacheService = commentCacheService;
    }

    public Boolean isCacheDisabled() {
        return cacheDisabled;
    }

    public boolean isCacheEnabled() {
        if (cacheDisabled == null) {
            if (this.getConfig() != null) {
                return this.getConfig().isNeedCache();
            } else {
                return true;
            }
        }
        return !cacheDisabled;
    }

    public CommentStrategy setCacheDisabled(Boolean cacheDisabled) {
        this.cacheDisabled = cacheDisabled;
        return this;
    }

    public Boolean getSendNotice() {
        return sendNotice;
    }

    public void setSendNotice(Boolean sendNotice) {
        this.sendNotice = sendNotice;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public Boolean getReplaceUbb() {
        return replaceUbb;
    }

    public void setReplaceUbb(Boolean replaceUbb) {
        this.replaceUbb = replaceUbb;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public Boolean getShouldAudit() {
        return shouldAudit;
    }

    public void setShouldAudit(Boolean shouldAudit) {
        this.shouldAudit = shouldAudit;
    }

    public Boolean getReturnFullHeadUrl() {
        return returnFullHeadUrl;
    }

    public Boolean getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Boolean needCount) {
        this.needCount = needCount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setFeedType(Integer feedType) {
        this.feedType = feedType;
    }

    public boolean isNeedLikeInfo() {
        if (needLikeInfo == null) {
            if (this.getConfig() != null) {
                return config.isNeedLikeInfo();
            } else {
                return false;
            }
        }
        return needLikeInfo;
    }

    public boolean isNeedVoiceInfo() {
        if (needVoiceInfo == null) {
            if (this.getConfig() != null) {
                return config.isNeedVoiceInfo();
            } else {
                return false;
            }
        }
        return needVoiceInfo;
    }

    public boolean isReplaceAt() {
        if (replaceAt == null) {
            if (config == null) {
                return false;
            } else {
                return config.isReplaceAt();
            }
        }
        return replaceAt;
    }

    public boolean isReplaceShortUrl() {
        if (replaceShortUrl == null) {
            if (config == null) {
                return false;
            } else {
                return config.isReplaceShortUrl();
            }
        }
        return replaceShortUrl;
    }

    public List<Integer> getAtIdLists() {
        return atIdLists;
    }

    public void setAtIdLists(List<Integer> atIdLists) {
        this.atIdLists = atIdLists;
    }

    public Boolean getReplaceUbbLarge() {
        return replaceUbbLarge;
    }

    public void setReplaceUbbLarge(Boolean replaceUbbLarge) {
        this.replaceUbbLarge = replaceUbbLarge;
    }

    public boolean isReplaceUbbLarge() {
        if (replaceUbbLarge == null) {
            return false;
        }
        return replaceUbbLarge;
    }

    public List<CommentLinkedInfo> getCommentLinkedInfos() {
        return commentLinkedInfos;
    }

    public void setCommentLinkedInfos(List<CommentLinkedInfo> commentLinkedInfos) {
        this.commentLinkedInfos = commentLinkedInfos;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getUrlMd5() {
        if (this.getEntry() == null
            || this.getEntry().getEntryProps() == null
            || !this.getEntry().getEntryProps().containsKey(
                EntryConfig.ENTRY_URL_MD5)) {
            return "";
        }
        return this.getEntry().getEntryProps().get(EntryConfig.ENTRY_URL_MD5);
    }

    public boolean isTrimWhenContentTooLong() {
        if (trimWhenContentTooLong != null) {
            return trimWhenContentTooLong;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.isTrimWhenContentTooLong();
            }
        }
    }

    public boolean isNeedGlobal() {
        if (needGlobal != null) {
            return needGlobal;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.isNeedGlobal();
            }
        }
    }

    public CommentStrategy setNeedGlobal(boolean needGlobal) {
        this.needGlobal = needGlobal;
        return this;
    }

    public Boolean isNeedAntispam() {
        if (needAntispam != null) {
            return needAntispam;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.shouldCheckSpamContent();
            }
        }
    }

    public void setNeedAntispam(Boolean needAntispam) {
        this.needAntispam = needAntispam;
    }

    public Boolean getNeedSendAtInfo() {
        if (needSendAtInfo != null) {
            return needSendAtInfo;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.shouldNeedSendAtInfo();
            }
        }
    }

    public void setNeedSendAtInfo(Boolean needSendAtInfo) {
        this.needSendAtInfo = needSendAtInfo;
    }

    public int getMaxContentLength() {
        if (maxContentLength != null) {
            return maxContentLength;
        } else {
            if (config == null) {
                return CommentConfig.DEFAULT_MAX_CONTENT_LENGTH;
            } else {
                return config.getMaxContentLength();
            }
        }
    }

    public Metadata getMetaData() {
        return metadata;
    }

    public void setMetaData(Metadata metaData) {
        this.metadata = metaData;
    }

    public boolean isAllowRealTimeNotify() {
        return isAllowRealTimeNotify;
    }

    public void setAllowRealTimeNotify(boolean isAllowRealTimeNotify) {
        this.isAllowRealTimeNotify = isAllowRealTimeNotify;
    }

    public boolean isFeedDispatch() {
        return isFeedDispatch;
    }

    public List<PhotoWith> getPhotoWiths() {
        return photoWiths;
    }

    public void setFeedDispatch(boolean isFeedDispatch) {
        this.isFeedDispatch = isFeedDispatch;
    }

    public void setPhotoWiths(List<PhotoWith> photoWiths) {
        this.photoWiths = photoWiths;
    }

    public void setReplaceAt(Boolean replaceAt) {
        this.replaceAt = replaceAt;
    }

    public void setReplaceShortUrl(Boolean replaceShortUrl) {
        this.replaceShortUrl = replaceShortUrl;
    }

    public boolean isNeedMetadata() {
        return needMetadata;
    }

    public void setNeedMetadata(boolean needMetadata) {
        this.needMetadata = needMetadata;
    }

    public boolean isZujiComment() {
        return isZujiComment;
    }

    public void setZujiComment(boolean isZujiComment) {
        this.isZujiComment = isZujiComment;
    }

    public boolean isPhotoTagComment() {
        return isPhotoTagComment;
    }

    public void setPhotoTagComment(boolean isPhotoTagComment) {
        this.isPhotoTagComment = isPhotoTagComment;
    }

    /**
     * For those config passed in by params which will be handled by comment
     * center, set their values to the current comment strategy.<br/>
     * Note, this method is different with <code>setParams</code> method, which
     * simply save the params; This method will convert the params to comment
     * strategy fields.
     * 
     * @param params
     */
    public void setByParams(Map<String, String> params) {
        Param.initStrategyByParams(params, this);
    }

    /**
     * Hold the keys of params that will be handled by comment center, which
     * could be used to override the config in <code>CommentConfig</code>. <br/>
     * User can pass in any key-value pair by params, but only those which
     * starts with "comment." will be handled by comment center.
     * 
     * @author jiankuan.xing
     */
    static class Param {

        public static Logger logger = Logger.getLogger(Param.class);

        /**
         * Feed type
         */
        public static final String FEED_TYPE = "stype";

        /**
         * Feed source id
         */
        public static final String FEED_SOURCE_ID = "sourceId";

        /**
         * Whether to replace "at
         */
        public static final String REPLACE_AT = "repalceAt";

        /**
         * whether to replace "ubb"
         */
        public static final String REPLACE_UBB = "replaceUbb";

        /**
         * whether to replace large ubb
         */
        public static final String REPLACE_LARGE_UBB = "replaceUBBLarge";

        /**
         * whether to replace "short url"
         */
        public static final String REPLACE_SHORT_URL = "replaceShortUrl";

        /**
         * Whether to return meta along with the comment object
         */
        public static final String RETURN_META = "needMetadata";

        /**
         * whether to return meta data along with the comments
         */
        public static final String RETURN_FULL_HEAD_URL = "returnFullHeadUrl";

        /**
         * pass in the user ip that will be handled by antispam api
         */
        public static final String ANTISPAM_USER_IP = "antispam.userip";

        /**
         * Photo Tag
         */
        public static final String PHOTO_TAG_INFO = "isPhotoTag";

        /**
         * Whether to double write to global comment list (url_comment table)
         */
        public static final String NEED_GLOBAL = "needGlobal";

        /**
         * Whether to call antispam interface
         */
        public static final String NEED_ANTISPAM = "needAntispam";

        /**
         * Whether to call @ interface
         */
        public static final String NEED_SENDATINFO = "needSendAtInfo";

        /**
         * Whether to call friend notify function
         */
        public static final String NEED_SEND_FRIEND_NOTIFY =
                "needSendFriendNotify";

        /**
         * Whether to get entry when read(etc:getlist)
         */
        public static final String GET_ENTRY_WHEN_READ = "getEntryWhenRead";

        /**
         * feedOwnerId
         */
        public static final String FEED_OWNER_ID = "feedOwnerId";

        /**
         * feedOwnerId
         */
        public static final String ENTRY_PASSWORD = "entryPassword";
        
        /**
         * isOriginalContent
         */
        public static final String IS_ORIGINAL_CONTENT = "isOriginalContent";
        
        /**
         * isFrom
         */
        public static final String IS_FROM = "isFrom";
        
        /**
         * 是否需要简单的评论信息
         * 
         */
        public static final String NEED_SIMPLE_INFO = "needSimpleInfo";
        
        /**
         * 是否需要返回"这条评论之前有多少信息"
         */
        public static final String NEED_HAS_PRE ="needHasPre";
        
        /**
         * 是否需要替换作者信息，true的话，评论中心会保存每次"创建评论的当时的作者"的名字和头像，当取评论的时候，就为"创建评论的当时的作者"的名字和头像
         */
        public static final String SAVE_AUTHOR_INFO ="saveAuthorInfo";
        
        /**
         * 校园主页的"学校id"
         */
        public static final String SCHOOL_ID = "schoolId";

        public static Map<String, CommentStrategySetter> paramKeys;

        static {
            paramKeys = new HashMap<String, CommentStrategySetter>();
            paramKeys.put(REPLACE_AT, new AtReplaceSetter());
            paramKeys.put(REPLACE_UBB, new UbbReplaceSetter());
            paramKeys.put(REPLACE_LARGE_UBB, new LargeUbbReplaceSetter());
            paramKeys.put(REPLACE_SHORT_URL, new ShortUrlReplaceSetter());
            paramKeys.put(RETURN_META, new MetaReturnSetter());
            paramKeys.put(RETURN_FULL_HEAD_URL, new ReturnFullHeadUrlSetter());
            paramKeys.put(ANTISPAM_USER_IP, new AntispamUserIpSetter());
            paramKeys.put(PHOTO_TAG_INFO, new PhotoTagSetter());
            paramKeys.put(FEED_TYPE, new FeedTypeSetter());
            paramKeys.put(FEED_SOURCE_ID, new FeedSourceIdSetter());
            paramKeys.put(NEED_GLOBAL, new NeedGlobalSetter());
            paramKeys.put(GET_ENTRY_WHEN_READ, new GetEntrySetter());
            paramKeys.put(NEED_ANTISPAM, new NeedAntispamSetter());
            paramKeys.put(NEED_SENDATINFO, new NeedSendAtInfoSetter());
            paramKeys.put(FEED_OWNER_ID, new GetFeedOwnerIdSetter());
            paramKeys.put(NEED_SEND_FRIEND_NOTIFY,
                new NeedSendFriendNotifySetter());
            paramKeys.put(ENTRY_PASSWORD, new GetEntryPasswordSetter());
            paramKeys.put(IS_ORIGINAL_CONTENT, new IsOriginalContentSetter());
            paramKeys.put(IS_FROM, new IsFromSetter());
            paramKeys.put(NEED_SIMPLE_INFO, new NeedSimpleInfoSetter());
            paramKeys.put(NEED_HAS_PRE, new NeedHasPreSetter());
            paramKeys.put(SAVE_AUTHOR_INFO, new SaveAuthorInfoSetter());
            paramKeys.put(SCHOOL_ID, new SchoolIdSetter());
        }

        private static void initStrategyByParams(Map<String, String> params,
            CommentStrategy strategy) {
            if (params == null) {
                return;
            }

            for (String key : params.keySet()) {
                CommentStrategySetter setter = paramKeys.get(key);
                if (setter != null) {
                    String rawValue = params.get(key);
                    setter.setStrategy(rawValue, strategy);
                }
            }
        }

        static abstract class CommentStrategySetter {

            public boolean getBooleanValue(String rawValue) {
                return Boolean.valueOf(rawValue);
            }

            public int getIntValue(String rawValue) {
                return Integer.valueOf(rawValue);
            }

            public String getStringValue(String rawValue) {
                return rawValue;
            }

            public long getLongValue(String rawValue) {
                return Long.valueOf(rawValue);
            }

            public abstract void setStrategy(String rawValue,
                CommentStrategy strategy);
        }

        static class UbbReplaceSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setReplaceUbb(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class ShortUrlReplaceSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setReplaceShortUrl(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class AtReplaceSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setReplaceAt(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class LargeUbbReplaceSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setReplaceUbbLarge(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class AntispamUserIpSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        String value = getStringValue(rawValue);
                        strategy.setUserIp(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class MetaReturnSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setNeedMetadata(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class FeedTypeSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        int value = getIntValue(rawValue);
                        strategy.setFeedType(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class FeedSourceIdSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        long value = getLongValue(rawValue);
                        strategy.setSourceId(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class ReturnFullHeadUrlSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        boolean value = getBooleanValue(rawValue);
                        strategy.setReturnFullHeadUrl(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class PhotoTagSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setPhotoTagComment(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class NeedGlobalSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setNeedGlobal(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class NeedAntispamSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setNeedAntispam(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class NeedSendAtInfoSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setNeedSendAtInfo(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class GetFeedOwnerIdSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        int feedOwnerId = getIntValue(rawValue);
                        strategy.setFeedOwnerId(feedOwnerId);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class NeedSendFriendNotifySetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setNeedSendFriendNotify(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class GetEntryPasswordSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {
                        strategy.setEntryPassword(rawValue);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }
        
        static class IsOriginalContentSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {
                    	 Boolean value = getBooleanValue(rawValue);
                        strategy.setOriginalContentQuery(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }
        
        static class IsFromSetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        int value = getIntValue(rawValue);
                        strategy.setIsFrom(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }

        static class GetEntrySetter extends CommentStrategySetter {

            @Override
            public void setStrategy(String rawValue, CommentStrategy strategy) {
                if (rawValue != null) {
                    try {

                        Boolean value = getBooleanValue(rawValue);
                        strategy.setGetEntryWhenRead(value);
                    } catch (Exception e) {
                        // for any exception, we ignore it
                        logger.warn("Ignore the invalid param, value is "
                                    + rawValue);
                    }
                }
            }
        }
        static class NeedSimpleInfoSetter extends CommentStrategySetter {

			@Override
			public void setStrategy(String rawValue, CommentStrategy strategy) {
				if(null != rawValue){
					try{
						boolean value = getBooleanValue(rawValue);
						strategy.setNeedSimpleInfo(value);
					}catch(Exception e){
                        logger.warn("Ignore the invalid param, value is "
                                + rawValue);
					}
				}
				
			} 
        	
        }
        
        static class NeedHasPreSetter extends CommentStrategySetter {

			@Override
			public void setStrategy(String rawValue, CommentStrategy strategy) {
				if(null != rawValue){
					try{
						boolean value = getBooleanValue(rawValue);
						strategy.setNeedHasPre(value);
					}catch(Exception e){
                        logger.warn("Ignore the invalid param, value is "
                                + rawValue);
					}
				}
				
			} 
        	
        }
        
        static class SaveAuthorInfoSetter extends CommentStrategySetter {

			@Override
			public void setStrategy(String rawValue, CommentStrategy strategy) {
				if(null != rawValue){
					try{
						boolean value = getBooleanValue(rawValue);
						strategy.setSaveAuthorInfo(value);
					}catch(Exception e){
                        logger.warn("Ignore the invalid param, value is "
                                + rawValue);
					}
				}
			} 	
        }
        
        static class SchoolIdSetter extends CommentStrategySetter {

			@Override
			public void setStrategy(String rawValue, CommentStrategy strategy) {
				if(null != rawValue){
					try{
						int value = getIntValue(rawValue);
						strategy.setSchoolId(value);
					}catch(Exception e){
                        logger.warn("Ignore the invalid param, value is "
                                + rawValue);
					}
				}
			} 	
        }
    }

    /**
     * 分享相册中一张照片的评论，需要传该值，其他请勿使用 真实分享的title
     */
    public String getShareTitle() {
        if (params == null) {
            return null;
        }
        return this.params.get("title");
    }

    /**
     * 分享相册中一张照片的评论，需要传该值，其他请勿使用 真实分享的实体Id
     */
    public String getShareRealId() {
        if (params == null) {
            return null;
        }
        return this.params.get("shareRealId");
    }

//    public Boolean isReplyLinked() {
//        return isReplyLinked;
//    }
//
//    public void setIsReplyLinked(Boolean isReplyLinked) {
//        this.isReplyLinked = isReplyLinked;
//    }

    public Boolean getGetEntryWhenRead() {
        return getEntryWhenRead;
    }

    public void setGetEntryWhenRead(Boolean getEntryWhenRead) {
        this.getEntryWhenRead = getEntryWhenRead;
    }

    public int getOldestCount() {
        return oldestCount;
    }

    public void setOldestCount(int oldestCount) {
        this.oldestCount = oldestCount;
    }

    public int getLatestCount() {
        return latestCount;
    }

    public void setLatestCount(int latestCount) {
        this.latestCount = latestCount;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    /**
     * 是否在发送新鲜事的时候加上quote,参见{@link:FeedPublisher#buildFeedReplyObj()}方法
     */
    private Boolean needFeedQuote = null;

    public boolean isNeedFeedQuote() {
        if (needFeedQuote == null) {
            if (config == null) {
                return false;
            } else {
                return config.isNeedFeedQuote();
            }
        }
        return needFeedQuote;
    }

    /**
     * 是否在发送im新鲜事的时候加上quote,参见{@link:FeedPublisher#buildFeedReplyObj()}方法
     */
    private Boolean needIMFeedQuote = null;

    public boolean isNeedIMFeedQuote() {
        if (needIMFeedQuote == null) {
            if (config == null) {
                return false;
            } else {
                return config.isNeedIMFeedQuote();
            }
        }
        return needIMFeedQuote;
    }

    public Boolean getNeedSendFriendNotify() {
        if (needSendFriendNotify != null) {
            return needSendFriendNotify;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.isNeedSendFriendNotify();
            }
        }
    }

    public void setNeedSendFriendNotify(Boolean needSendFriendNotify) {
        this.needSendFriendNotify = needSendFriendNotify;
    }

	public int getOriginalQueryLimit() {
		return originalQueryLimit;
	}

	public CommentStrategy setOriginalQueryLimit(int originalQueryLimit) {
		this.originalQueryLimit = originalQueryLimit;
		return this;
	}

	public boolean isOriginalContentQuery() {
		return isOriginalContentQuery;
	}

	public void setOriginalContentQuery(boolean isOriginalContentQuery) {
		this.isOriginalContentQuery = isOriginalContentQuery;
	}

	public int getIsFrom() {
		return isFrom;
	}

	public void setIsFrom(int isFrom) {
		this.isFrom = isFrom;
	}

	public long getParentEntryId() {
		return parentEntryId;
	}

	public void setParentEntryId(long parentEntryId) {
		this.parentEntryId = parentEntryId;
	}

	public boolean isNeedSimpleInfo() {
		return needSimpleInfo;
	}
	
	public boolean isNotNeedSimpleInfo() {
		return !isNeedSimpleInfo();
	}

	public void setNeedSimpleInfo(boolean needSimpleInfo) {
		this.needSimpleInfo = needSimpleInfo;
	}

	public boolean isNeedHasPre() {
		return needHasPre;
	}

	public void setNeedHasPre(boolean needHasPre) {
		this.needHasPre = needHasPre;
	}
	
	public Boolean isNeedMetadataResolve() {
        if (needMetadataResolve != null) {
            return needMetadataResolve;
        } else {
            if (config == null) {
                return false;
            } else {
                return config.isNeedMetadataResolve();
            }
        }
    }

	public void setNeedMetadataResolve(boolean needMetadataResolve) {
		this.needMetadataResolve = needMetadataResolve;
	}

	public boolean isSaveAuthorInfo() {
		return saveAuthorInfo;
	}

	public void setSaveAuthorInfo(boolean replaceAuthorInfo) {
		this.saveAuthorInfo = replaceAuthorInfo;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}


}
