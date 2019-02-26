/**
 * 
 */
package com.ppx.cloud.demo.product;

import java.sql.Date;

import com.ppx.cloud.common.jdbc.annotation.Column;

/**
 * @author mark
 * @date 2018年11月9日
 */
public class Product {

	private Integer prodId;
	private String prodTitle;
	private Float prodPrice;
	private Integer catId;
	private Integer recommend;
	private String mainImgSrc;
	private String userAgent;
	private String prodUsp;
	private Integer creator;
	private Date created;
	@Column(readonly = true)
	private String imgSrc;

	private String catName;

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProdTitle() {
		return prodTitle;
	}

	public void setProdTitle(String prodTitle) {
		this.prodTitle = prodTitle;
	}

	public Float getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(Float prodPrice) {
		this.prodPrice = prodPrice;
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

	public String getMainImgSrc() {
		return mainImgSrc;
	}

	public void setMainImgSrc(String mainImgSrc) {
		this.mainImgSrc = mainImgSrc;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getProdUsp() {
		return prodUsp;
	}

	public void setProdUsp(String prodUsp) {
		this.prodUsp = prodUsp;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

}
