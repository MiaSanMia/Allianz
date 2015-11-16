package com.renren.ugc.comment.feed.xoa2;

import com.renren.ugc.comment.xoa2.*;
import com.renren.xoa2.annotation.XoaService;

/**
 * 为新鲜事专用的评论中心服务接口，参照comment.thrift 和 XCommentCenterForFeed.thrift
 *
 * @author jiankuan.xing
 */
@XoaService("feed_comment.ugc.xoa.renren.com")
public interface IXCommentCenterForFeed extends XCommentCenterForFeed.Iface {
    @Override
    public PingResponse ping(PingRequest req);

    @Override
    public GetCommentsForFeedResponse getCommentsForFeed(GetCommentsForFeedRequest req);

    @Override
    public GetCommentsForFeedCountResponse getCommentsForFeedCount(GetCommentsForFeedCountRequest req);

}
