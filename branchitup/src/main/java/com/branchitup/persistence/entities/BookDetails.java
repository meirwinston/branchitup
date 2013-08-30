package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

//(SELECT publishedbooks.coverImageFileId FROM publishedbooks WHERE publishedbooks.bookId = b.derivedFromId) parentCoverImageFileId
@NamedQueries({
	@NamedQuery(
		name="BookDetails.selectByBookId",
		query="SELECT new BookDetails(b.bookId, b.title, b.bookSummary, b.createdOn, b.modifiedOn, parent.coverImageFileId) FROM Book AS b LEFT JOIN b.derivedFrom as parent WHERE b.bookId = :bookId"
	)
})
@Entity
public class BookDetails {
	@Id
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="title", length=50)
	protected String title;
	
	@Column(name="bookSummary",length=255)
	protected String bookSummary;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
	@Column(name="modifiedOn",nullable=false)
	protected Date modifiedOn;
	
	@Column(name="parentCoverImageFileId")
	protected Long parentCoverImageFileId;
	
	@Transient
	protected String parentCoverImageUrl;
	
//	@Column(name="publishCount")
//	protected Integer publishCount;
	
//	@Column(name="ownerAccountId",nullable=false)
//	protected Long ownerAccountId;
	
//	@ManyToMany(fetch=FetchType.LAZY)
//	@JoinTable(
//		name="sheet_book",
//		joinColumns={
//			@JoinColumn(name="bookId",referencedColumnName="bookId")
//		},
//		inverseJoinColumns={
//			@JoinColumn(name="sheetId",referencedColumnName="sheetId")	
//		}
//	)
//	protected List<BookDetailsSheet> sheetList;
	
//	@OneToMany(mappedBy="book",fetch=FetchType.LAZY)
//	protected List<Sheet_Book> sheet_book;
	
	@Transient
	protected List<BookDetailsSheet> sheetList;
	
	public BookDetails(){}
	
	public BookDetails(Long bookId, String title, String bookSummary, Date createdOn, Date modifiedOn, Long parentCoverImageFileId){
		this.bookId = bookId;
		this.title = title;
		this.bookSummary = bookSummary;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.parentCoverImageFileId = parentCoverImageFileId;
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

	public String getBookSummary() {
		return bookSummary;
	}

	public void setBookSummary(String bookSummary) {
		this.bookSummary = bookSummary;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

//	public Long getCoverImageFileId() {
//		return coverImageFileId;
//	}
//
//	public void setCoverImageFileId(Long coverImageFileId) {
//		this.coverImageFileId = coverImageFileId;
//	}

	public List<BookDetailsSheet> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<BookDetailsSheet> sheetList) {
		this.sheetList = sheetList;
	}

	public Long getParentCoverImageFileId() {
		return parentCoverImageFileId;
	}

	public void setParentCoverImageFileId(Long parentCoverImageFileId) {
		this.parentCoverImageFileId = parentCoverImageFileId;
	}

	public String getParentCoverImageUrl() {
		return parentCoverImageUrl;
	}

	public void setParentCoverImageUrl(String parentCoverImageUrl) {
		this.parentCoverImageUrl = parentCoverImageUrl;
	}
	
	

//	public String getCoverImageUrl() {
//		return coverImageUrl;
//	}
//
//	public void setCoverImageUrl(String coverImageUrl) {
//		this.coverImageUrl = coverImageUrl;
//	}
}
