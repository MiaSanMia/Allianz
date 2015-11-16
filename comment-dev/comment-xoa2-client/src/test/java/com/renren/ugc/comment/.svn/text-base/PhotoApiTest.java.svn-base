package com.renren.ugc.comment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
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

public class PhotoApiTest {
    
    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();
    
    private static CommentType type = CommentType.Photo;
    
    private static Logger logger = Logger.getLogger(PhotoApiTest.class);

    private CreateCommentResponse create(Param param, String content, int replyToId, Map<String, String> params) {
        return COMMENT_CLIENT.createComment(type, param.getActorId(), param.getEntryId(),
                param.getEntryOwnerId(), content, replyToId, params);
    }
    
    private CreateCommentResponse create(Param param) {
        String content = "test";
        return COMMENT_CLIENT.createComment(type, param.getActorId(), param.getEntryId(),
                param.getEntryOwnerId(), content+new Date().getTime(), 0, null);
    }
    
    @After
    public void after(){
        try {
            // sleep for a while to avoid the antispam accidentally block the request
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /********************************** 最大众化的四个测试用例，能够测试评论的大部分功能是否正常 **************/
    
    /** 一个大众化的测试（最常用的操作）    **/
    @Test
    public void testGetPhotoCommentList() {
        int actorId = 454926876;
        int entryOwnerId = 454926876;
        long entryId = 7460397035L;
        int offset = 0;
        int limit = 10;
        boolean isDesc = true;

        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(CommentType.Photo, actorId,
                entryId, entryOwnerId, offset, limit, isDesc, null);

        List<Comment> comments = resp.getCommentList();
        Assert.assertNotNull(comments);
        Assert.assertEquals(1, comments.size());
    }

    /** 一个大众化的测试（最常用的操作）    **/
    @Test
    public void testGetPhotoFriendsCommentListDesc() {

        int actorId = 454926876;
        long entryId = 7460397034l;
        int entryOwnerId = 454926876;

        GetFriendsCommentListResponse resp = COMMENT_CLIENT.getFriendsCommentList(
                CommentType.Photo, actorId, entryId, entryOwnerId, 0, 10);
        List<Comment> comments = resp.getCommentList();
        
        Assert.assertNotNull(comments);
        Assert.assertEquals(2, comments.size());
    }

    /**
     * 一个大众化的测试（最常用的操作）
     * 
     */
    @Test
    public void testCreateAndRemoveComment() throws InterruptedException {

        int actorId = 454265592;
        long entryId = 7448865224L;
        int entryOwnerId = 454926876;
        int replyToId = 0;
        String content = "my test  测试创建后删除评论" + new Date().toString();

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Photo, actorId,
                entryId, entryOwnerId, content, replyToId, null);

        Comment comment = resp.getComment();
        Assert.assertTrue(comment != null);
        Assert.assertTrue(comment.getAuthorId() == 454265592);

        String expectContent = content;
        Assert.assertEquals(expectContent, comment.getContent());

        Thread.sleep(1000);

        RemoveCommentResponse removeResp = COMMENT_CLIENT.removeComment(CommentType.Photo, actorId,
                entryId, entryOwnerId, comment.getId(), null);

        Assert.assertTrue(removeResp.isRemoved());
    }

    /** 一个大众化的测试（最常用的操作）    **/
    @Test
    public void testGetPhotoCommentByCommentId() {

        int actorId = 454265592;
        long entryId = 0; // the entryId is actually of no use when query in DB
        int entryOwnerId = 454926876;
        long commentId = 8085323007L;

        GetCommentResponse resp = COMMENT_CLIENT.getComment(CommentType.Photo, actorId, entryId,
                entryOwnerId, commentId);

        Comment comment = resp.getComment();
        Assert.assertEquals(entryOwnerId, resp.getEntry().getOwnerId());
        Assert.assertEquals(454926876, comment.getAuthorId());
        Assert.assertEquals(commentId, comment.getId());
        Assert.assertNotNull(comment);

    }

    @Test
    public void testGetPhotoGlobalCommentListByEntryId() {

        int actorId = 454926876;
        long entryId = 7460397034L;
        int entryOwnerId = 454926876;

        GetGlobalCommentListResponse resp = COMMENT_CLIENT.getGlobalCommentList(CommentType.Photo,
                actorId, entryId, entryOwnerId, 0, 10, true);

        List<Comment> comments = resp.getCommentList();
        
        Assert.assertNotNull(comments);
        Assert.assertEquals(2, comments.size());
    }
    
 /********************************** 创建评论，对评论中的表情，短链接，at是否能正常转换进行测试 **************/
    
    
    /** 在评论中发普通ubb，测试普通表情是否能正常解析    **/
    @Test
    public void createUbbTest() {
        Param param = Param.FRIEND;
        String time = new Date().toString();
        String replyContent = "(rs)"+time;

        String expect = "<img src='http://a.xnimg.cn/imgpro/icons/statusface/rose0314.gif' alt='玫瑰花'  />"+time;
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(param.getEntryId(), resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
       
    }
    
    /** 在评论中发评论大表情，测试大表情是否能正常解析 **/
    @Test
    public void createBigUbbTest() {
        Param param = Param.FRIEND;
        String time = new Date().toString();
        String replyContent = "[罗小黑--拍地笑]"+time;

        String expect = "<img class='feedbigemotion' width='30' height='30' src='http://a.xnimg.cn/imgpro/icons/dynamic-emotion/MrBlack/5.gif' alt='罗小黑--拍地笑'  />"+time;
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }
    
    /** 在评论中发at，测试是否能@成功，或者被破坏成功    **/
    @Test
    public void createAtTest() {
        Param param = Param.FRIEND;
        String time = new Date().toString();
        String replyContent = "@许雷(123456) @王小小(454926876) "+time;

        String expect = "@许雷(123456 )<a href='http://www.renren.com/g/454926876' namecard='454926876' target='_blank'>@王小小</a> "+ time;
        CreateCommentResponse resp = create(param, replyContent, 0, null);


        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }
    
    /********************************** ********************************************** **************/
    
/********************************** 创建评论，对权限进行测试 ************************************************/
    
    /** actor和entryOwner非好友关系   **/
    @Test
    public void createNoFriendTest() {
        Param param = Param.NO_FRIEND;
        CreateCommentResponse resp = create(param);
        Assert.assertTrue(resp.isSetComment());
        
    }
    
    
    /** actor被entryOwner拉黑  **/
    @Test
    public void createBeBlackTest() {
        Param param = Param.BE_BLACKED;
        CreateCommentResponse resp = create(param);
        Assert.assertTrue(!resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
       Assert.assertEquals(4, resp.getBaseRep().getErrorInfo().getCode());
        
    }
    
    /** actor把entryOwner拉黑,结果是actor还能对entryOwner的状态进行评论 **/
    @Test
    public void createBlackTest() {
        Param param = Param.BLACK;
        CreateCommentResponse resp = create(param);
        
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
    }
    
    /** entry不存在    **/
    @Test
    public void createEntryNotExitTest() {
        Param param = Param.ENTRY_NOT_EXIT;
        CreateCommentResponse resp = create(param);
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(10, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    
 /********************************** 创建评论，对长度，安全过滤进行测试 ***********************************/
    
    //对长度限制进行测试,状态的评论过长，会被截断，不会抛异常
    @Test
    public void createLengthLimitTest() {
        Param param = Param.FRIEND;
        String replyContent = "1111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" + 
                "111111111111111111111111111111111111111111111111111111111" + 
                "111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111" +
                 "111111111111111111111111111111111111111111111111111111111" ;
        logger.info("length : " + replyContent.length());

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(3, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    //对违禁内容进行测试
    @Test
    public void createForbidTest() {
        Param param = Param.FRIEND;
        String replyContent = "八九学潮";

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.PROHIBITED_BY_ANTISPAM,
                resp.getBaseRep().getErrorInfo().getCode());
    }
    
    //对频率检查进行测试
    @Test
    public void testCreateFastOrSame() {
        Param param = Param.FRIEND;
        String replyContent = "a";

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        
        CreateCommentResponse resp2 = create(param, replyContent, 0, null);

        Assert.assertFalse(resp2.isSetComment());
        Assert.assertNotNull(resp2.getBaseRep());
        Assert.assertNotNull(resp2.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.COMMENT_TOO_FAST,
                resp2.getBaseRep().getErrorInfo().getCode());
    }
    
    @Test
    public void testCreateAndRemoveComment11() throws InterruptedException {

        int actorId = 454265592;
        long entryId = 7585443456L;
        int entryOwnerId = 454926876;
        int replyToId = 0;
        String content = "my test  测试创建后删除评论" + new Date().toString();

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Photo, actorId,
                entryId, entryOwnerId, content, replyToId, null);

        Comment comment = resp.getComment();
      
        System.out.println(comment.getContent());
    }
    
    /********************************** ********************************************** **************/
    
    /**
     * 评论所需的通用参数类,只能用于权限测试
     * @author wangxx
     *
     */
    enum Param {
        //actor和entryOwner是好友
        FRIEND(454265592, 7448711380l, 454926876),
        //actor和entryOwner非好友
        NO_FRIEND(426337276, 7448711380l, 454926876),
        //actor把entryOwner拉黑
        BLACK(454926876, 7138530874l, 471859607),
        //actor被entryOwner拉黑
        BE_BLACKED(471859607, 7448711380l, 454926876),
        //该album entry对actor可不见
        ENTRY_INVISIBLE(454265592, 7448519643l, 454926876),
        //该album entry不存在
        ENTRY_NOT_EXIT(454265592, 111222l, 454926876);
        
        private int actorId;
        private long entryId;
        private int entryOwnerId;
        private Param(int actorId, long entryId, int entryOwnerId) {
            this.actorId = actorId;
            this.entryId = entryId;
            this.entryOwnerId = entryOwnerId;
        }
        
        public int getActorId() {
            return actorId;
        }
        public long getEntryId() {
            return entryId;
        }
        public int getEntryOwnerId() {
            return entryOwnerId;
        }
        
    }
    
    @Test
    public void testCreateComment() {

        int actorId = 454265592;
        long entryId = 7704709846L;
        int entryOwnerId = 454926876;
        int replyToId = 0;
        String content = "my test  测试创建后删除评论" + "|randomkry:"+new Date().toString();
        
        Map<String,String> params = new HashMap<String,String>();
        params.put("isFrom", "20000001");

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(CommentType.Photo, actorId,
                entryId, entryOwnerId, content, replyToId, params);

        Comment comment = resp.getComment();
        Assert.assertTrue(comment != null);
        Assert.assertTrue(comment.getAuthorId() == actorId);

        Assert.assertEquals(content, comment.getContent());

    }

}
