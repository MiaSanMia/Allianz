package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.util.AsyncCommentOpUtil;
import com.renren.ugc.comment.util.CommentCenterConsts;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.xoa2.CommentType;

public class PhotoCommentRowMapper implements RowMapper {
    
    private static Logger logger = Logger.getLogger(PhotoCommentRowMapper.class);

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getInt("author"));
        comment.setType(CommentType.Photo.getValue());

        Entry entry = new Entry();
        entry.setId(rs.getLong("photo"));
        entry.setOwnerId(rs.getInt("owner"));

        comment.setEntry(entry);
        
        String body = rs.getString("body");
        comment.setContent(body);
        //过滤悄悄话
       getAndFilterWhisperId(body,entry.getOwnerId(),comment);
        //过滤wap
        boolean isWap = getAndFilterWap(body,comment);
        
        comment.setCreatedTime(rs.getTimestamp("time").getTime());

        //这里做个photo和评论中心的state转换
        //现在photo业务只用到了两个状态位
        int photoCommentState = rs.getInt("state");
        Flag flag = new Flag();
        Metadata metadata = new Metadata();
        
        if(CommentOldUgcStateUtil.isPhotoVoice(photoCommentState)){
            flag.setUseVoice();
        }
        if(isWap){
            flag.setFromWap();
        }
        comment.setFlag(flag);
        
        if(CommentOldUgcStateUtil.isPhotoTag(photoCommentState)){
            //和相册技术商量好的>_<
            metadata.put(CommentOldUgcStateUtil.PHOTO_TAG_INFO,"true");
        }
        comment.setMetadata(metadata);

        return comment;
    }
    
    private void getAndFilterWhisperId(String body,int ownerId,Comment comment){
        int whisperId = 0;
        //悄悄话初始化
        if (StringUtils.contains(body, CommentCenterConsts.WHISPER_TOID_MARK)) {
            try {
                int ccuser = Integer.parseInt(StringUtils.substring(body,
                        StringUtils.indexOf(body, CommentCenterConsts.WHISPER_TOID_MARK) + CommentCenterConsts.WHISPER_TOID_MARK.length()));
                
                body = StringUtils.substring(body, 0, StringUtils.indexOf(body, CommentCenterConsts.WHISPER_MARK));
                     
                whisperId =  ccuser == 0 ? ownerId:ccuser;
            } catch (Exception e) {
                logger.error("getAndFilterWhisperId error|e:"+e.getMessage());
            }
        } else if (StringUtils.contains(body, CommentCenterConsts.WHISPER_MARK)) {
            body = StringUtils.substring(body, 0, StringUtils.indexOf(body, CommentCenterConsts.WHISPER_MARK));
            //这里ugc把主人在自己ugc的悄悄话的whisperId设为了0,好dt
            whisperId = ownerId;
        }
        comment.setContent(body);
        comment.setWhipserToId(whisperId);
    }
    
    private boolean getAndFilterWap(String body,Comment comment){
        if (StringUtils.contains(body, CommentCenterConsts.WAP_MARK)) {
            body = StringUtils.substring(body, 0, StringUtils.indexOf(body, CommentCenterConsts.WAP_MARK));
            comment.setContent(body);
            return true;
        } 
        return false;
    }

}
