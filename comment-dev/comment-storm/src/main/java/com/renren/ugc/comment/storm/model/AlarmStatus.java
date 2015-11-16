package com.renren.ugc.comment.storm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Record the alarm status
 * 
 * @author jiankuan.xing, created at Aug 22, 2013 5:18:07 PM
 */
public class AlarmStatus implements Serializable {

	private static final long serialVersionUID = -8626830320253782476L;
	/**
     * alarm types
     */
    public static final int UNKNOWN = 0;
    public static final int API_MAX_EXEC_TIME = 1;
    public static final int API_AVG_EXEC_TIME = 2;
    public static final int API_MIN_EXEC_TIME = 3;
    public static final int API_COUNT = 4;

    private Set<String> alarmSet = new HashSet<String>();

    public boolean hasAlarmed(String keyPrefix, int type) {
        String key = getKey(keyPrefix, type);
        return alarmSet.contains(key);
    }

    public void markAlarmed(String keyPrefix, int type) {
        String key = getKey(keyPrefix, type);
        alarmSet.add(key);
    }

    public void resetAlarm(String keyPrefix, int type) {
        String key = getKey(keyPrefix, type);
        alarmSet.remove(key);
    }

    private String getKey(String keyPrefix, int type) {
        return keyPrefix + ":" + type;
    }
}
