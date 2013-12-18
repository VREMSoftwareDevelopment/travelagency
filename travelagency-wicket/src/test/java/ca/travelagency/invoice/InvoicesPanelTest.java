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

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class InvoicesPanelTest extends BaseWicketTester {
	private static final String STATUS_PATH = InvoicesPanel.DATA_TABLE+":body:rows:1:cells:3:cell";
	private List<Invoice> invoices;
	private Criteria criteria1;
	private Criteria criteria2;

	private InvoiceFilter invoiceFilter;

	@Before
	public void setUp() {
		stubInvoiceDataProvider();
		invoiceFilter = new InvoiceFilter();
	}

	private void stubInvoiceDataProvider() {
		invoices = Lists.newArrayList(InvoiceHelper.makeInvoice(),
				InvoiceHelper.makeInvoice(),
				InvoiceHelper.makeInvoice(),
				InvoiceHelper.makeInvoice());

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
		// setup
		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, Model.of(invoiceFilter));
		// execute
		tester.startComponentInPage(invoicesPanel);
		// validate
		tester.assertComponent(COMPONENT_PATH+InvoicesPanel.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		Assert.assertEquals(4, getInvoiceDataTable().getItemCount());

		DataTable<Invoice, String> dataTable = getInvoiceDataTable();
		List<? extends IColumn<Invoice, String>> columns = dataTable.getColumns();
		Assert.assertEquals(11, columns.size());

		validateSortableColumn(DaoEntity.PROPERTY_ID, Invoice.Properties.invoiceNumber.name(), columns.get(0));
		validateNotSortableColumn(Invoice.PROPERTY_ID, columns.get(1));
		validateNotSortableColumn(Invoice.Properties.status.name(), columns.get(2));
		validateSortableColumn(Invoice.Properties.date.name(), columns.get(3));
		validateNotSortableColumn(InvoicesPanel.AGENT_PATH+DaoEntity.PROPERTY_NAME, columns.get(4));
		validateNotSortableColumn(Invoice.Properties.customer.name(), columns.get(5));
		validateNotSortableColumn(Invoice.Properties.invoiceTravelers.name(), columns.get(6));
		validateSortableColumn(Invoice.Properties.totalDueDate.name(), columns.get(7));
		validateNotSortableColumn(InvoicesPanel.SALES_AMOUNT_PATH+SalesAmounts.Properties.saleAsString.name(), columns.get(8));
		validateNotSortableColumn(InvoicesPanel.SALES_AMOUNT_PATH+SalesAmounts.Properties.paidAsString.name(), columns.get(9));
		validateNotSortableColumn(InvoicesPanel.SALES_AMOUNT_PATH+SalesAmounts.Properties.dueAsString.name(), columns.get(10));
	}

	@SuppressWarnings("unchecked")
	private DataTable<Invoice, String> getInvoiceDataTable() {
		return (DataTable<Invoice, String>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+InvoicesPanel.DATA_TABLE);
	}

	@Test
	public void testSpecialColumns() {
		// setup
		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, Model.of(invoiceFilter));
		// execute
		tester.startComponentInPage(invoicesPanel);
		// validate
		tester.assertComponent(COMPONENT_PATH+InvoicesPanel.DATA_TABLE+":body:rows:1:cells:1:cell", InvoicePageLink.class);
		tester.assertComponent(COMPONENT_PATH+InvoicesPanel.DATA_TABLE+":body:rows:1:cells:2:cell:", InvoiceViewLink.class);
		tester.assertComponent(COMPONENT_PATH+InvoicesPanel.DATA_TABLE+":body:rows:1:cells:6:cell", InvoiceCustomerPanel.class);
		tester.assertComponent(COMPONENT_PATH+InvoicesPanel.DATA_TABLE+":body:rows:1:cells:7:cell", InvoiceTravelersPanel.class);
	}

	@Test
	public void testInvoicesPanelUsingAgentSystemUser() {
		// setup
		usingAgentSystemUser();
		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, Model.of(invoiceFilter));
		// execute
		tester.startComponentInPage(invoicesPanel);
		// validate
		tester.assertComponent(COMPONENT_PATH+STATUS_PATH, Label.class);
		tester.assertModelValue(COMPONENT_PATH+STATUS_PATH, invoices.get(0).getStatus());
	}

	@Test
	public void testInvoicesPanelUsingAdminSystemUser() {
		// setup
		usingAdminSystemUser();
		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, Model.of(invoiceFilter));
		// execute
		tester.startComponentInPage(invoicesPanel);
		// validate
		tester.assertComponent(COMPONENT_PATH+STATUS_PATH, InvoiceStatusForm.class);
	}

	@Test
	public void testWithSystemUser() throws Exception {
		// setup
		SystemUser systemUser = invoices.get(0).getSystemUser();

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);

		invoiceFilter.setSystemUser(systemUser);

		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, Model.of(invoiceFilter));

		// execute
		tester.startComponentInPage(invoicesPanel);

		// validate
		DataTable<Invoice, String> dataTable = getInvoiceDataTable();
		List<? extends IColumn<Invoice, String>> columns = dataTable.getColumns();
		Assert.assertEquals(10, columns.size());

		validateSortableColumn(Invoice.PROPERTY_ID, Invoice.Properties.invoiceNumber.name(), columns.get(0));
		validateNotSortableColumn(Invoice.PROPERTY_ID, columns.get(1));
		validateNotSortableColumn(Invoice.Properties.status.name(), columns.get(2));
		validateSortableColumn(Invoice.Properties.date.name(), Invoice.Properties.date.name(), columns.get(3));
		validateNotSortableColumn(Invoice.Properties.customer.name(), columns.get(4));
		validateNotSortableColumn(Invoice.Properties.invoiceTravelers.name(), columns.get(5));
		validateSortableColumn(Invoice.Properties.totalDueDate.name(), Invoice.Properties.totalDueDate.name(), columns.get(6));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString.name(), columns.get(7));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.paidAsString.name(), columns.get(8));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.dueAsString.name(), columns.get(9));
	}

	@Test
	public void testWithCustomer() throws Exception {
		// setup
		Customer customer = invoices.get(0).getCustomer();

		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);

		invoiceFilter.setCustomer(customer);
		IModel<InvoiceFilter> model = Model.of(invoiceFilter);
		InvoicesPanel invoicesPanel = new InvoicesPanel(COMPONENT_ID, model);
		// execute
		tester.startComponentInPage(invoicesPanel);

		// validate
		DataTable<Invoice, String> dataTable = getInvoiceDataTable();
		List<? extends IColumn<Invoice, String>> columns = dataTable.getColumns();
		Assert.assertEquals(10, columns.size());

		validateSortableColumn(Invoice.PROPERTY_ID, Invoice.Properties.invoiceNumber.name(), columns.get(0));
		validateNotSortableColumn(Invoice.PROPERTY_ID, columns.get(1));
		validateNotSortableColumn(Invoice.Properties.status.name(), columns.get(2));
		validateSortableColumn(Invoice.Properties.date.name(), Invoice.Properties.date.name(), columns.get(3));
		validateNotSortableColumn(Invoice.Properties.systemUser.name() + Condition.SEPARATOR + DaoEntity.PROPERTY_NAME, columns.get(4));
		validateNotSortableColumn(Invoice.Properties.invoiceTravelers.name(), columns.get(5));
		validateSortableColumn(Invoice.Properties.totalDueDate.name(), Invoice.Properties.totalDueDate.name(), columns.get(6));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString.name(), columns.get(7));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.paidAsString.name(), columns.get(8));
		validateNotSortableColumn(Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.dueAsString.name(), columns.get(9));
	}
}
