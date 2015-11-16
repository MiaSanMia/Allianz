package com.renren.ugc.comment.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.comment.xoa2.CommentLikeInfo;
import com.renren.ugc.comment.xoa2.CommentVoiceInfo;
import com.renren.ugc.comment.xoa2.Entry;
import com.renren.ugc.comment.xoa2.RepliedUser;

public class JSONConvertUtils {

    public static JSONArray buildJSON(List<Comment> commentList, Entry entry) {
        JSONArray array = new JSONArray();
        for (Comment comment : commentList) {
            array.put(JSONConvertUtils.comment2Json(comment,
                entry != null ? entry : comment.getEntry()));
        }
        return array;
    }

    // jackson
    /**
     * @param comment
     * @param entry
     * @return
     */
    public static JSONObject comment2Json(Comment comment, Entry entry) {
        JSONObject object = new JSONObject();
        object.put("id", String.valueOf(comment.getId()));
        object.put("authorId", comment.getAuthorId());
        object.put("authorName", comment.getAuthorName());
        object.put("authorHeadUrl", comment.getAuthorHead());
        object.put("authorKeepUse", comment.isAuthorKeepUse());
        if (entry != null) {
            object.put("entryOwnerId", entry.getOwnerId());
            object.put("entryId", String.valueOf(entry.getId()));
            object.put("resourceUserId", entry.getOwnerId());
        }
        object.put("content", comment.getContent());
        object.put("time", DateFormatUtils.format(
            new Date(comment.getCreateTimeMillis()), "yyyy-MM-dd HH:mm"));
        object.put("createTimeMillis",
            String.valueOf(comment.getCreateTimeMillis()));
        if (comment.getWhipserId() > 0) {
            object.put("whisperId", comment.getWhipserId());
        }
        object.put("isWhisper", comment.getWhipserId() > 0);
        if (comment.getToCommentId() > 0) {
            object.put("toCommentId", comment.getToCommentId());
        }

        RepliedUser user = comment.getReplyToUser();
        if (user != null) {
            object.put("repliedUser", JSONConvertUtils.toJSON(user));
        }

        CommentLikeInfo likeInfo = comment.getLikeInfo();
        if (likeInfo != null) {
            object.put("like", JSONConvertUtils.toJSON(likeInfo));
        }

        CommentVoiceInfo voiceInfo = comment.getVoiceInfo();
        if (voiceInfo != null) {
            //object.put("voice", JSONConvertUtils.toJSON(voiceInfo));
            //modified by wangxx,与现有的前端判断逻辑保持一致
            object.put("vocal_length", voiceInfo.getVoiceLength());
            object.put("vocal_size", voiceInfo.getVoiceSize());
            object.put("vocal_rate", voiceInfo.getVoiceRate());
            object.put("vocal_url", voiceInfo.getVoiceUrl());
        }
        // feed need this for comment's like
        // commentId与id是重复的，这是因为主工程暂时只接受“commentId”这个key，所以只能加上，以后可以考虑去掉
        object.put("commentId", String.valueOf(comment.getId()));

        // vip icon url
        object.put("vipIconUrl", comment.getAuthorVipIcon());
        /*
         * 评论类型
         */
        object.put("type", comment.getType().name());
        
        //楼层
        object.put("floor", comment.getFloor());
        //引用评论
        if(comment.getSimpleComment() != null){
        	object.put("relatedAuthorId", comment.getSimpleComment().getAuthorId());
        	object.put("relatedAuthorName", comment.getSimpleComment().getAuthorName());
        	object.put("relatedFloor", comment.getSimpleComment().getFloor());
        	object.put("relatedContent", comment.getSimpleComment().getContent());
        }

        return object;
    }

    /**
     * convert a <code>RepliedUser</code> object to JSON
     */
    public static JSONObject toJSON(RepliedUser user) {
        JSONObject obj = new JSONObject();
        obj.put("userId", user.getId());
        obj.put("userName", user.getName());
        return obj;
    }

    /**
     * convert a <code>LikeInfo</code> object to JSON
     */
    public static JSONObject toJSON(CommentLikeInfo likeInfo) {
        JSONObject obj = new JSONObject();
        obj.put("liked", likeInfo.isLiked());
        obj.put("count", likeInfo.getLikeCount());
        return obj;
    }

    /**
     * convert a <code>VoiceInfo</code> object to JSON
     */
    public static JSONObject toJSON(CommentVoiceInfo voiceInfo) {
        JSONObject obj = new JSONObject();

        obj.put("url", voiceInfo.getVoiceUrl());
        if (voiceInfo.isSetVoiceLength()) {
            obj.put("length", voiceInfo.getVoiceLength());
        }
        if (voiceInfo.isSetVoiceSize()) {
            obj.put("size", voiceInfo.getVoiceSize());
        }
        if (voiceInfo.isSetVoiceRate()) {
            obj.put("rate", voiceInfo.getVoiceRate());
        }
        if (voiceInfo.isSetVoicePlayCount()) {
            obj.put("playCount", voiceInfo.getVoicePlayCount());
        }
        return obj;
    }

    public static Object entry2Json(Entry entry) {
        return null;
    }
}
