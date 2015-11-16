package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.KVKeyGen;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

/**
 * @author wangxx
 *
 *  设置作者信息
 * 
 */
public class SetAuthorInfoInterceptor extends CommentLogicAdapter{
	
	 private Logger logger = Logger.getLogger(SetAuthorInfoInterceptor.class);
	
	 @Override
	    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
	            Comment comment, CommentStrategy strategy) {
		 
			if(strategy.isSaveAuthorInfo()){
			
				//1. get author info
				User host = WUserAdapter.getInstance().get(actorId);
				//2.set into map
				strategy.addParam(CommentCenterConsts.AUTHOR_NAME, host.getName());
				strategy.addParam(CommentCenterConsts.AUTHOR_URL, host.getHeadFullUrl());
			}
			
			return null;
	 }
	 

}
