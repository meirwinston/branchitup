package com.branchitup.transfer.arguments;

public class ScrollSheetsArgs extends BaseArgs{
	public String owner;
//	public Integer permissionsMask;
	public long offset;
	public int maxResults;
	public String phrase;
	public boolean privateOnly;
	public boolean withAssociatedBooks = false;
	@Override
	public String toString() {
		return "BrowseSheetsArgs [owner=" + owner + ", offset=" + offset
				+ ", maxResults=" + maxResults + ", phrase=" + phrase
				+ ", privateOnly=" + privateOnly + ", withAssociatedBooks="
				+ withAssociatedBooks + "]";
	}
	
	
}
