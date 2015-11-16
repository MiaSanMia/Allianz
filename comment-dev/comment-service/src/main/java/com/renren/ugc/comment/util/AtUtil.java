package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.app.at.local.service.AtParseHelper;
import com.renren.app.at.model.AtFormatType;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.commons.tools.at.AtFormartUtil;
import com.renren.ugc.model.at.AtInfoBean;
import com.renren.ugc.model.at.AtNotifyInfoBean;
import com.renren.ugc.model.at.AtPrivacyParam;
import com.renren.ugc.model.at.AtReturnInfo;
import com.renren.ugc.model.at.AtReturnParam;
import com.renren.xoa.at.api.AtHandleService;
import com.renren.xoa.at.api.util.AtInfoBeanBuilder;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.xiaonei.antispam.AntiSpamUtils;
import com.xiaonei.platform.component.xfeed.helper.FeedDefinition;

/**
 * Utility class to access At service
 * 
 * @author wangxx
 * 
 */
public class AtUtil {

    private Logger logger = Logger.getLogger(this.getClass());

    private static AtUtil instance = new AtUtil();

    private AtHandleService atHandlerService = null;
 
    private Map<CommentType, Integer> atNoticeIdMap = new HashMap<CommentType, Integer>();

    // 足迹照片@提醒type与schema
    private final static int ZUJIPHOTO_ATNOTICE = 446;
    
    private AtUtil() {
        atHandlerService = ServiceFactories.getFactory().getService(AtHandleService.class);

        atNoticeIdMap.put(CommentType.Blog, 172);
        atNoticeIdMap.put(CommentType.Photo, 197);
        atNoticeIdMap.put(CommentType.Album, 171);
        atNoticeIdMap.put(CommentType.Status, 196);
        atNoticeIdMap.put(CommentType.ShortVideo, 1050);
        atNoticeIdMap.put(CommentType.CampusPost, 1080);
        atNoticeIdMap.put(CommentType.CampusExcellent, 1086);
    }

    public static AtUtil getInstance() {
        return instance;
    }

