/**
 * 
 */
package com.renren.pillar.common.bak;

import java.io.Serializable;

import com.renren.pillar.common.service.BaseService;

/**
 * @author zhang.liang
 * @createTime 2014-5-15 上午11:34:16
 * 
 */
public class AccessLogService extends BaseService implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /*public List<AccessLog> queryLog(AccessLog accessLog) {
        return PillarUtils.change(super.queryLog(accessLog));
    }

    public List<AccessLog> overview(AccessLog accessLog) {
        return PillarUtils.change(super.overview(accessLog));
    }*/
    
    
    /*private static final Log logger = LogFactory.getLog(AccessLogService.class);
 
    public void storeLog(String keyWithTime, int count) {

        String[] props = keyWithTime.split(PillarUtils.SEPERATOR);
        String strLogTime = props[0];
        //String logType = props[1];
        String business = props[2];
        String ip = props[3];
        String responseCode = props[4];
        String url = props[5];

        AccessLog accessLog = new AccessLog();

        try {
            Date logTime = PillarUtils.DF.parse(strLogTime);
            accessLog.setLogTime(logTime);
        } catch (ParseException e1) {
            accessLog.setNote(strLogTime);
            logger.error("strLogTime:" + strLogTime, e1);
        }
        accessLog.setBusiness(business);
        accessLog.setIp(ip);
        accessLog.setResponseCode(responseCode);
        accessLog.setUrl(url);
        accessLog.setCreateTime(Calendar.getInstance().getTime());
        accessLog.setCount(count);
        try {
            baseDAO.insert(accessLog);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }*/

    /*

    public static void main(String[] args) throws SQLException {

        AccessLogService errorLogService = new AccessLogService();
        errorLogService.storeLog(
                "2014-05-11 14:33#share#10.3.2.34#nullpointer#Share.getComment 111", 444);

    }
*/
}
