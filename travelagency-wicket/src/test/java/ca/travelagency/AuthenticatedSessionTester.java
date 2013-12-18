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
package ca.travelagency;

import org.apache.wicket.request.Request;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.identity.SystemUser;

public class AuthenticatedSessionTester extends AuthenticatedSession {
	private static final long serialVersionUID = 1L;

	private SystemUser systemUser;

	public AuthenticatedSessionTester(Request request) {
		super(request);
	}

	@Override
	public boolean authenticate(final String name, final String password) {
		return systemUser == null ? false : true;
	}

	@Override
	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
		if (systemUser == null) {
			signOut();
			return;
		}
		signIn(systemUser.getName(), "password");
	}
}
