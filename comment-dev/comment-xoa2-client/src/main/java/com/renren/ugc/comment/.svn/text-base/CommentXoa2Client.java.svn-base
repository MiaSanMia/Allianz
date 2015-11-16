package com.renren.ugc.comment;

import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.xoa2.CommentLinkedInfo;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.CreateCommentRequest;
import com.renren.ugc.comment.xoa2.CreateCommentResponse;
import com.renren.ugc.comment.xoa2.CreateLinkedCommentRequest;
import com.renren.ugc.comment.xoa2.CreateLinkedCommentResponse;
import com.renren.ugc.comment.xoa2.CreateVoiceCommentRequest;
import com.renren.ugc.comment.xoa2.CreateVoiceCommentResponse;
import com.renren.ugc.comment.xoa2.FeedCommentInfo;
import com.renren.ugc.comment.xoa2.GetCommentListRequest;
import com.renren.ugc.comment.xoa2.GetCommentListResponse;
import com.renren.ugc.comment.xoa2.GetCommentRequest;
import com.renren.ugc.comment.xoa2.GetCommentResponse;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedCountRequest;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedCountResponse;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedRequest;
import com.renren.ugc.comment.xoa2.GetCommentsForFeedResponse;
import com.renren.ugc.comment.xoa2.GetFriendsCommentListRequest;
import com.renren.ugc.comment.xoa2.GetFriendsCommentListResponse;
import com.renren.ugc.comment.xoa2.GetGlobalCommentListRequest;
import com.renren.ugc.comment.xoa2.GetGlobalCommentListResponse;
import com.renren.ugc.comment.xoa2.GetHeadAndTailCommentListRequest;
import com.renren.ugc.comment.xoa2.GetHeadAndTailCommentListResponse;
import com.renren.ugc.comment.xoa2.IXCommentCenter;
import com.renren.ugc.comment.xoa2.PingRequest;
import com.renren.ugc.comment.xoa2.PingResponse;
import com.renren.ugc.comment.xoa2.RemoveCommentRequest;
import com.renren.ugc.comment.xoa2.RemoveCommentResponse;
import com.renren.xoa2.client.ServiceFactory;

public class CommentXoa2Client {

    private IXCommentCenter xcc;

    public CommentXoa2Client(){
        String addr = System.getProperty("comment.addr", null);
        int timeout =
                Integer.parseInt(System.getProperty("comment.timeout", "0"));

        if (addr != null) {
            System.setProperty("xoa2.hosts.comment.ugc.xoa.renren.com", addr);
        }

        if (timeout > 0) {
            xcc = ServiceFactory.getService(IXCommentCenter.class, timeout);
        } else {
            xcc = ServiceFactory.getService(IXCommentCenter.class);
        }
    }

    public PingResponse ping(String content) {
        PingRequest req = new PingRequest();
        req.setContent("hello");
        return xcc.ping(req);

    }

    public GetFriendsCommentListResponse getFriendsCommentList(
        CommentType type, int actorId, long entryId, int entryOwnerId,
        int offset, int limit) {
        GetFriendsCommentListRequest req = new GetFriendsCommentListRequest();
        req.setType(type);
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setOffset(offset);
        req.setLimit(limit);
        return xcc.getFriendsCommentList(req);
    }

