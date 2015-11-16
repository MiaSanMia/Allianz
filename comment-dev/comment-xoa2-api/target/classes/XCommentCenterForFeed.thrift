# XCommentCenter is a XOA2 version comment center
# jiankuan.xing@renren-inc.com

namespace java com.renren.ugc.comment.feed.xoa2
namespace cpp ugc.comment.feed.xoa2

include "base/xoabase.thrift"
include "ugc/comment/comment.thrift"
include "XCommentCenter.thrift"

service XCommentCenterForFeed {

  XCommentCenter.PingResponse ping(1: XCommentCenter.PingRequest req);

  /**
   * 为新鲜事定制的批量获取最老和最新的评论列表的接口
   */
  XCommentCenter.GetCommentsForFeedResponse getCommentsForFeed(1: XCommentCenter.GetCommentsForFeedRequest req);

    /**
   * 为新鲜事定制的批量获取评论的接口
   */
  XCommentCenter.GetCommentsForFeedCountResponse getCommentsForFeedCount(1: XCommentCenter.GetCommentsForFeedCountRequest req);
}