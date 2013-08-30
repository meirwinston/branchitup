package com.branchitup.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.encoding.PasswordEncoder;//^^p
import com.branchitup.exception.InvalidInputException;
import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.exception.UserAccountException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.persistence.ScrollResult;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.AudioFile;
import com.branchitup.persistence.entities.Blog;
import com.branchitup.persistence.entities.Book;
import com.branchitup.persistence.entities.BookDetails;
import com.branchitup.persistence.entities.BookDetailsSheet;
import com.branchitup.persistence.entities.BookShowcase;
import com.branchitup.persistence.entities.Genre;
import com.branchitup.persistence.entities.GenreDetails;
import com.branchitup.persistence.entities.GenreTitle;
import com.branchitup.persistence.entities.ImageFile;
import com.branchitup.persistence.entities.ParentBook;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.PublishedBookDetails;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.Sheet;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.persistence.entities.UserItem;
import com.branchitup.persistence.entities.UserName;
import com.branchitup.persistence.entities.UserWallRecord;
import com.branchitup.system.Constants;
import com.branchitup.system.FileUtilities;
import com.branchitup.system.SystemAttributes;
import com.branchitup.transfer.arguments.BranchPublishedBookArgs;
import com.branchitup.transfer.arguments.FileItemArgs;
import com.branchitup.transfer.arguments.IncrementDownloadCountArgs;
import com.branchitup.transfer.arguments.NewBookArgs;
import com.branchitup.transfer.arguments.NewGenreArgs;
import com.branchitup.transfer.arguments.NewSheetArgs;
import com.branchitup.transfer.arguments.NewUserAccountArgs;
import com.branchitup.transfer.arguments.PublishBookArgs;
import com.branchitup.transfer.arguments.PublishSheetToBlogArgs;
import com.branchitup.transfer.arguments.ScrollArticleCommentsArgs;
import com.branchitup.transfer.arguments.ScrollAttachmentsArgs;
import com.branchitup.transfer.arguments.ScrollBookShowcaseArgs;
import com.branchitup.transfer.arguments.ScrollGenreArgs;
import com.branchitup.transfer.arguments.ScrollImageFileArgs;
import com.branchitup.transfer.arguments.ScrollPublicationsArgs;
import com.branchitup.transfer.arguments.ScrollReviewsArgs;
import com.branchitup.transfer.arguments.ScrollSheetItemsArgs;
import com.branchitup.transfer.arguments.ScrollUserItemsArgs;
import com.branchitup.transfer.arguments.AttachAudioArgs;
import com.branchitup.transfer.arguments.ScrollUserWallRecordArgs;
import com.branchitup.transfer.arguments.SubmitArticleCommentArgs;
import com.branchitup.transfer.arguments.SubmitAudioFileRatingArgs;
import com.branchitup.transfer.arguments.SubmitBookCommentArgs;
import com.branchitup.transfer.arguments.SubmitBookRatingArgs;
import com.branchitup.transfer.arguments.UpdateBookArgs;
import com.branchitup.transfer.arguments.UpdateGenreArgs;
import com.branchitup.transfer.arguments.UpdateSheetArgs;
import com.branchitup.transfer.arguments.UserProfileInfoArgs;
import com.branchitup.transfer.arguments.NewUserWallRecordArgs;
import com.mysql.jdbc.MysqlDataTruncation;
import org.springframework.security.authentication.dao.SaltSource;
//import org.springframework.security.core.userdetails.UserDetails; //^^p
//import org.springframework.test.annotation.Repeat;//^^p

/**
 * Transaction layer, exception handling and rollback handling
 * 
 * @author Meir Winston
 */
//@Service //declared in configuration 
public class ModelServiceImpl implements ModelService{
	Logger logger = Logger.getLogger(getClass());
	
	private SessionFactory sessionFactory;
	private MailService mailService;
	private ScheduledService scheduledService;
	private PasswordEncoder passwordEncoder; //^^p
	private SaltSource saltSource;
	
	
	@PostConstruct
	public void init(){
//		this.scheduler = this.schedulerFactoryBean.getObject();
//		try {
//			this.wkExporter = new WKExporter();
//			pdfJobDetail = new JobDetail("pdfJobDetail",ModelServiceImpl.class);
//			pdfJobDetail.getJobDataMap().put("SessionFactory", this.sessionFactory);
//			
//			this.scheduler.start();
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}
	
	public void setSystemAttributes(SystemAttributes atts){
		System.out.println("ModelServiceImpl: Languages Map SET");
		System.out.println(atts);
		
	}
	//^^p
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	//^^p
	@Required
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	
	public SaltSource getSaltSource() {
		return saltSource;
	}

	@Required
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	//	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		
		System.out.println("---->ModelServiceImpl.setSessionFactory " + sessionFactory);
		this.sessionFactory = sessionFactory;
		
//		new ManagedSessionContext(sessionFactoryImplementor);
//		this.h = new HibernateTemplate(sessionFactory);
	}
	
	public ScheduledService getScheduledService() {
		return scheduledService;
	}

	public void setScheduledService(ScheduledService scheduledService) {
		this.scheduledService = scheduledService;
	}
	
	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * to make sessionFactory.getCurrentSession work make sure to declare OpenSessionInViewFilter in web.xml
	 * @return
	 */
	public Session getSession(){
		Session s = null;
		try {
			s = this.sessionFactory.getCurrentSession();
		} 
		catch (Exception e) {
			s = this.sessionFactory.openSession();
		}
		return s;
	}
	
