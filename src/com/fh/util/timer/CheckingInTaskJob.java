package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Recharge_recordManager;
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
 * 说明：USDT查余额充值定时任务
 * 如果查到的余额大于 本地记录的余额  就增加到钱包里
 * 创建人：Ajie
 * 创建时间：2019年12月30日 12:28:00
 */
public class CheckingInTaskJob extends BaseController implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();

        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        // 系统参数
        Sys_configManager sys_configService = (Sys_configManager) webctx.getBean("sys_configService");
        // 线上钱包充值记录
        Recharge_recordManager recharge_recordService = (Recharge_recordManager) webctx.getBean("recharge_recordService");

        try {
            // 获取所有用户列表
            List<PageData> userAll = accountService.listAll();
            PageData pd = new PageData();
            // 循环查询
            for (PageData map : userAll) {
                // 获取系统参数
                PageData par = sys_configService.findById(new PageData());
                // 注册时赠送的USDT
                double additionalUsdt = Convert.toDouble(par.get("EACH_FULL_FUND"));
                // 获取usdt 钱包地址
                String address = map.getString("USDT_ADDRESS");
                // 调用区块链查询余额api
                double amount = Convert.toDouble(BlockUtil.usdtBalance(address));
//                logger.info("用户名：" + map.get("USER_NAME") + "  查询到的余额:" + amount);
                // 充值范围
                double min = Convert.toDouble(par.get("MIN_RECHARGE"));
                double max = Convert.toDouble(par.get("MAX_RECHARGE"));
                // 不在充值范围 直接循环下一个
                if (min > amount || amount > max) {
                    continue;
                }
                // 本地记录的余额
                double localAmount = Convert.toDouble(map.get("USDT_WALLET_ACTUAL"));
                double money = NumberUtil.sub(amount, localAmount);
                pd.put("USER_ID", map.get("ACCOUNT_ID"));
                List<PageData> order = recharge_recordService.listByUserId(pd);
                // 首次充值 扣除赠送的usdt
                if (order == null || order.size() < 1) {
                    money = NumberUtil.sub(money, additionalUsdt);
                }
                // 如果查到的余额大于本地记录的余额 加钱到钱包上
                if (amount > localAmount) {
                    //异步执行数据库操作
                    double finalMoney = money;
                    ThreadUtil.execute(() -> {
                        try {
                            map.put("amount", amount);
                            map.put("money", finalMoney);
                            accountService.updateRechargeMoneyAndNewRec(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            Console.log("查询余额定时任务，并增加余额失败");
            e.printStackTrace();
        }

    }


}
