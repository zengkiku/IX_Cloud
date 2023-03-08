package com.ix.server.job.util;

import com.ix.api.job.domain.SysJob;
import org.quartz.JobExecutionContext;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 定时任务工具类
 */
public class QuartzJobExecution extends AbstractQuartzJob {

	@Override
	protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
		JobInvokeUtil.invokeMethod(sysJob);
	}

}
