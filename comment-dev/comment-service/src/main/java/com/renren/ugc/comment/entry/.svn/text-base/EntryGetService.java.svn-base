package com.renren.ugc.comment.entry;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;

public interface EntryGetService {

    /**
     * @param actorId
     * @param entryOwnerId
     * @param entryId
     * @param comment
     * @return 去调用各个业务的接口去取entry,配置和取的属性在{@link:
     *         com.renren.ugc.comment.util.EntryConfig}中
     */
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException;

}
