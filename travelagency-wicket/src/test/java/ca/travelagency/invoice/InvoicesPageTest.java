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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;


public class InvoicesPageTest extends BaseWicketTester {
	private static final String PATH = InvoicesPage.SEARCH_FORM+InvoicePage.PATH_SEPARATOR;

	private List<Invoice> invoices;
	private Criteria criteria1;
	private Criteria criteria2;
	private SystemUser systemUser;

	@Before
	public void setUp() {
		stubInvoiceDataProvider();
		stubSystemUserField();
	}

	private void stubSystemUserField() {
		systemUser = invoices.get(0).getSystemUser();
		stubSystemUserField(Lists.newArrayList(
			systemUser,	invoices.get(1).getSystemUser(), invoices.get(2).getSystemUser()
		));
	}

	private void stubInvoiceDataProvider() {
		invoices = Lists.newArrayList(InvoiceHelper.makeInvoice(), InvoiceHelper.makeInvoice(), InvoiceHelper.makeInvoice());

		for (Invoice invoice: invoices) {
			Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
			Customer customer = invoice.getCustomer();
			Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);
		}

		List<DaoEntity> results = new ArrayList<DaoEntity>();
		results.addAll(invoices);

		criteria1 = Criteria.make(Invoice.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) invoices.size());

		criteria2 = Criteria.make(Invoice.class).setOffset(0).setCount(invoices.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}

	@Test
	public void testInvoiceDataTable() {
		tester.startPage(InvoicesPage.class);
		tester.assertRenderedPage(InvoicesPage.class);

		tester.assertComponent(InvoicesPage.INVOICES, InvoicesPanel.class);
	}

	@Test
	public void testSearchForm() throws Exception {
		tester.startPage(InvoicesPage.class);
		tester.assertRenderedPage(InvoicesPage.class);

		tester.assertComponent(InvoicesPage.SEARCH_FORM, Form.class);

		tester.assertComponent(PATH+InvoicesPage.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(PATH+InvoicesPage.SEARCH_BUTTON, Button.class);
		tester.assertComponent(PATH+InvoicesPage.CLEAR_BUTTON, Button.class);

		tester.assertComponent(PATH+InvoiceFilter.Properties.searchText, TextField.class);
		tester.assertComponent(PATH+InvoiceFilter.Properties.invoiceDateFrom, DateField.class);
		tester.assertComponent(PATH+InvoiceFilter.Properties.invoiceDateTo, DateField.class);
		tester.assertComponent(PATH+InvoiceFilter.Properties.showUnpaidInvoicesOnly, CheckBox.class);
		tester.assertComponent(PATH+InvoiceFilter.Properties.status, DropDownChoice.class);
		tester.assertComponent(PATH+InvoiceFilter.Properties.systemUser, SystemUserField.class);

		FormTester formTester = tester.newFormTester(InvoicesPage.SEARCH_FORM);
		formTester.select(Invoice.Properties.systemUser.name(), 0);

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);

		formTester.submit(InvoicesPage.SEARCH_BUTTON);

		tester.assertRenderedPage(InvoicesPage.class);
		tester.assertModelValue(
			InvoicesPage.SEARCH_FORM+InvoicesPage.PATH_SEPARATOR+Invoice.Properties.systemUser, systemUser);
	}

	@Test
	public void testSystemUserChoiceIsNotAvailableForAgents() throws Exception {
		usingAgentSystemUser();

		tester.startPage(InvoicesPage.class);
		tester.assertRenderedPage(InvoicesPage.class);

		tester.assertInvisible(PATH+InvoiceFilter.Properties.systemUser);
	}
}
