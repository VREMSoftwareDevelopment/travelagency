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

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.CancelDepartureField;
import ca.travelagency.components.fields.ChangeDepartureField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.ProductField;
import ca.travelagency.components.fields.SupplierField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.utils.StringUtils;

public class ItemFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+ItemFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private Invoice invoice;
	private InvoiceItem invoiceItem;
	private ItemsPanel itemsPanel;

	@Before
	public void setUp() {
		stubItemDataProvider();

		Panel totalsPanel = new Panel("totalsPanel") {private static final long serialVersionUID = 1L;};
		itemsPanel = new ItemsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice), totalsPanel);
	}

	private void stubItemDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceItem = invoice.getInvoiceItems().get(0);

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceItem.class, invoiceItem.getId())).toReturn(invoiceItem);
	}

	@Test
	public void testComponents() {
		ItemFormPanel itemFormPanel = new ItemFormPanel(COMPONENT_ID, invoiceItem, itemsPanel);
		tester.startComponentInPage(itemFormPanel);

		tester.assertComponent(COMPONENT_PATH+ItemFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+ItemFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+ItemFormPanel.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+ItemFormPanel.CANCEL_BUTTON, CancelPanel.class);

		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.description, ProductField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.supplier, SupplierField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.commission, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.taxOnCommission, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.price, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.tax, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.cancelBeforeDeparture, CancelDepartureField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.changeBeforeDeparture, ChangeDepartureField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.changeAfterDeparture, ChangeDepartureField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.qty, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.bookingNumber, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.bookingDate, DateField.class);
		tester.assertComponent(FORM_PATH+InvoiceItem.Properties.commissionStatus, DropDownChoice.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testNewItemComponents() {
		ItemFormPanel itemFormPanel = new ItemFormPanel(COMPONENT_ID, itemsPanel);
		tester.startComponentInPage(itemFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.cancelBeforeDeparture, "Non-refundable");
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.changeBeforeDeparture, "Non-changeable");
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.changeAfterDeparture, "Non-changeable");
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.bookingDate, invoice.getDate());

		tester.assertInvisible(FORM_PATH+ItemFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testValues() {
		ItemFormPanel itemFormPanel = new ItemFormPanel(COMPONENT_ID, invoiceItem, itemsPanel);
		tester.startComponentInPage(itemFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.description, invoiceItem.getDescription());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.supplier, invoiceItem.getSupplier());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.commission, invoiceItem.getCommission());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.taxOnCommission, invoiceItem.getTaxOnCommission());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.price, invoiceItem.getPrice());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.tax, invoiceItem.getTax());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.cancelBeforeDeparture, invoiceItem.getCancelBeforeDeparture());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.changeBeforeDeparture, invoiceItem.getChangeBeforeDeparture());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.changeAfterDeparture, invoiceItem.getChangeAfterDeparture());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.qty, invoiceItem.getQty());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.bookingNumber, invoiceItem.getBookingNumber());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.bookingDate, invoiceItem.getBookingDate());
		tester.assertModelValue(FORM_PATH+InvoiceItem.Properties.commissionStatus, invoiceItem.getCommissionStatus());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceItem.class, invoiceItem.getId());
	}

	@Test
	public void testCommissionStatus() {
		ItemFormPanel itemFormPanel = new ItemFormPanel(COMPONENT_ID, invoiceItem, itemsPanel);
		tester.startComponentInPage(itemFormPanel);

		@SuppressWarnings("unchecked")
		DropDownChoice<CommissionStatus> component =
				(DropDownChoice<CommissionStatus>) tester.getComponentFromLastRenderedPage(FORM_PATH+InvoiceItem.Properties.commissionStatus);

		Assert.assertTrue(component.isEnabled());

		List<? extends CommissionStatus> choices = component.getChoices();
		Assert.assertEquals(2, choices.size());
		Assert.assertEquals(CommissionStatus.None, choices.get(0));
		Assert.assertEquals(CommissionStatus.Received, choices.get(1));
	}

	@Test
	public void testCommissionStatusVerified() {
		invoiceItem.setCommissionStatus(CommissionStatus.Verified);

		ItemFormPanel itemFormPanel = new ItemFormPanel(COMPONENT_ID, invoiceItem, itemsPanel);
		tester.startComponentInPage(itemFormPanel);

		@SuppressWarnings("unchecked")
		DropDownChoice<CommissionStatus> component =
				(DropDownChoice<CommissionStatus>) tester.getComponentFromLastRenderedPage(FORM_PATH+InvoiceItem.Properties.commissionStatus);

		Assert.assertFalse(component.isEnabled());

		List<? extends CommissionStatus> choices = component.getChoices();
		Assert.assertEquals(3, choices.size());
		Assert.assertEquals(CommissionStatus.None, choices.get(0));
		Assert.assertEquals(CommissionStatus.Received, choices.get(1));
		Assert.assertEquals(CommissionStatus.Verified, choices.get(2));
	}

	@Test
	public void testRequiredFields() {
		ItemFormPanel panel = new ItemFormPanel(COMPONENT_ID, itemsPanel);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+ItemFormPanel.FORM);
		formTester.setValue(InvoiceItem.Properties.price.name(), StringUtils.EMPTY);
		formTester.setValue(InvoiceItem.Properties.qty.name(), StringUtils.EMPTY);
		formTester.submit();

		tester.assertErrorMessages(new String [] {
			"'Description' is required.",
			"'Supplier' is required.",
			"'Price' is required.",
			"'Qty' is required."
		});
	}
}

