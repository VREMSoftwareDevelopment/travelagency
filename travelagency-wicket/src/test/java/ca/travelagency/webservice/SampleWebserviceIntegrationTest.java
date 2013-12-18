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

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.WebserviceTester;
import ca.travelagency.identity.NameCriteria;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.utils.JsonUtils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class SampleWebserviceIntegrationTest extends WebserviceTester<SystemUser> {
	private static final String PATH_URL = "http://localhost:8080/webservice/sample";

	private SystemUser systemUser;

	@Before
	public void setUp() throws Exception {
		systemUser = getCurrentSystemUser();
	}

	@Test
	public void testNotAutorized() throws Exception {
		// setup
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(PATH_URL).openConnection();
		setupAuthorization(httpURLConnection);
		// execute
		httpURLConnection.connect();
		// validate
		Assert.assertEquals(HttpServletResponse.SC_FORBIDDEN, httpURLConnection.getResponseCode());
	}

	@Test
	public void testAuthorized() throws Exception {
		// setup
		String expected = new JsonUtils().serialize(systemUser.getId());
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(PATH_URL+makeParameters()).openConnection();
		setupAuthorization(httpURLConnection);
		// execute
		httpURLConnection.connect();
		// validate
		Assert.assertEquals(HttpServletResponse.SC_OK, httpURLConnection.getResponseCode());
		Assert.assertEquals(expected, CharStreams.toString(new InputStreamReader(httpURLConnection.getInputStream(), Charsets.UTF_8)));
	}

	private String makeParameters() {
		return "?"+SampleWebservice.USERNAME+"="+systemUser.getName()+"&"+SampleWebservice.PASSWORD+"=Pa55w0rD";
	}

	private SystemUser getCurrentSystemUser() throws Exception {
		List<SystemUser> results = find(NameCriteria.make("Admin"));
		return results.isEmpty() ? null : results.get(0);
	}
}
