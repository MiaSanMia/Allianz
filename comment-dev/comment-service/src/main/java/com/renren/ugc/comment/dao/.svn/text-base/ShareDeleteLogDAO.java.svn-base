/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import com.renren.app.share.model.ShareDel;

/**
 * Descriptions of the class ShareDeleteLogDAO.java's implementation：TODO
 * described the implementation of class
 * 
 * @author xiaoqiang 2013-12-3 下午3:48:52
 */
@DAO(catalog = "share_new")
public interface ShareDeleteLogDAO {

    @SQL("select id, user_id, type, url, url_md5, thumb_url, title, "
         + "summary, resource_id, resource_user_id, source_share_id, source_user_id, "
         + "from_share_id, from_user_id, meta, creation_date, comment_count, comment, `status`, "
         + "del_userid, del_time from share_del where id = :id")
    public ShareDel getById(@ShardBy @SQLParam("user_id") int userId,
        @SQLParam("id") long id);

}
