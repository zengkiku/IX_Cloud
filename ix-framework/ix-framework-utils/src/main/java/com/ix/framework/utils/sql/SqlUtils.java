package com.ix.framework.utils.sql;

import com.ix.framework.utils.TUtils;
import com.ix.framework.utils.exception.IXUtilsException;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: sql操作工具类
 */
public class SqlUtils {

	public SqlUtils() {
		throw new IXUtilsException("This is a utility class and cannot be instantiated");
	}

	/**
	 * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
	 */
	public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

	/**
	 * 检查字符，防止注入绕过
	 * @param value 需要检测的字符
	 * @return 返回处理数据
	 */
	public static String escapeOrderBySql(String value) {
		if (TUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
			throw new IXUtilsException("参数不符合规范，不能进行查询");
		}
		return value;
	}

	/**
	 * 验证 order by 语法是否符合规范
	 * @param value 验证字段
	 * @return 返回结果
	 */
	public static boolean isValidOrderBySql(String value) {
		return value.matches(SQL_PATTERN);
	}

}
