package com.ix.framework.core.constants;

public class Constants {

	public static final String AUTHORIZATION = "Authorization";

	/**
	 * UTF-8 字符集
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * GBK 字符集
	 */
	public static final String GBK = "GBK";

	/**
	 * RMI 远程方法调用
	 */
	public static final String LOOKUP_RMI = "rmi:";

	/**
	 * LDAP 远程方法调用
	 */
	public static final String LOOKUP_LDAP = "ldap:";

	/**
	 * LDAPS 远程方法调用
	 */
	public static final String LOOKUP_LDAPS = "ldaps:";

	/**
	 * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
	 */
	public static final String[] JOB_WHITELIST_STR = { "com.ix" };

	/**
	 * 定时任务违规的字符
	 */
	public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
			"org.springframework", "org.apache", "com.ix.framework.utils.file" };

	/**
	 * http请求
	 */
	public static final String HTTP = "http://";

	/**
	 * https请求
	 */
	public static final String HTTPS = "https://";

	/**
	 * 成功标记
	 */
	public static final Integer SUCCESS = 0;

	/**
	 * 失败标记
	 */
	public static final Integer FAIL = 1;

	/**
	 * 登录成功
	 */
	public static final String LOGIN_SUCCESS = "Success";

	/**
	 * 注销
	 */
	public static final String LOGOUT = "Logout";

	/**
	 * 注册
	 */
	public static final String REGISTER = "Register";

	/**
	 * 登录失败
	 */
	public static final String LOGIN_FAIL = "Error";

	/**
	 * 当前记录起始索引
	 */
	public static final String PAGE_NUM = "pageNum";

	/**
	 * 每页显示记录数
	 */
	public static final String PAGE_SIZE = "pageSize";

	/**
	 * 排序列
	 */
	public static final String ORDER_BY_COLUMN = "orderByColumn";

	/**
	 * 排序的方向 "desc" 或者 "asc".
	 */
	public static final String IS_ASC = "isAsc";

	/**
	 * 验证码 redis key
	 */
	public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

	/**
	 * 验证码有效期（分钟）
	 */
	public static final Integer CAPTCHA_EXPIRATION = 2;

	/**
	 * 参数管理 cache key
	 */
	public static final String SYS_CONFIG_KEY = "sys_config:";

	/**
	 * 资源映射路径 前缀
	 */
	public static final String RESOURCE_PREFIX = "/profile";

}
