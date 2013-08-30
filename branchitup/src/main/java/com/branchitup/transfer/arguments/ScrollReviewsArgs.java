package com.branchitup.transfer.arguments;

public class ScrollReviewsArgs extends BaseArgs{
	public long offset;
	public int maxResults;
	public long bookId;
	
	public ScrollReviewsArgs(){}
	public ScrollReviewsArgs(long bookId,long offset,int maxResults){
		this.bookId = bookId;
		this.offset = offset;
		this.maxResults = maxResults;
	}
	@Override
	public String toString() {
		return "BrowseReviewsArgs [offset=" + offset + ", maxResults="
				+ maxResults + ", bookId=" + bookId + "]";
	}
	
	
}
