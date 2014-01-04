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
package ca.travelagency.invoice.travelers;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.BirthDateField;
import ca.travelagency.components.fields.SalutationField;
import ca.travelagency.components.fields.TravelDocumentField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.customer.FirstNameField;
import ca.travelagency.customer.LastNameField;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.traveler.TravelerLookupLink;

public class TravelerFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+TravelerFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private static final String DOCUMENT_TYPE = "Document Type";

	private Invoice invoice;
	private InvoiceTraveler invoiceTraveler;
	private TravelersPanel travelersPanel;

	@Before
	public void setUp() {
		stubTravelerDataProvider();

		travelersPanel = new TravelersPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubTravelerDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceTraveler = invoice.getInvoiceTravelers().first();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceTraveler.class, invoiceTraveler.getId())).toReturn(invoiceTraveler);
		Mockito.stub(parameterRepository.getDefaultTravelDocumentType()).toReturn(DOCUMENT_TYPE);
	}

	@Test
	public void testComponents() {
		TravelerFormPanel travelerFormPanel = new TravelerFormPanel(COMPONENT_ID, invoiceTraveler, travelersPanel);
		tester.startComponentInPage(travelerFormPanel);

		tester.assertComponent(COMPONENT_PATH+TravelerFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+TravelerFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+TravelerFormPanel.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+TravelerFormPanel.CANCEL_BUTTON, CancelPanel.class);
		tester.assertComponent(FORM_PATH+TravelerFormPanel.LOOKUP_TRAVELERS, TravelerLookupLink.class);

		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.salutation, SalutationField.class);
		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.firstName, FirstNameField.class);
		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.lastName, LastNameField.class);
		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.documentType, TravelDocumentField.class);
		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.documentNumber, TextField.class);
		tester.assertComponent(FORM_PATH+InvoiceTraveler.Properties.dateOfBirth, BirthDateField.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceTraveler.class, invoiceTraveler.getId());
	}

	@Test
	public void testNewTravelerComponents() {
		TravelerFormPanel travelerFormPanel = new TravelerFormPanel(COMPONENT_ID, travelersPanel);
		tester.startComponentInPage(travelerFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.documentType, DOCUMENT_TYPE);
		tester.assertInvisible(FORM_PATH+TravelerFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoiceTraveler.class, invoiceTraveler.getId());
		Mockito.verify(parameterRepository, Mockito.atLeastOnce()).getDefaultTravelDocumentType();
	}

	@Test
	public void testValues() {
		TravelerFormPanel travelerFormPanel = new TravelerFormPanel(COMPONENT_ID, invoiceTraveler, travelersPanel);
		tester.startComponentInPage(travelerFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.salutation, invoiceTraveler.getSalutation());
		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.firstName, invoiceTraveler.getFirstName());
		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.lastName, invoiceTraveler.getLastName());
		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.documentType, invoiceTraveler.getDocumentType());
		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.documentNumber, invoiceTraveler.getDocumentNumber());
		tester.assertModelValue(FORM_PATH+InvoiceTraveler.Properties.dateOfBirth, invoiceTraveler.getDateOfBirth());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceTraveler.class, invoiceTraveler.getId());
	}

	@Test
	public void testRequiredFields() {
		TravelerFormPanel panel = new TravelerFormPanel(COMPONENT_ID, travelersPanel);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+TravelerFormPanel.FORM);
		formTester.submit();

		tester.assertErrorMessages(new String [] {
			"'Last Name' is required.",
			"'First Name' is required."
		});
	}
}

