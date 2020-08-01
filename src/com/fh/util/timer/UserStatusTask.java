package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Rank_systemManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Receipt_recManager;
import com.fh.service.record.Recharge_recordManager;
import com.fh.service.record.Usdt_recManager;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.Tools;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * 说明：检查用户是否活跃
 * 创建人：Ajie
 * 创建时间：2020年4月1日14:16:53
 */
public class UserStatusTask extends BaseController implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("开始检查用户状态：" + DateUtil.getTime());

        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();

        // usdt充值记录表
        Recharge_recordManager rechargeRecordService = (Recharge_recordManager) webctx.getBean("recharge_recordService");
        // USDT钱包记录表
        Usdt_recManager usdt_recService = (Usdt_recManager) webctx.getBean("usdt_recService");
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");
        // 系统参数
        Sys_configManager sysConfigService = (Sys_configManager) webctx.getBean("sys_configService");
        // 等级制度表
        Rank_systemManager rank_systemService = (Rank_systemManager) webctx.getBean("rank_systemService");

        PageData pd = new PageData();
        try {
            // 查询系统参数
            PageData par = sysConfigService.findById(pd);
            // 充值多少变成有效会员
            double formalUserCost = Convert.toDouble(par.get("RELEASE_FROM_REST"));
            // 查询所有用户列表
            List<PageData> listAll = accountService.listAll();
            pd = new PageData();
            for (PageData map : listAll) {
                if ("0".equals(map.get("IS_REST").toString())) {
                    continue;
                }
                // 查询用户充值或者转账记录
                double userCumulativeAmount = Convert.toDouble(accountService.getSumAmountByUserId(map).get("TOTAL_AMOUNT"));
                logger.info("用户：" + map.get("USER_NAME") + " 累积充值/转账总额：" + userCumulativeAmount);
                if (formalUserCost > userCumulativeAmount) {
                    continue;
                }
                // 更改用户状态设置
                pd.put("ACCOUNT_ID", map.getString("ACCOUNT_ID"));
                // 0:正常 1:休息号
                pd.put("IS_REST", 0);
                pd.put("GMT_MODIFIED", DateUtil.getTime());
                accountService.editFor(pd);
            }
            // 执行重复用户等级检查
            for (int i = 0; i < 2; i++) {
                this.checkRank(accountService, rank_systemService);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 功能描述：判断用户当前级别
     *
     * @author Ajie
     * @date 2020/3/28 0028
     */
    public void checkRank(AccountManager accountService, Rank_systemManager rank_systemService) throws Exception {
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
            rankJudgment(map, commission, accountService, rank_systemService);
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
    private void rankJudgment(PageData user, double commission, AccountManager accountService, Rank_systemManager rank_systemService) throws Exception {
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
        log.info("用户：{} 等级从 {} 升到 {}", user.get("USER_NAME"), user.get("USER_RANK"), userRank );
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

}
