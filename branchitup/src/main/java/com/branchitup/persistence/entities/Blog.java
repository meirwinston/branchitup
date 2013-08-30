package com.branchitup.persistence.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="Blog.selectByUserAccount",
	    query="select b from Blog as b where b.userAccountId = :userAccountId"
	)
})
@Table(name="blogs",schema="branchitup")
@Entity
public class Blog extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="blogId")
	protected Long blogId;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="title")
	protected String title;
	
	@Column(name="createdOn")
	protected Date createdOn;
	
	@Column(name="lastUpdated")
	protected Date lastUpdated;
	
	@Column(name="userAccountId")
	protected Long userAccountId;
	
	@JoinColumn(name="userAccountId",referencedColumnName="userAccountId", updatable=false, insertable=false)
	@ManyToOne(fetch=FetchType.LAZY)
	protected UserAccount userAccount;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="blog")
	protected List<Sheet> sheets;
	
	@Column(name="mastheadImageFileId")
	protected Long mastheadImageFileId;
	
	@JoinColumn(name = "mastheadImageFileId",referencedColumnName="imageFileId",insertable=false,updatable=false)
	@OneToOne(fetch=FetchType.LAZY)
	protected ImageFile mastheadImageFile;
	
	protected String subtitle;
	
	protected String description;
	
	protected String footer;
	
	public Long getBlogId() {
		return blogId;
	}
	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Long getUserAccountId() {
		return userAccountId;
	}
	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}
	public List<Sheet> getSheets() {
		return sheets;
	}
	public void setSheets(List<Sheet> sheets) {
		this.sheets = sheets;
	}
	public Long getMastheadImageFileId() {
		return mastheadImageFileId;
	}
	public void setMastheadImageFileId(Long mastheadImageFileId) {
		this.mastheadImageFileId = mastheadImageFileId;
	}
	public ImageFile getMastheadImageFile() {
		return mastheadImageFile;
	}
	public void setMastheadImageFile(ImageFile mastheadImageFile) {
		this.mastheadImageFile = mastheadImageFile;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
}
