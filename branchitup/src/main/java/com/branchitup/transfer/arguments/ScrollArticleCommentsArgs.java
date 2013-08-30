package com.branchitup.transfer.arguments;

public class ScrollArticleCommentsArgs extends BaseArgs{
	public long offset;
	public int maxResults;
	public long sheetId;
	
	public ScrollArticleCommentsArgs(){}
	public ScrollArticleCommentsArgs(long sheetId,long offset,int maxResults){
		this.sheetId = sheetId;
		this.offset = offset;
		this.maxResults = maxResults;
	}
}