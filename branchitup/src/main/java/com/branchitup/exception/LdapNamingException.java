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

public class LdapNamingException extends YBlobException{
	private static final long serialVersionUID = 1L;
	
	public LdapNamingException(Throwable t){
		super(t);
	}
	public LdapNamingException(String message,Throwable cause) {
		super(message,cause);
	}
	public LdapNamingException (String msg) {
		super(msg);
	}
	public LdapNamingException(Throwable t,String title){
		super(t,title);
	}
	public LdapNamingException(String message,Throwable cause,String title) {
		super(message,cause,title);
	}
	public LdapNamingException(String msg,String title) {
		super(msg,title);
	}
}