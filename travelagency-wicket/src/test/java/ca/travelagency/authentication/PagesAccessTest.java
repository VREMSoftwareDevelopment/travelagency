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
package ca.travelagency.authentication;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.HomePage;
import ca.travelagency.config.CitiesPage;
import ca.travelagency.config.CompanyPage;
import ca.travelagency.config.ProductsPage;
import ca.travelagency.config.ProvincesPage;
import ca.travelagency.config.SalutationsPage;
import ca.travelagency.customer.CustomerPage;
import ca.travelagency.customer.CustomersPage;
import ca.travelagency.identity.MyProfilePage;
import ca.travelagency.identity.SystemUserPage;
import ca.travelagency.identity.SystemUsersPage;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicePreviewPage;
import ca.travelagency.invoice.InvoicesPage;
import ca.travelagency.reconciliation.CommissionPage;
import ca.travelagency.reconciliation.PaymentPage;
import ca.travelagency.salesreports.ReportsPage;


public class PagesAccessTest extends BaseWicketTester {

	private static final PageInfo [] pageClasses = new PageInfo [] {
		PageInfo.makeAgent(CustomerPage.class),
		PageInfo.makeAgent(CustomersPage.class),
		PageInfo.makeAgent(HomePage.class),
		PageInfo.makeAgent(InvoicePage.class),
		PageInfo.makeAgent(InvoicePreviewPage.class),
		PageInfo.makeAgent(InvoicesPage.class),
		PageInfo.makeAgent(MyProfilePage.class),
		PageInfo.makeAgent(ReportsPage.class),
		PageInfo.makeAgent(SystemUserPage.class),

		PageInfo.makeAdmin(CitiesPage.class),
		PageInfo.makeAdmin(CommissionPage.class),
		PageInfo.makeAdmin(CompanyPage.class),
		PageInfo.makeAdmin(PaymentPage.class),
		PageInfo.makeAdmin(ProductsPage.class),
		PageInfo.makeAdmin(ProvincesPage.class),
		PageInfo.makeAdmin(SalutationsPage.class),
		PageInfo.makeAdmin(SystemUsersPage.class),
	};

	@Test
	public void testWhenNotSignedInRerouteToSignInPage() {
		System.out.println(">>> Not Signed Pages");
		usingNoSystemUser();

		for (PageInfo pageInfo: pageClasses) {
			System.out.println(pageInfo.getPage().getSimpleName());
			try {
				tester.startPage(pageInfo.getPage());
			} catch (UnauthorizedInstantiationException e) {
				// expected
				continue;
			}
			tester.assertRenderedPage(SignInPage.class);
		}
	}

	@Test
	public void testWhenAgentIsSignedIn() {
		System.out.println(">>> Agent Pages");
		usingAgentSystemUser();

		for (PageInfo pageInfo: pageClasses) {
			System.out.println(pageInfo.getPage().getSimpleName());
			try {
				tester.startPage(pageInfo.getPage());
			} catch (UnauthorizedInstantiationException e) {
				// expected
				continue;
			}
			if (pageInfo.isAgentVisible()) {
				tester.assertRenderedPage(pageInfo.getPage());
			} else {
				tester.assertRenderedPage(SignInPage.class);
			}
		}
	}

	@Test
	public void testWhenAdminIsSignedIn() {
		System.out.println(">>> Admin Pages");
		usingAdminSystemUser();

		for (PageInfo pageInfo: pageClasses) {
			System.out.println(pageInfo.getPage().getSimpleName());
			tester.startPage(pageInfo.getPage());
			if (pageInfo.isAdminVisible()) {
				tester.assertRenderedPage(pageInfo.getPage());
			} else {
				tester.assertRenderedPage(SignInPage.class);
			}
		}
	}
/*
	@Test
	public void testStatelessPages() {
		System.out.println(">>> Stateless Pages");
		usingAdminSystemUser();

		for (PageInfo pageInfo: pageClasses) {
			tester.startPage(pageInfo.getPage());
			Page page = tester.getLastRenderedPage();
			if (!page.isPageStateless()) {
				System.err.println(pageInfo.getPage().getSimpleName() + " page is NOT stateless.");
	            Component component = page.visitChildren(Component.class, new StatelessChecker());
	            if (component != null){
	            	System.err.println(component.getClass().getName()
	            			+" ID:" + component.getId()
	            			+" Markup ID:" + component.getMarkupId()
	            			+ " component is NOT stateless.");
	            }
			}
		}
	}

	class StatelessChecker implements IVisitor<Component, Component> {
		@Override
		public void component(Component component, IVisit<Component> iVisit) {
			if (!component.isStateless()) {
				iVisit.stop(component);
			}
		}
	}
*/
}
