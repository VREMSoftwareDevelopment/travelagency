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

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserRole;

public class RoleFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+RoleFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private SystemUser systemUser;
	private SystemUserRole systemUserRole;
	private RolesPanel rolesPanel;

	@Before
	public void setUp() {
		stubRoleDataProvider();
		rolesPanel = new RolesPanel(COMPONENT_ID, DaoEntityModelFactory.make(systemUser));
	}

	private void stubRoleDataProvider() {
		systemUser = SystemUserHelper.makeSystemUserWithRole();
		systemUserRole = systemUser.getSystemUserRoles().first();

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);
	}

	@Test
	public void testComponents() {
		RoleFormPanel roleFormPanel = new RoleFormPanel(COMPONENT_ID, rolesPanel);
		tester.startComponentInPage(roleFormPanel);

		tester.assertComponent(COMPONENT_PATH+RoleFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+RoleFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+RoleFormPanel.RESET_BUTTON, ResetPanel.class);

		tester.assertComponent(FORM_PATH+SystemUserRole.Properties.role, DropDownChoice.class);

		Mockito.verify(daoSupport, Mockito.never()).find(SystemUserRole.class, systemUserRole.getId());
	}
}

