package com.renren.ugc.comment.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentLinkedInfo;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;

public class LinkedInfoUtil {

    private static Logger logger = Logger.getLogger(LinkedInfoUtil.class);

    /**
     * @param mainComment
     * @param comments
     * @return
     * 
     *         根据commentId和comments生成如下格式的存在db中
     *         比如commentLinkedInfos中有两条linked，那么加上mainCommentId 总共3条
     *         linkedCommentCount
     *         =3&linkedCommentId0=****&linkedCommentId1=*
     *         ***&linkedCommentId2=******
     */
    public static Map<String, String> getLinkedMaps(Comment mainComment, List<Comment> comments) {

        if (comments == null || mainComment == null) {
            return null;
        }

        Map<String, String> results = new HashMap<String, String>(comments.size() + 2);

        int index = 0;
        results.put(CommentCenterConsts.LINKED_COMMENT_OWNER_ID + index, mainComment.getEntry()
                .getOwnerId() + "");
        results.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_ID + index, mainComment.getEntry()
                .getId() + "");
        results.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index, mainComment
        		.getType() + "");
        results.put(CommentCenterConsts.LINKED_COMMENT_ID + (index++), mainComment.getId() + "");

        for (Comment comment : comments) {
            results.put(CommentCenterConsts.LINKED_COMMENT_OWNER_ID + index, comment.getEntry()
                    .getOwnerId() + "");
            results.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_ID + index, comment.getEntry()
                    .getId() + "");
            results.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index, comment
                    .getType() + "");
            results.put(CommentCenterConsts.LINKED_COMMENT_ID + (index++), comment.getId() + "");
        }

        //放入size
        results.put(CommentCenterConsts.LINKED_COMMENT_COUNT, comments.size() + 1 + "");

        return results;
    }

    /**
     * @param comment
     * @return
     * 
     *         返回:
     *         这个评论linked的所有评论信息，其中会排除和参数comment的id相等的id
     */
    public static  List<CommentLinkedInfo> getLinkedCommentIdList(Comment comment,CommentStrategy strategy) {

        if (comment == null) {
            return null;
        }
        
        List<CommentLinkedInfo> commentLinkedInfos = new ArrayList<CommentLinkedInfo>();
        
        Metadata metaData = comment.getMetadata();
        if (metaData == null) {
            return null;
        }
        int linkedCount = 0;
        try {
            linkedCount = Integer.parseInt(metaData.get(CommentCenterConsts.LINKED_COMMENT_COUNT));

            for (int index = 0; index < linkedCount; ++index) {
                int linkedCommentOwnerId = Integer.parseInt(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_OWNER_ID + index));
                long linkedCommentEntryId = Long.parseLong(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_ENTRY_ID + index));
                long linkedCommentId = Long.parseLong(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_ID + index));
                if(!metaData.containsKey(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index)){
                	//历史原因，以前"虚拟相册"同步到"相册"的时候没有包含这个字段
                	//见http://jira.d.xiaonei.com/browse/COMMENT-40
                	//对于不包含这个字段的，就不进行同步了
                	continue;
                }
                int linkedCommentType = Integer.parseInt(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index));

                if (linkedCommentId != comment.getId()) {
                	commentLinkedInfos.add(new CommentLinkedInfo(linkedCommentId,
                            linkedCommentOwnerId, linkedCommentEntryId,linkedCommentType));
                }
            }

        } catch (Exception e) {
            logger.error("getLinkedCommentIdList error|commentId:" + comment.getId() + "|entryId:"
                    + comment.getEntry().getId() + "|ownerId:" + comment.getEntry().getOwnerId()
                    + "|type:" + comment.getType());
        }

        strategy.setCommentLinkedInfos(commentLinkedInfos);
        return commentLinkedInfos;
    }

    /**
     * @param comment
     * @param commentLinkedInfos
     * @return
     * 
     *         根据comment里的body,time等信息，结和commentLinkedInfos里的entryOwnerId,
     *         entryId共同生成一个Comment的列表
     */
    public static List<Comment> getCommentFromLinkedInfos(CommentType commentType,
    		 List<CommentLinkedInfo> commentLinkedInfos, Comment comment) {

        if (CollectionUtils.isEmpty(commentLinkedInfos)) {
            return Lists.newArrayList();
        }

        List<Comment> comments = new ArrayList<Comment>(commentLinkedInfos.size());
        if (!CollectionUtils.isEmpty(commentLinkedInfos)) {

            for (CommentLinkedInfo linkedInfo : commentLinkedInfos) {
                Comment linkedComment = comment.clone();
                if (linkedComment != null && linkedComment.getEntry() != null) {
                    //重新设置entryId,entryOwnerId,type,id
                    initializeCommentId(CommentType.findByValue(linkedInfo.getCommentType()), linkedComment);
                    linkedComment.buildLinkedInfos(linkedInfo.getEntryId(), linkedInfo.getEntryOwnerId(), CommentType.findByValue(linkedInfo.getCommentType()));

                    comments.add(linkedComment);
                }
            }
        }

        return comments;
    }

    private static void initializeCommentId(CommentType commentType,
            com.renren.ugc.comment.model.Comment comment) {
        long commentId = comment.getId();
        try {
                commentId = IDGenerator.nextCommentId(commentType);
        } catch (Exception e) {
            logger.error("an error occurs during generating comment id", e);
            throw new UGCCommentException(CommentError.STORAGE_ERROR,
                    CommentError.STORAGE_ERROR_MSG, e);
        }

        comment.setId(commentId);
    }

    /**
     * @param comment
     * @param commentLinkedInfos
     * @return
     * 
     *         根据replyComment的metadata,替换对应的commentId
     *         这个函数有2个返回:
     *         1. 把从replyComment解析得到的metadata放在linkedMaps里供db写入
     *         2. 把需要异步生成的commentId放在linkedComments里
     */
    public static List<Comment> getCommentFromReplyComment(CommentType commentType,
            Comment replyComment, Comment comment,Map<String, String> linkedMaps) {

        if (comment == null || replyComment == null || replyComment.getMetadata() == null || CollectionUtils.isEmpty(linkedMaps)) {
            return Collections.EMPTY_LIST;
        }
        
        List<Comment> linkedComments = new ArrayList<Comment>();
        Metadata metaData = replyComment.getMetadata();

        int index = 0;
        try {
            int linkedCount = Integer.parseInt(metaData
                    .get(CommentCenterConsts.LINKED_COMMENT_COUNT));
            //设置到Map里
            linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_COUNT, linkedCount+"");

            for (int i = 0; i < linkedCount; ++i) {
                int linkedCommentOwnerId = Integer.parseInt(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_OWNER_ID + index));
                long linkedCommentEntryId = Long.parseLong(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_ENTRY_ID + index));
                if(!metaData.containsKey(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index)){
                	//历史原因，以前"虚拟相册"同步到"相册"的时候没有包含这个字段
                	//见http://jira.d.xiaonei.com/browse/COMMENT-40
                	//对于不包含这个字段的，就不进行同步了
                	continue;
                }
                int linkedCommentEntryType = Integer.parseInt(metaData
                        .get(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index));
                
                //设置到Map里
                linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_ID + index, linkedCommentEntryId+"");
                linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_OWNER_ID + index, linkedCommentOwnerId+"");
                linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_ENTRY_TYPE + index, linkedCommentEntryType+"");
                
                if (linkedCommentOwnerId == comment.getEntry().getOwnerId()
                        && linkedCommentEntryId == comment.getEntry().getId()) {
                    //不需要设置到linkedComments中
                    //设置到Map里
                    linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_ID + index++, comment.getId()+"");
                } else {
                    //重新设置entryId,entryOwnerId,type,id
                    Comment linkedComment = comment.clone();
                    initializeCommentId(CommentType.findByValue(linkedCommentEntryType), linkedComment);
                    linkedComment.buildLinkedInfos(linkedCommentEntryId, linkedCommentOwnerId, CommentType.findByValue(linkedCommentEntryType));

                    linkedComments.add(linkedComment);
                    //设置到Map里
                    linkedMaps.put(CommentCenterConsts.LINKED_COMMENT_ID + index++, linkedComment.getId()+"");
                }
            }

        } catch (Exception e) {
            logger.error("getCommentFromReplyComment error|commentId:" + comment.getId() + "|entryId:"
                    + comment.getEntry().getId() + "|ownerId:" + comment.getEntry().getOwnerId()
                    + "|type:" + comment.getType());
        }

        return linkedComments;
    }
    
    public static void buildExtraInfoFromReplyComment(Comment replyComment,CommentStrategy strategy){
    	//被回复的评论中附带了被回复的图片url等信息，我们要取出来
    	Map<String, String> params = strategy.getParams(); 
		if(params == null){
    		params = new HashMap<String,String>();
        	strategy.setParams(params);
		}
		
		Metadata metaData = replyComment.getMetadata();
		
		if(metaData != null
				&& metaData.containsKey(CommentCenterConsts.COMMENT_PIC_URL_KEY)
				&& metaData.containsKey(CommentCenterConsts.COMMENT_PIC_ID_KEY )){
			params.put(CommentCenterConsts.COMMENT_PIC_URL_KEY, metaData.get(CommentCenterConsts.COMMENT_PIC_URL_KEY));
			params.put(CommentCenterConsts.COMMENT_PIC_ID_KEY,  metaData.get(CommentCenterConsts.COMMENT_PIC_ID_KEY));
		}
    }

}
