package com.renren.ugc.comment.service;

import java.util.List;
import java.util.Map;

import net.paoding.rose.scanning.context.RoseAppContext;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.FeedCommentInfo;
import com.renren.ugc.comment.model.FeedCommentResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.proxy.CommentLogicInvocationHandler;
import com.renren.ugc.comment.service.impl.CommentLogicImpl;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author lvenle
 * 
 *         在此会判断业务属于相册、ugc，会有Ugc2AlbumCommentMgr统一处理
 */
public class CommentCenter implements ApplicationContextAware {

    private static final Object monitor = new Object();

    private static ApplicationContext currentContext;

    private static Logger logger = Logger.getLogger(CommentCenter.class);

    static {
        synchronized (monitor) {
            if (currentContext == null) {

                if (logger.isInfoEnabled()) {
                    logger.info("Comment center init by starting internal roseAppContext.");
                }
                currentContext = new RoseAppContext();
            }
        }
    }

    private static CommentCenter instance = new CommentCenter();

    private CommentLogic logic = CommentLogicImpl.getInstance();

    private CommentLogicInvocationHandler handler = new CommentLogicInvocationHandler();

    private CommentLogic logicProxy = (CommentLogic) handler.bind(logic);

    private CommentCenter() {

    }

    public static CommentCenter getInstance() {
        return instance;
    }