//	public void sendPasswordToUser(Long userAccountId){
//		
//	}
	
	@Override
	public UserAccount findUserAccountByEmail(String email) throws Exception{
		UserAccount userAccount = null;
		Session session = getSession();
		userAccount = StatelessDAO.findUserAccountByEmail(session, email);
		return userAccount;
	}
	
	@Override
	public String getPublisherComment(Long bookId) {
		Session session = getSession();
		return StatelessDAO.getPublisherComment(session, bookId);
	}

	@Override
	public UserAccount findUserAccount(Long userAccountId) throws Exception{
		return StatelessDAO.findUserAccount(getSession(), userAccountId);
	}
	
	public List<BookShowcase> browseBookShowcaseByDownloads(ScrollBookShowcaseArgs args)
	throws Exception{
		args.session = getSession();
		try {
			return StatelessDAO.scrollBookShowcaseByDownloads(args);
		} 
		catch (Exception exp) {
			throw exp;
		}
	}
	
	@Override
	public PublishedBookDetails findPublishedBookDetails(Long bookId)
	throws Exception{
		try {
			return StatelessDAO.findPublishedBookDetails(getSession(), bookId);
		} 
		catch (Exception exp) {
			throw exp;
		}
	}
	
	@Override
	public ScrollResult scrollUserWallRecords(ScrollUserWallRecordArgs args){
		ScrollResult result = new ScrollResult();
		args.session = getSession();
		result.list = StatelessDAO.scrollUserWallRecords(args);
		result.count = StatelessDAO.countUserWallRecords(args.session, args.userAccountId);
		return result;
	}
	
	@Override
	public UserWallRecord newUserWallRecord(NewUserWallRecordArgs args) throws Exception{
		UserWallRecord newRecord = null;
		args.session = getSession();
		Transaction t = args.session.beginTransaction();
		try {
			newRecord = StatelessDAO.insertUserWallRecord(args);
			t.commit();
			if(!args.senderAccountId.equals(args.userAccountId)){
				mailService.sendWallMessageNotification(args);
//				System.out.println("----sending EMAIL");
			}
//			else{
//				System.out.println("----NOT sending EMAIL");
//			}
		} catch (Exception exp) {
			t.rollback();
			throw exp;
		}
		return newRecord;
	}
	
//	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	@Override
	public ScrollResult scrollPublicationRecords(ScrollPublicationsArgs args) throws Exception {
		ScrollResult result = new ScrollResult();
		try {
			args.session = getSession();
			result.list = StatelessDAO.selectPublicationRecords(args);
			result.count = StatelessDAO.countPublicationRecords(args);
		} 
		catch (Exception exp) {
			throw exp;
		}
		return result;
	}

	
//	public static Map<PublisherRole, List<Publisher>> getPublishersByRole(List<Publisher> plist){
//		Map<PublisherRole,List<Publisher>> map = new Hashtable<PublisherRole, List<Publisher>>();
//		if(plist != null){
//			for(Publisher p : plist){
//				List<PublisherRole> roles = p.getPublisherRoles();
//				for(PublisherRole role : roles){
//					List<Publisher> publishers = map.get(role);
//					if(publishers == null){
//						publishers = new ArrayList<Publisher>();
//						map.put(role, publishers);
//					}
//					publishers.add(p);
//				}
//			}
//		}
//		return map;
//	}
	
	@Override
	public List<ParentBook> discoverBranch(Long bookId) throws Exception{
		List<ParentBook> list = null;
		try{
			list = StatelessDAO.selectPathOfBranch(getSession(), bookId);
		}
		catch(Exception exp){
			throw exp;
		}
		return list;
	}
	
//	@Override
//	public Map<String,Object> findPublicationContributors(long bookId) throws Exception {
//		Map<String,Object> m = new HashMap<String,Object>();
//		try {
//			Session session = getSession();
//			List<Publisher> publisherList = StatelessDAO.findAllPublishers(session,bookId);
//			m.put("publishers",JacksonUtils.toModelList(publisherList)); //publisher role will determine what exactly each publisher contributed
//			
//			//--
//			Map<String, List<PublisherModel>> contributorsMap = new HashMap<String, List<PublisherModel>>();
//			Iterator<Entry<PublisherRole,List<Publisher>>> itr = getPublishersByRole(publisherList).entrySet().iterator();
//			while(itr.hasNext()){
//				Entry<PublisherRole,List<Publisher>> entry = itr.next();
//				List<Publisher> publishers = entry.getValue();
//				if(publishers.size() > 0){
//					List<PublisherModel> plist = new ArrayList<PublisherModel>();
//					for(Publisher p : publishers){
//						PublisherModel publisherModel = new PublisherModel(p);
//						plist.add(publisherModel);
//					}
//					contributorsMap.put(entry.getKey().toString(), plist);
//				}
//			}
//			m.put("sheetOwners",JacksonUtils.toModelList(StatelessDAO.findSheetOwners(session, bookId)));
//			m.put("contributorsByRole", contributorsMap);
//		}
//		catch (Exception exp) {
//			throw exp;
//		}
////		System.out.println("----->WebBean.findPublicationContributors for " + bookId + ", returns map size " + p.getPublishersByRole().size() + ", pub: " + p.publishers.size());
//		return m;
//	}
	
	public static void f(){
		
	}
	
