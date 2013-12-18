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
package ca.travelagency.invoice.destinations;


import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

public class DestinationFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+DestinationFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private static final String DEPARTURE_PLACE = "Departure Place";

	private Invoice invoice;
	private InvoiceDestination invoiceDestination;
	private DestinationsPanel destinationsPanel;

	@Before
	public void setUp() {
		stubDestinationDataProvider();

		destinationsPanel = new DestinationsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubDestinationDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceDestination = invoice.getInvoiceDestinations().first();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceDestination.class, invoiceDestination.getId())).toReturn(invoiceDestination);
	}

	@Test
	public void testComponents() {
		DestinationFormPanel destinationFormPanel = new DestinationFormPanel(COMPONENT_ID, invoiceDestination, destinationsPanel);
		tester.startComponentInPage(destinationFormPanel);

		tester.assertComponent(COMPONENT_PATH+DestinationFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+DestinationFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+DestinationFormPanel.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+DestinationFormPanel.CANCEL_BUTTON, CancelPanel.class);

		tester.assertComponent(FORM_PATH+InvoiceDestination.Properties.departurePlace, CityField.class);
		tester.assertComponent(FORM_PATH+InvoiceDestination.Properties.departureDate, DateField.class);
		tester.assertComponent(FORM_PATH+InvoiceDestination.Properties.arrivalPlace, CityField.class);
		tester.assertComponent(FORM_PATH+InvoiceDestination.Properties.arrivalDate, DateField.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceDestination.class, invoiceDestination.getId());
	}

	@Test
	public void testNewDestinationComponentsFirstRow() {
		Mockito.when(parameterRepository.getDefaultDeparturePlace()).thenReturn(DEPARTURE_PLACE);

		invoice.removeInvoiceDestination(invoiceDestination);
		DestinationFormPanel destinationFormPanel = new DestinationFormPanel(COMPONENT_ID, destinationsPanel);
		tester.startComponentInPage(destinationFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.departurePlace, DEPARTURE_PLACE);
		tester.assertInvisible(FORM_PATH+DestinationFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoiceDestination.class, invoiceDestination.getId());
		Mockito.verify(parameterRepository).getDefaultDeparturePlace();
	}

	@Test
	public void testNewDestinationComponentsSecondRow() {
		DestinationFormPanel destinationFormPanel = new DestinationFormPanel(COMPONENT_ID, destinationsPanel);
		tester.startComponentInPage(destinationFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.departurePlace, invoiceDestination.getArrivalPlace());
		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.departureDate,
			DateUtils.addDays(invoiceDestination.getDepartureDate(), DestinationFormPanel.TRAVEL_DAY_RANGE));
		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.arrivalPlace, invoiceDestination.getDeparturePlace());

		tester.assertInvisible(FORM_PATH+DestinationFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoiceDestination.class, invoiceDestination.getId());
	}

	@Test
	public void testValues() {
		DestinationFormPanel destinationFormPanel = new DestinationFormPanel(COMPONENT_ID, invoiceDestination, destinationsPanel);
		tester.startComponentInPage(destinationFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.departurePlace, invoiceDestination.getDeparturePlace());
		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.departureDate, invoiceDestination.getDepartureDate());
		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.arrivalPlace, invoiceDestination.getArrivalPlace());
		tester.assertModelValue(FORM_PATH+InvoiceDestination.Properties.arrivalDate, invoiceDestination.getArrivalDate());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceDestination.class, invoiceDestination.getId());
	}

	@Test
	public void testRequiredFields() {
		DestinationFormPanel panel = new DestinationFormPanel(COMPONENT_ID, destinationsPanel);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+DestinationFormPanel.FORM);
		formTester.setValue(InvoiceDestination.Properties.departurePlace.name(), StringUtils.EMPTY);
		formTester.setValue(InvoiceDestination.Properties.departureDate.name(), StringUtils.EMPTY);
		formTester.setValue(InvoiceDestination.Properties.arrivalPlace.name(), StringUtils.EMPTY);
		formTester.setValue(InvoiceDestination.Properties.arrivalDate.name(), StringUtils.EMPTY);
		formTester.submit();

		tester.assertErrorMessages(new String [] {
			"'Departure Place' is required.",
			"'Arrival Place' is required."
		});
	}

}

