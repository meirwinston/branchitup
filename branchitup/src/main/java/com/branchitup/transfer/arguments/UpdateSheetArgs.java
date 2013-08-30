package com.branchitup.transfer.arguments;

public class UpdateSheetArgs extends BaseArgs{
	public Long sheetId;
	public String content;
	public String cssText;
	public String name;
	public String modifiedByIP;
	public String visibility;
	public String folderName;
	public Boolean allowEditing;
	public Long userAccountId; //make sure the sheet really belongs to this account
}
