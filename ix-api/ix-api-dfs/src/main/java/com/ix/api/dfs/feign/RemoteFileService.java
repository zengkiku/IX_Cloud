package com.ix.api.dfs.feign;

import com.ix.api.dfs.domain.SysFile;
import com.ix.api.dfs.feign.factory.RemoteFileFallbackFactory;
import com.ix.framework.core.constants.SecurityConstants;
import com.ix.framework.core.constants.ServiceNameConstants;
import com.ix.framework.core.domain.R;
import com.ix.framework.feign.annotation.IXBackoff;
import com.ix.framework.feign.annotation.IXFeignRetry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: DFS服务
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE,
		fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService {

	/**
	 * 上传文件
	 * @param file 文件信息
	 * @return 结果
	 */
	@PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			headers = SecurityConstants.HEADER_FROM_IN)
	R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

}
