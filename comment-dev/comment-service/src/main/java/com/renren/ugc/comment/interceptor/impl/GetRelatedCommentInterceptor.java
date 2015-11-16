package com.renren.ugc.comment.interceptor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.SimpleCommentInfo;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author tuanwang.liu
 * 
 *  获取评论的引用信息
 *
 */
public class GetRelatedCommentInterceptor  extends CommentLogicAdapter{
	
	private static Logger logger = Logger
			.getLogger(GetRelatedCommentInterceptor.class);
	
	@Override
	public Comment create(CommentType commentType, int actorId, long entryId,
			int entryOwnerId, Comment comment, CommentStrategy strategy)
			throws UGCCommentException {
		getRelatedCommentInfoById(commentType, actorId, entryId, entryOwnerId,
				comment, strategy);
		return null;
	}
	
	@Override
	public CommentListResult getList(CommentType commentType, int actorId,
			long entryId, int entryOwner, CommentStrategy strategy)
			throws UGCCommentException {
		return getRelatedCommentInfo(commentType, actorId, entryId, entryOwner, strategy);
	}
	
	@Override
	public CommentListResult getCommentListWithFilter(CommentType type,
			int actorId, long entryId, int entryOwnerId, int authorId,
			CommentStrategy strategy) {
		return getRelatedCommentInfo(type, actorId, entryId, entryOwnerId, strategy);
	}
	
	/**
	 * 取单个id的评论
	 * 
	 * @param commentType
	 * @param actorId
	 * @param entryId
	 * @param entryOwnerId
	 * @param comment
	 * @param strategy
	 */
	private void getRelatedCommentInfoById(CommentType commentType,
			int actorId, long entryId, int entryOwnerId, Comment comment,
			CommentStrategy strategy) {
		try {
			Comment createdComment = (Comment) strategy.getReturnedValue();
			int replyToUser = createdComment.getToUserId();
			long toCommentId = createdComment.getToCommentId();
			if (replyToUser <= 0 || toCommentId <= 0) {
				return;
			}
			//解析楼层
			strategy.setNeedMetadataResolve(true);
			Comment relatedComment = strategy.getCommentLogic().get(
					commentType, actorId, entryId, entryOwnerId, toCommentId,
					strategy);
			if (null != relatedComment) {
				createdComment.setSimpleComment(new SimpleCommentInfo(
						relatedComment));
			}
		} catch (Exception e) {
			logger.error("get relatedCommentInfo while creating  comment error|actorId:"
					+ actorId + "|entryOwnerId:" + entryOwnerId);
		}
		return;
	}
	
	private CommentListResult getRelatedCommentInfo(CommentType type,
			int actorId, long entryId, int entryOwnerId,CommentStrategy strategy){
		long start = System.nanoTime();
		CommentListResult result = (CommentListResult) strategy
				.getReturnedValue();
		
		List<Comment> list = result.getCommentLists();
		
		if(CollectionUtils.isEmpty(list)){
			return result;
		}
		// 关联的评论id到id映射,存在一对多，用list
		Map<Long, List<Long>> relatedCommentId2IdMap = new HashMap<Long, List<Long>>(
				list.size());
		// 评论id到这个评论的映射
		Map<Long, com.renren.ugc.comment.model.Comment> id2CommentMap = new HashMap<Long, Comment>(
				list.size());
		// list转map便于后面的查询
		List<Long> relatedIdList = new ArrayList<Long>(list.size());
		for (com.renren.ugc.comment.model.Comment comment : list) {
			long toCommentId = comment.getToCommentId();
			if (toCommentId > 0) {
				List<Long> commentIds = relatedCommentId2IdMap.get(toCommentId);
				if (null == commentIds) {
					List<Long> initList = new ArrayList<Long>(3);
					initList.add(comment.getId());
					relatedCommentId2IdMap.put(toCommentId, initList);
				} else {
					commentIds.add(comment.getId());
					relatedCommentId2IdMap.put(toCommentId, commentIds);
				}
				id2CommentMap.put(comment.getId(), comment);
				relatedIdList.add(comment.getToCommentId());
			}
		}
		if (relatedCommentId2IdMap.isEmpty()) {
			return result;
		}
		CommentStrategy tmpStrategy = new CommentStrategy();
		// 只嵌套一层
		tmpStrategy.setNeedSimpleInfo(false);
		// 暂时不处理at,短链接，ubb了
		tmpStrategy.setReplaceAt(false);
		tmpStrategy.setReplaceShortUrl(false);
		tmpStrategy.setReplaceUbbLarge(false);
		tmpStrategy.setReplaceUbb(false);
		tmpStrategy.setReturnFullHeadUrl(strategy.isReturnFullHeadUrl());
		tmpStrategy.setNeedMetadataResolve(true);
		Map<Long, Comment> id2RelatedMap = strategy.getCommentLogic().getMulti(type, actorId, entryId,
				entryOwnerId, relatedIdList, tmpStrategy);
		if (null == id2RelatedMap || id2RelatedMap.isEmpty()) {
			return result;
		}
		for (Map.Entry<Long, Comment> entry : id2RelatedMap.entrySet()) {
			long cId = entry.getKey();
			SimpleCommentInfo relatedComment = new SimpleCommentInfo(
					entry.getValue());
			for (Long l : relatedCommentId2IdMap.get(cId)) {
				id2CommentMap.get(l).setSimpleComment(relatedComment);
			}
		}
		long end = System.nanoTime();
		StatisticsHelper.invokeGetRelatedComments((end - start)
				/ StatisticsHelper.NANO_TO_MILLIS, true);
		
		return result;
	}

}
