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

import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceStatus;

public class DestinationRowPanelTest extends BaseWicketTester {
	private Invoice invoice;
	private DestinationsPanel destinationsPanel;
	private InvoiceDestination invoiceDestination;

	@Before
	public void setUp() {
		stubDestinationDataProvider();

		destinationsPanel = new DestinationsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubDestinationDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoice.addInvoiceDestination(InvoiceHelper.makeDestination());
		invoice.addInvoiceDestination(InvoiceHelper.makeDestination());

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		invoiceDestination = invoice.getInvoiceDestinations().first();
		IModel<InvoiceDestination> model = DaoEntityModelFactory.make(invoiceDestination);
		Mockito.when(daoSupport.find(InvoiceDestination.class, invoiceDestination.getId())).thenReturn(invoiceDestination);

		DestinationRowPanel destinationRowPanel = new DestinationRowPanel(COMPONENT_ID, model, destinationsPanel);
		tester.startComponentInPage(destinationRowPanel);

		tester.assertModelValue(COMPONENT_PATH+InvoiceDestination.Properties.departurePlace.name(), invoiceDestination.getDeparturePlace());
		tester.assertModelValue(COMPONENT_PATH+InvoiceDestination.Properties.departureDate.name(), invoiceDestination.getDepartureDate());
		tester.assertModelValue(COMPONENT_PATH+InvoiceDestination.Properties.arrivalPlace.name(), invoiceDestination.getArrivalPlace());
		tester.assertModelValue(COMPONENT_PATH+InvoiceDestination.Properties.arrivalDate.name(), invoiceDestination.getArrivalDate());

		tester.assertComponent(COMPONENT_PATH+DestinationRowPanel.EDIT_BUTTON, EditPanel.class);
		tester.assertComponent(COMPONENT_PATH+DestinationRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceDestination.class, invoiceDestination.getId());
	}

	@Test
	public void testWithNotActiveInvoice() {
		invoice.setStatus(InvoiceStatus.Closed);

		invoiceDestination = invoice.getInvoiceDestinations().first();
		IModel<InvoiceDestination> model = DaoEntityModelFactory.make(invoiceDestination);
		Mockito.when(daoSupport.find(InvoiceDestination.class, invoiceDestination.getId())).thenReturn(invoiceDestination);

		DestinationRowPanel destinationRowPanel = new DestinationRowPanel(COMPONENT_ID, model, destinationsPanel);
		tester.startComponentInPage(destinationRowPanel);

		tester.assertInvisible(COMPONENT_PATH+DestinationRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+DestinationRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceDestination.class, invoiceDestination.getId());
	}
}
