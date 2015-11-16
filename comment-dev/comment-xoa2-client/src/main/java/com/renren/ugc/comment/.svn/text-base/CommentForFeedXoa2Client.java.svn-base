package com.renren.ugc.comment;

import com.renren.ugc.comment.feed.xoa2.IXCommentCenterForFeed;
import com.renren.ugc.comment.xoa2.*;
import com.renren.xoa2.client.ServiceFactory;

import java.util.List;

/**
 * User: jiankuan
 * Date: 3/12/13
 * Time: 14:53
 * The Client to access Comment for Feed XOA2 service
 */
public class CommentForFeedXoa2Client {

    private IXCommentCenterForFeed xcc;

    public CommentForFeedXoa2Client(){
        String addr = System.getProperty("comment.feed.addr", null);
        int timeout =
                Integer.parseInt(System.getProperty("comment.feed.timeout", "0"));

        if (addr != null) {
            System.setProperty("xoa2.hosts.feed_comment.ugc.xoa.renren.com", addr);
        }

        if (timeout > 0) {
            xcc = ServiceFactory.getService(IXCommentCenterForFeed.class, timeout);
        } else {
            xcc = ServiceFactory.getService(IXCommentCenterForFeed.class);
        }
    }

    public PingResponse ping(String content) {
        PingRequest req = new PingRequest();
        req.setContent(content);
        return xcc.ping(req);

    }

    public GetCommentsForFeedResponse getCommentsForFeed(int actorId, List<FeedCommentInfo> infos) {

        GetCommentsForFeedRequest req = new GetCommentsForFeedRequest();
        req.setInfos(infos);
        req.setActorId(actorId);

        return xcc.getCommentsForFeed(req);
    }

    public GetCommentsForFeedCountResponse getCommentsForFeedCount(List<FeedCommentInfo> infos,int actorId){

        GetCommentsForFeedCountRequest req = new GetCommentsForFeedCountRequest();
        req.setInfos(infos);
        req.setActorId(actorId);

        return xcc.getCommentsForFeedCount(req);

    }
}
