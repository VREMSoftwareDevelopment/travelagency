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
package ca.travelagency;

import org.apache.wicket.model.IModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.invoice.InvoicesPanel;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.salesreports.SalesSummaryPanel;

public class HomePageTest extends BaseWicketTester {

	@Test
	public void testPageIsRendered() {
		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);

		tester.assertComponent(HomePage.UNPAID_INVOICES, InvoicesPanel.class);
		tester.assertComponent(HomePage.INVOICES_STATS, SalesSummaryPanel.class);
	}

	@Test
	public void testGetSystemUserAsAdminReturnsNull() {
		// setup
		usingAdminSystemUser();
		HomePage homePage = new HomePage();
		// execute
		SystemUser systemUser = homePage.getSystemUser();
		// validate
		Assert.assertNull(systemUser);
	}

	@Test
	public void testGetSystemUserAsAgentReturnsLogedInUser() {
		// setup
		usingAgentSystemUser();
		HomePage homePage = new HomePage();
		// execute
		SystemUser systemUser = homePage.getSystemUser();
		// validate
		Assert.assertNotNull(systemUser);
	}

	@Test
	public void testGetInvoiceModel() {
		// setup
		usingAgentSystemUser();
		HomePage homePage = new HomePage();
		Mockito.stub(daoSupport.find(SystemUser.class, currentSystemUser.getId())).toReturn(currentSystemUser);
		// execute
		IModel<InvoiceFilter> invoiceModel = homePage.getInvoiceModel();
		// validate
		InvoiceFilter invoiceFilter = invoiceModel.getObject();
		Assert.assertEquals(currentSystemUser, invoiceFilter.getSystemUser());
		Assert.assertEquals(InvoiceStatus.Active, invoiceFilter.getStatus());
		Assert.assertEquals(OrderBy.make(Invoice.Properties.totalDueDate.name()), invoiceFilter.getDefaultOrderBy());
		Assert.assertTrue(invoiceFilter.isShowUnpaidInvoicesOnly());

	}
}
