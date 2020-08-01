package entity;

import com.fh.util.MD5;
import org.junit.Test;

/**
 * 说明：区块链钱包测试
 * 创建人：Ajie
 * 创建时间：2019年12月24日18:02:11
 */
public class usdtTest {

    /**
     * 功能描述：添加钱包地址
     * @author Ajie
     * @date 2019/12/24 0024
     * @param
     * @return
     */
    @Test
    public void addWalletAddress() {
        // 新增钱包api
        String api = "http://node3.bbw-rj.com/index.php/index/geth/newAccount_post";
        String appid = "XS23221577180508";
        // 用户名
        String userName = "Ajie";
        // 钱包密码
        String pass = "1";
        // 用户密码加密 哈希
        String userPassword = MD5.md5(pass);
        // 签名 用户密码哈希+appid
        String sign = MD5.md5(userName + userPassword + appid);
        // 拼接地址
        api = api + "?appid=" + appid + "&username=" + userName + "&pass=" + pass + "&sign=" + sign + "&userpassword=" + userPassword;
        System.out.println(api);

        // 合约地址
        String contractAddress = "0xdac17f958d2ee523a2206206994597c13d831ec7";
        // 钱包地址
        String address = "0xe3c2dabdb21a2682f86f88f76ca71209ea5d92fb";
        // 密令
        String apikey = "HFFSRZGHBKH1IZFT28771X9WMBAA7N1V81";
        // 查询余额api
        String chaApi = "http://node3.bbw-rj.com/index.php/index/geth/ethnumber";
        // 拼接地址
        chaApi = chaApi + "?appid=" + appid + "&username=" + userName + "&sign=" + sign + "&address=" + address + "&contractAddress=" + contractAddress + "&userpassword=" + userPassword;

        System.out.println("查币地址： " + chaApi);

        // 区块的查余额api
        String usdtApi = "https://api.etherscan.io/api";
        // 拼接地址
        usdtApi = usdtApi + "?module=account&action=tokenbalance&contractaddress=" + contractAddress + "&address=" + address + "&tag=latest&apikey=" + apikey;
        System.out.println("区块查余额地址： " + usdtApi);
    }



}

