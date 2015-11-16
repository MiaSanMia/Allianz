package com.renren.ugc.comment.model;

import java.util.Collections;
import java.util.List;

/**
 * @author wangxx
 * 
 *         封装"取评论列表"的返回结果 现在用于获取"普通/全局/好友"评论类表的接口中
 * 
 */
public class CommentListResult {

	/**
	 * 评论列表
	 */
	private final List<Comment> commentLists;

	/**
	 * 这条评论之后是否有更多评论
	 */
	private final boolean hasMore;

	/**
	 * 这条评论之前是否有更多评论
	 */
	private boolean hasPre;

	/**
	 * 评论列表为空
	 */
	public static final CommentListResult EMPTY_LIST_RESULT = new CommentListResult(
			Collections.EMPTY_LIST, false, false);

	public List<Comment> getCommentLists() {
		return commentLists;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public boolean isHasPre() {
		return hasPre;
	}

	public CommentListResult(final List<Comment> commentLists,
			final boolean hasMore) {
		this.hasMore = hasMore;
		this.commentLists = commentLists;
		this.hasPre = false;
	}

	public CommentListResult(final List<Comment> commentLists,
			final boolean hasMore, final boolean hasPre) {
		this.hasMore = hasMore;
		this.commentLists = commentLists;
		this.hasPre = hasPre;
	}

}
