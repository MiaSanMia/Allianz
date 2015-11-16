package com.renren.ugc.comment.dao;

import com.xiaonei.jade.datasource.Catalogs;

import net.paoding.rose.jade.annotation.CacheDelete;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/**
 * 状态相关计数的db，主要用来更新某人的状态被评论的次数
 * @author lei.xu1	
 * @since 2013-09-12
 *
 */

@DAO(catalog = Catalogs.FRIEND_DOING)
public interface StatusCountDAO {

	@SQL("UPDATE doing_count SET reply_doing_count = :incCount+reply_doing_count WHERE id = :statusOwnerId")
    public int incCommentCount( // NL
    		@SQLParam("statusOwnerId") int statusOwnerId, //NL
    		@SQLParam("incCount") int incCount);
}
