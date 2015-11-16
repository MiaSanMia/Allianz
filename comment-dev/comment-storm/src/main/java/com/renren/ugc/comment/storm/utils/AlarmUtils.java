package com.renren.ugc.comment.storm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.renren.ugc.comment.storm.alarm.AlarmParam;



/**
 * @author xulei
 * @date 2013-8-5
 * @email lei.xu1@renren-inc.com
 * @tags 报警
 */
public class AlarmUtils {

    private static final Log log = LogFactory.getLog(AlarmUtils.class);
    private static final String PHONE_NUM = "18600911263";
    private static final String EMAIL_ADDRESS = "lei.xu1@renren-inc.com";
    private static final String EMAIL_CC_ADDRESS = null;

    /*private static AlarmChain alarmChain =
            new AlarmChain().add(new SmsAlarm()).add(new EmailAlarm());*/
    /*private static AlarmChain alarmChain =
            new AlarmChain().add(new TestAlarm());*/


    public static void sendAlarm(String method, long timestamp, String msg) {
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String content =
                    "[method: " + method + ", time: "
                        + format.format(new Date(timestamp)) + "]" + msg;
            AlarmParam ap =
                    new AlarmParam().setCcEmail(EMAIL_CC_ADDRESS).setEmail(
                        EMAIL_ADDRESS).setTelNum(PHONE_NUM).setMsg(content).setTitle(
                        "call chain alarm").setMethod(method).setTime(timestamp);
//            alarmChain.setAlarmParam(ap);
//            AsyncExecutor.getInstance().execute(alarmChain);

        } catch (Exception e) {
            log.error("alarm inteface error!");
        }
    }
}
