package com.campus.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.renren.tair.area.feature.Area;
import com.renren.tair.util.TairGroup;

/**
 * 用来封装cache的接口，当cache变动时（例如由MemCache和XCache改为通用Cache时），只需要修改此类即可；另一方面，
 * 从这个类产生后， 分享里用的cache都可以通过调用这个类的方法来创建、写、读、删除等操作
 * 
 * @author Wang Shufeng [shufeng.wang@renren-inc.com]
 * @author lei.xu1
 * @since Mar 12, 2014
 */
public class CacheClientFactory {

    /** 自己封装的缓存池类型CachePoolType与通用Cache的缓存池类型的对应关系 */
    @SuppressWarnings("serial")
    private static Map<CachePoolType, CacheClient> cacheMap = new ConcurrentHashMap<CachePoolType, CacheClient>() {
        {
            put(CachePoolType.SINGLE_POSTS, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_SINGLE_POST));
            
            put(CachePoolType.FOREMOST_POSTS_LIST, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_POST_LIST));
            
            put(CachePoolType.CHANGEABLE_CONTENT_FOR_POSTS_AND_ALBUM, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_POSTS));
            
            put(CachePoolType.VIEW_COUNT, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_VIEW_COUNT));
            
            put(CachePoolType.TOP_COUNT, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_TOP_POST_COUNT));
            
            // 图帖相关
            put(CachePoolType.FOREMOST_ALBUM_LIST, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_PIC_POST_LIST));
            
            put(CachePoolType.SINGLE_ALBUM, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_SINGLE_PIC_POST));
            
            put(CachePoolType.PHOTO_LIST, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_PHOTO_LIST));

            put(CachePoolType.CAMPUS_POST_REPLY, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_POST_REPLY));// Need to add the constant
            
            //学校相关信息cache ： 包括，学校信息，学校管理员信息，学校投票信息
            put(CachePoolType.SCHOOL_ADMIN_VOTE, new TairCacheClient(TairGroup.FEATURE,
                    Area.CAMPUS_SCHOOL)); //Area.CAMPUS_SCHOOL

            //热门帖子列表
            put(CachePoolType.HOT_POSTS, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_HOT_POSTS));//Area.CAMPUS_HOT_POSTS
            
            put(CachePoolType.MODULE_AND_ANONY, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_ANOMYMOUS_USER_SECTION));
            
            put(CachePoolType.SHOW_TOP_IDS, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_TOP_POST_IDS));
            
            put(CachePoolType.MODULE_POSTS_COUNT, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_MODULE_POSTS_COUNT));
            
            put(CachePoolType.SCHOOL_POSTS_COUNT, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_SCHOOL_POSTS_COUNT));
            
            put(CachePoolType.CAMPUS_BANNED_TO_POST_LIST, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_BANNED_TO_POST_LIST));
            
            put(CachePoolType.CAMPUS_SUCCESSIVE_LOGIN_LIST, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_SUCCESSIVE_LOGIN_LIST));
            
            put(CachePoolType.CAMPUS_LOGINED_OR_NOT, new TairCacheClient(TairGroup.FEATURE,
            		Area.CAMPUS_LOGINED_OR_NOT));
            
        }
    };

    /** 获取池子的客户端调用对象 */
    public static CacheClient getClient(CachePoolType cachePoolType) {
        return cacheMap.get(cachePoolType);
    }

}
