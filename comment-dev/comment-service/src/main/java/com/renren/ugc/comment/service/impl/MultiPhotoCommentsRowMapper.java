package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.xoa2.CommentType;


public class MultiPhotoCommentsRowMapper implements RowMapper {
    
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
        comment.setContent(rs.getString("body"));
        comment.setCreatedTime(rs.getTimestamp("time").getTime());

        //这里做个photo和评论中心的state转换
        //现在photo业务只用到了两个状态位
        int photoCommentState = rs.getInt("state");
        Flag flag = new Flag();
        Metadata metadata = new Metadata();
        
        if(CommentOldUgcStateUtil.isPhotoVoice(photoCommentState)){
            flag.setUseVoice();
        }
        comment.setFlag(flag);

        if(CommentOldUgcStateUtil.isPhotoTag(photoCommentState)){
            //和相册技术商量好的>_<
            metadata.put(CommentOldUgcStateUtil.PHOTO_TAG_INFO,"true" );
        }
        comment.setMetadata(metadata);

        Map.Entry<Long, Comment> mapEntry = new AbstractMap.SimpleEntry<Long, Comment>(
                comment.getId(), comment);
        return mapEntry;
    }

}
