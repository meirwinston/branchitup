package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="AudioFileRating.selectRateByUserAndId",
	    query="SELECT r.rate " +
	    		"FROM AudioFileRating AS r " +
	    		"WHERE r.audioFileId = :audioFileId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="AudioFileRating.countByUserAndId",
	    query="SELECT COUNT(r.audioFileId) " +
	    		"FROM AudioFileRating AS r " +
	    		"WHERE r.audioFileId = :audioFileId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="AudioFileRating.updateRateByUserAndId",
	    query="UPDATE AudioFileRating AS r SET r.rate = :rate " +
	    		"WHERE r.audioFileId = :audioFileId AND r.userAccountId = :userAccountId"
	),
	@NamedQuery(
		name="AudioFileRating.ratingAverageById",
		query=
			"SELECT AVG(r.rate) " +
			"FROM AudioFileRating AS r " +
			"WHERE r.audioFileId = :audioFileId"
	),
	@NamedQuery(
		name="AudioFileRating.ratingCountById",
		query="SELECT COUNT(r.rate) " +
				"FROM AudioFileRating AS r " +
				"WHERE r.audioFileId = :audioFileId"
	),
	@NamedQuery(
		name="AudioFileRating.averageAndCountById",
		query="SELECT AVG(r.rate),COUNT(r.rate) " +
				"FROM AudioFileRating AS r " +
				"WHERE r.audioFileId = :audioFileId"
	)
})
@Table(name="audiofileratings",schema="branchitup")
@Entity
public class AudioFileRating extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@Id
	@Column(name="audioFileId")
	protected Long audioFileId;
	
	@Column(name="rate")
	protected Float rate;
	
	@Column(name="createdOn")
	protected Date createdOn;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="audioFileId", referencedColumnName="audioFileId",insertable=false,updatable=false)
	protected AudioFile audioFile;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userAccountId", referencedColumnName="userAccountId",insertable=false,updatable=false)
	protected UserAccount userAccount;
	
	public AudioFileRating(){}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Long getAudioFileId() {
		return audioFileId;
	}

	public void setAudioFileId(Long audioFileId) {
		this.audioFileId = audioFileId;
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

	public AudioFile getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(AudioFile audioFile) {
		this.audioFile = audioFile;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
}
