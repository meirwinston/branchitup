package com.branchitup.controller;

import java.security.Principal;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.branchitup.exception.UnauthorizedException;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.ModelService;

public abstract class AbstractController {
	
	public static String getClientIP(HttpServletRequest request){
		String ip = request.getHeader("X-FORWARDED-FOR"); //returns ip address of client system
		if(ip == null){
			ip = request.getHeader("VIA"); //returns gateway;
		}
		if(ip == null){
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	public static void refreshUserAccount(HttpServletRequest request, ModelService bean) throws Exception{
		Principal principal = request.getUserPrincipal();
		UserAccount user = bean.findUserAccountByEmail(principal.getName());
//		System.out.println("--->refreshUserAccount:3:" + user);
		if(user != null){
			request.getSession().setAttribute(UserAccount.class.getSimpleName(), user);
//			System.out.println("--->refreshUserAccount: put user account in session " + user);
		}
	}
	
	public static String userName(HttpServletRequest request) throws UnauthorizedException{
		Principal principal = request.getUserPrincipal();
//		request.getSession().setAttribute("uri", request.getRequestURI());
		if(principal != null){
			return principal.getName();
		}
		else{
			throw new UnauthorizedException("User is not logged in");
		}
	}
	
	public static UserAccount userAccount(HttpServletRequest request) throws UnauthorizedException{
		UserAccount userAccount = (UserAccount)request.getSession().getAttribute(UserAccount.class.getSimpleName());
//		System.out.println("---->AbstractController.userAccount "+ userAccount);
		if(userAccount != null){
			return userAccount;
		}
		else{
			throw new UnauthorizedException("You are not logged in",request.getRequestURI());
		}
	}
	
	public static UserDetails userDetails(HttpServletRequest request) throws UnauthorizedException{
		UserDetails userDetails = null;
		if(request.getUserPrincipal() != null){
			userDetails = (UserDetails)((Authentication)request.getUserPrincipal()).getPrincipal();
		}
		else{
			throw new UnauthorizedException("You are not logged in",request.getRequestURI());
		}
		return userDetails;
	}
	
	public boolean isLoggedIn(HttpServletRequest request){
		return request.getUserPrincipal() != null;
//		return request.getSession().getAttribute(UserAccount.class.getSimpleName()) != null;
	}
	
	protected static String extractUrl(HttpServletRequest request){
		StringBuffer buf = request.getRequestURL();
		@SuppressWarnings("unchecked")
		Set<Entry<String,String[]>> paramSet = request.getParameterMap().entrySet();
		if(paramSet != null && paramSet.size() > 0){
			buf.append('?');
			for(Entry<String,String[]> e : paramSet){
				buf.append(e.getKey());
				if(e.getValue().length > 0){
					buf.append('=');				
					buf.append(e.getValue()[0]);
				}
				buf.append('&');
			}
		}
		return buf.toString();
	}
}
