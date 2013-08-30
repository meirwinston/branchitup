package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;

import com.branchitup.persistence.entities.ImageFile;

public class PublishBookArgs extends BaseArgs{
	public Long bookId;
	public Long publisherAccountId;
	public long[] genreIds;
	public String[] genreNewValues;
	public PublisherRoles publisherRoles;
	public Deficiency deficiency;
	public String publisherComment;
	public String translationLanguage;
	public String bookLanguage;
	public String publishedByIP;
	public Boolean allowBranching;
	public String coverUploadId; //session id of uploaded cover image
	public FileItem coverImageFileItem;
	public Long coverImageFileId; //image file ID if exists
	
	public ImageFile coverImageFile;
	
	public static class Deficiency{
		public Boolean illustrating;
		public Boolean translating;
		public Boolean coauthoring;
		public Boolean editing;
		public Boolean proofreading;
	}
	
	public static class PublisherRoles{
		public Boolean author;
		public Boolean illustrator;
		public Boolean translator;
		public Boolean editor;
		public Boolean proofreader;
	}
	
	public PublishBookArgs(){}
}
