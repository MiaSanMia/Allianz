package com.renren.ugc.comment.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.RowHandler;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.impl.CommentUrlRowMapper;

/**
 * @author wangxx
 * 
 *         全站分享的dao,orz
 */
@DAO(catalog = "universe_comment")
public interface UrlCommentDAO {

    static final String COMMENT_TABLE = "url_comment";

    static final String DB_FIELDS = " id, url_md5,user_id, share_id, share_owner, comment, "
            + "create_time, reply_to_user, mark_bit, meta ,type";

    /**
     * create a comment
     */
    @SQL("INSERT INTO " + COMMENT_TABLE + "(" + DB_FIELDS
            + ") VALUES (:id, :url_md5,:user_id, :share_id, :share_owner, :comment, "
            + "now(), :reply_to_user, :mark_bit, :meta, :type)")
    public int insert(@SQLParam("id") long id, @ShardBy @SQLParam("url_md5") String urlMd5,
            @SQLParam("user_id") int userId, @SQLParam("share_id") long shareId,
            @SQLParam("share_owner") int shareOwner, @SQLParam("comment") String comment,
            @SQLParam("reply_to_user") int replyToUser, @SQLParam("mark_bit") int markBit,
            @SQLParam("meta") String metatdata,@SQLParam("type") int type);

    /**
     * 获得一个评论Url md5值对应的评论列表
     * 
     * @param urlMd5
     * @param limit
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE + " FORCE INDEX(idx_url_md5_create_time)"
            + " WHERE url_md5 = :url_md5 AND id <= :id ORDER BY create_time ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getUrlCommentListByUrlWithId(@ShardBy @SQLParam("url_md5") final String urlMd5,
            @SQLParam("id") long id, @SQLParam("order") String order,
            @SQLParam("limit") final int limit);

    /**
     * 获得一个评论Url md5值对应的评论列表
     * 
     * @param urlMd5
     * @param limit
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE url_md5 = :url_md5 ORDER BY create_time ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getUrlCommentListByUrl(@ShardBy @SQLParam("url_md5") final String urlMd5,
            @SQLParam("order") String order, @SQLParam("limit") final int limit);

    /**
     * 获得一个评论Url md5值对应的评论列表
     * 
     * @param urlMd5
     * @param offset
     * @param limit
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE url_md5 = :url_md5  ORDER BY create_time ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getUrlCommentListByUrlWithOffset(
            @ShardBy @SQLParam("url_md5") final String urlMd5, @SQLParam("order") String order,
            @SQLParam("limit") final int limit, @SQLParam("offset") int offset);

    /**
     * 获得特定评论ID的UrlComment
     * 
     * @param urlMd5
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE  + " FORCE INDEX(idx_mdt_id)"
            + " WHERE url_md5 = :url_md5 and id = :id")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public Comment getUrlCommentByUrl(@ShardBy @SQLParam("url_md5") final String urlMd5,
            @SQLParam("id") long id);

    /**
     * 获得指定Url的最新评论
     * 
     * @param urlMd5
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE 
            + " WHERE url_md5 = :url_md5 ORDER BY create_time ASC LIMIT 1")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public Comment getLatestCommentOfEntry(@ShardBy @SQLParam("url_md5") final String urlMd5);

    /**
     * 获得指定的Url的最老一条评论
     * 
     * @param urlMd5
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE url_md5 = :url_md5 ORDER BY create_time ASC LIMIT 1")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public Comment getOldestCommentOfEntry(@ShardBy @SQLParam("url_md5") final String urlMd5);

    /**
     * 删除一条评论 根据 评论ID
     * 
     * @param id
     * @return
     */
    @SQL("DELETE FROM " + COMMENT_TABLE + " WHERE id = :id AND url_md5 = :url_md5")
    public int deleteUrlComment(@SQLParam("id") final long id,
            @ShardBy @SQLParam("url_md5") final String urlMd5);

    /**
     * 查询好友评论列表
     * 
     * @param md5
     * @param entryOwnerId
     * @param offset
     * @param order
     * @param limit
     * @param userIds
     * @param replyToUsers
     * @return
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE url_md5 = :md5 AND user_id in (:userIds) AND reply_to_user in (:replyToUsers) ORDER BY create_time ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getFriendsCommentListByEntryWithOffset(
            @ShardBy @SQLParam("md5") String md5, @SQLParam("entryOwnerId") int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit, @SQLParam("userIds") List<Integer> userIds,
            @SQLParam("replyToUsers") List<Integer> replyToUsers);

    /**
     * 获取好友列表
     * 
     * @param md5
     * @param entryOwnerId
     * @return
     */
    @SQL("SELECT user_id" + " FROM " + COMMENT_TABLE
            + " WHERE url_md5 = :md5 group by user_id")
    public List<Integer> getAuthorListByEntry(@ShardBy @SQLParam("md5") String md5,
            @SQLParam("entryOwnerId") int entryOwnerId);

    /**
     * 根据urlmd5获取全局评论数量
     * 
     * @param urlmd5
     * @return
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE  + " FORCE INDEX(idx_mdt_id) WHERE url_md5 = :urlmd5")
    public long countByEntry(@ShardBy @SQLParam("urlmd5") String urlmd5);
    

    /**
     * 根据urlmd5和userIds、replyToUsers获取好友评论数量
     * 
     * @param urlmd5
     * @param userIds
     * @param replyToUsers
     * @return
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE + " WHERE url_md5 = :urlmd5 AND user_id in (:userIds) AND reply_to_user in (:replyToUsers)")
    public long getFriendCommentCountByEntry(@ShardBy @SQLParam("urlmd5") String urlmd5, @SQLParam("userIds") List<Integer> userIds,
            @SQLParam("replyToUsers") List<Integer> replyToUsers);
    
    /**
     * 获取最新的N条好友评论
     * 
     * @param urlmd5
     * @param userIds
     * @param replyToUsers
     * @param limit
     * @return
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE url_md5 = :md5 AND user_id in (:userIds) AND reply_to_user in (:replyToUsers) ORDER BY created_time DESC LIMIT :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getNLatestFriendCommentOfEntry(@ShardBy @SQLParam("urlmd5") String urlmd5, @SQLParam("userIds") List<Integer> userIds,
            @SQLParam("replyToUsers") List<Integer> replyToUsers, @SQLParam("limit") int limit);
    
    /**
     * 获取最老的N条好友评论
     * 
     * @param urlmd5
     * @param userIds
     * @param replyToUsers
     * @param limit
     * @return
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE url_md5 = :md5 AND user_id in (:userIds) AND reply_to_user in (:replyToUsers) ORDER BY created_time ASC LIMIT :limit")
    @RowHandler(rowMapper = CommentUrlRowMapper.class)
    public List<Comment> getNOldestFriendCommentOfEntry(@ShardBy @SQLParam("urlmd5") String urlmd5, @SQLParam("userIds") List<Integer> userIds,
            @SQLParam("replyToUsers") List<Integer> replyToUsers, @SQLParam("limit") int limit);

}
