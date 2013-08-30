package com.branchitup.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import com.branchitup.export.ExportHandler;

//@Service
public class ServiceLocator implements ApplicationContextAware,PersistenceUnitPostProcessor{
	private static ServiceLocator instance;
	protected ModelService bean;
	protected ExportHandler wkExporter;
	
	private ServiceLocator(){
//		System.out.println("----->ServiceLocator: " + bean);
		instance = this;
	}
	
	public static ServiceLocator getLastInstance(){
		return instance;
	}
	
	@Autowired
	public void setItemsBrowser(ModelService b){
//		System.out.println("----->setItemsBrowser " + b);
		this.bean = b ;
	}

	public ModelService getTransactionService() {
		return bean;
	}

	@Override
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		System.out.println("----->postProcessPersistenceUnitInfo");
		
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
	throws BeansException {
//		System.out.println("----->setApplicationContext");
	}

	public ExportHandler getWkExporter() {
		return wkExporter;
	}

//	@Autowired
	public void setWkExporter(ExportHandler wkExporter) {
//		System.out.println("------>ServiceLocator.setWkExporter");
		this.wkExporter = wkExporter;
	}

	
}
