/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util;

import com.renren.ugc.comment.model.ShareBusinessNotifyBody;
import com.renren.ugc.comment.util.share.ShareBusinessNotifyBodyBuilder;
import org.apache.log4j.Logger;

import com.renren.app.share.IShareConstants;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.util.share.ShareAtNotifyInfoBeanBuilder;

/**
 * Descriptions of the class TerminalShareAtNotifyInfoBeanBuilder.java's
 * implementation：终端页分享AtNotifyInfoBean构建<br/>
 * 注意：这里不包括分享相册下的照片终端页
 * 
 * @author xiaoqiang 2013-9-12 下午3:20:26
 */
public class TerminalShareAtNotifyInfoBeanBuilder extends ShareAtNotifyInfoBeanBuilder {

    private Logger logger = Logger.getLogger(TerminalShareAtNotifyInfoBeanBuilder.class);

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.util.ShareAtNotifyInfoBeanBuilder#buildBusinessNotifyBody(com.renren.ugc.comment.xoa2.Entry)
     */
    @Override
    public ShareBusinessNotifyBody buildBusinessNotifyBody(Entry share) {
        ShareBusinessNotifyBodyBuilder builder = new ShareBusinessNotifyBodyBuilder();
        builder.setSchemaId(167).setType(167);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "@终端页页面，title:%s，type:%s,link:http://share.renren.com/share/%d/%d",
                    share.getEntryProps().get(IShareConstants.META_KEY_TYPE22_ALBUM_NAME),
                    share.getEntryProps().get("share_type"), share.getOwnerId(), share.getId()));
        }
        return builder.build();
    }

}
