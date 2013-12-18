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
package ca.travelagency.components.formheader;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;

public class DaoEntityModelTest extends BaseWicketTester {

	private DaoEntityModel<SystemUser> fixture;

	@Test
	public void testNewObject() {
		// setup
		SystemUser expected = SystemUserHelper.makeSystemUserWithoutId();
		fixture = DaoEntityModel.make(expected, SystemUser.class);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNotNull(fixture.getNewInstance());
		Assert.assertFalse(fixture.isPersistedDaoEntity());
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(expected, fixture.getNewInstance());
		Assert.assertEquals(actual, fixture.getNewInstance());
	}

	@Test
	public void testNewObjectInstanceIsCreatedWhenIdIsNotGiven() {
		// setup
		fixture = DaoEntityModel.make(null, SystemUser.class);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNotNull(fixture.getNewInstance());
		Assert.assertFalse(fixture.isPersistedDaoEntity());
		Assert.assertEquals(actual, fixture.getNewInstance());
	}

	@Test
	public void testExistingObjectIsLoadedWhenIdIsGiven() {
		// setup
		SystemUser expected = SystemUserHelper.makeSystemUser();
		fixture = DaoEntityModel.make(expected, expected.getTrueClass());
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, expected.getId())).thenReturn(expected);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNull(fixture.getNewInstance());
		Assert.assertTrue(fixture.isPersistedDaoEntity());
		Assert.assertEquals(expected, actual);
		// verify
		Mockito.verify(daoSupport).find(SystemUser.class, expected.getId());
	}

	@Test
	public void testExistingObjectIsLoadedWhenExistingObjectIsGiven() {
		// setup
		SystemUser expected = SystemUserHelper.makeSystemUser();
		fixture = DaoEntityModel.make(expected, expected.getTrueClass());
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, expected.getId())).thenReturn(expected);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNull(fixture.getNewInstance());
		Assert.assertTrue(fixture.isPersistedDaoEntity());
		Assert.assertEquals(expected, actual);
		// verify
		Mockito.verify(daoSupport).find(SystemUser.class, expected.getId());
	}

	@Test
	public void testNewObjectIsLoadedWhenItBecomesExisting() {
		// setup
		fixture = DaoEntityModel.make(null, SystemUser.class);
		SystemUser expected = fixture.getNewInstance();
		expected.setId(1L);
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, expected.getId())).thenReturn(expected);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNull(fixture.getNewInstance());
		Assert.assertTrue(fixture.isPersistedDaoEntity());
		Assert.assertEquals(expected, actual);
		// verify
		Mockito.verify(daoSupport).find(SystemUser.class, expected.getId());
	}

	@Test
	public void testObjectIsLoadedWhenItBecomesExisting() {
		// setup
		SystemUser expected = SystemUserHelper.makeSystemUserWithoutId();
		fixture = DaoEntityModel.make(expected, SystemUser.class);
		expected.setId(1L);
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, expected.getId())).thenReturn(expected);
		// execute
		SystemUser actual = fixture.getObject();
		// validate
		Assert.assertNull(fixture.getNewInstance());
		Assert.assertTrue(fixture.isPersistedDaoEntity());
		Assert.assertEquals(expected, actual);
		// verify
		Mockito.verify(daoSupport).find(SystemUser.class, expected.getId());
	}

}
