package com.renren.ugc.comment.storm.dao;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.renren.ugc.comment.storm.model.CommentAccessLogEntry;
import com.renren.ugc.comment.storm.model.CommentApiObj;
import com.renren.ugc.comment.storm.utils.DBUtils;
import com.renren.ugc.comment.storm.utils.TimeUtils;


public class CommentApiDBWriter implements Runnable {

    private static Logger LOG = Logger.getLogger(CommentApiDBWriter.class);

    private static final String tableName = "comment_api_info";

    private static String insertSql =
            "insert into "
                + tableName
                + " (type,method,count, max_time, avg_time, miss_count,timeout_count,exception_count, ip, date) values(?, ?, ?, ?, ?, ?,?, ?, ?, ?) ";

    private CommentAccessLogEntry obj;

    public CommentApiDBWriter(CommentAccessLogEntry obj){
        this.obj = obj;
    }

    public int insert() throws SQLException {
        PreparedStatement ps = DBUtils.getPreparedStatement(insertSql);
        ps.setString(1, obj.getType());
        ps.setString(2, obj.getMethod());
        ps.setInt(3, obj.getCount());
        ps.setInt(4, obj.getMaxTime());
        ps.setInt(5, obj.getAvgTime());
        ps.setInt(6, obj.getMissCount());
        ps.setInt(7, obj.getTimeoutCount());
        ps.setInt(8, obj.getExceptionCount());
        ps.setString(9, obj.getIpAddress());
        ps.setTimestamp(10,
            new Timestamp(obj.getDate()));
        ps.executeUpdate();
        ps.close();
        return 0;
    }

    public void run() {
        try {
            insert();
        } catch (SQLException e) {
            LOG.error(e);
        }
    }
}
