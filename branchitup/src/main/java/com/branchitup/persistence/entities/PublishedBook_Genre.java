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
	    name="PublishedBook_Genre.deleteByPublishedBookId",
	    query="DELETE FROM PublishedBook_Genre AS pbg WHERE pbg.bookId = :bookId"
	)
})

@IdClass(PublishedBook_GenreKey.class)
@Table(name="publishedbooks_genres",schema="branchitup")
@Entity
public class PublishedBook_Genre extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public PublishedBook_Genre(){}
	
	public PublishedBook_Genre(long bookId, long genreId,int sequenceIndex){
		this.bookId = bookId;
		this.genreId = genreId;
		this.sequenceIndex = sequenceIndex;
	}
	
	@Id
	@Column(name="bookId")
	protected long bookId;
	
	@Id
	@Column(name="genreId")
	protected long genreId;
	
	@Column(name="sequenceIndex")
	protected int sequenceIndex;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bookId",referencedColumnName="bookId",insertable=false,updatable=false)
	protected PublishedBook publishedBook;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="genreId",referencedColumnName="genreId",insertable=false,updatable=false)
	protected Genre genre;

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
	
	public int getSequenceIndex() {
		return sequenceIndex;
	}

	public void setSequenceIndex(int sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}
	
	public PublishedBook getPublishedBook() {
		return publishedBook;
	}

	public void setPublishedBook(PublishedBook publishedBook) {
		this.publishedBook = publishedBook;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "PublishedBook_Genre [bookId=" + bookId + ", genreId=" + genreId + "]";
	}
}
