package com.branchitup.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.hibernate.Query;
import org.hibernate.Session;

import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.Book;
import com.branchitup.persistence.entities.PublishedBookDetails;
import com.branchitup.persistence.entities.Publisher;

public class HibernateDao_Cache {
	private Session session;
	private Map<String,Query> queryMap = new HashMap<String, Query>();
	public HibernateDao_Cache(Session session){
		this.session = session;
		init();
	}
	
	@SuppressWarnings("unchecked")
	static public <T> T uncheckedCast(Object obj) {
		return (T)obj;
	}
	
	private void init(){
		Query query = this.session.createSQLQuery("CALL `branchitup`.`Publisher.selectRecursiveByBookId`(:bookId)").addEntity(Publisher.class);
		query.setCacheable(true);
		queryMap.put("Publisher.selectRecursiveByBookId", query);
		
		query = session.createSQLQuery("call `publishedbooks.searchByTitle`(%:phrase%,:offset,:maxResults)").addEntity(PublishedBookDetails.class);
		queryMap.put("publishedbooks.searchByTitle", query);
		
		query = session.createSQLQuery("call `publishedbooks.browse`(:offset,:maxResults)").addEntity(PublishedBookDetails.class);
		queryMap.put("publishedbooks.browse", query);
	}
	
	public List<Publisher> findAllPublishers(long bookId){
		Query query = queryMap.get("Publisher.selectRecursiveByBookId");
		query.setParameter("bookId", bookId);
		return query.list();
	}
	
	public Attachment findAttachment(Session session,Long attachmentId){
		return (Attachment)session.load(Attachment.class, attachmentId);
	}
	
	public Book selectBook(Session session,long bookId){
		return (Book)session.load(Book.class, bookId);
	}
	
	public FileItem generatePDF(Session session,long bookId,String username) throws InterruptedException{
		FileItem fileItem = null;
		
		Book book = this.selectBook(session,bookId);
		if(book != null){
//			ExportableBook xbook = new ExportableBook(book,this.selectUserAccount(userName));
//			fileItem = WKExporter.getInstance().export(xbook);
		}
		
		return fileItem;
	}
}
