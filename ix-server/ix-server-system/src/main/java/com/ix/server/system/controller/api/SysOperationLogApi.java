package com.ix.server.system.controller.api;

import com.ix.api.system.domain.SysOperationLog;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.framework.core.application.controller.IXController;
import com.ix.server.system.service.ISysOperationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 操作日志记录
 */
@Tag(description = "SysOperationLogApi", name = "操作日志记录API")
@RestController
@RequestMapping("/api/operationLog")
public class SysOperationLogApi extends IXController {

	@Autowired
	private ISysOperationLogService iSysOperationLogService;

	/**
	 * 新增操作日志
	 * @param operationLog SysOperationLog
	 */
	@Operation(summary = "新增操作日志")
	@AuthIgnore
	@PostMapping
	public void saveLog(@RequestBody SysOperationLog operationLog) {
		iSysOperationLogService.insertOperationLog(operationLog);
	}

}
