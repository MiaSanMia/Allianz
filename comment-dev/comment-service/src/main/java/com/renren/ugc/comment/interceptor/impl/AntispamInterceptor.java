package com.renren.ugc.comment.interceptor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.CommentPackage;
import com.renren.ugc.comment.service.CommentLogicAdapter;
import com.renren.ugc.comment.strategy.CommentConfig;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.strategy.ForInvokeStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.renren.ugc.comment.xoa2.util.CommentError;
import com.xiaonei.antispam.AntiSpamAdapter;
import com.xiaonei.antispam.model.CheckResult;
import com.xiaonei.antispam.model.CheckResultMore;

/**
 * The interceptor that apply antispam before the comment is created. The
 * antispam's result may affect the audit behavior. To apply antispam, you
 * need to specify a valid antispam "check type" in the comment config for
 * the current comment type and ensure this interceptor is enabled.
 * 
 * @author jiankuan.xing
 * 
 */
public final class AntispamInterceptor extends CommentLogicAdapter {

    private Logger logger = Logger.getLogger(this.getClass());
    
    
    @Override
    public Comment create(CommentType type, int actorId, long entryId, int entryOwnerId,
            Comment comment, CommentStrategy strategy) {

        String safeContent = doAntispam(type, actorId, entryOwnerId, comment.getContent(), strategy);

        comment.setContent(safeContent);

        return null; // as an interceptor, returned value is of no use
    }

    @Override
    public boolean update(CommentType type, int actorId, long entryId, int entryOwnerId,
            long commentId, Comment newComment, CommentStrategy strategy) {
        String safeContent = doAntispam(type, actorId, entryOwnerId, newComment.getContent(),
                strategy);
        newComment.setContent(safeContent);
        return false;
    }
    
    /**
     * 现阶段安全验证取id只取list中的第一个作为传参
     */
    @Override
    public List<CommentPackage> createByList(CommentType type, long entryId, int entryOwnerId,
    		ForInvokeStrategy forInvokeStrategy) throws UGCCommentException {
    	List<String> contents = new ArrayList<String>();
    	int actorId =0;
    	for(CommentPackage onepackage : forInvokeStrategy.getPackageList()){
    		if(actorId == 0){
    			actorId=onepackage.getActorId();
    		}
    		contents.add(onepackage.getComment().getContent());
    	}
    	
    	//批量验证接口
    	List<String> safeContents = doAntispamForMulti(type, actorId, entryOwnerId, contents,
    			forInvokeStrategy);
    	
    	if(safeContents.size() != forInvokeStrategy.getPackageList().size()){
    		throw new UGCCommentException(CommentError.ANTISPANM_EXCEPTION,
                    CommentError.ANTISPANM_EXCEPTION_MSG);
    	}else{
    		for(int i=0;i<safeContents.size();++i){
    			forInvokeStrategy.getPackageList().get(i).getComment().setContent(safeContents.get(i));
    		}
    	}
    	
		return null;
	}
    
    @Override
    public boolean remove(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            long commentId, CommentStrategy strategy) throws UGCCommentException {
    	//content采用时间戳+随机数+entryId+type的形式
    	String content = Long.toString(System.currentTimeMillis() + new Random().nextLong())+"|"+entryId+"|"+commentType.toString();
    	doAntispam(commentType, actorId, entryOwnerId, content, strategy);
        return false;
    }
    
    @Override
    public boolean removeAll(CommentType commentType, int actorId, long entryId, int entryOwnerId,
            CommentStrategy strategy) throws UGCCommentException {
    	//content采用时间戳+随机数+entryId+type的形式
    	String content = Long.toString(System.currentTimeMillis() + new Random().nextLong())+"|"+entryId+"|"+commentType.toString();
    	doAntispam(commentType, actorId, entryOwnerId, content, strategy);
        return false;
    }
    
