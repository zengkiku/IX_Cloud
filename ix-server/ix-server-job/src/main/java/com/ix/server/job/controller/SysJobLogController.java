package com.ix.server.job.controller;

import com.ix.api.job.domain.SysJobLog;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.application.domain.JsonResult;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.jdbc.web.utils.PageUtils;
import com.ix.framework.log.annotation.Log;
import com.ix.framework.log.enums.BusinessType;
import com.ix.framework.utils.poi.ExcelUtils;
import com.ix.server.job.service.ISysJobLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 调度日志操作处理
 */
@Tag(description = "SysJobLogController", name = "调度日志操作处理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class SysJobLogController extends IXController {

	private final ISysJobLogService jobLogService;

	/**
	 * 查询定时任务调度日志列表
	 * @param sysJobLog SysJobLog
	 * @return JsonResult
	 */
	@Operation(summary = "查询定时任务调度日志列表")
	@GetMapping("/pageQuery")
	@PreAuthorize("@role.hasPermi('system:job:list')")
	public JsonResult<TableDataInfo> pageQuery(SysJobLog sysJobLog) {
		PageUtils.startPage();
		List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
		return JsonResult.success(PageUtils.getDataTable(list));
	}

	/**
	 * 导出定时任务调度日志列表
	 * @param response HttpServletResponse
	 * @param sysJobLog SysJobLog
	 */
	@Operation(summary = "导出定时任务调度日志列表")
	@Log(service = "任务调度日志", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@PreAuthorize("@role.hasPermi('system:job:export')")
	public void export(HttpServletResponse response, @RequestBody SysJobLog sysJobLog) {
		List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
		ExcelUtils<SysJobLog> excelUtils = new ExcelUtils<>(SysJobLog.class);
		excelUtils.exportExcel(response, list, "调度日志");
	}

	/**
	 * 根据调度编号获取详细信息
	 * @param jobLogId id
	 * @return JsonResult
	 */
	@Operation(summary = "根据调度编号获取详细信息")
	@GetMapping("/{jobLogId}")
	@PreAuthorize("@role.hasPermi('system:job:query')")
	public JsonResult<SysJobLog> getInfo(@PathVariable Long jobLogId) {
		return JsonResult.success(jobLogService.selectJobLogById(jobLogId));
	}

	/**
	 * 删除定时任务调度日志
	 * @param jobLogIds 数组id
	 * @return JsonResult
	 */
	@Operation(summary = "删除定时任务调度日志")
	@Log(service = "定时任务调度日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobLogIds}")
	@PreAuthorize("@role.hasPermi('system:job:remove')")
	public JsonResult<String> remove(@PathVariable Long[] jobLogIds) {
		return json(jobLogService.deleteJobLogByIds(jobLogIds));
	}

	/**
	 * 清空定时任务调度日志
	 * @return JsonResult
	 */
	@Operation(summary = "清空定时任务调度日志")
	@Log(service = "调度日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clean")
	@PreAuthorize("@role.hasPermi('system:job:remove')")
	public JsonResult<String> clean() {
		jobLogService.cleanJobLog();
		return JsonResult.success();
	}

}
