package com.branchitup.persistence.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


//@NamedNativeQueries({
//	@NamedNativeQuery(
//	    name="SheetIconRecord.select(phrase)",
//	    resultClass=SheetIconRecord.class,
//	    query="CALL `branchitup`.`sheetrecord.selectByPhrase`(?phrase,0,10)"
////	    query="SELECT s.name,s.sheetId,g.iconImageFileId AS genreImageFileId FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId WHERE s.name LIKE ?phrase ORDER BY s.name LIMIT 0,?maxResults"
//	),
//	@NamedNativeQuery(
//	    name="SheetIconRecord.selectPrivate(phrase,userName)",
//	    resultClass=SheetIconRecord.class,
//	    query="CALL `branchitup`.`sheetrecord.browseByPhraseAndOwner`(?phrase, ?userName,0,10)"
////		    query="SELECT s.name,s.sheetId,g.iconImageFileId AS genreImageFileId FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId WHERE s.name LIKE ?phrase ORDER BY s.name LIMIT 0,?maxResults"
//	),
//	@NamedNativeQuery(
//	    name="SheetIconRecord.selectPublic(phrase,userName)",
//	    resultClass=SheetIconRecord.class,
//	    query="CALL `branchitup`.`sheetrecord.browseByPhrase`(?phrase, ?userName,0,10)"
////		    query="SELECT s.name,s.sheetId,g.iconImageFileId AS genreImageFileId FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId WHERE s.name LIKE ?phrase ORDER BY s.name LIMIT 0,?maxResults"
//	),
//	@NamedNativeQuery( //prepared statement - > MYSQL 5.5 has no array argument capability through stored procedures
//	    name="SheetIconRecord.selectTop(phrase,genreIdList)",
//	    resultClass=SheetIconRecord.class,
//	    query="SELECT s.name,s.sheetId, s.modifiedOn, g.name AS genreName, g.iconImageFileId AS genreImageFileId, u.userName AS owner, u.firstName, u.lastName FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId INNER JOIN useraccounts AS u ON s.owner = u.userName WHERE s.name LIKE ?phrase AND s.genreId IN (?genreIdList) ORDER BY s.name LIMIT 0,10"
////	    query="SELECT s.name,s.sheetId, s.modifiedOn, g.iconImageFileId AS genreImageFileId, u.userName, u.firstName, u.lastName FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId INNER JOIN useraccounts AS u ON s.owner = u.userName WHERE s.name LIKE ?phrase AND s.genreId IN (69,71,72) ORDER BY s.name LIMIT 0,10"
//	),
//	@NamedNativeQuery(
//		name="SheetIconRecord.selectTop(phrase,genreIdList,userName)",
//		resultClass=SheetIconRecord.class,
//		query="SELECT s.name,s.sheetId, s.modifiedOn, s.permissionsMask, g.name AS genreName, g.iconImageFileId AS genreImageFileId, u.userName AS owner, u.firstName, u.lastName FROM sheets AS s INNER JOIN genres AS g ON s.genreId = g.genreId INNER JOIN useraccounts AS u ON s.owner = u.userName WHERE s.name LIKE ?phrase AND s.genreId IN ?genreIdList AND s.owner <> ?userName ORDER BY s.name LIMIT 0,10"
//	)
//})
@Deprecated
@Entity
public class SheetIconRecord {
//	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="sheetId")
	public Long sheetId;
	
	@Column(name="name")
	public String name;
	
	@Column(name="genreName")
	public String genreName;
	
	@Column(name="genreImageFileId")
	public Long genreImageFileId;
	
	@Column(name="owner")
	public String owner;
	
	@Column(name="firstName")
	public String firstName;
	
	@Column(name="lastName")
	public String lastName;
	
//	@Column(name="createdOn")
//	public Timestamp createdOn;
	
	@Column(name="modifiedOn")
	public Timestamp modifiedOn;
	
	@Column(name="permissionsMask")
	public Integer permissionsMask;
	
	@Column(name="folderName")
	public String folderName;
	
	public SheetIconRecord(){
	}
	
//	public SheetNameRecord(String name,Long sheetId,Long genreImageFileId){
//		this.name = name;
//		this.sheetId = sheetId;
//		this.genreImageFileId = genreImageFileId;
//	}
}
