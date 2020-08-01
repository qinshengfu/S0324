import com.fh.util.RedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：Redis缓存测试
 * 创建人：Ajie
 * 创建时间：2019年11月8日11:51:38
 */
public class RedisTest {


    /**
     * 功能描述：redis性能测试
     * @author Ajie
     * @date 2019/11/8 0008
     */
    @Test
    public void redisTester() {
        Jedis jedis = RedisUtil.getJedis();
        int i = 0;
        try {
            long start = System.currentTimeMillis();// 开始毫秒数
            while (true) {
                long end = System.currentTimeMillis();
                if (end - start >= 1000) {// 当大于等于1000毫秒（相当于1秒）时，结束操作
                    break;
                }
                i++;
                jedis.set("test" + i, i + "");
            }
            assert jedis != null;
            System.out.println(jedis.get("test666"));
        } finally {// 关闭连接
            RedisUtil.returnResource(jedis);
        }
        // 打印1秒内对Redis的操作次数
        System.out.println("redis每秒操作：" + i + "次");

    }

    /**
     * 功能描述：redis乐观锁测试
     * @author Ajie
     * @date 2019/11/13 0013
     */
    public static void main(String[] args) {

        long starTime = System.currentTimeMillis();
        initPrduct();
        initClient();
        printResult();

        long endTime = System.currentTimeMillis();
        long Time = endTime - starTime;
        System.out.println("程序运行时间： "+Time/1000+"s");
    }


    /**
     * 输出结果
     */
    public static void printResult() {
        Jedis jedis = RedisUtil.getJedis();
        Set<String> set = jedis.smembers("clientList");

        int i = 1;
        for (String value : set) {
            System.out.println("第" + i++ + "个抢到商品，"+value + " ");
        }

        RedisUtil.returnResource(jedis);
    }

    /**
     * 初始化顾客开始抢商品
     */
    public static void initClient() {
        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 模拟客户数目
        int clientNum = 100;
        for (int i = 0; i < clientNum; i++) {
            cachedThreadPool.execute(new ClientThread(i));
        }
        cachedThreadPool.shutdown();

        while(true){
            if(cachedThreadPool.isTerminated()){
                System.out.println("所有的线程都结束了！");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化商品个数
     */
    public static void initPrduct() {
        // 商品个数
        int prdNum = 2;
        String key = "prdNum";
        // 抢购到商品的顾客列表
        String clientList = "clientList";
        Jedis jedis = RedisUtil.getJedis();

        if (jedis.exists(key)) {
            jedis.del(key);
        }

        if (jedis.exists(clientList)) {
            jedis.del(clientList);
        }

        jedis.set(key, String.valueOf(prdNum));// 初始化
        RedisUtil.returnResource(jedis);
    }

}

/**
 * 顾客线程
 *
 * @author linbingwen
 *
 */
class ClientThread implements Runnable {
    Jedis jedis = null;
    String key = "prdNum";// 商品主键
    String clientList = "clientList";//// 抢购到商品的顾客列表主键
    String clientName;

    public ClientThread(int num) {
        clientName = "编号=" + num;
    }

    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 5000));// 随机睡眠一下
        } catch (InterruptedException e1) {
        }
        while (true) {
            System.out.println("顾客:" + clientName + "开始抢商品");
            jedis = RedisUtil.getJedis();
            try {
                // 开启事务
                jedis.watch(key);
                // 当前商品个数
                int prdNum = Integer.parseInt(jedis.get(key));
                if (prdNum > 0) {
                    // 标记一个事务块的开始
                    Transaction transaction = jedis.multi();
                    transaction.set(key, String.valueOf(prdNum - 1));
                    // 执行事务
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        // 可能是watch-key被外部修改，或者是数据操作被驳回
                        System.out.println("悲剧了，顾客:" + clientName + "没有抢到商品");
                    } else {
                        jedis.sadd(clientList, clientName);// 抢到商品记录一下
                        System.out.println("好高兴，顾客:" + clientName + "抢到商品,当前库存剩余：" + jedis.get(key));
                        break;
                    }
                } else {
                    System.out.println("悲剧了，库存为0，顾客:" + clientName + "没有抢到商品");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 取消 WATCH 命令对所有 key 的监视 (关闭事务)
                jedis.unwatch();
                RedisUtil.returnResource(jedis);
            }

        }
    }

}
