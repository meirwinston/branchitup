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
/*
 * 

CREATE DEFINER=`root`@`localhost` PROCEDURE `genredetails.countByName`(phrase VARCHAR(50))
BEGIN
SELECT COUNT(g.genreId)
	FROM genres AS g 
	WHERE g.name LIKE CONCAT("%",phrase,"%");
END$$


 */
@NamedQueries({
	@NamedQuery(
	    name="Genre.selectAll",
	    query="select object(p) from Genre as p order by p.name"
	),
	@NamedQuery(
	    name="Genre.selectName",
	    query="SELECT p.name FROM Genre AS p WHERE p.genreId = :genreId"
	),
	@NamedQuery(
	    name="Genre.count",
	    query="SELECT COUNT(g.genreId) FROM Genre AS g"
	),
	@NamedQuery(
	    name="Genre.countByPhrase",
	    query="SELECT COUNT(g.genreId) FROM Genre AS g WHERE g.name LIKE :phrase"
	),
	@NamedQuery(
//	    name="Genre.select(phrase)",
		name="Genre.findLikeName",
	    query="SELECT g FROM Genre AS g WHERE g.name LIKE :name ORDER BY g.name"
	)
})
@Table(name="genres",schema="branchitup")
@Entity
public class Genre extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="genreId")
	protected Long genreId;

	@Column(name="name")
	protected String name;
	
	@Column(name="description")
	protected String description;
	
//	@Column(name="parentId")
//	protected Long parentId;
	
	@Column(name="modifiedOn")
	protected Date modifiedOn;
	
	@Column(name="modifiedBy")
	protected Long modifiedBy;
	
//	@Column(name="iconDiskResourceId")
//	protected Long iconDiskResourceId;
//	
//	@OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="iconDiskResourceId", referencedColumnName="diskResourceId",insertable=false,updatable=false)
//	protected DiskResource iconDiskResource;
	
	@Column(name="iconImageFileId")
	protected Long iconImageFileId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="iconImageFileId", referencedColumnName="imageFileId",insertable=false,updatable=false)
	protected ImageFile iconImageFile;
	
	@Column(name="originatorAccountId")
	protected Long originatorAccountId;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="genreId", referencedColumnName="genreId",insertable=false,updatable=false)
//	protected Genre parent;
	
	@Column(name="createdByIP")
	protected String createdByIP;
	
	@Column(name="modifiedByIP")
	protected String modifiedByIP;

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

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Long getIconImageFileId() {
		return iconImageFileId;
	}

	public void setIconImageFileId(Long iconImageFileId) {
		this.iconImageFileId = iconImageFileId;
	}

	public ImageFile getIconImageFile() {
		return iconImageFile;
	}

	public void setIconImageFile(ImageFile iconImageFile) {
		this.iconImageFile = iconImageFile;
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
	
	public String getCreatedByIP() {
		return createdByIP;
	}

	public void setCreatedByIP(String createdByIP) {
		this.createdByIP = createdByIP;
	}

	public String getModifiedByIP() {
		return modifiedByIP;
	}

	public void setModifiedByIP(String modifiedByIP) {
		this.modifiedByIP = modifiedByIP;
	}

	@Override
	public String toString() {
		return "Genre [genreId=" + genreId + ", name=" + name
				+ ", description=" + description
				+ ", modifiedOn=" + modifiedOn + ", modifiedBy="
				+ modifiedBy + ", iconImageFileId=" + iconImageFileId
				+ ", originator="
				+ originatorAccountId + ", createdOn=" + createdOn + "]";
	}
}
