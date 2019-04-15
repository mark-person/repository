package com.ppx.cloud.repository.subject;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;

@Service
public class KnowledgeSubjectServiceImpl extends MyDaoSupport implements KnowledgeSubjectService {
 
	public List<KnowledgeSubject> list() {
		
		String sql = "select subject_id, subject_name from repo_knowledge_subject where subject_status = 1 order by subject_prio";
		
		List<KnowledgeSubject> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(KnowledgeSubject.class));
		
		return list;
	}
	
	

}
