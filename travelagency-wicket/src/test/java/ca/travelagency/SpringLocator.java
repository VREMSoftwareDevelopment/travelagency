/**
 *    Copyright (C) 2010 - 2014 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ca.travelagency;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;

public enum SpringLocator {
	INSTANCE;

	private EntityManagerFactory entityManagerFactory;
	private JpaTransactionManager jpaTransactionManager;
	private DataSource dataSource;
	private ApplicationContext context;

	private SpringLocator() {
		context = new ClassPathXmlApplicationContext(new String[] {"classpath:domainContext.xml"});
		entityManagerFactory = (EntityManagerFactory) context.getBean("entityManagerFactory");
		jpaTransactionManager = (JpaTransactionManager) context.getBean("transactionManager");
		dataSource = (DataSource) context.getBean("dataSource");
	}

	public ApplicationContext getContext() {
		return context;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
	public JpaTransactionManager getJpaTransactionManager() {
		return jpaTransactionManager ;
	}
}