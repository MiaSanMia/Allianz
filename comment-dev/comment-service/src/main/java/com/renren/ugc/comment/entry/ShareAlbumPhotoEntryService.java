/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.entry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.renren.privacy.permission.define.PrivacySourceControlType;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.EntryConfig;

/**
 * Descriptions of the class ShareAlbumPhotoEntryService.java's
 * implementation：TODO described the implementation of class
 * 
 * @author xiaoqiang 2013-9-22 下午5:32:14
 */
public class ShareAlbumPhotoEntryService implements EntryGetService {

    private static final Logger logger = Logger.getLogger(ShareAlbumPhotoEntryService.class);

    private static ShareAlbumPhotoEntryService instance = new ShareAlbumPhotoEntryService();

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.entry.EntryGetService#getEntryInfo(int, int, long, com.renren.ugc.comment.xoa2.Comment, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public com.renren.ugc.comment.model.Entry getEntryInfo(int actorId, int entryOwnerId,
            long entryId, Comment comment, CommentStrategy strategy) throws UGCCommentException {
        com.renren.ugc.comment.model.Entry entry = new com.renren.ugc.comment.model.Entry();
        entry.setId(entryId);
        entry.setOwnerId(entryOwnerId);
        Map<String, String> pros = new HashMap<String, String>();
        if (StringUtils.isNotBlank(strategy.getShareTitle())) {
            pros.put(EntryConfig.ENTRY_SHARE_TITLE, strategy.getShareTitle());
        }
        if (StringUtils.isNotBlank(strategy.getShareRealId())) {
            pros.put(EntryConfig.ENTRY_SHARE_REAL_ID, strategy.getShareRealId());
        }
        
        //分享当中没有隐私
        pros.put(EntryConfig.ENTRY_CONTROL, PrivacySourceControlType.Open.getType() + "");
        
        pros.put(EntryConfig.ENTRY_CREATE_TIME, (new Date()).getTime() + "");

        entry.setEntryProps(pros);

        return entry;
    }

    /**
     * @return
     */
    public static ShareAlbumPhotoEntryService getInstance() {
        return instance;
    }

}
