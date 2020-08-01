package com.fh.util.timer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.fh.controller.base.BaseController;
import com.fh.service.front.AccountManager;
import com.fh.service.front.Sys_configManager;
import com.fh.service.record.Recharge_recordManager;
import com.fh.util.BlockUtil;
import com.fh.util.PageData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 说明：每天0点重置所有用户提现次数为0
 * 创建人：Ajie
 * 创建时间：2020年6月27日10:38:32
 */
public class DayResetWithdrawalTask extends BaseController implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("开始重置用户每日提现次数为0" + DateUtil.now());
        // 普通类从spring容器中拿出service
        WebApplicationContext webctx = ContextLoader.getCurrentWebApplicationContext();
        // 用户表
        AccountManager accountService = (AccountManager) webctx.getBean("accountService");

        try {
            accountService.updateDayWithdrawalsCount(null, false);
        } catch (Exception e) {
            logger.info(DateUtil.now() + "---每天0点重置所有用户提现次数为0 失败原因: " + e.getMessage());
        }

    }


}
