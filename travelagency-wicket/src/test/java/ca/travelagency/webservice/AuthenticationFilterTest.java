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

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {
	private AuthenticationFilter fixture;

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain chain;

	@Before
	public void setUp() throws Exception {
		fixture = new AuthenticationFilter();
	}

	@Test
	public void testAuthenticationKeyFailure() throws Exception {
		// expected
		Mockito.when(request.getHeader(AuthenticationFilter.AUTHORIZATION_KEY)).thenReturn(null);
		// execute
		fixture.doFilter(request, response, chain);
		// validate
		Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		Mockito.verify(request).getHeader(AuthenticationFilter.AUTHORIZATION_KEY);
		Mockito.verify(chain, Mockito.never()).doFilter(request, response);
	}

	@Test
	public void testAuthenticationKeySuccess() throws Exception {
		// expected
		Mockito.when(request.getHeader(AuthenticationFilter.AUTHORIZATION_KEY)).thenReturn(AuthenticationFilter.MAGIC_VALUE);
		// execute
		fixture.doFilter(request, response, chain);
		// validate
		Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		Mockito.verify(request).getHeader(AuthenticationFilter.AUTHORIZATION_KEY);
		Mockito.verify(chain).doFilter(request, response);
	}
}
