package com.branchitup.controller.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.branchitup.exception.YBlobException;
import com.branchitup.persistence.entities.ImageFile;
import com.branchitup.service.ModelService;
import com.branchitup.system.Constants;
import com.branchitup.system.FileUtilities;
import com.branchitup.system.Utils;

@Controller
public class ImageService extends ServiceController{
	private DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
	
//	@Autowired
//	private HibernatePersistenceManager h;
	
	@Autowired
	private ModelService bean;
	
	@RequestMapping(value = "/imageservice", method = {RequestMethod.POST,RequestMethod.GET})
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) 
	throws ServletException, IOException {
		try {
			if (Constants.UploadTarget.SESSION.toString().equals(request.getParameter("target"))) {
				streamImageFromSession(request, response);
			}
			else if (Constants.UploadTarget.CAPTCHA.toString().equals(request.getParameter("target"))) {
				generateAndStreamCaptcha(request, response);
			}
			else {
//				streamImageFromDisk(request, response);
				sendImageRedirect(request, response);
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	protected void sendImageRedirect(HttpServletRequest request,HttpServletResponse response) 
	throws IOException {
		String idParam = request.getParameter("id");
//		 System.out.println("----->imageservice.streamImageFromDisk."+idParam);
		if (idParam != null && !idParam.isEmpty()) {
			long imageFileId = Long.valueOf(idParam);
			
			ImageFile imageFile = bean.findImageFile(imageFileId);
			if (imageFile != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(Utils.getProperty("branchitup.diskresources.imagesUrl"));
				sb.append("/");
				sb.append(imageFile.getFolderName());
				sb.append("/thumbnail_");
				sb.append(imageFile.getFileName());
//				sb.append(".thumbnail.");
//				sb.append(imageFile.getFormat());
				
//				System.out.println("-----------^^^>" + sb);
				response.setStatus(301);
//				response.setHeader( "Location", "https://www.google.com/images/srpr/logo3w.png" );
				response.setHeader( "Connection", "close" );
//				response.sendRedirect("http://localhost:8080/branchitup/images/GENRES/e332073c-3df5-41bb-8cba-ff8364e9a3a9.JPEG");
//				response.sendRedirect("images/GENRES/e332073c-3df5-41bb-8cba-ff8364e9a3a9.JPEG");
				response.sendRedirect(sb.toString());
			}
			else{
				
			}
		}
	}
	@Deprecated //unused
	protected void streamImageFromDisk(HttpServletRequest request,HttpServletResponse response) 
	throws IOException, YBlobException {
		String idParam = request.getParameter("id");
//		 System.out.println("----->imageservice.streamImageFromDisk."+idParam);
		if (idParam != null && !idParam.isEmpty()) {
			long imageFileId = Long.valueOf(idParam);
			String thumbnailParam = request.getParameter("thumbnail");
			ImageFile imageFile = null;
			if (thumbnailParam != null) {
				String key = "thunmnail." + imageFileId;
				FileItem fileItem = (FileItem) request.getSession().getAttribute(key);
//				request.getSession().removeAttribute(key);
				if (fileItem == null) {
					fileItem = diskFileItemFactory.createItem(key, "image",false, key);
					imageFile = bean.findImageFile(imageFileId);
					if (imageFile != null) {
						String path = Utils.getProperty("branchitup.rootDir")
							+ "/"
							+ Utils.getProperty("branchitup.diskresources.imagesPath")
							+ "/"
							+ imageFile.getFolderName()
							+ "/"
							+ imageFile.getFileName();
						FileUtilities.streamImage(path,fileItem.getOutputStream(), 200);
						request.getSession().setAttribute(key, fileItem);
						System.out.println("imageservice.streamImageFromDisk created NEW fileItem thumbnail: " + key + ", path: " + path);
					}
				} else {
					System.out.println("imageservice.streamImageFromDisk fetched thumbnail " + key + " from session. size:  " + fileItem.getSize());
				}
				if (fileItem != null) {
					response.getOutputStream().write(fileItem.get());
				}
			} else { // request for an image (not thumbnail)
				imageFile = bean.findImageFile(imageFileId);
				if (imageFile != null) {
					String path = Utils.getProperty("branchitup.rootDir")
						+ "/"
						+ Utils.getProperty("branchitup.diskresources.imagesPath")
						+ "/" + imageFile.getFolderName() + "/" + imageFile.getFileName();
					String widthParam = request.getParameter("width");
					if (widthParam != null) {
						FileUtilities.streamImage(path,response.getOutputStream(),Integer.valueOf(widthParam));
					} else {
						FileUtilities.writeFileToStream(path,response.getOutputStream());
					}
					// System.out.println("***imageservice::streamImageFromDisk.widthParam: "
					// + widthParam);
					response.setContentType("image/" + imageFile.getFormat());
				} else {
					org.apache.log4j.Logger.getLogger(this.getClass()).warn("imageservice: diskResource is NULL, ignoring response, id:" + idParam + ", thumbnail: " + thumbnailParam);
				}
			}
		}
	}

	protected static void writeFile(String path, OutputStream out) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = new FileInputStream(file);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
			out.close();
		}
	}

	protected void streamImageDisk(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String fileName = request.getParameter("file");
		String path = Utils.getProperty("branchitup.rootDir") + "/" + fileName;
		System.out.println("imageservice: " + path);
		File file = new File(path);
		if (file.exists()) {
			InputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}

			// out.flush();
			System.out.println("finished writing image back to front end");
			in.close();
			out.close();
		}
	}
	
	protected void generateAndStreamCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String challenge = UUID.randomUUID().toString().substring(0,8);
		request.getSession().setAttribute("challenge", challenge);
		BufferedImage image = FileUtilities.generateCaptcha(challenge);
		
		response.setContentType("image/gif");
