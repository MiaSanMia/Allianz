package com.renren.ugc.comment.xoa2.util;

import java.util.Date;

import com.renren.ugc.comment.xoa2.Comment;

/**
 * Utility methods for {@link com.renren.ugc.comment.xoa2.Comment Comment}
 * class
 * 
 * @author jiankuan
 * 
 */
public class CommentUtils {

    /**
     * Judge whether the comment is a whisper
     */
    public static final boolean isWhisper(Comment comment) {
        return comment.getWhipserId() > 0;
    }

    /**
     * retrieve the comment's corresponding entry's ID. If the entry is not
     * set, return 0
     */
    public static final long getEntryId(Comment comment) {
        if (comment.getEntry() != null) {
            return comment.getEntry().getId();
        } else {
            return 0;
        }
    }

    /**
     * retrieve the comment's corresponding entry's owner ID. If the entry
     * is not set, return 0
     */
    public static final int getEntryOwnerId(Comment comment) {
        if (comment.getEntry() != null) {
            return comment.getEntry().getOwnerId();
        } else {
            return 0;
        }
    }

    /**
     * get the time when the comment is created
     */
    public static final Date getCreateTime(Comment comment) {
        return new Date(comment.getCreateTimeMillis());
    }

    public static final int getReplyToUserId(Comment comment) {
        if (comment.isSetReplyToUser()) {
            return comment.getReplyToUser().getId();
        } else {
            return 0;
        }
    }

}
