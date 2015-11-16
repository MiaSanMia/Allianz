package com.renren.ugc.comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.renren.photo.xoa.api.service.AlbumCommentService;
import com.renren.photo.xoa.api.service.AlbumService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.model.album.Album;
import com.renren.ugc.model.album.AlbumComment;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFactory;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;

public class AlbumCommentServiceTest {

    private static AlbumCommentService albumCommentService;

    private static AlbumService albumService;

    private static final Log logger = LogFactory.getLog(AlbumCommentServiceTest.class);

    private static final int TIMEOUT = 1000;

    @BeforeClass
    public static void setUp() throws Exception {
        final ServiceFactory fact = ServiceFactories.getFactory();
        albumCommentService = fact.getService(AlbumCommentService.class);
        albumService = fact.getService(AlbumService.class);
    }

    @Test
    public void getAlbumCommentListTest() throws UGCCommentException {
        CommentType commentType = CommentType.Album;
        int actorId = 361463487;
        long entryId = 863487477;
        int entryOwnerId = 476208839;
        int pageNum = 0; // photos & album's page starts with 0
        int limit = 10;
        boolean desc = true;
        checkProtected(actorId, entryOwnerId, entryId);
        ServiceFuture<AlbumComment[]> f = albumCommentService.getAlbumComments(actorId,
                entryOwnerId, entryId, pageNum, limit, desc, "");
        XoaBizErrorListener listener = new XoaBizErrorListener();
        f.addListener(listener);
        f.submit();
        try {
            f.await(TIMEOUT);
            if (f.isSuccess()) {
                AlbumComment[] acs = f.getContent();
                for (AlbumComment ac : acs) {
                    logger.debug("id:" + ac.getId() + ",content:" + ac.getBody());
                }
            } else {
                XoaBizErrorBean errorBean = listener.getReturn();
                if (errorBean != null) {
                    throw new UGCCommentException(errorBean.getErrorCode(),
                            "ugcAlbumGetListError:" + errorBean.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");

        }
    }

    private void checkProtected(int actorId, int entryOwnerId, long entryId) {
        ServiceFuture<Album> albumFuture = albumService.getAlbum(actorId, entryOwnerId, entryId,
                "", true);
        XoaBizErrorListener albumBizErrorListener = new XoaBizErrorListener();
        albumFuture.addListener(albumBizErrorListener);
        albumFuture.submit();
        try {
            if (albumFuture.await(TIMEOUT)) {
                if (albumFuture.isSuccess()) {
                    if (albumFuture.getContent() == null) {
                        throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
                                "album not exist");
                    }
                } else {
                    XoaBizErrorBean errorBean = albumBizErrorListener.getReturn();
                    if (errorBean != null) {
                        throw new UGCCommentException(errorBean.getErrorCode(), "createUgcAlbumCommentError:"
                                        + errorBean.getMessage());
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:");
        }
    }

}
