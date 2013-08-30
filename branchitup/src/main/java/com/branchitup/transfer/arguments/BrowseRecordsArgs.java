package com.branchitup.transfer.arguments;

public class BrowseRecordsArgs extends BaseArgs{
	public String owner;
	public Integer permissionsMask;
	public long offset;
	public int maxResults;
	
	public BrowseRecordsArgs(){}
	public BrowseRecordsArgs(String owner,Integer permissionsMask,long offset,int maxResults){
		this.owner = owner;
		this.permissionsMask = permissionsMask;
		this.offset = offset;
		this.maxResults = maxResults;
	}
	
	public BrowseRecordsArgs(String owner,long offset,int maxResults){
		this.owner = owner;
		this.offset = offset;
		this.maxResults = maxResults;
	}
}
