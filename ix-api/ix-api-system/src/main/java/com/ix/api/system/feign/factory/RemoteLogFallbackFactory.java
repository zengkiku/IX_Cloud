package com.ix.api.system.feign.factory;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.api.system.domain.SysOperationLog;
import com.ix.api.system.feign.RemoteLogService;
import com.ix.framework.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 日志服务降级处理
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

	private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

	@Override
	public RemoteLogService create(Throwable throwable) {
		log.error("日志服务调用失败:{}", throwable.getMessage());
		return new RemoteLogService() {
			@Override
			public R<Boolean> saveLog(SysOperationLog sysOperationLog) {
				return R.fail();
			}

			@Override
			public R<Boolean> saveLoginInfo(SysLoginInfo sysLoginInfo) {
				return R.fail();
			}
		};
	}

}
