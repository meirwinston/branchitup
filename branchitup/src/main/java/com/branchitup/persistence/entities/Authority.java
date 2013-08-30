package com.branchitup.persistence.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="Authority.select",
	    query="SELECT a FROM Authority AS a"
	)
})
//@Table(name="authorities",schema="branchitup")
//@Entity
public class Authority extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="authorityId")
	protected Long authorityId;
	
	@Column(name="name")
	protected String name;
	
	@ManyToMany(mappedBy="authorities",fetch=FetchType.LAZY)
	protected List<UserGroup> userGroups; 
	
	@OneToMany(mappedBy="authority")
	protected List<UserGroup_Authority> userGroup_authority;
}
