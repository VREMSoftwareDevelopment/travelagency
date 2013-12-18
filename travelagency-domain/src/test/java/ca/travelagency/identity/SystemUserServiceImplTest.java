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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SystemUserServiceImplTest {
	private static final String SOME_NAME = "Some Name";

	private static final String PASSWORD = "password";

	@Mock
	private DaoSupport<SystemUser> daoSupport;

	private SystemUserServiceImpl fixture;

	@Before
	public void setUp() {
		fixture = new SystemUserServiceImpl();
		fixture.setDaoSupport(daoSupport);
	}

	@Test(expected=AuthenticationException.class)
	public void testDoNotAuthorizeEmptyNameAndPassword() throws Exception {
		NameCriteria nameCriteria = NameCriteria.make(StringUtils.EMPTY);
		fixture.authorize(StringUtils.EMPTY, StringUtils.EMPTY);

		Mockito.verify(daoSupport, Mockito.never()).find(nameCriteria);
	}

	@Test
	public void testAuthorizeExistingSystemUser() throws Exception {
		List<SystemUser> systemUserList = makeSingleSystemUserList();
		NameCriteria nameCriteria = NameCriteria.make(systemUserList.get(0).getName());

		Mockito.when(daoSupport.find(nameCriteria)).thenReturn(systemUserList);

		SystemUser actual = fixture.authorize(systemUserList.get(0).getName(), PASSWORD);

		Assert.assertEquals(systemUserList.get(0), actual);

		Mockito.verify(daoSupport).find(nameCriteria);
	}

	@Test(expected=AuthenticationException.class)
	public void testDoNotAuthorizeWhenPasswordDoesNotMatch() throws Exception {
		List<SystemUser> systemUserList = makeSingleSystemUserList();
		SystemUser systemUser = systemUserList.get(0);
		NameCriteria nameCriteria = NameCriteria.make(systemUser.getName());

		Mockito.when(daoSupport.find(nameCriteria)).thenReturn(systemUserList);

		fixture.authorize(systemUser.getName(), "NOT-"+PASSWORD);

		Mockito.verify(daoSupport).find(nameCriteria);
	}

	@Test(expected=AuthenticationException.class)
	public void testDoNotAuthorizeDiactivateSystemUser() throws Exception {
		List<SystemUser> systemUserList = makeSingleSystemUserList();
		SystemUser systemUser = systemUserList.get(0);
		systemUser.setActive(false);

		NameCriteria nameCriteria = NameCriteria.make(systemUser.getName());

		Mockito.when(daoSupport.find(nameCriteria)).thenReturn(systemUserList);

		fixture.authorize(systemUser.getName(), PASSWORD);

		Mockito.verify(daoSupport).find(nameCriteria);
	}

	@Test(expected=AuthenticationException.class)
	public void testDoNotAuthorizeNonExistingUser() throws Exception {
		Mockito.when(daoSupport.find(Mockito.any(NameCriteria.class))).thenReturn(makeEmptySystemUserList());

		fixture.authorize(SOME_NAME, PASSWORD);

		Mockito.verify(daoSupport).find(Mockito.any(NameCriteria.class));
	}

	@Test(expected=AuthenticationException.class)
	public void testDoNotAuthorizeMoreThenOneUserReturned() throws Exception {
		NameCriteria nameCriteria = NameCriteria.make(SOME_NAME);
		Mockito.when(daoSupport.find(nameCriteria)).thenReturn(makeSystemUserList());

		fixture.authorize(SOME_NAME, PASSWORD);

		Mockito.verify(daoSupport).find(nameCriteria);
	}

	private List<SystemUser> makeEmptySystemUserList() {
		return new ArrayList<SystemUser>();
	}

	private List<SystemUser> makeSingleSystemUserList() {
		return Lists.newArrayList(SystemUserHelper.makeSystemUser());
	}

	private List<SystemUser> makeSystemUserList() {
		return Lists.newArrayList(SystemUserHelper.makeSystemUser(), SystemUserHelper.makeSystemUser());
	}

}
