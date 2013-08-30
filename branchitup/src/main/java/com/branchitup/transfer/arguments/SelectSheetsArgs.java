package com.branchitup.transfer.arguments;

public class SelectSheetsArgs extends BaseArgs{
	public String phrase;
	public Long genreId;
	public String username;
	public Integer permissionsMask;
	
	@Override
	public String toString() {
		return "SelectSheetsArgs [phrase=" + phrase + ", genreId=" + genreId
				+ ", userName=" + username + "]";
	}
}
