package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.renren.app.like.data.client.model.LikeInfoResult;
import com.renren.app.like.data.client.service.GetLikeDataClient;
import com.renren.ugc.comment.cache.CommentEntryCacheService;
import com.renren.ugc.comment.cache.impl.CommentEntryCacheServiceImpl;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLikeInfo;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         用来获取各个业务评论的"喜欢"信息
 * 
 */
public class CommentLikeInfoUtil {

    private static final Logger logger = Logger.getLogger(CommentLikeInfoUtil.class);

    private Map<CommentType, String> likePrefixMap = new HashMap<CommentType, String>();

    private static CommentLikeInfoUtil instance = new CommentLikeInfoUtil();

    private CommentEntryCacheService commentEntryCacheService = CommentEntryCacheServiceImpl.getInstance();

    private CommentLikeInfoUtil() {
        //init
        likePrefixMap.put(CommentType.Album, "albumcomment_");
        likePrefixMap.put(CommentType.Photo, "photocomment_");
        likePrefixMap.put(CommentType.Blog, "blogcomment_");
        likePrefixMap.put(CommentType.Status, "statuscomment_");
        likePrefixMap.put(CommentType.Share, "sharecomment_");
        likePrefixMap.put(CommentType.ShareAlbumPhoto, "sharecomment_");
        likePrefixMap.put(CommentType.ShortVideo, "shortvideocomment_");
    }

    public static CommentLikeInfoUtil getInstance() {
        return instance;
    }

    /**
     * @param type :{@link CommentType} type
     * @param ugcComments : a list of comments
     * @param userId : who view this page
     * @return 获取到的喜欢信息放在{@link Comment } 的likeInfo字段中
     */
    public void getCommentLikeInfo(CommentType type, List<Comment> ugcComments, int userId) {

        if (ugcComments == null || ugcComments.size() == 0) {
            return;
        }

        //1.判断likePrefixMap有没有前缀
        String prefix = likePrefixMap.get(type);

        if (logger.isDebugEnabled()) {
            logger.debug("getCommentLikeInfo type = " + type);
        }

        if (prefix == null || "".equals(prefix)) {
            logger.warn("found  no match prefix in likePrefixMap type = " + type);
            return;
        }

        //2.转化成“喜欢”业务所需的id
        //String[] ugcCommentIds = new String[ugcComments.size()];
        Map<String, Comment> commentMap = new HashMap<String, Comment>(ugcComments.size());
        for (Comment c : ugcComments) {
            String key = prefix
                    + (c.getOriginalCommentId() == 0 ? c.getId() : c.getOriginalCommentId());
            //ugcCommentIds.add(key);
            commentMap.put(key, c);
        }
        long start = System.nanoTime();
        boolean success = false;
        //决定对like信息不进行缓存，以后若要缓存的话，恢复这个,或者直接访问喜欢的cache
        //List<Comment> missedComments = commentEntryCacheService.getCommentLikeInfoCache(commentMap);
        List<Comment> missedComments = ugcComments;
        List<String> ugcCommentIds = new ArrayList<String>(missedComments.size());
        if (CollectionUtils.isEmpty(missedComments)) {
            success = true;
            long end = System.nanoTime();
            StatisticsHelper.invokeGetLikeDetailBatch((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
            return;
        } else {
            for (Comment c : missedComments) {
                String key = prefix
                        + (c.getOriginalCommentId() == 0 ? c.getId() : c.getOriginalCommentId());
                ugcCommentIds.add(key);
            }
            Map<String, LikeInfoResult> likeMap = new HashMap<String, LikeInfoResult>();
            GetLikeDataClient client = GetLikeDataClient.getInstance();
            likeMap = client.multiGetLikeUsers(ugcCommentIds, userId);
            for(Comment comment : missedComments){
                LikeInfoResult result = likeMap.get(prefix
                        + comment.getId());
                if (null != result) {
                    CommentLikeInfo likeInfo = new CommentLikeInfo();
                    likeInfo.setLiked(result.isOwnerLike());
                    likeInfo.setLikeCount(result.getLikeCount());
                    
                    comment.setLikeInfo(likeInfo);
                }
            }
            success = true;
            //setLikeInfoCacheAsyn(likeMap);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetLikeDetailBatch((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
    }

    /**
     * 异步取喜欢的信息
     * 
     * @param ugcCommentIds
     * @param userId
     */
/*    private void setLikeInfoCacheAsyn(final Map<String, LikeInfoResult> likeMap) {
        AsynJobService.asynRun(new Runnable() {

            @Override
            public void run() {
//                Map<String, LikeInfoResult> likeMap = new HashMap<String, LikeInfoResult>();
//                GetLikeDataClient client = GetLikeDataClient.getInstance();
//                likeMap = client.multiGetLikeUsers(ugcCommentIds, userId);
                commentEntryCacheService.setCommentLikeInfoCache(likeMap);
            }
        });
    }*/
}
