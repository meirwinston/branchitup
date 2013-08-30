package com.branchitup;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.branchitup.config.DataConfig;
import com.branchitup.config.SecurityConfig;
import com.branchitup.config.ServiceConfig;
import com.branchitup.persistence.StatelessDAO;
import com.branchitup.persistence.entities.UserAccount;
import com.branchitup.service.ModelService;
import com.branchitup.transfer.arguments.ScrollUserAccountsArgs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
//		"/hd/git/branchitup/branchitup/target/branchitup-1.0.0-BUILD-SNAPSHOT/WEB-INF/spring/security-context.xml", 
//		"/hd/git/branchitup/branchitup/target/branchitup-1.0.0-BUILD-SNAPSHOT/WEB-INF/spring/root-context.xml",
//		"/hd/git/branchitup/branchitup/target/branchitup-1.0.0-BUILD-SNAPSHOT/WEB-INF/spring/appServlet/servlet-context.xml"
},classes={
	DataConfig.class,
	SecurityConfig.class,
	ServiceConfig.class
})
public class HibernateTest {
	@Autowired
	protected ModelService modelService;
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@Autowired
	protected Md5PasswordEncoder passwordEncoder;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		// one-time initialization code
		System.out.println("@BeforeClass - oneTimeSetUp");
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// one-time cleanup code
		System.out.println("@AfterClass - oneTimeTearDown");
	}

	@Before
	public void setUp() {
		System.out.println("@Before - setUp");
	}

	@After
	public void tearDown() {
		System.out.println("@After - tearDown");
	}

	@Test
	public void testA() {
		System.out.println("testA " + this.modelService);
	}
	
	@Test
	public void encryptPasswords(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			ScrollUserAccountsArgs args = new ScrollUserAccountsArgs(0, 1000);
			args.session = session;
			List<UserAccount> accounts = StatelessDAO.scrollUserAccount(args);
			for(UserAccount account : accounts){
				String newPass = passwordEncoder.encodePassword(account.getPassword(), account.getEmail());
				System.out.print("old pass: " + account.getPassword());
				account.setPassword(newPass);
				System.out.println(" set new password " + newPass + " for " + account.getFirstName() + " " + account.getLastName());
			}
			if(transaction != null){
				transaction.commit();
			}
		}
		catch(Exception exp){
			exp.printStackTrace();
			if(transaction != null){
				transaction.rollback();
			}
		}
		finally {
			session.close();
		}
	}
	
	
//	@Test
//	public void encryptPasswords(){
//		Session session = sessionFactory.openSession();
//		try {
//			ScrollUserAccountsArgs args = new ScrollUserAccountsArgs(0, 1000);
//			args.session = session;
//			List<UserAccount> accounts = StatelessDAO.scrollUserAccount(args);
//			for(UserAccount account : accounts){
//				String newPass = passwordEncoder.encodePassword(account.getPassword(), account.getEmail());
//				
//				System.out.println("set new password " + newPass + " for " + account.getFirstName() + " " + account.getLastName());
//			}
//		}
//		catch(Exception exp){
//			exp.printStackTrace();
//		}
//		finally {
//			session.close();
//		}
//	}
}