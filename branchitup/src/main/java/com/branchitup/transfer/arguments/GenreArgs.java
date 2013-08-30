package com.branchitup.transfer.arguments;

import org.apache.commons.fileupload.FileItem;

public class GenreArgs extends BaseArgs{
	public Long userAccountId;
	public FileItem imageItem;
	public long genreId;
	public String modifiedByIP;
	public String name;
	public String description;
	
	public GenreArgs(){}
	
//	public GenreArgs(Map<String,String> map){
//		super(map);
//	}
}
