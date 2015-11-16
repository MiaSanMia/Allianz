//package com.renren.ugc.comment.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.renren.ugc.comment.exception.UGCCommentException;
//import com.renren.ugc.comment.model.Comment;
//import com.renren.ugc.comment.service.CommentIdentify;
//import com.renren.ugc.comment.service.CommentLogic;
//import com.renren.ugc.comment.service.ICommentCountCallback;
//import com.renren.ugc.comment.service.KeySpace;
//import com.renren.ugc.comment.strategy.PostCommentStrategy;
//import com.renren.ugc.comment.strategy.QueryCommentStrategy;
//import com.xiaonei.platform.component.task.TaskManager;
//import com.xiaonei.platform.core.model.User;
//
//public class AsyncCommentLogic implements CommentLogic{
//	
//	private static final CommentLogic logic = new CommentLogicImpl();
//	
//	/**
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param comment
//	 *            评论对象
//	 * @param strategy
//	 *            发表评论策略，antispam,feed,audit,webpager
//	 * @return
//	 */
//	public Comment create (final CommentIdentify commentIdentify, final User host,final User entryOwner,final Comment comment, final PostCommentStrategy strategy,final ICommentCountCallback callback){
//		//logger.debug("create comment======");
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Comment comm = logic.create(commentIdentify, host,entryOwner,comment, strategy, callback);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return comment;
//	}
//	/**
//	 * 删除一条评论
//	 * 
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param strategy
//	 *            查询评论的策略 feed,queryOrder,queryCount,queryBorderID,filterWhisper
//	 * @param host
//	 *            当前操作的用户
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @param commentId
//	 *            回复Id
//	 * @return
//	 */
//	public boolean remove(final CommentIdentify commentIdentify, final KeySpace keySapce,final  QueryCommentStrategy strategy, final User host,
//			final long sourceID, final int entryOwner, final long commentId,final ICommentCountCallback callback){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					boolean succ = logic.remove(commentIdentify, keySapce, strategy, host, sourceID, entryOwner, commentId, callback);
//					logger.debug("AsyncCommentLogic remove :"+succ);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return false;
//	}
//	
//	/**
//	 * 删除某个源的所有评论
//	 * 
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param host
//	 *            当前操作的用户
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @return
//	 */
//	public boolean removeAll(final CommentIdentify commentIdentify, final KeySpace keySapce, final int host,
//			final long sourceID, final int entryOwner){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					boolean succ = logic.removeAll(commentIdentify, keySapce,  host, sourceID, entryOwner);
//					logger.debug("AsyncCommentLogic removeAll :"+succ);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return false;
//	}
//	/**
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param strategy
//	 *            查询评论的策略 feed,queryOrder,queryCount,queryBorderID,filterWhisper
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @param host 当前用户id，过滤悄悄话用
//	 * @return
//	 */
//	public List<Comment> getList(final CommentIdentify commentIdentify, final KeySpace keySapce,
//			final QueryCommentStrategy strategy, final long sourceID, final User entryOwner,final User host){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					List<Comment> list = logic.getList(commentIdentify, keySapce, strategy, sourceID, entryOwner, host);
//					logger.debug("AsyncCommentLogic getList :"+list.size());
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return new ArrayList<Comment>();
//	}
//	/**
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param strategy
//	 *            查询评论的策略 feed,queryOrder,queryCount,queryBorderID,filterWhisper
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @return
//	 */
//	public long getCount(final CommentIdentify commentIdentify, final KeySpace keySapce,
//			final QueryCommentStrategy strategy, final long sourceID, final int entryOwner,final int authorID){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					long count = logic.getCount(commentIdentify, keySapce, strategy, sourceID, entryOwner,authorID);
//					logger.debug("AsyncCommentLogic getCount :"+count);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return 0;
//	}
//	/**
//	 * 获取一条评论
//	 * 
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @param commentId
//	 *            回复Id
//	 * @return
//	 */
//	public Comment get(final CommentIdentify commentIdentify, final KeySpace keySapce, final long sourceID, final int entryOwner,final  long commentId){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Comment comm = logic.get(commentIdentify, keySapce, sourceID, entryOwner, commentId);
//					//logger.debug("AsyncCommentLogic get :"+comm.getContent());
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return null;
//	}
//	/**
//	 * 恢复一个源的所有评论 undo delete all
//	 * 
//	 * @param commentIdentify
//	 *            业务类型 enum
//	 * @param keySapce
//	 *            存储类型 enum，1，按源存储，2，按用户存储
//	 * @param host
//	 *            当前操作的用户
//	 * @param sourceID
//	 *            源id，比如日志id，相册id，分享id
//	 * @param entryOwner
//	 *            源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
//	 * @return
//	 */
//	public boolean recoverAll(final CommentIdentify commentIdentify, final KeySpace keySapce, final int host, final long sourceID, final int entryOwner){
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					boolean succ = logic.recoverAll(commentIdentify, keySapce, host, sourceID, entryOwner);
//					logger.debug("AsyncCommentLogic recoverAll : "+succ);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return false;
//	}
//	@Override
//	public boolean recover(final CommentIdentify commentIdentify,final KeySpace keySpace, final int host,
//			final long sourceID,final  int entryOwner, final long commentId,final  ICommentCountCallback callback)
//			throws UGCCommentException {
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				try {
//					boolean succ = logic.recover(commentIdentify, keySpace, host, sourceID, entryOwner, commentId, callback);
//					logger.debug("AsyncCommentLogic recoverAll : "+succ);
//				} catch (Exception e) {
//					logger.error("", e);
//				}
//			}
//		};
//		TaskManager.getInstance().addTask(1, task);
//		return false;
//	}
//	@Override
//	public boolean update(CommentIdentify commentIdentify, KeySpace keySpace,
//			int host, long entryId, int entryOwner, long commentId,
//			String newContent,String newExtension) throws UGCCommentException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
