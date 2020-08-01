package com.fh.service.front.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.service.front.AccountManager;
import com.fh.service.record.*;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 说明： 前台用户表
 * 创建人：
 * 创建时间：2020-03-25
 */
@Service("accountService")
@CacheConfig(cacheNames = "S0324_user")
public class AccountService implements AccountManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    // usdt钱包记录
    @Resource(name = "usdt_recService")
    private Usdt_recManager usdt_recService;
    // usdt充值记录
    @Resource(name = "recharge_recordService")
    private Recharge_recordManager recharge_recordService;
    // 分享基金记录
    @Resource(name = "fund_recService")
    private Fund_recManager fund_recService;
    // 娱乐积分钱包记录
    @Resource(name = "score_recService")
    private Score_recManager score_recService;
    // 跑单记录
    @Resource(name = "receipt_recService")
    private Receipt_recManager receipt_recService;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        // 因为键是自增
        pd.put("ACCOUNT_ID", "");
        dao.save("AccountMapper.save", pd);
    }

    /**
     * 清除所有用户，保留顶点账号
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void deleteAll(PageData pd) throws Exception {
        dao.delete("AccountMapper.deleteAllData", pd);
    }

    /**
     * 重置顶点账号
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void resetAccount(PageData pd) throws Exception {
        dao.delete("AccountMapper.resetAccount", pd);
    }

    /**
     * 修改测试
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public int updateTest(PageData pd) throws Exception {
        return (int) dao.update("AccountMapper.updateTest", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void edit(PageData pd) throws Exception {
        dao.update("AccountMapper.edit", pd);
    }

    /**
     * 修改去掉乐观锁
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void editFor(PageData pd) throws Exception {
        dao.update("AccountMapper.editFor", pd);
    }

    /**
     * 用户线上充值增加usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateRechargeMoneyAndNewRec(PageData pd) throws Exception {
        // 查询交易所的余额
        double amount = Convert.toDouble(pd.get("amount"));
        // 本次增加到用户usdt钱包的金额
        double money = Convert.toDouble(pd.get("money"));
        PageData map = new PageData();
        // 先更新本地记录的usdt余额
        map.put("ACCOUNT_ID", pd.get("ACCOUNT_ID"));
        map.put("USDT_WALLET_ACTUAL", amount);
        editFor(map);
        // 更新钱包余额
        map.put("GMT_MODIFIED", DateUtil.now());
        map.put("money", money);
        map.put("tag", "+");
        updateUsdtNumber(map);
        // 创建USDT钱包账单记录
        pd.put("USER_NAME", pd.get("USER_NAME"));    //用户名
        pd.put("USER_ID", pd.get("ACCOUNT_ID"));    //用户ID
        pd.put("AMOUNT_TYPE", "充值");    //金额类型
        pd.put("WALLET_ADDRESS", pd.get("USDT_ADDRESS"));    //钱包地址
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("MONEY", money);    //金额
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", "已完成");    //状态
        pd.put("CHARGE", "");    //手续费
        pd.put("ACTUAL_ARRIVAL", money);    //实际到账
        usdt_recService.save(pd);
        // 创建usdt线上充值记录
        recharge_recordService.save(pd);

    }

    /**
     * 用户线下充值创建待审核记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateOffLineRechargeMoneyAndRec(PageData pd) throws Exception {
        // 充值的余额
        double money = Convert.toDouble(pd.get("money"));
        // 创建待审核的线下充值记录
        pd.put("USER_NAME", pd.get("USER_NAME"));    //用户名
        pd.put("USER_ID", pd.get("ACCOUNT_ID"));    //用户ID
        pd.put("WALLET_ADDRESS", pd.get("voucher"));    //支付凭证
        pd.put("MONEY", money);    //金额
        pd.put("STATUS", "待审核");    //状态
        recharge_recordService.save(pd);
    }

    /**
     * 用户线下充值更新记录通过
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateOffLineRechargeRecSuccess(PageData pd) throws Exception {
        // 注册时赠送的usdt
        double additionalUsdt = Convert.toDouble(pd.get("additionalUsdt"));
        // 充值的余额
        double money = Convert.toDouble(pd.get("MONEY"));
        List<PageData> order = recharge_recordService.listByUserId(pd);
        // 首次充值 扣除赠送的usdt
        if (order == null || order.size() < 1) {
            money = NumberUtil.sub(money, additionalUsdt);
        }
        PageData map = new PageData();
        // 更新状态已完成
        map.put("RECHARGE_RECORD_ID", pd.get("RECHARGE_RECORD_ID"));
        map.put("STATUS", "已完成");
        map.put("GMT_MODIFIED", DateUtil.now());
        recharge_recordService.edit(map);
        // 更新钱包余额
        map.put("ACCOUNT_ID", pd.get("USER_ID"));
        map.put("money", money);
        map.put("tag", "+");
        updateUsdtNumber(map);
        // 创建usdt钱包充值记录
        pd.put("USER_NAME", pd.get("USER_NAME"));    //用户名
        pd.put("USER_ID", pd.get("USER_ID"));    //用户ID
        pd.put("WALLET_ADDRESS", "");    //钱包地址
        pd.put("MONEY", money);    //金额
        pd.put("STATUS", "已完成");    //状态
        pd.put("AMOUNT_TYPE", "充值");    //金额类型
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("CHARGE", "");    //手续费
        pd.put("ACTUAL_ARRIVAL", money);    //实际到账
        usdt_recService.save(pd);
    }

    /**
     * 用户线下充值更新记录驳回
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateOffLineRechargeRecFail(PageData pd) throws Exception {
        PageData map = new PageData();
        // 更新状态  驳回
        map.put("RECHARGE_RECORD_ID", pd.get("RECHARGE_RECORD_ID"));
        map.put("STATUS", "驳回");
        map.put("GMT_MODIFIED", DateUtil.now());
        recharge_recordService.edit(map);
    }

    /**
     * 后台充值增加usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateAdminRechargeMoneyAndNewRec(PageData pd) throws Exception {
        // 本次增加到用户usdt钱包的金额
        double money = Convert.toDouble(pd.get("MONEY"));
        PageData map = new PageData();
        // 更新钱包余额
        map.put("ACCOUNT_ID", pd.get("ACCOUNT_ID"));
        map.put("GMT_MODIFIED", DateUtil.now());
        map.put("money", money);
        map.put("tag", "+");
        updateUsdtNumber(map);
        // 创建USDT钱包账单记录
        pd.put("USER_NAME", pd.get("USER_NAME"));    //用户名
        pd.put("USER_ID", pd.get("ACCOUNT_ID"));    //用户ID
        pd.put("AMOUNT_TYPE", "充值");    //金额类型
        pd.put("WALLET_ADDRESS", "系统赠送");    //钱包地址
        pd.put("TAG", " ");    //标签 + 或者 -
        if (money > 0) {
            pd.put("TAG", "+");
        }
        pd.put("MONEY", money);    //金额
        pd.put("HE_NAME", "系统赠送");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", "已完成");    //状态
        pd.put("CHARGE", "");    //手续费
        pd.put("ACTUAL_ARRIVAL", money);    //实际到账
        usdt_recService.save(pd);
        // 创建usdt线上充值记录
        recharge_recordService.save(pd);
    }

    /**
     * 用户投资减少usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateInvestMoneyAndNewRec(PageData pd) throws Exception {
        // 投资数量
        String investNum = pd.get("money").toString();
        // 扣除用户 usdt数量
        pd.put("tag", "-");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateUsdtNumber(pd);
        // 创建usdt钱包记录
        pd.put("AMOUNT_TYPE", "接单");    //金额类型
        pd.put("WALLET_ADDRESS", "");    //钱包地址
        pd.put("TAG", "-");    //标签 + 或者 -
        pd.put("MONEY", investNum);    //金额
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", "已完成");    //状态
        pd.put("CHARGE", "");    //手续费
        pd.put("ACTUAL_ARRIVAL", "");    //实际到账
        usdt_recService.save(pd);
        // 创建跑单记录
        pd.put("STATUS", "收益中");    //状态
        pd.put("GMT_MODIFIED", pd.getString("dueTime"));
        receipt_recService.save(pd);
    }

    /**
     * 用户提币减少usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateWithdrawMoney(PageData pd) throws Exception {
        // 提币数量、手续费、实际到账
        String usdtNum = pd.get("money").toString();
        String charge = pd.get("charge").toString();
        String actualArrival = pd.get("actualArrival").toString();
        // 扣除用户 usdt数量
        pd.put("tag", "-");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateUsdtNumber(pd);
        // 创建usdt钱包记录
        pd.put("AMOUNT_TYPE", "提币");    //金额类型
        pd.put("TAG", "-");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("HE_NAME", "");    //对方用户名
        pd.put("HE_ID", "");    //对方用户ID
        pd.put("STATUS", "审核中");    //状态
        pd.put("CHARGE", charge);    //手续费
        pd.put("ACTUAL_ARRIVAL", actualArrival);    //实际到账
        usdt_recService.save(pd);
    }

    /**
     * 用户转账更新usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateUsdtTransfer(PageData pd) throws Exception {
        // 转账数量、对方用户名、ID
        String usdtNum = pd.get("money").toString();
        String heName = pd.get("HE_NAME").toString();
        String heId = pd.get("HE_ID").toString();
        // 我的用户名、ID
        String myName = pd.get("USER_NAME").toString();
        String myId = pd.get("USER_ID").toString();
        // 扣除转出用户 usdt数量
        pd.put("ACCOUNT_ID", myId);
        pd.put("tag", "-");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateUsdtNumber(pd);
        // 创建usdt钱包记录
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "-");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("STATUS", "转出");    //状态
        pd.put("CHARGE", 0);    //手续费
        pd.put("ACTUAL_ARRIVAL", usdtNum);    //实际到账
        pd.put("WALLET_ADDRESS", "");    //钱包地址
        usdt_recService.save(pd);

        // 增加转入用户 usdt数量
        pd.put("ACCOUNT_ID", heId);
        pd.put("tag", "+");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateUsdtNumber(pd);
        // 创建usdt钱包记录
        pd.put("USER_NAME", heName);
        pd.put("USER_ID", heId);
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("HE_NAME", myName);    //对方用户名
        pd.put("HE_ID", myId);    //对方用户ID
        pd.put("STATUS", "转入");    //状态
        usdt_recService.save(pd);
    }

    /**
     * 用户转账更新分享基金并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateFundransfer(PageData pd) throws Exception {
        // 转账数量、对方用户名、ID
        String usdtNum = pd.get("money").toString();
        String heName = pd.get("HE_NAME").toString();
        String heId = pd.get("HE_ID").toString();
        // 我的用户名、ID
        String myName = pd.get("USER_NAME").toString();
        String myId = pd.get("USER_ID").toString();
        // 扣除转出用户 分享基金数量
        pd.put("ACCOUNT_ID", myId);
        pd.put("tag", "-");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateSharedFundsNumber(pd);
        // 创建分享基金钱包记录
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "-");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("STATUS", "转出");    //状态
        fund_recService.save(pd);

        // 增加转入用户 分享基金数量
        pd.put("ACCOUNT_ID", heId);
        pd.put("tag", "+");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateSharedFundsNumber(pd);
        // 创建分享基金钱包记录
        pd.put("USER_NAME", heName);
        pd.put("USER_ID", heId);
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("HE_NAME", myName);    //对方用户名
        pd.put("HE_ID", myId);    //对方用户ID
        pd.put("STATUS", "转入");    //状态
        fund_recService.save(pd);
    }

    /**
     * 用户转账更新娱乐积分并创建记录
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)//  让checked例外也回滚
    public void updateIntegral(PageData pd) throws Exception {
        // 转账数量、对方用户名、ID
        String usdtNum = pd.get("money").toString();
        String heName = pd.get("HE_NAME").toString();
        String heId = pd.get("HE_ID").toString();
        // 我的用户名、ID
        String myName = pd.get("USER_NAME").toString();
        String myId = pd.get("USER_ID").toString();
        // 扣除转出用户 娱乐积分数量
        pd.put("ACCOUNT_ID", myId);
        pd.put("tag", "-");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateScoreNumber(pd);
        // 创建娱乐积分钱包记录
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "-");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("STATUS", "转出");    //状态
        score_recService.save(pd);

        // 增加转入用户 娱乐积分数量
        pd.put("ACCOUNT_ID", heId);
        pd.put("tag", "+");
        pd.put("GMT_MODIFIED", DateUtil.now());
        updateScoreNumber(pd);
        // 创建娱乐积分钱包记录
        pd.put("USER_NAME", heName);
        pd.put("USER_ID", heId);
        pd.put("AMOUNT_TYPE", "转账");    //金额类型
        pd.put("TAG", "+");    //标签 + 或者 -
        pd.put("MONEY", usdtNum);    //金额
        pd.put("HE_NAME", myName);    //对方用户名
        pd.put("HE_ID", myId);    //对方用户ID
        pd.put("STATUS", "转入");    //状态
        score_recService.save(pd);
    }

    /**
     * 更新推荐人数和团队数量
     *
     * @param user 用户信息
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateReAndTeamNumber(MemUser user) throws Exception {
        if ("10000".equals(user.getACCOUNT_ID())) {
            user.setRE_PATH(user.getACCOUNT_ID());
        } else {
            user.setRE_PATH(user.getRE_PATH() + "," + user.getACCOUNT_ID());
        }
        dao.update("AccountMapper.addRecommendQuantity", user);
        dao.update("AccountMapper.addTeamAmount", user);
    }

    /**
     * 更新每日提现累积
     *
     * @param user  用户信息
     * @param isAdd true:计次+1，false：重置每天提现累积为0
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateDayWithdrawalsCount(MemUser user, boolean isAdd) throws Exception {
        if (isAdd) {
            dao.update("AccountMapper.addDayWithdrawalsCount", user);
        } else {
            dao.update("AccountMapper.resetDayWithdrawalsCount", "");
        }
    }

    /**
     * 更新分享基金数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateSharedFundsNumber(PageData pd) throws Exception {
        if ("+".equals(pd.getString("tag"))) {
            dao.update("AccountMapper.addSharedFunds", pd);
        } else {
            dao.update("AccountMapper.reduceSharedFunds", pd);
        }
    }

    /**
     * 更新娱乐积分数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateScoreNumber(PageData pd) throws Exception {
        if ("+".equals(pd.getString("tag"))) {
            dao.update("AccountMapper.addScoreNumber", pd);
        } else {
            dao.update("AccountMapper.reduceScoreNumber", pd);
        }
    }

    /**
     * 更新USDT数量
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateUsdtNumber(PageData pd) throws Exception {
        if ("+".equals(pd.getString("tag"))) {
            dao.update("AccountMapper.addUsdt", pd);
        } else {
            dao.update("AccountMapper.reduceUsdt", pd);
        }
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAll() throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.listAll", new PageData());
    }

    /**
     * 列表(追龙未完成的)
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> notFinishedList() throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.notFinishedList", new PageData());
    }

    /**
     * 列表(所有上级)
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllSuperiorByPath(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.listAllSuperiorByPath", pd);
    }

    /**
     * 列表(所有下级)
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<PageData> listAllSubByUserId(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("AccountMapper.listAllSubByUserId", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findById", pd);
    }

    /**
     * 获取今日充值累积
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getDayRechargeTotalSum() throws Exception {
        return (PageData) dao.findForObject("AccountMapper.getDayRechargeTotalSum", "");
    }

    /**
     * 获取所有充值累积
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getRechargeTotalSumAll() throws Exception {
        return (PageData) dao.findForObject("AccountMapper.getRechargeTotalSumAll", "");
    }

    /**
     * 获取今日提现累积
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getDayWithdrawalTotalSum() throws Exception {
        return (PageData) dao.findForObject("AccountMapper.getDayWithdrawalTotalSum", "");
    }

    /**
     * 获取所有提现累积
     *
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getWithdrawalTotalSumAll() throws Exception {
        return (PageData) dao.findForObject("AccountMapper.getWithdrawalTotalSumAll", "");
    }

    /**
     * 通过用户id获取充值或者转账累计总额
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData getSumAmountByUserId(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.getSumAmountByUserId", pd);
    }

    /**
     * 查询团队业绩余额
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByIdTeamPerformance(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findByIdTeamPerformance", pd);
    }

    /**
     * 通过非休息号查询有多少团队活跃人数
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByActiveTeams(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findByActiveTeams", pd);
    }

    /**
     * 通过非休息号查询有多少直推活跃人数
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByActiveRecommended(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findByActiveRecommended", pd);
    }

    /**
     * 通过id获取数据返回实体
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public MemUser findByIdPojo(PageData pd) throws Exception {
        return (MemUser) dao.findForObject("AccountMapper.findByIdPojo", pd);
    }

    /**
     * 按用户名查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public MemUser findByName(PageData pd) throws Exception {
        return (MemUser) dao.findForObject("AccountMapper.findByNameReturnEntity", pd);
    }

    /**
     * 按邀请码查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public MemUser findByInvitationCode(PageData pd) throws Exception {
        return (MemUser) dao.findForObject("AccountMapper.findByInvitationCode", pd);
    }

    /**
     * 按用户名和密码查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public MemUser findByNameAndPass(PageData pd) throws Exception {
        return (MemUser) dao.findForObject("AccountMapper.findByNameAndPass", pd);
    }

    /**
     * 根据用户ID查询我的团队
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public List<MemUser> findByByIdOnPath(PageData pd) throws Exception {
        return (List<MemUser>) dao.findForList("AccountMapper.findByByIdOnPath", pd);
    }


}

