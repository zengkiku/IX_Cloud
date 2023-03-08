package com.ix.framework.log;

import com.ix.api.system.feign.RemoteLogService;
import com.ix.framework.log.aspect.SysLogAspect;
import com.ix.framework.log.event.listener.SysLoginLogListener;
import com.ix.framework.log.event.listener.SysSysOperationLogEventLogListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 日志自动配置
 */
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogAutoConfiguration {

	/**
	 * 登录日志监听
	 * @param remoteLogService RemoteLogService
	 * @return SysLoginLogListener
	 */
	@Bean
	public SysLoginLogListener sysLoginLogListener(RemoteLogService remoteLogService) {
		return new SysLoginLogListener(remoteLogService);
	}

	/**
	 * 操作日志监听
	 * @param remoteLogService RemoteLogService
	 * @return SysLoginLogListener
	 */
	@Bean
	public SysSysOperationLogEventLogListener sysOperationLogEventLogListener(RemoteLogService remoteLogService) {
		return new SysSysOperationLogEventLogListener(remoteLogService);
	}

	/**
	 * 系统操作日志
	 * @return SysLogAspect
	 */
	@Bean
	public SysLogAspect sysLogAspect() {
		return new SysLogAspect();
	}

}