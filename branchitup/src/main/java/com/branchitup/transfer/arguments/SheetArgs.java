package com.branchitup.transfer.arguments;

public class SheetArgs extends BaseArgs{
	public String owner;
	public Integer startPosition;
	public Integer maxResults;
	public String[] fields;
	public Integer permissionsMask;
	public boolean selectOwnerUser = false;
	public boolean selectGenre = false;
}
