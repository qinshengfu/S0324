package com.fh.util.verificationCode;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;

/**
 * 说明：短信验证码，对接 榛子云短信平台
 * 创建人：Ajie
 * 创建时间：2019年10月31日14:49:41
 */
public class PhoneVerificaCodeUtil {

    //短信平台相关参数
    private static final String apiUrl = "https://sms_developer.zhenzikj.com";
    private static final String appId = "103163";
    private static final String appSecret = "YjdmYWMxN2EtYjY2Ny00MWE2LWI4ZDgtZWEzM2E3ODFmM2Iy";


    /**
     * 功能描述：发送短信
     *
     * @param phone -手机号
     * @param verifCode -验证码
     * @param termOfValidity -有效期/分钟
     * @return success -发送成功
     * @author Ajie
     * @date 2019/10/31 0031
     */
    public static String SendSMS(String phone, String verifCode, int termOfValidity) throws Exception {
        JSONObject json = null;
        // 发送短信
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
        String result = client.send(phone, "您的验证码为:" + verifCode + "，该码有效期为"+termOfValidity+"分钟，该码只能使用一次!");
        json = JSONObject.parseObject(result);
        // 发送短信失败
        if (json.getIntValue("code") != 0) {
            return json.getString("data");
        }
        return "success";
    }

    /**
     * 功能描述：查看短信余额
     * @author Ajie
     * @date 2019/10/31 0031
     * @return spare -剩余数额
     */
    public static String getSpare() throws Exception {
        // 实例化短信接口
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
        //  查看短信余额
        String spare = client.balance();
        return spare;
    }

}
