/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import com.renren.ugc.comment.model.ShareBusinessNotifyBody;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.model.at.AtNotifyInfoBean;
import com.renren.xoa.at.api.util.AtNotifyInfoBeanBuilder;

/**
 * Descriptions of the class ShareAtNotifyInfoBeanBuilder.java's
 * implementation：分享atNotifyInfoBean构建
 * 
 * @author xiaoqiang 2013-9-12 下午2:37:58
 */
public abstract class ShareAtNotifyInfoBeanBuilder {

    private Logger logger = Logger.getLogger(this.getClass());

    public static final String KEY_OWNER = "owner";

    public static final String KEY_SHARE_ID = "share_id";

    public static final String KEY_SHARE_USER_ID = "share_user_id";

    public static final String KEY_COMMENT_ID = "comment_id";

    /** 概念版消息中心需要的图片链接 */
    public static final String KEY_SHARE_IMGURL = "imgUrl";

    public static final String KEY_SHARE_TYPE = "share_type";

    public static final String KEY_SHARE_ALBUM_THUMB_URL = "thumb_url";

    /**
     * 构建
     * 
     * @param strategy
     * 
     * @param shareCommentAtMessage
     * @return
     */
    public AtNotifyInfoBean build(Comment comment, CommentStrategy strategy) {
        Entry share = strategy.getEntry();
        AtNotifyInfoBeanBuilder builder = new AtNotifyInfoBeanBuilder();
        builder.setHostId(comment.getAuthorId()).setOwnerId(share.getOwnerId()).setHtmlContentKey(
                "doing_content").setSourceId(share.getId());
        //获取各个分享子业务的BussinessNotifyBody
        ShareBusinessNotifyBody bussinessNotifyBody = buildBusinessNotifyBody(share);
        builder.setSchemaId(bussinessNotifyBody.getSchemaId()).setType(
                bussinessNotifyBody.getType()).setBusiParams(bussinessNotifyBody.getValueMap());
        dealCommonValues(builder, comment, strategy);
        return builder.build();
    }

    /**
     * 处理公共的业务参数
     * 
     * @param builder
     * @param shareCommentAtTask
     */
    void dealCommonValues(AtNotifyInfoBeanBuilder builder, Comment comment, CommentStrategy strategy) {
        Entry share = strategy.getEntry();
        builder.setValue("from_name", comment.getAuthorName());
        builder.setValue("share_type", share.getEntryProps().get(KEY_SHARE_TYPE));

        String title = getTitle(share);
        builder.setValue("doing_content", title);

        builder.setValue(KEY_SHARE_ID, String.valueOf(share.getId()));
        builder.setValue(KEY_SHARE_USER_ID, String.valueOf(share.getOwnerId()));

        // 概念版消息中心改动添加图片
        builder.setValue(KEY_SHARE_IMGURL, share.getEntryProps().get(KEY_SHARE_ALBUM_THUMB_URL));
    }

    /**
     * 构建分享子业务的bussinessNotifyBody
     * 
     * @param comment
     * @return
     */
    public abstract ShareBusinessNotifyBody buildBusinessNotifyBody(Entry share);

    /**
     * 获得提醒标题
     * 
     * @return
     */
    protected String getTitle(final Entry share) {
        String title = share.getEntryProps().get("share_title");
        if (StringUtils.isBlank(title)) {
            /*
            if (StringUtils.isNotBlank(NotifyUtils.getMetaNameForType(share.getType()))) {
                title = share.getMetaMap().get(NotifyUtils.getMetaNameForType(share.getType()));
            } else {
                title = "分享";
            } */
            title = "分享";
        }
        return title;
    }

}
