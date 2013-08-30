/*
 * BranchItUp PROPRIETARY/CONFIDENTIAL.
 * 
 * BranchItUp Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//		name="Book.copyPublishedBookToBook",
//		query="INSERT INTO books(title,createdByIP, ownerAccountId, bookSummary, createdOn, derivedFromId) SELECT pb.title, createdByIP,ownerAccountId, pb.bookSummary, NOW() AS createdOn, pbookId FROM publishedbooks AS pb WHERE pb.bookId=:bookId"
//	)
//})
//
@NamedQueries({
	@NamedQuery(
	    name="Book.parentId",
	    query="SELECT b.derivedFromId FROM Book AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
	    name="Book.selectCreatedOn",
	    query="SELECT b.createdOn FROM Book AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
	    name="Book.selectByOwner",
	    query="SELECT o FROM Book AS o WHERE o.ownerAccountId = :ownerAccountId ORDER BY o.modifiedOn"
	),
	@NamedQuery(
	    name="Book.selectByIds",
	    query="SELECT o FROM Book AS o WHERE o.bookId IN :bookId ORDER BY o.modifiedOn"
	),
	@NamedQuery(
	    name="Book.selectTitleById",
	    query="SELECT b.title FROM Book AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
	    name="Book.deleteByIds",
	    query="DELETE FROM Book AS o WHERE o.bookId IN :bookIds"
	),
	@NamedQuery(
	    name="Book.selectAll",
	    query="SELECT b FROM Book AS b ORDER BY b.modifiedOn DESC"
	),
	@NamedQuery(
	    name="Book.selectByPublishedBookId",
	    query="SELECT b FROM Book AS b WHERE b.derivedFromId = :publishedBookId"
	),
	@NamedQuery(
	    name="Book.countAll",
	    query="SELECT COUNT(b.bookId) FROM Book AS b"
	),
	@NamedQuery(
	    name="Book.exists", //exists?
	    query="SELECT COUNT(o.bookId) FROM Book AS o WHERE o.bookId = :bookId"
	),
	@NamedQuery(
		name="Book.countByOwnerAccountId",
		query="SELECT COUNT(o.bookId) FROM Book AS o WHERE o.ownerAccountId = :ownerAccountId"
	),
	@NamedQuery(
	    name="Book.delete",
	    query="DELETE FROM Book AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
	    name="Book.selectOwnerAccountIdByBookId",
	    query="SELECT b.ownerAccountId FROM Book AS b WHERE b.bookId = :bookId"
	)
})
@Table(name="books",schema="branchitup")
@Entity
public class Book extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	//GenerationType.SEQUENCE will not populate the id field after persist, annoying
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="derivedFromId")
	protected Long derivedFromId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="derivedFromId", referencedColumnName="bookId",insertable=false,updatable=false)
	protected PublishedBook derivedFrom;
	
	@Column(name="title", length=50)
	protected String title;
	
	@Column(name="bookSummary",length=255)
	protected String bookSummary;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
	@Column(name="modifiedOn",nullable=false)
	protected Date modifiedOn;
	
//	@Column(name="coverImageFileId")
//	protected Long coverImageFileId;
	
//	@Column(name="publishCount")
//	protected Integer publishCount;
	
//	@JoinColumn(name = "coverImageFileId",referencedColumnName="imageFileId",insertable=false,updatable=false)
//	@OneToOne(fetch=FetchType.LAZY)
//	protected ImageFile coverImageFile; 
	
	/**
	 * the current owner of this version of the book
	 * the maximum length of an email address is 320 characters.
	 */
//	@Column(name="owner",nullable=false)
//	protected String owner;
	
	@Column(name="ownerAccountId",nullable=false)
	protected Long ownerAccountId;
	
//	@Enumerated(EnumType.STRING)
//	@Column(name="status")
//	protected UserItemStatus status;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="sheet_book",
		joinColumns={
			@JoinColumn(name="bookId",referencedColumnName="bookId")
		},
		inverseJoinColumns={
			@JoinColumn(name="sheetId",referencedColumnName="sheetId")	
		}
	)
	protected List<Sheet> sheetList;
	
	@OneToMany(mappedBy="book",fetch=FetchType.LAZY)
	protected List<Sheet_Book> sheet_book;
	
//	@Column(name="genreId")
//	protected Long genreId;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="genreId",referencedColumnName="genreId",insertable=false,updatable=false)
//	protected Genre genre;
	
	@Column(name="createdByIP")
	protected String createdByIP;
	
	@Column(name="modifiedByIP")
	protected String modifiedByIP;
	
	public Book(){
		
	}

	public void setSheet_Book(List<Sheet_Book> sheet_book) {
		this.sheet_book = sheet_book;
	}
	public String getCreatedByIP() {
		return createdByIP;
	}

	public void setCreatedByIP(String createdByIP) {
		this.createdByIP = createdByIP;
	}

	public String getModifiedByIP() {
		return modifiedByIP;
	}



	public void setModifiedByIP(String modifiedByIP) {
		this.modifiedByIP = modifiedByIP;
	}



	public void setDerivedFrom(PublishedBook derivedFrom) {
		this.derivedFrom = derivedFrom;
	}

