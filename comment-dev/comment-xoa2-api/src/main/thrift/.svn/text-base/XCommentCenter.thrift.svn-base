# XCommentCenter is a XOA2 version comment center
# yuan.liu1@renren-inc.com

namespace java com.renren.ugc.comment.xoa2
namespace cpp ugc.comment.xoa2

include "base/xoabase.thrift"
include "ugc/comment/comment.thrift"

# --- ping ---
struct PingRequest {

  1: string content;
}

struct PingResponse {
  
  1: string content;
}

# ---- createComment ----
struct CreateCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 添加评论用户Id
   */
  2: i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 评论内容
   */
  5: string content

  /**
   * 被回复的用户Id列表 - 废弃
   */
  // 6: optional list<i32> replyTo;
  
  /**
   * 是否是悄悄话
   */
  7: optional bool whisper;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  8: optional map<string, string> params;
  
  /**
   * 被回复的用户Id
   */
  13: optional i32 replyToId
  
  /**
   * 被回复的评论（楼层）的id
   */
  14: optional i64 toCommentId;
  
  /**
   * 评论扩展内容 - 废弃
   */
  // 9: optional string extension;
  
  # 未知使用方法的域
  // 10: optional i32 sourceOwnerId;

  // 12: optional bool wap;
}

struct CreateCommentResponse {

  /**
   * 新创建的评论
   */
  1: optional comment.Comment comment;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
  /**
   * 被评论的“实体”
   */
  3: optional comment.Entry entry;
}

# ---- create voice comment ----
struct CreateVoiceCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 添加评论用户Id
   */
  2: i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 语音评论的URL
   */
  5: string voiceUrl;
  
  /**
   * 语音评论的时间长度 (in sec)
   */
  6: optional i32 voiceLength;
  
  /**
   * 语音评论的文件大小 (in byte)
   */
  7: optional i32 voiceSize;
  
  /**
   * 语音评论的频率
   */
  8: optional i32 voiceRate;
  
  /**
   * 是否是悄悄话
   */
  9: optional bool whisper;
  
  /**
   * 被回复的用户Id
   */
  10: optional i32 replyToId
  
  /**
   * 被回复的评论（楼层）的id
   */
  11: optional i64 toCommentId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  20: optional map<string, string> params;
}

struct CreateVoiceCommentResponse {

  /**
   * 新创建的评论
   */
  1: optional comment.Comment comment;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
   /**
   * 被评论的“实体”
   */
    3: optional comment.Entry entry;
}

# ---- getComment ----
struct GetCommentRequest {
  
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论的用户id
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 评论Id
   */
  5: i64 commentId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  6: optional map<string, string> params;
}

struct GetCommentResponse {
  /**
   * 获取的评论
   */
  1: optional comment.Comment comment;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
   /**
   * 被评论的“实体”
   */
    3: optional comment.Entry entry;
}

# ---- getCommentList ----
struct GetCommentListRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 搜索开始的位置
   */
  5: optional i32 offset;
  
  /**
   * 返回结果的最大个数
   */
  6: optional i32 limit;
  
  /**
   * 当前的分页数, 从1开始计数
   */
  7: optional i32 pageNum;
  
  /**
   * 返回结果排序，true为从最新到旧返回结果
   */
  8: optional bool desc;

  /**
   * 查询的commentId边界，通过设置此边界可以避免多次获取列表操作可能出现重复值的情况
   * 建议采用此值来进行分页。
   */
  9: optional i64 borderCommentId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  10: optional map<string, string> params;
  
    /**
   * 是否要获取全站评论，目前只对share有效
   */
  11: optional bool includeGlobalComment;

}

struct GetCommentListResponse {
  /**
   * 获取的评论的列表
   */
  1: optional list<comment.Comment> commentList;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
  
  /**
   * 这条评论之后是否有更多的评论
   */
  3: optional bool more;
  
  /**
   * 属于当前Entry的评论的总数
   */
  4: optional i64 totalCount;
  
    /**
   * 获取的全站评论的列表，目前只对share有效
   */
  5: optional list<comment.Comment> globalCommentList;

