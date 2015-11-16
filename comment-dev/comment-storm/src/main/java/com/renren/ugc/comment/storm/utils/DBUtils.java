package com.renren.ugc.comment.storm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.storm.model.Config;
import com.xiaonei.xce.XceAdapter;

/**
 * @author xulei
 * @date 2013-8-7
 * @email lei.xu1@renren-inc.com
 * @tags
 */
public class DBUtils {

    private static final Log log = LogFactory.getLog(DBUtils.class);
    private static String url = Config.DB_CONNECTION_URL;
    private static String dataSource = Config.DB_BIZNAME;
    private static String userName = Config.DB_USERNAME;
    private static String password = Config.DB_PASSWORD;
    // private static Properties props = new Properties();
    private static Connection conn = null;
    
    static {

        // 注册驱动类
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("#ERROR# :加载数据库驱动异常，请检查！", e);
        }
    }

    /**
     * 创建一个数据库连接
     * 
     * @return 一个数据库连接
     */
    public static Connection getConnection() {
        //Connection conn = null;
        // 创建数据库连接
    	if(conn == null){
    		synchronized(DBUtils.class){
    			if(conn == null){
			        try {
			            conn = DriverManager.getConnection(url, userName, password);
			            //conn = XceAdapter.getInstance().getWriteConnection(dataSource);
			        } catch (SQLException e) {
			            log.error("#ERROR# :创建数据库连接发生异常，请检查！", e);
			        }
    			}
    		}
    	}
        return conn;
    }

    public static PreparedStatement getPreparedStatement(String sql)
        throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    /**
     * 在一个数据库连接上执行一个静态SQL语句查询
     * 
     * @param conn 数据库连接
     * @param staticSql 静态SQL语句字符串
     * @return 返回查询结果集ResultSet对象
     */
    public static ResultSet executeQuery(Connection conn, String staticSql) {
        ResultSet rs = null;
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            // 执行SQL，并获取返回结果
            rs = stmt.executeQuery(staticSql);
        } catch (SQLException e) {
            log.error("#ERROR# :执行SQL语句出错，请检查！\n" + staticSql, e);
        }
        return rs;
    }

    /**
     * 在一个数据库连接上执行一个静态SQL语句
     * 
     * @param conn 数据库连接
     * @param staticSql 静态SQL语句字符串
     */
    public static void executeSQL(Connection conn, String staticSql) {
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            // 执行SQL，并获取返回结果
            stmt.execute(staticSql);
        } catch (SQLException e) {
            log.error("#ERROR# :执行SQL语句出错，请检查！\n" + staticSql, e);
        }
    }

    /**
     * 在一个数据库连接上执行一批静态SQL语句
     * 
     * @param conn 数据库连接
     * @param sqlList 静态SQL语句字符串集合
     */
    public static void executeBatchSQL(Connection conn, List<String> sqlList) {
        try {
            // 创建执行SQL的对象
            Statement stmt = conn.createStatement();
            for (String sql : sqlList) {
                stmt.addBatch(sql);
            }
            // 执行SQL，并获取返回结果
            stmt.executeBatch();
        } catch (SQLException e) {
            log.error("#ERROR# :执行批量SQL语句出错，请检查！", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn == null) return;
        try {
            if (!conn.isClosed()) {
                // 关闭数据库连接
                conn.close();
            }
        } catch (SQLException e) {
            log.error("#ERROR# :关闭数据库连接发生异常，请检查！", e);
        }
    }
}
