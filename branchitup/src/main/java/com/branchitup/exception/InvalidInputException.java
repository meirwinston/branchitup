package com.branchitup.exception;

import java.util.Map;

public class InvalidInputException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public String requestUri;
	public Map<String,String> messagesMap;
	
	public InvalidInputException(Map<String,String> messagesMap){
		this.messagesMap = messagesMap;
	}
	
	public InvalidInputException(Throwable t){
		super(t);
	}
	public InvalidInputException(String message,Throwable cause) {
		super(message,cause);
	}
	public InvalidInputException (String msg) {
		super(msg);
	}
	public InvalidInputException (String msg,String requestUri) {
		super(msg);
		this.requestUri = requestUri;
	}
}
