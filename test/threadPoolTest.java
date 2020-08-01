import com.fh.util.pool.MyThreadPoolManager;

/**
 * 说明：线程池测试
 * 创建人：Ajie
 * 创建时间：2019年11月2日15:28:19
 */
public class threadPoolTest {

    public static void main(String[] args) {

        MyThreadPoolManager.getsInstance().execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("pool-任务名");
                System.out.println("当前执行的线程： 【 " + Thread.currentThread().getName() + " 】=======》  执行完毕");
            }
        });

        MyThreadPoolManager.getsInstance().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("当前执行的线程： 【 " + Thread.currentThread().getName() + " 】=======》  执行完毕");
            }
        });
        MyThreadPoolManager.shutdown();
        System.out.println("线程池已关闭");

    }


}

