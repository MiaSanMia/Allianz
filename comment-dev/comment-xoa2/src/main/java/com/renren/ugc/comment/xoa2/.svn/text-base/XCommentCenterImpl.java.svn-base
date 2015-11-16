/**
 * @(#)XCommentCenterImpl.java, 2012-11-9. Copyright 2012 RenRen, Inc. All
 *                              rights reserved.
 */
package com.renren.ugc.comment.xoa2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.CommentListResult;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForCommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.xoa2.util.AccessLogger;
import com.renren.ugc.comment.xoa2.util.CommentConvertUtils;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.renren.ugc.comment.xoa2.util.CommentIdDescComparator;
import com.renren.xoa2.BaseResponse;
import com.renren.xoa2.ErrorInfo;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

/**
 * XOA2化的评论中心服务接口实现
 * 
 * @author
 */
@Service
public class XCommentCenterImpl implements IXCommentCenter {

    private static final int NANO_TO_MILLIS = 1000000;

    private static final String VOICE_COMMENT_CONTENT = "这是一条语音评论。";

    protected static final Logger logger =
            Logger.getLogger(XCommentCenterImpl.class);

    CommentCenter commentCenter = CommentCenter.getInstance();

    /** 默认的Comment查询最大条目数 */
    private static final int DEFAULT_QUERY_LIMIT = 100;

    /** 默认的Comment查询头部尾部的最大条目数 */
    private static final int DEFAULT_HEAD_TAIL_QUERY_LIMIT = 1;

    /**
     * 最大的单次查询数量限制
     */
    private static final int MAX_QUERY_LIMIT = 100;

    /**
     * 最大的头部尾部数量限制
     */
    private static final int DEFAULT_HEAD_TAIL_QUERY_MAX_LIMIT = 30;

    /**
     * 最大的新鲜事查询数量限制
     */
    private static final int MAX_FEED_QUERY_LIMIT = 30;

    @Override
    public PingResponse ping(PingRequest req) {
        String content = req.getContent();
        PingResponse resp = new PingResponse();
        resp.setContent("OK:" + content);
        return resp;
    }

