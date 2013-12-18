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
package ca.travelagency.invoice.payments;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePayment;

public class PaymentsPanelTest extends BaseWicketTester {
	@Mock private AjaxRequestTarget target;

	private Invoice invoice;
	private PaymentsPanel fixture;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoicePayment.class, invoice.getInvoicePayments().first().getId())).toReturn(invoice.getInvoicePayments().first());

		Panel totalsPanel = new Panel("totalsPanel") {private static final long serialVersionUID = 1L;};
		totalsPanel.setOutputMarkupId(true);

		fixture = new PaymentsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice), totalsPanel);
	}

	@Test
	public void testUpdate() {
		tester.startComponentInPage(fixture);

		InvoicePayment invoicePayment = InvoiceHelper.makePayment();

		fixture.update(target, invoicePayment);

		Assert.assertEquals(invoicePayment, invoice.getInvoicePayments().last());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	@Test
	public void testDelete() {
		tester.startComponentInPage(fixture);

		fixture.delete(target, invoice.getInvoicePayments().first());

		Assert.assertTrue(invoice.getInvoicePayments().isEmpty());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	private static final String PATH = COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER+BasePage.PATH_SEPARATOR;

	@Test
	public void testDetailsPanelComponents() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER, WebMarkupContainer.class);
		tester.assertComponent(PATH+DetailsPanel.HEADER, PaymentsHeaderPanel.class);
		tester.assertComponent(PATH+DetailsPanel.FORM, PaymentFormPanel.class);
		tester.assertComponent(PATH+DetailsPanel.ROWS, ListView.class);
	}

}
