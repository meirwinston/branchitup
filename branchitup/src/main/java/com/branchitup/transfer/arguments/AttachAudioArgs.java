package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;

public class AttachAudioArgs extends BaseArgs{
	public FileItem fileItem;
	public String fileName;
	public String sessionAttributeId;
	public String description;
	public String ipAddress;
	public Long userAccountId;
	public Long bookId;
	public Integer trackLength;
	public Integer sampleRate;
	public Long bitRate;
	public String channels;
	public String format;
	
}
