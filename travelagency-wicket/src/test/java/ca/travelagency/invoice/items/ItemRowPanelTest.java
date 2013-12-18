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
package ca.travelagency.invoice.items;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;

public class ItemRowPanelTest extends BaseWicketTester {
	private Invoice invoice;
	private ItemsPanel itemsPanel;
	private InvoiceItem invoiceItem;

	@Before
	public void setUp() {
		stubItemDataProvider();

		Panel totalsPanel = new Panel("totalsPanel") {private static final long serialVersionUID = 1L;};
		itemsPanel = new ItemsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice), totalsPanel);
	}

	private void stubItemDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoice.addInvoiceItem(InvoiceHelper.makeItem());
		invoice.addInvoiceItem(InvoiceHelper.makeItem());

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		invoiceItem = invoice.getInvoiceItems().get(1);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		// middle item
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.description.name(), invoiceItem.getDescription());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.supplier.name(), invoiceItem.getSupplier());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.commissionAsString.name(), invoiceItem.getCommissionAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.commissionAsString.name(),
			invoiceItem.getSalesAmounts().getCommissionAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.taxOnCommissionAsString.name(), invoiceItem.getTaxOnCommissionAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.priceAsString.name(), invoiceItem.getPriceAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.taxAsString.name(), invoiceItem.getTaxAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.cancelBeforeDeparture.name(), invoiceItem.getCancelBeforeDeparture());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.changeBeforeDeparture.name(), invoiceItem.getChangeBeforeDeparture());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.changeAfterDeparture.name(), invoiceItem.getChangeAfterDeparture());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.qty.name(), invoiceItem.getQty());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString.name(),
			invoiceItem.getSalesAmounts().getSaleAsString());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.bookingNumber.name(), invoiceItem.getBookingNumber());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.bookingDate.name(), invoiceItem.getBookingDate());
		tester.assertModelValue(COMPONENT_PATH+InvoiceItem.Properties.commissionStatus.name(), invoiceItem.getCommissionStatus());

		tester.assertComponent(COMPONENT_PATH+ItemRowPanel.EDIT_BUTTON, EditPanel.class);
		tester.assertComponent(COMPONENT_PATH+ItemRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testMoveUpAndDownFirstRow() {
		invoiceItem = invoice.getInvoiceItems().get(0);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_DOWN);
		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.MOVE_UP);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testMoveUpAndDownSecondRow() {
		invoiceItem = invoice.getInvoiceItems().get(1);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_DOWN);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_UP);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testMoveUpAndDownLastRow() {
		invoiceItem = invoice.getInvoiceItems().get(2);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.MOVE_DOWN);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_UP);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testWithNotActiveInvoice() {
		invoice.setStatus(InvoiceStatus.Closed);

		invoiceItem = invoice.getInvoiceItems().get(1);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.DELETE_BUTTON);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_DOWN);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_UP);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testWithVerifiedInvoiceItem() {
		invoiceItem = invoice.getInvoiceItems().get(1);
		invoiceItem.setCommissionStatus(CommissionStatus.Verified);
		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem);
		Mockito.when(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).thenReturn(invoiceItem);

		ItemRowPanel itemRowPanel = new ItemRowPanel(COMPONENT_ID, model, itemsPanel);
		tester.startComponentInPage(itemRowPanel);

		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+ItemRowPanel.DELETE_BUTTON);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_DOWN);
		tester.assertVisible(COMPONENT_PATH+ItemRowPanel.MOVE_UP);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}
}
