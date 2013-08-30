package com.branchitup.json;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import com.branchitup.persistence.ScrollResult;

public class ScrollResultsSerializer extends JsonSerializer<ScrollResult>{
	@Override
	public void serialize(ScrollResult value, JsonGenerator generator, SerializerProvider provider) 
	throws IOException,JsonProcessingException {
		generator.writeStartObject();
		
		generator.writeNumberField("count", value.count);
		generator.writeObjectField("list", value.list);
		
		generator.writeEndObject();
	}
	
	@Override
	public Class<ScrollResult> handledType() {
		return ScrollResult.class; 
	}
}
