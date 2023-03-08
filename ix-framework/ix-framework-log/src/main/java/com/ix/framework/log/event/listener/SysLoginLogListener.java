package com.ix.framework.log.event.listener;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.api.system.feign.RemoteLogService;
import com.ix.framework.log.event.SysLoginLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 异步监听系统登录日志事件
 */
public class SysLoginLogListener {

	private final RemoteLogService remoteLogService;

	public SysLoginLogListener(RemoteLogService remoteLogService) {
		this.remoteLogService = remoteLogService;
	}

	@Async
	@Order
	@EventListener(SysLoginLogEvent.class)
	public void saveSysLog(SysLoginLogEvent event) {
		SysLoginInfo sysLoginInfo = (SysLoginInfo) event.getSource();
		remoteLogService.saveLoginInfo(sysLoginInfo);
	}

}