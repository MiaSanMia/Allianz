package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.Map;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.at.AtNotifyInfoBean;
import com.xiaonei.xce.notify.NotifyBody;

/**
 * @author wangxx
 *
 * "看世界"业务url工具类
 */
public class LookingWorldUrlUtil {
	
	private static Map<CommentType,Integer> lookingWorldNotifyMaps = new HashMap<CommentType,Integer>();
	
	private static Map<CommentType,Integer> lookingWorldAtMaps = new HashMap<CommentType,Integer>(); 
	
	static {
		
		lookingWorldNotifyMaps.put(CommentType.Blog, 1054);
		lookingWorldNotifyMaps.put(CommentType.Album, 1057);
		lookingWorldNotifyMaps.put(CommentType.Photo, 1059);
		
		lookingWorldAtMaps.put(CommentType.Blog, 1055);
		lookingWorldAtMaps.put(CommentType.Album, 1056);
		lookingWorldAtMaps.put(CommentType.Photo, 1058);
		
	}
	
	private static String getUrl(CommentType type,int entryOwnerId,long entryId,long parentEntryId){
		
		String url = "";
		switch(type){
		case Blog:
			url = String.format("http://public.renren.com/blog/%d/%d", entryOwnerId,entryId);
			break;
		case Photo:
			url = String.format("http://public.renren.com/photo/%d/%d/%d", entryOwnerId,parentEntryId,entryId);
			break;
		case Album:
			url = String.format("http://public.renren.com/album/%d/%d", entryOwnerId,entryId);
			break;
		default:
				
		}
		return url;
	}
	
	private static int getNotifySchemaId(CommentType type){
		return lookingWorldNotifyMaps.containsKey(type) ? lookingWorldNotifyMaps.get(type) : 0;
	}
	
	private static int getAtSchemaId(CommentType type){
		return lookingWorldAtMaps.containsKey(type) ? lookingWorldAtMaps.get(type) : 0;
	}
	
	private static void buildLookingWorldNotifyBody(int schemaId,String titleUrl, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy) {
		//1.修改type schema
		notifyBody.setSchemaId(schemaId);
		notifyBody.setType(schemaId);
		
		//2.设置消息
        notifyBody.setValue("from_name", comment.getAuthorName());
        notifyBody.setValue("title",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
        notifyBody.setValue("title_url",titleUrl);
        notifyBody.setValue("reply_content",comment.getContent());
        notifyBody.setValue("imgUrl",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));
    }
	
	/**
	 * @param type
	 * @param actorId
	 * @param entryOwnerId
	 * @param entryId
	 * @param comment
	 * @param notifyBody
	 * @param strategy
	 * 
	 * collect together
	 */
	public static void buildLookingWorldNotifyBodyAll(CommentType type, int entryOwnerId,
            long entryId, Comment comment, NotifyBody notifyBody,
            CommentStrategy strategy){
		
		//1.get titleUrl
    	String titleUrl = getUrl(type, entryOwnerId, entryId, EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_PARENT_ID));
    	
    	//2.get schemaId
    	int schemaId = getNotifySchemaId(type);
    	
    	//3.build body
    	buildLookingWorldNotifyBody(schemaId, titleUrl, comment, notifyBody, strategy);
	}
	
	 private static Map<String, String> buildLookingWorldBizParams(int schemaId,String titleUrl, 
	            Comment comment, CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean) {
	        if (comment == null || strategy.getEntry() == null) {
	            return null;
	        }
	        
	        //1.修改type
	        atNotifyInfoBean.setSchemaId(schemaId);
	        atNotifyInfoBean.setType(schemaId);
	        
	        //2.新的bizmap
	        Map<String, String> bizParams = new HashMap<String, String>();

	        bizParams.put("from_name", comment.getAuthorName());
	        bizParams.put("title",  EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_TITLE));
	        bizParams.put("title_url",titleUrl);
	        bizParams.put("reply_content", comment.getContent());
	        bizParams.put("imgUrl",EntryConfigUtil.getString(strategy, EntryConfig.ENTRY_HEADURL));

	        return bizParams;
	    }
	 
	 /**
	 * @param type
	 * @param actorId
	 * @param entryOwnerId
	 * @param entryId
	 * @param comment
	 * @param notifyBody
	 * @param strategy
	 * @param atNotifyInfoBean
	 * 
	 *  collect together
	 */
	public static Map<String, String> buildLookingWorldBizBodyAll(CommentType type, int entryOwnerId,
	            long entryId, Comment comment, 
	            CommentStrategy strategy,AtNotifyInfoBean atNotifyInfoBean){
			
			//1.get titleUrl
	    	String titleUrl = getUrl(type, entryOwnerId, entryId, EntryConfigUtil.getLong(strategy, EntryConfig.ENTRY_PARENT_ID));
	    	
	    	//2.get schemaId
	    	int schemaId = getAtSchemaId(type);
	    	
	    	//3.build body
	    	return buildLookingWorldBizParams(schemaId,titleUrl,comment,strategy,atNotifyInfoBean);
		}

}
