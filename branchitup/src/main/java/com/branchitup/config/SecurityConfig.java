package com.branchitup.config;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;//^^p
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.AntPathRequestMatcher;
import com.branchitup.security.AuthenticationHandler;
import com.branchitup.security.SecurityContextLogoutHandler;
import org.springframework.security.authentication.dao.ReflectionSaltSource;

/**
 * No. MD5 is not encryption (though it may be used as part of some encryption algorithms), it is a one way hash function. Much of the original data is actually "lost" as part of the transformation.
 * Think about this: An MD5 is always 128 bits long. That means that there are 2^128 possible MD5 hashes. That is a reasonably large number, and yet it is most definitely finite. And yet, there are an infinite number of possible inputs to a given hash function (and most of them contain more than 128 bits, or a measly 16 bytes). So there are actually an infinite number of possibilities for data that would hash to the same value. The thing that makes hashes interesting is that it is incredibly difficult to find two pieces of data that hash to the same value, and the chances of it happening by accident are almost 0.
 * A simple example for a (very insecure) hash function (and this illustrates the general idea of it being one-way) would be to take all of the bits of a piece of data, and treat it as a large number. Next, perform integer division using some large (probably prime) number n and take the remainder (see: Modulus). You will be left with some number between 0 and n. If you were to perform the same calculation again (any time, on any computer, anywhere), using the exact same string, it will come up with the same value. And yet, there is no way to find out what the original value was, since there are an infinite number of numbers that have that exact remainder, when divided by n.
 * That said, MD5 has been found to have some weaknesses, such that with some complex mathematics, it may be possible to find a collision without trying out 2128 possible input strings. And the fact that most passwords are short, and people often use common values (like "password" or "secret") means that in some cases, you can make a reasonably good guess at someone's password by Googling for the hash or using a Rainbow table. That is one reason why you should always "salt" hashed passwords, so that two identical values, when hashed, will not hash to the same value.
 * Once a piece of data has been run through a hash function, there is no going back.

 * @author meir
 *
 */
@Configuration
public class SecurityConfig implements ApplicationContextAware{
	protected Logger logger = Logger.getLogger(SecurityConfig.class);
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
    public SessionRegistry sessionRegistry(){
        SessionRegistryImpl sessRegistry=new SessionRegistryImpl();
        return sessRegistry;
    }
	
