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
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicePayment;

public class ReconciledFormTest extends BaseWicketTester {
	private static final String PATH = COMPONENT_PATH+ReconciledForm.FORM+InvoicePage.PATH_SEPARATOR+InvoicePayment.Properties.reconciled;
	private InvoicePayment invoicePayment;

	@Before
	public void setUp() {
		invoicePayment = InvoiceHelper.makePayment();

		Mockito.stub(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).toReturn(invoicePayment);
	}

	@Test
	public void testComponents() {
		//setup
		ReconciledForm panel = new ReconciledForm(COMPONENT_ID, invoicePayment);
		// execute
		tester.startComponentInPage(panel);
		// validate
		tester.assertComponent(COMPONENT_PATH+ReconciledForm.FORM, Form.class);
		tester.assertComponent(PATH, CheckBox.class);
		tester.assertModelValue(PATH, invoicePayment.isReconciled());
	}

	@Test
	public void testReconciled() {
		//setup
		ReconciledForm panel = new ReconciledForm(COMPONENT_ID, invoicePayment);
		tester.startComponentInPage(panel);
		// execute
		FormTester formTester = tester.newFormTester(COMPONENT_PATH+ReconciledForm.FORM);
		formTester.setValue(InvoicePayment.Properties.reconciled.name(), true);
		tester.executeAjaxEvent(PATH, "onchange");
		// validate
		Assert.assertTrue(invoicePayment.isReconciled());
		Mockito.verify(daoSupport).persist(invoicePayment);
	}

	@Test
	public void testNotReconciled() {
		//setup
		ReconciledForm panel = new ReconciledForm(COMPONENT_ID, invoicePayment);
		tester.startComponentInPage(panel);
		// execute
		FormTester formTester = tester.newFormTester(COMPONENT_PATH+ReconciledForm.FORM);
		formTester.setValue(InvoicePayment.Properties.reconciled.name(), false);
		tester.executeAjaxEvent(PATH, "onchange");
		// validate
		Assert.assertFalse(invoicePayment.isReconciled());
		Mockito.verify(daoSupport).persist(invoicePayment);
	}

	@Test(expected=UnauthorizedInstantiationException.class)
	public void testWithSystemUserAgent() {
		//setup
		usingAgentSystemUser();
		ReconciledForm panel = new ReconciledForm(COMPONENT_ID, invoicePayment);
		// execute
		tester.startComponentInPage(panel);
	}

}
