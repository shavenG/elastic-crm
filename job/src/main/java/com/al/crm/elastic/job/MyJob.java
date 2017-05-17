package com.al.crm.elastic.job;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MyJob extends JdbcDaoSupport implements SimpleJob {

	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

	@Override
	public void execute(final ShardingContext shardingContext) {
		String procName = shardingContext.getJobParameter();
		getJdbcTemplate().execute("CALL " + procName + "()");
	}
}
