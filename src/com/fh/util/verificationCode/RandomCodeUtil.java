package com.fh.util.verificationCode;

import java.util.Random;

/**
 * 说明：随机生成指定几位数的 邀请码，数字+大（小）写字母 或者 纯数字 或者纯大（小）写字母；
 * 创建人：Ajie
 * 创建时间：2019年10月30日15:51:47
 */
public class RandomCodeUtil {

    /**
     * 功能描述：获取随机指定N位数和类型的
     * @author Ajie
     * @date 2019/10/30 0030
     * @param length 几位数
     * @param type 类型（0：随机大小写字母+数字、1：纯数字、2：纯小写字母、3：纯大写字母、4：随机大小写字母、5：随机小写字母+数字、6：随机大写字母+数字）
     * @return string
     */
   public static String getInvitaCode(int length, int type) {
       // 实例化可变的字符序列
       StringBuilder sb = new StringBuilder();
       // 随机类
       Random random = new Random();

       for (int i = 0; i < length; i++) {
           // 输入字母还是数字
           String charOrNum = getType(type, random);
           if ("char".equalsIgnoreCase(charOrNum)) {
               // 字符串 取得大写字母还是小写字母
               int choine = getSize(type,random);
               // 随机26个字母 添加到字符缓冲器
               sb.append((char) (choine + random.nextInt(26)));
           }
           if ("num".equalsIgnoreCase(charOrNum)) {
               // 数字 随机 0-9
               sb.append(random.nextInt(10));
           }
       }
       return sb.toString();
   }

    /**
     * 功能描述：判断随机类型
     * @author Ajie
     * @date 2019/10/30 0030
     */
    public static String getType(int type, Random random) {
        String result = "";
        // 随机大小写字母+数字
        if (type == 0 || type == 5 || type == 6) {
            result = random.nextInt(2) % 2 == 0 ? "char" : "num";
        }
        // 随机纯数字
        if (type == 1) {
            result = "num";
        }
        // 随机纯小写、大写字母、大小字母
        if (type >= 2 && type <= 4) {
            result = "char";
        }
        return result;
    }

    /**
     * 功能描述：判断大小
     * @author Ajie
     * @date 2019/10/30 0030
     */
    public static int getSize(int type, Random random) {
        int result = 0;
        // 随机大小写字母
        if (type == 0 || type == 4) {
            result = random.nextInt(2) % 2 == 0 ? 65 : 97;
        }
        // 小写字母
        if (type == 2 || type == 5) {
            result = 97;
        }
        // 大写字母
        if (type == 3 || type == 6) {
            result = 65;
        }
        return result;
    }




}
