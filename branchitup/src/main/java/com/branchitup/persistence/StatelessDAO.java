package com.branchitup.persistence;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import com.branchitup.exception.InvalidInputException;
import com.branchitup.exception.NoSuchItemException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.exception.UserAccountException;
import com.branchitup.exception.runtime.BookNotFoundException;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.AudioFileDetails;
import com.branchitup.persistence.entities.AudioFile;
import com.branchitup.persistence.entities.AudioFileRating;
import com.branchitup.persistence.entities.Blog;
import com.branchitup.persistence.entities.Book;
import com.branchitup.persistence.entities.BookDetails;
import com.branchitup.persistence.entities.BookDetailsSheet;
import com.branchitup.persistence.entities.BookShowcase;
import com.branchitup.persistence.entities.Genre;
import com.branchitup.persistence.entities.GenreDetails;
import com.branchitup.persistence.entities.GenreTitle;
import com.branchitup.persistence.entities.ImageFile;
import com.branchitup.persistence.entities.ImageFileId;
import com.branchitup.persistence.entities.ParentBook;
import com.branchitup.persistence.entities.PublishedBookDetails;
import com.branchitup.persistence.entities.PublicationTitle;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.PublishedBook_Genre;
import com.branchitup.persistence.entities.PublishedSheet;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.Rating;
import com.branchitup.persistence.entities.Review;
import com.branchitup.persistence.entities.ReviewRate;
import com.branchitup.persistence.entities.Sheet;
import com.branchitup.persistence.entities.ArticleComment;
import com.branchitup.persistence.entities.Sheet_Book;
import com.branchitup.persistence.entities.Test;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.persistence.entities.UserDownloadsCount;
import com.branchitup.persistence.entities.UserName;
import com.branchitup.persistence.entities.UserItem;
import com.branchitup.persistence.entities.UserWallRecord;
import com.branchitup.system.Constants;
import com.branchitup.system.FileUtilities;
import com.branchitup.system.SystemAttributes;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.BranchPublishedBookArgs;
import com.branchitup.transfer.arguments.FileItemArgs;
import com.branchitup.transfer.arguments.IncrementDownloadCountArgs;
import com.branchitup.transfer.arguments.NewSheetArgs;
import com.branchitup.transfer.arguments.PublishSheetToBlogArgs;
import com.branchitup.transfer.arguments.ScrollArticleCommentsArgs;
import com.branchitup.transfer.arguments.ScrollAttachmentsArgs;
import com.branchitup.transfer.arguments.ScrollBookShowcaseArgs;
import com.branchitup.transfer.arguments.ScrollGenreArgs;
import com.branchitup.transfer.arguments.ScrollImageFileArgs;
import com.branchitup.transfer.arguments.ScrollPublicationsArgs;
import com.branchitup.transfer.arguments.ScrollReviewsArgs;
import com.branchitup.transfer.arguments.NewBookArgs;
import com.branchitup.transfer.arguments.NewUserAccountArgs;
import com.branchitup.transfer.arguments.PublishBookArgs;
import com.branchitup.transfer.arguments.ScrollSheetItemsArgs;
import com.branchitup.transfer.arguments.ScrollUserAccountsArgs;
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

//@Service
public class StatelessDAO {	
	private static Logger logger = Logger.getLogger(StatelessDAO.class);
	/*
	 * cast to a different type
	 */
	@SuppressWarnings("unchecked")
	static public <T> T uncheckedCast(Object obj) {
		return (T)obj;
	}
	
	public static Attachment findAttachment(Session session,Long attachmentId){
		return (Attachment)session.load(Attachment.class, attachmentId);
	}
	
	public static AudioFile findAudioFile(Session session,Long audioFileId){
		return (AudioFile)session.load(AudioFile.class, audioFileId);
	}
	
	public static Book findBook(Session session,long bookId){
		return (Book)session.load(Book.class, bookId);
	}
	
	public static String getBookTitle(Session session,long bookId){
		Query query = session.getNamedQuery("Book.selectTitleById");
		query.setLong("bookId", bookId);
		return (String)query.uniqueResult();
	}
	
	public static FileItem generatePDF(Session session,long bookId,String username) throws InterruptedException{
		FileItem fileItem = null;
		
		Book book = findBook(session,bookId);
		if(book != null){
//			ExportableBook xbook = new ExportableBook(book,this.selectUserAccount(username));
//			fileItem = WKExporter.getInstance().export(xbook);
		}
		
		return fileItem;
	}
	public static void submitReview(SubmitBookCommentArgs args){
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Review review = new Review();
		review.setBookId(args.bookId);
		review.setComment(args.comment);
		review.setUserAccountId(args.userAccountId);
		review.setCreatedOn(currentTime);
		args.session.persist(review);
	}
	
	public static void submitArticleComment(SubmitArticleCommentArgs args){
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		ArticleComment c = new ArticleComment();
		c.setSheetId(args.sheetId);
		c.setComment(args.comment);
		c.setUserAccountId(args.userAccountId);
		c.setCreatedOn(currentTime);
		args.session.persist(c);
	}
	
	public static int submitAudioFileRating(SubmitAudioFileRatingArgs args){
		int result = 0;
		Query query = args.session.getNamedQuery("AudioFileRating.countByUserAndId");
		query.setLong("userAccountId", args.userAccountId);
		query.setLong("audioFileId", args.audioFileId);
		Number count = (Number)query.uniqueResult();
		
		if(count.intValue() > 0){
			query = args.session.getNamedQuery("AudioFileRating.updateRateByUserAndId");
			query.setLong("userAccountId", args.userAccountId);
			query.setLong("audioFileId", args.audioFileId);
			query.setFloat("rate", args.rate);
			result = query.executeUpdate();
		}
		else{
			AudioFileRating rating = new AudioFileRating();
			rating.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			rating.setRate(args.rate);
			rating.setUserAccountId(args.userAccountId);
			rating.setAudioFileId(args.audioFileId);
			args.session.persist(rating);
			args.session.flush();
			result = 1;
		}
		return result;
	}
	
	public static int submitBookRating(SubmitBookRatingArgs args){
		int result = 0;
		Query query = args.session.getNamedQuery("Rating.countByUserAndBook");
		query.setLong("userAccountId", args.userAccountId);
		query.setLong("bookId", args.bookId);
		Number count = (Number)query.uniqueResult();
		
		if(count.intValue() > 0){
			query = args.session.getNamedQuery("Rating.updateRateByUserAndBook");
			query.setLong("userAccountId", args.userAccountId);
			query.setLong("bookId", args.bookId);
			query.setFloat("rate", args.rate);
			result = query.executeUpdate();
		}
		else{
			Rating rating = new Rating();
			rating.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			rating.setRate(args.rate);
			rating.setUserAccountId(args.userAccountId);
			rating.setBookId(args.bookId);
			args.session.persist(rating);
			args.session.flush();
			result = 1;
		}
		return result;
	}
	
	public static UserAccount findUserAccount(Session session, Long userAccountId){
		UserAccount account = (UserAccount)session.get(UserAccount.class, userAccountId);
		if(account != null && account.getProfileImageFileId() != null){
			account.setProfileImageUrl(resolveThumbnailUrl(session, account.getProfileImageFileId()));
		}
		return account;
	}
	@SuppressWarnings("unchecked")
	public static List<UserAccount> scrollUserAccount(ScrollUserAccountsArgs args){
		Query query = args.session.getNamedQuery("UserAccount.select");
		query.setMaxResults(args.maxResults);
		query.setFirstResult(args.offset);
		List<UserAccount> list = query.list();
		
//		if(account != null && account.getProfileImageFileId() != null){
//			account.setProfileImageUrl(resolveThumbnailUrl(session, account.getProfileImageFileId()));
//		}
		
		return list;
	}
	
	public static String selectUserEmail(Session session, Long userAccountId){
		Query query = session.getNamedQuery("UserAccount.selectEmail");
		query.setLong("userAccountId", userAccountId);
		
		return (String)query.uniqueResult();
	}
	
	public static Long getPublisherAccountId(Session session, Long bookId){
		Query query = session.getNamedQuery("PublishedBook.selectPublisherAccountId");
		query.setLong("bookId", bookId);
		
		return (Long)query.uniqueResult();
	}
	
	public static String getPublisherComment(Session session, Long bookId){
		Query query = session.getNamedQuery("PublishedBook.selectPublisherComment");
		query.setLong("bookId", bookId);
		
		return (String)query.uniqueResult();
	}
	
	//UserAccount.selectEmail
	public static UserAccount findUserAccountByEmail(Session session,String email){
		Query query = session.getNamedQuery("UserAccount.selectByEmail");
		query.setParameter("email", email);
		UserAccount userAccount = (UserAccount)query.uniqueResult();
		if(userAccount != null && userAccount.getProfileImageFileId() != null){
			userAccount.setProfileImageUrl(resolveThumbnailUrl(session, userAccount.getProfileImageFileId()));
		}
		return userAccount;
	}
	
	public static Long countPublishedBookDownloads(Session session, Long bookId){
		Query query; 
		query = session.createSQLQuery("CALL `userdownloadscount.countByBookGroupByIP`(" + bookId + ")");
		long a = ((BigInteger)query.uniqueResult()).longValue();
		
		query = session.createSQLQuery("CALL `userdownloadscount.countByBookGroupByUserAccount`(" + bookId + ")");
		long b = ((BigInteger)query.uniqueResult()).longValue();

		return a + b;
	}
	
	public static Integer incrementUserDownloadsCount(IncrementDownloadCountArgs args){
		Query query;
		Integer count = null;
		if(args.userAccountId != null){
			query = args.session.getNamedQuery("UserDownloadsCount.countByUserAndBook");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("bookId", args.bookId);
			
			count = (Integer)query.uniqueResult();
		}
		else{
			query = args.session.getNamedQuery("UserDownloadsCount.countAnonymousByIPAndBook");
			query.setParameter("bookId", args.bookId);
			query.setParameter("ipAddress", args.ipAddress);
			
			count = (Integer)query.uniqueResult();
		}
		
		if(count == null){
			UserDownloadsCount c = new UserDownloadsCount();
			c.setBookId(args.bookId);
			c.setUserAccountId(args.userAccountId);
			c.setIpAddress(args.ipAddress);
			c.setDownloadsCount(1);
			
			args.session.persist(c);
			
			count = 1;
		}
		else{
			if(args.userAccountId != null){
				query = args.session.getNamedQuery("UserDownloadsCount.incrementByUserAndBook");
				query.setParameter("userAccountId", args.userAccountId);
				query.setParameter("bookId", args.bookId);
				
				if(query.executeUpdate() > 0){
					count++;
				}
			}
			else{
				query = args.session.getNamedQuery("UserDownloadsCount.incrementAnonymousByIpAndBook");
				query.setParameter("bookId", args.bookId);
				query.setParameter("ipAddress", args.ipAddress);
				
				if(query.executeUpdate() > 0){
					count++;
				}
			}
			
		}
		
		return count;
//		Query query = args.session.createSQLQuery("CALL `userdownloadscount.increment`(" + args.userAccountId + "," + args.bookId + ");");
//		BigInteger count = (BigInteger)query.uniqueResult();
//		return count;
	}
	
//	public static BigInteger incrementUserDownloadsCount(Session session,Long userAccountId,Long bookId){
//		Query query = session.createSQLQuery("CALL `userdownloadscount.increment`(" + userAccountId + "," + bookId + ");");
//		BigInteger count = (BigInteger)query.uniqueResult();
//		return count;
//	}
	
	@SuppressWarnings("unchecked")
	public static List<BookShowcase> scrollBookShowcaseByDownloads(ScrollBookShowcaseArgs args){
		logger.debug("--->StatelessDAP.scrollBookShowcaseByDownloads");
		Query query = args.session.createSQLQuery("CALL `bookshowcase.scrollBookShowcaseByDownloads`(" + args.offset + "," + args.maxResults + ")").addEntity(BookShowcase.class);
		
		
		//countPublishedBookDownloads
		return query.list();
	}
	
	public static Long countPublicationRecords(ScrollPublicationsArgs args){
		Query query;
		//return only books that have matching records on publishedbooks_genres
		if(ScrollPublicationsArgs.ConditionKey.BY_GENRE.equals(args.conditionKey)){
			query = args.session.getNamedQuery("PublishedBook.countByGenre");
			query.setLong("genreId", Long.valueOf(args.conditionValue));
		}
		else if(ScrollPublicationsArgs.ConditionKey.BY_PUBLISHER.equals(args.conditionKey)){
			query = args.session.getNamedQuery("PublishedBook.countByPublisher");
			query.setLong("publisherAccountId", Long.valueOf(args.conditionValue));
		}
		else if(ScrollPublicationsArgs.ConditionKey.BY_PHRASE.equals(args.conditionKey)){
			query = args.session.getNamedQuery("PublishedBook.countLikeBookTitle");
			query.setString("phrase", "%" + args.conditionValue + "%");
		}
		else{
			query = args.session.getNamedQuery("PublishedBook.countActive");
		}
//		System.out.println("---->count " + query.uniqueResult());
		return (Long)query.uniqueResult();
	}
	
	
	public static PublishedBookDetails findPublishedBookDetails(Session session, Long bookId){
		PublishedBookDetails book = null;
		if(bookId != null){
			//PublishedBook.selectByPublishedBookId
			Query query = session.getNamedQuery("PublishedBookDetails.selectByPublishedBookId");
			query.setLong("bookId", bookId);
			
//			Query query = session.createSQLQuery("CALL `publishedbookdetails.findById`(" + bookId + ")").addEntity(PublishedBookDetails.class);
			book = (PublishedBookDetails)query.uniqueResult();
			if(book == null){
				throw new BookNotFoundException("The book could not be found, probably was removed by its owner",bookId);
			}
			if(book.getCoverImageFileId() != null){
				book.setCoverImageUrl(resolveThumbnailUrl(session, book.getCoverImageFileId()));
			}
			book.downloadsCount = countPublishedBookDownloads(session, bookId);
			book.genreTitles = getGenreTitles(session, bookId);
			List<Attachment> attachmentList = selectAttachmentsByBookId(session, bookId);
			for(Attachment a : attachmentList){
				if(AttachmentFileType.PDF.equals(a.getFileType())){
					book.pdfAttachmentId = a.getAttachmentId();
				}
				if(AttachmentFileType.EPUB.equals(a.getFileType())){
					book.epubAttachmentId = a.getAttachmentId();
				}
			}
			if(book.getPublisherRoleMask() != null){
				book.publisherRoleList = toPublisherRoleList(book.getPublisherRoleMask());
			}
			//--
			
			query = session.getNamedQuery("Rating.averageAndCountByBook");
			query.setLong("bookId", book.bookId);
			
			Object[] arr = (Object[])query.uniqueResult();
			if(arr != null && arr.length > 1){
				book.ratingAverage = (Double)arr[0];
				book.ratingCount = (Long)arr[1];
			}
		}
		return book;
	}
//	@SuppressWarnings("unchecked")
//	public static List<PublishedBookDetails> selectPublicationRecords(ScrollPublicationsArgs args){
////		System.out.println("HibernatePersistenceManager:search: " + args.conditionValue + ", " + args.offset + ", " + args.maxResults);
//		SQLQuery query;
//		if(args.conditionKey != null){
//			switch(args.conditionKey){
//			case BY_PHRASE:
////				System.out.println("---->selectPublicationRecords:v: " + ("CALL `branchitup`.`publishedbookdetails.scrollLikeBookTitle`(\"" + v + "\"," + args.offset + "," + args.maxResults + ")") + ", type: " + args.conditionKey + ", " + "the b'".equals(args.conditionValue));
//				query = args.session.createSQLQuery("CALL `branchitup`.`publishedbookdetails.scrollLikeBookTitle`(:phrase," + args.offset + "," + args.maxResults + ")").addEntity(PublishedBookDetails.class);
//				query.setParameter("phrase", args.conditionValue);
//				break;
//			case BY_GENRE:
//				query = args.session.createSQLQuery("CALL `branchitup`.`publishedbookdetails.scrollByGenre`('" + args.conditionValue + "'," + args.offset + "," + args.maxResults + ")").addEntity(PublishedBookDetails.class);
//				break;
//			case BY_PUBLISHER:
//				query = args.session.createSQLQuery("CALL `branchitup`.`publishedbookdetails.scrollByPublisher`(" + args.conditionValue + "," + args.offset + "," + args.maxResults + ")").addEntity(PublishedBookDetails.class);
////				query.setParameter("username", args.conditionValue);
////				System.out.println("StatelessDAO.by publisher " + "CALL `branchitup`.`publishedbookdetails.scrollByPublisher`(" + args.conditionValue + "," + args.offset + "," + args.maxResults + ")");
//				break;
//			default:
//				query = args.session.createSQLQuery("CALL `branchitup`.`publishedbookdetails.scrollLikeBookTitle`(:phrase," + args.offset + "," + args.maxResults + ")").addEntity(PublishedBookDetails.class);
//				query.setParameter("phrase", args.conditionValue);
//				break;
//			}
//		}
//		else{
//			query = args.session.createSQLQuery("CALL `branchitup`.`publishedbookdetails.scrollActive`(" + args.offset + "," + args.maxResults + ")").addEntity(PublishedBookDetails.class);
//		}
//		List<PublishedBookDetails> list = query.list();
////		System.out.println("---->selectPublicationRecords: " + list.size() + ", type: " + args.conditionKey);
//		Map<String,String> languageMap = SystemAttributes.getInstance().getLanguageMap();
//		for(PublishedBookDetails r : list){
//			r.genreTitles = getGenreTitles(args.session, r.bookId);
//			if(r.getCoverImageFileId() != null){
//				r.setCoverImageUrl(resolveThumbnailUrl(args.session, r.getCoverImageFileId()));
//			}
//			r.bookLanguage = languageMap.get(r.bookLanguage);
//			
//			r.downloadsCount = countPublishedBookDownloads(args.session, r.bookId);
//		}
//		return list;
//	}
	