    public GetCommentListResponse getCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, int offset, int limit, boolean isDesc, Map<String, String> params) {
        GetCommentListRequest req = new GetCommentListRequest();
        req.setType(type);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setActorId(actorId);
        req.setOffset(offset);
        req.setLimit(limit);
        req.setDesc(isDesc);
        req.putToParams("replaceUBB", "false");
        req.putToParams("replaceShortUrl", "true");
        if (params != null) {
            req.setParams(params);
        }
        return xcc.getCommentList(req);
    }

    public GetCommentResponse getComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId) {
        GetCommentRequest req = new GetCommentRequest();
        req.setType(type);
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);
        return xcc.getComment(req);
    }

    public GetHeadAndTailCommentListResponse getHeadAndTailCommentList(
        CommentType type, int actorId, long entryId, int entryOwnerId,
        int headLimit, int tailLimit, boolean isDesc) {
        GetHeadAndTailCommentListRequest req =
                new GetHeadAndTailCommentListRequest();
        req.setType(type);
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setHeadLimit(headLimit);
        req.setTailLimit(tailLimit);
        req.setDesc(isDesc);

        return xcc.getHeadAndTailCommentList(req);
    }

    public CreateCommentResponse createComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, String content, int replyToId,
        Map<String, String> params) {
        CreateCommentRequest req = new CreateCommentRequest();
        req.setType(type);
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setContent(content);
        if (replyToId > 0) {
            req.setReplyToId(replyToId);
        }
        if (params != null) {
            req.setParams(params);
        }

        return xcc.createComment(req);
    }

    public GetGlobalCommentListResponse getGlobalCommentList(CommentType type,
        int actorId, long entryId, int entryOwnerId, int offset, int limit,
        boolean isDesc) {
        GetGlobalCommentListRequest req = new GetGlobalCommentListRequest();
        req.setActorId(actorId);
        req.setType(type);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setOffset(offset);
        req.setLimit(limit);
        req.setDesc(isDesc);

        return xcc.getGlobalCommentList(req);
    }

    public GetGlobalCommentListResponse getGlobalCommentList(CommentType type,
        int actorId, String urlMd5, int offset, int limit, boolean isDesc) {
        GetGlobalCommentListRequest req = new GetGlobalCommentListRequest();
        req.setActorId(actorId);
        req.setType(type);
        req.setUrlmd5(urlMd5);
        req.setOffset(offset);
        req.setLimit(limit);
        req.setDesc(isDesc);

        return xcc.getGlobalCommentList(req);
    }

    public GetGlobalCommentListResponse getGlobalCommentList(CommentType type,
        int actorId, String urlMd5, long borderCommentId, int limit,
        boolean isDesc) {
        GetGlobalCommentListRequest req = new GetGlobalCommentListRequest();
        req.setActorId(actorId);
        req.setType(type);
        req.setUrlmd5(urlMd5);
        req.setBorderCommentId(borderCommentId);
        req.setLimit(limit);
        req.setDesc(isDesc);

        return xcc.getGlobalCommentList(req);
    }

    public RemoveCommentResponse removeComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId,
        Map<String, String> params) {
        RemoveCommentRequest req = new RemoveCommentRequest();
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setCommentId(commentId);
        req.setType(type);

        return xcc.removeComment(req);

    }
    
    public CreateLinkedCommentResponse createLinkedComment(CommentType type, int actorId, long entryId,
            int entryOwnerId, String content, int replyToId, Map<String, String> params, List<CommentLinkedInfo> commentLinkedInfos) {
        CreateLinkedCommentRequest req = new CreateLinkedCommentRequest();
        req.setType(type);
        req.setActorId(actorId);
        req.setEntryId(entryId);
        req.setEntryOwnerId(entryOwnerId);
        req.setContent(content);
        if (replyToId > 0) {
            req.setReplyToId(replyToId);
        }
        if (params != null) {
            req.setParams(params);
        }
        req.setCommentLinkedInfos(commentLinkedInfos);

        return xcc.createLinkedComment(req);
    }
    
    public CreateVoiceCommentResponse createVoiceComment(CommentType type, int actorId,
            long entryId, int entryOwnerId, String content, int replyToId,
            Map<String, String> params,int voiceLength,int voiceRate,int voiceSize,String voiceUrl) {
        CreateVoiceCommentRequest req = new CreateVoiceCommentRequest();
            req.setType(type);
            req.setActorId(actorId);
            req.setEntryId(entryId);
            req.setEntryOwnerId(entryOwnerId);
            if (replyToId > 0) {
                req.setReplyToId(replyToId);
            }
            if (params != null) {
                req.setParams(params);
            }
            req.setVoiceLength(voiceLength);
            req.setVoiceRate(voiceRate);
            req.setVoiceSize(voiceSize);
            req.setVoiceUrl(voiceUrl);
            

            return xcc.createVoiceComment(req);
        }
    
    public GetCommentsForFeedCountResponse getCommentsForFeedCount(List<FeedCommentInfo> infos,int actorId){
        
        GetCommentsForFeedCountRequest req = new GetCommentsForFeedCountRequest();
        req.setInfos(infos);
        req.setActorId(actorId);
        
        return xcc.getCommentsForFeedCount(req);
        
    }
    
public GetCommentsForFeedResponse getCommentsForFeed(List<FeedCommentInfo> infos,int actorId){
        
        GetCommentsForFeedRequest req = new GetCommentsForFeedRequest();
        req.setInfos(infos);
        req.setActorId(actorId);
        
        return xcc.getCommentsForFeed(req);
        
    }
}
