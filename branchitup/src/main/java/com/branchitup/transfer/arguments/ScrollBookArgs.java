package com.branchitup.transfer.arguments;

public class ScrollBookArgs extends BaseArgs{
	public String owner;
	public long offset;
	public int maxResults;
	
	public ScrollBookArgs(){}
	
	public ScrollBookArgs(String owner,long offset,int maxResults){
		this.owner = owner;
		this.offset = offset;
		this.maxResults = maxResults;
	}
}
