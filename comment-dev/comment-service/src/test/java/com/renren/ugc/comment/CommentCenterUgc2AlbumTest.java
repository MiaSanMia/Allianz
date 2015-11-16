package com.renren.ugc.comment;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.xoa2.Comment;
import com.renren.ugc.model.blog.CommentListView;
import com.renren.ugc.model.blog.Entry;
import com.renren.xoa.commons.bean.XoaBizErrorBean;
import com.renren.xoa.lite.ServiceFactories;
import com.renren.xoa.lite.ServiceFuture;
import com.renren.xoa.lite.impl.listener.XoaBizErrorListener;
import com.renren.xoa2.api.ugc.status.model.Doing;
import com.renren.xoa2.api.ugc.status.model.DoingHtmlType;
import com.renren.xoa2.api.ugc.status.model.GetDoingByIdRequest;
import com.renren.xoa2.api.ugc.status.model.GetDoingByIdResponse;
import com.renren.xoa2.api.ugc.status.model.GetDoingControlParam;
import com.renren.xoa2.api.ugc.status.service.IStatusService;
import com.renren.xoa2.client.ServiceFactory;
import com.renren.app.blog.xoa.api.BlogCommentService;
import com.renren.app.blog.xoa.api.BlogService;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;


public class CommentCenterUgc2AlbumTest {
    
    Logger logger = Logger.getLogger(this.getClass());
	private static BlogCommentService blogCommentService = null;
    
