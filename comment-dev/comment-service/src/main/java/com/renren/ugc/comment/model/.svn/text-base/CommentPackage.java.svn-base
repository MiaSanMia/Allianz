package com.renren.ugc.comment.model;


import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForCommentStrategy;

/**
 * 为了批量接口创建
 * 原create接口一个comment对应一个strategy，
 * 在批量接口中将strategy和comment封装成一个对象，方便对应
 * 以及在上下文环境中进行传递
 * @author Liu-Yao
 *
 */
public class CommentPackage{
	
	com.renren.ugc.comment.model.Comment comment;
	
	int actorId;
	
	ForCommentStrategy forCommentStrategy;

	public com.renren.ugc.comment.model.Comment getComment() {
		return comment;
	}

	public void setComment(com.renren.ugc.comment.model.Comment comment) {
		this.comment = comment;
	}

	public int getActorId() {
		return actorId;
	}

	public void setActorId(int actorId) {
		this.actorId = actorId;
	}

	public ForCommentStrategy getForCommentStrategy() {
		return forCommentStrategy;
	}

	public void setForCommentStrategy(ForCommentStrategy forCommentStrategy) {
		this.forCommentStrategy = forCommentStrategy;
	}

}
