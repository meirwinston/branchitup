/*
 * %W% %E%
 *
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * DEFFERED - CURRENTLY THERE IS NO OBVIOUS REASON TO MAKE A MANY TO MANY
 * RELATIONSHIP BETWEEN Sheet AND Genre
 * 
 * @author Meir Winston
 *
 */
//@Table(name="sheet_genre",schema="branchitup")
//@Entity
public class Sheet_Genre {
//	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="sheetId")
	private long sheetId;
	
	@Id
	@Column(name="genreId")
	private long genreId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sheetId",referencedColumnName="sheetId",insertable=false,updatable=false)
	private Sheet sheet;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="genreId",referencedColumnName="id",updatable=false, insertable=false)
	private Genre genre;

	public long getSheetId() {
		return sheetId;
	}

	public void setSheetId(long sheetId) {
		this.sheetId = sheetId;
	}

	public long getGenreId() {
		return genreId;
	}

	public void setGenreId(long genreId) {
		this.genreId = genreId;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
}
