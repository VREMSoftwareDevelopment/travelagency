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
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceTraveler;

public class TravelersPanelTest extends BaseWicketTester {
	@Mock private AjaxRequestTarget target;

	private Invoice invoice;
	private TravelersPanel fixture;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceTraveler.class, invoice.getInvoiceTravelers().first().getId())).toReturn(invoice.getInvoiceTravelers().first());

		fixture = new TravelersPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	@Test
	public void testUpdate() {
		tester.startComponentInPage(fixture);

		InvoiceTraveler invoiceTraveler = InvoiceHelper.makeTraveler();

		fixture.update(target, invoiceTraveler);

		Assert.assertEquals(invoiceTraveler, invoice.getInvoiceTravelers().last());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	@Test
	public void testDelete() {
		tester.startComponentInPage(fixture);

		fixture.delete(target, invoice.getInvoiceTravelers().first());

		Assert.assertTrue(invoice.getInvoiceTravelers().isEmpty());
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).persist(invoice);
	}

	private static final String PATH = COMPONENT_PATH+TravelersPanel.ROWS_CONTAINER+BasePage.PATH_SEPARATOR;

	@Test
	public void testComponents() {
		tester.startComponentInPage(fixture);

		tester.assertComponent(COMPONENT_PATH+TravelersPanel.ROWS_CONTAINER, WebMarkupContainer.class);
		tester.assertComponent(PATH+TravelersPanel.HEADER, TravelersHeaderPanel.class);
		tester.assertComponent(PATH+TravelersPanel.FORM, TravelerFormPanel.class);
		tester.assertComponent(PATH+TravelersPanel.ROWS, ListView.class);
	}

}
