package com.fh.service.front;

import com.fh.entity.MemUser;
import com.fh.entity.Page;
import com.fh.util.PageData;

import java.util.List;

/**
 * 说明： 前台用户表接口
 * 创建人：
 * 创建时间：2020-03-25
 */
public interface AccountManager {

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    void save(PageData pd) throws Exception;

    /**
     * 清除所有用户，保留顶点账号
     *
     * @param pd
     * @throws Exception
     */
    void deleteAll(PageData pd) throws Exception;

    /**
     * 重置顶点账号
     *
     * @param pd
     * @throws Exception
     */
    void resetAccount(PageData pd) throws Exception;

    /**
     * 修改测试
     *
     * @param pd
     * @throws Exception
     */
    int updateTest(PageData pd) throws Exception;

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    void edit(PageData pd) throws Exception;

    /**
     * 修改去掉乐观锁
     *
     * @param pd
     * @throws Exception
     */
    void editFor(PageData pd) throws Exception;

    /**
     * 用户线上充值增加usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateRechargeMoneyAndNewRec(PageData pd) throws Exception;

    /**
     * 用户线下充值创建待审核记录
     *
     * @param pd
     * @throws Exception
     */
    void updateOffLineRechargeMoneyAndRec(PageData pd) throws Exception;

    /**
     * 用户线下充值更新记录通过
     *
     * @param pd
     * @throws Exception
     */
    void updateOffLineRechargeRecSuccess(PageData pd) throws Exception;

    /**
     * 用户线下充值更新记录驳回
     *
     * @param pd
     * @throws Exception
     */
    void updateOffLineRechargeRecFail(PageData pd) throws Exception;

    /**
     * 后台充值增加usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateAdminRechargeMoneyAndNewRec(PageData pd) throws Exception;

    /**
     * 用户投资减少usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateInvestMoneyAndNewRec(PageData pd) throws Exception;

    /**
     * 用户提币减少usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateWithdrawMoney(PageData pd) throws Exception;

    /**
     * 用户转账更新usdt并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateUsdtTransfer(PageData pd) throws Exception;

    /**
     * 用户转账更新分享基金并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateFundransfer(PageData pd) throws Exception;

    /**
     * 用户转账更新娱乐积分并创建记录
     *
     * @param pd
     * @throws Exception
     */
    void updateIntegral(PageData pd) throws Exception;

    /**
     * 更新推荐人数和团队数量
     *
     * @param user
     * @throws Exception
     */
    void updateReAndTeamNumber(MemUser user) throws Exception;

    /**
     * 更新每日提现累积
     *
     * @param user 用户信息
     * @param isAdd true:计次+1，false：重置每天提现累积为0
     * @throws Exception
     */
    void updateDayWithdrawalsCount(MemUser user, boolean isAdd) throws Exception;

    /**
     * 更新分享基金数量
     *
     * @param pd
     * @throws Exception
     */
    void updateSharedFundsNumber(PageData pd) throws Exception;

    /**
     * 更新娱乐积分数量
     *
     * @param pd
     * @throws Exception
     */
    void updateScoreNumber(PageData pd) throws Exception;

    /**
     * 更新USDT数量
     *
     * @param pd
     * @throws Exception
     */
    void updateUsdtNumber(PageData pd) throws Exception;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    List<PageData> list(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @throws Exception
     */
    List<PageData> listAll() throws Exception;

    /**
     * 列表(追龙未完成的)
     *
     * @throws Exception
     */
    List<PageData> notFinishedList() throws Exception;

    /**
     * 列表(所有上级)
     *
     * @throws Exception
     */
    List<PageData> listAllSuperiorByPath(PageData pd) throws Exception;

    /**
     * 列表(所有下级)
     *
     * @throws Exception
     */
    List<PageData> listAllSubByUserId(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    PageData findById(PageData pd) throws Exception;

    /**
     * 获取今日充值累积
     *
     * @throws Exception
     */
    PageData getDayRechargeTotalSum() throws Exception;

    /**
     * 获取所有充值累积
     *
     * @throws Exception
     */
    PageData getRechargeTotalSumAll() throws Exception;

    /**
     * 获取今日提现累积
     *
     * @throws Exception
     */
    PageData getDayWithdrawalTotalSum() throws Exception;

    /**
     * 获取所有提现累积
     *
     * @throws Exception
     */
    PageData getWithdrawalTotalSumAll() throws Exception;

    /**
     * 通过用户id获取充值或者转账累计总额
     *
     * @param pd
     * @throws Exception
     */
    PageData getSumAmountByUserId(PageData pd) throws Exception;

    /**
     * 查询团队业绩余额
     *
     * @param pd
     * @throws Exception
     */
    PageData findByIdTeamPerformance(PageData pd) throws Exception;

    /**
     * 通过非休息号查询有多少团队活跃人数
     *
     * @param pd
     * @throws Exception
     */
    PageData findByActiveTeams(PageData pd) throws Exception;

    /**
     * 通过非休息号查询有多少直推活跃人数
     *
     * @param pd
     * @throws Exception
     */
    PageData findByActiveRecommended(PageData pd) throws Exception;

    /**
     * 通过id获取数据返回实体
     *
     * @param pd
     * @throws Exception
     */
    MemUser findByIdPojo(PageData pd) throws Exception;

    /**
     * 按用户名查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    MemUser findByName(PageData pd) throws Exception;

    /**
     * 按邀请码查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    MemUser findByInvitationCode(PageData pd) throws Exception;

    /**
     * 按用户名和密码查找返回实体
     *
     * @param pd
     * @throws Exception
     */
    MemUser findByNameAndPass(PageData pd) throws Exception;

    /**
     * 根据用户ID查询我的团队
     *
     * @param pd
     * @throws Exception
     */
    List<MemUser> findByByIdOnPath(PageData pd) throws Exception;


}

