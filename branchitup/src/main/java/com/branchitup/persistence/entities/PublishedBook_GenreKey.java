package com.branchitup.persistence.entities;

import java.io.Serializable;

public class PublishedBook_GenreKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long bookId;
	private long genreId;
	
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public long getGenreId() {
		return genreId;
	}
	public void setGenreId(long genreId) {
		this.genreId = genreId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bookId ^ (bookId >>> 32));
		result = prime * result + (int) (genreId ^ (genreId >>> 32));
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
		PublishedBook_GenreKey other = (PublishedBook_GenreKey) obj;
		if (bookId != other.bookId)
			return false;
		if (genreId != other.genreId)
			return false;
		return true;
	}
	
	
}
