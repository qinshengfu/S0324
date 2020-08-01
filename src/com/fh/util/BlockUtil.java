package com.fh.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：区块链基础工具类
 * 创建人：Ajie
 * 创建时间：2019年12月25日09:48:53
 */
@Data
public class BlockUtil {

    private static final String APPID = "LU28681585363944";
    // 用户名
    private static final String USER_NAME = "s0324";
    // 钱包密码
    private static final String PASSWORD = "s0324......";
    // 用户密码加密
    private static String USER_PASSWORD = MD5.md5(PASSWORD);
    // 签名 用户名+密码哈希+APPID
    private static String SIGN = MD5.md5(USER_NAME + USER_PASSWORD + APPID);

    // usdt 合约地址
    private static final String CONTRACT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7";
    // 密令
    private static final String API_KEY = "HFFSRZGHBKH1IZFT28771X9WMBAA7N1V81";
    // 新增钱包api
    private static final String ADD_API = "http://node3.bbw-rj.com/index.php/index/geth/newAccount_post";
    // 查询余额api
    private static final String CHA_API = "http://node3.bbw-rj.com/index.php/index/geth/ethnumber";
    // 区块的查余额api 需要外网支持
    private static final String USDT_API = "https://api.etherscan.io/api";
    // 公司的代转币api
    private static final String TRANSFER_API = "http://node3.bbw-rj.com/index.php/index/geth/newsendTokenTransaction";
    // 交易状态的API
    private static final String TRADING_STATUS_API = "http://node3.bbw-rj.com/index.php/index/geth/order_status";


    /**
     * 功能描述：新增钱包地址
     *
     * @return 钱包地址
     * @author Ajie
     * @date 2019/12/25 0025
     */
    public static String addAddress() {
        String result;
        // 拼接地址
        String param = "appid=" + APPID + "&username=" + USER_NAME + "&pass=" + PASSWORD + "&sign=" + SIGN + "&userpassword=" + USER_PASSWORD;
        // 访问链接
        result = HttpRequest.sendGet(ADD_API, param);
        // 转map格式
        PageData map = JSON.parseObject(result, PageData.class);
        // 获取钱包地址
        return map.get("address").toString();
    }

    /**
     * 功能描述：公司的api查询钱包余额
     *
     * @param address 钱包地址
     * @return eth_numbers 以太坊余额 、 token_numbers 合约币余额
     * @author Ajie
     * @date 2019/12/25 0025
     */
    public static PageData selectBalance(String address){
        // 拼接地址
        String param = "appid=" + APPID + "&username=" + USER_NAME + "&sign=" + SIGN + "&address=" + address + "&contractAddress=" + CONTRACT_ADDRESS + "&userpassword=" + USER_PASSWORD;
        String result = HttpRequest.sendGet(CHA_API, param);
        // 转map格式
        return JSON.parseObject(result, PageData.class);
    }

    /**
     * 功能描述：区块的api查询钱包余额
     *
     * @param address 钱包地址
     * @return 余额
     * @author Ajie
     * @date 2019/12/25 0025
     */
    public static BigDecimal usdtBalance(String address) {
        USER_PASSWORD = MD5.md5(PASSWORD);
        SIGN = MD5.md5(USER_NAME + USER_PASSWORD + APPID);
        String result;
        // 拼接地址
        String param = "module=account&action=tokenbalance&contractaddress=" + CONTRACT_ADDRESS + "&address=" + address + "&tag=latest&apikey=" + API_KEY;
        result = HttpRequest.sendGet(USDT_API, param);
        // 转map格式
        HashMap map = JSON.parseObject(result, HashMap.class);
        String money = StrUtil.isBlank(map.get("result").toString()) ? "0" : StrUtil.toString(map.get("result")) ;
        // 返回余额
        return NumberUtil.div(money, "1000000");
    }