    /**
     * @param type 业务类型 enum
     * @param comment 评论对象
     * @param strategy 发表评论策略，antispam,feed,audit,webpager
     * @return
     * @throws UGCCommentException
     */
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.create(type, actorId, entryId, entryOwnerId, comment, strategy);
    }

    /**
     * @param type 业务类型 enum
     * @param strategy 查询评论的策略
     *        feed,queryOrder,queryCount,queryBorderID,filterWhisper
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @return
     * @throws UGCCommentException
     */
    public CommentListResult getListByEntry(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.getList(type, actorId, entryId, entryOwnerId, strategy);
    }

    /**
     * @param type 业务类型 enum
     * @param actorId 调用者ID
     * @param entryId 实体ID
     * @param entryOwnerId 实体拥有者ID
     * @param urlmd5 根据类型、entryId、entryOwnnerId生成的urlmd5值
     * @param strategy 查询评论的策略
     *        feed,queryOrder,queryCount,queryBorderID,filterWhisper
     * @return
     * @throws UGCCommentException
     */
    public CommentListResult getGlobalCommentListByEntry(CommentType type, int actorId, long entryId,
            int entryOwnerId, String urlmd5, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.getGlobalCommentList(type, actorId, entryId, entryOwnerId, urlmd5,
                strategy);

    }

    /**
     * @param type 评论类型
     * @param actorId 调用者ID
     * @param entryId 实体ID
     * @param entryOwnerId 实体拥有者ID
     * @param strategy 查询评论的策略
     * @return
     * @throws UGCCommentException
     */
    public CommentListResult getFriendsCommentListByEntry(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.getFriendsCommentList(type, actorId, entryId, entryOwnerId, strategy);
    }

    /**
     * @param type 业务类型 enum
     * @param strategy 查询评论的策略
     *        feed,queryOrder,queryCount,queryBorderID,filterWhisper
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @return
     * @throws UGCCommentException
     */
    public long getCountByEntry(CommentType type, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.getCount(type, actorId, entryId, entryOwnerId, strategy);
    }

    /**
     * @param type 业务类型 enum
     * @param urlmd5 全局评论唯一键
     * @param strategy 查询评论的策略
     *        feed,queryOrder,queryCount,queryBorderID,filterWhisper
     * @return
     * @throws UGCCommentException
     */
    public long getCountByEntry(CommentType type, int actorId, String urlmd5,
            CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.getGlobalCount(type, actorId, urlmd5, strategy);
    }

    /**
     * 删除一条评论
     * 
     * @param type 业务类型 enum
     * @param strategy 查询评论的策略 此处只需设置feedType
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @param commentId 回复Id
     * @return
     */
    public boolean remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.remove(type, actorId, entryId, entryOwnerId, commentId, strategy);
    }
    
    /**
     * 删除一条全局评论
     * 
     * @param type 业务类型 enum
     * @param strategy 查询评论的策略 此处只需设置feedType
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @param commentId 回复Id
     * @return
     */
    public boolean removeGlobalComment(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.removeGlobalComment(type, actorId, entryId, entryOwnerId, commentId, strategy);
    }

    /**
     *
     * @param type
     * @param actorId
     * @param entryId
     * @param entryOwnerId
     * @param commentId
     * @param strategy
     * @return
     * @throws UGCCommentException
     */
    public boolean sendFriendNotify(CommentType type, int actorId, long entryId, int entryOwnerId,
                                    long commentId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.sendFriendNotify(type, actorId, entryId, entryOwnerId, commentId, strategy);
    }

    /**
     * 删除某个源的所有评论
     * 
     * @param type 业务类型 enum
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @return
     */
    public boolean removeAllByEntry(CommentType type, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.removeAll(type, actorId, entryId, entryOwnerId, strategy);
    }

    /**
     * 恢复一条评论 undo delete comment
     * 
     * @param type 业务类型 enum
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @param commentId 回复Id
     * @return
     * @throws UGCCommentException
     */
    public boolean recover(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.recover(type, actorId, entryId, entryOwnerId, commentId, strategy);
    }

    /**
     * 恢复一个源的所有评论 undo delete all
     * 
     * @param type 业务类型 enum
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @return
     * @throws UGCCommentException
     */
    @Deprecated
    public boolean recoverAllByEntry(CommentType type, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.recoverAll(type, actorId, entryId, entryOwnerId, strategy);
    }

    @Deprecated
    public boolean update(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy)
            throws UGCCommentException {
        return logicProxy.update(type, actorId, entryId, entryOwnerId, commentId, newComment,
                strategy);
    }

    /**
     * 获取一条评论
     * 
     * @param type 业务类型 enum
     * @param actorId 当前操作的用户
     * @param entryId 源id，比如日志id，相册id，分享id
     * @param entryOwnerId 源拥有者，比如日志sourceOwner，相册sourceOwner，分享sourceOwner
     * @param commentId 回复Id
     * @return
     * @throws UGCCommentException
     */
    public Comment get(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return logicProxy.get(type, actorId, entryId, entryOwnerId, commentId, strategy);
    }

    public Map<Long, Comment> getMulti(CommentType type, int actorId, long entryId,
            int entryOwnerId, List<Long> commentIds, CommentStrategy strategy)
            throws UGCCommentException {
        return logicProxy.getMulti(type, actorId, entryId, entryOwnerId, commentIds, strategy);
    }

    public int increaseVoiceCommentPlayCount(CommentType type, int actorId, long entryId,
            int entryOwnerId, long commentId, int increment, CommentStrategy strategy) {
        return logicProxy.increaseVoiceCommentPlayCount(type, entryId, entryOwnerId, commentId,
                increment, strategy);
    }

    public Map<Long, Integer> getCountByEntryBatch(CommentType type, int actorId,
            List<Long> entryIds, int entryOwnerId, CommentStrategy strategy) {
        return logicProxy.getCountBatch(type, actorId, entryIds, entryOwnerId, strategy);
    }
    
    public Map<Long,FeedCommentResult> getCommentsForFeed(CommentType type,List<FeedCommentInfo> infos, CommentStrategy strategy) {
        return logicProxy.getCommentsForFeed(type,infos, strategy);
    }
    
    public Map<Long,Integer> getCommentsForFeedCount(CommentType type,List<FeedCommentInfo> infos, CommentStrategy strategy) {
        return logicProxy.getCommentsForFeedCount(type,infos, strategy);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("the application context is injected " + applicationContext.getClass());
        currentContext = applicationContext;
    }

    public static ApplicationContext getAppContext() {
        return currentContext;
    }

    /**
     * @param type
     * @param strategy
     * @return
     */
    public Map<Long, Integer> getFriendCommentsForFeedCount(CommentType type,
        List<com.renren.ugc.comment.model.FeedCommentInfo> infos,
        CommentStrategy strategy) {
        return logicProxy.getFriendCommentsForFeedCount(type, infos, strategy);
    }
    
    public Map<Long,FeedCommentResult> getFriendCommentsForFeed(CommentType type, List<FeedCommentInfo> infos, CommentStrategy strategy) {
        return logicProxy.getFriendCommentsForFeed(type, infos, strategy);
    }
    
	public CommentListResult getCommentListWithFilter(CommentType type,
			int actorId, long entryId, int entryOwnerId, int authorId,
			CommentStrategy strategy) {
		return logicProxy.getCommentListWithFilter(type, actorId, entryId,
				entryOwnerId, authorId, strategy);
	}
	
	public long getCountWithFilter(CommentType type, int actorId, long entryId, int entryOwnerId, int authorId, CommentStrategy strategy){
		return logicProxy.getCountWithFilter(type, actorId, entryId, entryOwnerId, authorId, strategy);
	}
	
	
    public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
            ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
        return logicProxy.createByList(type, entryId, entryOwnerId, forInvokeStrategy);
    }
}
