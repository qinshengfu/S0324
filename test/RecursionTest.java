import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 说明：递归练习
 * 创建人：Ajie
 * 创建时间：2019年11月19日16:56:36
 */
public class RecursionTest {

    final Logger logger = LoggerFactory.getLogger(RecursionTest.class);


    @Test
    public void resurl() {
        int i = 6;
        int result = fibonacciSeriesTest(i);
        logger.info("结果：{}", result);

    }


    /**
     * 功能描述：斐波那契数列（第 n 项的值是多少。）
     *
     * @param n 正整数
     * @return int 结果
     * @author Ajie
     * @date 2019/11/19 0019
     */
    private int fibonacciSeriesTest(int n) {
        // 结束条件
        if (n <= 2) {
            return 1;
        }
        // 函数等价关系式, 自体调用
        return fibonacciSeriesTest(n - 1) + fibonacciSeriesTest(n - 2);
    }

    /**
     * 功能描述：一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
     * @author Ajie
     * @date 2019/11/19 0019
     * @param n 台阶
     * @return 有多少种跳法
     */
    private int frogJumpTest(int n) {
        // 结束条件，f(0) = 0,f(1) = 1，f(2) = 2等价于 n<=2时，f(n) = n。
        if (n <= 2) {
            return n;
        }
        // 函数等价关系式
        return frogJumpTest(n - 1) + frogJumpTest(n - 2);
    }

}