    @Override
    public CreateCommentResponse createComment(CreateCommentRequest req) {

        if (logger.isDebugEnabled()) {
            logger.debug("Invoke comment center xoa2 api " + req.toString());
        }
        long start = System.nanoTime();
        CreateCommentResponse resp = new CreateCommentResponse();

        CommentType type = req.getType();

        com.renren.ugc.comment.model.Comment comm =
                new com.renren.ugc.comment.model.Comment();
        comm.setContent(req.getContent());

        if (req.getActorId() < 0 || req.getReplyToId() < 0) {
            resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID,
                "Params invalid. actorId:[" + req.getActorId()
                        + "] replyToId:[" + req.getReplyToId() + "]."));
            return resp;
        }

        if (req.getReplyToId() != 0) {
            comm.setToUserId(req.getReplyToId());
            comm.setToCommendId(req.getToCommentId());
            // 悄悄话
            if (req.isWhisper()) {
                comm.setWhipserToId(req.getReplyToId());
            }
        } else {
            // 悄悄话
            if (req.isWhisper()) {
                comm.setWhipserToId(req.getEntryOwnerId());
            }
        }

        Map<String, String> params = req.getParams();
        CommentStrategy strategy = new CommentStrategy();

        strategy.setParams(params);
        strategy = prepareParamsForType(type, strategy, params);

        // get all entry props
        // getEntryProps(type, params, comm);
        boolean success = false;
        try {
            com.renren.ugc.comment.model.Comment createdComment =
                    commentCenter.create(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), comm, strategy);
            resp.setComment(CommentConvertUtils.toXoa2Comment(createdComment,
                strategy.isNeedMetadata()));

            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }

            success = true;
        } catch (UGCCommentException e) {
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + type + " " + e.getMessage()));
        }
        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        // do statistics
        StatisticsHelper.invokeCreateComment(execTime, success);
        AccessLogger.logCreateComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public CreateVoiceCommentResponse createVoiceComment(
        CreateVoiceCommentRequest req) {
        if (logger.isDebugEnabled()) {
            logger.debug("Invoke comment center xoa2 api " + req.toString());
        }
        long start = System.nanoTime();

        CreateVoiceCommentResponse resp = new CreateVoiceCommentResponse();

        CommentType type = req.getType();

        com.renren.ugc.comment.model.Comment comm =
                new com.renren.ugc.comment.model.Comment();

        if (req.getReplyToId() != 0) {
            comm.setToUserId(req.getReplyToId());
            comm.setToCommendId(req.getToCommentId());
            // 悄悄话
            if (req.isWhisper()) {
                comm.setWhipserToId(req.getReplyToId());
            }
        } else {
            // 悄悄话
            if (req.isWhisper()) {
                comm.setWhipserToId(req.getEntryOwnerId());
            }
        }

        // build voice info
        com.renren.ugc.comment.model.CommentVoiceInfo voiceInfo =
                new com.renren.ugc.comment.model.CommentVoiceInfo();
        voiceInfo.setVoiceUrl(req.getVoiceUrl());
        voiceInfo.setVoiceLength(req.getVoiceLength());
        voiceInfo.setVoiceRate(req.getVoiceRate());
        voiceInfo.setVoiceSize(req.getVoiceSize());
        comm.setVoiceInfo(voiceInfo);
        String content = VOICE_COMMENT_CONTENT;
        if (req.getReplyToId() != 0) {
            User replyUser = WUserAdapter.getInstance().get(req.getReplyToId());
            content = "回复" + replyUser.getName() + ": " + VOICE_COMMENT_CONTENT;
        }
        comm.setContent(content); // the content for a voice
                                  // comment is constant literal

        CommentStrategy strategy = new CommentStrategy();
        Map<String, String> params = req.getParams();
        strategy = prepareParamsForType(type, strategy, params);

        boolean success = false;
        try {
            com.renren.ugc.comment.model.Comment createdComment =
                    commentCenter.create(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), comm, strategy);
            resp.setComment(CommentConvertUtils.toXoa2Comment(createdComment,
                strategy.isNeedMetadata()));
            success = true;
            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }
        } catch (UGCCommentException e) {
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(-1, "Unknown comment error:" + type
                                                + " " + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeCreateVoiceComment(execTime, success);
        AccessLogger.logCreateVoiceComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    /**
     * prepare business related strategy according to comment type
     */
    private CommentStrategy prepareParamsForType(CommentType type,
        CommentStrategy strategy, Map<String, String> params) {
        if (type == null || params == null) {
            return strategy;
        }

        // first save the params
        strategy.setParams(params);

        // 利用params的参数来重置comment config里的配置
        strategy.setByParams(params);

        return strategy;
    }

    @Override
    public GetCommentListResponse getCommentList(GetCommentListRequest req) {

        long start = System.nanoTime();

        GetCommentListResponse resp = new GetCommentListResponse();

        int queryLimit = req.getLimit();
        if (queryLimit <= 0 || queryLimit > MAX_QUERY_LIMIT) {
            queryLimit = DEFAULT_QUERY_LIMIT;
        }

        // set query limit = limit + 1 to support "has more"
        CommentStrategy strategy =
                new CommentStrategy().setQueryLimit(queryLimit + 1).setQueryOrder(
                    req.isDesc() ? QueryOrder.DESC : QueryOrder.ASC).setFilterWhisper(
                    false).setOriginalQueryLimit(queryLimit);

        // Paging, if the border comment id is set
        if (req.isSetBorderCommentId()) {
            strategy.setQueryBorderID(req.getBorderCommentId());
        }

        // for compatibility reason, support offset + limit query (offset starts
        // from 0)
        if (req.isSetOffset()) {
            strategy.setQueryOffset(req.getOffset());
        }

        // for compatibility reason, support page num + limit query (page num
        // starts from 1)
        if (req.isSetPageNum() && req.getPageNum() > 0) {
            int pageNum = req.getPageNum();
            // if (pageNum < 1) pageNum = 1;
            strategy.setQueryOffset((pageNum - 1) * queryLimit);
            strategy.setPageNum(pageNum);
        }

        CommentType type = req.getType();

        strategy = prepareParamsForType(type, strategy, req.getParams());
        boolean success = false;
        try {

            List<com.renren.ugc.comment.model.Comment> commentList = null;
//            commentList =
//                    commentCenter.getListByEntry(type, req.getActorId(),
//                        req.getEntryId(), req.getEntryOwnerId(), strategy);
//
//            if (commentList.size() == queryLimit + 1) {
//                // has more
//                resp.setMore(true);
//                // remove the last comment for detecting has more
//                commentList.remove(commentList.size() - 1);
//            } else {
//                resp.setMore(false);
//            }
            CommentListResult commentListResult = commentCenter.getListByEntry(type, req.getActorId(),
                    req.getEntryId(), req.getEntryOwnerId(), strategy);
            
            resp.setMore(commentListResult.isHasMore());
            resp.setPre(commentListResult.isHasPre());
            
            commentList = commentListResult.getCommentLists();
            resp.setCommentList(CommentConvertUtils.toXoa2CommentList(
                commentList, strategy.isNeedMetadata()));

            // BIZFRAMEWORK-20 get the total number of comments that belong to
            // the current entry
            long totalCount =
                    commentCenter.getCountByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), strategy);
            resp.setTotalCount(totalCount);

            // 支持"全站评论"评论
            if (req.isIncludeGlobalComment()) {
                List<com.renren.ugc.comment.model.Comment> globalCommentList =
                        null;
                // set the urlmd5 "null" because we don't know it yet.
                // comment center will try to get the url md5 internally
//                globalCommentList =
//                        commentCenter.getGlobalCommentListByEntry(type,
//                            req.getActorId(), req.getEntryId(),
//                            req.getEntryOwnerId(), null, strategy);
                CommentListResult globalCommentListResult =  commentCenter.getGlobalCommentListByEntry(type,
                     req.getActorId(), req.getEntryId(),
                      req.getEntryOwnerId(), null, strategy);
                
                globalCommentList = globalCommentListResult.getCommentLists();

                resp.setGlobalCommentList(CommentConvertUtils.toXoa2CommentList(
                    globalCommentList, strategy.isNeedMetadata()));

            }

            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }

            success = true;
        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + type + " " + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetCommentList(execTime, success);
        AccessLogger.logGetCommentList(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            queryLimit,
            success ? resp.getCommentListSize() : 0,
            (strategy.getQueryBorderID() == 0 && strategy.getQueryOffset() == 0),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetCommentResponse getComment(GetCommentRequest req) {
        long start = System.nanoTime();

        GetCommentResponse resp = new GetCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        CommentStrategy strategy = new CommentStrategy();

        boolean success = false;
        try {
            com.renren.ugc.comment.model.Comment comment =
                    commentCenter.get(type, req.getActorId(), req.getEntryId(),
                        req.getEntryOwnerId(), req.getCommentId(), strategy);

            if (comment != null) {
                resp.setComment(CommentConvertUtils.toXoa2Comment(comment,
                    strategy.isNeedMetadata()));
                success = true;

                if (strategy.getEntry() != null) {
                    resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
                } else if (comment.getEntry() != null) {
                    resp.setEntry(CommentConvertUtils.toXoa2Entry(comment.getEntry()));
                }
            } else {
                logger.error("getComment comment not exist|entryId:"
                             + req.getEntryId() + "|ownerId:"
                             + req.getEntryOwnerId() + "|commentId:"
                             + req.getCommentId() + "|type:" + type.getValue());
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG));
                success=true;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + type + " " + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetComment(execTime, success);
        AccessLogger.logGetComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetMultiCommentsResponse getMultiComments(GetMultiCommentsRequest req) {
        GetMultiCommentsResponse resp = new GetMultiCommentsResponse();

        long start = System.nanoTime();
        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        if (req.getCommentIdsSize() > MAX_QUERY_LIMIT) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "too many comment ids"));
        }

        CommentStrategy strategy = new CommentStrategy();

        boolean success = false;
        try {
            Map<Long, com.renren.ugc.comment.model.Comment> commentMap =
                    commentCenter.getMulti(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        req.getCommentIds(), strategy);

            resp.setComments(CommentConvertUtils.toXoa2CommentMap(commentMap,
                strategy.isNeedMetadata()));
            success = true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + type + " " + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetMultiComments(execTime, success);
        AccessLogger.logGetMultiComments(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentIdsSize() > 0 ? req.getCommentIds().get(0) : 0,
            req.getCommentIdsSize(),
            success ? resp.getCommentsSize() : 0,
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());
        return resp;
    }

    @Override
    public RemoveCommentResponse removeComment(RemoveCommentRequest req) {

        long start = System.nanoTime();

        RemoveCommentResponse resp = new RemoveCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        CommentStrategy strategy = new CommentStrategy();
        strategy = prepareParamsForType(type, strategy, req.getParams());

        boolean success = false;
        try {

            boolean removed =
                    commentCenter.remove(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        req.getCommentId(), strategy);

            if (removed == true) {
                resp.setRemoved(removed);
            } else {
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG));
            }
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when removing comment:" + type + " "
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeRemoveComment(execTime, success);
        AccessLogger.logRemoveComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetGlobalCommentListResponse getGlobalCommentList(
        GetGlobalCommentListRequest req) {
        long start = System.nanoTime();

        GetGlobalCommentListResponse resp = new GetGlobalCommentListResponse();

        int queryLimit = req.getLimit();
        CommentStrategy strategy = null;
        if (queryLimit <= 0 || queryLimit > MAX_QUERY_LIMIT) {
            queryLimit = DEFAULT_QUERY_LIMIT;
        }

        // set query limit = limit + 1 to support "has more"
        strategy =
                new CommentStrategy().setQueryLimit(queryLimit + 1).setQueryOrder(
                    req.isDesc() ? QueryOrder.DESC : QueryOrder.ASC).setFilterWhisper(
                    false).setOriginalQueryLimit(queryLimit);

        // Paging, if the border comment id is set
        if (req.isSetBorderCommentId()) {
            strategy.setQueryBorderID(req.getBorderCommentId());
        }

        // for compatibility reason, support offset + limit query
        if (req.isSetOffset()) {
            strategy.setQueryOffset(req.getOffset());
        }

        CommentType type = req.getType();

        strategy = prepareParamsForType(type, strategy, req.getParams());
        boolean success = false;
        try {

            List<com.renren.ugc.comment.model.Comment> commentList = null;
//            commentList =
//                    commentCenter.getGlobalCommentListByEntry(type,
//                        req.getActorId(), req.getEntryId(),
//                        req.getEntryOwnerId(), req.getUrlmd5(), strategy);
//
//            if (commentList.size() == queryLimit + 1) {
//                // has more
//                resp.setMore(true);
//                // remove the last comment for detecting has more
//                commentList.remove(commentList.size() - 1);
//            } else {
//                resp.setMore(false);
//            }
            CommentListResult commentListResult = commentCenter.getGlobalCommentListByEntry(type,
                  req.getActorId(), req.getEntryId(),
                  req.getEntryOwnerId(), req.getUrlmd5(), strategy);
            
            resp.setMore(commentListResult.isHasMore());
            
            commentList = commentListResult.getCommentLists();
            resp.setCommentList(CommentConvertUtils.toXoa2CommentList(commentList));
            resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            // BIZFRAMEWORK-20 get the total number of comments that belong to
            // the current entry
            long totalCount = 0L;
            if (!StringUtils.isEmpty(strategy.getUrlMd5())) {
                totalCount =
                        commentCenter.getCountByEntry(type, req.getActorId(),
                            strategy.getUrlMd5(), strategy);
            }
            resp.setTotalCount(totalCount);

            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }

            success = true;
        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when getting comment list:" + type + ", "
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetGlobalCommentList(execTime, success);
        AccessLogger.logGetGlobalCommentList(
            type,
            req.getActorId(),
            req.getUrlmd5(),
            strategy.getQueryLimit(),
            success ? resp.getCommentListSize() : 0,
            (strategy.getQueryBorderID() == 0 && strategy.getQueryOffset() == 0),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());
        return resp;
    }

    /**
     * The code for this api isn't ready for release
     */
    @Override
    public RemoveAllCommentResponse removeAllComment(RemoveAllCommentRequest req) {
        RemoveAllCommentResponse resp = new RemoveAllCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        try {
            boolean removedAll =
                    commentCenter.removeAllByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        new CommentStrategy());

            resp.setAllRemoved(removedAll);
            if (removedAll == false) {
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG));
            }

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when removing all comments:" + type + " "
                        + e.getMessage()));
        }

        return resp;
    }

    @Override
    public RecoverCommentResponse recoverComment(RecoverCommentRequest req) {
        long start = System.nanoTime();
        RecoverCommentResponse resp = new RecoverCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }
        CommentStrategy strategy = new CommentStrategy();
        strategy = prepareParamsForType(type, strategy, req.getParams());

        boolean success = false;
        try {

            boolean recovered =
                    commentCenter.recover(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        req.getCommentId(), strategy);

            if (recovered == true) {
                resp.setRecovered(recovered);
            } else {
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_DELETED,
                    CommentError.COMMENT_NOT_DELETED_MSG));
            }
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when recovering comment:" + type + " "
                        + e.getMessage()));
        }
        long end = System.nanoTime();

        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeRecoverComment(execTime, success);
        AccessLogger.logRecoverComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;

    }

    /**
     * The code for this api isn't ready for release
     */
    @Override
    public RecoverAllCommentResponse recoverAllComment(
        RecoverAllCommentResquest req) {
        RecoverAllCommentResponse resp = new RecoverAllCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        try {
            boolean allRecovered =
                    commentCenter.recoverAllByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        new CommentStrategy());

            resp.setAllRecovered(allRecovered);

            if (allRecovered == false) {
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_DELETED,
                    CommentError.COMMENT_NOT_DELETED_MSG));
            }

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(-1, e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            resp.setBaseRep(getBaseResponse(-1, "Recover all comment error:"
                                                + e.getMessage()));
        }

        return resp;
    }

    /**
     * The code for this api isn't ready for release
     */
    @Override
    public UpdateCommentResponse updateComment(UpdateCommentRequest req) {
        UpdateCommentResponse resp = new UpdateCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        CommentStrategy strategy = new CommentStrategy();

        com.renren.ugc.comment.model.Comment newComment =
                new com.renren.ugc.comment.model.Comment();
        newComment.setContent(req.getNewContent());
        try {
            boolean updated =
                    commentCenter.update(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        req.getCommentId(), newComment, strategy);

            resp.setUpdated(updated);

            if (updated == false) {
                return resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG));
            }

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            return resp.setBaseRep(getBaseResponse(e.getErrorCode(),
                e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknowncomment error:" + type + e.getMessage()));
        }

        return resp;
    }

    /**
     * 异常情况下，设定错误返回类型
     * 
     * @param errorCode
     * @param errorMsg
     * @return
     */
    static BaseResponse getBaseResponse(int errorCode, String errorMsg) {
        BaseResponse baseRep = new BaseResponse();

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(errorCode);
        errorInfo.setMsg(errorMsg);

        baseRep.setErrorInfo(errorInfo);

        return baseRep;
    }

    @Override
    public IncreaseVoiceCommentPlayCountResponse increaseVoiceCommentPlayCount(
        IncreaseVoiceCommentPlayCountRequest req) {

        long start = System.nanoTime();

        IncreaseVoiceCommentPlayCountResponse resp =
                new IncreaseVoiceCommentPlayCountResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        CommentStrategy strategy = new CommentStrategy();
        strategy = prepareParamsForType(type, strategy, req.getParams());

        /**
         * 要增加的播放的次数，如果不设，默认为1
         */
        int increment = req.isSetIncrement() ? req.getIncrement() : 1;

        boolean success = false;
        try {

            int playCount =
                    commentCenter.increaseVoiceCommentPlayCount(type,
                        req.getActorId(), req.getEntryId(),
                        req.getEntryOwnerId(), req.getCommentId(), increment,
                        strategy);

            resp.setPlayCount(playCount);

            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when increasing voice play count:" + type
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeIncreaseVoicePlayCount(execTime, success);
        AccessLogger.logIncreaseVoicePlayCount(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentId(),
            req.getIncrement(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetCommentCountResponse getCommentCount(GetCommentCountRequest req) {

        long start = System.nanoTime();

        GetCommentCountResponse resp = new GetCommentCountResponse();
        CommentStrategy strategy = new CommentStrategy();

        CommentType type = req.getType();

        boolean success = false;
        try {
            long totalCount =
                    commentCenter.getCountByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), strategy);

            resp.setCount((int) totalCount);
            success = true;
        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when get comment count:" + type + ", "
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetCommentCount(execTime, success);
        AccessLogger.logGetCommentCount(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            success ? resp.getCount() : 0,
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetCommentCountBatchResponse getCommentCountBatch(
        GetCommentCountBatchRequest req) {

        long start = System.nanoTime();

        GetCommentCountBatchResponse resp = new GetCommentCountBatchResponse();

        CommentStrategy strategy = new CommentStrategy();

        boolean success = false;
        try {
            Map<Long, Integer> results =
                    commentCenter.getCountByEntryBatch(req.getType(),
                        req.getActorId(), req.getEntryIds(),
                        req.getEntryOwnerId(), strategy);
            resp.setCountMap(results);
            success = true;
        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown error when get comment count batch, :" + req.getType()
                        + ", " + e.getMessage()));
        }
        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetCommentCountBatch(execTime, success);
        AccessLogger.logGetCommentCountBatch(
            req.getType(),
            req.getActorId(),
            req.getEntryIdsSize() > 0 ? req.getEntryIds().get(0) : 0,
            req.getEntryOwnerId(),
            success ? resp.getCountMapSize() : 0,
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.xoa2.IXCommentCenter#getFriendsCommentList(com
     * .renren.ugc.comment.xoa2.GetFriendsCommentListRequest)
     */
    @Override
    public GetFriendsCommentListResponse getFriendsCommentList(
        GetFriendsCommentListRequest req) {

        long start = System.nanoTime();

        GetFriendsCommentListResponse resp =
                new GetFriendsCommentListResponse();

        if (req.getActorId() == 0) {
            resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "actorId is 0"));
            return resp;
        }

        int queryLimit = req.getLimit();
        CommentStrategy strategy = null;
        if (queryLimit <= 0 || queryLimit > MAX_QUERY_LIMIT) {
            queryLimit = DEFAULT_QUERY_LIMIT;
        }

        if (req.getType() == CommentType.Status
            || req.getType() == CommentType.Blog
            || req.getType() == CommentType.Share
            || req.getType() == CommentType.Album
            || req.getType() == CommentType.Photo
            || req.getType() == CommentType.ShareAlbumPhoto) {
            strategy =
                    new CommentStrategy().setQueryLimit(queryLimit + 1).setQueryOrder(
                        req.isDesc() ? QueryOrder.DESC : QueryOrder.ASC).setFilterWhisper(
                        false).setOriginalQueryLimit(queryLimit);
        } else {
            logger.error(CommentError.INVALID_COMMENT_TYPE_MSG
                         + " comment type " + req.getType().toString()
                         + " doesn't support getting friends comment");
            resp.setBaseRep(getBaseResponse(CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
            return resp;
        }

        // support offset + limit query
        if (req.isSetOffset()) {
            strategy.setQueryOffset(req.getOffset());
        }

        CommentType type = req.getType();

        strategy = prepareParamsForType(type, strategy, req.getParams());

        boolean success = false;
        try {

            List<com.renren.ugc.comment.model.Comment> commentList = null;
//            if (commentList.size() == queryLimit + 1) {
//                // has more
//                resp.setMore(true);
//                // remove the last comment for detecting has more
//                commentList.remove(commentList.size() - 1);
//            } else {
//                resp.setMore(false);
//            }
            CommentListResult commentListResult =
                    commentCenter.getFriendsCommentListByEntry(type,
                        req.getActorId(), req.getEntryId(),
                        req.getEntryOwnerId(), strategy);
            
            resp.setMore(commentListResult.isHasMore());
            
            commentList = commentListResult.getCommentLists();
            resp.setCommentList(CommentConvertUtils.toXoa2CommentList(
                commentList, strategy.isNeedMetadata()));
            resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            success = true;
        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown error when get friends comment list:" + type + ", "
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetFriendsCommentList(execTime, success);
        AccessLogger.logGetFriendsCommentList(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            strategy.getQueryLimit(),
            success ? resp.getCommentListSize() : 0,
            (strategy.getQueryBorderID() == 0 && strategy.getQueryOffset() == 0),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;

    }

    @Override
    public GetHeadAndTailCommentListResponse getHeadAndTailCommentList(
        GetHeadAndTailCommentListRequest req) {

        long start = System.nanoTime();

        GetHeadAndTailCommentListResponse resp =
                new GetHeadAndTailCommentListResponse();

        int queryHeadLimit = req.getHeadLimit();
        int queryTailLimit = req.getTailLimit();

        queryHeadLimit =
                queryHeadLimit >= 0 ? queryHeadLimit : DEFAULT_HEAD_TAIL_QUERY_LIMIT;
        queryTailLimit =
                queryTailLimit >= 0 ? queryTailLimit : DEFAULT_HEAD_TAIL_QUERY_LIMIT;

        if (queryHeadLimit + queryTailLimit > DEFAULT_HEAD_TAIL_QUERY_MAX_LIMIT) {
            String msg =
                    "the sum of queryHeadLimit and queryTailLimit is too big";
            logger.error(msg);
            resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, msg));
            return resp;
        }

        CommentStrategy strategy = new CommentStrategy();
        CommentType type = req.getType();
        strategy = prepareParamsForType(type, strategy, req.getParams());
        CommentListResult commentListResult = null;

        // in the tree set, the comments are sorted by their ids in desc order
        TreeSet<com.renren.ugc.comment.model.Comment> commentSet =
            new TreeSet<com.renren.ugc.comment.model.Comment>(new CommentIdDescComparator());
        boolean success = false;
        try {
            // 1. get total count
            long totalCount =
                    commentCenter.getCountByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), strategy);
            resp.setTotalCount(totalCount);

            // 2. get head comment list
            List<com.renren.ugc.comment.model.Comment> headCommentList = new ArrayList<com.renren.ugc.comment.model.Comment>(queryHeadLimit);

            strategy.setQueryOrder(QueryOrder.ASC);
            strategy.setQueryLimit(queryHeadLimit);
            strategy.setOriginalQueryLimit(queryHeadLimit);
            commentListResult  =
                    commentCenter.getListByEntry(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), strategy);
            
            commentSet.addAll(commentListResult.getCommentLists());


            // 3. get tail comment list
            List<com.renren.ugc.comment.model.Comment> tailCommentList = new ArrayList<com.renren.ugc.comment.model.Comment>(queryTailLimit);
            strategy.setQueryOrder(QueryOrder.DESC);
            strategy.setQueryLimit(queryTailLimit);
            strategy.setOriginalQueryLimit(queryTailLimit);
            commentListResult  =
                    commentCenter.getListByEntry(type, req.getActorId(),
                            req.getEntryId(), req.getEntryOwnerId(), strategy);

            commentSet.addAll(commentListResult.getCommentLists());


            // 4. construct the final result
            Iterator<com.renren.ugc.comment.model.Comment> it = commentSet.iterator();

            // 4.1 filter all the whispers, currently we'll not display any whisper comments
            while (it.hasNext()) {
                com.renren.ugc.comment.model.Comment c = it.next();
                if (c.isWhisper()) {
                    it.remove();
                }
            }


            if (req.isDesc()) {
                // 4.2 first construct the tail comment list
                for (int i = 0; i < queryTailLimit && !commentSet.isEmpty(); i++) {
                    tailCommentList.add(commentSet.pollFirst());
                }

                // 4.3 then construct the head comment list, if there is any remaining
                for (int i = 0; i < queryHeadLimit && !commentSet.isEmpty(); i++) {
                    headCommentList.add(commentSet.pollFirst());
                }
            } else {
                // 4.2 first construct the head comment list
                for (int i = 0; i < queryHeadLimit && !commentSet.isEmpty(); i++) {
                    headCommentList.add(commentSet.pollLast());
                }

                // 4.3 then construct the tail comment list, if there is any remaining
                for (int i = 0; i < queryTailLimit && !commentSet.isEmpty(); i++) {
                    tailCommentList.add(commentSet.pollLast());
                }
            }

            resp.setHeadCommentList(CommentConvertUtils.toXoa2CommentList(headCommentList));
            resp.setTailCommentList(CommentConvertUtils.toXoa2CommentList(tailCommentList));

            resp.setMore(totalCount > queryHeadLimit + queryTailLimit);

            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                e.getMessage()));
        }
        long end = System.nanoTime();

        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeGetHeadAndTailCommentList(execTime, success);
        AccessLogger.logGetHeadAndTailCommentList(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getHeadLimit(),
            req.getTailLimit(),
            success ? resp.getHeadCommentListSize() : 0,
            success ? resp.getTailCommentListSize() : 0,
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public CreateLinkedCommentResponse createLinkedComment(
        CreateLinkedCommentRequest req) {
        long start = System.nanoTime();

        if (logger.isDebugEnabled()) {
            logger.debug("Invoke comment center xoa2 api " + req.toString());
        }

        CreateLinkedCommentResponse resp = new CreateLinkedCommentResponse();

        CommentType type = req.getType();

        com.renren.ugc.comment.model.Comment comm =
                new com.renren.ugc.comment.model.Comment();
        comm.setContent(req.getContent());

        if (req.getReplyToId() != 0) {
            comm.setToUserId(req.getReplyToId());
            comm.setToCommendId(req.getToCommentId());
            // 悄悄话
            if (req.isWhisper()) {
                comm.setWhipserToId(req.getReplyToId());
            }
        }

        Map<String, String> params = req.getParams();
        CommentStrategy strategy = new CommentStrategy();

        strategy.setParams(params);
        strategy = prepareParamsForType(type, strategy, params);

//        if (req.isSetCommentLinkedInfos()) {
//            // 如果评论被关联的话，转化以后放在commentstrategy里
//
//            List<CommentLinkedInfo> linkedInfos = req.getCommentLinkedInfos();
//            strategy.setCommentLinkedInfos(com.renren.ugc.comment.model.CommentLinkedInfo.toInternalCommentLinkInfos(linkedInfos));
//            comm.setLinked(true);
//        }

        boolean success = false;
        try {
            com.renren.ugc.comment.model.Comment createdComment =
                    commentCenter.create(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(), comm, strategy);
            resp.setComment(CommentConvertUtils.toXoa2Comment(createdComment));

            if (strategy.getEntry() != null) {
                resp.setEntry(CommentConvertUtils.toXoa2Entry(strategy.getEntry()));
            }
            success = true;

        } catch (UGCCommentException e) {
            return resp.setBaseRep(getBaseResponse(e.getErrorCode(),
                e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + type + " " + e.getMessage()));
        }
        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeCreateLinkedComment(execTime, success);
        AccessLogger.logCreateLinkedComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }

    @Override
    public GetCommentsForFeedResponse getCommentsForFeed(
        GetCommentsForFeedRequest req) {

        List<FeedCommentInfo> infos = req.getInfos();

        GetCommentsForFeedResponse resp = new GetCommentsForFeedResponse();

        long start = System.nanoTime();

        if (req.getInfos() == null || req.getInfos().size() == 0) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "feedCommentInfos is null"));
        }

        if (req.getInfos().size() > MAX_FEED_QUERY_LIMIT) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID,
                "too many feedCommentInfos"));
        }

        CommentStrategy strategy =
                new CommentStrategy().setReturnFullHeadUrl(true);
        
        // commentType没有用到，这里的作用仅仅是通过{@link: CommentLogicInvocationHandler}的校验,
        // 因为CommentLogicInvocationHandler强制要求地一个方法调用的参数是comment type
        CommentType fakeType = CommentType.Dummy;
        Map<String, String> params = req.getParams();
        strategy = prepareParamsForType(fakeType,strategy, params);

        List<com.renren.ugc.comment.model.FeedCommentInfo> internalInfos =
                new ArrayList<com.renren.ugc.comment.model.FeedCommentInfo>(
                    req.infos.size());
        for (FeedCommentInfo info : req.getInfos()) {
            internalInfos.add(new com.renren.ugc.comment.model.FeedCommentInfo(
                info));
        }

        boolean success = false;
        try {
            Map<Long, com.renren.ugc.comment.model.FeedCommentResult> commentMap =
                    commentCenter.getCommentsForFeed(fakeType, internalInfos,
                        strategy);

            resp.setResults(CommentConvertUtils.toXoa2FeedCommentMap(commentMap));
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetCommentsForFeed(execTime, success);

        return resp;
    }

    @Override
    public GetCommentsForFeedCountResponse getCommentsForFeedCount(
        GetCommentsForFeedCountRequest req) {
        // TODO Auto-generated method stub
        List<FeedCommentInfo> infos = req.getInfos();

        GetCommentsForFeedCountResponse resp =
                new GetCommentsForFeedCountResponse();

        long start = System.nanoTime();

        if (req.getInfos() == null || req.getInfos().size() == 0) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "feedCommentInfos is null"));
        }

        if (req.getInfos().size() > MAX_FEED_QUERY_LIMIT) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID,
                "too many feedCommentInfos"));
        }

        CommentStrategy strategy =
                new CommentStrategy().setReturnFullHeadUrl(true);

        List<com.renren.ugc.comment.model.FeedCommentInfo> internalInfos =
                new ArrayList<com.renren.ugc.comment.model.FeedCommentInfo>(
                    req.infos.size());
        for (FeedCommentInfo info : req.getInfos()) {
            internalInfos.add(new com.renren.ugc.comment.model.FeedCommentInfo(
                info));
        }

        boolean success = false;
        // commentType没有用到，这里的作用仅仅是通过{@link: CommentLogicInvocationHandler}的校验,
        // 因为CommentLogicInvocationHandler强制要求地一个方法调用的参数是comment type
        CommentType fakeType = CommentType.Dummy;
        try {
            Map<Long, Integer> commentMap =
                    commentCenter.getCommentsForFeedCount(fakeType,
                        internalInfos, strategy);

            resp.setResults(commentMap);
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetCommentsForFeedCount(execTime, success);

        return resp;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.xoa2.XCommentCenter.Iface#getFriendCommentsForFeed
     * (com.renren.ugc.comment.xoa2.GetFriendCommentsForFeedRequest)
     */
    @Override
    public GetFriendCommentsForFeedResponse getFriendCommentsForFeed(
        GetFriendCommentsForFeedRequest req) throws TException {

        GetFriendCommentsForFeedResponse resp =
                new GetFriendCommentsForFeedResponse();

        long start = System.nanoTime();

        if (req.getInfos() == null || req.getInfos().size() == 0) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "feedCommentInfos is null"));
        }

        if (req.getInfos().size() > MAX_FEED_QUERY_LIMIT) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID,
                "too many feedCommentInfos"));
        }

        CommentStrategy strategy =
                new CommentStrategy().setReturnFullHeadUrl(true);
        strategy.setCacheDisabled(true);
        strategy.setReplaceUbb(true);
        strategy.setReplaceShortUrl(true);
        strategy.setReplaceAt(true);

        List<com.renren.ugc.comment.model.FeedCommentInfo> internalInfos =
                new ArrayList<com.renren.ugc.comment.model.FeedCommentInfo>(
                    req.infos.size());
        for (FeedCommentInfo info : req.getInfos()) {
            internalInfos.add(new com.renren.ugc.comment.model.FeedCommentInfo(
                info));
        }

        boolean success = false;
        // commentType没有用到，这里的作用仅仅是通过{@link: CommentLogicInvocationHandler}的校验,
        // 因为CommentLogicInvocationHandler强制要求地一个方法调用的参数是comment type
        CommentType fakeType = CommentType.Dummy;
        try {
            Map<Long, com.renren.ugc.comment.model.FeedCommentResult> commentMap =
                    commentCenter.getFriendCommentsForFeed(fakeType,
                        internalInfos, strategy);

            resp.setResults(CommentConvertUtils.toXoa2FeedCommentMap(commentMap));
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetFriendCommentsForFeed(execTime, success);

        return resp;
    }

    /*
     * (non-Javadoc)
     * @see com.renren.ugc.comment.xoa2.XCommentCenter.Iface#
     * getFriendCommentsForFeedCount
     * (com.renren.ugc.comment.xoa2.GetFriendCommentsForFeedCountRequest)
     */
    @Override
    public GetFriendCommentsForFeedCountResponse getFriendCommentsForFeedCount(
        GetFriendCommentsForFeedCountRequest req) throws TException {

        GetFriendCommentsForFeedCountResponse resp =
                new GetFriendCommentsForFeedCountResponse();

        long start = System.nanoTime();

        if (req.getInfos() == null || req.getInfos().size() == 0) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID, "feedCommentInfos is null"));
        }

        if (req.getInfos().size() > MAX_FEED_QUERY_LIMIT) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.COMMENT_PARAMS_INVALID,
                "too many feedCommentInfos"));
        }

        CommentStrategy strategy =
                new CommentStrategy().setReturnFullHeadUrl(true);

        List<com.renren.ugc.comment.model.FeedCommentInfo> internalInfos =
                new ArrayList<com.renren.ugc.comment.model.FeedCommentInfo>(
                    req.infos.size());
        for (FeedCommentInfo info : req.getInfos()) {
            internalInfos.add(new com.renren.ugc.comment.model.FeedCommentInfo(
                info));
        }

        boolean success = false;
        // commentType没有用到，这里的作用仅仅是通过{@link: CommentLogicInvocationHandler}的校验,
        // 因为CommentLogicInvocationHandler强制要求地一个方法调用的参数是comment type
        CommentType fakeType = CommentType.Dummy;
        try {
            Map<Long, Integer> commentMap =
                    commentCenter.getFriendCommentsForFeedCount(fakeType,
                        internalInfos, strategy);

            resp.setResults(commentMap);
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
                "Unknown comment error:" + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetFriendCommentsForFeedCount(execTime, success);

        return resp;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.renren.ugc.comment.xoa2.IXCommentCenter#removeGlobalComment(com.renren
     * .ugc.comment.xoa2.RemoveGlobalCommentRequest)
     */
    @Override
    public RemoveGlobalCommentResponse removeGlobalComment(
        RemoveGlobalCommentRequest req) {
        long start = System.nanoTime();

        RemoveGlobalCommentResponse resp = new RemoveGlobalCommentResponse();

        CommentType type = req.getType();
        if (type == null) {
            return resp.setBaseRep(getBaseResponse(
                CommentError.INVALID_COMMENT_TYPE,
                CommentError.INVALID_COMMENT_TYPE_MSG));
        }

        CommentStrategy strategy = new CommentStrategy();
        strategy = prepareParamsForType(type, strategy, req.getParams());

        boolean success = false;
        try {

            boolean removed =
                    commentCenter.removeGlobalComment(type, req.getActorId(),
                        req.getEntryId(), req.getEntryOwnerId(),
                        req.getCommentId(), strategy);

            if (removed == true) {
                resp.setRemoved(removed);
            } else {
                resp.setBaseRep(getBaseResponse(
                    CommentError.COMMENT_NOT_EXISTS,
                    CommentError.COMMENT_NOT_EXISTS_MSG));
            }
            success = true;

        } catch (UGCCommentException e) {
            logger.error(e.getMessage(), e);
            resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            resp.setBaseRep(getBaseResponse(
                CommentError.UNKNOWN_ERROR,
                "Unknown error when removing comment:" + type + " "
                        + e.getMessage()));
        }

        long end = System.nanoTime();
        long execTime = (end - start) / NANO_TO_MILLIS;

        StatisticsHelper.invokeRemoveComment(execTime, success);
        AccessLogger.logRemoveComment(
            type,
            req.getActorId(),
            req.getEntryId(),
            req.getEntryOwnerId(),
            req.getCommentId(),
            execTime,
            success ? CommentError.SUCCESS : resp.getBaseRep().getErrorInfo().getCode());

        return resp;
    }



	@Override
	public GetCommentListWithFilterResponse getCommentListWithFilter(
			GetCommentListWithFilterRequest req) throws TException {
		long start = System.nanoTime();
		// 根据authrId过滤
		int actorId = req.getActorId();
		int entryOwnerId = req.getEntryOwnerId();
		long entryId = req.getEntryId();

		// start
		GetCommentListWithFilterResponse response = new GetCommentListWithFilterResponse();
		int queryLimit = req.getLimit();
		if (queryLimit <= 0 || queryLimit > MAX_QUERY_LIMIT) {
			queryLimit = DEFAULT_QUERY_LIMIT;
		}
		// set query limit = limit + 1 to support "has more"
		CommentStrategy strategy = new CommentStrategy()
				.setQueryLimit(queryLimit + 1)
				.setQueryOrder(req.isDesc() ? QueryOrder.DESC : QueryOrder.ASC)
				.setFilterWhisper(true).setOriginalQueryLimit(queryLimit);
		
		 // Paging, if the border comment id is set
        if (req.isSetBorderCommentId()) {
            strategy.setQueryBorderID(req.getBorderCommentId());
        }
        
		if (req.isSetOffset()) {
            strategy.setQueryOffset(req.getOffset());
        }
		
		if (req.isSetPageNum() && req.getPageNum() > 0) {
			int pageNum = req.getPageNum();
			// if (pageNum < 1) pageNum = 1;
			strategy.setQueryOffset((pageNum - 1) * queryLimit);
			strategy.setPageNum(pageNum);
		}
		CommentType type = req.getType();

		int authorId = req.getAuthorId() == 0 ? entryOwnerId : req
				.getAuthorId();
		strategy = prepareParamsForType(type, strategy, req.getParams());
		boolean success = true;
		try {
			CommentListResult result = commentCenter.getCommentListWithFilter(
					type, actorId, entryId, entryOwnerId, authorId, strategy);
			response.setMore(result.isHasMore());
			response.setCommentList(CommentConvertUtils.toXoa2CommentList(
					result.getCommentLists(), strategy.isNeedMetadata()));
			//  返回总数
			response.setTotalCount(commentCenter.getCountWithFilter(type,
					actorId, entryId, entryOwnerId, authorId, strategy));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			success = false;
			logger.error(e.getMessage(), e);
			response.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
					"Unknown comment error:" + type + " " + e.getMessage()));
		}
		
        long execTime = (System.nanoTime() - start) / NANO_TO_MILLIS;
        StatisticsHelper.invokeGetCommentListWithFilter(execTime, success);
        AccessLogger.logRemoveComment(
                type,
                req.getActorId(),
                req.getEntryId(),
                req.getEntryOwnerId(),
                0,
                execTime,
                success ? CommentError.SUCCESS : response.getBaseRep().getErrorInfo().getCode());

		return response;
	}

	/**
	 * 批量创建评论接口
	 */
	@Override
	public CreateCommentByListResponse createCommentByList(
			CreateCommentByListRequest req) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoke comment center xoa2 api " + req.toString());
		}
		long start = System.nanoTime();
		CreateCommentByListResponse resp = new CreateCommentByListResponse();

		CommentType type = req.getType();

		// 一系列参数检查以及commentinit(检查actorId,replayId，创建commentList，根据参数判断是否是悄悄话等)		
		ForInvokeStrategy invokeStraList = new ForInvokeStrategy();
		List<CommentPackage> packageList = new ArrayList<CommentPackage>();
		for (MultiParamForCreate multiParam : req.getMutiParam()) {
			com.renren.ugc.comment.model.Comment comm = new com.renren.ugc.comment.model.Comment();
			ForCommentStrategy forCommentStrategy = new ForCommentStrategy();
			if (multiParam.getReplyToId() < 0 || multiParam.getActorId()<0) {
				return resp.setBaseRep(getBaseResponse(CommentError.COMMENT_PARAMS_INVALID,
						"Params invalid.ReplyToId" + ":["+ multiParam.getReplyToId() + "]."+
						"Params invalid.ActorToId" + ":["+ multiParam.getActorId() + "]."));
			}
			comm.setContent(multiParam.getContent());
			if (multiParam.getReplyToId() != 0) {
				comm.setToUserId(multiParam.getReplyToId());
				comm.setToCommendId(multiParam.getToCommentId());
				// 悄悄话
				if (multiParam.isWhisper()) {
					comm.setWhipserToId(multiParam.getReplyToId());
				}
			} else {
				// 悄悄话
				if (multiParam.isWhisper()) {
					comm.setWhipserToId(req.getEntryOwnerId());
				}
			}
			Map<String, String> params = multiParam.getParams();
			if (type != null && params != null) {
				 // first save the params
				forCommentStrategy.setParams(params);
		        // 利用params的参数来重置comment config里的配置
				forCommentStrategy.setByParams(params);
	        }

	       
			CommentPackage commentPackage = new CommentPackage();
			commentPackage.setComment(comm);
			commentPackage.setActorId(multiParam.getActorId());
			commentPackage.setForCommentStrategy(forCommentStrategy);
			packageList.add(commentPackage);
		}
		
		invokeStraList.setPackageList(packageList);
		if (packageList.size() <= 0) {
			resp.setBaseRep(getBaseResponse(
					CommentError.COMMENT_PARAMS_INVALID,
					"comment list count is 0"));
		}

		// 调用核心方法并设置返回值
		boolean success = false;
		try {
			List<CommentPackage> createdComment = commentCenter
					.createByList(type,  req.getEntryId(),
							req.getEntryOwnerId(), invokeStraList);
			List<com.renren.ugc.comment.xoa2.Comment> xoaCommentList = new ArrayList<com.renren.ugc.comment.xoa2.Comment>();

			for (CommentPackage onepackage : createdComment) {
				xoaCommentList.add(CommentConvertUtils.toXoa2Comment(onepackage
						.getComment(), onepackage.getForCommentStrategy()
						.isNeedMetadata()));
				if (null != resp.getEntry()
						&& onepackage.getForCommentStrategy().getEntry() != null) {
					resp.setEntry(CommentConvertUtils.toXoa2Entry(onepackage.getForCommentStrategy()
							.getEntry()));
				}
			}
			resp.setCommentList(xoaCommentList);

			success = true;
		} catch (UGCCommentException e) {
			resp.setBaseRep(getBaseResponse(e.getErrorCode(), e.getMessage()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setBaseRep(getBaseResponse(CommentError.UNKNOWN_ERROR,
					"Unknown comment error:" + type + " " + e.getMessage()));
		}
		long end = System.nanoTime();
		long execTime = (end - start) / NANO_TO_MILLIS;

		// do statistics
		StatisticsHelper.invokeCreateCommentByList(execTime, success);
		AccessLogger.logCreateCommentByList(type, req.getEntryId(),
				req.getEntryOwnerId(), execTime, success ? CommentError.SUCCESS
						: resp.getBaseRep().getErrorInfo().getCode());

		return resp;
	}

	@Override
	public GetFeedsCommonCommentsResponse getFeedsCommonComments(
			GetFeedsCommonCommentsRequest req) {
		// TODO Auto-generated method stub
		return null;
	}


}
