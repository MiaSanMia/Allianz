package com.renren.ugc.comment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Metadata;

/**
 * @author wangxx
 * 
 * 解析metaData字段，然后设置到comment的相应字段
 *
 */
public class MetadataResolveUtil {
	
	private static Logger logger = Logger
			.getLogger(MetadataResolveUtil.class);
	
	public static Map<String, CommentMetadataSetter> paramKeys;

	static {
		paramKeys = new HashMap<String,CommentMetadataSetter>();
		paramKeys.put(CommentCenterConsts.FLOOR_KEY, new FloorSetter());
		paramKeys.put(CommentCenterConsts.AUTHOR_NAME, new AuthorNameSetter());
		paramKeys.put(CommentCenterConsts.AUTHOR_URL, new AuthorUrlSetter());
		paramKeys.put(CommentCenterConsts.COMMENT_PIC_URL_KEY, new CommentPicUrlSetter());
		paramKeys.put(CommentCenterConsts.COMMENT_PIC_ID_KEY, new CommentPicIdSetter());
	}
	
	public static void resolveCommentFields(List<Comment> comments){
		
		if(CollectionUtils.isEmpty(comments)){
			return;
		}
		
		for(Comment comment:comments){
			setSingleCommentFields(comment);
		}
		
	}

	private static void setSingleCommentFields(Comment comment) {

		if (comment == null || comment.getMetadata() == null) {
			return;
		}

		Metadata metadata = comment.getMetadata();

		for (String key : paramKeys.keySet()) {
			CommentMetadataSetter setter = paramKeys.get(key);
			if (setter != null) {
				String rawValue = metadata.get(key);
				setter.setCommentFields(rawValue, comment);
				
			}
		}
	}

	static abstract class CommentMetadataSetter {

		public boolean getBooleanValue(String rawValue) {
			return Boolean.valueOf(rawValue);
		}

		public int getIntValue(String rawValue) {
			return Integer.valueOf(rawValue);
		}

		public String getStringValue(String rawValue) {
			return rawValue;
		}

		public long getLongValue(String rawValue) {
			return Long.valueOf(rawValue);
		}

		public abstract void setCommentFields(String rawValue, Comment comment);
	}

	static class FloorSetter extends CommentMetadataSetter {

		@Override
		public void setCommentFields(String rawValue, Comment comment) {
			if (rawValue != null) {
				try {
					comment.setFloor(getIntValue(rawValue));
				} catch (Exception e) {
					logger.error("error happened FloorSetter ,rawValue = "
							+ rawValue, e);
				}
			}

		}
	}
	
	static class AuthorNameSetter extends CommentMetadataSetter {

		@Override
		public void setCommentFields(String rawValue, Comment comment) {
			if (rawValue != null) {
				try {
					comment.setAuthorName(getStringValue(rawValue));
				} catch (Exception e) {
					logger.error("error happened AuthorNameSetter ,rawValue = "
							+ rawValue, e);
				}
			}

		}
	}
	
	static class AuthorUrlSetter extends CommentMetadataSetter {

		@Override
		public void setCommentFields(String rawValue, Comment comment) {
			if (rawValue != null) {
				try {
					comment.setAuthorHead(getStringValue(rawValue));
				} catch (Exception e) {
					logger.error("error happened AuthorUrlSetter ,rawValue = "
							+ rawValue, e);
				}
			}

		}
	}
	
	static class CommentPicUrlSetter extends CommentMetadataSetter {

		@Override
		public void setCommentFields(String rawValue, Comment comment) {
			if (rawValue != null) {
				try {
					comment.setCommentPhotoUrl(getStringValue(rawValue));
				} catch (Exception e) {
					logger.error("error happened AuthorUrlSetter ,rawValue = "
							+ rawValue, e);
				}
			}

		}
	}
	
	static class CommentPicIdSetter extends CommentMetadataSetter {

		@Override
		public void setCommentFields(String rawValue, Comment comment) {
			if (rawValue != null) {
				try {
					comment.setCommentPhotoId(getLongValue(rawValue));
				} catch (Exception e) {
					logger.error("error happened AuthorUrlSetter ,rawValue = "
							+ rawValue, e);
				}
			}

		}
	}

}
