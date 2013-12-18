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
package ca.travelagency.authentication;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserRole;

public class AuthenticatedSessionTest extends BaseWicketTester {

	private static final String PASSWORD = "password";

	private AuthenticatedSession fixture;
	private SystemUser systemUser;

	@Before
	public void setUp() throws Exception {
		Request request = tester.getRequestCycle().getRequest();

		fixture = new AuthenticatedSession(request);

		systemUser = SystemUserHelper.makeSystemUserWithRole(Role.ADMIN);

		Mockito.when(systemUserService.authorize(systemUser.getName(), PASSWORD)).thenReturn(systemUser);
		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);
	}

	@Test
	public void testSignInWithValidUserValidPassword() throws Exception {
		// execute
		boolean actual = fixture.signIn(systemUser.getName(), PASSWORD);
		// validate
		Assert.assertTrue(actual);
		Assert.assertEquals(systemUser.getId(), fixture.getSystemUserId());

		Mockito.verify(systemUserService).authorize(systemUser.getName(), PASSWORD);
		Mockito.verify(daoSupport, Mockito.never()).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testGetRoles() throws Exception {
		// setup
		fixture.signIn(systemUser.getName(), PASSWORD);
		// execute
		Roles roles = fixture.getRoles();
		// validate
		for (SystemUserRole systemUserRole : systemUser.getSystemUserRoles()) {
			Assert.assertTrue(systemUserRole.getRole().name(), roles.hasRole(systemUserRole.getRole().name()));
		}
		Mockito.verify(systemUserService).authorize(systemUser.getName(), PASSWORD);
		Mockito.verify(daoSupport).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testNotSignedInGetEmptyRoles() throws Exception {
		// execute
		Assert.assertTrue(fixture.getRoles().isEmpty());
		// validate
		Mockito.verify(daoSupport, Mockito.never()).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testHasRole() throws Exception {
		// setup
		fixture.signIn(systemUser.getName(), PASSWORD);
		// execute
		Assert.assertTrue(fixture.hasRole(Role.ADMIN));
		// validate
		Mockito.verify(systemUserService).authorize(systemUser.getName(), PASSWORD);
		Mockito.verify(daoSupport).find(SystemUser.class, systemUser.getId());
	}
}
