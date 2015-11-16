package com.renren.ugc.comment;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.GetCommentResponse;
import com.renren.ugc.comment.xoa2.RemoveCommentResponse;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.renren.xoa2.ErrorInfo;

public class StatusApiTest {

	private static Logger logger = Logger.getLogger(StatusApiTest.class);
	
    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();

    private static CommentType type = CommentType.Status;
    
    private CreateCommentResponse create(Param param, String content, int replyToId, Map<String, String> params) {
    	return COMMENT_CLIENT.createComment(type, param.getActorId(), param.getEntryId(),
    			param.getEntryOwnerId(), content, replyToId, params);
    }
    
    private CreateCommentResponse create(Param param) {
    	String content = "test";
    	return COMMENT_CLIENT.createComment(type, param.getActorId(), param.getEntryId(),
    			param.getEntryOwnerId(), content, 0, null);
    }
    
    private void printLog(String msg) {
    	if(logger.isInfoEnabled()) {
    		logger.info(msg);
    	}
    }
    
    /********************************** 最大众化的四个测试用例，能够测试评论的大部分功能是否正常 **************/
    
    /**	一个大众化的测试（最常用的操作）	**/
    @Test
    public void testCreate() {
        int actorId = 470947981; //发表评论的 用户id
        long entryId = 4989932639l; //状态id
        int entryOwnerId = 471859607;
        String replyContent = "@许雷(123456)  test" + " http://www.baidu.com/  (rs)";
        int replyToId = 471859607;//直接回复某人

        // 破坏@, 替换链接和表情
        String expect = "@许雷(123456 ) test <a href='http://rrurl.cn/bC1cnh' target='_blank' title='http://www.baidu.com/'>http://rrurl.cn/bC1cnh </a>   "
                + "<img src='http://a.xnimg.cn/imgpro/icons/statusface/rose0314.gif' alt='玫瑰花'  />";

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(type, actorId, entryId,
                entryOwnerId, replyContent, replyToId, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(entryId, resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }

    /**	一个大众化的测试（最常用的操作）	**/
    @Test
    public void testGetCommentList() {
        int actorId = 514605099;
        long entryId = 5053024171l; //状态id
        int entryOwnerId = 514605099;
        int offset = 0;
        int limit = 50;
        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(type, actorId, entryId,
                entryOwnerId, offset, limit, true, null);
        if (resp.isSetCommentList()) {
            printLog("comment list size : " + resp.getCommentList().size());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        for (Comment comment : resp.getCommentList()) {
            //printLog(comment.getContent());
        	System.out.println(comment.getContent());
        }
        Assert.assertTrue(resp.isSetCommentList());
        Assert.assertEquals(3, resp.getCommentList().size());
    }

    /** entry对actor不可见 **/
    @Test
    public void testCreateForInvisibleStatus() {
        Param param = Param.ENTRY_INVISIBLE;
        CreateCommentResponse resp = create(param);
        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.COMMENT_GETENTRY_ERROR,
                resp.getBaseRep().getErrorInfo().getCode());
    }

    /**	一个大众化的测试（最常用的操作）	**/
    @Test
    public void testGetComment() {
        int actorId = 471859607;
        //long entryId = 5005029403l; //状态id
        long commentId = 15528290326l;
        int entryOwnerId = 471859607;
        String expect = "http://rrurl.cn/301jqy ";

        GetCommentResponse resp = COMMENT_CLIENT.getComment(type, actorId, 0, entryOwnerId,
                commentId);

        if (resp.isSetComment()) {
            printLog("comment get : " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertEquals(commentId, resp.getComment().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }

    /**	一个大众化的测试（最常用的操作）	**/
    @Test
    public void testRemoveComment() {
        //先创建一条评论然后删除
        int actorId = 470947981;
        long entryId = 4989932639l;
        int entryOwnerId = 471859607;
        String replyContent = "test remove";
        int replyToId = 471859607;//直接回复某人

        CreateCommentResponse resp = COMMENT_CLIENT.createComment(type, actorId, entryId,
                entryOwnerId, replyContent, replyToId, null);

        if (resp.isSetComment()) {
            printLog("create success..");
            printLog("comment get : " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        long commentId = resp.getComment().getId();
        RemoveCommentResponse removeCommentResponse = COMMENT_CLIENT.removeComment(type, actorId,
                entryId, entryOwnerId, commentId, null);
        if (removeCommentResponse.isRemoved()) {
            printLog("remove success...");
        } else {
            ErrorInfo err = removeCommentResponse.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(removeCommentResponse.isRemoved());

    }
    
    /********************************** ********************************************** **************/
    
    
    
    
    
    /********************************** 创建评论，对评论中的表情，短链接，at是否能正常转换进行测试 **************/
    
    
    /**	在评论中发普通ubb，测试普通表情是否能正常解析	**/
    @Test
    public void testCreateCommentWithUbb() {
    	Param param = Param.FRIEND;
        String replyContent = "(rs)";

        String expect = "<img src='http://a.xnimg.cn/imgpro/icons/statusface/rose0314.gif' alt='玫瑰花'  />";
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(param.getEntryId(), resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }
    
    /**	在评论中发评论大表情，测试大表情是否能正常解析	**/
    @Test
    public void testCreateWithBigUbb() {
    	Param param = Param.FRIEND;
        String replyContent = "[罗小黑--拍地笑]";

        String expect = "<img class='feedbigemotion' width='30' height='30' src='http://a.xnimg.cn/imgpro/icons/dynamic-emotion/MrBlack/5.gif' alt='罗小黑--拍地笑'  />";
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(param.getEntryId(), resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
        
    }
    
    @After
    public void sleep() {
        // sleep for a while to avoid antispam rate check
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
    
    /**	在评论中发链接，测试短链接是否能正常转换	**/
    @Test
    public void testCreateWithShortUrl() {
    	Param param = Param.FRIEND;
        String replyContent = "http://www.baidu.com/";

        String expect = "<a href='http://rrurl.cn/bC1cnh' target='_blank' title='http://www.baidu.com/'>http://rrurl.cn/bC1cnh </a> ";
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(param.getEntryId(), resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }
    
    /**	在评论中发at，测试是否能@成功，或者被破坏成功	**/
    @Test
    public void testCreateCommentWithAt() {
    	Param param = Param.FRIEND;
        String replyContent = "@许雷(123456) @于心(471859607) ";

        String expect = "@许雷(123456 )<a href='http://www.renren.com/g/471859607' namecard='471859607' target='_blank'>@于心</a> ";
        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(param.getEntryId(), resp.getEntry().getId());
        Assert.assertEquals(expect, resp.getComment().getContent());
    }
    
    /********************************** ********************************************** **************/
    
    
    
    
    /********************************** 创建评论，对权限进行测试 ************************************************/
    
    /**	actor和entryOwner是好友关系	**/
    @Test
    public void testCreateCommentToStatusWhoseOwnerIsActorFriend() {
    	Param param = Param.FRIEND;
    	CreateCommentResponse resp = create(param);
        Assert.assertTrue(resp.isSetComment());
    }
    
    /**	actor和entryOwner非好友关系	**/
    @Test
    public void testCreateCommentToStatusWhoseOwnerIsNotActorFriend() {
    	Param param = Param.NO_FRIEND;
    	CreateCommentResponse resp = create(param);
        Assert.assertTrue(resp.isSetComment());
    }
    
    
    /**	actor被entryOwner拉黑,实际能评论成功	**/
    @Test
    public void testCreateCommentToStatusWhoseOnwerBlockActor() {
    	Param param = Param.BE_BLACKED;
    	CreateCommentResponse resp = create(param);
        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.PERMISSON_DENY, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    /**	actor把entryOwner拉黑,结果是actor还能对entryOwner的状态进行评论	**/
    @Test
    public void testCreateCommentToStatusWhoseOnwerIsBlockedByActor() {
    	Param param = Param.BLACK;
    	CreateCommentResponse resp = create(param);
    	Assert.assertTrue(resp.isSetComment());
    }
    
    /**	entry不存在	**/
    @Test
    public void testCreateCommentForNotExistingStatus() {
    	Param param = Param.ENTRY_NOT_EXIT;
    	CreateCommentResponse resp = create(param);
        //Assert.assertTrue(!resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.COMMENT_GETENTRY_ERROR, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    /********************************** ********************************************** **************/
    
    
    
    
    /********************************** 创建评论，对长度，安全过滤进行测试 ***********************************/
    
    //对长度限制进行测试,状态的评论过长，会被截断，不会抛异常
    @Test
    public void testCreateCommentWithTooLongContent() {
    	Param param = Param.FRIEND;
        String replyContent = "1111111111111111111111111111111111111" +
        		"1111111111111111111111111111111111111111111111111111" +
        		"111111111111111111111111111111111111111111111111111" +
        		"111111111111111111111111111111111111111111111111111111111" + 
        		"111111111111111111111111111111111111111111111111111111111" + 
        		"111111111111111111111111111111111111111111111111111111111";
        printLog("length : " + replyContent.length());

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertTrue(resp.isSetComment());
        Assert.assertNotNull(resp.getComment());
        Assert.assertEquals(240, resp.getComment().getContent().length());
    }
    
    //对违禁内容进行测试
    @Test
    public void testCreateCommentWithForbiddenContent() {
    	Param param = Param.FRIEND;
        String replyContent = "八九学潮";

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.PROHIBITED_BY_ANTISPAM, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    //对频率检查进行测试
    @Test
    public void testCreateTooFast() {
    	Param param = Param.FRIEND;
        String replyContent = "a";

        CreateCommentResponse resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        
        resp = create(param, replyContent, 0, null);

        if (resp.isSetComment()) {
            printLog("comment created: " + resp.getComment().getContent());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        Assert.assertFalse(resp.isSetComment());
        Assert.assertNotNull(resp.getBaseRep());
        Assert.assertNotNull(resp.getBaseRep().getErrorInfo());
        Assert.assertEquals(CommentError.COMMENT_TOO_FAST, resp.getBaseRep().getErrorInfo().getCode());
    }
    
    /********************************** ********************************************** **************/
    
    
    
    /********************************** 取评论列表 ************************************************/
    
    //对返回结果的条数进行测试
    @Test
    public void testGetCommentListSize() {
        int actorId = 471859607;
        long entryId = 5005029403l; //状态id
        int entryOwnerId = 471859607;
        int offset = 0;
        int limit = 50;
        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(type, actorId, entryId,
                entryOwnerId, offset, limit, true, null);
        if (resp.isSetCommentList()) {
            printLog("comment list size : " + resp.getCommentList().size());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        for (Comment comment : resp.getCommentList()) {
            printLog(comment.getContent());
        }
        Assert.assertTrue(resp.isSetCommentList());
        Assert.assertEquals(4, resp.getCommentList().size());
    }
    
    //对返回结果的顺序进行测试，升序测试
    @Test
    public void testGetCommentListAsc() {
        int actorId = 471859607;
        long entryId = 5005029403l; //状态id
        int entryOwnerId = 471859607;
        int offset = 0;
        int limit = 50;
        boolean isDesc = false;
        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(type, actorId, entryId,
                entryOwnerId, offset, limit, isDesc, null);
        if (resp.isSetCommentList()) {
            printLog("comment list size : " + resp.getCommentList().size());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        for (Comment comment : resp.getCommentList()) {
            printLog(comment.getContent());
        }
        Assert.assertTrue(resp.isSetCommentList());
        Assert.assertEquals(4, resp.getCommentList().size());
        for (int i = 0; i < resp.getGlobalCommentListSize() - 1; i++) {
            Assert.assertTrue(resp.getCommentList().get(i).getId() < resp.getCommentList().get(
                    i + 1).getId());
        }
    }
    
    //对返回结果的顺序进行测试，降序测试
    @Test
    public void testGetCommentListDesc() {
        int actorId = 471859607;
        long entryId = 5005029403l; //状态id
        int entryOwnerId = 471859607;
        int offset = 0;
        int limit = 50;
        boolean isDesc = true;
        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(type, actorId, entryId,
                entryOwnerId, offset, limit, isDesc, null);
        if (resp.isSetCommentList()) {
            printLog("comment list size : " + resp.getCommentList().size());
        } else {
            ErrorInfo err = resp.getBaseRep().getErrorInfo();
            printLog("error, code:" + err.getCode() + ", msg:" + err.getMsg());
        }
        for (Comment comment : resp.getCommentList()) {
            printLog(comment.getContent());
        }
        Assert.assertTrue(resp.isSetCommentList());
        Assert.assertEquals(4, resp.getCommentList().size());
        for (int i = 1; i < resp.getGlobalCommentListSize(); i++) {
            Assert.assertTrue(resp.getCommentList().get(0).getId() > resp.getCommentList().get(
                    i - 1).getId());
        }
    }
    
    
    /**
     * 评论所需的通用参数类,只能用于权限测试
     * @author lei.xu1
     *
     */
    enum Param {
    	//actor和entryOwner是好友
    	FRIEND(470947981, 5004449397l, 471859607),
    	//actor和entryOwner非好友
    	NO_FRIEND(454265592, 4989932639l, 471859607),
    	//actor把entryOwner拉黑
    	BLACK(454926876, 4989932639l, 471859607),
    	//actor被entryOwner拉黑
    	BE_BLACKED(477871526, 4989932639l, 471859607),
    	//该状态entry对actor可不见
    	ENTRY_INVISIBLE(470947981, 5006111112l, 471859607),
    	//该状态entry不存在
    	ENTRY_NOT_EXIT(505305288, 111222l, 471859607);
    	
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
}
