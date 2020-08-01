import com.google.gson.GsonBuilder;
import entity.Block;

import java.util.ArrayList;

/**
 * 说明：比特币区块链测试
 * 创建人：Ajie
 * 创建时间：2019年11月23日10:07:16
 */
public class NoobChainTest {

    // 初始化区块链
    private static ArrayList<Block> blockChain = new ArrayList<Block>(18);
    // 初始化挖矿难度
    private static int difficulty = 3;

    public static void main(String[] args) {

        // 将我们的区块添加到区块链阵列列表
        blockChain.add(new Block("Hi im the first block", "0"));
        System.out.println("试图开采1号区块... ");
        blockChain.get(0).minBlock(difficulty);

        blockChain.add(new Block("Yo im the second block", blockChain.get(blockChain.size()-1).hash));
        System.out.println("试图开采2号区块... ");
        blockChain.get(1).minBlock(difficulty);

        blockChain.add(new Block("Hey im the third block", blockChain.get(blockChain.size()-1).hash));
        System.out.println("试图开采3号区块... ");
        blockChain.get(2).minBlock(difficulty);

        System.out.println("\n区块链是否有效: " + isChainValid());

        // 把数组格式化成【JSON】方式
        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\n区块链：");
        System.out.println(blockChainJson);

    }

    /**
     * 功能描述：检查区块链是否完整
     * @author Ajie
     * @date 2019/11/23 0023
     * @return 完整返回：true 否则返回：false
     */
    public static Boolean isChainValid() {
        // 当前块
        Block currentBlock;
        // 前一个块
        Block previousBlock;

        // 通过区块链循环检查哈希
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
            // 比较注册哈希和计算哈希
            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("当前哈希值不相等");
                return false;
            }
            // 比较上一个哈希和注册的上一个哈希
            if (!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("以前的哈希值不相等");
                return false;
            }
        }
        return true;
    }



}
