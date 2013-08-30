/*
 * BranchItUp PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.persistence.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;

@NamedQueries({
//	@NamedQuery(
//	    name="Book_DiskResource.select(bookId,resourceType)",
//	    query="SELECT o FROM Book_DiskResource AS o INNER JOIN o.diskResource as d WHERE o.diskResourceId = d.diskResourceId AND o.bookId = :bookId AND d.resourceType =:resourceType"
//	),
//	,
//	@NamedQuery(
//		name="Book_DiskResource.updateDiskResourceId(bookId,diskResourceId)",
//		query="UPDATE Book_DiskResource AS o SET o.diskResourceIdWHERE o.diskResourceId = d.diskResourceId AND o.bookId = :bookId AND d.resourceType =:resourceType"
//	)
//	---------------------------------------------------------

//	------------------------------------------------------------
	
	
//	--------------------------------------------------------------
	
	//	------------------------------------------------------------------------
	

//	@NamedQuery(
//	    name="Sheet.selectTitlesByUserName",
//	    query="select new com.branchitup.persistence.users.Sheet(o.sheetId, o.title) from Sheet as o where o.userName = :userName"
//	),
//	@NamedQuery(
//	    name="Sheet.selectByUserName",
//	    query="select obejct(o) from Sheet as o where o.userName = :userName"
//	),
//	@NamedQuery(
//	    name="Sheet.selectById",
//	    query="select o from Sheet as o where o.sheetId = :sheetId"
//	),
//	@NamedQuery(
//	    name="Sheet.selectByIds",
//	    query="SELECT o FROM Sheet as o WHERE o.sheetId IN :sheetIds ORDER BY o.modifiedOn DESC"
//	),
//	@NamedQuery(
//	    name="Sheet.selectById",
//	    query="SELECT o FROM Sheet as o WHERE o.sheetId = :sheetId ORDER BY o.lastModified ASC"
//	)
//	------------------------------------------------------
//	@NamedQuery(
//	    name="Sheet.selectTitlesByUserName",
//	    query="select new com.branchitup.persistence.users.Sheet(o.sheetId, o.title) from Sheet as o where o.userName = :userName"
//	),
//	@NamedQuery(
//	    name="Sheet.selectByUserName",
//	    query="select obejct(o) from Sheet as o where o.userName = :userName"
//	)
//	@NamedQuery(
//	    name="Sheet.selectById",
//	    query="select o from Sheet as o where o.sheetId = :sheetId"
//	),
//	@NamedQuery(
//	    name="Sheet.selectByIds",
//	    query="SELECT o FROM Sheet as o WHERE o.sheetId IN :sheetIds ORDER BY o.createdOn DESC"
//	),
//	@NamedQuery(
//	    name="Sheet_Book.selectByBookId",
//	    query="SELECT o FROM Sheet_Book as o WHERE o.bookId = :bookId ORDER BY o.sequenceIndex ASC"
//	),
//	@NamedQuery(
//	    name="Sheet_Book.update(bookId,newBookId)",
//	    query="UPDATE Sheet_Book as o SET o.bookId = :newBookId, o.sequenceIndex = NULL WHERE o.bookId = :bookId"
//	),
//	@NamedQuery(
//	    name="Sheet_Book.count(bookId)",
//	    query="SELECT COUNT(o) FROM Sheet_Book as o WHERE o.bookId = :bookId"
//	),
//	@NamedQuery(
//		name="Sheet_Book.delete(bookId,sheetId)",
//		query="DELETE FROM Sheet_Book AS o WHERE o.bookId = :bookId AND o.sheetId = :sheetId"
//	)
})
@MappedSuperclass
public abstract class BaseEntity implements Serializable,Cloneable{
	private static final long serialVersionUID = 1l;
	
	@Override
	public Object clone(){
		try {
			return super.clone();
		} 
		catch (CloneNotSupportedException exp) {
//			Logger.log(exp);
		}
		return null;
	}

	@Override
	public boolean equals(Object obj){
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
		sb.append("[");
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				sb.append(field.getName());
//				sb.append(": ");
//				sb.append(String.valueOf(field.get(this)));
				sb.append(", ");
			}
		} catch (Exception exp) {
//			Logger.log(exp);
		}
		sb.append("]");
		return sb.toString();
	}
}
