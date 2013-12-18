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

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.utils.StringUtils;

public class PaymentFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+PaymentFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private Invoice invoice;
	private InvoicePayment invoicePayment;
	private PaymentsPanel paymentsPanel;

	@Before
	public void setUp() {
		stubPaymentDataProvider();

		Panel totalsPanel = new Panel("totalsPanel") {private static final long serialVersionUID = 1L;};
		paymentsPanel = new PaymentsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice), totalsPanel);
	}

	private void stubPaymentDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoicePayment = invoice.getInvoicePayments().first();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).toReturn(invoicePayment);
	}

	@Test
	public void testComponents() {
		PaymentFormPanel paymentFormPanel = new PaymentFormPanel(COMPONENT_ID, invoicePayment, paymentsPanel);
		tester.startComponentInPage(paymentFormPanel);

		tester.assertComponent(COMPONENT_PATH+PaymentFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+PaymentFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+PaymentFormPanel.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+PaymentFormPanel.CANCEL_BUTTON, CancelPanel.class);

		tester.assertComponent(FORM_PATH+InvoicePayment.Properties.amount, RequiredTextField.class);
		tester.assertComponent(FORM_PATH+InvoicePayment.Properties.reconciled, CheckBox.class);
		tester.assertComponent(FORM_PATH+InvoicePayment.Properties.paymentType, DropDownChoice.class);
		tester.assertComponent(FORM_PATH+InvoicePayment.Properties.date, DateField.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoicePayment.class, invoicePayment.getId());
	}

	@Test
	public void testComponentsAsAgent() {
		usingAgentSystemUser();
		PaymentFormPanel paymentFormPanel = new PaymentFormPanel(COMPONENT_ID, invoicePayment, paymentsPanel);
		tester.startComponentInPage(paymentFormPanel);

		tester.isInvisible(FORM_PATH+InvoicePayment.Properties.reconciled);
	}

	@Test
	public void testNewPaymentComponents() {
		PaymentFormPanel paymentFormPanel = new PaymentFormPanel(COMPONENT_ID, paymentsPanel);
		tester.startComponentInPage(paymentFormPanel);

		tester.assertInvisible(FORM_PATH+PaymentFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoicePayment.class, invoicePayment.getId());
	}

	@Test
	public void testValues() {
		PaymentFormPanel paymentFormPanel = new PaymentFormPanel(COMPONENT_ID, invoicePayment, paymentsPanel);
		tester.startComponentInPage(paymentFormPanel);

		tester.assertModelValue(FORM_PATH+InvoicePayment.Properties.amount, invoicePayment.getAmount());
		tester.assertModelValue(FORM_PATH+InvoicePayment.Properties.reconciled, invoicePayment.isReconciled());
		tester.assertModelValue(FORM_PATH+InvoicePayment.Properties.paymentType, invoicePayment.getPaymentType());
		tester.assertModelValue(FORM_PATH+InvoicePayment.Properties.date, invoicePayment.getDate());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoicePayment.class, invoicePayment.getId());
	}

	@Test
	public void testRequiredFields() {
		PaymentFormPanel panel = new PaymentFormPanel(COMPONENT_ID, paymentsPanel);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+PaymentFormPanel.FORM);
		formTester.setValue(InvoicePayment.Properties.amount.name(), StringUtils.EMPTY);
		formTester.submit();

		tester.assertErrorMessages(new String [] {
			"'Payment Amount' is required."
		});
	}

}

