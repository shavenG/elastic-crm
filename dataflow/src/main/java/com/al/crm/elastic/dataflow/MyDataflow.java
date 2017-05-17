package com.al.crm.elastic.dataflow;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

@SuppressWarnings("unchecked")
public class MyDataflow extends JdbcDaoSupport implements DataflowJob {

	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

	@Override
    public List<Map<String,Object>> fetchData(ShardingContext context) {
        return getJdbcTemplate().queryForList("SELECT A.*, ROWIDTOCHAR(ROWID) ID "
												 + " FROM JOB_TEST_TAB A"
												 + " WHERE A.STATUS_CD = 'D'"
												 + " AND MOD(TO_NUMBER(TO_CHAR(A.SO_DATE, 'mi')), " 
												 + context.getShardingTotalCount() 
												 + ") = " 
												 + context.getShardingItem());
    }
    
	@Override
    public void processData(ShardingContext shardingContext, List list) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE JOB_TEST_TAB");
		sb.append(" SET STATUS_CD = 'C', REMARK = 'DATAFLOW;' || REMARK");
		sb.append(" WHERE ROWID IN (");
        for(int i = 0;i < list.size();i++){
        	Map<String,Object> param = (Map<String,Object>)list.get(i);
        	sb.append("'" + param.get("ID").toString() + "'");  
        	if(i < list.size() - 1){
        		sb.append(",");
        	}
        }
		sb.append(")");
		System.out.println(sb.toString());
		getJdbcTemplate().update(sb.toString());     
    }
}
