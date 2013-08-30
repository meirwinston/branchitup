package com.branchitup.controller.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.model.EntityModel;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.ImageFile;
import com.branchitup.persistence.entities.ImageFile.SystemFolder;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.ModelService;
import com.branchitup.system.Constants;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.FileItemArgs;

@Controller
public class FileUpload extends ServiceController{
	@Autowired
	private ModelService bean;
	
	//up to 10MB in memory image
	private DiskFileItemFactory factory = new DiskFileItemFactory(10000000,new File(Utils.getTmpDir()));
	
	@RequestMapping(value = "/service/FileUpload", method = {RequestMethod.POST,RequestMethod.GET})
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
		Map<String,String> params = JacksonUtils.deserializeStringMap(request.getParameter("params"));//objectMapper.readValue(request.getParameter("content"), new TypeReference<Map<String, Object>>(){});
//		System.out.println("FileUpload.handleRequest::"+params);
		Constants.UploadTarget target = Constants.UploadTarget.UPLOADS; //initialize with default value
		
		String s = (String)params.get("target");
 		if(s != null){
			target = Constants.UploadTarget.valueOf(s);
		}
		switch (target) {
		case SESSION:
			storeInSession(request, response);
			break;
		case GENRES:
			storeImageInDisk(request,response,Constants.UploadTarget.GENRES);
			break;
		case PROFILE:
			storeImageInDisk(request,response,Constants.UploadTarget.PROFILE);
			break;
		default:
			storeImageInDisk(request,response,Constants.UploadTarget.UPLOADS);
//			storeInSession(request, response); //TEST ONLY
			break;
		}
	}
	
	@RequestMapping(value = "/service/UploadAudioFlash", method = {RequestMethod.POST,RequestMethod.GET})
	public void uploadAudioFlash(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
		userAccount(request);
		storeInSessionFlash(request,response);
	}
	
	@RequestMapping(value = "/service/UploadAudioHTML5", method = {RequestMethod.POST,RequestMethod.GET})
	public void uploadAudioHTML5(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException, UnauthorizedException {
		userAccount(request);
		storeInSessionHTML5(request,response);
	}
	
//	@SuppressWarnings("unchecked")
//	protected void storeImageInDisk(HttpServletRequest request, HttpServletResponse response,Constants.UploadTarget target) throws IOException, UnauthorizedException {
//		System.out.println("--->storeImageInDisk " + request.getParameterMap());
//		ServletFileUpload upload = new ServletFileUpload(factory);
//		UserAccount userAccount = userAccount(request);
//		try {
//			if(ServletFileUpload.isMultipartContent(request)){
//				List<FileItem> fileItemList = upload.parseRequest(request);
//				JsonNode jparams = JacksonUtils.toNode(request.getParameter("params")); //^^
//				String method = jparams.get("method").getTextValue(); //^^
//				Object res = null;
//				if("flash".equals(method)){
//					response.setContentType("text/plain");
//					res = new StringBuilder();
//				}
//				else{
//					response.setContentType("text/json");
//					res = new ArrayList<ImageFileModel>(); //^^
//					
//				}
//				//IE responses with multiple fileItems, make sure to parse non formFiled only
//				for(FileItem fileItem : fileItemList){
//					if(fileItem.isFormField()){ //IE Flash - extra fileItem -> ignore
////						Logger.info("FileUploadServlet.storeImageInUploads.formField " + fileItem);
//					}
//					else{
////						Logger.info("FileUploadServlet.storeImageInUploads.NOT formField "  + fileItem);
//						System.out.println("---->FileUploadServlet.storeImageInUploads:" + target);
//						ImageFile imageFile = null;
//						FileItemArgs args = new FileItemArgs();
//						
//						switch(target){
//						case UPLOADS:
//							args = new FileItemArgs();
//							System.out.println("--->FileUpload: " + request.getParameterMap());
//							args.album = request.getParameter("album");
//							args.systemFolder = SystemFolder.USER_UPLOADS;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//							break;
//						case GENRES:
//							args = new FileItemArgs();
//							args.systemFolder = SystemFolder.GENRES;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//							break;
//						case PROFILE:
//							args = new FileItemArgs();
//							args.systemFolder = SystemFolder.PROFILE_PHOTOS;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//						}
//						
//						if("flash".equals(method)){
//							//--- RESPONSE - Flash IE
//							((StringBuilder)res).append(ImageFileModel.toFlashFormat(imageFile));
////							response.getWriter().println(new JSONImageFile().toFlashFormat(imageFile));
//						}
//						else{
//							//--- RESPONSE - FireFox/HTML5
//							((List<ImageFileModel>)res).add(new ImageFileModel(imageFile));
//						}
//					}
//				}
//				JacksonUtils.serialize(res,response.getWriter());
//			}
//			else{
//				Constants.LOGGER.info("FileUploadServlet.storeImageInUploads.NOT multipart.fileItem: " + upload.parseRequest(request));
//			}
//		} 
//		catch (Exception exp) {
//			exp.printStackTrace();
//		} 
//	}
	
	@SuppressWarnings("unchecked")
	protected void storeImageInDisk(HttpServletRequest request, HttpServletResponse response,Constants.UploadTarget target) throws IOException, UnauthorizedException {
		ServletFileUpload upload = new ServletFileUpload(factory);
		UserAccount userAccount = userAccount(request);
		try {
			if(ServletFileUpload.isMultipartContent(request)){
				List<FileItem> fileItemList = upload.parseRequest(request);
				Map<String,String> paramsMap =  JacksonUtils.deserializeStringMap(request.getParameter("params"));
//				JsonNode jparams = JacksonUtils.toNode(request.getParameter("params")); //^^
//				String method = paramsMap.get("method"); //^^
				response.setContentType("text/html");
//				Object res = null;
//				if("flash".equals(method)){
//					response.setContentType("text/plain");
//					res = new StringBuilder();
//				}
//				else{
//					response.setContentType("text/json");
//					res = new ArrayList<ImageFileModel>(); //^^
//					
//				}
				List<ImageFile> imageFileList = new ArrayList<ImageFile>();
				//IE responses with multiple fileItems, make sure to parse non formFiled only
				for(FileItem fileItem : fileItemList){
					if(fileItem.isFormField()){ //IE Flash - extra fileItem -> ignore
//						Logger.info("FileUploadServlet.storeImageInUploads.formField " + fileItem);
					}
					else{
//						Logger.info("FileUploadServlet.storeImageInUploads.NOT formField "  + fileItem);
						ImageFile imageFile = null;
						FileItemArgs args = new FileItemArgs();
						
						switch(target){
						case UPLOADS:
							args = new FileItemArgs();
							args.album = paramsMap.get("album");
							args.systemFolder = SystemFolder.USER_UPLOADS;
							args.fileItem = fileItem;
							args.userAccountId = userAccount.getUserAccountId();
							imageFile = bean.storeImage(args);
							
							break;
						case GENRES:
							args = new FileItemArgs();
							args.systemFolder = SystemFolder.GENRES;
							args.fileItem = fileItem;
							args.userAccountId = userAccount.getUserAccountId();
							imageFile = bean.storeImage(args);
							break;
						case PROFILE:
							args = new FileItemArgs();
							args.systemFolder = SystemFolder.PROFILE_PHOTOS;
							args.fileItem = fileItem;
							args.userAccountId = userAccount.getUserAccountId();
							bean.clearUserProfileImages(args.userAccountId);
							imageFile = bean.storeImage(args);
							refreshUserAccount(request,bean);
							break;
						case SESSION:
							break;
						case CAPTCHA:
							break;
						}
						
						if(imageFile != null){
//							System.out
							imageFileList.add(imageFile);
						}
						
//						if("flash".equals(method)){
//							//--- RESPONSE - Flash IE
//							((StringBuilder)res).append(ImageFileModel.toFlashFormat(imageFile));
////							response.getWriter().println(new JSONImageFile().toFlashFormat(imageFile));
//						}
//						else{
//							//--- RESPONSE - FireFox/HTML5
//							((List<ImageFileModel>)res).add(new ImageFileModel(imageFile));
//						}
					}
				}
				
				List<String> urls = new ArrayList<String>();
				for(ImageFile imageFile : imageFileList){
					urls.add(StatelessDAO.resolveThumbnailUrl(imageFile));
				}
				System.out.println("FileUploads " + JacksonUtils.serialize(urls));
				response.getWriter().print("<textarea>{urls: " + JacksonUtils.serialize(urls) + "}</textarea>");
//				response.getWriter().print("<textarea>value='" + JacksonUtils.serialize(urls) + "',status=''</textarea>");
			}
			else{
				Constants.LOGGER.info("FileUploadServlet.storeImageInUploads.NOT multipart.fileItem: " + upload.parseRequest(request));
			}
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		} 
	}
	
	protected static Map<String,Object> generateJsonResponse(FileItem fileItem){
		Map<String,Object> json = new HashMap<String,Object>();
		
		json.put("file", fileItem.getName());
		json.put("fieldName", fileItem.getFieldName());
		json.put("name", fileItem.getName());
		json.put("size", fileItem.getSize());
		json.put("resourceType", fileItem.getContentType());
		
		return json;
	}
	
	protected static String generateFlashResponse(FileItem fileItem, String fileItemId){
		StringBuilder sb = new StringBuilder();
		
		sb.append("fileItemId");
		sb.append("=");
		sb.append(fileItemId);
		sb.append(",");
		
		sb.append("file");
		sb.append("=");
		sb.append(fileItem.getName());
		sb.append(",");
		
		sb.append("fieldName");
		sb.append("=");
		sb.append(fileItem.getFieldName());
		sb.append(",");
		
		sb.append("name");
		sb.append("=");
		sb.append(fileItem.getName());
		sb.append(",");
		
		sb.append("size");
		sb.append("=");
		sb.append(fileItem.getSize());
		sb.append(",");
		
		sb.append("resourceType");
		sb.append("=");
		sb.append(fileItem.getContentType());
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected void storeInSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("FileUploadServlet.storeInSession: " + request.getParameterMap());
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			if(ServletFileUpload.isMultipartContent(request)){
				List<FileItem> fileItemList = upload.parseRequest(request);
//				JsonNode jparams = JacksonUtils.toNode(request.getParameter("params"));
				response.setContentType("text/html");
				for(FileItem fileItem : fileItemList){
					if(fileItem.isFormField()){
//						Constants.LOGGER.info("FileUploadServlet.storeInSession - file item is form field::: " + fileItem);
					}
					else{
						
						Constants.LOGGER.info("FileUploadServlet.storeImageInSession.NOT formField "  + fileItem);
//						ImageFile imageFile = webClientBean.storeImageInUploads(fileItem);
						String id = UUID.randomUUID().toString();
						request.getSession().setAttribute(id, fileItem);
						
						response.getWriter().print("<textarea>{target: 'SESSION',id: '" + id + "'}</textarea>");
					}
				}
				
			}
		} 
		catch (Exception exp) {
			Constants.LOGGER.error(exp.getMessage(),exp);
			JacksonUtils.serialize(EntityModel.createErrorMessage("FileUpload Exception", exp.getMessage()), response.getWriter());
		} 
	}
	
	@SuppressWarnings("unchecked")
	protected void storeInSessionFlashOrHTML5(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("FileUploadServlet.storeInSessionFlashOrHTML5: " + request.getParameterMap());
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			if(ServletFileUpload.isMultipartContent(request)){
				List<FileItem> fileItemList = upload.parseRequest(request);
				List<Map<String,Object>> jsonResponse = new ArrayList<Map<String,Object>>();
				StringBuilder stringResponse = new StringBuilder();
				
				String params = request.getParameter("params");
				
				JsonNode jparams = JacksonUtils.toNode(params);
				String method = jparams.get("method").getTextValue();//request.getParameter("method"); 
				logger.info("Upload file, METHOD: " + method);
				
				if("flash".equals(method)){
					response.setContentType("text/plain");
				}
				else{
					response.setContentType("text/json");
				}
				
				for(FileItem fileItem : fileItemList){
					if(fileItem.isFormField()){
						Constants.LOGGER.info("FileUploadServlet.storeInSessionFlashOrHTML5 - file item is form field::: " + fileItem);
					}
					else{
						
						Constants.LOGGER.info("FileUploadServlet.storeInSessionFlashOrHTML5.NOT formField "  + fileItem);
//						ImageFile imageFile = webClientBean.storeImageInUploads(fileItem);
						String id = UUID.randomUUID().toString();
						request.getSession().setAttribute(id, fileItem);
						
						//--- RESPONSE - 
						if("flash".equals(method)){
							stringResponse.append(generateFlashResponse(fileItem, id));
						}
						else{
							//-- FireFox/HTML5
							Map<String,Object> o = generateJsonResponse(fileItem);
							o.put("fileItemId", id);
							jsonResponse.add(o);
							
						}
					}
				}
				if("flash".equals(method)){
					response.getWriter().print(stringResponse);
				}
				else{
					JacksonUtils.serialize(jsonResponse, response.getWriter());
				}
			}
		} 
		catch (Exception exp) {
			Constants.LOGGER.error(exp.getMessage(),exp);
			JacksonUtils.serialize(EntityModel.createErrorMessage("FileUpload Exception", exp.getMessage()), response.getWriter());
		} 
	}
	
	@SuppressWarnings("unchecked")
	protected void storeInSessionFlash(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("FileUploadServlet.storeInSessionFlashOrHTML5: " + request.getParameterMap());
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			if(ServletFileUpload.isMultipartContent(request)){
				List<FileItem> fileItemList = upload.parseRequest(request);
				Map<String,Object> jsonResponse = new HashMap<String,Object>();
				response.setContentType("text/json");
				for(FileItem fileItem : fileItemList){
					if(fileItem.isFormField()){
						logger.info("FileUploadServlet.storeInSessionFlash - file item is form field::: " + fileItem);
					}
					else{
						
						logger.info("FileUploadServlet.storeInSessionFlash.NOT formField "  + fileItem);
						String id = UUID.randomUUID().toString();
						request.getSession().setAttribute(id, fileItem);
						
						//--- RESPONSE - 
						jsonResponse = generateJsonResponse(fileItem);
						jsonResponse.put("id", id);
						jsonResponse.put("sessionAttributeId", id);
						
					}
				}
				//IE is very sensitive on the response
				JacksonUtils.serialize(jsonResponse, response.getWriter());
				
			}
		} 
		catch (Exception exp) {
			logger.error(exp.getMessage(),exp);
			JacksonUtils.serialize(EntityModel.createErrorMessage("Problem Uploading Audio", exp.getMessage()), response.getWriter());
		} 
	}
	
	/**
	 * returns a JSON array and not JSON object as the Flash method
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void storeInSessionHTML5(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		System.out.println("FileUploadServlet.storeInSessionHTML5: " + request.getParameterMap());
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			if(ServletFileUpload.isMultipartContent(request)){
				List<FileItem> fileItemList = upload.parseRequest(request);
				List<Map<String,Object>> jsonResponse = new ArrayList<Map<String,Object>>();
				response.setContentType("text/json");
				
				for(FileItem fileItem : fileItemList){
					if(fileItem.isFormField()){
						logger.info("FileUploadServlet.storeInSessionHTML5 - file item is form field::: " + fileItem);
					}
					else{
						logger.info("FileUploadServlet.storeInSessionHTML5.NOT formField "  + fileItem);
						String id = UUID.randomUUID().toString();
						request.getSession().setAttribute(id, fileItem);
						
						//-- FireFox/HTML5
						Map<String,Object> o = generateJsonResponse(fileItem);
						o.put("sessionAttributeId", id);
						jsonResponse.add(o);
					}
				}
				
				//Json list
				JacksonUtils.serialize(jsonResponse, response.getWriter());
			}
		} 
		catch (Exception exp) {
			Constants.LOGGER.error(exp.getMessage(),exp);
			JacksonUtils.serialize(EntityModel.createErrorMessage("FileUpload Exception", exp.getMessage()), response.getWriter());
		} 
	}
	
	public static void main(String[] args){
//		String s = "{dirPath:diskresources/images/uploads,height:910,width:600,fileName:81c98d36-21a8-4761-a042-9b18f7acc907.JPEG,absolutePath:/yblobenv/diskresources/images/uploads/81c98d36-21a8-4761-a042-9b18f7acc907.JPEG,format:JPEG}";
//		System.out.println(JSONObject.fromObject(s));
	}
	
//	@SuppressWarnings("unchecked")
//	protected void storeImageInDisk(HttpServletRequest request, HttpServletResponse response,Constants.UploadTarget target) throws IOException, UnauthorizedException {
//		ServletFileUpload upload = new ServletFileUpload(factory);
//		UserAccount userAccount = userAccount(request);
//		try {
//			if(ServletFileUpload.isMultipartContent(request)){
//				List<FileItem> fileItemList = upload.parseRequest(request);
//				Map<String,String> paramsMap =  JacksonUtils.deserializeStringMap(request.getParameter("params"));
////				JsonNode jparams = JacksonUtils.toNode(request.getParameter("params")); //^^
//				String method = paramsMap.get("method"); //^^
//				Object res = null;
//				if("flash".equals(method)){
//					response.setContentType("text/plain");
//					res = new StringBuilder();
//				}
//				else{
//					response.setContentType("text/json");
//					res = new ArrayList<ImageFileModel>(); //^^
//					
//				}
//				//IE responses with multiple fileItems, make sure to parse non formFiled only
//				for(FileItem fileItem : fileItemList){
//					if(fileItem.isFormField()){ //IE Flash - extra fileItem -> ignore
////						Logger.info("FileUploadServlet.storeImageInUploads.formField " + fileItem);
//					}
//					else{
////						Logger.info("FileUploadServlet.storeImageInUploads.NOT formField "  + fileItem);
//						ImageFile imageFile = null;
//						FileItemArgs args = new FileItemArgs();
//						
//						switch(target){
//						case UPLOADS:
//							args = new FileItemArgs();
//							args.album = paramsMap.get("album");
//							args.systemFolder = SystemFolder.USER_UPLOADS;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//							break;
//						case GENRES:
//							args = new FileItemArgs();
//							args.systemFolder = SystemFolder.GENRES;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//							break;
//						case PROFILE:
//							args = new FileItemArgs();
//							args.systemFolder = SystemFolder.PROFILE_PHOTOS;
//							args.fileItem = fileItem;
//							args.userAccountId = userAccount.getUserAccountId();
//							imageFile = bean.storeImage(args);
//						}
//						
//						if("flash".equals(method)){
//							//--- RESPONSE - Flash IE
//							((StringBuilder)res).append(ImageFileModel.toFlashFormat(imageFile));
////							response.getWriter().println(new JSONImageFile().toFlashFormat(imageFile));
//						}
//						else{
//							//--- RESPONSE - FireFox/HTML5
//							((List<ImageFileModel>)res).add(new ImageFileModel(imageFile));
//						}
//					}
//				}
//				JacksonUtils.serialize(res,response.getWriter());
//			}
//			else{
//				Constants.LOGGER.info("FileUploadServlet.storeImageInUploads.NOT multipart.fileItem: " + upload.parseRequest(request));
//			}
//		} 
//		catch (Exception exp) {
//			exp.printStackTrace();
//		} 
//	}
	
//	@SuppressWarnings("unchecked")
//	protected void storeImageInUploads(HttpServletRequest request, HttpServletResponse response) throws IOException, UnauthorizedException {
//		ServletFileUpload upload = new ServletFileUpload(factory);
////		String userName = userName(request);
//		UserAccount userAccount = userAccount(request);
//		
//		try {
//			if(ServletFileUpload.isMultipartContent(request)){
//				List<FileItem> fileItemList = upload.parseRequest(request);
//				//IE responses with multiple fileItems, make sure to parse non formFiled only
//				for(FileItem fileItem : fileItemList){
//					if(fileItem.isFormField()){ //IE Flash - extra fileItem -> ignore
////						Logger.info("FileUploadServlet.storeImageInUploads.formField " + fileItem);
//					}
//					else{
//						FileItemArgs args = new FileItemArgs();
//						args.fileItem = fileItem;
//						args.userAccountId = userAccount.getUserAccountId();
//						args.systemFolder = SystemFolder.USER_UPLOADS;
//						
//						ImageFile imageFile = bean.storeImage(args);
//						
//						JsonNode jparams = JacksonUtils.toNode(request.getParameter("params"));
//						String method = jparams.get("method").getTextValue();
//						if(method != null && method.equals("flash")){
//							//--- RESPONSE - Flash IE
//							response.setContentType("text/plain");
//							response.getWriter().println(ImageFileModel.toFlashFormat(imageFile));
//						}
//						else{
//							//--- RESPONSE - FireFox/HTML5
//							response.setContentType("text/json");
//							List<ImageFileModel> res = new ArrayList<ImageFileModel>();
//							res.add(new ImageFileModel(imageFile));
//							JacksonUtils.serialize(res,response.getWriter());
//							System.out.println("FileUploadServelt: HTML5 responded with " + res);
//						}
//					}
//				}
//			}
//			else{
//				Constants.LOGGER.info("FileUploadServlet.storeImageInUploads.NOT multipart.fileItem: " + upload.parseRequest(request));
//			}
//		} 
//		catch (Exception exp) {
//			exp.printStackTrace();
//		} 
//	}
	
//	@SuppressWarnings("unchecked")
//	protected void storeImageInGenres(HttpServletRequest request, HttpServletResponse response) throws IOException, UnauthorizedException {
//		ServletFileUpload upload = new ServletFileUpload(factory);
////		String userName = userName(request);
//		UserAccount userAccount = userAccount(request);
//		
//		List<FileItem> fileItemList;
//		try {
//			fileItemList = upload.parseRequest(request);
//			for(FileItem fileItem : fileItemList){
//				FileItemArgs args = new FileItemArgs();
//				args.fileItem = fileItem;
//				args.userAccountId = userAccount.getUserAccountId();
//				args.systemFolder = SystemFolder.GENRES;
//				
//				ImageFile imageFile = bean.storeImage(args);
////				System.out.println("Disk Resource is: " + diskResource);
//				
//				//--- RESPONSE
//				
//				JsonNode jparams = JacksonUtils.toNode(request.getParameter("params"));
//				String method = jparams.get("method").getTextValue();
//				if(method != null && method.equals("flash")){
//					//TODO implement response to flash
//					System.out.println("FileUploadServlet.storeImageInGenres NOT IMPLEMENTED FOR FLASH");
//				}
//				else{
//					List<ImageFileModel> jArr = new ArrayList<ImageFileModel>();
//					jArr.add(new ImageFileModel(imageFile));
//					
//					response.setContentType("text/json");
//					response.getWriter().println(jArr);
//				}
//			}
//		} 
//		catch (Exception exp) {
//			Constants.LOGGER.info(exp);
//			JacksonUtils.serialize(EntityModel.createErrorMessage("FileUpload Exception", exp.getMessage()), response.getWriter());
//		} 
//		
//	}

}
