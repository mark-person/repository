/**
 * 
 */
package com.ppx.cloud.repository.usp;

import com.ppx.cloud.common.jdbc.annotation.Table;

/**
 * 
 * @author mark
 * @date 2019年4月2日
 */
@Table("repo_category")
public class KnowledgeUsp {
	private Integer uspId;
	private String uspName;
	private Integer uspPrio;
	private Integer uspStatus;
	
	
	private String key;
	private String value;
	
	public Integer getUspId() {
		return uspId;
	}
	public void setUspId(Integer uspId) {
		this.uspId = uspId;
	}
	public String getUspName() {
		return uspName;
	}
	public void setUspName(String uspName) {
		this.uspName = uspName;
	}
	public Integer getUspPrio() {
		return uspPrio;
	}
	public void setUspPrio(Integer uspPrio) {
		this.uspPrio = uspPrio;
	}
	public Integer getUspStatus() {
		return uspStatus;
	}

	public void setUspStatus(Integer uspStatus) {
		this.uspStatus = uspStatus;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	


}
