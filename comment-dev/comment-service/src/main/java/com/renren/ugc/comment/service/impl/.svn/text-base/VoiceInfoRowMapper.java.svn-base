package com.renren.ugc.comment.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.renren.ugc.comment.model.CommentVoiceInfo;

/**
 * Convert voice select result to a map <id, CommentVoiceInfo>
 * 
 * @author jiankuan.xing
 * 
 */
public class VoiceInfoRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        CommentVoiceInfo voiceInfo = new CommentVoiceInfo();
        voiceInfo.setVoiceUrl(rs.getString("voice_url"));
        voiceInfo.setVoiceLength(rs.getInt("voice_length"));
        voiceInfo.setVoiceSize(rs.getInt("voice_size"));
        voiceInfo.setVoiceRate(rs.getInt("voice_rate"));
        voiceInfo.setVoicePlayCount(rs.getInt("voice_play_count"));
        Map.Entry<Long, CommentVoiceInfo> entry = new AbstractMap.SimpleEntry<Long, CommentVoiceInfo>(
                rs.getLong("id"), voiceInfo);

        return entry;
    }
}
