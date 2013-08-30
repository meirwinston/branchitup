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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.branchitup.persistence.UserItemStatus;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//		name="Sheet.copyPublishedSheetToSheet",
//		query="INSERT INTO sheets(`name`, content, ownerAccountId, createdOn, cssText, derivedFromId, permissionsMask, createdByIP,folderName) SELECT ps.name, ps.content, :ownerAccountId, NOW() AS createdOn, ps.cssText, ps.sheetId,7, :createdByIP,:folderName FROM publishedsheets AS ps WHERE ps.sheetId = :sheetId"
//	)
//})

/*
 * 
 * CREATE DEFINER=`root`@`localhost` PROCEDURE `sheets.selectFolders`(userAccountId BIGINT)
BEGIN
		SELECT DISTINCT folderName
		FROM sheets AS s 
		WHERE s.ownerAccountId = userAccountId
		LIMIT 0,1000;
    END$$

DELIMITER ;
 */
@NamedQueries({
	@NamedQuery(
	    name="Sheet.deleteByCreatedOnAndId",
	    query="DELETE FROM Sheet AS s WHERE s.sheetId = :sheetId AND s.createdOn = :createdOn"
	),
	@NamedQuery(
	    name="Sheet.countActiveByOwnerAccountId", //exists?
	    query="SELECT COUNT(s.sheetId) FROM Sheet AS s WHERE s.ownerAccountId = :ownerAccountId AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE"
	),
	@NamedQuery(
	    name="Sheet.selectFolders",
	    query="SELECT DISTINCT folderName FROM Sheet AS s WHERE s.ownerAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="Sheet.incrementViewsCount",
	    query="UPDATE Sheet AS s SET s.viewsCount=s.viewsCount+1 WHERE s.sheetId = :sheetId"
	),
	@NamedQuery(
	    name="Sheet.publishAsArticleToBlog",
	    query="UPDATE Sheet AS s SET s.publishedOn=NOW(), s.blogId = :blogId " +
	    		"WHERE s.sheetId = :sheetId"
	),
	@NamedQuery(
	    name="Sheet.ownerAccountId",
	    query="SELECT s.ownerAccountId FROM Sheet AS s WHERE s.sheetId = :sheetId"
	)
})

@Table(name="sheets",schema="branchitup")
@Entity
public class Sheet extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@Column(name="sheetId")
	protected Long sheetId;
	
	@Column(name="name", length=100)
	protected String name;
	
	@Column(name="cssText")
	protected String cssText;
	
	/**
	 * TINYTEXT 	256 bytes
	 * TEXT 	65,535 bytes 	~64kb
	 * MEDIUMTEXT 	 16,777,215 bytes 	~16MB
	 * LONGTEXT 	4,294,967,295 bytes 	~4GB
	 */
	@Basic(fetch=FetchType.LAZY)
	@Lob
	@Column(name="content")
	protected String content;
	
	@Column(name="ownerAccountId")
	protected Long ownerAccountId;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
	@Column(name="modifiedOn",nullable=false)
	protected Date modifiedOn;
	
//	@Column(name="bookId")
//	protected Long bookId;
	
//	@JoinColumn(name = "bookId",referencedColumnName="bookId" ,nullable = true,insertable=false,updatable=false)
//	@ManyToOne(fetch=FetchType.LAZY)
//	protected Book book;
	
	@Column(name="permissionsMask")
	protected Integer permissionsMask;
	
//	@Column(name="genreId")
//	protected Long genreId;
	
	//on branching keep a link to the parent sheet
	@Column(name="derivedFromId")
	protected Long derivedFromId;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="genreId",referencedColumnName="genreId",insertable=false,updatable=false)
//	protected Genre genre;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ownerAccountId",referencedColumnName="userAccountId",insertable=false,updatable=false)
	protected UserAccount ownerAccount;
	
	@ManyToMany(mappedBy="sheetList",fetch=FetchType.LAZY)
	protected List<Book> books; 
	
	@OneToMany(mappedBy="sheet")
	protected List<Sheet_Book> sheet_book;
	
	@Column(name="createdByIP")
	protected String createdByIP;
	
	@Column(name="modifiedByIP")
	protected String modifiedByIP;
	
