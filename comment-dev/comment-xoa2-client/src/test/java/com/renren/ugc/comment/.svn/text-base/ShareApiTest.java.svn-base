package com.renren.ugc.comment;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.GetCommentResponse;
import com.renren.ugc.comment.xoa2.GetFriendsCommentListResponse;
import com.renren.ugc.comment.xoa2.GetGlobalCommentListResponse;
import com.renren.ugc.comment.xoa2.RemoveCommentResponse;
import com.renren.ugc.comment.xoa2.util.CommentError;

public class ShareApiTest {

    private static Logger logger = Logger.getLogger(ShareApiTest.class);

    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();

    @Test
    public void testGetShareFriendsCommentListDesc() {

        int actorId = 556560299;
        long entryId = 16437390094L;
        int entryOwnerId = 556560299;

        GetFriendsCommentListResponse resp = COMMENT_CLIENT.getFriendsCommentList(
                CommentType.Share, actorId, entryId, entryOwnerId, 0, 10);
        List<Comment> comments = resp.getCommentList();
        Assert.assertEquals(2, comments.size());
        Assert.assertEquals(entryOwnerId, comments.get(0).getAuthorId());
    }

    @Test
    public void testGetShareCommentByCommentId() {

        int actorId = 556560299;
        long entryId = 0; // the entryId is actually of no use when query in DB
        int entryOwnerId = 558700163;
        long commentId = 162129590164021045L;

        GetCommentResponse resp = COMMENT_CLIENT.getComment(CommentType.Share, actorId, entryId,
                entryOwnerId, commentId);

        Comment comment = resp.getComment();

        // When get a share object, the entry will be always null
        // Assert.assertEquals(entryOwnerId, resp.getEntry().getOwnerId());
        Assert.assertEquals(556560299, comment.getAuthorId());
        Assert.assertEquals(commentId, comment.getId());
        Assert.assertNotNull(comment);
    }

    @Test
    public void testGetShareCommentList() {
        int actorId = 556560299;
        long entryId = 16416460226L;
        int entryOwnerId = 558700163;
        int offset = 0;
        int limit = 20;
        boolean isDesc = true;

        Map<String, String> params = new HashMap<String, String>();
        params.put("replaceShortUrl", String.valueOf(false));

        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(CommentType.Share, actorId,
                entryId, entryOwnerId, offset, limit, isDesc, null);

        List<Comment> comments = resp.getCommentList();
        Assert.assertNotNull(comments);
        Assert.assertEquals(3, comments.size());
    }
    
//    @Test
//    public void testGetShareAlbumPhotoCommentList() {
//        int actorId = 321831281;
//        long entryId = 16411946359L;
//        int entryOwnerId = 321831281;
//        int offset = 0;
//        int limit = 20;
//        boolean isDesc = true;
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("replaceShortUrl", String.valueOf(false));
//
//        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(CommentType.ShareAlbumPhoto, actorId,
//                entryId, entryOwnerId, offset, limit, isDesc);
//
//        List<Comment> comments = resp.getCommentList();
//        Assert.assertNotNull(comments);
//        Assert.assertEquals(4, comments.size());
//    }

    @Test
    public void testGetShareGlobalCommentListByEntryId() {

        int actorId = 558700163;
        long entryId = 16416642009L;
        int entryOwnerId = 558700163;

        GetGlobalCommentListResponse resp = COMMENT_CLIENT.getGlobalCommentList(CommentType.Share,
                actorId, entryId, entryOwnerId, 0, 10, true);

        List<Comment> comments = resp.getCommentList();
        Assert.assertEquals(4, comments.size());
        Assert.assertEquals(558700163, comments.get(0).getAuthorId());
    }

    @Test
    public void testGetShareGlobalCommentListByUrlmd5() {

        String urlmd5 = "adff31231f9b337b849a4531f7922854";
        int actorId = 321831281;
        int limit = 30;
        boolean isDesc = true;

        GetGlobalCommentListResponse resp = COMMENT_CLIENT.getGlobalCommentList(CommentType.Share,
                actorId, urlmd5, 0, limit, isDesc);

        List<Comment> comments = resp.getCommentList();
        Assert.assertEquals(4, comments.size());
        Assert.assertEquals(558700163, comments.get(0).getAuthorId());

    }
    
    @Test
    public void testCreateLongComment() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
        		"五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论测试";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(558700163, comment.getAuthorId());

