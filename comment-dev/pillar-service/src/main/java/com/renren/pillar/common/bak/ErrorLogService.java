/**
 * 
 */
package com.renren.pillar.common.bak;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.renren.pillar.common.model.BaseModel;
import com.renren.pillar.common.model.ErrorLog;
import com.renren.pillar.common.service.BaseService;

/**
 * @author zhang.liang
 * @createTime 2014-5-15 上午11:34:16
 * 
 */
public class ErrorLogService extends BaseService implements Serializable {

    private static final long serialVersionUID = 2317318150409056358L;

    /*public List<ErrorLog> queryLog(ErrorLog errorLog) {
        return PillarUtils.change(super.queryLog(errorLog));
    }

    
    public List<ErrorLog> overview(ErrorLog errorLog) {
        return PillarUtils.change(super.overview(errorLog));
    }*/
    
    //private static final Log logger = LogFactory.getLog(ErrorLogService.class);

    /*public void storeLog(BaseModel baseModel) {

        String[] props = keyWithTime.split("" + PillarUtils.SEPERATOR);
        String strLogTime = props[0];
        //String logType = props[1];
        String business = props[2];
        String ip = props[3];
        String message = props[4];
        String location = props[5];

        ErrorLog errorLog = new ErrorLog();

        try {
            Date logTime = PillarUtils.DF.parse(strLogTime);
            errorLog.setLogTime(logTime);
        } catch (ParseException e1) {
            errorLog.setNote(strLogTime);
            logger.error("strLogTime:" + strLogTime, e1);
        }

        errorLog.setBusiness(business);
        errorLog.setIp(ip);
        errorLog.setMessage(message);
        errorLog.setLocation(location); 
        errorLog.setCreateTime(Calendar.getInstance().getTime());
        errorLog.setCount(count);
        try {
            baseDAO.insert(errorLog);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }*/

    /*public List<ErrorLog> queryErrorLog(ErrorLog errorLog) {
        List<ErrorLog> errorLogs = new ArrayList<ErrorLog>();
        try {
            if(errorLogs != null){
                for (BaseModel baseModel : baseDAO.query(errorLog)) {
                    errorLogs.add((ErrorLog)baseModel);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return errorLogs;
    }
    
    public List<ErrorLog> overview(ErrorLog errorLog) {
        List<ErrorLog> errorLogs = new ArrayList<ErrorLog>();
        try {
            if(errorLogs != null){
                for (BaseModel baseModel : baseDAO.overview(errorLog)) {
                    errorLogs.add((ErrorLog)baseModel);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return errorLogs;
    }*/

    
    public static void main(String[] args) throws SQLException {
        /*BaseDAO baseDAO = new BaseDAO();  
        ErrorLog errorLog = new ErrorLog();
        errorLog.setLogType(LogType.ERROR_LOG);
        errorLog.setBusiness("test");
        
        baseDAO.insert(errorLog);*/

        /*ErrorLogService errorLogService = new ErrorLogService();
        errorLogService.storeLog(
                "2014-05-11 14:33#share#10.3.2.34#nullpointer#Share.getComment 111", 444);*/

    }

}
