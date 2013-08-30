package com.branchitup.system;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;

public class ApplicationHandler implements ApplicationListener<ApplicationEvent> {
	@Override
	public void onApplicationEvent(ApplicationEvent e) {
		System.out.println("--^^-->ApplicationHandler.onApplicationEvent: " + e.getClass().getSimpleName());
		if (e instanceof AuthenticationSuccessEvent)
	    {
	        AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) e;
	        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
	        System.out.println("---->ApplicationHandler.onApplicationEvent:userDetails " + userDetails);
	    }
	}
}
