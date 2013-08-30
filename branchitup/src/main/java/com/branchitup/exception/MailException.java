package com.branchitup.exception;

public class MailException extends YBlobException{
	private static final long serialVersionUID = 1L;
	
	public MailException(Throwable t){
		super(t);
	}
	public MailException(String message,Throwable cause) {
		super(message,cause);
	}
	public MailException (String msg) {
		super(msg);
	}
	public MailException(Throwable t,String title){
		super(t,title);
	}
	public MailException(String message,Throwable cause,String title) {
		super(message,cause,title);
	}
	public MailException(String msg,String title) {
		super(msg,title);
	}
}