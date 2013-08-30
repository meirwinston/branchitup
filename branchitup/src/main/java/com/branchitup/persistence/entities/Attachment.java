/*
 * BRANCHITUP PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import java.io.File;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.branchitup.persistence.AttachmentFileType;
import com.branchitup.system.Utils;

/**
 * @author Meir Winston
 *
 */

@NamedQueries({
	@NamedQuery(
	    name="Attachment.selectByPublishedBookId",
	    query="SELECT a FROM Attachment AS a WHERE a.publishedBookId = :bookId"
	),
	@NamedQuery(
	    name="Attachment.selectIdByPublishedBookId",
	    query="SELECT a.attachmentId FROM Attachment AS a WHERE a.publishedBookId = :bookId"
	),
	@NamedQuery(
	    name="Attachment.selectIDsByBookAndFileType",
	    query="SELECT a.attachmentId " +
	    		"FROM Attachment AS a " +
	    		"WHERE a.publishedBookId = :bookId " +
	    		"AND a.fileType = :fileType"
	),
	@NamedQuery(
	    name="Attachment.countByBookAndFileType",
	    query="SELECT COUNT(a.attachmentId) " +
	    		"FROM Attachment AS a " +
	    		"WHERE a.publishedBookId = :bookId " +
	    		"AND a.fileType = :fileType"
	)
})
@Table(name="attachments",schema="branchitup")
@Entity
public class Attachment extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	/*
	 *   if($mimetype=="audio/mpeg"||
	 *   $mimetype=="audio/x-mpeg-3"||
	 *   $mimetype=="audio/mp3"||
	 *   $mimetype=="audio/x-mpeg"||
	 *   $mimetype=="audio/x-mp3"||
	 *   $mimetype=="audio/mpeg3"||
	 *   $mimetype=="audio/x-mpeg3"||
	 *   $mimetype=="audio/mpg"||
	 *   $mimetype=="audio/x-mpg"||
	 *   $mimetype=="audio/x-mpegaudio")
            
	 */
//	public enum FileType {
//		PDF("application/pdf"),
//		MP3("audio/mpeg"),
//		WAV("audio/wav");
//		public String mimeType;
//		FileType(String mimeType){
//			this.mimeType = mimeType;
//		}
//	};
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="attachmentId")
	protected Long attachmentId;
	
	@Column(name="fileName")
	protected String fileName;
	
	@Column(name="createdOn",nullable=false)
//	@Transient
	protected Date createdOn;
	
	@Enumerated(value=EnumType.STRING)
	@Column(name="fileType")
	protected AttachmentFileType fileType;
	
	@Column(name="dirPath")
	protected String dirPath; //I am not sure if path is needed
	
	@Column(name="publishedBookId")
	protected Long publishedBookId;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="publishedBookId",referencedColumnName="bookId",updatable=false, insertable=false)
	protected PublishedBook publishedBook;

//	@OneToMany(mappedBy="attachment", fetch=FetchType.LAZY)
//	protected List<AudioFileRating> ratings;
	
	@Column(name="ownerAccountId")
	protected Long ownerAccountId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ownerAccountId",referencedColumnName="userAccountId",updatable=false, insertable=false)
	protected UserAccount owner;
	
	@Column(name="fileHeader")
	protected String fileHeader;
	
	@Column(name="size")
	protected Integer size;
	
	@Column(name="contentType")
	protected String contentType;
	
	public Long getAttachmentId() {
		return attachmentId;
	}
 
	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
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

	public AttachmentFileType getFileType() {
		return fileType;
	}

	public void setFileType(AttachmentFileType fileType) {
		this.fileType = fileType;
	}

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
	
	public boolean delete(){
		boolean deleted = false;
		String fullName = Utils.getProperty("branchitup.rootDir") + "/" + this.dirPath + "/" + this.fileName;
		
		File file = new File(fullName);
//		System.out.println("Attachment.delete: " + file.getAbsolutePath() + ", " + file.exists());
		if(file.exists()){
			deleted = file.delete();
		}
		else{
			System.out.println("FileSystem.DID NOT delete: " + fullName + "(" + this.fileName + ")");
		}
		return deleted;
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
	
	public String getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "Attachment [attachmentId=" + attachmentId + ", fileName="
				+ fileName + ", createdOn=" + createdOn + ", fileType="
				+ fileType + ", dirPath=" + dirPath + ", publishedBookId="
				+ publishedBookId + "]";
	}
}
