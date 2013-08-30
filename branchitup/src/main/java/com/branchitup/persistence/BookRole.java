/*
 * BranchItUp PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence;

public enum BookRole {
	WRITING(1),
	ILLUSTRATING(2),
	TRANSLATING(4),
	EDITING(8),
	PROOF_READING(16);
	
	public int maskVal = 0;
	
	private BookRole(int maskVal){
		this.maskVal = maskVal;
	}
}
