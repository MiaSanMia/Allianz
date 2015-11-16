package com.renren.ugc.comment.xoa2.util;

import org.apache.commons.lang.StringUtils;

import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Utility methods for {@link com.renren.ugc.comment.xoa2.CommentType
 * CommentType} class
 * 
 * @author jiankuan
 * 
 */
public class CommentTypeUtils {

    /**
     * Convert comment type string to its enum constant value
     */
    public static final CommentType valueOf(String typeStr) {
        try {
            typeStr = StringUtils.capitalize(typeStr);
            return CommentType.valueOf(typeStr);
        } catch (Exception e) {
            throw new RuntimeException("不支持的评论类型", e);
        }
    }
}
