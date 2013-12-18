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
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestinationsPanel;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicePageLink;
import ca.travelagency.invoice.InvoiceTravelersPanel;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;


public class CommissionPageTest extends BaseWicketTester {
	private static final String COMMISSION_FORM_PATH = CommissionPage.DATA_TABLE+":body:rows:1:cells:11:cell";

	private static final String PATH = CommissionPage.SEARCH_FORM+InvoicePage.PATH_SEPARATOR;

	private List<InvoiceItem> invoiceItems;
	private Criteria criteria1;
	private Criteria criteria2;

	@Before
	public void setUp() {
		invoiceItems = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0),
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0),
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0));

		List<DaoEntity> results = new ArrayList<DaoEntity>();
		results.addAll(invoiceItems);

		criteria1 = Criteria.make(InvoiceItem.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) invoiceItems.size());

		criteria2 = Criteria.make(InvoiceItem.class).setOffset(0).setCount(invoiceItems.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);

		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItems.get(0).getId())).toReturn(invoiceItems.get(0));
		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItems.get(1).getId())).toReturn(invoiceItems.get(1));
		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItems.get(2).getId())).toReturn(invoiceItems.get(2));

		Mockito.stub(daoSupport.find(Invoice.class, invoiceItems.get(0).getInvoice().getId())).toReturn(invoiceItems.get(0).getInvoice());
		Mockito.stub(daoSupport.find(Invoice.class, invoiceItems.get(1).getInvoice().getId())).toReturn(invoiceItems.get(1).getInvoice());
		Mockito.stub(daoSupport.find(Invoice.class, invoiceItems.get(2).getInvoice().getId())).toReturn(invoiceItems.get(2).getInvoice());
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}

	@Test
	public void testSearchForm() {
		tester.startPage(CommissionPage.class);
		tester.assertRenderedPage(CommissionPage.class);

		tester.assertComponent(CommissionPage.SEARCH_FORM, Form.class);

		tester.assertComponent(PATH+CommissionPage.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(PATH+CommissionPage.SEARCH_BUTTON, Button.class);
		tester.assertComponent(PATH+CommissionPage.CLEAR_BUTTON, Button.class);

		tester.assertComponent(PATH+InvoiceItemFilter.Properties.systemUser, SystemUserField.class);
		tester.assertComponent(PATH+InvoiceItemFilter.Properties.searchText, SupplierField.class);
		tester.assertComponent(PATH+InvoiceItemFilter.Properties.invoiceDateFrom, DateField.class);
		tester.assertComponent(PATH+InvoiceItemFilter.Properties.invoiceDateTo, DateField.class);
		tester.assertComponent(PATH+InvoiceItemFilter.Properties.commissionStatus, DropDownChoice.class);
	}

	@Test
	public void testDataTable() {
		tester.startPage(CommissionPage.class);
		tester.assertRenderedPage(CommissionPage.class);

		tester.assertComponent(CommissionPage.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoiceItem, String> dataTable = (DataTable<InvoiceItem, String>) tester.getComponentFromLastRenderedPage(CommissionPage.DATA_TABLE);

		Assert.assertEquals(3, dataTable.getItemCount());

		List<? extends IColumn<InvoiceItem, String>> columns = dataTable.getColumns();
		Assert.assertEquals(13, columns.size());

		validateNotSortableColumn(CommissionPage.INVOICE_PATH+Invoice.Properties.invoiceNumber.name(), columns.get(0));
		validateNotSortableColumn(InvoiceItem.PROPERTY_ID, columns.get(1));
		validateSortableColumn(InvoiceItem.Properties.supplier.name(), columns.get(2));
		validateSortableColumn(InvoiceItem.Properties.description.name(), columns.get(3));
		validateNotSortableColumn(CommissionPage.INVOICE_PATH+Invoice.Properties.invoiceTravelers.name(), columns.get(4));
		validateNotSortableColumn(InvoiceItem.Properties.saleAsString.name(), columns.get(5));
		validateNotSortableColumn(InvoiceItem.Properties.qty.name(), columns.get(6));
		validateNotSortableColumn(CommissionPage.SALES_AMOUNTS_PATH+SalesAmounts.Properties.saleAsString.name(), columns.get(7));
		validateNotSortableColumn(CommissionPage.SALES_AMOUNTS_PATH+SalesAmounts.Properties.taxOnCommissionAsString.name(), columns.get(8));
		validateNotSortableColumn(CommissionPage.SALES_AMOUNTS_PATH+SalesAmounts.Properties.commissionAsString.name(), columns.get(9));
		validateNotSortableColumn(InvoiceItem.Properties.commissionStatus.name(), columns.get(10));
		validateNotSortableColumn(InvoiceItem.Properties.bookingDate.name(), columns.get(11));
		validateNotSortableColumn(CommissionPage.INVOICE_PATH+Invoice.Properties.invoiceDestinations.name(), columns.get(12));
	}

	@Test
	public void testSpecialColumns() {
		tester.startPage(CommissionPage.class);
		tester.assertRenderedPage(CommissionPage.class);

		tester.assertComponent(CommissionPage.DATA_TABLE+":body:rows:1:cells:1:cell", InvoicePageLink.class);
		tester.assertComponent(CommissionPage.DATA_TABLE+":body:rows:1:cells:2:cell", InvoiceViewLink.class);
		tester.assertComponent(CommissionPage.DATA_TABLE+":body:rows:1:cells:5:cell", InvoiceTravelersPanel.class);
		tester.assertComponent(CommissionPage.DATA_TABLE+":body:rows:1:cells:13:cell", InvoiceDestinationsPanel.class);

		tester.assertComponent(COMMISSION_FORM_PATH, CommissionStatusForm.class);
	}

}
