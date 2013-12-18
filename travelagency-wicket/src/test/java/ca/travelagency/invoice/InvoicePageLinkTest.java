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
package ca.travelagency.invoice;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;

public class InvoicePageLinkTest extends BaseWicketTester {
	private Invoice invoice;
	private InvoicePageLink fixture;

	@Before
	public void setUp() throws Exception {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(Customer.class, invoice.getCustomer().getId())).toReturn(invoice.getCustomer());
		Mockito.stub(daoSupport.find(InvoiceDestination.class, invoice.getInvoiceDestinations().first().getId())).toReturn(invoice.getInvoiceDestinations().first());

		fixture = new InvoicePageLink(COMPONENT_ID, Model.of(invoice));
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+InvoicePageLink.LINK, Link.class);
		tester.assertLabel(COMPONENT_PATH+InvoicePageLink.LINK+BasePage.PATH_SEPARATOR+InvoicePageLink.LABEL, invoice.getInvoiceNumber());
	}

	@Test
	public void testRedirectToInvoicePage() {
		// setup
		tester.startComponentInPage(fixture);
		// execute
	 	tester.clickLink(COMPONENT_PATH+InvoicePageLink.LINK);
		// validate
		tester.assertRenderedPage(InvoicePage.class);
	}

}
