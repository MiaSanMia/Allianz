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

/**
 * Descriptions of the class FriendShareDAO.java's implementation：好友分享DAO
 * @author xiaoqiang 2013-10-9 上午10:35:40
 */
@DAO(catalog = "share_info_new")
public interface FriendShareDAO {
    /**
     * 更新 friend_share 评论计数
     * 
     * @param id
     * @param userId
     * @param count
     */
    @SQL("update friend_share set comment_count = comment_count + :count where id = :id")
    public void updateFriendShareCommentCount(@SQLParam("id") long id, @SQLParam("count") int count);

}
