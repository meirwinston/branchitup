package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.branchitup.persistence.BookRole;

@NamedQueries({
	@NamedQuery(
	    name="PublishedBookDetails.selectByPublishedBookId",
	    query="SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.bookId = :bookId "
	),
//	@NamedQuery(
//	    name="PublishedBookDetails.selectActive",
//	    query=
//	    	"SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
//	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
//	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title) " +
//	    	"FROM PublishedBook AS p " +
//	    	"INNER JOIN p.publisherAccount AS u " +
//	    	"LEFT JOIN p.parentBook AS parent " +
//	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
//	    	"ORDER BY p.createdOn DESC"
//	),
	@NamedQuery( //^^^
	    name="PublishedBookDetails.selectActive",
	    query=
	    	"SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title," +
	    	"p.ratingCount, " +
	    	"p.ratingAverage) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"ORDER BY p.ratingAverage DESC, p.ratingCount DESC, p.createdOn DESC"
	),
	@NamedQuery(
	    name="PublishedBookDetails.selectByGenre",
	    query=
	    	"SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title, " +
	    	"p.ratingCount, " +
	    	"p.ratingAverage) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"INNER JOIN p.publishedBook_genre AS pbg " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"AND pbg.genreId = :genreId " +
	    	"ORDER BY p.ratingAverage DESC, p.ratingCount DESC, p.createdOn DESC"
	),
	@NamedQuery(
	    name="PublishedBookDetails.selectByPublisher",
	    query=
	    	"SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title, " +
	    	"p.ratingCount, " +
	    	"p.ratingAverage) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"AND u.userAccountId = :publisherAccountId " +
	    	"ORDER BY p.ratingAverage DESC, p.ratingCount DESC, p.createdOn DESC"
	),
	@NamedQuery(
	    name="PublishedBookDetails.selectLikeTitle",
	    query=
	    	"SELECT new PublishedBookDetails(p.bookId,p.title, p.version, p.parentId, p.branchable," +
	    	"p.bookLanguage, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, " +
	    	"p.coverImageFileId,p.publisherAccountId,u.firstName, u.lastName, u.profileImageFileId, parent.title, " +
	    	"p.ratingCount, " +
	    	"p.ratingAverage) " +
	    	"FROM PublishedBook AS p " +
	    	"INNER JOIN p.publisherAccount AS u " +
	    	"LEFT JOIN p.parentBook AS parent " +
	    	"WHERE p.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    	"AND p.title LIKE :phrase " +
	    	"ORDER BY p.ratingAverage DESC, p.ratingCount DESC, p.createdOn DESC"
	)
})
@Entity
public class PublishedBookDetails extends CompositeEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="bookId")
	public Long bookId;
	
	@Column(name="title")
	public String title;
	
	@Column(name="bookSummary")
	public String bookSummary;
	
	@Column(name="publisherAccountId")
	public Long publisherAccountId;
	
	@Column(name="publisherFirstName")
	public String publisherFirstName;
	
	@Column(name="publisherLastName")
	public String publisherLastName;
	
	@Column(name="publisherProfileImageFileId")
	public Long publisherProfileImageFileId;
	
	@Column(name="createdOn")
	public Date createdOn;
	
	@Column(name="publisherRoleMask")
	public Integer publisherRoleMask;
	
	@Column(name="deficiencyMask")
	public Integer deficiencyMask;

//	@Formula(value="(SELECT AVG(r.rate) FROM ratings AS r WHERE r.bookId = bookId)")
	@Column(name="ratingAverage")
	public Double ratingAverage;
	
//	@Formula(value="(SELECT COUNT(r.rate) FROM ratings AS r WHERE r.bookId = bookId)")
	@Column(name="ratingCount")
	public Long ratingCount;
	
	@Column(name="coverImageFileId")
	public Long coverImageFileId;
	
	@Column(name="version")
	public String version;
	
//	@Formula("SELECT a.attachmentId FROM Attachment AS a WHERE a.publishedBookId = bookId")
//	@Column(name="pdfAttachmentId")
//	public Long pdfAttachmentId;
	
	@Column(name="parentId")
	public Long parentId;
	
//	@Formula("SELECT pb.title FROM PublishedBook AS pb WHERE pb.bookId = parentId")
	@Column(name="parentTitle")
	public String parentTitle;
	
	@Column(name="bookLanguage")
	public String bookLanguage;
	
//	@Transient
	@Column(name="downloadsCount")
	public Long downloadsCount;
	
	@Column(name="branchable")
	public Boolean branchable;
	
	@Transient
	public List<GenreTitle> genreTitles;
	
	@Transient
	public String coverImageUrl;
	
	@Transient
	public List<BookRole> publisherRoleList;
	
	@Transient
	public List<BookRole> deficiencyList;
	
//	@Transient
//	public List<Attachment> attachments;
	
	public transient Long pdfAttachmentId;
	public transient Long epubAttachmentId;
	
	
