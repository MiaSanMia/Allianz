package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.util.CommentOldUgcStateUtil;
import com.renren.ugc.comment.xoa2.CommentType;

public class StatusCommentRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getInt("replyer_id"));
        comment.setType(CommentType.Status.getValue());

        Entry entry = new Entry();
        entry.setId(rs.getLong("doing_id"));
        entry.setOwnerId(rs.getInt("owner_id"));

        comment.setEntry(entry);
        comment.setContent(rs.getString("reply_content"));
        comment.setCreatedTime(rs.getTimestamp("reply_time").getTime());

        //这里做个status和评论中心的state转换
        //现在status业务只用到了一个状态位
        int statusCommentState = rs.getInt("markbit");
        Flag flag = new Flag();
        Metadata metadata = new Metadata();
        
        if(CommentOldUgcStateUtil.isStatusVoice(statusCommentState)){
            flag.setUseVoice();
        }
        comment.setFlag(flag);
        
        comment.setMetadata(metadata);

        return comment;
    }

}
