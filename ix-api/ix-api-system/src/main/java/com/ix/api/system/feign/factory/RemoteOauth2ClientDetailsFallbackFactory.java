package com.ix.api.system.feign.factory;

import com.ix.api.system.domain.SysClientDetails;
import com.ix.api.system.feign.RemoteOauth2ClientDetailsService;
import com.ix.framework.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: Oauth2服务降级处理
 */
@Component
public class RemoteOauth2ClientDetailsFallbackFactory implements FallbackFactory<RemoteOauth2ClientDetailsService> {

	private static final Logger log = LoggerFactory.getLogger(RemoteOauth2ClientDetailsFallbackFactory.class);

	@Override
	public RemoteOauth2ClientDetailsService create(Throwable throwable) {
		log.error("Oauth2服务调用失败:{}", throwable.getMessage());
		return new RemoteOauth2ClientDetailsService() {

			@Override
			public R<SysClientDetails> getClientDetailsById(String clientId) {
				return R.fail();
			}
		};
	}

}
