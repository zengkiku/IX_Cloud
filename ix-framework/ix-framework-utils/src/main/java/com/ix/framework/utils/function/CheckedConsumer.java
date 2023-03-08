package com.ix.framework.utils.function;

import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 函数式接口
 */
@FunctionalInterface
public interface CheckedConsumer<T> extends Serializable {

	/**
	 * Run the Consumer
	 * @param var1 T
	 * @throws Throwable UncheckedException
	 */

	@Nullable
	void accept(@Nullable T var1) throws Throwable;

}
