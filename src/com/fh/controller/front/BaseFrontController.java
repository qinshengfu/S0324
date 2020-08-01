package com.fh.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Rank_systemManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.*;
import com.fh.util.PageData;
import com.fh.util.Tools;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 说明：前台基础控制器
 * 创建人：Ajie
 * 创建时间：2020年4月1日17:34:45
 */
public class BaseFrontController extends BaseController {

    // 用户管理
    @Resource(name = "accountService")
    public AccountManager accountService;
    // 系统参数
    @Resource(name = "sys_configService")
    public Sys_configManager sys_configService;
    // usdt钱包记录
    @Resource(name = "usdt_recService")
    public Usdt_recManager usdt_recService;
    // 分享基金记录
    @Resource(name = "fund_recService")
    public Fund_recManager fund_recService;
    // 跑单记录
    @Resource(name = "receipt_recService")
    public Receipt_recManager receipt_recService;
    // 跑单收益详情
    @Resource(name = "profit_recordService")
    public Profit_recordManager profit_recordService;
    // 娱乐积金钱包记录
    @Resource(name = "score_recService")
    public Score_recManager score_recService;
    // 新闻公告
    @Resource(name = "sys_newsService")
    public Sys_newsManager sys_newsService;
    /**
     * 等级制度
     */
    @Resource(name = "rank_systemService")
    public Rank_systemManager rank_systemService;

