import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.verificationCode.RandomCodeUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 说明：测试总入口
 * 创建人：Ajie
 * 创建时间：2019年10月30日17:20:12
 */
public class MainTest {

    // 创建slf4j 日志
    final Logger log = LoggerFactory.getLogger(MainTest.class);


    /**
     * 功能描述：定时任务测试
     *
     * @author Ajie
     * @date 2019/12/3 0003
     */
    public static void main(String[] args) {

        PageData pd = new PageData();
        pd.put("1", 1);
        pd.put("2", 2);

        Map<String, String> map = new HashMap<>();
        


        // 添加一个定位任务 每隔一分钟执行一次
//        QuartzManager.addJob("test", StaticBonusRelease.class, "0 */1 * * * ?");

        // 启动所有定时任务
//        QuartzManager.startJobs();

        // 移除定时任务
//        QuartzManager.removeJob("测试");

//        // 关闭所有定时任务
//        QuartzManager.removeJob("staticRewardTask");
    }


    /**
     * 功能描述：生成随机N位邀请码和类型测试
     *
     * @author Ajie
     * @date 2019/10/30 0030
     */
    @Test
    public void randomTest() {
        String result1 = RandomCodeUtil.getInvitaCode(15, 0);
        String result2 = RandomCodeUtil.getInvitaCode(14, 1);
        String result3 = RandomCodeUtil.getInvitaCode(13, 2);
        String result4 = RandomCodeUtil.getInvitaCode(12, 3);
        String result5 = RandomCodeUtil.getInvitaCode(11, 4);
        String result6 = RandomCodeUtil.getInvitaCode(10, 5);
        String result7 = RandomCodeUtil.getInvitaCode(9, 6);


        log.info("随机15位数（大小字母+数字）：{}", result1);
        log.info("随机14位数（纯数字）：{}", result2);
        log.info("随机13位数（纯小字母）：{}", result3);
        log.info("随机12位数（纯大字母）：{}", result4);
        log.info("随机11位数（大小字母）：{}", result5);
        log.info("随机10位数（小字母+数字）：{}", result6);
        log.info("随机9位数（大字母+数字）：{}", result7);

    }

    /**
     * 功能描述：邮箱验证码测试
     *
     * @author Ajie
     * @date 2019/10/31 0031
     */
    @Test
    public void EmailTest() throws Exception {
        String email = "1243206485@qq.com";
        //获得系统的时间，单位为毫秒,转换为妙
        long startTime = System.currentTimeMillis() / 60 % 60;
        long endTime = startTime + 2;
        log.info("开始时间：{}分，结束时间：{}分，有效期：{}分钟", startTime, endTime, endTime - startTime);

        long validity = endTime - startTime;


      /*  String code = (String) Jurisdiction.getSession().getAttribute(Const.SESSION_SECURITY_CODE);
        log.info("本次邮箱验证码：{}",code);*/
    }

    /**
     * 功能描述：通过System.currentTimeMillis 获取当前时、分、秒
     *
     * @author Ajie
     * @date 2019/10/31 0031
     */
    @Test
    public void timeMillisTest() {
        //获得系统的时间，单位为毫秒,转换为妙
        long totalMilliSeconds = System.currentTimeMillis();
        long totalSeconds = totalMilliSeconds / 1000;
        //求出现在的秒
        long currentSecond = totalSeconds % 60;
        //求出现在的分
        long totalMinutes = totalSeconds / 60;
        long currentMinute = totalMinutes % 60;
        //求出现在的小时(北京时间)
        long totalHour = totalMinutes / 60;
        long currentHour = totalHour % 24 + 8;
        DateUtil.getTime();
        //显示时间
        log.info("总毫秒 / 1000 为：{}", totalSeconds);
        log.info("总毫秒为：{}", totalMilliSeconds);
        log.info("现在的小时：{}，现在的分：{}，现在的秒：{}", currentHour, currentMinute, currentSecond);
    }

    /**
     * 功能描述：随机数校验
     *
     * @author Ajie
     * @date 2019/11/15 0015
     */
    @Test
    public void mathRandomTest() {
        for (int j = 0; j < 10; j++) {
            int i = (int) (Math.random() * 3) + 1;
            System.out.println(i);
        }

    }

    /**
     * 功能描述：判断字符串是否包含某个字符
     *
     * @author Ajie
     * @date 2019/12/21 0021
     */
    @Test
    public void containsStr() {

        String a = "1,2,3,4,5,6,7,8,9";
        String b = "1";
        String c = "3";
        // 方法一 java.lang.String.contains() 方法返回true，当且仅当此字符串包含指定的char值序列
        boolean result = a.contains(b);
        if (result) {
            System.out.println("包含：" + result);
        } else {
            System.out.println("不包含： " + result);
        }

        // 方法二 java.lang.String.indexOf() 的用途是在一个字符串中寻找一个字的位置，同时也可以判断一个字符串中是否包含某个字符。

        int result1 = a.indexOf(c);
        if (-1 != result1) {
            System.out.println("包含：" + result1);
        } else {
            System.out.println("不包含： " + result1);
        }


    }

    /**
     * 功能描述： 获取int类型的数字 有多少位
     *
     * @author Ajie
     * @date 2020/2/5 0005
     */
    @Test
    public void intSize() {
        int b = 0;
        for (int i = 0; i < 3; i++) {
            b += i;
            int a = String.valueOf(b * 10).length();
            System.out.println(a + " ::: " + b * 10);
            b *= 10;
        }

    }

    /**
     * 功能描述：判断字符长度
     *
     * @author Ajie
     * @date 2020/2/6 0006
     */
    @Test
    public void strSize() {
        String a = "2020-02-06 22:13:45";
        System.out.println(a.length());
        System.out.println(a.substring(a.length() - 8));

    }


}