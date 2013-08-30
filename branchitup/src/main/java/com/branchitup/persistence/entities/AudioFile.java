package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="AudioFile.selectByPublishedBookId",
	    query="SELECT a FROM AudioFile AS a WHERE a.publishedBookId = :bookId"
	),
	@NamedQuery(
	    name="AudioFile.selectIdByPublishedBookId",
	    query="SELECT a.audioFileId FROM AudioFile AS a WHERE a.publishedBookId = :bookId"
	),
	@NamedQuery(
	    name="AudioFile.selectIDsByBook",
	    query="SELECT a.audioFileId " +
    		"FROM AudioFile AS a " +
    		"WHERE a.publishedBookId = :bookId "
	),
	@NamedQuery(
	    name="AudioFile.countByBook",
	    query="SELECT COUNT(a.audioFileId) " +
	    		"FROM AudioFile AS a " +
	    		"WHERE a.publishedBookId = :bookId "
	)
})
@Table(name="audiofiles",schema="branchitup")
@Entity
public class AudioFile extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="audioFileId")
	protected Long audioFileId;
	
	@Column(name="fileName")
	protected String fileName;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
//	@Enumerated(value=EnumType.STRING)
//	@Column(name="fileType")
//	protected AttachmentFileType fileType;
	
	@Column(name="dirPath")
	protected String dirPath; //I am not sure if path is needed
	
	@Column(name="publishedBookId")
	protected Long publishedBookId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="publishedBookId",referencedColumnName="bookId",updatable=false, insertable=false)
	protected PublishedBook publishedBook;

	@OneToMany(mappedBy="audioFile", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	protected List<AudioFileRating> ratings;
	
	@Column(name="ownerAccountId")
	protected Long ownerAccountId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ownerAccountId",referencedColumnName="userAccountId",updatable=false, insertable=false)
	protected UserAccount owner;
	
	@Column(name="description")
	protected String description;
	
	@Column(name="size")
	protected Long size;
	
	@Column(name="contentType")
	protected String contentType;
	
	@Column(name="trackLength")
	protected Integer trackLength;
	
	@Column(name="sampleRate")
	protected Integer sampleRate;
	
	@Column(name="bitRate")
	protected Long bitRate;
	
	@Column(name="channels")
	protected String channels;//Stereo
	
	@Column(name="format")
	protected String format;//MPEG-1 Layer 3

	public Long getAudioFileId() {
		return audioFileId;
	}

	public void setAudioFileId(Long audioFileId) {
		this.audioFileId = audioFileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

//	public AttachmentFileType getFileType() {
//		return fileType;
//	}
//
//	public void setFileType(AttachmentFileType fileType) {
//		this.fileType = fileType;
//	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	public Long getPublishedBookId() {
		return publishedBookId;
	}

	public void setPublishedBookId(Long publishedBookId) {
		this.publishedBookId = publishedBookId;
	}

	public PublishedBook getPublishedBook() {
		return publishedBook;
	}

	public void setPublishedBook(PublishedBook publishedBook) {
		this.publishedBook = publishedBook;
	}

	public List<AudioFileRating> getRatings() {
		return ratings;
	}

	public void setRatings(List<AudioFileRating> ratings) {
		this.ratings = ratings;
	}

	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	public UserAccount getOwner() {
		return owner;
	}

	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getTrackLength() {
		return trackLength;
	}

	public void setTrackLength(Integer trackLength) {
		this.trackLength = trackLength;
	}

	public Integer getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(Integer sampleRate) {
		this.sampleRate = sampleRate;
	}

	public Long getBitRate() {
		return bitRate;
	}

	public void setBitRate(Long bitRate) {
		this.bitRate = bitRate;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}