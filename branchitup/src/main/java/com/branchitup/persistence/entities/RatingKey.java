package com.branchitup.persistence.entities;

import java.io.Serializable;

public class RatingKey implements Serializable{
	private static final long serialVersionUID = 1L;

	public Long userAccountId;
	public Long bookId;
	
	public RatingKey(){}
	
	public RatingKey(Long userAccountId,Long bookId){
		this.userAccountId = userAccountId;
		this.bookId = bookId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
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
		RatingKey other = (RatingKey) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (userAccountId == null) {
			if (other.userAccountId != null)
				return false;
		} else if (!userAccountId.equals(other.userAccountId))
			return false;
		return true;
	}
	
}
