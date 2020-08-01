import com.fh.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 说明：判断是否处于某个时间段内
 * 创建人：Ajie
 * 创建时间：2019-12-21 17:49:21
 */
public class TimeTest {


    /**
     * 功能描述：是否处于时间段内
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

    public static void main(String[] args) {
        String time = "08:00:00 - 22:00:00";

        String[] timeSlot = StringUtil.strList2(time);
        for (String pd : timeSlot) {
            System.out.println(pd);
        }

        //调用判断方法
        boolean flag = isBelongTime(timeSlot[0],timeSlot[1]);
        //输出为结果
        if (flag) {
            //处于规定的时间段内，执行对应的逻辑代码
            System.out.println(flag);
        } else {
            //不处于规定的时间段内，执行对应的逻辑代码
            System.out.println(flag);
        }

    }
}