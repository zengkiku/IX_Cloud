package com.ix.server.dfs.controller;

import com.ix.api.dfs.domain.SysDfs;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.application.domain.JsonResult;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.jdbc.web.utils.PageUtils;
import com.ix.framework.log.annotation.Log;
import com.ix.framework.log.enums.BusinessType;
import com.ix.server.dfs.service.IDFSService;
import com.ix.server.dfs.service.impl.DFSServiceMinioImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 文件请求处理
 */
@Tag(description = "DFSController", name = "文件请求处理")
@RestController
@RequiredArgsConstructor
public class DFSController extends IXController {

	private final DFSServiceMinioImpl dfsServiceMinioImpl;

	/**
	 * 多文件上传
	 * @param files MultipartFile[]
	 * @return JsonResult<List<SysDfs>>
	 */
	@Operation(summary = "多文件上传")
	@Log(service = "多文件上传", businessType = BusinessType.IMPORT)
	@PostMapping("/batchUpload")
	public JsonResult<List<SysDfs>> batchUpload(MultipartFile[] files) {
		// 上传并返回访问地址
		List<SysDfs> sysDfsList = dfsServiceMinioImpl.uploadFiles(files);

		return JsonResult.success(sysDfsList);
	}

	/**
	 * 单文件上传
	 * @param file MultipartFile
	 * @return JsonResult<String>
	 */
	@Operation(summary = "单文件上传")
	@PostMapping("/commonUpload")
	@Log(service = "单文件上传", businessType = BusinessType.IMPORT)
	public JsonResult<String> commonUpload(MultipartFile file) {
		// 上传并返回访问地址
		SysDfs sysDfs = dfsServiceMinioImpl.uploadFile(file);
		return JsonResult.success("上传成功", sysDfs.getPath());
	}

	/**
	 * 批量删除文件
	 * @param fileIds 文件地址
	 * @return JsonResult<String>
	 */
	@Operation(summary = "批量删除文件")
	@PreAuthorize("@role.hasPermi('dfs:dfs:remove')")
	@Log(service = "删除文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
	public JsonResult<String> deleteFile(@PathVariable Long[] fileIds) {
		dfsServiceMinioImpl.deleteFile(fileIds);
		return JsonResult.success();
	}

	/**
	 * 分页查询
	 * @param sysDfs SysDfs
	 * @return JsonResult<TableDataInfo>
	 */
	@Operation(summary = "分页查询")
	@PreAuthorize("@role.hasPermi('dfs:dfs:list')")
	@GetMapping("/pageQuery")
	public JsonResult<TableDataInfo> pageQuery(SysDfs sysDfs) {
		PageUtils.startPage();
		List<SysDfs> sysDfsList = dfsServiceMinioImpl.selectSysDfsList(sysDfs);
		return JsonResult.success(PageUtils.getDataTable(sysDfsList));
	}

}