//	@Enumerated(EnumType.STRING)
//	@Column(name="language")
//	protected Language language;
	
	@Column(name="folderName")
	protected String folderName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	protected UserItemStatus status;
	
	protected Long blogId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blogId",referencedColumnName="blogId",updatable=false, insertable=false)
	protected Blog blog;
	
	@Column(name="publishedOn")
	protected Date publishedOn;
	
	protected Integer viewsCount;
	
	public Sheet(){}
	
	public Sheet(Sheet sheet){
		this.content = sheet.getContent();
		this.ownerAccountId = sheet.getOwnerAccountId();
		this.name = sheet.getName();
		this.folderName = sheet.getFolderName();
	}
	
	
	
//	public Sheet(PublishedSheet psheet){
////		this.bookList
//		this.content = psheet.getContent();
//		this.createdOn = new Timestamp(System.currentTimeMillis());
//		this.cssText = psheet.getCssText();
////		this.genre = psheet.get
//	}

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public List<Sheet_Book> getSheet_book() {
		return sheet_book;
	}

	public void setSheet_book(List<Sheet_Book> sheet_book) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		if(this.content != null){
			if(this.name == null || this.name.equals("")){
				if(this.content.length() < 20){
					this.setName(this.content);
				}
				else{
					this.setName(this.content.substring(0, 20) + "...");
				}
			}
		}
		
	}

	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
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
	
	public String getCssText() {
		return cssText;
	}

	public void setCssText(String cssText) {
		this.cssText = cssText;
	}

	public Integer getPermissionsMask() {
		return permissionsMask;
	}

	public void setPermissionsMask(Integer permissionsMask) {
		this.permissionsMask = permissionsMask;
	}
	
	public List<Sheet_Book> getSheet_Book() {
		return sheet_book;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public void setSheet_Book(List<Sheet_Book> sheet_book) {
		this.sheet_book = sheet_book;
	}
	
	public Long getDerivedFromId() {
		return derivedFromId;
	}

	public void setDerivedFromId(Long derivedFromId) {
		this.derivedFromId = derivedFromId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public UserItemStatus getStatus() {
		return status;
	}

	public void setStatus(UserItemStatus status) {
		this.status = status;
	}
	
	public UserAccount getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(UserAccount ownerAccount) {
		this.ownerAccount = ownerAccount;
	}
	
	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}
	
	public Integer getViewsCount() {
		return viewsCount;
	}

	public void setViewsCount(Integer viewsCount) {
		this.viewsCount = viewsCount;
	}

	@Override
	public String toString() {
		return "Sheet [sheetId=" + sheetId + ", name=" + name + ", cssText="
				+ cssText + 
//				", content=" + content + 
				", ownerAccountId=" + ownerAccountId + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + ", permissionsMask=" + permissionsMask
				+ ", derivedFromId=" + derivedFromId + 
//				", bookList=" + bookList + 
//				", sheet_book=" + sheet_book + 
				", createdByIP=" + createdByIP + 
				", modifiedByIP=" + modifiedByIP + ", folderName="
				+ folderName + "]";
	}

	@Override
	public Sheet clone(){
		Sheet s = (Sheet)super.clone();
		//book is holding the main relationship with sheet_book, no need to clone it here
//		if(this.sheet_book != null){
//			List<Sheet_Book> sbList = new ArrayList<Sheet_Book>(this.sheet_book.size());
//			
//			for(Sheet_Book sb : this.sheet_book){
//				Sheet_Book newSb = (Sheet_Book)sb.clone();
//				sbList.add(newSb);
//			}
//			s.sheet_book = sbList;
//		}
		
		//NO NEED TO CLONE UserAccount it is NOT insertable or updatable
		
		return s;
	}
}