//	@Override
//	public Map<String,Object> findPublicationContributors(long bookId) throws Exception {
//		Map<String,Object> m = new HashMap<String,Object>();
//		try {
//			Session session = getSession();
//			List<Publisher> publisherList = StatelessDAO.findAllPublishers(session,bookId);
//			m.put("publishers",JacksonUtils.toModelList(publisherList)); //publisher role will determine what exactly each publisher contributed
//			
//			//--
////			Map<String, List<Publisher>> contributorsMap = new HashMap<String, List<Publisher>>();
////			Iterator<Entry<PublisherRole,List<Publisher>>> itr = getPublishersByRole(publisherList).entrySet().iterator();
////			while(itr.hasNext()){
////				Entry<PublisherRole,List<Publisher>> entry = itr.next();
////				List<Publisher> publishers = entry.getValue();
////				if(publishers.size() > 0){
////					List<Publisher> plist = new ArrayList<Publisher>();
////					for(Publisher p : publishers){
////						plist.add(p);
////					}
////					contributorsMap.put(entry.getKey().toString(), plist);
////				}
////			}
//			m.put("sheetOwners",JacksonUtils.toModelList(StatelessDAO.findSheetOwners(session, bookId)));
//			m.put("contributorsByRole", StatelessDAO.getContributorsMap(publisherList));
//		}
//		catch (Exception exp) {
//			throw exp;
//		}
////		System.out.println("----->WebBean.findPublicationContributors for " + bookId + ", returns map size " + p.getPublishersByRole().size() + ", pub: " + p.publishers.size());
//		return m;
//	}

	public List<Publisher> getAllPublishers(Long bookId){
		return StatelessDAO.findAllPublishers(getSession(),bookId);
	}
	
	public List<UserName> getSheetOwners(Long bookId){
		return StatelessDAO.findSheetOwners(getSession(), bookId);
	}
	
	public Map<String, List<Publisher>> getContributorsMap(Long bookId){
		return StatelessDAO.getContributorsMap(getSession(),bookId);
	}
	
	@Override
	public Map<String,Object> findPublicationContributors(long bookId) throws Exception {
		Map<String,Object> m = new HashMap<String,Object>();
		try {
			Session session = getSession();
			List<Publisher> publisherList = StatelessDAO.findAllPublishers(session,bookId);
			m.put("publishers",JacksonUtils.toModelList(publisherList)); //publisher role will determine what exactly each publisher contributed
			m.put("sheetOwners",JacksonUtils.toModelList(StatelessDAO.findSheetOwners(session, bookId)));
			m.put("contributorsByRole", StatelessDAO.createContributorsMap(publisherList));
		}
		catch (Exception exp) {
			throw exp;
		}
//		System.out.println("----->WebBean.findPublicationContributors for " + bookId + ", returns map size " + p.getPublishersByRole().size() + ", pub: " + p.publishers.size());
		return m;
	}

	@Override
	public ScrollResult scrollReviews(ScrollReviewsArgs args) 
	throws Exception {
		ScrollResult result = new ScrollResult();
		try {
			args.session = getSession();
			result.list = StatelessDAO.scrollReviews(args);
			result.count = StatelessDAO.countReviews(args.session, args.bookId);
			
		} 
		catch (Exception exp) {
			throw exp;
		}
		return result; 
	}
	
	@Override
	public ScrollResult scrollArticleComments(ScrollArticleCommentsArgs args) 
	throws Exception{
		ScrollResult result = new ScrollResult();
		args.session = getSession();
		result.list = StatelessDAO.scrollArticleComments(args);
		result.count = StatelessDAO.countArticleComments(args.session, args.sheetId);
		return result;
	}


	@Override
	public void test(){
		Session session = getSession();
		Transaction t = session.beginTransaction();
		try {
//			HibernatePersistenceManager.test(session);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			e.printStackTrace();
		}
	
	}
	
	@Override
	public Float findBookRatingByUser(Long bookId,Long userAccountId) 
	throws Exception{
		try{
			return StatelessDAO.findRatingByUser(getSession(),bookId,userAccountId);
		}
		catch(Exception exp){
			throw exp;
		}
	}
	@Override
	public void submitArticleComment(SubmitArticleCommentArgs args) throws Exception{
		Transaction transaction = null;
		try {
			args.session = getSession();
			transaction = args.session.beginTransaction();
			StatelessDAO.submitArticleComment(args);
			transaction.commit();
//			if(!args.userAccountId.equals(StatelessDAO.getPublisherAccountId(args.session,args.bookId))){
//				mailService.sendBookCommentNotification(args);
//			}
		} 
		catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
	}
	
	@Override
	public Long submitBookComment(SubmitBookCommentArgs args) throws Exception{
		Long newReviewsCount = null;
		Transaction transaction = null;
		try {
			args.session = getSession();
			transaction = args.session.beginTransaction();
			StatelessDAO.submitReview(args);
			newReviewsCount = StatelessDAO.getRatingCount(args.session, args.bookId);
			transaction.commit();
			if(!args.userAccountId.equals(StatelessDAO.getPublisherAccountId(args.session,args.bookId))){
				mailService.sendBookCommentNotification(args);
//				System.out.println("-----------Sending email notification");
			}
//			else{
//				System.out.println("-----------Not sending email notification");
//			}
			
		} 
		catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
		return newReviewsCount;
	}
	
	@Override
	public Map<String,Object> submitBookRating(SubmitBookRatingArgs args) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Transaction transaction = null;
		try {
			args.session = getSession();
			transaction = args.session.beginTransaction();
			StatelessDAO.submitBookRating(args);
			resultMap.put("ratingAverage", StatelessDAO.getRatingAverage(args.session, args.bookId));
			resultMap.put("ratingCount", StatelessDAO.getRatingCount(args.session, args.bookId));
			transaction.commit();
			
		} catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
		return resultMap;
	}
	
	@Override
	public Map<String,Object> submitAudioFileRating(SubmitAudioFileRatingArgs args) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Transaction transaction = null;
		try {
			args.session = getSession();
			transaction = args.session.beginTransaction();
			StatelessDAO.submitAudioFileRating(args);
			resultMap.put("ratingAverage", StatelessDAO.getAudioFileRatingAverage(args.session, args.audioFileId));
			resultMap.put("ratingCount", StatelessDAO.getAudioFileRatingCount(args.session, args.audioFileId));
			transaction.commit();
			
		} catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
		return resultMap;
	}
	
	
	/**
	 * counts sheets of the logged in user
	 * @return
	 * @throws YBlobException
	 */
