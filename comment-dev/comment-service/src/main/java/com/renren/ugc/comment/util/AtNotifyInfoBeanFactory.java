/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.renren.app.at.model.AtFormatType;
import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.share.ShareAtNotifyInfoBeanBuilder;
import com.renren.ugc.comment.util.share.ShareListAtNotifyInfoBeanBuilder;
import com.renren.ugc.model.at.AtInfoBean;
import com.renren.ugc.model.at.AtNotifyInfoBean;
import com.renren.xoa.at.api.util.AtInfoBeanBuilder;

/**
 * Descriptions of the class AtNotifyInfoBeanFactory.java's
 * implementation：一个notifyInfoBean的工厂，用来获得不同类型的notifyInfoBean
 * 
 * @author xiaoqiang 2013-9-12 下午2:26:56
 */
public class AtNotifyInfoBeanFactory {

    private static ShareAtNotifyInfoBeanBuilder builder;

    private static List<Integer> list = new ArrayList<Integer>();

    static {
        list = Arrays.asList(IShareConstants.ARRAYS_URL_TYPE_TERMINAL);
    }

    /**
     * @param comment
     * @param strategy
     * @return
     */
    public static AtNotifyInfoBean getAtNotifyInfoBean(Comment comment, CommentStrategy strategy) {
        Entry entry = strategy.getEntry();
        long virtualId = entry.getEntryProps().get("virtual_id") == null ? 0 : Long.valueOf(entry.getEntryProps().get("virtual_id"));
        int shareType = Integer.valueOf(entry.getEntryProps().get("share_type"));
        if (shareType == IShareConstants.URL_TYPE_BLOG) {
            builder = new BlogShareAtNotifyInfoBeanBuilder();
        } else if (shareType != IShareConstants.URL_TYPE_LINK && list.contains(shareType)
                && virtualId == 0) {
            // at提醒不再跳终端页了,要跳列表页 20140618 wenchen.di
            builder = new TerminalShareAtNotifyInfoBeanBuilder();
        } else if ((shareType == IShareConstants.URL_TYPE_PHOTO)
                && virtualId > 0) {
            builder = new VirtualShareIndexAtNotifyInfoBeanBuilder();
        } else {
            builder = new ShareListAtNotifyInfoBeanBuilder();
        }
        return builder.build(comment, strategy);
    }

    /**
     * @param comment
     * @return
     */
    public static AtInfoBean getAtInfoBean(Comment comment) {
        AtInfoBeanBuilder builder = new AtInfoBeanBuilder();
        builder.setContent(comment.getOriginalContent()).setHostId(comment.getAuthorId()).setFormatType(
                AtFormatType.NO_HREF_NAME_ID);
        return builder.build();
    }

}
