package com.renren.ugc.comment.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.renren.photo.xoa.api.service.PhotoFeedService;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.model.album.PhotoComment;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFactory;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.ServiceFutureHelper;

public class Photo708FeedPublisher {

    private static final Logger logger = Logger.getLogger(Photo708FeedPublisher.class);

    private static PhotoFeedService photoFeedServiceXOA1 = null;

    private static int TIME_OUT = 1000;

    static {
        final ServiceFactory fact = ServiceFactories.getFactory();
        photoFeedServiceXOA1 = fact.getService(PhotoFeedService.class);
    }

    public static void send708Feed(int actorId, int entryOwnerId, long entryId,
            CommentStrategy commentStrategy, Comment latest, Comment oldest) {

        PhotoComment first = turnPhotoComment(oldest);
        PhotoComment last = turnPhotoComment(latest);

        long albumId = EntryConfigUtil.getLong(commentStrategy, EntryConfig.ENTRY_PARENT_ID);

        try {
            final ServiceFuture<Boolean> serviceFuture = photoFeedServiceXOA1.send708Feed(actorId,
                    entryOwnerId, entryId, albumId, first, last);
            boolean ret = ServiceFutureHelper.execute(serviceFuture, TIME_OUT);

            if (!serviceFuture.getContent() || !ret) {
                logger.error("send708feed return error|ownerId:" + entryOwnerId + "|entryId:"
                        + entryId + "|albumId:" + albumId);
            }
        } catch (Exception e) {
            logger.error("send708feed error|ownerId:" + entryOwnerId + "|entryId:" + entryId
                    + "|albumId:" + albumId ,e);
        }
    }

    private static PhotoComment turnPhotoComment(Comment comment) {

        if (comment == null) {
            return null;
        }

        PhotoComment photoComment = new PhotoComment();

        photoComment.setId(comment.getId());
        photoComment.setBody(comment.getContent());
        photoComment.setTime(new Date(comment.getCreatedTime()));
        photoComment.setName(comment.getAuthorName());
        photoComment.setAuthor(comment.getAuthorId());
        //这里要用相对路径
        photoComment.setHeadUrl(getRelativeUrl(comment.getAuthorHead()));

        return photoComment;
    }

    private static String getRelativeUrl(String url) {
        if (!url.startsWith("http://")) {
            return url;
        }

        String key = "/photos/";
        int index = url.indexOf(key);
        if (index == -1) {
            return url;
        }

        return url.substring(index + key.length(), url.length());
    }

}
