package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.xoa2.api.ugc.status.model.DoingHtmlType;
import com.renren.xoa2.api.ugc.status.model.GetDoingByIdRequest;
import com.renren.xoa2.api.ugc.status.model.GetDoingByIdResponse;
import com.renren.xoa2.api.ugc.status.model.GetDoingControlParam;
import com.renren.xoa2.api.ugc.status.service.IStatusService;
import com.renren.xoa2.client.ServiceFactory;

/**
 * @author lei.xu1
 * @since 2013-09-10
 */
public class StatusEntryService implements EntryGetService {

    private static final Logger logger = Logger.getLogger(StatusEntryService.class);

    private static StatusEntryService instance = new StatusEntryService();

    public static StatusEntryService getInstance() {
        return instance;
    }

    private final int XOA_TIMEOUT = 500;

    private IStatusService statusService = null;

    public static <T> T getService(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    private StatusEntryService() {
        statusService = getService(IStatusService.class, XOA_TIMEOUT);
    }

    @Override
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException {

        long start = System.nanoTime();
        boolean success = false;

        GetDoingByIdRequest req = new GetDoingByIdRequest();
        req.setUserId(entryOwnerId);
        req.setDoingId(entryId);
        req.setHost(actorId);

        GetDoingControlParam getDoingControlParam = new GetDoingControlParam();
        getDoingControlParam.setHtmlType(DoingHtmlType.HTML_NORMAL);
        getDoingControlParam.setNeedRoot(true);

        req.setGetDoingControlParam(getDoingControlParam);

        try {
            GetDoingByIdResponse response = statusService.getDoingById(req);
            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
                        "ugcStatusCreateError," + response.getBaseRep().getErrorInfo().getMsg());
            } else if(response.getDoing() == null) {
            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                        "status not exist");
            } 
            return Transfer2EntryUtil.statusTransfer2EntryUtil(response.getDoing(),
                    strategy.getEntry(), comment);
        } catch (UGCCommentException ue) {
            throw ue;
        } catch (Exception e) {
            logger.error("getEntryInfo error|type:status|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetStatus((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
        return null;
    }

}
