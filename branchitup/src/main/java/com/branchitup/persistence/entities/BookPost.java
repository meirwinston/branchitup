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

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


//@NamedQueries({
//	@NamedQuery(
//	    name="BookPost.select",
//	    query="select o from BookPost as o order by o.timestamp"
//	)
//})
@Table(name="bookposts",schema="branchitup")
@Entity
@Deprecated
public class BookPost extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public enum PostState {REQUIRES_EDITING, REQUIRES_ILLUSTRATION, PROOF_READ, TRANSLATION};
	
	@Id
	@Column(name="bookId")
	private Long bookId;
	
	@Column(name="username")
	private String username;
	
	@JoinColumn(name = "bookId",referencedColumnName="bookId" ,nullable = true,insertable=false,updatable=false)
	@ManyToOne(fetch=FetchType.EAGER)
	protected Book book;
	
	@Enumerated(value=EnumType.STRING)
	@Column(name="postState")
	private PostState postState = PostState.REQUIRES_EDITING;
	
	@Column(name="timestamp")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public PostState getPostState() {
		return postState;
	}

	public void setPostState(PostState postState) {
		this.postState = postState;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "BookPost [bookId=" + bookId + ", postState=" + postState
				+ ", timestamp=" + timestamp + "]";
	}
	
}