package com.juyou.wx.util.cache;

import java.util.Random;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-29
 */
public final class CacheExpiredUtil {

    private final static int FORMAT_MINUTE = 60;
    private final static int FORMAT_HOUR = 3600;
    private final static int FORMAT_DAY = 86400;
    private final static int FORMAT_WEEK = 604800;
    private final static int FORMAT_MONTH = 2592000;
    private final static int FORMAT_YEAR = 31104000;

    private static long getRandom(int bound) {
        return new Random().nextInt(bound);
    }

    public static long getSecond(int second) {
        return second;
    }

    public static long getMinute(int minute) {
        return minute * FORMAT_MINUTE + getRandom(10);
    }

    public static long getHour(int hour) {
        return hour * FORMAT_HOUR + getRandom(100);
    }

    public static long getDay(int day) {
        return day * FORMAT_DAY + getRandom(100);
    }

    public static long getWeek(int week) {
        return week * FORMAT_WEEK + getRandom(100);
    }

    public static long getMonth(int month) {
        return month * FORMAT_MONTH + getRandom(1000);
    }

    public static long getYear(int year) {
        return year * FORMAT_YEAR + getRandom(1000);
    }

}
