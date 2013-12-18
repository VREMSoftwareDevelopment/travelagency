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
package ca.travelagency.identity;

import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.persistence.DatabaseTester;

public class SystemUserPersistenceTest extends DatabaseTester<SystemUser> {

	@Test
	public void testPersistence() throws Exception {
		// setup
		SystemUser expected = SystemUserHelper.makeSystemUserWithoutId();
		persist(expected);
		// execute
		SystemUser actual = find(expected.getClass(), expected.getId());
		// validate
		Assert.assertNotNull(expected.getId());
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testAddSystemUserRole() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUserWithoutId();
		SystemUserRole expected = SystemUserHelper.makeSystemUserRoleWithoutId(Role.ADMIN);
		// execute
		systemUser.addSystemUserRole(expected);
		persist(systemUser);
		// validate
		validateRelationship(expected);
		SystemUser result = find(SystemUser.class, systemUser.getId());
		// validate
		SortedSet<SystemUserRole> systemUserRoles = result.getSystemUserRoles();
		SystemUserRole actual = systemUserRoles.first();
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}
}