    /**
     * 获取原始文本进行AT替换后的文本
     * 
     * @return 调用本地类进行正则匹配，返回带链接的AT HTML片段。 如果替换失败，会返回原始文本
     */
    public String getWithHrefAt(final String content) {

        if (AtParseHelper.hasAtInfo(content) == false) return content;

        String contentWithAt = content;
        long start = System.nanoTime();
        boolean success = false;
        try {
            contentWithAt = AtFormartUtil.formartHerfWithName(content);
            success = true;
        } catch (final Exception e) {
            logger.warn("error occurs during AT replacement", e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeFormatAtWithHerf((end - start) / StatisticsHelper.NANO_TO_MILLIS,
                success);
        // even AT replacement fails, we should always return original content
        return contentWithAt;
    }

    public String filterAtInfo(final int hostId, final String content, final CommentType type,
            final int entryOwnerId, final long entryId, final Comment comment,
            final CommentStrategy strategy) {

        if (StringUtils.isBlank(content) || !AtParseHelper.hasAtInfo(content)) {
            return content;
        }
        String contentSafe = content;

        //是否需要破坏@
        boolean isBrokean = this.getIfSetBroken(type, comment, strategy);

        AtFormatType formatType = isBrokean ? AtFormatType.BROKEN_STR
                : AtFormatType.NO_HREF_NAME_ID;

        contentSafe = this.formatAt(getAtInfoBean(hostId, content, formatType, type, entryOwnerId,
                entryId, comment, strategy));

        return contentSafe;

    }

    private AtInfoBean getAtInfoBean(int userId, String commentBody, AtFormatType formatType,
            final CommentType type, final int entryOwnerId, final long entryId,
            final Comment comment, final CommentStrategy strategy) {
        AtInfoBeanBuilder atInfoBeanBuilder = new AtInfoBeanBuilder().setHostId(userId).setContent(
                commentBody).setFormatType(formatType).setAtPrivacyParam(
                this.buildAtPrivacyParam(type, entryOwnerId, entryId, comment, strategy));
        return atInfoBeanBuilder.build();
    }

    public String formatAt(AtInfoBean atInfoBean) throws UGCCommentException {

        if (atInfoBean == null) {
            return null;
        }

        String content = atInfoBean.getContent();;

        long start = System.nanoTime();
        boolean success = false;

        try {
            // 调用@ xoa 接口
            if (AtParseHelper.hasAtInfo(atInfoBean.getContent())) {
                ServiceFuture<String> serviceFuture = atHandlerService.format(atInfoBean);
                content = (String) XoaClientAdapter.doSubmit2(serviceFuture);

            }
            success = true;
        } catch (Exception e) {
            logger.error("formatAt error|content:" + content, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeFormatAt((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);

        return content;
    }

    /**
     * 
     * 发送@通知。当评论中@了某个人时，这个被@的人会收到通知
     * 
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @return
     * 
     * @的人合法列表，因为"与我相关"可能会调用到这个列表，见{@link : MatterToMeInterceptor}
     */
    public List<Integer> sendNotice(CommentType type, int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy) {

        if (comment == null || !AtParseHelper.hasAtInfo(comment.getOriginalContent())) {
            return null;
        }

        AtInfoBean atInfoBean = null;
        AtNotifyInfoBean atNotifyInfoBean = null;
        AtReturnParam atReturnParam = null;
        switch (type) {
            case Blog:
            case Photo:
            case Album:
            case Status:
            case ShortVideo:
            case CampusPost:
            case CampusExcellent:
                //1.get type and schemaId, type = schemId,so get only one just ok
                Integer schemaId = atNoticeIdMap.get(type);
                if (schemaId == null) {
                    logger.warn("sendNotice gettype from atNoticeIdMap is null | type:" + type);
                    return null;
                }
                //special needs
                if (type == CommentType.Photo && strategy.isZujiComment()) {
                    //photo zuji
                    schemaId = ZUJIPHOTO_ATNOTICE;
                }
                
                //2.build "at" bean
                atNotifyInfoBean = this.buildAtNotifyInfoBean(actorId, schemaId, entryOwnerId,
                        entryId);
             
                //3. build "at" xoa params
                atInfoBean = new AtInfoBean();
                atInfoBean.setFormatType(AtFormatType.HREF_NAME);
                atInfoBean.setContent(comment.getOriginalContent());
                atInfoBean.setHostId(actorId);

                this.buildBizParams(type, actorId, entryOwnerId,
                        entryId, comment, strategy,atNotifyInfoBean);

                atReturnParam = new AtReturnParam();
                atReturnParam.setNeedReturnUserIds(true);
                break;
            case Share:
            case ShareAlbumPhoto:
                atNotifyInfoBean = AtNotifyInfoBeanFactory.getAtNotifyInfoBean(comment, strategy);
                atInfoBean = AtNotifyInfoBeanFactory.getAtInfoBean(comment);

                atReturnParam = new AtReturnParam();

                break;
            default:
                logger.warn("no valid type in getBusiParams | type:" + type);
        }

        // 3.提交XOA处理
        ServiceFuture<AtReturnInfo> serviceFuture = atHandlerService.handleAtWithRichReturn(
                atInfoBean, atNotifyInfoBean, atReturnParam);
        AtReturnInfo atReturnInfo = (AtReturnInfo) XoaClientAdapter.doSubmit2(serviceFuture);
        return atReturnInfo != null ? atReturnInfo.getUserIdList() : new ArrayList<Integer>();
    }

    /**
     * @param type
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @return
     * 
     *         由于每个业务@的通知走的模板都不一样，而且key值也不一样，因此这里做个统一
     */
    private void buildBizParams(CommentType type, int actorId, int entryOwnerId,
            long entryId, Comment comment, CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean) {

        Map<String, String> bizParams = null;

        //1. 去各个业务取entry       
        // GetEntryUtil.getInstance().getEntryInfo(type, actorId, entryOwnerId, entryId, comment);

        //2. build
        switch (type) {
            case Status:
                bizParams = buildStatusBizParams(actorId, entryOwnerId, entryId, comment, strategy);
                break;
            case Blog:
                bizParams = buildBlogBizParams(actorId, entryOwnerId, entryId, comment, strategy,atNotifyInfoBean);
                break;
            case Album:
                bizParams = buildAlbumBizParams(actorId, entryOwnerId, entryId, comment, strategy,atNotifyInfoBean);
                break;
            case Photo:
                if (strategy.isZujiComment()) {
                    //足迹评论
                    bizParams = buildPhotoZujiBizParams(actorId, entryOwnerId, entryId, comment,
                            strategy);
                } else {
                    //普通照片评论
                    bizParams = buildPhotoBizParams(actorId, entryOwnerId, entryId, comment,
                            strategy,atNotifyInfoBean);
                }
                break;
            case ShortVideo:
            	bizParams = buildVideoBizParams(actorId, entryOwnerId, entryId, comment, strategy);
            	break;
            case CampusPost:
            	bizParams = buildCampusPostBizParams(actorId, entryOwnerId, entryId, comment, strategy);
            	break;
            case CampusExcellent:
            	bizParams = buildCampusExcellentBizParams(actorId, entryOwnerId, entryId, comment, strategy);
            	break;
            default:
                logger.warn("no valid type in getBusiParams | type:" + type);
        }
        
        atNotifyInfoBean.setBusiParams(bizParams);

    }

    private Map<String, String> buildStatusBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> busiParams = new HashMap<String, String>();

        busiParams.put("source", String.valueOf(entryId));
        busiParams.put("owner", String.valueOf(entryOwnerId));
        busiParams.put("from", String.valueOf(comment.getAuthorId()));
        busiParams.put("time", String.valueOf(System.currentTimeMillis()));
        busiParams.put("feed_stype", String.valueOf(strategy.getFeedType()));

        busiParams.put("feed_source", String.valueOf(entryId));
        busiParams.put("feed_actor", String.valueOf(entryOwnerId));

        busiParams.put("from_name", comment.getAuthorName());
        busiParams.put("doing_id", String.valueOf(entryId));
        // TODO 这个地方是否需要修改成其它content eg: htmlContent
        busiParams.put(
                "doing_content",
                AntiSpamUtils.getInstance().htmlSecurityEscape(
                        strategy.getEntry().getEntryProps() != null ? strategy.getEntry().getEntryProps().get(
                                EntryConfig.ENTRY_CONTENT)
                                : ""));
        // busiParams.put("doing_content", htmlContent);

        busiParams.put("replied_id", String.valueOf(comment.getId()));
        busiParams.put("reply_id", String.valueOf(comment.getId()));
        // busiParams.put("schema_id", String.valueOf(196));

        busiParams.put("from_pic", comment.getAuthorHead());

        busiParams.put("reply_content", comment.getContent());

        return busiParams;
    }

    private Map<String, String> buildBlogBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = null;

        // copy from blog...
        switch(strategy.getIsFrom()){
        	// 根据来源发不同的通知
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	bizParams = LookingWorldUrlUtil.buildLookingWorldBizBodyAll(CommentType.Blog, entryOwnerId, entryId, comment, strategy,atNotifyInfoBean);
		    	break;
		    default: {
		    		bizParams = new HashMap<String, String>();
			        bizParams.put("from_name", comment.getAuthorName());
			        bizParams.put("comment_id", comment.getId() + "");
			        bizParams.put("anchor", comment.getId() + "");
			        bizParams.put("blog_id", entryId + "");
			        bizParams.put("author_id", actorId + "");
			        bizParams.put(
			                "blog_title",
			                AntiSpamUtils.getInstance().htmlSecurityEscape(
			                        strategy.getEntry().getEntryProps() != null ? strategy.getEntry().getEntryProps().get(
			                                EntryConfig.ENTRY_TITLE)
			                                : ""));
			        bizParams.put("is_whisper", (comment.getWhipserToId() > 0) ? "1" : "0");
		    	}
        }

        return bizParams;
    }

    private Map<String, String> buildAlbumBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = null;
        switch(strategy.getIsFrom()){
        	// 根据来源发不同的通知
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	bizParams = LookingWorldUrlUtil.buildLookingWorldBizBodyAll(CommentType.Album, entryOwnerId, entryId, comment, strategy,atNotifyInfoBean);
		    	break;
		    	default:{
		    		bizParams = new HashMap<String, String>();
			        // copy from album
			        bizParams.put("from_name", comment.getAuthorName());
			        bizParams.put("album_id", entryId + "");
			        bizParams.put(
			                "album_name",
			                AntiSpamUtils.getInstance().htmlSecurityEscape(
			                        EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE)));
			        bizParams.put("is_whisper", (comment.getWhipserToId() > 0) ? "1" : "0");
			        bizParams.put("feed_stype", String.valueOf(FeedDefinition.PHOTO_PUBLISH_MORE));
			        bizParams.put("feed_source", entryId + "");
			        bizParams.put("feed_actor", entryOwnerId + "");
			        
			        //用于手机上消息中心@评论的展示
			        bizParams.put("imgUrl", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
		    	}
        }

        return bizParams;
    }

    private Map<String, String> buildPhotoBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = null;
        switch(strategy.getIsFrom()){
        	// 根据来源发不同的通知
		    case CommentFromConstant.FROM_LOOKING_WORLD:
		    	bizParams = LookingWorldUrlUtil.buildLookingWorldBizBodyAll(CommentType.Photo, entryOwnerId, entryId, comment, strategy,atNotifyInfoBean);
		    	break;
		    	default:{
		    		bizParams = new HashMap<String, String>();
			        bizParams.put("from_name", comment.getAuthorName());
			        bizParams.put("photo_id", entryId + "");
			        bizParams.put(
			                "album_name",
			                AntiSpamUtils.getInstance().htmlSecurityEscape(
			                        EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_PARENT_NAME)));
			        bizParams.put("is_whisper", (comment.getWhipserToId() > 0) ? "1" : "0");
			        bizParams.put("feed_stype", String.valueOf(FeedDefinition.PHOTO_PUBLISH_MORE));
			        bizParams.put("feed_source", entryId + "");
			        bizParams.put("feed_actor", entryOwnerId + "");
			
			        //copy from photo
			        final int stype = EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_MULTI) ? FeedDefinition.PHOTO_REPLY
			                : FeedDefinition.PHOTO_PUBLISH_ONE;
			
			        bizParams.put("feed_stype", String.valueOf(stype));
			
			        bizParams.put("feed_source", entryId + "");
			        bizParams.put("feed_actor", entryOwnerId + "");
			
			        bizParams.put("from_pic", comment.getAuthorHead());
			        bizParams.put("reply_content", comment.getContent());
			
			        bizParams.put("big_image", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_LARGEURL));
			        //用于手机上消息中心@评论的展示
			        bizParams.put("imgUrl", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
		    	}
        }

