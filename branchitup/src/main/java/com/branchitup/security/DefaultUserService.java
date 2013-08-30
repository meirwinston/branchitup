package com.branchitup.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//com.yblob.security.DefaultUserService
@Deprecated
//not in use
public class DefaultUserService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("---->DefaultUserService.loadUserByUsername: " + userName);
//		org.springframework.web.filter.DelegatingFilterProxy
		return null;
	}

}
