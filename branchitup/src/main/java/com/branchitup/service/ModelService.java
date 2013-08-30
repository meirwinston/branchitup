package com.branchitup.service;

import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.persistence.ScrollResult;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.AudioFile;
import com.branchitup.persistence.entities.Blog;
import com.branchitup.persistence.entities.BookDetails;
import com.branchitup.persistence.entities.BookDetailsSheet;
import com.branchitup.persistence.entities.BookShowcase;
import com.branchitup.persistence.entities.GenreDetails;
import com.branchitup.persistence.entities.GenreTitle;
import com.branchitup.persistence.entities.ImageFile;
import com.branchitup.persistence.entities.ParentBook;
import com.branchitup.persistence.entities.PublishedBookDetails;
import com.branchitup.persistence.entities.Publisher;
import com.branchitup.persistence.entities.Sheet;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.persistence.entities.UserItem;
import com.branchitup.persistence.entities.UserName;
import com.branchitup.persistence.entities.UserWallRecord;
import com.branchitup.transfer.arguments.BranchPublishedBookArgs;
import com.branchitup.transfer.arguments.FileItemArgs;
import com.branchitup.transfer.arguments.IncrementDownloadCountArgs;
import com.branchitup.transfer.arguments.NewBookArgs;
import com.branchitup.transfer.arguments.NewGenreArgs;
import com.branchitup.transfer.arguments.NewSheetArgs;
import com.branchitup.transfer.arguments.NewUserAccountArgs;
import com.branchitup.transfer.arguments.NewUserWallRecordArgs;
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

/**
 * why to return a model and not the entity object itself? because each query requests
 * different combination of fields from a each single entity or across multiple entities.
 * 
 * @author Meir Winston
 */
public interface ModelService {
	ScrollResult scrollPublicationRecords(ScrollPublicationsArgs args) 
	throws Exception;
	
	public PublishedBookDetails findPublishedBookDetails(Long bookId)
	throws Exception;
	
	ScrollResult scrollUserWallRecords(ScrollUserWallRecordArgs args);
	
	UserWallRecord newUserWallRecord(NewUserWallRecordArgs args) throws Exception;
	
	List<BookShowcase> browseBookShowcaseByDownloads(ScrollBookShowcaseArgs args)
	throws Exception;
	
//	List<PublicRecord> findMatchingPublicRecords(String phrase,String type) 
//	throws Exception;
	
	List<String> selectUserAlbums(Long userAccountId);
	
	List<String> selectUserFolders(Long userAccountId);
	
	List<Blog> getUserBlogs(Long userAccountId);
	
	void incrementSheetViewsCount(Long sheetId);
	
	ScrollResult scrollImageFileIdsByAlbum(ScrollImageFileArgs args)
	throws Exception;
	
	List<UserItem> findMatchingUserItems(ScrollUserItemsArgs args) 
	throws Exception;
	
	ScrollResult scrollUserItems(ScrollUserItemsArgs args) 
	throws Exception;
	
	Long countUserSheets(Long ownerAccountId);
	
//	ScrollResult<UserItem> scrollUserItemsForWorkdesk(ScrollUserItemsArgs args) 
//	throws Exception;
	
	ScrollResult scrollBookUserItems(ScrollUserItemsArgs args) 
	throws Exception;
	
	ScrollResult scrollSheetUserItems(ScrollSheetItemsArgs args) 
	throws Exception;
	
	UserAccount findUserAccountByEmail(String email) throws Exception;
	
	UserAccount findUserAccount(Long userAccountId) throws Exception;
	
	ScrollResult scrollReviews(ScrollReviewsArgs args) 
	throws Exception;
	
	ScrollResult scrollArticleComments(ScrollArticleCommentsArgs args) 
	throws Exception;
	
	/**
	 * 
	 * @return the new rating average
	 * @throws Exception
	 */
	Map<String,Object> submitBookRating(SubmitBookRatingArgs args) 
	throws Exception;
	
