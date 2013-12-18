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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.travelagency.identity.AuthenticationException;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserService;
import ca.travelagency.utils.AutowiredUtils;
import ca.travelagency.utils.JsonUtils;
import ca.travelagency.utils.StringUtils;

@Component
public class SampleWebservice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";

	@Autowired
	private SystemUserService systemUserService;

	@Override
	public void init() throws ServletException {
		AutowiredUtils.autowire(getServletConfig().getServletContext(), this);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		try {
			SystemUser systemUser = systemUserService.authorize(username, password);
			new JsonUtils().serialize(systemUser.getId(), response.getWriter());
		} catch (AuthenticationException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	void setSystemUserService(SystemUserService systemUserService) {
		this.systemUserService = systemUserService;
	}
}
