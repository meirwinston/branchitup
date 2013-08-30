package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(
	    name="PublicRecord.selectGenres",
	    query="SELECT g.genreId, g.name FROM Genre AS g WHERE g.name LIKE :phrase ORDER BY g.name"
	),
	@NamedQuery(
	    name="PublicRecord.selectPublications",
	    query="SELECT p.bookId, p.title FROM PublishedBook AS p WHERE p.title LIKE :phrase ORDER BY p.title"
	),
	@NamedQuery(
	    name="PublicRecord.selectPublicSheets",
	    query="SELECT s.sheetId, s.name FROM Sheet AS s WHERE s.name LIKE :phrase AND s.permissionsMask >= 287 ORDER BY s.name"
	),
	@NamedQuery(
	    name="PublicRecord.selectUsers",
	    query="SELECT u.username, u.firstName, u.lastName FROM UserAccount AS u WHERE u.firstName LIKE :phrase OR u.lastName LIKE :phrase ORDER BY u.firstName, u.lastName"
	)
})
@Deprecated //user UserItem instead
@Entity
public class PublicRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public enum Type{
		SHEET,
		GENRE,
		BOOK,
		USER
	}

	public PublicRecord(){}
	
	public PublicRecord(String id, String name, Type type){
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	@Id
	@Column(name="id")
	public String id;
	
	@Column(name="name")
	public String name;
	
//	@Column(name="imageFileId")
//	public Long imageFileId;
	
	@Column(name="type")
	public Type type;

	public String getRecordId() {
		return id;
	}

	public void setRecordId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	
}