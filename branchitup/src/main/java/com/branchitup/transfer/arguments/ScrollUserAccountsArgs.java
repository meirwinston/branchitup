package com.branchitup.transfer.arguments;

public class ScrollUserAccountsArgs extends BaseArgs{
	public int offset;
	public int maxResults;
	
	public ScrollUserAccountsArgs(){}
	
	public ScrollUserAccountsArgs(int offset,int maxResults){
		this.offset = offset;
		this.maxResults = maxResults;
	}
}
