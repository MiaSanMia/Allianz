package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.xoa2.api.campus.model.From;
import com.renren.xoa2.api.campus.model.GetPostsByIdRequest;
import com.renren.xoa2.api.campus.model.GetPostsByIdResponse;
import com.renren.xoa2.api.campus.model.GetPostsControlParam;
import com.renren.xoa2.api.campus.service.IPostsService;
import com.renren.xoa2.client.ServiceFactory;

public class CampusPostEntryService implements EntryGetService{

	  private static final Logger logger = Logger.getLogger(CampusPostEntryService.class);

	    private static CampusPostEntryService instance = new CampusPostEntryService();

	    public static CampusPostEntryService getInstance() {
	        return instance;
	    }

	    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
	        return ServiceFactory.getService(serviceClass, timeout);
	    }
	    
	    private IPostsService postService ;
	    
	    private final int XOA_TIMEOUT = 500;

	    private CampusPostEntryService() {
	    	postService = getServiceXOA2(IPostsService.class, XOA_TIMEOUT);
	    }

	    @Override
	    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
	            CommentStrategy strategy) throws UGCCommentException {
	    	
	    	long start = System.nanoTime();
	        boolean success = false;

	        GetPostsByIdRequest req = new GetPostsByIdRequest();
	        req.setOwnerId(entryOwnerId);
	        req.setPostsId(entryId);
	        req.setVisitorId(actorId);
	        req.setFrom(From.COMMENT_CENTER);
	        GetPostsControlParam controlParam = new GetPostsControlParam();
			req.setGetPostsControlParam(controlParam);

	        //req.setFrom(ShortvideoFromConstants.SHORTVIDEO_FROM_COMMENT_CENTER);

	        try {
	        	GetPostsByIdResponse response = postService.getPostsById(req);
	            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
	                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
	                        "ugcVideoCreateError," + response.getBaseRep().getErrorInfo().getMsg());
	            } else if(response.getPosts() == null) {
	            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
	                        "posts not exist");
	            } 
	            return Transfer2EntryUtil.postsTransfer2EntryUtil(response.getPosts(),
	                    strategy.getEntry(), entryOwnerId,entryId);
	        } catch (UGCCommentException ue) {
	            throw ue;
	        } catch (Exception e) {
	            logger.error("getEntryInfo error|type:post|actorId:" + actorId + "|entryId:"
	                    + entryId + "|entryOwner:" + entryOwnerId, e);
	        }
	        long end = System.nanoTime();
	        StatisticsHelper.invokeGetPosts((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
	        return null;
	    }

}
