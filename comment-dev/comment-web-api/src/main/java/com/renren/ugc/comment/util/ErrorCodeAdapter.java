package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import com.renren.ugc.comment.xoa2.util.CommentError;


/**
 * @author wangxx
 * 
 *  现在新鲜事前端，用了自己一套错误码，和"评论中心"现有的错误码不一样，这里做个适配
 *   等以后出来了评论通用框的时候，这个类也就没有作用了
 *   
 *   评论中心的错误码参见{@link:com.renren.ugc.comment.xoa2.util.CommentError}
 *   see alsp http://wiki.d.xiaonei.com/pages/viewpage.action?pageId=24478473
 *   
 *   新鲜事的错误码
 *   1: '请不要从站外提交',
        2: '该状态不存在',
        6: '对不起，请重试。',
        3: '抱歉，您不能发布空状态',
        4: '抱歉，某些信息是不能发布的哦：）谢谢您的谅解。',
        5: '你短时间内发表了太多相同的内容',
        9: '你还不是TA的好友，不能使用\“回复所有人\”',
        11: '该用户不是您的好友, 不能转发其状态',
        12: '参数不完整, 提交失败, 请联系客服',
        15: '请激活账号',
        16: '抱歉，由于对方的隐私设置，你无法进行该操作',
        100: '本公共主页管理员关闭了该公共主页，请稍后再试',
        101: '你现在不是该公共主页的粉丝，成为粉丝后才可回复',
        102: '此公共主页的主人关闭了回复功能，目前不能回复',
        103: '检测到异常无法发布，请尝试刷新页面或重新登录',
        105: '你现在不是该情侣空间的关注者，加入后才可回复',
        106: '该报到不存在',
        107: '回复失败',
        301: '请您不要频繁发布相同内容',
        407: '抱歉，某些信息是不能发布的哦：）谢谢您的谅解',
        998: '输入不能为空',
        999: '发生异常，请尝试刷新页面后再试',
        1030: '抱歉，某些信息是不能发布的哦：）',
        1031: '请您不要频繁发布相同内容。',
        1020: '您没有权限发布。',
        1021: '发布内容太长',
        1052: '该状态不存在，或者已经被删除！',
        10: '系统繁忙,请您稍候再试。',
        6010014: '获取日志失败，请您稍后再试。',
        6020109: '该日志不存在！',
        6030110: '由于对方的隐私设置，您不能留言。',
        6030101: '您的留言权限被暂时封禁，请联系管理员。',
        6030107: '抱歉，您已超过每日的评论限额！（<a href=\""+ DomainUtil.getUrlMain()+ "/notselectuser.do?action=no\">成为星级用户</a>）可继续评论。',
        6030103: '抱歉，某些信息是不能发布的哦：）',
        6010106: '请您不要频繁发布相同内容。',
        6030104: '评论内容不能为空',
        6030105: '评论内容不能长于500个字符',
        6010000: '系统繁忙,请您稍候再试。',
        6010010: '由于对方的隐私设置，你没有权限执行该操作。',
        1201: '由于对方的隐私设置，您不能进行此操作。',
        201: '该分享不存在，或者已经被删除！',
        901: '请您不要频繁发布相同内容。',
        501: '抱歉，某些信息是不能发布的哦：）',
        1001: '系统繁忙,请您稍候再试。',
        6030108: '查询评论出错',
        2001: '服务器忙,请稍候重试',
        2002:'未知错误',
        2003:'获取用户信息失败',
        2004:'删除评论失败，请您稍候再试。',
        66400: '表怪我，相册/照片可能
 *   
 */
public class ErrorCodeAdapter {
    
    private static final Map<Integer,Integer> errorInfoMaps = new HashMap<Integer,Integer>();
    
    static {
        //这里做个适配
        errorInfoMaps.put(CommentError.COMMENT_IS_NOT_VOICE,999);
        errorInfoMaps.put(CommentError.COMMENT_NOT_DELETED, 999);
        errorInfoMaps.put(CommentError.COMMENT_NOT_EXISTS, 999);
        errorInfoMaps.put(CommentError.CONTENT_TOO_LONG, 1021);
        errorInfoMaps.put(CommentError.EMPTY_CONTENT, 998);
        errorInfoMaps.put(CommentError.INVALID_COMMENT_TYPE, 999);
        errorInfoMaps.put(CommentError.PERMISSON_DENY, 1020);
        errorInfoMaps.put(CommentError.STORAGE_ERROR, 999);
        errorInfoMaps.put(CommentError.UNKNOWN_ERROR, 999);
        errorInfoMaps.put(CommentError.PROHIBITED_BY_ANTISPAM, 1030);
        errorInfoMaps.put(CommentError.COMMENT_GETENTRY_ERROR, 6010014);
        errorInfoMaps.put(CommentError.COMMENT_TOO_FAST, 6010106);
    }
    
    public static int getAdapterErrorCode(int commentCenterCode){
        
        if(errorInfoMaps.containsKey(commentCenterCode)){
            return errorInfoMaps.get(commentCenterCode);
        }
        return 999;
    }
    
    

}
