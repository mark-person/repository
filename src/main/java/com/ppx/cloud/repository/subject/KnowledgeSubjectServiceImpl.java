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
	
	public List<Integer> listSubjectId(int kId) {
		String sql = "select subject_id from repo_knowledge_map_subject where k_id = ?";
		return getJdbcTemplate().queryForList(sql, Integer.class, kId);
	}
	
	public void insertSubjectMap(int subjectId, int kId) {
		String insertSql = "insert ignore into repo_knowledge_map_subject(subject_id, k_id) values(?, ?)";
		getJdbcTemplate().update(insertSql, subjectId, kId);
	}
	
	public void deleteSubjectMap(int subjectId, int kId) {
		String insertSql = "delete from  repo_knowledge_map_subject where subject_id = ? and k_id = ?";
		getJdbcTemplate().update(insertSql, subjectId, kId);
	}
	
	

}
