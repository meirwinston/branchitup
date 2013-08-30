package com.branchitup.transfer.arguments;

import java.util.List;

public class ScrollUserItemsArgs extends BaseArgs{
	public Long ownerAccountId;
	public long offset;
	public int maxResults;
	public String phrase;
	public List<String> types;
	public Integer permissionsMask;
	
	@Override
	public String toString() {
		return "ScrollUserItemsArgs [ownerAccountId=" + ownerAccountId
				+ ", offset=" + offset + ", maxResults=" + maxResults
				+ ", phrase=" + phrase + ", types=" + types + "]";
	}
	
	
}
