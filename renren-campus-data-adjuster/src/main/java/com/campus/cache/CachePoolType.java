package com.campus.cache;

/**
 * @author shufeng.wang
 * @author lei.xu1
 * @date 2014年3月14日
 * @desc cache客户端类型
 */
public enum CachePoolType {
	
    /**
     * 某个版块下的图贴列表
     */
    FOREMOST_ALBUM_LIST("FOREMOST_ALBUM_LIST"),
    
    /**
     * 单个图帖
     */
    SINGLE_ALBUM("SINGLE_ALBUM"),
    
    /**
     * 某个图帖的照片列表
     */
    PHOTO_LIST("PHOTO_LIST"),
    
    /**
     * 单个帖子
     */
    SINGLE_POSTS("SINGLE_POSTS"),
    
    /**
     * 某个版块下最初几十条帖子列表
     */
    FOREMOST_POSTS_LIST("FOREMOST_POSTS_LIST"),
    
    /**
     * 版块下开头展示的几条置顶帖子的id
     */
    SHOW_TOP_IDS("SHOW_TOP_IDS"),
    
    /**
     * 某个版块下的帖子数量
     */
    MODULE_POSTS_COUNT("MODULE_POSTS_COUNT"),
    
    /**
     * 某个学校的帖子数量
     */
    SCHOOL_POSTS_COUNT("SCHOOL_POSTS_COUNT"),
    
    /**
     * 帖子对象中可编辑的部分（用于列表也展示，不包含内容），比如标题，简介（包含贴图）
     */
    CHANGEABLE_CONTENT_FOR_POSTS_AND_ALBUM("CHANGEABLE_CONTENT_FOR_POSTS_AND_ALBUM"),
    
    /**
     * 帖子的浏览数，包含贴图部分
     */
    VIEW_COUNT("VIEW_COUNT"),
    
    /**
     * 某个版块下置顶帖子数量，包含贴图部分
     */
    TOP_COUNT("TOP_COUNT"),

    /**
     * 回帖信息，以秒数作为key
     */

    CAMPUS_POST_REPLY("CAMPUS_POST_REPLY"),
    
    /**
     * 热门帖子
     *  *  评估 key:100 ( 所有学校总量 1.3w )
     *     value：list[ <=100 * (8+4+4+8+256+1024)]        ==       130.4K]
     *     100所学校所需13.04M 加上一些其他参数约 14M或15M
     */
    HOT_POSTS("HOT_POSTS"),
    
    /**
     * 学校信息
     * 4，4，50，1024
     * 100所学校所需 约11M
     */
    SCHOOL_ADMIN_VOTE("SCHOOL_ADMIN_VOTE"),
    
    /**
     * 版块信息和匿名用户放在一个area里
     */
    MODULE_AND_ANONY("MODULE_AND_ANONY"),
    
    /**
     * 是否禁言
     */
    CAMPUS_BANNED_TO_POST_LIST("CAMPUS_BANNED_TO_POST_LIST"),
    
    /**
     * 连续登录信息
     */
    CAMPUS_SUCCESSIVE_LOGIN_LIST("CAMPUS_SUCCESSIVE_LOGIN_LIST"),
    
    CAMPUS_LOGINED_OR_NOT("CAMPUS_LOGINED_OR_NOT");
    
    private String areaName;
    
    private CachePoolType(String areaName) {
    	this.areaName = areaName;
    }
    
}
