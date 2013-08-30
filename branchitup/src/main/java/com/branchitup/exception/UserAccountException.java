package com.branchitup.exception;

import java.util.Hashtable;
import java.util.Map;

public class UserAccountException extends YBlobException{
	private static final long serialVersionUID = 1L;
	public Map<String,String> messagesMap = new Hashtable<String, String>();
	public UserAccountException(Map<String,String> messageMap){
		this.messagesMap = messageMap;
	}
	
	public UserAccountException(String message,String title){
		messagesMap.put(title,message);
	}
}
