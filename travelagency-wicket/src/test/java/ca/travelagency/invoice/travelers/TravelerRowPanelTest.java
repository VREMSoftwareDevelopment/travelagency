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

import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.invoice.InvoiceTraveler;

public class TravelerRowPanelTest extends BaseWicketTester {
	private Invoice invoice;
	private TravelersPanel travelersPanel;
	private InvoiceTraveler invoiceTraveler;

	@Before
	public void setUp() {
		stubTravelerDataProvider();

		travelersPanel = new TravelersPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubTravelerDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		invoiceTraveler = invoice.getInvoiceTravelers().first();
		IModel<InvoiceTraveler> model = DaoEntityModelFactory.make(invoiceTraveler);
		Mockito.when(daoSupport.find(InvoiceTraveler.class, invoiceTraveler.getId())).thenReturn(invoiceTraveler);

		TravelerRowPanel travelerRowPanel = new TravelerRowPanel(COMPONENT_ID, model, travelersPanel);
		tester.startComponentInPage(travelerRowPanel);

		tester.assertModelValue(COMPONENT_PATH+InvoiceTraveler.PROPERTY_NAME, invoiceTraveler.getName());
		tester.assertModelValue(COMPONENT_PATH+InvoiceTraveler.Properties.documentType.name(), invoiceTraveler.getDocumentType());
		tester.assertModelValue(COMPONENT_PATH+InvoiceTraveler.Properties.documentNumber.name(), invoiceTraveler.getDocumentNumber());
		tester.assertModelValue(COMPONENT_PATH+InvoiceTraveler.Properties.dateOfBirth.name(), invoiceTraveler.getDateOfBirth());

		tester.assertComponent(COMPONENT_PATH+TravelerRowPanel.EDIT_BUTTON, EditPanel.class);
		tester.assertComponent(COMPONENT_PATH+TravelerRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceTraveler.class, invoiceTraveler.getId());
	}

	@Test
	public void testWithNotActiveInvoice() {
		invoice.setStatus(InvoiceStatus.Closed);

		invoiceTraveler = invoice.getInvoiceTravelers().first();
		IModel<InvoiceTraveler> model = DaoEntityModelFactory.make(invoiceTraveler);
		Mockito.when(daoSupport.find(InvoiceTraveler.class, invoiceTraveler.getId())).thenReturn(invoiceTraveler);

		TravelerRowPanel travelerRowPanel = new TravelerRowPanel(COMPONENT_ID, model, travelersPanel);
		tester.startComponentInPage(travelerRowPanel);

		tester.assertInvisible(COMPONENT_PATH+TravelerRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+TravelerRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceTraveler.class, invoiceTraveler.getId());
	}
}
