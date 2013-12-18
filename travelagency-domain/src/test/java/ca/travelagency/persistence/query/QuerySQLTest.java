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
package ca.travelagency.persistence.query;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;

@RunWith(MockitoJUnitRunner.class)
public class QuerySQLTest {
	@Mock
	private EntityManager entityManager;

	private Criteria criteria;

	@Before
	public void setUp() {
		criteria = Criteria.make(SystemUser.class);
	}

	@Test
	public void testQuerySQLCount() {
		// setup
		QuerySQL querySQL = QuerySQL.count(criteria, entityManager);
		// execute
		String actual = querySQL.sqlAsString();
		// validate
		Assert.assertEquals("SELECT COUNT(DISTINCT SystemUser) FROM ca.travelagency.identity.SystemUser SystemUser", actual);
	}

	@Test
	public void testQuerySQLFind() {
		// setup
		QuerySQL querySQL = QuerySQL.find(criteria, entityManager);
		// execute
		String actual = querySQL.sqlAsString();
		// validate
		Assert.assertEquals("SELECT DISTINCT SystemUser FROM ca.travelagency.identity.SystemUser SystemUser", actual);

	}
	@Test
	public void testQuerySQLGroupBy() {
		// setup
		criteria.setGroupBy(DaoEntity.PROPERTY_ID);
		QuerySQL querySQL = QuerySQL.groupBy(criteria, entityManager);
		// execute
		String actual = querySQL.sqlAsString();
		// validate
		Assert.assertEquals("SELECT DISTINCT SystemUser.id FROM ca.travelagency.identity.SystemUser SystemUser GROUP BY SystemUser.id", actual);
	}

}