//		response.setContentLength(image);
		ImageIO.write(image, "gif", response.getOutputStream());
	}

//	
	
	/**
	 * pops an image from the session and writes it to the output stream of the
	 * response
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void streamImageFromSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileItemId = request.getParameter("id");
		if (fileItemId != null) {
			FileItem fileItem = (FileItem)request.getSession().getAttribute(fileItemId);
			System.out.println("--U->ImageService.streamImageFromSession: " + fileItem + ", ");
			if (fileItem != null) {
				int length = IOUtils.copy(fileItem.getInputStream(), response.getOutputStream());
				response.setContentLength(length);
				response.setContentType("image/" + request.getParameter("fileType"));
				
				request.removeAttribute(fileItemId);
			} else {
				org.apache.log4j.Logger.getLogger(this.getClass()).warn("imageservice fileItem is null " + request.getParameterMap());
				System.out.println("imageservice.fileItemId is null, lets forward the request to the real image file::::"
					+ "images/"
					+ fileItemId
					+ "."
					+ request.getParameter("fileType"));
				response.sendRedirect("images/" + fileItemId + "." + request.getParameter("fileType"));
			}
		} else {
			System.out.println("imageservice.fileItemId is null, lets forward the request to the real image file");
			response.sendRedirect("/images/image_missing.png");
		}
	}
}
//	protected void streamImageFromSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String fileItemId = request.getParameter("id");
//		if (fileItemId != null) {
//			FileItem fileItem = (FileItem)request.getSession().getAttribute(fileItemId);
//			this.logger.debug("--U->ImageService.streamImageFromSession: " + fileItem + ", ");
//			if (fileItem != null) {
//				// BufferedImage bufferedImage =
//				// ImageIO.read(fileItem.getInputStream());
//
//				//if bytes is null, possible because tmp directory is not set, so it tries to grab the file
//				//from a tomcat-node instance
//				//try turning off one of the tomcat instances and try again to confirm that this is the problem
//				byte[] bytes = fileItem.get();
//				if (bytes != null && bytes.length > 0) {
//					// response.setContentType("image/" +
//					// Utils.parseExtention(fileItem.getName()));
//					response.setContentType("image/" + request.getParameter("fileType"));
//					// System.out.println("imageservice::::setContentType::" +
//					// "image/" + request.getParameter("fileType"));
//					response.setContentLength(bytes.length);
//					
//					response.getOutputStream().write(bytes);
//					request.removeAttribute(fileItemId);
//				}
//			} else {
//				org.apache.log4j.Logger.getLogger(this.getClass()).warn("imageservice fileItem is null " + request.getParameterMap());
//				System.out.println("imageservice.fileItemId is null, lets forward the request to the real image file::::"
//					+ "images/"
//					+ fileItemId
//					+ "."
//					+ request.getParameter("fileType"));
//				response.sendRedirect("images/" + fileItemId + "." + request.getParameter("fileType"));
//			}
//		} else {
//			System.out.println("imageservice.fileItemId is null, lets forward the request to the real image file");
//			response.sendRedirect("/images/image_missing.png");
//		}
//	}
//}
