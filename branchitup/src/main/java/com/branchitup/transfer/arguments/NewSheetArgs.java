package com.branchitup.transfer.arguments;

public class NewSheetArgs extends BaseArgs{
	public Long userAccountId;
	public String content;
	public String cssText;
	public String createdByIP;
	public String name;
	public Boolean allowEditing;
	public String visibility;
	public String folderName;
//	public Long derivedFromId;
	
	public NewSheetArgs(){
	}
	
//	public NewSheetArgs(JsonNode jnode){
//		super(jnode);
//	}

	@Override
	public String toString() {
		return "NewSheetArgs [userAccountId=" + userAccountId + ", content="
				+ content + ", cssText=" + cssText + ", createdByIP="
				+ createdByIP + ", name=" + name + ", allowEditing="
				+ allowEditing + ", visibility=" + visibility
				+ ",folderName="
				+ folderName + "]";
	}
	
//	public Sheet newSheet(){
//		Sheet sheet = new Sheet();
//		sheet.setContent(this.content);
//		sheet.setCssText(this.cssText);
//		sheet.setCreatedByIP(this.createdByIP);
//		sheet.setName(this.name);
//		sheet.setOwnerAccountId(this.userAccountId);
//		sheet.setGenreId(this.genreId);
//		sheet.setDerivedFromId(this.derivedFromId);
//		sheet.setLanguage(Language.valueOf(this.language));
//		sheet.setCreatedOn(this.createdOn);
//		sheet.setPermissionsMask(this.permissionsMask);
//		sheet.setFolderName(this.folderName);
//		
//		return sheet;
//	}
	
	
}
