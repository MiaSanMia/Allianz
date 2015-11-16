//package com.renren.ugc.comment.interceptor.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//import com.renren.ugc.comment.exception.UGCCommentException;
//import com.renren.ugc.comment.model.Comment;
//import com.renren.ugc.comment.model.CommentListResult;
//import com.renren.ugc.comment.model.Metadata;
//import com.renren.ugc.comment.service.CommentLogicAdapter;
//import com.renren.ugc.comment.strategy.CommentStrategy;
//import com.renren.ugc.comment.util.CommentCenterConsts;
//import com.renren.ugc.comment.xoa2.CommentType;
//
///**
// * @author wangxx
// * 
// *         获取评论中的metadata字段，然后放在Comment类中的相关属性
// */
//public class GetMetadataInterceptor extends CommentLogicAdapter {
//
//	private static Logger logger = Logger
//			.getLogger(GetMetadataInterceptor.class);
//
//	@Override
//	public CommentListResult getList(CommentType commentType, int actorId,
//			long entryId, int entryOwner, CommentStrategy strategy)
//			throws UGCCommentException {
//		return batchSetCommentFields(strategy);
//	}
//
//	@Override
//	public CommentListResult getCommentListWithFilter(CommentType type,
//			int actorId, long entryId, int entryOwnerId, int authorId,
//			CommentStrategy strategy) {
//		return batchSetCommentFields(strategy);
//	}
//
//	private CommentListResult batchSetCommentFields(CommentStrategy strategy) {
//		// 1.get return value
//		@SuppressWarnings("unchecked")
//		CommentListResult commentListResult = (CommentListResult) strategy
//				.getReturnedValue();
//		List<Comment> comments = commentListResult.getCommentLists();
//
//		for (Comment comment : comments) {
//
//			// set all fields
//			setCommentFields(comment);
//		}
//
//		return commentListResult;
//	}
//
//	public static Map<String, CommentMetadataSetter> paramKeys;
//
//	static {
//		paramKeys = new HashMap<String,CommentMetadataSetter>();
//		paramKeys.put(CommentCenterConsts.FLOOR_KEY, new FloorSetter());
//		paramKeys.put(CommentCenterConsts.AUTHOR_NAME, new AuthorNameSetter());
//		paramKeys.put(CommentCenterConsts.AUTHOR_URL, new AuthorUrlSetter());
//	}
//
//	public static void setCommentFields(Comment comment) {
//
//		if (comment == null || comment.getMetadata() == null) {
//			return;
//		}
//
//		Metadata metadata = comment.getMetadata();
//
//		for (String key : paramKeys.keySet()) {
//			CommentMetadataSetter setter = paramKeys.get(key);
//			if (setter != null) {
//				String rawValue = metadata.get(key);
//				setter.setCommentFields(rawValue, comment);
//			}
//		}
//	}
//
//	static abstract class CommentMetadataSetter {
//
//		public boolean getBooleanValue(String rawValue) {
//			return Boolean.valueOf(rawValue);
//		}
//
//		public int getIntValue(String rawValue) {
//			return Integer.valueOf(rawValue);
//		}
//
//		public String getStringValue(String rawValue) {
//			return rawValue;
//		}
//
//		public long getLongValue(String rawValue) {
//			return Long.valueOf(rawValue);
//		}
//
//		public abstract void setCommentFields(String rawValue, Comment comment);
//	}
//
//	static class FloorSetter extends CommentMetadataSetter {
//
//		@Override
//		public void setCommentFields(String rawValue, Comment comment) {
//			if (rawValue != null) {
//				try {
//					comment.setFloor(getIntValue(rawValue));
//				} catch (Exception e) {
//					logger.error("error happened FloorSetter ,rawValue = "
//							+ rawValue, e);
//				}
//			}
//
//		}
//	}
//	
//	static class AuthorNameSetter extends CommentMetadataSetter {
//
//		@Override
//		public void setCommentFields(String rawValue, Comment comment) {
//			if (rawValue != null) {
//				try {
//					comment.setAuthorName(getStringValue(rawValue));
//				} catch (Exception e) {
//					logger.error("error happened AuthorNameSetter ,rawValue = "
//							+ rawValue, e);
//				}
//			}
//
//		}
//	}
//	
//	static class AuthorUrlSetter extends CommentMetadataSetter {
//
//		@Override
//		public void setCommentFields(String rawValue, Comment comment) {
//			if (rawValue != null) {
//				try {
//					comment.setAuthorHead(getStringValue(rawValue));
//				} catch (Exception e) {
//					logger.error("error happened AuthorUrlSetter ,rawValue = "
//							+ rawValue, e);
//				}
//			}
//
//		}
//	}
//
//}