  /**
   * 是否有更多的全站评论，目前只对share有效
   */
  6: optional bool moreGlobalComment;
  
  /**
   * 属于当前Entry的全站评论的总数，目前只对share有效
   */
  7: optional i64 globalCommentTotalCount;
  
  /**
   * 被评论的“实体”
   */
  8: optional comment.Entry entry;
  
    /**
   * 这条评论之前是否有更多的评论
   */
  9: optional bool pre;
}

# ---- getMultiComments ----
struct GetMultiCommentsRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 删除评论者用户id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 待获取评论id列表
   */
  5: list<i64> commentIds;
  
  /**
   * 扩展参数
   */
  6: optional map<string, string> params;
}

struct GetMultiCommentsResponse {

   /**
    * 获取结果的map，key为commentId，value为评论本体
    */
   1: optional map<i64, comment.Comment> comments;
   
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
}

# ---- GetGlobalCommentList ----
struct GetGlobalCommentListRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 全局评论的urlmd5, urlmd5与entryId 两者之一是必填，如果以urlmd5为准，如果urlmd5为空，会根据entryId自动生成urlmd5
   */
  3: optional string urlmd5;
  
  /**
   * 被评论实体Id
   */
  4: optional i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  5: optional i32 entryOwnerId;
  
  /**
   * 查询的commentId边界，通过设置此边界可以避免多次获取列表操作可能出现重复值的情况
   * 建议采用此值来进行分页。
   */
  6: optional i64 borderCommentId;
  
  /**
   * 搜索开始的位置
   */
  7: optional i32 offset;

  /**
   * 返回结果的最大个数
   */
  8: optional i32 limit;
  
  /**
   * 返回结果排序，true为从最新到旧返回结果
   */
  9: optional bool desc;

  /**
   * 额外的参数，用于传入业务相关的参数
   */
  10: optional map<string, string> params;
}

struct GetGlobalCommentListResponse {
  /**
   * 获取的评论的列表
   */
  1: optional list<comment.Comment> commentList;
  
  /**
   * 全站评论对象
   */
  2: optional comment.Entry entry;
  
  /**
   * 是否有更多的评论
   */
  3: optional bool more;
  
  /**
   * 属于当前Entry的评论的总数
   */
  4: optional i64 totalCount;
  
  /**
   * 响应信息
   */
  5: optional xoabase.BaseResponse baseRep;
 
}


# ---- removeComment ----
struct RemoveCommentRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 删除评论者用户id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 要删除的评论Id
   */
  5: i64 commentId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  6: optional map<string, string> params;
}

struct RemoveCommentResponse {
  /**
   * 是否已删除
   */
  1: optional bool removed;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;

}

# ---- removeGlobalComment ----
struct RemoveGlobalCommentRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;

  /**
   * 删除评论者用户id
   */
  2: optional i32 actorId;

  /**
   * 被评论实体Id
   */
  3: optional i64 entryId;

  /**
   * 被评论实体所有者Id
   */
  4: optional i32 entryOwnerId;

  /**
   * 要删除的评论Id
   */
  5: i64 commentId;

  /**
   * 额外的参数，用于传入业务相关的参数
   */
  6: optional map<string, string> params;
}

struct RemoveGlobalCommentResponse {
  /**
   * 是否已删除
   */
  1: optional bool removed;

  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
}

# ---- removeAllComment ----
struct RemoveAllCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 删除评论者用户id
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  5: optional map<string, string> params;
}

struct RemoveAllCommentResponse {

  /**
   * 是否已全部删除
   */
  1: optional bool allRemoved;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
}

# ---- recover  ----
struct RecoverCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 恢复评论者用户id, 这个是必须的参数，
   * 但是为了保证兼容性标记为optional
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 要恢复的评论Id
   */
  5: i64 commentId
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  6: optional map<string, string> params;
}

struct RecoverCommentResponse {
  /**
   * 是否已经恢复
   */
  1: optional bool recovered;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 

}

# ---- recoverAll  ----
struct RecoverAllCommentResquest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 恢复评论者用户id
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  5: optional map<string, string> params;
}

