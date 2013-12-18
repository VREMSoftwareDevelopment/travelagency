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
package ca.travelagency.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class CustomersPageTest extends BaseWicketTester {
	private static final String PATH = CustomersPage.SEARCH_FORM+CustomerPage.PATH_SEPARATOR;

	private List<Customer> customers;
	private Criteria criteria1;
	private Criteria criteria2;

	@Before
	public void setUp() {
		stubCustomerDataProvider();
		stubSystemUserField();

		tester.startPage(CustomersPage.class);
		tester.assertRenderedPage(CustomersPage.class);
	}

	private void stubSystemUserField() {
		stubSystemUserField(Lists.newArrayList(SystemUserHelper.makeSystemUser()));
	}

	private void stubCustomerDataProvider() {
		Customer customerOnHold = CustomerHelper.makeCustomer();
		customerOnHold.setStatus(CustomerStatus.OnHold);

		customers = Lists.newArrayList(
				CustomerHelper.makeCustomer(),
				CustomerHelper.makeCustomer(),
				CustomerHelper.makeCustomer(),
				CustomerHelper.makeCustomer(),
				customerOnHold);

		List<DaoEntity> results = new ArrayList<DaoEntity>();
		results.addAll(customers);

		criteria1 = Criteria.make(Customer.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) customers.size());

		criteria2 = Criteria.make(Customer.class).setOffset(0).setCount(customers.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);

		Mockito.stub(daoSupport.find(Customer.class, customers.get(0).getId())).toReturn(customers.get(0));
		Mockito.stub(daoSupport.find(Customer.class, customers.get(1).getId())).toReturn(customers.get(1));
		Mockito.stub(daoSupport.find(Customer.class, customers.get(2).getId())).toReturn(customers.get(2));
		Mockito.stub(daoSupport.find(Customer.class, customers.get(3).getId())).toReturn(customers.get(3));
		Mockito.stub(daoSupport.find(Customer.class, customers.get(4).getId())).toReturn(customers.get(4));
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}

	@Test
	public void testCustomerDataTable() {
		tester.assertComponent(CustomersPage.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		Assert.assertEquals(5, getCustomerDataTable().getItemCount());
		validateColumns();
	}

	@SuppressWarnings("unchecked")
	private DataTable<Customer, String> getCustomerDataTable() {
		return (DataTable<Customer, String>) tester.getComponentFromLastRenderedPage(CustomersPage.DATA_TABLE);
	}

	private void validateColumns() {
		DataTable<Customer, String> dataTable = getCustomerDataTable();
		List<? extends IColumn<Customer, String>> columns = dataTable.getColumns();
		Assert.assertEquals(9, columns.size());

		validateSortableColumn(Customer.Properties.lastName.name(), DaoEntity.PROPERTY_NAME, columns.get(0));
		validateNotSortableColumn(Customer.Properties.status.name(), columns.get(1));
		validateSortableColumn(Customer.Properties.companyName.name(), columns.get(2));
		validateNotSortableColumn(Customer.Properties.dateOfBirth.name(), columns.get(3));
		validateNotSortableColumn(Customer.Properties.address.name(), columns.get(4));
		validateNotSortableColumn(Customer.Properties.email.name(), columns.get(5));
		validateNotSortableColumn(Customer.Properties.primaryPhone.name(), columns.get(6));
		validateNotSortableColumn(Customer.Properties.secondaryPhone.name(), columns.get(7));
		validateNotSortableColumn(DaoEntity.PROPERTY_ID, columns.get(8));
	}

	@Test
	public void testCreateCustomer() {
		tester.assertComponent(CustomersPage.CREATE, Link.class);

		tester.clickLink(CustomersPage.CREATE);

		tester.assertRenderedPage(CustomerPage.class);
	}

	@Test
	public void testModifyCustomer() {
		tester.startPage(CustomersPage.class);
		tester.assertRenderedPage(CustomersPage.class);

		String path = CustomersPage.DATA_TABLE+":body:rows:1:cells:1:cell:"+CustomersPage.GO_TO_CUSTOMER;

		tester.assertComponent(path, Link.class);
		tester.assertLabel(path + ":" + CustomersPage.GO_TO_CUSTOMER_LABEL, customers.get(0).getName());

	 	tester.clickLink(path);
		tester.assertRenderedPage(CustomerPage.class);
	}

	@Test
	public void testAddInvoiceLink() {
		tester.startPage(CustomersPage.class);
		tester.assertRenderedPage(CustomersPage.class);

		String pathInvisible = CustomersPage.DATA_TABLE+":body:rows:5:cells:9:cell:"+CustomersPage.ADD_INVOICE;
		tester.assertInvisible(pathInvisible);

		String pathVisible = CustomersPage.DATA_TABLE+":body:rows:1:cells:9:cell:"+CustomersPage.ADD_INVOICE;
		tester.assertComponent(pathVisible, Link.class);
		tester.assertVisible(pathVisible);

	 	tester.clickLink(pathVisible);
		tester.assertRenderedPage(InvoicePage.class);
	}

	@Test
	public void testSearchForm() throws Exception {
		String value = "Last Name";

		tester.assertComponent(CustomersPage.SEARCH_FORM, Form.class);
		tester.assertComponent(PATH+CustomersPage.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(PATH+CustomersPage.SEARCH_BUTTON, Button.class);
		tester.assertComponent(PATH+CustomersPage.CLEAR_BUTTON, Button.class);

		tester.assertComponent(PATH+CustomerFilter.Properties.searchText, TextField.class);
		tester.assertComponent(PATH+CustomerFilter.Properties.status, DropDownChoice.class);
		tester.assertComponent(PATH+CustomerFilter.Properties.systemUser, SystemUserField.class);

		FormTester formTester = tester.newFormTester(CustomersPage.SEARCH_FORM);
		formTester.setValue(CustomerFilter.Properties.searchText.name(), value);

		formTester.submit(CustomersPage.SEARCH_BUTTON);

		tester.assertRenderedPage(CustomersPage.class);
		tester.assertModelValue(
			CustomersPage.SEARCH_FORM+CustomersPage.PATH_SEPARATOR+CustomerFilter.Properties.searchText, value);
	}

	@Test
	public void testSystemUserChoiceIsNotAvailableForAgents() throws Exception {
		usingAgentSystemUser();

		tester.startPage(CustomersPage.class);
		tester.assertRenderedPage(CustomersPage.class);

		tester.assertInvisible(PATH+CustomerFilter.Properties.systemUser);
	}

}