    /**
     * 功能描述：公司的代转币API (转USDT)
     *
     * @param from_address 转出钱包地址
     * @param to_address   转入钱包地址
     * @param tr_value     转出数量
     * @return map ["statuses": 0失败；1成功 "msg" : 返回信息 "orderox" : 交易哈希 "ramount" 交易金额 ]
     * @author Ajie
     * @date 2019/12/25 0025
     */
    public static PageData usdtTransfer(String from_address, String to_address, double tr_value) {
        tr_value /= Math.pow(10, 12);
        USER_PASSWORD = MD5.md5(PASSWORD);
        SIGN = MD5.md5(USER_NAME + USER_PASSWORD + APPID);
        String result;
        // 拼接地址
        String param = "appid=" + APPID + "&username=" + USER_NAME + "&pass=" + PASSWORD + "&sign=" + SIGN
                + "&from_address=" + from_address + "&to_address=" + to_address + "&tr_value=" + tr_value
                + "&contractAddress=" + CONTRACT_ADDRESS + "&userpassword=" + USER_PASSWORD;
        result = HttpRequest.sendGet(TRANSFER_API, param);
        // 转map格式
        return JSON.parseObject(result, PageData.class);
    }

    /**
     * 功能描述：公司的代转币API (转ETH)
     *
     * @param from_address 转出钱包地址
     * @param to_address   转入钱包地址
     * @param tr_value     转出数量
     * @return map ["statuses": 0失败；1成功 "msg" : 返回信息 "orderox" : 交易哈希 "ramount" 交易金额 ]
     * @author Ajie
     * @date 2019/12/25 0025
     */
    public static PageData usdtTransferEth(String from_address, String to_address, double tr_value) {
        tr_value /= Math.pow(10, 12);
        USER_PASSWORD = MD5.md5(PASSWORD);
        SIGN = MD5.md5(USER_NAME + USER_PASSWORD + APPID);
        String result;
        // 拼接地址
        String param = "appid=" + APPID + "&username=" + USER_NAME + "&pass=" + PASSWORD + "&sign=" + SIGN
                + "&from_address=" + from_address + "&to_address=" + to_address + "&tr_value=" + tr_value
                + "&userpassword=" + USER_PASSWORD;
        result = HttpRequest.sendGet(TRANSFER_API, param);
        // 转map格式
        return JSON.parseObject(result, PageData.class);
    }

    /**
     * 功能描述：获取交易状态
     *
     * @param txhash 交易哈希
     * @return map["statuses"：0交易中；1成功；2失败， msg:返回信息]
     * @author Ajie
     * @date 2020/1/2 0002
     */
    public static PageData getTradingStatus(String txhash) {
        String result;
        // 拼接地址
        String param = "&appid=" + APPID + "&username=" + USER_NAME + "&sign=" + SIGN + "&txhash=" + txhash + "&userpassword=" + USER_PASSWORD;
        result = HttpRequest.sendGet(TRADING_STATUS_API, param);
        // 转map格式
        return JSON.parseObject(result, PageData.class);
    }


    public static void main(String[] args) {
//        String add = addAddress();
//        System.out.println("新增钱包地址：" + add);
        String address = "0x9305d08c7cd6a172448714167a54c8998f103aea";
        String to_address = "0x9f08221c98b721cecb7b9b9173111ea936f20487";

//        System.out.println("区块链api查询余额地址： " + usdtBalance(address));

//        System.out.println("公司api查询余额地址： " + selectBalance(to_address));


//        System.out.println("公司代转币api回调信息：" + usdtTransferEth(address, to_address, 0.0001));

        System.out.println("交易状态：" + getTradingStatus("0x7ce9ed9d02674f8c775cb6ddab578788f8b5d5f8507ffd1f91b31d9bcaa9ca84"));


    }

    @Test
    public void tetst() {
        double a = 10;
        double b = 16;

        double result = Math.pow(a, b);

        System.out.println(result);

    }

}
