/**
 * 
 */
package com.renren.pillar.common.dao;

import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.renren.pillar.common.model.BaseModel;

/**
 * @author zhang.liang
 * @createTime 2014-5-15 下午2:54:36
 * 
 */
public class BaseDAO implements Serializable{


    private static final long serialVersionUID = 4085283474206183828L;

    private static SqlMapClient sqlMapClient;

    private static final Object object = new Object();

    private static final Log logger = LogFactory.getLog(BaseDAO.class);

    protected String getNamespace(BaseModel baseModel) {
        return baseModel.getLogType().name + ".";
    }
 
    @SuppressWarnings("unchecked")
    public List<BaseModel> query(BaseModel baseModel) throws SQLException {

        return getSqlMapClient().queryForList(getNamespace(baseModel) + "query", baseModel);

    }
    
    @SuppressWarnings("unchecked")
    public List<BaseModel> overview(BaseModel baseModel) throws SQLException {

        return getSqlMapClient().queryForList(getNamespace(baseModel) + "overview", baseModel);

    }

    public void insert(BaseModel baseModel) throws SQLException {
        getSqlMapClient().insert(getNamespace(baseModel) + "insert", baseModel);
    }

    private SqlMapClient getSqlMapClient() {
        if (sqlMapClient == null) {
            synchronized (object) {
                if (sqlMapClient == null) {
                    try {
                        Reader reader = Resources.getResourceAsReader("sqlMapConfig.xml");
                        sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
                        reader.close();
                        return sqlMapClient;
                    } catch (Exception e) {
                        logger.error("获取 SqlMapClient 出错了", e);
                    }
                }
            }
        }
        return sqlMapClient;
    }

}
