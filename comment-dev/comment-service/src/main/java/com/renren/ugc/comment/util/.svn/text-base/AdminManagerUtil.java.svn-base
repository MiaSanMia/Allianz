package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.opt.adminPermission.AdminManager;

/**
 * Check whether a use is an administrator, who has rights to delete and
 * recover a comment. 
 * 
 * @author wangxx
 * 
 */
public class AdminManagerUtil {

    private static Logger logger = Logger.getLogger(AdminManagerUtil.class);

    private Map<CommentType, AdminRight> adminRightsMap = null;

    private static AdminManagerUtil instance = new AdminManagerUtil();

    public static AdminManagerUtil getInstance() {
        return instance;
    }

    private AdminManagerUtil() {

        adminRightsMap = new HashMap<CommentType, AdminRight>();
        adminRightsMap.put(CommentType.Blog, new AdminRight(202, 2));
        adminRightsMap.put(CommentType.Album, new AdminRight(202, 16));
        adminRightsMap.put(CommentType.Photo, new AdminRight(189, 32));
        adminRightsMap.put(CommentType.Share, new AdminRight(202, 4));
        adminRightsMap.put(CommentType.Status, new AdminRight(189, 8));
    }

    public boolean hasRight(CommentType type, int userId, CommentStrategy strategy,int entryOwnerId,int authorId) {

        boolean hasRight = false;
    	
    	//2.判断是否是管理员
        AdminRight adminRight = adminRightsMap.get(type);
        if (type == null) {
            logger.error("AdminManagerUtil found no adminRight|type:" + type.getValue());
            return false;
        }
        
        if(adminRight == null){
            //type里找不到的我们就判断是否是"超级管理员"
            logger.error("AdminManagerUtil adminRight is null|type:" + type.getValue());
            hasRight =   AdminManager.getInstance().isSuperAdmin(userId);
        } else {
        	//调用安全接口是否是管理员
        	hasRight = AdminManager.getInstance().hasRight(userId, adminRight.getFunctionId(),
        	                adminRight.getRight());
        }

        //调用各个业务的接口来判断
        if(!hasRight){
        	hasRight = this.hasAdmainRightDispatch(type, userId, strategy,entryOwnerId,authorId);
        }
        return hasRight;
    }
    
    /**
     * @param type
     * @param userId
     * @param strategy
     * @return
     * 
     *  各个业务的分发
     */
    private boolean hasAdmainRightDispatch(CommentType type, int userId, CommentStrategy strategy,int entryOwnerId,int authorId){
    	
    	boolean ret = false;
    	switch(type){
	    	 case CampusPost:
	         case CampusAlbumPost:
	         case CampusExcellent:
	         case CampusTop:
	        	 	ret = AlbumPostUtil.getInstance().canDeleteComment(type,userId, strategy,entryOwnerId,authorId);
	        	 break;
	         default:
	        		 
    	}
    	return ret;
    }

    class AdminRight {

        /**
         * 函数id
         */
        private int functionId;

        /**
         * 权限
         */
        private int right;

        public AdminRight(int functionId, int right) {
            this.functionId = functionId;
            this.right = right;
        }

        public int getFunctionId() {
            return functionId;
        }

        public int getRight() {
            return right;
        }

    }

}
