package com.renren.ugc.comment.xoa2.util;

import com.renren.ugc.comment.model.Comment;

import java.util.Comparator;

/**
 * Compare two {link Comment} objects by comment id (desc order)
 * User: jiankuan
 * Date: 9/12/13
 * Time: 17:53
 */
public class CommentIdDescComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment c1, Comment c2) {
        if (c1.getId() < c2.getId())  {
            return 1;
        } else if (c1.getId() > c2.getId()) {
            return -1;
        }
        return 0;
    }
}
