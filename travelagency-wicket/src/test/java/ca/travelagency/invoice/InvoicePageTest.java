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

import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerPanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.destinations.DestinationsPanel;
import ca.travelagency.invoice.items.ItemsPanel;
import ca.travelagency.invoice.notes.NotesPanel;
import ca.travelagency.invoice.payments.PaymentsPanel;
import ca.travelagency.invoice.travelers.TravelersPanel;
import ca.travelagency.invoice.view.InvoiceTotalsPanel;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class InvoicePageTest extends BaseWicketTester {
	private static final String PATH = InvoicePage.FORM+InvoicePage.PATH_SEPARATOR;
	private static final String RESET_PATH = PATH+InvoicePage.RESET_BUTTON+BasePage.PATH_SEPARATOR+ResetPanel.RESET_BUTTON;
	private static final String SAVE_PATH = PATH+InvoicePage.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;
	private static final String PRINT_PATH = PATH+InvoicePage.PREVIEW_BUTTON;

	private static final String INVOICE_NOTE1 = "InvoiceNote1";
	private static final String INVOICE_NOTE2 = "InvoiceNote2";

	private Invoice invoice;
	private SystemUser dropDownSystemUser;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoice();
		dropDownSystemUser = invoice.getSystemUser();
		stubSystemUserField(Lists.newArrayList(dropDownSystemUser));

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(Customer.class, invoice.getCustomer().getId())).toReturn(invoice.getCustomer());

		Mockito.stub(parameterRepository.getDefaultDeparturePlace()).toReturn("DEPARTURE_PLACE");
	}

	@Test
	public void testPageComponents() {
		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		validateComponents();
		validateValues(invoice);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(Customer.class, invoice.getCustomer().getId());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(Invoice.class, invoice.getId());
	}

	private void validateValues(Invoice invoice) {
		tester.assertModelValue(PATH+Invoice.Properties.status, invoice.getStatus());
		tester.assertModelValue(PATH+Invoice.Properties.invoiceNumber, invoice.getInvoiceNumber());
		tester.assertModelValue(PATH+Invoice.Properties.date, invoice.getDate());
		tester.assertModelValue(PATH+Invoice.Properties.billCompany, invoice.isBillCompany());
		tester.assertModelValue(PATH+Invoice.Properties.totalDueDate, invoice.getTotalDueDate());
		tester.assertModelValue(PATH+Invoice.Properties.systemUser, invoice.getSystemUser());
	}

	private void validateComponents() {
		tester.assertComponent(InvoicePage.FORM, Form.class);

		tester.assertComponent(PATH+InvoicePage.FEEDBACK, ComponentFeedbackPanel.class);

		tester.assertComponent(PATH+InvoicePage.SAVE_BUTTON, SavePanel.class);
		tester.assertComponent(PATH+InvoicePage.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(PATH+InvoicePage.PREVIEW_BUTTON, Link.class);

		tester.assertComponent(PATH+InvoicePage.CUSTOMER_PANEL, CustomerPanel.class);
		tester.assertComponent(PATH+InvoicePage.TOTALS_PANEL, InvoiceTotalsPanel.class);

		tester.assertComponent(PATH+Invoice.Properties.invoiceNumber, TextField.class);
		tester.assertComponent(PATH+Invoice.Properties.date, DateField.class);
		tester.assertComponent(PATH+Invoice.Properties.billCompany, CheckBox.class);
		tester.assertComponent(PATH+Invoice.Properties.totalDueDate, DateField.class);
		tester.assertComponent(PATH+Invoice.Properties.status, DropDownChoice.class);
		tester.assertComponent(PATH+Invoice.Properties.systemUser, SystemUserField.class);

		tester.assertDisabled(PATH+Invoice.Properties.invoiceNumber);

		tester.assertComponent(InvoicePage.DETAILS, AjaxTabbedPanel.class);

		@SuppressWarnings("unchecked")
		AjaxTabbedPanel<? extends ITab> ajaxTabbedPanel= (AjaxTabbedPanel<? extends ITab>) tester.getComponentFromLastRenderedPage(InvoicePage.DETAILS);

		List<? extends ITab> tabs = ajaxTabbedPanel.getTabs();
		Assert.assertEquals(5, tabs.size());
		validateTabPanel(tabs.get(0), DestinationsPanel.class);
		validateTabPanel(tabs.get(1), TravelersPanel.class);
		validateTabPanel(tabs.get(2), ItemsPanel.class);
		validateTabPanel(tabs.get(3), NotesPanel.class);
		validateTabPanel(tabs.get(4), PaymentsPanel.class);
	}

	private void validateTabPanel(ITab tab, Class<?> clazz) {
		Assert.assertEquals(clazz.getName(), tab.getPanel("containerId").getClass().getName());
	}

	@Test
	public void testExistingInvoiceWithSubmit() {
		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.totalDueDate.name()+":date",
				DateUtils.formatDateShort(invoice.getTotalDueDate()));
		formTester.select(Invoice.Properties.status.name(), 0);
		formTester.select(Invoice.Properties.systemUser.name(), 0);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {invoice.getInvoiceNumber()+" saved."});

		tester.assertRenderedPage(InvoicePage.class);

		Mockito.verify(daoSupport).persist(invoice);
	}

	@Test
	public void testExistingInvoiceWithPreview() {
		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.totalDueDate.name()+":date",
				DateUtils.formatDateShort(invoice.getTotalDueDate()));
		formTester.select(Invoice.Properties.status.name(), 0);
		formTester.select(Invoice.Properties.systemUser.name(), 0);

		tester.clickLink(PRINT_PATH);

		tester.assertRenderedPage(InvoicePreviewPage.class);
	}

	@Test
	public void testNewInvoiceWithSubmitWithAgentSystemUser() {
		Mockito.when(parameterRepository.getDefaultInvoiceNotes())
			.thenReturn(Lists.newArrayList(INVOICE_NOTE1, INVOICE_NOTE2));

		usingAgentSystemUser();
		Customer customer = invoice.getCustomer();
		tester.startPage(makeInvoicePage(customer));
		tester.assertRenderedPage(InvoicePage.class);

		tester.isInvisible(PATH+InvoicePage.PREVIEW_BUTTON);
		tester.isInvisible(PATH+Invoice.Properties.invoiceNumber);
		tester.isInvisible(InvoicePage.DETAILS);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.totalDueDate.name()+":date",
				DateUtils.formatDateShort(invoice.getTotalDueDate()));
		formTester.select(Invoice.Properties.status.name(), 0);
		formTester.select(Invoice.Properties.systemUser.name(), 0);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		Invoice invoice = (Invoice) formTester.getForm().getModelObject();

		assertSuccessMessages(new String [] {invoice.getInvoiceNumber()+" saved."});

		tester.assertRenderedPage(InvoicePage.class);
		tester.isVisible(PATH+InvoicePage.PREVIEW_BUTTON);
		tester.isVisible(PATH+Invoice.Properties.invoiceNumber);
		tester.isVisible(InvoicePage.DETAILS);

		Assert.assertEquals(customer, invoice.getCustomer());
		Assert.assertEquals(currentSystemUser, invoice.getSystemUser());

		SortedSet<InvoiceTraveler> travelers = invoice.getInvoiceTravelers();
		Assert.assertEquals(1, travelers.size());
		InvoiceTraveler traveler = travelers.first();
		Assert.assertEquals(customer.getSalutation(), traveler.getSalutation());
		Assert.assertEquals(customer.getFirstName(), traveler.getFirstName());
		Assert.assertEquals(customer.getLastName(), traveler.getLastName());
		Assert.assertEquals(customer.getDateOfBirth(), traveler.getDateOfBirth());

		SortedSet<InvoiceNote> notes = invoice.getInvoiceNotes();
		Assert.assertEquals(2, notes.size());
		Assert.assertEquals(INVOICE_NOTE1, notes.first().getText());
		Assert.assertEquals(INVOICE_NOTE2, notes.last().getText());

		Mockito.verify(daoSupport).persist(invoice);
		Mockito.verify(parameterRepository).getDefaultInvoiceNotes();
	}

	@Test
	public void testNewInvoiceWithSubmitWithAdminSystemUser() {
		Mockito.stub(parameterRepository.getDefaultInvoiceNotes())
			.toReturn(Lists.newArrayList(INVOICE_NOTE1, INVOICE_NOTE2));

		usingAdminSystemUser();
		Customer customer = invoice.getCustomer();
		tester.startPage(makeInvoicePage(customer));
		tester.assertRenderedPage(InvoicePage.class);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.totalDueDate.name()+":date",	DateUtils.formatDateShort(invoice.getTotalDueDate()));
		formTester.select(Invoice.Properties.status.name(), 0);
		formTester.select(Invoice.Properties.systemUser.name(), 0);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		Invoice invoice = (Invoice) formTester.getForm().getModelObject();

		Assert.assertEquals(customer, invoice.getCustomer());
		Assert.assertEquals(dropDownSystemUser, invoice.getSystemUser());

		Mockito.verify(daoSupport).persist(invoice);
	}

	@Test
	public void testExistingInvoiceNotActive() {
		Invoice invoice = InvoiceHelper.makeInvoice();
		invoice.setStatus(InvoiceStatus.Closed);

		Mockito.when(daoSupport.find(Invoice.class, invoice.getId())).thenReturn(invoice);
		Mockito.when(daoSupport.find(Customer.class, invoice.getCustomer().getId())).thenReturn(invoice.getCustomer());

		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		tester.assertEnabled(PATH+Invoice.Properties.status);

		tester.assertDisabled(PATH+Invoice.Properties.date);
		tester.assertDisabled(PATH+Invoice.Properties.billCompany);
		tester.assertDisabled(PATH+Invoice.Properties.totalDueDate);
		tester.assertDisabled(PATH+Invoice.Properties.systemUser);
	}

	@Test
	public void testExistingInvoiceWithReset() {
		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		tester.executeAjaxEvent(RESET_PATH, "onclick");

		tester.assertRenderedPage(InvoicePage.class);

		Mockito.verify(daoSupport, Mockito.never()).persist(invoice);
	}

	@Test
	public void testVerifyRequiredFields() throws Exception {
		tester.startPage(new InvoicePage(invoice.getCustomer()));
		tester.assertRenderedPage(InvoicePage.class);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.status.name(), StringUtils.EMPTY);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
		   "'Agent' is required.",
		   "'Invoice Status' is required."
		});
	}

	@Test
	public void testVerifyValidFields() throws Exception {
		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		FormTester formTester = tester.newFormTester(InvoicePage.FORM);
		formTester.setValue(Invoice.Properties.totalDueDate.name()+":date", "date");

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
		   "The value of 'Total Due Date' is not a valid Date.",
		});
	}

	private InvoicePage makeInvoicePage(Invoice invoice) {
		return new InvoicePage(invoice);
	}

	private InvoicePage makeInvoicePage(Customer customer) {
		return new InvoicePage(customer);
	}

	@Test
	public void testInvoicePageUsingSystemUser() throws Exception {
		usingAgentSystemUser();

		tester.startPage(makeInvoicePage(invoice));
		tester.assertRenderedPage(InvoicePage.class);

		tester.assertDisabled(PATH+Invoice.Properties.status);
		tester.assertInvisible(PATH+Invoice.Properties.systemUser);
	}
}
