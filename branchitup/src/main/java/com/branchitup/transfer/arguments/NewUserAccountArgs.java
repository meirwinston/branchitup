package com.branchitup.transfer.arguments;

public class NewUserAccountArgs extends BaseArgs{
	public String password;
	public String passwordConfirm;
	public String firstName;
	public String middleName;
	public String lastName;
	public String email;
	public String createdByIP;
	public Float gender;
	public Long profileImageFileId;
	public String challenge;
	
	public NewUserAccountArgs(){
	}
//	public NewUserAccountArgs(Map<String,String> map){
//		super(map);
//	}
	
	

	@Override
	public String toString() {
		return "NewUserAccountArgs [password=" + password
				+ ", passwordConfirm=" + passwordConfirm + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", email=" + email + ", createdByIP="
				+ createdByIP + ", gender=" + gender + ", profileImageFileId="
				+ profileImageFileId + ", challenge=" + challenge + "]";
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedByIP() {
		return createdByIP;
	}

	public void setCreatedByIP(String createdByIP) {
		this.createdByIP = createdByIP;
	}

	public Float getGender() {
		return gender;
	}

	public void setGender(Float gender) {
		this.gender = gender;
	}

	public Long getProfileImageFileId() {
		return profileImageFileId;
	}

	public void setProfileImageFileId(Long profileImageFileId) {
		this.profileImageFileId = profileImageFileId;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
