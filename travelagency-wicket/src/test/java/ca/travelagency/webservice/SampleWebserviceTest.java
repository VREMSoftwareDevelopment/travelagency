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
package ca.travelagency.webservice;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.identity.AuthenticationException;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserService;
import ca.travelagency.utils.JsonUtils;

@RunWith(MockitoJUnitRunner.class)
public class SampleWebserviceTest {

	private static final String TEST_USERNAME = "test_username";
	private static final String TEST_PASSWORD = "test_password";

	@Mock private SystemUserService systemUserService;
	@Mock private HttpServletResponse response;
	@Mock private HttpServletRequest request;

	private StringWriter stringWriter;
	private SampleWebservice fixture;
	private SystemUser systemUser;

	@Before
	public void setUp() throws Exception {
		systemUser = SystemUserHelper.makeSystemUserWithRole();

		stringWriter = new StringWriter();

		fixture = new SampleWebservice();
		fixture.setSystemUserService(systemUserService);

		Mockito.stub(response.getWriter()).toReturn(new PrintWriter(stringWriter));
	}

	@After
	public void tearDown() throws Exception {
		verifyRequestParameters();
	}

	@Test
	public void testSystemUserNotFound() throws Exception {
		// setup
		setupRequestParameters(TEST_USERNAME, TEST_PASSWORD);
		// expected
		Mockito.when(systemUserService.authorize(TEST_USERNAME, TEST_PASSWORD)).thenThrow(new AuthenticationException());
		// execute
		fixture.doGet(request, response);
		// validate
		Mockito.verify(systemUserService).authorize(TEST_USERNAME, TEST_PASSWORD);
		Mockito.verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	@Test
	public void testSystemUserWithEmptyUsername() throws Exception {
		// setup
		setupRequestParameters(null, TEST_PASSWORD);
		// execute
		fixture.doGet(request, response);
		// validate
		Mockito.verify(systemUserService, Mockito.never()).authorize(null, TEST_PASSWORD);
		Mockito.verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	@Test
	public void testSystemUserWithEmptyPassword() throws Exception {
		// setup
		setupRequestParameters(TEST_USERNAME, null);
		// execute
		fixture.doGet(request, response);
		// validate
		Mockito.verify(systemUserService, Mockito.never()).authorize(TEST_USERNAME, null);
		Mockito.verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	@Test
	public void testSystemUserWithEmptyUsernameAndPassword() throws Exception {
		// setup
		setupRequestParameters(null, null);
		// execute
		fixture.doGet(request, response);
		// validate
		Mockito.verify(systemUserService, Mockito.never()).authorize(null, null);
		Mockito.verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	@Test
	public void testSystemUserWithUsernameAndPassword() throws Exception {
		// setup
		String expected = new JsonUtils().serialize(systemUser.getId());
		setupRequestParameters(TEST_USERNAME, TEST_PASSWORD);
		// expected
		Mockito.when(systemUserService.authorize(TEST_USERNAME, TEST_PASSWORD)).thenReturn(systemUser);
		// execute
		fixture.doGet(request, response);
		// validate
		Assert.assertEquals(expected, stringWriter.toString());
		Mockito.verify(systemUserService).authorize(TEST_USERNAME, TEST_PASSWORD);
	}

	private void verifyRequestParameters() {
		Mockito.verify(request).getParameter(SampleWebservice.USERNAME);
		Mockito.verify(request).getParameter(SampleWebservice.PASSWORD);
	}

	private void setupRequestParameters(String username, String password) {
		Mockito.when(request.getParameter(SampleWebservice.USERNAME)).thenReturn(username);
		Mockito.when(request.getParameter(SampleWebservice.PASSWORD)).thenReturn(password);
	}
}
