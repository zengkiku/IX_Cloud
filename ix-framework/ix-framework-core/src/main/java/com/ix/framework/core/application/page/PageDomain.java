package com.ix.framework.core.application.page;

import com.ix.framework.utils.StringUtils;
import com.ix.framework.utils.TUtils;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 分页数据
 */
public class PageDomain {

	/**
	 * 当前记录起始索引
	 */
	private Integer current;

	/**
	 * 每页显示记录数
	 */
	private Integer pageSize;

	/**
	 * 排序列
	 */
	private String orderByColumn;

	/**
	 * 排序的方向 "desc" 或者 "asc".
	 */

	private String isAsc;

	public String getOrderBy() {
		if (TUtils.isEmpty(orderByColumn)) {
			return null;
		}
		return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer page) {
		this.current = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderByColumn() {
		return orderByColumn;
	}

	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	public String getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(String isAsc) {
		this.isAsc = isAsc;
	}

}
