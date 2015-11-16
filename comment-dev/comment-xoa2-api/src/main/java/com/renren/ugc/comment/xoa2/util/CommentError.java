package com.renren.ugc.comment.xoa2.util;

/**
 * Hold all the error code and msg for comment api
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentError {

    /*
     * error codes
     */
    public static final int UNKNOWN_ERROR = -1;

    public static final int SUCCESS = 0;

    public static final int PROHIBITED_BY_ANTISPAM = 1;

    public static final int EMPTY_CONTENT = 2;

    public static final int CONTENT_TOO_LONG = 3;

    public static final int PERMISSON_DENY = 4;

    public static final int COMMENT_NOT_EXISTS = 5;

    public static final int COMMENT_NOT_DELETED = 6;

    public static final int STORAGE_ERROR = 7;

    public static final int INVALID_COMMENT_TYPE = 8;

    public static final int COMMENT_IS_NOT_VOICE = 9;
    
    public static final int COMMENT_GETENTRY_ERROR = 10;
    
    public static final int COMMENT_TOO_FAST = 11;
    
    public static final int COMMENT_PARAMS_INVALID = 12;
    
    public static final int PROHIBITED_BY_ADMIN = 13;
    
    public static final int ANTISPANM_EXCEPTION = 14;

    /*
     * error messages
     */
    public static final String SUCCESS_MSG = "操作成功";

    // antispam error will directly use antispam's msg

    public static final String EMPTY_CONTENT_MSG = "评论内容不能为空";

    public static final String CONTENT_TOO_LONG_MSG = "发布的内容过长";

    public static final String PERMISSION_DENY_MSG = "您没有权限发布";

    public static final String COMMENT_NOT_EXISTS_MSG = "该评论不存在或者已删除";

    public static final String COMMENT_NOT_DELETED_MSG = "在已经删除的评论中找不到要恢复的评论";

    public static final String STORAGE_ERROR_MSG = "存储异常";

    public static final String INVALID_COMMENT_TYPE_MSG = "无效的评论业务类型";

    public static final String COMMENT_IS_NOT_VOICE_MSG = "该评论不是语音评论";
    
    public static final String COMMENT_GETENTRY_ERROR_MSG = "获取评论实体失败";
    
    public static final String COMMENT_TOO_FAST_MSG = "请您不要频繁发布相同内容";
    
    public static final String COMMENT_PARAMS_INVALID_MSG = "参数无效";
    
    public static final String PROHIBITED_BY_ADMIN_MSG = "已被管理员禁言，发布失败";
    
    public static final String ANTISPANM_EXCEPTION_MSG = "评论被过滤";
    
}
