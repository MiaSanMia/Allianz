package com.renren.ugc.comment.interceptor.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentBusIDentifier;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.xoa2.CommentType;

/**
 * @author wangxx
 * 
 * 
 *         对于老的全站评论Id，因为用的是老的share_comment_id，
 *         而share_comment本身的数据都已经迁移到了评论中心来了，两个id不一样
 *         带来的问题是当删除的时候，还要判断是老id还是新id
 *         ，因此返回的时候这里坐下统一，如果是老id的话，统一转化为新id,这样删除的时候我们就不用麻烦判断了
 * 
 *         对于新的全站评论Id，因为评论中心写向url_comment的时候，id也是评论中心的id，见代码{@link:
 *         CommentUrlMySQLService#create()} 方法
 */
public class GlobalShareCommentIdTransferInterceptor extends CommentLogicAdapter {

    private static Logger logger = Logger.getLogger(GlobalShareCommentIdTransferInterceptor.class);

    @Override
    public CommentListResult getGlobalCommentList(CommentType commentType, int actorId, long entryId,
            int entryOwner, String urlmd5, CommentStrategy strategy) throws UGCCommentException {

        //1.get return value
        //List<Comment> comments = (List<Comment>) strategy.getReturnedValue();
        CommentListResult commentListResult = (CommentListResult)strategy.getReturnedValue();
        List<Comment> comments = commentListResult.getCommentLists();
        
        if (commentType == CommentType.Share) {

            if (comments != null) {
                for (Comment comment : comments) {
                    if (comment.getId() < CommentCenterConsts.MAX_SHARE_COMMENT_ID) {
                        long newId = CommentBusIDentifier.getInstance().genCommentId(
                                CommentType.Share, comment.getId());
                        comment.setId(newId);
                    }
                }
            }
        }

        return commentListResult;
    }

}
