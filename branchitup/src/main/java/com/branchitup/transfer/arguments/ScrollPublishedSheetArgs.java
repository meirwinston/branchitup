package com.branchitup.transfer.arguments;

public class ScrollPublishedSheetArgs extends BaseArgs{
	public Long bookId;
	public Integer offset;
	public Integer maxResults;
	
	@Override
	public String toString() {
		return "BrowsePublishedSheetArgs [bookId=" + bookId + ", offset="
				+ offset + ", maxResults=" + maxResults + "]";
	}
}
