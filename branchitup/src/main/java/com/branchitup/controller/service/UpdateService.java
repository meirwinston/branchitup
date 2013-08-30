package com.branchitup.controller.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.MailService;
import com.branchitup.service.ModelService;
import com.branchitup.system.Constants;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.BranchPublishedBookArgs;
import com.branchitup.transfer.arguments.InviteFriendArgs;
import com.branchitup.transfer.arguments.NewBookArgs;
import com.branchitup.transfer.arguments.NewGenreArgs;
import com.branchitup.transfer.arguments.NewSheetArgs;
import com.branchitup.transfer.arguments.NewUserWallRecordArgs;
import com.branchitup.transfer.arguments.PublishBookArgs;
import com.branchitup.transfer.arguments.AttachAudioArgs;
import com.branchitup.transfer.arguments.PublishSheetToBlogArgs;
import com.branchitup.transfer.arguments.SubmitArticleCommentArgs;
import com.branchitup.transfer.arguments.SubmitAudioFileRatingArgs;
import com.branchitup.transfer.arguments.SubmitBookCommentArgs;
import com.branchitup.transfer.arguments.SubmitBookRatingArgs;
import com.branchitup.transfer.arguments.UpdateBookArgs;
import com.branchitup.transfer.arguments.UpdateGenreArgs;
import com.branchitup.transfer.arguments.UpdateSheetArgs;

@Controller
public class UpdateService extends ServiceController{
	@Autowired
	private ModelService bean;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value = "/service/StoreAudioUpload", method = {RequestMethod.GET})
	public void storeAudioUpload(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		AttachAudioArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), AttachAudioArgs.class);
		args.fileItem = (FileItem)request.getSession().getAttribute(args.sessionAttributeId);
		if(args.fileItem != null){
			args.ipAddress = getClientIP(request);
			args.userAccountId = userAccount.getUserAccountId();
			
			bean.attachAudioFile(args);
			
			request.getSession().removeAttribute(args.sessionAttributeId);
			args.fileItem.delete();
		}
		
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/PostUserMessage", method = {RequestMethod.GET})
	public void newUserRecord(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		NewUserWallRecordArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), NewUserWallRecordArgs.class);
//		System.out.println("PostUserMessage " + args);
		args.senderAccountId = userAccount.getUserAccountId();
		
		this.bean.newUserWallRecord(args);
		
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/InviteFriend", method = {RequestMethod.GET})
	public void inviteFriend(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		InviteFriendArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), InviteFriendArgs.class);
		args.senderName = userAccount.getFirstName();
		if(StringUtils.isEmpty(args.senderName)){
			args.senderName = userAccount.getLastName();
		}
		mailService.sendInviteFriend(args.email, args.senderName, args.body);
