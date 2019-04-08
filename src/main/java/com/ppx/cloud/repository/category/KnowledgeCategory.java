/**
 * 
 */
package com.ppx.cloud.repository.category;

import com.ppx.cloud.common.jdbc.annotation.Table;

/**
 * @author mark
 * @date 2019年3月14日
 */
@Table("repo_category")
public class KnowledgeCategory {
	private Integer catId;
	private Integer parentId;
	private String catName;
	private Integer catPrio;
	private Integer catStatus;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Integer getCatPrio() {
		return catPrio;
	}

	public void setCatPrio(Integer catPrio) {
		this.catPrio = catPrio;
	}

	public Integer getCatStatus() {
		return catStatus;
	}

	public void setCatStatus(Integer catStatus) {
		this.catStatus = catStatus;
	}

	

}
