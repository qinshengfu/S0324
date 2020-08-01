package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import com.fh.controller.base.BaseController;
import com.fh.service.front.AccountManager;
import com.fh.util.BlockUtil;
import com.fh.util.PageData;
import com.fh.util.QuartzManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 说明：USDT查余额[提现]定时任务
 * 如果查到的余额小于 本地记录的余额  就减掉本地余额
 * 创建人：Ajie
 * 创建时间：2020年2月12日08:47:15
 */
public class CheckingOutTaskJob extends BaseController implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();

        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        try {
            // 获取所有用户列表
            List<PageData> userAll = accountService.listAll();
            // 循环查询
            for (PageData map : userAll) {
                // 获取usdt 钱包地址
                String address = map.getString("USDT_ADDRESS");
                // 调用区块链查询余额api
                double amount = Convert.toDouble(BlockUtil.usdtBalance(address));
                // 本地记录的余额
                double localAmount = Convert.toDouble(map.get("USDT_WALLET_ACTUAL"));
                // 如果查到的余额小于本地记录的余额 减少本地钱包余额
                if (amount < localAmount) {
                    //异步执行数据库操作
                    ThreadUtil.execute(() -> {
                        PageData pd = new PageData();
                        // 更新本地记录的usdt余额
                        try {
                            pd.put("ACCOUNT_ID", map.get("ACCOUNT_ID"));
                            pd.put("USDT_WALLET_ACTUAL", amount);
                            pd.put("GMT_MODIFIED", DateUtil.now());
                            accountService.edit(pd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            Console.log("查询余额定时任务，并减余额失败");
            e.printStackTrace();
        }


    }


}
