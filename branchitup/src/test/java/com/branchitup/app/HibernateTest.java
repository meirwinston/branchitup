package com.branchitup.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.branchitup.export.EbookAttachmentCarrier;
import com.branchitup.export.ExportHandler;
import com.branchitup.model.BaseModel;
import com.branchitup.model.EntityModel;
import com.branchitup.persistence.HibernateDao_Cache;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.PublishedSheet;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.persistence.entities.UserName;
import com.branchitup.transfer.arguments.PublishedBookExportArgs;
import com.branchitup.transfer.arguments.ScrollPublicationsArgs;
import com.branchitup.transfer.arguments.PublishedBookExportArgs.PublisherExportArgs;


public class HibernateTest {
	
	public static void generateConfigOutput(){
		File file = new File("/home/mwinston/workspace/branchitup-workspace/branchitup/src/main/java/com/branchitup/persistence/entities");
		File[] files = file.listFiles();
		for(File f : files){
//			String n = f.getName();
			
			System.out.println("<mapping class=\"com.branchitup.persistence.entities." + f.getName().replace(".java", "") + "\" />");
		}
		
	}
	
	public static void testPDF(SessionFactory sessionFactory){
		System.out.println("---->testPDF");
	}
	
	public static void createEpub(SessionFactory sessionFactory){
		Long bookId = 10l;
		ExportHandler wkExporter = new ExportHandler();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		PublishedBook pb = StatelessDAO.findPublishedBook(session,bookId);;
		try {//bookId=26 the mouse
			
			
			PublishedBookExportArgs args = new PublishedBookExportArgs();
			args.title = pb.getTitle();
			args.bookId = pb.getBookId();
			
			args.publisherAccount = new PublishedBookExportArgs.PublisherExportArgs();
			args.publisherAccount.firstName = pb.getPublisherAccount().getFirstName();
			args.publisherAccount.lastName = pb.getPublisherAccount().getLastName();
			
			args.coverImageFile = pb.getCoverImageFile();
			args.sheetList = new ArrayList<PublishedBookExportArgs.SheetExportArgs>(pb.getSheetList().size());
			for(PublishedSheet ps : pb.getSheetList()){
				PublishedBookExportArgs.SheetExportArgs s = new PublishedBookExportArgs.SheetExportArgs();
				s.sheetId = ps.getSheetId();
				s.name = ps.getName();
				s.content = ps.getContent();
				s.cssText = ps.getCssText();
				
				args.sheetList.add(s);
			}
			Map<String, List<Publisher>> pmap = StatelessDAO.getContributorsMap(session,pb.getBookId());
			
			args.contributorsByRole = new HashMap<String,List<PublishedBookExportArgs.PublisherExportArgs>>();
			if(pmap != null && pmap.size() > 0){
				Set<String> keys = pmap.keySet();
				for(String key : keys){
					List<Publisher> plist = pmap.get(key);
					List<PublisherExportArgs> publisherExportList = new ArrayList<PublisherExportArgs>(plist.size());
					for(Publisher publisher : plist){
						PublisherExportArgs publisherArgs = new PublisherExportArgs();
						publisherArgs.firstName = publisher.getFirstName();
						publisherArgs.lastName = publisher.getLastName();
						publisherExportList.add(publisherArgs);
					}
					
					if(publisherExportList.size() > 0){
						args.contributorsByRole.put(key, publisherExportList);
					}
				}
			}
			args.sheetOwners = new ArrayList<PublishedBookExportArgs.PublisherExportArgs>();
			List<UserName> sheetOwners = StatelessDAO.findSheetOwners(session, pb.getBookId());
			for(UserName userName : sheetOwners){
				PublisherExportArgs ownerArgs = new PublisherExportArgs();
				ownerArgs.firstName = userName.firstName;
				ownerArgs.lastName = userName.lastName;
				
				args.sheetOwners.add(ownerArgs);
			}
			List<EbookAttachmentCarrier> files = wkExporter.export(args);
			for(EbookAttachmentCarrier f : files){
				System.out.println("-*-*--->file: " + f.file.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		finally {
			if(transaction != null){
				transaction.rollback();
			}
			session.close();
		}
	}
	
	public static void encryptPassword(SessionFactory sessionFactory){
//		SessionFactory sessionFactory = createSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		try {//bookId=26 the mouse
			Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			
			ReflectionSaltSource saltSource = new ReflectionSaltSource();
			saltSource.setUserPropertyToUse("username");
			
			String newPass = passwordEncoder.encodePassword("mwinston", "meirwinston@yahoo.com");
			
			System.out.println("set new password " + newPass);
			
			if(transaction != null){
				transaction.commit();
			}
		}
		catch(Exception exp){
			exp.printStackTrace();
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally {
			session.close();
		}
	}
	
	public static void encryptPasswords(SessionFactory sessionFactory){
//		SessionFactory sessionFactory = createSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		try {//bookId=26 the mouse
			Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			
			ReflectionSaltSource saltSource = new ReflectionSaltSource();
			saltSource.setUserPropertyToUse("username");
			
			for(long l = 0 ; l < 100 ; l++){
				UserAccount account = StatelessDAO.findUserAccount(session, l);
				if(account != null){
					String newPass = passwordEncoder.encodePassword(account.getPassword(), account.getEmail());
					account.setPassword(newPass);
					System.out.println("set new password " + newPass);
					
				}
			}
			if(transaction != null){
				transaction.commit();
			}
		}
		catch(Exception exp){
			exp.printStackTrace();
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally {
			session.close();
		}
	}
	
	public static void main(String[] args){
		///data/projects/springsource/yblob/yblob/src/main/java/com/yblob/persistence/entities/PublishedBook.java
//		if(true){
//			generateConfigOutput();
//			return;
//		}
		try{
			SessionFactory sessionFactory = createSessionFactory();
			encryptPassword(sessionFactory);
//			encryptPasswords(sessionFactory);
			
//			createEpub(sessionFactory);
//			testQueries(sessionFactory);
//			testHibernateDao(sessionFactory);
//			testJacksonEntityConversion(sessionFactory);
//			testPDF(sessionFactory);
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	public static SessionFactory createSessionFactory(){
		File file = new File("/hd/git/branchitup/branchitup/src/test/java/com/branchitup/app/hibernate.cfg.xml");
		Configuration configuration = new Configuration();
		configuration.configure(file);
		configuration.setProperty("hibernate.show_sql", "false");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://192.168.0.115:3306/branchitup");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/branchitup");
		configuration.setProperty("hibernate.connection.username", "root");
		configuration.setProperty("hibernate.connection.password", "root");
		configuration.setProperty("hibernate.connection.pool_size", "10");
		configuration.setProperty("show_sql", "true");
		configuration.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.current_session_context_class", "thread");
		
				
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);
		
		return factory;
	}
	
	public static void testHibernateDao(SessionFactory sessionFactory){
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
//			HibernateDao_Cache h = new HibernateDao_Cache(session);
//			System.out.println("-----> " + h.findAllPublishers(1956l));
			//System.out.println("-----> " + HibernatePersistenceManager.findAttachmentIdByBookId(session, 251l));
			long s = System.currentTimeMillis();
//			System.out.println("-----> " + StatelessDAO.findDerivedPublicationGenresByBook(session, 7l).get(0));
//			System.out.println("-----> " + StatelessDAO.getGenreTitles_(session, 4l));
//			System.out.println("-----> " + HibernatePersistenceManager.getGenreTitles(session, 4l));
//			System.out.println("----->full version " + StatelessDAO.fullVersion(session, 4l));
			
//			SELECT COUNT(c.downloadsCount) AS downloadsCount 
//			FROM (SELECT downloadsCount
//					FROM userdownloadscount 
//					WHERE userdownloadscount.bookId = bookId
//					AND userAccountId IS NULL 
//					GROUP BY ipAddress ) AS c;
//			Query query = session.createQuery("SELECT d.downloadsCount FROM UserDownloadsCount AS d WHERE d.bookId = 19 AND d.userAccountId IS NULL GROUP BY ipAddress");
			
			Query query = session.createQuery("SELECT COUNT(*) FROM (SELECT d.downloadsCount FROM UserDownloadsCount AS d WHERE d.bookId = 19 AND d.userAccountId IS NULL GROUP BY ipAddress)");
			System.out.println(query.list());
			System.out.println(System.currentTimeMillis() - s);
//			
		} finally {
		}
	}
	
	
	
	public static void testJacksonEntityConversion(SessionFactory sessionFactory) throws Exception{
		Session session = null;
//		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
//			HibernateDao_Cache h = new HibernateDao_Cache(session);
//			System.out.println("-----> " + h.findAllPublishers(1956l));
			//System.out.println("-----> " + HibernatePersistenceManager.findAttachmentIdByBookId(session, 251l));
			long s = System.currentTimeMillis();
			List<Publisher> publisherList = StatelessDAO.findAllPublishers(session,352l);
			System.out.println("-----> size: " + publisherList.size());
//			System.out.println("-----> " + new BaseModel(publisherList.get(0)));
//			System.out.println("-----> " + new EntityModel(publisherList.get(0)));
			System.out.println(System.currentTimeMillis() - s);
//			
		} finally {
		}
	}
	
	public static void testQueries(SessionFactory sessionFactory){
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			StatelessDAO p = new StatelessDAO();
			
			transaction = session.beginTransaction();
//			System.out.println("-----> " + p.getRatingAverage(session, 1956l));
			//--
			ScrollPublicationsArgs args = new ScrollPublicationsArgs();
//			args.phrase = null;
			args.offset = 0;
			args.maxResults = 6;
			args.session = session;
//			System.out.println("-----> " + StatelessDAO.selectPublication(args));
			
			
			transaction.commit();
		} 
		catch(Exception exp){
			exp.printStackTrace();
		}
		finally {
			if(transaction != null){
				
			}
			if(session != null ){
				session.close();
			}
			
		}
	}
	
	public static void testIncrementSubversion(SessionFactory sessionFactory){
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			StatelessDAO p = new StatelessDAO();
			
			transaction = session.beginTransaction();
//			System.out.println("----->session " + session.get(PublishedBook_1.class, 1969l));
//			System.out.println("----->session " + p.findPublishedBook(session, 1969l));
//			System.out.println("----->session " + p.findBook(session, 859l));
			
			
			transaction.commit();
		} finally {
			if(transaction != null){
				
			}
			if(session != null ){
				session.close();
			}
			
		}
	}
}
