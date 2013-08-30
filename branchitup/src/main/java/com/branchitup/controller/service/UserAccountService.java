package com.branchitup.controller.service;

import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.branchitup.exception.InvalidInputException;
import com.branchitup.exception.UserAccountException;
import com.branchitup.json.JacksonUtils;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.MailService;
import com.branchitup.service.ModelService;
import com.branchitup.transfer.arguments.ContactUsArgs;
import com.branchitup.transfer.arguments.NewUserAccountArgs;
import com.branchitup.transfer.arguments.UserProfileInfoArgs;

@Controller
public class UserAccountService extends ServiceController{
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;
	
	
//	@RequestMapping(value = "/service/ResetPassword", method = {RequestMethod.POST})
//	public void resetPassword(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		request.setCharacterEncoding("UTF-8"); //necessary?
//		response.setContentType("text/html; charset=UTF-8");
//		UserAccount userAccount = userAccount(request);
//		
//		response.getWriter().print("{}");
//	}
	
	
	
	@RequestMapping(value = "/service/ContactUs", method = {RequestMethod.GET})
	public void contactUs(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Map<String,String> messageMap = new HashMap<String,String>();
		
		ContactUsArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), ContactUsArgs.class);
		args.ipAddress = getClientIP(request);
		
		if(!args.challenge.equals(request.getSession().getAttribute("challenge"))){
			messageMap.put("challenge", "Your input for the challenge does not match the challenge");
		}
		if(StringUtils.isEmpty(args.email)){
			messageMap.put("Invalid Email address","");
		}
		if(StringUtils.isEmpty(args.comments)){
			messageMap.put("Invalid comments","");
		}
		if(StringUtils.isEmpty(args.subject)){
			messageMap.put("Invalid subject","");
		}
		if(messageMap.size() > 0){
			throw new InvalidInputException(messageMap);
		}
		StringBuilder sb = new StringBuilder(args.comments);
		sb.append(System.getProperty("line.separator"));
		sb.append("IP: ");
		sb.append(args.ipAddress);
		sb.append(System.getProperty("line.separator"));
		sb.append("Email: ");
		sb.append(args.email);
		this.mailService.sendAsyncMail(args.email, "branchitup@gmail.com", args.subject, sb.toString());
		response.getWriter().print("{}");
	}
	
	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public SimpleAsyncTaskExecutor getSimpleAsyncTaskExecutor() {
		return simpleAsyncTaskExecutor;
	}

	public void setSimpleAsyncTaskExecutor(
			SimpleAsyncTaskExecutor simpleAsyncTaskExecutor) {
		this.simpleAsyncTaskExecutor = simpleAsyncTaskExecutor;
	}

	@RequestMapping(value = "/service/UpdateUserProfileInfo", method = {RequestMethod.GET})
	public void updateUserProfileInfo(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserProfileInfoArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UserProfileInfoArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		int totalUpdates = modelService.updateUserProfileInfo(args);
		if(totalUpdates > 0){
			UserAccount newUserAccount = modelService.findUserAccount(args.userAccountId);
			if(newUserAccount != null){
				request.getSession().setAttribute(UserAccount.class.getSimpleName(),newUserAccount);
			}
		}
		 
		response.getWriter().print("{totalUpdates: " + totalUpdates + "}");
	}
	
	@RequestMapping(value = "/service/UpdateProfilePassword", method = {RequestMethod.GET})
	public void updateProfilePassword(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserProfileInfoArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UserProfileInfoArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		args.userDetails = userDetails(request);
		if(args.newPassword != null){
			modelService.updateProfilePassword(args);
		}
		response.getWriter().print("{}");
	}
	
	@RequestMapping(value = "/service/UpdateProfileAboutMe", method = {RequestMethod.GET})
	public void updateProfileAboutMe(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserProfileInfoArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UserProfileInfoArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		int totalUpdates = 0;
		if(args.aboutMe != null){
			totalUpdates = modelService.updateProfileAboutMe(args);
		}
		response.getWriter().print("{totalUpdates: " + totalUpdates + "}");
	}
	
	@RequestMapping(value = "/service/UpdateProfileVisibility", method = {RequestMethod.GET})
	public void updateProfileVisibility(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserProfileInfoArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UserProfileInfoArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		int totalUpdates = 0;
		if(args.visibility != null){
			totalUpdates = modelService.updateProfileVisibility(args);
		}
		response.getWriter().print("{totalUpdates: " + totalUpdates + "}");
	}
	
	@RequestMapping(value = "/service/UpdateProfileEmailAddress", method = {RequestMethod.GET})
	public void updateProfileEmailAddress(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserProfileInfoArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), UserProfileInfoArgs.class);
		args.userAccountId = userAccount(request).getUserAccountId();
		int totalUpdates = 0;
		if(args.email != null){
			totalUpdates = modelService.updateEmailAddress(args);
			if(totalUpdates > 0){
				UserAccount newUserAccount = modelService.findUserAccount(args.userAccountId);
				if(newUserAccount != null){
					request.getSession().setAttribute(UserAccount.class.getSimpleName(),newUserAccount);
				}
			}
		}
		response.getWriter().print("{totalUpdates: " + totalUpdates + "}");
	}
	
	@RequestMapping(value = "/service/ClearUserProfileImage", method = {RequestMethod.GET})
	public void clearUserProfileImage(HttpServletRequest request, HttpServletResponse response)
	throws Exception{
		UserAccount userAccount = userAccount(request);
		int updatesCount = modelService.clearUserProfileImages(userAccount.getUserAccountId());
		if(updatesCount > 0){
//			userAccount.setProfileImageFileId(null);
//			userAccount.setProfileImageUrl(null);
//			System.out.println("---->>>IMAGE WAS CLEARED");
			
			refreshUserAccount(request,modelService);
		}
		
		response.getWriter().print("{updatesCount: " + updatesCount + "}");
	}
	
	@RequestMapping(value = {"/service/RestorePassword"}, method = {RequestMethod.GET})
	public void submitRecoverPassword(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Map<String,String> args = JacksonUtils.deserializeStringMap(request.getParameter("content"));
//		System.out.println("Before Sending Mail");
//		mailService.sendMail("mail@branchitup.com", "meirwinston@yahoo.com", "Branch It Up", "this is a <b>message</b>");
//		System.out.println("After sending mail");
//		UserAccount userAccount = userAccount(request);
//		mailService.sendPasswordToUser(userAccount.getEmail(), userAccount.getPassword());
		
		UserAccount userAccount = modelService.findUserAccountByEmail(args.get("email"));
		if(userAccount != null){
			String newPassword = modelService.resetUserAccountPassword(userAccount);
			
			mailService.sendPasswordToUser(userAccount.getEmail(), newPassword);
			logger.trace("password restored to " + newPassword);
			response.getWriter().print("{}");
		}
		else{
			Map<String,String> messageMap = new HashMap<String,String>();
			messageMap.put("body", "The Email address you submitted could not be found in the system, please make sure the email is correct and resubmit!");
			messageMap.put("javaClass", "UserAccountException");
			messageMap.put("title", "Missing Account");
			throw new UserAccountException(messageMap);
		}
		
	}
	//^^1