struct RecoverAllCommentResponse {
  /**
   * 是否已经全部恢复
   */
  1: optional bool allRecovered;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
}

# ---- update ----
struct UpdateCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 更新评论者用户id
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 要更新的评论Id
   */
  5: i64 commentId
  
  /**
   * 要更新的新评论内容
   */
  6: string newContent;
  
  /**
   * 要评论更新的扩展内容 - 废弃
   */
  7: optional string newExtension;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  8: optional map<string, string> params;
}

struct UpdateCommentResponse {
  /**
   * 是否已经更新
   */
  1: optional bool updated;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
}

# ---- increase the voice comment's play count ----
struct IncreaseVoiceCommentPlayCountRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 增加播放次数的调用方用户id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 要增加播放次数的评论Id
   */
  5: i64 commentId;
  
  /**
   * 要增加的播放的次数，如果不设，默认为1
   */
  6: optional i32 increment = 1;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  7: optional map<string, string> params;
}

struct IncreaseVoiceCommentPlayCountResponse {
  
  /**
   * 目前该语音评论的播放次数
   */
  1: optional i32 playCount;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
}

struct GetCommentCountRequest {

   /**
    * 评论业务类型
    */
   1: comment.CommentType type;
  
   /**
    * 获取评论总数的用户Id - 对于某些业务是可选的，但是
    * 对于某些业务（比如UGC Blog）是必须的，用于
    * 来校验权限
    */
   2: optional i32 actorId;
  
   /**
    * 被评论实体Id
    */
   3: i64 entryId;
  
   /**
    * 被评论实体所有者Id
    */
   4: i32 entryOwnerId;
}

struct GetCommentCountResponse {

   /**
    * 评论总数
    */
   1: optional i32 count;
   
   /**
    * 响应信息
    */
   2: optional xoabase.BaseResponse baseRep;
}

/**
 * 批量获取多个entry评论的总数。注意，由于Sharding的限制，
 * 只支持一个对属于一个entryOwnerId的评论数量进行批量获取。
 */
struct GetCommentCountBatchRequest {

   /**
    * 评论业务类型
    */
   1: comment.CommentType type;
  
   /**
    * 获取评论总数的用户Id - 对于某些业务是可选的，但是
    * 对于某些业务（比如UGC Blog）是必须的，用于
    * 来校验权限
    */
   2: optional i32 actorId;
  
   /**
    * 被评论实体Id
    */
   3: list<i64> entryIds;
  
   /**
    * 被评论实体所有者Id
    */
   4: i32 entryOwnerId;
}

struct GetCommentCountBatchResponse {

   /**
    * 评论总数
    */
   1: optional map<i64,i32> countMap;
   
   /**
    * 响应信息
    */
   2: optional xoabase.BaseResponse baseRep;
}


# ---- Get Head And Tail Comment List ---
struct GetHeadAndTailCommentListRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 返回头部结果的最大个数, 默认是1
   * headLimit与tailLimit的和最大不能超过30
   */
  6: optional i32 headLimit = 1;
  
  /**
   * 返回尾部结果的最大个数, 默认是1
   * headLimit与tailLimit的和最大不能超过30
   */
  7: optional i32 tailLimit = 1;
  
  /**
   * 返回结果两个列表的排序。例如原始评论的id是1～10.
   * headLimit是3, tailLimit是2. 当desc为false时，返回
   * - headCommentList: 1,2,3
   * - tailCommentList: 9,10
   * 当desc为true时，返回
   * - headCommentList: 10,9,8
   * - tailCommentList: 2,1
   * desc的默认值是false
   */
  8: optional bool desc;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  9: optional map<string, string> params;

}


struct GetHeadAndTailCommentListResponse {

  /**
   * 获取的头部评论的列表
   */
  1: optional list<comment.Comment> headCommentList;
  
  /**
   * 获取的尾部评论的列表
   */
  2: optional list<comment.Comment> tailCommentList;
  
  /**
   * 响应信息
   */
  3: optional xoabase.BaseResponse baseRep;
  
  /**
   * 是否有更多的评论
   */
  4: optional bool more;
  
