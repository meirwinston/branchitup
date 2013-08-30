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
package com.branchitup.transfer;

public class Mail{
	
	public String toEmailAddress;
	public String subject;
	public String content;
	public String personalName; //full name, this name shows up in the "To" field of the mail
	public String type = "text/plain";
}
