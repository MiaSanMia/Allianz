# Basic data model for UGC comment
# yuan.liu1@renren-inc.com

namespace java com.renren.ugc.comment.xoa2
namespace cpp ugc.comment.xoa2

enum CommentType {
  Generic = 0,                      # 新鲜事中没有对应业务的评论
  Dummy = 1,                        # 用于测试的评论类型
  Blog = 2,                         # "日志" 
  Photo = 3,                        # "照片"
  Album = 4,                        # "相册"
  Event = 5,                        # "活动"
  EventPhoto = 6,                   # "活动照片"
  PageShop = 7,                     # "page商店"
  PageShopComment = 8,              # "page商店"
  Share = 9,                        # "分享"
  SchoolPhoto = 10,                  # "校园照片"
  ForumPhoto = 11,                   # "小组照片"
  Thread = 12,                       # "帖子"
  MusicComment = 13,                 # "音乐评论"
  SongBookComment = 14,              # "人人爱听评论"
  Gift = 15,                         # "人人礼物评论"
  DiyGift = 16,                      # "人人Diy礼物评论"
  MusicLike = 17,                    # "音乐喜欢"
  WapLocation = 18,                  # "wap签到"
  Qun = 19,                          # "小群"
  OpenCommentBoxF = 20,              # "开放平台评论一楼"
  OpenCommentBoxS = 21,              # "开放平台评论二楼"
  Minisite = 22,                     # "小站"
  Diary = 23,                        # "九宫格日志"
  Video = 24,                        # "视频"
  Status = 25,                       # "状态"
  VoiceStatus = 26,                  # "音频状态"
  CustomerExperiencePlatform = 27,   # "用户体验平台"
  ShareAlbumPhoto = 28,              # "分享的相册的照片,这是一种特殊的分享类型"
  GroupStatus = 29,                  # "群状态"
  GroupSinglePhoto = 30,             # "群单张照片"
  GroupMultiPhoto = 31,              # "群多张照片"
  ShortVideo = 32,               # "短视频"
   CampusPost = 33,              # "校园主页普通帖子"
   CampusAlbumPost = 34,               # "校园主页图帖"
   Meet = 35,               # "app向右"
   CampusExcellent = 36,    #"校园主页人人精选"
   CampusTop = 37,    #"校园主页置顶帖子"
   MeetMood = 38,               # "app向右心情模块"
  Feed = 100                         # "用于新鲜事的评论展示"
}

 

/**
 * 被评论的实体
 */
struct Entry {

  /**
   * 实体id
   */
  1: i64 id;
  
  /**
   * 实体所有者id
   */
  2: i32 ownerId;
  
  /**
   * 实体名称
   */
  3: optional string name;
  
  /**
   * 实体所有者名称
   */
  4: optional string ownerName;
  
  /**
   * 实体类型
   */
  6: optional string type;
  
  /**
   * 实体的属性。由于实体可能是各种各样不同的东西，因此这里
   * 用一个map来表示。
   */
  10: optional map<string, string> entryProps;
}

/**
 * 被回复的用户信息
 */
struct RepliedUser {
  1: i32 id;
  2: string name;
}


/**
 * 喜欢的数据
 */
struct CommentLikeInfo {

   /**
    * 是否被喜欢
    */
   1: bool liked;
   
   /**
    * 被喜欢的数量
    */
   2: i32 likeCount;
}

/**
 * 语音的数据
 */
struct CommentVoiceInfo {

  /**
   * 语音评论的URL
   */
  1: string voiceUrl;
  
  /**
   * 语音评论的时间长度 (in sec)
   */
  2: optional i32 voiceLength;
  
  /**
   * 语音评论的文件大小 (in byte)
   */
  3: optional i32 voiceSize;
  
  /**
   * 语音评论的频率
   */
  4: optional i32 voiceRate;
  
  /**
   * 语音评论播放的次数
   */
  5: optional i32 voicePlayCount;
}

/**
 * 评论被关联的信息
 */
struct CommentLinkedInfo {

   /**
   * 评论业务类型
   */
  1: CommentType type;
  
  /**
   * 被评论实体Id
   */
  2: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  3: i32 entryOwnerId;
}

  /**
   *  缩减版的评论
   */
struct SimpleComment{
  /**
   * comment的Id
   */
  1: i64 id ;
  
