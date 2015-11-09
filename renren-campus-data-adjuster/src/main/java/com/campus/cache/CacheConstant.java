package com.campus.cache;

/**
 * @author lei.xu1
 * @date 2014年3月5日
 * @desc cache 中key
 */

public enum CacheConstant {
    /**
     * 单个帖子（包括帖子的内容）
     */
    SINGLE_POSTS("p_", TimeConstant.MINUTE * 5),
    /**
     * 某个版块下的最初几十条帖子列表的key，见
     * {@link CampusConstant#FOREMOST_OF_MODULE_CACHE_LIST_SIZE}
     */
    FOREMOST_POSTS_LIST("fpl_", TimeConstant.MINUTE * 10),
    /**
     * 帖子中可编辑的部分（包括标题，简介，学校id），目前用评论过的帖子展示
     */
    CHANGEABLE_CONTENT_FOR_POSTS("p_", 0),
    /**
     * 贴图中可编辑的部分（包括描述，封面信息），目前用评论过的帖子展示
     */
    CHANGEABLE_CONTENT_FOR_ALBUM("a_", 0),

    /**
     * 某个版块下的置顶帖子数量
     */
    TOP_COUNT("", 0),

    /**
     * 普通帖子的浏览量
     */
    POSTS_VIEW_COUNT("p_", 0),

    /**
     * 贴图帖子的浏览量
     */
    ALBUM_VIEW_COUNT("a_", 0),

    /**
     * 某一学校管理员列表
     */
    SCHOOL_ADMINS("school_admins_", 0),

    /**
     * 安全管理员列表
     */
    SECURE_ADMINS("secure_admins_", 0),

    /**
     * 学校投票者列表
     */
    SCHOOL_VOTERS("school_voters_", 0),

    /**
     * 学校信息 7day
     */
    SCHOOL("school_", TimeConstant.DAY * 7),

    /**
     * 校友信息， 根据需求增加的，减轻接口的访问压力 时间没定，暂定7天
     */
    SCHOOLMATE_INFO("schoolmate_", TimeConstant.DAY * 7),

    /**
     * 热门帖子列表 7 day
     */
    HOT_POSTS("hotposts_", TimeConstant.DAY * 7),
    
    /**
     * 热门帖子列表 7 day
     */
    HOT_ALBUM("hotalbum_", TimeConstant.DAY * 7),

    /**
     * 每个版块第一页帖子列表开头的几个帖子的id
     */
    TOP_POSTS_IDS("", TimeConstant.HOUR * 2),

    /**
     * 某个版块下的帖子数量
     */
    MODULE_POSTS_COUNT("", TimeConstant.HOUR * 2),

    /**
     * 某个学校下的帖子数量
     */
    SCHOOL_POSTS_COUNT("", 0),

    /**
     * 已加人品
     */
    RP_POST("rppost_", TimeConstant.DAY), // 发帖人品
    RP_COMMENT("rpcomment_", TimeConstant.DAY), //回复人品
    RP_SHARE("rpshare_", TimeConstant.DAY), //分享人品
    RP_LOGIN("rplogin_", TimeConstant.DAY), //分享人品

    /**
     * 最近七天评论帖子（用户计算热门帖子）
     */
    WEEK_COMMENT("weekcomment_", TimeConstant.DAY * 7),

    //-------------------------匿名用户模块------------------------------
    NICKNAME_EDIT_TIME_MARK("nickname_edit_mark_", 1 * TimeConstant.MONTH), 
    IS_NAME_EXIST_IN_CACHE("name_exist_", 0),
    AUTO_ANONY_NAME("auto_", 5 * TimeConstant.MINUTE),
    //-------------------------------------------------------------------

    //------------------------版块管理模块---------------------------------
    SCHOOL_MODULE_CACHE_PREFIX("school_module_", TimeConstant.DAY * 7), 
    MODULE_CACHE_PREFIX("module_", TimeConstant.DAY * 7),

    //-------------------------------------------------------------------
    
    
    //-------------------------广场---------------------------------------
    /** 学校帖子列表 */
    PIAZZA_SCHOOL_LIST("piazza_school_list_",TimeConstant.HOUR * 1),
    /** 个人经过排序的帖子列表 */
    PIAZZA_USER_LIST("piazza_user_list_",TimeConstant.HOUR * 1),
    /** 用户已读帖子记录 */
    PIAZZA_USER_READ("piazza_user_read_",TimeConstant.MONTH *12),
    /** 用户屏蔽的帖子记录 */
    PIAZZA_USER_REMOVE("piazza_user_remove_",TimeConstant.MONTH * 12),
    /** 用户每次排完序的帖子列表里最小的feedId*/
    PIAZZA_USER_LAST_FEED_ID("piazza_user_last_feed_id_",TimeConstant.DAY),
    //-------------------------------------------------------------------
	TOP_AD_CACHE_PREFIX("top_ad_", TimeConstant.DAY);

    private String key;

    private int expiredTime;

    private CacheConstant(String key, int expiredTime) {
        this.key = key;
        this.expiredTime = expiredTime;
    }

    public String getKey() {
        return key;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

}
