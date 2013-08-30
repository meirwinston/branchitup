/*
 * %W% %E%
 *
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
import javax.persistence.Id;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//	    name="PublisherRecord.select(publishedBookId)",
//	    resultClass=PublisherRecord.class,
//	    query="SELECT DISTINCT p.bookId,p.roleMask,p.userName,u.firstName,u.lastName FROM publishers AS p INNER JOIN useraccounts AS u ON p.userName = u.userName WHERE p.bookId = ?publishedBookId;"
//	)
//})
//@NamedQueries({
//	@NamedQuery(
//	    name="Publisher.select",
//	    query="select o from Publisher as o where o.userName = :userName and o.bookId = :bookId"
//	)
//})
//@IdClass(PublisherKey.class)
//@Entity
public class PublisherRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="bookId")
	public Long bookId;
	
	@Id
	@Column(name="username")
	public String username;
	
	@Column(name="firstName")
	public String firstName;
	
	@Column(name="lastName")
	public String lastName;
	
	@Column(name="roleMask")
	public Integer roleMask;

	@Override
	public String toString() {
		return "PublisherRecord [bookId=" + bookId + ", userName="
				+ username + ", firstName=" + firstName + ", lastName="
				+ lastName + ", roleMask=" + roleMask + "]";
	}
}
