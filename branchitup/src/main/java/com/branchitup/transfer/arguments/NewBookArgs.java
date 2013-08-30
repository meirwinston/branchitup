package com.branchitup.transfer.arguments;


public class NewBookArgs extends BaseArgs{
//{"title":"MyBook1","bookSummary":"my book 1","deficiencyMask":0,"publisherRoleMask":0,"fileItemId":"2142921a-dad2-4627-8b7c-c77ea95d54b1","sheetIds":[]}
	public String title;
	public String bookSummary;
	public long[] sheetIds;
	public Long userAccountId;
	public String createdByIP;
}
