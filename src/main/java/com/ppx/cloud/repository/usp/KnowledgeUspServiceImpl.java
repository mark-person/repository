package com.ppx.cloud.repository.usp;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;

@Service
public class KnowledgeUspServiceImpl extends MyDaoSupport implements KnowledgeUspService {
 
	public List<KnowledgeUsp> list() {
		
		String sql = "select usp_id, usp_name `key`, usp_name value from repo_knowledge_usp where usp_status = 1 order by usp_prio";
		
		List<KnowledgeUsp> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(KnowledgeUsp.class));
		
		return list;
	}
	
	

}
