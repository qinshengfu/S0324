package entity;

import com.fh.util.DateUtil;
import com.fh.util.StringUtil;

/**
 * 说明：区块链实体类
 * 创建人：Ajie
 * 创建时间：2019年11月23日09:27:08
 */
public class Block {
    // 自己的数字签名
    public String hash;
    // 前一个块的数字签名
    public String previousHash;
    // 数据块
    private String data; //our data will be a simple message.
    // 时间戳
    private String timeStamp; //as number of milliseconds since 1/1/1970.
    // 挖矿次数
    private int nonce;


    /**
     * 功能描述：有参构造函数
     * @author Ajie
     * @date 2019/11/23 0023
     * @param data 数据块
     * @param previousHash 前一个块的数字签名
     */
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = DateUtil.getTime();
        // 确保在设置其他值后执行此操作。
        this.hash = calculateHash();
    }

    /**
     * 功能描述：调用写好的SHA256方法进行加密
     * @author Ajie
     * @date 2019/11/23 0023
     * @return 数字签名【HASH】
     */
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash + timeStamp +
                        Integer.toHexString(nonce) + data
        );
    }

    /**
     * 功能描述：挖矿（比特币）
     * @author Ajie
     * @date 2019/11/23 0023
     * @param difficulty 难度（1 ~ N）
     */
    public void minBlock(int difficulty) {
        // 创建字符串【难度】*“0”
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("已找到最小的块：" + hash);
    }


}
