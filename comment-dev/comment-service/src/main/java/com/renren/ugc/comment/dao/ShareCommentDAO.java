/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.dao;

import java.util.Date;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

/**
 * Descriptions of the class ShareCommentDAO.java's implementation：对share_comment表进行操作
 * @author xiaoqiang 2013-9-30 上午9:01:42
 */
@DAO(catalog="share_new")
public interface ShareCommentDAO {
    public static final String SELECT_DB_FIELD = " id, share_id, creator, creator_name, creator_head, body, time, share_owner, mark_bit, meta, reply_to_user, reply_to_comment  ";
    public static final String INSERT_DB_FIELD = " id, share_id, creator, creator_name, creator_head, body, time, share_owner, mark_bit, meta, reply_to_user, reply_to_comment  ";
    
    /**
     * 保存分享评论 返回 分享评论对象 含id
     * @param sc
     * @param user_id
     * @return
     */
    @ReturnGeneratedKeys
    @SQL("insert into share_comment (" + INSERT_DB_FIELD + ")" +
            "values (:id,:shareId, :creator, :creatorName, :creatorHead, :body, :time, :shareOwner, :markBit, :meta, :replyToUser, :toCommentId)")
    public long saveShareComment(@SQLParam("id") long id, @SQLParam("shareId") long shareId, @SQLParam("creator") int creator, @SQLParam("time") Date time,
            @SQLParam("creatorName") String creatorName, @SQLParam("creatorHead") String creatorHead, @SQLParam("body") String body,
            @SQLParam("shareOwner") int shareOwner, @SQLParam("markBit") int markBit, @SQLParam("meta") String meta, 
            @ShardBy @SQLParam("owner") int userId, @SQLParam("replyToUser") int replyToUser, @SQLParam("toCommentId") long toCommentId);
    
    /**
     * 更新分享的评论数目
     * 
     * @param id
     * @param userId
     * @param count
     */
    @SQL("update share set comment_count = comment_count + :count where id = :id")
    public void updateShareCommentCount(@SQLParam("id") long id,
            @ShardBy @SQLParam("user_id") int userId, @SQLParam("count") int count);

}
