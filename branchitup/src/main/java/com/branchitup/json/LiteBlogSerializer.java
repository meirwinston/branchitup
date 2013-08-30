package com.branchitup.json;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import com.branchitup.persistence.entities.Blog;

public class LiteBlogSerializer extends JsonSerializer<Blog>{
	@Override
	public void serialize(Blog blog, JsonGenerator generator, SerializerProvider provider) 
	throws IOException,JsonProcessingException {
		generator.writeStartObject();
		
		generator.writeNumberField("blogId", blog.getBlogId());
		generator.writeStringField("name", blog.getName());
		
		generator.writeEndObject();
	}
	
	@Override
	public Class<Blog> handledType() {
		return Blog.class; 
	}

}
