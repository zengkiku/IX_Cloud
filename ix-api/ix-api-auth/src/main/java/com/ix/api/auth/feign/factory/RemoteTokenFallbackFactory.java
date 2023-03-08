package com.ix.api.auth.feign.factory;

import com.ix.api.auth.feign.RemoteTokenService;
import com.ix.api.auth.feign.domain.dto.TokenDTO;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 令牌管理服务降级处理
 */
@Component
public class RemoteTokenFallbackFactory implements FallbackFactory<RemoteTokenService> {

	private static final Logger log = LoggerFactory.getLogger(RemoteTokenFallbackFactory.class);

	@Override
	public RemoteTokenService create(Throwable throwable) {
		log.error("令牌管理服务调用失败:{}", throwable.getMessage());
		return new RemoteTokenService() {

			@Override
			public R<TableDataInfo> getTokenPage(TokenDTO tokenDTO) {
				return R.fail();
			}

			@Override
			public R<Void> removeToken(String token) {
				return R.fail();
			}
		};
	}

}
