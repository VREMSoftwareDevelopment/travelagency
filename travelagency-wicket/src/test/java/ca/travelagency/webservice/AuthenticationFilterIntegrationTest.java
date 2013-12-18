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

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.WebserviceTester;
import ca.travelagency.identity.SystemUser;

public class AuthenticationFilterIntegrationTest extends WebserviceTester<SystemUser> {
	private static final String WEBSERVICE_URL = "http://localhost:8080/webservice";

	private HttpURLConnection httpURLConnection;

	@Before
	public void setUp() throws Exception {
		httpURLConnection = (HttpURLConnection) new URL(WEBSERVICE_URL).openConnection();
	}

	@Test
	public void testNotAuthorized() throws Exception {
		// execute
		httpURLConnection.connect();
		// validate
		Assert.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, httpURLConnection.getResponseCode());
	}

	@Test
	public void testAuthorized() throws Exception {
		// setup
		setupAuthorization(httpURLConnection);
		// execute
		httpURLConnection.connect();
		// validate
		Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, httpURLConnection.getResponseCode());
	}

}
