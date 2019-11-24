package com.radowiecki.transformers.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {
	@Bean
	public DataSource dataSource() {
		SimpleDataSource dataSource = new SimpleDataSource();
		dataSource.setDatabaseName("transformers");
		dataSource.setUser("postgres");
		dataSource.setPassword("damian");
		dataSource.setPortNumber(5432);
		return dataSource;
	}

	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.format_sql", "true");
		hibernateProperties.put("hibernate.show_sql", "false");
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return hibernateProperties;
	}

	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder localSessionFacotryBuilder = new LocalSessionFactoryBuilder(this.dataSource());
		localSessionFacotryBuilder.addProperties(this.hibernateProperties());
		localSessionFacotryBuilder.scanPackages(new String[]{"com.radowiecki.transformers.model"});
		return localSessionFacotryBuilder.buildSessionFactory();
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(this.sessionFactory());
		return hibernateTransactionManager;
	}
}