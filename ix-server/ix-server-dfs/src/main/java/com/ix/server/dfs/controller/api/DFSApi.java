package com.ix.server.dfs.controller.api;

import com.ix.api.dfs.domain.SysDfs;
import com.ix.api.dfs.domain.SysFile;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.domain.R;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.framework.utils.file.FileUtils;
import com.ix.server.dfs.service.IDFSService;
import com.ix.server.dfs.service.impl.DFSServiceMinioImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 文件请求处理API
 */
@AuthIgnore
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DFSApi extends IXController {

//	@Autowired
//	private IDFSService dfsServiceMinioImpl;
	private final DFSServiceMinioImpl dfsServiceMinioImpl;

	/**
	 * 系统单文件上传API
	 * @param file MultipartFile
	 * @return R<SysFile>
	 */
	@PostMapping("/upload")
	public R<SysFile> upload(MultipartFile file) {
		// 上传并返回访问地址
		SysDfs sysDfs = dfsServiceMinioImpl.uploadFile(file);

		String path = sysDfs.getPath();

		SysFile sysFile = new SysFile();
		sysFile.setName(FileUtils.getName(path));
		sysFile.setUrl(path);

		return R.ok(sysFile);
	}

}
