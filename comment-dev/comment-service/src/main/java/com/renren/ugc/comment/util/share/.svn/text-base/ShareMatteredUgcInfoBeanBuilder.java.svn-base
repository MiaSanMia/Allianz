/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.matter.model.MatterActionType;
import com.renren.ugc.matter.model.MatteredUgcInfo;
import com.renren.ugc.matter.model.UgcType;

/**
 * Descriptions of the class MatteredUgcInfoBeanBuilder.java's
 * implementation：TODO described the implementation of class
 * 
 * @author xiaoqiang 2013-9-12 下午4:56:09
 */
public class ShareMatteredUgcInfoBeanBuilder {

    private static Logger logger = Logger.getLogger(ShareMatteredUgcInfoBeanBuilder.class);

    /**
     * 分享类型和与我相关类型的映射
     * 
     */
    public static Map<Integer, UgcType> SHARETYPE_MATTERTYPE_MAPPING = new HashMap<Integer, UgcType>(
            5) {

        private static final long serialVersionUID = 7368173422516744719L;

        {
            put(IShareConstants.URL_TYPE_BLOG, UgcType.SHARE_BLOG);
            put(IShareConstants.URL_TYPE_PHOTO, UgcType.SHARE_PHOTO);
            put(IShareConstants.URL_TYPE_ALBUM, UgcType.SHARE_ALBUM);
            put(IShareConstants.URL_TYPE_LINK, UgcType.SHARE_LINK);
            put(IShareConstants.URL_TYPE_VIDEO, UgcType.SHARE_VIDEO);
        }

    };

    /** 默认 权限类型 */
    public static int defaultAuthType = 99;

    /**
     * @param comment
     * @param entryOwnerId
     * @param entryId
     * @param userIdList
     * @param strategy
     * @return
     */
    public static MatteredUgcInfo build(Comment comment, int entryOwnerId, long entryId,
            List<Integer> userIdList, CommentStrategy strategy) {
        Entry share = strategy.getEntry();
        int shareType = Integer.valueOf(share.getEntryProps().get("share_type"));
        int currentUserId = comment.getAuthorId();

        if (!SHARETYPE_MATTERTYPE_MAPPING.containsKey(shareType)) {
            return null;
        }

        logger.debug(shareType + " | " + currentUserId + "进来");

        int replyToUserId = comment.getToUserId();
        long currentCommentId = comment.getId();

        if (!SHARETYPE_MATTERTYPE_MAPPING.containsKey(shareType)) {
            logger.error("没有找到该分享类型对应的与我相关类型 shareType:" + shareType);
        }
        Integer ugcType = SHARETYPE_MATTERTYPE_MAPPING.get(shareType).ordinal();

        MatteredUgcInfo data = new MatteredUgcInfo();
        data.setUgcId(share.getId());
        data.setUgcType(ugcType);
        data.setOwnerId(share.getOwnerId());
        data.setCommentAuthorId(currentUserId);
        data.setCommentId(currentCommentId);
        data.setCommentedUgcId(comment.getToCommentId());
        data.setCommentedUgcAuthorId(replyToUserId);
        data.setWhisper(false);
        data.setEncrypt(false);
        data.setAuthType(defaultAuthType);
        data.setActionType(MatterActionType.REPLAY);
        data.setAtUserIdList(userIdList);

        logger.debug(shareType + " | " + currentUserId + "构造结束");

        return data;
    }

}
