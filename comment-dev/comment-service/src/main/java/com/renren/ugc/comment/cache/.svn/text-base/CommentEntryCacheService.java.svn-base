package com.renren.ugc.comment.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.renren.app.like.data.client.model.LikeInfoResult;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * 用来操作，评论对应的本体的缓存
 * 
 * @author tuanwang.liu
 * 
 *         2013-9-12
 */
public interface CommentEntryCacheService {

    /**
     * cache the entry which comment belongs to
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param entry
     */
    public void setEntryCache(final long entryId, final int entryOwnerId, CommentType type,
            Serializable entry);

    /**
     * get the entry which comment belongs to
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     */
    public Object getEntryCache(final long entryId, final int entryOwnerId, CommentType type);

    /**
     * 缓存评论的喜欢信息
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param likeMap
     */
    public void setCommentLikeInfoCache(Map<String, LikeInfoResult> likeMap);

    /**
     * 从缓存拿一条评论的混存信息
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @param commnetId
     */
    public List<Comment> getCommentLikeInfoCache(Map<String, Comment> commentMap);

}
