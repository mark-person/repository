/**
 * 
 */
package com.ppx.cloud.repository.user;

import java.util.Date;

/**
 * @author mark
 * @date 2019年4月9日
 */
public class RepoUser {

	private Integer repoUserId;
	private String repoUserName;
	private Integer repoUserStatus;
	private Integer favoriteN;
	private Date created;

	public Integer getRepoUserId() {
		return repoUserId;
	}

	public void setRepoUserId(Integer repoUserId) {
		this.repoUserId = repoUserId;
	}

	public String getRepoUserName() {
		return repoUserName;
	}

	public void setRepoUserName(String repoUserName) {
		this.repoUserName = repoUserName;
	}

	public Integer getRepoUserStatus() {
		return repoUserStatus;
	}

	public void setRepoUserStatus(Integer repoUserStatus) {
		this.repoUserStatus = repoUserStatus;
	}

	public Integer getFavoriteN() {
		return favoriteN;
	}

	public void setFavoriteN(Integer favoriteN) {
		this.favoriteN = favoriteN;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
