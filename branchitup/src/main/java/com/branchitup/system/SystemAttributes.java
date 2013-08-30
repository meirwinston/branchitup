package com.branchitup.system;

import java.util.Map;

public class SystemAttributes {
	private static SystemAttributes instance;
	protected Map<String,String> languageMap;
	
	private SystemAttributes(){
		if(instance == null){
			instance = this;
		}
	}
	
	public static SystemAttributes getInstance(){
		return instance;
	}
	public void setLanguageMap(Map<String,String> map){
		this.languageMap = map;
	}
	public Map<String, String> getLanguageMap() {
		return languageMap;
	}

}
