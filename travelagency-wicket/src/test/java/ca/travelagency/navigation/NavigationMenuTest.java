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
package ca.travelagency.navigation;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.HomePage;
import ca.travelagency.customer.CustomersPage;
import ca.travelagency.identity.MyProfilePage;
import ca.travelagency.identity.SystemUsersPage;
import ca.travelagency.invoice.InvoicesPage;
import ca.travelagency.salesreports.ReportsPage;

public class NavigationMenuTest extends BaseWicketTester {

	@Test
	public void testNavigationMenuWithAdminSystemUser() {
		usingAdminSystemUser();

		tester.startComponentInPage(new NavigationMenu(COMPONENT_ID, HomePage.class));

		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.CUSTOMERS, CustomersPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.HOME, HomePage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.INVOICES, InvoicesPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.MYPROFILE, MyProfilePage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.REPORTS, ReportsPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+NavigationMenu.SYSTEM_USERS, SystemUsersPage.class, new PageParameters());

		tester.assertComponent(COMPONENT_PATH+NavigationMenu.CONFIG_MENU, ConfigMenu.class);
		tester.assertComponent(COMPONENT_PATH+NavigationMenu.RECONCILIATION_MENU, ReconciliationMenu.class);

		tester.isVisible(COMPONENT_PATH+NavigationMenu.CUSTOMERS);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.HOME);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.INVOICES);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.MYPROFILE);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.SYSTEM_USERS);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.CONFIG_MENU);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.RECONCILIATION_MENU);
	}

	@Test
	public void testNavigationMenuWithNotAdminSystemUser() {
		usingAgentSystemUser();

		tester.startComponentInPage(new NavigationMenu(COMPONENT_ID, HomePage.class));

		tester.isVisible(COMPONENT_PATH+NavigationMenu.CUSTOMERS);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.HOME);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.INVOICES);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.MYPROFILE);
		tester.isVisible(COMPONENT_PATH+NavigationMenu.REPORTS);

		tester.isInvisible(COMPONENT_PATH+NavigationMenu.SYSTEM_USERS);

		tester.isInvisible(COMPONENT_PATH+NavigationMenu.CONFIG_MENU);
		tester.isInvisible(COMPONENT_PATH+NavigationMenu.RECONCILIATION_MENU);
	}
}
