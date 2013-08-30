/*
 * %W% %E%
 *
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="PublishedSheet.selectByBookId",
	    query="SELECT s FROM PublishedSheet AS s WHERE s.bookId = :bookId ORDER BY s.sequenceIndex"
	),
	@NamedQuery(
	    name="PublishedSheet.selectIDsByBookId",
	    query="SELECT s.sheetId FROM PublishedSheet AS s WHERE s.bookId = :bookId ORDER BY s.sequenceIndex"
	),
	@NamedQuery(
	    name="PublishedSheet.deleteByPublishedBookId",
	    query="DELETE FROM PublishedSheet AS ps WHERE ps.bookId = :bookId"
	),
	@NamedQuery(
	    name="PublishedSheet.countByBookId",
	    query="SELECT COUNT(s.sheetId) FROM PublishedSheet AS s WHERE s.bookId = :bookId"
	)
})
@Table(name="publishedsheets",schema="branchitup")
@Entity
public class PublishedSheet extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@Column(name="sheetId")
	protected Long sheetId;
	
	@Column(name="name", length=100)
	protected String name;
	
	@Column(name="cssText")
	protected String cssText;
	
	@Column(name="content")
	protected String content;
	
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="ownerAccountId")
	protected Long ownerAccountId;

	@Column(name="sequenceIndex")
	protected Integer sequenceIndex;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "bookId",referencedColumnName="bookId" ,nullable = true,insertable=false,updatable=false)
	protected PublishedBook book;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ownerAccountId",referencedColumnName="userAccountId" ,nullable = true,insertable=false,updatable=false)
	protected UserAccount userAccount;
	
	public PublishedSheet(){}
	
	public PublishedSheet(Sheet sheet){
		this(sheet,null);
	}
	
	public PublishedSheet(Sheet sheet,Sheet_Book sb){
		this.name = sheet.getName();
		this.cssText = sheet.getCssText();
		this.content = sheet.getContent();
		this.sequenceIndex = sb.getSequenceIndex();
		this.bookId = sb.getBookId();
//		this.owner = sheet.getOwner();
	}

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCssText() {
		return cssText;
	}

	public void setCssText(String cssText) {
		this.cssText = cssText;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public PublishedBook getBook() {
		return book;
	}

	public void setBook(PublishedBook book) {
		this.book = book;
	}

	public Integer getSequenceIndex() {
		return sequenceIndex;
	}

	public void setSequenceIndex(Integer sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}
	
	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	@Override
	public String toString() {
		return "PublishedSheet [sheetId=" + sheetId + ", name=" + name
				+ ", cssText=" + cssText + ", content=" + content + ", bookId="
				+ bookId + ", owner=" + ownerAccountId + ", sequenceIndex="
				+ sequenceIndex + ", book=" + book + "]";
	}
}