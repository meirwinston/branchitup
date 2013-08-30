package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;

public class NewGenreArgs extends BaseArgs{
	public String name;
	public String description;
	public String fileItemId;
	public FileItem coverImageFileItem;
	public Long userAccountId;
	public String ipAddress;
}
