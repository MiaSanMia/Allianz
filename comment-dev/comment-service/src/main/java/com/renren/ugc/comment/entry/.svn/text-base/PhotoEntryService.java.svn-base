package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import com.renren.photo.xoa.api.service.PhotoService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.ugc.model.album.Photo;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.client.ServiceFactory;

public class PhotoEntryService implements EntryGetService {

    private static final Logger logger = Logger.getLogger(PhotoEntryService.class);

    private static PhotoEntryService instance = new PhotoEntryService();

    public static PhotoEntryService getInstance() {
        return instance;
    }

    private final int XOA_TIMEOUT = 500;

    //private IBlogService blogService = null;

    private PhotoService photoServiceXOA1 = null;

    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    public static <T> T getServiceXOA1(final Class<T> serviceClass) {
        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
        return fact.getService(serviceClass);
    }

    private PhotoEntryService() {
        //blogService = getServiceXOA2(IBlogService.class, XOA_TIMEOUT);
        photoServiceXOA1 = getServiceXOA1(PhotoService.class);
    }

    @Override
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException {
        long start = System.nanoTime();
        boolean success = false;
        String password = StringUtils.isNotEmpty(strategy.getEntryPassword()) ? strategy.getEntryPassword() : "";
        ServiceFuture<Photo> shareFuture = photoServiceXOA1.getPhoto(actorId, entryOwnerId,
                entryId, password, false);

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
                        return Transfer2EntryUtil.photoTransfer2EntryUtil(shareFuture.getContent(),
                                strategy.getEntry(), strategy, entryOwnerId, entryId);
                    }
                } else {
                    XoaBizErrorBean errorBean = sharelistener.getReturn();
                    if (errorBean != null) {
                        logger.error("getEntryInfo return error|type:share|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId + "|msg:"
                                + errorBean.getMessage());
                       //针对隐私做特殊处理
                        if(errorBean.getErrorCode() == 200){
                            throw new UGCCommentException(CommentErrorCode.PROTECTED_SOURCE,
                                    "photo protected source");
                        } else {
                            throw new UGCCommentException(errorBean.getErrorCode(),
                                    "ugcCheckPhotoProtectedError:" + errorBean.getMessage());
                        }
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
            logger.error("getEntryInfo interrupt|type:photo|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
        } catch (Exception e) {
            // 服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo error|type:photo|actorId:" + actorId + "|entryId:" + entryId
                    + "|entryOwner:" + entryOwnerId, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetPhoto((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);

        return null;
    }

}
