package com.branchitup.servlet.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.view.AbstractView;

//NOT IN USE
@Deprecated
public class JSONView extends AbstractView{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
	HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.getWriter().print("TEST custom view");
		System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHh");
		
	}
	static class H{
		public String p1 = "aaa";
		public String p2 = "bbbb";
	}
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException{
		org.codehaus.jackson.map.ObjectMapper o = new ObjectMapper();
		Map<String,String> m = new HashMap<String,String>();
		m.put("key1", "val1");
		
		
		System.out.println(o.writeValueAsString(new H()) + " --- " + o.convertValue("{a: 'ffff', b: 'ggg'}", java.util.Hashtable.class));
	}

}
