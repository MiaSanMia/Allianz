package com.renren.ugc.comment.storm.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xulei
 * @date 2013-8-5
 * @email lei.xu1@renren-inc.com
 * @tags
 */
public class TimeUtils {

    public static final long MINUTE_MS = 60 * 1000;

    public static final long HOUR_MS = MINUTE_MS * 60;

    public static final long DAY_MS = HOUR_MS * 24;

    public static Date lastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date nextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    public static Date lastHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date nextHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        date = calendar.getTime();
        return date;
    }

    public static Date lastMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date nextMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 格式化date，把分和秒清零， 如2013-08-08 12:24:35格式化后结果为2013-08-08 12:00:00
     * 
     * @return
     */
    public static Date formatToHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    /**
     * purge the time precision to the floor hour
     */
    public static long toHourInMillis(long time) {
        return time - time % HOUR_MS;
    }

    /**
     * purge the time precision to the floor minute
     */
    public static long toMinuteInMillis(long time) {
        return time - time % MINUTE_MS;
    }

    /**
     * 格式化date，把秒清零， 如2013-08-08 12:24:35格式化后结果为2013-08-08 12:24:00
     * 
     * @return
     */
    public static Date formatToMin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }

    /**
     * Whether <code>now</code> and <code>time</time> has the same 
     * minute value
     */
    public static boolean hasSameMinute(long now, long time) {
        return ((now / MINUTE_MS) == (time / MINUTE_MS));
    }
    
    /**
     * Whether <code>time</code> is <code>now's</time> next time 
     * minute value
     */
    public static boolean isNextMinute(long now, long time) {
        return ((time / MINUTE_MS) - (now / MINUTE_MS)) == 1;
    }

    /**
     * Whether <code>now</code> and <code>time</time> has the same 
     * hour value
     */
    public static boolean hasSameHour(long now, long time) {
        return ((now / HOUR_MS) == (time / HOUR_MS));
    }

    public static void main(String a[]) {
        Date date = new Date();
        System.out.println(formatToMin(date));
        /*
         * java.sql.Date sqlDate = new java.sql.Date(date.getTime());
         * System.out.println(sqlDate);
         */
    }
}
