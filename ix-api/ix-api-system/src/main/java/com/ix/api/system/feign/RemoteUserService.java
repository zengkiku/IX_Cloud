package com.ix.api.system.feign;

import com.ix.api.system.feign.factory.RemoteUserFallbackFactory;
import com.ix.api.system.model.UserInfo;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.constants.ServiceNameConstants;
import com.ix.framework.core.domain.R;
import com.ix.framework.feign.annotation.IXBackoff;
import com.ix.framework.feign.annotation.IXFeignRetry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 用户信息服务
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE,
		fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

	/**
	 * 通过用户名查询用户信息
	 * @param username 用户名称
	 * @return R<UserInfo>
	 */
	@IXFeignRetry(maxAttempt = 5, backoff = @IXBackoff(delay = 2000L))
	@GetMapping(value = "/api/user/info/{username}", headers = SecurityConstants.HEADER_FROM_IN)
	R<UserInfo> getUserInfo(@PathVariable("username") String username);

}
