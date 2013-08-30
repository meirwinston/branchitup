package com.branchitup.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(
	    name="UserItem.selectPublishedBookByPublisherPhrase",
	    query="SELECT new UserItem(b.title, b.bookId, b.createdOn, 'BOOK') " +
	    		"FROM PublishedBook AS b " +
	    		"WHERE b.publisherAccountId = :publisherAccountId " +
	    		"AND b.title LIKE :phrase " +
	    		"ORDER BY b.createdOn DESC"
	),
	@NamedQuery(
		name="UserItem.selectPublishedBookByPhrase",
	    query="SELECT new UserItem(b.title, b.bookId, b.createdOn, 'BOOK') " +
	    		"FROM PublishedBook AS b " +
	    		"WHERE b.title " +
	    		"LIKE :phrase " +
	    		"ORDER BY b.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectBooksByOwnerAndPhrase",
	    query="SELECT new UserItem(b.title, b.bookId, b.createdOn, b.modifiedOn, 'BOOK') " +
	    		"FROM Book AS b WHERE b.ownerAccountId = :ownerAccountId " +
	    		"AND b.title LIKE :phrase " +
	    		"ORDER BY b.modifiedOn,b.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectBooksByPhrase",
	    query="SELECT new UserItem(b.title, b.bookId, b.createdOn, b.modifiedOn, 'BOOK') " +
	    		"FROM Book AS b " +
	    		"WHERE b.title " +
	    		"LIKE :phrase " +
	    		"ORDER BY b.modifiedOn,b.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectSheetsByOwnerPhrasePermission",
	    query="SELECT new UserItem(s.name, s.sheetId, s.createdOn, s.modifiedOn, (CASE WHEN s.blogId IS NULL THEN 'SHEET' ELSE 'ARTICLE' END)) " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.ownerAccountId = :ownerAccountId " +
	    		"AND s.name LIKE :phrase " +
	    		"AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"AND s.permissionsMask >= :permissionMask " +
	    		"ORDER BY s.modifiedOn,s.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectAccountsLikeName",
	    query="SELECT new UserItem(CONCAT(a.firstName,' ',a.lastName), a.userAccountId, a.createdOn, a.createdOn, 'USER') " +
	    		"FROM UserAccount AS a " +
	    		"WHERE CONCAT(a.firstName,' ',a.lastName) LIKE :phrase " +
	    		"AND a.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"ORDER BY a.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectArticleByPhrase",
	    query="SELECT new UserItem(s.name, s.sheetId, s.createdOn, s.modifiedOn,'ARTICLE') " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.name " +
	    		"LIKE :phrase AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"AND s.blogId IS NOT NULL " +
	    		"ORDER BY s.publishedOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectArticleByPhraseAndOwner",
	    query="SELECT new UserItem(s.name, s.sheetId, s.createdOn, s.modifiedOn,'ARTICLE') " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.name " +
	    		"LIKE :phrase AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"AND s.blogId IS NOT NULL " +
	    		"AND s.ownerAccountId = :ownerAccountId " +
	    		"ORDER BY s.publishedOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectSheetsByPhrasePermission",
	    query="SELECT new UserItem(s.name, s.sheetId, s.createdOn, s.modifiedOn, (CASE WHEN s.blogId IS NULL THEN 'SHEET' ELSE 'ARTICLE' END)) " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.name " +
	    		"LIKE :phrase AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"AND s.permissionsMask >= :permissionMask " +
	    		"ORDER BY s.modifiedOn,s.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectSheetsInFolder",
	    query="SELECT new com.branchitup.persistence.entities.UserItem(s.name, s.sheetId, s.createdOn, " +
	    		"s.modifiedOn, (CASE WHEN s.blogId IS NULL THEN 'SHEET' ELSE 'ARTICLE' END)) " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.ownerAccountId = :ownerAccountId " +
	    		"AND s.folderName = :folderName " +
	    		"AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"ORDER BY s.modifiedOn, s.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectSheets",
	    query="SELECT new com.branchitup.persistence.entities.UserItem(s.name, s.sheetId, " +
				"s.createdOn, s.modifiedOn, (CASE WHEN s.blogId IS NULL THEN 'SHEET' ELSE 'ARTICLE' END)) " +
	    		"FROM Sheet AS s " +
	    		"WHERE s.ownerAccountId = :ownerAccountId " +
	    		"AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE " +
	    		"ORDER BY s.modifiedOn, s.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectBooks",
	    query="SELECT new com.branchitup.persistence.entities.UserItem(b.title, b.bookId, b.createdOn, " +
	    "b.modifiedOn, 'BOOK') " +
	    "FROM Book AS b " +
	    "ORDER BY b.modifiedOn, b.createdOn DESC"
	),
	@NamedQuery(
	    name="UserItem.selectBooksByOwner",
	    query="SELECT new com.branchitup.persistence.entities.UserItem(b.title, b.bookId, b.createdOn, " +
	    "b.modifiedOn, 'BOOK') " +
	    "FROM Book AS b " +
	    "WHERE b.ownerAccountId = :ownerAccountId " +
	    "ORDER BY b.modifiedOn, b.createdOn DESC"
	),
	@NamedQuery(
		name="UserItem.countSheetsInFolder",
		query="SELECT COUNT(s.sheetId) FROM Sheet AS s " +
				"WHERE s.ownerAccountId = :ownerAccountId " +
				"AND s.folderName = :folderName " +
				"AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE"
	),
	@NamedQuery(
		name="UserItem.countSheets",
		query="SELECT COUNT(s.sheetId) FROM Sheet AS s " +
				"WHERE s.ownerAccountId = :ownerAccountId " +
				"AND s.status = com.branchitup.persistence.UserItemStatus.ACTIVE"
	)
})
@Entity
//@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@Polymorphism(type=PolymorphismType.EXPLICIT)
public class UserItem extends BaseEntity{
	private static final long serialVersionUID = 1L;
	 
	public enum Type{
		GENRE,
		PUBLISHER,
		SHEET,
		BOOK,
		USER,
		ARTICLE;
	}
	
	@Column(name="name")
	public String name;
	
	@Id
	@Column(name="id")
	public Long id;
	
	@Id
	@Enumerated 
	@Column(name="type")
	public Type type;
	
	@Column(name="modifiedOn")
	public Date modifiedOn;
	
	@Column(name="createdOn")
	public Date createdOn;
	
	public UserItem(){
		
	}

	public UserItem(String name, Long id, Date createdOn, Date modifiedOn, Type type){
		this.name = name;
		this.id = id;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.type = type;
	}
	
	public UserItem(String name, Long id, Date createdOn, Date modifiedOn, String type){
		this(name,id,createdOn,modifiedOn,Type.valueOf(type));
	}
	
	public UserItem(String name, Long id, Date createdOn, String type){
		this(name,id,createdOn,null,Type.valueOf(type));
	}
	
//	public UserItem(String name, Long id, Date createdOn, Date modifiedOn){
//		this.name = name;
//		this.id = id;
//		this.createdOn = createdOn;
//		this.modifiedOn = modifiedOn;
//	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}