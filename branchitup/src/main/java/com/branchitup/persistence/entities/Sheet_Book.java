/*
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
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(
	    name="Sheet_Book.deleteAllByBookId",
	    query="DELETE FROM Sheet_Book AS sb WHERE sb.bookId = :bookId"
	),
	@NamedQuery(
	    name="Sheet_Book.selectByBookId",
	    query="SELECT sb FROM Sheet_Book AS sb WHERE sb.bookId = :bookId ORDER BY sb.sequenceIndex"
	),
	@NamedQuery(
	    name="Sheet_Book.update(bookId,newBookId)",
	    query="UPDATE Sheet_Book as o SET o.bookId = :newBookId, o.sequenceIndex = NULL WHERE o.bookId = :bookId"
	),
	@NamedQuery(
	    name="Sheet_Book.countByBookId",
	    query="SELECT COUNT(sb.sheetId) FROM Sheet_Book as sb WHERE sb.bookId = :bookId"
	),
	@NamedQuery(
		name="Sheet_Book.delete(bookId,sheetId)",
		query="DELETE FROM Sheet_Book AS o WHERE o.bookId = :bookId AND o.sheetId = :sheetId"
	)
})
@IdClass(Sheet_BookKey.class)
@Table(name="sheet_book",schema="branchitup")
@Entity
public class Sheet_Book extends BaseEntity{
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="sheetId")
	protected Long sheetId;
	
	@Id
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="sequenceIndex")
	protected Integer sequenceIndex;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sheetId",referencedColumnName="sheetId",insertable=false,updatable=false)
	protected Sheet sheet;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bookId",referencedColumnName="bookId",updatable=false, insertable=false)
	protected Book book;
	
	public Sheet_Book(){}
	
	public Sheet_Book(Long sheetId,Long bookId, Integer sequenceIndex){
		this.sheetId = sheetId;
		this.bookId = bookId;
		this.sequenceIndex = sequenceIndex;
	}

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getSequenceIndex() {
		return sequenceIndex;
	}

	public void setSequenceIndex(Integer sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}
	
}
