package com.ix.framework.log.annotation;

import com.ix.framework.log.enums.BusinessType;
import com.ix.framework.log.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 自定义操作日志记录注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	/**
	 * 模块名称
	 */
	String service() default "Unknown Service";

	/**
	 * 功能
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 操作人类别
	 */
	OperatorType operatorType() default OperatorType.MANAGE;

	/**
	 * 是否保存请求的参数
	 */
	boolean isSaveRequestData() default true;

}
