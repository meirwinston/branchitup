package com.branchitup.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@NamedQueries({
	@NamedQuery(
	    name="GenreTitle.selectByBookId",
	    query="SELECT new com.branchitup.persistence.entities.GenreTitle(pg.genreId, g.name, pg.sequenceIndex) FROM PublishedBook_Genre AS pg INNER JOIN pg.genre AS g WHERE pg.bookId = :bookId ORDER BY pg.sequenceIndex"
	),
	@NamedQuery(
	    name="GenreName.selectByBookId",
	    query="SELECT g.name FROM PublishedBook_Genre AS pg INNER JOIN pg.genre AS g WHERE pg.bookId = :bookId"
	)
})
@Entity
public class GenreTitle extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="genreId")
	public Long genreId;

	@Column(name="name")
	public String name;
	
	@Column(name="sequenceIndex")
	public Integer sequenceIndex;
	
	public GenreTitle(){}
	
	public GenreTitle(Long genreId,String name,Integer sequenceIndex){
		this.genreId = genreId;
		this.name = name;
		this.sequenceIndex = sequenceIndex;
	}
	
	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequenceIndex() {
		return sequenceIndex;
	}

	public void setSequenceIndex(Integer sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}

	@Override
	public String toString() {
		return "GenreTitle [genreId=" + genreId + ", name=" + name
				+ ", sequenceIndex=" + sequenceIndex + "]";
	}
	
	
}