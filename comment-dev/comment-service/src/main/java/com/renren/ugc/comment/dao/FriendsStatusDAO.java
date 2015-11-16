package com.renren.ugc.comment.dao;

import com.xiaonei.jade.datasource.Catalogs;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/**
 * 操作好友的状态db，主要用来更新评论数
 * @author lei.xu1	
 * @since 2013-09-12
 *
 */

@DAO(catalog = Catalogs.FRIEND_DOING)
public interface FriendsStatusDAO {

	@SQL("UPDATE friends_doing SET comment_count = :incCount+comment_count WHERE id = :id and userid=:userid")
	public int incCommentCount( // NL
			@SQLParam("id") long id, // NL
			@SQLParam("userid") int userId, //NL
			@SQLParam("incCount") int incCount);

}
