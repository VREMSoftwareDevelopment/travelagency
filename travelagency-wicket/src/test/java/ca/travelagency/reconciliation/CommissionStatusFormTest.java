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
package ca.travelagency.reconciliation;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoiceStatus;

public class CommissionStatusFormTest extends BaseWicketTester {
	private static final String PATH = COMPONENT_PATH+CommissionStatusForm.FORM+InvoicePage.PATH_SEPARATOR+InvoiceItem.Properties.commissionStatus;
	private InvoiceItem invoiceItem;
	private Invoice invoice;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceItem = invoice.getInvoiceItems().get(0);

		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).toReturn(invoiceItem);
	}

	@Test
	public void testComponents() {
		//setup
		CommissionStatusForm panel = new CommissionStatusForm(COMPONENT_ID, invoiceItem);
		// execute
		tester.startComponentInPage(panel);
		// validate
		tester.assertComponent(COMPONENT_PATH+CommissionStatusForm.FORM, Form.class);
		tester.assertComponent(PATH, DropDownChoice.class);
		tester.assertModelValue(PATH, invoiceItem.getCommissionStatus());
	}

	@Test
	public void testCommissionVerified() {
		//setup
		CommissionStatusForm panel = new CommissionStatusForm(COMPONENT_ID, invoiceItem);
		tester.startComponentInPage(panel);
		// execute
		FormTester formTester = tester.newFormTester(COMPONENT_PATH+CommissionStatusForm.FORM);
		formTester.select(InvoiceItem.Properties.commissionStatus.name(), CommissionStatus.Verified.ordinal());
		tester.executeAjaxEvent(PATH, "onchange");
		// validate
		Assert.assertTrue(invoiceItem.isCommissionVerified());
		Assert.assertTrue(invoice.getSalesAmounts().isVerified());
		Assert.assertEquals(InvoiceStatus.Closed, invoice.getStatus());
		Mockito.verify(daoSupport).persist(invoiceItem);
	}

	@Test
	public void testCommissionIsNotVerified() {
		//setup
		CommissionStatusForm panel = new CommissionStatusForm(COMPONENT_ID, invoiceItem);
		tester.startComponentInPage(panel);
		// execute
		FormTester formTester = tester.newFormTester(COMPONENT_PATH+CommissionStatusForm.FORM);
		formTester.select(InvoiceItem.Properties.commissionStatus.name(), CommissionStatus.None.ordinal());
		tester.executeAjaxEvent(PATH, "onchange");
		// validate
		Assert.assertFalse(invoiceItem.isCommissionVerified());
		Assert.assertFalse(invoice.getSalesAmounts().isVerified());
		Assert.assertEquals(InvoiceStatus.Active, invoice.getStatus());
		Mockito.verify(daoSupport).persist(invoiceItem);
	}

	@Test(expected=UnauthorizedInstantiationException.class)
	public void testWithSystemUserAgent() {
		//setup
		usingAgentSystemUser();
		CommissionStatusForm panel = new CommissionStatusForm(COMPONENT_ID, invoiceItem);
		// execute
		tester.startComponentInPage(panel);
	}

}
