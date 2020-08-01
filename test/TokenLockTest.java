import com.fh.util.DateUtil;
import com.fh.util.RedisUtil;
import com.fh.util.Tools;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：测试Redis令牌锁
 * 创建人：Ajie
 * 创建时间：2020年1月17日12:11:39
 */
public class TokenLockTest {

    /**
     * 功能描述：抽象层
     *
     * @author Ajie
     * @date 2020/1/17 0017
     */
    public static void main(String[] args) {
        Jedis jedis = RedisUtil.getJedis();
        // 初始化
        jedis.set("lock", "");
        if (jedis.exists("clientList")) {
            jedis.del("clientList");
        }

        // 如果是空的 说明的第一个访问 释放锁
        if (Tools.isEmpty(jedis.get("lock"))) {
            System.out.println("首次访问，释放锁");
            jedis.set("lock", "0");
        }
        RedisUtil.returnResource(jedis);
        // 并发开始
        initClient();
        // 查看结果
        printResult();

    }

    /**
     * 功能描述：初始化客户端
     *
     * @author Ajie
     * @date 2020/1/17 0017
     */
    public static void initClient() {
        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 模拟客户数目
        int clientNum = 10000;
        for (int i = 0; i < clientNum; i++) {
            cachedThreadPool.execute(new ClientThread(i));
        }
        cachedThreadPool.shutdown();

        while (true) {
            if (cachedThreadPool.isTerminated()) {
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
     * 输出结果
     */
    public static void printResult() {
        Jedis jedis = RedisUtil.getJedis();
        Set<String> set = jedis.smembers("clientList");

        int i = 1;
        for (String value : set) {
            System.out.println("第" + i++ + "个抢到锁，"+value + " ");
        }

        RedisUtil.returnResource(jedis);
    }

    /**
     * 顾客线程
     *
     * @author linbingwen
     */
    static class ClientThread implements Runnable {
        Jedis jedis = null;
        String clientName;
        // 抢购到锁的顾客列表
        String clientList = "clientList";

        public ClientThread(int num) {
            clientName = "编号=" + num;
        }

        public void run() {
            try {
                Thread.sleep((int) (Math.random() * 1000));// 随机睡眠一下
            } catch (InterruptedException e1) {
            }
            while (true) {
                jedis = RedisUtil.getJedis();
                // 获取令牌锁
                try {
                    String result = jedis.get("lock");
                    if (result.equals("0")) {
                        // 上锁
                        getLock();
                        // System.out.println("顾客:" + clientName + "抢到锁");
                        // 执行主方法
                        home();
                        jedis.sadd(clientList, clientName);
                        break;
                    } else {
                        break;
                    }
                } finally {
                    // 关闭连接
                    RedisUtil.returnResource(jedis);
                }
            }
        }
    }

    /**
     * 功能描述：获取锁
     *
     * @author Ajie
     * @date 2020/1/17 0017
     */
    public static void getLock() {
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.set("lock", "1");
        } finally {
            // 关闭连接
            RedisUtil.returnResource(jedis);
        }
    }

    /**
     * 功能描述：释放锁
     *
     * @author Ajie
     * @date 2020/1/17 0017
     */
    public static void closeLock() {
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.set("lock", "0");
        } finally {
            // 关闭连接
            RedisUtil.returnResource(jedis);
        }
    }

    /**
     * 功能描述：主体方法
     *
     * @author Ajie
     * @date 2020/1/17 0017
     */
    public static void home() {
        System.out.println("==========》执行方法内容：" + DateUtil.getTime());
        try {
            Thread.sleep((int) (Math.random() * 3000));// 随机睡眠一下
        } catch (InterruptedException e1) {
        }
        // 释放锁
        closeLock();

    }


}
