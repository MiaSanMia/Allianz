package com.renren.ugc.comment.cache;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 *
 * kv的Key生成
 */
public class KVKeyGen {
	
	protected static final String SEPARATOR = "_";
	
	private static final String FLOOR_PREFIX = "floor";
	
	 /**
	 * @param type
	 * @param entryId
	 * @return
	 *  生成"楼层的key"
	 */
	public static  String buildFloorKey(CommentType type,long entryId){
		 
		 StringBuilder sb = new StringBuilder();
		 
		 sb.append(FLOOR_PREFIX);
		 sb.append(SEPARATOR);
		 sb.append(type.toString().toLowerCase());
		 sb.append(SEPARATOR);
		 sb.append(entryId);
		 
		 return sb.toString();
	 }

}
