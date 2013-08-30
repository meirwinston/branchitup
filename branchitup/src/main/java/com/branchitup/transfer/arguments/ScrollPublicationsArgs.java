package com.branchitup.transfer.arguments;

public class ScrollPublicationsArgs extends BaseArgs{
//	public String phrase;
	public long offset;
	public int maxResults;
	public ConditionKey conditionKey;
	public String conditionValue;
	
	public enum ConditionKey {
		BY_PUBLISHER,
		BY_GENRE,
//		BY_RECOMMENDED,
		BY_AUTHOR,
		BY_PHRASE;
	};
	
	public ScrollPublicationsArgs(){}
	
//	public ScrollPublicationsArgs(String phrase,long offset,int maxResults){
////		this.phrase = phrase;
//		this.offset = offset;
//		this.maxResults = maxResults;
//	}
}
