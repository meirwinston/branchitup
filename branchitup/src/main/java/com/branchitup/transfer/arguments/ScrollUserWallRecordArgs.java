package com.branchitup.transfer.arguments;

public class ScrollUserWallRecordArgs extends BaseArgs{
	public long offset;
	public int maxResults;
	public Long userAccountId;
	
	public ScrollUserWallRecordArgs(){}
	public ScrollUserWallRecordArgs(Long userAccountId,long offset,int maxResults){
		this.userAccountId = userAccountId;
		this.offset = offset;
		this.maxResults = maxResults;
	}
}
