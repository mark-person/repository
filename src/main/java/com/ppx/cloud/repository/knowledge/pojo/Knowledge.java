/**
 * 
 */
package com.ppx.cloud.repository.knowledge.pojo;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Column;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;

/**
 * @author mark
 * @date 2019年3月13日
 */
@Table("repo_knowledge")
public class Knowledge {

	@Id
	private Integer kId;
	private String kTitle;
	private Integer catId;
	private Integer recommend;
	private Integer recommendPrio;
	private Date modified;
	private Integer modifiedBy;
	private String mainImgSrc;
	@Column(readonly = true)
	private String kContent;
	@Column(readonly = true)
	private String imgSrc;
	@Column(readonly = true)
	private String uspIds;
	@Column(readonly = true)
	private String subjectId;
	
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	private String catName;

	public String getUspIds() {
		return uspIds;
	}

	public void setUspIds(String uspIds) {
		this.uspIds = uspIds;
	}

	public String getMainImgSrc() {
		return mainImgSrc;
	}

	public void setMainImgSrc(String mainImgSrc) {
		this.mainImgSrc = mainImgSrc;
	}

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

	public Integer getRecommendPrio() {
		return recommendPrio;
	}

	public void setRecommendPrio(Integer recommendPrio) {
		this.recommendPrio = recommendPrio;
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

	public String getkContent() {
		return kContent;
	}

	public void setkContent(String kContent) {
		this.kContent = kContent;
	}

}
