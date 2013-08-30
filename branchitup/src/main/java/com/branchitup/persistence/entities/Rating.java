package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
//	@NamedQuery(
//	    name="Rating.selectByUserAndBook",
//	    query="SELECT r FROM Rating AS r WHERE r.bookId = :bookId AND r.userAccountId = :userAccountId"
//	),
	@NamedQuery(
	    name="Rating.selectRateByUserAndBook",
	    query="SELECT r.rate FROM Rating AS r WHERE r.bookId = :bookId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="Rating.countByUserAndBook",
	    query="SELECT COUNT(r.bookId) FROM Rating AS r " +
	    		"WHERE r.bookId = :bookId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="Rating.updateRateByUserAndBook",
	    query="UPDATE Rating AS r SET r.rate = :rate WHERE r.bookId = :bookId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
		name="Rating.ratingAverageByBook",
		query="SELECT AVG(r.rate) FROM Rating AS r WHERE r.bookId = :bookId"
	),
	@NamedQuery(
		name="Rating.ratingCountByBook",
		query="SELECT COUNT(r.rate) FROM Rating AS r WHERE r.bookId = :bookId"
	),
	@NamedQuery(
		name="Rating.averageAndCountByBook",
		query="SELECT AVG(r.rate),COUNT(r.rate) FROM Rating AS r WHERE r.bookId = :bookId"
	)
//	,
//	@NamedQuery(
//	    name="Rating.select(bookId)",
//	    query="SELECT r FROM Rating AS r WHERE r.bookId = :bookId"
//	),

})
@IdClass(RatingKey.class)
@Table(name="ratings",schema="branchitup")
@Entity
public class Rating extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@Id
	@Column(name="bookId")
	protected Long bookId;
	
	
	/**
	 * User's rate
	 * 
	 * Assessment or classification of a book on a scale according to how much or how little 
	 * of a particular quality is possesses
	 * 
	 * values from 0 to 1
	 */
	@Column(name="rate")
	protected Float rate;
	
	@Column(name="createdOn")
	protected Date createdOn;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bookId", referencedColumnName="bookId",insertable=false,updatable=false)
	protected PublishedBook publishedBook;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userAccountId", referencedColumnName="userAccountId",insertable=false,updatable=false)
	protected UserAccount reviewer;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="rating")
	protected List<Review> reviews;

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public PublishedBook getPublishedBook() {
		return publishedBook;
	}

	public void setPublishedBook(PublishedBook publishedBook) {
		this.publishedBook = publishedBook;
	}
	
	public UserAccount getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserAccount reviewer) {
		this.reviewer = reviewer;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "Rating [userName=" + userAccountId + ", bookId=" + bookId
				+ ", rate=" + rate + ", createdOn=" + createdOn + "]";
	}
}