//	public void setCoverImageFile(ImageFile coverImageFile) {
//		this.coverImageFile = coverImageFile;
//	}

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

	public String getBookSummary() {
		return bookSummary;
	}

	public void setBookSummary(String bookSummary) {
		this.bookSummary = bookSummary;
	}

	
	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	public List<Sheet> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<Sheet> sheetList) {
		this.sheetList = sheetList;
	}
	
	public Long getDerivedFromId() {
		return derivedFromId;
	}

	public void setDerivedFromId(Long derivedFromId) {
		this.derivedFromId = derivedFromId;
	}

	public PublishedBook getDerivedFrom() {
		return derivedFrom;
	}
//
//	public void setParentBook(PublishedBook parentBook) {
//		this.parentBook = parentBook;
//	}
//
	public List<Sheet_Book> getSheet_Book() {
		return sheet_book;
	}
//
//	public void setSheet_Book(List<Sheet_Book> sheet_book) {
//		this.sheet_book = sheet_book;
//	}
//	
//	public Long getGenreId() {
//		return genreId;
//	}
//
//	public void setGenreId(Long genreId) {
//		this.genreId = genreId;
//	}
//
//	public Genre getGenre() {
//		return genre;
//	}
//	
//	public void setGenre(Genre genre) {
//		this.genre = genre;
//	}
//
//	public Long getCoverImageFileId() {
//		return coverImageFileId;
//	}
//
//	public void setCoverImageFileId(Long coverImageFileId) {
//		this.coverImageFileId = coverImageFileId;
//	}

//	public ImageFile getCoverImageFile() {
//		return coverImageFile;
//	}

//	public void setCoverImageFile(ImageFile coverImageFile) {
//		this.coverImageFile = coverImageFile;
//	}
	
	

//	@Override
//	public Book branch(String newOwner) {
//		Book b = new Book();
//		b.bookId = null;
//		b.bookSummary = (this.bookSummary != null ? new String(this.bookSummary) : null);
//		b.createdOn = new Date(System.currentTimeMillis());
//		b.modifiedOn = new Date(System.currentTimeMillis());
//		b.owner = newOwner;
//		b.parentId = this.bookId;
//		b.title = (this.title != null ? new String(title) : null);
//		b.genreId = (this.genreId != null ? new Long(this.genreId) : null);
////		b.setRatingAverage(null); //transient
////		b.setRatingCount(0l); //transient
//		b.branchCount = this.branchCount+1;
//		
////		if(this.diskResource != null){
////			List<DiskResource> list = new ArrayList<DiskResource>(this.diskResource.size());
////			for(DiskResource dr : this.diskResource){
////				list.add(dr.branch(newOwner));
////			}
////			b.diskResource  = list;
////		}
//		
//		
////		if(this.sheetList != null){
////			List<Sheet> sheetList = new ArrayList<Sheet>(this.sheetList.size());
////			for(Sheet sheet : this.sheetList){
////				sheetList.add(sheet.branch(newOwner));
////			}
////			b.sheetList = sheetList;
////		}
//		return b;
//	}
	
//	public Integer getBranchCount() {
//		return branchCount;
//	}
//
//	public void setBranchCount(Integer branchCount) {
//		this.branchCount = branchCount;
//	}
//
//	public String getVersion() {
//		return version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

//	public Integer getPublishCount() {
//		return publishCount;
//	}
//
//	public void setPublishCount(Integer publishCount) {
//		this.publishCount = publishCount;
//	}
	

	@Override
	public Object clone() {
		Book b = (Book)super.clone();
		
		if(this.sheet_book != null){
			List<Sheet_Book> sbList = new ArrayList<Sheet_Book>(this.sheet_book.size());
			for(Sheet_Book sb : this.sheet_book){
				sbList.add((Sheet_Book)sb.clone());
			}
			b.sheet_book = sbList;
		}
		
		if(this.sheetList != null){
			List<Sheet> sheetList = new ArrayList<Sheet>(this.sheetList.size());
			for(Sheet s : this.sheetList){
				sheetList.add((Sheet)s.clone());
			}
			b.sheetList = sheetList;
		}
//		if(this.coverImageFile != null){
//			b.coverImageFile = (ImageFile)this.coverImageFile.clone();
//		}
		return b;
	}
	
	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + ", bookSummary="
				+ bookSummary + ", created=" + createdOn + ", modified="
				+ modifiedOn + ", owner="
				+ ownerAccountId + ", sheetList=" + sheetList + "]";
	}
}