	@SuppressWarnings("unchecked")
	public static List<PublishedBookDetails> selectPublicationRecords(ScrollPublicationsArgs args){
		Query query;
		if(args.conditionKey != null){
			switch(args.conditionKey){
			case BY_PHRASE:
				query = args.session.getNamedQuery("PublishedBookDetails.selectLikeTitle");
				query.setParameter("phrase", "%" + args.conditionValue + "%");
				break;
			case BY_GENRE:
				query = args.session.getNamedQuery("PublishedBookDetails.selectByGenre");
				query.setLong("genreId", Long.valueOf(args.conditionValue));
				break;
			case BY_PUBLISHER:
				query = args.session.getNamedQuery("PublishedBookDetails.selectByPublisher");
				query.setLong("publisherAccountId", Long.valueOf(args.conditionValue));
				break;
			default:
				query = args.session.getNamedQuery("PublishedBookDetails.selectLikeTitle");
				query.setParameter("phrase", "%" + args.conditionValue + "%");
				break;
			}
		}
		else{
			query = args.session.getNamedQuery("PublishedBookDetails.selectActive");
		}
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		List<PublishedBookDetails> list = query.list();
		Map<String,String> languageMap = SystemAttributes.getInstance().getLanguageMap();
		for(PublishedBookDetails r : list){
//			System.out.println("-------------->" + r.ratingCount + ", " + r.ratingAverage);
//			query = args.session.getNamedQuery("Rating.averageAndCountByBook");
//			query.setLong("bookId", r.bookId);
//			
//			Object[] arr = (Object[])query.uniqueResult();
//			if(arr != null && arr.length > 1){
//				r.ratingAverage = (Double)arr[0];
//				r.ratingCount = (Long)arr[1];
//			}
			r.genreTitles = getGenreTitles(args.session, r.bookId);
			if(r.getCoverImageFileId() != null){
				r.setCoverImageUrl(resolveThumbnailUrl(args.session, r.getCoverImageFileId()));
			}
			//no cover image, put user profile image if available
			else if(r.getPublisherProfileImageFileId() != null){
				r.setCoverImageUrl(resolveThumbnailUrl(args.session, r.getPublisherProfileImageFileId()));
			}
			r.bookLanguage = languageMap.get(r.bookLanguage);
			r.downloadsCount = countPublishedBookDownloads(args.session, r.bookId);
			
			List<Attachment> attachmentList = selectAttachmentsByBookId(args.session, r.bookId);
			for(Attachment a : attachmentList){
				if(AttachmentFileType.PDF.equals(a.getFileType())){
					r.pdfAttachmentId = a.getAttachmentId();
				}
				if(AttachmentFileType.EPUB.equals(a.getFileType())){
					r.epubAttachmentId = a.getAttachmentId();
				}
			}
			
			if(r.getPublisherRoleMask() != null){
				r.publisherRoleList = toPublisherRoleList(r.getPublisherRoleMask());
			}
			
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<PublishedBook> selectPublications(ScrollPublicationsArgs args){
		Query query = args.session.getNamedQuery("PublishedBook.selectActive");
		List<PublishedBook> list = query.list();
		return list;
	}
	
	public static List<BookRole> toPublisherRoleList(int publisherRoleMask){
		List<BookRole> list = new ArrayList<BookRole>();
		BookRole[] roles = BookRole.values();
		for(int i = 0 ; i < roles.length ; i++){
			if((publisherRoleMask & roles[i].maskVal) != 0){
				list.add(roles[i]);
			}
		}
		return list;
	}
	
	public static Float findRatingByUser(Session session, Long publishedBookId, Long userAccountId){
		Query query = session.getNamedQuery("Rating.selectRateByUserAndBook");
		query.setParameter("bookId", publishedBookId);
		query.setParameter("userAccountId", userAccountId);
		
		return (Float)query.uniqueResult();
	}
	
	public static Double getRatingAverage(Session session,long publishedBookId){
		Query query = session.getNamedQuery("Rating.ratingAverageByBook");
		query.setLong("bookId", publishedBookId);
		return (Double)query.uniqueResult();
	}

	public static Long getRatingCount(Session session,long publishedBookId){
		Query query = session.getNamedQuery("Rating.ratingCountByBook");
		query.setLong("bookId", publishedBookId);
		return (Long)query.uniqueResult();
	}
	
	public static Double getAudioFileRatingAverage(Session session,long audioFileId){
		Query query = session.getNamedQuery("AudioFileRating.ratingAverageById");
		query.setLong("audioFileId", audioFileId);
		return (Double)query.uniqueResult();
	}

	public static Long getAudioFileRatingCount(Session session,long audioFileId){
		Query query = session.getNamedQuery("AudioFileRating.ratingCountById");
		query.setLong("audioFileId", audioFileId);
		return (Long)query.uniqueResult();
	}
//	public static BigInteger findAttachmentIdByBookId(Session session,long publishedBookId){
////		Session s = this.sessionFactory.openSession();
//		SQLQuery query = session.createSQLQuery("call `attachment.selectIdByBookId`(" + publishedBookId + ")");
//		BigInteger attachmentId = null;
//		List<?> list = query.list();
//		
//		attachmentId = (BigInteger)list.get(0);
////		System.out.println("----->I: " +attachmentId);
////		try {
////			attachmentId = (BigInteger)query.getSingleResult();
////		} catch (NoResultException exp) { //DO NOTHING, JUST KEEP IT NULL
////			Logger.getLogger(this.getClass()).warn("ATTACHMENT COULD NOT BE FOUND FOR PBOOK ID: " + publishedBookId + ", make sure that during publish book the procedure creates a pdf and an attachment record in the database.");
////		}
////		return attachmentId;
//		
////		s.close();
//		return attachmentId;
//	}
	
	public static Long findAttachmentIdByBookId(Session session,long publishedBookId){
		Criteria query = session.createCriteria(Attachment.class);
		query.add(Property.forName("publishedBookId").eq(publishedBookId));
		query.setProjection(Projections.id());
		return (Long)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<GenreTitle> getGenreTitles(Session session,Long bookId){
		Query query = session.getNamedQuery("GenreTitle.selectByBookId");
		query.setParameter("bookId", bookId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<AudioFileDetails> scrollAudioAttachmentsByBookId(ScrollAttachmentsArgs args){
		Query query = args.session.getNamedQuery("AudioFileDetails.selectByBook");
		query.setLong("bookId", args.bookId);
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		List<AudioFileDetails> list = query.list();
		query = args.session.getNamedQuery("AudioFileRating.ratingAverageById");
		for(AudioFileDetails a : list){
			query.setLong("audioFileId", a.audioFileId);
			a.rate = (Double)query.uniqueResult();
			
//			System.out.println("--------StatelessDao.scrollAudioAttachmentsByBookId: " + a.rate);
		}
		if(args.userAccountId != null){
			query = args.session.getNamedQuery("AudioFileRating.selectRateByUserAndId");
			query.setLong("userAccountId", args.userAccountId);
			for(AudioFileDetails a : list){
				query.setLong("audioFileId", a.audioFileId);
				a.userRate = (Float)query.uniqueResult();
			}
		}
		return list;
	}
	
	public static Long countAudioAttachmentsByBookId(ScrollAttachmentsArgs args){
		Query query = args.session.getNamedQuery("AudioFile.countByBook");
		query.setLong("bookId", args.bookId);
		return (Long)query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Long> selectAttachmentIDsByBookId(Session session, Long bookId){
		Query query = session.getNamedQuery("Attachment.selectIdByPublishedBookId");
		query.setLong("bookId", bookId);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Attachment> selectAttachmentsByBookId(Session session, Long bookId){
		Query query = session.getNamedQuery("Attachment.selectByPublishedBookId");
		query.setLong("bookId", bookId);
		
		return query.list();
	}
//	@SuppressWarnings("unchecked")
	public static List<Attachment> deleteAttachmentByBookId(Session session, Long bookId, boolean deleteFiles){
//		Query query = session.getNamedQuery("Attachment.selectByPublishedBookId");
//		query.setLong("bookId", bookId);
//		List<Attachment> list = query.list();
		List<Attachment> list = selectAttachmentsByBookId(session,bookId);
		for(Attachment a : list){
			if(deleteFiles){
				a.delete();
			} 
			session.delete(a);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<AudioFile> deleteAudioFilesByBookId(Session session, Long bookId, boolean deleteFiles){
		Query query = session.getNamedQuery("AudioFile.selectByPublishedBookId");
		query.setLong("bookId", bookId);
		List<AudioFile> list = query.list();
		for(AudioFile audioFile : list){
			if(deleteFiles){
				FileUtilities.delete(audioFile);
			}
			session.delete(audioFile);
		}
		return list;
	}
	
	protected static int deletePublishedBook(Session session, Long bookId){
		Query query;
		int records = 0;
		query = session.getNamedQuery("PublishedBook.selectCoverImageFile");
		query.setLong("bookId", bookId);
		ImageFile cover = (ImageFile)query.uniqueResult();
		
		List<Attachment> attachmentList = deleteAttachmentByBookId(session,bookId,false);
		List<AudioFile> audioList = deleteAudioFilesByBookId(session,bookId,false);
		session.flush();
		
		query = session.getNamedQuery("PublishedBook_Genre.deleteByPublishedBookId");
		query.setLong("bookId", bookId);
		records += query.executeUpdate();
		
		query = session.getNamedQuery("PublishedSheet.deleteByPublishedBookId");
		query.setLong("bookId", bookId);
		records += query.executeUpdate();
		
		query = session.getNamedQuery("UserDownloadsCount.deleteByPublishedBookId");
		query.setLong("bookId", bookId);
		records += query.executeUpdate();
		
		query = session.getNamedQuery("PublishedBook.delete");
		query.setLong("bookId", bookId);
		records += query.executeUpdate();
		
		if(cover != null){
			//make sure no other publication is referencing this image
			query = session.getNamedQuery("PublishedBook.countOtherReferencingImageFile");
			query.setLong("bookId", bookId);
			query.setLong("imageFileId", cover.getImageFileId());
			Long count = (Long)query.uniqueResult();
			if(count == 0){
				cover.delete();
			}
		}
		for(Attachment a : attachmentList){
			a.delete();
		}
		
		for(AudioFile audioFile : audioList){
			FileUtilities.delete(audioFile);
		}
		session.flush();
		return records;
	}
	
	protected static int inactivatePublishedBook(Session session, Long bookId){
		Query query = session.createQuery("UPDATE  PublishedBook AS pb SET pb.status = 'REMOVED' WHERE pb.bookId = " + bookId);
		return query.executeUpdate();
	}
	
	/**
	 * scenario:
	 * user A: create book1 -> publish book1
	 * user B: branch book1 -> publish book1
	 * user A: delete publication 'book1' -> sheets of userB are referencing published sheets of user A ('derivedFromId')
	 * 
	 * Books (derivedFromId), Sheets (derivedFromId), PublishedBooks (parentId) can all reference a publication
	 * @param session
	 * @param bookId
	 * @param userAccountId
	 * @return
	 */
	public static int deletePublishedBook(Session session, Long bookId, Long userAccountId){
		int i = 0;
		Query query;
		
		query = session.createQuery("SELECT pb.publisherAccountId FROM PublishedBook AS pb WHERE pb.bookId = " + bookId);
		Long dbPublisherId = (Long)query.uniqueResult();
		if(dbPublisherId != null){
			if(dbPublisherId.equals(userAccountId)){
				try {
					i = deletePublishedBook(session, bookId);
				} catch (ConstraintViolationException exp) {
//					exp.printStackTrace();
					inactivatePublishedBook(session,bookId);
				}
//				Long totalBranches = countPublishedBookBranches(session,bookId);
//				if(totalBranches.longValue() == 0){
//					i = deletePublishedBook(session, bookId);
//				}
//				else{
//					i = inactivatePublishedBook(session,bookId);
//				}
			}
		}
		else{
			throw new UnauthorizedException("You cannot delete a publication which belongs to a different account");
		}
		
		
		return i;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<GenreTitle> getGenreTitles_(Session session,long bookId){
		Criteria query = session.createCriteria(PublishedBook_Genre.class,"pg");
		query.add(Property.forName("bookId").eq(bookId));
		
		query = query.createCriteria("genre", "g", JoinType.INNER_JOIN);
		ProjectionList l = Projections.projectionList();
		l.add(Projections.property("g.genreId"));
		l.add(Projections.property("g.name"));
		l.add(Projections.property("pg.sequenceIndex"));
		query.setProjection(l);
		
		query.setResultTransformer(new ResultTransformer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Object transformTuple(Object[] values, String[] alias) {
				return new GenreTitle((Long)values[0], (String)values[1],(Integer)values[2]);
			}
			@Override
			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
				return list;
			}
		});
		return query.list();
	}
	
	public static long countPublishedBooks(Session session,String phrase){
		Query query;
		if(phrase == null){
			query = session.getNamedQuery("PublishedBook.count");
		}
		else{
			query = session.getNamedQuery("PublishedBook.count(titlePhrase)");
			query.setParameter("titlePhrase", "%" + phrase + "%");
		}
		
		return (Long)query.uniqueResult();
	}
	
	public static ImageFile findImageFile(Session session,long id){
		return (ImageFile)session.get(ImageFile.class, id);
	}
	
	public static List<Publisher> findAllPublishers(Session session,long bookId){
		Long newBookId = bookId;
		int bookCount = 0;
		List<Publisher> list = new ArrayList<Publisher>();
		Query query = session.getNamedQuery("Publisher.selectByPublicationId");
		while(newBookId != null && bookCount < 100){
			query.setLong("publicationId", newBookId);
			Publisher p = (Publisher)query.uniqueResult();
			list.add(p);
			newBookId = p.getParentId();
			bookCount++;
		}
		return list;
		
//		Query query = session.createSQLQuery("CALL `branchitup`.`publishers.selectRecursiveByBookId`(" + bookId + ")").addEntity(Publisher.class);
//		return query.list();
	}
	
	/*CREATE PROCEDURE `publishedbook.selectPathOfBranch`(bookId BIGINT)
	BEGIN
		DECLARE newBookId BIGINT;
		DECLARE bookCount BIGINT;
		
		SET bookCount = 0;
		SET newBookId = bookId;
		DROP TABLE IF EXISTS t1;
		CREATE TEMPORARY TABLE t1(title VARCHAR(50),`version` VARCHAR(50),bookId BIGINT, userAccountId BIGINT, firstName VARCHAR(50), lastName VARCHAR(50), roleMask INT);
		WHILE (newBookId IS NOT NULL AND bookCount < 100)
		DO
			INSERT INTO t1 (SELECT pb.title, pb.version, pb.bookId, u.userAccountId, u.firstName, u.lastName, pb.publisherRoleMask AS roleMask FROM publishedbooks AS pb
			INNER JOIN useraccounts AS u ON pb.publisherAccountId = u.userAccountId
			WHERE pb.bookId = newBookId);
		
			SET newBookId = (SELECT pb.parentId FROM publishedbooks AS pb WHERE pb.bookId = newBookId);
			SET bookCount = bookCount + 1;
		END WHILE;
		
		SELECT * FROM t1;
	END $$
	DELIMITER ;*/
	
	public static List<ParentBook> selectPathOfBranch(Session session, Long bookId){
		Long newBookId = bookId;
		int bookCount = 0;
		List<ParentBook> list = new ArrayList<ParentBook>();
		Query query = session.getNamedQuery("ParentBook.selectByPublicationId");
		while(newBookId != null && bookCount < 100){
			query.setLong("publicationId", newBookId);
			ParentBook pb = (ParentBook)query.uniqueResult();
			list.add(pb);
			newBookId = pb.getParentId();
			bookCount++;
		}
		return list;
		
//		Query query = session.createSQLQuery("CALL `publishedbook.selectPathOfBranch`(" + bookId + ")").addEntity(ParentBook.class);
//		return query.list();

	}
	
	@SuppressWarnings("unchecked")
	public static List<UserName> findSheetOwners(Session session,long bookId){
		List<UserName> list = null;
//		Query query = session.createSQLQuery("CALL `branchitup`.`useracconts.selectSheetOwners`(" + bookId + ")").addEntity(UserName.class);
		Query query = session.getNamedQuery("UserAccount.selectSheetOwnersByPublicationId");
		query.setLong("publicationId", bookId);
		list = query.list();
//		System.out.println("---->PersistenceModel.findSheetOwners for " + bookId + ", " + list);
		return list;
	}
	
//	public static Rating selectRating(Session session,String username, long bookId){
//		Query query = session.getNamedQuery("Rating.select(username,bookId)");
//		query.setParameter("bookId", bookId);
//		query.setParameter("username", username);
//		//22461, meir
//		System.out.println("PersistenceManager.selectRating:" + bookId + ", " + username);
//		Rating rating = null;
//		try {
//			rating = (Rating)query.list().get(0);
//		} catch (NoResultException exp) {
//			//DO NOTHING
//			logger.warn("PersistenceManager.selectRating: NO RESULT for bookId " + bookId +", username: " + username); 
//		}
//		return rating;		
//	}
	
	
//	@SuppressWarnings("unchecked")
//	public static List<ReviewRate> scrollReviews(ScrollReviewsArgs args){
//		Session s = args.session;
//		//[offset=0, maxResults=10, publicationId=1957]
//		//ReviewRate.selectByBookId
//		Query query = s.createSQLQuery("CALL `branchitup`.`reviewrate.selectByBookIdWithRate`(:bookId,:offset,:maxResults)").addEntity(ReviewRate.class);
//		query.setParameter("bookId", args.bookId);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		List<ReviewRate> list = query.list();
//		System.out.println("----->browseReviewRecords " + list.size() + ", " + args);
//		return list;
//	}
	
	@SuppressWarnings("unchecked")
	public static List<ReviewRate> scrollReviews(ScrollReviewsArgs args){
		Session s = args.session;
		Query query = s.getNamedQuery("ReviewRate.selectByBookId");
		query.setLong("bookId", args.bookId);
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		List<ReviewRate> list = query.list();
//		System.out.println("------------->ScrollReviews.size " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ArticleComment> scrollArticleComments(ScrollArticleCommentsArgs args) 
	throws Exception{
		Session s = args.session;
		Query query = s.getNamedQuery("ArticleComment.selectBySheetId");
		query.setLong("sheetId", args.sheetId);
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		List<ArticleComment> list = query.list();
//		System.out.println("------------->ArticleComment.size " + list.size());
		return list;
	}
	
	public static Long countArticleComments(Session session,long sheetId){
		Query query = session.getNamedQuery("ArticleComment.countBySheetId");
		query.setParameter("sheetId", sheetId);
		return (Long)query.uniqueResult();
	}
	
	public static Long countReviews(Session session,long bookId){
		Query query = session.getNamedQuery("Review.countByBookId(bookId)");
		query.setParameter("bookId", bookId);
		return (Long)query.uniqueResult();
	}
	
//	public static Map<String,Number> getRatingsByUser(Session session,Long bookId){
//		List<Rating> list = selectRatings(session,bookId);
//		Map<String,Number> map = new Hashtable<String, Number>();
//		for(Rating r : list){
//			map.put(r.getUsername(), r.getRate());
//		}
//		
//		return map;
//	}
	
	@SuppressWarnings("unchecked")
	public static List<Rating> selectRatings(Session session,Long bookId){
		Query query = session.getNamedQuery("Rating.select(bookId)");
		query.setParameter("bookId",bookId);
		return query.list();
	}
	
//	@Deprecated //use the same call as selectPublicationRecords but with bookId
//	@SuppressWarnings("unchecked")
//	public static PublishedBookDetails getPublicationSample(Session session,long bookId){
//		SQLQuery query = session.createSQLQuery("CALL `publishedbooks.selectById`(" + bookId + ")").addEntity(PublishedBookDetails.class);
////		query.setParameter("bookId", bookId);
//		
//		List<PublishedBookDetails> list = query.list(); //only one result
//		for(PublishedBookDetails r : list){
//			r.ratingAverage = getRatingAverage(session,r.bookId);
//			r.ratingCount = getRatingCount(session,r.bookId);
//			r.pdfAttachmentId = findAttachmentIdByBookId(session,r.bookId);
//			r.genreTitles = getGenreTitles(session,r.bookId);
//		}
//		if(list.size() > 0){
//			return list.get(0);
//		}
//		return null;
//	}
	
//	public void submitReview(Session session,Review review){
//		session.persist(review);
//		
//	}
	
//	public static void submitRating(Session session, Rating rating){
////		Rating rating = this.selectRating(record.username, record.bookId);
////		if(rating == null){
////			rating = new Rating();
////			rating.setUserName(record.userName);
////			rating.setBookId(record.bookId);
////		}
////		rating.setRate(record.rate);
////		rating.setCreatedOn(new Timestamp(System.currentTimeMillis()));
////		this.getEntityManager().merge(rating);
//		session.persist(rating);
//	}
	
	
	
//	@Deprecated
//	public static Review submitReview(SubmitReviewArgs args){
//		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//		Review review = new Review();
//		review.setBookId(args.bookId);
//		review.setComment(args.comment);
//		review.setUserAccountId(args.userAccountId);
//		review.setCreatedOn(currentTime);
////		if(args.rate != null){
////			submitRating(args);
////			
////		}
//		args.session.persist(review);
//		
//		return review;
//	}
	
	public void test(Session session){
		Test tt = new Test();
		tt.setMyname("My Value 2");
		
		
		session.persist(tt);
//		System.out.println("---->HibernatePersistence.after persist " + tt);
	}
	
//	public Transaction beginTransaction(){
//		Session s = getSession();
//		System.out.println("----->begin transaction " + s.hashCode());
//		Transaction t = s.getTransaction();
//		t.begin();
//		return t;
//	}
//	
//	public void flush(){
//		getSession().flush();
//	}
//	
//	
//	public void commitTransaction(){
//		Session s = getSession();
//		s.getTransaction().commit();
//	}
//	
//	public void rollbackTransaction(){
//		Session s = getSession();
//		s.getTransaction().rollback();
//	}
	
//	public static Genre findGenre(Session session,long genreId){
//		return (Genre)session.load(Genre.class, genreId);
//	}
	
	public static String resolveImageUrl(Session session, long imageFileId){
		ImageFile imageFile = findImageFile(session,imageFileId);
//		System.out.println("--->>" + imageFileId + ">>-------" + imageFile);
		return resolveImageUrl(imageFile);
	}
	
	public static String resolveImageUrl(ImageFile imageFile){
		if(imageFile == null) return null;

		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getProperty("branchitup.diskresources.imagesUrl"));
		sb.append("/");
		sb.append(imageFile.getFolderName());
		sb.append("/");
		sb.append(imageFile.getFileName());
		
		return sb.toString();
	}
	
	public static String resolveThumbnailUrl(ImageFile imageFile){
		if(imageFile == null) return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getProperty("branchitup.diskresources.imagesUrl"));
		sb.append("/");
		sb.append(imageFile.getFolderName());
		sb.append("/thumbnail_");
		sb.append(imageFile.getFileName());
		return sb.toString();
	}
	
	public static String resolveThumbnailUrl(Session session, Long imageFileId){
		if(imageFileId == null) return null;
		
		ImageFile imageFile = findImageFile(session,imageFileId);
		if(imageFile != null){
			return resolveThumbnailUrl(imageFile);
//			StringBuilder sb = new StringBuilder();
//			sb.append(Utils.getProperty("branchitup.diskresources.imagesUrl"));
//			sb.append("/");
//			sb.append(imageFile.getFolderName());
//			sb.append("/thumbnail_");
//			sb.append(imageFile.getFileName());
//			return sb.toString();
		}
		return null;
	}
	public static GenreDetails findGenreDetails(Session session,Long genreId){
//		
		Query query = session.getNamedQuery("GenreDetails.selectByGenreId");
		query.setLong("genreId", genreId);
//		Query query = session.createSQLQuery("CALL `branchitup`.`genredetails.byId`(" + genreId + ")").addEntity(GenreDetails.class);
		GenreDetails genre = (GenreDetails)query.uniqueResult();
		if(genre != null && genre.getIconImageFileId() != null){
			genre.setIconImageUrl(resolveThumbnailUrl(session,genre.getIconImageFileId()));
		}
		return genre;
	}
	
	public static Long countSheets(Session session, Long bookId){
		Query query = session.getNamedQuery("Sheet_Book.countByBookId");
		query.setLong("bookId", bookId);
		
		return (Long)query.uniqueResult();
	}
	
	public static List<GenreTitle> findDerivedPublicationGenresByBook(Session session, Long bookId){
		List<GenreTitle> list;
		Query query = session.getNamedQuery("Book.parentId");
		query.setLong("bookId", bookId);
		Long parentBookId = (Long)query.uniqueResult();
		if(parentBookId != null){
			list = getGenreTitles(session, parentBookId);
		}
		else{
			list = new ArrayList<GenreTitle>();
		}
		return list;
	}
	
	public static long countGenres(Session session, String phrase){
		Query query;
		if(phrase == null){
//			query = session.createSQLQuery("CALL `genredetails.count`()");
			query = session.getNamedQuery("Genre.count");
		}
		else{
			
//			query = session.createSQLQuery("CALL `genredetails.countByName`(:phrase)");
			query = session.getNamedQuery("Genre.countByPhrase");
			query.setParameter("phrase", "%" + phrase + "%");
		}
		
		return (Long)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<GenreDetails> scrollGenreDetails(ScrollGenreArgs args){
		Query query = null;
		Session s = args.session;
		s.clear(); //image icon seams to be cached
		if(args.conditionValue != null){
//			System.out.println("----->>>IM HERE");
//			GenreDetails.selectLikeName
			query = s.getNamedQuery("GenreDetails.selectLikeName");
			query.setString("phrase", "%" + args.conditionValue + "%");
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
//			query = s.createSQLQuery("CALL `genredetails.scrollByName`(:phrase,:offset,:maxResults)").addEntity(GenreDetails.class);
//			query.setCacheable(false);
//			query.setParameter("phrase", "%" + args.conditionValue + "%");
//			query.setParameter("offset", args.offset);
//			query.setParameter("maxResults",args.maxResults);
		}
		else{
//			System.out.println("----->>>IM HERE2");
			query = s.getNamedQuery("GenreDetails.select");
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
//			query = s.createSQLQuery("CALL `genredetails.scroll`(:offset,:maxResults)").addEntity(GenreDetails.class);
//			query.setCacheable(false);
//			query.setParameter("offset", args.offset);
//			query.setParameter("maxResults",args.maxResults);
		}
		List<GenreDetails> list = query.list();
		for(GenreDetails g : list){
			if(g.getIconImageFileId() != null){
				g.setIconImageUrl(resolveThumbnailUrl(args.session, g.getIconImageFileId()));
			}
		}
		return list;
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<BookRecord> scrollBookRecords(ScrollBookArgs args){
//		Session s = args.session;
//		StringBuilder sb = new StringBuilder("call `bookrecord.browseByOwner`(");
//		sb.append("'");
//		sb.append(args.owner);
//		sb.append("',");
//		sb.append(args.offset);
//		sb.append(",");
//		sb.append(args.maxResults);
//		sb.append(")");
////		System.out.println("selectBookRecords " + sb);
//		Query query = s.createSQLQuery(sb.toString()).addEntity(BookRecord.class);
//		
//		List<BookRecord> list = query.list();
//		return list;
//		
//	}
	
//	public static long countUserBookRecords(Session session, String owner){
//		Query query = session.getNamedQuery("Book.count(owner)");
//		query.setParameter("owner", owner);
//		
//		return (Long)query.list().get(0);
//		
//	}
	

	
//	@SuppressWarnings("unchecked")
//	public static List<SheetRecord> scrollPrivateSheetRecords(ScrollSheetsArgs args){
//		Session s = args.session;
//		s.clear();
//		System.out.println("-----> browsePrivateSheetRecords");
//		Query query = null;
//		
//		query = s.createSQLQuery("CALL `sheetrecord.browseByOwner`(:owner,:offset,:maxResults)").addEntity(SheetRecord.class);
////		query = s.getNamedQuery("SheetRecord.browseByOwner");
//		query.setParameter("owner", args.owner);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		
//		List<SheetRecord> sheetList = query.list();
//		if(args.withAssociatedBooks){
//			initBookList(args.session, sheetList);
//		}
//		return sheetList;
//	}
	
//	public static SheetContentRecord selectSheetContentRecord(Session session, Long sheetId){
//		Query query = session.createSQLQuery("CALL `SheetContentRecord.selectBySheetId`(:sheetId)").addEntity(SheetContentRecord.class);
//		query.setParameter("sheetId", sheetId);
//		try{
//			SheetContentRecord record = (SheetContentRecord)query.uniqueResult();
//			//System.out.println("PersistenceHandler.selectSheetRecord::::::"+record.content);
//			return record;
//		}
//		catch(NoResultException exp){
//			//nothing
//		}
//
//		return null;
//	}
	
	public static Sheet findSheet(Session session, Long sheetId){
		Sheet sheet = (Sheet)session.get(Sheet.class, sheetId);
		return sheet;
	}
	
//	public static SheetContent selectSheetContent(Session session, Long sheetId){
//		Query query = session.createSQLQuery("CALL `branchitup`.`sheetcontent.selectById`(" + sheetId + ")").addEntity(SheetContent.class);
//		
//		return (SheetContent)query.uniqueResult();
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<PublishedSheet> scrollPublishedSheets(ScrollPublishedSheetArgs args){
//		Query query = args.session.createQuery("SELECT s FROM PublishedSheet AS s WHERE s.bookId = " + args.bookId);
//		query.setMaxResults(args.maxResults);
//		query.setFirstResult(args.offset);
//		
//		return (List<PublishedSheet>)query.list();
//	}
	
//	public static Long countPublishedSheets(Session session, Long bookId){
//		Query query = session.getNamedQuery("PublishedSheet.countByBookId");
//		query.setLong("bookId", bookId);
//		
//		return (Long)query.uniqueResult();
////		Query query = session.createQuery("SELECT COUNT(s.sheetId) FROM PublishedSheet AS s WHERE s.bookId = " + bookId);
////		return (Long)query.uniqueResult();
//	}
	//
	
	@SuppressWarnings("unchecked")
	public static List<String> selectUserFolders(Session session, Long userAccountId){
		Query query = session.getNamedQuery("Sheet.selectFolders");
		query.setLong("userAccountId", userAccountId);
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Blog> selectUserBlogs(Session session, Long userAccountId){
		Query query = session.getNamedQuery("Blog.selectByUserAccount");
		query.setLong("userAccountId", userAccountId);
		query.setFirstResult(0);
		query.setMaxResults(10);
		
		return query.list();
	}
	
	public static int incrementSheetViewsCount(Session session, Long sheetId){
		Query query = session.getNamedQuery("Sheet.incrementViewsCount");
		query.setLong("sheetId", sheetId);
		
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> selectUserAlbums(Session session, Long userAccountId){
		Query query = session.getNamedQuery("ImageFile.selectAlbums");
		query.setLong("userAccountId", userAccountId);
		query.setFirstResult(0);
		query.setMaxResults(1000);
		
		return query.list();
		
//		Query query = session.createSQLQuery("CALL `imagefiles.selectAlbums`(" + userAccountId + ")");
//		return (List<String>)query.list();
	}
	
	public static long countImageFileIdsByAlbum(ScrollImageFileArgs args){
		Query query = args.session.getNamedQuery("ImageFile.countByOwnerAlbumFolder");
//		Query query = args.session.createSQLQuery("CALL `imagefiles.countByAlbum`(:ownerAccountId, :album)");
		query.setLong("ownerAccountId", args.userAccountId);
		query.setString("album", args.album);
		query.setString("folderName", "USER_UPLOADS");
		return (Long)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<ImageFileId> scrollImageFileIdsByAlbum(ScrollImageFileArgs args){
		Query query = args.session.getNamedQuery("ImageFile.scrollIDsByOwnerAlbumFolder");
		
//		Query query = args.session.createSQLQuery("CALL `imagefiles.scrollIdsByAlbum`(:ownerAccountId, :album, :offset, :maxResults)").addEntity(ImageFileId.class);
		query.setLong("ownerAccountId", args.userAccountId);
		query.setString("album", args.album);
		query.setString("folderName", "USER_UPLOADS");
		query.setFirstResult(args.offset);
		query.setMaxResults(args.maxResults);
		
		List<Long> ids = query.list();
		List<ImageFileId> list = new ArrayList<ImageFileId>(ids.size());
		for(Long id : ids){
			ImageFileId item = new ImageFileId();
			item.setId(id);
			
			item.setUrl(resolveImageUrl(args.session, item.id));
			item.setThumbnailUrl(resolveThumbnailUrl(args.session, item.id));
//			System.out.print("image URL is" + item.getUrl() + ", " + item.getThumbnailUrl());
			list.add(item);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollAccountsLikeName(ScrollUserItemsArgs args){
		Query query;
		query = args.session.getNamedQuery("UserItem.selectAccountsLikeName");
		query.setParameter("phrase", "%" + args.phrase + "%");
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollSheetUserItemsByPhrase(ScrollUserItemsArgs args){
		Query query;
		if(args.ownerAccountId != null){
			query = args.session.getNamedQuery("UserItem.selectSheetsByOwnerPhrasePermission");
			query.setParameter("ownerAccountId", args.ownerAccountId);
			query.setParameter("phrase", "%" + args.phrase + "%");
			query.setParameter("permissionMask", args.permissionsMask);
		}
		else{
			query = args.session.getNamedQuery("UserItem.selectSheetsByPhrasePermission");
			query.setParameter("phrase", "%" + args.phrase + "%");
			query.setParameter("permissionMask", args.permissionsMask);
		}
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollArticleUserItemsByPhrase(ScrollUserItemsArgs args){
		Query query;
		query = args.session.getNamedQuery("UserItem.selectArticleByPhrase");
		query.setParameter("phrase", "%" + args.phrase + "%");
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollArticleUserItemsByPhraseAndOwner(ScrollUserItemsArgs args){
		Query query;
		query = args.session.getNamedQuery("UserItem.selectArticleByPhraseAndOwner");
		query.setString("phrase", "%" + args.phrase + "%");
		query.setLong("ownerAccountId", args.ownerAccountId);
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		
		return query.list();
	}
	
	public static int publishSheetAsArticle(PublishSheetToBlogArgs args){
		Query query = args.session.getNamedQuery("Sheet.ownerAccountId");
		query.setLong("sheetId", args.sheetId);
		Long ownerAccountId = (Long)query.uniqueResult();
		if(ownerAccountId.equals(args.userAccountId)){
			query = args.session.getNamedQuery("Sheet.publishAsArticleToBlog");
			
			query.setLong("blogId", args.blogId);
			query.setLong("sheetId", args.sheetId);
		}
		else{
			throw new UnauthorizedException("You are not the owner of this sheet, you can not publish it");
		}
		
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollBookUserItemsByPhrase(ScrollUserItemsArgs args){
		Query query;
		if(args.ownerAccountId != null){
			query = args.session.getNamedQuery("UserItem.selectBooksByOwnerAndPhrase");
			query.setParameter("ownerAccountId", args.ownerAccountId);
			query.setParameter("phrase", "%" + args.phrase + "%");
		}
		else{
			query = args.session.getNamedQuery("UserItem.selectBooksByPhrase");
			query.setParameter("phrase", "%" + args.phrase + "%");
		}
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		
		return query.list();
		
//		Query query = args.session.createSQLQuery("CALL `useritems.scrollBookLikeTitle`(:ownerAccountId, :phrase, :offset, :maxResults)").addEntity(UserItem.class);
//		query.setParameter("ownerAccountId", args.ownerAccountId);
//		query.setParameter("phrase", args.phrase);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollPublishedBookUserItemsByPhrase(ScrollUserItemsArgs args){
		Query query;
		if(args.ownerAccountId != null){
			query = args.session.getNamedQuery("UserItem.selectPublishedBookByPublisherPhrase");
			
			query.setLong("publisherAccountId", args.ownerAccountId);
			query.setString("phrase", "%" + args.phrase + "%");
		}
		else{
			query = args.session.getNamedQuery("UserItem.selectPublishedBookByPhrase");
			query.setString("phrase", "%" + args.phrase + "%");
		}
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
//		Query query = args.session.createSQLQuery("CALL `useritems.scrollPublishedBookLikeTitle`(:ownerAccountId, :phrase, :offset, :maxResults)").addEntity(UserItem.class);
//		query.setParameter("ownerAccountId", args.ownerAccountId);
//		query.setParameter("phrase", args.phrase);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollUserItems(ScrollUserItemsArgs args){
//		List<UserItem> list = new ArrayList<>();
//
//		Query query = args.session.getNamedQuery("UserItem.scrollBooks");
//		query.setLong("ownerAccountId", args.ownerAccountId);
//		query.setFirstResult((int)args.offset);
//		query.setMaxResults(args.maxResults);
//		list.addAll(query.list());
//		
//		query = args.session.getNamedQuery("UserItem.selectSheets");
//		query.setLong("ownerAccountId", args.ownerAccountId);
//		query.setFirstResult((int)args.offset);
//		query.setMaxResults(args.maxResults);
//		list.addAll(query.list());
//		return list;
		
		Query query = args.session.createSQLQuery("CALL `branchitup`.`useritems.scrollByOwner`(" + args.ownerAccountId + ", " + args.offset + ", " + args.maxResults + ")").addEntity(UserItem.class);
		return query.list();
		
		
	}
	
	public static Long countUserSheets(Session session, Long ownerAccountId){
		Query query = session.getNamedQuery("Sheet.countActiveByOwnerAccountId");
		query.setLong("ownerAccountId", ownerAccountId);
		return (Long)query.uniqueResult();
	}
	
	public static Long countUserBooks(Session session, Long ownerAccountId){
		Query query = session.getNamedQuery("Book.countByOwnerAccountId");
		query.setLong("ownerAccountId", ownerAccountId);
		return (Long)query.uniqueResult();
	}
	
	public static Long countUserItems(Session session,Long ownerAccountId){
		Long count = 0l;
		
//		Query query = session.getNamedQuery("Book.countByOwnerAccountId");
//		query.setLong("ownerAccountId", ownerAccountId);
//		count += (Long)query.uniqueResult();
		
		count += countUserBooks(session,ownerAccountId);
		
//		query = session.getNamedQuery("Sheet.countActiveByOwnerAccountId");
//		query.setLong("ownerAccountId", ownerAccountId);
//		count += (Long)query.uniqueResult();
		
		count += countUserSheets(session,ownerAccountId);
		
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollBookUserItems(ScrollUserItemsArgs args){
//		Query query = args.session.createSQLQuery("CALL `branchitup`.`useritems.scrollBooks`(" + args.ownerAccountId + ", " + args.offset + ", " + args.maxResults + ")").addEntity(UserItem.class);
		Query query;
		if(args.ownerAccountId == null){
			query = args.session.getNamedQuery("UserItem.selectBooks");
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
		}
		else{
			query = args.session.getNamedQuery("UserItem.selectBooksByOwner");
			query.setLong("ownerAccountId", args.ownerAccountId);
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
		}
		return query.list();
	}
	
	public static Long countBookUserItems(Session session,Long ownerAccountId){
//		Query query = session.createSQLQuery("CALL `branchitup`.`useritems.countBooks`(" + ownerAccountId + ")");
		
//		Query query = session.createQuery("SELECT COUNT(b.bookId) FROM Book AS b WHERE b.ownerAccountId = " + ownerAccountId);
		Query query = session.getNamedQuery("Book.countByOwnerAccountId");
		query.setLong("ownerAccountId", ownerAccountId);
		return (Long)query.uniqueResult();
	}
	
//	public static Long countPublishedBookBranches(Session session,Long publishedBookId){
////		Query query = session.createQuery("SELECT COUNT(b.bookId) FROM Book AS b WHERE b.derivedFromId = " + publishedBookId);
//		Query query = session.getNamedQuery("PublishedBook.countBranches");
//		query.setLong("publishedBookId", publishedBookId);
//		return (Long)query.uniqueResult();
//	}
	
//	public static Long countPublishedBookPublishedBranches(Session session,Long publishedBookId){
//		Query query = session.createQuery("SELECT COUNT(pb.bookId) FROM PublishedBook AS pb WHERE pb.parentId = " + publishedBookId);
//		return (Long)query.uniqueResult();
//	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollSheetUserItems(ScrollSheetItemsArgs args){
		if(args.folder != null){
			Query query = args.session.getNamedQuery("UserItem.selectSheetsInFolder");
			query.setLong("ownerAccountId", args.ownerAccountId);
			query.setString("folderName",args.folder);
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
			return query.list();	
		}
		else{
			Query query = args.session.getNamedQuery("UserItem.selectSheets");
			query.setLong("ownerAccountId", args.ownerAccountId);
			query.setFirstResult((int)args.offset);
			query.setMaxResults(args.maxResults);
			return query.list();
		}
		
		
	}
	
	public static Long countSheetUserItems(Session session,Long ownerAccountId,String folderName){
//		Query query = session.createSQLQuery("CALL `branchitup`.`useritems.countSheets`(:ownerAccountId,:folderName)");
//		query.setParameter("ownerAccountId", ownerAccountId);
//		query.setParameter("folderName", folderName);
//		return ((BigInteger)query.uniqueResult()).longValue();
		if(folderName != null){
			Query query = session.getNamedQuery("UserItem.countSheetsInFolder");
			query.setParameter("ownerAccountId", ownerAccountId);
			query.setParameter("folderName", folderName);
			return (Long)query.uniqueResult();
		}
		else{
			Query query = session.getNamedQuery("UserItem.countSheets");
			query.setParameter("ownerAccountId", ownerAccountId);
			return (Long)query.uniqueResult();
		}
		
	}
	
//	public static Long countPrivateByPhrase(Session session,String phrase,String owner){
//		
//		Query query = session.createSQLQuery("CALL `sheetrecord.countByPhraseAndOwner`(:phrase,:owner)");
////		Query query = s.getNamedQuery("SheetRecord.countByPhraseAndOwner");
//		query.setParameter("owner", owner);
//		query.setParameter("phrase", "%" + phrase + "%");
//		return (Long)query.list().get(0);
//	}
	
//	public static Long countPublicByPhrase(Session session,String phrase){
//		Query query = session.createSQLQuery("CALL `sheetrecord.countByPhrase`(:phrase,:permissionsMask)").addScalar("count",LongType.INSTANCE);;
////		Query query = s.getNamedQuery("SheetRecord.countByPhrase");
//		query.setParameter("permissionsMask", Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN);
//		query.setParameter("phrase", "%" + phrase + "%");
//		return (Long)query.uniqueResult();
//	}
	
//	public static Long countByPermissions(Session session, int permissionsMask){
//		Query query = session.createSQLQuery("CALL `sheetrecord.count`(:permissionsMask)").addScalar("count",LongType.INSTANCE); 
////		Query query = s.getNamedQuery("SheetRecord.count");
//		query.setParameter("permissionsMask", permissionsMask);
//		return (Long)query.uniqueResult();
//	}
	
//	public static Long countPublic(Session session){
//		return countByPermissions(session,Constants.SheetPermission.PUBLIC_GROUP_VIEW);
//	}
	
//	public static Long countPrivate(Session session,String owner){
//		Query query = session.getNamedQuery("SheetRecord.count(owner)");
//		query.setParameter("owner", owner);
//		return (Long)query.uniqueResult();
//	}
	
//	public static long countSheetRecords(Session session,String owner){
//		Query query = session.getNamedQuery("SheetRecord.count(owner)");
//		query.setParameter("owner", owner);
//		
//		return (Long)query.uniqueResult();
//	}
	
	public static void insertImageFile(Session session, ImageFile imageFile){
		session.persist(imageFile);
	}
	
//	public static int deleteProfileImageFileByUserAccountId(Session session, Long userAccountId){
//		if(userAccountId == null) return 0;
//		
//		//TODO not yet finished
//		Query query = session.createQuery("DELETE FROM ImageFile AS i WHERE i.userAccountId = " + userAccountId);
//		return  query.executeUpdate();
//	}
	
	public static ImageFile newImageFile(Map<String,Object> imageProps){
//		System.out.println("WebBean.newImageFile:"+imageProps);
		ImageFile imageFile = new ImageFile();
		imageFile.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		imageFile.setWidth((Integer)imageProps.get("width"));
		imageFile.setHeight((Integer)imageProps.get("height"));
		imageFile.setSize((Long)imageProps.get("size"));
		imageFile.setFormat(ImageFile.Format.valueOf(String.valueOf(imageProps.get("format")).toUpperCase()));
		imageFile.setFileName((String)imageProps.get("fileName"));
//		imageFile.dirPath = (String)imageProps.get("dirPath");
		imageFile.setOwnerAccountId((Long)imageProps.get("ownerAccountId"));
		imageFile.setFolderName((String)imageProps.get("album"));
//		if(imageProps.containsKey("imageUse")){
//			imageFile.imageUse = ImageFile.ImageUse.valueOf(((String)imageProps.get("imageUse")).toUpperCase());
//		}
		
		
		return imageFile;
	}
	public static ImageFile storeImageIn(FileItemArgs args) throws Exception{
		Map<String,Object> imageProps = null;
		ImageFile imageFile = null;
		
		imageProps = FileUtilities.writeImageToDisk(args.fileItem, args.systemFolder.toString(),true);
		imageFile = newImageFile(imageProps);
		imageFile.setOwnerAccountId(args.userAccountId);
		imageFile.setFolderName(args.systemFolder.toString());
		imageFile.setAlbum(args.album);
//		System.out.println("---->PersistenceHandler.storeImageIn " + imageFile);
		insertImageFile(args.session, imageFile);
		
		if(imageFile != null && imageFile.getImageFileId() != null && ImageFile.SystemFolder.PROFILE_PHOTOS.toString().equals(imageFile.getFolderName())){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.profileImageFileId = " + imageFile.getImageFileId() + " WHERE ua.userAccountId = " + args.userAccountId);
			query.executeUpdate();
		}
		
		return imageFile;
	}
	
//	public static ImageFile updateProfileImage(FileItemArgs args) throws Exception{
//		Map<String,Object> imageProps = null;
//		ImageFile imageFile = null;
//		
//		imageProps = FileUtilities.writeImageToDisk(args.fileItem, ImageFile.SystemFolder.PROFILE_PHOTOS.toString(),true);
//		imageFile = newImageFile(imageProps);
//		imageFile.setOwnerAccountId(args.userAccountId);
//		imageFile.setFolderName(ImageFile.SystemFolder.PROFILE_PHOTOS.toString());
//		imageFile.setAlbum(args.album);
//		
////		System.out.println("---->PersistenceHandler.storeImageIn " + imageFile);
//		
//		insertImageFile(args.session, imageFile);
//		
//		return imageFile;
//	}
	@SuppressWarnings("unchecked")
	public static void insertGenre(Session session,Genre genre){
		boolean createNew = true;
		String genreName = genre.getName();
		genreName = genreName.trim();
		genre.setName(genreName);
		if(StringUtils.isEmpty(genreName)){
			throw new InvalidInputException("Genre name cannot be empty");
		}
		//try to find genre with same name first
		Query query = session.getNamedQuery("Genre.findLikeName");
		query.setParameter("name", genreName);
		
		List<Genre> genres = query.list();
		
		for(Genre g : genres){
			if(g.getName().equalsIgnoreCase(genreName)){
				genre.setGenreId(g.getGenreId());
				createNew = false;
				break;
			}
		}
		if(createNew){
			session.persist(genre);
		}
	}
	
	public static Genre updateGenre(UpdateGenreArgs args) throws Exception{
		
		Genre genre = (Genre)args.session.load(Genre.class, args.genreId);
		if(args.imageFileItem != null){
			ImageFile imageFile = genre.getIconImageFile();
			if(imageFile != null){
				imageFile.delete();
				args.session.delete(imageFile);
			}
			
			
			FileItemArgs fileArgs = new FileItemArgs();
			fileArgs.session = args.session;
			fileArgs.systemFolder = ImageFile.SystemFolder.GENRES;
			fileArgs.userAccountId = args.userAccountId;
			fileArgs.fileItem = args.imageFileItem;
			fileArgs.album = null;
			imageFile = storeImageIn(fileArgs);
			
			genre.setIconImageFileId(imageFile.getImageFileId());
		}
		if(!StringUtils.isEmpty(args.description)){
			genre.setDescription(args.description);
		}
		genre.setModifiedByIP(args.ipAddress);
		genre.setModifiedOn(new Date(System.currentTimeMillis()));
		genre.setModifiedBy(args.userAccountId);
		
//		Query query = args.session.createQuery("UPDATE Genre AS g SET g.description = :description, g.modifiedOn = :modifiedOn, g.modifiedByIP = :modifiedByIP, g.modifiedBy = :modifiedBy WHERE g.genreId = :genreId");
//		query.setParameter("genreId", args.genreId);
//		query.setParameter("description", args.description);
//		query.setParameter("modifiedByIP", args.ipAddress);
//		query.setParameter("modifiedOn", new Date(System.currentTimeMillis()));
//		query.setParameter("modifiedBy", args.userAccountId);
		
		return (Genre)args.session.merge(genre);
		
//		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public static List<ImageFile> selectImageFiles(Session session, Long userAccountId,String folder){
		Query query = session.getNamedQuery("ImageFile.selectByAccountAndFolder");
		query.setParameter("ownerAccountId", userAccountId);
		query.setParameter("folderName", folder);
		return query.list();
	}
	
	public static void deleteUserPhoto(Session session, Long userAccountId){
		//we expect to see only one result, but if there are many, delete them all
		List<ImageFile> list = selectImageFiles(session,userAccountId,ImageFile.SystemFolder.PROFILE_PHOTOS.toString());
		if(list.size() > 0){
			for(ImageFile image : list){
				//the file exists here, if it cannot be deleted it means
				//the JVM is using the file for some reason,
				//do not remove from database is this case try to figure out why the file is being used,
				//the next time the user will upload an image hopefully the file will be deleted successfuly
				if(image.delete()){
					session.delete(image);
				}
			}
		}
	}
	
	public static int deleteSheet(Session session, Long sheetId, Long userAccountId)
	throws UnauthorizedException{
		int updatedCount = 0;
		Query query = session.createQuery("SELECT s.ownerAccountId FROM Sheet AS s WHERE s.sheetId = " + sheetId);
		Long dbOwnerAccountId = (Long)query.uniqueResult();
		if(dbOwnerAccountId != null && dbOwnerAccountId.equals(userAccountId)){
			query = session.createQuery("SELECT COUNT(sb.bookId) FROM Sheet_Book sb WHERE sb.sheetId = " + sheetId);
			Long count = (Long)query.uniqueResult();
			if(count != null && count.longValue() > 0){
				query = session.createQuery("UPDATE Sheet as s SET s.status = 'REMOVED' WHERE s.sheetId=" + sheetId);
				updatedCount = query.executeUpdate();
//				System.out.println("---111-> " + updatedCount);
			}
			else{
				query = session.createQuery("DELETE FROM Sheet AS s WHERE s.sheetId = " + sheetId);
				updatedCount = query.executeUpdate();
//				System.out.println("---222-> " + updatedCount);
			}
		}
		else{
			throw new UnauthorizedException("You must be the owner of this sheet in order to delete it");
		}
		
		return updatedCount;
	}
//	public static int deleteBook(Session session, Long bookId, Long userAccountId)
//	throws UnauthorizedException{
//		int recordsUpdated = 0;
//		
//		Query query = session.getNamedQuery("Book.selectOwnerAccountIdByBookId");
//		query.setParameter("bookId", bookId);
////		Query query = session.createQuery("SELECT b.ownerAccountId FROM Book AS b WHERE b.bookId = " + bookId);
//		Long dbOwnerAccountId = (Long)query.uniqueResult();
//		if(dbOwnerAccountId != null && dbOwnerAccountId.equals(userAccountId)){
//			query = session.getNamedQuery("Sheet_Book.deleteAllByBookId");
//			query.setParameter("bookId", bookId);
//			recordsUpdated += query.executeUpdate();
//			
//			query = session.getNamedQuery("Book.delete");
//			query.setParameter("bookId", bookId);
//			recordsUpdated += query.executeUpdate();
//			
//			return recordsUpdated;
//		}
//		else{
//			throw new UnauthorizedException("You have no permissions to delete this book");
//		}
//	}
	
	public static int deleteBook(Session session, Long bookId, Long userAccountId)
	throws UnauthorizedException{
		int recordsUpdated = 0;
		Book book = (Book)session.get(Book.class, bookId);
		if(book != null){
			if(book.getOwnerAccountId().equals(userAccountId)){
				for(Sheet sheet : book.getSheetList()){
					if(sheet.getCreatedOn().equals(book.getCreatedOn())){
						session.delete(sheet);
						recordsUpdated++;
					}
				}
				session.delete(book);
				recordsUpdated++;
			}
			else{
				throw new UnauthorizedException("You have no permissions to delete this book");
			}
		}
		if(recordsUpdated > 0){
			session.flush();
		}
		return recordsUpdated;
	}
	public static void delete(Session session, ImageFile imageFile){
		session.delete(imageFile);
	}
	

	//-
	
//	public static void insertImageFile(ImageFile imageFile){
//		this.getEntityManager().persist(imageFile);
//	}
	public static long countUserImages(Session session, String owner){
		Query query = session.getNamedQuery("ImageFile.count(owner,folder)");
		query.setParameter("owner", owner);
		query.setParameter("folder", ImageFile.SystemFolder.USER_UPLOADS.toString());
		return (Long)query.uniqueResult();
	}
	
	public static Sheet newSheet(NewSheetArgs args){
		Sheet sheet = new Sheet();
		sheet.setContent(args.content);
		sheet.setCssText(args.cssText);
		sheet.setCreatedByIP(args.createdByIP);
		sheet.setName(args.name);
		sheet.setOwnerAccountId(args.userAccountId);
		sheet.setViewsCount(0);
//		sheet.setDerivedFromId(args.derivedFromId);
		sheet.setCreatedOn(new Date(System.currentTimeMillis()));
		sheet.setStatus(UserItemStatus.ACTIVE);
		if("PUBLIC".equals(args.visibility)){
			if(args.allowEditing != null && args.allowEditing.equals(Boolean.TRUE)){
				sheet.setPermissionsMask(Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN_EDIT);
			}
			else{
				sheet.setPermissionsMask(Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN);
			}
		}
		else{
			sheet.setPermissionsMask(0);
		}
		sheet.setFolderName(args.folderName);
		args.session.persist(sheet);
		
		return sheet;
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<String> selectUserFolders(Session session, String username){
//		Query query = session.getNamedQuery("Sheet.selectUserFolders(owner)");
//		query.setParameter("owner", username);
//		return (List<String>)query.list();
//	}
	/*
	 public static Sheet updateSheet(Sheet sheet) {
		Sheet dbSheet = this.getEntityManager().find(Sheet.class, sheet.sheetId);
		if(dbSheet != null){
			dbSheet.setContent(sheet.content);
			dbSheet.setName(sheet.name);
			dbSheet.setPermissionsMask(sheet.permissionsMask);
			dbSheet.setGenreId(sheet.genreId);
			dbSheet.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			dbSheet.setCssText(sheet.cssText);
			dbSheet.setLanguage(sheet.language);
		}
		return dbSheet;
	}
	 */
	
	/**
	 * NOTE - It is possible to update the sheet's permissions only to a value that is greater then
	 * the previous one.
	 * once a sheet is public it can not be private again
	 * 
	 * @param args
	 * @return
	 */
	public static Sheet updateSheet(UpdateSheetArgs args) throws UnauthorizedException{
		Session s = args.session;
		Sheet dbSheet = (Sheet)s.load(Sheet.class, args.sheetId);
//		System.out.println("----->HibernatePersistenceManager.updateSheet.dbSheet " + dbSheet);
		if(dbSheet != null){
			if(!dbSheet.getOwnerAccountId().equals(args.userAccountId)){
				if(dbSheet.getPermissionsMask() <= Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN){
					throw new UnauthorizedException("This sheet was published as PUBLIC but with no permissions to change its content. It can be added to your books as is, but it cannot ba changed!");
				}
				
			}
			dbSheet.setContent(args.content);
			if(!dbSheet.getName().equals(args.name)){
				dbSheet.setName(args.name);
			}
			if(!StringUtils.isEmpty(args.folderName) && !dbSheet.getFolderName().equals(args.folderName)){
				dbSheet.setFolderName(args.folderName);
			}
			Integer permissionsMask = null;
			if("PUBLIC".equals(args.visibility)){
				if(Boolean.TRUE.equals(args.allowEditing)){
					permissionsMask = Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN_EDIT;
				}
				else{
					permissionsMask = Constants.SheetPermission.PUBLIC_GROUP_VIEW;
				}
			}
			if(permissionsMask != null){
				if(dbSheet.getPermissionsMask().intValue() < permissionsMask.intValue()){
					dbSheet.setPermissionsMask(permissionsMask.intValue());
				}
			}
			dbSheet.setModifiedOn(new Date(System.currentTimeMillis()));
			dbSheet.setCssText(args.cssText);
			dbSheet.setModifiedByIP(args.modifiedByIP);
		}
		return dbSheet;
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public static List<PublicationTitle> selectPublicationTitleStartWith(Session session,String phrase,int maxResults){
		if(phrase == null || phrase.trim().equals("")){
			return new ArrayList<PublicationTitle>(0);
		}
		Query query = session.createSQLQuery("CALL `publishedbooks.selectTitleByPhrase`(:phrase,:offset,:maxResults)").addEntity(PublicationTitle.class);
//		System.out.println("********HHH***:"+phrase + ", " + maxResults);
		query.setParameter("phrase", phrase + "%");
		query.setParameter("offset", 0);
		query.setParameter("maxResults", maxResults);
		
		return query.list();
	}
//	@SuppressWarnings("unchecked")
//	public static List<PublicRecord> selectPublicRecordsByPhrase(Session session,String phrase,int maxResults){
//		if(phrase == null || phrase.trim().equals("")){
//			return new ArrayList<PublicRecord>(0);
//		}
//		Query query = session.createSQLQuery("CALL `branchitup`.`publicrecord.selectTopByPhrase`('%" + phrase + "%', " +  maxResults + ");").addEntity(PublicRecord.class);
//		return query.list();
//	}
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> selectPublicItemsByPhrase(ScrollUserItemsArgs args){
		if(args.phrase == null || args.phrase.trim().equals("")){
			return new ArrayList<UserItem>(0);
		}
//		System.out.println("--->StatelessDAO.selectPublicItemsByPhrase: " + "CALL `branchitup`.`useritems.scrollLikeItemName`('" + args.phrase + "', " + args.offset + ", " +  args.maxResults + ");");
		
		Query query = args.session.createSQLQuery("CALL `branchitup`.`useritems.scrollLikeItemName`('" + args.phrase + "', " + args.offset + ", " +  args.maxResults + ");").addEntity(UserItem.class);
		return query.list();
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<PublicRecord> selectGenresByPhrase(Session session,String phrase,int maxResults){
//		if(phrase == null || phrase.trim().equals("")){
//			return new ArrayList<PublicRecord>(0);
//		}
//		Query query = session.createSQLQuery("CALL `branchitup`.`genres.selectTopByPhrase`('%" + phrase + "%'," +  maxResults + ")").addEntity(PublicRecord.class);
//		return query.list();
//	}
	//
	
	@SuppressWarnings("unchecked")
	public static List<UserItem> scrollGenreUserItemsByPhrase(ScrollUserItemsArgs args){
		if(args.phrase == null || args.phrase.trim().equals("")){
			return new ArrayList<UserItem>(0);
		}
		Query query = args.session.createSQLQuery("CALL `branchitup`.`useritems.scrollGenresLikeName`('" + args.phrase + "'," + args.offset + "," +  args.maxResults + ")").addEntity(UserItem.class);
		return query.list();
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<UserItem> selectGenresByPhrase(Session session,String phrase,int maxResults){
//		if(phrase == null || phrase.trim().equals("")){
//			return new ArrayList<UserItem>(0);
//		}
//		Query query = session.createSQLQuery("CALL `branchitup`.`genres.selectTopByPhrase`('%" + phrase + "%'," +  maxResults + ")").addEntity(UserItem.class);
//		return query.list();
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<PublicRecord> selectPublicRecordsByPhrase(Session session,String phrase,int maxResults){
//		if(phrase == null || phrase.trim().equals("")){
//			return new ArrayList<PublicRecord>(0);
//		}
//		List<PublicRecord> list;// = new ArrayList<PublicRecord>();
//
//		Query query = session.getNamedQuery("PublicRecord.selectGenres");
//		query.setMaxResults(maxResults);
//		query.setParameter("phrase", phrase + "%");
//		query.setResultTransformer(new ResultTransformer() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public Object transformTuple(Object[] arr, String[] arg1) {
//				return new PublicRecord(arr[0].toString(),(String)arr[1],PublicRecord.Type.GENRE);
//			}
//			
//			@Override
//			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
//				return list;
//			}
//		});
//		list = query.list();
//		
//		query = session.getNamedQuery("PublicRecord.selectPublications");
//		query.setMaxResults(maxResults);
//		query.setParameter("phrase", phrase + "%");
//		query.setResultTransformer(new ResultTransformer() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public Object transformTuple(Object[] arr, String[] arg1) {
//				return new PublicRecord(arr[0].toString(),(String)arr[1],PublicRecord.Type.BOOK);
//			}
//			
//			@Override
//			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
//				return list;
//			}
//		});
//		
//		list.addAll(query.list());
//		
//		query = session.getNamedQuery("PublicRecord.selectPublicSheets");
//		query.setMaxResults(maxResults);
//		query.setParameter("phrase", phrase + "%");
//		query.setResultTransformer(new ResultTransformer() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public Object transformTuple(Object[] arr, String[] arg1) {
//				return new PublicRecord(arr[0].toString(),(String)arr[1],PublicRecord.Type.SHEET);
//			}
//			
//			@Override
//			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
//				return list;
//			}
//		});
//		
//		list.addAll(query.list());
//		
//		query = session.getNamedQuery("PublicRecord.selectUsers");
//		query.setMaxResults(maxResults);
//		query.setParameter("phrase", phrase + "%");
//		query.setResultTransformer(new ResultTransformer() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public Object transformTuple(Object[] arr, String[] arg1) {
//				return new PublicRecord(arr[0].toString(),(String)arr[1],PublicRecord.Type.USER);
//			}
//			@Override
//			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
//				return list;
//			}
//		});
//		
//		list.addAll(query.list());
//		
//		Collections.sort(list, new Comparator<PublicRecord>(){
//			@Override
//			public int compare(PublicRecord o1, PublicRecord o2) {
//				return o1.name.compareTo(o2.name);
//			}
//		});
//
//		if(list.size() > maxResults){
//			return list.subList(0, maxResults);
//		}
//		else{
//			return list;
//		}
//
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<Genre> selectGenresStartWith(Session session,String phrase,int maxResults){
//		if(phrase == null || phrase.trim().equals("")){
//			return new ArrayList<Genre>(0);
//		}
////		Query query = s.createQuery("SELECT g FROM Genre AS g WHERE g.name LIKE '" + phrase + "%' ORDER BY g.name");
////		query.setParameter("phrase", phrase);
//		
//		Query query = session.getNamedQuery("Genre.selectByPhrase");
//		query.setParameter("phrase", phrase + "%");
//		query.setMaxResults(maxResults);
//		return query.list();
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<SheetIconRecord> selectTopSheets(SelectSheetsArgs args){
////		System.out.println("----->selectTopPublicSheets: " + args);
//		Session s = args.session;
//		if(args.phrase == null || args.phrase.trim().equals("")){
//			return new ArrayList<SheetIconRecord>(0);
//		}
//		List<SheetIconRecord> list = new ArrayList<SheetIconRecord>(10);
//		Query query;
//		if(args.genreId == null){
//			query = s.createSQLQuery("CALL `branchitup`.`sheetrecord.browseByPhrase`(:phrase, :permissionsMask,0,10)").addEntity(SheetIconRecord.class);
//			query.setParameter("phrase",args.phrase + "%");
//			query.setParameter("permissionsMask", args.permissionsMask);
//			list = query.list();
//		}
//		else{
//		}
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<SheetIconRecord> selectTopPrivateSheets(SelectSheetsArgs args){
//		System.out.println("----->selectTopPrivateSheets: " + args);
//		Session s = args.session;
//		if(args.phrase == null || args.phrase.trim().equals("")){
//			return new ArrayList<SheetIconRecord>(0);
//		}
//		List<SheetIconRecord> list = new ArrayList<SheetIconRecord>(10);
//		
//		Query query;
//		if(args.genreId == null){
//			query = s.createSQLQuery("CALL `branchitup`.`sheetrecord.browseByPhraseAndOwner`(:phrase, :username,0,10)").addEntity(SheetIconRecord.class);
//			query.setParameter("phrase",args.phrase + "%");
//			query.setParameter("username", args.username);
//			list = query.list();
//		}
//		else{
//		}
//		return list;
//	}

	//--
	
//	public static ProfileUserNamesRecord getUserProfileNames(Session session,String username){
//		Query query = session.getNamedQuery("ProfileUserNamesRecord.select(username)");
//		query.setParameter("username", username);
//		
//		return (ProfileUserNamesRecord)query.uniqueResult();
//	}
	
//	public static String getUserEmail(Session session, String username){
//		Query query = session.getNamedQuery("UserAccount.selectEmailByUsername");
//		query.setParameter("username", username);
//		
//		return (String)query.uniqueResult();
//	}
	
	public static int updateProfileEmailAddress(UserProfileInfoArgs args){
		int updatesCount = 0;
		if(args.email != null){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.email = :email WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("email", args.email);
			updatesCount += query.executeUpdate();
		}
		return updatesCount;
	}
	
	public static int updateUserAccountPassword(Session session, Long userAccountId, String newPassword){
		Query query = session.getNamedQuery("UserAccount.updatePassword");
		query.setLong("userAccountId", userAccountId);
		query.setParameter("password", newPassword);
		return query.executeUpdate();
	}
	
	public static UserAccount findUserAccountByToken(Session session, String token){
		Query query = session.getNamedQuery("UserAccount.findByToken");
		query.setString("token", token);
		return (UserAccount)query.uniqueResult();
	}
	
	public static int updateProfilePassword(UserProfileInfoArgs args){
		int updatesCount = 0;
		if(args.newPassword != null){
			if(args.newPassword.equals(args.retypePassword)){
				Query query;
				query = args.session.createQuery("SELECT ua.password FROM UserAccount AS ua WHERE ua.userAccountId = :userAccountId");
				query.setParameter("userAccountId", args.userAccountId);
				String dbPassword = (String)query.uniqueResult();
				if(dbPassword == null || dbPassword.equals(args.currentPassword)){
//					query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.password = :password WHERE ua.userAccountId = :userAccountId AND ua.password = :currentPassword");
//					query.setParameter("userAccountId", args.userAccountId);
//					query.setParameter("password", args.newPassword);
//					query.setParameter("currentPassword", args.currentPassword);
//					updatesCount += query.executeUpdate();
					updatesCount += updateUserAccountPassword(args.session, args.userAccountId, args.newPassword);
					
				}
				else{
					throw new InvalidInputException("The current password you provided does not match the value we have in our records");
				}
			}
			else{
				throw new InvalidInputException("Your passwords do not match, please retype passwords and submit again!");
			}
		}
		return updatesCount;
	}

	public static int updateProfileAboutMe(UserProfileInfoArgs args){
		int updatesCount = 0;
		
		if(!StringUtils.isEmpty(args.aboutMe)){
			Query query = args.session.getNamedQuery("UserAccount.updateAboutMe");
			query.setParameter("aboutMe", args.aboutMe);
			query.setParameter("userAccountId", args.userAccountId);
			updatesCount = query.executeUpdate();
		}
	
		return updatesCount;
	}
	
	public static int updateProfileVisibility(UserProfileInfoArgs args){
		int updatesCount = 0;
		if(args.visibility != null){
			Query query = args.session.getNamedQuery("UserAccount.updateVisibility");
			query.setParameter("visibility", UserAccount.Visibility.valueOf(args.visibility));
			query.setParameter("userAccountId", args.userAccountId);
			updatesCount = query.executeUpdate();
		}
	
		return updatesCount;
	}
	
	public static int updateUserAccount(UserProfileInfoArgs args){
		int updatesCount = 0;
		if(args.firstName != null){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.firstName = :firstName WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("firstName", args.firstName);
			updatesCount += query.executeUpdate();
		}
		if(args.lastName != null){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.lastName = :lastName WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("lastName", args.lastName);
			updatesCount += query.executeUpdate();
		}
		if(args.middleName != null){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.middleName = :middleName WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("middleName", args.middleName);
			updatesCount += query.executeUpdate();
		}
		else{
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET us.middleName = NULL WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			updatesCount += query.executeUpdate();
		}
		if(args.gender != null){
			Query query = args.session.createQuery("UPDATE UserAccount AS ua SET ua.gender = :gender WHERE ua.userAccountId = :userAccountId");
			query.setParameter("userAccountId", args.userAccountId);
			query.setParameter("gender", args.gender);
			updatesCount += query.executeUpdate();
		}
		
		return updatesCount;
	}
	
	public static int deleteUserProfileImagesByAccountId(Session session, Long userAccountId){
		int updatesCount = 0;
		
		//update the reference to be null
		Query query = session.createQuery("UPDATE UserAccount AS ua SET ua.profileImageFileId = NULL WHERE ua.userAccountId = :userAccountId");
		query.setParameter("userAccountId", userAccountId);
		updatesCount += query.executeUpdate();
		
		//delete files
		List<ImageFile> imageFiles = selectImageFiles(session,userAccountId,ImageFile.SystemFolder.PROFILE_PHOTOS.toString());
		for(ImageFile imageFile : imageFiles){
			session.delete(imageFile);
			imageFile.delete();
			updatesCount++;
		}
		
		return updatesCount;
	}
	
//	public static int updateUserFirstName(Session session,String username,String firstName){
//		if(firstName.isEmpty()){
//			firstName = null;
//		}
//		Query query = session.getNamedQuery("UserAccount.updateFirstName(username)");
//		query.setParameter("username", username);
//		query.setParameter("firstName", firstName);
//		
//		return query.executeUpdate();
//	}
//	
//	public static int updateUserMiddleName(Session session, String username,String middleName){
//		if(middleName.isEmpty()){
//			middleName = null;
//		}
//		Query query = session.getNamedQuery("UserAccount.updateMiddleName(username)");
//		query.setParameter("username", username);
//		query.setParameter("middleName", middleName);
//		
//		return query.executeUpdate();
//	}
//	
//	public static int updateUserLastName(Session session, String username,String lastName){
//		if(lastName.isEmpty()){
//			lastName = null;
//		}
//		Query query = session.getNamedQuery("UserAccount.updateLastName(username)");
//		query.setParameter("username", username);
//		query.setParameter("lastName", lastName);
//		
//		return query.executeUpdate();
//	}
//	
//	public static int updateUserEmail(Session session, String username,String email){
//		if(email.isEmpty()){
//			email = null;
//		}
//		Query query = session.getNamedQuery("UserAccount.update(username,email)");
//		query.setParameter("username", username);
//		query.setParameter("email", email);
//		
//		return query.executeUpdate();
//	}
//	public static int updateUserPassword(Session session, String username, String currentPassword,String newPassword){
//		System.out.println("updateUserPassword::" + username + ", " + currentPassword + ", " + newPassword);
//		int totalUpdates = 0;
//		Query query = session.getNamedQuery("UserAccount.selectPassword(username)");
//		query.setParameter("username", username);
//		String dbPassword = (String)query.uniqueResult();//throws NonUniqueResultException,NoResultException
//		System.out.println("passwords:::"+dbPassword + ", " + currentPassword + " , " + newPassword);
//		if(dbPassword != null && dbPassword.equals(currentPassword)){
//			query = session.getNamedQuery("UserAccount.update(username,password)");
//			query.setParameter("username", username);
//			query.setParameter("password", newPassword);
//			totalUpdates = query.executeUpdate();
//		}
//		return totalUpdates;
//	}
//	
//	public static int updateUsername(Session session, String username,String newUsername){
//		if(newUsername == null || newUsername.isEmpty()){
//			return 0;
//		}
//		Query query = session.getNamedQuery("UserAccount.update(username, newUsername)");
//		query.setParameter("username", username);
//		query.setParameter("newUsername", newUsername);
//		
//		return query.executeUpdate();
//		
//	}
	
	@SuppressWarnings("unchecked")
	public static List<Long> selectImageFileIds(Session session, Long userAccountId,String folder){
		Query query = session.getNamedQuery("ImageFile.selectIds(userAccountId,folder)");
		query.setParameter("userAccountId", userAccountId);
		query.setParameter("folder", folder);
		return query.list();
	}
	
	public static UserAccount newUserAccount(NewUserAccountArgs args) throws UserAccountException{
		UserAccount userAccount = null;
		if(args.email != null){
			Session s = args.session;
//			System.out.println("----->newUserAccount " + s.hashCode());
			Query query = s.createSQLQuery("CALL `branchitup`.`useraccounts.countByEmail`('" + args.email + "')");
			BigInteger count = (BigInteger)query.uniqueResult();
			
			if(count != null && count.longValue() == 0){
//				System.out.println("UserAccount count^^ " + count);
				userAccount = new UserAccount();
				userAccount.setEmail(args.email);
				userAccount.setFirstName(args.firstName);
				userAccount.setLastName(args.lastName);
				userAccount.setMiddleName(args.middleName);
				userAccount.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				userAccount.setGroupName(UserAccount.Group.USER);
				userAccount.setGender(args.gender);
				userAccount.setCreatedByIP(args.createdByIP);
				userAccount.setPassword(args.password);
				userAccount.setStatus(UserItemStatus.ACTIVE);
				userAccount.setVisibility(UserAccount.Visibility.HIDE_CONTACT_INFORMATION);
				userAccount.setLoginCount(1);
				
				s.persist(userAccount);
				s.flush();
//				System.out.println("UserAccount persisted " + userAccount);
			}
			else{
				throw new UserAccountException("Email " + args.email + " already exists", "Account Exists");
			}
		}
		else{
			throw new NullPointerException("email address cannot be null");
		}
		return userAccount;
	}
	
//	public static Attachment generatePDFAttachment(Session session, PublishedBook pb) throws IOException, InterruptedException{
//		System.out.println("----->generatePDFAttachment:1 " + pb);
//		Attachment attachment = null;
//		Map<String,Object> hints = new Hashtable<String, Object>();
//		
//		File pdfFile = ServiceLocator.getLastInstance().getWkExporter().export(pb,hints);
////		File pdfFile = WKExporter.getInstance(this).export(pb,hints);
////		File pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".PDF"); //***TEST
//		System.out.println("----->generatePDFAttachment:2 " + pdfFile);
//		System.out.println("THE PDF FILE IS: " + pdfFile.getAbsoluteFile());
//		if(pdfFile != null){
//			
//			attachment = new Attachment();
//			attachment.setCreatedOn(new Timestamp(System.currentTimeMillis()));
//			attachment.setDirPath("diskresources/pdf");//?
//			attachment.setFileName(pdfFile.getName());
//			attachment.setFileType(Attachment.FileType.PDF);
//			attachment.setPublishedBookId(pb.getBookId());
//			
//			session.persist(attachment);
//		}
//		return attachment;
//	}
	
	public static Attachment insertAttachment(Session session, Long bookId, File file, AttachmentFileType type) 
	throws IOException, InterruptedException{
		Attachment attachment = null;
		if(file != null){
			attachment = new Attachment();
			attachment.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			attachment.setFileName(file.getName());
			if(AttachmentFileType.PDF.equals(type)){
				attachment.setDirPath(Utils.getProperty("branchitup.diskresources.pdfDir"));//?
				attachment.setFileType(AttachmentFileType.PDF);
			}
			else if(AttachmentFileType.EPUB.equals(type)){
				attachment.setDirPath(Utils.getProperty("branchitup.diskresources.epubDir"));//?
				attachment.setFileType(AttachmentFileType.EPUB);
			}
			attachment.setPublishedBookId(bookId);
			session.persist(attachment);
		}
		return attachment;
	}
	
	public static AudioFile insertAudioAttachment(AttachAudioArgs args) throws IOException, InterruptedException{
		AudioFile audioFile = new AudioFile();
		
		audioFile.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		audioFile.setDirPath(Utils.getProperty("branchitup.diskresources.audioDir"));//? 
		audioFile.setFileName(args.fileName);
//		audioFile.setFileType(AttachmentFileType.MP3);
		audioFile.setSampleRate(args.sampleRate);
		audioFile.setPublishedBookId(args.bookId);
		audioFile.setOwnerAccountId(args.userAccountId);
		audioFile.setBitRate(args.bitRate);
		audioFile.setTrackLength(args.trackLength);
		audioFile.setChannels(args.channels);
		audioFile.setFormat(args.format);
		audioFile.setContentType(args.fileItem.getContentType());
		audioFile.setSize(args.fileItem.getSize());
		if(!StringUtils.isEmpty(args.description)){
			audioFile.setDescription(args.description);
		}
		args.session.persist(audioFile);
		return audioFile;
	}
	
	public static void insertPublishedBook(Session session, PublishedBook book){
		session.persist(book);
	}
	
	public static void insertPublishedSheet(Session session, PublishedSheet sheet){
		session.persist(sheet);
	}
	
//	public static BookRecord findBook(Session session, long bookId){
//		Query query = session.createSQLQuery("SELECT b.bookId, b.title, b.bookSummary, b.coverImageFileId, b.createdOn, b.derivedFromId, b.modifiedOn FROM books AS b WHERE b.bookId = :bookId").addEntity(BookRecord.class);
//		query.setParameter("bookId", bookId);
////		System.out.println("PersistenceHandler.findBook: " + bookId + ", ");
//		return (BookRecord)query.uniqueResult();
//	}
//	public static PublishedBook toPublishedBook(Book book){
//		PublishedBook pb = new PublishedBook();
//		pb.setBookSummary(book.getBookSummary());
//		pb.setTitle(book.getTitle());
//		pb.setCoverImageFile(book.getCoverImageFile());
//		pb.setCoverImageFileId(book.getCoverImageFileId());
//		
//		if(book.getSheet_Book() != null && book.getSheetList() != null){
//			Hashtable<Long, Sheet_Book> sheetIndexHash = new Hashtable<Long, Sheet_Book>();
//			for(Sheet_Book sb : book.getSheet_Book()){
//				sheetIndexHash.put(sb.getSheetId(), sb);
//			}
//			pb.sheetList = new ArrayList<PublishedSheet>(book.getSheetList().size());
//			for(Sheet s : book.getSheetList()){
//				pb.sheetList.add(new PublishedSheet(s,sheetIndexHash.get(s.getSheetId())));
//			}
//		}
//	}
	
//	public static String incrementSubversion(Session session, Long bookId){
//		Query query = session.createQuery("UPDATE PublishedBook AS b SET b.subversionCount = (b.subversionCount + 1) WHERE b.bookId = " + bookId);
//		Integer value = query.executeUpdate();
//		String version = null;
//		if(value == 1){
//			query = session.createQuery("SELECT b.version, b.subversionCount FROM PublishedBook AS b WHERE b.bookId = " + bookId);
//			Object[] arr = (Object[])query.uniqueResult();
//			version = arr[0] + "." + arr[1];
////			pbook.setVersion(parent.getVersion() + "." + parent.getSubversionCount());
//		}
//		return version;
//	}
	
//	public static String incrementParentSubversion(Session session, Long bookId){
//		Query query = session.createQuery("UPDATE PublishedBook AS b SET b.subversionCount = (b.subversionCount + 1) WHERE b.derivedFromId = " + bookId);
//		Integer value = query.executeUpdate();
//		String version = null;
//		if(value == 1){
//			query = session.createQuery("SELECT b.version, b.subversionCount FROM PublishedBook AS b WHERE b.derivedFromId = " + bookId);
//			Object[] arr = (Object[])query.uniqueResult();
//			version = arr[0] + "." + arr[1];
////			pbook.setVersion(parent.getVersion() + "." + parent.getSubversionCount());
//		}
//		return version;
//	}
	
	
//	public static Book branchPublishedBook_(BranchPublishedBookArgs args){
//		Book book = null;
//		PublishedBook pbook = findPublishedBook(args.session, args.bookId);
//		if(pbook != null){
////			System.out.println("branchPublication: " + book.getBookId());
//			book = new Book();
//			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//			
//			book.setCreatedByIP(args.ipAddress);
//			book.setBookSummary(pbook.getBookSummary());
////			book.setCoverImageFile(pbook.getCoverImageFile());
////			book.setCoverImageFileId(pbook.getCoverImageFileId());
//			book.setCreatedOn(currentTime);
////			book.genre = pbook.genre;
////			book.genreId = pbook.genreId;
//			book.setModifiedOn(null);
//			book.setOwnerAccountId(args.userAccountId);
//			book.setDerivedFrom(pbook);
//			book.setDerivedFromId(pbook.getBookId());
//			book.setTitle(pbook.getTitle());
////			book.setPublishCount(0);
//			args.session.persist(book);
//			
//			System.out.println("SHEET LIST SIZE:::::"+pbook.getSheetList().size());
//			if(pbook.getSheetList() != null){
//				int sequenceIndex = 0;
//				for(PublishedSheet psheet : pbook.getSheetList()){
//					Sheet s = new Sheet();
//					s.setContent(psheet.getContent());
//					s.setCreatedOn(currentTime);
//					s.setCssText(psheet.getCssText());
//					s.setOwnerAccountId(psheet.getOwnerAccountId());
////					s.genre = book.genre;
////					s.genreId = book.genreId;
//					s.setModifiedOn(null);
//					s.setName(psheet.getName());
////					s.setLanguage(pbook.getBookLanguage());
//					s.setOwnerAccountId(args.userAccountId);
//					s.setDerivedFromId(psheet.getSheetId());
//					s.setPermissionsMask(0xF);
//					s.setCreatedByIP(args.ipAddress);
//					args.session.persist(s);
//					
//					Sheet_Book sb = new Sheet_Book(s.getSheetId(),book.getBookId(),sequenceIndex);
//					args.session.persist(sb);
//					sequenceIndex++;
//				}
//			}
//		}
//		return book;
//	}
	
	/*
INSERT INTO books(title,createdByIP, ownerAccountId, bookSummary, createdOn, derivedFromId)
	SELECT pb.title, createdByIP,ownerAccountId, pb.bookSummary, NOW() AS createdOn, pbookId
	FROM publishedbooks AS pb
	WHERE pb.bookId = pbookId;
	
	SET @bookId = LAST_INSERT_ID();
	SET @bookTitle = (SELECT books.title FROM books WHERE books.bookId = @bookId);
	
	
	INSERT INTO sheets(`name`, content, ownerAccountId, createdOn, cssText, derivedFromId, permissionsMask, createdByIP,folderName)
	SELECT ps.name, ps.content, ownerAccountId, NOW() AS createdOn, ps.cssText, ps.sheetId,7, createdByIP,@bookTitle
	FROM publishedsheets AS ps
	WHERE ps.bookId = pbookId;
	
	INSERT INTO sheet_book(sheetId,bookId,sequenceIndex)
	SELECT s.sheetId, @bookId, ps.sequenceIndex
	FROM publishedsheets AS ps 
	INNER JOIN sheets AS s ON ps.sheetId = s.derivedFromId
	WHERE ps.bookId = pbookId;
	SELECT @bookId;
	 */
	
//	private static Long copyPublishedBookToBook(Session session, Long bookId, String createdByIP, Long ownerAccountId){
//		Long newBookId = null;
//		StringBuilder sb = new StringBuilder("INSERT INTO books(title,createdByIP, ownerAccountId, bookSummary, createdOn, derivedFromId)");
//		sb.append(" SELECT pb.title, '");
//		sb.append(createdByIP);
//		sb.append("',");
//		sb.append(ownerAccountId);
//		sb.append(", pb.bookSummary, NOW() AS createdOn, ");
//		sb.append(bookId);
//		sb.append(" FROM publishedbooks AS pb");
//		sb.append(" WHERE pb.bookId=");
//		sb.append(bookId);
//		Query query = session.createSQLQuery(sb.toString());
////		Query query = session.getNamedQuery("Book.copyPublishedBookToBook");
////		query.setParameter("bookId", bookId);
//		
//		int count = query.executeUpdate();
//		if(count > 0){
//			query = session.createSQLQuery("SELECT LAST_INSERT_ID()");
//			newBookId = ((BigInteger)query.uniqueResult()).longValue();
//		}
//		return newBookId;
//	}
	
//	private static Long copyPublishedBookToBook(Session session, Long bookId, String createdByIP, Long ownerAccountId){
//		Long newBookId = null;
////		StringBuilder sb = new StringBuilder("INSERT INTO Book(title,createdByIP, ownerAccountId, bookSummary, createdOn, derivedFromId)");
////		sb.append(" SELECT pb.title, :createdByIP,:ownerAccountId, pb.bookSummary, :createdOn, :bookId");
////		sb.append(" FROM PublishedBook AS pb");
////		sb.append(" WHERE pb.bookId=");
////		sb.append(bookId);
//		
////		StringBuilder sb = new StringBuilder("INSERT INTO Book(title, createdByIP, ownerAccountId, bookSummary, createdOn, derivedFromId)");
////		sb.append(" SELECT pb.title, CAST(:createdByIP AS string), CAST(:ownerAccountId AS long), pb.bookSummary,CAST(:createdOn AS date), CAST(:bookId AS long)");
////		sb.append(" FROM PublishedBook AS pb");
////		sb.append(" WHERE pb.bookId=:bookId");
//		
//		
//		StringBuilder sb = new StringBuilder("INSERT INTO Book(title, createdByIP, ownerAccountId)");
//		sb.append(" SELECT pb.title, CAST(:createdByIP AS string), CAST(:ownerAccountId AS long)");
//		sb.append(" FROM PublishedBook AS pb");
//		sb.append(" WHERE pb.bookId=:bookId");
//		
//		Query query = session.createQuery(sb.toString());
////		query.setDate("createdOn", new Date());
//		query.setLong("bookId", bookId);
//		query.setLong("ownerAccountId", ownerAccountId);
//		query.setString("createdByIP", createdByIP);
//		
//		
//		int count = query.executeUpdate();
//		if(count > 0){
////			query = session.createSQLQuery("SELECT LAST_INSERT_ID()");
////			newBookId = ((BigInteger)query.uniqueResult()).longValue();
//		}
//		return newBookId;
//	}

	private static Book copyPublishedBookToBook(Session session, Long bookId, String createdByIP, Long ownerAccountId, Date createdOn){
		Book book = null;
		PublishedBook pbook = (PublishedBook)session.get(PublishedBook.class, bookId);
		if(pbook != null){
			book = new Book();
			book.setTitle(pbook.getTitle());
			book.setCreatedByIP(createdByIP);
			book.setOwnerAccountId(ownerAccountId);
			book.setBookSummary(pbook.getBookSummary());
			book.setCreatedOn(createdOn);
			book.setDerivedFrom(pbook);
			book.setDerivedFromId(pbook.getBookId());
			
			session.persist(book);
		}
		
		return book;
	}
	
	private static Long copyPublishedSheetToSheet(Session session, Long sheetId, String folderName,String createdByIP, Long ownerAccountId, Date createdOn){
		Long newSheetId = null;
		StringBuilder sb = new StringBuilder("INSERT INTO sheets(`name`, content, ownerAccountId, createdOn, cssText, derivedFromId, permissionsMask, createdByIP,folderName)");
		sb.append(" SELECT ps.name, ps.content, :ownerAccountId, :createdOn, ps.cssText, ps.sheetId,7, :createdByIP, :folderName");
		sb.append(" FROM publishedsheets AS ps WHERE ps.sheetId = :sheetId");
		
		Query query = session.createSQLQuery(sb.toString());
		query.setParameter("sheetId", sheetId);
		query.setParameter("folderName", folderName);
		query.setParameter("createdByIP", createdByIP);
		query.setParameter("ownerAccountId", ownerAccountId);
		query.setParameter("createdOn", createdOn);
		
		int count = query.executeUpdate();
		if(count > 0){
			query = session.createSQLQuery("SELECT LAST_INSERT_ID()");
			newSheetId = ((BigInteger)query.uniqueResult()).longValue();
		}
		return newSheetId;
	}
	
	@SuppressWarnings("unchecked")
	public static Book branchPublishedBook(BranchPublishedBookArgs args){
		Date now = new Date();
		Book book = copyPublishedBookToBook(args.session,args.bookId,args.ipAddress,args.userAccountId,now);
		
		Query query = args.session.getNamedQuery("PublishedSheet.selectIDsByBookId");
		query.setParameter("bookId", args.bookId);
		List<Long> psheetList = query.list(); //in order
		
		int sequenceIndex = 0;
		for(Long psheetId : psheetList){
			Long sheetId = copyPublishedSheetToSheet(args.session,psheetId,book.getTitle(),args.ipAddress,args.userAccountId,now);
			Sheet_Book sb = new Sheet_Book(sheetId,book.getBookId(),sequenceIndex);
			args.session.persist(sb);
			sequenceIndex++;
		}
		args.session.flush();
		return book;
	}
	
//	public static Long branchPublishedBook(BranchPublishedBookArgs args){
//		Long bookId = null;
//		Query query = args.session.createSQLQuery("CALL `books.copyFromPublishedBook`(:bookId,:ownerAccountId,:createdByIP)");
//		query.setParameter("bookId", args.bookId);
//		query.setParameter("ownerAccountId", args.userAccountId);
//		query.setParameter("createdByIP", args.ipAddress);
//		
//		bookId = ((BigInteger)query.uniqueResult()).longValue();
//		return bookId;
//	}
	
	public static Book newBook(NewBookArgs args){
		Session s = args.session;
//		ImageFile cover = null;
		if(args.sheetIds != null && args.sheetIds.length > 300){
			throw new com.branchitup.exception.InvalidInputException("Number of sheets exceeds the amount allowed on book " + args.title);
		}
		Book book = null;
		book = new Book();
		book.setBookSummary(args.bookSummary);
		book.setTitle(args.title);
		book.setOwnerAccountId(args.userAccountId);
//		book.setPublishCount(0);
		book.setCreatedOn(new Date(System.currentTimeMillis()));
		book.setCreatedByIP(args.createdByIP);
//		if(args.coverImageFileItem != null){
//			FileItemArgs fileArgs = new FileItemArgs();
//			fileArgs.session = args.session;
//			fileArgs.systemFolder = ImageFile.SystemFolder.BOOK_COVERS;
//			fileArgs.userAccountId = args.userAccountId;
//			fileArgs.fileItem = args.coverImageFileItem;
//			fileArgs.album = null;
//			cover = storeImageIn(fileArgs);
//			
////			book.setCoverImageFileId(cover.getImageFileId());
////			book.setCoverImageFile(cover);
//		}
		s.persist(book);
		
//		System.out.println("---->HibernatePersistenceManager.persistNewBook. " + book.getBookId());
		
		int sequenceIndex = 0;
		for(long sheetId : args.sheetIds){
//			if(isSheetExist(args.session,sheetId)){}
			Sheet_Book join = new Sheet_Book();
			join.setBookId(book.getBookId());
			join.setSheetId(sheetId);
			join.setSequenceIndex(sequenceIndex++);
			
//			System.out.println("---->HibernatePersistenceManager.persistNewBook.addSheet " + sheetId);
			s.persist(join);
		}
		
		return book;
	}
	
//	public static String fullVersion(Session session, Long bookId){
//		Query query = session.getNamedQuery("PublishedBook.fullVersion");
//		query.setLong("bookId", bookId);
//		String version = (String)query.uniqueResult();
//		
//		return version;
//	}
	
	public static String incrementParentSubversion(Session session, Long bookId){
		Query query;
//		Query query = session.getNamedQuery("PublishedBook.incrementSubversionCount");
//		query.setLong("bookId", bookId);
//		Integer value = query.executeUpdate();
		String version = null;
//		if(value == 1){
			query = session.getNamedQuery("PublishedBook.fullVersion");
			version = (String)query.uniqueResult();
//		}
		return version;
	}
	
	/*
	 * copyFromBook
	 */
	public static void copyBook(Session session, Long publicationId){
		PublishedBook pb = (PublishedBook)session.get(PublishedBook.class, publicationId);
		if(pb == null){
			throw new NullPointerException("Published Book could not be found");
		}
	}
	
	/**
	 * on publish -> subversionCount is always 0 because it is a new book, nobody yet has branched it out.
	 * version -> is a concatenation of the previous published book fields: version + "." + subversionCount
	 * the previous published book is the publication that the specified book was branched out from: book.derivedFromId is a reference
	 * @param args
	 * @return
	 * @throws Exception 
	 */
//	public static Long publishBook(PublishBookArgs args) throws Exception{
//		Long publishedBookId = null;
//		Integer publisherRoleMask = null;
//		Integer deficiencyMask = null;
//		ImageFile cover = null;
//		Query query;
//		
//		query = args.session.getNamedQuery("Book.exists");
//		query.setParameter("bookId", args.bookId);
//		Long count = (Long)query.uniqueResult();
//		if(count == 0){
//			throw new com.branchitup.exception.NoSuchItemException("Book " + args.bookId + " could not be found, it may have been removed!");
//		}
//		try {
//			
//			
//			//--handle genre input
//			List<Long> genres = new ArrayList<Long>();
//			if(args.genreIds != null){
//				for(long l : args.genreIds){
//					genres.add(l);
//				}
//			}
//			if(args.genreNewValues != null){
//				for(String genreValue : args.genreNewValues){
//					if(StringUtils.isEmpty(genreValue)) continue;
//					Genre newGenre = new Genre();
//					newGenre.setName(genreValue);
//					newGenre.setCreatedByIP(args.publishedByIP);
//					newGenre.setCreatedOn(new Date());
//					newGenre.setOriginatorAccountId(args.publisherAccountId);
//					insertGenre(args.session, newGenre);
//					
//					genres.add(newGenre.getGenreId());
////					args.genreIds[i++] = newGenre.getGenreId().longValue();
//				}
//			}
//			args.genreIds = new long[genres.size()];
//			for(int i = 0 ; i < genres.size() ; i++){
//				args.genreIds[i] = genres.get(i).longValue();
//			}
//			
//			//-- end of genre handling
//			if(args.coverImageFileItem != null){
//				FileItemArgs fileArgs = new FileItemArgs();
//				fileArgs.session = args.session;
//				fileArgs.systemFolder = ImageFile.SystemFolder.BOOK_COVERS;
//				fileArgs.userAccountId = args.publisherAccountId;
//				fileArgs.fileItem = args.coverImageFileItem;
//				fileArgs.album = null;
//				cover = storeImageIn(fileArgs);
//				
////				book.setCoverImageFileId(cover.getImageFileId());
////				book.setCoverImageFile(cover);
//			}
//			
//			
//			int i;
//			
//			if(args.deficiency != null && args.allowBranching){
//				i = 0;
//				if(args.deficiency.illustrating){
//					i |= BookRole.ILLUSTRATING.maskVal;
//				}
//				if(args.deficiency.coauthoring){
//					i |= BookRole.WRITING.maskVal;
//				}
//				if(args.deficiency.proofreading){
//					i |= BookRole.PROOF_READING.maskVal;
//				}
//				if(args.deficiency.translating){
//					i |= BookRole.TRANSLATING.maskVal;
//				}
//				if(args.deficiency.editing){
//					i |= BookRole.EDITING.maskVal;
//				}
//				deficiencyMask = i;
//			}
//			
//			if(args.publisherRoles != null){
//				i = 0;
//				if(args.publisherRoles.author){
//					i |= BookRole.WRITING.maskVal;
//				}
//				if(args.publisherRoles.editor){
//					i |= BookRole.EDITING.maskVal;
//				}
//				if(args.publisherRoles.illustrator){
//					i |= BookRole.ILLUSTRATING.maskVal;
//				}
//				if(args.publisherRoles.proofreader){
//					i |= BookRole.PROOF_READING.maskVal;
//				}
//				if(args.publisherRoles.translator){
//					i |= BookRole.TRANSLATING.maskVal;
//				}
//				publisherRoleMask = i;
//			}
//			
//			query = args.session.createSQLQuery("CALL `publishedbooks.copyFromBook`(:bookId, :publisherAccountId, :publisherRoleMask, :deficiencyMask, :bookLanguage, :publisherComment,:publishedByIP,:branchable,:coverImageFileId)");
//			query.setParameter("bookId", args.bookId);
//			query.setParameter("publisherAccountId", args.publisherAccountId);
//			query.setParameter("publisherRoleMask", publisherRoleMask);
//			query.setParameter("deficiencyMask", deficiencyMask);
//			query.setParameter("bookLanguage", args.bookLanguage);
//			query.setParameter("publisherComment", args.publisherComment);
//			query.setParameter("publishedByIP", args.publishedByIP);
//			query.setParameter("branchable", args.allowBranching);
//			//if the user uploaded an image, cover is not null -> use it's Id. otherwise use the parent imageFileId
//			query.setParameter("coverImageFileId", cover == null ? args.coverImageFileId : cover.getImageFileId());
//			publishedBookId = ((BigInteger)query.uniqueResult()).longValue();
//			
//			if(args.genreIds != null){
//				for(i = 0 ; i < args.genreIds.length ; i++){
////					System.out.println("PersistenceHandler::publishBook:genreId "  + args.genreIds[i]);
//					PublishedBook_Genre pg = new PublishedBook_Genre(publishedBookId,args.genreIds[i],i);
//					args.session.persist(pg);
//				}
//			}
//			
//			deleteBook(args.session, args.bookId, args.publisherAccountId);
//		} 
//		catch (Exception exp) {
//			//if the book fails on persist, delete the cover image!
//			//the next time the user will submit it, the image will be recreated
//			if(cover != null){
//				cover.delete();
//			}
//			throw exp;
//		}
//		return publishedBookId;
//	}
	
	
	public static ImageFile saveBookCoverImage(PublishBookArgs args) throws Exception{
		FileItemArgs fileArgs = new FileItemArgs();
		fileArgs.session = args.session;
		fileArgs.systemFolder = ImageFile.SystemFolder.BOOK_COVERS;
		fileArgs.userAccountId = args.publisherAccountId;
		fileArgs.fileItem = args.coverImageFileItem;
		fileArgs.album = null;
		return storeImageIn(fileArgs);
	}
	/**
	 * on publish -> subversionCount is always 0 because it is a new book, nobody yet has branched it out.
	 * version -> is a concatenation of the previous published book fields: version + "." + subversionCount
	 * the previous published book is the publication that the specified book was branched out from: book.derivedFromId is a reference
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	public static PublishedBook publishBook(PublishBookArgs args) throws Exception{
		Integer publisherRoleMask = null;
		Integer deficiencyMask = null;
		Date now = new Date();
		
		Book book = (Book)args.session.get(Book.class, args.bookId);
		if(book == null){
			throw new NoSuchItemException("Book " + args.bookId + " could not be found, it may have been removed!");
		}
		//--handle genre input
		List<Long> genres = new ArrayList<Long>();
		if(args.genreIds != null){
			for(long l : args.genreIds){
				genres.add(l);
			}
		}
		if(args.genreNewValues != null){
			for(String genreValue : args.genreNewValues){
				if(StringUtils.isEmpty(genreValue)) continue;
				Genre newGenre = new Genre();
				newGenre.setName(genreValue);
				newGenre.setCreatedByIP(args.publishedByIP);
				newGenre.setCreatedOn(new Date());
				newGenre.setOriginatorAccountId(args.publisherAccountId);
				insertGenre(args.session, newGenre);
				
				genres.add(newGenre.getGenreId());
			}
		}
		args.genreIds = new long[genres.size()];
		for(int i = 0 ; i < genres.size() ; i++){
			args.genreIds[i] = genres.get(i).longValue();
		}
		
		//-- end of genre handling
		if(args.coverImageFileItem != null){
			FileItemArgs fileArgs = new FileItemArgs();
			fileArgs.session = args.session;
			fileArgs.systemFolder = ImageFile.SystemFolder.BOOK_COVERS;
			fileArgs.userAccountId = args.publisherAccountId;
			fileArgs.fileItem = args.coverImageFileItem;
			fileArgs.album = null;
			args.coverImageFile = storeImageIn(fileArgs);
		}
		
		int i;
		
		if(args.deficiency != null && args.allowBranching){
			i = 0;
			if(args.deficiency.illustrating){
				i |= BookRole.ILLUSTRATING.maskVal;
			}
			if(args.deficiency.coauthoring){
				i |= BookRole.WRITING.maskVal;
			}
			if(args.deficiency.proofreading){
				i |= BookRole.PROOF_READING.maskVal;
			}
			if(args.deficiency.translating){
				i |= BookRole.TRANSLATING.maskVal;
			}
			if(args.deficiency.editing){
				i |= BookRole.EDITING.maskVal;
			}
			deficiencyMask = i;
		}
		
		if(args.publisherRoles != null){
			i = 0;
			if(args.publisherRoles.author){
				i |= BookRole.WRITING.maskVal;
			}
			if(args.publisherRoles.editor){
				i |= BookRole.EDITING.maskVal;
			}
			if(args.publisherRoles.illustrator){
				i |= BookRole.ILLUSTRATING.maskVal;
			}
			if(args.publisherRoles.proofreader){
				i |= BookRole.PROOF_READING.maskVal;
			}
			if(args.publisherRoles.translator){
				i |= BookRole.TRANSLATING.maskVal;
			}
			publisherRoleMask = i;
		}
		
		PublishedBook parent = book.getDerivedFrom();
		if(parent != null){
			parent.setSubversionCount(parent.getSubversionCount()+1);
		}
		
		//clone book
		PublishedBook newBook = new PublishedBook();
		newBook.setParentBook(parent); //not updatable or insertable, I put it to have access it the parent right after creating the entity
		newBook.setTitle(book.getTitle());
		newBook.setBookSummary(book.getBookSummary());
		newBook.setCoverImageFileId(args.coverImageFile == null ? args.coverImageFileId : args.coverImageFile.getImageFileId());
		newBook.setPublisherAccountId(args.publisherAccountId);
		newBook.setPublisherAccount(findUserAccount(args.session, args.publisherAccountId));
		newBook.setCreatedOn(now);
		newBook.setStatus(UserItemStatus.PENDING_ACTIVATION);
		newBook.setPublisherRoleMask(publisherRoleMask);
		newBook.setDeficiencyMask(deficiencyMask);
		newBook.setBookLanguage(args.bookLanguage);
		newBook.setPublisherComment(args.publisherComment);
		newBook.setPublishedByIP(args.publishedByIP);
		if(book.getDerivedFromId() != null){
			newBook.setVersion(parent.getVersion() + "." + parent.getSubversionCount());
		}
		else{
			newBook.setVersion("1");
		}
		newBook.setParentId(book.getDerivedFromId());
		newBook.setSubversionCount(0);
		newBook.setBranchable(args.allowBranching);
		
		args.session.persist(newBook);
		
		//clone sheets
		for(Sheet_Book sb : book.getSheet_Book()){
			Sheet sheet = sb.getSheet();
			PublishedSheet psheet = new PublishedSheet();
			psheet.setName(sheet.getName());
			psheet.setCssText(sheet.getCssText());
			psheet.setContent(sheet.getContent());
			psheet.setBookId(newBook.getBookId());
			psheet.setSequenceIndex(sb.getSequenceIndex());
			psheet.setOwnerAccountId(sheet.getOwnerAccountId());
			
			args.session.persist(psheet);
		}
		
		if(args.genreIds != null){
			for(i = 0 ; i < args.genreIds.length ; i++){
//				System.out.println("PersistenceHandler::publishBook:genreId "  + args.genreIds[i]);
				PublishedBook_Genre pg = new PublishedBook_Genre(newBook.getBookId(),args.genreIds[i],i);
				args.session.persist(pg);
			}
		}
		//after publishing we delete the book
		deleteBook(args.session, args.bookId, args.publisherAccountId);
		
		args.session.flush();
		return newBook;
	}
	/*
	CREATE PROCEDURE `publishedbookdetails.scrollByGenre`(genreId BIGINT,`offset` BIGINT, maxResults INT)
	BEGIN
	SELECT DISTINCT p.bookId,p.title, p.version, p.parentId, p.branchable, p.bookLanguage,
	(SELECT pp.title FROM publishedbooks AS pp WHERE pp.bookId = p.parentId) AS parentTitle, 
	p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, p.coverImageFileId,p.publisherAccountId,
	u.firstName AS publisherFirstName, u.lastName AS publisherLastName , 
	(SELECT a.attachmentId FROM attachments AS a WHERE a.publishedBookId = p.bookId) AS pdfAttachmentId , 
	(SELECT AVG(r.rate) FROM ratings AS r WHERE r.bookId = p.bookId) AS ratingAverage , 
	(SELECT COUNT(r.rate) FROM ratings AS r WHERE r.bookId = p.bookId) AS ratingCount
	FROM publishedbooks AS p 
	INNER JOIN useraccounts AS u ON p.publisherAccountId = u.userAccountId
	INNER JOIN publishedbooks_genres AS g ON p.bookId = g.bookId AND g.genreId = genreId 
	WHERE p.status = 'ACTIVE' AND g.sequenceIndex = 0 
	ORDER BY p.createdOn DESC LIMIT `offset`,maxResults;
	END $$
	DELIMITER ;*/
	public static void aa(){
		
	}

	public static void updateBook(UpdateBookArgs args) throws Exception{
		Book dbBook = findBook(args.session, args.bookId);
		if(dbBook != null){
			if(args.title != null && !args.title.isEmpty()){
				dbBook.setTitle(args.title);
			}
			if(args.bookSummary != null && !args.bookSummary.isEmpty()){
				dbBook.setBookSummary(args.bookSummary);
			}
			
			dbBook.setModifiedOn(new Date(System.currentTimeMillis()));
			
//			if(args.coverImageFileItem != null){
//				FileItemArgs fileArgs = new FileItemArgs();
//				fileArgs.session = args.session;
//				fileArgs.systemFolder = ImageFile.SystemFolder.BOOK_COVERS;
//				fileArgs.album = ImageFile.Album.DEFAULT;
//				fileArgs.userAccountId = dbBook.getOwnerAccountId();
//				fileArgs.fileItem = args.coverImageFileItem;
//				
////				cover = storeImageIn(args.session, args.coverImageFileItem, ImageFile.SystemFolder.BOOK_COVERS.toString(),dbBook.getOwnerAccountId());
//				cover = storeImageIn(fileArgs);
////				System.out.println("WebBean:Managed to store image: " + cover);
//				
////				dbBook.setCoverImageFileId(cover.getImageFileId());
//			}
			Query query = args.session.getNamedQuery("Sheet_Book.deleteAllByBookId");
			query.setParameter("bookId", args.bookId);
			query.executeUpdate();
			
			if(args.sheetIds != null && args.sheetIds.length > 0){
				int sequenceIndex = 0;
				for(long sheetId : args.sheetIds){
					Sheet_Book sb = new Sheet_Book(sheetId,args.bookId,sequenceIndex++);
					args.session.persist(sb);
				}
			}
			args.session.flush();
//			System.out.println("----->HibernatePersistenceManager.updateBook " + args);
		}
	}
	
	
	public static BookDetails findBookDetails(Session session, Long bookId){
		Query query = session.getNamedQuery("BookDetails.selectByBookId");
		query.setLong("bookId", bookId);
//		Query query = session.createSQLQuery("CALL `branchitup`.`bookdetails.find`(" + bookId + ")").addEntity(BookDetails.class);
		BookDetails book = (BookDetails)query.uniqueResult();
		if(book != null){
			book.setSheetList(findBookDetailsSheets(session, bookId));
			book.setParentCoverImageUrl(resolveThumbnailUrl(session, book.getParentCoverImageFileId()));
		}
		else{
			System.out.println("THE BOOK ID " + bookId +", IS " + book);
			(new Exception()).printStackTrace();
		}
//		System.out.println("----->BookDetails " + book.getParentCoverImageUrl() + ", " + book.getParentCoverImageFileId() + ", bookId: " + bookId);
		return book;
	}
	
	public static Long getBookOwnerAccountId(Session session, long bookId){
		Query query = session.createQuery("SELECT b.ownerAccountId FROM Book AS b WHERE b.bookId = " + bookId);
		return (Long)query.uniqueResult();
	}
	
//	public static Long getSheetOwnerAccountId(Session session, long sheetId){
//		Query query = session.createQuery("SELECT s.ownerAccountId FROM Sheet AS s WHERE s.sheetId = " + sheetId);
//		return (Long)query.uniqueResult();
//	}
	
	@SuppressWarnings("unchecked")
	public static List<BookDetailsSheet> findBookDetailsSheets(Session session, Long bookId){
		Query query = session.getNamedQuery("BookDetailsSheet.selectByBookId");
		query.setLong("bookId", bookId);
//		Query query = session.createSQLQuery("CALL `branchitup`.`bookdetails.selectSheets`(" + bookId + ")").addEntity(BookDetailsSheet.class);
		List<BookDetailsSheet> list = query.list();
		return list;
	}
	/*CREATE PROCEDURE `bookdetails.findSheet`(sheetId BIGINT, bookId BIGINT)
	BEGIN
		IF bookId IS NOT NULL
		THEN
			SELECT sb.sequenceIndex, s.sheetId, s.name, s.permissionsMask, s.ownerAccountId, ua.firstName AS ownerFirstName, ua.lastName AS ownerLastName
			FROM sheet_book AS sb
			INNER JOIN sheets AS s ON sb.sheetId = s.sheetId
			INNER JOIN useraccounts AS ua ON ua.userAccountId = s.ownerAccountId
			WHERE sb.sheetId = sheetId AND sb.bookId = bookId;
		ELSE
			SELECT NULL AS sequenceIndex, s.sheetId, s.name, s.permissionsMask, s.ownerAccountId, ua.firstName AS ownerFirstName, ua.lastName AS ownerLastName
			FROM sheets AS s
			INNER JOIN useraccounts AS ua ON ua.userAccountId = s.ownerAccountId
			WHERE s.sheetId = sheetId;
		END IF;
	END $$*/
	/**
	 * 
	 * @param bookId is optional
	 */
	public static BookDetailsSheet findBookDetailsSheet(Session session, Long bookId, Long sheetId){
		Query query;
		if(bookId != null){
			query = session.getNamedQuery("BookDetailsSheet.selectBySheetIdAndBookId");
			query.setLong("bookId", bookId);
			query.setLong("sheetId", sheetId);
		}
		else{
			query = session.getNamedQuery("BookDetailsSheet.selectBySheetId");
			query.setLong("sheetId", sheetId);
		}
//		Query query = session.createSQLQuery("CALL `bookdetails.findSheet`(:sheetId,:bookId)").addEntity(BookDetailsSheet.class);
//		query.setParameter("bookId", bookId);
//		query.setParameter("sheetId", sheetId);
		return (BookDetailsSheet)query.uniqueResult();
	}
	
	public static PublishedBook findPublishedBook(Session session, long bookId){
		return (PublishedBook)session.get(PublishedBook.class, bookId);
	}
	
	public static int setPublicationStatus(Session session, long publicationId,UserItemStatus status){
		Query query = session.getNamedQuery("PublishedBook.updateStatus");
		query.setParameter("bookId", publicationId);
		query.setParameter("status", status);
		
		return query.executeUpdate();
	}
	
	public static Map<BookRole, List<Publisher>> createPublishersByRoleMap(List<Publisher> plist){
		Map<BookRole,List<Publisher>> map = new Hashtable<BookRole, List<Publisher>>();
		if(plist != null){
			for(Publisher p : plist){
				List<BookRole> roles = p.getPublisherRoles();
				for(BookRole role : roles){
					List<Publisher> publishers = map.get(role);
					if(publishers == null){
						publishers = new ArrayList<Publisher>();
						map.put(role, publishers);
					}
					publishers.add(p);
				}
			}
		}
		return map;
	}
	
	public static Map<String, List<Publisher>> getContributorsMap(Session session, Long bookId){
		List<Publisher> publisherList = StatelessDAO.findAllPublishers(session,bookId);
		return createContributorsMap(publisherList);
	}
	
	public static Map<String, List<Publisher>> createContributorsMap(List<Publisher> publisherList){
		Map<String, List<Publisher>> contributorsMap = new HashMap<String, List<Publisher>>();
		Iterator<Entry<BookRole,List<Publisher>>> itr = createPublishersByRoleMap(publisherList).entrySet().iterator();
		while(itr.hasNext()){
			Entry<BookRole,List<Publisher>> entry = itr.next();
			List<Publisher> publishers = entry.getValue();
			if(publishers.size() > 0){
				List<Publisher> plist = new ArrayList<Publisher>();
				for(Publisher p : publishers){
					if(!plist.contains(p)){
						plist.add(p);
					}
				}
				contributorsMap.put(entry.getKey().toString(), plist);
			}
		}
		return contributorsMap;
	}
	
	public static UserWallRecord insertUserWallRecord(NewUserWallRecordArgs args){
		UserWallRecord record = new UserWallRecord();
		record.setCreatedOn(new Date());
		record.setMessage(args.message);
		record.setSenderAccountId(args.senderAccountId);
		record.setUserAccountId(args.userAccountId);
		
		args.session.persist(record);
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public static List<UserWallRecord> scrollUserWallRecords(ScrollUserWallRecordArgs args){
		Session s = args.session;
//		System.out.println("---->"+args.userAccountId);
		Query query = s.getNamedQuery("UserWallRecord.selectByUserAccountId");
		query.setLong("userAccountId", args.userAccountId);
		query.setFirstResult((int)args.offset);
		query.setMaxResults(args.maxResults);
		List<UserWallRecord> list = query.list();
		return list;
	}
	
	public static Long countUserWallRecords(Session session,long userAccountId){
		Query query = session.getNamedQuery("UserWallRecord.countByUserAccountId");
		query.setParameter("userAccountId", userAccountId);
		return (Long)query.uniqueResult();
	}

	public void _clearDatabase(){
		
	}
//	@SuppressWarnings("unchecked")
//	public List<PublicationSample> browsePublicationRecords_(final BrowsePublicationsArgs args){
//		System.out.println("HibernatePersistenceManager:search: " + args.phrase + ", " + args.offset + ", " + args.maxResults);
//		List<PublicationSample> list = this.h.execute(new HibernateCallback<List<PublicationSample>>() {
//
//		@Override
//		public List<PublicationSample> doInHibernate(Session session) 
//		throws HibernateException, SQLException {
//			SQLQuery query;
//			if(args.phrase != null){
//				query = session.createSQLQuery("call `publishedbooks.searchByTitle`(%" + args.phrase + "%," + args.offset + "," + args.maxResults  + ")").addEntity(PublicationSample.class);
//			}
//			else{
//				query = session.createSQLQuery("call `publishedbooks.browse`(" + args.offset + "," + args.maxResults  + ")").addEntity(PublicationSample.class);
//			}
//			
//			return query.list();
//		}
//		
//	});
////		List<PublicationSample> list = query.list();
//		System.out.println("---->browsePublicationRecords: " + list);
////		List<PublicationSample> list = query.getResultList();
////		for(PublicationSample r : list){
////			r.ratingAverage = this.getRatingAverage(r.bookId);
////			r.ratingCount = this.getRatingCount(r.bookId);
////			r.pdfAttachmentId = this.findAttachmentIdByBookId(r.bookId);
////			r.genreTitles = getGenreTitles(r.bookId);
////			
////			System.out.println("PersistenceHandler:FOUND: " + r.title);
////		}
////		s.close();
//		return list;
//	}

//	@SuppressWarnings("unchecked")
//	public static List<SheetRecord> scrollPrivateSheetRecordsByPhrase(ScrollSheetsArgs args){
//		Session s = args.session;
//		Query query = null;
////		System.out.println("-----> browsePrivateSheetRecordsByPhrase");
//		query = s.createSQLQuery("CALL `sheetrecord.browseByPhraseAndOwner`(:phrase,:owner,:offset,:maxResults)").addEntity(SheetRecord.class);
//		query = s.getNamedQuery("SheetRecord.browseByPhraseAndOwner");
//		query.setParameter("owner", args.owner);
////		query.setParameter("permissionsMask", PUBLIC_PERMISSION); //any permission is permissible
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		query.setParameter("phrase", "%" + args.phrase + "%");
//		
//		List<SheetRecord> sheetList = query.list();
//		if(args.withAssociatedBooks){
//			initBookList(args.session, sheetList);
//		}
//		return sheetList;
//	}
//	@SuppressWarnings("unchecked")
//	private static void initBookList(Session session,List<SheetRecord> sheetList){
//		System.out.println("PersistenceHandler.selectSheetRecords: " + sheetList);
//		Query query = session.getNamedQuery("SheetRecord.AssociatedBook.select(sheetId)");
//		for(SheetRecord sr : sheetList){
//			query.setParameter("sheetId", sr.sheetId);
//			sr.bookList = query.list();
//		}
//	}	
//	@SuppressWarnings("unchecked")
//	public static List<SheetRecord> scrollPublicSheetRecordsByPhrase(ScrollSheetsArgs args){
//		Session s = args.session;
//		Query query = null;
//		
//		query = s.createSQLQuery("CALL `sheetrecord.browseByPhrase`(:phrase,:permissionsMask,:offset,:maxResults)").addEntity(SheetRecord.class);
////		query = s.getNamedQuery("SheetRecord.browseByPhrase");
////		query.setParameter("owner", args.owner);
//		query.setParameter("permissionsMask", Constants.SheetPermission.PUBLIC_GROUP_PRIVATE_ALL);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		query.setParameter("phrase", "%" + args.phrase + "%");
//		
//		List<SheetRecord> sheetList = query.list();
//		if(args.withAssociatedBooks){
//			initBookList(args.session,sheetList);
//		}
//		return sheetList;
//	}
//	@SuppressWarnings("unchecked")
//	public static List<SheetRecord> scrollPublicSheetRecords(ScrollSheetsArgs args){
//		Query query = null;
////		System.out.println("----->browsePublicSheetRecords");
//		query = args.session.createSQLQuery("CALL `sheetrecord.browse`(:permissionsMask,:offset,:maxResults)").addEntity(SheetRecord.class);
////		query = s.getNamedQuery("SheetRecord.browse");
//		query.setParameter("permissionsMask", Constants.SheetPermission.PUBLIC_GROUP_PRIVATE_ALL);
//		query.setParameter("offset", args.offset);
//		query.setParameter("maxResults", args.maxResults);
//		
//		List<SheetRecord> sheetList = query.list();
//		if(args.withAssociatedBooks){
//			initBookList(args.session,sheetList);
//		}
//		return sheetList;
//	}
//	@SuppressWarnings("unchecked")
//	public static List<SheetRecord> selectSheetRecords(SheetForBookArgs args){
//		//MAX 100 records
//		Query query = args.session.createSQLQuery("CALL `sheetrecord.selectAllByBookId`(:bookId)").addEntity(SheetRecord.class);
//		query.setParameter("bookId", args.bookId);
//		
//		return query.list();
//	}
//	@Deprecated
//	@SuppressWarnings("unchecked")
//	public static List<PublishedBookDetails> scrollPublicationRecords_orig(ScrollPublicationsArgs args){
//		System.out.println("HibernatePersistenceManager:search: " + args.conditionValue + ", " + args.offset + ", " + args.maxResults);
////		Session s = this.sessionFactory.openSession();
//		Session s = args.session;
//		SQLQuery query;
//		
//		if(args.conditionValue != null){
//			query = s.createSQLQuery("call `publishedbooks.searchByTitle`(%" + args.conditionValue + "%," + args.offset + "," + args.maxResults  + ")").addEntity(PublishedBookDetails.class); 
//		}
//		else{
//			query = s.createSQLQuery("call `publishedbooks.browse`(" + args.offset + "," + args.maxResults  + ")").addEntity(PublishedBookDetails.class);
//		}
//		List<PublishedBookDetails> list = query.list();
////		System.out.println("---->browsePublicationRecords: " + list);
////		List<PublicationSample> list = query.getResultList();
//		for(PublishedBookDetails r : list){
//			r.ratingAverage = getRatingAverage(args.session,r.bookId);
//			r.ratingCount = getRatingCount(args.session,r.bookId);
//			r.pdfAttachmentId = findAttachmentIdByBookId(args.session, r.bookId);
//			r.genreTitles = getGenreTitles(args.session, r.bookId);
////			
//			System.out.println("----->PersistenceHandler:FOUND:pdfAttachmentId " + r.pdfAttachmentId);
//		}
//		return list;
//	}
	//@SuppressWarnings("unchecked")
//	public static List<GenreTitle> getGenreTitles_(Session session,long bookId){
//		Criteria query = session.createCriteria(PublishedBook_Genre.class,"pg");
//		query.add(Property.forName("bookId").eq(bookId));
//		
//		query = query.createCriteria("genre", "g", JoinType.INNER_JOIN);
//		ProjectionList l = Projections.projectionList();
//		l.add(Projections.property("g.genreId"));
//		l.add(Projections.property("g.name"));
//		l.add(Projections.property("pg.sequenceIndex"));
//		query.setProjection(l);
//		
//		query.setResultTransformer(new ResultTransformer() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			public Object transformTuple(Object[] values, String[] alias) {
//				return new GenreTitle((Long)values[0], (String)values[1],(Integer)values[2]);
//			}
//			@Override
//			public List<?> transformList(@SuppressWarnings("rawtypes") List list) {
//				return list;
//			}
//		});
//		return query.list();
//	}
//	public static Long countPublicationRecords(ScrollPublicationsArgs args){
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT DISTINCT COUNT(p.bookId) ");
//		sb.append("FROM publishedbooks AS p "); 
//		sb.append("INNER JOIN useraccounts AS u ON p.publisherUserName = u.userName ");
//		
//		//return only books that have matching records on publishedbooks_genres
//		if(ScrollPublicationsArgs.ConditionKey.BY_GENRE.equals(args.conditionKey)){
//			sb.append("INNER JOIN publishedbooks_genres AS g ON p.bookId = g.bookId AND g.genreId = ");
//			sb.append(args.conditionValue);
//			sb.append(" ");
//		}
//
//		sb.append("WHERE p.status = 'ACTIVE' ");
//		if(ScrollPublicationsArgs.ConditionKey.BY_PUBLISHER.equals(args.conditionKey)){
//			sb.append("AND u.userName = '");
//			sb.append(args.conditionValue);
//			sb.append("' ");
//		}
//		else if(ScrollPublicationsArgs.ConditionKey.BY_PHRASE.equals(args.conditionKey)){
//			sb.append("AND p.title LIKE '%");
//			sb.append(args.conditionValue); 
//			sb.append("%' ");
//		}
//		else if(ScrollPublicationsArgs.ConditionKey.BY_GENRE.equals(args.conditionKey)){
//			sb.append("AND g.sequenceIndex = 0 ");
//		}
//		System.out.println("---->countPublicationRecords: " + sb);
//		Query query = args.session.createSQLQuery(sb.toString());
//		
//		return ((BigInteger)query.uniqueResult()).longValue();
//	}
//	public static List<PublishedBookDetails> selectPublicationRecords(ScrollPublicationsArgs args){
//	System.out.println("HibernatePersistenceManager:search: " + args.conditionValue + ", " + args.offset + ", " + args.maxResults);
//	SQLQuery query;
//	StringBuilder sb = new StringBuilder();
//	sb.append("SELECT DISTINCT p.bookId,p.title, p.version, p.parentId,(SELECT pp.title FROM publishedbooks AS pp WHERE pp.bookId = p.parentId) AS parentTitle, p.bookSummary,p.createdOn,p.publisherRoleMask,p.deficiencyMask, p.coverImageFileId,p.publisherUserName,");
//	sb.append("u.firstName AS publisherFirstName, u.lastName AS publisherLastName ");
//	sb.append(", (SELECT a.attachmentId FROM attachments AS a WHERE a.publishedBookId = p.bookId) AS pdfAttachmentId ");
//	sb.append(", (SELECT AVG(r.rate) FROM ratings AS r WHERE r.bookId = p.bookId) AS ratingAverage ");
//	sb.append(", (SELECT COUNT(r.rate) FROM ratings AS r WHERE r.bookId = p.bookId) AS ratingCount ");
//	sb.append(", (SELECT COUNT(*) FROM userdownloadscount WHERE bookId=p.bookId) AS downloadsCount ");
//
//	sb.append("FROM publishedbooks AS p "); 
//	sb.append("INNER JOIN useraccounts AS u ON p.publisherUserName = u.userName ");
//	
//	//return only books that have matching records on publishedbooks_genres
//	if(ScrollPublicationsArgs.ConditionKey.BY_GENRE.equals(args.conditionKey)){
//		sb.append("INNER JOIN publishedbooks_genres AS g ON p.bookId = g.bookId AND g.genreId = ");
//		sb.append(args.conditionValue);
//		sb.append(" ");
//	}
////	else if(ScrollPublicationsArgs.ConditionKey.BY_RECOMMENDED.equals(args.conditionKey)){
////		
////	}
//	sb.append("WHERE p.status = 'ACTIVE' ");
//	if(ScrollPublicationsArgs.ConditionKey.BY_PUBLISHER.equals(args.conditionKey)){
//		sb.append("AND u.userName = '");
//		sb.append(args.conditionValue);
//		sb.append("' ");
//	}
//	else if(ScrollPublicationsArgs.ConditionKey.BY_PHRASE.equals(args.conditionKey)){
//		sb.append("AND p.title LIKE '%");
//		sb.append(args.conditionValue); 
//		sb.append("%' ");
//	}
//	else if(ScrollPublicationsArgs.ConditionKey.BY_GENRE.equals(args.conditionKey)){
//		sb.append("AND g.sequenceIndex = 0 ");
//	}
////	else if(ScrollPublicationsArgs.ConditionKey.BY_RECOMMENDED.equals(args.conditionKey)){
////		
////	}
//	
//	sb.append("ORDER BY p.createdOn DESC "); 
//	sb.append("LIMIT ");
//	sb.append(args.offset);
//	sb.append(",");
//	sb.append(args.maxResults);
//	System.out.println("--->" + sb.toString());
//	query = args.session.createSQLQuery(sb.toString()).addEntity(PublishedBookDetails.class);
//	List<PublishedBookDetails> list = query.list();
//	for(PublishedBookDetails r : list){
//		r.genreTitles = getGenreTitles(args.session, r.bookId);
//	}
//	return list;
//}

}
