package com.renren.ugc.comment.entry;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.CommentErrorCode;
import com.renren.ugc.comment.util.Transfer2EntryUtil;
import com.renren.xoa2.client.ServiceFactory;
import com.renren.xoa2.ugc.shortvideo.constants.ShortvideoFromConstants;
import com.renren.xoa2.ugc.shortvideo.model.GetShortvideoRequest;
import com.renren.xoa2.ugc.shortvideo.model.GetShortvideoResponse;
import com.renren.xoa2.ugc.shortvideo.service.IShortvideoService;

/**
 * @author wangxx
 * 
 *  短视频服务
 *
 */
public class ShortVideoEntryService implements EntryGetService{

	  private static final Logger logger = Logger.getLogger(ShortVideoEntryService.class);

	    private static ShortVideoEntryService instance = new ShortVideoEntryService();

	    public static ShortVideoEntryService getInstance() {
	        return instance;
	    }

	    public <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
	        return ServiceFactory.getService(serviceClass, timeout);
	    }
	    
	    private IShortvideoService shortvideoService ;
	    
	    private final int XOA_TIMEOUT = 500;

	    private ShortVideoEntryService() {
	    	shortvideoService = getServiceXOA2(IShortvideoService.class, XOA_TIMEOUT);
	    }

	    @Override
	    public Entry getEntryInfo(int actorId, int entryOwnerId, long entryId, Comment comment,
	            CommentStrategy strategy) throws UGCCommentException {
	    	
	    	long start = System.nanoTime();
	        boolean success = false;

	        GetShortvideoRequest req = new GetShortvideoRequest();
	        req.setHostId(actorId);
	        req.setUgcId(entryId);
	        req.setUgcOwnerId(entryOwnerId);
	        req.setNeedMp4(false);
	        //req.setFrom(ShortvideoFromConstants.SHORTVIDEO_FROM_COMMENT_CENTER);

	        try {
	        	GetShortvideoResponse response = shortvideoService.getShortvideo(req);
	            if (response.getBaseResponse() != null && response.getBaseResponse().getErrorInfo() != null) {
	                throw new UGCCommentException(response.getBaseResponse().getErrorInfo().getCode(),
	                        "ugcVideoCreateError," + response.getBaseResponse().getErrorInfo().getMsg());
	            } else if(response.getShortvideo() == null) {
	            	throw new UGCCommentException(CommentErrorCode.ENTRY_NOT_EXIST,
	                        "video not exist");
	            } 
	            return Transfer2EntryUtil.videoTransfer2EntryUtil(response.getShortvideo(),
	                    strategy.getEntry(), entryOwnerId,entryId);
	        } catch (UGCCommentException ue) {
	            throw ue;
	        } catch (Exception e) {
	            logger.error("getEntryInfo error|type:video|actorId:" + actorId + "|entryId:"
	                    + entryId + "|entryOwner:" + entryOwnerId, e);
	        }
	        long end = System.nanoTime();
	        StatisticsHelper.invokeGetVideo((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
	        return null;
	    }
}
