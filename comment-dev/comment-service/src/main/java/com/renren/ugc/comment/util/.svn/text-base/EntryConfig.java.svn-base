package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import com.renren.ugc.comment.entry.AlbumEntryService;
import com.renren.ugc.comment.entry.BlogEntryService;
import com.renren.ugc.comment.entry.CampusAlbumPostEntryService;
import com.renren.ugc.comment.entry.CampusExcellentEntryService;
import com.renren.ugc.comment.entry.CampusPostEntryService;
import com.renren.ugc.comment.entry.CampusTopPostEntryService;
import com.renren.ugc.comment.entry.EntryGetService;
import com.renren.ugc.comment.entry.PhotoEntryService;
import com.renren.ugc.comment.entry.ShareAlbumPhotoEntryService;
import com.renren.ugc.comment.entry.ShareEntryService;
import com.renren.ugc.comment.entry.ShortVideoEntryService;
import com.renren.ugc.comment.entry.StatusEntryService;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 * 
 */
public class EntryConfig {

    private static Map<CommentType, EntryGetService> entryMaps = new HashMap<CommentType, EntryGetService>();

    static {
        entryMaps.put(CommentType.Blog, BlogEntryService.getInstance());
        entryMaps.put(CommentType.Share, ShareEntryService.getInstance());
        entryMaps.put(CommentType.Photo, PhotoEntryService.getInstance());
        entryMaps.put(CommentType.Status, StatusEntryService.getInstance());
        entryMaps.put(CommentType.Album, AlbumEntryService.getInstance());
        entryMaps.put(CommentType.ShareAlbumPhoto, ShareAlbumPhotoEntryService.getInstance());
        entryMaps.put(CommentType.ShortVideo, ShortVideoEntryService.getInstance());
        entryMaps.put(CommentType.CampusPost, CampusPostEntryService.getInstance());
        entryMaps.put(CommentType.CampusAlbumPost, CampusAlbumPostEntryService.getInstance());
        entryMaps.put(CommentType.CampusTop, CampusTopPostEntryService.getInstance());
        entryMaps.put(CommentType.CampusExcellent, CampusExcellentEntryService.getInstance());
    }

    public static EntryGetService getEntryService(CommentType type) {
        return entryMaps.get(type);
    }

    /**
     * entry内容,用于状态评论的通知
     */
    public static final String ENTRY_CONTENT = "content";

    /**
     * 状态entry新鲜事类型
     */
    public static final String ENTRY_STYPE = "stype";

    /**
     * entry标题,用于通知
     */
    public static final String ENTRY_TITLE = "title";

    /**
     * entry的control,用于"与我相关"
     */
    public static final String ENTRY_CONTROL = "control";

    /**
     * entry的创建时间,用于"@"过滤
     */
    public static final String ENTRY_CREATE_TIME = "create_time";

    /**
     * 分享的全站md5
     */
    public static final String ENTRY_URL_MD5 = "url_md5";

    /**
     * entry是否密码保护
     */
    public static final String ENTRY_IS_PASSWORD_PROTECTED = "is_password_protected";

    /**
     * 父entry的创建时间，比如对于照片来说，就是相册的
     */
    public static final String ENTRY_PARENT_CREATE_TIME = "parent_create_time";

    /**
     * 父entry是否密码保护，比如对于照片来说，就是相册的
     */
    public static final String ENTRY_PARENT_IS_PASSWORD_PROTECTED = "parent_is_password_protected";

    /**
     * 父entry的id，比如对于照片来说，就是相册的
     */
    public static final String ENTRY_PARENT_ID = "parent_id";

    /**
     * 父entry的control
     */
    public static final String ENTRY_PARENT_CONTROL = "parent_control";

    /**
     * 父entry的名字
     */
    public static final String ENTRY_PARENT_NAME = "parent_name";

    /**
     * 照片的headurl
     */
    public static final String ENTRY_HEADURL = "headurl";

    /**
     * 照片的largeurl
     */
    public static final String ENTRY_LARGEURL = "largeurl";

    /**
     * 照片是否通过多张上传
     */
    public static final String ENTRY_IS_MULTI = "entry_is_multi";

    /**
     * 照片是否有头像新鲜事
     */
    public static final String ENTRY_IS_HEAD_FEED = "entry_is_head_feed";

    /**
     * 分享类型
     */
    public static final String ENTRY_SHARE_TYPE = "share_type";

    /**
     * 分享标题
     */
    public static final String ENTRY_SHARE_TITLE = "share_title";

    /**
     * 照片是否有圈人信息
     */
    public static final String ENTRY_IS_TAG = "entry_is_tag";

    /**
     * 分享的缩略图
     */
    public static final String ENTRY_SHARE_THUMB_URL = "thumb_url";

    /**
     * 照片是否有with
     */
    public static final String ENTRY_IS_WITH = "entry_is_with";
    
    /**
     * 照片是否有708新鲜事
     */
    public static final String ENTRY_IS_708Feed = "entry_is_with708Feed";

    /**
     * 分享虚拟ID
     */
    public static final String ENTRY_SHARE_VIRTUAL_ID = "virtual_id";
    
    /**
     * 分享真实ID
     */
    public static final String ENTRY_SHARE_REAL_ID = "real_id";

    /**
     * 分享blogID
     */
    public static final String ENTRY_SHARE_RESOURCE_ID = "resource_id";

    /**
     * 分享资源用户ID
     */
    public static final String ENTRY_SHARE_RESOURCE_USER_ID = "resource_user_id";

    /**
     * 分享的URL
     */
    public static final String ENTRY_SHARE_URL = "share_url";

    /**
     * 足迹站点名字
     */
    public static final String ENTRY_ZUJISITE_NAME = "entry_site_name";

    /**
     * 足迹站点id
     */
    public static final String ENTRY_ZUJISITE_ID = "entry_site_id";

    /**
     * 足迹id
     */
    public static final String ENTRY_ZUJI_ID = "entry_zuji_id";

    /**
     * 足迹itemId
     */
    public static final String ENTRY_ZUJIITEM_ID = "entry_item_id";

    /**
     * 含有zuji信息
     */
    public static final String ENTRY_HAS_ZUJI = "entry_has_zuji";
    
    /**
     * entry的url
     */
    public static final String ENTRY_URL = "entry_url";
    
    /**
     * 论坛的学校id
     */
    public static final String ENTRY_SCHOOL_ID = "school_id";
    
    /**
     * 论坛的帖子的创建时间
     */
    public static final String ENTRY_CAMPUS_POST_CREATETIME = "campus_post_createtime";
    
    /**
     * 照片的子相册id
     */
    public static final String ENTRY_CHILD_PARENT_ID = "child_parent_id";
    
    /**
     * 校园的分享类型
     */
    public static final String ENTRY_CAMPUS_SHARE_TYPE = "campus_share_type";
    
    /**
     * 校园的分享名字
     */
    public static final String ENTRY_CAMPUS_SHARE_NAME = "campus_share_name";
    
    /**
     * 校园的分享时间
     */
    public static final String ENTRY_CAMPUS_SHARE_TIME = "campus_share_time";
    
    /**
     * 校园的分享url
     */
    public static final String ENTRY_CAMPUS_SHARE_IMG = "campus_share_img";

}
