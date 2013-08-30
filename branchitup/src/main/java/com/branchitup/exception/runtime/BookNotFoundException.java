package com.branchitup.exception.runtime;

import javax.persistence.EntityNotFoundException;

public class BookNotFoundException extends EntityNotFoundException{
	private static final long serialVersionUID = 1L;
	public long bookId;
	public BookNotFoundException (long bookId) {
		super("Book with id " + bookId + " does not exist");
		this.bookId = bookId;
	}
	
	public BookNotFoundException () {
	}
	
	public BookNotFoundException (String message,long bookId) {
		super(message);
		this.bookId = bookId;
	}

}
