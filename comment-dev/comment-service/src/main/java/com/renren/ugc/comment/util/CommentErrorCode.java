package com.renren.ugc.comment.util;

/**
 * 接口收口错误码，完全迁移后会删除
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentErrorCode {

    //被回复用户没有发表过评论
    public static final int INVALID_TARGET_USER_CODE = -9;

    //服务器超时
    public static final int SERVICE_TIMEOUT = -10;

    //受保护资源不可访问
    public static final int PROTECTED_SOURCE = -11;

    //资源不存在
    public static final int ENTRY_NOT_EXIST = -12;
}
