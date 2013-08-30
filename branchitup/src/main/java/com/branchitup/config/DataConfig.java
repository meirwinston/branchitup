package com.branchitup.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import com.branchitup.service.Flyway;
import com.branchitup.system.Utils;

/**
 * 
 * @author meir
 *
 */
@Configuration
public class DataConfig {
	
	@Bean(name="dataSource")
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		
//		dataSource.setUsername("root");
//		dataSource.setPassword("root");
		dataSource.setUrl(Utils.getProperty("branchitup.jdbc.url"));
//		dataSource.setUrl("jdbc:mysql://192.168.0.115:3306/branchitup?useEncoding=true&characterEncoding=UTF-8");
		dataSource.setUsername(Utils.getProperty("branchitup.jdbc.username"));
		dataSource.setPassword(Utils.getProperty("branchitup.jdbc.password"));
		return dataSource;
	}
	
	@Bean(name="sessionFactory")
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource){
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan("com.branchitup.persistence.entities");
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//		hibernateProperties.put("hibernate.dialect", "com.branchitup.persistence.InfocubeMSSQLDialect");
		//
		hibernateProperties.put("hibernate.show_sql", false);
		bean.setHibernateProperties(hibernateProperties);
		return bean;
	}
	//<!property name="hibernate.dialect">it.infocube.draft.persistence.InfocubeMSSQLDialect</property>
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
		HibernateTransactionManager m = new HibernateTransactionManager();
		m.setSessionFactory(sessionFactory);
		return m;
	}
	
	@DependsOn(value="flyway")
	@Bean(name="persistenceUnitManager")
	public DefaultPersistenceUnitManager persistenceUnitManager(){
		return new DefaultPersistenceUnitManager();
	}
	
	@Bean(name="flyway",initMethod="migrate")
	public Flyway flyway(){
		Flyway bean =  new Flyway();
		bean.setDataSource(dataSource());
		return bean;
	}
}
