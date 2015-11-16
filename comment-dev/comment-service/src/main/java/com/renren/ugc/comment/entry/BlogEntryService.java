package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;

import com.renren.app.blog.xoa.api.BlogService;
import com.renren.ugc.comment.cache.CommentEntryCacheService;
import com.renren.ugc.comment.cache.impl.CommentEntryCacheServiceImpl;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.client.ServiceFactory;
import com.renren.xoa2.ugc.blog.BlogControlParam;
import com.renren.xoa2.ugc.blog.CommonControl;
import com.renren.xoa2.ugc.blog.GetBlogRequest;

public class BlogEntryService implements EntryGetService {

    private static final Logger logger = Logger.getLogger(BlogEntryService.class);

    private static BlogEntryService instance = new BlogEntryService();

    public static BlogEntryService getInstance() {
        return instance;
    }

    private final int XOA_TIMEOUT = 700;

    //private IBlogService blogService = null;

    private BlogService blogServiceXOA1 = null;


    private CommentEntryCacheService commentEntryCacheService = CommentEntryCacheServiceImpl.getInstance();

    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    public static <T> T getServiceXOA1(final Class<T> serviceClass) {
        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
        return fact.getService(serviceClass);
    }

    private BlogEntryService() {
        //blogService = getServiceXOA2(IBlogService.class, XOA_TIMEOUT);
        blogServiceXOA1 = getServiceXOA1(BlogService.class);
    }

    @Override
    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
            CommentStrategy strategy) throws UGCCommentException {
        //get blog by xoa1
        //让前端改
        if (actorId == 0) {
            actorId = entryOwnerId;
        }
        Entry commentEntry = null;
        ServiceFuture<com.renren.ugc.model.blog.Entry> blogFuture = blogServiceXOA1.getEntryById(
                actorId, entryOwnerId, entryId);
        blogFuture.setParam("getSummary", "true");
        blogFuture.setParam("from", "02020000003");

        XoaBizErrorListener bloglistener = new XoaBizErrorListener();
        blogFuture.addListener(bloglistener);
        blogFuture.submit();
        try {
            boolean ret = blogFuture.await(XOA_TIMEOUT);
            if (ret) {
                if (blogFuture.isSuccess()) {
                    if (blogFuture.getContent() == null) {
                        logger.error("getEntryInfo is null|type:blog|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
                        throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                                "blog not exist");
                    } else {
                        // logger.debug("blog id is:" + blogFuture.getContent().getBlogId());
                        commentEntry = Transfer2EntryUtil
                                .blogTransfer2EntryUtil(blogFuture.getContent(),
                                        strategy.getEntry(), entryOwnerId, entryId);
                        if (null != commentEntry) {
//                            commentEntryCacheService.setEntryCache(entryId, entryOwnerId,
//                                    CommentType.Blog, commentEntry);
                            return commentEntry;
                        }
                    }

                } else {
                    XoaBizErrorBean errorBean = bloglistener.getReturn();
                    if (errorBean != null) {
                        logger.error("getEntryInfo return error|type:blog|actorId:" + actorId
                                + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId + "|msg:"
                                + errorBean.getMessage());
                        //针对隐私做特殊处理
                        if(errorBean.getErrorCode() == 6010010){
                            throw new UGCCommentException(CommentErrorCode.PROTECTED_SOURCE,
                                    "blog protected source");
                        } else {
                            throw new UGCCommentException(errorBean.getErrorCode(),
                                    "ugcCheckBlogProtectedError:" + errorBean.getMessage());
                        }
                    }
                }
            } else {
                //超时啦,优先保证服务质量，评论中心继续工作
                logger.error("getEntryInfo timeout error|type:blog|actorId:" + actorId
                        + "|entryId:" + entryId + "|entryOwner:" + entryOwnerId);
                //               throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT,
                //                       "get blog timeout");
            }
        } catch (UGCCommentException ue) {
            throw ue;
        } catch (InterruptedException e) {
            // blog服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo interrupt|type:blog|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
            //throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        } catch (Exception e) {
            // blog服务如果出现问题的话，优先保证服务质量，评论中心继续工作
            logger.error("getEntryInfo error|type:blog|actorId:" + actorId + "|entryId:" + entryId
                    + "|entryOwner:" + entryOwnerId, e);
        }
        //logger.error("get Blog Entry  from cache");
        return commentEntry;
    }

    private GetBlogRequest buildBlogRequest(int actorId, int entryOwnerId, long entryId) {
        GetBlogRequest request = new GetBlogRequest();
        BlogControlParam bcp = new BlogControlParam();
        CommonControl cc = new CommonControl();
        bcp.setCommonControl(cc);
        request.setBlogControlParam(bcp);
        request.setHostId(actorId != 0 ? actorId : entryOwnerId);
        request.setBlogId(entryId);
        request.setOwnerId(entryOwnerId);
        request.setFrom("04020000002");

        return request;

    }

//    /**
//     * 从缓存里面来拿Entry
//     * 
//     * @param actorId
//     * @param entryOwnerId
//     * @param entryId
//     * @return
//     */
//    private Entry getFromEntryCache(int actorId, int entryOwnerId, long entryId) {
//        return (Entry) commentEntryCacheService.getEntryCache(entryId, entryOwnerId,
//                CommentType.Blog);
//    }

}
