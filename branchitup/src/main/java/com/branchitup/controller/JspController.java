package com.branchitup.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.branchitup.exception.NoSuchItemException;
import com.branchitup.exception.PageNotFoundException;
import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.exception.UserAccountException;
import com.branchitup.exception.runtime.BookNotFoundException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.json.LiteBlogSerializer;
import com.branchitup.persistence.BookRole;
import com.branchitup.persistence.entities.BookDetails;
import com.branchitup.persistence.entities.BookDetailsSheet;
import com.branchitup.persistence.entities.GenreDetails;
import com.branchitup.persistence.entities.PublishedBookDetails;
import com.branchitup.persistence.entities.Sheet;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.MailService;
import com.branchitup.service.ModelService;
import com.branchitup.service.ScheduledService;
import com.branchitup.system.Constants;

@Controller
public class JspController extends AbstractController{
	private Logger logger = Logger.getLogger(JspController.class);
	
	@Autowired
	private ModelService modelService; 
	
	@Autowired
	private MailService mailService;
	
	@ExceptionHandler(NullPointerException.class)
	protected void handleException(NullPointerException exp, HttpServletRequest request, HttpServletResponse response){
		logger.error(exp.getMessage(), exp);
		try {
			response.sendRedirect("");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@ExceptionHandler(NumberFormatException.class)
	protected void handleNumberFormatException(NumberFormatException exp, HttpServletRequest request, HttpServletResponse response){
		try {
			response.sendRedirect("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ExceptionHandler(PageNotFoundException.class)
	protected void handlePageNotFoundException(PageNotFoundException exp, HttpServletRequest request, HttpServletResponse response){
		try {
			response.sendRedirect("pagenotavailable");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ExceptionHandler(BookNotFoundException.class)
	protected void handleBookNotFoundException(BookNotFoundException exp, HttpServletRequest request, HttpServletResponse response){
		try {
			response.sendRedirect("booknotavailable");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ExceptionHandler(UserAccountException.class)
	protected void handleUserAccountException(UserAccountException exp, HttpServletRequest request, HttpServletResponse response){
		exp.printStackTrace();
		try {
			exp.printStackTrace(response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@ExceptionHandler(UnauthorizedException.class)
	protected void handleUserAccountException(UnauthorizedException exp, HttpServletRequest request, HttpServletResponse response){
		try {
//			StringBuffer buf = request.getRequestURL();
//			@SuppressWarnings("unchecked")
//			Set<Entry<String,String[]>> paramSet = request.getParameterMap().entrySet();
//			if(paramSet != null && paramSet.size() > 0){
//				buf.append('?');
//				for(Entry<String,String[]> e : paramSet){
//					buf.append(e.getKey());
//					if(e.getValue().length > 0){
//						buf.append('=');				
//						buf.append(e.getValue()[0]);
//					}
//					buf.append('&');
//				}
//			}
			request.getSession().setAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL, extractUrl(request));
//			System.out.println("------------> Setting the URL to " + request.getSession().getAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL));
			response.sendRedirect("login");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) throws Exception {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
//		binder.registerCustomEditor(Timestamp.class, "createdOn", new CustomDateEditor(dateFormat, true));
//	}
	
//	http://98.221.50.197/branchitup/
//	@RequestMapping(value = {"/google44b2537eeaf22ad7.html"}, method = {RequestMethod.GET})
//	public String googleauth(HttpServletRequest request, HttpServletResponse response)
//	throws IOException, PersistenceException {
//		return "google44b2537eeaf22ad7.jsp";
//	}
	
//	//google web master authenticate 
//	@RequestMapping(value = {"/google4f8c9ddd5eb681f7.html"}, method = {RequestMethod.GET})
//	public String googleWebMasterAuth(HttpServletRequest request, HttpServletResponse response)
//	throws IOException, PersistenceException {
//		return "google4f8c9ddd5eb681f7.jsp";
//	}
	
//	@RequestMapping(value = {"/sitemap.xml"}, method = {RequestMethod.GET})
//	public String sitemap(HttpServletRequest request, HttpServletResponse response)
//	throws IOException, PersistenceException {
//		return "resources/WEB-INF/views/sitemap.xml";
//	}

//	static int count = 1;
	@RequestMapping(value = {"/home","/"}, method = {RequestMethod.GET})
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		ModelAndView mv = new ModelAndView("home.jsp");
//		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		try {
//			String s = getClientIP(request);
			if(request.getUserPrincipal() != null){
				mv.addObject("userAccount", userAccount(request));
//				
//				
//				UserAccount a = userAccount(request);
//				if(a.getFirstName() != null){
//					s += (" " + a.getFirstName());
//				}
//				if(a.getLastName() != null){
//					s += (" " + a.getLastName());
//				}
			}
//			System.out.println("ACCESS " + (count++) + ", by " + s + " at " + new Date());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping(value = {"/homejquery"}, method = {RequestMethod.GET})
	public ModelAndView homejquery(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		ModelAndView mv = new ModelAndView("homeq.jsp");
		try {
			if(request.getUserPrincipal() != null){
				mv.addObject("userAccount", userAccount(request));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@Autowired
	private ScheduledService ss;
	
	@RequestMapping(value = {"/signup"}, method = {RequestMethod.GET})
	public String signup(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "signup.jsp";
	}
	
	
	@RequestMapping(value = {"/restorepassword"}, method = {RequestMethod.GET})
	public String restorepassword(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		System.out.println("---->JspController.restorePassword");
		return "restore-password.jsp";
	}
	
	//not active
//	@RequestMapping(value = {"/resetpassword"}, method = {RequestMethod.GET})
	public String resetpassword(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, PageNotFoundException {
		String token = request.getParameter("token");
		if(StringUtils.isEmpty(token)){
			throw new PageNotFoundException();
		}
//		ModelAndView mv = new ModelAndView("reset-password.jsp");
		request.getSession().setAttribute("token", token);
//		mv.addObject("token", token);
		System.out.println("---->JspController.resetPassword");
		return "reset-password.jsp";
	}
	
	@RequestMapping(value = {"/passwordack"}, method = {RequestMethod.GET})
	public String passwordack(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "password-ack.jsp";
	}
	
	
	@RequestMapping(value = {"/publishedbook"}, method = {RequestMethod.GET})
	public ModelAndView publishedbook(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		ModelAndView mv = new ModelAndView("published-book.jsp");
		
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		String bookIdParam = request.getParameter("bookId");
		if(bookIdParam != null){
			Long bookId = Long.valueOf(bookIdParam);
			PublishedBookDetails p = modelService.findPublishedBookDetails(bookId);
			if(p != null){
				mv.addObject("book", p);
				mv.addObject("publisherComment", modelService.getPublisherComment(bookId));
//				mv.addObject("publisherRoles",publisherRoles(p.getPublisherRoleMask()));
				//put constants
				for(BookRole d : BookRole.values()){
					mv.addObject(d.toString(),d.maskVal);
				}
				if(isLoggedIn(request)){
					UserAccount userAccount = userAccount(request);
					mv.addObject("userAccountId",userAccount.getUserAccountId());
					Float userRating = modelService.findBookRatingByUser(bookId, userAccount(request).getUserAccountId());
					if(userRating != null){
						mv.addObject("userRating", userRating);
					}
					
					if(userAccount.getUserAccountId().equals(p.getPublisherAccountId())){
						mv.addObject("allowDeleting",true);
					}
				}
			}
			else{
				mv.addObject("exception", new NoSuchItemException("Book id " + bookIdParam + " not found"));
			}
		}
		return mv;
	}
	
//	@RequestMapping(value = {"/bookattachments"}, method = {RequestMethod.GET})
//	public ModelAndView bookattachments(HttpServletRequest request, HttpServletResponse response)
//	throws Exception{
//		ModelAndView mv = new ModelAndView("book-attachments.jsp");
//		
//		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
//		String bookIdParam = request.getParameter("bookId");
//		if(bookIdParam != null){
//			Long bookId = Long.valueOf(bookIdParam);
//			PublishedBookDetails p = modelService.findPublishedBookDetails(bookId);
//			if(p != null){
////				System.out.println("-genre---> " + p.getGenreTitles().size());
//				mv.addObject("book", p);
//				
//				//put constants
//				for(BookRole d : BookRole.values()){
//					mv.addObject(d.toString(),d.maskVal);
//				}
////				//roles constants
////				mv.addObject("PUBLISHER_ROLES",PublisherRole);
//				if(isLoggedIn(request)){
//					UserAccount userAccount = userAccount(request);
//					Float userRating = modelService.findBookRatingByUser(bookId, userAccount(request).getUserAccountId());
//					if(userRating != null){
//						mv.addObject("userRating", userRating);
//					}
//					
//					if(userAccount.getUserAccountId().equals(p.getPublisherAccountId())){
//						mv.addObject("allowDeleting",true);
//					}
//				}
//			}
//			else{
//				mv.addObject("exception", new NoSuchItemException("Book id " + bookIdParam + " not found"));
//			}
//		}
//		return mv;
//	}
	
	@RequestMapping(value = {"/publishbook"}, method = {RequestMethod.GET})
	public ModelAndView publishbook(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, NoSuchItemException, UnauthorizedException{
		userAccount(request);
		ModelAndView mv = new ModelAndView("publish-book.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		String bookIdParam = request.getParameter("bookId");
		if(bookIdParam != null){
			Long bookId = Long.valueOf(bookIdParam);
			mv.addObject("supportedLanguages",modelService.getSupportedLanguages());
			BookDetails book = modelService.findBookDetails(bookId);
			
			if(book != null){
				mv.addObject("derivedGenres",modelService.findDerivedGenresByBook(bookId));
				mv.addObject("book", book);
				mv.addObject("sheetsCount", modelService.countBookSheets(bookId));
				
				//put constants
				for(BookRole d : BookRole.values()){
					mv.addObject(d.toString(),d.maskVal);
				}
			}
			else{
				throw new PersistenceException("The specified book was not found in the system");
			}
		}
		return mv;
	}
	
	@RequestMapping(value = {"/workdesk"}, method = {RequestMethod.GET})
	public ModelAndView workdesk(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserAccount userAccount = userAccount(request);
		ModelAndView mv = new ModelAndView("work-desk.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		try {
			mv.addObject("userAccount", userAccount);
			mv.addObject("blogs",JacksonUtils.serialize(modelService.getUserBlogs(userAccount.getUserAccountId()), new LiteBlogSerializer()));
			List<String> folders = modelService.selectUserFolders(userAccount.getUserAccountId());
			mv.addObject("folders",JacksonUtils.serialize(folders));
		} 
		catch (UnauthorizedException e) {
			response.sendRedirect("login");
		}
		return mv;
	}
	
	@RequestMapping(value = {"/userwall"}, method = {RequestMethod.GET})
	public ModelAndView userwall(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		userAccount(request);
		ModelAndView mv = new ModelAndView("user-wall.jsp");
		String userAccountIdParam = request.getParameter("userAccountId");
		if(userAccountIdParam != null){
			Long userAccountId = Long.valueOf(userAccountIdParam);
			mv.getModel().put("userAccountId", userAccountId);
			UserAccount userAccount = modelService.findUserAccount(userAccountId);
			if(userAccount != null){
				mv.getModel().put("userFullName", userAccount.getFirstName() + " " + userAccount.getLastName());
			}
		}
		else{
			throw new PageNotFoundException();
		}
		
		return mv;
	}
	@RequestMapping(value = "/genres", method = {RequestMethod.GET})
	public String genres(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "genres.jsp";
	}
	
	@RequestMapping(value = "/genre", method = {RequestMethod.GET})
	public ModelAndView genre(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		ModelAndView mv = new ModelAndView("genre.jsp");
//		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		
		String genreIdParam = request.getParameter("genreId");
		if(genreIdParam != null){
			Long genreId = null;
			try {
				genreId = Long.valueOf(genreIdParam);
			} catch (NumberFormatException exp) {
				response.sendRedirect("genres");
				return null;
			}
			
			GenreDetails g = modelService.findGenreDetails(genreId);
			
			if(g != null){
				mv.addObject("genre", g);
			}
//			else{
//				mv.addObject("exception", new NoSuchItemException("Genre id " + genreIdParam + " not found"));
//			}
		}
		return mv;
	}
	@RequestMapping(value = "/contactus", method = {RequestMethod.GET})
	public String contactus(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return "contact-us.jsp";
	}
	@RequestMapping(value = "/genreeditor", method = {RequestMethod.GET})
	public ModelAndView genreeditor(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		userAccount(request);
		
		ModelAndView mv = new ModelAndView("genre-editor.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		
		String genreIdParam = request.getParameter("genreId");
		String noHeaderParam = request.getParameter("noHeader");
		
		mv.addObject("noHeader", (noHeaderParam != null));
		
		if(genreIdParam != null){
			try {
				Long genreId = Long.valueOf(genreIdParam);
				GenreDetails g = modelService.findGenreDetails(genreId);
				if(g != null){
					mv.addObject("genre", g);
				}
				else{
					response.sendRedirect("genreeditor");
				}
				
			} 
			catch (NumberFormatException exp) {
				throw new UnauthorizedException("Invalid genre ID");
			}
		}
//		else{
//			throw new UnauthorizedException("Genre not found");
//		}
		return mv;
	}
	
//	@RequestMapping(value = "/sheeteditor", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView sheeteditor(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		ModelAndView mv = new ModelAndView("sheet-editor.jsp");
//		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
//		String sheetId = request.getParameter("sheetId");
//		UserAccount userAccount = userAccount(request);
//		if(sheetId != null){
//			Sheet sheet = bean.findSheet(Long.parseLong(sheetId));
//			Map<String,Object> sheetModel = new HashMap<String,Object>();
//			sheetModel.put("sheetId", sheet.getSheetId());
//			sheetModel.put("name", sheet.getName().replace("\"", "\\\""));
//			sheetModel.put("folderName", JacksonUtils.serialize(sheet.getFolderName()));
//			sheetModel.put("content", sheet.getContent().replace("\"", "\\\""));
//			mv.getModel().put("albums", JacksonUtils.serialize(bean.selectUserAlbums(userAccount.getUserAccountId())));
//			if(sheet.getOwnerAccountId().equals(userAccount.getUserAccountId())){
//				sheetModel.put("ownedByUser", true);
//			}
//			else{
//				sheetModel.put("ownedByUser", false);
//			}
//			if(sheet.getPermissionsMask().intValue() == Constants.SheetPermission.PUBLIC_GROUP_PRIVATE_ALL){
//				sheetModel.put("visibility", "PUBLIC");
//				sheetModel.put("allowEditing", true);
//			}
//			else if(sheet.getPermissionsMask().intValue() == Constants.SheetPermission.PUBLIC_GROUP_VIEW){
//				sheetModel.put("visibility", "PUBLIC");
//				sheetModel.put("allowEditing", false);
//			}
//			else{
//				sheetModel.put("visibility", "PRIVATE");
//				sheetModel.put("allowEditing", false);
//			}
//			mv.getModel().put("sheet", sheetModel);
//		}
//		return mv;
//	}
	
	public static class SheetPermissions{
		public String visibility = "PRIVATE";
		public boolean allowEditing = false;
		public boolean ownedByUser = false;
		public boolean forcePrivate = false;
	}
	
	private SheetPermissions getSheetPermissions(UserAccount userAccount, Sheet sheet){
		SheetPermissions p = new SheetPermissions();
		if(sheet.getPermissionsMask().intValue() >= Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN_EDIT){
			p.visibility = "PUBLIC";
			if(sheet.getBlogId() == null){
				p.allowEditing = true;
			}
			
		}
		else if(sheet.getPermissionsMask().intValue() >= Constants.SheetPermission.PUBLIC_GROUP_VIEW_JOIN){
			p.visibility = "PUBLIC";
		}
		else{
		}
		
		if(userAccount != null){
			if(sheet.getOwnerAccountId().equals(userAccount.getUserAccountId())){
				p.ownedByUser = true;
				if(sheet.getBlogId() == null){
					p.allowEditing = true;
				}
				
				//we are not permitting changing the visibility if the sheet was branched out
				if(sheet.getDerivedFromId() != null){
					p.forcePrivate = true;
				}
				
			}
			else{
				p.ownedByUser = false;
			}
		}
		else{
			p.ownedByUser = false;
		}
//		System.out.println("*************************----------: sheet.getDerivedFromId() " + sheet.getDerivedFromId());
		
		
		return p;
	}
	
	@RequestMapping(value = {"/article"}, method = {RequestMethod.GET})
	public ModelAndView articles(HttpServletRequest request, HttpServletResponse response, @RequestParam Long articleId)
	throws Exception{
		Sheet sheet = modelService.findSheet(articleId);
		if(sheet != null && sheet.getBlogId() != null){
			request.getSession().setAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL, extractUrl(request));
			ModelAndView mv = new ModelAndView("article.jsp");
			mv.addObject("sheet", sheet);
			modelService.incrementSheetViewsCount(sheet.getSheetId());
			return mv;
		}
		else{
			throw new PageNotFoundException();
		}
	}
	
//	@RequestMapping(value = {"/article/{articleId}"}, method = {RequestMethod.GET})
//	public ModelAndView articles(HttpServletRequest request, HttpServletResponse response, @PathVariable Long articleId)
//	throws Exception{
//		Sheet sheet = modelService.findSheet(articleId);
//		if(sheet != null && sheet.getBlogId() != null){
//			ModelAndView mv = new ModelAndView("article.jsp");
//			mv.addObject("sheet", sheet);
//			return mv;
//		}
//		else{
//			throw new PageNotFoundException();
//		}
//	}
	
	@RequestMapping(value = {"/sheetpreview"}, method = {RequestMethod.GET})
	public ModelAndView sheetpreview(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		ModelAndView mv = new ModelAndView("sheet-preview.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		
		String sheetIdParam = request.getParameter("sheetId");
		String noHeaderParam = request.getParameter("noHeader");
		
		if(sheetIdParam != null){
			Long sheetId = Long.valueOf(sheetIdParam);
			Sheet sheet = modelService.findSheet(sheetId);
			
			if(sheet != null){
				UserAccount userAccount = null;
				try {
					userAccount = userAccount(request);
				} catch (Exception e) {
					//not logged in
				}
				SheetPermissions p = getSheetPermissions(userAccount, sheet);
				
				//--
				if(p.ownedByUser || "PUBLIC".equals(p.visibility)){
					mv.addObject("sheet", sheet);
					mv.addObject("allowEditing",p.allowEditing);
					
					if(noHeaderParam != null){
						mv.addObject("noHeader", true);
					}
					else{
						mv.addObject("noHeader", false);
					}
										
				}
				else{
					throw new PageNotFoundException();
				}
				
			}
			else{
				throw new PageNotFoundException();
			}
		}
		else{
			throw new PageNotFoundException();
		}
		return mv;
	}
	
//	@RequestMapping(value = "/sheeteditor", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView sheeteditor(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		//to edit a sheet the user must be logged in
//		UserAccount userAccount = userAccount(request);
//		ModelAndView mv = new ModelAndView("sheet-editor.jsp");
//		mv.getModel().put("albums", JacksonUtils.serialize(modelService.selectUserAlbums(userAccount.getUserAccountId())));
//		
//		String sheetIdParam = request.getParameter("sheetId");
//		if(sheetIdParam != null){
//			Long sheetId = Long.parseLong(sheetIdParam);
//			Sheet sheet = modelService.findSheet(sheetId);
//			
//			if(sheet.getOwnerAccountId().equals(userAccount.getUserAccountId()) || sheet.getPermissionsMask().intValue() >= Constants.SheetPermission.PUBLIC_GROUP_VIEW){
//				Map<String,Object> sheetModel = new HashMap<String,Object>();
//				sheetModel.put("sheetId", sheet.getSheetId());
//				sheetModel.put("name",sheet.getName());
//				sheetModel.put("folderName",sheet.getFolderName());
//				
//				sheetModel.put("content", sheet.getContent());
//				
//				mv.getModel().put("sheet", sheetModel);
//				mv.getModel().put("permissions", JacksonUtils.serialize(getSheetPermissions(userAccount,sheet)));
//			}
//			else{
//				throw new PageNotFoundException();
//			}
//		}
//		return mv;
//	}
	
	@RequestMapping(value = "/sheeteditor", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView sheeteditor(HttpServletRequest request, HttpServletResponse response, 
	@RequestParam(required=false) Long sheetId) throws Exception {
		//to edit a sheet the user must be logged in
		UserAccount userAccount = userAccount(request);
		ModelAndView mv = new ModelAndView("sheet-editor.jsp");
		mv.getModel().put("albums", JacksonUtils.serialize(modelService.selectUserAlbums(userAccount.getUserAccountId())));
		if(sheetId != null){
			Sheet sheet = modelService.findSheet(sheetId);
			if(sheet.getBlogId() != null){
				throw new PageNotFoundException();
			}
			if(sheet.getOwnerAccountId().equals(userAccount.getUserAccountId()) || sheet.getPermissionsMask().intValue() >= Constants.SheetPermission.PUBLIC_GROUP_VIEW){
				Map<String,Object> sheetModel = new HashMap<String,Object>();
				sheetModel.put("sheetId", sheet.getSheetId());
				sheetModel.put("name",sheet.getName());
				sheetModel.put("folderName",sheet.getFolderName());
				
				sheetModel.put("content", sheet.getContent());
				
				mv.getModel().put("sheet", sheetModel);
				mv.getModel().put("permissions", JacksonUtils.serialize(getSheetPermissions(userAccount,sheet)));
			}
			else{
				throw new PageNotFoundException();
			}
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/edituserprofile", method = {RequestMethod.GET})
	public ModelAndView edituserprofile(HttpServletRequest request, HttpServletResponse response)
	throws Exception, PersistenceException, UnauthorizedException {
		ModelAndView mv = new ModelAndView("edit-user-profile.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		UserAccount userAccount = userAccount(request);
		userAccount = modelService.findUserAccount(userAccount.getUserAccountId());
//		refreshUserAccount(request, modelService);
		mv.addObject("userAccount",userAccount);	
		return mv;
	}
	
//	@RequestMapping(value = "/edituserprofile", method = {RequestMethod.GET})
//	@ResponseBody
//	public UserAccount edituserprofile(HttpServletRequest request, HttpServletResponse response)
//	throws Exception, PersistenceException, UnauthorizedException {
//		UserAccount userAccount = userAccount(request);
//		userAccount = modelService.findUserAccount(userAccount.getUserAccountId());
//		return userAccount;
//	}
	
	@RequestMapping(value = "/userprofile", method = {RequestMethod.GET})
	public ModelAndView userprofile(HttpServletRequest request, HttpServletResponse response)
	throws Exception, PersistenceException, UnauthorizedException {
		String userAccountIdParam = request.getParameter("userAccountId");
		ModelAndView mv = new ModelAndView("user-profile.jsp");
		
		if(userAccountIdParam != null){
			Long userAccountId = Long.valueOf(userAccountIdParam);
			UserAccount userAccount = modelService.findUserAccount(userAccountId);
			if(userAccount != null){
				if(UserAccount.Visibility.PUBLIC.equals(userAccount.getVisibility())){
					mv.addObject("userAccount",userAccount);
				}
				if(UserAccount.Visibility.HIDE_CONTACT_INFORMATION.equals(userAccount.getVisibility())){
					mv.addObject("userAccount",userAccount);
				}
				if(UserAccount.Visibility.PRIVATE.equals(userAccount.getVisibility())){
					throw new PageNotFoundException();
				}
			}
			else{
				throw new PageNotFoundException();
			}
		}
		else{
			throw new com.branchitup.exception.InvalidInputException("Invalid user account");
		}
			
		return mv;
	}
	
	@RequestMapping(value = "/test", method = {RequestMethod.GET,RequestMethod.POST})
	public String test(HttpServletRequest request, HttpServletResponse response){
		return "test.jsp";
	}
	
	@RequestMapping(value = "/pagenotavailable", method = {RequestMethod.GET})
	public String pageNotUvailable(HttpServletRequest request, HttpServletResponse response){
		return "page-not-available.jsp";
	}
	
	@RequestMapping(value = "/booknotavailable", method = {RequestMethod.GET})
	public String bookNotAvailable(HttpServletRequest request, HttpServletResponse response){
		return "book-not-available.jsp";
	}
	
	@RequestMapping(value = "/bookeditor", method = {RequestMethod.GET})
	public ModelAndView bookeditor(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException, PageNotFoundException {
		ModelAndView mv = new ModelAndView("book-editor.jsp");
		mv.getModel().put("params", JacksonUtils.serialize(request.getParameterMap()));
		String bookIdParam = request.getParameter("bookId");
		UserAccount userAccount = userAccount(request);

		mv.addObject("userSheetsCount", modelService.countUserSheets(userAccount.getUserAccountId()));
		
		if(bookIdParam != null){
			Long bookId = Long.parseLong(bookIdParam);
//			userAccount = userAccount(request);
			Long ownerAccountId = modelService.getBookOwnerAccountId(bookId);
			if(ownerAccountId != null && ownerAccountId.equals(userAccount.getUserAccountId())){
				BookDetails book = modelService.findBookDetails(bookId);
				for(BookDetailsSheet s : book.getSheetList()){
					s.evaluateAllowEdit(userAccount.getUserAccountId());
				}
				mv.addObject("book",book);
			}
			else{
//				mv = new ModelAndView("page-not-available.jsp");
				throw new PageNotFoundException();
			}
		}
		return mv;
	}
	
	@RequestMapping(value = "/readbook", method = {RequestMethod.GET})
	public String readBook(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "readbook.jsp";
	}
	
	@RequestMapping(value = "/sheets", method = {RequestMethod.GET})
	public String sheets(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "sheets.jsp";
	}
	
	@RequestMapping(value = "/userbooks", method = {RequestMethod.GET})
	public String userbooks(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		return "userbooks.jsp";
	}
	
//	@RequestMapping(value = "/sheeteditor", method = {RequestMethod.GET})
//	public String sheeteditor(HttpServletRequest request, HttpServletResponse response)
//	throws IOException, PersistenceException {
//		return "sheeteditor.jsp";
//	}
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST,RequestMethod.GET})
	public String handleRequest(HttpServletRequest request,HttpServletResponse response) 
	throws ServletException, IOException {
		return "login.jsp";
	}
}
