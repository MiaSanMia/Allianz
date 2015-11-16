package com.renren.ugc.comment.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.RowHandler;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.impl.CommentRowMapper;
import com.renren.ugc.comment.service.impl.MultiCommentsRowMapper;

@DAO(catalog = "universe_comment")
public interface CommentDAO {

    static final String COMMENT_TABLE = "universe_comment";

    static final String COMMENT_DEL_TABLE = "universe_comment_del";

    /**
     * 全站评论表
     */
    static final String GLOBAL_COMMENT_TABLE = "url_comment";

    static final String DB_FIELDS = "id, type, author_id, entry_id, entry_owner_id, content, "
            + "created_time, to_user_id, to_comment_id, whisper_to_id, flag, metadata";

    static final String URL_COMMENT_FIELDS = "id, url_md5, user_id, comment, created_time, share_id, share_owner, "
            + "mark_bit, meta, extbit, source, reply_to_user, reply_to_comment";

    /**
     * create a comment
     */
    @SQL("INSERT INTO "
            + COMMENT_TABLE
            + "("
            + DB_FIELDS
            + ") VALUES (:id, :type, :authorId, :entryId, :entryOwnerId, :content, :createdTime, :toUserId, :toCommentId, :whisperToId, :flag, :metadata)")
    public int insert(@SQLParam("id") long id, @SQLParam("type") int type,
            @SQLParam("authorId") int authorId, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("content") String content, @SQLParam("createdTime") Date createdTime,
            @SQLParam("toUserId") int toUserId, @SQLParam("toCommentId") long toCommentId,
            @SQLParam("whisperToId") int whisperToId, @SQLParam("flag") int flag,
            @SQLParam("metadata") String metatdata);

    /**
     * move a comment from comment table to comment_del table
     */
    @SQL("INSERT INTO "
            + COMMENT_DEL_TABLE
            + "("
            + DB_FIELDS
            + ", del_user_id, del_time)"
            + " VALUES(:id, :type, :authorId, :entryId, :entryOwnerId, :content, :createdTime, :toUserId, :toCommentId, :whisperToId, :flag, :metadata, :delUser, :delTime)")
    public int insertToDeletedCommentTable(@SQLParam("id") long id, @SQLParam("type") int type,
            @SQLParam("authorId") int authorId, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("content") String content, @SQLParam("createdTime") Date createdTime,
            @SQLParam("toUserId") int toUserId, @SQLParam("toCommentId") long toCommentId,
            @SQLParam("whisperToId") int whisperToId, @SQLParam("flag") int flag,
            @SQLParam("metadata") String metatdata, @SQLParam("delUser") int actorId,
            @SQLParam("delTime") Date deletedTime);

