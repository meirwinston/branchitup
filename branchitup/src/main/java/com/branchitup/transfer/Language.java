package com.branchitup.transfer;

import java.io.IOException;
import java.io.Serializable;

import com.branchitup.json.JacksonUtils;

@Deprecated //use simple map
public class Language implements Serializable{
	static final long serialVersionUID = 1l;
	
	public String threeCharCode;
	public String name;
	
	public Language(){}
	public Language(String threeCharCode, String name){
		this.threeCharCode = threeCharCode;
		this.name = name;
	}
	@Override
	public String toString() {
		try {
			return JacksonUtils.serialize(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
