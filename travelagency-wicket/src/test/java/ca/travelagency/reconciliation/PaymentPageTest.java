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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SupplierField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.customer.Customer;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceCustomerPanel;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicePageLink;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTravelersPanel;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;


public class PaymentPageTest extends BaseWicketTester {
	private static final String RECONCILATION_FORM_PATH = PaymentPage.DATA_TABLE+":body:rows:1:cells:4:cell";

	private static final String PATH = PaymentPage.SEARCH_FORM+InvoicePage.PATH_SEPARATOR;

	private List<InvoicePayment> invoicePayments;
	private Criteria criteria1;
	private Criteria criteria2;

	@Before
	public void setUp() {
		invoicePayments = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments().first(),
			InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments().first(),
			InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments().first());

		for (InvoicePayment invoicePayment: invoicePayments) {
			Mockito.stub(daoSupport.find(InvoicePayment.class, invoicePayment.getId())).toReturn(invoicePayment);

			Invoice invoice = invoicePayment.getInvoice();
			Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);

			Customer customer = invoice.getCustomer();
			Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);
		}

		List<DaoEntity> results = new ArrayList<DaoEntity>();
		results.addAll(invoicePayments);

		criteria1 = Criteria.make(InvoicePayment.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) invoicePayments.size());

		criteria2 = Criteria.make(InvoicePayment.class).setOffset(0).setCount(invoicePayments.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}

	@Test
	public void testSearchForm() {
		tester.startPage(PaymentPage.class);
		tester.assertRenderedPage(PaymentPage.class);

		tester.assertComponent(PaymentPage.SEARCH_FORM, Form.class);

		tester.assertComponent(PATH+PaymentPage.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(PATH+PaymentPage.SEARCH_BUTTON, Button.class);
		tester.assertComponent(PATH+PaymentPage.CLEAR_BUTTON, Button.class);

		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.systemUser, SystemUserField.class);
		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.searchText, SupplierField.class);
		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.paymentDateFrom, DateField.class);
		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.paymentDateTo, DateField.class);
		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.paymentType, DropDownChoice.class);
		tester.assertComponent(PATH+InvoicePaymentFilter.Properties.reconciled, CheckBox.class);
	}

	@Test
	public void testDataTable() {
		tester.startPage(PaymentPage.class);
		tester.assertRenderedPage(PaymentPage.class);

		tester.assertComponent(PaymentPage.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoicePayment, String> dataTable = (DataTable<InvoicePayment, String>) tester.getComponentFromLastRenderedPage(PaymentPage.DATA_TABLE);

		Assert.assertEquals(3, dataTable.getItemCount());

		List<? extends IColumn<InvoicePayment, String>> columns = dataTable.getColumns();
		Assert.assertEquals(9, columns.size());

		validateNotSortableColumn(InvoicePayment.Properties.paymentType.name(), columns.get(0));
		validateNotSortableColumn(InvoicePayment.Properties.date.name(), columns.get(1));
		validateNotSortableColumn(InvoicePayment.Properties.amountAsString.name(), columns.get(2));
		validateNotSortableColumn(InvoicePayment.Properties.reconciled.name(), columns.get(3));
		validateNotSortableColumn(PaymentPage.INVOICE_PATH+Invoice.Properties.invoiceNumber.name(), columns.get(4));
		validateNotSortableColumn(PaymentPage.INVOICE_PATH+Invoice.PROPERTY_ID, columns.get(5));
		validateNotSortableColumn(PaymentPage.INVOICE_PATH+Invoice.Properties.date.name(), columns.get(6));
		validateNotSortableColumn(PaymentPage.INVOICE_PATH+Invoice.Properties.customer.name(), columns.get(7));
		validateNotSortableColumn(PaymentPage.INVOICE_PATH+Invoice.Properties.invoiceTravelers.name(), columns.get(8));

	}

	@Test
	public void testSpecialColumns() {
		tester.startPage(PaymentPage.class);
		tester.assertRenderedPage(PaymentPage.class);

		tester.assertComponent(PaymentPage.DATA_TABLE+":body:rows:1:cells:5:cell", InvoicePageLink.class);
		tester.assertComponent(PaymentPage.DATA_TABLE+":body:rows:1:cells:6:cell", InvoiceViewLink.class);
		tester.assertComponent(PaymentPage.DATA_TABLE+":body:rows:1:cells:8:cell", InvoiceCustomerPanel.class);
		tester.assertComponent(PaymentPage.DATA_TABLE+":body:rows:1:cells:9:cell", InvoiceTravelersPanel.class);

		tester.assertComponent(RECONCILATION_FORM_PATH, ReconciledForm.class);
	}

}
