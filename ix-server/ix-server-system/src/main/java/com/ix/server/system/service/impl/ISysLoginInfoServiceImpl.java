package com.ix.server.system.service.impl;

import com.ix.api.system.domain.SysLoginInfo;
import com.ix.api.system.domain.SysUser;
import com.ix.framework.utils.TUtils;
import com.ix.server.system.mapper.SysLoginInfoMapper;
import com.ix.server.system.mapper.SysUserMapper;
import com.ix.server.system.service.ISysLoginInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 系统操作/访问日志
 */
@Service
@RequiredArgsConstructor
public class ISysLoginInfoServiceImpl implements ISysLoginInfoService {


	private final SysLoginInfoMapper sysLoginInfoMapper;


	private final SysUserMapper sysUserMapper;

	/**
	 * 查询系统登录日志集合
	 * @param loginInfo 访问日志对象
	 * @return List<SysLoginInfo>
	 */
	@Override
	public List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo) {
		return sysLoginInfoMapper.selectLoginInfoList(loginInfo);
	}

	/**
	 * 批量删除系统登录日志
	 * @param infoIds 需要删除的登录日志ID
	 * @return 操作结果
	 */
	@Override
	public int deleteLoginInfoByIds(Long[] infoIds) {
		return sysLoginInfoMapper.deleteLoginInfoByIds(infoIds);
	}

	/**
	 * 清空系统登录日志
	 */
	@Override
	public void cleanLoginInfo() {
		sysLoginInfoMapper.cleanLoginInfo();
	}

	/**
	 * 新增系统登录日志
	 * @param loginInfo 访问日志对象
	 */
	@Override
	public int insertLoginInfo(SysLoginInfo loginInfo) {
		String userName = loginInfo.getUserName();

		if (TUtils.isNotEmpty(userName)) {
			SysUser sysUser = sysUserMapper.selectUserByUserName(userName);
			if (TUtils.isNotEmpty(sysUser)) {
				Long deptId = sysUser.getDeptId();
				loginInfo.setDeptId(deptId);
				return sysLoginInfoMapper.insertLoginInfo(loginInfo);
			}
		}

		return 0;
	}

}
