package com.branchitup.persistence.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//		name="ReviewRecord.browseByBookId(bookId,offset,maxResults)",
//		resultClass=com.branchitup.persistence.entities.ReviewRecord.class,
//		query="call `branchitup`.`reviewrecord.selectByBookId`(?bookId,?offset,?maxResults)"
//	)
//})
@NamedQueries({
	@NamedQuery(//userAccountId
		name="ReviewRate.selectByBookId",
		query="SELECT new ReviewRate(r.reviewId, r.comment, r.userAccountId, r.createdOn, u.firstName, " +
			"u.lastName, ra.rate) " +
			"FROM Review AS r " +
			"INNER JOIN r.reviewer AS u " +
			"LEFT JOIN r.rating AS ra " +
			"WHERE r.bookId = :bookId " +
			"ORDER BY r.createdOn DESC"
	)
})
@Entity
public class ReviewRate{
//	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="reviewId")
	public Long reviewId;
	
//	@Column(name="bookId")
//	public Long bookId;
	
	@Column(name="createdOn")
	public Date createdOn;
	
	@Column(name="comment")
	public String comment;
	
	@Column(name="rate")
	public Float rate;
	
	@Column(name="userAccountId")
	public Long userAccountId;
	
	@Column(name="firstName")
	public String firstName;
	
	@Column(name="lastName")
	public String lastName;
	
//	@Column(name="")
	@Transient
	public UserName reviewer;
	
	public ReviewRate(){
		
	}
	
	public ReviewRate(Long reviewId, String comment, Long userAccountId, Date createdOn, String firstName, 
	String lastName, Float rate){
		this.reviewId = reviewId;
		this.comment = comment;
		this.userAccountId = userAccountId;
		this.createdOn = createdOn;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rate = rate;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public String getFirstName() {
		return firstName;
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

	public UserName getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserName reviewer) {
		this.reviewer = reviewer;
	}

	@Override
	public String toString() {
		return "ReviewRecord [reviewId=" + reviewId + ", createdOn=" + createdOn + ", comment="
				+ comment + ", rate=" + rate + ", userAccountId=" + userAccountId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", reviewer=" + reviewer + "]";
	}
	
//	public ReviewRecord(Long reviewId,Long publicationId, Timestamp createdOn, String comment, Float rate, String userName, String firstName, String lastName){
//		this.reviewId = reviewId;
//		this.publicationId = publicationId;
//		this.createdOn = createdOn;
//		this.comment = comment;
//		this.rate = rate;
//		this.reviewer = new UserAccountRecord(userName, firstName, lastName)
//	}
	
	
	
}
