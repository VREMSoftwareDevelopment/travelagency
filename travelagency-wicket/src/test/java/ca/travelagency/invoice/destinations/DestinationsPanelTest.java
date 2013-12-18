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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceHelper;

public class DestinationsPanelTest extends BaseWicketTester {
	@Mock private AjaxRequestTarget target;

	private Invoice invoice;
	private DestinationsPanel fixture;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceDestination.class, invoice.getInvoiceDestinations().first().getId())).toReturn(invoice.getInvoiceDestinations().first());

		fixture = new DestinationsPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	@Test
	public void testUpdate() {
		tester.startComponentInPage(fixture);

		InvoiceDestination invoiceDestination = InvoiceHelper.makeDestination();

		fixture.update(target, invoiceDestination);

		Assert.assertEquals(invoiceDestination, invoice.getInvoiceDestinations().last());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	@Test
	public void testDelete() {
		tester.startComponentInPage(fixture);

		fixture.delete(target, invoice.getInvoiceDestinations().first());

		Assert.assertTrue(invoice.getInvoiceDestinations().isEmpty());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	private static final String PATH = COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER+BasePage.PATH_SEPARATOR;

	@Test
	public void testDetailsPanelComponents() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+DetailsPanel.ROWS_CONTAINER, WebMarkupContainer.class);
		tester.assertComponent(PATH+DetailsPanel.HEADER, DestinationsHeaderPanel.class);
		tester.assertComponent(PATH+DetailsPanel.FORM, DestinationFormPanel.class);
		tester.assertComponent(PATH+DetailsPanel.ROWS, ListView.class);
	}

}
