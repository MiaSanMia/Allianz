/**
 * 
 */
package com.renren.pillar.common.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.pillar.common.dao.BaseDAO;
import com.renren.pillar.common.model.BaseModel;

/**
 * @author zhang.liang
 * @createTime 2014-5-21 下午6:03:53
 * 
 */
public class BaseService<T extends BaseModel> {

    private static final Log logger = LogFactory.getLog(BaseService.class);

    protected BaseDAO baseDAO = new BaseDAO();

    public void storeLog(BaseModel baseModel) {
        try {
            baseDAO.insert(baseModel); 
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<T> queryLog(T baseModel) {
        List<T> l = new ArrayList<T>();
        try {
            l = PillarUtils.change(baseDAO.query(baseModel));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return l;
    }

    public List<T> overview(T baseModel) {
        List<T> l = new ArrayList<T>();
        try {
            l = PillarUtils.change(baseDAO.overview(baseModel));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return l;
    }
}
