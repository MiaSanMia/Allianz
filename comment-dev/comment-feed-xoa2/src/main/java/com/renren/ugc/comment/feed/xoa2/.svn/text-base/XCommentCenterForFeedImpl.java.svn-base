package com.renren.ugc.comment.feed.xoa2;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.feed.util.CommentConvertUtils;
import com.renren.ugc.comment.service.CommentCenter;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.*;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.renren.xoa2.BaseResponse;
import com.renren.xoa2.ErrorInfo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jiankuan
 * Date: 29/11/13
 * Time: 15:07
 * The Comment Center Portal for feed
 */
public class XCommentCenterForFeedImpl implements IXCommentCenterForFeed {

    private static final int NANO_TO_MILLIS = 1000000;


    protected static final Logger logger =
            Logger.getLogger(XCommentCenterForFeedImpl.class);

    private static CommentCenter commentCenter = CommentCenter.getInstance();

    /**
     * 最大的新鲜事查询数量限制
     */
    private static final int MAX_FEED_QUERY_LIMIT = 30;

    @Override
    public PingResponse ping(PingRequest req) {
        String str = req.getContent();
        String msg =  "comment feed service is running";
        if (str != null) {
           msg = msg + ": " + str;
        }

        PingResponse resp = new PingResponse();
        resp.setContent(msg);
        return resp;
    }

    @Override
    public GetCommentsForFeedResponse getCommentsForFeed(
            GetCommentsForFeedRequest req) {

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
        
        Map<String, String> params = req.getParams();
        strategy = prepareParamsForType( strategy, params);

        List<com.renren.ugc.comment.model.FeedCommentInfo> internalInfos =
                new ArrayList<com.renren.ugc.comment.model.FeedCommentInfo>(
                        req.infos.size());
        for (FeedCommentInfo info : req.getInfos()) {
            internalInfos.add(new com.renren.ugc.comment.model.FeedCommentInfo(
                    info));
        }

        boolean success = false;
        // Use CommentType.Feed means here is a feed batch fetch operation. The
        // real types are contained in <code>FeedCommentInfo</code> objects
        CommentType fakeType = CommentType.Feed;
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
        // Use CommentType.Feed means here is a feed batch fetch operation. The
        // real types are contained in <code>FeedCommentInfo</code> objects; Besides
        // CommentCenter requires the first parameter must be <code>CommentType</code>
        CommentType fakeType = CommentType.Feed;
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

    /**
     * 异常情况下，设定错误返回类型
     *
     * @param errorCode the error code
     * @param errorMsg the error description message for the error code
     * @return the error base response object
     */
    static BaseResponse getBaseResponse(int errorCode, String errorMsg) {
        BaseResponse baseRep = new BaseResponse();

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(errorCode);
        errorInfo.setMsg(errorMsg);

        baseRep.setErrorInfo(errorInfo);

        return baseRep;
    }
    
    /**
     * prepare business related strategy according to comment type
     */
    private CommentStrategy prepareParamsForType(CommentStrategy strategy, Map<String, String> params) {
        if (params == null) {
            return strategy;
        }

        // first save the params
        strategy.setParams(params);

        // 利用params的参数来重置comment config里的配置
        strategy.setByParams(params);

        return strategy;
    }
}
