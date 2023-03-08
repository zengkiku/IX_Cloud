package com.ix.framework.redis.service.aspect;

import com.ix.framework.redis.service.annotation.IXSynchronized;
import com.ix.framework.utils.TUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 注解分布式锁
 */
@Aspect
@Component
public class IXSynchronizedAspect {

	private final static Logger log = LoggerFactory.getLogger(IXSynchronizedAspect.class);

	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 配置织入点
	 */
	@Pointcut("@annotation(com.ix.framework.redis.service.annotation.IXSynchronized)")
	public void synchronizedPointCut() {
	}

	/**
	 * 执行钱锁定
	 * @param point JoinPoint
	 */
	@Before("synchronizedPointCut()")
	public void doBefore(JoinPoint point) {
		handleLock(point);
	}

	/**
	 * 执行后解锁
	 * @param point JoinPoint
	 */
	@After("synchronizedPointCut()")
	public void doAfter(JoinPoint point) {
		handleUnLock(point);
	}

	/**
	 * 分布式锁try上锁Lock
	 * @param joinPoint JoinPoint
	 */
	protected void handleLock(final JoinPoint joinPoint) {
		// 获得注解
		IXSynchronized twSync = getAnnotationLog(joinPoint);
		if (twSync == null) {
			return;
		}

		// 获得锁名称
		String lockValue = twSync.value();
		// 分布式锁
		RLock lock = redissonClient.getLock(lockValue);
		lock.lock();
		log.info("cloud synchronized：Lock");
	}

	/**
	 * 分布式锁解锁Lock
	 * @param joinPoint JoinPoint
	 */
	protected void handleUnLock(final JoinPoint joinPoint) {
		// 获得注解
		IXSynchronized twSync = getAnnotationLog(joinPoint);
		if (twSync == null) {
			return;
		}

		// 获得锁名称
		String lockValue = twSync.value();
		// 解锁
		RLock lock = redissonClient.getLock(lockValue);
		lock.unlock();
		log.info("cloud synchronized：UnLock");
	}

	/**
	 * 是否存在注解，如果存在就获取
	 * @param joinPoint JoinPoint
	 * @return 返回注解信息
	 */
	private IXSynchronized getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (TUtils.isNotEmpty(method)) {
			return method.getAnnotation(IXSynchronized.class);
		}
		return null;
	}

}
