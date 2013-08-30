package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * publication - The preparation and issuing of a book, journal, piece of music, or other work for public sale.
 * 
 * @author Meir Winston
 */
@NamedQueries({
//	@NamedQuery(
//	    name="Review.selectByPublicationId",
//	    query="SELECT o FROM Review AS o WHERE o.publicationId = :publicationId ORDER BY o.createdOn DESC"
//	),
	@NamedQuery(
	    name="Review.countByBookId(bookId)",
	    query="SELECT COUNT(o) FROM Review AS o WHERE o.bookId = :bookId"
	),
//	@NamedQuery(
//	    name="Review.ratingAverage(publishedBookId)",
//	    query="SELECT AVG(r.rate) FROM Review AS r WHERE r.bookId = :publishedBookId AND r.rate IS NOT NULL"
//	),
//	@NamedQuery(
//	    name="Review.ratingCount(publishedBookId)",
//	    query="SELECT COUNT(r.rate) FROM Review AS r WHERE r.bookId = :publishedBookId AND r.rate IS NOT NULL"
//	)
//	@NamedQuery(
//	    name="ReviewRecord.selectByPublicationId",
//	    query="SELECT new com.branchitup.transfer.persistence.ReviewRecord(o.reviewId,o.publicationId,o.createdOn, o.comment, o.rate, o.reviewer.userName, o.reviewer.firstName, o.reviewer.lastName) FROM Review AS o WHERE o.publicationId = :publicationId ORDER BY o.createdOn DESC"
//	)
})

@Table(name="reviews",schema="branchitup")
@Entity
public class Review extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	//GenerationType.SEQUENCE will not populate the id field after persist, annoying
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reviewId")
	protected Long reviewId;
	
	@Column(name="bookId")
	protected Long bookId;
	
	@JoinColumn(name = "bookId",referencedColumnName="bookId" ,nullable = true,insertable=false,updatable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	protected PublishedBook book;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@JoinColumn(name = "userAccountId",referencedColumnName="userAccountId",insertable=false,updatable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	protected UserAccount reviewer;
	
//	/**
//	 * User's rate
//	 * 
//	 * Assessment or classification of a book on a scale according to how much or how little 
//	 * of a particular quality is possesses
//	 * 
//	 * values from 0 to 1
//	 */
//	@Column(name="rate")
//	protected Float rate;
	
	@Column(name="comment")
	protected String comment;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
	/**
	 * comment about a comment
	 */
	@Column(name="parentId")
	protected Long parentId;
	
	/**
	 * the sequence number of a comment in the context of a single book.
	 * this number is unique in a book
	 * 
	 * we could generate numbers on the fly based on timestamp, but a user might
	 * refer his comment to a specific number???
	 */
//	@Column(name="commentNumber")
//	protected Integer commentNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "parentId",referencedColumnName="parentId" ,updatable=false,insertable=false)
	protected Review parent;
	
	@OneToMany(mappedBy="parent",fetch=FetchType.LAZY) 
	protected List<Review> children;
	
	@ManyToOne(fetch=FetchType.LAZY)
	
	@JoinColumns({
		@JoinColumn(name = "bookId",referencedColumnName="bookId" ,updatable=false,insertable=false),
		@JoinColumn(name = "userAccountId",referencedColumnName="userAccountId" ,updatable=false,insertable=false)
	})
	protected Rating rating;

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Number getBookId() {
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

//	public Float getRate() {
//		return rate;
//	}
//
//	public void setRate(Float rate) {
//		this.rate = rate;
//	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Review getParent() {
		return parent;
	}

	public void setParent(Review parent) {
		this.parent = parent;
	}

	public List<Review> getChildren() {
		return children;
	}

	public UserAccount getReviewer() {
		return reviewer;
	}
	
	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public void setReviewer(UserAccount reviewer) {
		this.reviewer = reviewer;
	}

	@Override
	public String toString() {
		return "Review [reviewId=" + reviewId + ", bookId=" + bookId
				+ ", book=" + book + ", userName=" + userAccountId + ", reviewer="
				+ reviewer + ", comment=" + comment + ", createdOn="
				+ createdOn + ", parentId=" + parentId + ", parent=" + parent
				+ ", children=" + children + "]";
	}
}