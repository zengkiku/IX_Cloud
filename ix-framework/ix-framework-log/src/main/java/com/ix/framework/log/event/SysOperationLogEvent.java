package com.ix.framework.log.event;

import com.ix.api.system.domain.SysOperationLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 系统操作日志事件
 */
public class SysOperationLogEvent extends ApplicationEvent {

	public SysOperationLogEvent(SysOperationLog source) {
		super(source);
	}

}