  /**
   * 属于当前Entry的评论的总数
   */
  5: optional i64 totalCount;
  
  /**
   * 被评论的“实体”
   */
   6: optional comment.Entry entry;
}


#----------- getFriendsCommentList -----------
struct GetFriendsCommentListRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论列表Id - 该字段对于获取好友评论是必须的
   */
  2: i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 搜索开始的位置
   */
  5: optional i32 offset;
  
  /**
   * 返回结果的最大个数
   */
  6: optional i32 limit;
  
  /**
   * 返回结果排序，true为从最新到旧返回结果
   */
  7: optional bool desc;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  8: optional map<string, string> params;

}

struct GetFriendsCommentListResponse {
  /**
   * 获取的评论的列表
   */
  1: optional list<comment.Comment> commentList;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
  
  /**
   * 是否有更多的评论
   */
  3: optional bool more;
  
  /**
   * 被评论的“实体”
   */
  4: optional comment.Entry entry;
}

# ---- createLinkedComment ----
struct CreateLinkedCommentRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 添加评论用户Id
   */
  2: i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 评论内容
   */
  5: string content
  
  /**
   * 是否是悄悄话
   */
  6: optional bool whisper;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  7: optional map<string, string> params;
  
  /**
   * 被回复的用户Id
   */
  8: optional i32 replyToId
  
  /**
   * 被回复的评论（楼层）的id
   */
  9: optional i64 toCommentId;
  
  /**
   * 被关联的评论列表
   */
  10: optional list<comment.CommentLinkedInfo> commentLinkedInfos;
}

struct CreateLinkedCommentResponse {

  /**
   * 新创建的评论
   */
  1: optional comment.Comment comment;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
  /**
   * 被评论的“实体”
   */
  3: optional comment.Entry entry;
}

# ---- GetCommentsForFeed ----
# ---- 这个接口现在只给新鲜事用 ---

# ---- GetCommentsForFeed请求的参数 ---
struct FeedCommentInfo {

  /**
   * 新鲜事id, 这个id仅用于返回结果map时用于map
   */
  1: i64 feedid;

  /**
   * 评论业务类型
   */
  2: comment.CommentType type;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 返回头部结果的最大个数, 默认是1
   */
  5: optional i32 headLimit = 1;
  
  /**
   * 返回尾部结果的最大个数, 默认是1
   */
  6: optional i32 tailLimit = 1;
  
  /**
   * 该字段废弃
   * 返回结果两个列表的排序。例如原始评论的id是1～10.
   * headLimit是3, tailLimit是2. 当desc为false时，返回
   * - headCommentList: 1,2,3
   * - tailCommentList: 9,10
   * 当desc为true时，返回
   * - headCommentList: 10,9,8
   * - tailCommentList: 2,1
   * desc的默认值是false
   */
  // 7: optional bool desc;

}

# ---- getCommentsForFeed返回的结果 ---
struct FeedCommentResult {
  /**
   * 获取的头部评论的列表
   */
  1: optional list<comment.Comment> headCommentList;
  
  /**
   * 获取的尾部评论的列表
   */
  2: optional list<comment.Comment> tailCommentList;
  
  /**
   * 是否有更多的评论
   */
  3: optional bool more;
  
  /**
   * 属于当前Entry的评论的总数
   */
  4: optional i64 totalCount;
}

struct GetCommentsForFeedRequest {

  /**
   * 批量取首尾评论的参数
   */
  1: list<FeedCommentInfo> infos;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  3: optional map<string,string> params;
}

struct GetCommentsForFeedResponse {
  /**
   * 批量取首尾评论的结果, key是请求中每个FeedCommentInfo的feedid
   */
  1: map<i64,FeedCommentResult> results;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;

}

# ---- GetFriendCommentsForFeed ----
struct GetFriendCommentsForFeedRequest {

  /**
   * 批量取首尾评论的参数
   */
  1: list<FeedCommentInfo> infos;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  3: optional map<string,string> params;
}

struct GetFriendCommentsForFeedResponse {
  /**
   * 批量取首尾评论的结果, key是请求中每个FeedCommentInfo的feedid
   */
  1: map<i64,FeedCommentResult> results;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;

}


