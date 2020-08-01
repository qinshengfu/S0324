import com.fh.util.PageData;
import com.fh.util.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 说明：分页工具类测试
 * 创建人：Ajie
 * 创建时间：2019年12月13日12:17:43
 */
public class PagerTest {
    /**
     * 功能描述：具体用法
     *
     * @author Ajie
     * @date 2019/12/13 0013
     */
    public static void main(String[] args) {
        PageData pd;
        List<PageData> list = new ArrayList<>(16);
        for (int i = 1; i <= 50; i++) {
            pd = new PageData();
            pd.put("test"+i,"任务" + i);
            list.add(pd);
        }
        System.out.println("全部数据为：" + list);

        Pager<PageData> pager = new Pager<>();
        // 第 N 页
        pager.setCurentPageIndex(1);
        // 每页 N 条
        pager.setCountPerpage(5);
        // 查询到的大集合
        pager.setBigList(list);
        // 得到总条数
        int count =  pager.getRecordCount();
        // 得到总页数
        int pageCount = pager.getPageCount();
        // 得到小的集合(分页的当前页的记录)
        List<PageData> pageList = pager.getSmallList();
        System.out.println("总条数：" + count);
        System.out.println("总页数：" + pageCount);
        System.out.println("当前页数据为：" + pageList);
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("请输入页数：");
            int pageIndex = sc.nextInt();
            pager.setCurentPageIndex(pageIndex);
            pageList = pager.getSmallList();
            System.out.println("当前在第 " + pager.getCurentPageIndex() + " 页");
            System.out.println("当前页数据为：" + pageList);
            if (pager.getCurentPageIndex() >= pageCount) {
                System.out.println("输入 【quit 】退出");
                String result = sc.next();
                if ("quit".equalsIgnoreCase(result)) {
                    break;
                }
            }
        }

    }

}
