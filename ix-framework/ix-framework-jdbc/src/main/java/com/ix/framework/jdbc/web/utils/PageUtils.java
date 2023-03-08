package com.ix.framework.jdbc.web.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ix.framework.core.application.page.PageDomain;
import com.ix.framework.core.application.page.TableDataInfo;
import com.ix.framework.core.application.page.TableSupport;
import com.ix.framework.utils.TUtils;
import com.ix.framework.utils.sql.SqlUtils;

import java.util.List;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 分页基础控制器
 */
public class PageUtils {

	/**
	 * 注入分页信息
	 */
	public static void startPage() {
		// 清除分页bug
		PageHelper.clearPage();
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer page = pageDomain.getCurrent();
		Integer pageSize = pageDomain.getPageSize();
		if (TUtils.isNotEmpty(page) && TUtils.isNotEmpty(pageSize)) {
			String orderBy = SqlUtils.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.startPage(page, pageSize, orderBy);
		}
	}

	/**
	 * 响应请求分页数据
	 * @param list 数据列表
	 * @return 适应Json
	 */
	public static TableDataInfo getDataTable(List<?> list) {
		TableDataInfo rspData = new TableDataInfo();
		rspData.setRecords(list);
		PageInfo<?> pageInfo = new PageInfo<>(list);
		rspData.setTotal(pageInfo.getTotal());
		return rspData;
	}

}
