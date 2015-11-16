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
import com.renren.xoa2.api.campus.model.GetFeedControlParam;
import com.renren.xoa2.api.campus.model.GetFeedRequest;
import com.renren.xoa2.api.campus.model.GetFeedResponse;
import com.renren.xoa2.api.campus.service.IPiazzaService;
import com.renren.xoa2.client.ServiceFactory;

public class CampusExcellentEntryService implements EntryGetService{

	  private static final Logger logger = Logger.getLogger(CampusExcellentEntryService.class);

	    private static CampusExcellentEntryService instance = new CampusExcellentEntryService();

	    public static CampusExcellentEntryService getInstance() {
	        return instance;
	    }

	    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
	        return ServiceFactory.getService(serviceClass, timeout);
	    }
	    
	    private IPiazzaService piazzaService ;
	    
	    private final int XOA_TIMEOUT = 500;

	    private CampusExcellentEntryService() {
	    	piazzaService = getServiceXOA2(IPiazzaService.class, XOA_TIMEOUT);
	    }

	    @Override
	    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
	            CommentStrategy strategy) throws UGCCommentException {
	    	
	    	long start = System.nanoTime();
	        boolean success = false;

	        //对于CampusExcellent-校园精品来说，entryId就是feedid，entryOwnerId就是学校id
	        GetFeedRequest req = new GetFeedRequest();
            req.setId(entryId);
            req.setSchoolId(entryOwnerId);
            req.setVisitorId(actorId);
            req.setFrom(From.COMMENT_CENTER);
            GetFeedControlParam controlParam = new GetFeedControlParam();
            controlParam.setNeedSchoolName(true);
            req.setControlParam(controlParam);

	        try {
	        	GetFeedResponse response = piazzaService.getFeed(req);
	            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
	                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
	                        "ugcCommentCreateError," + response.getBaseRep().getErrorInfo().getMsg());
	            } else if(response.getFeed() == null) {
	            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
	                        "campusFeed not exist");
	            } 
	            return Transfer2EntryUtil.campusExcellentTransfer2EntryUtil(response.getFeed(),
	                    strategy.getEntry(), entryOwnerId,entryId);
	        } catch (UGCCommentException ue) {
	            throw ue;
	        } catch (Exception e) {
	            logger.error("getEntryInfo error|type:campusExcellent|actorId:" + actorId + "|entryId:"
	                    + entryId + "|entryOwner:" + entryOwnerId, e);
	        }
	        long end = System.nanoTime();
	        StatisticsHelper.invokeGetTopPosts((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
	        return null;
	    }

}