        return bizParams;
    }

    private AtNotifyInfoBean buildAtNotifyInfoBean(int actorId, int schemaId, int entryOwnerId,
            long entryId) {
        AtNotifyInfoBean atNotifyInfoBean = new AtNotifyInfoBean();
        atNotifyInfoBean.setType(schemaId);
        atNotifyInfoBean.setSchemaId(schemaId);
        atNotifyInfoBean.setFormatType(AtFormatType.HREF_NAME);
        atNotifyInfoBean.setHostId(actorId);
        atNotifyInfoBean.setOwnerId(entryOwnerId);
        atNotifyInfoBean.setSourceId(entryId);
        //atNotifyInfoBean.setBusiParams(busiParams);

        return atNotifyInfoBean;
    }

    private AtPrivacyParam buildAtPrivacyParam(final CommentType type, final int entryOwnerId,
            final long entryId, final Comment comment, final CommentStrategy strategy) {
        if (comment == null || !needPrivacy(type)) {
            return null;
        }
        AtPrivacyParam atPrivacyParam = new AtPrivacyParam();

        long createTime = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_CREATE_TIME);
        boolean isPublic = EntryConfigUtil.getIsPublic(type, strategy);

        atPrivacyParam.setCreateTime(createTime);
        atPrivacyParam.setOwnerId(entryOwnerId);
        atPrivacyParam.setUgcGlobalId(GlobalIdUtil.getGlobalId(type, entryId));
        atPrivacyParam.setPublic(isPublic);

        if(needParentPrivacy(type)){
            this.setParentPrivacyParam(atPrivacyParam, comment, type, strategy);
        }

        return atPrivacyParam;
    }

    private void setParentPrivacyParam(AtPrivacyParam atPrivacyParam, final Comment comment,
            final CommentType type, final CommentStrategy strategy) {

        if (atPrivacyParam == null || comment == null) {
            return;
        }
        CommentType parentType = type;
        if (type == CommentType.Photo) {
            parentType = CommentType.Album;
        }

        long parentId = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_PARENT_ID);
        long createTime = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_PARENT_CREATE_TIME);
        boolean isPublic = EntryConfigUtil.getParentIsPublic(type, strategy);

        atPrivacyParam.setParentCreateTime(createTime);
        atPrivacyParam.setParentIsPublic(isPublic);
        atPrivacyParam.setParentUgcGlobalId(GlobalIdUtil.getGlobalId(parentType, parentId));
    }

    /**
     * @param comment
     * @return 这个评论是否应该破坏@
     */
    private boolean getIfSetBroken(CommentType type, Comment comment, CommentStrategy strategy) {

        if (type == CommentType.Photo) {
            //以前情况需要破坏@
            boolean isParentPasswordProtected = EntryConfigUtil.getBoolean(strategy,
                    EntryConfig.ENTRY_PARENT_IS_PASSWORD_PROTECTED);
            int parentControl = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_PARENT_CONTROL);

            return isParentPasswordProtected || EntryConfigUtil.isOnlySelf(parentControl)
                    || comment.getWhipserToId() > 0;

        } else if (type == CommentType.Album) {
            //以前情况需要破坏@
            boolean isPasswordProtected = EntryConfigUtil.getBoolean(strategy,
                    EntryConfig.ENTRY_IS_PASSWORD_PROTECTED);
            int control = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_CONTROL);

            return isPasswordProtected || EntryConfigUtil.isOnlySelf(control)
                    || comment.getWhipserToId() > 0;
        }
        return comment.getWhipserToId() > 0;
    }

    private Map<String, String> buildPhotoZujiBizParams(int actorId, int entryOwnerId,
            long entryId, Comment comment, CommentStrategy strategy) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = new HashMap<String, String>();

        bizParams.put("from_name", comment.getAuthorName());
        bizParams.put("address",
                EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJISITE_NAME));
        bizParams.put("ownerId", entryOwnerId + "");
        bizParams.put("siteId", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJISITE_ID));
        bizParams.put("zujiId", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJI_ID));
        bizParams.put("itemId", EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_ZUJIITEM_ID));

        return bizParams;
    }
    
    /**
     * @param type
     * @return
     * 
     *  判断是否需要parent 隐私参数，现在只有photo业务再用
     */
    private boolean needParentPrivacy(CommentType type){
        return type == CommentType.Photo;
    }
    
    private Map<String, String> buildVideoBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = new HashMap<String, String>();

        // copy from album
        bizParams.put("from_name", comment.getAuthorName());
        bizParams.put("title",  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
        bizParams.put("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL));
        bizParams.put("reply_content", comment.getContent());
        bizParams.put("tiny_photo",  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));

        return bizParams;
    }
    
    /**
     * @param type
     * @return
     *  
     *  是否需要隐私
     */
    private boolean needPrivacy(CommentType type){
        return type == CommentType.Photo || type == CommentType.Album || type == CommentType.Blog ||
        				type == CommentType.Share || type == CommentType.Status;
    }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     * @return
     * 
     *  校园主页的bizParam构造
     */
    private Map<String, String> buildCampusPostBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = new HashMap<String, String>();

        // copy from album
        bizParams.put("from_name", comment.getAuthorName());
        bizParams.put("title",  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
        bizParams.put("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
        String text = ImgJsonUtil.getCampusText(comment.getContent());
        //因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
        bizParams.put("reply_content", text);
        bizParams.put("floor",  comment.getId()+"");

        return bizParams;
    }
    
    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @param strategy
     * @return
     * 
     *  校园主页人人精品的bizParam构造
     */
    private Map<String, String> buildCampusExcellentBizParams(int actorId, int entryOwnerId, long entryId,
            Comment comment, CommentStrategy strategy) {
        if (comment == null || strategy.getEntry() == null) {
            return null;
        }

        Map<String, String> bizParams = new HashMap<String, String>();
        String imgUrl =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL);
        long sourceId = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_SHARE_RESOURCE_ID);
        int sourceOwnerId = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SHARE_RESOURCE_USER_ID);
        int feedType =  EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_TYPE);
        String shareName =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_NAME);
        String shareTime =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_TIME);
        String shareImg =  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_CAMPUS_SHARE_IMG);

        bizParams.put("from_name", comment.getAuthorName());
        bizParams.put("title",  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
        bizParams.put("title_url",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_URL)+"?comment="+comment.getId());
        String text = ImgJsonUtil.getCampusText(comment.getContent());
        //因为评论里可能有图片，评论这里自己解析区分"文字"和"图片"
        bizParams.put("reply_content", text);
        bizParams.put("floor",  comment.getId()+"");
        bizParams.put("imgUrl",imgUrl);
        bizParams.put("share_type",String.valueOf(feedType));
        bizParams.put("source_id",String.valueOf(sourceId));
        bizParams.put("source_owner_id",String.valueOf(sourceOwnerId));
        bizParams.put("share_name",shareName);
        bizParams.put("share_time",shareTime);
        bizParams.put("share_img",shareImg);

        return bizParams;
    }

}