    private String doAntispam(CommentType type, int actorId, int entryOwnerId, String content,
            CommentStrategy strategy) {
        CommentConfig config = strategy.getConfig();
        if (config != null && !config.shouldCheckSpamContent()) {
            if (logger.isDebugEnabled()) {
                logger.debug("antispam is ignored for comment type " + type.name());
            }
            return content;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "do antispam with check type:%d, authorId:%d, toId:%d, userIp:%s",
                    config.getAntispamType(), actorId, entryOwnerId, strategy.getUserIp()));
        }

        CheckResult checkResult = AntiSpamAdapter.getInstance().antispamFilter(actorId,
                entryOwnerId, config.getAntispamType(), content, strategy.getUserIp());

        if (logger.isDebugEnabled()) {
            logger.debug("antispam result is " + checkResult.getFlag());
        }
        //默认不送审
        strategy.setShouldAudit(false);
        switch (checkResult.getFlag()) {
            case CheckResult.PROHIBITED:
                if(checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_FAST || checkResult.getInfoType() == checkResult.ACTIVE_TYPE_FAST_OVER_COUNT ||
                        checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_SAME || checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_SAME_OVER_COUNT){
                	logger.error("AntispamInterceptor tooFast|actorId:"+actorId+"|entryOwnerId:"+entryOwnerId+"|content:"+content);
                    throw new UGCCommentException(CommentError.COMMENT_TOO_FAST,
                            CommentError.COMMENT_TOO_FAST_MSG);
                } else {
                    throw new UGCCommentException(CommentError.PROHIBITED_BY_ANTISPAM,
                            checkResult.getShowMessage());
                }
                
            case CheckResult.CONTENT_NEED_AUDIT:
                strategy.setShouldAudit(true);
                break;

            case CheckResult.CONTENT_NOT_NOTIFY:
                break;

            case CheckResult.AUDIT_AND_NOT_NOTIFY:
                strategy.setShouldAudit(true);
                strategy.setSendNotice(false);
                break;

        }

        return checkResult.getSafePureContent();
    }
    
    private List<String> doAntispamForMulti(CommentType type, int actorId, int entryOwnerId, List<String> contents,
            CommentStrategy strategy) {
        CommentConfig config = strategy.getConfig();
        if (config != null && !config.shouldCheckSpamContent()) {
            if (logger.isDebugEnabled()) {
                logger.debug("antispam is ignored for comment type " + type.name());
            }
            return contents;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "do antispamForList with check type:%d, authorId:%d, toId:%d, userIp:%s",
                    config.getAntispamType(), actorId, entryOwnerId, strategy.getUserIp()));
        }
        CheckResultMore checkResult = AntiSpamAdapter.getInstance().antispamFilter(actorId,
                entryOwnerId, config.getAntispamType(), contents, strategy.getUserIp());


        if (logger.isDebugEnabled()) {
            logger.debug("antispam result is " + checkResult.getFlag());
        }
        //默认不送审
        strategy.setShouldAudit(false);
        switch (checkResult.getFlag()) {
            case CheckResult.PROHIBITED:
                if(checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_FAST || checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_FAST_OVER_COUNT ||
                        checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_SAME || checkResult.getInfoType() == CheckResult.ACTIVE_TYPE_SAME_OVER_COUNT){
                	logger.error("AntispamInterceptor tooFast|actorId:"+actorId+"|entryOwnerId:"+entryOwnerId+"|contents:"+contents.toString());
                    throw new UGCCommentException(CommentError.COMMENT_TOO_FAST,
                            CommentError.COMMENT_TOO_FAST_MSG);
                } else {
                    throw new UGCCommentException(CommentError.PROHIBITED_BY_ANTISPAM,
                            checkResult.getShowMessage());
                }
                
            case CheckResult.CONTENT_NEED_AUDIT:
                strategy.setShouldAudit(true);
                break;

            case CheckResult.CONTENT_NOT_NOTIFY:
                break;

            case CheckResult.AUDIT_AND_NOT_NOTIFY:
                strategy.setShouldAudit(true);
                strategy.setSendNotice(false);
                break;

        }

        return checkResult.getSafePureContentList();
    }
}
