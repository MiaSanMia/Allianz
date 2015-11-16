package com.renren.ugc.comment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.renren.photo.xoa.api.service.PhotoCommentService;
import com.renren.photo.xoa.api.service.PhotoService;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.model.album.Photo;
import com.renren.ugc.model.album.PhotoComment;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFactory;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.ServiceFutureHelper;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;

import junit.framework.TestCase;

public class PhotoCommentServiceTest extends TestCase {

    private PhotoCommentService photoCommentService;
    private PhotoService photoService;
    
    private static final Log logger = LogFactory.getLog(PhotoCommentServiceTest.class);

    long photoId = 7019855890l;
    int ownerId = 476208839;
    int visitor = 361463487;
    int timeout = 10000;
    @Override
    protected void setUp() throws Exception {
        final ServiceFactory fact = ServiceFactories.getFactory();
        photoCommentService = fact.getService(PhotoCommentService.class);
        photoService = fact.getService(PhotoService.class);
    }

    @Override
    protected void tearDown() throws Exception {
    }

//    @Test
//    public void testGetPhotoComment() {
//        final ServiceFuture<PhotoComment> serviceFuture = photoCommentService.getPhotoComment(visitor, ownerId, photoId);
//        final PhotoComment c = ServiceFutureHelper.execute(serviceFuture, timeout);
//        System.out.println(c.getBody());
//    }

//    @Test
//    public void testGetPhotoCommentsCount() {
//        final ServiceFuture<Integer> serviceFuture = photoCommentService.getPhotoCommentsCount(visitor, ownerId, photoId);
//        final int i = ServiceFutureHelper.execute(serviceFuture, timeout);
//        System.out.println(i);
//    }
//
//    @Test
//    public void testGetPhotoCommentsCountByOwner() {
//        final ServiceFuture<Integer> serviceFuture = photoCommentService.getPhotoCommentsCountByOwner(visitor, ownerId);
//        final int i = ServiceFutureHelper.execute(serviceFuture, timeout);
//        System.out.println(i);
//    }
//
    @Test
    public void testGetPhotoComments() {
        checkProtected(visitor, ownerId, photoId);
    	logger.debug("-----------get list photo comments-----------");
        final ServiceFuture<PhotoComment[]> serviceFuture = photoCommentService.getPhotoComments(visitor, ownerId, photoId, 0, 10, "",
                false);
        final PhotoComment[] pcs = ServiceFutureHelper.execute(serviceFuture, timeout);
        for (final PhotoComment pc : pcs) {
        	logger.debug("get list photo comments:"+ pc.getOriginalBody()+",id:"+pc.getId());
        }
    }
//
//    @Test
//    public void testGetPhotoComments4Feed() {
//        final ServiceFuture<String> serviceFuture = photoCommentService.getPhotoComments4Feed(visitor, ownerId, photoId, 0, 10, "",
//                true);
//        final String s = ServiceFutureHelper.execute(serviceFuture, timeout);
//        System.out.println(s);
//    }

    @Test
    public void testAddPhotoComment() {
        checkProtected(visitor, ownerId, photoId);
    	logger.debug("---------add photo comments-----------");
        final PhotoComment photoComment = new PhotoComment();
        photoComment.setBody("renrenwang,I love哈哈哈");
        final ServiceFuture<String> serviceFuture = null; //photoCommentService.addPhotoComment(visitor, ownerId, photoComment, photoId, true, 0);
        final String s = ServiceFutureHelper.execute(serviceFuture, timeout);
        logger.debug("add photo comment results:" + s);
    }
//
//    @Test
//    public void testDeletePhotoComment() {
//    	logger.debug("--------delete photo comments ------------");
//        final ServiceFuture<String> serviceFuture = photoCommentService.deletePhotoComment(visitor, ownerId, 7632194570L);
//        final String s = ServiceFutureHelper.execute(serviceFuture, timeout);
//        logger.debug("delete photo comments result:" + s);
//    }

//    @Test
//    public void testGetFirstAndLastComment() {
//        final ServiceFuture<PhotoComment[]> serviceFuture = photoCommentService.getFirstAndLastComment(visitor, ownerId, photoId);
//        final PhotoComment[] pcs = ServiceFutureHelper.execute(serviceFuture, timeout);
//        for (final PhotoComment pc : pcs) {
//            System.out.println(pc.getBody());
//        }
//    }
    
//    @Test
//    public void testPhotoById() {
//    	int entryOwnerId =  510785309;
//    	long entryId = 7065470766l;
//    	int actorId = 476208839;
//    	checkProtected(actorId, entryOwnerId, entryId);
//    }
//
	 private void checkProtected(int actorId,int entryOwnerId, long entryId) {
	    	ServiceFuture<Photo> future = photoService.getPhoto(actorId, entryOwnerId, entryId, "", false);
	    	XoaBizErrorListener photoListener = new XoaBizErrorListener();
	        future.addListener(photoListener);
	        future.submit();
	        try {
	             future.await(5000);
	             if (future.isSuccess()) {
	            	 Photo photo = future.getContent();
	            	 if (photo == null) {
	            		 throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST, "photo not exitstes");
	            	 }
	             } else {
	                 XoaBizErrorBean errorBean = photoListener.getReturn();
	                 if (errorBean != null) {
	                     throw new UGCCommentException(errorBean.getErrorCode(),"ugcPhotoGetError:" + errorBean.getMessage());
	                 }
	             }
	        } catch (InterruptedException e) {
	        	throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:" );
	        }
	    }

}
