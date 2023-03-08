package com.ix.framework.log.event;

import com.ix.api.system.domain.SysLoginInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 系统登录日志事件
 */
public class SysLoginLogEvent extends ApplicationEvent {

	public SysLoginLogEvent(SysLoginInfo source) {
		super(source);
	}

}