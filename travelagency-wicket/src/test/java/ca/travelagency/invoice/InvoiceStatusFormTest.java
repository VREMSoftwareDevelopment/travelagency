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

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;

public class InvoiceStatusFormTest extends BaseWicketTester {
	private static final String PATH = COMPONENT_PATH+InvoiceStatusForm.FORM+InvoicePage.PATH_SEPARATOR+Invoice.Properties.status;
	private Invoice invoice;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoice();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		InvoiceStatusForm panel = new InvoiceStatusForm(COMPONENT_ID, invoice);
		tester.startComponentInPage(panel);

		tester.assertComponent(COMPONENT_PATH+InvoiceStatusForm.FORM, Form.class);

		tester.assertComponent(PATH, DropDownChoice.class);
		tester.assertModelValue(PATH, invoice.getStatus());
	}

	@Test
	public void testStatusChanges() {
		InvoiceStatusForm panel = new InvoiceStatusForm(COMPONENT_ID, invoice);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+InvoiceStatusForm.FORM);
		formTester.select(Invoice.Properties.status.name(), 0);

		tester.executeAjaxEvent(PATH, "onchange");

		Mockito.verify(daoSupport).persist(invoice);
	}

	@Test(expected=UnauthorizedInstantiationException.class)
	public void testWithSystemUserAgent() {
		usingAgentSystemUser();

		InvoiceStatusForm panel = new InvoiceStatusForm(COMPONENT_ID, invoice);
		tester.startComponentInPage(panel);
	}

}
