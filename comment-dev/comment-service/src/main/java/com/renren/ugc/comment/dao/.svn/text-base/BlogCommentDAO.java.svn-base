package com.renren.ugc.comment.dao;

import java.util.Date;

import org.springframework.dao.DataAccessException;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;
import net.paoding.rose.jade.annotation.ShardParam;


@DAO(catalog = "blog")
public interface BlogCommentDAO {
    
    @SQL("insert into entry_comment (id,author, blog, body, owner,name,head_url, time,markbit,meta ) "
            + "values (:id, :author, :blog, :body, :owner, :name, :headUrl,  :time, :markbit, :meta )")
    public void saveBlogComment(@SQLParam("id") long id, @SQLParam("author") int authorId, 
            @SQLParam("blog") long entryId, @SQLParam("body") String content,
            @SQLParam("owner") @ShardBy int entryOwnerId,@SQLParam("name") String authorName,
            @SQLParam("headUrl") String headUrl, @SQLParam("time") Date createdTime,@SQLParam("markbit") int markbit,@SQLParam("meta") String meta);
    
    /**
     * 删除一篇blog日志的所有评论
     * 
     * @param owner
     * @param entry
     * @return
     * @throws DataAccessException
     */
    @SQL("delete from entry_comment where blog=:entry and owner=:owner")
    public int removeByEntry(@SQLParam("owner") @ShardBy int ownerId, @SQLParam("entry") long entryId) ;
    
    /**
     * 删除一条评论
     * 
     * @param blogOwnerId
     * @param id
     * @return
     * @throws DataAccessException
     */
    @SQL("delete from entry_comment where id=:id and owner=:owner")
    public int remove(@SQLParam("owner") @ShardBy int ownerId, @SQLParam("id") long id) ;
    
    /**
     * 把一篇日志的一条评论备份到entry_comment_del表中
     * 
     * @param blogOwnerId
     * @param id
     * @param hostId
     * @throws DataAccessException
     */
    @SQL("insert ignore into entry_comment_del(id,author,name,head_url,blog,body,time,owner,del_userid,del_time,meta) select id,author,name,head_url,blog,body,time,owner,"
            + ":hostId as del_userid,now() as del_time,meta from entry_comment where id=:id and owner=:blogOwnerId")
    @ShardParam(":blogOwnerId")
    public void insertEntryCommentDel(@SQLParam("blogOwnerId")
    int blogOwnerId, @SQLParam("id")
    long id, @SQLParam("hostId")
    int hostId) throws DataAccessException;

    /**
     * 备份一篇日志的所有评论
     * @param owner
     * @param entry
     * @param hostId
     * @throws DataAccessException
     */
    @SQL("replace into entry_comment_del(id,author,name,head_url,blog,body,time,owner,del_userid,del_time) select id,author,name,head_url,blog,body,time,owner,"
            + ":hostId as del_userid,now() as del_time from entry_comment where blog=:entry and owner=:owner")
    @ShardParam(":owner")
    public void insertEntryCommentDelList(@SQLParam("owner")
    int owner, @SQLParam("entry")
    long entry, @SQLParam("hostId")
    int hostId) throws DataAccessException;
    

}
