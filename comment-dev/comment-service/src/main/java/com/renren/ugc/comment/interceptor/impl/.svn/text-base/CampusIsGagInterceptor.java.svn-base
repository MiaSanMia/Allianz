package com.renren.ugc.comment.interceptor.impl;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.statistics.StatisticsHelper;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.util.EntryConfig;
import com.renren.ugc.comment.util.EntryConfigUtil;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.renren.xoa2.api.campus.service.IGagService;
import com.renren.xoa2.api.schoolId.model.GetIsGaggedRequest;
import com.renren.xoa2.api.schoolId.model.GetIsGaggedResponse;
import com.renren.xoa2.client.ServiceFactory;

/**
 * @author wangxx
 * 
 * 校园主页是否"禁言"
 * 
 * 注意这个interceptor必须在{@link:GetEntryInterceptor}之后，因为需要取到这个帖子的schoolId
 *
 */
public class CampusIsGagInterceptor extends CommentLogicAdapter{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final int XOA_TIMEOUT = 500;
	
	private static IGagService gagService  = getServiceXOA2(IGagService.class, XOA_TIMEOUT);
	
	public static <T> T getServiceXOA2(final Class<T> serviceClass, final int timeout) {
        return ServiceFactory.getService(serviceClass, timeout);
    }
	
	@Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {
		
		long start = System.nanoTime();
        boolean success = false;

        GetIsGaggedRequest req = new GetIsGaggedRequest();
        req.setOperatorId(actorId);
        req.setUserId(actorId);
        int schoolId = EntryConfigUtil.getInt(strategy, EntryConfig.ENTRY_SCHOOL_ID);
        if(schoolId <= 0){
	        	logger.error("CampusGagInterceptor schoolId is invalid,ownerId:"+entryOwnerId+"|entryId:"+entryId);
	        	//这里我们允许可以继续发评论，宁可错杀一个，不能乱杀无辜
	        	return null;
        }

        req.setSchoolId(schoolId);

        try {
        	GetIsGaggedResponse response = gagService.isGagged(req);
            if (response.getBaseRep() != null && response.getBaseRep().getErrorInfo() != null) {
                throw new UGCCommentException(response.getBaseRep().getErrorInfo().getCode(),
                        "IGagService invoke isGagged error" + response.getBaseRep().getErrorInfo().getMsg());
            } else {
	            success = true;
	            if(response.isIsGagged()){
	            	throw new UGCCommentException(CommentError.PROHIBITED_BY_ADMIN,
	            			CommentError.PROHIBITED_BY_ADMIN_MSG);
	            }
            }
        } catch (UGCCommentException ue) {
            throw ue;
        } catch (Exception e) {
            logger.error("CampusGagInterceptor error|actorId:" + actorId + "|entryId:"
                    + entryId + "|entryOwner:" + entryOwnerId, e);
        }
        long end = System.nanoTime();
        StatisticsHelper.invokeIsGagged((end - start) / StatisticsHelper.NANO_TO_MILLIS, success);
        
        return null;
		
	}

}
