package com.renren.ugc.comment.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.RowHandler;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import com.renren.ugc.comment.model.CommentVoiceInfo;
import com.renren.ugc.comment.service.impl.VoiceInfoRowMapper;

@DAO(catalog = "universe_comment_voice")
public interface VoiceCommentDAO {

    static final String COMMENT_VOICE_TABLE = "universe_comment_voice";

    /**
     * Insert a voice comment to table. "universe_comment_voice" table is
     * originally managed by "voice comment center". Now it's changed to be
     * part of "comment center". We force ugc_comment_id to 0 so we can
     * distinguish the which voice comments are inserted by comment center
     * while which ones are the old data. Besides ext_type is no use for
     * now, but in db schema it's "NOT NULL", so we need to insert
     * something to it.
     */
    @SQL("INSERT INTO "
            + COMMENT_VOICE_TABLE
            + "(id, type, ugc_comment_id, entry_id, entry_owner_id, url, length, rate, voice_size, play_count, created_time, ext_type)"
            + " VALUES(:id, :type, :ugcCommentId, :entryId, :entryOwnerId, :url, :length, :rate, :size, 0, :createdTime, 0)")
    public int insert(@SQLParam("id") long id, @SQLParam("type") int type, @SQLParam("ugcCommentId") long ugcCommentId,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("url") String url, @SQLParam("length") int len, @SQLParam("rate") int rate,
            @SQLParam("size") int size, @SQLParam("createdTime") Date createdTime);

    /**
     * get the voice information of multiple comments
     */
    @SQL("SELECT id, url as voice_url, length as voice_length, voice_size, rate as voice_rate,"
            + " play_count as voice_play_count FROM " + COMMENT_VOICE_TABLE
            + " WHERE id in (:ids) ")
    @RowHandler(rowMapper = VoiceInfoRowMapper.class)
    public Map<Long, CommentVoiceInfo> getVoiceInfos(@SQLParam("ids") List<Long> ids,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get the voice information of a single comment
     */
    @SQL("SELECT  url as voice_url, length as voice_length, voice_size, rate as voice_rate,"
            + " play_count as voice_play_count FROM " + COMMENT_VOICE_TABLE
            + " WHERE id = :id")
    public CommentVoiceInfo getVoiceInfo(@SQLParam("id") long id,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    @SQL("UPDATE " 
            + COMMENT_VOICE_TABLE
            + " SET play_count = play_count + :inc WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId ")
    public int increasePlayCount(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("inc") int increment);

}
