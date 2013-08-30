package com.branchitup.model;

import java.util.HashMap;

public class BaseModel extends HashMap<String,Object>{
	private static final long serialVersionUID = 1L;
	public BaseModel(){}
	
//	public BaseModel(Object o) throws JsonParseException, JsonMappingException, IOException{
//		super(JacksonUtils.objectToMap(o));
//	}
//	
//	public List<BaseModel> asList(List<Object> l) throws JsonParseException, JsonMappingException, IOException{
//		List<BaseModel> list = new ArrayList<BaseModel>();
//		for(Object o : l){
//			list.add(new BaseModel(o));
//		}
//		return list;
//	}
}
