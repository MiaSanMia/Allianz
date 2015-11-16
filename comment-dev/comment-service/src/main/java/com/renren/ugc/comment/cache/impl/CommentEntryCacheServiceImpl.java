package com.renren.ugc.comment.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.app.like.data.client.model.LikeInfoResult;
import com.renren.ugc.comment.cache.CacheManager;
import com.renren.ugc.comment.cache.CommentEntryCacheService;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLikeInfo;
import com.renren.ugc.comment.xoa2.CommentType;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;

@Service
public class CommentEntryCacheServiceImpl implements CommentEntryCacheService, InitializingBean {

    private static String BIZ_NAME_COMMENT_ENTRY = "comment_entry";

    private static String BIZ_NAME_COMMENT_LIKEINFO = "comment_likeinfo";

    private static CommentEntryCacheServiceImpl instance;

    public static CommentEntryCacheServiceImpl getInstance() {
        return instance;
    }

    @Autowired
    private CacheManager tairCacheMangager;

    @Override
    public void setEntryCache(long entryId, int entryOwnerId, CommentType type, Serializable entry) {
        if (null == entry) {
            return;
        }
        String key = getKeyForEntryCache(entryId, entryOwnerId, type);
        tairCacheMangager.put(BIZ_NAME_COMMENT_ENTRY, key, entry);
    }

    @Override
    public Object getEntryCache(long entryId, int entryOwnerId, CommentType type) {
        String key = getKeyForEntryCache(entryId, entryOwnerId, type);
        return tairCacheMangager.get(BIZ_NAME_COMMENT_ENTRY, key);
    }

    @Override
    public void setCommentLikeInfoCache(Map<String, LikeInfoResult> likeMap) {
        if (likeMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, LikeInfoResult> entry : likeMap.entrySet()) {
            String key = entry.getKey().toString();
            tairCacheMangager.put(BIZ_NAME_COMMENT_LIKEINFO, key, entry.getValue());
        }
    }

    @Override
    public List<Comment> getCommentLikeInfoCache(Map<String, Comment> commentMap) {
        Result<List<DataEntry>> result = tairCacheMangager.mget(BIZ_NAME_COMMENT_LIKEINFO,
                new ArrayList<String>(commentMap.keySet()));
        List<Comment> missedComments = new ArrayList<Comment>();
        if (null == result || CollectionUtils.isEmpty(result.getValue())) {
            return new ArrayList<Comment>(commentMap.values());
        }
        for (DataEntry data : result.getValue()) {
            LikeInfoResult like = (LikeInfoResult) data.getValue();
            if (null == like) {
                missedComments.add(commentMap.get(data.getKey()));
            }
            CommentLikeInfo likeInfo = new CommentLikeInfo();
            likeInfo.setLikeCount(like.getLikeCount());
            likeInfo.setLiked(like.isOwnerLike());
            commentMap.get(like.getSourceKey()).setLikeInfo(likeInfo);
        }
        return missedComments;
    }

    /**
     * 获取本体缓存的key
     * 
     * @param entryId
     * @param entryOwnerId
     * @param type
     * @return
     */
    private String getKeyForEntryCache(long entryId, int entryOwnerId, CommentType type) {
        StringBuffer sb = new StringBuffer();
        sb.append(entryId);
        sb.append('_');
        sb.append(entryOwnerId);
        sb.append('_');
        sb.append(type.getValue());
        return sb.toString();
    }

    public static void main(String[] args) {
        CommentEntryCacheServiceImpl impl = new CommentEntryCacheServiceImpl();
        System.out.println(impl.getKeyForEntryCache(0, 0, CommentType.Blog));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
