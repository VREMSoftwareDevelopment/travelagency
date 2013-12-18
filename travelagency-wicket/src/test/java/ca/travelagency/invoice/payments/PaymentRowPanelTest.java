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

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceStatus;

public class PaymentRowPanelTest extends BaseWicketTester {
	private Invoice invoice;
	private PaymentsPanel paymentsPanel;
	private InvoicePayment invoicePayment;

	@Before
	public void setUp() {
		stubPaymentDataProvider();

		Panel totalsPanel = new Panel("totalsPanel") {private static final long serialVersionUID = 1L;};
		paymentsPanel = new PaymentsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice), totalsPanel);
	}

	private void stubPaymentDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoice.addInvoicePayment(InvoiceHelper.makePayment());
		invoice.addInvoicePayment(InvoiceHelper.makePayment());

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		invoicePayment = invoice.getInvoicePayments().first();
		IModel<InvoicePayment> model = DaoEntityModelFactory.make(invoicePayment);
		Mockito.when(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).thenReturn(invoicePayment);

		PaymentRowPanel paymentRowPanel = new PaymentRowPanel(COMPONENT_ID, model, paymentsPanel);
		tester.startComponentInPage(paymentRowPanel);

		tester.assertModelValue(COMPONENT_PATH+InvoicePayment.Properties.date.name(), invoicePayment.getDate());
		tester.assertModelValue(COMPONENT_PATH+InvoicePayment.Properties.amountAsString.name(), invoicePayment.getAmountAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoicePayment.Properties.reconciled.name(), invoicePayment.isReconciled());
		tester.assertModelValue(COMPONENT_PATH+InvoicePayment.Properties.paymentType.name(), invoicePayment.getPaymentType());

		tester.assertComponent(COMPONENT_PATH+PaymentRowPanel.EDIT_BUTTON, EditPanel.class);
		tester.assertComponent(COMPONENT_PATH+PaymentRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoicePayment.class, invoicePayment.getId());
	}

	@Test
	public void testWithNotActiveInvoice() {
		invoice.setStatus(InvoiceStatus.Closed);

		invoicePayment = invoice.getInvoicePayments().first();
		IModel<InvoicePayment> model = DaoEntityModelFactory.make(invoicePayment);
		Mockito.when(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).thenReturn(invoicePayment);

		PaymentRowPanel paymentRowPanel = new PaymentRowPanel(COMPONENT_ID, model, paymentsPanel);
		tester.startComponentInPage(paymentRowPanel);

		tester.assertInvisible(COMPONENT_PATH+PaymentRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+PaymentRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoicePayment.class, invoicePayment.getId());
	}

	@Test
	public void testWithReconciledPayment() {
		invoicePayment = invoice.getInvoicePayments().first();
		invoicePayment.setReconciled(true);
		IModel<InvoicePayment> model = DaoEntityModelFactory.make(invoicePayment);
		Mockito.when(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).thenReturn(invoicePayment);

		PaymentRowPanel paymentRowPanel = new PaymentRowPanel(COMPONENT_ID, model, paymentsPanel);
		tester.startComponentInPage(paymentRowPanel);

		tester.assertInvisible(COMPONENT_PATH+PaymentRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+PaymentRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoicePayment.class, invoicePayment.getId());
	}
}
