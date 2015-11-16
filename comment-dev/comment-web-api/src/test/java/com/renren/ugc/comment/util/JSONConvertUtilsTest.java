package com.renren.ugc.comment.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.json.JSONObject;
import org.junit.Test;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentLikeInfo;
import com.renren.ugc.comment.xoa2.CommentVoiceInfo;
import com.renren.ugc.comment.xoa2.Entry;
import com.renren.ugc.comment.xoa2.RepliedUser;

public class JSONConvertUtilsTest {

    @Test
    public void testCommentToJson() throws ParseException {
        Comment comment = new Comment();
        comment.setId(1355464747391027000L);
        comment.setAuthorId(501522889);
        comment.setAuthorName("王小明");
        comment.setAuthorKeepUse(true);
        comment.setAuthorHead("hdn321/20121213/1730/h_tiny_Dh32_5c7c000022a51376.jpg");
        comment.setContent("this is a test comment to convert json");
        comment.setCreateTimeMillis(1355475600502L);
        comment.setToCommentId(18);
        comment.setWhipserId(96);
        RepliedUser ru = new RepliedUser();
        ru.setId(501522890);
        ru.setName("Jonh");
        comment.setReplyToUser(ru);
        Entry entry = new Entry();
        entry.setId(6809537162L);
        entry.setOwnerId(501522889);
        comment.setEntry(entry);
        JSONObject json = JSONConvertUtils.comment2Json(comment, entry);

        String expected = "{\"toCommentId\":18,\"authorHeadUrl\":\"hdn321\\/20121213\\/1730\\/h_tiny_Dh32_5c7c000022a51376.jpg\",\"authorKeepUse\":true,\"isWhisper\":true,"
                + "\"commentId\":\"1355464747391027000\",\"entryOwnerId\":501522889,\"authorId\":501522889,\"entryId\":\"6809537162\",\"id\":\"1355464747391027000\","
                + "\"content\":\"this is a test comment to convert json\",\"repliedUser\":{\"userId\":501522890,\"userName\":\"Jonh\"},"
                + "\"time\":\"2012-12-14 17:00\",\"createTimeMillis\":\"1355475600502\",\"whisperId\":96,"
                + "\"authorName\":\"\\u738b\\u5c0f\\u660e\",\"resourceUserId\":501522889}";
        String actual = json.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testLikeInfoToJson() {
        CommentLikeInfo likeInfo = new CommentLikeInfo();
        likeInfo.setLiked(true);
        likeInfo.setLikeCount(15);

        assertEquals("{\"count\":15,\"liked\":true}", JSONConvertUtils.toJSON(likeInfo).toString());
    }

    @Test
    public void testVoiceInfoToJson() {
        CommentVoiceInfo voiceInfo = new CommentVoiceInfo();
        voiceInfo.setVoiceUrl("http://abc.def.com/ddle/abc.mp3");
        voiceInfo.setVoiceLength(5);
        voiceInfo.setVoiceSize(378);
        voiceInfo.setVoiceRate(44100);

        assertEquals(
                "{\"rate\":44100,\"length\":5,\"url\":\"http:\\/\\/abc.def.com\\/ddle\\/abc.mp3\",\"size\":378}",
                JSONConvertUtils.toJSON(voiceInfo).toString());

        // test optional field
        CommentVoiceInfo voiceInfo2 = new CommentVoiceInfo();
        voiceInfo2.setVoiceUrl("http://abc.def.com/ddle/def.mp3");
        assertEquals("{\"url\":\"http:\\/\\/abc.def.com\\/ddle\\/def.mp3\"}",
                JSONConvertUtils.toJSON(voiceInfo2).toString());
    }
}