    @BeforeClass
    public static void setUp() {
    	com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
    	blogCommentService = fact.getService(BlogCommentService.class);
    }
    
//    @Test
//    public void testUgcStatusComment() {
//        
//        long doingId = 4645567583L; // 4628714779L; 
//        int ownerId = 476208839;
//        int actorId = 361463487;
//        String userIp = "10.3.21.54";
//        Comment comm = new Comment();
//        String content = "this is a test status comment";
//        comm.setContent(content);
//        Entry entry = new Entry();
//        entry.setId(doingId);
//        entry.setOwnerId(ownerId);
//        comm.setEntry(entry);
//        comm.setWhipserId(0);
//        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true)
//        .setFeedType(217).setSourceId(576928345L).setUserIp(userIp).setQueryOffset(1).setQueryLimit(30);
//
//        logger.debug("------------------create a status comment-------------");
//        Comment result = CommentCenter.getInstance().create(CommentType.Status, actorId, doingId,
//                ownerId, comm, strategy);
//        long commentId = result.getId();
//        logger.debug("statusCommentId:" + CommentBusIDentifier.getInstance().reverseGenCommentId(commentId));
//        assertEquals(content, result.getContent());
//        assertEquals(entry.getId(), result.getEntry().getId());
//        assertEquals(entry.getOwnerId(), result.getEntry().getOwnerId());
//        
//        logger.debug("------------------get a status list comment-------------");
//        List<Comment> list = CommentCenter.getInstance().getListByEntry(CommentType.Status, actorId, doingId, ownerId, strategy);
//        Assert.assertTrue("list contain a comment", list.contains(commentId));
//        
//        logger.debug("--------------------delete a status comment---------");
//        boolean flag = CommentCenter.getInstance().remove(CommentType.Status, actorId, doingId, ownerId, commentId, strategy);
//        Assert.assertTrue(" delete doingId comment", flag);
//        
//    }
//    
//    @Test
//    public void testUgcShareComment() {
//        String userIp = "10.3.25.26";
//        long shareId = 15462362661L; 
//        int ownerId = 476208839;
//        int actorId = 361463487;
//        
//        Comment comm = new Comment();
//        String content = "this is a test share comment";
//        comm.setContent(content);
//        Entry entry = new Entry();
//        entry.setId(shareId);
//        entry.setOwnerId(ownerId);
//        comm.setEntry(entry);
//        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true)
//        .setFeedType(217).setSourceId(576928345L).setUserIp(userIp).setQueryOffset(1).setQueryLimit(30);
//        logger.debug("------------------create a share comment-------------");
//        Comment result = CommentCenter.getInstance().create(CommentType.Share, actorId, shareId,
//                ownerId, comm, strategy);
//        long commentId = result.getId();
//        logger.debug("shareCommentId:" + CommentBusIDentifier.getInstance().reverseGenCommentId(commentId));
//        assertEquals(content, result.getContent());
//        assertEquals(entry.getId(), result.getEntry().getId());
//        assertEquals(entry.getOwnerId(), result.getEntry().getOwnerId());
//        
//        logger.debug("------------------get a share list comment-------------");
//        List<Comment> list = CommentCenter.getInstance().getListByEntry(CommentType.Share, actorId, shareId, ownerId, strategy);
//        Assert.assertTrue("list contain a comment", list.contains(commentId));
//        
//        logger.debug("--------------------delete a share comment---------");
//        boolean flag = CommentCenter.getInstance().remove(CommentType.Share, actorId, shareId, ownerId, commentId, strategy);
//        Assert.assertTrue(" delete share comment", flag);
//    }
//
//    @Test
//    public void testUgcBlogComment() {
//        String userIp = "10.3.25.26";
//        long blogId = 897465033L; 
//        int ownerId = 476208839;
//        int actorId = 361463487;
//        
//        Comment comm = new Comment();
//        String content = "this is a test share comment";
//        comm.setContent(content);
//        Entry entry = new Entry();
//        entry.setId(blogId);
//        entry.setOwnerId(ownerId);
//        comm.setEntry(entry);
//        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true)
//        .setFeedType(217).setSourceId(576928345L).setUserIp(userIp).setQueryOffset(1).setQueryLimit(30);
//        logger.debug("------------------create a share comment-------------");
//        Comment result = CommentCenter.getInstance().create(CommentType.Blog, actorId, blogId,
//                ownerId, comm, strategy);
//        long commentId = result.getId();
//        logger.debug("blogCommentId:" + CommentBusIDentifier.getInstance().reverseGenCommentId(commentId));
//        assertEquals(content, result.getContent());
//        assertEquals(entry.getId(), result.getEntry().getId());
//        assertEquals(entry.getOwnerId(), result.getEntry().getOwnerId());
//        
//        logger.debug("------------------get a blog list comment-------------");
//        List<Comment> list = CommentCenter.getInstance().getListByEntry(CommentType.Blog, actorId, blogId, ownerId, strategy);
//        Assert.assertTrue("list contain a comment", list.contains(commentId));
//        
//        logger.debug("--------------------delete a blog comment---------");
//        boolean flag = CommentCenter.getInstance().remove(CommentType.Blog, actorId, blogId, ownerId, commentId, strategy);
//        Assert.assertTrue(" delete blog comment", flag);
//    }
////    
////    
//    @Test
//    public void testAlbumComment() {
//        String userIp = "10.3.25.26";
//        int visitor = 361463487;
//        int ownerId = 476208839;
//        int albumId = 863487477;
//        
//        Comment comm = new Comment();
//        String content = "this is a test album comment";
//        comm.setContent(content);
//        Entry entry = new Entry();
//        entry.setId(albumId);
//        entry.setOwnerId(ownerId);
//        comm.setEntry(entry);
//        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true)
//        .setFeedType(217).setSourceId(576928345L).setUserIp(userIp).setQueryOffset(1).setQueryLimit(30);
//        logger.debug("------------------create a album comment-------------");
//        Comment result = CommentCenter.getInstance().create(CommentType.Album, visitor, albumId,
//                ownerId, comm, strategy);
//        long commentId = result.getId();
//        logger.debug("blogCommentId:" + CommentBusIDentifier.getInstance().reverseGenCommentId(commentId));
//        assertEquals(content, result.getContent());
//        assertEquals(entry.getId(), result.getEntry().getId());
//        assertEquals(entry.getOwnerId(), result.getEntry().getOwnerId());
//        
//        logger.debug("------------------get a album list comment-------------");
//        List<Comment> list = CommentCenter.getInstance().getListByEntry(CommentType.Album, visitor, albumId, ownerId, strategy);
//        Assert.assertTrue("list contain a comment", list.contains(commentId));
//        
//        logger.debug("--------------------delete a album comment---------");
//        boolean flag = CommentCenter.getInstance().remove(CommentType.Album, visitor, albumId, ownerId, commentId, strategy);
//        Assert.assertTrue(" delete album comment", flag);
//    }
////    
////    
//    @Test
//    public void testPhotoComment() {
//        String userIp = "10.3.25.26";
//        long photoId = 7019857420L;
//        int ownerId = 476208839;
//        int visitor = 361463487;
//        
//        Comment comm = new Comment();
//        String content = "this is a test photo comment";
//        comm.setContent(content);
//        Entry entry = new Entry();
//        entry.setId(photoId);
//        entry.setOwnerId(ownerId);
//        comm.setEntry(entry);
//        CommentStrategy strategy = new CommentStrategy().setSendNotice(true).setReplaceUbb(true)
//        .setFeedType(217).setSourceId(576928345L).setUserIp(userIp).setQueryOffset(1).setQueryLimit(30);
//        logger.debug("------------------create a photo comment-------------");
//        Comment result = CommentCenter.getInstance().create(CommentType.Photo, visitor, photoId,
//                ownerId, comm, strategy);
//        long commentId = result.getId();
//        logger.debug("blogCommentId:" + CommentBusIDentifier.getInstance().reverseGenCommentId(commentId));
//        assertEquals(content, result.getContent());
//        assertEquals(entry.getId(), result.getEntry().getId());
//        assertEquals(entry.getOwnerId(), result.getEntry().getOwnerId());
//        
//        logger.debug("------------------get a photo list comment-------------");
//        List<Comment> list = CommentCenter.getInstance().getListByEntry(CommentType.Photo, visitor, photoId, ownerId, strategy);
//        Assert.assertTrue("list contain a comment", list.contains(commentId));
//        
//        logger.debug("--------------------delete a photo comment---------");
//        boolean flag = CommentCenter.getInstance().remove(CommentType.Photo, visitor, photoId, ownerId, commentId, strategy);
//        Assert.assertTrue(" delete album comment", flag);
//    }
//    
    
//    @Test
//    public void findStatusByIdTest() {
//    		int actorId = 361463487;
//           long entryId = 4645567583L; 
//           int entryOwnerId = 361463487;
// 
//			GetDoingByIdRequest req = new GetDoingByIdRequest();
//			req.setUserId(entryOwnerId);
//			req.setDoingId(entryId);
//			GetDoingControlParam getDoingControlParam = new GetDoingControlParam();
//			//getDoingControlParam.setHtmlType(DoingHtmlType.HTML_NORMAL);
//			req.setGetDoingControlParam(getDoingControlParam );
//			// 判断返回值是否是个null ，xoa2不支持返回null，所以会返回一个empty对象,这样就不能使用这个对象了
//			IStatusService statusService = ServiceFactory.getService(IStatusService.class);
//			GetDoingByIdResponse response = statusService.getDoingById(req);
//			if(response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null)
//				System.out.println("error:"+response.getBaseRep().getErrorInfo().getCode()
//						+response.getBaseRep().getErrorInfo().getMsg());
//			else{
//			Doing doingById = response.getDoing();
//			System.out.println(doingById.getContent());
//			System.out.println(String.format("[id:%d,userId:%d]",
//					doingById.getId(), doingById.getUserId()));
//    	}
//
//    }
    
//    @Test
//    public void findBlogByIdTest() {
//    	//查看日志是否存在，由于listcomment没有判断是否存在
//    	int actorId = 361463487;
//    	int entryOwnerId = 476208839;
//    	long entryId = -1;
//        com.renren.xoa.lite.ServiceFactory fact = ServiceFactories.getFactory();
//        BlogService blogService =fact.getService(BlogService.class);
//    	ServiceFuture<Entry> blogFuture = blogService.getEntryById(actorId, entryOwnerId, entryId);
//    	XoaBizErrorListener bloglistener = new XoaBizErrorListener();
//        blogFuture.addListener(bloglistener);
//        blogFuture.submit();
//        try {
//        	blogFuture.await(5000);
//             if (blogFuture.isSuccess()) {
//            	 if (blogFuture.getContent() == null) {
//            		 throw new UGCCommentException("blog not exist", CommentErrorCode.ENTRY_NOT_EXIST);
//            	 }
//             } else {
//                 XoaBizErrorBean errorBean = bloglistener.getReturn();
//                 if (errorBean != null) {
//                     throw new UGCCommentException("ugcCheckBlogProtectedError:" + errorBean.getMessage(),errorBean.getErrorCode());
//                 }
//             }
//        } catch (InterruptedException e) {
//     	   throw new UGCCommentException("service timeout:", CommentErrorCode.SERVICE_TIMEOUT );
//        }
//    }
    
