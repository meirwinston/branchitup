/*
 * BranchItUp PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(
	    name="UserGroup_Authority.test",
	    query="DELETE FROM UserGroup_Authority AS sb"
	)
})
//@IdClass(UserGroup_AuthorityKey.class)
//@Table(name="usergroup_authority",schema="branchitup")
//@Entity
public class UserGroup_Authority extends BaseEntity{
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="userGroupId")
	protected Long userGroupId;
	
	@Id
	@Column(name="authorityId")
	protected Long authorityId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userGroupId",referencedColumnName="userGroupId",insertable=false,updatable=false)
	protected UserGroup userGroup;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="authorityId",referencedColumnName="authorityId",updatable=false, insertable=false)
	protected Authority authority;
	
	public UserGroup_Authority(){}
	
	public UserGroup_Authority(Long userGroupId,Long authorityId){
		this.userGroupId = userGroupId;
		this.authorityId = authorityId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
}
