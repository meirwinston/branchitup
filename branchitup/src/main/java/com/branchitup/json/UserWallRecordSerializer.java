package com.branchitup.json;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.UserWallRecord;

public class UserWallRecordSerializer extends SerializerBase<UserWallRecord>{
	public UserWallRecordSerializer(){
		super(UserWallRecord.class,true);
	}
	@Override
	public void serialize(UserWallRecord item, JsonGenerator gen,SerializerProvider p) 
	throws IOException,JsonProcessingException {
		gen.writeStartObject();
		
		gen.writeNumberField("recordId", item.getRecordId());
		gen.writeNumberField("senderAccountId", item.getSenderAccountId());
		gen.writeNumberField("userAccountId", item.getUserAccountId());
		gen.writeStringField("message", item.getMessage());
		gen.writeStringField("senderFirstName", item.getSenderAccount().getFirstName());
		gen.writeStringField("senderLastName", item.getSenderAccount().getLastName());
		gen.writeNumberField("createdOn", item.getCreatedOn().getTime());
		
		String profileImageUrl = StatelessDAO.resolveThumbnailUrl(item.getSenderAccount().getProfileImageFile());
		if(profileImageUrl != null){
			gen.writeStringField("senderProfileImageUrl", profileImageUrl);
		}
		
		
		gen.writeEndObject();
	}
}
