package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.Comment;
import com.renren.ugc.comment.xoa2.CommentType;


public class StatusAuthorCommentRowMapper implements RowMapper{
    
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();

        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getInt("replyer_id"));
        comment.setType(CommentType.Status.getValue());

        return comment;
    }

}
