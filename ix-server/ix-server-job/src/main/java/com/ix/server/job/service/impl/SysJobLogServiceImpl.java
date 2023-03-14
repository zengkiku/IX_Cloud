package com.ix.server.job.service.impl;

import java.util.List;

import com.ix.api.job.domain.SysJobLog;
import com.ix.server.job.mapper.SysJobLogMapper;
import com.ix.server.job.service.ISysJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: 定时任务调度日志信息 服务层
 */
@Service
@RequiredArgsConstructor
public class SysJobLogServiceImpl implements ISysJobLogService {

	private final SysJobLogMapper jobLogMapper;

	/**
	 * 获取quartz调度器日志的计划任务
	 * @param jobLog 调度日志信息
	 * @return 调度任务日志集合
	 */
	@Override
	public List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
		return jobLogMapper.selectJobLogList(jobLog);
	}

	/**
	 * 通过调度任务日志ID查询调度信息
	 * @param jobLogId 调度任务日志ID
	 * @return 调度任务日志对象信息
	 */
	@Override
	public SysJobLog selectJobLogById(Long jobLogId) {
		return jobLogMapper.selectJobLogById(jobLogId);
	}

	/**
	 * 新增任务日志
	 * @param jobLog 调度日志信息
	 */
	@Override
	public void addJobLog(SysJobLog jobLog) {
		jobLogMapper.insertJobLog(jobLog);
	}

	/**
	 * 批量删除调度日志信息
	 * @param logIds 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteJobLogByIds(Long[] logIds) {
		return jobLogMapper.deleteJobLogByIds(logIds);
	}

	/**
	 * 删除任务日志
	 * @param jobId 调度日志ID
	 */
	@Override
	public int deleteJobLogById(Long jobId) {
		return jobLogMapper.deleteJobLogById(jobId);
	}

	/**
	 * 清空任务日志
	 */
	@Override
	public void cleanJobLog() {
		jobLogMapper.cleanJobLog();
	}

}
