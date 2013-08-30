package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

//@NamedNativeQueries({
//	@NamedNativeQuery(
//	    name="UserAccountRecord.select(bookId)",
//	    resultClass=UserAccountRecord.class,
//	    query="SELECT DISTINCT u.userName,u.firstName,u.lastName FROM useraccounts AS u INNER JOIN books AS b ON b.owner = u.userName WHERE b.bookId = ?bookId;"
//	),
//	@NamedNativeQuery(
//	    name="UserAccountRecord.selectSheetOwners",
//	    resultClass=UserAccountRecord.class,
//	    query="CALL `branchitup`.`useracconts.selectSheetOwners`(?publishedBookId)"
//	)
//})
//@NamedQuery(
//	    name="UserAccount.selectSheetOwnersByPublicationId",
//	    query="SELECT DISTINCT ps.ownerAccountId AS userName, ua.firstName, ua.lastName FROM PublishedSheet AS ps INNER JOIN ps.userAccount AS ua WHERE ps.bookId = :publicationId"
//	),
@NamedQueries({
	@NamedQuery(
		name="UserAccount.selectSheetOwnersByPublicationId",
		query="SELECT DISTINCT new UserName(ps.ownerAccountId, ua.firstName, ua.lastName) FROM PublishedSheet AS ps INNER JOIN ps.userAccount AS ua WHERE ps.bookId = :publicationId"
	)
})
@Entity
public class UserName extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="userAccountId")
	public Long userAccountId;
	
	@Column(name="firstName")
	public String firstName;
	
	@Column(name="lastName")
	public String lastName;
	
	public UserName(){
	}
	
	public UserName(Long userAccountId, String firstName, String lastName){
		this.userAccountId = userAccountId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
