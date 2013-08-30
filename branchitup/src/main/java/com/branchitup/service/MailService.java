package com.branchitup.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.PublishedBook;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.system.Constants;
import com.branchitup.system.Utils;
import com.branchitup.transfer.arguments.NewUserWallRecordArgs;
import com.branchitup.transfer.arguments.SubmitBookCommentArgs;

public class MailService {
	private Logger logger = Logger.getLogger(MailService.class);
	
	private JavaMailSender mailSender;
	
	@Autowired 
	private ServletContext servletContext;
	
	@Autowired
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;
	
//	private SessionFactory sessionFactory;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = (JavaMailSender)mailSender;
	}
	
//	public void setSessionFactory(SessionFactory sessionFactory)
//	{
//		System.out.println("---->MailService.setSessionFactory " + sessionFactory);
//		this.sessionFactory = sessionFactory;
//	}
//	
//	public SessionFactory getSessionFactory(){
//		return this.sessionFactory;
//	}
// 
	public void sendMail(String from, String to, String subject, String msg) {
		SimpleMailMessage message = new SimpleMailMessage();
//		System.out.println("---->sendMail " + from);
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);	
	}
	
	public void sendAsyncMail(final String from, final String to, final String subject, final String msg) {
		this.simpleAsyncTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendMail(from,to,subject,msg);
			}
		});
	}
	
	public void sendSystemMail(final String to, final String subject, final String msg) {
		this.simpleAsyncTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendMail(Constants.SYSTEM_EMAIL_USERNAME,to,subject,msg);
			}
		});
	}
	
//	public void sendSystemHtmlMail(String to, String subject, String msg) {
//		int attempts = 0;
//		System.out.println("-----> BEFORE attempts: " + attempts);
//		while(attempts < 3){
//			try {
//				sendHTMLMail(Constants.SYSTEM_EMAIL_USERNAME,to,subject,msg);
//				//logger.log(Priority.INFO, "email was sent to " + to);
//				attempts = 3;
//				System.out.println("-----> attempts: " + attempts);
//			} 
//			catch (MessagingException e) {
//				logger.error(e.getMessage(), e);
//			}
//			finally{
//				attempts++;				
//			}
//		}
//			
//	}
	
	public void sendSystemHtmlMail(final String to, final String subject, final String msg) {
		this.simpleAsyncTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					sendHTMLMail(Constants.SYSTEM_EMAIL_USERNAME,to,subject,msg);
//					sendHTMLMail(Constants.SYSTEM_USERNAME,to,subject,msg);
				} 
				catch (MessagingException e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}
	
	public void sendWallMessageNotification(NewUserWallRecordArgs args){
		UserAccount userAccount = StatelessDAO.findUserAccount(args.session, args.senderAccountId);
		String toEmail = StatelessDAO .selectUserEmail(args.session, args.userAccountId);//
		if(userAccount != null && toEmail != null){
			String fullName = userAccount.getFirstName() + " " + userAccount.getLastName();
			StringBuilder body = new StringBuilder();
			body.append("<a href='");
			body.append(Utils.getProperty("branchitup.globalUrl"));
			body.append("/userprofile?userAccountId=");
			body.append(args.userAccountId);
			body.append("'>");
			body.append(fullName);
			body.append("</a> left you a message on your wall<br/><br/>");
			body.append(args.message);
			
			sendSystemHtmlMail(toEmail,fullName + " left you a message on your wall",body.toString());
		}
		else{
			logger.error("MailService.sendWallMessageNotification userAccount or toEmail is null. cannot send mail!");
		}
	}
	
	public void sendBookCommentNotification(SubmitBookCommentArgs args){
		PublishedBook book = StatelessDAO.findPublishedBook(args.session, args.bookId);
		UserAccount userAccount = StatelessDAO.findUserAccount(args.session, args.userAccountId);
		if(book != null && userAccount != null){
			String fullName = userAccount.getFirstName() + " " + userAccount.getLastName();
			StringBuilder body = new StringBuilder();
			body.append("<a href='");
			body.append(Utils.getProperty("branchitup.globalUrl"));
			body.append("/userprofile?userAccountId=");
			body.append(args.userAccountId);
			body.append("'>");
			body.append(fullName);
			body.append("</a> commented on your book <a href='");
			body.append(Utils.getProperty("branchitup.globalUrl"));
			body.append("/publishedbook?bookId=");
			body.append(args.bookId);
			body.append("'>");
			body.append(book.getTitle());
			body.append("</a>");
			
			body.append("<br/><br/>");
			body.append(args.comment);
			sendSystemHtmlMail(book.getPublisherAccount().getEmail(),fullName + " commented on your book '" + book.getTitle() + "'",body.toString());
		}
		else{
			logger.error("MailService.sendBookCommentNotification book or userAccount is null. cannot send mail!");
		}
	}
	
	private void sendEmailByTemplate(String filePath, String to, String subject, String fullName) {
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			File file = new File(filePath);
			if(file.exists()){
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);
				StringBuilder sb = new StringBuilder();
				
				String s = reader.readLine();
				while(s != null){
					sb.append(s);
					s = reader.readLine();
				}
				s = sb.toString().replace("${NAME}", fullName);
				sendSystemHtmlMail(to,subject,s);
				logger.info("MailService.sendJoinUsWriterEmail to " + to + " Success!");
			}
			else{
				logger.error("File Not Found: " + file.getAbsolutePath());
			}
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	public void sendJoinUsWriterEmail(final String to, final String fullName) {
		//servletContext.getRealPath("/resources/email-templates/joinus-writer.html")
		this.simpleAsyncTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailByTemplate("src/main/webapp/resources/email-templates/joinus-writer.html", to,"New Network for Writers", fullName);
			}
		});
	}
	
	public void sendWelcomeEmail(final String to, final String fullName) {
		//root is - '/apps/apache-tomcat-7.0.29/branchitup/branchitup/'
		this.simpleAsyncTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailByTemplate(servletContext.getRealPath("/resources/email-templates/welcome-new-account.html"), to,"Your account at branchiup", fullName);
			}
		});
	}
	
	public synchronized void sendHTMLMail(final String from,final String to,final String subject,final String msg) 
	throws MessagingException {
		MimeMessage mime = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime,true,"UTF-8");
		
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(msg,true);
//		helper.setBcc("meirwinston@yahoo.com");
		
		mailSender.send(mime);
		
	}
	
	public void sendPasswordToUser(String email, String password){
		this.sendSystemMail(email, "Branch It Up Password", "Your password is " + password);
	}
	
	/*
	 * Title of e-mail:  BanchItUp update: NAME OF THE WORK THAT WAS BRANCHED has been branched
Inside the e-mail:
USERNAME has branched up your work, NAME OF WORK!  Click Here to view the new publication.  

	 */
