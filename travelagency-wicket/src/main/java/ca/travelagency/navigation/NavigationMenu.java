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

import org.apache.wicket.Page;

import ca.travelagency.HomePage;
import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.customer.CustomersPage;
import ca.travelagency.identity.MyProfilePage;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUsersPage;
import ca.travelagency.invoice.InvoicesPage;
import ca.travelagency.salesreports.ReportsPage;

public class NavigationMenu extends BaseNavigationMenu {
	private static final long serialVersionUID = 1L;

	static final String CUSTOMERS = "customers";
	static final String HOME = "home";
	static final String INVOICES = "invoices";
	static final String MYPROFILE = "myprofile";
	static final String REPORTS = "reports";
	static final String SYSTEM_USERS = "systemUsers";

	static final String RECONCILIATION_MENU = "reconciliationMenu";
	static final String CONFIG_MENU = "configMenu";

	public NavigationMenu(String id, Class<? extends Page> currentPage) {
		super(id);

		add(bookmarkablePageLink(CUSTOMERS, CustomersPage.class, currentPage));
		add(bookmarkablePageLink(HOME, HomePage.class, currentPage));
		add(bookmarkablePageLink(INVOICES, InvoicesPage.class, currentPage));
		add(bookmarkablePageLink(MYPROFILE, MyProfilePage.class, currentPage));
		add(bookmarkablePageLink(REPORTS, ReportsPage.class, currentPage));
		add(bookmarkablePageLink(SYSTEM_USERS, SystemUsersPage.class, currentPage).setVisible(hasRole(Role.ADMIN)));

		add(new ConfigMenu(CONFIG_MENU, currentPage).setVisible(hasRole(Role.ADMIN)));
		add(new ReconciliationMenu(RECONCILIATION_MENU, currentPage).setVisible(hasRole(Role.ADMIN)));
	}

	private boolean hasRole(Role role) {
		return ((AuthenticatedSession) getSession()).hasRole(role);
	}

}
