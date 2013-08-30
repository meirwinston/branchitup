package com.branchitup.config;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import com.branchitup.service.AdminService;
import com.branchitup.service.MailService;
import com.branchitup.service.ModelService;
import com.branchitup.service.ModelServiceImpl;
import com.branchitup.service.ScheduledService;
import com.branchitup.service.ScheduledServiceImpl;
import com.branchitup.servlet.view.BaseViewResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.authentication.encoding.PasswordEncoder; //^^p
import org.springframework.security.authentication.dao.SaltSource; //^^p

/**
 * this class also must be boostrapped, 
 * e.g. <context:component-scan base-package="com.branchitup.config" /> in servlet-context.xml 
 * 
 * without bootstrapping, we may get errors as follows:
 * WARN : org.springframework.web.servlet.PageNotFound - No mapping found for HTTP request with URI [/branchitup/] in DispatcherServlet with name 'appServlet'
 * WARN : org.springframework.web.servlet.PageNotFound - No mapping found for HTTP request with URI [/branchitup/home] in DispatcherServlet with name 'appServlet'
 * 
 * @author Meir Winston
 */
@Configuration
@EnableWebMvc //important for addResourceHandlers
@ComponentScan(basePackages={"com.branchitup"}) //I believe it can replace: <context:component-scan base-package="com.branchitup" /> in servlet-context.xml
public class ServiceConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware{
//	@Inject DatabaseConfig dbConfig
	private ApplicationContext applicationContext;
	
	public ServiceConfig(){
//		System.out.println("----->ServiceConfig");
	}
	
//	private void setLanguagesMap(){
//		List<Language> list = new ArrayList<Language>();
//		list.add(new Language(null, "Any"));
//		list.add(new Language("ENG", "English"));
//		list.add(new Language("SPA", "Spanish"));
//		list.add(new Language("HEB", "Hebrew"));
//	}
//	static{
//		AnnotationConfigApplicationContext c = new AnnotationConfigApplicationContext();
//		c.register(ServiceConfig.class);
//	}

	/**
	 * Handles HTTP GET requests for /resources/** by efficiently serving up static 
	 * resources in the ${webappRoot}/resources directory
	 * <resources mapping="/resources/**" location="/resources/" />
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/**").addResourceLocations("/external/");
	}
	
	/**
	 * <beans:bean id="schedulerFactoryBean" class="org.springframework.scheduling.quartz.SchedulerFactoryBean"></beans:bean> 
	 * @return
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(){
		return new SchedulerFactoryBean();
	}
	
	@Bean(name="simpleAsyncTaskExecutor")
	public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor(){
		SimpleAsyncTaskExecutor b = new SimpleAsyncTaskExecutor();
		return b;
	}
	//SimpleAsyncTaskExecutor 
	//-------------MAIL
	@Bean(name="mailService")
	public MailService mailService(){
		MailService m = new MailService();
		m.setMailSender((MailSender)applicationContext.getBean("javaMailSender"));
//		m.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
		return m;
	}
	
	@Bean(name="javaMailSender")
	public JavaMailSender javaMailSender(){
		//using google apps
		JavaMailSenderImpl m = new JavaMailSenderImpl();
//		m.setHost("smtp.gmail.com"); //173.194.68.108
		m.setHost("173.194.68.108");
		m.setPort(587);
		m.setUsername("mail@branchitup.com");
		m.setPassword("lpko-jihu-gyft-drse");

		Properties javaMailProps = new Properties();
		javaMailProps.put("mail.smtp.auth", true);
		javaMailProps.put("mail.smtp.starttls.enable", true);
		m.setJavaMailProperties(javaMailProps);
		return m;
	}
	
	//--------------END OF MAIL
	
	@Bean(autowire=Autowire.BY_TYPE,name="adminService")
	public AdminService adminService(){
		AdminService b = new AdminService();
		b.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
//		b.setScheduledService((ScheduledService)applicationContext.getBean("scheduledService"));
		b.setMailService((MailService)applicationContext.getBean("mailService"));
		return b;
	}
	
	@Bean(autowire=Autowire.BY_TYPE,name="modelService")
	public ModelService modelService(){
		ModelServiceImpl b = new ModelServiceImpl();
		b.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
		b.setScheduledService((ScheduledService)applicationContext.getBean("scheduledService"));
		b.setMailService((MailService)applicationContext.getBean("mailService"));
		
		b.setPasswordEncoder((PasswordEncoder)applicationContext.getBean("passwordEncoder")); //^^p
		b.setSaltSource((SaltSource)applicationContext.getBean("saltSource")); //^^p
		return b;
	}
	
//	@Bean(autowire=Autowire.BY_TYPE,name="persistenceUnitManager")
//	public DefaultPersistenceUnitManager persistenceUnitManager(){
//		return new DefaultPersistenceUnitManager();
//	}
	
	@Bean(name="scheduledService")
	public ScheduledService scheduledService(){
		ScheduledServiceImpl b = new ScheduledServiceImpl();
		b.setSessionFactory((SessionFactory)applicationContext.getBean("sessionFactory"));
//		b.setModelService((ModelService)applicationContext.getBean("modelService"));
		return b;
	}
	
	/**
	 * <beans:bean id="beanNameViewResolver" class="org.springframework.web.servlet.view.BeanNameViewResolver"/> 
	 * @return
	 */
	@Bean(name="beanNameViewResolver")
	public BeanNameViewResolver beanNameViewResolver(){
		return new BeanNameViewResolver();
	}
	
	//no need - gets event for every serlvelt request
//	@Bean(name="applicationHandler")
//	public ApplicationHandler applicationHandler(){
//		return new ApplicationHandler();
//	}
	
	/**
	 * Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory
	 * 
	 * <beans:bean id="inernalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	 *	<beans:property name="prefix" value="/WEB-INF/views/" />
	 * </beans:bean>
	 * 
	 * @return
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver(){
		InternalResourceViewResolver i = new InternalResourceViewResolver();
		i.setPrefix("/WEB-INF/views/");
		return i;
	}
	
	/**
	 * <beans:bean id="branchitupViewResolver" class="com.branchitup.servlet.view.BaseViewResolver">
	 *  <beans:property name="beanNameViewResolver" ref="beanNameViewResolver" />
	 *  <beans:property name="inernalResourceViewResolver" ref="inernalResourceViewResolver" />
	 * </beans:bean>
	 * @param inernalResourceViewResolver
	 * @param beanNameViewResolver
	 * @return
	 */
	@Bean
	public BaseViewResolver baseViewResolver(InternalResourceViewResolver inernalResourceViewResolver, BeanNameViewResolver beanNameViewResolver){
		BaseViewResolver r = new BaseViewResolver();
		r.setInernalResourceViewResolver(inernalResourceViewResolver);
		r.setBeanNameViewResolver(beanNameViewResolver);
		
		return r;
	}
	
	@Bean
	public MappingJacksonJsonView mappingJacksonJsonView(){
		return new MappingJacksonJsonView();
	}
	
    public void setApplicationContext(ApplicationContext applicationContext) {
    	System.out.println("--->ServiceConfig.applicationContextAware");
        this.applicationContext = applicationContext;
    }
    
    @Bean
    public MockServletContext mockServletContext(){
    	return new MockServletContext();
    }
}
