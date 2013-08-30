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

public class UnauthorizedException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public String requestUri;
	
	public UnauthorizedException(Throwable t){
		super(t);
	}
	public UnauthorizedException(String message,Throwable cause) {
		super(message,cause);
	}
	public UnauthorizedException (String msg) {
		super(msg);
	}
	public UnauthorizedException (String msg,String requestUri) {
		super(msg);
		this.requestUri = requestUri;
	}
//	public UnothorizedException(Throwable t,String title){
//		super(t,title);
//	}
//	public UnothorizedException(String message,Throwable cause,String title) {
//		super(message,cause,title);
//	}
//	public UnothorizedException(String msg,String title) {
//		super(msg,title);
//	}
}