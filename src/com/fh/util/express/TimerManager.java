package com.fh.util.express;

import com.fh.util.Const;
import com.fh.util.QuartzManager;
import com.fh.util.timer.*;


/**
 * 定时任务控制
 *
 * @author liming
 */
public class TimerManager {


    public TimerManager() {

        // 添加每日0点重置用户提现次数任务
        String dayTime = "0 0 0 * * ?";
        QuartzManager.addJob("dayResetWithdrawalTask", DayResetWithdrawalTask.class, dayTime);

/*			// 隔4分钟查一次区块链余额并增加到用户钱包
		String inTime = "0 /4 * * * ? *";
		QuartzManager.addJob("TimingQuery_In", CheckingInTaskJob.class, inTime);

		// 从8分钟后开始，隔4分钟查一次区块链余额并减少用户钱包
		String outTiam = "0 8/4 * * * ? *";
		QuartzManager.addJob("TimingQuery_Out", CheckingOutTaskJob.class, outTiam);*/

        // 从0小时开始，隔6小时检查一次用户状态
        String userCheckTiam = "0 0 0/6 * * ? *";
        QuartzManager.addJob(Const.USER_STATUS_CHECK_TASK, UserStatusTask.class, userCheckTiam);

        // 默认隔10分钟查询发放一次跑单收益
        String minute = "10";
        String minutrCrom = "0 0/" + minute + " * * * ? *";
        QuartzManager.addJob(Const.INVEST_BONUS_TASK, InvestBonusTask.class, minutrCrom);

    }


}
