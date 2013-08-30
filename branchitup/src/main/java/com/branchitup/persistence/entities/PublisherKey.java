package com.branchitup.persistence.entities;

import java.io.Serializable;

public class PublisherKey implements Serializable{
	private static final long serialVersionUID = 1L;

	public int roleMask;
	public Long userAccountId;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roleMask;
		result = prime * result
				+ ((userAccountId == null) ? 0 : userAccountId.hashCode());
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
		PublisherKey other = (PublisherKey) obj;
		if (roleMask != other.roleMask)
			return false;
		if (userAccountId == null) {
			if (other.userAccountId != null)
				return false;
		} else if (!userAccountId.equals(other.userAccountId))
			return false;
		return true;
	}
	
}
