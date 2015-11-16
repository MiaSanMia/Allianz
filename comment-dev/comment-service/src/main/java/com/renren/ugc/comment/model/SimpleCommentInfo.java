package com.renren.ugc.comment.model;

/**
 * 
 * 封装评论里面基本的信息，只对外提供必要的信息，同时可以用来扩展接口
 * 
 * @author tuanwang.liu 2014-03-06
 *
 */
public class SimpleCommentInfo {

	/**
	 * comment的Id
	 */
	private long id;
	/**
	 * 评论者id
	 */
	private int authorId;
	/**
	 * 评论者名称
	 */
	private String authorName;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论在一个entry下面的楼数
	 */
	private int floor;
	
	@Override
	public String toString() {
		return "SimpleCommentInfo [id=" + id + ", authorId=" + authorId
				+ ", authorName=" + authorName + ", content=" + content
				+ ", floor=" + floor + "]";
	}

	public SimpleCommentInfo(Comment comment){
		setId(comment.getId());
		setAuthorId(comment.getAuthorId());
		setAuthorName(comment.getAuthorName());
		setContent(comment.getContent());
		setFloor(comment.getFloor());
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	  
}
