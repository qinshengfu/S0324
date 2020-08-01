package com.fh.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 说明：日期处理
 * 创建人：Ajie
 * 修改时间：2019年12月21日18:23:01
 */
public class DateUtil {

    private final static SimpleDateFormat SDF_YEAR = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat SDF_DAYS = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat SDF_TIMES = new SimpleDateFormat("yyyyMMddHHmmss");

    private final static long nd = 1000 * 24 * 60 * 60;
    private final static long nh = 1000 * 60 * 60;
    private final static long nm = 1000 * 60;
    private final static long ns = 1000;

    // cron 格式
    // 指定时间执行一次
    private final static String DATEFORMAT = "ss mm HH dd MM ? yyyy";
    // 指定每天的什么时候
    private final static String DATEFORMAT_DAY = "ss mm HH * * ? *";

    private final static String DATEFORMAT1 = "ss mm HH dd MM ? yyyy";

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getSdfTimes() {
        return SDF_TIMES.format(new Date());
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return SDF_YEAR.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return SDF_DAY.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return SDF_DAYS.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return SDF_TIME.format(new Date());
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     * @author fh
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //long aa=0;
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr 开始时间
     * @param endDateStr 结束时间
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        //System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     *
     * @param nowTine 现在时间
     * @param days 天数
     * @return yyyy-MM-dd HH:mm:ss格式的时间
     */
    public static String getAfterDayDate(String nowTine, String days) {
        Date date = Tools.str2Date(nowTine);
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        // 设置现在时间
        assert date != null;
        canlendar.setTime(date);
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        date = canlendar.getTime();
        return SDF_TIME.format(date);
    }

    /**
     * 得到n小时之后的日期
     *
     * @param nowTine 现在时间
     * @param hour    小时
     * @return yyyy-MM-dd HH:mm:ss格式的时间
     */
    public static String getAddHourDate(String nowTine, String hour) {
        Date date = Tools.str2Date(nowTine);
        int hoursInt = Integer.parseInt(hour);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        assert date != null;
        // 设置现在时间
        canlendar.setTime(date);
        canlendar.add(Calendar.HOUR, hoursInt); // 日期减 如果不够减会将月变动
        date = canlendar.getTime();
        return SDF_TIME.format(date);
    }

    /**
     * 得到n分钟之后的日期
     *
     * @param nowTine 现在时间
     * @param minute    分钟
     * @return yyyy-MM-dd HH:mm:ss格式的时间
     */
    public static String getAddMinuteDate(String nowTine, String minute) {
        Date date = Tools.str2Date(nowTine);
        int minuteInt = Integer.parseInt(minute);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        assert date != null;
        // 设置现在时间
        canlendar.setTime(date);
        canlendar.add(Calendar.MINUTE, minuteInt); // 日期减 如果不够减会将月变动
        date = canlendar.getTime();
        return SDF_TIME.format(date);
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 校验日期是否大于等于现在时间
     *
     * @param endTime 到期时间
     * @return 大于 返回true 小于 返回 false
     */
    public static boolean isEqualDate(String endTime) {
        // 当前时间
        String nowTime = getTime();
        try {
            //转成long类型比较
            if (SDF_TIME.parse(nowTime).getTime() >= SDF_TIME.parse(endTime).getTime()) {
//                System.out.println("当前时间大于等于到期时间 ");
                return true;
            } else {
//                System.out.println("当前时间小于等于到期时间");
                return false;
            }
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述：把时间根据 分钟 获取相差多少
     *
     * @param endTime 结束时间
     * @return 相差多少分钟
     * @author Ajie
     * @date 2019/12/4 0004
     */
    public static int getMinute(String endTime) {
        int result = 5;
        java.util.Date now;
        try {
            now = new Date();
            java.util.Date date = SDF_TIME.parse(endTime);
            long times = now.getTime() - date.getTime();
            long day = times / nd;
            long hour = times % nd / nh;
            long min = times % nd % nh / nm;
            long sec = times % nd % nh % nm / ns;
            result = (int) min;
        } catch (ParseException e) {
            result = 10;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 功能描述：把时间根据 小时 获取相差多少
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 相差多少小时
     * @author Ajie
     * @date 2019/12/4 0004
     */
    public static int getHour1(String startTime, String endTime) {
        int result = -1;
        Date staTime;
        try {
            staTime = SDF_TIME.parse(startTime);
            Date endDate = SDF_TIME.parse(endTime);
            long times = endDate.getTime() - staTime.getTime();
            long hour = times / nh;
            result = (int) hour;
        } catch (ParseException e) {
            result = 999;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 功能描述：把时间根据 小时 获取相差多少
     *
     * @param startTime 开始时间
     * @return 相差多少小时
     * @author Ajie
     * @date 2019/12/4 0004
     */
    public static int getHour(String startTime) {
        int result = -1;
        Date date = new Date();
        try {
            Date oidTime = SDF_TIME.parse(startTime);
            long times = date.getTime() - oidTime.getTime();
            long hour = times / nh;
            result = (int) hour;
        } catch (ParseException e) {
            result = 999;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 日期转cron表达式函数
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * @param cron       表达式
     * @param dateFormat 时间格式
     * @return YYYY-MM-DD HH:mm:ss 字符串
     * @throws ParseException
     */
    public static String parseStringToDate(String cron, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        if (cron != null) {
            date = sdf.parse(cron);
        }
        return Tools.date2Str(date);
    }

    /**
     * 将日期转换为cron,每天的什么时候执行
     *
     * @param date : 时间点
     * @return 转换后的cron表达式
     */
    public static String getCronDay(String date) {
        Date date1 = Tools.str2Date(date);
        return formatDateByPattern(date1, DATEFORMAT_DAY);
    }

    /**
     * 将日期转换为cron,只执行一次的表达式
     *
     * @param date : 时间点
     * @return 转换后的cron表达式
     */
    public static String getCron(String date) {
        Date date1 = Tools.str2Date(date);
        return formatDateByPattern(date1, DATEFORMAT);
    }

    /**
     * 将cron转换为date
     *
     * @param cron : cron表达式 cron表达式仅限于周为*
     * @return 返回YYYY-MM-DD HH:mm:ss格式
     */
    public static String getDate(String cron) throws ParseException {
        return parseStringToDate(cron, DATEFORMAT1);
    }

    /**
     * 功能描述：是否处于时间段内最终判断
     *
     * @param nowTime   当前时间
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 确认返回 【true】
     * @author Ajie
     * @date 2019/12/21 0021
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能描述：判断是否处于时间段内
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 处于时间段内 【true】
     * @author Ajie
     * @date 2019/12/21 0021
     */
    public static boolean isBelongTime(String beginTime, String endTime) {
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        // 初始化
        Date nowTime = null;
        Date beginDate = null;
        Date endDate = null;
        try {
            //格式化当前时间格式
            nowTime = df.parse(df.format(new Date()));
            //定义开始时间
            beginDate = df.parse(beginTime);
            //定义结束时间
            endDate = df.parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return belongCalendar(nowTime,beginDate,endDate);
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(getAddHourDate("2019-12-09 10:43:52", "1"));
        System.out.println(getHour1("2019-12-09 08:43:34", "2019-12-09 10:45:45"));

    }

}