//	public long countSheetRecords(String owner)  throws Exception{
//		try {
//			Session session = getSession();
//			return StatelessDAO.countSheetRecords(session,owner);
//		} 
//		catch (Exception e) {
//			throw new PersistenceException(e);
//		}
//	}
	
	@Override
	public Long countUserSheets(Long ownerAccountId){
		return StatelessDAO.countUserSheets(getSession(), ownerAccountId);
	}
	
	@Override
	public ScrollResult scrollUserItems(ScrollUserItemsArgs args) 
	throws Exception{
		ScrollResult result = new ScrollResult();
		args.session = getSession();
		try {
			result.list  = StatelessDAO.scrollUserItems(args);
			result.count = StatelessDAO.countUserItems(args.session, args.ownerAccountId);
		} catch (Exception e) {
			result.count = -1l;
			throw new PersistenceException(e);
		} 
		return result;
	}
	
	@Override
	public ScrollResult scrollBookUserItems(ScrollUserItemsArgs args) 
	throws Exception{
		ScrollResult result = new ScrollResult();
		args.session = getSession();
		try {
			result.list  = StatelessDAO.scrollBookUserItems(args);
			result.count = (long)StatelessDAO.countBookUserItems(args.session, args.ownerAccountId);
		} catch (Exception e) {
			result.count = -1l;
			throw new PersistenceException(e);
		} 
		return result;
	}
	
	@Override
	public ScrollResult scrollSheetUserItems(ScrollSheetItemsArgs args) 
	throws Exception{
		ScrollResult result = new ScrollResult();
		args.session = getSession();
		try {
			result.list = StatelessDAO.scrollSheetUserItems(args);
			result.count = (long)StatelessDAO.countSheetUserItems(args.session, args.ownerAccountId, args.folder);
		} catch (Exception e) {
			result.count = -1l;
			throw new PersistenceException(e);
		} 
		return result;
	}
	
