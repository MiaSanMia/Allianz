package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.model.Entry;
import com.renren.ugc.comment.model.Flag;
import com.renren.ugc.comment.model.Metadata;

public class CommentRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getInt("author_id"));
        comment.setType(rs.getInt("type"));

        Entry entry = new Entry();
        entry.setId(rs.getLong("entry_id"));
        entry.setOwnerId(rs.getInt("entry_owner_id"));

        comment.setEntry(entry);
        comment.setContent(rs.getString("content"));
        comment.setCreatedTime(rs.getTimestamp("created_time").getTime());
        comment.setToCommendId(rs.getLong("to_comment_id"));
        comment.setToUserId(rs.getInt("to_user_id"));
        comment.setWhipserToId(rs.getInt("whisper_to_id"));

        Flag flag = new Flag(rs.getInt("flag"));
        comment.setFlag(flag);

        Metadata metadata = new Metadata(rs.getString("metadata"));
        comment.setMetadata(metadata);

        return comment;
    }

}
