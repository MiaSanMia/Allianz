package com.renren.ugc.comment.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.FriendsStatusDAO;
import com.renren.ugc.comment.dao.StatusCommentDAO;
import com.renren.ugc.comment.dao.StatusCountDAO;
import com.renren.ugc.comment.dao.StatusDAO;
import com.xiaonei.commons.gid.util.G;
import com.xiaonei.commons.gid.util.Type;


/**
 * 状态的一些特殊需求
 * @author lei.xu1
 * @since 2013-09-12
 *
 */
@Service("comment.service.mysql.status.other")
public class StatusCommentSpecialNeedsMySQLService implements InitializingBean {

	@Autowired
    private StatusDAO statusDAO;
    
    @Autowired
    private FriendsStatusDAO friendsStatusDAO;
    
    @Autowired
    private StatusCountDAO statusCountDAO;
    
    @Autowired
    private StatusCommentDAO statusCommentDAO;

    private static StatusCommentSpecialNeedsMySQLService instance;

    public static StatusCommentSpecialNeedsMySQLService getInstance() {
        return instance;
    }
    
    public void updateCommentCount(int entryOwnerId, 
    		long entryId, int incCount) {
    	statusDAO.incCommentCount(entryId, entryOwnerId, incCount);
    	if(!G.isTypeOf(entryOwnerId, Type.PAGE)) {
    		friendsStatusDAO.incCommentCount(entryId, entryOwnerId, incCount);
    	}
		statusCountDAO.incCommentCount(entryOwnerId, incCount);
    }
    
    public long getStatusId(long commentId, int statusOwnerId) {
    	if(commentId == 0 || statusOwnerId == 0) {
    		return 0;
    	}
    	Long result = statusCommentDAO.getEntryId(commentId, statusOwnerId);
    	if(result != null) {
    		return result.longValue();
    	}
    	return 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    	StatusCommentSpecialNeedsMySQLService.instance = this;
    }

}
