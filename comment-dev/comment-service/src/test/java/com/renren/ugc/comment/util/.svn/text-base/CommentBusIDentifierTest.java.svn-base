/**
 * 
 */
package com.renren.ugc.comment.util;

import org.junit.Assert;
import org.junit.Test;

import com.renren.ugc.comment.util.CommentBusIDentifier;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author Administrator
 *
 */
public class CommentBusIDentifierTest {
    
    @Test
    public void testGenBusIDentifier() {
        
        long doingId = 4628714779L; 
        
        long commentId = CommentBusIDentifier.getInstance().genCommentId(CommentType.Status, doingId);
        
        long busId = CommentBusIDentifier.getInstance().reverseGenCommentId(commentId);
        
        Assert.assertEquals("评论id推出业务id", doingId, busId);
        
        Assert.assertEquals("状态类型相同", CommentType.Status.getValue(), commentId >> 54);
        
    }
}