    /**
     * 功能描述：释放奖金
     *
     * @param order 跑单记录
     * @author Ajie
     * @date 2020/5/18 0018
     */
    public void bonusPay(PageData order) throws Exception {
        PageData pd = new PageData();
        pd.put("ACCOUNT_ID", order.getString("USER_ID"));
        pd.put("USER_ID", order.getString("USER_ID"));
        // 查询用户信息
        MemUser user = accountService.findByIdPojo(pd);
        // 根据用户ID和订单状态查询跑单收益记录
        pd.put("STATUS", "待发放");
        pd.put("USER_NAME", user.getUSER_NAME());
        // 重试2次
        int retry = 0;
        do {
            retry++;
            List<PageData> profitRec = profit_recordService.findByOrderIdAndStatus(pd);
            if (profitRec == null || profitRec.size() < 1) {
                // 执行跑单记录状态更新为 已完成
                order.put("STATUS", "已完成");
                order.put("GMT_MODIFIED", cn.hutool.core.date.DateUtil.now());
                receipt_recService.edit(order);
                // 多线程异步执行
                ThreadUtil.newExecutor().execute(() -> {
                    try {
                        // 发放分享基金和娱乐积金
                        payFundAndScore(user);
                        // 发放 usdt极差奖
                        payUsdt(user);
                        // 发放 分享基金和娱乐积分极差奖
                        payFund(user);
                        payScore(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                logger.info("用户：" + user.getUSER_NAME() + "  订单完成！\n");
            } else {
                int i = 0;
                // 循环待发放列表释放奖金
                for (PageData data : profitRec) {
                    // 判断是否在 到释放时间
                    /*boolean isReleaseTime = com.fh.util.DateUtil.isEqualDate(data.getString("GMT_MODIFIED"));
                    if (!isReleaseTime) {
                        continue;
                    }*/
                    // 执行奖金发放，并更新这条跑单收益状态为 已完成
                    double assets = cn.hutool.core.convert.Convert.toDouble(data.get("ASSETS"));
                    double earnings = Convert.toDouble(data.get("EARNINGS"));
                    double money = NumberUtil.add(assets, earnings);
                    data.put("STATUS", "已完成");
                    profit_recordService.edit(data);
                    // 多线程执行
                    ThreadUtil.newExecutor().execute(() -> {
                        try {
                            addUserMoney(pd, money, 0);
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

    /**
     * 功能描述：查询上级未发放的usdt钱包记录
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020年5月18日09:49:17
     */
    public void payUsdt(MemUser user) throws Exception {
        // 顶点账号没有上级
        if ("10000".equals(user.getACCOUNT_ID())) {
            return;
        }
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("RE_PATH", user.getRE_PATH());
        pd.put("STATUS", "待发放");
        // 所有从我身上获得极差的上级 待发放 usdt列表
        List<PageData> usdtList = usdt_recService.listByPathAndStatus(pd);
        if (usdtList.size() <= 0) {
            return;
        }
        for (PageData map : usdtList) {
            double money = Convert.toDouble(map.get("MONEY"));
            pd.put("ACCOUNT_ID", map.get("USER_ID"));
            addUserMoney(pd, money, 1);
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
    public void payFund(MemUser user) throws Exception {
        // 顶点账号没有上级
        if ("10000".equals(user.getACCOUNT_ID())) {
            return;
        }
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("RE_PATH", user.getRE_PATH());
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
    public void payScore(MemUser user) throws Exception {
        // 顶点账号没有上级
        if ("10000".equals(user.getACCOUNT_ID())) {
            return;
        }
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("RE_PATH", user.getRE_PATH());
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
     * 功能描述：查询未发放的分享基金和娱乐奖金记录
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020年5月18日09:41:53
     */
    public void payFundAndScore(MemUser user) throws Exception {
        // 根据用户ID和状态查询
        PageData pd = new PageData();
        pd.put("USER_ID", user.getACCOUNT_ID());
        pd.put("STATUS", "待发放");
        // 待发放的分享基金列表
        List<PageData> fundList = fund_recService.listByUserIdAndStatus(pd);
        // 待发放的娱乐积分列表
        List<PageData> scoreList = score_recService.listByUserIdAndStatus(pd);
        if (fundList.size() < 1|| scoreList.size() < 1) {
            return;
        }
        // 只发放一条记录
        double fund = Convert.toDouble(fundList.get(0).get("MONEY"));
        double score = Convert.toDouble(scoreList.get(0).get("MONEY"));
        addScoreAndFundMoney(user, score, fund);
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
     * 功能描述：增加娱乐积金和分享基余额
     *
     * @param score 娱乐积分
     * @param fund  分享基金
     * @author Ajie
     * @date 2020年5月18日09:44:56
     */
    public void addScoreAndFundMoney(MemUser user, double score, double fund) throws Exception {
        PageData pd = new PageData();
        pd.put("tag", "+");
        pd.put("ACCOUNT_ID", user.getACCOUNT_ID());
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 增加娱乐积金
        pd.put("money", score);
        accountService.updateScoreNumber(pd);
        // 增加分享基金
        pd.put("money", fund);
        accountService.updateSharedFundsNumber(pd);
    }

    /**
     * 功能描述：增加usdt钱包余额
     *
     * @param user  用户信息
     * @param money usdt奖金
     * @param flag  0:创建钱包记录 1：不创建
     * @author Ajie
     * @date 2020年5月18日09:51:00
     */
    public void addUserMoney(PageData user, double money, int flag) throws Exception {
        PageData pd = new PageData();
        pd.put("tag", "+");
        pd.put("money", money);
        pd.put("ACCOUNT_ID", user.get("ACCOUNT_ID"));
        pd.put("GMT_MODIFIED", DateUtil.now());
        // 增加usdt余额
        accountService.updateUsdtNumber(pd);
        if (flag == 0) {
            // 增加记录
            addUsdtRecord(user, money);
        }
    }

    /**
     * 功能描述：判断用户当前级别
     *
     * @author Ajie
     * @date 2020/3/28 0028
     */
    public void checkRank() throws Exception {
        // 查询等级制度
        List<PageData> rankList = rank_systemService.listAllByNumPeople(new PageData());
        // 初始等级、最高等级的佣金点
        double max = Convert.toDouble(rankList.get(rankList.size() - 1).get("COMMISSION"));
        // 查询所有用户信息
        List<PageData> listAll = accountService.listAll();
        for (PageData map : listAll) {
            String userId = map.getString("ACCOUNT_ID");
            // 查询用户现在的佣金点
            double commission = Convert.toDouble(map.get("COMMISSION"));
            if (commission >= max) {
                // 因为是后台直接指派的佣金点，需要把等级修改为 max级
                ThreadUtil.execute(() -> {
                    PageData data = new PageData();
                    data.put("ACCOUNT_ID", userId);
                    data.put("USER_RANK", rankList.get(rankList.size() - 1).get("RANK"));
                    try {
                        accountService.editFor(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                continue;
            }
            rankJudgment(map, commission);
        }
    }

    /**
     * 功能描述：循环判断符合那个等级
     *
     * @param user       用户信息
     * @param commission 用户当前佣金点
     * @author Ajie
     * @date 2020/6/15 0015
     */
    private void rankJudgment(PageData user, double commission) throws Exception {
        // 查询等级制度
        List<PageData> rankList = rank_systemService.listAllByNumPeople(new PageData());
        // 查询用户活跃团队人数、直推人数
        int teamSum = Convert.toInt(accountService.findByActiveTeams(user).get("TEAM_SUM"));
        int recommendTotal = Convert.toInt(accountService.findByActiveRecommended(user).get("RECOMMEND_TOTAL"));
        log.info("用户：{}，活跃团队人数：{}， 活跃直推人数：{}", user.get("USER_NAME"), teamSum, recommendTotal);
        // 获取最少直推人数的等级
        PageData minRank = rank_systemService.findByMinRank(new PageData());
        int minRecommend = Convert.toInt(minRank.get("NUMBER_PEOPLE"));
        // 连最小的直推人数都没有达成直接返回
        if (recommendTotal < minRecommend) {
            return;
        }
        // 定义即将升级的等级、佣金点收益
        String userRank = "";
        double profit = 0;
        for (PageData rank : rankList) {
            // 条件(1：直推，0：团队)、要求人数、对应的佣金点、等级名称
            int ask = Convert.toInt(rank.get("ASK"));
            int numberPeople = Convert.toInt(rank.get("NUMBER_PEOPLE"));
            double rankCommission = Convert.toDouble(rank.get("COMMISSION"));
            String rankName = rank.getString("RANK");
            if (ask == 1) {
                // 用户佣金点 < 等级获得的佣金点
                if (commission < rankCommission) {
                    // 记录可升级的佣金点和等级
                    userRank = rankName;
                    profit = rankCommission;
                }
            }
            if (ask == 0) {
                // 用户团队人数 < 等级要求的团队人数退出循环
                if (teamSum < numberPeople) {
                    break;
                }
                if (commission < rankCommission) {
                    // 记录可升级的佣金点和等级
                    userRank = rankName;
                    profit = rankCommission;
                }
            }
        }
        // 更新数据库
        double finalProfit = profit;
        String finalUserRank = userRank;
        if (finalProfit <= 0 || Tools.isEmpty(finalUserRank)) {
            return;
        }
        ThreadUtil.execute(() -> {
            PageData data = new PageData();
            data.put("ACCOUNT_ID", user.get("ACCOUNT_ID"));
            data.put("COMMISSION", finalProfit);
            data.put("USER_RANK", finalUserRank);
            try {
                accountService.editFor(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 功能描述：添加分享基金钱包记录
     *
     * @param user   用户信息
     * @param money  金额
     * @param type   类型名称
     * @param status 状态
     * @param tag    + 或者 -
     * @author Ajie
     * @date 2020/4/9 0009
     */
    public void addfundRecord(MemUser user, Object money, String type, String status, String tag) throws Exception {
        PageData pd = new PageData();
        pd.put("USER_NAME", user.getUSER_NAME());    //用户名
        pd.put("USER_ID", user.getACCOUNT_ID());    //用户ID
        pd.put("AMOUNT_TYPE", type);    //金额类型
        pd.put("TAG", tag);    //标签 + 或者 -
        pd.put("MONEY", money);    //金额
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", status);    //状态
        fund_recService.save(pd);
    }

    /**
     * 功能描述：添加usdt钱包记录
     *
     * @param user    用户信息
     * @param money   金额
     * @param type    类型名称
     * @param status  状态
     * @param tag     + 或者 -
     * @param charge  手续费
     * @param heName  对方用户名
     * @param heId    对方用户ID
     * @param address 钱包地址
     * @author Ajie
     * @date 2020年4月10日09:54:50
     */
    public void addUsdtRecord(PageData user, Object money, String type, String status, String tag, Object charge
            , String heName, String heId, String address) throws Exception {
        PageData pd = new PageData();
        pd.put("USER_NAME", user.getString("USER_NAME"));    //用户名
        pd.put("USER_ID", user.getString("ACCOUNT_ID"));    //用户ID
        pd.put("AMOUNT_TYPE", type);    //金额类型
        pd.put("WALLET_ADDRESS", address);    //钱包地址
        pd.put("TAG", tag);    //标签 + 或者 -
        pd.put("MONEY", money);    //金额
        pd.put("HE_NAME", heName);    //对方用户名
        pd.put("HE_ID", heId);    //对方用户ID
        pd.put("STATUS", status);    //状态
        pd.put("CHARGE", charge);    //手续费
        pd.put("ACTUAL_ARRIVAL", money);    //实际到账
        usdt_recService.save(pd);
    }

    /**
     * 功能描述：添加usdt钱包记录
     *
     * @param user 用户信息
     * @author Ajie
     * @date 2020年5月18日09:55:24
     */
    public void addUsdtRecord(PageData user, Object money) throws Exception {
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

}
