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

import java.io.File;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.branchitup.system.Utils;

/*
 * IN THE FUTURE WE NEED TO FULLY SUPPORT FOLDERS
 * 
 * CREATE DEFINER=`root`@`localhost` PROCEDURE `imagefiles.selectAlbums`(userAccountId BIGINT)
BEGIN
		SELECT DISTINCT album
		FROM imagefiles AS i 
		WHERE i.ownerAccountId = userAccountId
		LIMIT 0,1000;
END$$

DELIMITER ;

CREATE DEFINER=`root`@`localhost` PROCEDURE `imagefiles.countByAlbum`(userAccountId BIGINT, album VARCHAR(50))
BEGIN
		SELECT COUNT(i.imageFileId) 
		FROM imagefiles AS i 
		WHERE i.ownerAccountId = userAccountId	AND i.album = album AND i.folderName = 'USER_UPLOADS';
END$$
 */
@NamedQueries({
	@NamedQuery(
	    name="ImageFile.selectByAccountAndFolder",
	    query="SELECT o FROM ImageFile AS o WHERE o.ownerAccountId = :ownerAccountId AND o.folderName = :folderName ORDER BY o.createdOn DESC"
	),
	@NamedQuery(
	    name="ImageFile.selectAlbums",
	    query="SELECT DISTINCT i.album FROM ImageFile AS i WHERE i.ownerAccountId = :userAccountId"
	),
	@NamedQuery(
	    name="ImageFile.countByOwnerAlbumFolder",
	    query="SELECT COUNT(i.imageFileId) FROM ImageFile AS i WHERE i.ownerAccountId = :ownerAccountId AND i.album = :album AND i.folderName = :folderName"
	),
	@NamedQuery(
	    name="ImageFile.scrollIDsByOwnerAlbumFolder",
	    query="SELECT i.imageFileId FROM ImageFile AS i WHERE i.ownerAccountId = :ownerAccountId AND i.album = :album AND i.folderName = :folderName ORDER BY i.createdOn DESC"
	)
//	@NamedQuery(
//	    name="ImageFile.selectIds(ownerAccountId,folderName)",
//	    query="SELECT o.imageFileId FROM ImageFile AS o WHERE o.ownerAccountId = :ownerAccountId AND o.folderName = :folderName ORDER BY o.createdOn DESC"
//	),
//	@NamedQuery(
//	    name="ImageFile.count(ownerAccountId,folderName)",
//	    query="SELECT COUNT(o.imageFileId) FROM ImageFile AS o WHERE o.ownerAccountId = :ownerAccountId AND o.folderName = :folderName"
//	),
//	@NamedQuery(
//	    name="ImageFile.delete(ownerAccountId,folderName)",
//	    query="DELETE FROM ImageFile AS o WHERE o.ownerAccountId = :ownerAccountId AND o.folderName = :folderName"
//	),
//	@NamedQuery(
//	    name="ImageFile.updateFolder(folderName,imageIds)",
//	    query="UPDATE ImageFile AS i SET i.folderName = :folderName WHERE i.imageFileId IN :imageIds"
//	)
//	@NamedQuery(
//	    name="ImageFile.selectThumbnail(imageFileId)",
//	    query="SELECT o FROM ImageFile AS o WHERE o.imageFileId = (SELECT o2.thumbnailId FROM ImageFile AS o2 WHERE o2.imageFileId = :imageFileId)"
//	)
})
@Table(name="imagefiles",schema="branchitup")
@Entity
public class ImageFile extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public interface Album{
		String DEFAULT = "Default";
	}
	
	public enum SystemFolder{
		USER_UPLOADS,
		GENRES,
		BOOK_COVERS,
		PROFILE_PHOTOS,
//		THUMBNAILS,
		GARBAGE
	}
	public enum Format{
		JPG,
		PNG,
		JPEG,
		GIF
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="imageFileId")
	protected Long imageFileId;
	
	@Column(name="fileName")
	protected String fileName;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
	@Column(name="folderName")
	protected String folderName;
	
	@Column(name="album")
	protected String album;
	
	@Column(name="ownerAccountId")
	protected Long ownerAccountId;
	
	@Column(name="width")
	protected Integer width;
	
	@Column(name="height")
	protected Integer height;
	
	@Column(name="size")
	protected Long size;
	
	@Enumerated(value=EnumType.STRING)
	@Column(name="format")
	protected Format format;
	
	public Long getImageFileId() {
		return imageFileId;
	}

	public void setImageFileId(Long imageFileId) {
		this.imageFileId = imageFileId;
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

	public Integer getWidth() {
		return width;
	}

	public Long getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(Long ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}
	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String resolveFullPath(){
		String fullName = Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + this.folderName + "/" + this.fileName;
		return fullName;
	}

	public boolean delete(){
		boolean deleted = false;
		String fullName = resolveFullPath();//Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + this.folderName + "/" + this.fileName;
		String thumbnailFullName = Utils.getProperty("branchitup.rootDir") + "/" + Utils.getProperty("branchitup.diskresources.imagesPath") + "/" + this.folderName + "/thumbnail_" + this.fileName;
		
		File file = new File(fullName);
		System.out.println("ImageFile.delete: " + file.getAbsolutePath() + ", " + file.exists());
		if(file.exists()){
			deleted = file.delete();
			System.out.println(file.getAbsolutePath() + "(id: " + this.imageFileId + ")" + " deleted? " + deleted);
		}
		else{
			System.out.println("FileSystem.DID NOT delete: " + fullName + "(" + this.imageFileId + ")");
		}
		file = new File(thumbnailFullName);
		System.out.println("ImageFile.thumbnail.delete: " + thumbnailFullName);
		if(file.exists()){
			deleted = file.delete();
			System.out.println(thumbnailFullName + "(thumbnail id: " + this.imageFileId + ")" + " deleted? " + deleted);
		}
		else{
			System.out.println("Thumbnail FileSystem.DID NOT delete: " + thumbnailFullName);
		}
		return deleted;
	}

	@Override
	public String toString() {
		return "ImageFile [imageFileId=" + imageFileId + ", fileName="
				+ fileName + ", createdOn=" + createdOn + ", folderName="
				+ folderName + ", album=" + album + ", ownerAccountId="
				+ ownerAccountId + ", width=" + width + ", height=" + height
				+ ", size=" + size + ", format=" + format + "]";
	}

	
}
