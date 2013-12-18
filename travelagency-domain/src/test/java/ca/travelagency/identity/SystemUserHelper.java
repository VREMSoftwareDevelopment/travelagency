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


public class SystemUserHelper {
	public static final String SECONDARY_PHONE = "222-222-2222";
	public static final String PRIMARY_PHONE = "111-111-1111";
	public static final String LAST_NAME = "Name Test Last";
	public static final String FIRST_NAME = "Name Test First";
	public static final String NAME = "Name Test";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email@email.com";

	private static long count = 0;

	public static SystemUser makeSystemUserWithRole(Role ... roles) {
		SystemUser systemUser = makeSystemUser();
		for (Role role : roles) {
			systemUser.addSystemUserRole(makeSystemUserRole(role));
		}
		return systemUser;
	}

	public static SystemUser makeSystemUserWithRole() {
		return makeSystemUserWithRole(Role.ADMIN);
	}

	public static SystemUser makeSystemUser() {
		SystemUser systemUser = makeSystemUserWithoutId();
		systemUser.setId(count);
		return systemUser;
	}

	public static SystemUser makeSystemUserWithoutId() {
		count--;
		SystemUser systemUser = SystemUser.make(NAME+count, PASSWORD);
		systemUser.setFirstName(FIRST_NAME+count);
		systemUser.setLastName(LAST_NAME+count);
		systemUser.setEmail(EMAIL);
		systemUser.setPrimaryPhone(PRIMARY_PHONE);
		systemUser.setSecondaryPhone(SECONDARY_PHONE);
		return systemUser;
	}

	public static SystemUserRole makeSystemUserRole(Role role) {
		SystemUserRole systemUserRole = makeSystemUserRoleWithoutId(role);
		systemUserRole.setId(count);
		return systemUserRole;
	}

	public static SystemUserRole makeSystemUserRoleWithoutId(Role role) {
		count--;
		return SystemUserRole.make(role);
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		SystemUserHelper.count = count;
	}


}
