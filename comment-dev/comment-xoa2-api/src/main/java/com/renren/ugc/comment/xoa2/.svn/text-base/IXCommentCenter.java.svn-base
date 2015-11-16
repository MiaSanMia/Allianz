/**
 * @(#)ICommentCenter.java, 2012-11-8. 
 * 
 * Copyright 2012 RenRen, Inc. All rights reserved.
 */
package com.renren.ugc.comment.xoa2;

import com.renren.xoa2.annotation.XoaService;

/**
 * 评论中心服务接口，参照comment.thrift 和 XCommentCenter.thrift
 * <p>
 * 统一访问入口
 * </p>
 * 
 * @author
 */
@XoaService("comment.ugc.xoa.renren.com")
public interface IXCommentCenter extends XCommentCenter.Iface {

    @Override
    public PingResponse ping(PingRequest req);

    @Override
    public CreateCommentResponse createComment(CreateCommentRequest req);

    @Override
    public CreateVoiceCommentResponse createVoiceComment(CreateVoiceCommentRequest req);

    @Override
    public GetCommentResponse getComment(GetCommentRequest req);

    @Override
    public GetMultiCommentsResponse getMultiComments(GetMultiCommentsRequest req);

    @Override
    public GetCommentListResponse getCommentList(GetCommentListRequest req);

    @Override
    public GetGlobalCommentListResponse getGlobalCommentList(GetGlobalCommentListRequest req);

    @Override
    public RemoveCommentResponse removeComment(RemoveCommentRequest req);

    @Override
    public RemoveAllCommentResponse removeAllComment(RemoveAllCommentRequest req);

    @Override
    public RecoverCommentResponse recoverComment(RecoverCommentRequest req);

    @Override
    public RecoverAllCommentResponse recoverAllComment(RecoverAllCommentResquest req);

    @Override
    public UpdateCommentResponse updateComment(UpdateCommentRequest req);

    @Override
    public GetCommentCountResponse getCommentCount(GetCommentCountRequest req);

    @Override
    public GetCommentCountBatchResponse getCommentCountBatch(GetCommentCountBatchRequest req);

    @Override
    public GetHeadAndTailCommentListResponse getHeadAndTailCommentList(
            GetHeadAndTailCommentListRequest req);

    @Override
    public CreateLinkedCommentResponse createLinkedComment(CreateLinkedCommentRequest req);

    @Override
    public GetFriendsCommentListResponse getFriendsCommentList(GetFriendsCommentListRequest req);
    
    @Override
    public GetCommentsForFeedResponse getCommentsForFeed(GetCommentsForFeedRequest req);
    
    @Override
    public GetCommentsForFeedCountResponse getCommentsForFeedCount(GetCommentsForFeedCountRequest req);
    
    @Override
    public RemoveGlobalCommentResponse removeGlobalComment(RemoveGlobalCommentRequest req);
    
    @Override
    public CreateCommentByListResponse createCommentByList(CreateCommentByListRequest req);
    
    @Override 
    MultiGetFriendsCommentsResponse multiGetFriendsCommonComments(MultiGetFriendsCommentsRequest req);
}
