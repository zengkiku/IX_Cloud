package com.ix.framework.security.annotation;

import java.lang.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 开放服务间认证权限（支持外部完全开放以及限制内部开放）
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {

	/**
	 * 默认只允许服务间访问,完全开放设置false
	 * @return false, true
	 */
	boolean value() default true;

}
