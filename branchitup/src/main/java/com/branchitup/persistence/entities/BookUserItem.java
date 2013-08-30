package com.branchitup.persistence.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Deprecated
@Entity
public class BookUserItem extends BaseEntity{
	private static final long serialVersionUID = 1L;

	public String name;

	@Id
	public Long id;

	@Transient
	public UserItem.Type type = UserItem.Type.BOOK;

	public Date modifiedOn;
	public Date createdOn;
}

//@Entity
//@DiscriminatorValue("BOOK")
//@Polymorphism(type=PolymorphismType.EXPLICIT)
//public class BookUserItem extends UserItem{
//	private static final long serialVersionUID = 1L;
//	
//	public String name;
//	
//	@Id
//	public Long id;
//	
//	@Transient
//	public UserItem.Type type = UserItem.Type.BOOK;
//	
//	public Date modifiedOn;
//	public Date createdOn;
//	
//	public Integer publishCount;
//
//}

