package com.branchitup.persistence;

/*
 *   if($mimetype=="audio/mpeg"||
 *   $mimetype=="audio/x-mpeg-3"||
 *   $mimetype=="audio/mp3"||
 *   $mimetype=="audio/x-mpeg"||
 *   $mimetype=="audio/x-mp3"||
 *   $mimetype=="audio/mpeg3"||
 *   $mimetype=="audio/x-mpeg3"||
 *   $mimetype=="audio/mpg"||
 *   $mimetype=="audio/x-mpg"||
 *   $mimetype=="audio/x-mpegaudio")
        
 */
public enum AttachmentFileType {
	PDF("application/pdf","pdf"),
	EPUB("application/epub+zip","epub"),
	MP3("audio/mpeg","mp3"),
	WAV("audio/wav","wav"),
	STREAM("octet/stream",null);
	public String mimeType;
	public String fileExtension;
	AttachmentFileType(String mimeType,String fileExtension){
		this.mimeType = mimeType;
		this.fileExtension = fileExtension;
	}
}
