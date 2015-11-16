package com.renren.ugc.comment;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renren.app.blog.xoa.api.BlogCommentService;
import com.renren.app.blog.xoa.api.BlogService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.QueryOrder;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.blog.CommentListView;
import com.renren.ugc.model.blog.Entry;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.api.ugc.status.service.IStatusCommentService;
import com.renren.xoa2.client.ServiceFactory;

public class BlogCommentTest {

    //状态id
    long doingId = 4832356754l;

    //userid
    int replyerId = 508224259;

    //hostid
    int ownerId = 374180476;

    long blogId = 906413323;

    long shareId = 15462362661L;

    IStatusCommentService statusCommentService;

    static BlogCommentService blogCommentService = null;

    static BlogService blogService = null;

    private static final Logger logger = Logger.getLogger(BlogCommentTest.class);

    private static final int TIMEOUT = 1000;

    public static <T> T getService(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }

    @BeforeClass
    public static void init() {

        System.setProperty("xoa.hosts.blog.ugc.xoa.renren.com", "10.4.24.19:7388");
        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();

        blogCommentService = fact.getService(BlogCommentService.class);
        blogService = fact.getService(BlogService.class);

    }

    @Test
    public void getCommentListTest() throws UGCCommentException {
        CommentType commentType = CommentType.Blog;
        int actorId = 361463487;
        long entryId = 906340529;
        int entryOwnerId = 476208839;
        int queryOffset = 0;
        int limit = 10;
        CommentStrategy strategy = new CommentStrategy();
        strategy.setQueryOrder(QueryOrder.ASC);
        System.out.println(strategy.getQueryOrder().toString().toLowerCase());
        System.out.println(strategy.getQueryOrder() == QueryOrder.DESC);
        checkProtected(actorId, entryOwnerId, entryId);
        ServiceFuture<CommentListView[]> f = blogCommentService.getCommentListViews(actorId,
                entryOwnerId, entryId, queryOffset, limit);
        f.setParam("desc", false);
        XoaBizErrorListener listener = new XoaBizErrorListener();
        f.addListener(listener);
        f.submit();
        try {
            f.await(TIMEOUT);
            if (f.isSuccess()) {
                CommentListView[] listViews = f.getContent();
                for (CommentListView listView : listViews) {
                    logger.info("id:" + listView.getId() + ",content:" + listView.getBody());
                }
            } else {
                XoaBizErrorBean errorBean = listener.getReturn();
                if (errorBean != null) {
                    throw new UGCCommentException(errorBean.getErrorCode(), "ugcBlogGetListError:"
                            + errorBean.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        }
    }

    private void checkProtected(int actotId, int entryOwnerId, long entryId) {
        //查看日志是否存在，由于listcomment没有判断是否存在
        ServiceFuture<Entry> blogFuture = blogService.getEntryById(entryOwnerId, entryOwnerId,
                entryId);
        XoaBizErrorListener bloglistener = new XoaBizErrorListener();
        blogFuture.addListener(bloglistener);
        blogFuture.submit();
        try {
            blogFuture.await(TIMEOUT);
            if (blogFuture.isSuccess()) {
                if (blogFuture.getContent() == null) {
                    throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                            "blog not exist");
                } else {
                    logger.debug("blog id is:" + blogFuture.getContent().getBlogId());
                }
            } else {
                XoaBizErrorBean errorBean = bloglistener.getReturn();
                if (errorBean != null) {
                    throw new UGCCommentException(errorBean.getErrorCode(),
                            "ugcCheckBlogProtectedError:" + errorBean.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        }
        ServiceFuture<Entry> blogServiceFuture = blogService.getEntryById(actotId, entryOwnerId,
                entryId);
        XoaBizErrorListener blogOtherListener = new XoaBizErrorListener();
        blogServiceFuture.addListener(blogOtherListener);
        blogServiceFuture.submit();
        try {
            blogServiceFuture.await(TIMEOUT);
            if (blogServiceFuture.isSuccess()) {
                if (blogServiceFuture.getContent() == null) {
                    throw new UGCCommentException(CommentErrorCode.PROTECTED_SOURCE,
                            "blog can not read");
                } else {
                    logger.debug("blog id is:" + blogFuture.getContent().getBlogId());
                }
            } else {
                XoaBizErrorBean errorBean = blogOtherListener.getReturn();
                if (errorBean != null) {
                    throw new UGCCommentException(errorBean.getErrorCode(),
                            "ugcCheckBlogProtectedError:" + errorBean.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        }
    }

}
