package com.branchitup.security;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public class SecurityContextLogoutHandler extends org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler{
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,Authentication authentication){
//		System.out.println("--------->logging out " + request.getSession().getAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL));
//		System.out.println("--------->logging out ");
//		request.getSession().removeAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL);
		
		//clear up all tmp files created by uploading files, that causing the tmp files to be deleted
		@SuppressWarnings("unchecked")
		Enumeration<String> atts = request.getSession().getAttributeNames();
		while(atts.hasMoreElements()){
			String attName = atts.nextElement();
			request.getSession().removeAttribute(attName);
			logger.trace("--------->logging out removing attribute " + attName);
		}
//		System.out.println("--------->logging out after " + request.getSession().getAttribute(Constants.SessionAttributeKey.MOST_RECENT_URL));
		super.logout(request, response, authentication);
	}
}
