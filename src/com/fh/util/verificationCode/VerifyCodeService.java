package com.fh.util.verificationCode;

import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 说明：加减乘验证码服务类，可自行修改 只要 加减
 * 创建人：Ajie
 * 创建时间：2019年10月30日14:21:09
 */

@Service("verifyCode")
public class VerifyCodeService {

    /**
     * 功能描述：获取验证码，存放在session中 KEY：Const.SESSION_SECURITY_CODE
     *
     * @author Ajie
     * @date 2019/10/30 0030
     */
    public BufferedImage getVerify() {
        int width = 60;
        int height = 32;
        // create the image 创建图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color 设置背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border 划定边界
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes 创建一个随机实例来生成代码
        Random rdm = new Random();
        // make some confusion 搞点混乱
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code 生成随机代码
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //计算把验证码存到redis或者session中（此处只提供计算方法）
        int rnd = calc(verifyCode);
        // 存放到session中
        Jurisdiction.getSession().setAttribute(Const.SESSION_SECURITY_CODE, rnd);
        // System.out.println("验证码："+rnd);
        //输出图片
        return image;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * + -
     */
    private String generateVerifyCode(Random rdm) {
        // System.out.println("数组内容："+ Arrays.toString(ops) + "  数组长度："+ops.length);
        // 随机10 以内的数字
        int num1 = rdm.nextInt(20);
        int num2 = rdm.nextInt(20);
        // 随机加还是减
        char op1 = ops[rdm.nextInt(2)];
        return "" + num1 + op1 + num2;
    }

}
