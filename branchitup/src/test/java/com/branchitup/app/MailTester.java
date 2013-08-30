package com.branchitup.app;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.branchitup.config.ServiceConfig;
import com.branchitup.persistence.entities.Contact;
import com.branchitup.service.MailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MailTester {
	ServiceConfig config = new ServiceConfig();
	MailService mailService;
	
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public MailTester(){
		mailService = config.mailService();
	}
	
	public void sendInroMail(){
		mailService.sendSystemMail("meirwinston@yahoo.com", "Branch It Up", "My Message!");
	}
	
	public static SessionFactory createSessionFactory(){
		File file = new File("/home/meir/workspace/branchitup/branchitup/src/test/java/com/branchitup/app/hibernate.cfg.xml");
		Configuration configuration = new Configuration();
		configuration.configure(file);
		configuration.setProperty("hibernate.show_sql", "false");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/branchitup");
		configuration.setProperty("hibernate.connection.username", "root");
		configuration.setProperty("hibernate.connection.password", "root");
		configuration.setProperty("hibernate.connection.pool_size", "10");
		configuration.setProperty("show_sql", "true");
		configuration.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.current_session_context_class", "thread");
		
				
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);
		
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Session session = null;
		Transaction t = null;
		try {
			ApplicationContext ctx = new AnnotationConfigApplicationContext("com.branchitup.config");
			MailService mailService = ctx.getBean(MailService.class);
			
			SessionFactory sessionFactory = createSessionFactory();
			session = sessionFactory.openSession();
			t = session.beginTransaction();
//			Query query = session.getNamedQuery("Contact.selectBySentCountAndCategory");
			Query query = session.getNamedQuery("Contact.selectBySentCountAndCategoryWithFullName");
			
			query.setParameter("category", Contact.Category.WRITER);
			query.setParameter("sentCount", 0);
			
			List<Contact> list = query.list();
			
			for(Contact c : list){
				String s = c.getEmail();
				
				if(!StringUtils.isEmpty(s)){
					
					c.setEmail(s.trim());
					
					if(StringUtils.isEmpty(c.getFullName())){
						mailService.sendJoinUsWriterEmail(c.getEmail(), "Friend");
					}
					else{
						mailService.sendJoinUsWriterEmail(c.getEmail(), c.getFullName());
					}
//					System.out.println("----->applicationContext " + mailService);
					c.setSentCount(c.getSentCount()+1);
					c.setLastSent(new Date());
					
					System.out.println("sent email to " + c.getEmail());				
					Thread.sleep(2000);
				}
				
				
			}
			session.flush();
			t.commit();
			
			System.out.println("COMMITTED");
		} catch (Exception e) {
			e.printStackTrace();
			t.rollback();
		}
		finally{
			if(session != null){
				session.close();
			}
		}
		
		
		if(true){
			return;
		}
		//make sure ServiceConfig is not annotated with @EnableWebMvc
		
		
		
//		mailService.sendWelcomeEmail("meirwinston@yahoo.com", "Meir Winston");
		
//		mailService.sendJoinUsWriterEmail("mark@flingdesign.com", "Mark");
	}

	
	/*
	 * GenericApplicationContext ctx = new GenericApplicationContext();
 XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
 xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
 PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(ctx);
 propReader.loadBeanDefinitions(new ClassPathResource("otherBeans.properties"));
 ctx.refresh();

 MyBean myBean = (MyBean) ctx.getBean("myBean");
	 */
}