//	@Override
//	public ScrollResult<UserItem> scrollUserItemsForWorkdesk(ScrollUserItemsArgs args) 
//	throws Exception{
//		ScrollResult<UserItem> result = new ScrollResult<UserItem>();
//		args.session = getSession();
//		try {
//			List<UserItem> books  = StatelessDAO.scrollBookUserItems(args);
//			List<UserItem> sheets  = StatelessDAO.scrollSheetUserItems(args);
//			result.list = new ArrayList<UserItem>(books.size() + sheets.size());
//			result.list.addAll(books);
//			result.list.addAll(sheets);
//			
//			long bookCount = (long)StatelessDAO.countBookUserItems(args.session, args.ownerAccountId);
//			long sheetCount = (long)StatelessDAO.countSheetUserItems(args.session, args.ownerAccountId);
//			
//			result.count = Math.max(bookCount, sheetCount);
//		} catch (Exception e) {
//			result.count = -1l;
//			throw new PersistenceException(e);
//		} 
//		return result;
//	}
//	
//	@Override
//	public List<PublicRecord> findMatchingPublicRecords(String phrase,String type) throws Exception{
//		Session session = getSession();
//		List<PublicRecord> list;
//		try {
//			if("GENRE".equals(type)){
//				list = StatelessDAO.selectGenresByPhrase(session,phrase,4);
//			}
//			else{
//				list = StatelessDAO.selectPublicRecordsByPhrase(session,phrase,4);
//			}
//			return list;
//		}
//		catch (Exception exp) {
//			throw exp;
//		}
//	}
	
	@Override
	public ScrollResult scrollAudioAttachmentsByBookId(ScrollAttachmentsArgs args){
		args.session = getSession();
		ScrollResult result = new ScrollResult();
		result.list = StatelessDAO.scrollAudioAttachmentsByBookId(args);
		result.count = StatelessDAO.countAudioAttachmentsByBookId(args);;
		
		return result;
	}
	
	@Override
	public void publishAsArticle(PublishSheetToBlogArgs args){
		args.session = getSession();
		StatelessDAO.publishSheetAsArticle(args);
	}
	
	@Override
	public List<UserItem> findMatchingUserItems(ScrollUserItemsArgs args) throws Exception{
		args.session = getSession();
		List<UserItem> list = new ArrayList<UserItem>();
		try {
			if(args.types == null){
				list = StatelessDAO.selectPublicItemsByPhrase(args);
			}
			else{
				if(args.types.contains("GENRE")){
					list.addAll(StatelessDAO.scrollGenreUserItemsByPhrase(args));
				}
				if(args.types.contains("PBOOK")){
					list.addAll(StatelessDAO.scrollPublishedBookUserItemsByPhrase(args));
				}
				if(args.types.contains("BOOK")){
					list.addAll(StatelessDAO.scrollBookUserItemsByPhrase(args));
				}
				if(args.types.contains("PSHEET")){
					args.permissionsMask = Constants.SheetPermission.PUBLIC_GROUP_VIEW;
					args.ownerAccountId = null; //select from all users
					list.addAll(StatelessDAO.scrollSheetUserItemsByPhrase(args));
				}
				else if(args.types.contains("SHEET")){
					//if PSHEET is selected, all sheets including private will be included
					//by the previous query
					args.permissionsMask = 0;
					list.addAll(StatelessDAO.scrollSheetUserItemsByPhrase(args));
				}
				else if(args.types.contains("ARTICLE")){
					//if PSHEET is selected, all sheets including private will be included
					//by the previous query
					args.permissionsMask = 0;
					list.addAll(StatelessDAO.scrollArticleUserItemsByPhrase(args));
				}
				
				else if(args.types.contains("USER")){
					list.addAll(StatelessDAO.scrollAccountsLikeName(args));
				}
//				if(args.types.contains("IMAGE")){
//					list.addAll(StatelessDAO.scrollImageUserItems(args));
//				}
			}
//			for(UserItem i : list){
//				System.out.println("----> " + i.type);
//			}
			return list;
		}
		catch (Exception exp) {
			throw exp;
		}
	}
	
	@Override
	public List<String> selectUserAlbums(Long userAccountId){
		return StatelessDAO.selectUserAlbums(getSession(), userAccountId);
	}
	
	@Override
	public List<String> selectUserFolders(Long userAccountId){
		return StatelessDAO.selectUserFolders(getSession(), userAccountId);
	}
	
	@Override
	public List<Blog> getUserBlogs(Long userAccountId){
		return StatelessDAO.selectUserBlogs(getSession(), userAccountId);
	}
	
	@Override
	public void incrementSheetViewsCount(Long sheetId){
		Transaction transaction = null;
		try{
			Session session = getSession();
			transaction = session.beginTransaction();
			StatelessDAO.incrementSheetViewsCount(getSession(), sheetId);
			transaction.commit();
		}
		catch(Exception exp){
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
	}
	
	@Override
	public ScrollResult scrollImageFileIdsByAlbum(ScrollImageFileArgs args)
	throws Exception{
		ScrollResult result = new ScrollResult();
		try {
			args.session = getSession();
			result.list = StatelessDAO.scrollImageFileIdsByAlbum(args);
			result.count = StatelessDAO.countImageFileIdsByAlbum(args);
		} catch (Exception exp) {
			throw exp;
		}
		return result;
	}

	@Override
	public ImageFile findImageFile(long id){
		Session session = getSession();
		return StatelessDAO.findImageFile(session,id);
	}
	
	@Override
	public GenreDetails findGenreDetails(Long genreId) throws Exception{
		return StatelessDAO.findGenreDetails(getSession(), genreId);
	}
	
	@Override
	public Long countBookSheets(Long bookId){
		return StatelessDAO.countSheets(getSession(), bookId);
	}
	
	@Override
	public List<GenreTitle> findDerivedGenresByBook(Long bookId){
		return StatelessDAO.findDerivedPublicationGenresByBook(getSession(), bookId);
	}
	
	@Override
	public ScrollResult scrollGenres(ScrollGenreArgs args) 
	throws Exception{
		ScrollResult result = new ScrollResult();
		try {
			args.session = getSession();
			result.list = StatelessDAO.scrollGenreDetails(args);
			result.count = StatelessDAO.countGenres(args.session, args.conditionValue);
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		return result;
	}

	@Override
	public Attachment findAttachment(Long attachmentId){
		return StatelessDAO.findAttachment(getSession(),attachmentId);
	}
	
	@Override
	public AudioFile findAudioFile(Long audioFileId){
		return StatelessDAO.findAudioFile(getSession(),audioFileId);
	}
	
	@Override
	public Integer incrementUserDownloadsCount(IncrementDownloadCountArgs args) 
	throws Exception{
		Integer downloadsCount = null;
		Transaction transaction = null;
		try{
			args.session = getSession();
			transaction = args.session.beginTransaction();
			downloadsCount = StatelessDAO.incrementUserDownloadsCount(args);
			
			transaction.commit();
		}
		catch(Exception exp){
			if(transaction != null){
				transaction.rollback();
			}
			throw exp;
		}
		return downloadsCount;
	}

//	@Override
//	public FileItem generatePDF(long bookId,String userName){
//		FileItem fileItem = null;
//		Session session = getSession();
//		try {
//			fileItem = StatelessDAO.generatePDF(session,bookId, userName);
//		} 
//		catch (IllegalStateException exp) {
//			logger.error(exp.getMessage(), exp);
//		} 
//		catch (InterruptedException exp) {
//			logger.error(exp.getMessage(), exp);
//		}
//		return fileItem;
//	}
	
	//----
	
	@Override
	public ImageFile storeImage(FileItemArgs args) throws Exception{
		ImageFile imageFile = null;
		Transaction transaction = null;
		try {
			args.session = getSession();
			transaction = args.session.beginTransaction();
			imageFile = StatelessDAO.storeImageIn(args);
			transaction.commit();
		} 
		catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			if(imageFile != null){
				imageFile.delete();
			}
		}
		
		return imageFile;
	}
	
//	@Override
//	public ImageFile updateUserProfileImage(FileItemArgs args) throws Exception{
//		ImageFile imageFile = null;
//		Transaction transaction = null;
//		try {
//			args.session = getSession();
//			transaction = args.session.beginTransaction();
//			imageFile = StatelessDAO.updateProfileImage(args);
//			transaction.commit();
//		} 
//		catch (Exception exp) {
//			if(transaction != null){
//				transaction.rollback();
//			}
//			if(imageFile != null){
//				imageFile.delete();
//			}
//			throw exp;
//		}
//		
//		return imageFile;
//	}
	@Override
	public Long newGenre(NewGenreArgs args) throws Exception{
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		
		Date currentTime = new Date(System.currentTimeMillis());
		ImageFile imageFile = null;
		Genre genre = new Genre();
		try {
			if(args.coverImageFileItem != null){
				//storeImageInGenres has its own transaction to deal with disk cleanup
//				imageFile = StatelessDAO.storeImageIn(args.session,args.coverImageFileItem,ImageFile.SystemFolder.GENRES.toString(),args.userAccountId);
				FileItemArgs fileArgs = new FileItemArgs();
				fileArgs.session = args.session;
				fileArgs.systemFolder = ImageFile.SystemFolder.GENRES;
				fileArgs.userAccountId = args.userAccountId;
				fileArgs.fileItem = args.coverImageFileItem;
				
				imageFile = StatelessDAO.storeImageIn(fileArgs);
				if(imageFile != null){
					genre.setIconImageFileId(imageFile.getImageFileId());
					System.out.println("WebBean.newGenre: ");
				}
			}
			genre.setDescription(args.description);
			genre.setName(args.name);
			genre.setCreatedOn(currentTime);
			genre.setOriginatorAccountId(args.userAccountId);
			genre.setCreatedByIP(args.ipAddress);
			StatelessDAO.insertGenre(args.session, genre);
			
			transaction.commit();
			args.session.flush();
			
			return genre.getGenreId();
		} 
		catch (Exception exp) {
			transaction.rollback();
			if(imageFile != null){
				imageFile.delete();
			}
			throw exp;
		}
	}
	
	@Override
	public int updateGenre(UpdateGenreArgs args) throws Exception{
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		int updatesCount = 0;
		try {
			StatelessDAO.updateGenre(args);
			updatesCount = 1;
			transaction.commit();
			args.session.flush();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
		
		return updatesCount;
	}
	
	@Override
	public Long newSheet(NewSheetArgs args) throws Exception{
		Long sheetId = null;
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		try {
			sheetId = StatelessDAO.newSheet(args).getSheetId();
			transaction.commit();
		}
		catch(DataException exp){
			if(exp.getCause() != null){
				if(exp.getCause() instanceof MysqlDataTruncation){
					throw new com.branchitup.exception.InvalidInputException("The sheet's content exceeded the characters limit, try breaking down the content into several sheets.", exp);
				}
				else{
					exp.printStackTrace();
				}
			}
		}
		catch(GenericJDBCException exp){
			throw new com.branchitup.exception.InvalidInputException("The sheet contains invalid characters", exp);
		}
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
		return sheetId;
	}
	
	@Override
	public Sheet findSheet(Long sheetId) throws Exception{
		try {
			return StatelessDAO.findSheet(getSession(), sheetId);
		} 
		catch (Exception exp) {
			throw exp;
		}
	}
	
	@Override
	public AudioFile attachAudioFile(AttachAudioArgs args) throws Exception{
		args.session = getSession();
		AudioFile audioFile = null;
		Transaction transaction = null;
		File file = null;
		try {
			file = FileUtilities.writeAudioFileToDisk(args);
			if(file.exists()){
				args.fileName = file.getName();
				transaction = args.session.beginTransaction();
				audioFile = StatelessDAO.insertAudioAttachment(args);
				transaction.commit();
			}
			
		} 
		catch(InvalidAudioFrameException exp){
			throw new InvalidAudioFrameException("The audio file is not supported, please upload only a valid MP3 file");
		}
		catch(CannotReadException exp){
			throw new CannotReadException("The audio file is not supported, please upload only a valid MP3 file");
		}
		catch (Exception exp) {
			if(transaction != null){
				transaction.rollback();
			}
			if(audioFile != null){
				FileUtilities.delete(audioFile);
			}
			if(file != null && file.exists()){
				file.delete();
			}
			throw exp;
		}
		return audioFile;
	}
	
	@Override
	public void updateSheet(UpdateSheetArgs args) throws Exception, UnauthorizedException{
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		try {
			StatelessDAO.updateSheet(args);
			transaction.commit();
			args.session.flush();
		} 
		catch (UnauthorizedException exp) {
			transaction.rollback();
			throw exp;
		}
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
//	@Override
//	public Long getSheetOwnerAccountId(Long sheetId){
//		return StatelessDAO.getSheetOwnerAccountId(getSession(), sheetId);
//	}
	
	@Override
	public Long getBookOwnerAccountId(Long bookId){
		return StatelessDAO.getBookOwnerAccountId(getSession(), bookId);
	}
	
	@Override
	public BookDetails findBookDetails(Long bookId){
		return StatelessDAO.findBookDetails(getSession(), bookId);
	}
	
	@Override
	public BookDetailsSheet findBookDetailSheet(Long bookId, Long sheetId){
		return StatelessDAO.findBookDetailsSheet(getSession(), bookId, sheetId);
	}

	@Override
	public void updateBook(UpdateBookArgs args) throws Exception,
	UnauthorizedException {
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction(); 
		try {
			StatelessDAO.updateBook(args);
			transaction.commit();
			args.session.flush();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
	@Override
	public Long newBook(NewBookArgs args) throws Exception,
	UnauthorizedException {
		Long bookId = null;
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction(); 
		try {
			bookId = StatelessDAO.newBook(args).getBookId();
			transaction.commit();
			args.session.flush();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
		return bookId;
	}
	
	
	//----------------
	
//	private static Attachment generatePDFAttachment(Session session, PublishedBook pb) throws IOException, InterruptedException{
//		System.out.println("----->generatePDFAttachment:1 " + pb);
//		Attachment attachment = null;
//		Map<String,Object> hints = new Hashtable<String, Object>();
//
//		File pdfFile = ServiceLocator.getLastInstance().getWkExporter().export(pb,hints);
//		//	File pdfFile = WKExporter.getInstance(this).export(pb,hints);
//		//	File pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".PDF"); //***TEST
//		System.out.println("----->generatePDFAttachment:2 " + pdfFile);
//		System.out.println("THE PDF FILE IS: " + pdfFile.getAbsoluteFile());
//		if(pdfFile != null){
//
//			StatelessDAO.insertPDFAttachment(session, pb.getBookId(), pdfFile);
//		}
//		return attachment;
//}

//	private void schedulePDF(Long bookId) throws SchedulerException{
//		Date date = new Date(System.currentTimeMillis() + Constants.PUBLISH_INTERVAL);
//		JobDataMap dataMap = new JobDataMap();
//		dataMap.put("bookId", bookId);
//		//the name will help us cancel the job if we want
//		String name = "PublishBook." + bookId;
//		Trigger trigger = new SimpleTrigger(name,date);
//		trigger.setJobDataMap(dataMap);
//		scheduler.scheduleJob(pdfJobDetail, trigger);
//		
////		System.out.println("----->schedulePDF started!!!" + HibernatePersistenceManager.findPublishedBook(session,p.getBookId()));
//	}
	
//	@Override
//	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//		System.out.println("----->GeneratePDFJob.start!!!!" + 
//			context.getTrigger().getJobDataMap() + ", " + context.getJobDetail().getJobDataMap()
//		);
//		Long bookId = (Long)context.getTrigger().getJobDataMap().get("bookId");
//		Session session = getSession();
//		System.out.println("----->GeneratePDFJob.executeInternal is now Executing!!!!" + bookId);
//		PublishedBook publication = null;
//		Transaction transaction = session.beginTransaction();
//		try {
//			System.out.println("TIMEOUT.before find." + bookId);
//			publication = StatelessDAO.findPublishedBook(session,bookId);
//			System.out.println("TIMEOUT.after find." + bookId);
//			if(publication != null){
//				System.out.println("----->TIMEOUT.start2." + publication);
//				generatePDFAttachment(session, publication);
//				System.out.println("----->TIMEOUT.after pdf." + publication.getBookId());
//				StatelessDAO.setPublicationStatus(session, publication.getBookId(), PublishedBook.Status.ACTIVE);
//			}
//			System.out.println("----->GeneratePDFJob.executeInternal. before commit");	
//			transaction.commit();
//			System.out.println("----->GeneratePDFJob.executeInternal. after commit");
//			session.flush();
//		}
//		catch(Exception exp){
//			exp.printStackTrace();
////			logger.trace(exp);
//			transaction.rollback();
//			
//			if(publication != null){
//				publication.setStatus(PublishedBook.Status.REMOVED);
//			}
//		}
//	}
	
	//-----------
	
	@Override
	public Long publishBook(PublishBookArgs args) throws Exception{
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		PublishedBook newBook = null;
		
		try {
			newBook = StatelessDAO.publishBook(args);
			transaction.commit();
			
			if(newBook != null){
				//1. creates pdf 2. activate the publication record by changing its status to active
				scheduledService.generatePDF(newBook.getBookId());
				
				mailService.sendBranchNotification(newBook);
//				PublishedBook parentBook = newBook.getParentBook();
//				
//				System.out.println("The Book ID is :: " + newBook.getBookId() + 
//						", parent Book: " + newBook.getParentBook() + ", " + newBook.getParentId());
//				if(parentBook != null){
//					mailService.sendBranchNotification(parentBook.getPublisherAccount().getEmail(),
//							parentBook.getTitle(), 
//							newBook.getPublisherAccount().getFirstName() + " " + newBook.getPublisherAccount().getLastName(),
//							newBook.getBookId());
//				}
				
			}
		} 
		catch (Exception exp) {
			if(args.coverImageFile != null){
				args.coverImageFile.delete();
			}
			transaction.rollback();
			throw exp;
		}
		
		return newBook.getBookId();
	}
	
	@Override
	public Long branchPublishedBook(BranchPublishedBookArgs args)
	throws HibernateException {
		Long bookId = null;
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction(); 
		try {
			Book book = StatelessDAO.branchPublishedBook(args);
			transaction.commit();
			if(book != null){
				bookId = book.getBookId();
			}
		} 
		catch (HibernateException exp) {
			transaction.rollback();
			throw exp;
		} 
		return bookId;
	}

	@Override
	public Map<String,String> getSupportedLanguages(){
		return SystemAttributes.getInstance().getLanguageMap();
//		Iterator<Entry<String,String>> itr = map.entrySet().iterator();
//		while(itr.hasNext()){
//			
//		}
//		for()
//		List<Language> list = new ArrayList<Language>();
//		list.add(new Language(null, "Any"));
//		list.add(new Language("ENG", "English"));
//		list.add(new Language("SPA", "Spanish"));
//		list.add(new Language("HEB", "Hebrew"));
//		return list;
	}

	@Override
	public boolean deleteSheet(Long sheetId, Long userAccountId) 
	throws Exception{
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		int recordsCount = 0;
		try {
			 recordsCount = StatelessDAO.deleteSheet(getSession(),sheetId, userAccountId);
			 transaction.commit();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
		return recordsCount > 0;
	}
	
	@Override
	public int deleteBook(Long bookId, Long userAccountId) throws Exception{
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		int recordsCount = 0;
		try {
			 recordsCount = StatelessDAO.deleteBook(getSession(),bookId, userAccountId);
			 transaction.commit();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
//		System.out.println("--->recordsCount " + recordsCount);
		return recordsCount;
	}

	
	/**
	 * delete publication completely if it is not referenced by any other book.
	 * otherwise:
	 * mark the specified publication and its sheets as inactive
	 */
	@Override
	public int deletePublication(Long bookId, Long userAccountId) throws Exception{
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		int recordsCount = 0;
		try {
			 recordsCount = StatelessDAO.deletePublishedBook(getSession(),bookId, userAccountId);
			 transaction.commit();
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
//		System.out.println("--->deletePublication.recordsCount " + recordsCount);
		return recordsCount;
	}
	@Override
	public int clearUserProfileImages(Long userAccountId) throws Exception{
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		int updatesCount = 0;
		try {
			updatesCount = StatelessDAO.deleteUserProfileImagesByAccountId(session, userAccountId);
			transaction.commit();
			session.flush();
			return updatesCount;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	@Override
	public int updateUserProfileInfo(UserProfileInfoArgs args) throws Exception{
		args.session = getSession();
		int totalUpdates;
		Transaction transaction = args.session.beginTransaction();
		try {
			totalUpdates = StatelessDAO.updateUserAccount(args);
			transaction.commit();
			
			return totalUpdates;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}

	@Override
	public int updateEmailAddress(UserProfileInfoArgs args) throws Exception {
		args.session = getSession();
		int totalUpdates;
		Transaction transaction = args.session.beginTransaction();
		try {
			totalUpdates = StatelessDAO.updateProfileEmailAddress(args);
			transaction.commit();
			
			return totalUpdates;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
	@Override
	public void updateProfilePassword(UserProfileInfoArgs args) throws Exception {
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		try {
			UserAccount userAccount = StatelessDAO.findUserAccount(args.session, args.userAccountId);
			if(!StringUtils.isEmpty(args.newPassword)){
				if(args.newPassword.equals(args.retypePassword)){
					String currentEncryptedPassword = passwordEncoder.encodePassword(args.currentPassword, saltSource.getSalt(args.userDetails));
					if(StringUtils.isEmpty(userAccount.getPassword()) || userAccount.getPassword().equals(currentEncryptedPassword)){
						String encryptedPassword = passwordEncoder.encodePassword(args.newPassword, saltSource.getSalt(args.userDetails));						
						userAccount.setPassword(encryptedPassword);
						logger.debug("--->password set to " + encryptedPassword);
						transaction.commit();
						args.session.flush();
					}
					else{
						throw new InvalidInputException("The current password you provided does not match the value we have in our records, please fix and re-submit!");
					}
				}
				else{
					throw new InvalidInputException("Your passwords do not match, please retype passwords and submit again!");
				}
			}
			
		} 
		catch (Exception exp) {
//			logger.error(exp.getMessage(), exp);
			logger.error(exp);
			transaction.rollback();
			throw exp;
		}
	}
	
	@Override
	public int updateProfileAboutMe(UserProfileInfoArgs args) throws Exception {
		args.session = getSession();
		int totalUpdates;
		Transaction transaction = args.session.beginTransaction();
		try {
			totalUpdates = StatelessDAO.updateProfileAboutMe(args);
			transaction.commit();
			
			return totalUpdates;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
	@Override
	public int updateProfileVisibility(UserProfileInfoArgs args) throws Exception {
		args.session = getSession();
		int totalUpdates;
		Transaction transaction = args.session.beginTransaction();
		try {
			totalUpdates = StatelessDAO.updateProfileVisibility(args);
			transaction.commit();
			
			return totalUpdates;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
	@Override
	public UserAccount newUserAccount(NewUserAccountArgs args) throws Exception{
		args.session = getSession();
		Transaction transaction = args.session.beginTransaction();
		try {
			
			System.out.println("--->>>UserDetails " + args.userDetails);
			//^^p
			if(args.userDetails != null){
				args.password = passwordEncoder.encodePassword(args.password, saltSource.getSalt(args.userDetails)); //^^p
			}
			else{
				args.password = passwordEncoder.encodePassword(args.password,args.email); //^^p
			}
			
			UserAccount userAccount = StatelessDAO.newUserAccount(args);
			transaction.commit();
			
			return userAccount;
		} 
		catch(ConstraintViolationException exp){
			throw new UserAccountException("Email " + args.email + " already exists", "Account Exists");
		}
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}

	@Override
	public String resetUserAccountPassword(UserAccount userAccount) throws Exception {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			String newPassword = UUID.randomUUID().toString().substring(0, 10);
			String encodedPassword = passwordEncoder.encodePassword(newPassword, userAccount.getEmail());
					
			StatelessDAO.updateUserAccountPassword(session,userAccount.getUserAccountId(),encodedPassword);
			transaction.commit();
			
			return newPassword;
		} 
		catch (Exception exp) {
			transaction.rollback();
			throw exp;
		}
	}
	
	

	//^^1
//	@Override
//	public void resetUserAccountPassword(ResetPasswordArgs args) throws Exception {
//		args.session = getSession();
//		Transaction transaction = args.session.beginTransaction();
//		try {
//			UserAccount userAccount = StatelessDAO.findUserAccountByToken(args.session, args.token);
//			if(userAccount != null){
//				Date expiresDate = new Date(System.currentTimeMillis()-604800000l); //7 days
//				if(expiresDate.before(userAccount.getResetPasswordTokenExpires())){
//					StatelessDAO.updateUserAccountPassword(args.session, userAccount.getUserAccountId(), args.newPassword);
//					
//					transaction.commit();
//				}
//				else{
//					throw new UserPasswordTokenExpiresException();
//				}
//			}
//			else{
//				throw new NoSuchItemException("Acount not found");
//			}
//		} 
//		catch (Exception exp) {
//			transaction.rollback();
//			throw exp;
//		}
//	}
	
	
	
	//--
	
	
}
