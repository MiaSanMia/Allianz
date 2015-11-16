package com.renren.ugc.comment.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/**
 * The DAO to generate sequence for ugc comments
 * 
 * @author wangxx
 * 
 */
@DAO(catalog = "idseq")
public interface IdSequenceDAO {

    public static final String BLOG_COMMENT_SEQ = "blog_comment_id_seq";

    public static final String PHOTO_COMMENT_SEQ = "photo_comment_id_seq";
    
    public static final String ALBUM_COMMENT_SEQ = "album_comment_id_seq";

    public static final String STATUS_COMMENT_SEQ = "reply_doing_id_seq";
    
    public static final String URL_COMMENT_SEQ = "share_comment_id_seq";

    @SQL("select nextval('##(:name)'::text)")
    public long nextId( // NL
            @SQLParam("name") String name);
}
