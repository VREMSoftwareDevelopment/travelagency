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
package ca.travelagency.invoice.view;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTraveler;

public class InvoiceViewLinkTest extends BaseWicketTester {
	private Invoice invoice;
	private InvoiceViewLink fixture;

	@Before
	public void setUp() throws Exception {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(Customer.class, invoice.getCustomer().getId())).toReturn(invoice.getCustomer());
		Mockito.stub(daoSupport.find(InvoiceDestination.class, invoice.getInvoiceDestinations().first().getId())).toReturn(invoice.getInvoiceDestinations().first());
		Mockito.stub(daoSupport.find(InvoiceTraveler.class, invoice.getInvoiceTravelers().first().getId())).toReturn(invoice.getInvoiceTravelers().first());
		Mockito.stub(daoSupport.find(InvoiceNote.class, invoice.getInvoiceNotes().first().getId())).toReturn(invoice.getInvoiceNotes().first());
		Mockito.stub(daoSupport.find(InvoicePayment.class, invoice.getInvoicePayments().first().getId())).toReturn(invoice.getInvoicePayments().first());
		Mockito.stub(daoSupport.find(InvoiceItem.class, invoice.getInvoiceItems().get(0).getId())).toReturn(invoice.getInvoiceItems().get(0));

		fixture = new InvoiceViewLink(COMPONENT_ID, Model.of(invoice));
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+InvoiceViewLink.LINK, AjaxLink.class);
		tester.assertComponent(COMPONENT_PATH+InvoiceViewLink.LINK_MODALWINDOW, InvoiceModalWindow.class);
		tester.isInvisible(COMPONENT_PATH+InvoiceViewLink.LINK_MODALWINDOW);
	}

	@Test
	public void testRedirectToInvoiceView() {
		// setup
		tester.startComponentInPage(fixture);
		// execute
	 	tester.clickLink(COMPONENT_PATH+InvoiceViewLink.LINK);
		// validate
		tester.isVisible(COMPONENT_PATH+InvoiceViewLink.LINK_MODALWINDOW);
	}

}
