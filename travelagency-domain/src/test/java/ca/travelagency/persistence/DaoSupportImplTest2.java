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

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserRole;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

/** DaoSupportImpl tests using SystemUser */
public class DaoSupportImplTest2 extends DatabaseTester2<SystemUser> {
	@Test
	public void testFind() throws Exception {
		// setup
		SystemUser expected = makeSystemUser();
		persist(expected);
		// execute
		SystemUser actual = find(expected.getClass(), expected.getId());
		// validate
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testRemove() throws Exception {
		// setup
		SystemUser expected = makeSystemUser();
		persist(expected);
		// execute
		remove(expected);
		// validate
		SystemUser actual = find(expected.getClass(), expected.getId());
		Assert.assertNull(actual);
	}

	private SystemUser makeSystemUser() {
		return SystemUserHelper.makeSystemUserWithoutId();
	}

	private SystemUser makeSystemUser(String lastName) {
		SystemUser systemUser = SystemUserHelper.makeSystemUserWithoutId();
		systemUser.setLastName(lastName);
		return systemUser;
	}

	@Test
	public void testFindUsingCriteria() throws Exception {
		// setup
		List<SystemUser> expected = makeSystemUsers();
		Criteria criteria = makeCriteriaWithCondition();
		// execute
		List<SystemUser> actual = find(criteria);
		// validate
		Assert.assertEquals(4, actual.size());
		validateSystemUsers(expected, actual);
	}

	@Test
	public void testFindUsingCriteriaAndOffsetWithCount() throws Exception {
		// setup
		makeSystemUsers();
		int offset = 1;
		int count = 2;
		Criteria criteria = makeCriteriaWithCondition().setOffset(offset).setCount(count);
		// execute
		List<SystemUser> actual = find(criteria);
		// validate
		Assert.assertEquals(count, actual.size());
	}

	@Test
	public void testFindAll() throws Exception {
		// setup
		List<SystemUser> expected = makeSystemUsers();
		// execute
		List<SystemUser> actual = find(SystemUser.class);
		// validate
		Assert.assertTrue(actual.size() >= expected.size());
		validateSystemUsers(expected, actual);
	}

	private void validateSystemUsers(List<SystemUser> expected, List<SystemUser> actual) {
		for (SystemUser systemUser : expected) {
			Assert.assertTrue(actual.contains(systemUser));
		}
	}

	private Criteria makeCriteriaWithCondition() {
		Criteria criteria = Criteria.make(SystemUser.class)
			.addAndConditions(
				Lists.newArrayList(
					Condition.make(
						DaoEntity.PROPERTY_NAME, Operator.LIKE, SystemUserHelper.NAME)))
			.addOrderBy(OrderBy.make(DaoEntity.PROPERTY_NAME));
		return criteria;
	}

	@Test
	public void testCountWithCriteria() throws Exception {
		// setup
		makeSystemUsers();
		// execute
		long count = count(makeCriteriaWithCondition());
		// validate
		Assert.assertEquals(4, count);
	}

	@Test
	public void testDuplicateDoesNotExistsForTheSameDaoEntity() throws Exception {
		SystemUser systemUser1 = makeSystemUser();
		persist(systemUser1);
		Assert.assertFalse(duplicated(systemUser1));
	}

	@Test
	public void testDuplicateDoesExistsForTheDifferentDaoEntityWithTheSameName() throws Exception {
		// setup
		SystemUser systemUser1 = makeSystemUser();
		persist(systemUser1);
		SystemUser systemUser2 = makeSystemUser();
		systemUser2.setName(systemUser1.getName());
		// execute & validate
		Assert.assertTrue(duplicated(systemUser2));
	}

	@Test
	public void testDuplicateDoesExistsForTheNewDaoEntityWithTheSameName() throws Exception {
		// setup
		SystemUser systemUser1 = makeSystemUser();
		persist(systemUser1);
		SystemUser systemUser2 = makeSystemUser();
		persist(systemUser2);
		systemUser2.setName(systemUser1.getName());
		// execute & validate
		Assert.assertTrue(duplicated(systemUser2));
	}

	@Test
	public void testFindUsingRoles() throws Exception {
		// setup
		String property = SystemUser.Properties.systemUserRoles.name()+Condition.SEPARATOR+SystemUserRole.Properties.role;
		Condition condition = Condition.make(property, Operator.EQUAL, Role.ADMIN).setInnerJoin();
		Criteria criteria = Criteria.make(SystemUser.class).addAndCondition(condition);
		// execute
		List<SystemUser> actual = find(criteria);
		// validate
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals("Admin", actual.get(0).getName());
	}

	@Test
	public void testFindDistinctUsingOnlyLastName() throws Exception {
		// setup
		String lastName = WordUtils.capitalize(StringUtils.lowerCase("LAST NAME"));
		SystemUser systemUser1 = makeSystemUser(lastName);
		persist(systemUser1);
		SystemUser systemUser2 = makeSystemUser(lastName);
		persist(systemUser2);
		SystemUser systemUser3 = makeSystemUser(lastName);
		persist(systemUser3);
		SystemUser systemUser4 = makeSystemUser(lastName);
		persist(systemUser4);
		Criteria criteria = makeDistinctCriteria(lastName);
		// execute
		List<String> actual = groupBy(criteria);
		// validate
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(lastName, actual.get(0));
	}

	private Criteria makeDistinctCriteria(final String lastName) {
		Criteria criteria = Criteria.make(SystemUser.class)
			.addAndConditions(
				Lists.newArrayList(
					Condition.make(
							SystemUser.Properties.lastName.name(), Operator.EQUAL, lastName)))
			.addOrderBy(OrderBy.make(SystemUser.Properties.lastName.name()))
			.setGroupBy(SystemUser.Properties.lastName.name());
		return criteria;
	}

	private List<SystemUser> makeSystemUsers() {
		SystemUser systemUser1 = makeSystemUser();
		persist(systemUser1);
		SystemUser systemUser2 = makeSystemUser();
		persist(systemUser2);
		SystemUser systemUser3 = makeSystemUser();
		persist(systemUser3);
		SystemUser systemUser4 = makeSystemUser();
		persist(systemUser4);
		return Lists.newArrayList(systemUser1, systemUser2, systemUser3, systemUser4);
	}
}