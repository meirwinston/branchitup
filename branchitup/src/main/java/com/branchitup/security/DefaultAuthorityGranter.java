package com.branchitup.security;

import java.security.Principal;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;

@Deprecated
//not in use
public class DefaultAuthorityGranter implements AuthorityGranter{

	@Override
	public Set<String> grant(Principal p) {
		// TODO Auto-generated method stub
		System.out.println("----> DefaultAuthorityGranter " + p.getName());
//		com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource a;
		
		return null;
	}

}