//	@Transient
//	public List<PublisherRole> publisherRoleList;
	
	public PublishedBookDetails(){}
	
	public PublishedBookDetails(Long bookId, String title, 
			String version, Long parentId, Boolean branchable,
			String bookLanguage, String bookSummary, Date createdOn, 
			Integer publisherRoleMask, Integer deficiencyMask, 
			Long coverImageFileId,Long publisherAccountId,
			String firstName, String lastName, Long publisherProfileImageFileId,
			String parentTitle){
		this.bookId = bookId;
		this.title = title;
		this.version = version;
		this.parentId = parentId;
		this.branchable = branchable;
		this.bookLanguage = bookLanguage;
		this.bookSummary = bookSummary;
		this.createdOn = createdOn;
		this.publisherRoleMask = publisherRoleMask;
		this.deficiencyMask = deficiencyMask;
		this.coverImageFileId = coverImageFileId;
		this.publisherAccountId = publisherAccountId;
		this.publisherFirstName = firstName;
		this.publisherLastName = lastName;
		this.publisherProfileImageFileId = publisherProfileImageFileId;
		this.parentTitle = parentTitle;
	}
	
	public PublishedBookDetails(Long bookId, String title, 
			String version, Long parentId, Boolean branchable,
			String bookLanguage, String bookSummary, Date createdOn, 
			Integer publisherRoleMask, Integer deficiencyMask, 
			Long coverImageFileId,Long publisherAccountId,
			String firstName, String lastName, Long publisherProfileImageFileId,
			String parentTitle, Long ratingCount,Double ratingAvg){
		this.bookId = bookId;
		this.title = title;
		this.version = version;
		this.parentId = parentId;
		this.branchable = branchable;
		this.bookLanguage = bookLanguage;
		this.bookSummary = bookSummary;
		this.createdOn = createdOn;
		this.publisherRoleMask = publisherRoleMask;
		this.deficiencyMask = deficiencyMask;
		this.coverImageFileId = coverImageFileId;
		this.publisherAccountId = publisherAccountId;
		this.publisherFirstName = firstName;
		this.publisherLastName = lastName;
		this.publisherProfileImageFileId = publisherProfileImageFileId;
		this.parentTitle = parentTitle;
		this.ratingCount = ratingCount;
		this.ratingAverage = ratingAvg;
		
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
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

	public Long getPublisherAccountId() {
		return publisherAccountId;
	}

	public void setPublisherAccountId(Long publisherAccountId) {
		this.publisherAccountId = publisherAccountId;
	}

	public String getPublisherFirstName() {
		return publisherFirstName;
	}

	public void setPublisherFirstName(String publisherFirstName) {
		this.publisherFirstName = publisherFirstName;
	}

	public String getPublisherLastName() {
		return publisherLastName;
	}

	public void setPublisherLastName(String publisherLastName) {
		this.publisherLastName = publisherLastName;
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

	public Double getRatingAverage() {
		return ratingAverage;
	}

	public void setRatingAverage(Double ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Long getCoverImageFileId() {
		return coverImageFileId;
	}

	public void setCoverImageFileId(Long coverImageFileId) {
		this.coverImageFileId = coverImageFileId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

//	public Long getPdfAttachmentId() {
//		return pdfAttachmentId;
//	}
//
//	public void setPdfAttachmentId(Long pdfAttachmentId) {
//		this.pdfAttachmentId = pdfAttachmentId;
//	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentTitle() {
		return parentTitle;
	}

	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	public Long getDownloadsCount() {
		return downloadsCount;
	}

	public void setDownloadsCount(Long downloadsCount) {
		this.downloadsCount = downloadsCount;
	}

	public List<GenreTitle> getGenreTitles() {
		return genreTitles;
	}

	public void setGenreTitles(List<GenreTitle> genreTitles) {
		this.genreTitles = genreTitles;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}
	public Boolean getBranchable() {
		return branchable;
	}

	public void setBranchable(Boolean branchable) {
		this.branchable = branchable;
	}

	public String getBookLanguage() {
		return bookLanguage;
	}

	public void setBookLanguage(String bookLanguage) {
		this.bookLanguage = bookLanguage;
	}

	public List<BookRole> getPublisherRoleList() {
		return publisherRoleList;
	}

	public void setPublisherRoleList(List<BookRole> publisherRoleList) {
		this.publisherRoleList = publisherRoleList;
	}
	
	
	public List<BookRole> getDeficiencyList() {
		if(this.deficiencyList == null){
			this.deficiencyList = PublishedBook.toDeficiencyList(this.deficiencyMask);
		}
		return deficiencyList;
	}

	public void setDeficiencyList(List<BookRole> deficiencyList) {
		this.deficiencyList = deficiencyList;
	}

	public Long getPdfAttachmentId() {
		return pdfAttachmentId;
	}

	public void setPdfAttachmentId(Long pdfAttachmentId) {
		this.pdfAttachmentId = pdfAttachmentId;
	}

	public Long getEpubAttachmentId() {
		return epubAttachmentId;
	}

	public void setEpubAttachmentId(Long epubAttachmentId) {
		this.epubAttachmentId = epubAttachmentId;
	}

	public Long getPublisherProfileImageFileId() {
		return publisherProfileImageFileId;
	}

	public void setPublisherProfileImageFileId(Long publisherProfileImageFileId) {
		this.publisherProfileImageFileId = publisherProfileImageFileId;
	}
	
//	private List<String> getPublisherRoleList(){
//		return publisherRoleList(publisherRoleMask);
//	}
//	
//	private static List<String> publisherRoleList(Integer publisherRoleMask){
//		List<String> list = new ArrayList<String>();
//		if(publisherRoleMask != null){
//			if((publisherRoleMask & BookRole.PROOF_READING.maskVal) > 0){
//				list.add("Proof-read");
//			}
//			if((publisherRoleMask & BookRole.EDITING.maskVal) > 0){
//				list.add("Edited");
//			}
//			if((publisherRoleMask & BookRole.ILLUSTRATING.maskVal) > 0){
//				list.add("Illustrated");
//			}
//			
//			if((publisherRoleMask & BookRole.TRANSLATING.maskVal) > 0){
//				list.add("Translated");
//			}
//			if((publisherRoleMask & BookRole.WRITING.maskVal) > 0){
//				list.add("Wrote");
//			}
//		}
//		return list;
//	}

}
