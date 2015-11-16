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
import com.renren.ugc.comment.service.impl.StatusCommentRowMapper;
import com.renren.ugc.comment.service.impl.StatusAuthorCommentRowMapper;

@DAO(catalog = "biz_doing_info")
public interface StatusCommentDAO {
    
    static final String COMMENT_TABLE = " reply_doing ";

    static final String COMMENT_DEL_TABLE = " reply_doing_del ";

    static final String DB_FIELDS = " id, owner_id, doing_id , replyer_id, reply_content, reply_time ,markbit ";
    
    /**
     * create a comment
     */
    @SQL("INSERT INTO "
            + COMMENT_TABLE
            + "("
            + DB_FIELDS + " ,replyer_tinyurl, replyer_name"
            + ") VALUES (:id, :owner_id, :doing_id , :replyer_id, :reply_content, :reply_time , :markbit , :head_url, :name  )")
    public int insert(@SQLParam("id") long id, @SQLParam("replyer_id") int authorId, 
            @SQLParam("doing_id") long entryId, @SQLParam("reply_content") String content,
            @SQLParam("owner_id") @ShardBy int entryOwnerId, @SQLParam("reply_time") Date createdTime,@SQLParam("markbit") int state
            ,@SQLParam("head_url") String head_url,@SQLParam("name") String name);
    
    /**
     * move a comment from comment table to comment_del table
     */
    @SQL("INSERT INTO "
            + COMMENT_DEL_TABLE
            + "("
            + DB_FIELDS
            + ", del_userid, del_time" +  " ,replyer_tinyurl, replyer_name"
            + " )VALUES (:id, :owner_id, :doing_id , :replyer_id, :reply_content, :reply_time , :markbit, :delUser, :delTime,:head_url, :name)")
    public int insertToDeletedCommentTable(@SQLParam("id") long id, @SQLParam("replyer_id") int authorId, 
            @SQLParam("doing_id") long entryId, @SQLParam("reply_content") String content,
            @SQLParam("owner_id") @ShardBy int entryOwnerId, @SQLParam("reply_time") Date createdTime,@SQLParam("markbit") int state, @SQLParam("delUser") int actorId,
            @SQLParam("delTime") Date deletedTime,@SQLParam("head_url") String head_url,@SQLParam("name") String name);

    /**
     * purge a comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE id = :id ")
    public int purgeFromCommentTable(@SQLParam("id") long id, 
            @SQLParam("owner_id") @ShardBy int entryOwnerId);

    /**
     * purge a comment from deleted comment table
     */
    @SQL("DELETE FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND doing_id = :doing_id AND owner_id = :owner_id")
    public int purgeFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    /**
     * 获取好友列表
     * 
     * @param md5
     * @param entryOwnerId
     * @return
     */
    @SQL("SELECT id, replyer_id" + " FROM " + COMMENT_TABLE + " FORCE INDEX(doing_id) "
            + " WHERE id > :id and doing_id = :entryId ORDER BY id asc LIMIT :limit ")
    @RowHandler(rowMapper = StatusAuthorCommentRowMapper.class)
    public List<Comment> getAuthorListByEntry(
            @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("entryId") long entryId,
            @SQLParam("id") long id,
            @SQLParam("limit") int limit);

    /**
     * get a comment
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE id = :id")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public Comment get(@SQLParam("id") long id, 
            @SQLParam("owner_id") @ShardBy int entryOwnerId);

    /**
     * get a comment from delete table
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND  doing_id = :doing_id AND owner_id = :owner_id")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public Comment getFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId);

    
    @SQL("select doing_id from " + COMMENT_TABLE 
    		+ " where id = :replyId and owner_id= :owner_id")
	public Long getEntryId( // NL
            @SQLParam("replyId") long replyId,
            @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    /**
     * main query, get the comment list by an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id ORDER BY id ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getCommentListByEntry(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("order") String order, @SQLParam("limit") int limit);

    /**
     * main query + paging support, get the comment list by an entry (asc)
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND id >= :borderId ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getCommentListByEntryASC(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * main query + paging support, get the comment list by an entry (desc)
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND id <= :borderId ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getCommentListByEntryDESC(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * main query for compatibility reason only!!
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getCommentListByEntryWithOffset(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit);

    /**
     * get the total number of comments belonging to an entry
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id")
    public long countByEntry(@SQLParam("doing_id") long entryId,
            @SQLParam("owner_id") @ShardBy int entryOwnerId);

    /**
     * get the oldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id ORDER BY id ASC LIMIT 1")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public Comment getOldestCommentOfEntry(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId);

    /**
     * get the latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id ORDER BY id DESC LIMIT 1")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public Comment getLatestCommentOfEntry(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    /**
     * batch get comment count
     */
    @SQL("SELECT doing_id,count(id) "
            + " FROM "
            + COMMENT_TABLE
            + " FORCE_INDEX(doing_id)"
            + " WHERE doing_id in (:ids) group by doing_id")
    public Map<Long,Integer> getCountBatch(@SQLParam("ids") List<Long> entryIds,
    @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    
    @SQL("replace into " 
            + COMMENT_DEL_TABLE 
            + "("
            + DB_FIELDS +  " ,replyer_tinyurl, replyer_name"
            + ", del_userid, del_time)  SELECT " + DB_FIELDS +  " ,replyer_tinyurl, replyer_name"
            + ", :hostId as del_userid, now() AS del_time FROM " + COMMENT_TABLE +" WHERE doing_id = :doing_id AND owner_id = :owner_id")
    public int insertEntryCommentDelList(@SQLParam("owner_id") @ShardBy int entryOwnerId, @SQLParam("doing_id")long entryId, @SQLParam("hostId")int hostId) ;
    
    /**
     * purge all comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE  doing_id = :doing_id AND owner_id = :owner_id")
    public int purgeAllFromCommentTable( @SQLParam("doing_id") long entryId,
            @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    @SQL("SELECT replyer_id"
            + " FROM "
            + COMMENT_TABLE
            + " FORCE_INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND owner_id = :owner_id GROUP BY replyer_id")
    public List<Integer> getAuthorListByEntry(@SQLParam("doing_id") long entryId,
            @SQLParam("owner_id") @ShardBy int entryOwnerId);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE_INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND owner_id = :owner_id AND replyer_id IN (:replyer_id) ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getFriendsCommentListByEntryWithOffset(@SQLParam("doing_id") long entryId, 
            @SQLParam("owner_id") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit, @SQLParam("replyer_id") List<Integer> userIds);
    
    /**
     * get the oldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND owner_id = :owner_id ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getNOldestCommentOfEntry(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,  @SQLParam("limit") int limit);

    /**
     * get the latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " FORCE INDEX(doing_id)"
            + " WHERE doing_id = :doing_id AND owner_id = :owner_id ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = StatusCommentRowMapper.class)
    public List<Comment> getNLatestCommentOfEntry(
            @SQLParam("doing_id") long entryId, @SQLParam("owner_id") @ShardBy int entryOwnerId,@SQLParam("limit") int limit);

}
