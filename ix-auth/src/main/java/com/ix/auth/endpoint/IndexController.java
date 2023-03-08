package com.ix.auth.endpoint;

import com.ix.framework.core.application.domain.AjaxResult;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 测试第三方登录
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public AjaxResult callback(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
		return AjaxResult.success(oAuth2AuthenticationToken);
	}

}