	Map<String,Object> submitAudioFileRating(SubmitAudioFileRatingArgs args) 
	throws Exception;
	
	/**
	 * 
	 * @return the new ratings count
	 * @throws Exception
	 */
	Long submitBookComment(SubmitBookCommentArgs args) throws Exception;
	void submitArticleComment(SubmitArticleCommentArgs args) throws Exception;
	
	Float findBookRatingByUser(Long bookId,Long userAccountId) throws Exception;
	
	Map<String,Object> findPublicationContributors(long bookId) 
	throws Exception;
	
	List<Publisher> getAllPublishers(Long bookId);
	List<UserName> getSheetOwners(Long bookId);
	Map<String, List<Publisher>> getContributorsMap(Long bookId);
	
	List<ParentBook> discoverBranch(Long bookId) throws Exception;
	
	ImageFile findImageFile(long id);	
	
	ScrollResult scrollGenres(ScrollGenreArgs args) 
	throws Exception;
	
	public GenreDetails findGenreDetails(Long genreId)
	throws Exception;
	
	Attachment findAttachment(Long attachmentId);
	
	AudioFile findAudioFile(Long audioFileId);
	
	Integer incrementUserDownloadsCount(IncrementDownloadCountArgs args)
	throws Exception;
	
	ImageFile storeImage(FileItemArgs args) throws Exception;
//	ImageFile updateUserProfileImage(FileItemArgs args) throws Exception;
	
	Long newGenre(NewGenreArgs args) throws Exception;
	int updateGenre(UpdateGenreArgs args) throws Exception;
	
	Long newSheet(NewSheetArgs args) throws Exception;
	
	Sheet findSheet(Long sheetId) throws Exception;
	
	AudioFile attachAudioFile(AttachAudioArgs args) throws Exception;
	void updateSheet(UpdateSheetArgs args) throws Exception, UnauthorizedException;
	void updateBook(UpdateBookArgs args) throws Exception, UnauthorizedException;
	Long newBook(NewBookArgs args) throws Exception, UnauthorizedException;
	Long publishBook(PublishBookArgs args) throws Exception;
	Long branchPublishedBook(BranchPublishedBookArgs args) throws HibernateException;
	
	boolean deleteSheet(Long sheetId, Long userAccountId) throws Exception;
	int deleteBook(Long bookId, Long userAccountId) throws Exception;
	int deletePublication(Long bookId, Long userAccountId) throws Exception;
	BookDetails findBookDetails(Long bookId);
	List<GenreTitle> findDerivedGenresByBook(Long bookId);
	
	Long getBookOwnerAccountId(Long bookId);
	BookDetailsSheet findBookDetailSheet(Long bookId, Long sheetId);
	Long countBookSheets(Long bookId);
	
	Map<String,String> getSupportedLanguages();
	
	String getPublisherComment(Long bookId);
	
	int updateUserProfileInfo(UserProfileInfoArgs args) throws Exception;
	int updateEmailAddress(UserProfileInfoArgs args) throws Exception;
	void updateProfilePassword(UserProfileInfoArgs args) throws Exception;
	int updateProfileVisibility(UserProfileInfoArgs args) throws Exception;
	int updateProfileAboutMe(UserProfileInfoArgs args) throws Exception;
	int clearUserProfileImages(Long userAccountId) throws Exception;
	UserAccount newUserAccount(NewUserAccountArgs args) throws Exception;
	void test();
	
	String resetUserAccountPassword(UserAccount userAccount) throws Exception;
	
	ScrollResult scrollAudioAttachmentsByBookId(ScrollAttachmentsArgs args);
	void publishAsArticle(PublishSheetToBlogArgs args);
	
	//^^1
//	void resetUserAccountPassword(ResetPasswordArgs args) throws Exception;
	
	//---------------OLD--------------------
//	@Deprecated //use parameter 'userAccountId' instead
//	FileItem generatePDF(long bookId,String userName);
}
