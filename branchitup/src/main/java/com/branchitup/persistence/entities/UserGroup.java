package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="UserGroup.select",
	    query="SELECT g FROM UserGroup AS g"
	)
})
//@Table(name="usergroups",schema="branchitup")
//@Entity
public class UserGroup extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userGroupId")
	protected Long userGroupId;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="usergroup_authority",
		joinColumns={
			@JoinColumn(name="userGroupId",referencedColumnName="userGroupId")
		},
		inverseJoinColumns={
			@JoinColumn(name="authorityId",referencedColumnName="authorityId")	
		}
	)
	protected List<Authority> authorities;
	
	@ManyToMany(fetch=FetchType.LAZY,mappedBy="userGroups")
	protected List<UserAccount> userAccounts;
	
	@OneToMany(mappedBy="userGroup",fetch=FetchType.LAZY)
	protected List<UserGroup_Authority> userGroup_authority;
}
