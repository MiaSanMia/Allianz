package com.renren.ugc.comment.util;

import com.renren.ugc.comment.xoa2.CommentType;


/**
 * 
 * {@link CommentBusIDentifier} ugc、相册id生成、解析器
 * 
 * @author xinyan.yang@renren-inc.com
 * 
 * @date 2013-3-14 17:35:48
 * 
 */
public class CommentBusIDentifier {
    
    private static CommentBusIDentifier instance = new CommentBusIDentifier();
    
    public static CommentBusIDentifier getInstance() {
        return instance;
    }
    
    private CommentBusIDentifier() {
        
    }
    
    /**
     * 各业务id推出评论id
     * 
     * @param commentType   评论类型
     * 
     * @param busCommentId  业务id
     * 
     * @return
     */
    public long genCommentId(CommentType type, long busCommentId) {
        long typeCode = (long) type.getValue();
        return (typeCode << 54 | busCommentId);
    } 
    
    /**
     * 逆向有评论id推出各业务id
     * 
     * @param commentId
     * 
     * @return
     */
    public long reverseGenCommentId(long commentId) {
        return (commentId & (0x00001FFFFFFFFFFFFFL));
    }
}
