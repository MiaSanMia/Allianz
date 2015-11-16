/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util;

import com.renren.ugc.comment.model.ShareBusinessNotifyBody;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.util.share.ShareAtNotifyInfoBeanBuilder;
import com.renren.ugc.comment.util.share.ShareBusinessNotifyBodyBuilder;

/**
 * Descriptions of the class
 * VirtualShareIndexAtNotifyInfoBeanBuilder.java's implementation：TODO
 * described the implementation of class
 * 
 * @author xiaoqiang 2013-9-12 下午3:27:44
 */
public class VirtualShareIndexAtNotifyInfoBeanBuilder extends ShareAtNotifyInfoBeanBuilder {

    private Logger logger = Logger.getLogger(VirtualShareIndexAtNotifyInfoBeanBuilder.class);

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.util.ShareAtNotifyInfoBeanBuilder#buildBusinessNotifyBody(com.renren.ugc.comment.xoa2.Entry)
     */
    @Override
    public ShareBusinessNotifyBody buildBusinessNotifyBody(Entry share) {
        ShareBusinessNotifyBodyBuilder builder = new ShareBusinessNotifyBodyBuilder();
        builder.setValue("virtual_id", share.getEntryProps().get("virtual_id"));
        builder.setValue(KEY_SHARE_ID, String.valueOf(share.getId()));
        builder.setValue(KEY_SHARE_USER_ID, String.valueOf(share.getOwnerId()));
        if ((Integer.valueOf(share.getEntryProps().get("share_type")) == IShareConstants.URL_TYPE_PHOTO)
                && !StringUtils.isNotBlank(share.getEntryProps().get("share_title"))) {
            builder.setValue("doing_content",
                    share.getEntryProps().get(IShareConstants.META_KEY_TYPE22_ALBUM_NAME));
        } else {
            builder.setValue("doing_content", share.getEntryProps().get("share_title"));
        }
        // TODO 经纬那提供分享相册的照片终端页页提醒type
        builder.setSchemaId(174);
        builder.setType(174);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "@分享相册的照片终端页，title:%s，type:%s,link:http://share.renren.com/share/%d/%d/?virtualId=%d",
                    share.getEntryProps().get(IShareConstants.META_KEY_TYPE22_ALBUM_NAME),
                    share.getEntryProps().get("share_type"), share.getOwnerId(), share.getId(),
                    share.getEntryProps().get("virtual_id")));
        }
        return builder.build();
    }

}
