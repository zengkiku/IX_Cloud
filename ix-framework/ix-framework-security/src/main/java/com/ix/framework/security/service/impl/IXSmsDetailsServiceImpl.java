package com.ix.framework.security.service.impl;

import com.ix.api.system.domain.SysUser;
import com.ix.api.system.feign.RemoteUserService;
import com.ix.api.system.model.UserInfo;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.domain.R;
import com.ix.framework.core.domain.utils.ResUtils;
import com.ix.framework.redis.service.constants.CacheConstants;
import com.ix.framework.security.domain.LoginUser;
import com.ix.framework.security.exception.UserFrozenException;
import com.ix.framework.security.service.IXUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 自定义手机登录处理
 */
@RequiredArgsConstructor
public class IXSmsDetailsServiceImpl implements IXUserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(IXSmsDetailsServiceImpl.class);

	/**
	 * 仅支持后台登录
	 */
	private final static String PLAT_FORM = "admin";


	private final RemoteUserService remoteUserService;


	private final CacheManager cacheManager;

	/**
	 * 识别是否使用此登录器
	 * @param clientId 目标客户端
	 * @param grantType 登录类型
	 * @return boolean
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.SMS.equals(grantType);
	}

	/**
	 * 用户名称登录
	 * @param username String
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (cache != null && cache.get(username) != null) {
			return (LoginUser) cache.get(username).get();
		}
		R<UserInfo> userResult = remoteUserService.getUserInfo(username);
		auth(userResult, username);
		UserDetails userDetails = getUserDetails(userResult);

		if (cache != null) {
			cache.put(username, userDetails);
		}
		return userDetails;
	}

	/**
	 * 自定义账号状态检测
	 * @param userInfo userResult
	 * @param username username
	 */
	private void auth(R<UserInfo> userInfo, String username) {
		SysUser sysUser = ResUtils.of(userInfo).getData().orElseThrow(() -> {
			log.info("登录用户：{} 不存在.", username);
			return new UsernameNotFoundException("登录用户：" + username + " 不存在");
		}).getSysUser();

		// 获取用户状态信息
		if (sysUser.getStatus().equals("1")) {
			log.info("{}： 用户已被冻结.", username);
			throw new UserFrozenException("账号已被冻结");
		}
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
