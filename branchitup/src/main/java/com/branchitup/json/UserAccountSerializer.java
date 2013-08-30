package com.branchitup.json;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.branchitup.persistence.entities.UserAccount;

public class UserAccountSerializer extends JsonSerializer<UserAccount>{

	@Override
	public void serialize(UserAccount userAccount, JsonGenerator generator, SerializerProvider provider) 
	throws IOException,JsonProcessingException {
		generator.writeStartObject();
		
		generator.writeNumberField("userAccountId", userAccount.getUserAccountId());
		if(StringUtils.isNotEmpty(userAccount.getFirstName()) && StringUtils.isNotEmpty(userAccount.getLastName())){
			generator.writeStringField("fullName", userAccount.getFirstName() + " " + userAccount.getLastName());
		}
		if(StringUtils.isNotEmpty(userAccount.getFirstName())){
			generator.writeStringField("firstName", userAccount.getFirstName());
		}
		if(StringUtils.isNotEmpty(userAccount.getMiddleName())){
			generator.writeStringField("middleName", userAccount.getMiddleName());
		}
		if(StringUtils.isNotEmpty(userAccount.getLastName())){
			generator.writeStringField("lastName", userAccount.getLastName());
		}
		if(StringUtils.isNotEmpty(userAccount.getEmail())){
			generator.writeStringField("email", userAccount.getEmail());
		}
		if(StringUtils.isNotEmpty(userAccount.getAboutMe())){
			generator.writeStringField("aboutMe", userAccount.getAboutMe());
		}
		if(StringUtils.isNotEmpty(userAccount.getProfileImageUrl())){
			generator.writeStringField("profileImageUrl", userAccount.getProfileImageUrl());
		}
		
		if(userAccount.getVisibility() != null){
			generator.writeStringField("visibility", userAccount.getVisibility().toString());
		}
		
		
		generator.writeEndObject();
	}
	
	@Override
	public Class<UserAccount> handledType() {
		return UserAccount.class;
	}

}
