package com.ix.auth.endpoint;

import cn.hutool.core.util.StrUtil;
import com.ix.api.system.domain.SysClientDetails;
import com.ix.api.system.feign.RemoteOauth2ClientDetailsService;
import com.ix.framework.core.application.domain.AjaxResult;
import com.ix.framework.core.domain.utils.ResUtils;
import com.ix.framework.redis.service.constants.CacheConstants;
import com.ix.framework.security.exception.OAuthClientException;
import com.ix.framework.security.utils.OAuth2EndpointUtils;
import com.ix.framework.security.utils.OAuth2ErrorCodesExpand;
import com.ix.framework.utils.SpringContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 统一登录管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class IXTokenEndpoint {

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

	private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

	private final OAuth2AuthorizationService authorizationService;

	private final RemoteOauth2ClientDetailsService remoteOauth2ClientDetailsService;


	private final RedisTemplate<String, Object> redisTemplate;

	private final CacheManager cacheManager;

	/**
	 * 认证页面（localhost:8888/oauth/authorize?response_type=code&client_id=ix&scope=server&redirect_uri=https://www.ix.cn）
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("/login");
		modelAndView.addObject("error", error);
		return modelAndView;
	}

	@GetMapping("/confirm_access")
	public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {

		SysClientDetails clientDetails = ResUtils.of(remoteOauth2ClientDetailsService.getClientDetailsById(clientId))
				.getData().orElseThrow(() -> new OAuthClientException("clientId 不合法"));
		Set<String> authorizedScopes = StringUtils.commaDelimitedListToSet(clientDetails.getScope());
		modelAndView.addObject("clientId", clientId);
		modelAndView.addObject("state", state);
		modelAndView.addObject("scopeList", authorizedScopes);
		modelAndView.addObject("principalName", principal.getName());
		modelAndView.setViewName("/confirm");
		return modelAndView;
	}

	/**
	 * 退出并删除token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public AjaxResult logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return AjaxResult.success();
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return removeToken(tokenValue);
	}

	/**
	 * 校验token
	 * @param token 令牌
	 */
	@GetMapping("/check_token")
	public void checkToken(String token, HttpServletResponse response) {
		try {
			ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

			if (StrUtil.isBlank(token)) {
				httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
				this.errorHttpResponseConverter.write(new OAuth2Error(OAuth2ErrorCodesExpand.TOKEN_MISSING), null,
						httpResponse);
			}
			OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

			// 如果令牌不存在 返回401
			if (authorization == null) {
				httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
				this.errorHttpResponseConverter.write(new OAuth2Error(OAuth2ErrorCodesExpand.TOKEN_MISSING), null,
						httpResponse);
				return;
			}

			Map<String, Object> claims = authorization.getAccessToken().getClaims();
			OAuth2AccessTokenResponse sendAccessTokenResponse = OAuth2EndpointUtils
					.sendAccessTokenResponse(authorization, claims);
			this.accessTokenHttpResponseConverter.write(sendAccessTokenResponse, MediaType.APPLICATION_JSON,
					httpResponse);
		} catch (Exception e) {
			throw new RuntimeException("返回信息错误");
		}

	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@DeleteMapping("/{token}")
	public AjaxResult removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			return AjaxResult.success();
		}
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return AjaxResult.success();
		}
		// 清空用户信息
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.USER_DETAILS)).evict(authorization.getPrincipalName());
		// 清空access token
		authorizationService.remove(authorization);
		// 处理自定义退出事件，保存相关日志
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
				authorization.getPrincipalName(), authorization.getRegisteredClientId())));
		return AjaxResult.success();
	}

}
