package com.branchitup.transfer.arguments;

public class UpdateBookArgs extends BaseArgs{
	public Long bookId;
	public String title;
	public String bookSummary;
//	public String fileItemId;
	public long[] sheetIds;
//	public FileItem coverImageFileItem;
	public String modifiedByIP;
	public Long userAccountId;
}
