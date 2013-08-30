/*
 * BranchItUp PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;
import com.branchitup.persistence.BookRole;
import com.branchitup.persistence.UserItemStatus;

@NamedQueries({
	@NamedQuery(
	    name="PublishedBook.countLikeBookTitle",
	    query=
	    	"SELECT DISTINCT COUNT(p.bookId) " +
	    	"FROM PublishedBook AS p " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"AND p.title LIKE :phrase"
	),
	@NamedQuery(
	    name="PublishedBook.countByPublisher",
	    query=
	    	"SELECT DISTINCT COUNT(p.bookId) " +
	    	"FROM PublishedBook AS p " +
	    	"WHERE p.publisherAccountId = :publisherAccountId " +
	    	"AND p.status = com.branchitup.persistence.UserItemStatus.ACTIVE"
	),
	@NamedQuery(
	    name="PublishedBook.countByGenre",
	    query=
	    	"SELECT DISTINCT COUNT(p.bookId) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publishedBook_genre AS g " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"AND g.genreId = :genreId " +
	    	"AND g.sequenceIndex = 0 "
	),
	@NamedQuery(
	    name="PublishedBook.countActive",
	    query=
	    	"SELECT DISTINCT COUNT(p.bookId) " +
	    	"FROM PublishedBook AS p " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE "
	),
	@NamedQuery(
	    name="PublishedBook.selectTitle",
	    query="SELECT p.title FROM PublishedBook AS p WHERE p.bookId = :bookId"
	),
	@NamedQuery(
	    name="PublishedBook.count",
	    query="SELECT COUNT(p) FROM PublishedBook AS p WHERE p.status = 'ACTIVE'"
	),
//	@NamedQuery(
//	    name="PublishedBook.count(titlePhrase)",
//	    query="SELECT COUNT(p) FROM PublishedBook AS p WHERE p.title LIKE :titlePhrase AND p.status = 'ACTIVE'"
//	),
	@NamedQuery(
		name="PublishedBook.selectTitleById",
		query="SELECT p.title FROM PublishedBook AS p WHERE p.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.updateStatus",
		query="UPDATE PublishedBook AS p SET p.status = :status WHERE p.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.selectCoverImageFile",
		query="SELECT pb.coverImageFile AS i FROM PublishedBook AS pb WHERE pb.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.delete",
		query="DELETE FROM PublishedBook AS pb WHERE pb.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.countOtherReferencingImageFile",
		query="SELECT COUNT(pb.bookId) FROM PublishedBook AS pb WHERE pb.coverImageFile = :imageFileId AND pb.bookId <> :bookId"
	),
	@NamedQuery(
		name="PublishedBook.incrementSubversionCount",
		query="UPDATE PublishedBook AS b SET b.subversionCount = (b.subversionCount + 1) WHERE b.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.selectVersionAndSubversionCount",
		query="SELECT b.version,b.subversionCount FROM PublishedBook AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.selectPublisherAccountId",
		query="SELECT b.publisherAccountId FROM PublishedBook AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery(
		name="PublishedBook.selectPublisherComment",
		query="SELECT b.publisherComment FROM PublishedBook AS b WHERE b.bookId = :bookId"
	),
	@NamedQuery( //^^^
	    name="PublishedBook.selectActive",
	    query=
	    	"SELECT p " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"ORDER BY p.ratingCount DESC, p.ratingAverage DESC, p.createdOn DESC"
	)
//	@NamedQuery(
//	    name="PublishedBook.countBranches",
//	    query="SELECT COUNT(b.bookId) FROM Book AS b WHERE b.derivedFromId = :publishedBookId"
//	)
	//
	//
})
@Table(name="publishedbooks",schema="branchitup")
//@AttributeOverrides({
//	@AttributeOverride(name = "bookId", column = @Column(name = "bookId"))
//})
@Entity
public class PublishedBook extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
//	public interface PublisherRole{
//		int ORIGINATOR = 1;
//		int AUTHOR = 2;
//		int ILLUSTRATOR = 4;
//		int EDITOR = 8;
//		int COMMENTATOR = 16;
//		int TRANSLATOR = 32;
//		int PROOF_READER = 64;
//	}
	
//	public enum Status{
//		ACTIVE,
//		PENDING_ACTIVATION,
//		REMOVED
//	}
	
//	static class PublishedSheetComparator implements Comparator<PublishedSheet>{
//		@Override
//		public int compare(PublishedSheet s1, PublishedSheet s2) {
//			System.out.println("PublishedSheetComparator----------------------------" + (s1.getSequenceIndex().compareTo(s2.getSequenceIndex())));
//			return s1.getSequenceIndex().compareTo(s2.getSequenceIndex());
//			
//		}
//	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="createdOn",nullable=false)
	protected Date createdOn;
	
	@Column(name="publisherComment")
	protected String publisherComment;
	
	//AUTHOR,ILLUSTRATOR,EDITOR,COMMENTATOR,TRANSLATOR,PROOF_READER
//	@Enumerated(value=EnumType.STRING)
	@Column(name="publisherRoleMask")
	protected Integer publisherRoleMask;
	
	//CO_AUTHORING,ILLUSTRATION,EDITING,COMMENTING,TRANSLATION,PROOF_READING
	@Column(name="deficiencyMask")
	protected Integer deficiencyMask;
	
	@Column(name="parentId")
	protected Long parentId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId", referencedColumnName="bookId",insertable=false,updatable=false)
	protected PublishedBook parentBook;
	
	@Column(name="version")
	protected String version;
	
	@Column(name="subversionCount")
	protected Integer subversionCount;
	
	@Column(name="title", length=50)
	protected String title;
	
	@Column(name="bookSummary",length=255)
	protected String bookSummary;
	
	@Column(name="coverImageFileId")
	protected Long coverImageFileId;
	
	@JoinColumn(name = "coverImageFileId",referencedColumnName="imageFileId",insertable=false,updatable=false)
	@OneToOne(fetch=FetchType.LAZY)
	protected ImageFile coverImageFile; 
	
	/**
	 * the current owner of this version of the book
	 * the maximum length of an email address is 320 characters.
	 */
	@Column(name="publisherAccountId",nullable=false)
	protected Long publisherAccountId;
	
	@JoinColumn(name = "publisherAccountId",referencedColumnName="userAccountId",insertable=false,updatable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	protected UserAccount publisherAccount;
	
//	@Transient //->this field causes StackOverflowException
//	@Sort(comparator=PublishedSheetComparator.class)
	@OrderBy("sequenceIndex")
	@OneToMany(mappedBy="book",fetch=FetchType.LAZY)
	protected List<PublishedSheet> sheetList;
	
	@Column(name="branchable")
	protected Boolean branchable;
	
//	@Column(name="genreId")
//	protected Long genreId;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="genreId",referencedColumnName="genreId",insertable=false,updatable=false)
//	protected Genre genre;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="publishedbooks_genres",
		joinColumns={
			@JoinColumn(name="bookId",referencedColumnName="bookId")
		},
		inverseJoinColumns={
			@JoinColumn(name="genreId",referencedColumnName="genreId")	
		}
	)
	protected List<Genre> genres;
	
	@OneToMany(mappedBy="publishedBook",fetch=FetchType.LAZY)
	protected List<PublishedBook_Genre> publishedBook_genre;
	
	@OneToMany(mappedBy="book",fetch=FetchType.LAZY)
	protected List<Review> reviews;
	
	@Column(name="properties")
	protected String properties;
	
	@Enumerated(EnumType.STRING)
	@Column(name="bookLanguage")
	protected String bookLanguage;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	protected UserItemStatus status = UserItemStatus.PENDING_ACTIVATION;
	
	@Column(name="publishedByIP")
	protected String publishedByIP;
	
	@OneToMany(mappedBy="publishedBook",fetch=FetchType.LAZY)
	protected List<Rating> rating;
	
	@OneToMany(mappedBy="publishedBook",fetch=FetchType.LAZY)
	protected List<Attachment> attachment;
	
	
	@OneToMany(mappedBy="publishedBook",fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	protected List<AudioFile> audioFiles;
	
	@Formula(value="(SELECT COUNT(r.rate) FROM ratings AS r WHERE r.bookId = bookId)")
	private Long ratingCount;
	
	@Formula(value="(SELECT AVG(r.rate) FROM ratings AS r WHERE r.bookId = bookId)")
	private Double ratingAverage;
	
	public PublishedBook(){
	}
	
	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getPublisherRoleMask() {
		return publisherRoleMask;
	}

	public void setPublisherRoleMask(Integer publisherRoleMask) {
		this.publisherRoleMask = publisherRoleMask;
	}

	public Integer getDeficiencyMask() {
		return deficiencyMask;
	}

	public void setDeficiencyMask(Integer deficiencyMask) {
		this.deficiencyMask = deficiencyMask;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public PublishedBook getParentBook() {
		return parentBook;
	}
//
//	public void setParentBook(PublishedBook parentBook) {
//		this.parentBook = parentBook;
//	}
//
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBookSummary() {
		return bookSummary;
	}

	public void setBookSummary(String bookSummary) {
		this.bookSummary = bookSummary;
	}

	public Long getCoverImageFileId() {
		return coverImageFileId;
	}

	public void setCoverImageFileId(Long coverImageFileId) {
		this.coverImageFileId = coverImageFileId;
	}

	public ImageFile getCoverImageFile() {
		return this.coverImageFile;
	}

	public void setCoverImageFile(ImageFile coverImageFile) {
		this.coverImageFile = coverImageFile;
	}

	public Long getPublisherAccountId() {
		return publisherAccountId;
	}

	public void setPublisherAccountId(Long publisherAccountId) {
		this.publisherAccountId = publisherAccountId;
	}

	public List<PublishedSheet> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<PublishedSheet> sheetList) {
		this.sheetList = sheetList;
	}

//	public Long getGenreId() {
//		return genreId;
//	}
//
//	public void setGenreId(Long genreId) {
//		this.genreId = genreId;
//	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	public UserAccount getPublisherAccount() {
		return publisherAccount;
	}

	public void setPublisherAccount(UserAccount publisherAccount) {
		this.publisherAccount = publisherAccount;
	}
	public void setSubversionCount(Integer subversionCount) {
		this.subversionCount = subversionCount;
	}

	public static List<BookRole> toDeficiencyList(Integer deficiencyMask){
		List<BookRole> list = new ArrayList<BookRole>();
		if(deficiencyMask != null){
			int val = deficiencyMask.intValue();
			for(BookRole d : BookRole.values()){
				if((val & d.maskVal) != 0){
					list.add(d);
				}
			}			
		}
		return list;
	}
	
