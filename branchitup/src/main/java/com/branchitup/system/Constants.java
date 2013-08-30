/*
 * branchitup PROPRIETARY/CONFIDENTIAL.
 * 
 * branchitup Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of branchitup.  This 
 * confidential information is owned by branchitup, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.system;

import org.apache.log4j.Logger;


public interface Constants {
//	public static final String branchitup_ROOTDIR = "E:\\branchitupenv";
	String BRANCHITUP_ROOTDIR = "/branchitup-env";
//	String BRANCHITUP_ROOTDIR = "/data/branchitupenv/"; //jupiter laptop server
	
	Logger LOGGER = Logger.getLogger("branchitup");
	String PROPS_FILE_NAME = "branchitup.props";
	String SYSTEM_EMAIL_USERNAME = "mail@branchitup.com";
	String SYSTEM_USERNAME = "BranchItUp";
	
	long PUBLISH_INTERVAL = 1000;
	
	//view,edit,execute (no need)
	//view, join-to-book, edit/change
	interface SheetPermission{
		int PUBLIC_VIEW = 64; //001 000 000
		int PUBLIC_JOIN = 128;  //010 000 000
		int PUBLIC_EDIT = 256;  //100 000 000
		
		int GROUP_VIEW = 8;//000 001 000
		int GROUP_JOIN = 16;//000 010 000
		int GROUP_EDIT = 32;//000 100 000
		
		int PUBLIC_GROUP_VIEW = PUBLIC_VIEW | GROUP_VIEW; //001 001 000 
		int PUBLIC_GROUP_JOIN = PUBLIC_JOIN | GROUP_JOIN; //010 010 000
		int PUBLIC_GROUP_EDIT = PUBLIC_EDIT | GROUP_EDIT; //100 100 000
		int PUBLIC_GROUP_VIEW_JOIN = PUBLIC_GROUP_VIEW | PUBLIC_GROUP_JOIN;
		int PUBLIC_GROUP_VIEW_JOIN_EDIT = PUBLIC_GROUP_VIEW | PUBLIC_GROUP_JOIN | PUBLIC_GROUP_EDIT;
	}
	
	interface Views{
		String JSON_VIEW = "jsonView"; //has to match the tag on servlet-context.xml
		String IMAGE_VIEW = "imageView";
	}
	
	enum TimerCommand{
		GENERATE_PDF_ATTACHMENT
	}
	
	interface SessionAttributeKey{
		String USER_ACCOUNT = "USER_ACCOUNT";
		String MOST_RECENT_URL = "MOST_RECENT_URL";
	}
	
	public enum UploadTarget {
		SESSION,
		UPLOADS,
		GENRES,
		PROFILE,
		CAPTCHA
	}
}