        // since the "@" user is not entry owner's friends, the @ will be broken
        //        String expectContent = "my test @郑跃强(67243 ) 测试创建后删除评论";
        String expectContent = "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论五百字评论" +
                "五百字评论五百字评论五百字评论五百字评论";
        Assert.assertEquals(expectContent, comment.getContent());
        
    }
    
    @Test
    public void testCreateCommentWithForbiddenWord() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "89学潮";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);

        Comment comment = resp.getComment();
        Assert.assertNull(comment);
        Assert.assertEquals(CommentError.PROHIBITED_BY_ANTISPAM,
                resp.getBaseRep().getErrorInfo().getCode());
    }
    
    @Test
    public void testCreateCommentWithAt() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "@尚琳琳(26496341) 测试@功能";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);
        
        String expectContent = "@尚琳琳(26496341 )测试@功能";

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(expectContent, comment.getContent());
        
    }
    
    @Test
    public void testCreateCommentWithShortUrl() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "http://share.renren.com/share/321831281/16372966900?from=0101090302&shfrom=010301003";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);
        
        String expectContent = "<a href='http://rrurl.cn/f6cGuQ' target='_blank' title='http://share.renren.com/share/321831281/16372966900?from=0101090302&amp;shfrom=010301003'>http://rrurl.cn/f6cGuQ </a> ";
        String expectOriginalContent = "http://rrurl.cn/f6cGuQ ";

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(expectOriginalContent, comment.getOriginalContent());
        Assert.assertEquals(expectContent, comment.getContent());
        
    }
    
    @Test
    public void testCreateCommentWithLargeUbb() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "[罗小黑--拍地笑]";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);
        
        String expectContent = "<img class='feedbigemotion' width='30' height='30' src='http://a.xnimg.cn/imgpro/icons/dynamic-emotion/MrBlack/5.gif' alt='罗小黑--拍地笑'  />";

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(expectContent, comment.getContent());
        
    }
    
    @Test
    public void testCreateCommentWithUbb() {
        sleep();

        int actorId = 558700163;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        String content = "(rs)";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);
        
        String expectContent = "<img src='http://a.xnimg.cn/imgpro/icons/statusface/rose0314.gif' alt='玫瑰花'  />";

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(expectContent, comment.getContent());
        
    }
    
    @Test
    public void testCreateCommentWithoutPermission() {
        sleep();

        int actorId = 477871526;
        long entryId = 16424122193L;
        int entryOwnerId = 471859607;
        String content = "[罗小黑--拍地笑]";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, 0, null);

        Comment comment = resp.getComment();
        Assert.assertNull(comment);
        Assert.assertEquals(CommentError.PERMISSON_DENY, resp.getBaseRep().getErrorInfo().getCode());
    }

    /**
     * test creating a share comment that's not written to global comment
     * list by setting "needGlobal" param to false
     */
    @Test
    public void testCreateNonGlobalShareComment()  {
        sleep();
        
        int actorId = 482657886;
        long entryId = 16427271633L;
        int entryOwnerId = 482657886;
        Map<String, String> params = new HashMap<String, String>();
        params.put("needGlobal", "false");
        
        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId, entryId, entryOwnerId, "test content", 0, params);
        long commentId = resp.getComment().getId();
        
        // confirm the comment can be get through Share comment list
        GetCommentResponse getResp = COMMENT_CLIENT.getComment(CommentType.Share, actorId, entryId, entryOwnerId, commentId);
        assertNotNull(getResp.getComment());
        
        // confirm this comment can be get by global comment list
        String urlmd5 = "024332b73ccefff3c0eb842fcb0e548d";
        GetGlobalCommentListResponse getGlobalResp = COMMENT_CLIENT.getGlobalCommentList(
                CommentType.Share, actorId, urlmd5, 0, 30, true);
        List<Comment> comments = getGlobalResp.getCommentList();
        for (Comment comment : comments) {
            Assert.assertNotSame(commentId, comment.getId());
        }
    }
    

    @Test
    public void testCreateAndRemoveComment() {
        sleep();

        int actorId = 67243;
        long entryId = 16412009164L;
        int entryOwnerId = 558700163;
        int replyToId = 558700163;
        String content = "my test @郑跃强(67243) 测试创建后删除评论";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Share, actorId,
                entryId, entryOwnerId, content, replyToId, null);

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(actorId, comment.getAuthorId());

        // since the "@" user is not entry owner's friends, the @ will be broken
        //        String expectContent = "my test @郑跃强(67243 ) 测试创建后删除评论";
        String expectContent = "my test @郑跃强(67243 )测试创建后删除评论";
        Assert.assertEquals(expectContent, comment.getContent());

        sleep();

        RemoveCommentResponse removeResp = COMMENT_CLIENT.removeComment(CommentType.Share, actorId,
                entryId, entryOwnerId, comment.getId(), null);

        Assert.assertTrue(removeResp.isRemoved());
    }
    
    @Test
    public void testGetComment() {
        int actorId = 477871526;
        long entryId = 0;
        int entryOwnerId = 477871526;
        long commentId = 173445255613293568L;
        
        GetCommentResponse resp = COMMENT_CLIENT.getComment(CommentType.Share, actorId, entryId, entryOwnerId, commentId);
        
        assertNotNull(resp);
        assertNotNull(resp.getComment());
        assertNotNull(resp.getComment().getContent());
        assertNotNull(resp.getComment().getOriginalContent());
    }

    private void sleep() {
        try {
            // sleep for a while to wait antispam
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
