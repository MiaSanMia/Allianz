package com.renren.ugc.comment;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CommentVoiceInfo;
import com.renren.ugc.comment.xoa2.CreateVoiceCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.GetCommentResponse;


/**
 * @author wangxx
 *
 */
public class VoiceApiTest {
    
    private static Logger logger = Logger.getLogger(VoiceApiTest.class);

    private static CommentXoa2Client COMMENT_CLIENT = new CommentXoa2Client();
    
    private static  String voiceUrl = "http://fmn.rrimg.com/fmn056/audio/20130926/1820/m_XExi_5f49000004da118e.mp3";

    @After
    public void sleep() throws InterruptedException {
        Thread.sleep(1500);
    }

    @Test
    public void testCreateVoice() {

        int actorId = 454926876;
        long entryId = 7455330517l;
        int entryOwnerId = 454265592;
        int replyToId = 0;
        int voiceLength = 5;
        int voiceRate = 16000;
        int voiceSize = 12852;
        String content = "";

        CreateVoiceCommentResponse resp = COMMENT_CLIENT.createVoiceComment(CommentType.Photo,
                actorId, entryId, entryOwnerId, content, replyToId, null, voiceLength, voiceRate,
                voiceSize, voiceUrl);

        if (!resp.isSetComment()) {
            logger.error("error:" + resp.getBaseRep().getErrorInfo().toString());
        }

        Comment comment = resp.getComment();
        Assert.assertNotNull(comment);
        Assert.assertEquals(454926876, comment.getAuthorId());
        String resultContent = comment.getContent();
        if (resultContent.endsWith("。")) {
            // the old data will return "这是一条语音评论。", unify it
            resultContent = resultContent.substring(0, resultContent.length() - 1);

        }
        Assert.assertEquals("这是一条语音评论", resultContent);

        GetCommentResponse getResp = COMMENT_CLIENT.getComment(CommentType.Photo, actorId, entryId,
                entryOwnerId, resp.getComment().getId());
        Comment comment2 = getResp.getComment();
        Assert.assertNotNull(comment2);
        Assert.assertEquals(454926876, comment2.getAuthorId());
        resultContent = comment2.getContent();
        if (resultContent.endsWith("。")) {
            // the old data will return "这是一条语音评论。", unify it
            resultContent = resultContent.substring(0, resultContent.length() - 1);

        }
        Assert.assertEquals("这是一条语音评论", resultContent);
        Assert.assertTrue(comment2.isVoiceComment());
        Assert.assertEquals(voiceUrl, comment2.getVoiceInfo().getVoiceUrl());
    }

    @Test
    public void testGetVoiceInfo() {

        int actorId = 454926876;
        long entryId = 7455330517l;
        int entryOwnerId = 454265592;
        int offset = 0;
        int limit = 10;
        boolean isDesc = false;

        GetCommentListResponse resp = COMMENT_CLIENT.getCommentList(CommentType.Photo, actorId,
                entryId, entryOwnerId, offset, limit, isDesc, null);

        List<Comment> comments = resp.getCommentList();
        Assert.assertNotNull(comments);
        //第一条评论为语音评论
        Comment comment = comments.get(0);
        Assert.assertNotNull(comment);
        Assert.assertTrue(comment.isVoiceComment());
        CommentVoiceInfo voiceInfo = comment.getVoiceInfo();
        Assert.assertNotNull(voiceInfo);
        Assert.assertEquals(voiceUrl, voiceInfo.getVoiceUrl());

        // The old photo comment uses "这是一条语音评论。" rather than "这是一条语音评论"
        // as the default literal of a voice comment.
        Assert.assertEquals("这是一条语音评论。", comment.getContent());
    }
}
