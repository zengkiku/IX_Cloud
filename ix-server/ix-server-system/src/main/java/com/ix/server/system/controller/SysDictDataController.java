package com.ix.server.system.controller;

import com.ix.api.system.domain.SysDictData;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.application.domain.JsonResult;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.jdbc.web.utils.PageUtils;
import com.ix.framework.log.annotation.Log;
import com.ix.framework.log.enums.BusinessType;
import com.ix.framework.security.utils.SecurityUtils;
import com.ix.framework.utils.poi.ExcelUtils;
import com.ix.server.system.service.ISysDictDataService;
import com.ix.server.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 数据字典信息
 */
@Tag(description = "SysDictDataController", name = "数据字典信息")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionaries/data")
public class SysDictDataController extends IXController {


	private final ISysDictDataService dictDataService;


	private final ISysDictTypeService dictTypeService;

	/**
	 * 分页查询数据字典
	 * @param sysDictData SysDictData
	 * @return JsonResult<TableDataInfo>
	 */
	@Operation(summary = "分页查询数据字典")
	@GetMapping("/pageQuery")
	@PreAuthorize("@role.hasPermi('system:dict:list')")
	public JsonResult<TableDataInfo> pageQuery(SysDictData sysDictData) {
		PageUtils.startPage();
		List<SysDictData> list = dictDataService.selectDictDataList(sysDictData);
		return JsonResult.success(PageUtils.getDataTable(list));
	}

	/**
	 * 导出数据字典excel
	 * @param response HttpServletResponse
	 * @param sysDictData SysDictData
	 */
	@Operation(summary = "导出数据字典excel")
	@Log(service = "字典数据", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@PreAuthorize("@role.hasPermi('system:dict:export')")
	public void exportExcel(HttpServletResponse response, @RequestBody SysDictData sysDictData) {
		List<SysDictData> list = dictDataService.selectDictDataList(sysDictData);
		ExcelUtils<SysDictData> excelUtils = new ExcelUtils<>(SysDictData.class);
		excelUtils.exportExcel(response, list, "字典数据");
	}

	/**
	 * 查询字典数据详细
	 * @param dictCode 数据字典Code
	 * @return JsonResult<SysDictData>
	 */
	@Operation(summary = "查询字典数据详细")
	@GetMapping(value = "/{dictCode}")
	@PreAuthorize("@role.hasPermi('system:dict:query')")
	public JsonResult<SysDictData> getDictDataById(@PathVariable Long dictCode) {
		return JsonResult.success(dictDataService.selectDictDataById(dictCode));
	}

	/**
	 * 根据字典类型查询字典数据信息
	 * @param dictType 字典类型
	 * @return JsonResult<List<SysDictData>>
	 */
	@Operation(summary = "根据字典类型查询字典数据信息")
	@GetMapping(value = "/type/{dictType}")
	public JsonResult<List<SysDictData>> dictType(@PathVariable String dictType) {
		return JsonResult.success(dictTypeService.selectDictDataByType(dictType));
	}

	/**
	 * 新增字典类型
	 * @param sysDictData SysDictData
	 * @return JsonResult<String>
	 */
	@Operation(summary = "新增字典类型")
	@Log(service = "字典数据", businessType = BusinessType.INSERT)
	@PostMapping
	@PreAuthorize("@role.hasPermi('system:dict:insert')")
	public JsonResult<String> insert(@Validated @RequestBody SysDictData sysDictData) {
		sysDictData.setCreateBy(SecurityUtils.getUsername());
		return json(dictDataService.insertDictData(sysDictData));
	}

	/**
	 * 修改保存字典类型
	 * @param sysDictData SysDictData
	 * @return JsonResult<String>
	 */
	@Operation(summary = "修改保存字典类型")
	@Log(service = "字典数据", businessType = BusinessType.UPDATE)
	@PutMapping
	@PreAuthorize("@role.hasPermi('system:dict:update')")
	public JsonResult<String> update(@Validated @RequestBody SysDictData sysDictData) {
		sysDictData.setUpdateBy(SecurityUtils.getUsername());
		return json(dictDataService.updateDictData(sysDictData));
	}

	/**
	 * 删除字典类型
	 * @param dictCodes 字典类型Codes
	 * @return JsonResult<String>
	 */
	@Operation(summary = "删除字典类型")
	@Log(service = "字典类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dictCodes}")
	@PreAuthorize("@role.hasPermi('system:dict:remove')")
	public JsonResult<String> remove(@PathVariable Long[] dictCodes) {
		return json(dictDataService.deleteDictDataByIds(dictCodes));
	}

}
