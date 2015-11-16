/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.util;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.antispam.AntiSpamHome;
import com.xiaonei.antispam.model.CheckResult;

/**
 * Descriptions of the class AntispamUtil.java's implementation：share专用 Antispam帮助工具
 * @author xiaoqiang 2013-9-11 下午9:03:08
 */
public class AntispamUtil {
    private static Logger logger = Logger.getLogger(AntispamUtil.class);
    
    /**
     * 检测频率
     * 
     * @param userId 当前用户
     * @param toUserId 接受用户，如果没有，传当前用户
     * @param type
     * @param content
     * @throws OperationFastOrSameException 操作过快或者内容重复
     */
    public static void checkActivity(int userId, int toUserId, int type, String content)
            throws UGCCommentException {

        int result = 0;
        try {
            if (logger.isDebugEnabled()) {
                logger.info(String.format("进入频率检测  params[userId:%d,toUserId:%d,content:%s]:",
                        userId, toUserId, content));
            }

            result = AntiSpamHome.getInstance().checkActivity(userId, toUserId, type, content);
            CheckResult.getShowMessage(result);
            if (logger.isDebugEnabled()) {
                logger.info("频率检测结果:" + result);
            }
        } catch (Exception e) {
            logger.warn("频率检测接口异常:" + e.getMessage());
        }
        if (result > 0) {
            logger.warn(String.format("操作过快：userId=%d, toUserId=%d, type=%d, content=%s", userId,
                    toUserId, type, content));
            throw new UGCCommentException(CommentError.COMMENT_TOO_FAST, CommentError.COMMENT_TOO_FAST_MSG);
        }
    }

}
