package com.ppx.cloud.repository.category;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.contoller.ReturnMap;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;

@Service
public class KnowledgeCategoryServiceImpl extends MyDaoSupport implements KnowledgeCategoryService {
 
	public List<KnowledgeCategory> list() {
		
		String sql = "select cat_id, concat((select cat_name from repo_knowledge_category where cat_id = c.parent_id), '-', cat_name) cat_name " + 
				"from repo_knowledge_category c where c.parent_id != 0 order by c.parent_id, c.cat_prio";
		
		List<KnowledgeCategory> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(KnowledgeCategory.class));
		
		return list;
	}
	
	

}