    @Test
    public void testBlogComment() {
    	  int actorId = 361463487;
    	  long entryId = 897465033;
    	  int entryOwnerId = 476208839;
//    	  CommentListView commentListView = new CommentListView();
//          commentListView.setAuthor(actorId);   //回复日志的评论者
//          commentListView.setEntry(entryId);   //日志id
//          commentListView.setOwner(entryOwnerId);  //日志作者
//          String content = "学潮";
//          ServiceFuture<CommentListView> serviceFuture = blogCommentService.addComment(actorId, entryOwnerId, entryId, commentListView);
//          commentListView.setBody(content);      //评论内容
//         
//          XoaBizErrorListener bizErrorListener = new XoaBizErrorListener();
//          serviceFuture.addListener(bizErrorListener);
//          serviceFuture.submit();
//          CommentListView view = null;
//          try {
//              if (serviceFuture.await(10000)) {
//                  if (serviceFuture.isSuccess()) {
//                      view = serviceFuture.getContent();
//                  } else {
//                      XoaBizErrorBean errorBean = bizErrorListener.getReturn();
//                      if (errorBean != null) {
//                    	  System.out.println("msg:" + errorBean.getMessage()+",code:"+errorBean.getErrorCode());
//                          throw new UGCCommentException("createUgcBlogCommentError:" + errorBean.getMessage(),errorBean.getErrorCode());
//                      }
//                  }
//              }
//          } catch (InterruptedException e) {
//          	throw new UGCCommentException("service timeout:", CommentErrorCode.SERVICE_TIMEOUT );
//
//          }
//          System.out.println(view.getBody());
          
          
          ServiceFuture<CommentListView[]> f = blogCommentService.getCommentListViews(actorId, entryOwnerId, entryId, 0, 100);
          List<Comment> list = new ArrayList<Comment>();
         XoaBizErrorListener listener = new XoaBizErrorListener();
         f.addListener(listener);
         f.submit();
         try {
              f.await(5000);
              if (f.isSuccess()) {
                  CommentListView[] listViews = f.getContent();
                  System.out.println("size:" + listViews.length);
              } else {
                  XoaBizErrorBean errorBean = listener.getReturn();
                  if (errorBean != null) {
                      throw new UGCCommentException(errorBean.getErrorCode(),"ugcBlogGetListError:" + errorBean.getMessage());
                  }
              }
         } catch (InterruptedException e) {
      	   throw new UGCCommentException(CommentErrorCode.SERVICE_TIMEOUT, "service timeout:" );
         }
    }
}