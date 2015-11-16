package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;

import com.renren.app.share.model.Share;
import com.renren.app.share.xoa.api.ShareService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.client.ServiceFactory;

public class ShareEntryService implements EntryGetService {

    /**
     * 调用share XOA需要传的调用来源
     */
    private static final String FROM_COMMENT_CENTER = "08260400000";

    private static final Logger logger = Logger.getLogger(ShareEntryService.class);

    private static ShareEntryService instance = new ShareEntryService();

    public static ShareEntryService getInstance() {
        return instance;
    }

    private final int XOA_TIMEOUT = 500;

    //private IBlogService blogService = null;

    private ShareService shareServiceXOA1 = null;

    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    public static <T> T getServiceXOA1(final Class<T> serviceClass) {
        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
        return fact.getService(serviceClass);
    }

    private ShareEntryService() {
        //blogService = getServiceXOA2(IBlogService.class, XOA_TIMEOUT);
        shareServiceXOA1 = getServiceXOA1(ShareService.class);
    }

    @Override
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException {
        long start = System.nanoTime();
        boolean success = false;
        ServiceFuture<Share> shareFuture = shareServiceXOA1.getShare(entryOwnerId, entryId);

        shareFuture.setParam("from", FROM_COMMENT_CENTER);
        XoaBizErrorListener sharelistener = new XoaBizErrorListener();
        shareFuture.addListener(sharelistener);
        shareFuture.submit();
        try {
            boolean ret = shareFuture.await(XOA_TIMEOUT);
            if (ret) {
                success = true;
                if (shareFuture.isSuccess()) {
                    if (shareFuture.getContent() == null) {
                        logger.error("getEntryInfo is null|type:share|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
                        throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                                "share not exist");
                    } else {
                        return Transfer2EntryUtil.shareTransfer2EntryUtil(shareFuture.getContent(),
                                strategy.getEntry());
                    }
                } else {
                    XoaBizErrorBean errorBean = sharelistener.getReturn();
                    if (errorBean != null) {
                        logger.error("getEntryInfo return error|type:share|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId + "|msg:"
                                + errorBean.getMessage());
                        throw new UGCCommentException(errorBean.getErrorCode(),
                                "ugcCheckShareProtectedError:" + errorBean.getMessage());
                    }
                }
            } else {
                //超时啦,优先保证服务质量，评论中心继续工作
                logger.error("getEntryInfo timeout error|type:share|actorId:" + actorId
                        + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
            }
        } catch (UGCCommentException ue) {
            throw ue;
        } catch (InterruptedException e) {
            // 服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo interrupt|type:share|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
        } catch (Exception e) {
            // 服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo error|type:share|actorId:" + actorId + "|entryId:" + entryId
                    + "|entryOwner:" + entryOwnerId, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetShare((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);

        return null;
    }

}
