package com.ix.server.system.controller.api;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.utils.http.IpUtils;
import com.ix.server.system.service.ISysLoginInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 系统操作/访问日志
 */
@Tag(description = "SysLoginInfoApi", name = "系统操作日志API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loginInfo")
public class SysLoginInfoApi extends IXController {

	private final ISysLoginInfoService iSysLoginInfoService;

	/**
	 * 记录登录信息
	 * @param sysLoginInfo SysLoginInfo
	 */
	@Operation(summary = "记录登录信息")
	@AuthIgnore
	@PostMapping
	public void insertLog(@RequestBody SysLoginInfo sysLoginInfo) {
		iSysLoginInfoService.insertLoginInfo(sysLoginInfo);
	}

}
