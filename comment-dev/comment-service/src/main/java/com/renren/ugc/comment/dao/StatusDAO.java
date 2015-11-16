package com.renren.ugc.comment.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.xiaonei.jade.datasource.Catalogs;

/**
 * 操作状态的db，主要用来更新评论数
 * @author lei.xu1	
 * @since 2013-09-12
 *
 */

@DAO(catalog = Catalogs.DOING_INFO)
public interface StatusDAO {

    @SQL("UPDATE doing SET comment_count = :incCount+comment_count WHERE id = :id and userid=:userid")
	public int incCommentCount( // NL
			@SQLParam("id") long id, // NL
			@SQLParam("userid") int userId, //NL
			@SQLParam("incCount") int incCount);

}
