package com.branchitup.persistence.entities;

import java.io.Serializable;

public class UserGroup_AuthorityKey implements Serializable{
	static final long serialVersionUID = 1l;
	
	public long userGroupId;
	public long authorityId;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (authorityId ^ (authorityId >>> 32));
		result = prime * result + (int) (userGroupId ^ (userGroupId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroup_AuthorityKey other = (UserGroup_AuthorityKey) obj;
		if (authorityId != other.authorityId)
			return false;
		if (userGroupId != other.userGroupId)
			return false;
		return true;
	}
}
