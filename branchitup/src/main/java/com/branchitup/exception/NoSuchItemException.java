package com.branchitup.exception;

public class NoSuchItemException extends Exception{
	private static final long serialVersionUID = 1L;
	private String title = this.getClass().getSimpleName();
	
	public NoSuchItemException(){
	}
	
	public NoSuchItemException(Throwable t){
		super(t);
	}
	
	public NoSuchItemException(String message,Throwable cause) {
		super(message,cause);
	}

	public NoSuchItemException (String msg) {
		super(msg);
	}
	
	public NoSuchItemException(Throwable t,String title){
		super(t);
		this.title = title;
	}
	
	public NoSuchItemException(String message,Throwable cause,String title) {
		super(message,cause);
		this.title = title;
	}

	public NoSuchItemException(String msg,String title) {
		super(msg);
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
