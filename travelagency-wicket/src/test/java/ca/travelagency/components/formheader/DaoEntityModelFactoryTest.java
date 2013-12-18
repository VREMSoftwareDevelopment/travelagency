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

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;


public class DaoEntityModelFactoryTest extends BaseWicketTester {

	@Test
	public void testExistingDaoEntityModel() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		// execute
		IModel<SystemUser> model = DaoEntityModelFactory.make(systemUser);
		// validate
		Assert.assertTrue(model instanceof CompoundPropertyModel);
		CompoundPropertyModel<SystemUser> actual = (CompoundPropertyModel<SystemUser>) model;
		Assert.assertTrue(actual.getChainedModel() instanceof DaoEntityModel);
	}

	@Test
	public void testNonExistingDaoEntityModel() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUserWithoutId();
		// execute
		IModel<SystemUser> model = DaoEntityModelFactory.make(systemUser);
		// validate
		Assert.assertTrue(model instanceof CompoundPropertyModel);
		CompoundPropertyModel<SystemUser> actual = (CompoundPropertyModel<SystemUser>) model;
		Assert.assertTrue(actual.getChainedModel() instanceof DaoEntityModel);
	}

	@Test
	public void testDaoEntityClassModel() throws Exception {
		// execute
		IModel<SystemUser> model = DaoEntityModelFactory.make(SystemUser.class);
		// validate
		Assert.assertTrue(model instanceof CompoundPropertyModel);
		CompoundPropertyModel<SystemUser> actual = (CompoundPropertyModel<SystemUser>) model;
		Assert.assertTrue(actual.getChainedModel() instanceof DaoEntityModel);
	}
}
