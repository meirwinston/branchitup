package com.branchitup.service;

import javax.sql.DataSource;

public class Flyway {
	protected DataSource dataSource;
	
	public void migrate(){
		System.out.println("Flyway.migrate");
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
