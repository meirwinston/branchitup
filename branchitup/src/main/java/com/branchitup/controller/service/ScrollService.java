package com.branchitup.controller.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.json.UserWallRecordSerializer;
import com.branchitup.persistence.ScrollResult;
import com.branchitup.persistence.entities.BookDetailsSheet;
import com.branchitup.persistence.entities.BookShowcase;
import com.branchitup.persistence.entities.GenreDetails;
import com.branchitup.persistence.entities.ParentBook;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.persistence.entities.UserItem;
import com.branchitup.service.ModelService;
import com.branchitup.transfer.arguments.ScrollArticleCommentsArgs;
import com.branchitup.transfer.arguments.ScrollAttachmentsArgs;
import com.branchitup.transfer.arguments.ScrollBookShowcaseArgs;
import com.branchitup.transfer.arguments.ScrollGenreArgs;
import com.branchitup.transfer.arguments.ScrollImageFileArgs;
import com.branchitup.transfer.arguments.ScrollPublicationsArgs;
import com.branchitup.transfer.arguments.ScrollReviewsArgs;
import com.branchitup.transfer.arguments.ScrollSheetItemsArgs;
import com.branchitup.transfer.arguments.ScrollUserItemsArgs;
import com.branchitup.transfer.arguments.ScrollUserWallRecordArgs;

@Controller
public class ScrollService extends ServiceController{
	@Autowired
	private ModelService bean;
	
	@RequestMapping(value = "/service/ScrollUserWallRecords", method = {RequestMethod.GET})
	public void scrollUserWallRecords(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		ScrollUserWallRecordArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollUserWallRecordArgs.class);
//		if(request.getUserPrincipal() != null){
//			args.userAccountId = userAccount(request).getUserAccountId();
//		}
		if(args.userAccountId == null){
			args.userAccountId = userAccount.getUserAccountId();
		}
//		System.out.println("---->scrollUserWallRecords " + args.userAccountId);
		ScrollResult result = bean.scrollUserWallRecords(args);
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("list", result.list);
		model.put("count", result.count);
		
		JacksonUtils.serialize(model, new UserWallRecordSerializer(),response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollSheetUserItems", method = {RequestMethod.GET})
	public void scrollSheetUserItems(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollSheetItemsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollSheetItemsArgs.class);
		if(request.getUserPrincipal() != null){
			args.ownerAccountId = userAccount(request).getUserAccountId();
			ScrollResult result = bean.scrollSheetUserItems(args);
			
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("items", result.list);
			model.put("count", result.count);
			
			JacksonUtils.serialize(model, response.getWriter());
		}
	}
	
