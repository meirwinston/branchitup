package com.branchitup.transfer.arguments;

public class ScrollBookShowcaseArgs extends BaseArgs{
	public long offset;
	public int maxResults;
	
	public ScrollBookShowcaseArgs(){}
	
	public ScrollBookShowcaseArgs(long offset,int maxResults){
		this.offset = offset;
		this.maxResults = maxResults;
	}
}
