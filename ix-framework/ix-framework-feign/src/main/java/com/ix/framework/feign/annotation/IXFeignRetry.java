package com.ix.framework.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 重试注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IXFeignRetry {

	/**
	 * 重试策略 我们使用自定义的{@link IXBackoff}
	 */
	IXBackoff backoff() default @IXBackoff();

	/**
	 * 最大重试次数 默认3次
	 */
	int maxAttempt() default 3;

	/**
	 * 抛出指定异常才会重试
	 */
	Class<? extends Throwable>[] include() default {};

}
