package com.branchitup.persistence.entities;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.branchitup.json.JacksonUtils;
import com.branchitup.system.Constants;

@NamedQueries({
	@NamedQuery(
		name="BookDetailsSheet.selectBySheetIdAndBookId",
		query="SELECT new BookDetailsSheet(sb.sequenceIndex, s.sheetId, s.name, s.permissionsMask, " +
				"s.ownerAccountId, ua.firstName, ua.lastName,s.blogId) FROM Sheet_Book AS sb " +
				"INNER JOIN sb.sheet AS s INNER JOIN sb.sheet.ownerAccount AS ua " +
				"WHERE sb.sheetId = :sheetId " +
				"AND sb.bookId = :bookId"
	),
	@NamedQuery(
		name="BookDetailsSheet.selectBySheetId",
		query="SELECT new BookDetailsSheet(s.sheetId, s.name, s.permissionsMask, s.ownerAccountId, " +
				"ua.firstName, ua.lastName,s.blogId) " +
				"FROM Sheet AS s " +
				"INNER JOIN s.ownerAccount AS ua " +
				"WHERE s.sheetId = :sheetId"
	),
	@NamedQuery(
		name="BookDetailsSheet.selectByBookId",
		query="SELECT new BookDetailsSheet(sb.sequenceIndex, s.sheetId, s.name, " +
				"s.permissionsMask, s.ownerAccountId, ua.firstName, ua.lastName,s.blogId) " +
				"FROM Sheet_Book AS sb " +
				"INNER JOIN sb.sheet AS s " +
				"INNER JOIN sb.sheet.ownerAccount AS ua " +
				"WHERE sb.bookId = :bookId " +
				"ORDER BY sb.sequenceIndex"
	)
})
@Entity
public class BookDetailsSheet {
	@Id
	@Column(name="sheetId")
	public Long sheetId;
	
	@Column(name="sequenceIndex")
	public Integer sequenceIndex;
	
	@Column(name="name")
	public String name;
	
	@Column(name="ownerFirstName")
	public String ownerFirstName;
	
	@Column(name="ownerLastName")
	public String ownerLastName;
	
	@Column(name="permissionsMask")
	public Integer permissionsMask;
	
	@Column(name="ownerAccountId")
	public Long ownerAccountId;
	
	@Column(name="blogId")
	public Long blogId;

	@Transient
	public Boolean allowEdit;
	
	@Transient
	public Boolean allowRemove;
	
	public BookDetailsSheet(){
		
	}
	
	public BookDetailsSheet(Integer sequenceIndex, Long sheetId, String name, Integer permissionsMask, 
	Long ownerAccountId, String firstName, String lastName,Long blogId){
		this.sequenceIndex = sequenceIndex;
		this.sheetId = sheetId;
		this.name = name;
		this.permissionsMask = permissionsMask;
		this.ownerAccountId = ownerAccountId;
		this.ownerFirstName = firstName;
		this.ownerLastName = lastName;
		this.blogId = blogId;
	}
	
	public BookDetailsSheet(Long sheetId, String name, Integer permissionsMask, Long ownerAccountId, 
	String firstName, String lastName, Long blogId){
		this(null,sheetId,name,permissionsMask,ownerAccountId,firstName,lastName,blogId);
	}
	
	public void evaluateAllowEdit(Long userAccountId){
		if(this.blogId != null){
			this.allowEdit = Boolean.FALSE;
			this.allowRemove = Boolean.FALSE;
		}
		else if(!this.ownerAccountId.equals(userAccountId)){
			this.allowRemove = Boolean.FALSE;
			if(this.permissionsMask.intValue() >= Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN_EDIT){
				this.allowEdit = Boolean.TRUE;
			}
			else{
				this.allowEdit = Boolean.FALSE;
			}
		}
		else{
			this.allowEdit = Boolean.TRUE;
			this.allowRemove = Boolean.TRUE;
		}
	}

	/**
	 * converts to JSON
	 */
	@Override
	public String toString() {
//		return "{sheetId: " + sheetId + ", sequenceIndex: "
//				+ sequenceIndex + ", name: '" + name + "'}";
		try {
			//this method is safer than concatenating string, if 'name' contains ' or " that could cause a problem
			return JacksonUtils.serialize(this);
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e.getMessage(),e);
//			e.printStackTrace();
			return null;
		}
	}
}
