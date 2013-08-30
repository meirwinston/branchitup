package com.branchitup.controller.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.branchitup.exception.PersistenceException;
import com.branchitup.persistence.AttachmentFileType;
import com.branchitup.persistence.entities.Attachment;
import com.branchitup.persistence.entities.AudioFile;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.ModelService;
import com.branchitup.system.Constants;
import com.branchitup.system.FileUtilities;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.IncrementDownloadCountArgs;

@Controller
public class AttachmentService extends ServiceController{
	@Autowired
	private ModelService bean;
	
	@RequestMapping(value = "/attachment_service", method = {RequestMethod.POST})
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
	throws IOException, PersistenceException {
		try {
//			System.out.println("AttachmentServlet.doPost:::"+ request.getParameter("generateNew"));
			String audio = request.getParameter("audio");
			if(audio != null){
				streamAudioFileFromDisk(request,response);
			}
			else{
				streamFileFromDisk(request,response);
			}
			
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	protected void streamFileFromDisk(HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		String idParam = request.getParameter("id");
		
		Attachment attachment;
		if(idParam != null){
			long attachmentId = Long.valueOf(idParam);
			attachment = bean.findAttachment(attachmentId);
//			System.out.println("----->AttachmentService.attachment*****: " + attachment.getPublishedBook());
			if(attachment != null){
				String path = Utils.getProperty("branchitup.rootDir") + "/" + attachment.getDirPath() + "/" + attachment.getFileName();
//				
				if(StringUtils.isEmpty(attachment.getPublishedBook().getTitle())){
					response.setHeader ("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
				}
				else{
					response.setHeader ("Content-Disposition", "attachment; filename=\"" + 
							attachment.getPublishedBook().getTitle() + "." + 
							attachment.getFileType().fileExtension + "\"");
					if(attachment.getOwner() != null){
						response.setHeader ("Content-Disposition", "attachment; filename=\"" + 
								attachment.getPublishedBook().getTitle() +
								"_" + 
								(attachment.getOwner().getFirstName() == null ? "" : attachment.getOwner().getFirstName()) +
								(attachment.getOwner().getLastName() == null ? "" : attachment.getOwner().getLastName()) +
								"." + attachment.getFileType().fileExtension + "\"");
					}
					
				}
				//octet/stream
				if(request.getParameter("octetstream") == null){
					response.setContentType(attachment.getFileType().mimeType);
				}
				else{
					response.setContentType(AttachmentFileType.STREAM.mimeType);
				}
				
//				System.out.println("AttachmentServlet:::READING FILE FROM STREAM: " + path);
				response.getOutputStream().write(FileUtilities.readFile(path));
				
				IncrementDownloadCountArgs args = new IncrementDownloadCountArgs();
//				args.userAccountId = account.getUserAccountId();
				args.bookId = attachment.getPublishedBookId();
				args.ipAddress = getClientIP(request);
				
				try {
					UserAccount account = userAccount(request);
					args.userAccountId = account.getUserAccountId();
				} catch (Exception exp) {
					//anonymous download
				}
				bean.incrementUserDownloadsCount(args);
			}
			else{
				Constants.LOGGER.warn("AttachmentServlet: Attachment is NULL, ignoring response, id:" + idParam);
			}
		}
	}
	
	
	protected void streamAudioFileFromDisk(HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		String idParam = request.getParameter("id");
		
		AudioFile audioFile;
		if(idParam != null){
			long audioFileId = Long.valueOf(idParam);
			audioFile = bean.findAudioFile(audioFileId);
//					System.out.println("----->AttachmentService.attachment*****: " + attachment.getPublishedBook());
			if(audioFile != null){
				String path = Utils.getProperty("branchitup.rootDir") + "/" + audioFile.getDirPath() + "/" + audioFile.getFileName();
//						
				if(StringUtils.isEmpty(audioFile.getPublishedBook().getTitle())){
					response.setHeader ("Content-Disposition", "attachment; filename=\"" + audioFile.getFileName() + "\"");
				}
				else{
					response.setHeader ("Content-Disposition", "attachment; filename=\"" + 
						audioFile.getPublishedBook().getTitle() + "." + AttachmentFileType.MP3.fileExtension + "\"");
					if(audioFile.getOwner() != null){
						response.setHeader ("Content-Disposition", "attachment; filename=\"" + 
								audioFile.getPublishedBook().getTitle() +
								"_" + 
								(audioFile.getOwner().getFirstName() == null ? "" : audioFile.getOwner().getFirstName()) +
								(audioFile.getOwner().getLastName() == null ? "" : audioFile.getOwner().getLastName()) +
								"." + AttachmentFileType.MP3.fileExtension + "\"");
					}
					
				}
				//octet/stream
				if(request.getParameter("octetstream") == null){
					response.setContentType(AttachmentFileType.MP3.mimeType);
				}
				else{
					response.setContentType(AttachmentFileType.STREAM.mimeType);
				}
				
//				System.out.println("AttachmentServlet:::READING FILE FROM STREAM: " + path);
				response.getOutputStream().write(FileUtilities.readFile(path));
//				IncrementDownloadCountArgs args = new IncrementDownloadCountArgs();
//				//args.userAccountId = account.getUserAccountId();
//				args.bookId = audioFile.getPublishedBookId();
//				args.ipAddress = getClientIP(request);
//				
//				try {
//					UserAccount account = userAccount(request);
//					args.userAccountId = account.getUserAccountId();
//				} catch (Exception exp) {
//					//anonymous download
//				}
//				bean.incrementUserDownloadsCount(args);
			}
			else{
				Constants.LOGGER.warn("AttachmentServlet: Attachment is NULL, ignoring response, id:" + idParam);
			}
		}
	}
	
//	protected void generatePDF(HttpServletRequest request,HttpServletResponse response)
//	throws IOException, UnauthorizedException{
//		String bookIdParam = request.getParameter("bookId");
//		if(bookIdParam != null){
//			long bookId = Long.valueOf(bookIdParam);
//			FileItem fileItem = bean.generatePDF(bookId,userName(request));
//			if(fileItem != null){
//				response.setHeader ("Content-Disposition", "attachment; filename=\"" + fileItem.getName() + "\"");
//				response.setContentType(Attachment.FileType.PDF.mimeType);
//				response.getOutputStream().write(fileItem.get());
//			}
//			else{
//				Constants.LOGGER.warn("ImageServlet: ByteArrayResource is NULL, ignoring response, id:" + bookId);
//			}
//		}
//	}
}
