package com.branchitup.controller.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.branchitup.controller.AbstractController;
import com.branchitup.exception.InvalidInputException;
import com.branchitup.exception.UnauthorizedException;
import com.branchitup.exception.UserAccountException;
import com.branchitup.json.JacksonUtils;

public abstract class ServiceController extends AbstractController{
	protected Logger logger = Logger.getLogger(ServiceController.class);
	
	@ExceptionHandler(UnauthorizedException.class)
	protected void handleUnauthorizedException(UnauthorizedException exp, HttpServletRequest request, HttpServletResponse response){
		logger.error(exp);
		try {
			Map<String,String> map = new Hashtable<String,String>();
//			map.put("type", "redirect");
//			map.put("url", "/branchitup/login.html");
			map.put("javaClass", UnauthorizedException.class.getSimpleName());
			map.put("type", "exception");
			map.put("message", exp.getMessage());
			JacksonUtils.serialize(map, response.getWriter());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	@ExceptionHandler(InvalidInputException.class)
	protected void handleInvalidInputException(InvalidInputException exp, HttpServletRequest request, HttpServletResponse response){
		try {
			logger.error(exp);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("type", "exception");
			if(exp.messagesMap != null){
				map.put("message", exp.messagesMap);
			}
			else{
				map.put("message", exp.getMessage());
			}
			map.put("class", exp.getClass().getSimpleName());
			JacksonUtils.serialize(map, response.getWriter());
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@ExceptionHandler(Exception.class)
	protected void handleException(Exception exp, HttpServletRequest request, HttpServletResponse response){
		try {
			logger.error(exp.getMessage(), exp); //full trace
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("type", "exception");
			map.put("message", exp.getMessage());
			map.put("class", exp.getClass().getSimpleName());
			JacksonUtils.serialize(map, response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ExceptionHandler(UserAccountException.class)
	protected void handleUserAccountException(UserAccountException exp, HttpServletRequest request, HttpServletResponse response){
		logger.error(exp);
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("type", "exception");
			if(exp.messagesMap != null){
				map.put("message", exp.messagesMap);
			}
			else{
				map.put("message", exp.getMessage());
			}
			map.put("class", exp.getClass().getSimpleName());
			JacksonUtils.serialize(map, response.getWriter());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
