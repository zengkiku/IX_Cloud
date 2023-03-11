package com.ix.server.system.controller.api;

import com.ix.api.system.domain.SysClientDetails;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.domain.R;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.server.system.service.ISysClientDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: OAuth2 API
 */
@Tag(description = "Oauth2ClientDetailsApi", name = "OAuth2 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class Oauth2ClientDetailsApi extends IXController {

	private final ISysClientDetailsService sysClientDetailsService;

	/**
	 * 获取终端配置详细信息
	 * @param clientId 终端ID
	 * @return R<SysClientDetails>
	 */
	@Operation(summary = "获取终端配置详细信息")
	@AuthIgnore
	@GetMapping(value = "/{clientId}")
	public R<SysClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId) {
		return R.ok(sysClientDetailsService.selectSysClientDetailsById(clientId));
	}

}
