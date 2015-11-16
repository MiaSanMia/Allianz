package com.renren.ugc.comment.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.ugc.comment.dao.IdSequenceDAO;
import com.renren.ugc.comment.dao.StatusCommentDAO;
import com.renren.ugc.comment.exception.UGCCommentException;
import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.strategy.CommentStrategy;
import com.renren.ugc.comment.xoa2.CommentType;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.opt.ice.WUserAdapter;

@Service
public class WriteOldDatabaseUtil implements InitializingBean {

    private static WriteOldDatabaseUtil instance;

    public static WriteOldDatabaseUtil getInstance() {
        return instance;
    }

    public final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IdSequenceDAO idSequenceDAO;

    @Autowired
    private StatusCommentDAO statusCommentDAO;
    
    public long getNextOldStatusCommentId() {
        return idSequenceDAO.nextId(IdSequenceDAO.STATUS_COMMENT_SEQ);
    }

    public void writeOldDatabase(CommentType type, Comment c, CommentStrategy strategy) {

        if (c == null) {
            return;
        }
        if (strategy.getEntry() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("writeOldDatabase entry is null|commentId:" + c.getId());
            }
            return;
        }
        try {
            //   long id = idSequenceDAO.nextId(IdSequenceDAO.sqlGetNextBlogComment);
            switch (type) {
              case Status:
            	  saveOldStatusComment(type,c,strategy);
            	  break;
            }
        } catch (Exception e) {
            logger.error("writeOldDatabase error|commentId:" + c.getId() + "|entryId:"
                    + strategy.getEntry().getId() + "|ownerId:" + strategy.getEntry().getOwnerId()+"|type:"+type.toString(),
                    e);
        }
    }

    private String getBody(Comment comm) {
        if (comm.isWhisper()) {
            String whipserString = "<xiaonei_only_to_me/><Toid/>" + comm.getWhipserToId();
            return comm.getOriginalContent() + whipserString;
        }
        return comm.getOriginalContent();
    }

    /**
     * @param url
     * @return from http://hdn.xnimg.cn/photos/hdn221/20130416/1205/
     *         h_tiny_Loag_a9c6000001f4113e.jpg to
     *         hdn221/20130416/1205/h_tiny_Loag_a9c6000001f4113e.jpg
     */
    private String getRelativeUrl(String url) {

        int index = url.indexOf("http://");

        if (index < 0) {
            return url;
        }

        //找到第2个/
        index = index + "http://".length();

        index = url.indexOf("/", index + 1);
        if (index < 0) {
            return url;
        }
        index = url.indexOf("/", index + 1);
        if (index < 0) {
            return url;
        }

        int len = url.length();
        url = url.substring(index + 1, len);

        return url;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WriteOldDatabaseUtil.instance = this;
    }
    
    private void saveOldStatusComment(CommentType type, Comment comment,
            CommentStrategy strategy) {
            Entry entry = comment.getEntry();
            if (comment.getEntry() == null) {
                logger.warn("the comment's corresponding entry is not specified, ignored comment creation");
                throw new UGCCommentException("the comment's entry is not specified");
            }
            
            long entryId = comment.getEntry().getId();

            if (entryId <= 0) {
                logger.warn("the entryId " + entryId + " is invalid");
            }
            
            int entryOwnerId = comment.getEntry().getOwnerId();
            if (entryId <= 0) {
                logger.warn("the entryOnwerId " + entryOwnerId + " is invalid");
            }
            
            //设置头像和作者名字,好dt >_<
            final User authorUser = WUserAdapter.getInstance().get(comment.getAuthorId());
            
            //标记从评论中心同步过来的
            int markBit = 1<<6;
            if (comment.isVoiceComment()) {
                markBit |= 1<<0;
            }
            
            long id = getNextOldStatusCommentId();
            
            try{
	            statusCommentDAO.insert(id, comment.getAuthorId(), entryId, getBody(comment), entryOwnerId,  new Date(comment.getCreatedTime()), markBit, getRelativeUrl(comment
	                    .getAuthorHead()), authorUser.getName());
            } catch(Exception e){
            	logger.error("WriteOldDatabaseUtil save statuscomment error,ownerId="+entryOwnerId+",entryId="+entryId+",id="+comment.getId(),e);
            }
            
        }

}