# ---- GetCommentsForFeedCount ----#

struct GetCommentsForFeedCountRequest {

  /**
   * 批量取首尾评论的参数
   */
  1: list<FeedCommentInfo> infos;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  3: optional map<string,string> params;
}

struct GetCommentsForFeedCountResponse {
  /**
   * 批量取首尾评论的结果, key是请求中每个FeedCommentInfo的feedid
   */
  1: map<i64,i32> results;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;

}

# ---- GetFriendCommentsForFeedCount ----
struct GetFriendCommentsForFeedCountRequest {

  /**
   * 批量取首尾好友评论的参数
   */
  1: list<FeedCommentInfo> infos;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: optional i32 actorId;
  
  3: optional map<string,string> params;
}

struct GetFriendCommentsForFeedCountResponse {
  /**
   * 批量取首尾好友评论的结果, key是请求中每个FeedCommentInfo的feedid
   */
  1: map<i64,i32> results;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;

}

# ---- getCommentListWithFilter ----
struct GetCommentListWithFilterRequest {

  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 获取评论列表Id - 对于某些业务是可选的，但是
   * 对于某些业务（比如UGC Blog）是必须的，用于
   * 来校验权限
   */
  2: i32 actorId;
  
  /**
   * 被评论实体Id
   */
  3: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  4: i32 entryOwnerId;
  
  /**
   * 搜索开始的位置
   */
  5: optional i32 offset;
  
  /**
   * 返回结果的最大个数
   */
  6: optional i32 limit;
  
  /**
   * 当前的分页数, 从1开始计数
   */
  7: optional i32 pageNum;
  
  /**
   * 返回结果排序，true为从最新到旧返回结果
   */
  8: optional bool desc;
  
  /**
   * 额外的参数，用于传入业务相关的参数
   */
  9: optional map<string, string> params;
  
  /**
   * 需要过滤的作者id
   */
  10: optional i32 authorId;
  
  /**
   * 查询的commentId边界，通过设置此边界可以避免多次获取列表操作可能出现重复值的情况
   * 建议采用此值来进行分页。
   */
  11: optional i64 borderCommentId;
}

struct GetCommentListWithFilterResponse {
  /**
   * 获取的评论的列表
   */
  1: optional list<comment.Comment> commentList;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep;
  
  /**
   * 是否有更多的评论
   */
  3: optional bool more;
  
  /**
   * 属于当前Entry的评论的总数
   */
  4: optional i64 totalCount;
  
  /**
   * 被评论的“实体”
   */
  5: optional comment.Entry entry;
}

# ---- createCommentByList ----
struct CreateCommentByListRequest {
  /**
   * 评论业务类型
   */
  1: comment.CommentType type;
  
  /**
   * 被评论实体Id
   */
  2: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  3: i32 entryOwnerId;
  
  /**
   * 多条评论的参数
   */
  4: list<comment.MultiParamForCreate> MutiParam; 

}

struct CreateCommentByListResponse {

  /**
   * 新创建的评论列表
   */
  1: optional list<comment.Comment> commentList;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
  
  /**
   * 被评论的“实体”
   */
  3: optional comment.Entry entry;
}

# ----  GetFeedsComments ----
struct MultiGetFriendsCommentsRequest {
  /**
   * 要获取评论的实体列表
   */
  1: required list<comment.CommonEntryInfo> entrysInfo;

  /**
   * 当前用户id
   */
  2: required i32 actorId;
  
  /**
   *额外参数
   */
  3: optional map<string,string> params;

}

struct MultiGetFriendsCommentsResponse {

   /**
   * 新创建的评论列表  FIXME : key 去掉feed信息
   */
  1: optional list<comment.FriendsCommentsResult> friendsComments;
  
  /**
   * 响应信息
   */
  2: optional xoabase.BaseResponse baseRep; 
}



###

/**
 * WIKI: http://wiki.d.xiaonei.com/pages/viewpage.action?pageId=18157488
 */
