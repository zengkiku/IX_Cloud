package com.ix.framework.security.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 用户已被冻结
 */
public class UserFrozenException extends AccessDeniedException {

	public UserFrozenException(String msg) {
		super(msg);
	}

	public UserFrozenException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
