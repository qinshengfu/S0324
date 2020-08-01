import com.fh.util.Tools;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * 说明：查询所有定时任务下次执行时间
 * 创建人：Ajie
 * 创建时间：2019年12月9日14:31:47
 */
//需要继承 ApplicationContextAware
public class TaskDetail implements ApplicationContextAware {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  //创建一个SchedulerFactory工厂实例

    private static ApplicationContext context = null;

    public static synchronized String taskMethod() {
        try {
            System.out.println("===获取所有定时任务信息开始===");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Scheduler scheduler = gSchedulerFactory.getScheduler();                //通过SchedulerFactory构建Scheduler对象
            //再获取Scheduler下的所有group
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                //组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    //通过triggerKey在scheduler中获取trigger对象
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    //获取trigger拥有的Job
                    JobKey jobKey = trigger.getJobKey();
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    //组装页面需要显示的数据
                    System.out.println("===[任务名称]:" + jobKey);
                    System.out.println("===[任务名称1]:" + jobDetail);
                    System.out.println("===[下次执行时间]:" + Tools.date2Str(trigger.getNextFireTime()));
                }
            }
            System.out.println("===获取所有定时任务信息结束===");
        } catch (Exception e) {
            System.out.println("获取定时任务信息出错" + e);
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.context = arg0;
    }


}