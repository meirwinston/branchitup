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
package com.branchitup.system;

public class YBlobSystemException extends Exception{
	static final long serialVersionUID = 87756242111l;
	protected String title = this.getClass().getSimpleName();
	
	public YBlobSystemException(){
	}
	
	public YBlobSystemException(Throwable t){
		super(t);
	}
	
	public YBlobSystemException(String message,Throwable cause) {
		super(message,cause);
	}

	public YBlobSystemException (String msg) {
		super(msg);
	}
	
	public YBlobSystemException(Throwable t,String title){
		super(t);
		this.title = title;
	}
	
	public YBlobSystemException(String message,Throwable cause,String title) {
		super(message,cause);
		this.title = title;
	}

	public YBlobSystemException (String msg,String title) {
		super(msg);
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}