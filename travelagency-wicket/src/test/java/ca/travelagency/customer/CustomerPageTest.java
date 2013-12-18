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

import java.util.Date;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.CountryField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.ProvinceField;
import ca.travelagency.components.fields.SalutationField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.components.fields.TravelDocumentField;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicesPanel;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class CustomerPageTest extends BaseWicketTester {
	private static final String PATH = CustomerPage.FORM+CustomerPage.PATH_SEPARATOR;
	private static final String RESET_PATH = PATH+CustomerPage.RESET_BUTTON+BasePage.PATH_SEPARATOR+ResetPanel.RESET_BUTTON;
	private static final String SAVE_PATH = PATH+CustomerPage.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;

	private static final String DOCUMENT_TYPE = "Document Type";

	@Test
	public void testNewCustomer() {
		// setup
		Mockito.stub(parameterRepository.getDefaultTravelDocumentType()).toReturn(DOCUMENT_TYPE);
		// execute
		tester.startPage(CustomerPage.class);
		// validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.assertComponent(CustomerPage.FORM, Form.class);

		tester.assertComponent(PATH+CustomerPage.FEEDBACK, ComponentFeedbackPanel.class);

		tester.assertComponent(PATH+CustomerPage.SAVE_BUTTON, SavePanel.class);
		tester.assertComponent(PATH+CustomerPage.RESET_BUTTON, ResetPanel.class);

		tester.assertComponent(PATH+Customer.Properties.salutation, SalutationField.class);
		tester.assertComponent(PATH+Customer.Properties.status, DropDownChoice.class);
		tester.assertComponent(PATH+Customer.Properties.lastName, LastNameField.class);
		tester.assertComponent(PATH+Customer.Properties.firstName, FirstNameField.class);
		tester.assertComponent(PATH+Customer.Properties.companyName, CompanyNameField.class);
		tester.assertComponent(PATH+Customer.Properties.address, TextField.class);
		tester.assertComponent(PATH+Customer.Properties.city, CityField.class);
		tester.assertComponent(PATH+Customer.Properties.province, ProvinceField.class);
		tester.assertComponent(PATH+Customer.Properties.postalCode, TextField.class);
		tester.assertComponent(PATH+Customer.Properties.country, CountryField.class);
		tester.assertComponent(PATH+Customer.Properties.email, TextField.class);
		tester.assertComponent(PATH+Customer.Properties.primaryPhone, RequiredTextField.class);
		tester.assertComponent(PATH+Customer.Properties.secondaryPhone, TextField.class);
		tester.assertComponent(PATH+Customer.Properties.notes, TextArea.class);
		tester.assertComponent(PATH+Customer.Properties.systemUser, SystemUserField.class);
		tester.assertComponent(PATH+Customer.Properties.dateOfBirth, DateField.class);
		tester.assertComponent(PATH+Customer.Properties.travelDocumentNumber, TextField.class);
		tester.assertComponent(PATH+Customer.Properties.travelDocumentType, TravelDocumentField.class);

		tester.assertInvisible(PATH+CustomerPage.CREATE_INVOICE_BUTTON);
		tester.assertInvisible(CustomerPage.INVOICES);

		tester.assertEnabled(PATH+Customer.Properties.status);

		tester.assertModelValue(PATH+Customer.Properties.travelDocumentType, DOCUMENT_TYPE);

		Mockito.verify(parameterRepository, Mockito.atLeastOnce()).getDefaultTravelDocumentType();
	}

	@Test
	public void testExistingCustomerValues() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		customer.setSystemUser(systemUser);
		stubSystemUserField(Lists.newArrayList(systemUser));

		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);

		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.assertComponent(PATH+CustomerPage.CREATE_INVOICE_BUTTON, Link.class);
		tester.assertComponent(CustomerPage.INVOICES, InvoicesPanel.class);

		tester.assertModelValue(PATH+Customer.Properties.salutation, customer.getSalutation());
		tester.assertModelValue(PATH+Customer.Properties.status, customer.getStatus());
		tester.assertModelValue(PATH+Customer.Properties.lastName, customer.getLastName());
		tester.assertModelValue(PATH+Customer.Properties.firstName, customer.getFirstName());
		tester.assertModelValue(PATH+Customer.Properties.companyName, customer.getCompanyName());
		tester.assertModelValue(PATH+Customer.Properties.address, customer.getAddress());
		tester.assertModelValue(PATH+Customer.Properties.city, customer.getCity());
		tester.assertModelValue(PATH+Customer.Properties.province, customer.getProvince());
		tester.assertModelValue(PATH+Customer.Properties.postalCode, customer.getPostalCode());
		tester.assertModelValue(PATH+Customer.Properties.country, customer.getCountry());
		tester.assertModelValue(PATH+Customer.Properties.email, customer.getEmail());
		tester.assertModelValue(PATH+Customer.Properties.primaryPhone, customer.getPrimaryPhone());
		tester.assertModelValue(PATH+Customer.Properties.secondaryPhone, customer.getSecondaryPhone());
		tester.assertModelValue(PATH+Customer.Properties.notes, customer.getNotes());
		tester.assertModelValue(PATH+Customer.Properties.systemUser, systemUser);
		tester.assertModelValue(PATH+Customer.Properties.dateOfBirth, customer.getDateOfBirth());
		tester.assertModelValue(PATH+Customer.Properties.travelDocumentNumber, customer.getTravelDocumentNumber());
		tester.assertModelValue(PATH+Customer.Properties.travelDocumentType, customer.getTravelDocumentType());
	}

	@Test
	public void testNewCustomerSave() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		stubSystemUserField(Lists.newArrayList(systemUser));

		// execute
		tester.startPage(new CustomerPage());
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		FormTester formTester = tester.newFormTester(CustomerPage.FORM);
		formTester.setValue(""+Customer.Properties.lastName, customer.getLastName());
		formTester.setValue(""+Customer.Properties.firstName, customer.getFirstName());
		formTester.setValue(""+Customer.Properties.companyName, customer.getCompanyName());
		formTester.setValue(""+Customer.Properties.address, customer.getAddress());
		formTester.setValue(""+Customer.Properties.city, customer.getCity());
		formTester.setValue(""+Customer.Properties.province, customer.getProvince());
		formTester.setValue(""+Customer.Properties.postalCode, customer.getPostalCode());
		formTester.setValue(""+Customer.Properties.country, customer.getCountry());
		formTester.setValue(""+Customer.Properties.email, customer.getEmail());
		formTester.setValue(""+Customer.Properties.primaryPhone, customer.getPrimaryPhone());
		formTester.setValue(""+Customer.Properties.secondaryPhone, customer.getSecondaryPhone());
		formTester.setValue(""+Customer.Properties.notes, customer.getNotes());
		formTester.setValue(""+Customer.Properties.salutation, customer.getSalutation());
		formTester.select(""+Customer.Properties.systemUser, 0);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {customer.getName()+" saved."});

		tester.assertRenderedPage(CustomerPage.class);
	}

	@Test
	public void testExistingCustomerSave() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		customer.setSystemUser(systemUser);
		stubSystemUserField(Lists.newArrayList(systemUser));

		Mockito.when(customerService.duplicated(customer)).thenReturn(false);
		Mockito.when(daoSupport.find(Customer.class, customer.getId())).thenReturn(customer);

		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		FormTester formTester = tester.newFormTester(CustomerPage.FORM);
		formTester.setValue(""+Customer.Properties.lastName, customer.getLastName());
		formTester.setValue(""+Customer.Properties.firstName, customer.getFirstName());
		formTester.setValue(""+Customer.Properties.companyName, customer.getCompanyName());
		formTester.setValue(""+Customer.Properties.address, customer.getAddress());
		formTester.setValue(""+Customer.Properties.city, customer.getCity());
		formTester.setValue(""+Customer.Properties.province, customer.getProvince());
		formTester.setValue(""+Customer.Properties.postalCode, customer.getPostalCode());
		formTester.setValue(""+Customer.Properties.country, customer.getCountry());
		formTester.setValue(""+Customer.Properties.email, customer.getEmail());
		formTester.setValue(""+Customer.Properties.primaryPhone, customer.getPrimaryPhone());
		formTester.setValue(""+Customer.Properties.secondaryPhone, customer.getSecondaryPhone());
		formTester.setValue(""+Customer.Properties.notes, customer.getNotes());
		formTester.setValue(""+Customer.Properties.salutation, customer.getSalutation());
		formTester.select(""+Customer.Properties.systemUser, 0);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {customer.getName()+" saved."});

		tester.assertRenderedPage(CustomerPage.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(Customer.class, customer.getId());
		Mockito.verify(daoSupport).persist(customer);
		Mockito.verify(customerService).duplicated(customer);
	}

	@Test
	public void testExistingCustomerNotActive() {
		// setup
		Customer customer = CustomerHelper.makeCustomer();
		customer.setStatus(CustomerStatus.OnHold);

		Mockito.when(daoSupport.find(Customer.class, customer.getId())).thenReturn(customer);

		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.assertInvisible(PATH+CustomerPage.CREATE_INVOICE_BUTTON);
		tester.assertVisible(CustomerPage.INVOICES);
	}

	@Test
	public void testCreateInvoiceRedirectsToInvoicePage() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		customer.setSystemUser(systemUser);
		stubSystemUserField(Lists.newArrayList(systemUser));

		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);

		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.assertComponent(PATH+CustomerPage.CREATE_INVOICE_BUTTON, Link.class);

		tester.clickLink(PATH+CustomerPage.CREATE_INVOICE_BUTTON);

		tester.assertRenderedPage(InvoicePage.class);
	}

	@Test
	public void testVerifyRequiredFields() throws Exception {
		// execute
		tester.startPage(CustomerPage.class);
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		FormTester formTester = tester.newFormTester(CustomerPage.FORM);
		formTester.setValue(Customer.Properties.status.name(), StringUtils.EMPTY);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"'Status' is required.",
			"'First Name' is required.",
			"'Last Name' is required.",
			"'Primary Phone' is required."
		});

		Mockito.verify(customerService, Mockito.never()).duplicated(Mockito.any(Customer.class));
	}

	@Test
	public void testVerifyValidFields() throws Exception {
		// execute
		tester.startPage(CustomerPage.class);
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		FormTester formTester = tester.newFormTester(CustomerPage.FORM);
		formTester.setValue(Customer.Properties.firstName.name(), "first name");
		formTester.setValue(Customer.Properties.lastName.name(), "last name");
		formTester.setValue(Customer.Properties.email.name(), "EMAIL");
		formTester.setValue(Customer.Properties.primaryPhone.name(), "123-1234");
		formTester.setValue(Customer.Properties.secondaryPhone.name(), "123-1234");

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"The value of 'Email' is not a valid email address.",
			"'Primary Phone' is not a valid phone number.",
			"'Secondary Phone' is not a valid phone number."
		});

		Mockito.verify(customerService, Mockito.never()).duplicated(Mockito.any(Customer.class));
	}

	@Test
	public void testVerifyDuplicatedCustomer() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		customer.setDateOfBirth(new Date());
		customer.setSystemUser(systemUser);
		stubSystemUserField(Lists.newArrayList(systemUser));

		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);
		Mockito.when(customerService.duplicated(Mockito.any(Customer.class))).thenReturn(true);
		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		FormTester formTester = tester.newFormTester(CustomerPage.FORM);
		formTester.setValue(Customer.Properties.firstName.name(), customer.getFirstName());
		formTester.setValue(Customer.Properties.lastName.name(), customer.getLastName());
		formTester.setValue(Customer.Properties.primaryPhone.name(), customer.getPrimaryPhone());
		formTester.setValue(Customer.Properties.dateOfBirth.name(), DateUtils.formatDateShort(customer.getDateOfBirth()));

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"'"
			+customer.getFirstName()
			+" "
			+customer.getLastName()
			+"' with phone number '"
			+customer.getPrimaryPhone()
			+"' and date of birth '"
			+DateUtils.formatDate(customer.getDateOfBirth())
			+"' already exists."
		});

		Mockito.verify(customerService).duplicated(Mockito.any(Customer.class));
	}

	@Test
	public void testExistingCustomerWithReset() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Customer customer = CustomerHelper.makeCustomer();
		customer.setSystemUser(systemUser);
		stubSystemUserField(Lists.newArrayList(systemUser));

		Mockito.when(daoSupport.find(Customer.class, customer.getId())).thenReturn(customer);

		// execute
		tester.startPage(new CustomerPage(customer));
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.executeAjaxEvent(RESET_PATH, "onclick");

		tester.assertRenderedPage(CustomerPage.class);

		Mockito.verify(daoSupport, Mockito.never()).persist(customer);
	}

	@Test
	public void testNewCustomerUsingAgentSystemUser() {
		// setup
		usingAgentSystemUser();

		// execute
		tester.startPage(CustomerPage.class);
		//validate
		tester.assertRenderedPage(CustomerPage.class);

		tester.assertDisabled(PATH+Customer.Properties.status);
		tester.assertInvisible(PATH+Customer.Properties.systemUser);
	}

	@Test
	public void testInvoicesPanelUsingAdminSystemUser() {
		// setup
		usingAdminSystemUser();
		Customer expectedCustomer = CustomerHelper.makeCustomer();
		Mockito.stub(daoSupport.find(Customer.class, expectedCustomer.getId())).toReturn(expectedCustomer);
		// execute
		tester.startPage(new CustomerPage(expectedCustomer));
		InvoicesPanel invoicesPanel = (InvoicesPanel) tester.getComponentFromLastRenderedPage(CustomerPage.INVOICES);
		InvoiceFilter invoiceFilter = (InvoiceFilter) invoicesPanel.getDefaultModelObject();
		// verify
		Assert.assertEquals(expectedCustomer, invoiceFilter.getCustomer());
		Assert.assertNull(invoiceFilter.getSystemUser());
	}

	@Test
	public void testInvoicesPanelUsingAgentSystemUser() {
		// setup
		SystemUser expectedSystemUser = usingAgentSystemUser();
		Mockito.stub(daoSupport.find(SystemUser.class, expectedSystemUser.getId())).toReturn(expectedSystemUser);
		Customer expectedCustomer = CustomerHelper.makeCustomer();
		Mockito.stub(daoSupport.find(Customer.class, expectedCustomer.getId())).toReturn(expectedCustomer);
		// execute
		tester.startPage(new CustomerPage(expectedCustomer));
		InvoicesPanel invoicesPanel = (InvoicesPanel) tester.getComponentFromLastRenderedPage(CustomerPage.INVOICES);
		InvoiceFilter invoiceFilter = (InvoiceFilter) invoicesPanel.getDefaultModelObject();
		// verify
		Assert.assertEquals(expectedCustomer, invoiceFilter.getCustomer());
		Assert.assertEquals(expectedSystemUser, invoiceFilter.getSystemUser());
	}

}
