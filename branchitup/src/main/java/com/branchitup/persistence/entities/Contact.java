package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
	    name="Contact.selectBySentCountAndCategory",
	    query="SELECT c FROM Contact AS c WHERE c.sentCount = :sentCount AND c.category = :category"
	),
	@NamedQuery(
	    name="Contact.selectBySentCountAndCategoryWithFullName",
	    query="SELECT c FROM Contact AS c " +
	    		"WHERE c.sentCount = :sentCount AND c.category = :category " +
	    		"AND c.fullName IS NOT NULL"
	)
})
@Table(name="contacts",schema="branchitup")
@Entity
public class Contact extends BaseEntity{
	private static final long serialVersionUID = 1L;
	@Id
	protected Long contactId;
	
	protected String email;
	protected Date lastSent;
	protected int sentCount;
	protected String fullName;
	
	
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	@Enumerated(EnumType.STRING)
	protected Category category;
	
	public enum Category{
		WRITER,
		TEST
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastSent() {
		return lastSent;
	}
	public void setLastSent(Date lastSent) {
		this.lastSent = lastSent;
	}
	public int getSentCount() {
		return sentCount;
	}
	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
