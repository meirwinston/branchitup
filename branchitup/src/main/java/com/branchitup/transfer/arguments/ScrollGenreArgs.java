package com.branchitup.transfer.arguments;

import com.branchitup.transfer.arguments.ScrollPublicationsArgs.ConditionKey;

public class ScrollGenreArgs extends BaseArgs{
//	public String phrase;
	public long offset;
	public int maxResults;
	public ConditionKey conditionKey;
	public String conditionValue;
	
	public ScrollGenreArgs(){}
//	public ScrollGenreArgs(String phrase,long offset,int maxResults){
//		this.phrase = phrase;
//		this.offset = offset;
//		this.maxResults = maxResults;
//	}
}