//	public static PublisherRole[] toPublisherRoles(Integer publisherRoleMask){
//		List<PublisherRole> list = new ArrayList<PublisherRole>();
//		int val = publisherRoleMask.intValue();
//		for(PublisherRole role : PublisherRole.values()){
//			if((val & role.maskVal) != 0){
//				list.add(role);
//			}
//		}
//		return list.toArray(new PublisherRole[0]);
//	}
	public UserItemStatus getStatus() {
		return status;
	}

	public void setStatus(UserItemStatus status) {
		this.status = status;
	}
	
	public String getPublisherComment() {
		return publisherComment;
	}

	public void setPublisherComment(String publisherComment) {
		this.publisherComment = publisherComment;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getBookLanguage() {
		return bookLanguage;
	}

	public void setBookLanguage(String bookLanguage) {
		this.bookLanguage = bookLanguage;
	}

	public Integer getSubversionCount() {
		return subversionCount;
	}

	public void setParentBook(PublishedBook parentBook) {
		this.parentBook = parentBook;
	}
	
	public String getPublishedByIP() {
		return publishedByIP;
	}

	public void setPublishedByIP(String publishedByIP) {
		this.publishedByIP = publishedByIP;
	}
	
	public Boolean getBranchable() {
		return branchable;
	}

	public void setBranchable(Boolean branchable) {
		this.branchable = branchable;
	}
	
	public List<PublishedBook_Genre> getPublishedBook_genre() {
		return publishedBook_genre;
	}

	public void setPublishedBook_genre(List<PublishedBook_Genre> publishedBook_genre) {
		this.publishedBook_genre = publishedBook_genre;
	}

	public List<Rating> getRating() {
		return rating;
	}

	public void setRating(List<Rating> rating) {
		this.rating = rating;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}
	
	public List<AudioFile> getAudioFiles() {
		return audioFiles;
	}

	public void setAudioFiles(List<AudioFile> audioFiles) {
		this.audioFiles = audioFiles;
	}

	public Long getRatingCount() {
		return ratingCount;
	}
	
	public Double getRatingAverage() {
		return ratingAverage;
	}

	//be aware not to include sheetList and genreList in toString
	//that will cause StackOverflowError
	@Override
	public String toString() {
		return "PublishedBook [bookId=" + bookId + ", createdOn=" + createdOn
//				+ ", publisherRoleMask=" + publisherRoleMask
//				+ ", deficiencyMask=" + deficiencyMask + ", parentId="
//				+ parentId + ", parentBook=" + parentBook + ", version="
//				+ version + ", subversionCount=" + subversionCount + ", title=" + title
//				+ ", bookSummary=" + bookSummary + ", coverImageFileId="
//				+ coverImageFileId + ", coverImageFile=" + coverImageFile
//				+ ", publisherUserName=" + publisherUserName
//				+ ", publisherAccount=" + publisherAccount
				+ "]";
	}
}
