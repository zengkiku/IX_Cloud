package com.ix.api.system.feign;

import com.ix.api.system.domain.SysClientDetails;
import com.ix.api.system.feign.factory.RemoteOauth2ClientDetailsFallbackFactory;
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
 * @Description: Oauth2服务
 */
@FeignClient(contextId = "RemoteOauth2ClientDetailsService", value = ServiceNameConstants.SYSTEM_SERVICE,
		fallbackFactory = RemoteOauth2ClientDetailsFallbackFactory.class)
public interface RemoteOauth2ClientDetailsService {

	/**
	 * 获取终端配置详细信息
	 * @param clientId 终端ID
	 * @return JsonResult
	 */
	@IXFeignRetry(maxAttempt = 5, backoff = @IXBackoff(delay = 2000L))
	@GetMapping(value = "/api/client/{clientId}", headers = SecurityConstants.HEADER_FROM_IN)
	R<SysClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId);

}
