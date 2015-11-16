package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.renren.ugc.common.util.UgcVipAdapter;
import com.renren.ugc.comment.model.Comment;
import com.xiaonei.platform.core.model.WUserCache;
import com.xiaonei.platform.core.model.factory.WUserCacheFactory;
import com.xiaonei.platform.core.opt.OpiConstants;
import com.xiaonei.xce.usercache.UserCacheAdapter;
import com.xiaonei.xce.vipiconinfo.VipIconInfo;

/**
 * @author wangxx
 * 
 *         封装对vip业务的操作,由于引用xiaonei-vip包的代价太大,引入了许多非必须的包，
 *         通过读vip源码，采用和vip业务一样的方法，从中间层的UserCache中读取的用户的vip icon url
 * 
 */
public class VipUtil {

    private static final Logger logger = Logger.getLogger(VipUtil.class);
    
    private static VipUtil instance = new VipUtil();

    private final String VIP_MEMBER_ICON_CUSTOM_URL = "http://" + OpiConstants.domainStatic
            + "/n/core/feed/cssimg/feed-vip-%02d-%02d.png";

    private UserCacheAdapter<WUserCache> userCacheAdapter = new UserCacheAdapter<WUserCache>(
            new WUserCacheFactory());
    
    private VipUtil(){
    }
    
    public static VipUtil getInstance(){
        return instance;
    }

    public void setVipIconUrlToComment(final List<Comment> comments) {
        
        if (comments == null || comments.isEmpty()) {
            return;
        }
     
        //1. get all authorIds
        List<Integer> authorIds = new ArrayList<Integer>(comments.size());
        for(Comment c:comments){
            authorIds.add(c.getAuthorId());
        }
        
        //2.generate vip urls
        Map<Integer, String> vipIconUrls =  new HashMap<Integer, String>();
        try{
            Map<Integer, WUserCache> wuserCacheMap = getVipUserCacheMap(authorIds);
            if (wuserCacheMap != null && !wuserCacheMap.isEmpty()) {
                    for (Entry<Integer, WUserCache> li : wuserCacheMap.entrySet()) {
                        String iconUrl = generateVipIconUrl(li.getValue());
                        if (StringUtils.isNotBlank(iconUrl)) {
                            vipIconUrls.put(li.getKey(), iconUrl);
                        }
                }
            }
        } catch (Exception e){
            logger.error("generateVipIconUrlMap exception",e);
        }
        
        //3.set url to comment
        if(vipIconUrls != null){
            for (Comment c : comments){
                c.setAuthorVipIcon(vipIconUrls.get(c.getAuthorId()) == null ? "":vipIconUrls.get(c.getAuthorId()));
            }
        }
    }
    
    private  String generateVipIconUrl(WUserCache wUserCache) {
        List<VipIconInfo> list = wUserCache.getVipIconInfoList();
        String vipIconUrl = null;
        if (list != null && list.size() > 0) {
            try {
                VipIconInfo vipIconInfo = list.get(0);
                vipIconUrl = getVipCustomIconUrl(vipIconInfo);
                return vipIconUrl;
            } catch (Exception e) {
                logger.error("VipUtil getVipCustomIconUrl error",e);
            }
        }
        return vipIconUrl;
    }

    private  Map<Integer, WUserCache> getVipUserCacheMap(List<Integer> userIds) {
        try {
            List<Integer> types = new ArrayList<Integer>();
            types.add(0);
            Map<Integer, WUserCache> userCacheMap = userCacheAdapter
                    .getUserCacheMapWithVipIconInfo(userIds, types);
            return userCacheMap;
        } catch (Exception e) {
            logger.error("VipUtil getVipUserCacheMap error", e);
            return Collections.emptyMap();
        }
    }

    //copy from vip..>_<
    private String getVipCustomIconUrl(VipIconInfo vipIconInfo) {
        String ret = null;
        if (vipIconInfo != null) {
            int type = 0;
            int iconid = vipIconInfo.getIconId();
            int level = vipIconInfo.getLevel();
            int status = vipIconInfo.getStatus();
            switch (type) {
                case 0:
            }
            if (iconid == 0) iconid = 1;

            if (level == 0) level = 1;

            if (status == 1) ret = String.format(VIP_MEMBER_ICON_CUSTOM_URL,
                    new Object[] { Integer.valueOf(iconid), Integer.valueOf(level) });
        }
        return ret;
    }

}
