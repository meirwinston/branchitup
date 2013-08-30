package com.branchitup.export;

import java.io.File;

import com.branchitup.persistence.AttachmentFileType;

public class EbookAttachmentCarrier {
	public File file;
	public AttachmentFileType type;

	public EbookAttachmentCarrier(File file, AttachmentFileType type){
		this.file = file;
		this.type = type;
	}
}
