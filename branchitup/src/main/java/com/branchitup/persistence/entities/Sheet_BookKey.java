package com.branchitup.persistence.entities;

import java.io.Serializable;


public final class Sheet_BookKey implements Serializable{
	private static final long serialVersionUID = 1L;

	public long sheetId;
	public long bookId;
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bookId ^ (bookId >>> 32));
		result = prime * result + (int) (sheetId ^ (sheetId >>> 32));
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
		Sheet_BookKey other = (Sheet_BookKey) obj;
		if (bookId != other.bookId)
			return false;
		if (sheetId != other.sheetId)
			return false;
		return true;
	}
}
