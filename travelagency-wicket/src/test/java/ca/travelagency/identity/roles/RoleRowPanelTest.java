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
package ca.travelagency.identity.roles;

import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserRole;

public class RoleRowPanelTest extends BaseWicketTester {
	private SystemUser systemUser;
	private RolesPanel rolesPanel;
	private SystemUserRole systemUserRole;

	@Before
	public void setUp() {
		stubItemDataProvider();
		rolesPanel = new RolesPanel(COMPONENT_ID, DaoEntityModelFactory.make(systemUser));
	}

	private void stubItemDataProvider() {
		systemUser = SystemUserHelper.makeSystemUserWithRole();

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);
	}

	@Test
	public void testComponents() {
		systemUserRole = systemUser.getSystemUserRoles().first();
		IModel<SystemUserRole> model = DaoEntityModelFactory.make(systemUserRole);
		Mockito.when(daoSupport.find(SystemUserRole.class, systemUserRole.getId())).thenReturn(systemUserRole);

		RoleRowPanel roleRowPanel = new RoleRowPanel(COMPONENT_ID, model, rolesPanel);
		tester.startComponentInPage(roleRowPanel);

		tester.assertModelValue(COMPONENT_PATH+SystemUserRole.Properties.role.name(), systemUserRole.getRole());

		tester.assertComponent(COMPONENT_PATH+RoleRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(SystemUserRole.class, systemUserRole.getId());
	}

	@Test
	public void testWithNotActiveSystemUser() {
		systemUser.setActive(false);

		systemUserRole = systemUser.getSystemUserRoles().first();
		IModel<SystemUserRole> model = DaoEntityModelFactory.make(systemUserRole);
		Mockito.when(daoSupport.find(SystemUserRole.class, systemUserRole.getId())).thenReturn(systemUserRole);

		RoleRowPanel roleRowPanel = new RoleRowPanel(COMPONENT_ID, model, rolesPanel);
		tester.startComponentInPage(roleRowPanel);

		tester.assertInvisible(COMPONENT_PATH+RoleRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(SystemUserRole.class, systemUserRole.getId());
	}
}
