package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BookShowcase extends BaseEntity{
	static final long serialVersionUID = 1l;
	
	@Id
	@Column(name="bookId")
	public Long bookId;
	
	@Column(name="title")
	public String title;
	
	@Column(name="bookSummary")
	public String bookSummary;
	
	@Column(name="coverImageFileId")
	public Long coverImageFileId;
}
