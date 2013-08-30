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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.branchitup.json.UserAccountSerializer;
import com.branchitup.persistence.UserItemStatus;

@NamedQueries({
	@NamedQuery(
	    name="UserAccount.selectPassword",
	    query="SELECT ua.password FROM UserAccount AS ua WHERE ua.userAccountId = :userAccountId"
	),
	@NamedQuery( //NEW
	    name="UserAccount.selectByEmail",
	    query="SELECT u FROM UserAccount as u WHERE u.email = :email"
	),
	@NamedQuery( //NEW
	    name="UserAccount.selectEmail",
	    query="SELECT u.email FROM UserAccount as u WHERE u.userAccountId = :userAccountId"
	),
	@NamedQuery( //NEW
	    name="UserAccount.updateVisibility",
	    query="UPDATE UserAccount AS ua SET ua.visibility = :visibility WHERE ua.userAccountId = :userAccountId"
	),
	@NamedQuery( //NEW
	    name="UserAccount.updateAboutMe",
	    query="UPDATE UserAccount AS ua SET ua.aboutMe = :aboutMe WHERE ua.userAccountId = :userAccountId"
	),
	@NamedQuery( //NEW
	    name="UserAccount.select",
	    query="SELECT u FROM UserAccount as u"
	),
	@NamedQuery( //^^p
		name="UserAccount.updatePassword",
		query="UPDATE UserAccount AS ua " +
		"SET ua.password = :password " +
		"WHERE ua.userAccountId = :userAccountId"
	)
})
@Table(name="useraccounts",schema="branchitup")
@Entity
@Access(AccessType.FIELD)
@JsonSerialize(using=UserAccountSerializer.class)
public class UserAccount extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public interface Group{
		String USER = "ROLE_USER";
		String ADMIN = "admin";
	}
	
	public enum Visibility{
		HIDE_CONTACT_INFORMATION,
		PUBLIC,
		PRIVATE
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="userAccountId")
	protected Long userAccountId;
	
//	@ManyToMany(fetch=FetchType.LAZY)
//	@JoinTable(
//		name="useraccount_usergroup",
//		joinColumns={
//			@JoinColumn(name="userAccountId",referencedColumnName="userAccountId")
//		},
//		inverseJoinColumns={
//			@JoinColumn(name="userGroupId",referencedColumnName="userGroupId")	
//		}
//	)
//	protected List<UserGroup> userGroups;
	
	@Column(name="username")
	protected String username;
	
	@Column(name="password")
	protected String password;
	
	@Column(name="groupName")
	protected String groupName;
	
	@Column(name="firstName")
	protected String firstName;
	
	@Column(name="middleName")
	protected String middleName;
	
	@Column(name="lastName")
	protected String lastName;
	
	@Column(name="createdOn")
	protected Timestamp createdOn;
	
	@Column(name="email")
	protected String email;
	
	@Column(name="createdByIP")
	protected String createdByIP;
	
	@Column(name="modifiedByIP")
	protected String modifiedByIP;
	
	@Column(name="gender")
	protected Float gender; //0 complete feminine 1 complete masculine
	
	@Column(name="profileImageFileId")
	protected Long profileImageFileId;
	
	@JoinColumn(name="profileImageFileId",referencedColumnName="imageFileId", updatable=false, insertable=false)
	@OneToOne(fetch=FetchType.LAZY)
	protected ImageFile profileImageFile;
	
	@Enumerated(EnumType.STRING)
	@Column(name="visibility")
	protected Visibility visibility;
	
	@Column(name="aboutMe")
	protected String aboutMe;
	
	@Transient
	protected String profileImageUrl;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	protected UserItemStatus status;
	
	@Column(name="lastLogIn")
	protected Date lastLogIn;
	
	@Column(name="loginCount")
	protected Integer loginCount;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="userAccount")
	protected List<Blog> blogs;
	
	//^^1
//	@Column(name="resetPasswordToken")
//	protected String resetPasswordToken;
//	
	//^^1
//	@Column(name="resetPasswordTokenExpires")
//	protected Date resetPasswordTokenExpires;
	
	
//	protected String personalWebsite;
//	protected String lifeStory;
//	protected String hometown;
//	protected String favoriteAuthors;
//	Favourite Stories
	
	
	public UserAccount(){}
	
	

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getCreatedByIP() {
		return createdByIP;
	}

	public void setCreatedByIP(String createdByIP) {
		this.createdByIP = createdByIP;
	}

	public String getModifiedByIP() {
		return modifiedByIP;
	}

	public void setModifiedByIP(String modifiedByIP) {
		this.modifiedByIP = modifiedByIP;
	}

	public Float getGender() {
		return gender;
	}

	public void setGender(Float gender) {
		this.gender = gender;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Long getProfileImageFileId() {
		return profileImageFileId;
	}

	public void setProfileImageFileId(Long profileImageFileId) {
		this.profileImageFileId = profileImageFileId;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	
	public ImageFile getProfileImageFile() {
		return profileImageFile;
	}

	public void setProfileImageFile(ImageFile profileImageFile) {
		this.profileImageFile = profileImageFile;
	}
	
	public UserItemStatus getStatus() {
		return status;
	}

	public void setStatus(UserItemStatus status) {
		this.status = status;
	}
	
	//^^1
//	public String getResetPasswordToken() {
//		return resetPasswordToken;
//	}
//	//^^1
//	public void setResetPasswordToken(String resetPasswordToken) {
//		this.resetPasswordToken = resetPasswordToken;
//	}
//	//^^1
//	public Date getResetPasswordTokenExpires() {
//		return resetPasswordTokenExpires;
//	}
//	//^^1
//	public void setResetPasswordTokenExpires(Date resetPasswordTokenExpires) {
//		this.resetPasswordTokenExpires = resetPasswordTokenExpires;
//	}

	public Date getLastLogIn() {
		return lastLogIn;
	}



	public void setLastLogIn(Date lastLogIn) {
		this.lastLogIn = lastLogIn;
	}



	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}



	@Override
	public String toString() {
		return "UserAccount [userAccountId=" + userAccountId + ", username="
				+ username + ", password=" + password + ", groupName="
				+ groupName + ", firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", createdOn="
				+ createdOn + ", email=" + email + ", createdByIP="
				+ createdByIP + ", modifiedByIP=" + modifiedByIP + ", gender="
				+ gender + ", profileImageFileId=" + profileImageFileId
				+ ", profileImageUrl=" + profileImageUrl + "]";
	}
}
