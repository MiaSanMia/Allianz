package com.renren.ugc.comment.interceptor.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLikeInfo;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.util.CommentLikeInfoUtil;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         获取"喜欢"信息
 * 
 */
public class GetLikeInfoInterceptor extends CommentLogicAdapter {

	@Override
	public Comment create(CommentType commentType, int actorId, long entryId,
			int entryOwnerId, Comment comment, CommentStrategy strategy)
			throws UGCCommentException {

		// get the just created comment
		Comment latest = (Comment) strategy.getReturnedValue();

		CommentLikeInfo likeInfo = new CommentLikeInfo();
		likeInfo.setLikeCount(0);
		likeInfo.setLiked(false);

		latest.setLikeInfo(likeInfo);

		return null;
	}

	@Override
	public List<CommentPackage> createByList(CommentType type, long entryId,
			int entryOwnerId, ForInvokeStrategy forInvokeStrategy)
			throws UGCCommentException {

		List<CommentPackage> latest = (List<CommentPackage>) forInvokeStrategy
				.getReturnedValue();
		for (CommentPackage comment : latest) {
			CommentLikeInfo likeInfo = new CommentLikeInfo();
			likeInfo.setLikeCount(0);
			likeInfo.setLiked(false);

			comment.getComment().setLikeInfo(likeInfo);
		}

		return null;
	}

	@Override
	public CommentListResult getList(CommentType commentType, int actorId,
			long entryId, int entryOwner, CommentStrategy strategy)
			throws UGCCommentException {

		// 1.get return value
		@SuppressWarnings("unchecked")
		CommentListResult commentListResult = (CommentListResult) strategy
				.getReturnedValue();
		List<Comment> comments = commentListResult.getCommentLists();

		if (actorId > 0) {
			// 2.if user login, get like info
			CommentLikeInfoUtil.getInstance().getCommentLikeInfo(commentType,
					comments, actorId);
		}

		return commentListResult;
	}

	@Override
	public CommentListResult getFriendsCommentList(CommentType type,
			int actorId, long entryId, int entryOwnerId,
			CommentStrategy strategy) throws UGCCommentException {

		// 1.get return value
		@SuppressWarnings("unchecked")
		CommentListResult commentListResult = (CommentListResult) strategy
				.getReturnedValue();
		List<Comment> comments = commentListResult.getCommentLists();

		if (actorId > 0) {
			// 2.if user login, get like info

			Map<Integer, List<Comment>> commentMap = getCommentType(comments);
			if (commentMap != null) {
				Set<Integer> keySet = commentMap.keySet();
				for (Integer key : keySet) {
					CommentLikeInfoUtil.getInstance().getCommentLikeInfo(
							CommentType.findByValue(key), commentMap.get(key),
							actorId);
				}
			}
		}

		return commentListResult;
	}

	/**
	 * @param comments
	 * @return
	 */
	private Map<Integer, List<Comment>> getCommentType(List<Comment> comments) {
		Map<Integer, List<Comment>> commentMap = null;
		if (CollectionUtils.isNotEmpty(comments)) {
			commentMap = new HashMap<Integer, List<Comment>>();
			for (int i = 0; i < comments.size(); i++) {
				if (commentMap.containsKey(comments.get(i).getType())) {
					List<Comment> commentList = commentMap.get(comments.get(i)
							.getType());
					commentList.add(comments.get(i));
				} else {
					List<Comment> tempComment = new LinkedList<Comment>();
					tempComment.add(comments.get(i));
					commentMap.put(comments.get(i).getType(), tempComment);
				}
			}
		}
		return commentMap;
	}

	@Override
	public CommentListResult getGlobalCommentList(CommentType commentType,
			int actorId, long entryId, int entryOwnerId, String urlmd5,
			CommentStrategy strategy) throws UGCCommentException {

		// 1.get return value
		@SuppressWarnings("unchecked")
		CommentListResult commentListResult = (CommentListResult) strategy
				.getReturnedValue();
		List<Comment> comments = commentListResult.getCommentLists();

		if (actorId > 0) {
			// 2.if user login, get like info
			Map<Integer, List<Comment>> commentMap = getCommentType(comments);
			if (commentMap != null) {
				Set<Integer> keySet = commentMap.keySet();
				for (Integer key : keySet) {
					CommentLikeInfoUtil.getInstance().getCommentLikeInfo(
							CommentType.findByValue(key), commentMap.get(key),
							actorId);
				}
			}
		}

		return commentListResult;
	}

}
