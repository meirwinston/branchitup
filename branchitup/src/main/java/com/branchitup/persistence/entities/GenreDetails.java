package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@NamedNativeQueries({
//	@NamedNativeQuery(
//	    name="genres.select(parentId,startPosition,maxResults)",
//	    query="SELECT g.genreId, g.name, g.iconImageFileId, g.originator, g.createdOn,g.description,(SELECT COUNT(c.genreId) FROM genres AS c WHERE c.parentId = g.genreId) AS numberOfChildren FROM genres AS g WHERE g.parentId = ?parentId ORDER BY g.name ASC LIMIT ?startPosition,?maxResults",
//	    resultClass=GenreRecord.class
//	),
//	@NamedNativeQuery(
//	    name="GenreRecord.browse",
//	    query="CALL `branchitup`.`genres.browse`(:offset,:maxResults)",
//	    resultClass=GenreRecord.class
//	),
//	@NamedNativeQuery(
//	    name="GenreRecord.searchByName",
////	    query="CALL `publishedbooks.searchByTitle`(?phrase,?offset,?maxResults)",
//	    query="CALL `genres.searchByName`(:phrase,:offset,:maxResults)",
//	    resultClass=GenreRecord.class
//	),
//	@NamedNativeQuery(
//	    name="genres.selectTopLevel(startPosition,maxResults)",
//	    query="SELECT g.genreId, g.name, g.iconImageFileId, g.originator, g.createdOn,g.description,(SELECT COUNT(c.genreId) FROM genres AS c WHERE c.parentId = g.genreId) AS numberOfChildren FROM genres AS g WHERE g.parentId IS NULL ORDER BY g.name ASC LIMIT ?startPosition,?maxResults",
//	    resultClass=GenreRecord.class
//	)
})
/*
CREATE PROCEDURE `genredetails.scrollByName`(phrase VARCHAR(50),`offset` BIGINT,maxResults INT)
BEGIN
SELECT g.genreId, g.name, g.iconImageFileId, g.originatorAccountId, g.createdOn,g.description
	FROM genres AS g 
	WHERE g.name LIKE phrase
	ORDER BY g.name ASC 
	LIMIT `offset`,maxResults;
END $$*/
@NamedQueries({
	@NamedQuery(
		name="GenreDetails.selectByGenreId",
		query="SELECT new GenreDetails(g.genreId, g.name, g.iconImageFileId, g.originatorAccountId, g.createdOn,g.description) FROM Genre AS g WHERE g.genreId = :genreId"
	),
	@NamedQuery(
		name="GenreDetails.select",
		query="SELECT new GenreDetails(g.genreId, g.name, g.iconImageFileId, g.originatorAccountId, g.createdOn,g.description) FROM Genre AS g ORDER BY g.modifiedOn DESC,g.createdOn DESC"
	),
	@NamedQuery(
		name="GenreDetails.selectLikeName",
		query="SELECT new GenreDetails(g.genreId, g.name, g.iconImageFileId, g.originatorAccountId, g.createdOn,g.description) FROM Genre AS g WHERE g.name LIKE :phrase ORDER BY g.name ASC"
	)
})
@Entity
public class GenreDetails {
	@Id
	@Column(name="genreId")
	protected Long genreId;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="description")
	protected String description;
	
	@Column(name="iconImageFileId")
	protected Long iconImageFileId;
	
	@Column(name="originatorAccountId")
	protected Long originatorAccountId;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
	@Transient
	protected String iconImageUrl;
	
	public GenreDetails(){
	}
	
	public GenreDetails(Long genreId, String name, Long iconImageFileId, Long originatorAccountId, Date createdOn, String description){
		this.genreId = genreId;
		this.name = name;
		this.iconImageFileId = iconImageFileId;
		this.originatorAccountId = originatorAccountId;
		this.createdOn = createdOn;
		this.description = description;
	}
	
	public GenreDetails(Long genreId, String genreName){
		this.genreId = genreId;
		this.name = genreName;
	}
	
	
	public Long getIconImageFileId() {
		return iconImageFileId;
	}

	public void setIconImageFileId(Long iconImageFileId) {
		this.iconImageFileId = iconImageFileId;
	}
	
	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getOriginatorAccountId() {
		return originatorAccountId;
	}

	public void setOriginatorAccountId(Long originatorAccountId) {
		this.originatorAccountId = originatorAccountId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getIconImageUrl() {
		return iconImageUrl;
	}

	public void setIconImageUrl(String iconImageUrl) {
		this.iconImageUrl = iconImageUrl;
	}

	@Override
	public String toString() {
		return "GenreRecord [genreId=" + genreId + ", name=" + name
				+ ", description=" + description
				+ ", iconImageFileId="
				+ iconImageFileId + ", originator=" + originatorAccountId
				+ ", createdOn=" + createdOn + ", numberOfChildren="
				+ "]";
	}
}
