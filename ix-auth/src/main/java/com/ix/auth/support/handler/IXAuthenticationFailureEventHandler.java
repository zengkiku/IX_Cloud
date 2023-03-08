package com.ix.auth.support.handler;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.domain.R;
import com.ix.framework.log.event.SysLoginLogEvent;
import com.ix.framework.log.utils.SysLogUtils;
import com.ix.framework.log.vo.SysLogVO;
import com.ix.framework.utils.DateUtils;
import com.ix.framework.utils.SpringContextHolder;
import com.ix.framework.utils.TUtils;
import com.ix.framework.utils.http.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 处理登录以及续签失败
 */
public class IXAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

	private static final Logger log = LoggerFactory.getLogger(IXAuthenticationFailureEventHandler.class);

	private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 * request.
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		String username = request.getParameter(OAuth2ParameterNames.USERNAME);

		log.error("用户：{} 登录失败，异常：", username, exception);
		if (TUtils.isNotEmpty(username)) {
			SysLoginInfo sysLoginInfo = new SysLoginInfo();
			SysLogVO sysLog = SysLogUtils.getSysLog();
			sysLoginInfo.setUserName(username);
			sysLoginInfo.setIpaddr(IpUtils.getIpAddr());
			sysLoginInfo.setStatus(SecurityConstants.LOGIN_FAIL);
			sysLoginInfo.setMsg(exception.getLocalizedMessage());
			// 发送异步日志事件
			sysLoginInfo.setCreateTime(DateUtils.getNowDate());
			sysLoginInfo.setCreateBy(username);
			sysLoginInfo.setUpdateBy(username);
			SpringContextHolder.publishEvent(new SysLoginLogEvent(sysLoginInfo));
		}
		// 写出错误信息
		try {
			sendErrorResponse(request, response, exception);
		}
		catch (IOException e) {
			log.error("返回错误信息失败", e);
		}
	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.OK);
		this.errorHttpResponseConverter.write(R.fail(HttpStatus.BAD_REQUEST.value(), error.getDescription()),
				MediaType.APPLICATION_JSON, httpResponse);
	}

}
