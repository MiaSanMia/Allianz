/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util.share;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.ShareBusinessNotifyBody;

/**
 * Descriptions of the class ShareListAtNotifyInfoBeanBuilder.java's
 * implementation：TODO described the implementation of class
 * 
 * @author xiaoqiang 2013-9-12 下午3:43:17
 */
public class ShareListAtNotifyInfoBeanBuilder extends ShareAtNotifyInfoBeanBuilder {

    private Logger logger = Logger.getLogger(ShareListAtNotifyInfoBeanBuilder.class);

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.util.ShareAtNotifyInfoBeanBuilder#buildBusinessNotifyBody(com.renren.ugc.comment.xoa2.Entry)
     */
    @Override
    public ShareBusinessNotifyBody buildBusinessNotifyBody(Entry share) {
        ShareBusinessNotifyBodyBuilder builder = new ShareBusinessNotifyBodyBuilder();
        builder.setValue(KEY_COMMENT_ID, String.valueOf(0));
        builder.setSchemaId(168);
        builder.setType(168);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "@分享列表页，title:%s，type:%s,link:http://www.renren.com/home#//share/share/%d?from=homeleft",
                    share.getEntryProps().get("share_title"),
                    share.getEntryProps().get("share_type"), share.getOwnerId()));
        }
        return builder.build();
    }

}
