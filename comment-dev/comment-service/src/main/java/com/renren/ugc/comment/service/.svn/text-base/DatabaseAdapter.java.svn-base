package com.renren.ugc.comment.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.service.impl.CommentUrlMySQLService;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.util.CommentBusIDentifier;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 *         鉴于现在整合进来的dao太多了，所以写了一个数据分发器
 */
@Service
public class DatabaseAdapter implements InitializingBean {

    @Autowired
    @Qualifier("comment.service.mysql")
    private CommentStorageService css;

    @Autowired
    @Qualifier("comment.service.mysql.url")
    private CommentUrlMySQLService urlCss;

    private static DatabaseAdapter instance;

    public static DatabaseAdapter getInstance() {
        return instance;
    }

    public void create(CommentType type, Comment c, Metadata metadata, CommentStrategy strategy) {
            css.create(type, c, metadata.encode());
    }

    public void createGlobal(CommentType type, Comment comment, String metadata,
            CommentStrategy strategy) {
        if (strategy.isNeedGlobal() && comment != null && comment.getWhipserToId() == 0) {

            //全局评论表
            String urlmd5 = strategy.getUrlMd5();
            urlCss.create(type, comment, metadata, urlmd5);
        }
    }

    public List<Comment> getListByEntryWithOffsetDispatch(CommentType type, long entryId,
           final int entryOwnerId, int offset, int limit, QueryOrder order, CommentStrategy strategy,final int actorId) {

        List<Comment> list =  css.getListByEntry(type, entryId, entryOwnerId, offset, limit, order);

        return list;
    }

    public Comment getLatestCommentOfEntry(CommentType type, long entryId, final int entryOwnerId,
            CommentStrategy strategy,final int actorId) {
    	
        Comment latestComment =  css.getLatestCommentOfEntry(type, entryId, entryOwnerId);
        
        return latestComment;
    }

    public Comment getOldestCommentOfEntry(CommentType type, long entryId, final int entryOwnerId,
            CommentStrategy strategy,final int actorId) {
    	
        Comment oldestComment =  css.getOldestCommentOfEntry(type, entryId, entryOwnerId);

        return oldestComment;
    }

    public long getCommentCountByEntry(CommentType type, long entryId, final int entryOwnerId,
            CommentStrategy strategy,final int actorId) {

        long count =  css.getCommentCountByEntry(type, entryId, entryOwnerId);

        return count;
    }

    public Comment remove(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) {

        Comment comment = css.remove(actorId, entryId, entryOwnerId, commentId, type,strategy);

        return comment;
    }

    public Comment removeGlobal(CommentType type, int actorId, String urlmd5, long commentId,
            CommentStrategy strategy) {

        Comment comment = null;
        if (strategy.isNeedGlobal()) {
            comment = urlCss.remove(actorId, urlmd5, commentId, type);
        }
        return comment;
    }

    public boolean recover(long entryId, int entryOwnerId, long commentId, CommentType type) {

        boolean ret = css.recover(entryId, entryOwnerId, commentId);

        return ret;
    }

    public Comment get(long entryId, int entryOwnerId, long commentId, CommentType type) {

        long tempCommentId = getCommentCenterCommentId(type,commentId);
        Comment comment = css.get(entryId, entryOwnerId, tempCommentId);

        return comment;
    }

    public boolean removeAll(int actorId, long entryId, int entryOwnerId, CommentType type,CommentStrategy strategy) {

        boolean ret =  css.removeAll(actorId, entryId, entryOwnerId, type, strategy);

        return ret;
    }

    public Map<Long, Integer> getCommentCountByEntryBatch(CommentType type, List<Long> entryIds,
            int entryOwnerId) {

        Map<Long, Integer> results = css.getCommentCountByEntryBatch(type, entryIds, entryOwnerId);

        return results;
    }

    public List<Comment> getListByEntry(CommentType type, long entryId,final int entryOwnerId,
            long borderCommentId, int limit, QueryOrder order, CommentStrategy strategy,final int actorId) {

        List<Comment> list = css.getListByEntry(type, entryId, entryOwnerId, borderCommentId, limit, order);

        return list;
    }

    public int increaseVoiceCommentPlayCount(CommentType type, long entryId, int entryOwnerId,
            long commentId, int increment) {
        int count = 0;

        count = css.increaseVoiceCommentPlayCount(type, entryId, entryOwnerId, commentId, increment);

        return count;
    }

    public boolean updateContent(CommentType type, long entryId, int entryOwnerId, long commentId,
            String newContent) {

        boolean ret = false;

        ret = css.updateContent(type, entryId, entryOwnerId, commentId, newContent);

        return ret;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DatabaseAdapter.instance = this;
    }

    public Map<Long, Comment> getMulti(CommentType type, long entryId, int entryOwnerId,
            List<Long> commentIds) {

        Map<Long, com.renren.ugc.comment.model.Comment> commentMap = null;

        //评论中心的db
        commentMap = css.getMulti(entryId, entryOwnerId, commentIds);
        return commentMap;
    }
    
    public List<Comment> getNLatestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit) {
    	
        List<Comment> latestComments =  css.getNLatestCommentOfEntry(type, entryId, entryOwnerId,limit);
        return latestComments;
    }

    public List<Comment> getNOldestCommentOfEntry(CommentType type, long entryId, int entryOwnerId,int limit) {
    	
        List<Comment> oldestComments = css.getNOldestCommentOfEntry(type, entryId, entryOwnerId,limit);
        return oldestComments;
    }
    
    /**
     * 根据作者和偏移量来获取评论列表
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> getCommentListWithFilterByOffset(CommentType type, long entryId, int entryOwnerId, int authorId, QueryOrder order, int offset, int limit){
    	List<Comment> comments = null;
    	// 2014年3月7日 为校园主页开发只看楼主功能，不需要数据源的分发
    	comments = css.getCommentListByAuthorIdWithOffset(type, entryId, entryOwnerId, authorId, order, offset, limit);
    	return comments;
    }
    
    public long getCountWithFilter(CommentType type, int actorId, long entryId,
			int entryOwnerId, int authorId){
    	return css.getCountByAuthorId(type, actorId, entryId, entryOwnerId, authorId);
    }
    
    /**
     * @param type
     * @param commentId
     * @return
     * 
     *  因为取评论的时候commentId可能是各个ugc业务的旧数据库的Id,因此这里坐下转化，变成评论中心的Id
     */
    private long getCommentCenterCommentId(CommentType type,long commentId){
        
        long minId = CommentBusIDentifier.getInstance().genCommentId(type,1);
        
        return commentId > minId ? commentId : CommentBusIDentifier.getInstance().genCommentId(type,commentId);
        
    }
    
    /**
     * 根据作者和borderId来获取评论列表
     * 
     * @param type
     * @param entryId
     * @param entryOwnerId
     * @param authorId
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> getCommentListWithFilterByBorderId(CommentType type, long entryId, int entryOwnerId, int authorId, QueryOrder order, long borderId, int limit){
    	List<Comment> comments = null;
    	// 2014年3月7日 为校园主页开发只看楼主功能，不需要数据源的分发
    	comments = css.getCommentListByAuthorIdWithBorderId(type, entryId, entryOwnerId, authorId, order, borderId, limit);
    	return comments;
    }
    
    public List<Integer> getAuthorListByEntry(long entryId, int entryOwnerId, CommentType commentType){
    	
    	List<Integer> authors =  css.getAuthorListByEntry(entryId, entryOwnerId, false, commentType);
    	if(CollectionUtils.isEmpty(authors)){
    		return Collections.EMPTY_LIST;
    	}
    	return authors;
    }

}
