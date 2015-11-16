package com.renren.ugc.comment.xoa2.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.Entry;
import com.renren.ugc.comment.xoa2.GetCommentCountBatchRequest;
import com.renren.ugc.comment.xoa2.GetCommentCountBatchResponse;
import com.renren.ugc.comment.xoa2.IXCommentCenter;
import com.renren.xoa2.ErrorInfo;

/**
 * @author wangxx
 *
 * 封装一次get entry count的rpc调用
 * 注意:这里面的entryLists必须都属于同一个entryOwnerId
 * 
 */
public  class GetEntryCountRPCTask implements Runnable{
	
	private static Logger logger = Logger.getLogger(GetEntryCountRPCTask.class);
	
	private List<Entry> entryLists;
	
	private CommentType commentType;
	
	private int actorId;
	
	private IXCommentCenter xcc;
	
	private CountDownLatch countLatch;
	
	//返回结果
	private ConcurrentHashMap<Long,Integer> result;
	
	public GetEntryCountRPCTask(List<Entry> entryLists,CommentType commentType,int actorId,IXCommentCenter xcc,CountDownLatch countLatch,ConcurrentHashMap<Long,Integer> result){
		this.entryLists = entryLists;
		this.commentType = commentType;
		this.actorId = actorId;
		this.xcc = xcc;
		this.countLatch = countLatch;
		this.result = result;
	}
	
	@Override
	public void run(){
		
		if (CollectionUtils.isEmpty(entryLists)) {
			return;
		}

		int entryOwnerId = entryLists.get(0).getOwnerId();
		GetCommentCountBatchRequest req = new GetCommentCountBatchRequest();
		req.setType(commentType); // 设置要获取总数评论类型
		req.setActorId(actorId);
		req.setEntryOwnerId(entryOwnerId); // 设置要获取总数评论实体所有者id

		// 添加多个要获取总数评论实体id
		for (Entry entry : entryLists) {
			req.addToEntryIds(entry.getId());
		}

		Map<Long, Integer> entryId2countMap = new HashMap<Long, Integer>();
		try {
			GetCommentCountBatchResponse resp = xcc
					.getCommentCountBatch(req);
			if (resp.isSetCountMap()) { 
				// map的key是entryId，value是对应的评论个数
				entryId2countMap = resp.getCountMap();
				
				//存入返回结果
				result.putAll(entryId2countMap);
			} else {
				ErrorInfo errInfo = resp.getBaseRep().getErrorInfo();
				logger.error("error happened.code="+errInfo.getCode()+",errMsg="+errInfo.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			countLatch.countDown();
		}
	}

}
