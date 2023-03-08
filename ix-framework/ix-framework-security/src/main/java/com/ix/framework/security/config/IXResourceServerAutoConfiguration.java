package com.ix.framework.security.config;

import com.ix.framework.security.service.PermissionService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 注入Bean配置
 */
@EnableConfigurationProperties(AuthIgnoreConfig.class)
public class IXResourceServerAutoConfiguration {

	/**
	 * 鉴权具体的实现逻辑
	 * @return （#role.xxx）
	 */
	@Bean("role")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 请求令牌的抽取逻辑
	 * @param urlProperties 对外暴露的接口列表
	 * @return BearerTokenExtractor
	 */
	@Bean
	public IXBearerTokenExtractor IXBearerTokenExtractor(AuthIgnoreConfig urlProperties) {
		return new IXBearerTokenExtractor(urlProperties);
	}

	/**
	 * 资源服务器异常处理
	 * @return ResourceAuthExceptionEntryPoint
	 */
	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(MessageSource securityMessageSource) {
		return new ResourceAuthExceptionEntryPoint(securityMessageSource);
	}

	/**
	 * 资源服务器toke内省处理器
	 * @return TokenIntrospector
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService oAuth2AuthorizationService) {
		return new IXCustomOpaqueTokenIntrospect(oAuth2AuthorizationService);
	}

}