//	public void sendBranchNotification(String to, String parentBookName, String brancherFullName, Long newBookId){
//		String subject = "BanchItUp update: '" + parentBookName + "' has been branched";
//		String content = (brancherFullName + " has branched up your work, " + parentBookName + "! Click <a href='" + Utils.getProperty("branchitup.globalUrl") + "/publishedbook?bookId=" + newBookId + "'>Here</a> to view the new publication.");
//		
//		sendSystemHtmlMail(to,subject,content);
//	}
	
	/*
	 * PublishedBook parentBook = newBook.getParentBook();
				
				System.out.println("The Book ID is :: " + newBook.getBookId() + 
						", parent Book: " + newBook.getParentBook() + ", " + newBook.getParentId());
				if(parentBook != null){
					mailService.sendBranchNotification(parentBook.getPublisherAccount().getEmail(),
							parentBook.getTitle(), 
							newBook.getPublisherAccount().getFirstName() + " " + newBook.getPublisherAccount().getLastName(),
							newBook.getBookId());
				}
	 */
	public void sendBranchNotification(PublishedBook newBook){
		PublishedBook parentBook = newBook.getParentBook();
		while(parentBook != null){
			StringBuilder subject = new StringBuilder();
			StringBuilder body = new StringBuilder();
			
			subject.append("Your book '");
			subject.append(parentBook.getTitle());
			subject.append("' was branched!");
			
			body.append("Your book '");
			body.append(parentBook.getTitle());
			body.append("' was branched by <a href='http://www.branchitup.com/userprofile?userAccountId=");
			body.append(newBook.getPublisherAccount().getUserAccountId());
			body.append("'>");
			body.append(newBook.getPublisherAccount().getFirstName());
			body.append(" ");
			body.append(newBook.getPublisherAccount().getLastName());
			body.append("</a>. The new version is: <a href='http://www.branchitup.com/publishedbook?bookId=");
			body.append(newBook.getBookId());
			body.append("'>");
			body.append(newBook.getTitle());
			body.append(" (");
			body.append(newBook.getVersion());
			body.append(")</a> ");
			if(!StringUtils.isEmpty(newBook.getPublisherComment())){
				body.append("<br/><br/>Publisher Comments: ");
				body.append(newBook.getPublisherComment());
			}
//			System.out.println("----SENDING MAIL to " + parentBook.getPublisherAccount().getEmail());
			sendSystemHtmlMail(parentBook.getPublisherAccount().getEmail(),subject.toString(),body.toString());
			
			
			parentBook = parentBook.getParentBook();
		}
	}
	
	public void sendInviteFriend(String to, String brancherFullName, String message){
		String subject = (brancherFullName + " invites you to join Branch It Up.");
		String content = (message + "<br><br>BranchItUp a collaborative web application for writers<br>To sign up for a free account click <a href='http://www.branchitup.com/signup'>here</a> or go to http://www.branchitup.com/signup");
		
		sendSystemHtmlMail(to,subject,content);
	}
}
