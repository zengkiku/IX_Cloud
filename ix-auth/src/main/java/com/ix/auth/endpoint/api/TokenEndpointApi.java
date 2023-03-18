package com.ix.auth.endpoint.api;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.util.StrUtil;
import com.ix.api.auth.feign.domain.dto.TokenDTO;
import com.ix.api.auth.feign.domain.vo.TokenVo;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.core.domain.R;
import com.ix.framework.redis.service.constants.CacheConstants;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.framework.utils.SpringContextHolder;
import com.ix.framework.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 统一管理API
 */
@Tag(description = "TokenEndpointApi", name = "TokenEndpointApi")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenEndpointApi {

	private final RedisTemplate<String, Object> redisTemplate;

	private final CacheManager cacheManager;

	private final OAuth2AuthorizationService authorizationService;

	/**
	 * 删除token
	 * @param token token
	 * @return R<Void>
	 */
	@Operation(summary = "删除token")
	@AuthIgnore
	@DeleteMapping("/{token}")
	public R<Void> removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return R.ok();
		}
		// 清空用户信息
		cacheManager.getCache(CacheConstants.USER_DETAILS).evict(authorization.getPrincipalName());
		// 清空access token
		authorizationService.remove(authorization);
		// 处理自定义退出事件，保存相关日志
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
				authorization.getPrincipalName(), authorization.getRegisteredClientId())));
		return R.ok();
	}

	/**
	 * 分页查询Token
	 * @param tokenDTO TokenDTO
	 * @return R<TableDataInfo>
	 */
	@Operation(summary = "分页token 信息")
	@AuthIgnore
	@GetMapping("/pageQuery")
	public R<TableDataInfo> tokenList(TokenDTO tokenDTO) {
		String username = tokenDTO.getUsername();
		Integer current = tokenDTO.getCurrent();
		Integer pageSize = tokenDTO.getPageSize();

		// 根据分页参数获取对应数据
		String key = String.format("%s::*", CacheConstants.PROJECT_OAUTH_ACCESS);
		if (StringUtils.isNotEmpty(username)) {
			key = String.format("%s::*%s*", CacheConstants.PROJECT_OAUTH_ACCESS, username);
		}
		Set<String> keys = redisTemplate.keys(key);
		assert keys != null;
		List<String> pages = keys.stream().skip((long) (current - 1) * pageSize).limit(pageSize)
				.collect(Collectors.toList());

		TableDataInfo tableDataInfo = new TableDataInfo();

		List<TokenVo> tokenVoList = Objects.requireNonNull(redisTemplate.opsForValue().multiGet(pages)).stream().map(obj -> {
			OAuth2Authorization authorization = (OAuth2Authorization) obj;
			TokenVo tokenVo = new TokenVo();
			tokenVo.setClientId(authorization.getRegisteredClientId());
			tokenVo.setId(authorization.getId());
			tokenVo.setUsername(authorization.getPrincipalName());
			tokenVo.setTokenType(authorization.getAuthorizationGrantType().toString());
			tokenVo.setScope(authorization.getAuthorizationGrantType().toString());

			OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
			tokenVo.setAccessToken(accessToken.getToken().getTokenValue());

			OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
			assert refreshToken != null;
			tokenVo.setRefreshToken(refreshToken.getToken().getTokenValue());

			String expiresAt = TemporalAccessorUtil.format(accessToken.getToken().getExpiresAt(),
					DatePattern.NORM_DATETIME_PATTERN);
			tokenVo.setExpiresAt(expiresAt);

			String issuedAt = TemporalAccessorUtil.format(accessToken.getToken().getIssuedAt(),
					DatePattern.NORM_DATETIME_PATTERN);
			tokenVo.setIssuedAt(issuedAt);
			return tokenVo;
		}).collect(Collectors.toList());

		tableDataInfo.setRecords(tokenVoList);
		tableDataInfo.setTotal(keys.size());

		return R.ok(tableDataInfo);
	}

}