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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AuthenticationFilter implements Filter {
	public static final String AUTHORIZATION_KEY = "Authorization";
	public static final String MAGIC_VALUE = "Some Magic Value That Nobody Knows";

	private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			if (!isAuthorized((HttpServletRequest) request)) {
				failedAuthorization((HttpServletResponse) response);
				LOGGER.error("Not authorization to access the service");
				return;
			}
			chain.doFilter(request, response);
		} finally {
		}
	}

	boolean isAuthorized(HttpServletRequest httpRequest) {
		return MAGIC_VALUE.equals(httpRequest.getHeader(AUTHORIZATION_KEY));
	}

	void failedAuthorization(HttpServletResponse httpResponse) {
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

}

