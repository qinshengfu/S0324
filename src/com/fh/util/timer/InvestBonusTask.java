package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.service.front.AccountManager;
import com.fh.service.record.*;
import com.fh.util.PageData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 说明：投资奖金定时任务，到点发放奖金
 * 创建人：Ajie
 * 创建时间：2020年4月1日10:10:55
 */
public class InvestBonusTask extends BaseController implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("开始执行 奖金释放任务\n");

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();

        // 系统参数
//        Sys_configManager sys_configService = (Sys_configManager) webctx.getBean("sys_configService");
        // 跑单记录表
        Receipt_recManager receiptRecService = (Receipt_recManager) webctx.getBean("receipt_recService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        // 跑单收益详情
        Profit_recordManager profitRecordService = (Profit_recordManager) webctx.getBean("profit_recordService");

        PageData pd = new PageData();
        try {
            // 查询所有 跑单未完成的用户
            List<PageData> userAll = accountService.notFinishedList();
            for (PageData map : userAll) {
                // 根据用户ID 查询最新一条未完成的跑单记录
                pd.put("USER_ID", map.get("ACCOUNT_ID"));
                PageData order = receiptRecService.getLatestRecord(pd);
                logger.info("用户：" + map.get("USER_NAME") + " 查询到的订单：" + order);
                if (order == null || "已完成".equals(order.getString("STATUS"))) {
                    continue;
                }
                // 根据用户ID和订单状态查询跑单收益记录
                pd.put("STATUS", "待发放");
                // 重试2次
                int retry = 0;
                do {
                    retry++;
                    List<PageData> profitRec = profitRecordService.findByOrderIdAndStatus(pd);
                    if (profitRec == null || profitRec.size() < 1) {
                        // 执行跑单记录状态更新为 已完成
                        order.put("STATUS", "已完成");
                        order.put("GMT_MODIFIED", DateUtil.now());
                        receiptRecService.edit(order);
                        // 多线程异步执行
                        ThreadUtil.newExecutor().execute(() -> {
                            try {
                                // 发放分享基金和娱乐积金
                                payFundAndScore(map, webctx);
                                // 发放 usdt极差奖
                                payUsdt(map, webctx);
                                // 发放分享基金和娱乐积分极差奖
                                payFund(map, webctx);
                                payScore(map, webctx);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        logger.info("用户：" + map.get("USER_NAME") + "  订单完成！\n");
                    } else {
                        int i = 0;
                        // 循环待发放列表释放奖金
                        for (PageData data : profitRec) {
                            // 判断是否在 到释放时间
                            boolean isReleaseTime = com.fh.util.DateUtil.isEqualDate(data.getString("GMT_MODIFIED"));
                            if (!isReleaseTime) {
                                continue;
                            }
                            // 执行奖金发放，并更新这条跑单收益状态为 已完成
                            double assets = Convert.toDouble(data.get("ASSETS"));
                            double earnings = Convert.toDouble(data.get("EARNINGS"));
                            double money = NumberUtil.add(assets, earnings);
                            data.put("STATUS", "已完成");
                            profitRecordService.edit(data);
                            // 多线程执行
                            ThreadUtil.newExecutor().execute(() -> {
                                try {
                                    addMoney(map, money, webctx, 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            i++;
                        }
                        if (i > 0) {
                            logger.info("到发放时间,发放奖金！共：" + i + " 条\n");
                        }
                    }
                } while (retry < 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：查询未发放的分享基金和娱乐奖金记录
     *
     * @param user   用户信息
     * @param webctx 上下文
     * @author Ajie
     * @date 2020/4/9 0009
     */
    private void payFundAndScore(PageData user, WebApplicationContext webctx) throws Exception {
        // 分享基金记录表
        Fund_recManager fund_recService = (Fund_recManager) webctx.getBean("fund_recService");
        // 娱乐积分记录表
        Score_recManager score_recService = (Score_recManager) webctx.getBean("score_recService");

        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getString("ACCOUNT_ID"));
        pd.put("STATUS", "待发放");
        // 待发放的分享基金列表
        List<PageData> fundList = fund_recService.listByUserIdAndStatus(pd);
        // 待发放的娱乐积分列表
        List<PageData> scoreList = score_recService.listByUserIdAndStatus(pd);
        if (fundList.size() < 1 || scoreList.size() < 1) {
            return;
        }
        // 只发放一条记录
        double fund = Convert.toDouble(fundList.get(0).get("MONEY"));
        double score = Convert.toDouble(scoreList.get(0).get("MONEY"));
        addMoney(user, score, fund, webctx);
        // 把订单状态改为 已完成
        pd.put("STATUS", "已完成");
        pd.put("GMT_MODIFIED", DateUtil.now());
        pd.put("FUND_REC_ID", fundList.get(0).get("FUND_REC_ID"));
        fund_recService.edit(pd);
        pd.remove("FUND_REC_ID");
        pd.put("SCORE_REC_ID", scoreList.get(0).get("SCORE_REC_ID"));
        score_recService.edit(pd);
    }

    /**
     * 功能描述：查询未发放的usdt钱包记录
     *
     * @param user   用户信息
     * @param webctx 上下文
     * @author Ajie
     * @date 2020年4月10日10:05:27
     */
    private void payUsdt(PageData user, WebApplicationContext webctx) throws Exception {
        // 顶点账号没有上级
        if ("10000".equals(user.getString("ACCOUNT_ID"))) {
            return;
        }
        // usdt钱包记录表
        Usdt_recManager usdt_recService = (Usdt_recManager) webctx.getBean("usdt_recService");
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getString("ACCOUNT_ID"));
        pd.put("RE_PATH", user.getString("RE_PATH"));
        pd.put("STATUS", "待发放");
        // 所有从我身上获得极差的上级 待发放 usdt列表
        List<PageData> usdtList = usdt_recService.listByPathAndStatus(pd);
        if (usdtList.size() <= 0) {
            return;
        }
        for (PageData map : usdtList) {
            double money = Convert.toDouble(map.get("MONEY"));
            pd.put("ACCOUNT_ID", map.get("USER_ID"));
            addMoney(pd, money, webctx, 1);
            // 更改状态为 已完成
            map.put("STATUS", "已完成");
            map.put("GMT_MODIFIED", DateUtil.now());
            usdt_recService.edit(map);
        }
    }

    /**
     * 功能描述：查询上级未发放的分享基金并释放
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020-6-26 14:32:03
     */
    public void payFund(PageData user, WebApplicationContext webctx) throws Exception {
        // 分享基金记录表
        Fund_recManager fund_recService = (Fund_recManager) webctx.getBean("fund_recService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");

        // 顶点账号没有上级
        if ("10000".equals(user.get("ACCOUNT_ID").toString())) {
            return;
        }
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.get("ACCOUNT_ID"));
        pd.put("RE_PATH", user.get("RE_PATH"));
        pd.put("STATUS", "待发放");
        // 所有从我身上获得分享基金极差的上级 待发放 分享基金列表
        List<PageData> list = fund_recService.listByPathAndStatus(pd);
        if (list.size() <= 0) {
            return;
        }
        for (PageData map : list) {
            pd.put("GMT_MODIFIED", DateUtil.now());
            pd.put("tag", "+");
            pd.put("ACCOUNT_ID", map.get("USER_ID"));
            pd.put("money", map.get("MONEY"));
            // 增加分享基金
            accountService.updateSharedFundsNumber(pd);
            // 更改状态为 已完成
            map.put("STATUS", "已完成");
            map.put("GMT_MODIFIED", DateUtil.now());
            fund_recService.edit(map);
        }
    }

    /**
     * 功能描述：查询上级未发放的娱乐积分并释放
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020-6-26 14:32:03
     */
    public void payScore(PageData user, WebApplicationContext webctx) throws Exception {
        // 娱乐积分记录表
        Score_recManager score_recService = (Score_recManager) webctx.getBean("score_recService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");

        // 顶点账号没有上级
        if ("10000".equals(user.get("ACCOUNT_ID").toString())) {
            return;
        }
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.get("ACCOUNT_ID"));
        pd.put("RE_PATH", user.get("RE_PATH"));
        pd.put("STATUS", "待发放");
        // 所有从我身上获得娱乐积分极差的上级 待发放 娱乐积分列表
        List<PageData> list = score_recService.listByPathAndStatus(pd);
        if (list.size() <= 0) {
            return;
        }
        for (PageData map : list) {
            pd.put("GMT_MODIFIED", DateUtil.now());
            pd.put("tag", "+");
            pd.put("ACCOUNT_ID", map.get("USER_ID"));
            pd.put("money", map.get("MONEY"));
            // 增加娱乐积分
            accountService.updateScoreNumber(pd);
            // 更改状态为 已完成
            map.put("STATUS", "已完成");
            map.put("GMT_MODIFIED", DateUtil.now());
            score_recService.edit(map);
        }
    }

    /**
     * 功能描述：添加usdt钱包记录
     *
     * @param user   用户信息
     * @param webctx 上下文
     * @author Ajie
     * @date 2020/4/9 0009
     */
    private void addUsdtRecord(PageData user, Object money, WebApplicationContext webctx) throws Exception {
        // usdt钱包记录表
        Usdt_recManager usdt_recService = (Usdt_recManager) webctx.getBean("usdt_recService");
        PageData pd = new PageData();
        pd.put("USER_NAME", user.getString("USER_NAME"));    //用户名
        pd.put("USER_ID", user.getString("ACCOUNT_ID"));    //用户ID
        pd.put("AMOUNT_TYPE", "接单收益");    //金额类型
        pd.put("WALLET_ADDRESS", "");    //钱包地址
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("MONEY", money);    //金额
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", "已完成");    //状态
        pd.put("CHARGE", "0");    //手续费
        pd.put("ACTUAL_ARRIVAL", money);    //实际到账
        usdt_recService.save(pd);
    }

    /**
     * 功能描述：增加usdt钱包余额
     *
     * @param user   用户信息
     * @param money  usdt奖金
     * @param webctx web上下文
     * @param flag   0:创建钱包记录 1：不创建
     * @author Ajie
     * @date 2020/4/1 0001
     */
    private void addMoney(PageData user, double money, WebApplicationContext webctx, int flag) throws Exception {
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        PageData pd = new PageData();
        pd.put("tag", "+");
        pd.put("money", money);
        pd.put("ACCOUNT_ID", user.get("ACCOUNT_ID"));
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 增加usdt余额
        accountService.updateUsdtNumber(pd);
        if (flag == 0) {
            // 增加记录
            addUsdtRecord(user, money, webctx);
        }
    }

    /**
     * 功能描述：增加娱乐积金和分享基余额
     *
     * @param score 娱乐积分
     * @param fund  分享基金
     * @author Ajie
     * @date 2020年4月9日15:32:43
     */
    private void addMoney(PageData user, double score, double fund, WebApplicationContext webctx) throws Exception {
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        PageData pd = new PageData();
        pd.put("tag", "+");
        pd.put("ACCOUNT_ID", user.get("ACCOUNT_ID"));
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 增加娱乐积金
        pd.put("money", score);
        accountService.updateScoreNumber(pd);
        // 增加分享基金
        pd.put("money", fund);
        accountService.updateSharedFundsNumber(pd);
    }


}
