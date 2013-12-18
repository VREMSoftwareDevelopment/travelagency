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

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SystemUserTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";

	private SystemUser fixture;

	@Before
	public void setUp() {
		fixture = SystemUserHelper.makeSystemUser();
	}

	@Test
	public void testDoesPasswordMatch() throws Exception {
		Assert.assertTrue(fixture.doesPasswordMatch(SystemUserHelper.PASSWORD));
		Assert.assertFalse(fixture.doesPasswordMatch("Any Other Password"));
	}

	@Test
	public void testEquals() throws Exception {
		SystemUser actual = SystemUserHelper.makeSystemUser();

		Assert.assertNotSame(fixture, actual);

		Assert.assertFalse(fixture.equals(actual));

		actual.setName(fixture.getName());
		Assert.assertTrue(fixture.equals(actual));
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setName(BIG_NAME_UPPER);
		fixture.setFirstName(BIG_NAME_UPPER);
		fixture.setLastName(BIG_NAME_UPPER);

		validateCamelCase(fixture);
	}

	private void validateCamelCase(SystemUser fixture) {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getFirstName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getLastName());
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setName(BIG_NAME_LOWER);
		fixture.setFirstName(BIG_NAME_LOWER);
		fixture.setLastName(BIG_NAME_LOWER);

		validateCamelCase(fixture);
	}

	@Test
	public void testOnlyUniqueRolesAreAdded() {
		// setup
		SystemUserRole systemUserRole1 = SystemUserHelper.makeSystemUserRole(Role.ADMIN);
		SystemUserRole systemUserRole2 = SystemUserHelper.makeSystemUserRole(Role.ADMIN);
		// execute
		fixture.addSystemUserRole(systemUserRole1);
		fixture.addSystemUserRole(systemUserRole2);
		// validate
		SortedSet<SystemUserRole> systemUserRoles = fixture.getSystemUserRoles();
		Assert.assertEquals(1, systemUserRoles.size());
		Assert.assertTrue(systemUserRoles.contains(systemUserRole1));
	}

	@Test
	public void testUniqueRolesAreAdded() {
		// setup
		SystemUserRole systemUserRole1 = SystemUserHelper.makeSystemUserRole(Role.ADMIN);
		SystemUserRole systemUserRole2 = SystemUserHelper.makeSystemUserRole(Role.AGENT);
		// execute
		fixture.addSystemUserRole(systemUserRole1);
		fixture.addSystemUserRole(systemUserRole2);
		// validate
		SortedSet<SystemUserRole> systemUserRoles = fixture.getSystemUserRoles();
		Assert.assertEquals(2, systemUserRoles.size());
		Assert.assertTrue(systemUserRoles.contains(systemUserRole1));
		Assert.assertTrue(systemUserRoles.contains(systemUserRole2));
	}

	@Test
	public void testDisplayEncodedPasswords() {
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		String [] passwords = new String[] {
			"password",
			"AgentPassword",
			"AdminPassword",
		};
		for (String password : passwords) {
			System.out.println(password + "='" + encryptor.encryptPassword(password) + "'");
		}
	}

	@Test
	public void testPrimaryPhone() {
		fixture.setPrimaryPhone("123-3456-6675-575");
		Assert.assertEquals("(123) 345-6667 ext:5575", fixture.getPrimaryPhone());
		Assert.assertEquals("12334566675575", fixture.getPrimaryPhoneRaw());
	}

	@Test
	public void testSecondaryPhone() {
		fixture.setSecondaryPhone("123-3456-6675-575");
		Assert.assertEquals("(123) 345-6667 ext:5575", fixture.getSecondaryPhone());
		Assert.assertEquals("12334566675575", fixture.getSecondaryPhoneRaw());
	}

}
