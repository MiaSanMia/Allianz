package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;
import com.renren.ugc.comment.xoa2.CommentType;


/**
 * @author wangxx
 * 
 * "全站分享 "的评论表映射
 */
public class CommentUrlRowMapper implements RowMapper{

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getInt("user_id"));
        comment.setType(rs.getInt("type"));

        Entry entry = new Entry();
        entry.setId(rs.getLong("share_id"));
        entry.setOwnerId(rs.getInt("share_owner"));

        comment.setEntry(entry);
        comment.setContent(rs.getString("comment"));
        comment.setCreatedTime(rs.getTimestamp("create_time").getTime());
        //comment.setToCommendId(rs.getLong("to_comment_id"));
        comment.setToUserId(rs.getInt("reply_to_user"));
        //comment.setWhipserToId(rs.getInt("whisper_to_id"));

        Flag flag = new Flag(rs.getInt("mark_bit"));
        comment.setFlag(flag);

        Metadata metadata = new Metadata(rs.getString("meta"));
        comment.setMetadata(metadata);

        return comment;
    }
}
