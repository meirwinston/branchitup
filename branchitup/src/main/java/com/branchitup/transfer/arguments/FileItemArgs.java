package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;
import com.branchitup.persistence.entities.ImageFile;

public class FileItemArgs extends BaseArgs{
	public FileItem fileItem;
	public Long userAccountId;
	public ImageFile.SystemFolder systemFolder;
	public boolean generateThumbnail = false;
	public String album;
}
