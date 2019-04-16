/**
 * 
 */
package com.ppx.cloud.repository.subject;

import java.util.List;

/**
 * @author mark
 * @date 2019年3月14日
 */
public interface KnowledgeSubjectService {
	
	List<KnowledgeSubject> list();
	
	public List<Integer> listSubjectId(int kId);
	
	void insertSubjectMap(int subjectId, int kId);
	
	void deleteSubjectMap(int subjectId, int kId);
}
