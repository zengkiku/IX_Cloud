package com.ix.framework.utils.constants;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 一些常用的单例对象
 */
public class SingleConstants {

	/**
	 * RANDOM
	 */
	public final static Random RANDOM = new Random();

	/**
	 * SECURE_RANDOM
	 */
	public final static SecureRandom SECURE_RANDOM = new SecureRandom();

}
