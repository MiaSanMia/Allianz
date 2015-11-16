package com.renren.ugc.comment.dao;

import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

@DAO(catalog = "universe_comment")
public interface ExtCommentDAO {

    static final String COMMENT_EXT_TABLE = "universe_comment_ext";

    @SQL("INSERT INTO "
            + COMMENT_EXT_TABLE
            + "(id, entry_id, entry_owner_id, ext_content) VALUES(:id, :entryId, :entryOwnerId, :content)")
    public int insert(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId, @SQLParam("content") String content);

    /**
     * get the ext contents of multiple comments
     */
    @SQL("SELECT id, ext_content FROM " + COMMENT_EXT_TABLE
            + " WHERE id in (:ids) AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    public Map<Long, String> getExtContents(@SQLParam("ids") List<Long> ids,
            @SQLParam("entryId") long entryId, @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    /**
     * get the ext content of a single comment
     */
    @SQL("SELECT ext_content FROM " + COMMENT_EXT_TABLE
            + " WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    public String getExtContent(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId);

    @SQL("UPDATE "
            + COMMENT_EXT_TABLE
            + " SET ext_content = :newContent WHERE id = :id AND entry_id = :entryId AND entry_owner_id = :entryOwnerId")
    public int updateCommentContent(@SQLParam("id") long id, @SQLParam("entryId") long entryId,
            @SQLParam("entryOwnerId") @ShardBy int entryOwnerId,
            @SQLParam("newContent") String newContent);
}