	@Bean
	public ConcurrentSessionControlStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry){
		ConcurrentSessionControlStrategy concSessRegistry=new ConcurrentSessionControlStrategy(sessionRegistry);
		return concSessRegistry;
	}
	 
	@Bean
	public SimpleUrlAuthenticationSuccessHandler postSuccessAuthHandler(){
		AuthenticationHandler authenticationHandler = new AuthenticationHandler();
		authenticationHandler.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
		return authenticationHandler;
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler postFailedAuthHandler(){
		SimpleUrlAuthenticationFailureHandler failureHandler=new SimpleUrlAuthenticationFailureHandler("/login?login_error=true");
		return failureHandler;
	}

	@Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
		JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
		jdbcDaoImpl.setAuthoritiesByUsernameQuery("select email,groupName from useraccounts where email = ?");
		jdbcDaoImpl.setUsersByUsernameQuery("select email,password,1 from useraccounts where email = ? and status = 'ACTIVE'");
		jdbcDaoImpl.setDataSource(dataSource);
        return jdbcDaoImpl;
    }
	
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder(); //^^p
	
	//^^p
	@Bean
	public Md5PasswordEncoder passwordEncoder(){
		return passwordEncoder;
	}
	
	@Bean
	public ReflectionSaltSource saltSource(){
		ReflectionSaltSource r = new ReflectionSaltSource();
		
		r.setUserPropertyToUse("username");
		return r;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userService){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		
		//^^p
		provider.setPasswordEncoder(passwordEncoder());
		provider.setSaltSource(saltSource());
		return provider;
	}
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider){
        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
        providers.add(authenticationProvider);
        
        ProviderManager authManager = new ProviderManager(providers);
        return authManager;
    }
	
	@Bean
	public LogoutFilter logoutFilter(){
		SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();	
		LogoutFilter filter = new LogoutFilter("/", new LogoutHandler[] {handler});
		return filter;
	}
	
	@Bean
	public ConcurrentSessionFilter concurrencyFilter(SessionRegistryImpl sessionRegistry){
		ConcurrentSessionFilter concurrencyFilter = new ConcurrentSessionFilter(sessionRegistry);
//		concurrencyFilter.setExpiredUrl("/auth/session-expired");
		return concurrencyFilter;
	}
	
	@Bean
    public SecurityContextPersistenceFilter securityContextFilter(HttpSessionSecurityContextRepository  securitycontextRepository){
        SecurityContextPersistenceFilter secFilter=new SecurityContextPersistenceFilter(securitycontextRepository);
        return secFilter;
    }    

    @Bean
    public SessionManagementFilter sessionMgmtFilter(HttpSessionSecurityContextRepository securitycontextRepository){
        SessionManagementFilter sessFilter=new SessionManagementFilter(securitycontextRepository);
        return sessFilter;
    }
    
    @Bean
    public HttpSessionSecurityContextRepository  securitycontextRepository(){
        HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
        return repo;
    }
	 
	@Bean(name="springSecurityFilterChain")    
	public FilterChainProxy springSecurityFilterChain(
	ConcurrentSessionFilter concurrentSessionFilter,
	SecurityContextPersistenceFilter securityContextPersistenceFilter,
	SessionManagementFilter sessionManagementFilter,
	UsernamePasswordAuthenticationFilter authenticationFilter,
	SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter,
	LogoutFilter logoutFilter){
//		System.out.println("---->in filterchain proxy......");
		
		SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
		securityContextHolderAwareRequestFilter,
		concurrentSessionFilter,
		securityContextPersistenceFilter,
		sessionManagementFilter,
		authenticationFilter,
		logoutFilter);
		FilterChainProxy filterChain = new FilterChainProxy(chain); 
		return filterChain;
	}
	
	
	@Bean
	public UsernamePasswordAuthenticationFilter formLoginFilter(
	AuthenticationManager authenticationManager,
	ConcurrentSessionControlStrategy sessionAuthenticationStrategy,
	SimpleUrlAuthenticationSuccessHandler postSuccessAuthHandler,
	SimpleUrlAuthenticationFailureHandler postFailedAuthHandler){
//		System.out.println("---->in formLoginFilter.....");
		UsernamePasswordAuthenticationFilter authFilter = new UsernamePasswordAuthenticationFilter();
		authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
		authFilter.setAuthenticationManager(authenticationManager);
		authFilter.setAuthenticationFailureHandler(postFailedAuthHandler);
		authFilter.setAuthenticationSuccessHandler(postSuccessAuthHandler);
		
		return authFilter;        
	}
	
	/**
	 * that bean will populate the httpServletRequest with Principal object,
	 * without this it will be null.
	 * @return
	 */
	@Bean
	public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter(){
		return new SecurityContextHolderAwareRequestFilter();
	}
	
//	//replaces the original implementation of on success.
//	@Bean
//	public AuthenticationSuccessHandler authenticationSuccessHandler(){
//		AuthenticationSuccessHandler handler = new AuthenticationSuccessHandler(){
//
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
//			throws IOException, ServletException {
//				System.out.println("---->AuthenticationSuccessHandler.onAuthenticationSuccess: " + authentication + ", " + request.getUserPrincipal());
//			}
//			
//		};
//		return handler;
//	}
	
	
//	 
//	 @Bean
//	 public LoginUrlAuthenticationEntryPoint authenticationEntryPoint(){
//		 LoginUrlAuthenticationEntryPoint authEntryPoint=new LoginUrlAuthenticationEntryPoint("/login");
//		 return authEntryPoint;
//	 }

	
	//--
	
	
//	public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
//		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
//		filter.setFilterProcessesUrl("login/j_spring_security_check");
////		filter.setAuthenticationManager(authenticationManager);
////		filter.setAuthenticationFailureHandler(failureHandler)
////		filter.setAuthenticationSuccessHandler(successHandler)
//		AuthenticationSuccessHandler s = new AuthenticationSuccessHandler() {
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication auth) 
//			throws IOException, ServletException {
//				
//			}
//		};
//		return filter;
//	}
	
	/*
	<beans:property name="authenticationManager" ref="authenticationManager"/>
    <beans:property name="authenticationFailureHandler">
        <beans:bean class="org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler">
            <beans:property name="defaultFailureUrl" value="/login?login_error=t"/>
        </beans:bean>
    </beans:property>
    <!-- <beans:property name="authenticationSuccessHandler">
        <beans:bean class="LocaleSettingAuthenticationSuccessHandler" />
    </beans:property> -->
 */
