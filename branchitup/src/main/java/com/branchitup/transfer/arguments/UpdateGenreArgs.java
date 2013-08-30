package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;

public class UpdateGenreArgs extends BaseArgs{
	public Long genreId;
	public String name;
	public String description;
	public String fileItemId;
	public FileItem imageFileItem;
	public Long userAccountId;
	public String ipAddress;
	@Override
	public String toString() {
		return "UpdateGenreArgs [genreId=" + genreId + ", name=" + name
				+ ", description=" + description + ", fileItemId=" + fileItemId
				+ ", imageFileItem=" + imageFileItem + ", userAccountId="
				+ userAccountId + ", ipAddress=" + ipAddress + "]";
	}
	
	
}
