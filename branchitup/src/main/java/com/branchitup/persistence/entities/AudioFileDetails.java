package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(
	    name="AudioFileDetails.selectByBook",
	    query="SELECT new AudioFileDetails(a.audioFileId, u.firstName, u.lastName, u.userAccountId, " +
	    		"a.createdOn, a.description) " +
	    		"FROM AudioFile AS a " +
	    		"INNER JOIN a.owner AS u " + 
	    		"WHERE a.publishedBookId = :bookId " +
	    		"ORDER BY a.createdOn DESC"
	)
})
@Entity
public class AudioFileDetails extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="audioFileId")
	public Long audioFileId;
	
	@Column(name="ownerFirstName")
	public String ownerFirstName;
	
	@Column(name="ownerLastName")
	public String ownerLastName;
	
	@Column(name="ownerAccountId")
	public Long ownerAccountId;
	
	@Column(name="createdOn")
	public Date createdOn;
	
	@Column(name="rate")
	public Double rate;
	
	@Column(name="userRate")
	public Float userRate; //specific user rating
	
	@Column(name="description")
	public String description;
	
	public AudioFileDetails(){}
	
	public AudioFileDetails(Long audioFileId, String ownerFirstName, String ownerLastName, Long ownerAccountId, 
	Date createdOn, String description){
		this.audioFileId = audioFileId;
		this.ownerFirstName = ownerFirstName;
		this.ownerLastName = ownerLastName;
		this.ownerAccountId = ownerAccountId;
		this.createdOn = createdOn;
		this.description = description;
	}

}
