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
import com.renren.xoa2.api.campus.model.GetTopAdByIdRequest;
import com.renren.xoa2.api.campus.model.GetTopAdByIdResponse;
import com.renren.xoa2.api.campus.service.ITopAdService;
import com.renren.xoa2.client.ServiceFactory;

public class CampusTopPostEntryService implements EntryGetService{

	  private static final Logger logger = Logger.getLogger(CampusTopPostEntryService.class);

	    private static CampusTopPostEntryService instance = new CampusTopPostEntryService();

	    public static CampusTopPostEntryService getInstance() {
	        return instance;
	    }

	    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
	        return ServiceFactory.getService(serviceClass, timeout);
	    }
	    
	    private ITopAdService topAdService ;
	    
	    private final int XOA_TIMEOUT = 500;

	    private CampusTopPostEntryService() {
	    	topAdService = getServiceXOA2(ITopAdService.class, XOA_TIMEOUT);
	    }

	    @Override
	    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
	            CommentStrategy strategy) throws UGCCommentException {
	    	
	    	long start = System.nanoTime();
	        boolean success = false;

	        GetTopAdByIdRequest req = new GetTopAdByIdRequest();
	        req.setTopAdId(entryId);
	        req.setFrom(From.COMMENT_CENTER);

	        try {
	        	GetTopAdByIdResponse response = topAdService.getTopAdById(req);
	            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
	                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
	                        "ugcCommentCreateError," + response.getBaseRep().getErrorInfo().getMsg());
	            } else if(response.getTopAd() == null) {
	            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
	                        "topAd not exist");
	            } 
	            return Transfer2EntryUtil.topPostsTransfer2EntryUtil(response.getTopAd(),
	                    strategy.getEntry(), entryOwnerId,entryId, strategy);
	        } catch (UGCCommentException ue) {
	            throw ue;
	        } catch (Exception e) {
	            logger.error("getEntryInfo error|type:topPost|actorId:" + actorId + "|entryId:"
	                    + entryId + "|entryOwner:" + entryOwnerId, e);
	        }
	        long end = System.nanoTime();
	        StatisticsHelper.invokeGetTopPosts((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
	        return null;
	    }

}
