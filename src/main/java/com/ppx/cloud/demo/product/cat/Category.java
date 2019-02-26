package com.ppx.cloud.demo.product.cat;

public class Category {
	private Integer catId;
	private String catName;
	private Integer catPrio;
	private Integer catStatus;
	private String catDesc;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
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

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

}