//		System.out.println("UpdateService.inviteFriend");
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/UpdateSheet", method = {RequestMethod.POST})
	public void updateSheet(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
//		request.setCharacterEncoding("UTF-8"); //necessary?
//		response.setContentType("text/html; charset=UTF-8");
		UserAccount userAccount = userAccount(request);
		UpdateSheetArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UpdateSheetArgs.class);
		args.modifiedByIP = getClientIP(request);
		args.userAccountId = userAccount.getUserAccountId();
		
		bean.updateSheet(args);
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/DeleteBook", method = {RequestMethod.GET})
	public void deleteBook(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		UserAccount userAccount = userAccount(request);
		Map<String,String> args = JacksonUtils.deserializeStringMap(request.getParameter("content"));
//		
		int deletedCount = bean.deleteBook(Long.valueOf((String)args.get("bookId")), userAccount.getUserAccountId());
//		System.out.println("--->DeleteBook " + deleted);
		response.getWriter().print("{deletedCount: " + deletedCount + "}");
	}
	
	@RequestMapping(value = "/service/DeletePublication", method = {RequestMethod.GET})
	public void deletePublication(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		UserAccount userAccount = userAccount(request);
		Map<String,String> args = JacksonUtils.deserializeStringMap(request.getParameter("content"));
//		
		int recordsCount = bean.deletePublication(Long.valueOf((String)args.get("bookId")), userAccount.getUserAccountId());
//		System.out.println("--->DeleteBook " + recordsCount);
		response.getWriter().print("{recordsCount: " + recordsCount + "}");
	}
	
	@RequestMapping(value = "/service/DeleteSheet", method = {RequestMethod.GET})
	public void deleteSheet(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		UserAccount userAccount = userAccount(request);
		Map<String,String> args = JacksonUtils.deserializeStringMap(request.getParameter("content"));
				
		boolean deleted = bean.deleteSheet(Long.valueOf((String)args.get("sheetId")), userAccount.getUserAccountId());
		response.getWriter().print("{deleted: " + deleted + "}");
	}
	
	@RequestMapping(value = "/service/PublishSheetToBlog", method = {RequestMethod.GET})
	public void publishSheetToBlog(HttpServletRequest request, HttpServletResponse response, @RequestParam String content)
	throws Exception{
//		System.out.println("--->publishToBlog");
		PublishSheetToBlogArgs args = JacksonUtils.deserializeAs(content, PublishSheetToBlogArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();	
		bean.publishAsArticle(args);
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/UpdateGenre", method = {RequestMethod.GET})
	public void updateGenre(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		UpdateGenreArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UpdateGenreArgs.class);
		args.userAccountId = userAccount.getUserAccountId();
		args.ipAddress = getClientIP(request);
		
		if(args.fileItemId != null){
			args.imageFileItem = (FileItem)request.getSession().getAttribute(args.fileItemId);
		}
		System.out.println("----->UpdateGenreArgs " + args);
		
		int updatesCount = bean.updateGenre(args);
		response.getWriter().print("{updatesCount: " + updatesCount + "}");
	}
	
	@RequestMapping(value = "/service/NewGenre", method = {RequestMethod.GET})
	public void newGenre(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		NewGenreArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), NewGenreArgs.class);
		args.userAccountId = userAccount.getUserAccountId();
		args.ipAddress = getClientIP(request);
//		System.out.println("----->NewGenre " + request.getParameter("content"));
		
		if(args.fileItemId != null){
			args.coverImageFileItem = (FileItem)request.getSession().getAttribute(args.fileItemId);
			
		}
		Long genreId = bean.newGenre(args);
		if(genreId != null){
			response.getWriter().print("{genreId: " + genreId + "}");
		}
		else{
			response.getWriter().print("{type: 'exception'}");
		}
	}
	
	@RequestMapping(value = "/service/UpdateBook", method = {RequestMethod.GET})
	public void updateBook(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		UpdateBookArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UpdateBookArgs.class);
//		if(args.fileItemId != null){
//			args.coverImageFileItem = (FileItem)request.getSession().getAttribute(args.fileItemId);
//		}
		args.modifiedByIP = getClientIP(request);
		args.userAccountId = userAccount.getUserAccountId();
		bean.updateBook(args);
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/NewSheet", method = {RequestMethod.POST})
	public void newSheet(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		NewSheetArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), NewSheetArgs.class);
//		System.out.println("--->NewSheet: " + args);
		args.userAccountId = userAccount.getUserAccountId();
		args.createdByIP = getClientIP(request);
//		System.out.println("----->NewSheet " + request.getParameter("content"));
		
		Long sheetId = bean.newSheet(args);
		if(sheetId != null){
			response.getWriter().print("{sheetId: " + sheetId + "}");
		}
		else{
			response.getWriter().print("{type: 'exception'}");
		}
	}
	
	@RequestMapping(value = "/service/NewBook", method = {RequestMethod.GET})
	public void newBook(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		NewBookArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), NewBookArgs.class);
