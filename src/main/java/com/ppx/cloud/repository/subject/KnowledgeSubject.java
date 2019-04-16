/**
 * 
 */
package com.ppx.cloud.repository.subject;

import com.ppx.cloud.common.jdbc.annotation.Table;

@Table("repo_subject")
public class KnowledgeSubject {
	private Integer subjectId;
	private String subjectName;
	private Integer subjectPrio;
	private Integer subjectStatus;
	
	private Boolean checked = false;
	
	

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getSubjectPrio() {
		return subjectPrio;
	}

	public void setSubjectPrio(Integer subjectPrio) {
		this.subjectPrio = subjectPrio;
	}

	public Integer getSubjectStatus() {
		return subjectStatus;
	}

	public void setSubjectStatus(Integer subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

}