//	@RequestMapping(value = {"/service/ResetPassword"}, method = {RequestMethod.GET})
//	public void submitResetPassword(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		ResetPasswordArgs args = JacksonUtils.deserializeAs(request.getParameter("content"),ResetPasswordArgs.class);
//		bean.resetUserAccountPassword(args);
//		response.getWriter().print("{}");
//		System.out.println("------>ResetPassword " + args);
//	}
	
	private static boolean isValidEmailAddress(String aEmailAddress)
	{
		if (aEmailAddress == null)
			return false;
		boolean result = true;
		try
		{
//			InternetAddress emailAddr = new InternetAddress(aEmailAddress);
			new InternetAddress(aEmailAddress);
			if (!hasNameAndDomain(aEmailAddress))
			{
				result = false;
			}
		}
		catch (AddressException ex)
		{
			result = false;
		}
		return result;
	}

	protected static boolean hasNameAndDomain(String aEmailAddress)
	{
		String[] tokens = aEmailAddress.split("@");
		return (tokens.length == 2 && tokens[0].length() > 0 && tokens[1].length() > 0);
	}
	
//	protected String toFormattedName(){
//		String formattedUserName = this.get("firstName") + " " + this.get("lastName");
//		if(formattedUserName.length() > 40){
//			formattedUserName = formattedUserName.substring(0,39) + "...";
//		}
//		return formattedUserName;
//	}
	
	public static void validate(NewUserAccountArgs args, HttpServletRequest request) throws UserAccountException{
		Map<String,String> messageMap = new HashMap<String,String>();
		
		if(args.firstName != null){
			if(args.firstName.length() < 1 || args.firstName.length() > 50){
				messageMap.put("Invalid User Name Length", "First name must contain at least 1 character and must not exceed 50 characters");
			}
		}
		else{
			messageMap.put("blankUserName","user name cannot be blank");
		}
		if(args.password != null){
			if(args.passwordConfirm != null){
				if(!args.password.equals(args.passwordConfirm)){
					messageMap.put("Password Confirm","password field must match the password confirm");
				}
			}
			else{
				messageMap.put("passwordConfirm","password field must match the password confirm");
			}
		}
		else{
			messageMap.put("requiredPassword","password cannot be blank");
		}
		if(args.email != null){
			if(!isValidEmailAddress(args.email)){
				messageMap.put("email","invalid email address");
			}
		}
		else{
			messageMap.put("Email Is Required","email address is a required field");
		}
		if(args.gender != null){
			System.out.println("JSONUserAccount:validate fail:"+args.gender);
			float val = args.gender;
			if(val < 0 || val > 1){
				messageMap.put("gender","Gender input is incorrect");
			}
		}
//		else{
//			System.out.println("JSONUserAccount:validate NO gender:");
//			messageMap.put("Failed to supmit","failed to submit please reload and try again");
//		}
		
		if(!args.challenge.equals(request.getSession().getAttribute("challenge"))){
			messageMap.put("challenge", "Your input for the challenge does not match the challenge");
		}
		if(messageMap.size() > 0){
			throw new UserAccountException(messageMap);
		}
	}
	
	@RequestMapping(value = {"/service/SignupUser"}, method = {RequestMethod.GET})
	public void signupUser(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		NewUserAccountArgs args = JacksonUtils.deserializeAs(request.getParameter("content"), NewUserAccountArgs.class);
		validate(args,request);
		args.createdByIP = getClientIP(request);
//		args.userDetails = userDetails(request);
		
		UserAccount userAccount = modelService.newUserAccount(args);
//		System.out.println(args.challenge.equals(request.getSession().getAttribute("challenge")));
		response.getWriter().print("{}");
		if(userAccount != null){
			try {
				mailService.sendWelcomeEmail(userAccount.getEmail(), userAccount.getFirstName() + " " + userAccount.getLastName());
				
				mailService.sendSystemHtmlMail("meirwinston@yahoo.com", "BranchItUp New Account" , userAccount.getFirstName() + " " + 
				userAccount.getLastName() + ", IP: " + 
				userAccount.getCreatedByIP());
			} catch (Exception e) {
				logger.error("Failed to send mail to new account", e);
			}
		}
	}
}