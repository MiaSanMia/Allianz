/*
 * Copyright 2013 Renren.com All right reserved. This software is the
 * confidential and proprietary information of Renren.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Renren.com.
 */
package com.renren.ugc.comment.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.FriendShareDAO;
import com.renren.ugc.comment.dao.ShareCommentDAO;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentStorageService;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * Descriptions of the class ShareCommentMySQLService.java's implementation：用于对原分享数据库的操作
 * @author xiaoqiang 2013-10-9 上午10:13:00
 */
@Service("comment.service.mysql.share")
public class ShareCommentMySQLService implements CommentStorageService, InitializingBean{
    
    @Autowired
    private ShareCommentDAO shareCommentDAO;
    
    @Autowired
    private FriendShareDAO friendShareDAO;
    
    private static ShareCommentMySQLService instance;

    private Logger logger = Logger.getLogger(ShareCommentMySQLService.class);

    public static ShareCommentMySQLService getInstance() {
        return instance;
    }
    
    /**
     * @param entryOwnerId
     * @param entryId
     * @param incCount
     * 
     *  修改分享评论数量
     */
    public void updateShareCommentCount(int entryOwnerId, 
            long entryId, int incCount) {
        shareCommentDAO.updateShareCommentCount(entryId, entryOwnerId, incCount);
        friendShareDAO.updateFriendShareCommentCount(entryId, incCount);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ShareCommentMySQLService.instance = this;
        
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#create(com.renren.ugc.comment.xoa2.CommentType, com.renren.ugc.comment.model.Comment, java.lang.String)
     */
    @Override
    public void create(CommentType type, Comment comment, String metadata) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#remove(int, long, int, long, com.renren.ugc.comment.xoa2.CommentType)
     */
    @Override
    public Comment remove(int actorId, long entryId, int entryOwnerId,
        long commentId, CommentType commentType,CommentStrategy strategy) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#recover(long, int, long)
     */
    @Override
    public boolean recover(long entryId, int entryOwnerId, long commentId) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#get(long, int, long)
     */
    @Override
    public Comment get(long entryId, int entryOwnerId, long commentId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getMulti(long, int, java.util.List)
     */
    @Override
    public Map<Long, Comment> getMulti(long entryId, int entryOwnerId,
        List<Long> commentIds) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getListByEntry(com.renren.ugc.comment.xoa2.CommentType, long, int, long, int, com.renren.ugc.comment.strategy.QueryOrder)
     */
    @Override
    public List<Comment> getListByEntry(CommentType type, long entryId,
        int entryOwnerId, long borderCommentId, int limit, QueryOrder order) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getListByEntry(com.renren.ugc.comment.xoa2.CommentType, long, int, int, int, com.renren.ugc.comment.strategy.QueryOrder)
     */
    @Override
    public List<Comment> getListByEntry(CommentType type, long entryId,
        int entryOwnerId, int offset, int limit, QueryOrder order) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getOldestCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, long, int)
     */
    @Override
    public Comment getOldestCommentOfEntry(CommentType type, long entryId,
        int entryOwnerId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getLatestCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, long, int)
     */
    @Override
    public Comment getLatestCommentOfEntry(CommentType type, long entryId,
        int entryOwnerId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getCommentCountByEntry(com.renren.ugc.comment.xoa2.CommentType, long, int)
     */
    @Override
    public long getCommentCountByEntry(CommentType type, long entryId,
        int entryOwnerId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#updateContent(com.renren.ugc.comment.xoa2.CommentType, long, int, long, java.lang.String)
     */
    @Override
    public boolean updateContent(CommentType type, long entryId,
        int entryOwnerId, long commentId, String newContent) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#increaseVoiceCommentPlayCount(com.renren.ugc.comment.xoa2.CommentType, long, int, long, int)
     */
    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId,
        int entryOwnerId, long commentId, int increment) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getCommentCountByEntryBatch(com.renren.ugc.comment.xoa2.CommentType, java.util.List, int)
     */
    @Override
    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type,
        List<Long> entryId, int entryOwnerId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#removeAll(int, long, int, com.renren.ugc.comment.xoa2.CommentType)
     */
    @Override
    public boolean removeAll(int actorId, long entryId, int entryOwnerId,
        CommentType commentType, CommentStrategy strategy) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getAuthorListByEntry(java.lang.String, int, boolean, com.renren.ugc.comment.xoa2.CommentType)
     */
    @Override
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId,
        boolean includeGlobalComments, CommentType commentType) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getFriendsCommentListByEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, boolean, int, int, com.renren.ugc.comment.strategy.QueryOrder, java.util.List)
     */
    @Override
    public List<Comment> getFriendsCommentListByEntry(CommentType type,
        String entryId, int entryOwnerId, boolean includeGlobalComments,
        int offset, int limit, QueryOrder order, List<Integer> userIds) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getNOldestCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, long, int, int)
     */
    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, int limit) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentStorageService#getNLatestCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, long, int, int)
     */
    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type,
        long entryId, int entryOwnerId, int limit) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public long getCountByAuthorId(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, long borderCommentId, int limit,
			QueryOrder order, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, int offset, int limit,
			QueryOrder order, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getOldestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getLatestCommentByAuthorIdAndToUserId(CommentType type,
			long entryId, int entryOwnerId, List<Integer> authorIds,
			List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCommentCountByEntry(CommentType type, long entryId,
			int entryOwnerId, List<Integer> authorIds, List<Integer> toUserIds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdWithOffset(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			int offset, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> getCommentListByAuthorIdWithBorderId(CommentType type,
			long entryId, int entryOwnerId, int authorId, QueryOrder order,
			long borderId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getEntryId(CommentType type, int entryOwnerId, long commentId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
