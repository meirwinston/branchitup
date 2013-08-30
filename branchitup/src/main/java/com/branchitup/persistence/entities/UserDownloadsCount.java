package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



@NamedQueries({
	@NamedQuery(
		name="UserDownloadsCount.countByUserAndBook", 
		query="SELECT c.downloadsCount FROM UserDownloadsCount AS c WHERE c.userAccountId = :userAccountId AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.incrementAnonymousByIpAndBook", 
		query="UPDATE UserDownloadsCount AS c SET c.downloadsCount=c.downloadsCount+1 WHERE c.ipAddress = :ipAddress AND c.userAccountId IS NULL AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.countAnonymousByIPAndBook", 
		query="SELECT c.downloadsCount FROM UserDownloadsCount AS c WHERE c.ipAddress = :ipAddress AND c.userAccountId IS NULL AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.countAnonymousByBook", 
		query="SELECT c.downloadsCount FROM UserDownloadsCount AS c WHERE c.userAccountId IS NULL AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.incrementByUserAndBook", 
		query="UPDATE UserDownloadsCount AS c SET c.downloadsCount=c.downloadsCount+1 WHERE c.userAccountId = :userAccountId AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.incrementByBook", 
		query="UPDATE UserDownloadsCount AS c SET c.downloadsCount=downloadsCount+1 WHERE c.userAccountId IS NULL AND c.bookId = :bookId"
	),
	@NamedQuery(
		name="UserDownloadsCount.deleteByPublishedBookId", 
		query="DELETE FROM UserDownloadsCount AS c WHERE c.bookId = :bookId"
	)
//	@NamedQuery(
//		name="UserDownloadsCount.incrementByIpAndBook", 
//		query="UPDATE UserDownloadsCount AS c SET c.downloadsCount=c.downloadsCount+1 WHERE c.ipAddress = :ipAddress AND c.bookId = :bookId"
//	)
})

@Table(name="userdownloadscount",schema="branchitup")
@Entity
public class UserDownloadsCount extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="downloadId")
	protected Long downloadId;
	
	@Column(name="bookId")
	protected Long bookId;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@Column(name="downloadsCount")
	protected Integer downloadsCount;
	
	@Column(name="ipAddress")
	protected String ipAddress;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}

	public Integer getDownloadsCount() {
		return downloadsCount;
	}

	public void setDownloadsCount(Integer downloadsCount) {
		this.downloadsCount = downloadsCount;
	}

	public Long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(Long downloadId) {
		this.downloadId = downloadId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}
}
