/**
 * 
 */
package com.ppx.cloud.repository.knowledge;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.common.jdbc.annotation.Table;



/**
 * @author mark
 * @date 2019年3月13日
 */
@Table("repo_knowledge")
public class Knowledge {
	
	private Integer kId;
	private String kTitle;
	private Integer catId;
	private Integer recommend;
	private Date created;
	private Integer createdBy;
	private Date modified;
	private Integer modifiedBy;
	
	@Column(readonly = true)
	private String imgSrc;
	
	private String catName;
	

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public Integer getkId() {
		return kId;
	}

	public void setkId(Integer kId) {
		this.kId = kId;
	}

	public String getkTitle() {
		return kTitle;
	}

	public void setkTitle(String kTitle) {
		this.kTitle = kTitle;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
