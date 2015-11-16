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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.doingubb.tool.DoingUbbReplace;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.model.WUserCache;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;
import com.xiaonei.platform.core.opt.ice.WUserCacheAdapter;

/**
 * Descriptions of the class ShareUBBUtil.java's implementation：share的ubb工具类
 * @author xiaoqiang 2013-9-25 下午6:02:50
 */
public class ShareUBBUtil {
 private static Log logger = LogFactory.getLog(ShareUBBUtil.class);
    
    /**
     * 判断是否是VIP
     * 
     * @param userId
     * @return
     */
    public static boolean isVip(int userId) {

        User host = null;
        try {
            host = WUserAdapter.getInstance().get(userId);
        } catch (Exception e) {
            logger.warn("WUserAdapter获取用户信息失败", e);
        }
        
        boolean canUse = false;
        if (host != null) {
            canUse = (host.getStatus() == User.statusNormal) || host.isVipMember();
        } else {
            canUse = false;
        }
        return canUse;

    }

    /**
     * 判断是否是VIP
     * 
     * @param host 用户信息
     * @return
     */
    public static boolean isVip(WUserCache host) {

        boolean canUse = false;
        if (host != null) {
            canUse = (host.getStatus() == User.statusNormal) || host.isVipMember();
        } else {
            canUse = false;
        }
        return canUse;

    }

    /**
     * 批量获取能发超级表情的用户
     * 
     * @param userIdList
     * @return
     */
    public static Map<Integer, Boolean> getVipUbbUserMaps(List<Integer> userIdList) {
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        Map<Integer, WUserCache> userCacheMap = WUserCacheAdapter.getInstance().getUserCacheMap(
                userIdList);
        if (userCacheMap != null) {
            for (Integer li : userIdList) {
                WUserCache wUserCache = userCacheMap.get(li);
                if (wUserCache != null) {
                    map.put(li, isVip(wUserCache));
                }
            }
        }
        return map;
    }
    
    /**
     * Replace the ubb in content.
     * 
     * @author shufeng.wang@renren-inc.com
     * @param content 包含表情符的待转换内容
     * @param isBigImg 是否是大图标
     * @return
     */
    public static String replaceUbb(String content, boolean isBigImg) {
        return DoingUbbReplace.getInstance().replaceUBB(content, isBigImg);
    }

}