service XCommentCenter {
  /**
   * Ping - 测试服务是否通畅
   */
  PingResponse ping(1: PingRequest req);
  
  /**
   * 创建一条评论
   */
  CreateCommentResponse createComment(1: CreateCommentRequest req);
  
  /**
   * 创建一条语音评论
   */
  CreateVoiceCommentResponse createVoiceComment(1: CreateVoiceCommentRequest req);

  /**
   * 获取一条评论
   */
  GetCommentResponse getComment(1: GetCommentRequest req);

  /**
   * 获取评论列表
   */
  GetCommentListResponse getCommentList(1: GetCommentListRequest req);
  
  /**
   * 获取多条评论
   */
  GetMultiCommentsResponse getMultiComments(1: GetMultiCommentsRequest req);
  
  /**
   * 获取全站评论列表
   */
  GetGlobalCommentListResponse getGlobalCommentList(1: GetGlobalCommentListRequest req);
   
  /**
   * 删除一条评论
   */
  RemoveCommentResponse removeComment(1: RemoveCommentRequest req);
  
  /**
   * 删除一条全局评论
   */
  RemoveGlobalCommentResponse removeGlobalComment(1: RemoveGlobalCommentRequest req);

  /**
   * 删除某个源的所有评论
   */
  RemoveAllCommentResponse removeAllComment(1: RemoveAllCommentRequest req);

  /**
   * 恢复一条评论 undo delete comment
   */
  RecoverCommentResponse recoverComment(1: RecoverCommentRequest req);

  /**
   * 恢复一个源的所有评论 undo delete all
   */
  RecoverAllCommentResponse recoverAllComment(1: RecoverAllCommentResquest req);

  /**
   * 更新一条评论
   */
  UpdateCommentResponse updateComment(1: UpdateCommentRequest req);
  
  /**
   * 增加一条语音评论的播放次数
   */
  IncreaseVoiceCommentPlayCountResponse increaseVoiceCommentPlayCount(1: IncreaseVoiceCommentPlayCountRequest req);

  /**
   * 获取评论总数
   */
  GetCommentCountResponse getCommentCount(1: GetCommentCountRequest req);
  
  /**
   * 获取多条评论总数
   */
  GetCommentCountBatchResponse getCommentCountBatch(1: GetCommentCountBatchRequest req);
  
    /**
   * 获取最老和最新的评论列表
   */
  GetHeadAndTailCommentListResponse getHeadAndTailCommentList(1: GetHeadAndTailCommentListRequest req);

  
  /**
   * 获取好友评论列表
   */
  GetFriendsCommentListResponse getFriendsCommentList(1: GetFriendsCommentListRequest req);

  
  /**
   * 创建一条有关联的评论
   */
  CreateLinkedCommentResponse createLinkedComment(1: CreateLinkedCommentRequest req);
  
  /**
   * 为新鲜事定制的批量获取最老和最新的评论列表的接口
   */
  GetCommentsForFeedResponse getCommentsForFeed(1: GetCommentsForFeedRequest req);
  
    /**
   * 为新鲜事定制的批量获取评论的接口
   */
  GetCommentsForFeedCountResponse getCommentsForFeedCount(1: GetCommentsForFeedCountRequest req);
  
  /**
   * 为新鲜事定制的批量获取最老和最新的好友评论列表的接口
   */
  GetFriendCommentsForFeedResponse getFriendCommentsForFeed(1: GetFriendCommentsForFeedRequest req);
  
  /**
   * 为新鲜事定制的批量获取好友评论的接口
   */
  GetFriendCommentsForFeedCountResponse getFriendCommentsForFeedCount(1: GetFriendCommentsForFeedCountRequest req);
  
  /**
   * 提供一个过滤功能，用来取特定某些人的评论
   */
  GetCommentListWithFilterResponse getCommentListWithFilter(1: GetCommentListWithFilterRequest req);
  
  /**
   *  为同一个实体、评论者批量创建评论接口
   */ 
   CreateCommentByListResponse createCommentByList(1: CreateCommentByListRequest req);

   /**
   *  feed获取共同好友评论
   */
   MultiGetFriendsCommentsResponse multiGetFriendsCommonComments(1: MultiGetFriendsCommentsRequest req);
}