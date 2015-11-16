package com.renren.ugc.comment.dao;

import java.util.Date;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import com.xiaonei.jade.datasource.Catalogs;

@DAO(catalog = Catalogs.ALBUM)
public interface AlbumCommentDAO {

    static final String DB_FIELDS = " id,album,author,time,body,owner,state ";

    static final String TABLE_NAME = " album_comment ";

    @SQL("insert into album_comment (id,album, author, name,head_url, body,time,owner,state,extension,to_id) "
            + "values ( :id, :album, :author, :name, :headUrl, :body, :time, :owner, :state, :extension, :to_id )")
    public void saveAlbumComment(@SQLParam("id") long id, @SQLParam("album") long entryId,
            @SQLParam("author") int authorId, @SQLParam("name") String authorName,
            @SQLParam("headUrl") String headUrl, @SQLParam("body") String content,
            @SQLParam("time") Date createdTime, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("state") int state, @SQLParam("extension") String meta,
            @SQLParam("to_id") long to_id);
    
    @SQL("insert into album_child_comment (id,album, author, name,head_url, body,time,owner,state) "
            + "values ( :id, :album,  :author, :name, :headUrl,  :body, :time, :owner, :state )")
    public void saveChildAlbumComment(@SQLParam("id") long id, @SQLParam("album") long entryId,
            @SQLParam("author") int authorId, @SQLParam("name") String authorName,
            @SQLParam("headUrl") String headUrl, @SQLParam("body") String content,
            @SQLParam("time") Date createdTime, @SQLParam("owner") @ShardBy int entryOwnerId,
            @SQLParam("state") int state);
    
    

}
