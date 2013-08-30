package com.branchitup.service;

import org.hibernate.SessionFactory;

public class AdminService {
	protected SessionFactory sessionFactory;
	protected MailService mailService;
	
	public void deleteUser(long userAccountId){
		
	}
	
	public void sendIntroMail(String email){
		
		
	}
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public void setMailService(MailService mailService){
		this.mailService = mailService;	
	}
}
