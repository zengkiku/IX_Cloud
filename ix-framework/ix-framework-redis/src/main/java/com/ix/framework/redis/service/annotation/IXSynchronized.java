package com.ix.framework.redis.service.annotation;

import java.lang.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 分布式锁（不支持重入）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IXSynchronized {

	/**
	 * 唯一锁名称
	 */
	String value();

}
