package com.renren.ugc.comment.util;


/**
 * @author wangxx
 *
 * 评论中心的一些常量
 */
public class CommentCenterConsts {
    
    public static final String ORIGIN_COMMENT_ID_FIELD = "originalCommentId";
    
    /**
     * 关联评论的评论数量在metadata字段中存储的key
     */
    public static final String LINKED_COMMENT_COUNT = "linkedCommentCount";
    
    /**
     * 关联评论的评论id在metadata字段中存储的key
     */
    public static final String LINKED_COMMENT_ID = "linkedCommentId";
    
    /**
     * 关联评论的评论ownerId在metadata字段中存储的key
     */
    public static final String LINKED_COMMENT_OWNER_ID = "linkedCommentOwnerId";
    
    /**
     * 关联评论的评论entryId在metadata字段中存储的key
     */
    public static final String LINKED_COMMENT_ENTRY_ID = "linkedCommentEntryId";
    
    /**
     * 关联评论的评论entryType在metadata字段中存储的key
     */
    public static final String LINKED_COMMENT_ENTRY_TYPE = "linkedCommentEntryType";
    
    public static final long MAX_SHARE_COMMENT_ID = 5000000000L;
    
    /**
     * 评论中心的数据库是否包含相片的评论
     */
    public static final String CENTER_INCLUDE_PHOTO_COMMENT = "center.include.photo.comment";
    
    /**
     * 评论中心的数据库是否包含相册的评论
     */
    public static final String CENTER_INCLUDE_ALBUM_COMMENT = "center.include.album.comment";
    
    /**
     * 评论中心的数据库是否包含状态的评论
     */
    public static final String CENTER_INCLUDE_STATUS_COMMENT = "center.include.status.comment";
    
    /**
     * 评论中心的数据库是否包含分享的评论
     */
    public static final String CENTER_INCLUDE_SHARE_COMMENT = "center.include.share.comment";
    
    /**
     * 好友评论的好友列表来自entry实体拥有者的好友
     */
    public static final String FRIEND_LIST_FROM_OWNER = "friend.list.from.owner";    
    
    /**
     * 评论的悄悄话toId,copy from ugc
     */
    public static final String WHISPER_TOID_MARK = "<Toid/>";
    
    /**
     * 评论的悄悄话mark,copy from ugc
     */
    public static final String WHISPER_MARK = "<xiaonei_only_to_me/>";
    
    /**
     * 评论是否从wap来
     */
    public static final String WAP_MARK = "<xiaonei_wap/>";
    
    /**
     * cache中取不到数据时，long默认返会的数据
     */
    public static final int CACHE_DEFUALT_RETURN_LONG_VALUE = -1;
    
    /**
     * seconds of one day
     */
    public static final int EXPIRE_HOUR = 3600 ;

    /**
     * seconds of one day
     */
    public static final int EXPIRE_DAY = EXPIRE_HOUR * 24;
    
    /**
     * seconds of cache expire time - 15 days
     */
    public static final int EXPIRE_TIME_15_DAYS = EXPIRE_DAY * 15;
    

    /**
     * seconds of cache expire time - 30 days
     */
    public static final int EXPIRE_TIME_30_DAYS = EXPIRE_DAY * 30;
    
    /**
     * 新鲜事字数限制 
     */
    public static final int FEED_COMMENT_LIMIT = 140;
    
    /**
     * 1 min
     */
    public static final int EXPIRE_MINS = 60 ;
    
    /**
     * 	KV 返回的错误码
     */
    public static final int ERR_KV_INT_CODE = -1; 
    
    /**
     * "也评论"一天之内发送的最大次数
     */
    public static final int MAX_ALSO_COMMENT_COUNT = 5;
    
    /**
     * albumname in share metaData
     */
    public static final String SHARE_METADATA_ALBUMNAME_KEY = "albumname";
    
    /**
     * “楼层"在map中的key
     */
    public static final String FLOOR_KEY = "floor";
    
    /**
     * 帖子终端页的url
     */
    public static final String POST_TERMINAL_FORMAT_URL_STRING = "http://xiaoyuan.renren.com/posts/%d/%d";
    
    /**
     * 图帖终端页的url
     */
    public static final String ALBUM_POST_TERMINAL_FORMAT_URL_STRING = "http://xiaoyuan.renren.com/album/%d/%d";
    
    /**
     * “author名字"在map中的key
     */
    public static final String AUTHOR_NAME = "author_name";
    
    /**
     * "author url"在map中的key
     */
    public static final String AUTHOR_URL = "author_url";
    
    /**
     *  "校园主页"标题截取的内容长度
     */
    public static final int MAX_CAMPUS_TRIM_LENGTH = 12;
    
    /**
     * 评论中图片url在metaData字段中存储的key
     */
    public static final String COMMENT_PIC_URL_KEY = "commentPicUrl";
    
    /**
     * 评论中图片id在metaData字段中存储的key
     */
    public static final String COMMENT_PIC_ID_KEY = "commentPicId";
    
    /**
     * 校园主页置顶帖子的url
     */
    public static final String TOP_POST_TERMINAL_FORMAT_URL_STRING = "http://xiaoyuan.renren.com/topad/view?id=%d";
    
    /**
     * 校园主页置顶帖子的url
     */
    public static final String EXCELLENT_POST_TERMINAL_FORMAT_URL_STRING = "http://xiaoyuan.renren.com/excellent/%d/%d";

    

}
