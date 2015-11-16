package com.renren.ugc.comment.service;

import java.util.List;
import java.util.Map;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.FeedCommentInfo;
import com.renren.ugc.comment.model.FeedCommentResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * This class is used for the implementation of interceptors. Each
 * interceptor class should extends this adapter to avoid overriding a lot
 * of empty method
 * 
 * @author jiankuan.xing
 * 
 */
public class CommentLogicAdapter implements CommentLogic {

    @Override
    public Comment get(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return null;
    }

    @Override
    public long getCount(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return 0;
    }

    @Override
    public boolean recover(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return false;
    }

    @Override
    public boolean recoverAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return false;
    }

    @Override
    public boolean removeAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
        return false;
    }

    @Override
    public Comment create(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) throws UGCCommentException {
        return null;
    }

    @Override
    public CommentListResult getList(CommentType commentType, int actorId, long entryId,
            int entryOwner, CommentStrategy strategy) throws UGCCommentException {
        return null;
    }

    @Override
    public boolean remove(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
        return false;
    }

    @Override
    public boolean update(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy)
            throws UGCCommentException {
        return false;
    }

    @Override
    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment, CommentStrategy strategy) {
        return 0;
    }

    @Override
    public Map<Long, Integer> getCountBatch(CommentType commentType, int actorId,
            List<Long> entryIds, int entryOwnerId, CommentStrategy strategy)
            throws UGCCommentException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendsCommentList(com.renren.ugc.comment.xoa2.CommentType, int, long, int, boolean, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public CommentListResult getFriendsCommentList(CommentType type, int actorId, long entryId,
            int entryOwnerId, CommentStrategy strategy) throws UGCCommentException {
        return null;
    }

    @Override
    public Map<Long, Comment> getMulti(CommentType type, int actorId, long entryId,
            int entryOwnerId, List<Long> commentIds, CommentStrategy strategy)
            throws UGCCommentException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getCount(com.renren.ugc.comment.xoa2.CommentType, int, java.lang.String, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public long getGlobalCount(CommentType commentType, int actorId, String urlmd5,
            CommentStrategy strategy) throws UGCCommentException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getGlobalCommentList(com.renren.ugc.comment.xoa2.CommentType, int, long, int, java.lang.String, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public CommentListResult getGlobalCommentList(CommentType commentType, int actorId, long entryId,
            int entryOwnerId, String urlmd5, CommentStrategy strategy) throws UGCCommentException {
        return null;
    }

    @Override
    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    @Override
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,
            CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    @Override
    public Map<Long, FeedCommentResult> getCommentsForFeed(CommentType type,List<FeedCommentInfo> infos,
            CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCount(com.renren.ugc.comment.xoa2.CommentType, int, java.lang.String, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public long getFriendCommentCount(CommentType commentType, int entryOwnerId,
        String urlmd5, CommentStrategy strategy) throws UGCCommentException {
    	// to be overrided
        return 0;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getNLatestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public List<Comment> getNLatestFriendCommentOfEntry(CommentType type,
        String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getNOldestFriendCommentOfEntry(com.renren.ugc.comment.xoa2.CommentType, java.lang.String, int, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public List<Comment> getNOldestFriendCommentOfEntry(CommentType type,
        String urlmd5, long entryId, int entryOwnerId, CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    @Override
    public Map<Long, Integer> getCommentsForFeedCount(CommentType type,
            List<FeedCommentInfo> infos, CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCommentsForFeedCount(com.renren.ugc.comment.xoa2.CommentType, java.util.List, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public Map<Long, Integer> getFriendCommentsForFeedCount(CommentType type,
        List<FeedCommentInfo> infos, CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#getFriendCommentsForFeed(com.renren.ugc.comment.xoa2.CommentType, java.util.List, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public Map<Long, FeedCommentResult> getFriendCommentsForFeed(
        CommentType type, List<FeedCommentInfo> infos, CommentStrategy strategy) {
    	// to be overrided
        return null;
    }

	@Override
	public List<Integer> getFriendsList(int actorId, long entryId,
			int entryOwnerId, String urlmd5, CommentType type) {
		// to be overrided
		return null;
	}

    /* (non-Javadoc)
     * @see com.renren.ugc.comment.service.CommentLogic#removeGlobalComment(com.renren.ugc.comment.xoa2.CommentType, int, long, int, long, com.renren.ugc.comment.strategy.CommentStrategy)
     */
    @Override
    public boolean removeGlobalComment(CommentType type, int actorId,
        long entryId, int entryOwnerId, long commentId, CommentStrategy strategy)
        throws UGCCommentException {
    	// to be overrided
        return false;
    }

	@Override
	public List<Integer> filterAuthorByFrequency(CommentType type, int actorId,
			long entryId, int entryOwnerId, int toId, List<Integer> authors,
			CommentStrategy strategy) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public boolean sendFriendNotify(CommentType type, int actorId, long entryId, int entryOwnerId,
                                    long commentId, CommentStrategy strategy) throws UGCCommentException {
        return false;
    }

	@Override
	public CommentListResult getCommentListWithFilter(CommentType type,
			int actorId, long entryId, int entryOwnerId, int authorId,
			CommentStrategy strategy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCountWithFilter(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId, CommentStrategy strategy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Comment getOldestCommentOfEntry(CommentType type, long entryId,
			int entryOwnerId, CommentStrategy strategy, int actorId) {
		return null;
	}

	@Override
	public Comment getLatestCommentOfEntry(CommentType type, long entryId,
			int entryOwnerId, CommentStrategy strategy, int actorId) {
		return null;
	}

	@Override
	public List<CommentPackage> createByList(CommentType type, 
			long entryId, int entryOwnerId, ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
		return null;
	}
}
