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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserRole;

public class RolesDetailPanelTest extends BaseWicketTester {
	private static final String PATH = COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER+BasePage.PATH_SEPARATOR;

	@Mock private AjaxRequestTarget target;
	private SystemUser systemUser;
	private RolesPanel fixture;

	@Before
	public void setUp() {
		systemUser = SystemUserHelper.makeSystemUserWithRole();

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);
		Mockito.stub(daoSupport.find(SystemUserRole.class, systemUser.getSystemUserRoles().first().getId())).toReturn(systemUser.getSystemUserRoles().first());

		fixture = new RolesPanel(COMPONENT_ID, DaoEntityModelFactory.make(systemUser));
	}

	@Test
	public void testUpdate() {
		tester.startComponentInPage(fixture);

		SystemUserRole systemUserRole = SystemUserHelper.makeSystemUserRole(Role.AGENT);

		fixture.update(target, systemUserRole);

		Assert.assertTrue(systemUser.getSystemUserRoles().contains(systemUserRole));
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(systemUser);
	}

	@Test
	public void testDelete() {
		tester.startComponentInPage(fixture);

		fixture.delete(target, systemUser.getSystemUserRoles().first());

		Assert.assertTrue(systemUser.getSystemUserRoles().isEmpty());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(systemUser);
	}

	@Test
	public void testDetailsPanelComponents() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER, WebMarkupContainer.class);
		tester.assertComponent(PATH+DetailsPanel.HEADER, RolesHeaderPanel.class);
		tester.assertComponent(PATH+DetailsPanel.FORM, RoleFormPanel.class);
		tester.assertComponent(PATH+DetailsPanel.ROWS, ListView.class);
	}

}