//		if(args.fileItemId != null){
//			args.coverImageFileItem = (FileItem)request.getSession().getAttribute(args.fileItemId);
//		}
		args.createdByIP = getClientIP(request);
		args.userAccountId = userAccount.getUserAccountId();
		
		Long bookId = bean.newBook(args);
		if(bookId != null){
			response.getWriter().print("{bookId: " + bookId + "}");	
		}
		else{
			throw new PersistenceException("There was a problem in storing the book");
		}
	}
	
	@RequestMapping(value = "/service/PublishBook", method = {RequestMethod.GET})
	public void publishBook(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		PublishBookArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), PublishBookArgs.class);
		args.publishedByIP = getClientIP(request);
		args.publisherAccountId = userAccount.getUserAccountId();
		Map<String,Object> genreMap = new HashMap<String,Object>();
		
		if(args.genreNewValues != null){
			for(String genreValue : args.genreNewValues){
				genreValue = genreValue.trim();
				if(!StringUtils.isEmpty(genreValue)){
					genreMap.put(genreValue, null);
				}
			}
			if(genreMap.size() > 0){
				args.genreNewValues = genreMap.keySet().toArray(new String[0]);				
			}
		}
		
		if((args.genreIds == null || args.genreIds.length == 0) && genreMap.size() == 0){
			throw new com.branchitup.exception.InvalidInputException("No genre was specified");
		}
		
		if(args.coverUploadId != null){
			args.coverImageFileItem = (FileItem)request.getSession().getAttribute(args.coverUploadId);
		}
		
		Long bookId = bean.publishBook(args);
		if(bookId != null){
			String url = (Utils.getProperty("branchitup.globalUrl") + "/publishedbook?bookId=" + bookId);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("url", url);
			model.put("bookId", bookId);
			JacksonUtils.serialize(model, response.getWriter());
		}
		else{
			throw new PersistenceException("There was a problem in storing the book");
		}
	}
	
	@RequestMapping(value = "/service/SubmitBookComment", method = {RequestMethod.GET})
	public void submitBookComment(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
//		System.out.println("----->SubmitBookComment:: " + request.getParameter("content"));
		SubmitBookCommentArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), SubmitBookCommentArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		Long reviewsCount = bean.submitBookComment(args);
		response.getWriter().print("{reviewsCount: " + reviewsCount + "}");
	}
	
	@RequestMapping(value = "/service/SubmitArticleComment", method = {RequestMethod.GET})
	public void submitBookComment(HttpServletRequest request, HttpServletResponse response, @RequestParam String content)
	throws Exception {
//		System.out.println("----->SubmitArticleComment:: " + content);
		SubmitArticleCommentArgs args = JacksonUtils.deserializeAs(content, SubmitArticleCommentArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		bean.submitArticleComment(args);
		
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/SubmitBookRating", method = {RequestMethod.GET})
	public void submitBookRating(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SubmitBookRatingArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), SubmitBookRatingArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		Map<String,Object> result = bean.submitBookRating(args);
		JacksonUtils.serialize(result, response.getWriter());
//		response.getWriter().print("{ratingAverage: " + ratingAverage + "}");
	}
	
	@RequestMapping(value = "/service/SubmitAudioFileRating", method = {RequestMethod.GET})
	public void submitAudioFileRating(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		SubmitAudioFileRatingArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), SubmitAudioFileRatingArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		Map<String,Object> result = bean.submitAudioFileRating(args);
		JacksonUtils.serialize(result, response.getWriter());
//		response.getWriter().print("{ratingAverage: " + ratingAverage + "}");
	}
	
	@RequestMapping(value = "/service/BranchPublishedBook", method = {RequestMethod.GET})
	public void branchPublishedBook(HttpServletRequest request,HttpServletResponse response) 
	throws ServletException, IOException, PersistenceException, UnauthorizedException {
		BranchPublishedBookArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), BranchPublishedBookArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		args.ipAddress = getClientIP(request);
		
		long bookId = bean.branchPublishedBook(args);
//		System.out.println("-----> Branch New Book " + bookId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("bookId", bookId);
		JacksonUtils.serialize(model, response.getWriter());
	}
}