package com.branchitup.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.branchitup.json.JacksonUtils;

public class JsonTest {
	public static void main(String[] args) throws IOException{
		Map<String,String> m = new HashMap<String,String>();
		m.put("key1","value1");
		m.put("key2","value2");
		System.out.println(JacksonUtils.serialize(m));
	}
}
