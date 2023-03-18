package com.ix.server.system.service.impl;

import com.ix.api.system.domain.SysRole;
import com.ix.api.system.domain.SysRoleDept;
import com.ix.api.system.domain.SysRoleMenu;
import com.ix.api.system.domain.SysUser;
import com.ix.framework.core.constants.UserConstants;
import com.ix.framework.core.exception.IXException;
import com.ix.framework.datascope.annotation.SysDataScope;
import com.ix.framework.redis.service.constants.CacheConstants;
import com.ix.framework.security.utils.SecurityUtils;
import com.ix.framework.utils.SpringContextHolder;
import com.ix.framework.utils.StringUtils;
import com.ix.framework.utils.TUtils;
import com.ix.server.system.mapper.SysRoleDeptMapper;
import com.ix.server.system.mapper.SysRoleMapper;
import com.ix.server.system.mapper.SysRoleMenuMapper;
import com.ix.server.system.mapper.SysUserRoleMapper;
import com.ix.server.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 角色权限服务
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {

	private final SysRoleMapper sysRoleMapper;

	private final SysRoleMenuMapper sysRoleMenuMapper;

	private final SysUserRoleMapper sysUserRoleMapper;

	private final SysRoleDeptMapper sysRoleDeptMapper;

	private final CacheManager cacheManager;

	/**
	 * 根据条件分页查询角色数据
	 * @param role 角色信息
	 * @return 角色数据集合信息
	 */
	@Override
	@SysDataScope(deptAlias = "d")
	public List<SysRole> selectRoleList(SysRole role) {
		return sysRoleMapper.selectRoleList(role);
	}

	/**
	 * 根据用户ID查询权限
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectRolePermissionByUserId(Long userId) {
		List<SysRole> perms = sysRoleMapper.selectRolePermissionByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (SysRole perm : perms) {
			if (TUtils.isNotEmpty(perm)) {
				permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 查询所有角色
	 * @return 角色列表
	 */
	@Override
	public List<SysRole> selectRoleAll() {
		return SpringContextHolder.getAopProxy(this).selectRoleList(new SysRole());
	}

	/**
	 * 根据用户ID获取角色选择框列表
	 * @param userId 用户ID
	 * @return 选中角色ID列表
	 */
	@Override
	public List<Integer> selectRoleListByUserId(Long userId) {
		return sysRoleMapper.selectRoleListByUserId(userId);
	}

	/**
	 * 通过角色ID查询角色
	 * @param roleId 角色ID
	 * @return 角色对象信息
	 */
	@Override
	public SysRole selectRoleById(Long roleId) {
		return sysRoleMapper.selectRoleById(roleId);
	}

	/**
	 * 校验角色名称是否唯一
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public String checkRoleNameUnique(SysRole role) {
		long roleId = TUtils.isEmpty(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = sysRoleMapper.checkRoleNameUnique(role.getRoleName());
		if (TUtils.isNotEmpty(info) && info.getRoleId() != roleId) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * 校验角色权限是否唯一
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public String checkRoleKeyUnique(SysRole role) {
		long roleId = TUtils.isEmpty(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = sysRoleMapper.checkRoleKeyUnique(role.getRoleKey());
		if (TUtils.isNotEmpty(info) && info.getRoleId() != roleId) {
			return UserConstants.NOT_UNIQUE;
		}
		return UserConstants.UNIQUE;
	}

	/**
	 * 校验角色是否允许操作
	 * @param role 角色信息
	 */
	@Override
	public void checkRoleAllowed(SysRole role) {
		if (TUtils.isNotEmpty(role.getRoleId()) && role.isAdmin()) {
			throw new IXException("不允许操作超级管理员角色");
		}
	}

	/**
	 * 校验角色是否有数据权限
	 * @param roleId 角色id
	 */
	@Override
	public void checkRoleDataScope(Long roleId) {
		if (!SysUser.isAdmin(SecurityUtils.getLoginUser().getUserId())) {
			SysRole role = new SysRole();
			role.setRoleId(roleId);
			List<SysRole> roles = SpringContextHolder.getAopProxy(this).selectRoleList(role);
			if (StringUtils.isEmpty(roles)) {
				throw new IXException("没有权限访问角色数据！");
			}
		}
	}

	/**
	 * 通过角色ID查询角色使用数量
	 * @param roleId 角色ID
	 * @return 结果
	 */
	@Override
	public int countUserRoleByRoleId(Long roleId) {
		return sysUserRoleMapper.countUserRoleByRoleId(roleId);
	}

	/**
	 * 新增保存角色信息
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = IXException.class)
	public int insertRole(SysRole role) {
		// 新增角色信息
		sysRoleMapper.insertRole(role);
		return insertRoleMenu(role);
	}

	/**
	 * 修改保存角色信息
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = IXException.class)
	public int updateRole(SysRole role) {
		// 修改角色信息
		sysRoleMapper.updateRole(role);
		// 删除角色与菜单关联
		sysRoleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
		// 删除角色与部门关联
		sysRoleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());

		// 新增数据权限
		insertRoleDept(role);

		// 清空userinfo
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.USER_DETAILS)).clear();

		return insertRoleMenu(role);
	}

	/**
	 * 修改角色状态
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public int updateRoleStatus(SysRole role) {
		return sysRoleMapper.updateRole(role);
	}

	/**
	 * 修改数据权限信息
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional(rollbackFor = IXException.class)
	public int authDataScope(SysRole role) {
		// 修改角色信息
		sysRoleMapper.updateRole(role);
		// 删除角色与部门关联
		sysRoleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
		// 新增角色和部门信息（数据权限）
		return insertRoleDept(role);
	}

	/**
	 * 新增角色菜单信息
	 * @param role 角色对象
	 */
	public int insertRoleMenu(SysRole role) {
		int rows = 1;
		// 新增用户与角色管理
		List<SysRoleMenu> list = new ArrayList<>();
		for (Long menuId : role.getMenuIds()) {
			SysRoleMenu rm = new SysRoleMenu();
			rm.setRoleId(role.getRoleId());
			rm.setMenuId(menuId);
			list.add(rm);
		}
		if (list.size() > 0) {
			rows = sysRoleMenuMapper.batchRoleMenu(list);
		}
		return rows;
	}

	/**
	 * 新增角色部门信息(数据权限)
	 * @param role 角色对象
	 */
	public int insertRoleDept(SysRole role) {
		int rows = 1;
		// 新增角色与部门（数据权限）管理
		List<SysRoleDept> list = new ArrayList<>();
		for (Long deptId : role.getDeptIds()) {
			SysRoleDept rd = new SysRoleDept();
			rd.setRoleId(role.getRoleId());
			rd.setDeptId(deptId);
			list.add(rd);
		}
		if (list.size() > 0) {
			rows = sysRoleDeptMapper.batchRoleDept(list);
		}
		return rows;
	}

	/**
	 * 通过角色ID删除角色
	 * @param roleId 角色ID
	 * @return 结果
	 */
	@Override
	public int deleteRoleById(Long roleId) {
		return sysRoleMapper.deleteRoleById(roleId);
	}

	/**
	 * 批量删除角色信息
	 * @param roleIds 需要删除的角色ID
	 * @return 结果
	 */
	@Override
	public int deleteRoleByIds(Long[] roleIds) {
		for (Long roleId : roleIds) {
			checkRoleAllowed(new SysRole(roleId));
			checkRoleDataScope(roleId);
			SysRole role = selectRoleById(roleId);
			if (countUserRoleByRoleId(roleId) > 0) {
				throw new IXException(String.format("%1$s已分配,不能删除", role.getRoleName()));
			}
		}
		return sysRoleMapper.deleteRoleByIds(roleIds);
	}

}
