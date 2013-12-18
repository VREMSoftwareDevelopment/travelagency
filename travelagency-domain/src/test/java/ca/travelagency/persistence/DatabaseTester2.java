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
package ca.travelagency.persistence;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:domainContext.xml"})
@TransactionConfiguration(defaultRollback=false)
@Transactional
public abstract class DatabaseTester2<T extends DaoEntity> {
	private static final Logger LOGGER = Logger.getLogger(DatabaseTester.class);

	private Deque<T> temporaryDaoEntitys;

	@Autowired
	private DaoSupport<T> daoSupport;

	@BeforeTransaction
	public final void setUpTestDataWithinTransaction() {
		temporaryDaoEntitys = new LinkedList<T>();
	}

	@AfterTransaction
	public final void tearDownWithinTransaction() {
		for (T daoEntity : temporaryDaoEntitys) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Remove object:" + daoEntity);
			}
			try {
				daoSupport.remove(daoEntity);
			} catch (Exception e) {
				LOGGER.error("Can NOT remove:"+daoEntity, e);
			}
		}
		temporaryDaoEntitys.clear();
	}

	protected void validateRelationship(final DaoEntity expected) {
		Assert.assertNotNull(expected.getId());
		DaoEntity actual = daoSupport.find(expected.getTrueClass(), expected.getId());
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	private void clear() {
		daoSupport.getEntityManager().flush();
		daoSupport.getEntityManager().clear();
	}

	protected void persist(T daoEntity) {
		daoSupport.persist(daoEntity);
		clear();
		temporaryDaoEntitys.addFirst(daoEntity);
	}

	protected void merge(T daoEntity) {
		daoSupport.merge(daoEntity);
		clear();
		temporaryDaoEntitys.addFirst(daoEntity);
	}

	protected void remove(T daoEntity) {
		daoSupport.remove(daoEntity);
		clear();
		temporaryDaoEntitys.remove(daoEntity);
	}

	protected T find(Class<?> clazz, Long id) {
		return daoSupport.find(clazz, id);
	}

	protected List<T> find(Class<?> clazz) {
		return daoSupport.find(clazz);
	}

	protected List<T> find(DaoCriteria daoCriteria) {
		return daoSupport.find(daoCriteria);
	}

	protected List<String> groupBy(DaoCriteria daoCriteria) {
		return daoSupport.groupBy(daoCriteria);
	}

	protected Long count(DaoCriteria daoCriteria) {
		return daoSupport.count(daoCriteria);
	}

	protected boolean duplicated(T DaoEntity) {
		return daoSupport.duplicated(DaoEntity);
	}
}