package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(
	    name="ParentBook.selectByPublicationId",
	    query="SELECT new ParentBook(pb.title, pb.version, pb.bookId, u.userAccountId, u.firstName, u.lastName, pb.publisherRoleMask,pb.parentId) FROM PublishedBook AS pb INNER JOIN pb.publisherAccount AS u WHERE pb.bookId = :publicationId"
//	    query="SELECT new com.branchitup.persistence.entities.ParentBook(pb.title) FROM PublishedBook AS pb WHERE pb.bookId = :publicationId"
	)
})
@Entity
public class ParentBook extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="title")
	protected String title;
	
	@Column(name="version")
	protected String version;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@Column(name="firstName")
	protected String firstName;
	
	@Column(name="lastName")
	protected String lastName;
	
	@Column(name="roleMask")
	protected Integer roleMask;
	
	@Column(name="parentId")
	protected Long parentId;
	
	public ParentBook(){}
	
	public ParentBook(String title, String version, Long bookId, Long userAccountId, String firstName, String lastName, Integer roleMask, Long parentId){
		this.title = title;
		this.version = version;
		this.bookId = bookId;
		this.userAccountId = userAccountId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roleMask = roleMask;
		this.parentId = parentId;
	}
	public String getFirstName() {
		return firstName;
	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getRoleMask() {
		return roleMask;
	}

	public void setRoleMask(Integer roleMask) {
		this.roleMask = roleMask;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
