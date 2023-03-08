package com.ix.auth.support.handler;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.log.event.SysLoginLogEvent;
import com.ix.framework.log.utils.SysLogUtils;
import com.ix.framework.log.vo.SysLogVO;
import com.ix.framework.utils.DateUtils;
import com.ix.framework.utils.SpringContextHolder;
import com.ix.framework.utils.http.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 处理退出登录成功事件
 */
@Component
public class IXLogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

	private static final Logger log = LoggerFactory.getLogger(IXLogoutSuccessEventHandler.class);

	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		Authentication authentication = (Authentication) event.getSource();
		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			handle(authentication);
		}
	}

	/**
	 * 处理退出成功方法
	 * <p>
	 * 获取到登录的authentication 对象
	 * @param authentication 登录对象
	 */
	public void handle(Authentication authentication) {
		log.info("用户：{} 退出成功", authentication.getPrincipal());
		String username = authentication.getName();
		SysLoginInfo sysLoginInfo = new SysLoginInfo();
		SysLogVO sysLog = SysLogUtils.getSysLog();
		sysLoginInfo.setStatus(SecurityConstants.LOGOUT_SUCCESS);
		sysLoginInfo.setUserName(username);
		sysLoginInfo.setMsg("退出成功");
		sysLoginInfo.setIpaddr(IpUtils.getIpAddr());
		// 发送异步日志事件
		sysLoginInfo.setCreateTime(DateUtils.getNowDate());
		sysLoginInfo.setCreateBy(username);
		sysLoginInfo.setUpdateBy(username);
		SpringContextHolder.publishEvent(new SysLoginLogEvent(sysLoginInfo));
	}

}
