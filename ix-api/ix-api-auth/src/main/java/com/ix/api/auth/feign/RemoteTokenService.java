package com.ix.api.auth.feign;

import com.ix.api.auth.feign.domain.dto.TokenDTO;
import com.ix.api.auth.feign.factory.RemoteTokenFallbackFactory;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.constants.ServiceNameConstants;
import com.ix.framework.core.domain.R;
import com.ix.framework.feign.annotation.IXBackoff;
import com.ix.framework.feign.annotation.IXFeignRetry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 令牌管理服务
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE,
		fallbackFactory = RemoteTokenFallbackFactory.class)
public interface RemoteTokenService {

	/**
	 * 分页查询token 信息
	 * @param tokenDTO TokenDTO
	 * @return R<TableDataInfo>
	 */
	@IXFeignRetry(maxAttempt = 5, backoff = @IXBackoff(delay = 2000L))
	@GetMapping(value = "/api/token/pageQuery", headers = SecurityConstants.HEADER_FROM_IN)
	R<TableDataInfo> getTokenPage(@SpringQueryMap TokenDTO tokenDTO);

	/**
	 * 删除token
	 * @param token token
	 * @return R<Void>
	 */
	@DeleteMapping(value = "/api/token/{token}", headers = SecurityConstants.HEADER_FROM_IN)
	R<Void> removeToken(@PathVariable("token") String token);

}