	@RequestMapping(value = "/service/ScrollBookAudioAttachments", method = {RequestMethod.GET})
	public void scrollBookAudioAttachments(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollAttachmentsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollAttachmentsArgs.class);
		if(request.getUserPrincipal() != null){
			args.userAccountId = userAccount(request).getUserAccountId();
		}
		ScrollResult result = bean.scrollAudioAttachmentsByBookId(args);
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("list", result.list);
		model.put("count", result.count);
		
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollBookUserItems", method = {RequestMethod.GET})
	public void scrollBookUserItems(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollUserItemsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollUserItemsArgs.class);
		if(request.getUserPrincipal() != null){
			args.ownerAccountId = userAccount(request).getUserAccountId();
			ScrollResult result = bean.scrollBookUserItems(args);
			
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("items", result.list);
			model.put("count", result.count);
			
			JacksonUtils.serialize(model, response.getWriter());
		}
	}
	
	@RequestMapping(value = "/service/GetPublisherComment", method = {RequestMethod.GET})
	public void getPublisherComment(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Map<String,String> args = JacksonUtils.deserializeStringMap(request.getParameter("content"));
		Long bookId = Long.valueOf(args.get("bookId"));
		
		String comment = bean.getPublisherComment(bookId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("comment", comment);
		
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollUserImageFiles", method = {RequestMethod.GET})
	public void scrollUserImageFiles(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		ScrollImageFileArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollImageFileArgs.class);
		args.userAccountId = userAccount.getUserAccountId();
//		
		ScrollResult result = bean.scrollImageFileIdsByAlbum(args);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("list", result.list);
		model.put("count", result.count);
		
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollUserItems", method = {RequestMethod.GET})
	public void scrollUserItems(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollUserItemsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollUserItemsArgs.class);
		if(request.getUserPrincipal() != null){
			args.ownerAccountId = userAccount(request).getUserAccountId();
			ScrollResult result = bean.scrollUserItems(args);
			
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("items", result.list);
			model.put("count", result.count);
			
			JacksonUtils.serialize(model, response.getWriter());
		}
	}
	
	@RequestMapping(value = "/service/SelectMatchingItems", method = {RequestMethod.GET})
	public void selectMatchingItems(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollUserItemsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"),ScrollUserItemsArgs.class);
		//to scroll through public items there is no need to be logged in
		if(isLoggedIn(request)){
			args.ownerAccountId = userAccount(request).getUserAccountId();
		}
		
		List<UserItem> list = bean.findMatchingUserItems(args);
		Map<String,List<UserItem>> model = new HashMap<String,List<UserItem>>();
		model.put("items", list);
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GerSupportedLanguages", method = {RequestMethod.GET})
	public void gerSupportedLanguages(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
//		List<Language> list = bean.getSupportedLanguages();
//		Map<String,Object> model = new HashMap<String,Object>();
//		model.put("list", list);
		JacksonUtils.serialize(bean.getSupportedLanguages(), response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollReviews", method = {RequestMethod.GET})
	public void scrollReviews(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		ScrollReviewsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollReviewsArgs.class);
		ScrollResult result = this.bean.scrollReviews(args);
		JacksonUtils.serialize(result, response.getWriter());
	}
	
//	@JsonSerialize(using=ScrollResultsSerializer.class)
//	@RequestMapping(value = "/service/ScrollArticleComments", method = {RequestMethod.GET})
//	public ScrollResult<ArticleComment> scrollArticleComments(HttpServletRequest request,HttpServletResponse response, @RequestParam String content) 
//	throws Exception {
//		ScrollArticleCommentsArgs args = JacksonUtils.deserializeAs(content, ScrollArticleCommentsArgs.class);
//		ScrollResult<ArticleComment> result = this.bean.scrollArticleComments(args);
////		JacksonUtils.serialize(result, response.getWriter());
//		return result;
//	}
	
	
	@RequestMapping(value = "/service/ScrollArticleComments", method = {RequestMethod.GET})
	@ResponseBody
	public ScrollResult scrollArticleComments(HttpServletRequest request,HttpServletResponse response, @RequestParam String content) 
	throws Exception {
		ScrollArticleCommentsArgs args = JacksonUtils.deserializeAs(content, ScrollArticleCommentsArgs.class);
		
		ScrollResult result = this.bean.scrollArticleComments(args);
		return result;
	}
	
	@RequestMapping(value = "/service/ScrollGenreDetails", method = {RequestMethod.GET})
	public void scrollGenreDetails(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ScrollGenreArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollGenreArgs.class);
		ScrollResult result = bean.scrollGenres(args);
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("genresList", result.list);
		model.put("count", result.count);
		
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GetGenreDetails", method = {RequestMethod.GET})
	public void getGenreDetails(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Map<String,String> map = JacksonUtils.deserializeStringMap(request.getParameter("content"));
		Long genreId = Long.valueOf(map.get("genreId"));
		GenreDetails genre = bean.findGenreDetails(genreId);
		JacksonUtils.serialize(genre, response.getWriter());
	}
	
	@RequestMapping(value = "/service/ScrollPublishedBookDetails", method = {RequestMethod.GET})
	public void scrollPublishedBookDetails(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		ScrollPublicationsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollPublicationsArgs.class);
		ScrollResult result = bean.scrollPublicationRecords(args);
//		System.out.println("---->result:: " + result.list.size() + ", count: " + result.count);
//		for(PublishedBookDetails a : result.list){
//			System.out.println("--->" + a.ratingCount + ", " + a.ratingAverage);
//		}
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("publications", result.list);
		model.put("count", result.count);
		model.put("userMessages", "");
		
		JacksonUtils.serialize(model, response.getWriter());		
	}
	
	@RequestMapping(value = "/service/ScrollUserPublishedBookDetails", method = {RequestMethod.GET})
	public void scrollUserPublishedBookDetails(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		ScrollPublicationsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollPublicationsArgs.class);
		args.conditionValue = userAccount(request).getUserAccountId().toString();
		args.conditionKey = ScrollPublicationsArgs.ConditionKey.BY_PUBLISHER;
		
		ScrollResult result = bean.scrollPublicationRecords(args);
//		System.out.println("---->result:: " + result.list.size() + ", count: " + result.count);
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("publications", result.list);
		model.put("count", result.count);
		model.put("userMessages", "");
		
		JacksonUtils.serialize(model, response.getWriter());		
	}
	
	@RequestMapping(value = "/service/GetAllUserAlbums", method = {RequestMethod.GET})
	public void getAllUserAlbums(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
		UserAccount userAccount = userAccount(request);
		List<String> list = bean.selectUserAlbums(userAccount.getUserAccountId());
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("albums", list);
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GetPublicationContributors", method = {RequestMethod.GET})
	public void getPublicationContributors(HttpServletRequest request,HttpServletResponse response) 
	throws NumberFormatException, Exception {
		Map<String,String> content = JacksonUtils.deserializeStringMap(request.getParameter("content"));//objectMapper.readValue(request.getParameter("content"), new TypeReference<Map<String, String>>(){}); 
		Map<String,Object> model = bean.findPublicationContributors(Long.parseLong(content.get("bookId")));
		
//		Long bookId = Long.parseLong(content.get("bookId"));
//		Map<String,Object> model = new HashMap<String,Object>();
//		List<Publisher> publisherList = bean.getAllPublishers(bookId);
//		model.put("publishers",JacksonUtils.toModelList(publisherList)); //publisher role will determine what exactly each publisher contributed
//		model.put("sheetOwners",JacksonUtils.toModelList(bean.getSheetOwners(bookId)));
//		model.put("contributorsByRole", bean.getContributorsMap(publisherList));
//		
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GetPathOfBranch", method = {RequestMethod.GET})
	public void getPathOfBranch(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Map<String,String> map = JacksonUtils.deserializeStringMap(request.getParameter("content"));
		Long bookId = Long.valueOf(map.get("bookId"));
		List<ParentBook> list = bean.discoverBranch(bookId);
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("branchList", list);
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GetBookDetailSheet", method = {RequestMethod.GET})
	public void getBookDetailSheet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
		UserAccount userAccount = userAccount(request);
		Map<String,String> map = JacksonUtils.deserializeStringMap(request.getParameter("content"));
		Long sheetId = Long.valueOf(map.get("sheetId"));
		Long bookId = null; //why always null?
		BookDetailsSheet sheet = bean.findBookDetailSheet(bookId, sheetId);
		if(sheet != null){
			sheet.evaluateAllowEdit(userAccount.getUserAccountId());
			JacksonUtils.serialize(sheet, response.getWriter());
		}
		else{
			throw new NullPointerException("The specified sheet could not be found! sheetId: " + sheetId);
		}
	}
	
	@RequestMapping(value = "/service/BrowseBookShowcase", method = {RequestMethod.GET})
	public void browseBookShowcase(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		ScrollBookShowcaseArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ScrollBookShowcaseArgs.class);
		
		List<BookShowcase> list = bean.browseBookShowcaseByDownloads(args);
//		System.out.println("-----> Branch New Book " + bookId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("publications", list);
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	@RequestMapping(value = "/service/GetUserFolders", method = {RequestMethod.GET})
	public void getUserSheets(HttpServletRequest request,HttpServletResponse response) 
	throws Exception {
		UserAccount userAccount = userAccount(request);
		
		List<String> list = bean.selectUserFolders(userAccount.getUserAccountId());
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("folders", list);
		JacksonUtils.serialize(model, response.getWriter());
	}
	
	//
}
