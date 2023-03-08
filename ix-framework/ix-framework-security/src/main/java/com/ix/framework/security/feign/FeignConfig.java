package com.ix.framework.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: Feign 配置注册
 */
public class FeignConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new FeignRequestInterceptor();
	}

}