  /**
   * 评论者id
   */
  2: i32 authorId ;
  /**
   *  评论者名称
   */
  3: string authorName ;
  /**
   * 评论内容
   */
  4:string content;
  /**
   * 评论在一个entry下面的楼数
   */
  5:optional i32 floor;
   
}


/**
 * 评论
 */
struct Comment {
  /**
   * comment的Id
   */
  1: i64 id ;
  
  /**
   * 评论者id
   */
  2: i32 authorId ;
  
  /**
   * 被评论的“实体”
   */
  3: Entry entry;
  
  /**
   * 评论内容，有140字限制
   */
  4: string content;

  /**
   * 评论者名字，冗余字段，发新鲜事用
   */
  5: string authorName ;
  
  /**
   * 评论者头像，冗余字段，发新鲜事用
   */
  6: string authorHead ;
  
  /**
   * 被回复者id列表 - 废弃
   */
  //8: optional list<i32> replyToUserIdList;
  
  /**
   * 被回复者列表 (等价于“被回复者id列表“，但是包含具体用户信息）- 废弃
   */
 // 9: optional list<RepliedUser> replyToUserList;
  
  /**
   * 评论内容经过处理后(例如Ubb替换)的新内容放在content，而原始的内容存储在这里
   */ 
  10: optional string originalContent;

  /**
   * 评论产生时间
   */
  12: i64 createTimeMillis;
  
  /**
   * 悄悄话用户id，该id可以看这条悄悄话
   */
  13: optional i32 whipserId ;
  
  /**
   * 被回复者用户
   */
  14: optional RepliedUser replyToUser;
  
  /**
   * 被回复评论/楼层id
   */
  15: optional i64 toCommentId;
  
  /**
   * like 信息
   */
  16: optional CommentLikeInfo likeInfo;
  
  /**
   * 是否是语音评论
   */
  17: optional bool voiceComment = false;
  
  /**
   * 语音评论详细信息
   */
  18: optional CommentVoiceInfo voiceInfo;
  
  /**
   * 评论作者的vip icon url
   */
  19: optional string authorVipIcon;
  
  /**
   * 保存评论的原有id，如果评论是从其他业务导入过来的话
   */
  20: optional i64 originalCommentId;
  
  /**
   * 扩展字段
   */
  21: optional map<string, string> params;

  /**
   * 是否是linked评论
   */
  22: optional bool linkedComment = false;
  
  /**
   * author是否是连续多天登录的(这样前端会判断是否显示成为橙名)
   */
  23: optional bool authorKeepUse = false;
  
  /**
   * 评论type
   */
  24: optional CommentType type;
    /**
   *  评论在一个entry下面的楼数
   */
  25:optional i32 floor;
  /**
   * 嵌套进入一个简单comment对象
   */
  26:optional SimpleComment simpleComment;
  
    /**
   * 评论附带图片的缩略图
   */
  27: optional string commentPhotoUrl;
  
  /**
   * 评论附带图片的id
   */
  28: optional i64 commentPhotoId;
}


# --- multi create struct --- #
struct MultiParamForCreate{
  /**
   * 评论内容
   */
  1: string content
  
  /**
   * 是否是悄悄话
   */
  2: optional bool whisper;
  
  
  /**
   * 被回复的用户Id
   */
  4: optional i32 replyToId
  
  /**
   * 被回复的评论（楼层）的id
   */
  5: optional i64 toCommentId;

  /**
   * 添加评论用户Id
   */
  6: i32 actorId;

  /**
   * 额外的参数，用于传入业务相关的参数
   */
  7: optional map<string, string> params;

}

# --- FeedsCommonCommentsInfo --- #
struct CommonEntryInfo{
  
  /**
   * 评论业务类型
   */
  1: CommentType type;

  /**
   * 被评论实体Id
   */
  2: i64 entryId;
  
  /**
   * 被评论实体所有者Id
   */
  3: i32 entryOwnerId;
  
  
  /**
   * 实体的评论数
   */
  4: i32 commentCount;

  /**
   * 辅助字段
   */
  5: optional string extraKey;

}

# --- commonFeedsCommentsResult --- #
struct FriendsCommentsResult{

  /**
   * 获取的头部评论的列表
   */
  1: optional list<Comment> commentList;

  /**
   * 是否有更多的评论
   */
  2: optional bool more;

  /**
   * 辅助字段 
   */
  3: optional string extraKey;

  /**
   *资源id
   */
  4: optional i64 entryId;

}


