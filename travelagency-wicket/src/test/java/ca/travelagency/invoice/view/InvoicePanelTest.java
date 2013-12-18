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
package ca.travelagency.invoice.view;

import java.util.List;

import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.CheckMarkPanel;
import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerPanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;

public class InvoicePanelTest extends BaseWicketTester {
	private InvoicePanel fixture;
	private Invoice invoice;
	private InvoiceItem invoiceItem;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceItem = invoice.getInvoiceItems().get(0);

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(Customer.class, invoice.getCustomer().getId())).toReturn(invoice.getCustomer());
		Mockito.stub(daoSupport.find(InvoiceDestination.class, invoice.getInvoiceDestinations().first().getId())).toReturn(invoice.getInvoiceDestinations().first());
		Mockito.stub(daoSupport.find(InvoiceTraveler.class, invoice.getInvoiceTravelers().first().getId())).toReturn(invoice.getInvoiceTravelers().first());
		Mockito.stub(daoSupport.find(InvoiceNote.class, invoice.getInvoiceNotes().first().getId())).toReturn(invoice.getInvoiceNotes().first());
		Mockito.stub(daoSupport.find(InvoicePayment.class, invoice.getInvoicePayments().first().getId())).toReturn(invoice.getInvoicePayments().first());
		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).toReturn(invoiceItem);

		fixture = new InvoicePanel(COMPONENT_ID, Model.of(invoice));
	}

	@Test
	public void testInvoiceHeader() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceNumber.name(), Label.class);
		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.date.name(), DateLabel.class);
		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.billCompany.name(), CheckMarkPanel.class);
		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.totalDueDate.name(), DateLabel.class);
		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.status.name(), Label.class);
		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.systemUser.name()+"."+SystemUser.PROPERTY_NAME, Label.class);

		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.status.name(), invoice.getStatus());
		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.invoiceNumber.name(), invoice.getInvoiceNumber());
		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.date.name(), invoice.getDate());
		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.totalDueDate.name(), invoice.getTotalDueDate());
		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.systemUser.name()+"."+SystemUser.PROPERTY_NAME, invoice.getSystemUser().getName());
		tester.assertModelValue(COMPONENT_PATH+Invoice.Properties.systemUser.name()+"."+SystemUser.PROPERTY_NAME, invoice.getSystemUser().getName());
	}

	@Test
	public void testCustomerPanel() {
		tester.startComponentInPage(fixture);
		tester.assertComponent(COMPONENT_PATH+InvoicePanel.CUSTOMER_PANEL, CustomerPanel.class);
	}

	@Test
	public void testInvoiceTotalsPanel() {
		tester.startComponentInPage(fixture);
		tester.assertComponent(COMPONENT_PATH+InvoicePanel.TOTALS_PANEL, InvoiceTotalsPanel.class);
	}

	@Test
	public void testInvoiceDestinations() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceDestinations.name(), DataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoiceDestination, String> dataTable =
			(DataTable<InvoiceDestination, String>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+Invoice.Properties.invoiceDestinations.name());

		List<? extends IColumn<InvoiceDestination, String>> columns = dataTable.getColumns();
		Assert.assertEquals(4, columns.size());

		validateNotSortableColumn(InvoiceDestination.Properties.departurePlace.name(), columns.get(0));
		validateNotSortableColumn(InvoiceDestination.Properties.departureDate.name(), columns.get(1));
		validateNotSortableColumn(InvoiceDestination.Properties.arrivalPlace.name(), columns.get(2));
		validateNotSortableColumn(InvoiceDestination.Properties.arrivalDate.name(), columns.get(3));
	}

	@Test
	public void testInvoiceTravelers() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceTravelers.name(), DataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoiceTraveler, String> dataTable =
			(DataTable<InvoiceTraveler, String>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+Invoice.Properties.invoiceTravelers.name());

		List<? extends IColumn<InvoiceTraveler, String>> columns = dataTable.getColumns();
		Assert.assertEquals(4, columns.size());

		validateNotSortableColumn(InvoiceTraveler.PROPERTY_NAME, columns.get(0));
		validateNotSortableColumn(InvoiceTraveler.Properties.documentType.name(), columns.get(1));
		validateNotSortableColumn(InvoiceTraveler.Properties.documentNumber.name(), columns.get(2));
		validateNotSortableColumn(InvoiceTraveler.Properties.dateOfBirth.name(), columns.get(3));
	}

	@Test
	public void testInvoiceNotes() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceNotes.name(), DataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoiceNote, String> dataTable =
			(DataTable<InvoiceNote, String>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+Invoice.Properties.invoiceNotes.name());

		List<? extends IColumn<InvoiceNote, String>> columns = dataTable.getColumns();
		Assert.assertEquals(3, columns.size());

		validateNotSortableColumn(InvoiceNote.Properties.text.name(), columns.get(0));
		validateNotSortableColumn(InvoiceNote.Properties.privateNote.name(), columns.get(1));
		validateNotSortableColumn(InvoiceNote.Properties.date.name(), columns.get(2));
	}

	@Test
	public void testInvoicePayments() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoicePayments.name(), DataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<InvoicePayment, String> dataTable =
			(DataTable<InvoicePayment, String>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+Invoice.Properties.invoicePayments.name());

		List<? extends IColumn<InvoicePayment, String>> columns = dataTable.getColumns();
		Assert.assertEquals(4, columns.size());

		validateNotSortableColumn(InvoicePayment.Properties.date.name(), columns.get(0));
		validateNotSortableColumn(InvoicePayment.Properties.amountAsString.name(), columns.get(1));
		validateNotSortableColumn(InvoicePayment.Properties.reconciled.name(), columns.get(2));
		validateNotSortableColumn(InvoicePayment.Properties.paymentType.name(), columns.get(3));
	}

	@Test
	public void testInvoiceItems() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceItems.name(), ListView.class);

		String path = COMPONENT_PATH+Invoice.Properties.invoiceItems.name()+BasePage.PATH_SEPARATOR+"0"+BasePage.PATH_SEPARATOR;
		tester.assertModelValue(path+InvoiceItem.Properties.description, invoiceItem.getDescription());
		tester.assertModelValue(path+InvoiceItem.Properties.supplier, invoiceItem.getSupplier());
		tester.assertModelValue(path+InvoiceItem.Properties.commissionAsString, invoiceItem.getCommissionAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.commissionAsString,
			invoiceItem.getSalesAmounts().getCommissionAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.taxOnCommissionAsString, invoiceItem.getTaxOnCommissionAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.priceAsString, invoiceItem.getPriceAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.taxAsString, invoiceItem.getTaxAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.cancelBeforeDeparture, invoiceItem.getCancelBeforeDeparture());
		tester.assertModelValue(path+InvoiceItem.Properties.changeBeforeDeparture, invoiceItem.getChangeBeforeDeparture());
		tester.assertModelValue(path+InvoiceItem.Properties.changeAfterDeparture, invoiceItem.getChangeAfterDeparture());
		tester.assertModelValue(path+InvoiceItem.Properties.qty, invoiceItem.getQty());
		tester.assertModelValue(path+InvoiceItem.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString,
			invoiceItem.getSalesAmounts().getSaleAsString());
		tester.assertModelValue(path+InvoiceItem.Properties.bookingNumber, invoiceItem.getBookingNumber());
		tester.assertModelValue(path+InvoiceItem.Properties.bookingDate, invoiceItem.getBookingDate());
		tester.assertModelValue(path+InvoiceItem.Properties.commissionStatus, invoiceItem.getCommissionStatus());
	}
}
