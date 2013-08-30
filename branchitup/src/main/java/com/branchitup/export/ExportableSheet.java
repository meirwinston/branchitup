package com.branchitup.export;

import com.branchitup.persistence.entities.PublishedSheet;
import com.branchitup.persistence.entities.Sheet;

public class ExportableSheet {
	public String name;
	public String content;
	public String cssText;
	
	public ExportableSheet(){}
	
	public ExportableSheet(Sheet sheet){
		this.name = sheet.getName();
		this.content = sheet.getContent();
		this.cssText = sheet.getCssText();
	}
	
	public ExportableSheet(PublishedSheet sheet){
		this.name = sheet.getName();
		this.content = sheet.getContent();
		this.cssText = sheet.getCssText();
		
	}
}
