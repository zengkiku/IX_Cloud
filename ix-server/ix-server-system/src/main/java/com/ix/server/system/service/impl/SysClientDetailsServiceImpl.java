package com.ix.server.system.service.impl;

import com.ix.api.system.domain.SysClientDetails;
import com.ix.api.system.domain.dto.SysClientDetailsDTO;
import com.ix.framework.redis.service.constants.CacheConstants;
import com.ix.server.system.mapper.SysClientDetailsMapper;
import com.ix.server.system.service.ISysClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 终端配置Service接口
 */
@Service
@RequiredArgsConstructor
public class SysClientDetailsServiceImpl implements ISysClientDetailsService {

	private final SysClientDetailsMapper sysClientDetailsMapper;

	/**
	 * 查询终端配置
	 * @param clientId 终端配置ID
	 * @return 终端配置
	 */
	// @TwSynchronized("anyLock")
	@Override
	public SysClientDetails selectSysClientDetailsById(String clientId) {
		return sysClientDetailsMapper.selectSysClientDetailsById(clientId);
	}

	/**
	 * 查询终端配置列表
	 * @param sysClientDetailsDTO 终端配置
	 * @return 终端配置
	 */
	@Override
	public List<SysClientDetails> selectSysClientDetailsList(SysClientDetailsDTO sysClientDetailsDTO) {
		return sysClientDetailsMapper.selectSysClientDetailsList(sysClientDetailsDTO);
	}

	/**
	 * 新增终端配置
	 * @param sysClientDetails 终端配置
	 * @return 结果
	 */
	@Override
	public int insertSysClientDetails(SysClientDetails sysClientDetails) {
		return sysClientDetailsMapper.insertSysClientDetails(sysClientDetails);
	}

	/**
	 * 修改终端配置
	 * @param sysClientDetails 终端配置
	 * @return 结果
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#sysClientDetails.clientId")
	public int updateSysClientDetails(SysClientDetails sysClientDetails) {
		return sysClientDetailsMapper.updateSysClientDetails(sysClientDetails);
	}

	/**
	 * 批量删除终端配置
	 * @param clientIds 需要删除的终端配置ID
	 * @return 结果
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
	public int deleteSysClientDetailsByIds(String[] clientIds) {
		return sysClientDetailsMapper.deleteSysClientDetailsByIds(clientIds);
	}

}
