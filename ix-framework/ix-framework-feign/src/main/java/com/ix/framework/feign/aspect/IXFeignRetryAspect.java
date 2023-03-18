/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ix.framework.feign.aspect;

import com.ix.framework.feign.annotation.IXFeignRetry;
import feign.RetryableException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 重试aop
 */
@Aspect
public class IXFeignRetryAspect {

	private static final Logger log = LoggerFactory.getLogger(IXFeignRetryAspect.class);

	@Around("@annotation(com.ix.framework.feign.annotation.IXFeignRetry)")
	public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = getCurrentMethod(joinPoint);
		IXFeignRetry ixFeignRetry = method.getAnnotation(IXFeignRetry.class);

		RetryTemplate retryTemplate = new RetryTemplate();
		// Setter for BackOffPolicy.
		retryTemplate.setBackOffPolicy(prepareBackOffPolicy(ixFeignRetry));
		// Setter for RetryPolicy.
		retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(ixFeignRetry));

		// execute
		return retryTemplate.execute(arg0 -> {
			int retryCount = arg0.getRetryCount();
			log.info("Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}", method.getName(),
					ixFeignRetry.maxAttempt(), ixFeignRetry.backoff().delay(), retryCount);
			return joinPoint.proceed(joinPoint.getArgs());
		});
	}

	private BackOffPolicy prepareBackOffPolicy(IXFeignRetry ixFeignRetry) {
		if (ixFeignRetry.backoff().multiplier() != 0) {
			ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
			backOffPolicy.setInitialInterval(ixFeignRetry.backoff().delay());
			backOffPolicy.setMaxInterval(ixFeignRetry.backoff().maxDelay());
			backOffPolicy.setMultiplier(ixFeignRetry.backoff().multiplier());
			return backOffPolicy;
		}
		else {
			FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
			fixedBackOffPolicy.setBackOffPeriod(ixFeignRetry.backoff().delay());
			return fixedBackOffPolicy;
		}
	}

	private SimpleRetryPolicy prepareSimpleRetryPolicy(IXFeignRetry ixFeignRetry) {
		Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
		policyMap.put(RetryableException.class, true);
		for (Class<? extends Throwable> t : ixFeignRetry.include()) {
			policyMap.put(t, true);
		}
		return new SimpleRetryPolicy(ixFeignRetry.maxAttempt(), policyMap, true);
	}

	/**
	 * 获取当前方法
	 * @param joinPoint 连接点
	 * @return {@link Method}
	 */
	private Method getCurrentMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}

}