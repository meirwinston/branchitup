package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SheetContent extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sheetId")
	protected Long sheetId;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="content")
	protected String content;
	
	@Column(name="cssText")
	protected String cssText;

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCssText() {
		return cssText;
	}

	public void setCssText(String cssText) {
		this.cssText = cssText;
	}	
}