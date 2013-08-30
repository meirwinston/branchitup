package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.branchitup.json.ArticleCommentSerializer;

@NamedQueries({
	@NamedQuery(
	    name="ArticleComment.select",
	    query="SELECT c from ArticleComment AS c"
	),
	@NamedQuery(
		name="ArticleComment.selectBySheetId",
		query="SELECT r " +
			"FROM ArticleComment AS r " +
			"INNER JOIN r.userAccount AS u " +
			"WHERE r.sheetId = :sheetId " +
			"ORDER BY r.createdOn DESC"
	),
	@NamedQuery(
		name="ArticleComment.countBySheetId",
		query="SELECT COUNT(r.sheetId) " +
			"FROM ArticleComment AS r " +
			"WHERE r.sheetId = :sheetId "
	)
})
@Entity
@Table(name="articlecomments",schema="branchitup")
@JsonSerialize(using=ArticleCommentSerializer.class)
public class ArticleComment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="commentId")
	protected Long commentId;
	
	@Column(name="sheetId")
	protected Long sheetId;
	
	@Column(name="comment")
	protected String comment;
	
	@Column(name="parentId")
	protected Long parentId;
	
	@Column(name="email")
	protected String email;
	
	@Column(name="fullName")
	protected String fullName;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@JoinColumn(name = "userAccountId",referencedColumnName="userAccountId",insertable=false,updatable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	protected UserAccount userAccount;
	
	@Column(name="createdOn")
	protected Date createdOn;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
}
