package com.fh.util.verificationCode;

import com.fh.util.Const;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 说明：加减法验证码（不会用！！！） 请更换 VerifyCodeService方法
 * 创建人：Ajie
 * 创建时间：2019年10月30日11:03:45
 */
@Controller
@RequestMapping("/setCode")
public class ValidateCodeUtil  {


    @RequestMapping
    public void generate(HttpServletResponse response){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int code = handleRequestInternal(output);
        System.out.println("加减法验证码："+code);

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute(Const.SESSION_SECURITY_CODE, code);

        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
            out.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private int handleRequestInternal(ByteArrayOutputStream output) {
        // 取随机产生的认证码(4位数字)
        int sRand = 0;
        try {
            // 在内存中创建图象
            int width = 59, height = 20;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 获取图形上下文
            Graphics g = image.getGraphics();
            // 生成随机类
            Random random = new Random();
            // 设定背景色
            g.setColor(getRandColor(200, 250));
            g.fillRect(0, 0, width, height);
            // 设定字体
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
            g.setColor(getRandColor(160, 200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
            // 是加法还是减法
            int math = random.nextInt(2);
            // 加法
            if (math == 0) {
                // 第一个数据
                int rand = random.nextInt(10);
                if (rand == 0) {
                    rand = 1;
                }
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand, 13 * 0 + 6, 16);
                //
                int rand1 = random.nextInt(10);
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand1, 13 * 1 + 6, 16);
                // +号
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("+", 13 * 2 + 6, 16);
                // 第二个数据
                int rand2 = random.nextInt(10);
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand2, 13 * 3 + 6, 16);
                sRand = rand * 10 + rand1 + rand2;
            } else {
                // 减法
                // 第一个数据
                int rand = random.nextInt(10);
                if (rand == 0) {
                    rand = 1;
                }
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand, 13 * 0 + 6, 16);
                //
                int rand1 = random.nextInt(10);
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand1, 13 * 1 + 6, 16);
                // -号
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("-", 13 * 2 + 6 + 3, 16);
                // 第二个数据
                int rand2 = random.nextInt(10);
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.drawString("" + rand2, 13 * 3 + 6, 16);
                sRand = rand * 10 + rand1 - rand2;
            }
            ImageIO.write(image, "jpg", output);
        } catch (Exception e) {
            // logger.error(e.getLocalizedMessage(), e.fillInStackTrace());
            System.out.println("验证码生成错误==》"+e.getLocalizedMessage());
        }
        return sRand;
    }

    // 给定范围获得随机颜色
    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
