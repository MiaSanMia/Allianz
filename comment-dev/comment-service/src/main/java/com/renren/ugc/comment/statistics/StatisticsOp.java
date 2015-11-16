package com.renren.ugc.comment.statistics;

/**
 * The object that transferred from all monitor threads to the statistics
 * thread
 * 
 * @author jiankuan.xing
 * 
 */
public class StatisticsOp {

    enum Type {
        // api
        CreateComment, RemoveComment, GetCommentList, GetComment, GetMultiComments,
        GetCommentCount, GetCommentCountBatch, GetGlobalCommentList, RecoverComment,
        CreateVoiceComment, GetHeadAndTailCommentList, CreateLinkedComment, IncreaseVoicePlayCount,
        GetFriendsCommentList, GetCommentsForFeed,GetCommentsForFeedCount, GetFriendCommentsForFeed, GetFriendCommentsForFeedCount,
        GetCommentListWithFilter,CreateCommentByList,
        
        // external api
        GetEncrpytedShortUrl, GetShortUrlOriginalContent, ReplaceLink, SendToFeed,
        GetShortUrlOriginalContentBatch, ReplaceUbb, FormatAt, FormatAtHerf,GetLikeDetailBatch,AtFilter,PostIsGagged,PostIsAdmin,
        
        // get entry
        GetShare, GetBlog, GetStatus, GetAlbum, GetPhoto, GetShareAlbumPhoto, GetBlogFromCache,GetVideo,GetPosts,GetAlbumPosts,GetTopPosts,
        
        // get entry from cache
        GetStatusFromCache, GetShareFromCache, GetAlbumFromCache,
        GetPhotoFromCache, GetShareAlbumPhotoFromCache,
        
        // internal cache
        GetFirstPageFromCache, GetGlobalFromCache, GetCountBatchFromCache, GetGlobalCountFromCache, 
        GetCountFromCache, GetCommentedFriendListFromCache, GetCommentedUserListFromCache, 
        GetLikeDetailBatchFromCache,
        
        // internal db
        GetFirstPageFromDB, CreateCommentDB, WriteOldDB,
        //internal api
        GetRelatedComments
    }

    private final Type type;

    private final long time;

    private final boolean success;

    private final boolean miss;

    public Type getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isMiss() {
        return miss;
    }

    public StatisticsOp(Type type, long time, boolean success) {
        this.type = type;
        this.time = time;
        this.success = success;
        this.miss = false;
    }

    public StatisticsOp(Type type, long time, boolean miss, boolean success) {
        this.type = type;
        this.time = time;
        this.success = success;
        this.miss = miss;
    }

}
