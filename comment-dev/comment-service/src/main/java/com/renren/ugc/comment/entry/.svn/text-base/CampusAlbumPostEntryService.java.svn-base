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
import com.renren.xoa2.api.campus.model.GetAlbumByIdRequest;
import com.renren.xoa2.api.campus.model.GetAlbumByIdResponse;
import com.renren.xoa2.api.campus.model.GetAlbumControlParam;
import com.renren.xoa2.api.campus.service.IAlbumService;
import com.renren.xoa2.client.ServiceFactory;

public class CampusAlbumPostEntryService implements EntryGetService{

	  private static final Logger logger = Logger.getLogger(CampusAlbumPostEntryService.class);

	    private static CampusAlbumPostEntryService instance = new CampusAlbumPostEntryService();

	    public static CampusAlbumPostEntryService getInstance() {
	        return instance;
	    }

	    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
	        return ServiceFactory.getService(serviceClass, timeout);
	    }
	    
	    private IAlbumService albumPostService ;
	    
	    private final int XOA_TIMEOUT = 500;

	    private CampusAlbumPostEntryService() {
	    	albumPostService = getServiceXOA2(IAlbumService.class, XOA_TIMEOUT);
	    }

	    @Override
	    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
	            CommentStrategy strategy) throws UGCCommentException {
	    	
	    	long start = System.nanoTime();
	        boolean success = false;

	        GetAlbumByIdRequest req = new GetAlbumByIdRequest();
	        req.setOwnerId(entryOwnerId);
	        req.setAlbumId(entryId);
	        req.setVisitorId(actorId);
	        req.setFrom(From.COMMENT_CENTER);
	        GetAlbumControlParam controlParam = new GetAlbumControlParam();
			req.setGetAlbumControlParam(controlParam);

	        //req.setFrom(ShortvideoFromConstants.SHORTVIDEO_FROM_COMMENT_CENTER);

	        try {
	        	GetAlbumByIdResponse response = albumPostService.getAlbumById(req);
	            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
	                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
	                        "ugcCommentCreateError," + response.getBaseRep().getErrorInfo().getMsg());
	            } else if(response.getAlbum() == null) {
	            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
	                        "albumpost not exist");
	            } 
	            return Transfer2EntryUtil.albumPostsTransfer2EntryUtil(response.getAlbum(),
	                    strategy.getEntry(), entryOwnerId,entryId);
	        } catch (UGCCommentException ue) {
	            throw ue;
	        } catch (Exception e) {
	            logger.error("getEntryInfo error|type:albumPost|actorId:" + actorId + "|entryId:"
	                    + entryId + "|entryOwner:" + entryOwnerId, e);
	        }
	        long end = System.nanoTime();
	        StatisticsHelper.invokeGetAlbumPosts((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
	        return null;
	    }

}
