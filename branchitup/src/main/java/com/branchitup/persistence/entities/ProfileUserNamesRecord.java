package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(
	    name="ProfileUserNamesRecord.select(username)",
	    query="SELECT new com.branchitup.persistence.entities.ProfileUserNamesRecord(ua.username,ua.firstName,ua.middleName,ua.lastName) FROM UserAccount AS ua WHERE ua.username = :username"
	)
})
@Entity
public class ProfileUserNamesRecord {
	
	@Id
	@Column(name="username")
	public String username;
	
	@Column(name="firstName")
	public String firstName;
	
	@Column(name="middleName")
	public String middleName;
	
	@Column(name="lastName")
	public String lastName;

	public ProfileUserNamesRecord(String username, String firstName,String middleName, String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}
	
	public ProfileUserNamesRecord(){}
}
