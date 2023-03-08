package com.ix.api.system.feign;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.api.system.domain.SysOperationLog;
import com.ix.api.system.feign.factory.RemoteLogFallbackFactory;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.constants.ServiceNameConstants;
import com.ix.framework.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 日志服务
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.SYSTEM_SERVICE,
		fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

	/**
	 * 保存系统日志
	 * @param sysOperationLog 日志实体
	 * @return 结果
	 */
	@PostMapping(value = "/api/operationLog", headers = SecurityConstants.HEADER_FROM_IN)
	R<Boolean> saveLog(@RequestBody SysOperationLog sysOperationLog);

	/**
	 * 保存登录记录
	 * @param sysLoginInfo 登录结果
	 * @return 结果
	 */
	@PostMapping(value = "/api/loginInfo", headers = SecurityConstants.HEADER_FROM_IN)
	R<Boolean> saveLoginInfo(@RequestBody SysLoginInfo sysLoginInfo);

}
