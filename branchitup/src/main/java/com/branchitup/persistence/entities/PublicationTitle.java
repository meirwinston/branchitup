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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//	    name="PublicationTitle.select(phrase,offset,maxResults)",
//	    resultClass=PublicationTitle.class,
//	    query="call `publishedbooks.selectTitleByPhrase`(?phrase,?offset,?maxResults)"
////	    query="SELECT p.bookId,p.title,p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, p.coverImageFileId,p.publisherUserName, u.firstName AS publisherFirstName, u.lastName AS publisherLastName FROM publishedbooks AS p INNER JOIN useraccounts AS u ON p.publisherUserName = u.userName ORDER BY p.createdOn DESC LIMIT ?startPosition,?maxResults"
//	)
////	@NamedNativeQuery(
////	    name="PublicationRecord.select(bookId)",
////	    query="SELECT p.bookId,p.title,p.bookSummary,p.publisherUserName,p.publisherAccount.firstName, p.publisherAccount.lastName,p.createdOn,p.publisherRoleMask,p.deficiencyMask, p.coverImageFileId FROM PublishedBook AS p WHERE p.bookId = :bookId"
////	)
//})
@Entity
public class PublicationTitle {

	@Id
	@Column(name="bookId")
	public Long bookId;
	
	@Column(name="title")
	public String title;
	
	@Column(name="coverImageFileId")
	public Long coverImageFileId;
}