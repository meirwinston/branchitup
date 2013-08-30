package com.branchitup.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.branchitup.controller.AbstractController;
import com.branchitup.exception.PersistenceException;
import com.branchitup.exception.UnauthorizedException;

@Controller
public class Logout extends AbstractController{
	
	@RequestMapping(value = "/logout", method = {RequestMethod.GET})
	public String handleRequest(HttpServletRequest request,HttpServletResponse response) 
	throws ServletException, IOException, PersistenceException, UnauthorizedException {
		System.out.println("----->Logout " + userName(request));
//		request.logout();
		System.out.println("----->AFTER Logout " + userName(request));
		return "/";
	}
}
