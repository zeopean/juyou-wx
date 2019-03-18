package com.juyou.wx.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用的时间方法
 * 使用joda-time，线程安全。
 *
 * @author zeopean
 */
public class DateTimeUtil {


    public static String DAY_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DT_INT_FORMAT = "yyyyMMdd";
    public static String DT_LONG_FORMAT = "yyyyMMddHHmmss";
    private static String CN_TIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    private static String CN_DAY_FORMAT = "yyyy年MM月dd日";


    private static DateTimeFormatter dayFormater = DateTimeFormat.forPattern(DAY_FORMAT);
    private static DateTimeFormatter dayToIntFormater = DateTimeFormat.forPattern(DT_INT_FORMAT);
    private static DateTimeFormatter timeFormater = DateTimeFormat.forPattern(TIME_FORMAT);
    private static DateTimeFormatter dtLongFormater = DateTimeFormat.forPattern(DT_LONG_FORMAT);


    public static Date nextYear(Date curDate) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.plusYears(1);
        return dateTime.toDate();
    }

    public static Date nextMonth(Date curDate) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.plusMonths(1);
        return dateTime.toDate();
    }

    public static Date nextDate(Date curDate) {
        return getSomeDate(curDate, 1);
    }

    public static Date prevWeek(Date curDate) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusWeeks(-1);
        return dateTime.toDate();
    }

    public static Date prevDate(Date curDate) {
        return getSomeDate(curDate, -1);
    }

    public static Date getSomeDate(Date curDate, Integer days) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.plusDays(days);
        return dateTime.toDate();
    }

    public static Date getSomeHour(Date curDate, Integer hours) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.plusHours(hours);
        return dateTime.toDate();
    }

    public static Date getSomeMonth(Date curDate, Integer months) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.plusMonths(months);
        return dateTime.toDate();
    }

    public static Date monthFirstDate(Date curDate) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.dayOfMonth().withMinimumValue();
        return dateTime.toDate();
    }

    public static Date monthFinalDate(Date curDate) {
        DateTime dateTime = new DateTime(curDate);
        dateTime = dateTime.dayOfMonth().withMaximumValue();
        return dateTime.toDate();
    }

    public static String timeFormatDateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(TIME_FORMAT);
    }



    /**
     * 包含中文的时间格式 yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param date
     * @return
     */
    public static String timeFormatDateToCNString(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(CN_TIME_FORMAT);
    }

    /**
     * 见 yyyy-mm-dd HH:mm:ss 转成 yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param dateTimeStr
     * @return
     */
    public static String timeFormatDateTimeStringToCNString(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        }
        Date date = timeFormatStringToDate(dateTimeStr);
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(CN_TIME_FORMAT);
    }

    /**
     * 见 yyyy-mm-dd HH:mm:ss 转成 yyyy年MM月dd日
     *
     * @param dateTimeStr
     * @return
     */
    public static String timeFormatDayStringToCNString(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        }
        Date date = timeFormatStringToDate(dateTimeStr);
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(CN_DAY_FORMAT);
    }

    public static String dayFormatDateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(DAY_FORMAT);
    }

    public static String dtLongFormatDateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(DT_LONG_FORMAT);
    }

    public static Date timeFormatStringToDate(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        return timeFormater.parseDateTime(str).toDate();
    }

    public static Date dayFormatStringToDate(String str) {
        return dayFormater.parseDateTime(str).toDate();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDateTime() {
        return new Date();
    }

    /**
     * 将日期字符串转为int形式，eg. 2016-05-05 -> 20160505
     *
     * @param datestr
     */
    public static int dayFormatStringToInt(String datestr) {
        DateTime dt = dayFormater.parseDateTime(datestr);
        String yyyyMMdd = dt.toString(dayToIntFormater);
        return Integer.valueOf(yyyyMMdd);
    }

    /**
     * 将日期字符串转为int形式，eg. 2016-05-05 00:00:00 -> 20160505000000
     *
     * @param datestr
     */
    public static long dayFormatStringToLong(String datestr) {
        DateTime dt = dayFormater.parseDateTime(datestr);
        String yyyyMMdd = dt.toString(dtLongFormater);
        return Long.valueOf(yyyyMMdd);
    }

    /**
     * 将日期字符串转为int形式，eg. 2016-05-05 -> 20160505
     *
     * @param datestr
     * @param addDay  增加天数
     * @return int
     */
    public static int dayFormatStringToInt(String datestr, int addDay) {
        if (addDay == 0) {
            return dayFormatStringToInt(datestr);
        }
        DateTime dt = dayFormater.parseDateTime(datestr);
        dt = dt.plusDays(addDay);
        String yyyyMMdd = dt.toString(dayToIntFormater);
        return Integer.valueOf(yyyyMMdd);
    }

    /**
     * 将int形式的日期转为日期字符串，eg. 20160505 -> 2016-05-05
     *
     * @param dateint
     */
    public static String dayFormatIntToString(int dateint) {
        String yyyyMMdd = String.valueOf(dateint);
        DateTime dt = dayToIntFormater.parseDateTime(yyyyMMdd);
        return dayFormater.print(dt);
    }

    /**
     * 将int形式的日期转为时间戳字符串，eg. 20160505 -> 2016-05-05 00:00:00
     *
     * @param dateint
     */
    public static String timestampFormatIntToString(int dateint) {
        String yyyyMMdd = String.valueOf(dateint);
        DateTime dt = dayToIntFormater.parseDateTime(yyyyMMdd);
        return timeFormater.print(dt);
    }

    public static long timeFormatIntToString(String dateStr) {
        DateTime dt = timeFormater.parseDateTime(dateStr);
        String yyyyMMddHHmmss = dt.toString();
        return Long.valueOf(yyyyMMddHHmmss);
    }


    /**
     * 将Date类型日期转为int形式
     *
     * @param date
     */
    public static int dayFormatDateToInt(Date date) {
        String dateStr = dayFormatDateToString(date);
        return dayFormatStringToInt(dateStr);
    }

    /**
     * 将Date类型日期转为long形式
     *
     * @param date
     */
    public static long dayFormatDateToLong(Date date) {
        String dateStr = new DateTime(date).toString(DT_LONG_FORMAT);
        return Long.valueOf(dateStr);
    }


    /**
     * 将int类型转为Date形式
     *
     * @param dateint
     */
    public static Date dayFormatIntToDate(int dateint) {
        String dateStr = dayFormatIntToString(dateint);
        return DateTimeUtil.dayFormatStringToDate(dateStr);
    }


    public static Date dtLongFormatStringToDate(String str) {
        return dtLongFormater.parseDateTime(str).toDate();
    }

    // 传入自定义格式日期类型，返回jdk Date
    public static Date fromStr(String date, String format) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.parseDateTime(date).toDate();
    }

    public static Date dayFormat(Date date) {
        String dayStr = dayFormatDateToString(date);
        return dayFormatStringToDate(dayStr);
    }

    public static DateTime withTimeEndAtDay(DateTime dateTime) {
        return dateTime.withTime(23, 59, 59, 999);
    }

    public static Date withTimeAtEndOfDay(Date date) {
        return new DateTime(date).withTime(23, 59, 59, 999).toDate();
    }

    public static Date withTimeAtStartfDay(Date date) {
        return new DateTime(date).withTime(0, 0, 0, 0).toDate();
    }

    public static Date withTimeAtEndOfWeek(Date date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withDayOfWeek(DateTimeConstants.SUNDAY);
        return withTimeAtEndOfDay(dateTime.toDate());
    }

    public static final boolean isEndOfWeek(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfWeek().get() == DateTimeConstants.SUNDAY;
    }

    public static final boolean isFirstOfWeek(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfWeek().get() == DateTimeConstants.MONDAY;
    }

    public static final boolean isDayOfWeek(Date date, int num) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfWeek().get() == num;
    }

    public static final boolean isEndOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        int lastDay = dateTime.dayOfMonth().getMaximumValue();
        return lastDay == dateTime.dayOfMonth().get();
    }

    public static int getDateRange(Date d1, Date d2) {
        DateTime dateTime1 = new DateTime(d1);
        DateTime dateTime2 = new DateTime(d2);
        Period p = new Period(dateTime1, dateTime2, PeriodType.days());
        return p.getDays();
    }

    public static int getDateRange(int d1, int d2) {
        Date date1 = dayFormatIntToDate(d1);
        Date date2 = dayFormatIntToDate(d2);
        return getDateRange(date1, date2);
    }

    /**
     * 判断时间是否超过指定的天数
     *
     * @param days
     * @param date
     * @return
     */
    public static boolean isNowOverDays(int days, Date date) {
        long time = date.getTime();
        long step = days * 24 * 3600 * 1000;
        // 获取现在的时间
        long nowTime = System.currentTimeMillis();
        if (nowTime > (time + step)) {
            return true;
        }
        return false;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        return nowTime.after(beginTime) && nowTime.before(endTime);
    }

    /**
     * 获取几天前的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获取几天后的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();

    }

    /**
     * 验证日期格式是否正确
     *
     * @param formatDate
     * @param str
     * @return
     */
    public static boolean isValidDate(String formatDate, String str) {
        boolean convertSuccess = true;
        // 指定的日期格式
        SimpleDateFormat format = new SimpleDateFormat(formatDate);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);

        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


}
