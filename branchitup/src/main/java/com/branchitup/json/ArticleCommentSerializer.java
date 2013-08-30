package com.branchitup.json;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import com.branchitup.persistence.entities.ArticleComment;

public class ArticleCommentSerializer extends JsonSerializer<ArticleComment>{
	@Override
	public void serialize(ArticleComment comment, JsonGenerator generator, SerializerProvider provider) 
	throws IOException,JsonProcessingException {
		generator.writeStartObject();
		
		generator.writeNumberField("commentId", comment.getCommentId());
		if(comment.getUserAccount() != null){
			generator.writeStringField("fullName", comment.getUserAccount().getFirstName() + " " + comment.getUserAccount().getLastName());
		}
		generator.writeNumberField("createdOn", comment.getCreatedOn().getTime());
		generator.writeStringField("comment", comment.getComment());
		generator.writeEndObject();
	}

}
