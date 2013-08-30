package com.branchitup.exception;

public class LookupException extends YBlobException{
	private static final long serialVersionUID = 1L;
	
	public LookupException(Throwable t){
		super(t);
	}
	public LookupException(String message,Throwable cause) {
		super(message,cause);
	}
	public LookupException (String msg) {
		super(msg);
	}
	public LookupException(Throwable t,String title){
		super(t,title);
	}
	public LookupException(String message,Throwable cause,String title) {
		super(message,cause,title);
	}
	public LookupException(String msg,String title) {
		super(msg,title);
	}
}