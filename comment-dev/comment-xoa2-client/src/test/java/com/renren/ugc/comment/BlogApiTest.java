package com.renren.ugc.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.GetCommentResponse;
import com.renren.ugc.comment.xoa2.GetGlobalCommentListResponse;
import com.renren.ugc.comment.xoa2.RemoveAllCommentResponse;
import com.renren.ugc.comment.xoa2.RemoveCommentResponse;
import com.renren.ugc.comment.xoa2.util.CommentError;

public class BlogApiTest {

    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();

    @Test
    public void testGetBlogCommentList() {
        int actorId = 413096671;
        int entryOwnerId = 444858512;
        long entryId = 893469338L;

        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(CommentType.Blog, actorId,
                entryId, entryOwnerId, 0, 50, true, null);

        assertEquals(5, resp.getCommentListSize());
        assertEquals(5, resp.getTotalCount());
        for(Comment comment:resp.getCommentList()){
        	System.out.println(comment.getContent());
        }
    }

    @Test
    public void testGetBlogCommentFail() {
        int actorId = 413096671;
        int entryOwnerId = 444858512;
        long entryId = 893469338L;
        long commentId = 47324728157647872L;

        GetCommentResponse resp = COMMENT_CLIENT.getComment(CommentType.Blog, actorId, entryId,
                entryOwnerId, commentId);

        assertNull(resp.getComment());
        assertEquals(CommentError.COMMENT_NOT_EXISTS, resp.getBaseRep().getErrorInfo().getCode());
    }

    @Test
    public void testGetGlobalBlogComment() {
        int actorId = 477871526;
        long entryId = 914001483;
        int entryOwnerId = 477871526;
        int offset = 0;
        int limit = 10;
        boolean isDesc = true;

        GetGlobalCommentListResponse resp = COMMENT_CLIENT.getGlobalCommentList(CommentType.Blog,
                actorId, entryId, entryOwnerId, offset, limit, isDesc);

        assertEquals(10, resp.getCommentListSize());
        assertEquals(18, resp.getTotalCount());
    }
    
    @Test
    public void testCreateComment() throws InterruptedException {

        int actorId = 454926876;
        long entryId = 918812156L;
        int entryOwnerId = 454265592;
        int replyToId = 0;
        String content = "haha中文" + new Date().toString();
        
        Map<String,String> params = new HashMap<String,String>();
       params.put("isFrom", "20000001");

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Blog, actorId,
                entryId, entryOwnerId, content, replyToId, params);

        Comment comment = resp.getComment();
        Assert.assertTrue(comment != null);
        Assert.assertTrue(comment.getAuthorId() == 454926876);

        String expectContent = content;
        Assert.assertEquals(expectContent, comment.getContent());

        Thread.sleep(1000);

//        RemoveCommentResponse removeResp = COMMENT_CLIENT.removeComment(CommentType.Photo, actorId,
//                entryId, entryOwnerId, comment.getId(), null);
//
//        Assert.assertTrue(removeResp.isRemoved());
    }
    
    @Test
    public void testRemoveComment(){
    	
    	 int actorId = 454926876;
         long entryId = 920281780l;
         int entryOwnerId = 454926876;
         long commentId = 47452617578398720l;
    	
    	RemoveCommentResponse removeResp = COMMENT_CLIENT.removeComment(CommentType.Blog, actorId,
                entryId, entryOwnerId, commentId, null);
    	System.out.println(removeResp.removed);
    	
    	
    }
    
    @Test
    public void testRemoveAllComment(){
    	
    	 int actorId = 454926876;
         long entryId = 920281780l;
         int entryOwnerId = 454926876;
         long commentId = 47405601811915776l;
    	
         RemoveAllCommentResponse removeResp = COMMENT_CLIENT.removeAllComment(CommentType.Blog, actorId, entryId, entryOwnerId, commentId, null);
    	System.out.println(removeResp.allRemoved);
    	
    	
    }
}
