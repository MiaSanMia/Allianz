/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.ShareBusinessNotifyBody;
import com.renren.ugc.comment.util.share.ShareAtNotifyInfoBeanBuilder;
import com.renren.ugc.comment.util.share.ShareBusinessNotifyBodyBuilder;

/**
 * Descriptions of the class BlogShareAtNotifyInfoBeanBuilder.java's
 * implementation：日志分享@notifyInfo构建
 * 
 * @author xiaoqiang 2013-9-12 下午3:12:51
 */
public class BlogShareAtNotifyInfoBeanBuilder extends ShareAtNotifyInfoBeanBuilder {

    private Logger logger = Logger.getLogger(this.getClass());

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.util.ShareAtNotifyInfoBeanBuilder#buildBusinessNotifyBody(com.renren.ugc.comment.xoa2.Entry)
     */
    @Override
    public ShareBusinessNotifyBody buildBusinessNotifyBody(Entry share) {
        ShareBusinessNotifyBodyBuilder builder = new ShareBusinessNotifyBodyBuilder();
        builder.setSchemaId(169).setType(169);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "@日志分享终端页，title:%s，type:%s，link:http://blog.renren.com/share/%d/%d",
                    share.getEntryProps().get("share_type"),
                    share.getEntryProps().get("share_type"), share.getOwnerId(), share.getId()));
        }
        return builder.build();
    }

}