    /**
     * purge a comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE id = :id ")
    public int purgeFromCommentTable(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * purge a comment from deleted comment table
     */
    @SQL("DELETE FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    public int purgeFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get a comment
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE id = :id")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment get(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);
    
    /**
     * get a comment
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE id = :id")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getByCommentId(@SQLParam("id") long id, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get a comment from delete table
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE id in (:commentIds) AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    @RowHandler(rowMapper = MultiCommentsRowMapper.class)
    public Map<Long, Comment> getMultipleCommentsByEntry(@SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("commentIds") List<Long> commentIds);

    /**
     * Main query, get the comment list by an entry.<br/>
     * We cannot use "ORDER BY id" because this will make MySQL use the
     * incorrect index.
     * 
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByEntry(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("order") String order, @SQLParam("limit") int limit);

    /**
     * Main query + paging support, get the comment list by an entry (asc).
     * We cannot use "ORDER BY id" because this will make MySQL use the
     * incorrect index.
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type AND id >= :borderId ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByEntryASC(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * Main query + paging support, get the comment list by an entry (desc)
     * We cannot use "ORDER BY id" because this will make MySQL use the
     * incorrect index.
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type AND id <= :borderId ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByEntryDESC(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * We cannot use "ORDER BY id" because this will make MySQL use the
     * incorrect index.
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByEntryWithOffset(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit);

    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id  = :entryOwnerId AND author_id in (:userIds) AND to_user_id in (:replyToUsers) AND type = :type ORDER BY created_time ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getFriendsCommentListByEntryWithOffset(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit, @SQLParam("userIds") List<Integer> userIds,
            @SQLParam("replyToUsers") List<Integer> replyToUsers);

    /**
     * get the total number of comments belonging to an entry
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type")
    public long countByEntry(@SQLParam("type") int type, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get the oldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id ASC LIMIT 1")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getOldestCommentOfEntry(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get the latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id DESC LIMIT 1")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getLatestCommentOfEntry(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * update comment's content
     */
    @SQL("UPDATE "
            + COMMENT_TABLE
            + " SET content = :newContent, flag = :newFlag WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    public int updateCommentContent(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("newContent") String newContent, @SQLParam("newFlag") int newFlag);

    /**
     * batch get comment count
     */
    @SQL("SELECT entry_id,count(id) "
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id in (:ids) AND entry_owner_id = :entryOwnerId AND type = :type group by entry_id")
    public Map<Long, Integer> getCountBatch(@SQLParam("type") int type,
            @SQLParam("ids") List<Long> entryIds,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    @SQL("SELECT author_id"
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type group by author_id")
    public List<Integer> getAuthorListByEntry(@SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("type") int type);

    /**
     * purge all comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE  entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type")
    public int purgeFromCommentTable(@SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("type") int type);
    
    /**
     * get the Noldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getNOldestCommentOfEntry(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("limit") int limit);

    /**
     * get theN latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(idxidx_entryId_entryOwnerId_type) "
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND type = :type ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getNLatestCommentOfEntry(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("limit") int limit);
    /**
     * 根据评论作者过滤要显示的评论
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @param offset
     * @param order
     * @param limit
     * @return
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE 
    		+ " WHERE  entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id = :authorId  AND type = :type " 
    		+ " ORDER BY  " + " id ##(:order) LIMIT :offset, :limit ")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdWithOffset(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("authorId") int authorId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit);
    
    /**
     * 返回一个entry下面某个人的回复总数
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id = :authorId AND type = :type ")
    public long countByEntryAndAuthorId(@SQLParam("type") int type, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("authorId") int authorId);
    
    /**
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param offset
     * @param order
     * @param limit
     * @param authorIds
     * @param toUserIds
     * @return
     * 
     *  根据authorId和回复者id进行过滤
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  AND "
            + " type = :type ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdAndToUserIdWithOffset(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type AND id >= :borderId ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdAndToUserIdWithIdAsc(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);

    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type AND id >= :borderId ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdAndToUserIdWithIdDesc(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type ORDER BY id ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdAndToUserId(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("order") String order, @SQLParam("limit") int limit,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type ORDER BY id ASC LIMIT 1")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getOldestCommentByAuthorIdAndToUserId(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);

    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type ORDER BY id DESC LIMIT 1")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public Comment getLatestCommentByAuthorIdAndToUserId(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);
    
    @SQL("SELECT count(id)"
            + " FROM "
            + COMMENT_TABLE
            + " WHERE entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id in (:authorIds) AND to_user_id in (:toUserIds)  "
            + " AND type = :type")
    public long getCommentCountByAuthorIdAndToUserId(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,@SQLParam("authorIds") List<Integer> authorIds,
            @SQLParam("toUserIds") List<Integer> toUserIds);
    
    //根据作者升序过滤评论
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE 
    		+ " WHERE  entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id = :authorId  AND type = :type " 
    		+ " AND id >= :borderId ORDER BY id ASC LIMIT :limit ")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdWithBorderIdASC(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("authorId") int authorId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);
    
    //根据作者降序过滤评论
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE 
    		+ " WHERE  entry_id = :entryId AND entry_owner_id = :entryOwnerId AND author_id = :authorId  AND type = :type " 
    		+ " AND id <= :borderId ORDER BY id DESC LIMIT :limit ")
    @RowHandler(rowMapper = CommentRowMapper.class)
    public List<Comment> getCommentListByAuthorIdWithBorderIdDESC(@SQLParam("type") int type,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("authorId") int authorId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);
    
    @SQL("SELECT entry_id "
            + " FROM "
            + COMMENT_TABLE
            + " WHERE id = :commentId")
    public Long getEntryId(@SQLParam("type") int type, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("commentId") long commentId);

}
