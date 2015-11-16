package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.cache.KVKeyGen;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.tair.TairCacheManagerImpl;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 *
 *  设置楼层
 * 
 */
public class SetFloorInterceptor extends CommentLogicAdapter{
	
	 private Logger logger = Logger.getLogger(SetFloorInterceptor.class);
	
	 @Override
	    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
	            Comment comment, CommentStrategy strategy) {
		 
			int defauleValue = 0;
			int addValue = 1;
			
			//2.build key
			String key = KVKeyGen.buildFloorKey(type, entryId);
			
			//3.incr
			// expiretime == 0 表示这个key 永不失效
			int count  = TairCacheManagerImpl.getInstance().incr(TairCacheManagerImpl.ENTRY_MAX_FLOOR, key, addValue, defauleValue, 0);
			
			if(count == TairCacheManagerImpl.ERR_INT_CODE){
				count = 0;
			}
			
			//4.set into map
			strategy.addParam(CommentCenterConsts.FLOOR_KEY, count+"");
			
			return null;
	 }
	 

}
