package com.renren.ugc.comment.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.impl.CommentRowMapper;
import com.renren.ugc.comment.service.impl.MultiPhotoCommentsRowMapper;
import com.renren.ugc.comment.service.impl.PhotoCommentRowMapper;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.RowHandler;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

@DAO(catalog = "album")
public interface PhotoCommentDAO {
    
    static final String COMMENT_TABLE = "photo_comment";

    static final String COMMENT_DEL_TABLE = "photo_comment_del";

    static final String DB_FIELDS = "id, owner, photo, author,  body, time , state";
    
    /**
     * create a comment
     */
    @SQL("INSERT INTO "
            + COMMENT_TABLE
            + "("
            + DB_FIELDS + " ,head_url, name"
            + ") VALUES (:id, :owner, :photo, :author, :body,  :time, :state, :head_url, :name )")
    public int insert(@SQLParam("id") long id, @SQLParam("author") int authorId, 
            @SQLParam("photo") long entryId, @SQLParam("body") String content,
            @SQLParam("owner") @ShardBy int entryOwnerId, @SQLParam("time") Date createdTime,@SQLParam("state") int state,
            @SQLParam("head_url") String head_url,@SQLParam("name") String name);
    
    /**
     * move a comment from comment table to comment_del table
     */
    @SQL("INSERT INTO "
            + COMMENT_DEL_TABLE
            + "("
            + DB_FIELDS
            + ", del_operater, del_time" +  " ,head_url, name"
            + " )VALUES (:id, :owner, :photo, :author, :body,  :time, :state, :delUser, :delTime, :head_url, :name)")
    public int insertToDeletedCommentTable(@SQLParam("id") long id, @SQLParam("author") int authorId, 
            @SQLParam("photo") long entryId, @SQLParam("body") String content,
            @SQLParam("owner") @ShardBy int entryOwnerId, @SQLParam("time") Date createdTime,@SQLParam("state") int state, @SQLParam("delUser") int actorId,
            @SQLParam("delTime") Date deletedTime,@SQLParam("head_url") String head_url,@SQLParam("name") String name);

    /**
     * purge a comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE id = :id ")
    public int purgeFromCommentTable(@SQLParam("id") long id, @SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * purge a comment from deleted comment table
     */
    @SQL("DELETE FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND photo = :photo AND owner = :owner")
    public int purgeFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * get a comment
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_TABLE
            + " WHERE id = :id ")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public Comment get(@SQLParam("id") long id, @SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * get a comment from delete table
     */
    @SQL("SELECT " + DB_FIELDS + " FROM " + COMMENT_DEL_TABLE
            + " WHERE id = :id AND photo = :photo")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public Comment getFromDeletedCommentTable(@SQLParam("id") long id,
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * main query, get the comment list by an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  ORDER BY id ##(:order) LIMIT :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getCommentListByEntry(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("order") String order, @SQLParam("limit") int limit);

    /**
     * main query + paging support, get the comment list by an entry (asc)
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  AND id >= :borderId ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getCommentListByEntryASC(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * main query + paging support, get the comment list by an entry (desc)
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  AND id <= :borderId ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getCommentListByEntryDESC(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("borderId") long borderCommentId, @SQLParam("limit") int limit);

    /**
     * main query for compatibility reason only!!
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getCommentListByEntryWithOffset(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit);

    /**
     * get the total number of comments belonging to an entry
     */
    @SQL("SELECT COUNT(id) FROM " + COMMENT_TABLE
            + " WHERE photo = :photo ")
    public long countByEntry(@SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * get the oldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo ORDER BY id ASC LIMIT 1")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public Comment getOldestCommentOfEntry(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId);

    /**
     * get the latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo ORDER BY id DESC LIMIT 1")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public Comment getLatestCommentOfEntry(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId);
    
    /**
     * batch get comment count
     */
    @SQL("SELECT photo,count(id) "
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo in (:ids) AND owner = :owner  group by photo")
    public Map<Long,Integer> getCountBatch(@SQLParam("ids") List<Long> entryIds,
    @SQLParam("owner") @ShardBy int entryOwnerId);
    
    
    @SQL("replace into " 
            + COMMENT_DEL_TABLE 
            + "("
            + DB_FIELDS  + " ,head_url, name"
            + ", del_operater, del_time)  select " + DB_FIELDS + " ,head_url, name"
            + ", :hostId as del_operater, now() as del_time from " + COMMENT_TABLE+" where photo = :photo AND owner = :owner")
    public int insertEntryCommentDelList(@SQLParam("owner") @ShardBy int entryOwnerId, @SQLParam("photo")long entryId, @SQLParam("hostId")int hostId) ;
    
    /**
     * purge all comment from comment table
     */
    @SQL("DELETE FROM " + COMMENT_TABLE
            + " WHERE  photo = :photo ")
    public int purgeFromCommentTable( @SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId);
    
    @SQL("SELECT author"
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  group by author")
    public List<Integer> getAuthorListByEntry(@SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  AND author in (:userIds) ORDER BY id ##(:order) LIMIT :offset, :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getFriendsCommentListByEntryWithOffset(@SQLParam("photo") long entryId, 
            @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("offset") int offset, @SQLParam("order") String order,
            @SQLParam("limit") int limit, @SQLParam("userIds") List<Integer> userIds);
    
    @SQL("update photo set commentcount = commentcount + :incCount  where id = :photo")
    int incPhotoCommentCount(@SQLParam("photo") final long photoId, @SQLParam("owner") @ShardBy final int owner,@SQLParam("incCount") int incCount);
    
    @SQL("update album set commentcount = commentcount + :incCount  where id = :album")
    int incAlbumCommentCount(@SQLParam("album") final long albumId, @SQLParam("owner") @ShardBy final int owner,@SQLParam("incCount") int incCount);
    
    @SQL("update photo_relation set count = count + :incCount  where child_id = :album")
    int incChildAlbumCommentCount(@SQLParam("album") final long albumId, @SQLParam("owner") @ShardBy final int owner,@SQLParam("incCount") int incCount);
    
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE id in (:commentIds) AND photo = :photo ")
    @RowHandler(rowMapper = MultiPhotoCommentsRowMapper.class)
    public Map<Long, Comment> getMultipleCommentsByEntry(@SQLParam("photo") long entryId,
            @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("commentIds") List<Long> commentIds);
    
    /**
     * get the oldest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo ORDER BY id ASC LIMIT :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getNOldestCommentOfEntry(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId, @SQLParam("limit") int limit);

    /**
     * get the latest comment of an entry
     */
    @SQL("SELECT "
            + DB_FIELDS
            + " FROM "
            + COMMENT_TABLE
            + " WHERE photo = :photo  ORDER BY id DESC LIMIT :limit")
    @RowHandler(rowMapper = PhotoCommentRowMapper.class)
    public List<Comment> getNLatestCommentOfEntry(
            @SQLParam("photo") long entryId, @SQLParam("owner") @ShardBy int entryOwnerId,@SQLParam("limit") int limit);
    

}
