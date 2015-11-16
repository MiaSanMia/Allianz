package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import com.renren.photo.xoa.api.service.AlbumService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.ugc.model.album.Album;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.client.ServiceFactory;

public class AlbumEntryService implements EntryGetService {

    private static final Logger logger = Logger.getLogger(AlbumEntryService.class);

    private static AlbumEntryService instance = new AlbumEntryService();

    public static AlbumEntryService getInstance() {
        return instance;
    }

    private final int XOA_TIMEOUT = 700;

    //private IBlogService blogService = null;

    private AlbumService albumServiceXOA1 = null;

    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    public static <T> T getServiceXOA1(final Class<T> serviceClass) {
        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
        return fact.getService(serviceClass);
    }

    private AlbumEntryService() {
        //blogService = getServiceXOA2(IBlogService.class, XOA_TIMEOUT);
        albumServiceXOA1 = getServiceXOA1(AlbumService.class);
    }

    @Override
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException {

        //让前端改
        if (actorId == 0) {
            actorId = entryOwnerId;
        }

        long start = System.nanoTime();
        boolean success = false;
//        ServiceFuture<Album> albumFuture = albumServiceXOA1.getParentOrChildAlbumNoValidate(actorId,
//                entryOwnerId, entryId);
        String password = StringUtils.isNotEmpty(strategy.getEntryPassword()) ? strategy.getEntryPassword() : "";
        ServiceFuture<Album> albumFuture = albumServiceXOA1.getAlbumWithChildId(actorId, entryOwnerId, entryId, password, false);

        XoaBizErrorListener bloglistener = new XoaBizErrorListener();
        albumFuture.addListener(bloglistener);
        albumFuture.submit();
        try {
            boolean ret = albumFuture.await(XOA_TIMEOUT);
            if (ret) {
                success = true;
                if (albumFuture.isSuccess()) {
                    if (albumFuture.getContent() == null) {
                        logger.error("getEntryInfo is null|type:album|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
                        throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                                "blog not exist");
                    } else {
                        // logger.debug("blog id is:" + albumFuture.getContent().getBlogId());
                        return Transfer2EntryUtil.albumTransfer2EntryUtil(albumFuture.getContent(),
                                strategy.getEntry(), entryId, entryOwnerId,comment,strategy);
                    }

                } else {
                    XoaBizErrorBean errorBean = bloglistener.getReturn();
                    if (errorBean != null) {
                        logger.error("getEntryInfo return error|type:album|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId + "|msg:"
                                + errorBean.getMessage());
                      //针对隐私做特殊处理
                        if(errorBean.getErrorCode() == 401){
                            throw new UGCCommentException(CommentErrorCode.PROTECTED_SOURCE,
                                    "album protected source");
                        } else {
                            throw new UGCCommentException(errorBean.getErrorCode(),
                                    "ugcCheckAlbumProtectedError:" + errorBean.getMessage());
                        }
                    }
                }
            } else {
                //超时啦,优先保证服务质量，评论中心继续工作
                logger.error("getEntryInfo timeout error|type:album|actorId:" + actorId
                        + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
                //               throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT,
                //                       "get blog timeout");
            }
        } catch (UGCCommentException ue) {
            throw ue;
        } catch (InterruptedException e) {
            // 服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo interrupt|type:album|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
            //throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        } catch (Exception e) {
            // 服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo error|type:album|actorId:" + actorId + "|entryId:" + entryId
                    + "|entryOwner:" + entryOwnerId, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeGetAlbum((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);

        return null;
    }

}
