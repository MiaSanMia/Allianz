package com.renren.ugc.comment.interceptor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.EntryConfigUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 *
 * 照片评论同步到虚拟相册
 * 
 * 2014-06-25c
 * 1.照片如果是多张上传的话，需要同步到相对应的"虚拟相册"
 * 2."虚拟相册"的评论不再同步到对应的相册
 * 文档见http://jira.d.xiaonei.com/browse/COMMENT-40
 */
public class SyncPhotoCommentToAlbumInterceptor extends CommentLogicAdapter{
	
	 private Logger logger = Logger.getLogger(this.getClass());

	    @Override
	    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
	            final Comment comment, final CommentStrategy strategy) throws UGCCommentException {
	    	
	    	if( type.getValue() != CommentType.Photo.getValue() ){
	    		return comment;
	    	}
	    	
	    	//如果这张photo是多张上传的话，则需要同步到虚拟相册
	    	long childAlbumId = EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_CHILD_PARENT_ID);
	    	
	    	if(childAlbumId > 0 && EntryConfigUtil.getBoolean(strategy, EntryConfig.ENTRY_IS_MULTI)){
	    		
	    		//设置需要的linkedInfo
	    		comment.setLinked(true);
	    		List<CommentLinkedInfo> linkedInfos = new ArrayList<CommentLinkedInfo>();
	    		CommentLinkedInfo info = new CommentLinkedInfo();
	    		info.setCommentType(CommentType.Album.getValue());
	    		info.setEntryId(childAlbumId);
	    		info.setEntryOwnerId(entryOwnerId);
	    		
	    		linkedInfos.add(info);
	    		strategy.setCommentLinkedInfos(linkedInfos);
	    		
	    		//设置照片id和url
	    		Map<String, String> params = strategy.getParams(); 
	    		if(params == null){
		    		params = new HashMap<String,String>();
		        	strategy.setParams(params);
	    		}
	    		params.put(CommentCenterConsts.COMMENT_PIC_URL_KEY, EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
	    		params.put(CommentCenterConsts.COMMENT_PIC_ID_KEY, String.valueOf(entryId));
	    	}
   	
	    	return comment;
	    }
	    
	    @Override
		public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
				ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
	    	if( type.getValue() != CommentType.Photo.getValue() ){
	    		return forInvokeStrategy.getPackageList();
	    	}
	    	for(CommentPackage onepackage : forInvokeStrategy.getPackageList()){

		    	//如果这张photo是多张上传的话，则需要同步到虚拟相册
		    	long childAlbumId = EntryConfigUtil.getLong(onepackage.getForCommentStrategy()
		    			, EntryConfig.ENTRY_CHILD_PARENT_ID);
		    	
		    	if(childAlbumId > 0 && EntryConfigUtil.getBoolean(onepackage.getForCommentStrategy(), 
		    			EntryConfig.ENTRY_IS_MULTI)){
		    		
		    		//设置需要的linkedInfo
		    		onepackage.getComment().setLinked(true);
		    		List<CommentLinkedInfo> linkedInfos = new ArrayList<CommentLinkedInfo>();
		    		CommentLinkedInfo info = new CommentLinkedInfo();
		    		info.setCommentType(CommentType.Album.getValue());
		    		info.setEntryId(childAlbumId);
		    		info.setEntryOwnerId(entryOwnerId);
		    		
		    		linkedInfos.add(info);
		    		onepackage.getForCommentStrategy().setCommentLinkedInfos(linkedInfos);
		    		//关联评论是针对实体的，所以设置实体的关联评论
		    		if(null == forInvokeStrategy.getCommentLinkedInfos())
		    			forInvokeStrategy.setCommentLinkedInfos(linkedInfos);
		    			    		
		    		//设置照片id和url
		    		Map<String, String> params = onepackage.getForCommentStrategy().getParams(); 
		    		if(params == null){
			    		params = new HashMap<String,String>();
			    		onepackage.getForCommentStrategy().setParams(params);
		    		}
		    		params.put(CommentCenterConsts.COMMENT_PIC_URL_KEY, EntryConfigUtil.getString(
		    				onepackage.getForCommentStrategy(), EntryConfig.ENTRY_HEADURL));
		    		params.put(CommentCenterConsts.COMMENT_PIC_ID_KEY, String.valueOf(entryId));
		    	}
	   	
	    	}
	    	
	    	return forInvokeStrategy.getPackageList();
		}

}