//--------------------------
	
	
//--------------------------
//	@Bean
//	public FilterChainProxy springSecurityFilterChain() throws Exception {
//	    // AuthenticationEntryPoint
//	    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
//	    entryPoint.setRealmName("AppName Realm");
//	    // accessDecisionManager
//	    List<AccessDecisionVoter> voters = Arrays.<AccessDecisionVoter>asList(new RoleVoter(), new WebExpressionVoter());
//	    AccessDecisionManager accessDecisionManager = new AffirmativeBased(voters);
//	    // SecurityExpressionHandler
//	    SecurityExpressionHandler<FilterInvocation> securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//	    // AuthenticationUserDetailsService
//	    UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService = new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(authUserDetailService);
//	    authenticationUserDetailsService.afterPropertiesSet();
//	    // PreAuthenticatedAuthenticationProvider
//	    PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
//	    preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);
//	    preAuthenticatedAuthenticationProvider.afterPropertiesSet();
//	    // AuthenticationManager
//	    List<AuthenticationProvider> providers = Arrays.<AuthenticationProvider>asList(preAuthenticatedAuthenticationProvider);
//	    AuthenticationManager authenticationManager = new ProviderManager(providers);
//	    // HttpSessionSecurityContextRepository
//	    HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
//	    // SessionRegistry
//	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
//	    // ConcurrentSessionControlStrategy
//	    ConcurrentSessionControlStrategy concurrentSessionControlStrategy = new ConcurrentSessionControlStrategy(sessionRegistry);
//
//	    // ConcurrentSessionFilter
//	    ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry);
//	    concurrentSessionFilter.afterPropertiesSet();
//	    // SecurityContextPersistenceFilter
//	    SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter(httpSessionSecurityContextRepository);
//	    // X509AuthenticationFilter
//	    X509AuthenticationFilter x509AuthenticationFilter = new X509AuthenticationFilter();
//	    x509AuthenticationFilter.setAuthenticationManager(authenticationManager);
//	    x509AuthenticationFilter.afterPropertiesSet();
//	    // RequestCacheAwareFilter
//	    RequestCacheAwareFilter requestCacheAwareFilter = new RequestCacheAwareFilter();
//	    // SecurityContextHolderAwareRequestFilter
//	    SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter = new SecurityContextHolderAwareRequestFilter();
//	    // SessionManagementFilter
//	    SessionManagementFilter sessionManagementFilter = new SessionManagementFilter(httpSessionSecurityContextRepository, concurrentSessionControlStrategy);
//	    // ExceptionTranslationFilter
//	    ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(entryPoint);
//	    exceptionTranslationFilter.setAccessDeniedHandler(new AccessDeniedHandlerImpl());
//	    exceptionTranslationFilter.afterPropertiesSet();
//	    // FilterSecurityInterceptor
//	    FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
//	    filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
//	    filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
//	    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
//	    map.put(new AntPathRequestMatcher("/**"), Arrays.<ConfigAttribute>asList(new SecurityConfig("isAuthenticated()")));
//	    ExpressionBasedFilterInvocationSecurityMetadataSource ms = new ExpressionBasedFilterInvocationSecurityMetadataSource(map, securityExpressionHandler);
//	    filterSecurityInterceptor.setSecurityMetadataSource(ms);
//	    filterSecurityInterceptor.afterPropertiesSet();
//	    // SecurityFilterChain
//	    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
//	            concurrentSessionFilter,
//	            securityContextPersistenceFilter,
//	            x509AuthenticationFilter,
//	            requestCacheAwareFilter,
//	            securityContextHolderAwareRequestFilter,
//	            sessionManagementFilter,
//	            exceptionTranslationFilter,
//	            filterSecurityInterceptor);
//	    return new FilterChainProxy(chain);
//	}

}
