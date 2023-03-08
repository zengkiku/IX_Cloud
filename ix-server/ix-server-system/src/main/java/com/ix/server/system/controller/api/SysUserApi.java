package com.ix.server.system.controller.api;

import com.ix.api.system.domain.SysUser;
import com.ix.api.system.model.UserInfo;
import com.ix.framework.core.application.controller.IXController;
import com.ix.framework.core.domain.R;
import com.ix.framework.security.annotation.AuthIgnore;
import com.ix.framework.utils.TUtils;
import com.ix.server.system.service.ISysPermissionService;
import com.ix.server.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 用户信息
 */
@Tag(description = "SysUserApi", name = "用户信息API")
@RestController
@RequestMapping("/api/user")
public class SysUserApi extends IXController {

	@Autowired
	private ISysUserService iSysUserService;

	@Autowired
	private ISysPermissionService iSysPermissionService;

	/**
	 * 获取当前用户信息(认证中心服务专用)
	 * @param username String
	 * @return R<UserInfo>
	 */
	@Operation(summary = "获取当前用户信息(认证中心服务专用)")
	@AuthIgnore
	@GetMapping("/info/{username}")
	public R<UserInfo> info(@PathVariable("username") String username) {
		SysUser sysUser = iSysUserService.selectUserByUserName(username, false);
		if (TUtils.isEmpty(sysUser)) {
			return R.fail("用户名或密码错误");
		}
		// 角色集合
		Set<String> roles = iSysPermissionService.getRolePermission(sysUser.getUserId());
		// 权限集合
		Set<String> permissions = iSysPermissionService.getMenuPermission(sysUser.getUserId());
		UserInfo sysUserVo = new UserInfo();
		sysUserVo.setSysUser(sysUser);
		sysUserVo.setRoles(roles);
		sysUserVo.setPermissions(permissions);
		return R.ok(sysUserVo);
	}

}
