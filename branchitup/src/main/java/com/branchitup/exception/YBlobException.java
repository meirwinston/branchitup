/*
 * %W% %E%
 *
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.exception;

public class YBlobException extends Exception{
	private static final long serialVersionUID = 1L;
	private String title = this.getClass().getSimpleName();
	
	public YBlobException(){
	}
	
	public YBlobException(Throwable t){
		super(t);
	}
	
	public YBlobException(String message,Throwable cause) {
		super(message,cause);
	}

	public YBlobException (String msg) {
		super(msg);
	}
	
	public YBlobException(Throwable t,String title){
		super(t);
		this.title = title;
	}
	
	public YBlobException(String message,Throwable cause,String title) {
		super(message,cause);
		this.title = title;
	}

	public YBlobException(String msg,String title) {
		super(msg);
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
