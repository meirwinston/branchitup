package com.branchitup.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
		name="UserWallRecord.selectByUserAccountId",
		query="SELECT w FROM UserWallRecord AS w WHERE w.userAccountId = :userAccountId ORDER BY w.createdOn DESC"
	),
	@NamedQuery(
		name="UserWallRecord.countByUserAccountId",
		query="SELECT COUNT(w) FROM UserWallRecord AS w WHERE w.userAccountId = :userAccountId"
	)
})
@Table(name="userwallrecords",schema="branchitup")
@Entity
public class UserWallRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="recordId")
	protected Long recordId;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@Column(name="senderAccountId")
	protected Long senderAccountId;
	
	@Column(name="message")
	protected String message;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
	@JoinColumn(name="userAccountId", referencedColumnName="userAccountId", updatable=false, insertable=false)
	@OneToOne(fetch=FetchType.LAZY)
	protected UserAccount userAccount;
	
	@JoinColumn(name="senderAccountId", referencedColumnName="userAccountId", updatable=false, insertable=false)
	@OneToOne(fetch=FetchType.LAZY)
	protected UserAccount senderAccount;

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Long getSenderAccountId() {
		return senderAccountId;
	}

	public void setSenderAccountId(Long senderAccountId) {
		this.senderAccountId = senderAccountId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public UserAccount getSenderAccount() {
		return senderAccount;
	}

	public void setSenderAccount(UserAccount senderAccount) {
		this.senderAccount = senderAccount;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}